/*
 * Copyright 2005-2014 The Kuali Foundation
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
/**
 * Collection of functions related to updating the request URL and refreshing for page
 * changes and back button support
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

/**
 * Invoked on document load to check URL indicators for refreshing and also sets up URL change handlers
 *
 * <p>
 * First a check is done to see if the URL hash contains the cache key. If it does, this indicates we need
 * to make a call to the server to retrieve the latest state for the view. This is the hack for browsers that
 * don't support the history API to prevent caching.
 *
 * If the cache key is not present, we then go forward and register handlers for the window popstate and
 * hashchange so we can update the page contents when the back button is used.
 *
 * Finally, we do a check to see if the URL hash contains the page id parm and it is different from the
 * page that is actually loaded in the DOM (which could happen for a bookmarked URL). If so an Ajax call is
 * made to retrieve the requested page
 * </p>
 *
 * @return boolean indicating whether a refresh call was made
 */
function handlePageAndCacheRefreshing() {
    // check if we have a cache key on the hash which indicates we need to reload the entire view from the
    // the entire view from the server
    var cacheKeyHashVal = getUrlParameter(kradVariables.CACHE_KEY, window.location.hash);
    if (cacheKeyHashVal) {
        // remove hash key so it is not present on refresh
        var hash = getRemovedUrlHashKey(kradVariables.CACHE_KEY);

        var refreshQueryString;

        // pick up form key for refresh call so we get the latest state
        var formKeyField = jQuery("#" + kradVariables.FORM_KEY);
        if (formKeyField.length && formKeyField.val()) {
            refreshQueryString = getUrlQueryString(kradVariables.FORM_KEY, formKeyField.val(), refreshQueryString);
        }

        var pageIdHashVal = getUrlParameter(kradVariables.PAGE_ID, window.location.hash);
        if (pageIdHashVal) {
            refreshQueryString = getUrlQueryString(kradVariables.PAGE_ID, pageIdHashVal, refreshQueryString);
        }

        window.location.replace("?" + refreshQueryString + hash);

        return true;
    }

    // check whether the view is multi-page, if not we don't need to setup page URL handling
    var singlePageView = (jQuery('#' + kradVariables.SINGLE_PAGE_VIEW).val() == "true");
    if (singlePageView) {
        return false;
    }

    // is a lightbox being closed by the back button
    var lightboxBackClose = false;

    // handlers for lightbox events to close lightbox on back by adding a history entry on load and removing it on close
    jQuery(document).bind("afterLoad", function(e){
        if(history.replaceState){
            history.pushState({lightbox: true}, null, window.location + "&" + kradVariables.LIGHTBOX_PARAM + "=true");
        }
        else{
            window.location.hash = window.location.hash + "&" + kradVariables.LIGHTBOX_PARAM + "=true"
        }
    });

    // TODO the back button only works correctly after a lightbox close on firefox because of other browser problems
    jQuery(document).bind("afterClose", function(e){
        // force a history back on close (if back button wasn't clicked to close)
        if(!lightboxBackClose && history.replaceState &&  window.location &&
                (window.location).toString().indexOf(kradVariables.LIGHTBOX_PARAM + "=true")){
            // TODO webkit incorrectly still stores the history of iframes after removal, back will cause bad behavior
            if (!jQuery.browser.webkit){
                history.back();
            }
        }
        else if(!lightboxBackClose && window.location.hash &&
                (window.location.hash).toString().indexOf("lightbox=true")){
            // TODO just removing attribute because IE has a bug with erasing the hash history so back cant be
            // used as we would like it to
            var hash = window.location.hash.toString();
            hash = hash.replace("&" + kradVariables.LIGHTBOX_PARAM + "=true", "");
            hash = hash.replace(kradVariables.LIGHTBOX_PARAM + "=true&", "");
            window.location.replace(hash);
        }
        lightboxBackClose = false;
    });

    // setup handlers for page id changes
    // add listener for popstate to change pages
    jQuery(window).bind("popstate", function (e) {
        var state = e.state;
        if(!state){
            state = e.originalEvent.state;
        }

        if (state && state.pageId && state.pageId != getCurrentPageId()) {
            navigateToPage(state.pageId);
        }

        if ((!state || (state && state.pageId == getCurrentPageId())) && jQuery.fancybox.isOpen){
            lightboxBackClose = true;
            jQuery.fancybox.close();
        }
    });

    // add listener on hashchange to change pages when pageId hash changes
    jQuery(window).bind("hashchange", function (e) {
        var pageId = null;
        if (window.location.hash) {
            pageId = getUrlParameter(kradVariables.PAGE_ID, window.location.hash);
        }

        var inLightbox = getUrlParameter(kradVariables.LIGHTBOX_PARAM, window.location.hash);

        if (pageId && (pageId != getCurrentPageId())) {
            navigateToPage(pageId);
        }

        if (!inLightbox && jQuery.fancybox.isOpen){
            lightboxBackClose = true;
            jQuery.fancybox.close();
        }
    });

    // check if page id on hash is different from the page we actually have, if so
    // retrieve the correct page
    var hashPageId = getUrlParameter(kradVariables.PAGE_ID, window.location.hash);

    var pageRedirect = hashPageId && (getCurrentPageId() != hashPageId);
    if (pageRedirect) {
        navigateToPage(hashPageId);
    }

    return pageRedirect;
}

