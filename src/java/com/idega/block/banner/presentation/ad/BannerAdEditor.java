package com.idega.block.banner.presentation.ad;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.banner.BannerConstants;
import com.idega.block.banner.business.AddBean;
import com.idega.block.banner.data.AdSpace;
import com.idega.block.banner.data.dao.GolfAdDao;
import com.idega.block.media.business.MediaBusiness;
import com.idega.block.web2.business.JQuery;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogicWrapper;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.AccessController;
import com.idega.core.file.business.ICFileSystemFactory;
import com.idega.core.file.data.ICFile;
import com.idega.event.IWPageEventListener;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.UploadFile;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.FileUploadUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class BannerAdEditor extends IWBaseComponent implements IWPageEventListener {
	
	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_AD = "prm_ad_id";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_URL = "prm_url";
	private static final String PARAMETER_FLASH = "prm_flash";
	private static final String PARAMETER_IMAGE = "prm_image";
	private static final String PARAMETER_FLASH_URL = "prm_flash_url";
	private static final String PARAMETER_IMAGE_URL = "prm_image_url";
	private static final String PARAMETER_REGION = "prm_region";
	private static final String PARAMETER_HTML = "prm_html";
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_DELETE = 3;

	@Autowired
	private GolfAdDao dao;
	
	@Autowired
	private JQuery jQuery;

	@Autowired
	private BuilderLogicWrapper wrapper;

	private boolean hasRights(IWContext iwc){
		AccessController access = iwc.getAccessController();
		try{
			return iwc.isLoggedOn() && (access.isAdmin(iwc) || access.hasRole("banner_ad_editor", iwc));
		}catch (Exception e) {
			getLogger().log(Level.WARNING, "failed getting permissions", e);
		}
		return false;
	}
	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		
		try {
			if (!hasRights(iwc)) {
				return;
			}
		}
		catch (Exception se) {
			se.printStackTrace();
		}
		
		IWBundle bundle = iwc.getIWMainApplication().getBundle(BannerConstants.IW_BUNDLE_IDENTIFIER);
		
		AddBean bean = getBeanInstance("addBean");
		String category = iwc.getApplicationSettings().getProperty("golf.ad.category", "none");
		
		FaceletComponent facelet = (FaceletComponent)iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				bean.setAds(getDao().getAds(category));
				facelet.setFaceletURI(bundle.getFaceletURI("ad/view.xhtml"));
				break;
	
			case ACTION_EDIT:
				try {
					bean.setResponseUrl(getWrapper().getBuilderService(iwc).getCurrentPageURI(iwc));
				}
				catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
				bean.setEventHandler(IWMainApplication.getEncryptedClassName(BannerAdEditor.class));

				List<AdSpace> adSpaces = getDao().getAdSpaces();
				if (iwc.isParameterSet(PARAMETER_AD)) {
					bean.setAd(getDao().getAd(Long.parseLong(iwc.getParameter(PARAMETER_AD))));
				}
				
				if (bean.getAd() != null) {
			        if (bean.getAd().getFlashUrl() != null && bean.getAd().getFlashUrl().length() > 0) {
						PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
						PresentationUtil.addJavaScriptSourceLineToHeader(iwc, bundle.getVirtualPathWithFileNameString("javascript/jquery.swfobject.min.js"));
			        }
				}
				
				List<AdvancedProperty> properties = new ArrayList<AdvancedProperty>();
				for (AdSpace adSpace : adSpaces) {
					AdvancedProperty property = new AdvancedProperty(adSpace.getName(), (adSpace.getDescription() != null ? adSpace.getDescription() : "") + " (" + adSpace.getWidth() + "x" + adSpace.getHeight() + ")");
					if (bean.getAd() != null && bean.getAd().getAdSpaces().contains(adSpace)) {
						property.setSelected(true);
					}
					
					properties.add(property);
				}
				bean.setProperties(properties);
				
				facelet.setFaceletURI(bundle.getFaceletURI("ad/edit.xhtml"));
				break;
				
			case ACTION_DELETE:
				if (iwc.isParameterSet(PARAMETER_AD)) {
					getDao().removeAd(Long.parseLong(iwc.getParameter(PARAMETER_AD)));
				}
				bean.setAds(getDao().getAds(category));
				facelet.setFaceletURI(bundle.getFaceletURI("ad/view.xhtml"));
				break;
	
		}
		add(facelet);
	}
	
	private int parseAction(IWContext iwc) {
		int action = iwc.isParameterSet(PARAMETER_ACTION) ? Integer.parseInt(iwc.getParameter(PARAMETER_ACTION)) : ACTION_VIEW;
		return action;
	}

	@Override
	public boolean actionPerformed(IWContext iwc) throws IWException {
		Long id = iwc.isParameterSet(PARAMETER_AD) ? Long.parseLong(iwc.getParameter(PARAMETER_AD)) : null;
		String name = iwc.getParameter(PARAMETER_NAME);
		String url = iwc.getParameter(PARAMETER_URL);
		String flashUrl = iwc.isParameterSet(PARAMETER_FLASH_URL) ? iwc.getParameter(PARAMETER_FLASH_URL) : null;
		String imageUrl = iwc.isParameterSet(PARAMETER_IMAGE_URL) ? iwc.getParameter(PARAMETER_IMAGE_URL) : null;
		String html = iwc.isParameterSet(PARAMETER_HTML) ? iwc.getParameter(PARAMETER_HTML) : null;
		String category = iwc.getApplicationSettings().getProperty("golf.ad.category", "none");
		String[] spaces = iwc.getParameterValues(PARAMETER_REGION);
		
		try {
			Map<String, UploadFile> images = FileUploadUtil.getAllUploadedFiles(iwc);

			UploadFile image = images.get(PARAMETER_IMAGE);
			if (image != null && !image.getRealPath().endsWith("/")) {
				ICFile icFile = MediaBusiness.saveMediaToDB(image, -1, iwc);
				if (icFile != null) {
					imageUrl = ICFileSystemFactory.getFileSystem(iwc).getFileURI(icFile);
				}
			}

			UploadFile flash = images.get(PARAMETER_FLASH);
			if (flash != null && !flash.getRealPath().endsWith("/")) {
				ICFile icFile = MediaBusiness.saveMediaToDB(flash, -1, iwc);
				if (icFile != null) {
					flashUrl = ICFileSystemFactory.getFileSystem(iwc).getFileURI(icFile);
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}

		List<AdSpace> adSpaces = new ArrayList<AdSpace>();
		if (spaces != null && spaces.length > 0) {
			for (String space : spaces) {
				adSpaces.add(getDao().getAdSpace(space));
			}
		}
		else {
			adSpaces = null;
		}
		
		getDao().storeAd(id, name, url, imageUrl, flashUrl, category, adSpaces,html);
		
		return true;
	}
	
	private GolfAdDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return dao;
	}
	
	private JQuery getJQuery() {
		if (jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return jQuery;
	}
	
	private BuilderLogicWrapper getWrapper() {
		if (wrapper == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return wrapper;
	}
}