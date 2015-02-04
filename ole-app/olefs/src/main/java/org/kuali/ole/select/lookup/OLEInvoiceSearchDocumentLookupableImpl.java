/*
 * Copyright 2011 The Kuali Foundation.
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

package org.kuali.ole.select.lookup;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEInvoiceSearchDocument;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.service.OLEInvoiceSearchService;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.web.form.LookupForm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OLEInvoiceSearchDocumentLookupableImpl extends LookupableImpl {
    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        List<OLEInvoiceSearchDocument> oleInvoiceSearchDocuments=new ArrayList<OLEInvoiceSearchDocument>();
        List<OLEInvoiceSearchDocument> oleInvoiceDocuments=new ArrayList<OLEInvoiceSearchDocument>();
        List<OLEInvoiceSearchDocument> oleInvoiceDocumentsForTitle=new ArrayList<OLEInvoiceSearchDocument>();
        Map<String, String> searchDocumentMap = new HashMap<String, String>();
        Map<String, String> fieldConversion=form.getFieldConversions();
        boolean isAddTitleScreen=false;
        if (fieldConversion.containsKey("invoiceNbr")) {
            isAddTitleScreen=true;
            for (Map.Entry<String, String> mapEntry : searchCriteria.entrySet()) {
                if (StringUtils.isNotEmpty(mapEntry.getValue())) {
                    if (mapEntry.getKey().equalsIgnoreCase("invoiceNbr")) {
                        searchDocumentMap.put(OleSelectConstant.InvoiceSearch.INV_NUMBER, mapEntry.getValue());
                    } else {
                        searchDocumentMap.put(mapEntry.getKey(), mapEntry.getValue());
                    }
                }
            }
        } else {
            for (Map.Entry<String, String> mapEntry : searchCriteria.entrySet()) {
                if (StringUtils.isNotEmpty(mapEntry.getValue())) {
                    searchDocumentMap.put(mapEntry.getKey(), mapEntry.getValue());
                }
            }
        }
        OLEInvoiceSearchService oleInvoiceSearchService=new OLEInvoiceSearchService();
        try {
            oleInvoiceDocuments= oleInvoiceSearchService.searchResults(searchDocumentMap);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if(isAddTitleScreen){
            for(OLEInvoiceSearchDocument oleInvoiceSearchDocument:oleInvoiceDocuments){
                 if(oleInvoiceSearchDocument.getDocumentStatus().equalsIgnoreCase("Initiated")){
                     oleInvoiceDocumentsForTitle.add(oleInvoiceSearchDocument);
                 }
            }
            return oleInvoiceDocumentsForTitle;
        }
        return oleInvoiceDocuments;
    }
}
