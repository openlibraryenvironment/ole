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
package org.kuali.rice.edl.impl.xml.export;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.krad.bo.Exporter;
import org.kuali.rice.krad.exception.ExportNotSupportedException;
import org.kuali.rice.krad.util.KRADConstants;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the {@link Exporter} class which facilitates exporting
 * of EDocLite data from the GUI.
 * 
 * @see ExportDataSet
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EdlDataExporter implements Exporter {

	private List<String> supportedFormats = new ArrayList<String>();

	public EdlDataExporter() {
		supportedFormats.add(KRADConstants.XML_FORMAT);
	}

	@Override
	public List<String> getSupportedFormats(Class<?> dataObjectClass) {
		return supportedFormats;
	}

	/**
	 * Builds the ExportDataSet based on the BusinessObjects passed in.
	 */
	protected ExportDataSet buildExportDataSet(Class<?> dataObjectClass, List<? extends Object> dataObjects) {
		EdlExportDataSet dataSet = new EdlExportDataSet();
		for (Object dataObject : dataObjects) {
			if (dataObjectClass.equals(EDocLiteAssociation.class)) {
				dataSet.getEdocLites().add((EDocLiteAssociation)dataObject);
			}   
		}

		return  dataSet.createExportDataSet();
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.krad.bo.Exporter#export(java.lang.Class, java.util.List, java.lang.String, java.io.OutputStream)
	 */
	@Override
	public void export(Class<?> dataObjectClass, List<? extends Object> dataObjects, String exportFormat, OutputStream outputStream) throws IOException,
			ExportNotSupportedException {
		if (!KRADConstants.XML_FORMAT.equals(exportFormat)) {
			throw new ExportNotSupportedException("The given export format of " + exportFormat + " is not supported by the EDocLite XML Exporter!");
		}
		ExportDataSet dataSet = buildExportDataSet(dataObjectClass, dataObjects);
		outputStream.write(CoreApiServiceLocator.getXmlExporterService().export(dataSet));
		outputStream.flush();
		
	}
}