/**
 * Invoked on page setup to update the request URL to reflect the current page
 *
 * <p>
 * To support the use of back button for page changes and bookmarking pages the request is updated
 * to include the id for the current page. If the HTML 5 history API is supported the page id is added
 * as a request parameter, otherwise we fall back to using the URL hash.
 *
 * The URL is also updated to include the form key so the latest changes are pulled when the user
 * goes back, and a cache key to bust browser caching
 * </p>
 *
 * @param pageId id for the page currently loaded
 */
function updateRequestUrl(pageId) {

    var formKeyField = jQuery("#" + kradVariables.FORM_KEY);

    // generate unique cache key (only has to be unique with a given form key)
    var disableCache = (jQuery('#' + kradVariables.DISABLE_BROWSER_CACHE).val() == "true");
    if (disableCache) {
        var cacheKey = generateQuickGuid();
    }

    // check for single page views in which case we don't need to update URL with page id
    var singlePageView = (jQuery('#' + kradVariables.SINGLE_PAGE_VIEW).val() == "true");

    if (!disableCache && singlePageView) {
        // no URL updates needed
        return;
    }

    // with HTML 5 history API we can update URL parameters, otherwise fall back to using the hash
    if (history.replaceState) {
        var queryString = "";

        if (formKeyField.length && formKeyField.val()) {
            queryString = getUrlQueryString(kradVariables.FORM_KEY, formKeyField.val());
        }

        // add cache busting key
        if (disableCache) {
            queryString = getUrlQueryString(kradVariables.CACHE_KEY, cacheKey, queryString);
        }

        // update page parameter in URL for multi-page views
        if (!singlePageView) {
            queryString = getUrlQueryString(kradVariables.PAGE_ID, pageId, queryString);
        }

        var updatedPageUrl = "?" + queryString + window.location.hash;

        if (!(getContext().fancybox.isOpen)){
            updatedPageUrl = updatedPageUrl.replace("&" + kradVariables.LIGHTBOX_PARAM + "=true", "");
        }

        var urlPageId = getUrlParameter(kradVariables.PAGE_ID);
        if (urlPageId && pageId && (urlPageId != pageId)) {
            // push state if new page
            history.pushState({pageId: pageId}, null, updatedPageUrl);
        }
        else {
            // otherwise replace state
            history.replaceState({pageId: pageId}, null, updatedPageUrl);
        }
    }
    else {
        // add keys hash if no HTML5 history support (old IE support)
        var hash = "";

        if (!singlePageView) {
            hash = getAppendedUrlHashKey(kradVariables.PAGE_ID, pageId);
        }

        if (formKeyField.length && formKeyField.val()) {
            hash = getAppendedUrlHashKey(kradVariables.FORM_KEY, formKeyField.val(), hash);
        }

        if (disableCache) {
            hash = getAppendedUrlHashKey(kradVariables.CACHE_KEY, cacheKey, hash);
        }

        // if the hash is empty or already contains the page id we want to replace
        // the current history item (not add to it)
        var replaceHistory = false;
        if (!window.location.hash || (pageId == getUrlParameter(kradVariables.PAGE_ID, window.location.hash))) {
            replaceHistory = true;
        }

        // if we are just updating the URL use location.replace which will not add an item
        // to the history, otherwise we will reset the hash which will add an item history (and allow
        // the user to go back to the previous URL with the back button)
        if (replaceHistory) {
            window.location.replace(hash);
        }
        else {
            window.location.hash = hash;
        }
    }
}

