package com.idega.block.banner.business;


import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.banner.data.BannerAd;
import com.idega.block.banner.data.BannerAdSpace;
import com.idega.builder.bean.AdvancedProperty;

@Service("addBean")
@Scope("request")
public class AddBean {
	private String eventHandler;
	private String responseUrl;
	private List<AdvancedProperty> properties;
	private BannerAdSpace adSpace;
	private BannerAd ad;
	private List<BannerAd> ads;
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
	public BannerAdSpace getAdSpace() {
		return adSpace;
	}

	public void setAdSpace(BannerAdSpace adSpace) {
		this.adSpace = adSpace;
	}

	public BannerAd getAd() {
		return ad;
	}

	public void setAd(BannerAd ad) {
		this.ad = ad;
	}

	public List<BannerAd> getAds() {
		return ads;
	}

	public void setAds(List<BannerAd> ads) {
		this.ads = ads;
	}
}