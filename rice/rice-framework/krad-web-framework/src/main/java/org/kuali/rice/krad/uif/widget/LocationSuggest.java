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
package org.kuali.rice.krad.uif.widget;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.util.ScriptUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * LocationSuggest widget for providing suggestions that represent locations.  When the suggestion is clicked, the
 * navigation occurs immediately.
 */
@BeanTag(name = "locationSuggest-bean", parent = "Uif-LocationSuggest")
public class LocationSuggest extends Suggest {

    private static final long serialVersionUID = 5940714417896326889L;
    private String baseUrl;
    private String additionalUrlPathPropertyName;
    private String hrefPropertyName;
    private String objectIdPropertyName;
    private Map<String, String> requestParameterPropertyNames;
    private Map<String, String> additionalRequestParameters;

    /**
     * Process the objectIdPropertyName, if set
     *
     * @see Component#performFinalize(org.kuali.rice.krad.uif.view.View, Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        if (requestParameterPropertyNames == null) {
            requestParameterPropertyNames = new HashMap<String, String>();
        }

        if (StringUtils.isNotBlank(objectIdPropertyName)) {
            requestParameterPropertyNames.put(objectIdPropertyName, objectIdPropertyName);
        }
    }

    /**
     * BaseUrl for the suggestions.  Unless the suggestion contains an href, baseUrl + additionalUrlPath value +
     * request parameters is used to generate the url.
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
     * AdditionalUrlPathProperty specifies the property on the retrieved suggestion result that contains a url
     * appendage
     * to be appended to the baseUrl when this selection is chosen.
     *
     * <p>One use case for setting this is to retrieve a controllerMapping that changes based on selection.  Note:
     * for suggestions that all point to the same controllerMapping, simply set it as part of the baseUrl.</p>
     *
     * @return the additionalUrlPathPropertyName
     */
    @BeanTagAttribute(name = "additionalUrlPropertyName")
    public String getAdditionalUrlPathPropertyName() {
        return additionalUrlPathPropertyName;
    }

    /**
     * Set additionalUrlPathProperty
     *
     * @param additionalUrlPathPropertyName
     */
    public void setAdditionalUrlPathPropertyName(String additionalUrlPathPropertyName) {
        this.additionalUrlPathPropertyName = additionalUrlPathPropertyName;
    }

    /**
     * The hrefPropertyName specifies the property on the retrieved suggestion result that contains the href
     * value (full url).
     *
     * <p>This property must contain a full url if it exists on the object.  If this property name is matched on
     * the suggestion result, it takes precedence over any other settings set on this locationSuggest
     * and is used as the navigation url.  If the property name does not exist on the object, the suggest will fall
     * back to building the url dynamically with baseUrl.</p>
     *
     * @return the hrefPropertyName
     */
    @BeanTagAttribute(name = "hrefPropertyName")
    public String getHrefPropertyName() {
        return hrefPropertyName;
    }

    /**
     * Set the hrefPropertyName
     *
     * @param hrefPropertyName
     */
    public void setHrefPropertyName(String hrefPropertyName) {
        this.hrefPropertyName = hrefPropertyName;
    }

    /**
     * The objectIdPropertyName that represents the key for getting the object as a request parameter.  The property
     * will be added to the request parameters by the name given with the value pulled from the result object.
     *
     * <p>
     *     This convenience method is essentially equivalent to having a property by objectIdPropertyName as a
     *     key and value in the requestParameterPropertyNames.
     * </p>
     *
     * @return the objectIdPropertyName which represents which property is the "key" of the object
     */
    public String getObjectIdPropertyName() {
        return objectIdPropertyName;
    }

    /**
     * Set the objectIdPropertyName
     *
     * @param objectIdPropertyName
     */
    public void setObjectIdPropertyName(String objectIdPropertyName) {
        this.objectIdPropertyName = objectIdPropertyName;
    }

    /**
     * RequestParameterPropertyNames specify the properties that should be included in the request parameters.
     *
     * <p>The key is used as the key of the request parameter and the value is used as the property name to look for in
     * the suggestion result object.  If the property name specified exists on the result object, the request
     * parameter in the url will appear as key=propertyValue in the request parameters.</p>
     *
     * @return the RequestParameterPropertyNames map with key and property names
     */
    @BeanTagAttribute(name = "requestParameterPropertyNames", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getRequestParameterPropertyNames() {
        return requestParameterPropertyNames;
    }

    /**
     * Set the requestParameterPropertyNames
     *
     * @param requestParameterPropertyNames
     */
    public void setRequestParameterPropertyNames(Map<String, String> requestParameterPropertyNames) {
        this.requestParameterPropertyNames = requestParameterPropertyNames;
    }

    /**
     * AdditionalRequestParameters specify the static(constant) request parameters that should be appended to the url.
     *
     * <p>The key represents the key of the request parameter and the value represents the value of the
     * request parameter.  This will be used on each suggestion which uses a generated url (using baseUrl
     * construction).
     * </p>
     *
     * @return
     */
    @BeanTagAttribute(name = "additionalRequestParameters", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getAdditionalRequestParameters() {
        return additionalRequestParameters;
    }

    /**
     * Get the additionalRequestParameters
     *
     * @param additionalRequestParameters
     */
    public void setAdditionalRequestParameters(Map<String, String> additionalRequestParameters) {
        this.additionalRequestParameters = additionalRequestParameters;
    }

    /**
     * Gets an object translated to js for the requestParameterPropertyNames.  Used to construct the url on the client.
     *
     * @return the requestParameterPropertyNames js map object
     */
    public String getRequestParameterPropertyNameJsObject() {
        if (requestParameterPropertyNames != null && !requestParameterPropertyNames.isEmpty()) {
            return ScriptUtils.translateValue(requestParameterPropertyNames);
        } else {
            return "{}";
        }
    }

    /**
     * Gets the constant additionalRequestParameters as a request string value to use as part of the url
     *
     * @return the request parameter string for additionalRequestParameters
     */
    public String getAdditionalRequestParameterString() {
        if (additionalRequestParameters != null) {
            return KRADUtils.getRequestStringFromMap(additionalRequestParameters);
        } else {
            return "";
        }
    }
}
