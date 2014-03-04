package com.idega.block.banner.presentation.ad;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.banner.BannerConstants;
import com.idega.block.banner.business.AddBean;
import com.idega.block.banner.data.Ad;
import com.idega.block.banner.data.AdSpace;
import com.idega.block.banner.data.dao.GolfAdDao;
import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.Web2Business;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class BannerAd extends IWBaseComponent {

	private String regionName;
	private String category;
	
	@Autowired
	private GolfAdDao dao;

	@Autowired
	private Web2Business web2Business;

	@Autowired
	private JQuery jQuery;

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		IWBundle bundle = iwc.getIWMainApplication().getBundle(BannerConstants.IW_BUNDLE_IDENTIFIER);
		
		AddBean bean = getBeanInstance("addBean");
		Random random = new Random();
		
		if (getRegionName() != null) {
			AdSpace space = getDao().getAdSpace(regionName);
			if (space != null) {
				bean.setAdSpace(space);
				
				List<Ad> ads = space.getAds();
				if (ads != null && !ads.isEmpty()) {
					int index = random.nextInt(ads.size());
			        bean.setAd(ads.get(index));
			        
					if (space.getPopup()) {
						try {
							Cookie cookie = iwc.getCookie("golf.ad.popup." + space.getName() + "." + bean.getAd().getId().toString());
							if (cookie != null || iwc.getAccessController().isAdmin(iwc)) {
								return;
							}
							else {
								cookie = new Cookie("golf.ad.popup." + space.getName() + "." + bean.getAd().getId().toString(), Boolean.TRUE.toString());
								cookie.setMaxAge(60 * 60 * 24);
								iwc.addCookies(cookie);
							}
						}
						catch (Exception se) {
							se.printStackTrace();
						}
					}

			        if (bean.getAd().getFlashUrl() != null && bean.getAd().getFlashUrl().length() > 0) {
						PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
						PresentationUtil.addJavaScriptSourceLineToHeader(iwc, bundle.getVirtualPathWithFileNameString("javascript/jquery.swfobject.min.js"));
			        }

					FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
					if (space.getPopup()) {
						PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, getWeb2Business().getBundleURIsToFancyBoxScriptFiles());
						PresentationUtil.addStyleSheetToHeader(iwc, getWeb2Business().getBundleURIToFancyBoxStyleFile());

						facelet.setFaceletURI(bundle.getFaceletURI("ad/popup.xhtml"));
					}
					else {
						facelet.setFaceletURI(bundle.getFaceletURI("ad/adRegion.xhtml"));
					}
					add(facelet);
				}
			}
		}
		else if (getCategory() != null) {
			List<Ad> ads = getDao().getAds(category);
			if (ads == null) {
				ads = new ArrayList<Ad>();
			}
			
			Long defaultAd = Long.parseLong(iwc.getApplicationSettings().getProperty("golf.default.club.ad", "-1"));
			if (defaultAd > 0) {
				ads.add(0, getDao().getAd(defaultAd));
			}
			
			bean.setAds(ads);
			
			if (!ads.isEmpty()) {
				PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getJQuery().getBundleURIToJQueryLib());
				PresentationUtil.addJavaScriptSourceLineToHeader(iwc, bundle.getVirtualPathWithFileNameString("javascript/jquery.bjqs.min.js"));
				PresentationUtil.addJavaScriptSourceLineToHeader(iwc, bundle.getVirtualPathWithFileNameString("javascript/clubBanners.js"));

				PresentationUtil.addStyleSheetToHeader(iwc, bundle.getVirtualPathWithFileNameString("style/bjqs.css"));
				
				FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
				facelet.setFaceletURI(bundle.getFaceletURI("ad/clubRegion.xhtml"));
				add(facelet);
			}
		}
	}
	
	private GolfAdDao getDao() {
		if (dao == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return dao;
	}

	protected Web2Business getWeb2Business() {
		if (web2Business == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return web2Business;
	}

	private JQuery getJQuery() {
		if (jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return jQuery;
	}
	
	private String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	private String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}