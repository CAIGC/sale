var app = document.getElementById("nav_masklayer");
var $appField = app.querySelectorAll(".app-field");
for(var i = 0, len = $appField.length; i < len; i++) {
    switch($appField[i].getAttribute("mod")) {
        case "notice":
            noticeRoll($appField[i]);
            break;
        case "banner":
            bannerRoll($appField[i], i);
            break;
    }
 }
function bannerInit(){
var $appField = app.querySelectorAll(".app-field");
for(var i = 0, len = $appField.length; i < len; i++) {
    switch($appField[i].getAttribute("mod")) {
        case "notice":
            noticeRoll($appField[i]);
            break;
        case "banner":
            bannerRoll($appField[i], i);
            break;
    }
}
}

// 公告滚动
function noticeRoll(ed) {
    var $text = ed.querySelector(".custom-notice-scroll"),
        textWidth = $text.offsetWidth,
        textBoxWidth = ed.querySelector(".custom-notice-inner").offsetWidth,
        count = 0,
        timer = null;

    if (timer) {
        clearInterval(timer);
        $text.style.left = 0;
    };
    if ($text.innerText != "" && textWidth > textBoxWidth) {
        timer = setInterval(function() {
            count--;
            $text.style.left = count + "px";

            if (-count == textWidth) {
                $text.style.left = textBoxWidth + "px";
                count = textBoxWidth;
            };
        }, 50)
    } else if($text.innerText == "" || textWidth <= textBoxWidth) {
        clearInterval(this.timer);
        $text.style.left = 0;
    }
}

// 轮播图滚动
function bannerRoll(ed, i) {
    var effect = ed.getAttribute("effect"),
        loop = ed.getAttribute("loop");

        ed.querySelector(".swiper-container").className += " swiper" + i;
        ed.querySelector(".swiper-pagination").className += " swiper-pagination" + i;

    var mySwiper = new Swiper ('.swiper' + i + '', {
        direction: 'horizontal',
        autoplay: 2500,
        effect: effect,
        loop: loop,
        
        // 如果需要分页器
        pagination: '.swiper-pagination',
        pagination: ".swiper-pagination" + i
  }) 
}

