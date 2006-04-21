package com.idega.block.banner.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class AdEntityHomeImpl extends IDOFactory implements AdEntityHome {

	public Class getEntityInterfaceClass() {
		return AdEntity.class;
	}

	public AdEntity create() throws CreateException {
		return (AdEntity) super.createIDO();
	}

	public AdEntity findByPrimaryKey(Object pk) throws FinderException {
		return (AdEntity) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByUser(int userID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AdEntityBMPBean) entity).ejbFindAllByUser(userID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}