/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.select.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.service.OleUrlResolver;
import org.kuali.ole.service.impl.OlePatronWebServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kns.lookup.AbstractLookupableHelperServiceImpl;
import org.kuali.rice.krad.bo.BusinessObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OlePersonRequestorLookupableServiceImpl extends AbstractLookupableHelperServiceImpl {

    private transient OleUrlResolver oleUrlResolver;
    private OlePatronRecordHandler olePatronRecordHandler;


    public OlePatronRecordHandler getOlePatronRecordHandler() {
        if (null == olePatronRecordHandler) {
            olePatronRecordHandler = new OlePatronRecordHandler();
        }
        return olePatronRecordHandler;
    }

    public void setOlePatronRecordHandler(OlePatronRecordHandler olePatronRecordHandler) {
        this.olePatronRecordHandler = olePatronRecordHandler;
    }

    @Override
    public List<? extends BusinessObject> getSearchResults(Map<String, String> fieldValues) {
        Map<String, String> personMap = new HashMap<String, String>();
        Map<String, String> requestorMap = new HashMap<String, String>();
        String personRequestorFirstName = fieldValues.get(OLEConstants.OlePersonRequestorLookupable.FIRST_NAME);
        String personRequestorLastName = fieldValues.get(OLEConstants.OlePersonRequestorLookupable.LAST_NAME);
        String personRequestorEmail = fieldValues.get(OLEConstants.OlePersonRequestorLookupable.EMAIL);
        String personRequestorPhoneNumber = fieldValues.get(OLEConstants.OlePersonRequestorLookupable.PHONE_NUMBER);
        String personRequestorId = fieldValues.get(OLEConstants.OlePersonRequestorLookupable.ID);
        String personRequestorTypeId = fieldValues.get(OLEConstants.OlePersonRequestorLookupable.REQUESTOR_TYPE_ID);
        String personRequestorRefKrimId = fieldValues.get(OLEConstants.OlePersonRequestorLookupable.REF_KRIM_ID);

        personMap.put(OLEConstants.OlePersonRequestorLookupable.FIRST_NAME, personRequestorFirstName);
        personMap.put(OLEConstants.OlePersonRequestorLookupable.LAST_NAME, personRequestorLastName);
        personMap.put(OLEConstants.OlePersonRequestorLookupable.EMAIL_ADDRESS, personRequestorEmail);
        personMap.put(OLEConstants.OlePersonRequestorLookupable.PHONE_NUMBER, personRequestorPhoneNumber);
        personMap.put(OLEConstants.OlePersonRequestorLookupable.PRINCIPAL_ID, personRequestorId);
        List<OlePersonRequestor> results = new ArrayList<OlePersonRequestor>(0);
        if ("".equals(personRequestorRefKrimId) && "".equals(personRequestorTypeId)) {
            List<Person> people = SpringContext.getBean(PersonService.class).findPeople(personMap, true);

            for (int i = 0; i < people.size(); i++) {
                OlePersonRequestor olePersonRequestor = new OlePersonRequestor();
                olePersonRequestor.setId(people.get(i).getPrincipalId());
                olePersonRequestor.setInternalRequestorId(people.get(i).getPrincipalId());
                olePersonRequestor.setName(people.get(i).getPrincipalName());
                olePersonRequestor.setFirstName(people.get(i).getFirstName());
                olePersonRequestor.setLastName(people.get(i).getLastName());
                olePersonRequestor.setEmail(people.get(i).getEmailAddress());
                olePersonRequestor.setPhoneNumber(people.get(i).getPhoneNumber());
                results.add(olePersonRequestor);
            }
        }

        String requestorTypeId = null;
        Map<String, String> requestorTypeMap = getRequestorTypes();

        if (!(personRequestorFirstName.equals(OLEConstants.OlePersonRequestorLookupable.NULLSTRING))
                && !(personRequestorFirstName.equals(OLEConstants.OlePersonRequestorLookupable.EMPTY))) {
            requestorMap.put(OLEConstants.OlePersonRequestorLookupable.REQUESTOR_FIRST_NAME, personRequestorFirstName);
        }
        if (!(personRequestorLastName.equals(OLEConstants.OlePersonRequestorLookupable.NULLSTRING))
                && !(personRequestorLastName.equals(OLEConstants.OlePersonRequestorLookupable.EMPTY))) {
            requestorMap.put(OLEConstants.OlePersonRequestorLookupable.REQUESTOR_LAST_NAME, personRequestorLastName);
        }
        if (!(personRequestorEmail.equals(OLEConstants.OlePersonRequestorLookupable.NULLSTRING))
                && !(personRequestorEmail.equals(OLEConstants.OlePersonRequestorLookupable.EMPTY))) {
            requestorMap.put(OLEConstants.OlePersonRequestorLookupable.REQUESTOR_EMAIL, personRequestorEmail);
        }
        if (!(personRequestorPhoneNumber.equals(OLEConstants.OlePersonRequestorLookupable.NULLSTRING))
                && !(personRequestorPhoneNumber.equals(OLEConstants.OlePersonRequestorLookupable.EMPTY))) {
            requestorMap.put(OLEConstants.OlePersonRequestorLookupable.REQUESTOR_PHONE_NUMBER,
                    personRequestorPhoneNumber);
        }
        if (!(personRequestorId.equals(OLEConstants.OlePersonRequestorLookupable.NULLSTRING))
                && !(personRequestorId.equals(OLEConstants.OlePersonRequestorLookupable.EMPTY))) {
            requestorMap.put(OLEConstants.OlePersonRequestorLookupable.REQUESTOR_ID, personRequestorId);
        }
        if (!(personRequestorRefKrimId.equals(OLEConstants.OlePersonRequestorLookupable.NULLSTRING))
                && !(personRequestorRefKrimId.equals(OLEConstants.OlePersonRequestorLookupable.EMPTY))) {
            requestorMap.put(OLEConstants.OlePersonRequestorLookupable.REF_KRIM_ID, personRequestorRefKrimId);
        }
        if (!(OLEConstants.OlePersonRequestorLookupable.NULLSTRING.equals(personRequestorTypeId))
                && !(OLEConstants.OlePersonRequestorLookupable.EMPTY.equals(personRequestorTypeId))) {
            requestorMap.put(OLEConstants.OlePersonRequestorLookupable.REQUESTOR_TYPE_ID, personRequestorTypeId);
        }

        /*
         * List<OleRequestor> requestors = getRequestorResults(requestorMap); for(int j=0;j<requestors.size();j++){
         * OlePersonRequestor olePersonRequestor = new OlePersonRequestor();
         * olePersonRequestor.setId(requestors.get(j).getRequestorId());
         * olePersonRequestor.setFirstName(requestors.get(j).getRequestorFirstName());
         * olePersonRequestor.setLastName(requestors.get(j).getRequestorLastName());
         * olePersonRequestor.setName(requestors.get(j).getRequestorName());
         * olePersonRequestor.setEmail(requestors.get(j).getRequestorEmail());
         * olePersonRequestor.setPhoneNumber(requestors.get(j).getRequestorPhoneNumber());
         * olePersonRequestor.setRefKrimId(requestors.get(j).getRefKrimId()); for(Entry<String,String> requestorTypeKey :
         * requestorTypeMap.entrySet()){ if(requestors.get(j).getRequestorTypeId().equals(requestorTypeKey.getKey())){ String
         * requestorTypeName = requestorTypeKey.getValue(); olePersonRequestor.setRequestorTypeName(requestorTypeName); } }
         * results.add(olePersonRequestor); }
         */

        List<OLERequestorPatronDocument> olePatronDocumentList = new ArrayList<OLERequestorPatronDocument>();
        OlePatronDocuments olePatronDocuments = new OlePatronDocuments();
        OlePatronWebServiceImpl olePatronWebService = new OlePatronWebServiceImpl();
        String patronRecords = olePatronWebService.getPatronRecords();
        olePatronDocuments = getOlePatronRecordHandler().retrievePatronFromXML(patronRecords);
        olePatronDocumentList = olePatronDocuments.getOlePatronDocuments();
        String requestorId = fieldValues.get(OLEConstants.OlePersonRequestorLookupable.ID);
        String firstName = fieldValues.get(OLEConstants.OlePersonRequestorLookupable.FIRST_NAME);
        String lastName = fieldValues.get(OLEConstants.OlePersonRequestorLookupable.LAST_NAME);

        for (int patronCount = 0; patronCount < olePatronDocumentList.size(); patronCount++) {
            OlePersonRequestor olePersonRequestor = new OlePersonRequestor();
            if (StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName) && StringUtils.isEmpty(requestorId)) {
                olePersonRequestor.setId(olePatronDocumentList.get(patronCount).getOlePatronId());
                olePersonRequestor.setExternalRequestorId(olePatronDocumentList.get(patronCount).getOlePatronId());
                olePersonRequestor.setFirstName(olePatronDocumentList.get(patronCount).getFirstName());
                olePersonRequestor.setLastName(olePatronDocumentList.get(patronCount).getLastName());
                olePersonRequestor.setName(olePatronDocumentList.get(patronCount).getLastName() + ", "
                        + olePatronDocumentList.get(patronCount).getFirstName());
            } else {
                if (olePatronDocumentList.get(patronCount).getFirstName().equalsIgnoreCase(firstName)
                        || olePatronDocumentList.get(patronCount).getLastName().equalsIgnoreCase(lastName)
                        || olePatronDocumentList.get(patronCount).getOlePatronId().equalsIgnoreCase(requestorId)) {
                    olePersonRequestor.setId(olePatronDocumentList.get(patronCount).getOlePatronId());
                    olePersonRequestor.setExternalRequestorId(olePatronDocumentList.get(patronCount).getOlePatronId());
                    olePersonRequestor.setFirstName(olePatronDocumentList.get(patronCount).getFirstName());
                    olePersonRequestor.setLastName(olePatronDocumentList.get(patronCount).getLastName());
                    olePersonRequestor.setName(olePatronDocumentList.get(patronCount).getLastName() + ", "
                            + olePatronDocumentList.get(patronCount).getFirstName());
                }
            }
            /*
             * olePersonRequestor.setEmail(olePatronDocumentList.get(patronCount).getRequestorEmail());
             * olePersonRequestor.setPhoneNumber(requestors.get(j).getRequestorPhoneNumber());
             * olePersonRequestor.setRefKrimId(requestors.get(j).getRefKrimId());
             */
            if (olePersonRequestor.getExternalRequestorId() != null) {
                results.add(olePersonRequestor);
            }
        }
        return results;
    }

    public List<OleRequestor> getRequestorResults(Map requestorMap) {
        List<OleRequestor> requestorsList;
        org.kuali.rice.krad.service.BusinessObjectService
                businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        if (requestorMap.isEmpty()) {
            requestorsList = (List) businessObjectService.findAll(OleRequestor.class);
        } else {
            requestorsList = (List) businessObjectService.findMatching(OleRequestor.class, requestorMap);
        }
        return requestorsList;
    }

    public Map<String, String> getRequestorTypes() {

        Map<String, String> requestorTypeMap = new HashMap<String, String>();
        org.kuali.rice.krad.service.BusinessObjectService
                businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        List<OleRequestorType> oleRequestorTypeList = (List) businessObjectService.findAll(OleRequestorType.class);
        for (int i = 0; i < oleRequestorTypeList.size(); i++) {
            requestorTypeMap.put(oleRequestorTypeList.get(i).getRequestorTypeId().toString(), oleRequestorTypeList.get(i).getRequestorType());
        }
        return requestorTypeMap;
    }

}