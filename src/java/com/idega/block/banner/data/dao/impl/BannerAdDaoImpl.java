package com.idega.block.banner.data.dao.impl;


import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.banner.data.Ad;
import com.idega.block.banner.data.AdSpace;
import com.idega.block.banner.data.dao.BannerAdDao;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;

@Repository(BannerAdDao.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class BannerAdDaoImpl extends GenericDaoImpl implements BannerAdDao {

	/* Ad spaces */
	@Override
	public List<AdSpace> getAdSpaces() {
		return getResultList(AdSpace.QUERY_FIND_ALL, AdSpace.class);
	}
	
	@Override
	public List<AdSpace> getOpenAdSpaces() {
		return getResultList(AdSpace.QUERY_FIND_ALL_OPEN_SPACES, AdSpace.class);
	}
	
	@Override
	public AdSpace getAdSpace(String name) {
		return find(AdSpace.class, name);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void removeAdSpace(String name) {
		AdSpace adSpace = getAdSpace(name);
		if (adSpace != null) {
			getEntityManager().remove(adSpace);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void storeAdSpace(String name, String description, int width, int height, boolean clubSpace) {
		AdSpace adSpace = getAdSpace(name);
		if (adSpace == null) {
			adSpace = new AdSpace();
			adSpace.setName(name);
		}
		adSpace.setDescription(description);
		adSpace.setWidth(width);
		adSpace.setHeight(height);
		adSpace.setClubSpace(clubSpace);
		
		getEntityManager().persist(adSpace);
	}
	
	/* Ads */
	@Override
	public List<Ad> getAds() {
		return getResultList(Ad.QUERY_FIND_ALL, Ad.class);
	}
	
	@Override
	public List<Ad> getAds(String category) {
		return getResultList(Ad.QUERY_FIND_ALL_BY_CATEGORY, Ad.class, new Param("category", category));
	}
	
	@Override
	public Ad getAd(Long id) {
		return find(Ad.class, id);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void removeAd(Long id) {
		Ad ad = getAd(id);
		if (ad != null) {
			getEntityManager().remove(ad);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void storeAd(Long id, String name, String url, String imageUrl, String flashUrl, String category, List<AdSpace> adSpaces,String html) {
		Ad ad = id != null ? getAd(id) : null;
		if (ad == null) {
			ad = new Ad();
		}
		ad.setName(name);
		ad.setUrl(url);
		ad.setImageUrl(imageUrl);
		ad.setFlashUrl(flashUrl);
		ad.setCategory(category);
		ad.setAdSpaces(adSpaces);
		ad.setHtml(html);
		
		getEntityManager().persist(ad);
	}
}