/*
 jQuery Bubble Popup v.2.3.1
 http://maxvergelli.wordpress.com/jquery-bubble-popup/

 Copyright (c) 2010 Max Vergelli

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
(function (a) {
    var initialized = false;
    a.fn.IsBubblePopupOpen = function () {
        var c = null;
        a(this).each(function (d, e) {
            var b = a(e).data("private_jquerybubblepopup_options");
            if (b != null && typeof b == "object" && !a.isArray(b) && !a.isEmptyObject(b) && b.privateVars != null && typeof b.privateVars == "object" && !a.isArray(b.privateVars) && !a.isEmptyObject(b.privateVars) && typeof b.privateVars.is_open != "undefined") {
                c = b.privateVars.is_open ? true : false
            }
            return false
        });
        return c
    };
    a.fn.GetBubblePopupLastDisplayDateTime = function () {
        var b = null;
        a(this).each(function (e, f) {
            var d = a(f).data("private_jquerybubblepopup_options");
            if (d != null && typeof d == "object" && !a.isArray(d) && !a.isEmptyObject(d) && d.privateVars != null && typeof d.privateVars == "object" && !a.isArray(d.privateVars) && !a.isEmptyObject(d.privateVars) && typeof d.privateVars.last_display_datetime != "undefined" && d.privateVars.last_display_datetime != null) {
                b = c(d.privateVars.last_display_datetime)
            }
            return false
        });

        function c(d) {
            return new Date(d * 1000)
        }

        return b
    };
    a.fn.GetBubblePopupLastModifiedDateTime = function () {
        var b = null;
        a(this).each(function (e, f) {
            var d = a(f).data("private_jquerybubblepopup_options");
            if (d != null && typeof d == "object" && !a.isArray(d) && !a.isEmptyObject(d) && d.privateVars != null && typeof d.privateVars == "object" && !a.isArray(d.privateVars) && !a.isEmptyObject(d.privateVars) && typeof d.privateVars.last_modified_datetime != "undefined" && d.privateVars.last_modified_datetime != null) {
                b = c(d.privateVars.last_modified_datetime)
            }
            return false
        });

        function c(d) {
            return new Date(d * 1000)
        }

        return b
    };
    a.fn.GetBubblePopupCreationDateTime = function () {
        var b = null;
        a(this).each(function (e, f) {
            var d = a(f).data("private_jquerybubblepopup_options");
            if (d != null && typeof d == "object" && !a.isArray(d) && !a.isEmptyObject(d) && d.privateVars != null && typeof d.privateVars == "object" && !a.isArray(d.privateVars) && !a.isEmptyObject(d.privateVars) && typeof d.privateVars.creation_datetime != "undefined" && d.privateVars.creation_datetime != null) {
                b = c(d.privateVars.creation_datetime)
            }
            return false
        });

        function c(d) {
            return new Date(d * 1000)
        }

        return b
    };
    a.fn.GetBubblePopupMarkup = function () {
        var b = null;
        a(this).each(function (d, e) {
            var c = a(e).data("private_jquerybubblepopup_options");
            if (c != null && typeof c == "object" && !a.isArray(c) && !a.isEmptyObject(c) && c.privateVars != null && typeof c.privateVars == "object" && !a.isArray(c.privateVars) && !a.isEmptyObject(c.privateVars) && typeof c.privateVars.id != "undefined") {
                b = a("#" + c.privateVars.id).length > 0 ? a("#" + c.privateVars.id).html() : null
            }
            return false
        });
        return b
    };
    a.fn.GetBubblePopupID = function () {
        var b = null;
        a(this).each(function (d, e) {
            var c = a(e).data("private_jquerybubblepopup_options");
            if (c != null && typeof c == "object" && !a.isArray(c) && !a.isEmptyObject(c) && c.privateVars != null && typeof c.privateVars == "object" && !a.isArray(c.privateVars) && !a.isEmptyObject(c.privateVars) && typeof c.privateVars.id != "undefined") {
                b = c.privateVars.id
            }
            return false
        });
        return b
    };
    a.fn.RemoveBubblePopup = function () {
        var b = 0;
        a(this).each(function (d, e) {
            var c = a(e).data("private_jquerybubblepopup_options");
            if (c != null && typeof c == "object" && !a.isArray(c) && !a.isEmptyObject(c) && c.privateVars != null && typeof c.privateVars == "object" && !a.isArray(c.privateVars) && !a.isEmptyObject(c.privateVars) && typeof c.privateVars.id != "undefined") {
                a(e).unbind("managebubblepopup");
                a(e).unbind("setbubblepopupinnerhtml");
                a(e).unbind("setbubblepopupoptions");
                a(e).unbind("positionbubblepopup");
                a(e).unbind("freezebubblepopup");
                a(e).unbind("unfreezebubblepopup");
                a(e).unbind("showbubblepopup");
                a(e).unbind("hidebubblepopup");
                a(e).data("private_jquerybubblepopup_options", {});
                if (a("#" + c.privateVars.id).length > 0) {
                    a("#" + c.privateVars.id).remove()
                }
                b++
            }
        });
        return b
    };
    a.fn.HasBubblePopup = function () {
        var c = false;
        a(this).each(function (d, e) {
            var b = a(e).data("private_jquerybubblepopup_options");
            if (b != null && typeof b == "object" && !a.isArray(b) && !a.isEmptyObject(b) && b.privateVars != null && typeof b.privateVars == "object" && !a.isArray(b.privateVars) && !a.isEmptyObject(b.privateVars) && typeof b.privateVars.id != "undefined") {
                c = true
            }
            return false
        });
        return c
    };
    a.fn.GetBubblePopupOptions = function () {
        var b = {};
        a(this).each(function (c, d) {
            b = a(d).data("private_jquerybubblepopup_options");
            if (b != null && typeof b == "object" && !a.isArray(b) && !a.isEmptyObject(b) && b.privateVars != null && typeof b.privateVars == "object" && !a.isArray(b.privateVars) && !a.isEmptyObject(b.privateVars)) {
                delete b.privateVars
            } else {
                b = null
            }
            return false
        });
        if (a.isEmptyObject(b)) {
            b = null
        }
        return b
    };
    a.fn.SetBubblePopupInnerHtml = function (b, c) {
        a(this).each(function (d, e) {
            if (typeof c != "boolean") {
                c = true
            }
            a(e).trigger("setbubblepopupinnerhtml", [b, c])
        })
    };
    a.fn.SetBubblePopupOptions = function (b) {
        a(this).each(function (c, d) {
            a(d).trigger("setbubblepopupoptions", [b])
        })
    };
    a.fn.ShowBubblePopup = function (b, c) {
        a(this).each(function (d, e) {
            a(e).trigger("showbubblepopup", [b, c, true]);
            return false
        })
    };
    a.fn.ShowAllBubblePopups = function (b, c) {
        a(this).each(function (d, e) {
            a(e).trigger("showbubblepopup", [b, c, true])
        })
    };
    a.fn.HideBubblePopup = function () {
        a(this).each(function (b, c) {
            a(c).trigger("hidebubblepopup", [true]);
            return false
        })
    };
    a.fn.HideAllBubblePopups = function () {
        a(this).each(function (b, c) {
            a(c).trigger("hidebubblepopup", [true])
        })
    };
    a.fn.FreezeBubblePopup = function () {
        a(this).each(function (b, c) {
            a(c).trigger("freezebubblepopup");
            return false
        })
    };
    a.fn.FreezeAllBubblePopups = function () {
        a(this).each(function (b, c) {
            a(c).trigger("freezebubblepopup")
        })
    };
    a.fn.UnfreezeBubblePopup = function () {
        a(this).each(function (b, c) {
            a(c).trigger("unfreezebubblepopup");
            return false
        })
    };
    a.fn.UnfreezeAllBubblePopups = function () {
        a(this).each(function (b, c) {
            a(c).trigger("unfreezebubblepopup")
        })
    };
    a.fn.CreateBubblePopup = function (selector, e) {
        var r = {
            /*me:this,*/
            cache:[],
            options_key:"private_jquerybubblepopup_options",
            model_tr:["top", "middle", "bottom"],
            model_td:["left", "middle", "right"],
            model_markup:'<div class="{BASE_CLASS} {TEMPLATE_CLASS}"{DIV_STYLE} id="{DIV_ID}"> 									<table{TABLE_STYLE}> 									<tbody> 									<tr> 										<td class="{BASE_CLASS}-top-left"{TOP-LEFT_STYLE}>{TOP-LEFT}</td> 										<td class="{BASE_CLASS}-top-middle"{TOP-MIDDLE_STYLE}>{TOP-MIDDLE}</td> 										<td class="{BASE_CLASS}-top-right"{TOP-RIGHT_STYLE}>{TOP-RIGHT}</td> 									</tr> 									<tr> 										<td class="{BASE_CLASS}-middle-left"{MIDDLE-LEFT_STYLE}>{MIDDLE-LEFT}</td> 										<td class="{BASE_CLASS}-innerHtml"{INNERHTML_STYLE}>{INNERHTML}</td> 										<td class="{BASE_CLASS}-middle-right"{MIDDLE-RIGHT_STYLE}>{MIDDLE-RIGHT}</td> 									</tr> 									<tr> 										<td class="{BASE_CLASS}-bottom-left"{BOTTOM-LEFT_STYLE}>{BOTTOM-LEFT}</td> 										<td class="{BASE_CLASS}-bottom-middle"{BOTTOM-MIDDLE_STYLE}>{BOTTOM-MIDDLE}</td> 										<td class="{BASE_CLASS}-bottom-right"{BOTTOM-RIGHT_STYLE}>{BOTTOM-RIGHT}</td> 									</tr> 									</tbody> 									</table> 									</div>',
            privateVars:{
                id:null,
                creation_datetime:null,
                last_modified_datetime:null,
                last_display_datetime:null,
                is_open:false,
                is_freezed:false,
                is_animating:false,
                is_animation_complete:false,
                is_mouse_over:false,
                is_position_changed:false,
                last_options:{}
            },
            position:"top",
            positionValues:["left", "top", "right", "bottom"],
            align:"center",
            alignValues:["left", "center", "right", "top", "middle", "bottom"],
            alignHorizontalValues:["left", "center", "right"],
            alignVerticalValues:["top", "middle", "bottom"],
            distance:"20px",
            width:null,
            height:null,
            divStyle:{},
            tableStyle:{},
            innerHtml:null,
            innerHtmlStyle:{},
            tail:{
                align:"center",
                hidden:false
            },
            dropShadow:true,
            alwaysVisible:true,
            selectable:false,
            manageMouseEvents:true,
            mouseMove:"show",
            mouseOverValues:["show", "hide"],
            mouseOut:"hide",
            mouseOutValues:["show", "hide"],
            openingSpeed:250,
            closingSpeed:250,
            openingDelay:0,
            closingDelay:0,
            baseClass:"jquerybubblepopup",
            themeName:"azure",
            themePath:"jquerybubblepopup-theme/",
            themeMargins:{
                total:"13px",
                difference:"10px"
            },
            afterShown:function () {
            },
            afterHidden:function () {
            },
            hideElementId:[]
        };
        if (initialized) {
            //only update by adding hooks for the new content divs
            a(selector).each(function (v, w) {
                var currentData = n(w);
                var skipInit = false;
                if(currentData && currentData.privateVars.id){
                    skipInit = true;
                }
                if(!skipInit){
                    /* Kuali customization */
                    //jQuery("body > div[data-for='" + w.id + "']").remove();
                    jQuery("form > div[data-for='" + w.id + "'], body > div[data-for='" + w.id + "']").remove();
                    /* end Kuali customization */
                    var u = g(e);
                    u.privateVars.creation_datetime = f();
                    u.privateVars.id = u.baseClass + "-" + u.privateVars.creation_datetime + "-" + v;
                    d(w, u);
                }
            });
        }
        else {
            initialized = true;
            h(e);
        }

        function g(v) {
            var w = {
                privateVars:{},
                width:r.width,
                height:r.height,
                divStyle:r.divStyle,
                tableStyle:r.tableStyle,
                position:r.position,
                align:r.align,
                distance:r.distance,
                openingSpeed:r.openingSpeed,
                closingSpeed:r.closingSpeed,
                openingDelay:r.openingDelay,
                closingDelay:r.closingDelay,
                mouseMove:r.mouseMove,
                mouseOut:r.mouseOut,
                tail:r.tail,
                innerHtml:r.innerHtml,
                innerHtmlStyle:r.innerHtmlStyle,
                baseClass:r.baseClass,
                themeName:r.themeName,
                themePath:r.themePath,
                themeMargins:r.themeMargins,
                dropShadow:r.dropShadow,
                manageMouseEvents:r.manageMouseEvents,
                alwaysVisible:r.alwaysVisible,
                selectable:r.selectable,
                afterShown:r.afterShown,
                afterHidden:r.afterHidden,
                hideElementId:r.hideElementId
            };
            var t = a.extend(false, w, (typeof v == "object" && !a.isArray(v) && !a.isEmptyObject(v) && v != null ? v : {}));
            t.privateVars.id = r.privateVars.id;
            t.privateVars.creation_datetime = r.privateVars.creation_datetime;
            t.privateVars.last_modified_datetime = r.privateVars.last_modified_datetime;
            t.privateVars.last_display_datetime = r.privateVars.last_display_datetime;
            t.privateVars.is_open = r.privateVars.is_open;
            t.privateVars.is_freezed = r.privateVars.is_freezed;
            t.privateVars.is_animating = r.privateVars.is_animating;
            t.privateVars.is_animation_complete = r.privateVars.is_animation_complete;
            t.privateVars.is_mouse_over = r.privateVars.is_mouse_over;
            t.privateVars.is_position_changed = r.privateVars.is_position_changed;
            t.privateVars.last_options = r.privateVars.last_options;
            t.width = (typeof t.width == "string" || typeof t.width == "number") && parseInt(t.width) > 0 ? parseInt(t.width) : r.width;
            t.height = (typeof t.height == "string" || typeof t.height == "number") && parseInt(t.height) > 0 ? parseInt(t.height) : r.height;
            t.divStyle = t.divStyle != null && typeof t.divStyle == "object" && !a.isArray(t.divStyle) && !a.isEmptyObject(t.divStyle) ? t.divStyle : r.divStyle;
            t.tableStyle = t.tableStyle != null && typeof t.tableStyle == "object" && !a.isArray(t.tableStyle) && !a.isEmptyObject(t.tableStyle) ? t.tableStyle : r.tableStyle;
            t.position = typeof t.position == "string" && o(t.position.toLowerCase(), r.positionValues) ? t.position.toLowerCase() : r.position;
            t.align = typeof t.align == "string" && o(t.align.toLowerCase(), r.alignValues) ? t.align.toLowerCase() : r.align;
            t.distance = (typeof t.distance == "string" || typeof t.distance == "number") && parseInt(t.distance) >= 0 ? parseInt(t.distance) : r.distance;
            t.openingSpeed = typeof t.openingSpeed == "number" && parseInt(t.openingSpeed) > 0 ? parseInt(t.openingSpeed) : r.openingSpeed;
            t.closingSpeed = typeof t.closingSpeed == "number" && parseInt(t.closingSpeed) > 0 ? parseInt(t.closingSpeed) : r.closingSpeed;
            t.openingDelay = typeof t.openingDelay == "number" && t.openingDelay >= 0 ? t.openingDelay : r.openingDelay;
            t.closingDelay = typeof t.closingDelay == "number" && t.closingDelay >= 0 ? t.closingDelay : r.closingDelay;
            t.mouseMove = typeof t.mouseMove == "string" && o(t.mouseMove.toLowerCase(), r.mouseOverValues) ? t.mouseMove.toLowerCase() : r.mouseMove;
            t.mouseOut = typeof t.mouseOut == "string" && o(t.mouseOut.toLowerCase(), r.mouseOutValues) ? t.mouseOut.toLowerCase() : r.mouseOut;
            t.tail = t.tail != null && typeof t.tail == "object" && !a.isArray(t.tail) && !a.isEmptyObject(t.tail) ? t.tail : r.tail;
            t.tail.align = typeof t.tail.align != "undefined" ? t.tail.align : r.tail.align;
            t.tail.hidden = typeof t.tail.hidden != "undefined" ? t.tail.hidden : r.tail.hidden;
            t.innerHtml = typeof t.innerHtml == "string" && t.innerHtml.length > 0 ? t.innerHtml : r.innerHtml;
            t.innerHtmlStyle = t.innerHtmlStyle != null && typeof t.innerHtmlStyle == "object" && !a.isArray(t.innerHtmlStyle) && !a.isEmptyObject(t.innerHtmlStyle) ? t.innerHtmlStyle : r.innerHtmlStyle;
            t.baseClass = j(typeof t.baseClass == "string" && t.baseClass.length > 0 ? t.baseClass : r.baseClass);
            t.themeName = typeof t.themeName == "string" && t.themeName.length > 0 ? a.trim(t.themeName) : r.themeName;
            t.themePath = typeof t.themePath == "string" && t.themePath.length > 0 ? a.trim(t.themePath) : r.themePath;
            t.themeMargins = t.themeMargins != null && typeof t.themeMargins == "object" && !a.isArray(t.themeMargins) && !a.isEmptyObject(t.themeMargins) && (typeof parseInt(t.themeMargins.total) == "number" && typeof parseInt(t.themeMargins.difference) == "number") ? t.themeMargins : r.themeMargins;
            t.dropShadow = typeof t.dropShadow == "boolean" && t.dropShadow == true ? true : false;
            t.manageMouseEvents = typeof t.manageMouseEvents == "boolean" && t.manageMouseEvents == true ? true : false;
            t.alwaysVisible = typeof t.alwaysVisible == "boolean" && t.alwaysVisible == true ? true : false;
            t.selectable = typeof t.selectable == "boolean" && t.selectable == true ? true : false;
            t.afterShown = typeof t.afterShown == "function" ? t.afterShown : r.afterShown;
            t.afterHidden = typeof t.afterHidden == "function" ? t.afterHidden : r.afterHidden;
            t.hideElementId = a.isArray(t.hideElementId) ? t.hideElementId : r.hideElementId;
            if (t.position == "left" || t.position == "right") {
                t.align = o(t.align, r.alignVerticalValues) ? t.align : "middle"
            } else {
                t.align = o(t.align, r.alignHorizontalValues) ? t.align : "center"
            }
            for (var u in t.tail) {
                switch (u) {
                    case "align":
                        t.tail.align = typeof t.tail.align == "string" && o(t.tail.align.toLowerCase(), r.alignValues) ? t.tail.align.toLowerCase() : r.tail.align;
                        if (t.position == "left" || t.position == "right") {
                            t.tail.align = o(t.tail.align, r.alignVerticalValues) ? t.tail.align : "middle"
                        } else {
                            t.tail.align = o(t.tail.align, r.alignHorizontalValues) ? t.tail.align : "center"
                        }
                        break;
                    case "hidden":
                        t.tail.hidden = t.tail.hidden == true ? true : false;
                        break
                }
            }
            return t
        }

        function l(t) {
            if (t == 0) {
                return 0
            }
            if (t > 0) {
                return -(Math.abs(t))
            } else {
                return Math.abs(t)
            }
        }

        function o(v, w) {
            var t = false;
            for (var u in w) {
                if (w[u] == v) {
                    t = true;
                    break
                }
            }
            return t
        }

        function k(t) {
            if (document.createElement) {
                for (var v = t.length - 1; v >= 0; v--) {
                    var u = document.createElement("img");
                    u.src = t[v];
                    if (a.inArray(t[v], r.cache) > -1) {
                        r.cache.push(t[v])
                    }
                }
            }
        }

        function b(t) {
            if (t.hideElementId && t.hideElementId.length > 0) {
                for (var u = 0; u < t.hideElementId.length; u++) {
                    var v = (t.hideElementId[u].charAt(0) != "#" ? "#" + t.hideElementId[u] : t.hideElementId[u]);
                    a(v).css({
                        visibility:"hidden"
                    })
                }
            }
        }

        function s(u) {
            if (u.hideElementId && u.hideElementId.length > 0) {
                for (var v = 0; v < u.hideElementId.length; v++) {
                    var x = (u.hideElementId[v].charAt(0) != "#" ? "#" + u.hideElementId[v] : u.hideElementId[v]);
                    a(x).css({
                        visibility:"visible"
                    });
                    var w = a(x).length;
                    for (var t = 0; t < w.length; t++) {
                        a(w[t]).css({
                            visibility:"visible"
                        })
                    }
                }
            }
        }

        function m(u) {
            var w = u.themePath;
            var t = u.themeName;
            var v = (w.substring(w.length - 1) == "/" || w.substring(w.length - 1) == "\\") ? w.substring(0, w.length - 1) + "/" + t + "/" : w + "/" + t + "/";
            return v + (u.dropShadow == true ? (a.browser.msie ? "ie/" : "") : "ie/")
        }

        function j(t) {
            var u = t.substring(0, 1) == "." ? t.substring(1, t.length) : t;
            return u
        }

        function q(u) {
            if (a("#" + u.privateVars.id).length > 0) {
                var t = "bottom-middle";
                switch (u.position) {
                    case "left":
                        t = "middle-right";
                        break;
                    case "top":
                        t = "bottom-middle";
                        break;
                    case "right":
                        t = "middle-left";
                        break;
                    case "bottom":
                        t = "top-middle";
                        break
                }
                if (o(u.tail.align, r.alignHorizontalValues)) {
                    a("#" + u.privateVars.id).find("td." + u.baseClass + "-" + t).css("text-align", u.tail.align)
                    /* kuali customization */
                    if (t == "top-middle") {
                        a("#" + u.privateVars.id).find("td." + u.baseClass + "-" + t + " img").css("margin-bottom","-4px");
                    }
                    /* end kuali customization */
                } else {
                    a("#" + u.privateVars.id).find("td." + u.baseClass + "-" + t).css("vertical-align", u.tail.align)
                }
            }
        }

        function p(v) {
            var H = r.model_markup;
            var F = m(v);
            var x = "";
            var G = "";
            var u = "";
            if (!v.tail.hidden) {
                switch (v.position) {
                    case "left":
                        G = "right";
                        u = "{MIDDLE-RIGHT}";
                        break;
                    case "top":
                        G = "bottom";
                        u = "{BOTTOM-MIDDLE}";
                        break;
                    case "right":
                        G = "left";
                        u = "{MIDDLE-LEFT}";
                        break;
                    case "bottom":
                        G = "top";
                        u = "{TOP-MIDDLE}";
                        break
                }
                x = '<img src="' + F + "tail-" + G + "." + (v.dropShadow == true ? (a.browser.msie ? "gif" : "png") : "gif") + '" alt="" class="' + v.baseClass + '-tail" />'
            }
            var t = r.model_tr;
            var z = r.model_td;
            var K, E, A, J;
            var B = "";
            var y = "";
            var D = new Array();
            for (E in t) {
                A = "";
                J = "";
                for (K in z) {
                    A = t[E] + "-" + z[K];
                    A = A.toUpperCase();
                    J = "{" + A + "_STYLE}";
                    A = "{" + A + "}";
                    if (A == u) {
                        H = H.replace(A, x);
                        B = ""
                    } else {
                        H = H.replace(A, "");
                        B = ""
                    }
                    if (t[E] + "-" + z[K] != "middle-middle") {
                        y = F + t[E] + "-" + z[K] + "." + (v.dropShadow == true ? (a.browser.msie ? "gif" : "png") : "gif");
                        D.push(y);
                        H = H.replace(J, ' style="' + B + "background-image:url(" + y + ');"')
                    }
                }
            }
            if (D.length > 0) {
                k(D)
            }
            var w = "";
            if (v.tableStyle != null && typeof v.tableStyle == "object" && !a.isArray(v.tableStyle) && !a.isEmptyObject(v.tableStyle)) {
                for (var C in v.tableStyle) {
                    w += C + ":" + v.tableStyle[C] + ";"
                }
            }
            w += (v.width != null || v.height != null) ? (v.width != null ? "width:" + v.width + "px;" : "") + (v.height != null ? "height:" + v.height + "px;" : "") : "";
            H = w.length > 0 ? H.replace("{TABLE_STYLE}", ' style="' + w + '"') : H.replace("{TABLE_STYLE}", "");
            var I = "";
            if (v.divStyle != null && typeof v.divStyle == "object" && !a.isArray(v.divStyle) && !a.isEmptyObject(v.divStyle)) {
                for (var C in v.divStyle) {
                    I += C + ":" + v.divStyle[C] + ";"
                }
            }
            H = I.length > 0 ? H.replace("{DIV_STYLE}", ' style="' + I + '"') : H.replace("{DIV_STYLE}", "");
            H = H.replace("{TEMPLATE_CLASS}", v.baseClass + "-" + v.themeName);
            H = v.privateVars.id != null ? H.replace("{DIV_ID}", v.privateVars.id) : H.replace("{DIV_ID}", "");
            while (H.indexOf("{BASE_CLASS}") > -1) {
                H = H.replace("{BASE_CLASS}", v.baseClass)
            }
            H = v.innerHtml != null ? H.replace("{INNERHTML}", v.innerHtml) : H.replace("{INNERHTML}", "");
            J = "";
            for (var C in v.innerHtmlStyle) {
                J += C + ":" + v.innerHtmlStyle[C] + ";"
            }
            H = J.length > 0 ? H.replace("{INNERHTML_STYLE}", ' style="' + J + '"') : H.replace("{INNERHTML_STYLE}", "");
            return H
        }

        function f() {
            return Math.round(new Date().getTime() / 1000)
        }

        function c(E, N, x) {
            var O = x.position;
            var K = x.align;
            var z = x.distance;
            var F = x.themeMargins;
            var I = new Array();
            var u = N.offset();
            var t = parseInt(u.top);
            var y = parseInt(u.left);
            var P = parseInt(N.outerWidth(false));
            var L = parseInt(N.outerHeight(false));
            var v = parseInt(E.outerWidth(false));
            var M = parseInt(E.outerHeight(false));
            F.difference = Math.abs(parseInt(F.difference));
            F.total = Math.abs(parseInt(F.total));
            var w = l(F.difference);
            var J = l(F.difference);
            var A = l(F.total);
            var H = m(x);
            switch (K) {
                case "left":
                    I.top = O == "top" ? t - M - z + l(w) : t + L + z + w;
                    I.left = y + A;
                    break;
                case "center":
                    var D = Math.abs(v - P) / 2;
                    I.top = O == "top" ? t - M - z + l(w) : t + L + z + w;
                    I.left = v >= P ? y - D : y + D;
                    break;
                case "right":
                    var D = Math.abs(v - P);
                    I.top = O == "top" ? t - M - z + l(w) : t + L + z + w;
                    I.left = v >= P ? y - D + l(A) : y + D + l(A);
                    break;
                case "top":
                    I.top = t + A;
                    I.left = O == "left" ? y - v - z + l(J) : y + P + z + J;
                    break;
                case "middle":
                    var D = Math.abs(M - L) / 2;
                    I.top = M >= L ? t - D : t + D;
                    I.left = O == "left" ? y - v - z + l(J) : y + P + z + J;
                    break;
                case "bottom":
                    var D = Math.abs(M - L);
                    I.top = M >= L ? t - D + l(A) : t + D + l(A);
                    I.left = O == "left" ? y - v - z + l(J) : y + P + z + J;
                    break
            }
            I.position = O;
            if (a("#" + x.privateVars.id).length > 0 && a("#" + x.privateVars.id).find("img." + x.baseClass + "-tail").length > 0) {
                a("#" + x.privateVars.id).find("img." + x.baseClass + "-tail").remove();
                var G = "bottom";
                var C = "bottom-middle";
                switch (O) {
                    case "left":
                        G = "right";
                        C = "middle-right";
                        break;
                    case "top":
                        G = "bottom";
                        C = "bottom-middle";
                        break;
                    case "right":
                        G = "left";
                        C = "middle-left";
                        break;
                    case "bottom":
                        G = "top";
                        C = "top-middle";
                        break
                }
                a("#" + x.privateVars.id).find("td." + x.baseClass + "-" + C).empty();
                a("#" + x.privateVars.id).find("td." + x.baseClass + "-" + C).html('<img src="' + H + "tail-" + G + "." + (x.dropShadow == true ? (a.browser.msie ? "gif" : "png") : "gif") + '" alt="" class="' + x.baseClass + '-tail" />');
                q(x)
            }
            if (x.alwaysVisible == true) {
                if (I.top < a(window).scrollTop() || I.top + M > a(window).scrollTop() + a(window).height()) {
                    if (a("#" + x.privateVars.id).length > 0 && a("#" + x.privateVars.id).find("img." + x.baseClass + "-tail").length > 0) {
                        a("#" + x.privateVars.id).find("img." + x.baseClass + "-tail").remove()
                    }
                    var B = "";
                    if (I.top < a(window).scrollTop()) {
                        I.position = "bottom";
                        I.top = t + L + z + w;
                        if (a("#" + x.privateVars.id).length > 0 && !x.tail.hidden) {
                            a("#" + x.privateVars.id).find("td." + x.baseClass + "-top-middle").empty();
                            a("#" + x.privateVars.id).find("td." + x.baseClass + "-top-middle").html('<img src="' + H + "tail-top." + (x.dropShadow == true ? (a.browser.msie ? "gif" : "png") : "gif") + '" alt="" class="' + x.baseClass + '-tail" />');
                            B = "top-middle"
                        }
                    } else {
                        if (I.top + M > a(window).scrollTop() + a(window).height()) {
                            I.position = "top";
                            I.top = t - M - z + l(w);
                            if (a("#" + x.privateVars.id).length > 0 && !x.tail.hidden) {
                                a("#" + x.privateVars.id).find("td." + x.baseClass + "-bottom-middle").empty();
                                a("#" + x.privateVars.id).find("td." + x.baseClass + "-bottom-middle").html('<img src="' + H + "tail-bottom." + (x.dropShadow == true ? (a.browser.msie ? "gif" : "png") : "gif") + '" alt="" class="' + x.baseClass + '-tail" />');
                                B = "bottom-middle"
                            }
                        }
                    }
                    if (I.left < 0) {
                        I.left = 0;
                        if (B.length > 0) {
                            a("#" + x.privateVars.id).find("td." + x.baseClass + "-" + B).css("text-align", "center")
                        }
                    } else {
                        if (I.left + v > a(window).width()) {
                            I.left = a(window).width() - v;
                            if (B.length > 0) {
                                a("#" + x.privateVars.id).find("td." + x.baseClass + "-" + B).css("text-align", "center")
                            }
                        }
                    }
                } else {
                    if (I.left < 0 || I.left + v > a(window).width()) {
                        if (a("#" + x.privateVars.id).length > 0 && a("#" + x.privateVars.id).find("img." + x.baseClass + "-tail").length > 0) {
                            a("#" + x.privateVars.id).find("img." + x.baseClass + "-tail").remove()
                        }
                        var B = "";
                        if (I.left < 0) {
                            I.position = "right";
                            I.left = y + P + z + J;
                            if (a("#" + x.privateVars.id).length > 0 && !x.tail.hidden) {
                                a("#" + x.privateVars.id).find("td." + x.baseClass + "-middle-left").empty();
                                a("#" + x.privateVars.id).find("td." + x.baseClass + "-middle-left").html('<img src="' + H + "tail-left." + (x.dropShadow == true ? (a.browser.msie ? "gif" : "png") : "gif") + '" alt="" class="' + x.baseClass + '-tail" />');
                                B = "middle-left"
                            }
                        } else {
                            if (I.left + v > a(window).width()) {
                                I.position = "left";
                                I.left = y - v - z + l(J);
                                if (a("#" + x.privateVars.id).length > 0 && !x.tail.hidden) {
                                    a("#" + x.privateVars.id).find("td." + x.baseClass + "-middle-right").empty();
                                    a("#" + x.privateVars.id).find("td." + x.baseClass + "-middle-right").html('<img src="' + H + "tail-right." + (x.dropShadow == true ? (a.browser.msie ? "gif" : "png") : "gif") + '" alt="" class="' + x.baseClass + '-tail" />');
                                    B = "middle-right"
                                }
                            }
                        }
                        if (I.top < a(window).scrollTop()) {
                            I.top = a(window).scrollTop();
                            if (B.length > 0) {
                                a("#" + x.privateVars.id).find("td." + x.baseClass + "-" + B).css("vertical-align", "middle")
                            }
                        } else {
                            if (I.top + M > a(window).scrollTop() + a(window).height()) {
                                I.top = (a(window).scrollTop() + a(window).height()) - M;
                                if (B.length > 0) {
                                    a("#" + x.privateVars.id).find("td." + x.baseClass + "-" + B).css("vertical-align", "middle")
                                }
                            }
                        }
                    }
                }
            }
            return I
        }

        function d(u, t) {
            a(u).data(r.options_key, t)
        }

        function n(t) {
            return a(t).data(r.options_key)
        }

        function i(t) {
            var u = t != null && typeof t == "object" && !a.isArray(t) && !a.isEmptyObject(t) ? true : false;
            return u
        }

        function h(t) {
            /*      Kuali customization below - we handle these ourselves:

             a(window).resize(function () {
             a(r.me).each(function (u, v) {
             a(v).trigger("positionbubblepopup")
             })
             });
             a(document).mousemove(function (u) {
             a(r.me).each(function (v, w) {
             a(w).trigger("managebubblepopup", [u.pageX, u.pageY])
             })
             });*/
            a(selector).each(function (v, w) {
                var u = g(t);
                u.privateVars.creation_datetime = f();
                u.privateVars.id = u.baseClass + "-" + u.privateVars.creation_datetime + "-" + v;
                d(w, u);
            });
            a(document).on("managebubblepopup", selector, function (y, C, B) {
                var N = n(this);
                if (i(N) && i(N.privateVars) && typeof C != "undefined" && typeof B != "undefined") {
                    if (N.manageMouseEvents) {
                        var E = a(this);
                        var z = E.offset();
                        var L = parseInt(z.top);
                        var H = parseInt(z.left);
                        var F = parseInt(E.outerWidth(false));
                        var K = parseInt(E.outerHeight(false));
                        var J = false;
                        if (H <= C && C <= F + H && L <= B && B <= K + L) {
                            J = true
                        } else {
                            J = false
                        }
                        if (J && !N.privateVars.is_mouse_over) {
                            N.privateVars.is_mouse_over = true;
                            d(this, N);
                            if (N.mouseMove == "show") {
                                a(this).trigger("showbubblepopup")
                            } else {
                                if (N.selectable && a("#" + N.privateVars.id).length > 0) {
                                    var x = a("#" + N.privateVars.id);
                                    var A = x.offset();
                                    var D = parseInt(A.top);
                                    var I = parseInt(A.left);
                                    var G = parseInt(x.outerWidth(false));
                                    var M = parseInt(x.outerHeight(false));
                                    if (I <= C && C <= G + I && D <= B && B <= M + D) {
                                    } else {
                                        a(this).trigger("hidebubblepopup")
                                    }
                                } else {
                                    a(this).trigger("hidebubblepopup")
                                }
                            }
                        } else {
                            if (!J && N.privateVars.is_mouse_over) {
                                N.privateVars.is_mouse_over = false;
                                d(this, N);
                                if (N.mouseOut == "show") {
                                    a(this).trigger("showbubblepopup")
                                } else {
                                    if (N.selectable && a("#" + N.privateVars.id).length > 0) {
                                        var x = a("#" + N.privateVars.id);
                                        var A = x.offset();
                                        var D = parseInt(A.top);
                                        var I = parseInt(A.left);
                                        var G = parseInt(x.outerWidth(false));
                                        var M = parseInt(x.outerHeight(false));
                                        if (I <= C && C <= G + I && D <= B && B <= M + D) {
                                        } else {
                                            a(this).trigger("hidebubblepopup")
                                        }
                                    } else {
                                        a(this).trigger("hidebubblepopup")
                                    }
                                }
                            } else {
                                if (!J && !N.privateVars.is_mouse_over) {
                                    if (N.selectable && a("#" + N.privateVars.id).length > 0 && !N.privateVars.is_animating) {
                                        var x = a("#" + N.privateVars.id);
                                        var A = x.offset();
                                        var D = parseInt(A.top);
                                        var I = parseInt(A.left);
                                        var G = parseInt(x.outerWidth(false));
                                        var M = parseInt(x.outerHeight(false));
                                        if (I <= C && C <= G + I && D <= B && B <= M + D) {
                                        } else {
                                            a(this).trigger("hidebubblepopup")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
            a(document).on("setbubblepopupinnerhtml", selector, function (A, x, z) {
                var y = n(this);
                if (i(y) && i(y.privateVars) && typeof x != "undefined") {
                    y.privateVars.last_modified_datetime = f();
                    if (typeof z == "boolean" && z == true) {
                        y.innerHtml = x
                    }
                    d(this, y);
                    if (a("#" + y.privateVars.id).length > 0) {
                        a("#" + y.privateVars.id).find("td." + y.baseClass + "-innerHtml").html(x);
                        if (y.privateVars.is_animation_complete) {
                            a(this).trigger("positionbubblepopup", [false])
                        } else {
                            a(this).trigger("positionbubblepopup", [true])
                        }
                    }
                }
            });
            a(document).on("setbubblepopupoptions", selector, function (A, z) {
                var x = n(this);
                if (i(x) && i(x.privateVars)) {
                    var y = x;
                    x = g(z);
                    x.privateVars.id = y.privateVars.id;
                    x.privateVars.creation_datetime = y.privateVars.creation_datetime;
                    x.privateVars.last_modified_datetime = f();
                    x.privateVars.last_display_datetime = y.privateVars.last_display_datetime;
                    x.privateVars.is_open = y.privateVars.is_open;
                    x.privateVars.is_freezed = y.privateVars.is_freezed;
                    x.privateVars.last_options = {};
                    d(this, x)
                }
            });
            a(document).on("positionbubblepopup", selector, function (A, y) {
                var z = n(this);
                if (i(z) && i(z.privateVars) && a("#" + z.privateVars.id).length > 0 && z.privateVars.is_open == true) {
                    var x = a("#" + z.privateVars.id);
                    var C = c(x, a(this), z);
                    var B = 2;
                    if (typeof y == "boolean" && y == true) {
                        x.css({
                            top:C.top,
                            left:C.left
                        })
                    } else {
                        switch (z.position) {
                            case "left":
                                x.css({
                                    top:C.top,
                                    left:(C.position != z.position ? C.left - (Math.abs(z.themeMargins.difference) * B) : C.left + (Math.abs(z.themeMargins.difference) * B))
                                });
                                break;
                            case "top":
                                x.css({
                                    top:(C.position != z.position ? C.top - (Math.abs(z.themeMargins.difference) * B) : C.top + (Math.abs(z.themeMargins.difference) * B)),
                                    left:C.left
                                });
                                break;
                            case "right":
                                x.css({
                                    top:C.top,
                                    left:(C.position != z.position ? C.left + (Math.abs(z.themeMargins.difference) * B) : C.left - (Math.abs(z.themeMargins.difference) * B))
                                });
                                break;
                            case "bottom":
                                x.css({
                                    top:(C.position != z.position ? C.top + (Math.abs(z.themeMargins.difference) * B) : C.top - (Math.abs(z.themeMargins.difference) * B)),
                                    left:C.left
                                });
                                break
                        }
                    }
                }
            });
            a(document).on("freezebubblepopup", selector, function () {
                var x = n(this);
                if (i(x) && i(x.privateVars)) {
                    x.privateVars.is_freezed = true;
                    d(this, x)
                }
            });
            a(document).on("unfreezebubblepopup", selector, function () {
                var x = n(this);
                if (i(x) && i(x.privateVars)) {
                    x.privateVars.is_freezed = false;
                    d(this, x)
                }
            });
            a(document).on("showbubblepopup", selector, function (x, A, D, G) {
                var H = n(this);
                var w = this;
                if ((typeof G == "boolean" && G == true && (i(H) && i(H.privateVars))) || (typeof G == "undefined" && (i(H) && i(H.privateVars) && !H.privateVars.is_freezed && !H.privateVars.is_open))) {
                    if (typeof G == "boolean" && G == true) {
                        a(this).trigger("unfreezebubblepopup")
                    }
                    H.privateVars.is_open = true;
                    H.privateVars.is_freezed = false;
                    H.privateVars.is_animating = false;
                    H.privateVars.is_animation_complete = false;
                    if (i(H.privateVars.last_options)) {
                        H = H.privateVars.last_options
                    } else {
                        H.privateVars.last_options = {}
                    }
                    if (i(A)) {
                        var C = H;
                        var F = f();
                        H = g(A);
                        H.privateVars.id = C.privateVars.id;
                        H.privateVars.creation_datetime = C.privateVars.creation_datetime;
                        H.privateVars.last_modified_datetime = F;
                        H.privateVars.last_display_datetime = F;
                        H.privateVars.is_open = true;
                        H.privateVars.is_freezed = false;
                        H.privateVars.is_animating = false;
                        H.privateVars.is_animation_complete = false;
                        H.privateVars.is_mouse_over = C.privateVars.is_mouse_over;
                        H.privateVars.is_position_changed = C.privateVars.is_position_changed;
                        H.privateVars.last_options = {};
                        if (typeof D == "boolean" && D == false) {
                            C.privateVars.last_modified_datetime = F;
                            C.privateVars.last_display_datetime = F;
                            H.privateVars.last_options = C
                        }
                    }
                    d(this, H);
                    b(H);
                    /* kuali customization */
/*                    if (a("#" + H.privateVars.id).length > 0) {
                        a("#" + H.privateVars.id).remove()
                    }
                    var y = {};
                    var B = p(H);
                    y = a(B);
                    y.appendTo("body");
                    y = a("#" + H.privateVars.id);*/
                    var y = a("#" + H.privateVars.id);
                    if (y.length == 0) {
                        var B = p(H);
                        y = a(B);
                        y.appendTo( (a('form').length > 0) ? "form" : "body" );
                    }
                    /* end kuali customization */
                    y.attr("data-for", w.id);
                    y.css({
                        opacity:0,
                        top:"0px",
                        left:"0px",
                        position:"absolute",
                        display:"block"
                    });
                    if (H.dropShadow == true) {
                        if (a.browser.msie && parseInt(a.browser.version) < 9) {
                            a("#" + H.privateVars.id + " table").addClass(H.baseClass + "-ie")
                        }
                    }
                    q(H);
                    var E = c(y, a(this), H);
                    y.css({
                        top:E.top,
                        left:E.left
                    });
                    if (E.position == H.position) {
                        H.privateVars.is_position_changed = false
                    } else {
                        H.privateVars.is_position_changed = true
                    }
                    d(this, H);
                    var z = setTimeout(function () {
                        H.privateVars.is_animating = true;
                        d(w, H);
                        y.stop();
                        switch (H.position) {
                            case "left":
                                y.animate({
                                    opacity:1,
                                    left:(H.privateVars.is_position_changed ? "-=" : "+=") + H.distance + "px"
                                }, H.openingSpeed, "swing", function () {
                                    H.privateVars.is_animating = false;
                                    H.privateVars.is_animation_complete = true;
                                    d(w, H);
                                    if (H.dropShadow == true) {
                                        if (a.browser.msie && parseInt(a.browser.version) > 8) {
                                            y.addClass(H.baseClass + "-ie")
                                        }
                                    }
                                    H.afterShown()
                                });
                                break;
                            case "top":
                                y.animate({
                                    opacity:1,
                                    top:(H.privateVars.is_position_changed ? "-=" : "+=") + H.distance + "px"
                                }, H.openingSpeed, "swing", function () {
                                    H.privateVars.is_animating = false;
                                    H.privateVars.is_animation_complete = true;
                                    d(w, H);
                                    if (H.dropShadow == true) {
                                        if (a.browser.msie && parseInt(a.browser.version) > 8) {
                                            y.addClass(H.baseClass + "-ie")
                                        }
                                    }
                                    H.afterShown()
                                });
                                break;
                            case "right":
                                y.animate({
                                    opacity:1,
                                    left:(H.privateVars.is_position_changed ? "+=" : "-=") + H.distance + "px"
                                }, H.openingSpeed, "swing", function () {
                                    H.privateVars.is_animating = false;
                                    H.privateVars.is_animation_complete = true;
                                    d(w, H);
                                    if (H.dropShadow == true) {
                                        if (a.browser.msie && parseInt(a.browser.version) > 8) {
                                            y.addClass(H.baseClass + "-ie")
                                        }
                                    }
                                    H.afterShown()
                                });
                                break;
                            case "bottom":
                                y.animate({
                                    opacity:1,
                                    top:(H.privateVars.is_position_changed ? "+=" : "-=") + H.distance + "px"
                                }, H.openingSpeed, "swing", function () {
                                    H.privateVars.is_animating = false;
                                    H.privateVars.is_animation_complete = true;
                                    d(w, H);
                                    if (H.dropShadow == true) {
                                        if (a.browser.msie && parseInt(a.browser.version) > 8) {
                                            y.addClass(H.baseClass + "-ie")
                                        }
                                    }
                                    H.afterShown()
                                });
                                break
                        }
                    }, H.openingDelay)
                }
            });
            /** Kuali customization begin **/
            a(document).on("hidebubblepopup", selector, function (B, x) {
                var w = this;
                var A = n(this);
                if ((typeof x == "boolean" && x == true && (i(A) && i(A.privateVars) && a("#" + A.privateVars.id).length > 0)) || (typeof x == "undefined" && (i(A) && i(A.privateVars) && a("#" + A.privateVars.id).length > 0 && !A.privateVars.is_freezed && A.privateVars.is_open))) {
                    if (typeof x == "boolean" && x == true) {
                        a(this).trigger("unfreezebubblepopup")
                    }
                    A.privateVars.is_open = false;
                    A.privateVars.is_freezed = false;
                    A.privateVars.is_animating = false;
                    A.privateVars.is_animation_complete = false;
                    d(this, A);
                    var y = a("#" + A.privateVars.id);
                    var z = typeof x == "undefined" ? A.closingDelay : 0;
                    var C = setTimeout(function () {
                        A.privateVars.is_animating = true;
                        d(w, A);
                        y.stop();
                        if (A.dropShadow == true) {
                            if (a.browser.msie && parseInt(a.browser.version) > 8) {
                                y.removeClass(A.baseClass + "-ie")
                            }
                        }
                        switch (A.position) {
                            case "left":
                                y.animate({
                                    opacity:0,
                                    left:(A.privateVars.is_position_changed ? "+=" : "-=") + A.distance + "px"
                                }, A.closingSpeed, "swing", function () {

                                    A.privateVars.is_animating = false;
                                    A.privateVars.is_animation_complete = true;
                                    d(w, A);
                                    y.css("display", "none");
                                    A.afterHidden()
                                });
                                break;
                            case "top":
                                y.animate({
                                    opacity:0,
                                    top:(A.privateVars.is_position_changed ? "+=" : "-=") + A.distance + "px"
                                }, A.closingSpeed, "swing", function () {
                                    A.privateVars.is_animating = false;
                                    A.privateVars.is_animation_complete = true;
                                    d(w, A);
                                    y.css("display", "none");
                                    A.afterHidden()
                                });
                                break;
                            case "right":
                                y.animate({
                                    opacity:0,
                                    left:(A.privateVars.is_position_changed ? "-=" : "+=") + A.distance + "px"
                                }, A.closingSpeed, "swing", function () {
                                    A.privateVars.is_animating = false;
                                    A.privateVars.is_animation_complete = true;
                                    d(w, A);
                                    y.css("display", "none");
                                    A.afterHidden()
                                });
                                break;
                            case "bottom":
                                y.animate({
                                    opacity:0,
                                    top:(A.privateVars.is_position_changed ? "-=" : "+=") + A.distance + "px"
                                }, A.closingSpeed, "swing", function () {
                                    A.privateVars.is_animating = false;
                                    A.privateVars.is_animation_complete = true;
                                    d(w, A);
                                    y.css("display", "none");
                                    A.afterHidden()
                                });
                                break
                        }
                    }, z);
                    A.privateVars.last_display_datetime = f();

                    d(this, A);
                    s(A)
                }
            });
            /** Kuali customization end **/
        }

        return this;
    }
})(jQuery);