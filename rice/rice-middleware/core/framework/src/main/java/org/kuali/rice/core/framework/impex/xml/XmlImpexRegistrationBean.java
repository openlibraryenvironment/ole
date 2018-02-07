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
package org.kuali.rice.core.framework.impex.xml;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * A Spring bean which can be used to register {@code XmlLoader} and {@code XmlExporter} with the core
 * {@code XmlImpexRegistry}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XmlImpexRegistrationBean implements InitializingBean, DisposableBean {

	private List<XmlLoader> xmlLoadersToRegister;
	private List<XmlExporter> xmlExportersToRegister;
	
	private XmlImpexRegistry xmlImpexRegistry;
	
	@Override
	public void afterPropertiesSet() {
		if (xmlLoadersToRegister != null) {
			for (XmlLoader xmlLoader : xmlLoadersToRegister) {
				xmlImpexRegistry.registerLoader(xmlLoader);
			}			
		}
		if (xmlExportersToRegister != null) {
			for (XmlExporter xmlExporter : xmlExportersToRegister) {
				xmlImpexRegistry.registerExporter(xmlExporter);
			}
		}
	}
	
	@Override
	public void destroy() throws Exception {
		if (xmlLoadersToRegister != null) {
			for (XmlLoader xmlLoader : xmlLoadersToRegister) {
				xmlImpexRegistry.unregisterLoader(xmlLoader);
			}			
		}
		if (xmlExportersToRegister != null) {
			for (XmlExporter xmlExporter : xmlExportersToRegister) {
				xmlImpexRegistry.unregisterExporter(xmlExporter);
			}
		}
	}

	/**
	 * @param xmlLoadersToRegister the xmlLoadersToRegister to set
	 */
	public void setXmlLoadersToRegister(List<XmlLoader> xmlLoadersToRegister) {
		this.xmlLoadersToRegister = xmlLoadersToRegister;
	}

	/**
	 * @param xmlExportersToRegister the xmlExportersToRegister to set
	 */
	public void setXmlExportersToRegister(List<XmlExporter> xmlExportersToRegister) {
		this.xmlExportersToRegister = xmlExportersToRegister;
	}
	
	public void setXmlImpexRegistry(XmlImpexRegistry xmlImpexRegistry) {
		this.xmlImpexRegistry = xmlImpexRegistry;
	}
}
