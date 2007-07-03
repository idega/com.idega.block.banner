package com.idega.block.banner.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.banner.business.BannerBusiness;
import com.idega.block.banner.business.BannerFinder;
import com.idega.block.banner.data.AdEntity;
import com.idega.block.banner.data.BannerEntity;
import com.idega.block.web2.business.Web2Business;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Script;
import com.idega.presentation.text.Heading3;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;

public class BannerCarousel extends Block implements Builderaware {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.banner";

	private int _bannerID = -1;

	private boolean _isAdmin = false;

	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;

	private boolean _newObjInst = false;

	private String iStyleClass = "myGallery";
	private String iObjectID = "myGallery";
	private String iTarget = Link.TARGET_BLANK_WINDOW;
	private int iDelay = 5000;

	public static String CACHE_KEY = "banner_carousel_cache";

	public BannerCarousel() {
		super.setCacheable(getCacheKey(), 999999999);
	}

	public String getCacheKey() {
		return CACHE_KEY;
	}

	protected String getCacheState(IWContext iwc, String cacheStatePrefix) {
		if (getParentPage() != null) {
			try {
				getParentPage().addJavascriptURL(getWeb2Business(iwc).getBundleURIToMootoolsLib());
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			getParentPage().addJavascriptURL(getBundle(iwc).getResourcesURL() + "/js/jd.gallery.js");
			getParentPage().addStyleSheetURL(getBundle(iwc).getResourcesURL() + "/style/banner.css");
		}

		return cacheStatePrefix + this._bannerID + iwc.hasEditPermission(this);
	}

	public void main(IWContext iwc) throws Exception {
		this._iwrb = getResourceBundle(iwc);
		this._iwb = getBundle(iwc);
		this._isAdmin = iwc.hasEditPermission(this);

		BannerEntity banner = null;

		if (this._bannerID <= 0) {
			String sBannerID = iwc.getParameter(BannerBusiness.PARAMETER_BANNER_ID);

			if (sBannerID != null) {
				this._bannerID = Integer.parseInt(sBannerID);
			}
			else if (getICObjectInstanceID() > 0) {
				this._bannerID = BannerFinder.getRelatedEntityId(getICObjectInstance());
				if (this._bannerID <= 0) {
					BannerBusiness.saveBanner(this._bannerID, getICObjectInstanceID(), null);
					this._newObjInst = true;
				}
			}
		}

		if (this._newObjInst) {
			this._bannerID = BannerFinder.getRelatedEntityId(((com.idega.core.component.data.ICObjectInstanceHome) com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(getICObjectInstanceID()));
		}

		if (this._bannerID > 0) {
			banner = BannerFinder.getBanner(this._bannerID);
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("function startGallery() {").append("\n");
		buffer.append("\t").append("var myGallery = new gallery($('").append(iObjectID).append("'), {").append("\n");
		buffer.append("\t").append("\t").append("timed: true,").append("\n");
		buffer.append("\t").append("\t").append("showArrows: false,").append("\n");
		buffer.append("\t").append("\t").append("showCarousel: false,").append("\n");
		buffer.append("\t").append("\t").append("embedLinks: true,").append("\n");
		buffer.append("\t").append("\t").append("showInfopane: false,").append("\n");
		buffer.append("\t").append("\t").append("delay: ").append(this.iDelay).append("\n");
		buffer.append("\t").append("});").append("\n");
		buffer.append("}");

		Script script = new Script();
		script.addFunction("startGallery", buffer.toString());
		script.addFunction("initialize", "window.onDomReady(startGallery);");
		add(script);

		if (this._isAdmin) {
			add(getAdminPart(iwc));
		}

		if (banner != null) {
			add(getCarousel(iwc, banner));
		}
	}

	private Layer getCarousel(IWContext iwc, BannerEntity banner) {
		Layer layer = new Layer();
		layer.setStyleClass(iStyleClass);
		layer.setID(iObjectID);

		Collection ads = new ArrayList();
		if (banner != null) {
			ads = BannerFinder.getAds(banner);
		}

		Iterator iterator = ads.iterator();
		while (iterator.hasNext()) {
			AdEntity ad = (AdEntity) iterator.next();

			Layer image = new Layer();
			image.setStyleClass("imageElement");
			layer.add(image);

			Heading3 heading = new Heading3(ad.getAdName());
			image.add(heading);

			Paragraph paragraph = new Paragraph();
			paragraph.add(new Text(ad.getAdName()));
			image.add(paragraph);

			Link link = new Link("");
			link.setURL(ad.getURL());
			link.setToolTip(ad.getURL());
			link.setStyleClass("open");
			if (iTarget != null) {
				link.setTarget(iTarget);
			}
			image.add(link);

			Image adImage = BannerBusiness.getImage(BannerBusiness.getImageID(ad));
			adImage.setStyleClass("full");
			adImage.setAlt(ad.getAdName());
			image.add(adImage);
		}

		return layer;
	}

	private Layer getAdminPart(IWContext iwc) {
		Layer layer = new Layer();
		layer.setStyleClass("adminLayer");

		Image createImage = iwc.getIWMainApplication().getCoreBundle().getImage("shared/create.gif");

		Link createLink = new Link(createImage);
		createLink.setWindowToOpen(BannerEditorWindow.class);
		createLink.addParameter(BannerBusiness.PARAMETER_BANNER_ID, this._bannerID);
		layer.add(createLink);

		return layer;
	}

	public Web2Business getWeb2Business(IWContext iwc) {
		try {
			return (Web2Business) IBOLookup.getServiceInstance(iwc, Web2Business.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	public boolean deleteBlock(int ICObjectInstanceID) {
		BannerEntity banner = BannerFinder.getObjectInstanceFromID(ICObjectInstanceID);
		if (banner != null) {
			return BannerBusiness.deleteBanner(banner);
		}

		return false;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public Object clone() {
		BannerCarousel obj = null;
		try {
			obj = (BannerCarousel) super.clone();
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

		return obj;
	}

	public void setTarget(String target) {
		this.iTarget = target;
	}

	public void setStyleClass(String styleClass) {
		this.iStyleClass = styleClass;
	}

	public void setObjectID(String objectID) {
		this.iObjectID = objectID;
	}

	public void setDelay(int delay) {
		this.iDelay = delay * 1000;
	}
}