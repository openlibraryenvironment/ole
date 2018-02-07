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

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;

/**
 * A utility class for managing an {@link ExportDataSet} containing EDocLite
 * data.  Provides a mechanism to convert instances of this class to a
 * populated {@link ExportDataSet}.
 * 
 * @see ExportDataSet
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EdlExportDataSet {

	public static final QName EDOCLITES = new QName("EDL", "eDocLites");
	
	private List<EDocLiteAssociation> edocLites = new ArrayList<EDocLiteAssociation>();

	public List<EDocLiteAssociation> getEdocLites() {
		return edocLites;
	}

	/**
	 * Populates the given {@link ExportDataSet} with the data from this data set.
	 * 
	 * @param exportDataSet the data set to populate the data into
	 */
	public void populateExportDataSet(ExportDataSet exportDataSet) {
		if (edocLites != null && !edocLites.isEmpty()) {
			exportDataSet.addDataSet(EDOCLITES, edocLites);
		}
	}
	
	/**
	 * Converts this data set to a standard {@link ExportDataSet}, populating
	 * it with the data from this data set.
	 * 
	 * @return the populated ExportDataSet
	 */	
	public ExportDataSet createExportDataSet() {
		ExportDataSet exportDataSet = new ExportDataSet();
		populateExportDataSet(exportDataSet);
		return exportDataSet;
	}

	/**
	 * A static utility for creating a {@link EdlExportDataSet} from an
	 * {@link ExportDataSet}.  This method will only populate the returned
	 * EdocLite data set with EdocLite data from the given export data set.  The
	 * rest of the data in the given export data set will be ignored.
	 * 
	 * @param exportDataSet the ExportDataSet to pull EdocLite data from
	 * @return a StyleExportDataSet with any EdocLite data from the given exportDataSet populated
	 */
	public static EdlExportDataSet fromExportDataSet(ExportDataSet exportDataSet) {
		EdlExportDataSet edlExportDataSet = new EdlExportDataSet();
		
		List<EDocLiteAssociation> edocLites = (List<EDocLiteAssociation>)exportDataSet.getDataSets().get(EDOCLITES);
		if (edocLites != null) {
			edlExportDataSet.getEdocLites().addAll(edocLites);
		}
		
		return edlExportDataSet;
	}
	
}
