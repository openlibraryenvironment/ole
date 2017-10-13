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
package org.kuali.rice.krad.datadictionary;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;

/**
 * TransactionalDocumentEntry
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "transactionalDocumentEntry-bean")
public class TransactionalDocumentEntry extends DocumentEntry {
    private static final long serialVersionUID = 5746921563371805425L;

    public TransactionalDocumentEntry() {}

    /**
     * @see org.kuali.rice.krad.datadictionary.DocumentEntry#completeValidation()
     */
    @Override
    public void completeValidation() {
        super.completeValidation();
        for (ReferenceDefinition reference : defaultExistenceChecks) {
            reference.completeValidation(documentClass, null);
        }
    }

    @Override
    public String toString() {
        return "TransactionalDocumentEntry for documentType " + getDocumentTypeName();
    }
}
