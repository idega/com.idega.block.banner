package com.idega.block.banner.data;


import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import java.util.Collection;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDOEntity;

public interface BannerEntity extends IDOEntity {

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#setAttribute
	 */
	public void setAttribute(String attribute);

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#getAttribute
	 */
	public String getAttribute();

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#addAd
	 */
	public void addAd(AdEntity ad) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#removeAd
	 */
	public void removeAd(AdEntity ad) throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#removeAds
	 */
	public void removeAds() throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#getAds
	 */
	public Collection getAds() throws IDORelationshipException;

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#hasRelation
	 */
	public boolean hasRelation(AdEntity ad);

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#addICObjectInstance
	 */
	public void addICObjectInstance(ICObjectInstance obj) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#removeICObjectInstance
	 */
	public void removeICObjectInstance(ICObjectInstance obj) throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#removeICObjectInstances
	 */
	public void removeICObjectInstances() throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#getICObjectInstances
	 */
	public Collection getICObjectInstances() throws IDORelationshipException;
}