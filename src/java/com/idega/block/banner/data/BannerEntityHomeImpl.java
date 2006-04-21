package com.idega.block.banner.data;


import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class BannerEntityHomeImpl extends IDOFactory implements BannerEntityHome {

	public Class getEntityInterfaceClass() {
		return BannerEntity.class;
	}

	public BannerEntity create() throws CreateException {
		return (BannerEntity) super.createIDO();
	}

	public BannerEntity findByPrimaryKey(Object pk) throws FinderException {
		return (BannerEntity) super.findByPrimaryKeyIDO(pk);
	}

	public BannerEntity findByAttribute(String attribute) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BannerEntityBMPBean) entity).ejbFindByAttribute(attribute);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}