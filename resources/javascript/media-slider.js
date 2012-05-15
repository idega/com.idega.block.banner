(function($) {
   $.fn.mediaSlider = function(options) {
	   
	   var delay = 0;

	    var opts = $.extend({}, $.fn.mediaSlider.defaults, options);
	    
	    var createMedia = function(options){
	    	options.link.media({
				width : options.width,
				height : options.height,
				slide: options.slide,
			},
			function(){},
			function(link,div,options){
				var media = jQuery(div);
				options.slide.data("idega-media-data",{"media": media});
				if(options.first){
					var data = slider.data("idega-media-data");
					data.previous = media;
				}
			});
	    }
	    return this.each(function(){
	    	var slider = jQuery(this);
	    	var check = slider.data("idega-media-data");
	    	if(check != undefined){
	    		return;
	    	}
	    	slider.data("idega-media-data",{});
	    	var width = slider.width();
			var height = slider.height();
	    	var first = slider.children(":first");
	    	var link = first.find("a");
			if(link.size() > 0){
				createMedia({
					link: link,
					width: width,
					height: height,
					slide: first,
					first: true
					});
			}
			
			if(slider.children().size() < 2){
				return;
			}
	    	var beforeSlide = function(currentSlideNumber, totalSlideQty,currentSlideHtmlObject){
	    		var nextSlide = currentSlideHtmlObject;
	    		link = nextSlide.find("a");
	    		if(link.size() > 0){
	    			createMedia({
						link: link,
						width: width,
						height: height,
						slide: nextSlide
						});
	    		}else{
	    			nextSlide.append(nextSlide.data("idega-media-data").media);
	    		}
	    	}
	    	
	    	
			slider.bxSlider({
				auto: 		true,
				speed: 		opts.speed,
				controls:	false,
				pause: 		opts.pause,
				onBeforeSlide:	beforeSlide,
				mode:		opts.mode,
				onAfterSlide:	function(currentSlideNumber, totalSlideQty, currentSlideHtmlObject){
					  var current = jQuery(currentSlideHtmlObject);
					  var data = slider.data("idega-media-data");
					  var previous = data.previous;
					  if(previous != undefined){
						  previous.remove();
					  }
//					  var previousData = current.data("idega-media-data");
//					  if(previousData != null){
//						  data.previous = previousData.media;
//					  }
					  data.previous = current.data("idega-media-data").media;
					  
				},
				autoDelay: 	delay + 500 + Math.floor(Math.random()*5001) + Math.floor(Math.random()*5001)
			});
//			jQuery("a").media({width:width,height:height});
	    } ); 
	}
   $.fn.mediaSlider.defaults = {
			  pause: 	10000,
			  speed:	500,
			  mode:		"fade"
	}
})(jQuery);
