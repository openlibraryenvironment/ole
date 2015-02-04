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
package org.kuali.ole.select.service;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.common.util.PropertyUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEInvoiceSearchDocument;
import org.kuali.ole.select.businessobject.DocInfoBean;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.select.service.impl.BibInfoWrapperServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.exception.WorkflowServiceError;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.DocumentHeaderService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class OLEInvoiceSearchService {

    public static final Map<String, String> INVOICE_FIELDS = getInvoiceNames();
    public static final List<String> INVOICE_FIELD_RESULT =getResultNames();
    public static List<String> INVOICE_SEARCH_CRITERIA_LIST=new ArrayList<String>();
    public List<OLEInvoiceSearchDocument> searchResults(Map<String, String> searchCriteria) throws Exception {
        List<OLEInvoiceSearchDocument> resultRows = new ArrayList<OLEInvoiceSearchDocument>();
        Map<String, List<String>> searchCriterias = new HashMap<String, List<String>>();
        INVOICE_SEARCH_CRITERIA_LIST=buildSearchCriteriaList(searchCriteria);
        try {
            searchCriterias.putAll(getSearchCriteria(searchCriteria));
            resultRows.addAll(populateCriteriaAndSearch(PurapConstants.PurapDocTypeCodes.INVOICE_DOCUMENT.toUpperCase(), searchCriterias));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return resultRows;
    }

    public Map<String, List<String>> getSearchCriteria(Map<String, String> itemFields) throws Exception {
        Map<String, List<String>> searchCriteriaMap = new HashMap<String, List<String>>();
        for (Map.Entry<String, String> entry : itemFields.entrySet()) {
            List<String> list = new ArrayList<String>();
            if (entry.getKey().equalsIgnoreCase(OleSelectConstant.InvoiceSearch.PURAP_ID)) {
                StringTokenizer stringTokenizer = new StringTokenizer(entry.getValue(), ",");
                while (stringTokenizer.hasMoreElements()) {
                    list.add(stringTokenizer.nextElement().toString());
                }
                searchCriteriaMap.put(entry.getKey().toString(), list);
            } else {
                searchCriteriaMap.put(entry.getKey().toString(), new ArrayList<String>(Arrays.asList(entry.getValue().toString())));
            }


        }
        return searchCriteriaMap;
    }

    public List<OLEInvoiceSearchDocument> populateCriteriaAndSearch(String docTypeFullName, Map<String, List<String>> fixedParameters)
            throws WorkflowException, Exception {
        DocumentSearchCriteria.Builder docSearchCriteria = DocumentSearchCriteria.Builder.create();
        DateTime dateTime = new DateTime(new Date());
        docSearchCriteria.setDateCreatedTo((dateTime));

        docSearchCriteria.setDocumentTypeName(docTypeFullName);

        List<OLEInvoiceSearchDocument> resultRows = new ArrayList<OLEInvoiceSearchDocument>();
        resultRows = performDocumentSearch(docSearchCriteria.build(), fixedParameters);

        return resultRows;
    }

    public List<OLEInvoiceSearchDocument> performDocumentSearch(DocumentSearchCriteria docSearchCriteria, Map<String, List<String>> fixedParameters) {
       /* List<String> strings=new ArrayList<String>();
        strings.add("1");
        fixedParameters.put(OleSelectConstant.InvoiceSearch.INV_NUMBER,strings);*/
        DocumentSearchCriteria docSearchCriteriaDTO = addDocumentAttributesToCriteria(DocumentSearchCriteria.Builder.create(docSearchCriteria), fixedParameters);
        List result = new ArrayList();
        List<OLEInvoiceSearchDocument> finalResult = new ArrayList<OLEInvoiceSearchDocument>();
        DocumentSearchResults components = null;
        try {
            components = KEWServiceLocator.getDocumentSearchService().lookupDocuments(GlobalVariables.getUserSession().getPrincipalId(),
                    docSearchCriteriaDTO);

            List<DocumentSearchResult> docSearchResults = components.getSearchResults();
            if (!fixedParameters.containsKey("displayType")) {
                finalResult = getFinalDocumentTypeResult(docSearchResults);
            }
        } catch (WorkflowServiceErrorException wsee) {
            for (WorkflowServiceError workflowServiceError : (List<WorkflowServiceError>) wsee.getServiceErrors()) {
                if (workflowServiceError.getMessageMap() != null && workflowServiceError.getMessageMap().hasErrors()) {
                    GlobalVariables.getMessageMap().merge(workflowServiceError.getMessageMap());
                } else {
                    GlobalVariables.getMessageMap().putError(workflowServiceError.getMessage(), RiceKeyConstants.ERROR_CUSTOM,
                            workflowServiceError.getMessage());
                }
            }
            ;
        }
        return finalResult;
    }

    private DocumentSearchCriteria addDocumentAttributesToCriteria(DocumentSearchCriteria.Builder criteria, Map<String, List<String>> propertyFields) {
        Map<String, List<String>> attributes = new HashMap<String, List<String>>();
        if (criteria != null) {
            if (!propertyFields.isEmpty()) {
                for (String propertyField : propertyFields.keySet()) {
                    if (propertyFields.get(propertyField) != null) {
                        attributes.put(propertyField, propertyFields.get(propertyField));
                    }
                }
            }
        }
        criteria.setDocumentAttributeValues(attributes);
        return criteria.build();
    }

    private List<OLEInvoiceSearchDocument> getFinalDocumentTypeResult(List<DocumentSearchResult> componentResults) {
        List<OLEInvoiceSearchDocument> docResult = new ArrayList<OLEInvoiceSearchDocument>();
        OLEInvoiceSearchDocument oleInvoiceDocument;
        if (!componentResults.isEmpty()) {
            for (DocumentSearchResult searchResult : componentResults) {
                if (!searchResult.getDocument().getApplicationDocumentStatus().equalsIgnoreCase("exception")) {
                    oleInvoiceDocument = convertToOleInvoiceDocument(searchResult);
                    oleInvoiceDocument.setDocumentNumber(searchResult.getDocument().getDocumentId());
                    oleInvoiceDocument.setDocumentStatus(searchResult.getDocument().getApplicationDocumentStatus());
                    Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                    DocumentHeader documentHeader = SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(searchResult.getDocument().getDocumentId());
                    try {
                        if (documentHeader != null) {
                            documentHeader.setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(searchResult.getDocument().getDocumentId(),
                                    principalPerson));
                            if (documentHeader.getWorkflowDocument() != null) {
                                oleInvoiceDocument.setWorkFlowDocumentStatus(documentHeader.getWorkflowDocument().getDocument().getStatus().getLabel());
                            }
                        }

                    }
                    catch (WorkflowException e) {
                       throw new RuntimeException(e);
                    }


                    if (oleInvoiceDocument != null) {
                        docResult.add(oleInvoiceDocument);
                    }
                }
            }
        }
        return docResult;

    }
    public List<String> buildSearchCriteriaList(Map<String, String> searchCriteria){
        List<String> buildSearchCriteriaList=new ArrayList<String>();
        for(Map.Entry<String,String> entry:searchCriteria.entrySet()){
            if(StringUtils.isNotEmpty(entry.getValue())){
                 buildSearchCriteriaList.add(entry.getKey());
            }
        }
        return buildSearchCriteriaList;
    }

    public OLEInvoiceSearchDocument convertToOleInvoiceDocument(DocumentSearchResult documentSearchResult) {
        OLEInvoiceSearchDocument invoiceDocument = new OLEInvoiceSearchDocument();
        Document document = documentSearchResult.getDocument();
        List<DocumentAttribute> documentAttributes = documentSearchResult.getDocumentAttributes();
        for (DocumentAttribute docAttribute : documentAttributes) {
            String name = docAttribute.getName();

            if (name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_DATE)) {
                if (docAttribute.getValue() != null) {
                    DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String stringDateObj = (String) docAttribute.getValue().toString();
                    Method getMethod;
                    try {
                        java.util.Date date = sourceFormat.parse(stringDateObj);
                        invoiceDocument.setInvoiceDate(date);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_PAY_DATE)) {
                if (docAttribute.getValue() != null) {
                    DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String stringDateObj = (String) docAttribute.getValue().toString();
                    Method getMethod;
                    try {
                        java.util.Date date = sourceFormat.parse(stringDateObj);
                        invoiceDocument.setInvoicePayDate(date);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
            if (name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_SUB_TYP_ID)) {
                if (docAttribute.getValue() != null) {
                    String stringDateObj = (String) docAttribute.getValue().toString();
                    invoiceDocument.setInvoiceSubTypeId(Integer.parseInt(stringDateObj));
                }
            }
            if (name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.PURAP_ID)) {
                if (invoiceDocument.getPurapDocumentIdentifier() != null && (!invoiceDocument.getPurapDocumentIdentifier().equalsIgnoreCase(""))) {
                    Set<String> hashSet = new HashSet<String>();
                    String purarIdList = "";
                    StringTokenizer stringTokenizer = new StringTokenizer(invoiceDocument.getPurapDocumentIdentifier(), ",");
                    while (stringTokenizer.hasMoreElements()) {
                        hashSet.add(stringTokenizer.nextElement().toString());
                    }
                    hashSet.add(((String) docAttribute.getValue().toString()).toString());
                    for (String s : hashSet) {
                        if (purarIdList.equalsIgnoreCase("")) {
                            purarIdList = s;
                        } else {
                            purarIdList = s + "," + purarIdList;
                        }
                    }
                    invoiceDocument.setPurapDocumentIdentifier(purarIdList);
                } else {
                    invoiceDocument.setPurapDocumentIdentifier(((String) docAttribute.getValue().toString()).toString());
                }

            }
            if (name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_TYP_ID)) {
                invoiceDocument.setInvoiceTypeId(((String) docAttribute.getValue().toString()));
            }
            if(name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_VND_NM)){
                invoiceDocument.setVendorName((String)docAttribute.getValue().toString());
            }
            if(name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_VND_NUM)){
                invoiceDocument.setVendorNumber((String)docAttribute.getValue().toString());
            }
            if(name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_NUMBER)){
                invoiceDocument.setInvoiceNumber((String)docAttribute.getValue().toString());
                invoiceDocument.setInvoiceNbr((String)docAttribute.getValue().toString());
            }

            if( name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_DOC_NUM)
                    || name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_TYP)
                    || name.equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_SUB_TYP)) {
                Method getMethod;
                try {
                    getMethod = getSetMethod(OLEInvoiceSearchDocument.class, name, new Class[]{String.class});
                    getMethod.invoke(invoiceDocument, docAttribute.getValue().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
        return invoiceDocument;
    }
    private Method getSetMethod(Class targetClass, String attr, Class[] objectAttributes) throws Exception {
        Method method = targetClass.getMethod("set" + StringUtils.capitalize(attr), objectAttributes);
        return method;
    }

    public static final List<String> getResultNames() {
        List<String> resultFields = new ArrayList<String>();
        resultFields.add(OleSelectConstant.InvoiceSearch.PURAP_ID);
        resultFields.add(OleSelectConstant.InvoiceSearch.INV_DATE);
        resultFields.add(OleSelectConstant.InvoiceSearch.INV_PAY_DATE);
        resultFields.add(OleSelectConstant.InvoiceSearch.INV_NUMBER);
        resultFields.add(OleSelectConstant.InvoiceSearch.INV_DOC_NUM);
        /*resultFields.add(OleSelectConstant.InvoiceSearch.INV_TYP);*/
        resultFields.add(OleSelectConstant.InvoiceSearch.INV_TYP_ID);
        resultFields.add(OleSelectConstant.InvoiceSearch.INV_SUB_TYP_ID);
        resultFields.add(OleSelectConstant.InvoiceSearch.INV_VND_NM);
        resultFields.add(OleSelectConstant.InvoiceSearch.INV_VND_NUM);

        return Collections.unmodifiableList(resultFields);
    }

    public static final Map<String, String> getInvoiceNames() {
        Map<String, String> invoiceFields = new HashMap<String, String>();
        invoiceFields.put(OleSelectConstant.InvoiceSearch.PURAP_ID,OleSelectConstant.InvoiceSearch.PURAP_ID);
        invoiceFields.put(OleSelectConstant.InvoiceSearch.INV_DATE, OleSelectConstant.InvoiceSearch.INV_DATE);
        invoiceFields.put(OleSelectConstant.InvoiceSearch.INV_PAY_DATE,OleSelectConstant.InvoiceSearch.INV_PAY_DATE);
        invoiceFields.put(OleSelectConstant.InvoiceSearch.INV_SUB_TYP_ID, OleSelectConstant.InvoiceSearch.INV_SUB_TYP_ID);
        invoiceFields.put(OleSelectConstant.InvoiceSearch.INV_NUMBER,OleSelectConstant.InvoiceSearch.INV_NUMBER);
        invoiceFields.put(OleSelectConstant.InvoiceSearch.INV_DOC_NUM,OleSelectConstant.InvoiceSearch.INV_DOC_NUM);
        /*invoiceFields.put(OleSelectConstant.InvoiceSearch.INV_TYP,OleSelectConstant.InvoiceSearch.INV_TYP);*/
        invoiceFields.put(OleSelectConstant.InvoiceSearch.INV_TYP_ID,OleSelectConstant.InvoiceSearch.INV_TYP_ID);
        invoiceFields.put(OleSelectConstant.InvoiceSearch.INV_VND_NM,OleSelectConstant.InvoiceSearch.INV_VND_NM);
        invoiceFields.put(OleSelectConstant.InvoiceSearch.INV_VND_NUM,OleSelectConstant.InvoiceSearch.INV_VND_NUM);

        return Collections.unmodifiableMap(invoiceFields);
    }


}
