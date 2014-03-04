package com.idega.block.banner.data.dao;


import java.util.List;

import com.idega.block.banner.data.Ad;
import com.idega.block.banner.data.AdSpace;
import com.idega.core.persistence.GenericDao;

public interface GolfAdDao extends GenericDao {

	public static final String BEAN_NAME = "golfAdDao";

	public List<AdSpace> getAdSpaces();
	
	public List<AdSpace> getOpenAdSpaces();
	
	public AdSpace getAdSpace(String name);
	
	public void removeAdSpace(String name);
	
	public void storeAdSpace(String name, String description, int width, int height, boolean clubSpace);
	
	public List<Ad> getAds();
	
	public List<Ad> getAds(String category);
	
	public Ad getAd(Long id);
	
	public void removeAd(Long id);
	
	public void storeAd(Long id, String name, String url, String imageUrl, String flashUrl, String category, List<AdSpace> adSpaces);
	
}