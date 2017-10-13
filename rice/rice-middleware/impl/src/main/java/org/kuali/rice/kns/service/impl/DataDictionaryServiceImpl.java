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
package org.kuali.rice.kns.service.impl;

import org.kuali.rice.kns.datadictionary.KNSDocumentEntry;
import org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.kns.datadictionary.exporter.DataDictionaryMap;
import org.kuali.rice.kns.rule.PromptBeforeValidation;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.krad.datadictionary.DataDictionary;

import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataDictionaryServiceImpl extends org.kuali.rice.krad.service.impl.DataDictionaryServiceImpl implements DataDictionaryService {

    private DataDictionaryMap dataDictionaryMap = new DataDictionaryMap(this);

    public DataDictionaryServiceImpl() {
        super();
    }
    
    public DataDictionaryServiceImpl(DataDictionary dataDictionary) {
        super(dataDictionary);
    }
    
    /**
     * @see org.kuali.rice.krad.service.DataDictionaryService#getPromptBeforeValidationClass(java.lang.String)
     */
    public Class<? extends PromptBeforeValidation> getPromptBeforeValidationClass(String docTypeName) {
        Class preRulesCheckClass = null;

        KNSDocumentEntry documentEntry = (KNSDocumentEntry) getDataDictionary().getDocumentEntry(docTypeName);
        preRulesCheckClass = documentEntry.getPromptBeforeValidationClass();

        return preRulesCheckClass;
    }

    public Map getDataDictionaryMap() {
        return dataDictionaryMap;
    }
}
