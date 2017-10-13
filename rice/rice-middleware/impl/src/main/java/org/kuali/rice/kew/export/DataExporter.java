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
package org.kuali.rice.kew.export;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.krad.bo.Exporter;
import org.kuali.rice.krad.exception.ExportNotSupportedException;
import org.kuali.rice.krad.util.KRADConstants;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The DataExporter allows for exporting of KEW BusinessObjects to various
 * supported formats. The current implementation supports only XML export. This
 * process is initiated from the KNS screens (lookups and inquiries) and this
 * implementation leverages the existing XmlExporterService which is part of KEW
 * and which was used to do exports before KEW was converted to use the KNS.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataExporter implements Exporter {

	private List<String> supportedFormats = new ArrayList<String>();

	public DataExporter() {
		supportedFormats.add(KRADConstants.XML_FORMAT);
	}

	/**
	 * @see org.kuali.rice.krad.bo.Exporter#getSupportedFormats(java.lang.Class)
	 */
	@Override
	public List<String> getSupportedFormats(Class<?> dataObjectClass) {
		return supportedFormats;
	}

	/**
	 * Builds the ExportDataSet based on the BusinessObjects passed in.
	 */
	protected ExportDataSet buildExportDataSet(Class<?> dataObjectClass, List<? extends Object> dataObjects) {
		KewExportDataSet dataSet = new KewExportDataSet();
		for (Object dataObject : dataObjects) {
			if (dataObjectClass.equals(RuleAttribute.class)) {
				dataSet.getRuleAttributes().add((RuleAttribute)dataObject);
			} else if (dataObjectClass.equals(RuleTemplateBo.class)) {
				dataSet.getRuleTemplates().add((RuleTemplateBo)dataObject);
			} else if (dataObjectClass.equals(DocumentType.class)) {
				dataSet.getDocumentTypes().add((DocumentType)dataObject);
			} else if (dataObjectClass.equals(RuleBaseValues.class)) {
				dataSet.getRules().add((RuleBaseValues)dataObject);
			} else if (dataObjectClass.equals(RuleDelegationBo.class)) {
				dataSet.getRuleDelegations().add((RuleDelegationBo)dataObject);
			} else if (dataObjectClass.equals(GroupBo.class)) {
				Group group = GroupBo.to((GroupBo)dataObject);
                dataSet.getGroups().add(group);
			}
		}

		ExportDataSet exportDataSet = new ExportDataSet();
		dataSet.populateExportDataSet(exportDataSet);
		return exportDataSet;
	}

	/**
	 * Export the given List of Objects of the specified type to XML.
	 * 
	 * @see org.kuali.rice.krad.bo.Exporter#export(java.lang.Class, java.util.List, java.lang.String, java.io.OutputStream)
	 */
	@Override
	public void export(Class<?> dataObjectClass, List<? extends Object> dataObjects, String exportFormat, OutputStream outputStream) throws IOException,
			ExportNotSupportedException {
		if (!KRADConstants.XML_FORMAT.equals(exportFormat)) {
			throw new ExportNotSupportedException("The given export format of "
					+ exportFormat
					+ " is not supported by the KEW XML Exporter!");
		}
		ExportDataSet dataSet = buildExportDataSet(dataObjectClass, dataObjects);
		outputStream.write(CoreApiServiceLocator.getXmlExporterService()
				.export(dataSet));
		outputStream.flush();
		
	}
}
