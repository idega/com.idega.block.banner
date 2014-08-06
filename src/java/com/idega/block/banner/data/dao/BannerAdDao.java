package com.idega.block.banner.data.dao;


import java.util.List;

import com.idega.block.banner.data.BannerAd;
import com.idega.block.banner.data.BannerAdSpace;
import com.idega.core.persistence.GenericDao;

public interface BannerAdDao extends GenericDao {

	public static final String BEAN_NAME = "bannerAdDao";

	public List<BannerAdSpace> getAdSpaces();

	public List<BannerAdSpace> getOpenAdSpaces();

	public BannerAdSpace getAdSpace(String name);

	public void removeAdSpace(String name);

	public void storeAdSpace(String name, String description, int width, int height, boolean clubSpace);

	public List<BannerAd> getAds();

	public List<BannerAd> getAds(String category);

	public BannerAd getAd(Long id);

	public void removeAd(Long id);

	public void storeAd(Long id, String name, String url, String imageUrl, String flashUrl, String category, List<BannerAdSpace> adSpaces,String html);

}