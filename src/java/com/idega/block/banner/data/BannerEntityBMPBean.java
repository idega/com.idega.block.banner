//idega 2001 - Laddi



package com.idega.block.banner.data;





import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.core.component.data.ICObjectInstance;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;



public class BannerEntityBMPBean extends GenericEntity implements BannerEntity {

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameAttribute(),"Attribute",true,true,String.class);

    addManyToManyRelationShip(ICObjectInstance.class,"BA_BANNER_IC_OBJECT_INSTANCE");
    addManyToManyRelationShip(AdEntity.class, "BA_BANNER_AD");
	}

	public static String getColumnNameBannerID() { return "BA_BANNER_ID"; }
	public static String getColumnNameAttribute() { return "ATTRIBUTE"; }
	public static String getEntityTableName() { return "BA_BANNER"; }

  public String getIDColumnName(){
		return getColumnNameBannerID();
	}

	public String getEntityName(){
		return getEntityTableName();
	}

  public void setAttribute(String attribute) {
    setColumn(getColumnNameAttribute(),attribute);
  }

  public String getAttribute() {
    return (String) getColumnValue(getColumnNameAttribute());
  }

  public void remove() throws RemoveException {
    try {
			idoRemoveFrom(ICObjectInstance.class);
		}
		catch (IDORemoveRelationshipException e) {
			e.printStackTrace();
		}
    try {
			idoRemoveFrom(AdEntity.class);
		}
		catch (IDORemoveRelationshipException e) {
			e.printStackTrace();
		}

    super.remove();
  }
  
  public void addAd(AdEntity ad) throws IDOAddRelationshipException {
  	idoAddTo(ad);
  }
  
  public void removeAd(AdEntity ad) throws IDORemoveRelationshipException {
  	idoRemoveFrom(ad);
  }
  
  public void removeAds() throws IDORemoveRelationshipException {
  	idoRemoveFrom(AdEntity.class);
  }
  
  public Collection getAds() throws IDORelationshipException {
  	return idoGetRelatedEntities(AdEntity.class);
  }
  
  public boolean hasRelation(AdEntity ad) {
  	try {
  		Collection entities = this.idoGetRelatedEntities(ad);
    	return !entities.isEmpty();
  	}
  	catch (IDORelationshipException ire) {
  		ire.printStackTrace();
  		return false;
  	}
  }
  
  public void addICObjectInstance(ICObjectInstance obj) throws IDOAddRelationshipException {
  	idoAddTo(obj);
  }
  
  public void removeICObjectInstance(ICObjectInstance obj) throws IDORemoveRelationshipException {
  	idoRemoveFrom(obj);
  }
  
  public void removeICObjectInstances() throws IDORemoveRelationshipException {
  	idoRemoveFrom(ICObjectInstance.class);
  }
  
  public Collection getICObjectInstances() throws IDORelationshipException {
  	return idoGetRelatedEntities(ICObjectInstance.class);
  }
  
  public Object ejbFindByAttribute(String attribute) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, getColumnNameAttribute(), MatchCriteria.EQUALS, attribute));
		
		return idoFindOnePKByQuery(query);
  }
 }