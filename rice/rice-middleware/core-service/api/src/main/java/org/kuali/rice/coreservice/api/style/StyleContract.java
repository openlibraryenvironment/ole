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
package org.kuali.rice.coreservice.api.style;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * This is the contract for a Style.  A style represents a stylesheet that is used for transforming data from
 * one format to another (currently only XSL is supported).
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface StyleContract extends Identifiable, Versioned, GloballyUnique, Inactivatable {

	/**
	 * Returns the name of this style.  All styles have a name and this value
	 * can never be null or blank.  The name must be unique within the entire
	 * repository of existing styles.
	 * 
	 * @return the name of this style
	 */
	String getName();
	
	/**
	 * Returns the XML definition of this style as a String.
	 * 
	 * @return the xml definition of this style
	 */
	String getXmlContent();
}
