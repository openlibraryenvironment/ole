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
package org.kuali.rice.core.impl.impex.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.core.framework.impex.xml.XmlImpexRegistry;
import org.kuali.rice.core.framework.impex.xml.XmlLoader;

/**
 * TODO 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class XmlImpexRegistryImpl implements XmlImpexRegistry {

	private final List<XmlLoader> xmlLoaders = Collections.synchronizedList(new ArrayList<XmlLoader>());
	private final List<XmlExporter> xmlExporters = Collections.synchronizedList(new ArrayList<XmlExporter>());
	
	@Override
	public void registerLoader(XmlLoader xmlLoader) {
		xmlLoaders.add(xmlLoader);
	}

	@Override
	public void registerExporter(XmlExporter exporter) {
		xmlExporters.add(exporter);
	}
	
	@Override
	public boolean unregisterLoader(XmlLoader xmlLoader) {
		return xmlLoaders.remove(xmlLoader);
	}

	@Override
	public boolean unregisterExporter(XmlExporter exporter) {
		return xmlExporters.remove(exporter);
	}

	@Override
	public List<XmlLoader> getLoaders() {
		return new ArrayList<XmlLoader>(xmlLoaders);
	}

	@Override
	public List<XmlExporter> getExporters() {
		return new ArrayList<XmlExporter>(xmlExporters);
	}

}
