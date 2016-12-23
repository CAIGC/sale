/**
 * Created by oliver on 2016/11/25.
 */
(function (exports) {
	var searchUrl = "";
    var search = {
        //初始化搜索框显示效果
        pinInit: function() {
            var pin = parseInt($(".app-field[mod='search']").attr("pin"));
            if (pin == 1) {
                $(".search-btn").pin({
                    containerSelector: "body"
                })
            } else {
                return;
            }
        },
        //蒙版显示
        maskShow: function(e) {
        	$("div[mod='search']").siblings().addClass("hide");
        	$(".pin-wrapper .search-btn").css({
        		"position": "static"
        	})
        	$("body").scrollTop(0);
            $(".search-mask").removeClass("hide");
            $(".search-mask input[type='search']").focus();
            var searchData = JSON.parse(localStorage.searchData);
            var str = "";
            if(searchData.length > 0) {
            	$(".title_btn").removeClass("hide");	
            	for(var i =0; i < searchData.length;i++) {
            		str+="<span>" + searchData[i] + "</span>";
            	}
            } else {
            	$(".title_btn").addClass("hide");
            	str+="<div class='s_empty'>无搜索历史</div>"
            }
            
            $(".lately-content").html(str);
        },
        //蒙版隐藏
        maskHide: function() {
			$("div[mod='search']").siblings().removeClass("hide");
            $(".search-mask").addClass("hide");
            $(".search-mask input[type='search']").blur();
        },
        getId: function(Id) {
        	searchUrl = Id;
        	if(localStorage.searchData == undefined) {
        		localStorage.searchData = "[]";
        	};
        },
        //搜索
        searchEve: function() {
            var tagName = $("#like_name").val();
            if(tagName == "" || tagName ==null || tagName == undefined ){
                 var xhr = AJAXHtml("shop_home_page.html");
                 $("#nav_masklayer").html(xhr);
                 headInit();
                 bannerInit();
                 this.pinInit();
            }else{
        	var searchData = JSON.parse(localStorage.searchData);
            for(var j = 0;j < searchData.length; j++) {
            	if(searchData[j] == tagName) {
            		break;
            	}
            }
            if (searchData.length == j) {
            	searchData.push(tagName);
            }
            localStorage.searchData = JSON.stringify(searchData);
            var webstoreId = $("#webstoreId").val();
            $.ajax({
                url:searchUrl,
                type:"post",
                dataType:"jsonp",
                data:{tagName:encodeURIComponent(tagName),webstoreId:webstoreId},
                jsonp:"callback",
                success:function(data){
                        $('#nav_masklayer').find('.app-field:not("div[mod=search]")').remove();
                        $('#nav_masklayer').find('.clearfix').after('<div class="app-field clearfix" id="searchData" mod="shopping" style="position: relative; top: 0px; left: 0px;"></div>');
                        $('#searchData').html(data.result);
                        echo.init({
                            offset: 150,
                            throttle: 200
                        });
                        $(".search-mask").addClass("hide");
                },
                error:function(data,res){
                	console.log("请求失败");
                }
            });
            $("#like_name").blur();
          }
        },
        //清除
        clearData: function() {
            $(this).addClass("hide");
            localStorage.searchData = "[]";
            $(".lately-content").html("<div class='s_empty'>无搜索历史</div>");
        }
    }
    exports.search = search;
    search.pinInit();

    //蒙版显示
    $("#nav_masklayer").on("click", ".search-btn", search.maskShow);

    //蒙版隐藏
    $("#nav_masklayer").on("click", ".search-close", search.maskHide);

    // 历史搜索清楚
    $("#nav_masklayer").on("click", ".title_btn", search.clearData);

    //热门搜索点击
    $(document).on("click", ".hot-content span, .lately-content span", function() {
            $(".search-box input").val($(this).text());
            search.searchEve();
    });

    //搜索
    $(document).on("search","#like_name", $.proxy(search.searchEve, search));
    
    $(document).on("click", ".search-box button", $.proxy(search.searchEve, search));

    //点击效果
    $(document).on("touchstart", ".hot-content span, .lately-content span", function() {
        $(this).addClass("eee");
    })

    //点击效果
    $(document).on("touchend", ".hot-content span, .lately-content span", function() {
        $(this).removeClass("eee");
    })
    function AJAXHtml(someHtml) {
        var xhr = $.ajax({
            url: someHtml,
            async: false
        }).responseText;
        return $(xhr).siblings("#nav_masklayer").html();
    }
})(window)