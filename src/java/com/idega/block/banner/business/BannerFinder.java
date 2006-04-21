package com.idega.block.banner.business;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.banner.data.AdEntity;
import com.idega.block.banner.data.AdEntityHome;
import com.idega.block.banner.data.BannerEntity;
import com.idega.block.banner.data.BannerEntityHome;
import com.idega.business.IBORuntimeException;
import com.idega.core.component.business.ICObjectBusiness;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author
 * @version 1.0
 */

public class BannerFinder {

	public static BannerEntity getBanner(String attribute) {
		try {
			return ((BannerEntityHome) IDOLookup.getHome(BannerEntity.class)).findByAttribute(attribute);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
		catch (FinderException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static BannerEntity getBanner(int bannerID) {
		try {
			return ((BannerEntityHome) IDOLookup.getHome(BannerEntity.class)).findByPrimaryKey(new Integer(bannerID));
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
		catch (FinderException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static AdEntity getAd(int adID) {
		try {
			AdEntity ad = ((AdEntityHome) IDOLookup.getHome(AdEntity.class)).findByPrimaryKey(new Integer(adID));
			return ad;
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Collection getAdsInBanner(BannerEntity banner, int userID) {
		try {
			Collection list = null;
			if (banner != null) {
				list = banner.getAds();
			}
			
			Collection userList = ((AdEntityHome) IDOLookup.getHome(AdEntity.class)).findAllByUser(userID);
			if (userList != null) {
				if (list != null) {
					Iterator iter = userList.iterator();
					while (iter.hasNext()) {
						AdEntity ad = (AdEntity) iter.next();
						if (!userList.contains(ad)) {
							userList.add(ad);
						}
					}
				}
				return userList;
			}
			else {
				if (list != null) {
					return list;
				}
			}
			return null;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	public static Collection getAdsInBanner(BannerEntity banner) {
		try {
			Collection ads = banner.getAds();
			if (!ads.isEmpty()) {
				return ads;
			}
			return null;
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Collection getAds(BannerEntity banner) {
		try {
			return banner.getAds();
		}
		catch (IDORelationshipException e) {
			return null;
		}
	}

	public static ICFile getFile(int ICFileID) {
		try {
			return ((com.idega.core.file.data.ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(ICFileID));
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Collection getFilesInAd(AdEntity ad) {
		try {
			Collection files = ad.getFiles();
			if (!files.isEmpty()) {
				return files;
			}
			return null;
		}
		catch (Exception e) {
			return null;
		}
	}

	// BEGIN COPY PASTE CRAP

	public static BannerEntity getObjectInstanceFromID(int ICObjectInstanceID) {
		try {
			ICObjectBusiness icob = ICObjectBusiness.getInstance();
			ICObjectInstance ICObjInst = icob.getICObjectInstance(ICObjectInstanceID);
			return (BannerEntity) icob.getRelatedEntity(ICObjInst, BannerEntity.class);
		}
		catch (com.idega.data.IDOFinderException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static int getRelatedEntityId(ICObjectInstance eObjectInstance) {
		try {
			ICObjectBusiness bis = ICObjectBusiness.getInstance();
			return bis.getRelatedEntityId(eObjectInstance, BannerEntity.class);
		}
		catch (NullPointerException n) {
			System.err.println("[BannerFinder] Exception caught...returning -1");
			n.printStackTrace();
			return -1;
		}
	}

	public static int getObjectInstanceIdFromID(int bannerID) {
		try {
			BannerEntity banner = ((com.idega.block.banner.data.BannerEntityHome) com.idega.data.IDOLookup.getHome(BannerEntity.class)).findByPrimaryKey(new Integer(bannerID));
			Collection obj = banner.getICObjectInstances();
			Iterator iter = obj.iterator();
			while (iter.hasNext()) {
				return ((ICObjectInstance) iter.next()).getID();
			}
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
}