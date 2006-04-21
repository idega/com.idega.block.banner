package com.idega.block.banner.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface BannerEntityHome extends IDOHome {

	public BannerEntity create() throws CreateException;

	public BannerEntity findByPrimaryKey(Object pk) throws FinderException;

	/**
	 * @see com.idega.block.banner.data.BannerEntityBMPBean#ejbFindByAttribute
	 */
	public BannerEntity findByAttribute(String attribute) throws FinderException;
}