/**
 * Search for a parameter by name in the window's url.  If a searchString is provided, look for the parameter there
 * instead.
 *
 * @param paramName the parameter by name we are looking for
 * @param searchString(optional) if provided, search for the parameter in this string
 * of variables (if ? exists in string, it will search after the ? character)
 * @return {*}
 */
function getUrlParameter(paramName, searchString) {
    if (searchString == undefined) {
        searchString = window.location.search.substring(1);
    }

    if (!searchString) {
        return null;
    }

    if (searchString.indexOf('?') > -1) {
        searchString = searchString.substring(searchString.indexOf('?') + 1);
    }

    var params = searchString.split("&");

    for (var i = 0; i < params.length; i++) {
        var val = params[i].split("=");

        if (val[0] == paramName) {
            return decodeURIComponent(val[1]);
        }
    }

    return null;
}

/**
 * Get the URL query string to append to the current location.  The id and value specified will be the
 * an appendage (or replacement, if the value exists) for the current value in the url.
 *
 * @param appendageId id of the value to append/replace
 * @param appendageValue value to append
 * @return {String} the new query string value to append via history pushState/replaceState
 */
function getUrlQueryString(appendageId, appendageValue, searchString) {
    if (searchString == undefined) {
        searchString = window.location.search.substring(1);
    }

    // if parameters are blank return back current query string
    if (!appendageId || !appendageValue) {
        return searchString;
    }

    //if the query string has nothing, append and return (should never happen in KRAD views in current implementation)
    if (!searchString) {
        return appendageId + "=" + appendageValue;
    }

    //if the current query parameters do not contain the id, just append and return
    if (searchString && searchString.indexOf(appendageId) == -1) {
        return searchString + "&" + appendageId + "=" + appendageValue;
    }

    //id already exists so replace it
    var params = searchString.split("&");
    var queryString = "";
    for (var i = 0; i < params.length; i++) {
        var val = params[i].split("=");

        //skip the param we are replacing
        if (val[0] == appendageId) {
            continue;
        }

        if (queryString.length) {
            queryString = queryString + "&" + val[0] + "=" + val[1];
        }
        else {
            queryString = val[0] + "=" + val[1];
        }
    }

    //append
    if (queryString.length) {
        queryString = queryString + "&" + appendageId + "=" + appendageValue;
    }
    else {
        queryString = appendageId + "=" + appendageValue;
    }

    return queryString;
}

/**
 * Appends the given key and value to the URL hash (or replaces the key if it already exists)
 * and returns the updated hash string
 *
 * @param key key for the hash param to append
 * @param value value for the hash param to append
 * @param hashString (optional) the hash string to append to, if not given the hash from the
 * current URL is pulled
 * @return {string} updated URL hash string
 */
function getAppendedUrlHashKey(key, value, hashString) {
    if (hashString == undefined) {
        hashString = window.location.hash;
    }

    hashString = stripHashDelimiter(hashString);

    return "#?" + getUrlQueryString(key, value, hashString);
}

/**
 * Removes the parameter with the given key from the URL hash string and returns the
 * update string
 *
 * @param key key for the hash param to remove
 * @param hashString (optional) hash string to remove param from, if not given the hash from
 * the current URL is pulled
 * @return {string} updated URL hash string
 */
function getRemovedUrlHashKey(key, hashString) {
    if (hashString == undefined) {
        hashString = window.location.hash;
    }

    if (!hashString) {
        return;
    }

    var newHash = "";

    hashString = stripHashDelimiter(hashString);

    var params = hashString.split("&");
    for (i = 0; i < params.length; i++) {
        var parm = params[i].split("=");

        if (parm[0] == key) {
            continue;
        }

        if (newHash.length) {
            newHash = newHash + "&" + parm[0] + "=" + parm[1];
        }
        else {
            newHash = parm[0] + "=" + parm[1];
        }
    }

    return "#?" + newHash;
}

/**
 * Strips off the hash delimiter '#?' from the given string
 *
 * @param hashString string to strip hash delimiter from
 * @return {*} updated string
 */
function stripHashDelimiter(hashString) {
    if ((hashString.length > 0) && (hashString.substring(0, 1) == '#')) {
        hashString = hashString.substring(1);
    }

    if ((hashString.length > 0) && (hashString.substring(0, 1) == '?')) {
        hashString = hashString.substring(1);
    }

    return hashString;
}