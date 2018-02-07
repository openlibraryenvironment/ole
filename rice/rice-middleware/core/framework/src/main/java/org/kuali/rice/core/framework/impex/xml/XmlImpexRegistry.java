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

import java.util.List;


/**
 * A registry of loader and exporters for XML representations of data and configuration.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface XmlImpexRegistry {

	void registerLoader(XmlLoader xmlLoader);
		
	void registerExporter(XmlExporter exporter);
	
	boolean unregisterLoader(XmlLoader xmlLoader);
	
	boolean unregisterExporter(XmlExporter exporter);
	
	List<XmlLoader> getLoaders();
	
	List<XmlExporter> getExporters();
	
}
