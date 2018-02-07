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
package org.kuali.rice.krad.util;

import java.io.Serializable;
import java.util.List;

/**
 * This class is a token-style class, that is write-once, then read-only for all consumers of the class. It is often used as a
 * return value from various PersistenceStructureService methods.
 * 
 * The object represents the state of the foreign-key fields of a reference object. For example, if Account is the bo, and
 * organization is the reference object, then chartOfAccountsCode and organizationCode are the foreign key fields. Their state,
 * rather they are all filled out, whether any of them are filled out, and which ones are not filled out, is what this class
 * represents.
 * 
 * 
 */
public class ForeignKeyFieldsPopulationState implements Serializable {

    private boolean allFieldsPopulated;
    private boolean anyFieldsPopulated;
    private List<String> unpopulatedFieldNames;

    public ForeignKeyFieldsPopulationState(boolean allFieldsPopulated, boolean anyFieldsPopulated, List<String> unpopulatedFieldNames) {
        this.allFieldsPopulated = allFieldsPopulated;
        this.anyFieldsPopulated = anyFieldsPopulated;
        this.unpopulatedFieldNames = unpopulatedFieldNames;
    }

    /**
     * Gets the allFieldsPopulated attribute.
     * 
     * @return Returns the allFieldsPopulated.
     */
    public boolean isAllFieldsPopulated() {
        return allFieldsPopulated;
    }

    /**
     * Gets the anyFieldsPopulated attribute.
     * 
     * @return Returns the anyFieldsPopulated.
     */
    public boolean isAnyFieldsPopulated() {
        return anyFieldsPopulated;
    }

    /**
     * Gets the unpopulatedFieldNames attribute.
     * 
     * @return Returns the unpopulatedFieldNames.
     */
    public List<String> getUnpopulatedFieldNames() {
        return unpopulatedFieldNames;
    }

    /**
     * @see org.kuali.rice.krad.service.PersistenceStructureService.ForeignKeyFieldsPopulation#hasUnpopulatedFieldName(java.lang.String)
     */
    public boolean hasUnpopulatedFieldName(String fieldName) {
        if (this.unpopulatedFieldNames.contains(fieldName)) {
            return true;
        }
        else {
            return false;
        }
    }
}
