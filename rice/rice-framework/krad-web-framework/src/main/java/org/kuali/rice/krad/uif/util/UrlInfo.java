/**
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
package org.kuali.rice.krad.uif.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.util.KRADUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This object represents a url in the Krad framework.  The url can be set explicitly to a specific href or a
 * controller
 * plus a viewId can be provided (at very minimum).  By default, the krad base bean config points the baseUrl property
 * to 'krad.url' configuration property and the methodToCall to 'start', but these can be reset to any value as needed.
 *
 * <p>
 * If href is not set, the generated value of href is constructed (in general) as follows:<br/>
 * baseUrl + /controllerMapping + ? + methodToCall param + viewId param + other parameters
 * <br/>
 * with any necessary tokens to construct a valid url.  If baseUrl is not provided, the url is not valid and a
 * blank string is returned.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "url-bean", parent = "Uif-Url")
public class UrlInfo extends UifDictionaryBeanBase implements Serializable {

    private static final long serialVersionUID = 3195177614468120958L;

    private String href;
    private String originalHref;
    private String baseUrl;
    private String controllerMapping;
    private String viewType;
    private String viewId;
    private String pageId;
    private String formKey;
    private String methodToCall;
    private Map<String, String> requestParameters;

    /**
     * Base constructor
     */
    public UrlInfo() {}

    /**
     * Constructor that initializes an href value
     *
     * @param href the href value
     */
    public UrlInfo(String href) {
        this.href = href;
        this.originalHref = href;
    }

    /**
     * Constructor that sets the base url construction properties
     *
     * @param baseUrl the baseUrl
     * @param controllerMapping the controllerMapping
     * @param viewId the id of the view
     * @param methodToCall the methodToCall
     */
    public UrlInfo(String baseUrl, String controllerMapping, String viewId, String methodToCall) {
        this.baseUrl = baseUrl;
        this.controllerMapping = controllerMapping;
        this.viewId = viewId;
        this.methodToCall = methodToCall;
    }

    /**
     * Generate the url based on properties of this object
     *
     * @return the generatedUrl, blank if not a valid url (no baseUrl value provided)
     */
    protected String generateUrl() {
        String generatedUrl = "";

        if (StringUtils.isBlank(baseUrl)) {
            return generatedUrl;
        }

        generatedUrl = baseUrl;

        if (StringUtils.isNotBlank(controllerMapping) && !controllerMapping.startsWith("/")) {
            generatedUrl = generatedUrl + "/" + controllerMapping;
        } else if (StringUtils.isNotBlank(controllerMapping)) {
            generatedUrl = generatedUrl + controllerMapping;
        }

        Map<String, String> allRequestParameters = new HashMap<String, String>();

        if (StringUtils.isNotBlank(methodToCall)) {
            allRequestParameters.put(UifConstants.CONTROLLER_METHOD_DISPATCH_PARAMETER_NAME, methodToCall);
        }

        if (StringUtils.isNotBlank(viewId)) {
            allRequestParameters.put(UifConstants.UrlParams.VIEW_ID, viewId);
        }

        if (StringUtils.isNotBlank(pageId)) {
            allRequestParameters.put(UifConstants.UrlParams.PAGE_ID, pageId);
        }

        if (StringUtils.isNotBlank(formKey)) {
            allRequestParameters.put(UifConstants.UrlParams.FORM_KEY, formKey);
        }

        if (requestParameters != null) {
            allRequestParameters.putAll(requestParameters);
        }

        //add the request parameters
        generatedUrl = generatedUrl + KRADUtils.getRequestStringFromMap(allRequestParameters);

        return generatedUrl;
    }

    /**
     * Get the href value for this url object.  This is the main call to this url object as it provides the full href
     * value represented by this object.
     *
     * <p>
     * If href has NOT been explicitly set to a value, the href is generated by
     * constructing pieces of the url set through the properties of this url object.
     * The generated value of href is constructed (in general) as follows:<br/>
     * baseUrl + /controllerMapping + ? + methodToCall param + viewId param + other parameters
     * <br/>
     * with any necessary tokens to construct a valid url.  If baseUrl is not provided, the url is not valid and a
     * blank string is returned.
     * </p>
     *
     * @return THE href represented by this url object, or blank if not valid
     */
    @BeanTagAttribute(name = "href")
    public String getHref() {
        if (StringUtils.isBlank(this.href)) {
            this.href = generateUrl();
        }

        return href;
    }

    /**
     * Explicitly set the href value - if this is called with a value, all other properties of the url object are
     * ignored.  This call is basically a full override.  This also sets the orginalHref value.
     *
     * @param href
     */
    public void setHref(String href) {
        this.href = href;
        this.originalHref = href;
    }

    /**
     * The base url value (the value that comes before other properties).  Default base bean value is set to use
     * 'krad.url' of the configuration properties.
     *
     * @return the baseUrl
     */
    @BeanTagAttribute(name = "baseUrl")
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Set the baseUrl
     *
     * @param baseUrl
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * The controllerMapping for the url (string that represents the controllerMapping path appended to baseUrl)
     *
     * @return the controllerMapping string
     */
    @BeanTagAttribute(name = "controllerMapping")
    public String getControllerMapping() {
        return controllerMapping;
    }

    /**
     * Set the controllerMapping
     *
     * @param controllerMapping
     */
    public void setControllerMapping(String controllerMapping) {
        this.controllerMapping = controllerMapping;
    }

    /**
     * The viewType representing the View's base type
     *
     * @return the viewType
     */
    @BeanTagAttribute(name = "viewType")
    public String getViewType() {
        return viewType;
    }

    /**
     * Set the viewType
     *
     * @param viewType
     */
    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    /**
     * ViewId representing the view by id to retrieve
     *
     * @return the viewId
     */
    @BeanTagAttribute(name = "viewId")
    public String getViewId() {
        return viewId;
    }

    /**
     * Set viewId
     *
     * @param viewId
     */
    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    /**
     * PageId representing the page of the view to retrieve by id
     *
     * @return the pageId
     */
    @BeanTagAttribute(name = "pageId")
    public String getPageId() {
        return pageId;
    }

    /**
     * Set pageId
     *
     * @param pageId
     */
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    /**
     * FormKey representing the key of the form data to retrieve
     *
     * @return the formKey
     */
    @BeanTagAttribute(name = "formKey")
    public String getFormKey() {
        return formKey;
    }

    /**
     * Set the formKey
     *
     * @param formKey
     */
    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    /**
     * MethodToCall representing the methodToCall on the controller (default base bean value is 'start')
     *
     * @return methodToCall on controller
     */
    @BeanTagAttribute(name = "methodToCall")
    public String getMethodToCall() {
        return methodToCall;
    }

    /**
     * Set the methodToCall
     *
     * @param methodToCall
     */
    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }

    /**
     * Map of key value pairs that will be appended to the request parameters to pass in any custom data
     *
     * @return the requestParameters map
     */
    @BeanTagAttribute(name = "requestParameters", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getRequestParameters() {
        return requestParameters;
    }

    /**
     * Set the requestParameters
     *
     * @param requestParameters
     */
    public void setRequestParameters(Map<String, String> requestParameters) {
        this.requestParameters = requestParameters;
    }

    /**
     * The original(set) href value.  This is generally used to determine if the href was explicitly set and not
     * generated by this url object.
     *
     * @return the original(set) href value
     */
    public String getOriginalHref() {
        return originalHref;
    }

    /**
     * toString override returns the href value of url
     *
     * @return href value
     */
    @Override
    public String toString() {
        return this.getHref();
    }

    /**
     * toString returns the original href value of url
     *
     * @return original href value
     */
    public void setOriginalHref(String originalHref) {
        this.originalHref = originalHref;
    }

    @Override
    protected <T> void copyProperties(T dictionaryBaseBean) {
        super.copyProperties(dictionaryBaseBean);

        UrlInfo urlInfoCopy = (UrlInfo) dictionaryBaseBean;

        urlInfoCopy.setHref(this.href);
        urlInfoCopy.setOriginalHref(this.originalHref);
        urlInfoCopy.setBaseUrl(this.baseUrl);
        urlInfoCopy.setControllerMapping(this.controllerMapping);
        urlInfoCopy.setViewType(this.viewType);
        urlInfoCopy.setViewId(this.viewId);
        urlInfoCopy.setPageId(this.pageId);
        urlInfoCopy.setFormKey(this.formKey);
        urlInfoCopy.setMethodToCall(this.methodToCall);

        if (this.requestParameters != null) {
            Map<String, String> requestParametersCopy = Maps.newHashMapWithExpectedSize(this.requestParameters.size());
            for (Map.Entry requestParameter : requestParameters.entrySet()) {
                requestParametersCopy.put(requestParameter.getKey().toString(), requestParameter.getValue().toString());
            }

            urlInfoCopy.setRequestParameters(requestParametersCopy);
        }
    }
}
