(function(exports) {
	$(".head-nav").on("click", ".swiper-slide", function() {
		var $this = $(this),
			index = $this.attr("index"),
			pagedata = $this.attr("pagedata"),
			tagUrl = $this.attr("tagUrl");
			if (parseInt(index) == 3) {
				return;
			} else if(parseInt(index) == 1) {
				// 标签
				$.ajax({
    		       url:tagUrl,
    		       type:"post",
    		       dataType:"jsonp",
    		       data:{tagId:pagedata},
    		       jsonp:"callback",
    		       success:function(data){
    		       	var test =$('#copyright').html();
    		       	$('#nav_masklayer').html('<div class="app-field clearfix" mod="search" style="position: relative; top: 0px; left: 0px;"><div class="control-group"><div class="search-box"><form id="tag_search_form" onsubmit="return false;"><input type="search" placeholder="请输入关键词" id="like_name"><button onclick="search()"></button></form></div></div></div><div class="app-field clearfix" id="searchData" mod="shopping" style="position: relative; top: 0px; left: 0px;">'+ data.result+'</div><div id="copyright">'+ test+'</div>');
    		       	echo.init({
                        offset: 150,
                        throttle: 200
                         });  
                     bannerInit();
                     search.pinInit();
    		       },
    		       error:function(data,res){
    		          console.log(data);
    		       }
    	         });
			} else if(parseInt(index) == 2){
				var xhr = AJAXHtmlFOOT(pagedata);
				$("#nav_masklayer").html(xhr);
				bannerInit();
				search.pinInit();
			}else{
				var xhr = AJAXHtml(pagedata);
				$("#nav_masklayer").html(xhr);
				bannerInit();
				search.pinInit();
			}
			echo.init({
		           offset: 150,
		           throttle: 200
		       });
	})
	function AJAXHtml(someHtml) {
		var xhr = $.ajax({
			url: someHtml,
			async: false
		}).responseText;
		return $(xhr).siblings("#nav_masklayer").html();
	}
	function AJAXHtmlFOOT(someHtml){
		var xhr = $.ajax({
			url: someHtml,
			async: false
		}).responseText;
		return $(xhr).siblings("#nav_masklayer").html()+ '<div id="copyright">' +$(xhr).siblings("#copyright").html()+'</div>';
	}
	function slideInit() {
		// 头部滑动初始化
		var swiper = new Swiper('.swiper-nav', {
	        slidesPerView: 'auto',
	        paginationClickable: true,
	        spaceBetween: 0,
	        freeMode: true,
	    });

	    // 头部选择效果
	    $(".head-nav .swiper-slide").eq(0).children().addClass("red");
		$(".head-nav .slider").width($(".head-nav .swiper-slide").eq(0).width() + 40);
		$(".head-nav").on("click", ".swiper-slide", function() {
			$(this).children().addClass("red").parent().siblings().children().removeClass("red");
			var widths = $(this).width() + 40;
			$(".head-nav .slider").width(widths);
			var howFar = $(this)[0].offsetLeft;
			$(".head-nav .slider").css({
				left: howFar + "px"
			});
		})
	}
    function headInit() {
		var swiper = new Swiper('.swiper-goods', {
	        slidesPerView: 'auto',
	        paginationClickable: true,
	        spaceBetween: 0,
	        freeMode: true,
	        onTouchMove: function(swiper){
                echo.init({
                        offset: 150,
                        throttle: 200
                    });
	        }
	    });
	}
	headInit();
	exports.slideInit = slideInit;
	exports.headInit = headInit;
})(window);