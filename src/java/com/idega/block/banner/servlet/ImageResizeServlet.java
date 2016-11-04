package com.idega.block.banner.servlet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.banner.bean.ImageResizeAction;
import com.idega.block.banner.bean.ImageResizeRequest;
import com.idega.block.banner.business.BannerImageResizeService;
import com.idega.core.cache.IWCacheManager2;
import com.idega.core.file.data.bean.ICFile;
import com.idega.core.persistence.GenericDao;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.expression.ELUtil;

public class ImageResizeServlet extends HttpServlet {

	private static final long serialVersionUID = 8854483436258326590L;

	private static final String CACHE_NAME_RESIZED_IMAGE = "ImageResizeServlet.resized";

	private static int maxAge = 604800;

	private static Logger LOGGER = Logger.getLogger(ImageResizeServlet.class
			.getName());

	@Autowired
	private BannerImageResizeService bannerImageResizeService;

	private BannerImageResizeService getBannerImageResizeService() {
		if (bannerImageResizeService == null) {
			ELUtil.getInstance().autowire(this);
		}
		return bannerImageResizeService;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		String configMaxAge = config.getInitParameter("maxAge");

		if (configMaxAge != null)
			maxAge = Integer.parseInt(configMaxAge);

		LOGGER.info("[ImageResizeServlet] Using \"" + maxAge + "\" as the cache timeout value");
	}

	private InputStream getStreamFromDatabase(String media) throws Exception {
		String name = media.substring(media.lastIndexOf(CoreConstants.SLASH) + 1);
		String id = name.substring(0, name.indexOf(CoreConstants.UNDER));
		GenericDao dao = ELUtil.getInstance().getBean("genericDAO");
		ICFile file = dao.find(ICFile.class, Integer.valueOf(id));
		return file.getFileValue().getBinaryStream();
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		String media = request.getParameter("image_url");

		try {
			ByteArrayOutputStream byteStream = null;

			Map<String, byte[]> cache = getCache(CACHE_NAME_RESIZED_IMAGE,
					(60 * 60 * 24));
			if (cache != null && !cache.isEmpty()) {
				if (cache.containsKey(request.getQueryString())) {
					byte[] bytes = cache.get(request.getQueryString());
					byteStream = new ByteArrayOutputStream();
					byteStream.write(bytes);
				}
			}

			// Get values of parameters
			String imageType = request.getParameter("type");

			InputStream stream = null;
			if (byteStream == null) {
				String width = request.getParameter("width");
				if (width == null) {
					width = "100";
				}

				String height = request.getParameter("height");
				if (height == null) {
					height = "100";
				}

				String maintainAspect = request.getParameter("maintain_aspect");
				if (maintainAspect == null) {
					maintainAspect = Boolean.TRUE.toString();
				}

				String cropToAspect = request.getParameter("crop");
				if (cropToAspect == null) {
					cropToAspect = Boolean.TRUE.toString();
				}

				String quality = request.getParameter("quality");
				if (quality == null) {
					quality = "1";
				}

				// Fetch the image
				if (media.indexOf("http") == -1) {
					media = request.getScheme()
							+ "://"
							+ request.getServerName()
							+ (request.getServerPort() != 80 ? ":"
									+ request.getServerPort() : "") + media;
				}

				if (imageType == null && media.indexOf(".") != -1) {
					imageType = media.substring(media.lastIndexOf(".") + 1);
				} else {
					imageType = "jpeg";
				}

				try {
					URL url = new URL(media);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					try {
						stream = connection.getInputStream();
					} catch (FileNotFoundException fnfe) {
						try {
							stream = getStreamFromDatabase(media);
						} catch (Exception e) {
							throw new IOException(e);
						}
						if (stream == null) {
							getLogger().warning("Error getting " + media);
							throw fnfe;
						}
					}
				} catch (Exception e) {
					try {
						stream = getStreamFromDatabase(media);
						if (stream == null) {
							getLogger().log(Level.WARNING, "Failed getting " + media + " from iw_cache", e);
						}
					} catch (Exception e1) {
						throw new IOException(e1);
					}
				}

				BufferedImage image = ImageIO.read(stream);

				BannerImageResizeService handler = getBannerImageResizeService();
				ImageResizeRequest imageRequest = new ImageResizeRequest();

				// Set resize/crop attributes
				imageRequest.setSourceImage(image);
				imageRequest.setTargetHeight(Integer.parseInt(height));
				imageRequest.setTargetWidth(Integer.parseInt(width));
				imageRequest.setMaintainAspect(Boolean
						.parseBoolean(maintainAspect));
				imageRequest
				.setCropToAspect(Boolean.parseBoolean(cropToAspect));
				imageRequest.setCompressionQuality(Float.parseFloat(quality));
				imageRequest.setResizeAction(ImageResizeAction.IF_LARGER);

				BufferedImage thumbnail = handler.resize(imageRequest);

				// Write the image into ByteArrayOutputStream
				byteStream = new ByteArrayOutputStream();
				ImageIO.write(thumbnail, imageType, byteStream);

				if (cache != null) {
					cache.put(request.getQueryString(),
							byteStream.toByteArray());
				}
			}

			// Read the image into a BufferedImage object
			try {
				IWTimestamp stamp = new IWTimestamp();
				stamp.addDays(7);

				// Update response
				ServletOutputStream out = response.getOutputStream();
				response.setContentType("image/" + imageType);
				response.setContentLength(byteStream.size());
				response.setHeader("Cache-Control", "PUBLIC, max-age=" + maxAge
						+ ", must-revalidate");
				response.setHeader("Expires",
						htmlExpiresDateFormat().format(stamp.getDate()));

				String token = '"' + getMd5Digest(byteStream.toByteArray()) + '"';
				response.setHeader("ETag", token);

				out.write(byteStream.toByteArray());

				out.flush();
				out.close();

				if (stream != null) {
					stream.close();
				}
			} catch (FileNotFoundException fnfe) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image " + media + " not found...");
			}
		} catch (IOException ie) {
			getLogger().log(Level.WARNING, "Error resizing " + media, ie);
		}
	}

	private Logger getLogger() {
		return Logger.getLogger(ImageResizeServlet.class.getName());
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "A simple servlet to resize images";
	}

	public static DateFormat htmlExpiresDateFormat() {
		DateFormat httpDateFormat = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return httpDateFormat;
	}

	private String getMd5Digest(byte[] bytes) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 algorithm not available", e);
		}
		byte[] messageDigest = md.digest(bytes);
		BigInteger number = new BigInteger(1, messageDigest);
		StringBuffer sb = new StringBuffer('0');
		sb.append(number.toString(16));
		return sb.toString();
	}

	private <K extends Serializable, V> Map<K, V> getCache(String cacheName,
			long timeToLive) {
		try {
			return IWCacheManager2.getInstance(getApplication()).getCache(
					cacheName, IWCacheManager2.DEFAULT_CACHE_SIZE,
					IWCacheManager2.DEFAULT_OVERFLOW_TO_DISK,
					IWCacheManager2.DEFAULT_ETERNAL,
					IWCacheManager2.DEFAULT_CACHE_TTL_IDLE_SECONDS, timeToLive,
					true);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error getting cache!", e);
		}
		return null;
	}

	private IWMainApplication getApplication() {
		return IWMainApplication.getDefaultIWMainApplication();
	}

	public static String thumbsUrl(String imageUrl, Integer width,
			Integer height) {
		return "/thumbsCreator/?image_url=" + imageUrl + "&width=" + width
				+ "&height=" + height;
	}
}