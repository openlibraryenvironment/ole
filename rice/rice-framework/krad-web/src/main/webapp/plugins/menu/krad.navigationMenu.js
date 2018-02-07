/*
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function ($) {
    $.fn.selectMenuItem = function (options) {
        return this.each(function () {
            options = options || {};
            //default setting
            options = $.extend({
                selectPage:"",
                resetPageMargin: false
            }, options);

            //if(options.resetPageMargin)

            if (options.selectPage) {
                var old = $(this).find("li.uif-navigationItem-current");
                if(old.length){
                    old.removeClass("uif-navigationItem-current");
                }

                var current = $(this).find("a[name='" + options.selectPage + "']");
                if (current.length) {
                    current.parent().addClass("uif-navigationItem-current");
                }
            }
        });
    }

    $.fn.navMenu = function (options) {
        return this.each(function () {
            options = options || {};
            //default setting
            options = $.extend({
                defaultSelectFirst:true,
                currentPage:"",
                animate:false,
                slideout:true
            }, options);

            //element id strings
            var id = $(this).parent().attr('id');
            var list_elements = "#" + id + " li";
            var link_elements = list_elements + " a";

            //Styling
            $(this).parent().addClass("uif-navigation");

            //Plain menu
            $("li", this).addClass("uif-navigationItem");
            $(this).addClass("uif-navigationMenu");
            $(this).wrap("<div class='uif-navigationMenu-wrapper'/>");

            var menuWidth = $(".uif-navigation").outerWidth(true);

            if (options.slideout) {
                $(this).before("<a id='uif-collapseLink' class='uif-collapseLink' "
                        + "alt='Close Navigation'>Collapse Navigation</a>");
                $(".uif-navigationMenu-wrapper").before("<a id='uif-collapseSmall' "
                        + "style='position: absolute; left:" + menuWidth + "px; "
                        + "margin-right: 5px; margin-left: 5px; margin-top:-2px;'"
                        + "class='uif-collapseLink' alt='Close Navigation'><<</a>");
            }

            if (options.defaultSelectFirst && !options.currentPage) {
                $(link_elements).first().parent().addClass("uif-navigationItem-current");
            }

            if (options.currentPage) {
                var current = $(this).find("a[name='" + options.currentPage + "']");
                if (current) {
                    current.parent().addClass("uif-navigationItem-current");
                }
            }

            //automatic width calculations for page margin
            var slideIconWidth = $("#uif-collapseSmall").outerWidth(true);
            var currentPageMarginLeft = $(".uif-pageContentWrapper").css("margin-left");
            var pageMarginLeft;
            var collapsedPageMargin;
            if(currentPageMarginLeft && currentPageMarginLeft != "auto"){
                pageMarginLeft = (menuWidth + slideIconWidth + parseInt(currentPageMarginLeft)) + "px";
                collapsedPageMargin = (slideIconWidth + parseInt(currentPageMarginLeft)) + "px";
            }
            else{
                pageMarginLeft = (menuWidth + slideIconWidth) + "px";
                collapsedPageMargin = (slideIconWidth) + "px";
            }

            $(".uif-pageContentWrapper").css("margin-left", pageMarginLeft);

            //Handlers and animation
            $(document).ready(function () {
                $(link_elements).each(function (i) {
                    $(this).click(
                            function () {
                                $("uif-navigationMenu li.uif-navigationItem-current").removeClass(
                                        "uif-navigationItem-current");
                                $(this).addClass("uif-navigationItem-current");
                            });
                });

                if (options.slideout) {
                    //Slideout animation
                    var inProcess = false;


                    $(".uif-collapseLink").click(function () {
                        if ($(".uif-navigationMenu-wrapper").is(":visible") && !inProcess) {
                            inProcess = true;


                            $(".uif-navigationMenu-wrapper").hide("slide", {direction:"left"}, 1000);
                            $("#uif-collapseSmall").animate({left:"0px"}, 1000);
                            $(".uif-pageContentWrapper").animate({marginLeft: collapsedPageMargin}, 1000, function () {
                                inProcess = false;
                                $("#uif-collapseSmall").html(">>");
                            });
                        }
                        else if (!inProcess) {
                            inProcess = true;

                            $(".uif-navigationMenu-wrapper").show("slide", {direction:"left"}, 1000);
                            $("#uif-collapseSmall").animate({left: menuWidth + "px"}, 1000);
                            $(".uif-pageContentWrapper").animate({marginLeft: pageMarginLeft}, 1000, function () {
                                inProcess = false;
                                $("#uif-collapseSmall").html("<<");
                            });
                        }
                    });
                }
            });
        });
    }
})(jQuery);