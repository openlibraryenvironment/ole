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
package org.kuali.rice.krad.datadictionary.validation.constraint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * This class is a direct copy of one that was in Kuali Student. Look up constraints are currently not implemented.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 1.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LookupConstraint extends CommonLookup implements Constraint {

    private static final long serialVersionUID = 1L;
    //	private String searchTypeId; // id of search type defined in search xml
    //	private String resultReturnKey; // key of searchResultColumn to map back to
    //									// this field
    //	protected List<LookupConstraintParamMapping> lookupParams; // maps fields to
    //																// search
    //																// params?
    //
    //	public List<LookupConstraintParamMapping> getLookupParams() {
    //		return lookupParams;
    //	}
    //
    //	public void setLookupParams(List<LookupConstraintParamMapping> lookupParams) {
    //		this.lookupParams = lookupParams;
    //	}
}