package com.idega.block.banner.data;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ba_advertisement")
@NamedQueries({
	@NamedQuery(name = BannerAd.QUERY_FIND_ALL_BANNER_ADS, query = "from BannerAd ba"),
	@NamedQuery(name = BannerAd.QUERY_FIND_ALL_BANNER_ADS_BY_CATEGORY, query = "from BannerAd ba where a.category = :category")
})
public class BannerAd implements Serializable {

	private static final long serialVersionUID = -9094300889717171697L;

	protected static final String COLUMN_ID = "ad_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_URL = "url";
	private static final String COLUMN_IMAGE_URL = "image_url";
	private static final String COLUMN_FLASH_URL = "flash_url";
	private static final String COLUMN_CATEGORY = "category";

	public static final String QUERY_FIND_ALL_BANNER_ADS = "bannerAd.findAll";
	public static final String QUERY_FIND_ALL_BANNER_ADS_BY_CATEGORY = "bannerAd.findAllByCategory";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_NAME)
	private String name;

	@Column(name = COLUMN_URL)
	private String url;

	@Column(name = COLUMN_IMAGE_URL)
	private String imageUrl;

	@Column(name = COLUMN_FLASH_URL)
	private String flashUrl;

	@Column(name = COLUMN_CATEGORY)
	private String category;

	@Column(name = "html",length=4000)
	private String html;

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = BannerAdSpace.class)
	@JoinTable(name = BannerAdSpace.AD_SPACE_AD, joinColumns = { @JoinColumn(name = COLUMN_ID) }, inverseJoinColumns = { @JoinColumn(name = BannerAdSpace.COLUMN_NAME) })
	private List<BannerAdSpace> adSpaces;

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getFlashUrl() {
		return flashUrl;
	}

	public void setFlashUrl(String flashUrl) {
		this.flashUrl = flashUrl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<BannerAdSpace> getAdSpaces() {
		return adSpaces;
	}

	public void setAdSpaces(List<BannerAdSpace> adSpaces) {
		this.adSpaces = adSpaces;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BannerAd other = (BannerAd) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
}