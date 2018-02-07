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
package org.kuali.rice.coreservice.impl.style;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.coreservice.impl.style.StyleBo;

/**
 * A utility class for managing an {@link ExportDataSet} containing StyleBo
 * data.  Provides a mechanism to convert instances of this class to a
 * populated {@link ExportDataSet}.
 * 
 * @see ExportDataSet
 * @see StyleBo
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StyleExportDataSet {

	public static final QName STYLES = new QName("CORE", "styles");
	
	private List<StyleBo> styles = new ArrayList<StyleBo>();

	public List<StyleBo> getStyles() {
		return styles;
	}
	
	/**
	 * Populates the given {@link ExportDataSet} with the data from this data set.
	 * 
	 * @param exportDataSet the data set to populate the data into
	 */
	public void populateExportDataSet(ExportDataSet exportDataSet) {
		if (styles != null && !styles.isEmpty()) {
			exportDataSet.addDataSet(STYLES, styles);
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
	 * A static utility for creating a {@link StyleExportDataSet} from an
	 * {@link ExportDataSet}.  This method will only populate the returned
	 * style data set with style data from the given export data set.  The
	 * rest of the data in the given export data set will be ignored.
	 * 
	 * @param exportDataSet the ExportDataSet to pull style data from
	 * @return a StyleExportDataSet with any style data from the given exportDataSet populated
	 */
	public static StyleExportDataSet fromExportDataSet(ExportDataSet exportDataSet) {
		StyleExportDataSet coreExportDataSet = new StyleExportDataSet();
		
		List<StyleBo> styles = (List<StyleBo>)exportDataSet.getDataSets().get(STYLES);
		if (styles != null) {
			coreExportDataSet.getStyles().addAll(styles);
		}
		
		return coreExportDataSet;
	}
	
}
