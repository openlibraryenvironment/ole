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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.core.web.format.Formatter;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.UifFormBase;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * Class for utility methods that pertain to UIF Lookup processing
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LookupInquiryUtils {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LookupInquiryUtils.class);

	public static String retrieveLookupParameterValue(UifFormBase form, HttpServletRequest request,
			Class<?> lookupObjectClass, String propertyName, String propertyValueName) {
		String parameterValue = "";

		// get literal parameter values first
		if (StringUtils.startsWith(propertyValueName, "'") && StringUtils.endsWith(propertyValueName, "'")) {
            parameterValue = StringUtils.removeStart(propertyValueName, "'");
			parameterValue = StringUtils.removeEnd(propertyValueName, "'");
		}
		else if (parameterValue.startsWith(KRADConstants.LOOKUP_PARAMETER_LITERAL_PREFIX
				+ KRADConstants.LOOKUP_PARAMETER_LITERAL_DELIMITER)) {
			parameterValue = StringUtils.removeStart(parameterValue, KRADConstants.LOOKUP_PARAMETER_LITERAL_PREFIX
					+ KRADConstants.LOOKUP_PARAMETER_LITERAL_DELIMITER);
		}
		// check if parameter is in request
		else if (request.getParameterMap().containsKey(propertyValueName)) {
			parameterValue = request.getParameter(propertyValueName);
		}
		// get parameter value from form object
		else {
			Object value = ObjectPropertyUtils.getPropertyValue(form, propertyValueName);
			if (value != null) {
				if (value instanceof String) {
					parameterValue = (String) value;
				}

				Formatter formatter = Formatter.getFormatter(value.getClass());
				parameterValue = (String) formatter.format(value);
			}
		}

		if (parameterValue != null
				&& lookupObjectClass != null
				&& KRADServiceLocatorWeb.getDataObjectAuthorizationService()
						.attributeValueNeedsToBeEncryptedOnFormsAndLinks(lookupObjectClass, propertyName)) {
			try {
                if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
				    parameterValue = CoreApiServiceLocator.getEncryptionService().encrypt(parameterValue)
					    	+ EncryptionService.ENCRYPTION_POST_PREFIX;
                }
			}
			catch (GeneralSecurityException e) {
				LOG.error("Unable to encrypt value for property name: " + propertyName);
				throw new RuntimeException(e);
			}
		}

		return parameterValue;
	}

    public static String getBaseLookupUrl() {
        return CoreApiServiceLocator.getKualiConfigurationService().
                getPropertyValueAsString(KRADConstants.KRAD_LOOKUP_URL_KEY);
    }

    /**
	 * Helper method for building the title text for an element and a map of
	 * key/value pairs
	 *
	 * @param prependText text to prepend to the title
	 * @param element element class the title is being generated for, used to as
	 *            the parent for getting the key labels
	 * @param keyValueMap map of key value pairs to add to the title text
	 * @return title text
	 */
	public static String getLinkTitleText(String prependText, Class<?> element, Map<String, String> keyValueMap) {
		StringBuffer titleText = new StringBuffer(prependText);
		for (String key : keyValueMap.keySet()) {
			String fieldVal = keyValueMap.get(key).toString();

			titleText.append(" " + KRADServiceLocatorWeb.getDataDictionaryService().getAttributeLabel(element, key) + "="
					+ fieldVal.toString());
		}

		return titleText.toString();
	}

}
