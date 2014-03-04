package com.idega.block.banner.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "ba_ad_space")
@NamedQueries({
	@NamedQuery(name = AdSpace.QUERY_FIND_ALL, query = "from AdSpace a"),
	@NamedQuery(name = AdSpace.QUERY_FIND_ALL_OPEN_SPACES, query = "from AdSpace a where a.clubSpace is false")
})
public class AdSpace implements Serializable {

	private static final long serialVersionUID = 6281281138790206814L;

	protected static final String COLUMN_NAME = "name";
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_WIDTH = "width";
	private static final String COLUMN_HEIGHT = "height";
	private static final String COLUMN_CLUB_SPACE = "club_space";
	private static final String COLUMN_POPUP = "is_popup";
	
	protected static final String AD_SPACE_AD = "banner_ad_space_ad";
	
	public static final String QUERY_FIND_ALL = "adSpace.findAll";
	public static final String QUERY_FIND_ALL_OPEN_SPACES = "adSpace.findAllOpenSpaces";

	@Id
	@Column(name = COLUMN_NAME, length = 50)
	private String name;
	
	@Column(name = COLUMN_DESCRIPTION)
	private String description;
	
	@Column(name = COLUMN_WIDTH)
	private int width;
	
	@Column(name = COLUMN_HEIGHT)
	private int height;
	
	@Column(name = COLUMN_CLUB_SPACE)
	private boolean clubSpace;
	
	@Column(name = COLUMN_POPUP, nullable = true)
	private boolean popup;
	
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Ad.class)
	@JoinTable(name = AD_SPACE_AD, joinColumns = { @JoinColumn(name = COLUMN_NAME) }, inverseJoinColumns = { @JoinColumn(name = Ad.COLUMN_ID) })
	private List<Ad> ads;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean getClubSpace() {
		return clubSpace;
	}
	
	public void setClubSpace(boolean clubSpace) {
		this.clubSpace = clubSpace;
	}
	
	public boolean getPopup() {
		return popup;
	}
	
	public void setPopup(boolean popup) {
		this.popup = popup;
	}

	public List<Ad> getAds() {
		return ads;
	}

	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdSpace other = (AdSpace) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}
}