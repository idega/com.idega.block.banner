package com.idega.block.banner.business;


import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.banner.data.Ad;
import com.idega.block.banner.data.AdSpace;
import com.idega.builder.bean.AdvancedProperty;

@Service("addBean")
@Scope("request")
public class AddBean {
	private String eventHandler;
	private String responseUrl;
	private List<AdvancedProperty> properties;
	private AdSpace adSpace;
	private Ad ad;
	private List<Ad> ads;
	public String getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(String eventHandler) {
		this.eventHandler = eventHandler;
	}

	public String getResponseUrl() {
		return responseUrl;
	}

	public void setResponseUrl(String responseUrl) {
		this.responseUrl = responseUrl;
	}
	public List<AdvancedProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<AdvancedProperty> properties) {
		this.properties = properties;
	}
	public AdSpace getAdSpace() {
		return adSpace;
	}

	public void setAdSpace(AdSpace adSpace) {
		this.adSpace = adSpace;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public List<Ad> getAds() {
		return ads;
	}

	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}
}