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
package org.kuali.rice.kim.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kim.api.KimConstants;

/**
 * This is a description of what this class does - bhargavp don't forget to fill
 * this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class KimCommonUtilsInternal {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(KimCommonUtilsInternal.class);

	private KimCommonUtilsInternal() {
		throw new UnsupportedOperationException("do not call");
	}

    public static void copyProperties(Object targetToCopyTo, Object sourceToCopyFrom){
		if(targetToCopyTo!=null && sourceToCopyFrom!=null)
		try{
			PropertyUtils.copyProperties(targetToCopyTo, sourceToCopyFrom);
		} catch(Exception ex){
			throw new RuntimeException("Failed to copy from source object: "+sourceToCopyFrom.getClass()+" to target object: "+targetToCopyTo,ex);
		}
	}

	public static String getKimBasePath(){
		String kimBaseUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                KimConstants.KimUIConstants.KIM_URL_KEY);
		if (!kimBaseUrl.endsWith(KimConstants.KimUIConstants.URL_SEPARATOR)) {
			kimBaseUrl = kimBaseUrl + KimConstants.KimUIConstants.URL_SEPARATOR;
		}
		return kimBaseUrl;
	}

	public static String getPathWithKimContext(String path, String kimActionName){
		String kimContext = KimConstants.KimUIConstants.KIM_APPLICATION+KimConstants.KimUIConstants.URL_SEPARATOR;
		String kimContextParameterized = KimConstants.KimUIConstants.KIM_APPLICATION+KimConstants.KimUIConstants.PARAMETERIZED_URL_SEPARATOR;
    	if(path.contains(kimActionName) && !path.contains(kimContext + kimActionName)
    			&& !path.contains(kimContextParameterized + kimActionName))
    		path = path.replace(kimActionName, kimContext+kimActionName);
    	return path;
	}
}
