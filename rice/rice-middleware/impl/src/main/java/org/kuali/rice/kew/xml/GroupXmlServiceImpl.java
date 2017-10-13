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
package org.kuali.rice.kew.xml;

import java.io.InputStream;

import org.jdom.Element;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.core.framework.impex.xml.XmlLoader;
import org.kuali.rice.kew.xml.export.GroupXmlExporter;

public class GroupXmlServiceImpl implements XmlLoader, XmlExporter{
	
	public void loadXml(InputStream inputStream, String principalId) {
        GroupXmlParser parser = new GroupXmlParser();
        try {
            parser.parseGroups(inputStream);
        } catch (Exception e) {
            throw new RiceRuntimeException("Error loading xml file", e);
        }
    }

	@Override
	public boolean supportPrettyPrint() {
		return true;
	}
	
    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.core.framework.impex.xml.XmlExporter#export(org.kuali.rice.core.api.impex.ExportDataSet)
     */
    public Element export(ExportDataSet dataSet) {
        GroupXmlExporter exporter = new GroupXmlExporter();
        return exporter.export(dataSet);
    }


}
