package com.idega.block.banner.data.dao.impl;


import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.banner.data.BannerAd;
import com.idega.block.banner.data.BannerAdSpace;
import com.idega.block.banner.data.dao.BannerAdDao;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;

@Repository(BannerAdDao.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = true)
public class BannerAdDaoImpl extends GenericDaoImpl implements BannerAdDao {

	/* Ad spaces */
	@Override
	public List<BannerAdSpace> getAdSpaces() {
		return getResultList(BannerAdSpace.QUERY_FIND_ALL_BANNER_AD_SPACES, BannerAdSpace.class);
	}
	
	@Override
	public List<BannerAdSpace> getOpenAdSpaces() {
		return getResultList(BannerAdSpace.QUERY_FIND_ALL_OPEN_BANNER_AD_SPACES, BannerAdSpace.class);
	}
	
	@Override
	public BannerAdSpace getAdSpace(String name) {
		return find(BannerAdSpace.class, name);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void removeAdSpace(String name) {
		BannerAdSpace adSpace = getAdSpace(name);
		if (adSpace != null) {
			getEntityManager().remove(adSpace);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void storeAdSpace(String name, String description, int width, int height, boolean clubSpace) {
		BannerAdSpace adSpace = getAdSpace(name);
		if (adSpace == null) {
			adSpace = new BannerAdSpace();
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
	public List<BannerAd> getAds() {
		return getResultList(BannerAd.QUERY_FIND_ALL_BANNER_ADS, BannerAd.class);
	}
	
	@Override
	public List<BannerAd> getAds(String category) {
		return getResultList(BannerAd.QUERY_FIND_ALL_BANNER_ADS_BY_CATEGORY, BannerAd.class, new Param("category", category));
	}
	
	@Override
	public BannerAd getAd(Long id) {
		return find(BannerAd.class, id);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void removeAd(Long id) {
		BannerAd ad = getAd(id);
		if (ad != null) {
			getEntityManager().remove(ad);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void storeAd(Long id, String name, String url, String imageUrl, String flashUrl, String category, List<BannerAdSpace> adSpaces,String html) {
		BannerAd ad = id != null ? getAd(id) : null;
		if (ad == null) {
			ad = new BannerAd();
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