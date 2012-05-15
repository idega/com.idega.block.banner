package com.idega.block.banner.presentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import com.idega.block.banner.BannerConstants;
import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.Web2Business;
import com.idega.block.web2.business.Web2BusinessBean;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.StringUtil;
import com.idega.webface.WFUtil;

public class MediaSlider extends Block{
	private static final String DEFAULT_WIDTH = "400px";
	private static final String DEFAULT_HEIGHT = "400px";
	
	private Collection<String> uris = null;
	
	private int pause = 0;
	
	private String urisString = null;
	
	public String getUrisString() {
		return urisString;
	}

	public void setUrisString(String urisString) {
		this.urisString = urisString;
	}

	public Collection<String> getUris() {
		if(!ListUtil.isEmpty(uris)){
			return uris;
		}
		if(!StringUtil.isEmpty(urisString)){
			String [] urisFromString = urisString.split(",");
			return Arrays.asList(urisFromString);
		}
		return uris;
	}

	public void setUris(Collection<String> uris) {
		this.uris = uris;
	}

	public int getPause() {
		if(pause == 0){
			return 10000;
		}
		return pause;
	}

	public void setPause(int pause) {
		this.pause = pause;
	}

	@Override
	public void main(IWContext iwc){
		Layer container = new Layer();
		add(container);
		container.setStyleAttribute("width", getWidth());
		container.setStyleAttribute("height", getHeight());
		
		Collection<String> uris = getUris();
		if(ListUtil.isEmpty(uris)){
			return;
		}
		for(String uri : uris){
			Layer slide = new Layer();
			container.add(slide);
			slide.setStyleAttribute("width", "100%");
			slide.setStyleAttribute("height", "100%");
			
			Link link = new Link(CoreConstants.EMPTY);
			slide.add(link);
			link.setStyleClass("media");
			link.setURL(uri);
			
		}
		
		Layer scriptLayer = new Layer();
		add(scriptLayer);
		String script = new StringBuilder("jQuery(document).ready(function(){jQuery('#").append(container.getId())
				.append("').mediaSlider({\n pause:").append(getPause()).append("\n});\n});").toString();
		
		scriptLayer.addText(PresentationUtil.getJavaScriptAction(script));
		addFiles(iwc);
	}
	
	private void addFiles(IWContext iwc){
		List<String> scripts = new ArrayList<String>();

		Web2Business web2 = WFUtil.getBeanInstance(iwc, Web2Business.SPRING_BEAN_IDENTIFIER);
		if (web2 != null) {
			JQuery  jQuery = web2.getJQuery();
			scripts.add(jQuery.getBundleURIToJQueryLib());

			scripts.addAll(web2.getBundleUrisToBxSliderScriptFiles(Web2BusinessBean.BX_SLIDER_3_0));

			scripts.addAll(web2.getBundleUrisTojQueryMediaScriptFiles(Web2BusinessBean.JQUERY_MEDIA_0_96));
			
		}else{
			getLogger().log(Level.WARNING, "Failed getting Web2Business no jQuery and it's plugins files were added");
		}

		IWMainApplication iwma = iwc.getApplicationContext().getIWMainApplication();
		IWBundle iwb = iwma.getBundle(BannerConstants.IW_BUNDLE_IDENTIFIER);
		scripts.add(iwb.getVirtualPathWithFileNameString("javascript/media-slider.js"));
		
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);
	}
	
	@Override
	public String getWidth(){
		String width = super.getWidth();
		if(StringUtil.isEmpty(width)){
			return DEFAULT_WIDTH;
		}
		return width;
	}
	
	@Override
	public String getHeight(){
		String height = super.getHeight();
		if(StringUtil.isEmpty(height)){
			return DEFAULT_HEIGHT;
		}
		return height;
	}

}
