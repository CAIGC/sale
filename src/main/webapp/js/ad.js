/* 
* @Author: Ling yusu
* @Date:   2016-07-08 09:21:18
* @Last Modified by:   Ling yusu
* @Last Modified time: 2016-07-08 17:33:46
*/
(function(exports) {
  // 默认配置
  var defaults = {
    popAni: "slideDown", //弹窗弹出方式(默认为slideDown)，fadeIn居中淡入，slideUp从底部出来并固定在底部，slideDown从顶部下拉至中间
           adLink: "javascript:;", // 广告链接
           adImgsrc: "", // 广告图片路径
           adName: "", // 广告名称
           state: true, // 状态：禁用(false)或启用(true)
           startTime: null, // 有效期开始时间
           endTime: null, // 有效期结束时间
           popTimeInterval: null, //popBtn为null时自动弹出的时间间隔，null为每次进来都弹，day为每天，week为每星期，month为月，year为年
           popCallback: null, //弹出后回调
           closeBtn: ".close-ad", //关闭modal的按钮类名
           closeCallback: null //关闭按钮回调
      },
     // 打开弹窗方法
     popModal = function(modal, bkg) {
          modal.addClass('modal-pop');
          bkg && bkg.show();
     },
      // 关闭弹窗方法
      closeModal = function(modal, bkg) {
          modal.removeClass('modal-pop').remove();
          bkg && bkg.hide();
      };

     function Modal(opt) {
          this.$modal = null;
          this.config = $.extend(defaults, opt);
          this.init();
     }

      exports.Modal = Modal;

      Modal.prototype = {
        constructor: Modal,
        init: function() {
          var that = this,
               config = that.config;

               that.expiryDate(); // 有效期
               that.$modal && that.autoPop(config.popTimeInterval); //自动弹窗
               (that.$modal && config.closeBtn) && that.closeModalEve(); // 关闭弹窗
           },
           // 有效期内弹出广告
           expiryDate: function() {
                var config = this.config,
                     date = new Date().getTime(),
                     startTime = new Date(config.startTime).getTime(),
                     endTime = new Date(config.endTime).getTime();

                startTime = startTime == 0 ? date : startTime;
                endTime = endTime == 0 ? (date + 10000) : endTime;

                if (date >= startTime && date <= endTime) {
                    this.createAd(); //生成广告
                } else {
                  return false;
                }
           },
          // 根据广告状态判断是否要生成广告
           createAd: function() {
            var that = this, 
              config = that.config;

            if (config.state == true) {
              var str = '<div id="richMedia" class="float-ad"><a href="' + config.adLink + '"><img src="' + config.adImgsrc + '" alt="' + config.adName + '" /></a><em class="close-ad"></em></div>';
              $("body").append(str);

              that.$modal = $("#richMedia");
                  that.popAniSet(); //初始化弹窗显示方式
            } else {
              return false;
            }
           },
           // 弹窗显示方式
           popAniSet: function() {
               var that = this;
               that.$modal.addClass(that.config.popAni);
          },
          // 一进入页面时自动弹窗
          autoPop: function(time) {
               var that = this,
                    config = that.config,
                    date = new Date(),
                    timeInterval = null,
                    lsTimeInterval = window.localStorage.getItem("autoPoptimeInterval");

                switch(time) {
                     case "day":
                          timeInterval = date.getDate();
                          break;
                     case "week":
                          timeInterval = date.getDay();
                          break;
                     default:
                          timeInterval = null;
                }

                $(function() {
                     if (timeInterval && lsTimeInterval != timeInterval) {
                          popModal(that.$modal);
                          config.popCallback && config.popCallback();
                          window.localStorage.setItem("autoPoptimeInterval", timeInterval);
                     } else if(!timeInterval) {
                          popModal(that.$modal);
                          config.popCallback && config.popCallback();
                     }
                })
          },
          // 关闭弹窗
        closeModalEve: function() {
          var that = this,
                config = that.config;
          
          that.$modal.on('click', config.closeBtn, function(e) {
                config.closeCallback && config.closeCallback(e);
                closeModal(that.$modal);
          });
        }
      }
})(window);