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

import java.io.Serializable;

/**
 * Defines methods common to all DataDictionaryDefinition types.
 */
public interface DataDictionaryDefinition extends DictionaryBean, Serializable {

    /**
     * Performs complete intra-definition validation which couldn't be done earlier - for example, verifies that field
     * references
     * refer to actual fields of some specific class.
     *
     * @param rootBusinessObjectClass Class of the BusinessObjectEntry which ultimately contains this definition
     * @param otherBusinessObjectClass other stuff required to complete validation
     * @throws org.kuali.rice.krad.datadictionary.exception.CompletionException if a problem arises during
     * validation-completion
     */
    public void completeValidation(Class<?> rootBusinessObjectClass, Class<?> otherBusinessObjectClass);

    public String getId();
}
