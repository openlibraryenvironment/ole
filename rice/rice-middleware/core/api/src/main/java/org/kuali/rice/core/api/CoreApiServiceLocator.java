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
package org.kuali.rice.core.api;

import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.core.api.impex.xml.XmlExporterService;
import org.kuali.rice.core.api.impex.xml.XmlIngesterService;
import org.kuali.rice.core.api.mail.Mailer;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import javax.xml.namespace.QName;

public class CoreApiServiceLocator {

	public static final String XML_EXPORTER_SERVICE = "xmlExporterService";
	public static final String XML_INGESTER_SERVICE = "xmlIngesterService";
    public static final String KUALI_CONFIGURATION_SERVICE = "kualiConfigurationService";

    static <T> T getService(String serviceName) {
        return GlobalResourceLoader.<T>getService(serviceName);
    }

    static <T> T getService(QName serviceName) {
        return GlobalResourceLoader.<T>getService(serviceName);
    }
    
    public static XmlExporterService getXmlExporterService() {
        return getService(XML_EXPORTER_SERVICE);
    }
    
    public static XmlIngesterService getXmlIngesterService() {
        return getService(XML_INGESTER_SERVICE);
    }
    
    public static final EncryptionService getEncryptionService() {
        return getService(CoreConstants.Services.ENCRYPTION_SERVICE);
    }

	public static DateTimeService getDateTimeService() {
	    return getService(CoreConstants.Services.DATETIME_SERVICE);
	}

    public static Mailer getMailer() {
        return getService(CoreConstants.Services.MAILER);
    }

    public static ConfigurationService getKualiConfigurationService() {
        return getService(KUALI_CONFIGURATION_SERVICE);
    }

}
