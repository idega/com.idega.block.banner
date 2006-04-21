package com.idega.block.banner.data;


import com.idega.core.file.data.ICFile;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import java.util.Collection;
import com.idega.data.IDORemoveRelationshipException;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface AdEntity extends IDOEntity {

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#getUserID
	 */
	public int getUserID();

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#getAdName
	 */
	public String getAdName();

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#getHits
	 */
	public int getHits();

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#getMaxHits
	 */
	public int getMaxHits();

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#getImpressions
	 */
	public int getImpressions();

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#getMaxImpressions
	 */
	public int getMaxImpressions();

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#getBeginDate
	 */
	public Timestamp getBeginDate();

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#getEndDate
	 */
	public Timestamp getEndDate();

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#getURL
	 */
	public String getURL();

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#setUserID
	 */
	public void setUserID(int userID);

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#setAdName
	 */
	public void setAdName(String adName);

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#setHits
	 */
	public void setHits(int hits);

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#setMaxHits
	 */
	public void setMaxHits(int maxHits);

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#setImpressions
	 */
	public void setImpressions(int impressions);

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#setMaxImpressions
	 */
	public void setMaxImpressions(int maxImpressions);

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#setBeginDate
	 */
	public void setBeginDate(Timestamp beginDate);

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#setEndDate
	 */
	public void setEndDate(Timestamp endDate);

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#setURL
	 */
	public void setURL(String URL);

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#addFile
	 */
	public void addFile(ICFile file) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#removeFile
	 */
	public void removeFile(ICFile file) throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#removeFiles
	 */
	public void removeFiles() throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#removeBanners
	 */
	public void removeBanners() throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.banner.data.AdEntityBMPBean#getFiles
	 */
	public Collection getFiles() throws IDORelationshipException;
}