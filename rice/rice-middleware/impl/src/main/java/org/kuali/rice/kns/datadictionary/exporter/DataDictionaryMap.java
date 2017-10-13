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
package org.kuali.rice.kns.datadictionary.exporter;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kns.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.DataDictionaryEntry;
import org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.kns.datadictionary.TransactionalDocumentEntry;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class DataDictionaryMap extends DataDictionaryMapBase {

    private DataDictionaryService dataDictionaryService;

    BusinessObjectEntryMapper boMapper = new BusinessObjectEntryMapper();
    MaintenanceDocumentEntryMapper maintDocMapper = new MaintenanceDocumentEntryMapper();
    TransactionalDocumentEntryMapper transDocMapper = new TransactionalDocumentEntryMapper();
    
    Map<String,Map> ddMap = new HashMap<String,Map>();
    
    public DataDictionaryMap(DataDictionaryService dataDictionaryService) {
        super();
        this.dataDictionaryService = dataDictionaryService;
    }

    public Object get(Object key) {
        Map subMap = ddMap.get( key );
        if ( subMap == null ) { // need to load from DD
            synchronized( this ) { // ensure only one update access happening at a time
                subMap = ddMap.get( key );
                if ( subMap == null ) { // recheck in case it was loaded by another thread while this one was blocked
                    DataDictionaryEntry entry = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDictionaryObjectEntry( key.toString() );
                    // if that fails try just using the simple name if a full class name was passed
                    if ( entry == null && key.toString().contains(".")) {
                    	entry = dataDictionaryService.getDataDictionary().getDictionaryObjectEntry( StringUtils.substringAfterLast( key.toString(), "." ) );
                    }
                    if ( entry != null ) {
                        if ( entry instanceof BusinessObjectEntry ) {
                            subMap = boMapper.mapEntry( (BusinessObjectEntry)entry ).getExportData();                    
                        } else if ( entry instanceof MaintenanceDocumentEntry ) {
                            subMap = maintDocMapper.mapEntry( (MaintenanceDocumentEntry)entry ).getExportData();                    
                        } else if ( entry instanceof TransactionalDocumentEntry ) {
                            subMap = transDocMapper.mapEntry( (TransactionalDocumentEntry)entry ).getExportData();                    
                        }
                    }
                    if ( subMap != null ) {
                        ddMap.put( key.toString(), subMap );
                    }
                }
            }
        }
        return subMap;
    }

    public DataDictionaryService getDataDictionaryService() {
        return dataDictionaryService;
    }

    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

}
