/*
 * Copyright 2013 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.service.impl;

import org.kuali.ole.select.businessobject.OLERequestorPatronDocument;
import org.kuali.ole.select.businessobject.OlePatronDocuments;
import org.kuali.ole.select.businessobject.OlePatronRecordHandler;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.service.impl.OlePatronWebServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;

import java.util.ArrayList;
import java.util.List;

public class OleSelectDocumentServiceImpl implements OleSelectDocumentService {
    private static transient OlePatronRecordHandler olePatronRecordHandler;

    @Override
    public List<OLERequestorPatronDocument> getPatronDocumentListFromWebService() {
        List<OLERequestorPatronDocument> olePatronDocumentList = new ArrayList<OLERequestorPatronDocument>();
        OlePatronDocuments olePatronDocument = getPatronObjectsFromWebService();
        if (olePatronDocument.getOlePatronDocuments() != null) {
            for (int olePatron = 0; olePatron < olePatronDocument.getOlePatronDocuments().size(); olePatron++) {
                olePatronDocumentList.add(olePatronDocument.getOlePatronDocuments().get(olePatron));
            }
        }
        return olePatronDocumentList;
    }

    private OlePatronDocuments getPatronObjectsFromWebService() {
        OlePatronDocuments olePatronDocuments = new OlePatronDocuments();

        OlePatronWebServiceImpl olePatronWebService = new OlePatronWebServiceImpl();
        String patronRecords = olePatronWebService.getPatronRecords();
        olePatronDocuments = getOlePatronRecordHandler().retrievePatronFromXML(patronRecords);
        return olePatronDocuments;
    }

    @Override
    public String getPatronName(List<OLERequestorPatronDocument> olePatronDocumentList, String requestorId) {
        StringBuffer patronName = new StringBuffer();
        for (OLERequestorPatronDocument olePatronDocument : olePatronDocumentList) {
            if (requestorId != null) {
                if (requestorId.equalsIgnoreCase(olePatronDocument.getOlePatronId())) {
                    patronName.append(olePatronDocument.getLastName());
                    patronName.append(", ");
                    patronName.append(olePatronDocument.getFirstName());
                }
            }
        }
        return patronName.toString();
    }

    public OlePatronRecordHandler getOlePatronRecordHandler() {
        if (null == olePatronRecordHandler) {
            olePatronRecordHandler = new OlePatronRecordHandler();
        }
        return olePatronRecordHandler;
    }

    public String getSelectParameterValue(String parameterName){
        ParameterKey parameterKey = ParameterKey.create(org.kuali.ole.OLEConstants.APPL_ID, org.kuali.ole.OLEConstants.SELECT_NMSPC, org.kuali.ole.OLEConstants.SELECT_CMPNT,parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }


    public void setOlePatronRecordHandler(OlePatronRecordHandler olePatronRecordHandler) {
        this.olePatronRecordHandler = olePatronRecordHandler;
    }
}