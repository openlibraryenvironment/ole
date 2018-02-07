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
package org.kuali.rice.kns.web.struts.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.MessageResourcesFactory;
import org.apache.struts.util.PropertyMessageResources;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a description of what this class does - jjhanso don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiPropertyMessageResources extends PropertyMessageResources {
    private static final long serialVersionUID = -7712311580595112293L;
    private HashMap kualiMessages;

    public KualiPropertyMessageResources(MessageResourcesFactory factory, String config) {
        super(factory, config);
    }

    public KualiPropertyMessageResources(MessageResourcesFactory factory, String config, boolean returnNull) {
        super(factory, config, returnNull);
    }

    protected void loadLocale(String localeKey) {
        String initialConfig = config;
        String[] propertyFiles = config.split(",");
        for (String propertyFile : propertyFiles) {
            config = propertyFile;
            locales.remove(localeKey);
            super.loadLocale(localeKey);
        }
        config = initialConfig;
    }
    
    public Map getKualiProperties(String localeKey) {
        if (this.kualiMessages != null && !this.kualiMessages.isEmpty()) {
            return this.kualiMessages;
        } 
        localeKey = (localeKey == null) ? "" : localeKey;
        String localePrefix = localeKey + ".";
        
        this.loadLocale((localeKey == null) ? "" : localeKey);
        this.kualiMessages = new HashMap(this.messages.size());
        Set<String> keys = this.messages.keySet();
        for (String key : keys) {
            this.kualiMessages.put(StringUtils.substringAfter(key, localePrefix), this.messages.get(key));
        }
        return this.kualiMessages;
    }

}
