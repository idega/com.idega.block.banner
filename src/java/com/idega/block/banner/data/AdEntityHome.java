package com.idega.block.banner.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface AdEntityHome extends IDOHome {

	public AdEntity create() throws CreateException;

	public AdEntity findByPrimaryKey(Object pk) throws FinderException;

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#ejbFindAllByUser
	 */
	public Collection findAllByUser(int userID) throws FinderException;
}