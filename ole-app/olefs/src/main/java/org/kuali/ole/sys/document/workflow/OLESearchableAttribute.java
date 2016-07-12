/*
 * Copyright 2009 The Kuali Foundation
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
package org.kuali.ole.sys.document.workflow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.Organization;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.businessobject.ReceivingItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.ReceivingDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleCorrectionReceivingDocument;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OleLineItemReceivingDocument;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.businessobject.FinancialSystemDocumentHeader;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocument;
import org.kuali.ole.sys.document.AmountTotaling;
import org.kuali.ole.sys.document.GeneralLedgerPostingDocument;
import org.kuali.ole.sys.document.datadictionary.AccountingLineGroupDefinition;
import org.kuali.ole.sys.document.datadictionary.FinancialSystemTransactionalDocumentEntry;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.web.format.Formatter;
import org.kuali.rice.kew.api.KewApiConstants.SearchableAttributeConstants;
import org.kuali.rice.kew.api.document.DocumentWithContent;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeDecimal;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeString;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kns.lookup.LookupUtils;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.service.DictionaryValidationService;
import org.kuali.rice.kns.util.FieldUtils;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.kns.workflow.attribute.DataDictionarySearchableAttribute;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.ObjectUtils;

//RICE20 This class needs to be fixed to support pre-rice2.0 features
public class OLESearchableAttribute extends DataDictionarySearchableAttribute {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLESearchableAttribute.class);

    protected static final String DISPLAY_TYPE_SEARCH_ATTRIBUTE_LABEL = "Search Result Type";
    protected static final String WORKFLOW_DISPLAY_TYPE_LABEL = "Workflow Data";
    protected static final String DOCUMENT_DISPLAY_TYPE_LABEL = "Document Specific Data";
    protected static final String WORKFLOW_DISPLAY_TYPE_VALUE = "workflow";
    protected static final String DOCUMENT_DISPLAY_TYPE_VALUE = "document";
    protected static final String DISPLAY_TYPE_SEARCH_ATTRIBUTE_NAME = "displayType";

    protected static final List<KeyValue> SEARCH_ResultType_OPTION_LIST = new ArrayList<KeyValue>(2);
    static {
        SEARCH_ResultType_OPTION_LIST.add(new ConcreteKeyValue(DOCUMENT_DISPLAY_TYPE_VALUE, DOCUMENT_DISPLAY_TYPE_LABEL));
        SEARCH_ResultType_OPTION_LIST.add(new ConcreteKeyValue(WORKFLOW_DISPLAY_TYPE_VALUE, WORKFLOW_DISPLAY_TYPE_LABEL));
    }

    // used to map the special fields to the DD Entry that validate it.
    private static final Map<String, String> magicFields = new HashMap<String, String>();

    static {
        magicFields.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, SourceAccountingLine.class.getSimpleName());
        magicFields.put(OLEPropertyConstants.ORGANIZATION_CODE, Organization.class.getSimpleName());
        magicFields.put(OLEPropertyConstants.ACCOUNT_NUMBER, SourceAccountingLine.class.getSimpleName());
        magicFields.put(OLEPropertyConstants.FINANCIAL_DOCUMENT_TYPE_CODE, GeneralLedgerPendingEntry.class.getSimpleName());
        magicFields.put(OLEPropertyConstants.FINANCIAL_DOCUMENT_TOTAL_AMOUNT, FinancialSystemDocumentHeader.class.getSimpleName() );
    }

    @Override
    protected List<Row> getSearchingRows(String documentTypeName) {
        if ( LOG.isDebugEnabled() ) {
            LOG.debug( "getSearchingRows( " + documentTypeName + " )" );
            if ( LOG.isTraceEnabled() ) {
                LOG.trace("Stack Trace at point of call", new Throwable());
            }
        }

        DataDictionaryService ddService = SpringContext.getBean(DataDictionaryService.class);

        List<Row> docSearchRows = super.getSearchingRows(documentTypeName);

        DocumentEntry entry = ddService.getDataDictionary().getDocumentEntry(documentTypeName);

        if (entry == null) {
            return docSearchRows;
        }
        Class<? extends Document> docClass = entry.getDocumentClass();

        if (AccountingDocument.class.isAssignableFrom(docClass)) {
            Map<String, AccountingLineGroupDefinition> alGroups = ((FinancialSystemTransactionalDocumentEntry) entry).getAccountingLineGroups();
            Class alClass = SourceAccountingLine.class;

            if (ObjectUtils.isNotNull(alGroups)) {
                if (alGroups.containsKey("source")) {
                    alClass = alGroups.get("source").getAccountingLineClass();
                }
            }

            BusinessObject alBusinessObject = null;

            try {
                alBusinessObject = (BusinessObject) alClass.newInstance();
            } catch (Exception cnfe) {
                throw new RuntimeException( "Unable to instantiate accounting line class: " + alClass, cnfe);
            }

            Field chartField = FieldUtils.getPropertyField(alClass, OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, true);
            chartField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            if (OleInvoiceDocument.class.isAssignableFrom(docClass)) {
                chartField.setColumnVisible(false);
            } else {
                chartField.setColumnVisible(true);
            }
            LookupUtils.setFieldQuickfinder(alBusinessObject, OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartField, Collections.singletonList(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE));
            docSearchRows.add(new Row(Collections.singletonList(chartField)));

            Field orgField = FieldUtils.getPropertyField(Organization.class, OLEPropertyConstants.ORGANIZATION_CODE, true);
            orgField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            orgField.setFormatter(new Formatter());
            if (OleInvoiceDocument.class.isAssignableFrom(docClass)) {
                orgField.setColumnVisible(false);
            } else {
                orgField.setColumnVisible(true);
            }

            LookupUtils.setFieldQuickfinder(new Account(), OLEPropertyConstants.ORGANIZATION_CODE, orgField, Collections.singletonList(OLEPropertyConstants.ORGANIZATION_CODE));
            docSearchRows.add(new Row(Collections.singletonList(orgField)));

            Field accountField = FieldUtils.getPropertyField(alClass, OLEPropertyConstants.ACCOUNT_NUMBER, true);
            accountField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            if (OleInvoiceDocument.class.isAssignableFrom(docClass)) {
                accountField.setColumnVisible(false);
            } else {
                accountField.setColumnVisible(true);
            }
            LookupUtils.setFieldQuickfinder(alBusinessObject, OLEPropertyConstants.ACCOUNT_NUMBER, accountField, Collections.singletonList(OLEPropertyConstants.ACCOUNT_NUMBER));
            docSearchRows.add(new Row(Collections.singletonList(accountField)));
        }

        boolean displayedLedgerPostingDoc = false;

        if (GeneralLedgerPostingDocument.class.isAssignableFrom(docClass) && !displayedLedgerPostingDoc) {
            Field searchField = FieldUtils.getPropertyField(GeneralLedgerPendingEntry.class, OLEPropertyConstants.FINANCIAL_DOCUMENT_TYPE_CODE, true);
            searchField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            if (OleInvoiceDocument.class.isAssignableFrom(docClass)) {
                searchField.setColumnVisible(false);
            } else {
                searchField.setColumnVisible(true);
            }
            LookupUtils.setFieldQuickfinder(new GeneralLedgerPendingEntry(), OLEPropertyConstants.FINANCIAL_DOCUMENT_TYPE_CODE, searchField, Collections.singletonList(OLEPropertyConstants.FINANCIAL_DOCUMENT_TYPE_CODE));
            docSearchRows.add(new Row(Collections.singletonList(searchField)));
        }

        if (AmountTotaling.class.isAssignableFrom(docClass)) {
            Field searchField = FieldUtils.getPropertyField(FinancialSystemDocumentHeader.class, OLEPropertyConstants.FINANCIAL_DOCUMENT_TOTAL_AMOUNT, true);
            searchField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_FLOAT);
            docSearchRows.add(new Row(Collections.singletonList(searchField)));
        }

        if(RequisitionDocument.class.isAssignableFrom(docClass)) {

            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID,OleRequisitionItem.class.getSimpleName());
            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID,OleRequisitionItem.class.getSimpleName());
            Class boClass = OleRequisitionItem.class;

            Field searchField = FieldUtils.getPropertyField(boClass, OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, true);
            searchField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            searchField.setColumnVisible(false);


            Field searchFieldLocalTitleId = FieldUtils.getPropertyField(boClass, OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID, true);
            searchFieldLocalTitleId.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);

            //searchField.setFieldType("multibox");

           /* List<Field> fieldList = new ArrayList<Field>();
            fieldList.add(searchField);*/
            docSearchRows.add(new Row(Collections.singletonList(searchField)));
            docSearchRows.add(new Row(Collections.singletonList(searchFieldLocalTitleId)));

        }

        if(PurchaseOrderDocument.class.isAssignableFrom(docClass)) {
            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID,OlePurchaseOrderItem.class.getSimpleName());
            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID,OlePurchaseOrderItem.class.getSimpleName());
            magicFields.put(OleSelectConstant.AcquisitionsSearch.DONOR_CODES,OlePurchaseOrderItem.class.getSimpleName());
            Class boClass = OlePurchaseOrderItem.class;

            Field searchField = FieldUtils.getPropertyField(boClass, OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, true);
            searchField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            searchField.setColumnVisible(false);

            Field searchFieldLocalTitleId = FieldUtils.getPropertyField(boClass, OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID, true);
            searchFieldLocalTitleId.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);

            Field searchFieldDonorCode = FieldUtils.getPropertyField(boClass, OleSelectConstant.AcquisitionsSearch.DONOR_CODES, true);
            searchFieldDonorCode.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            searchFieldDonorCode.setColumnVisible(false);

            //searchField.setFieldType("multibox");
            List<Field> fieldList = new ArrayList<Field>();
            fieldList.add(searchField);
            fieldList.add(searchFieldLocalTitleId);
            fieldList.add(searchFieldDonorCode);
            docSearchRows.add(new Row(fieldList));

        }
        if(OlePaymentRequestDocument.class.isAssignableFrom(docClass)) {
            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID,OlePaymentRequestItem.class.getSimpleName());
            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID,OlePaymentRequestItem.class.getSimpleName());
            Class boClass = OlePaymentRequestItem.class;

            Field searchField = FieldUtils.getPropertyField(boClass, OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, true);
            searchField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            searchField.setColumnVisible(false);

            Field searchFieldLocalTitleId = FieldUtils.getPropertyField(boClass, OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID, true);
            searchFieldLocalTitleId.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            /*List<Field> fieldList = new ArrayList<Field>();
            fieldList.add(searchField);*/
            docSearchRows.add(new Row(Collections.singletonList(searchField)));
            docSearchRows.add(new Row(Collections.singletonList(searchFieldLocalTitleId)));

        }
        if (OleLineItemReceivingDocument.class.isAssignableFrom(docClass)) {
            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, OleLineItemReceivingItem.class.getSimpleName());
            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID, OleLineItemReceivingItem.class.getSimpleName());
            Class boClass = OleLineItemReceivingItem.class;

            Field searchField = FieldUtils.getPropertyField(boClass, OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, true);
            searchField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            searchField.setColumnVisible(false);

            Field searchFieldLocalTitleId = FieldUtils.getPropertyField(boClass,OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID, true);
            searchFieldLocalTitleId.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
                /*List<Field> fieldList = new ArrayList<Field>();
                fieldList.add(searchField);*/
            docSearchRows.add(new Row(Collections.singletonList(searchField)));
            docSearchRows.add(new Row(Collections.singletonList(searchFieldLocalTitleId)));
        }
        if (OleCorrectionReceivingDocument.class.isAssignableFrom(docClass)) {

            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, OleCorrectionReceivingItem.class.getSimpleName());
            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID, OleCorrectionReceivingItem.class.getSimpleName());
            Class boClass = OleCorrectionReceivingItem.class;

            Field searchField = FieldUtils.getPropertyField(boClass, OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, true);
            searchField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            searchField.setColumnVisible(false);

            Field searchFieldLocalTitleId = FieldUtils.getPropertyField(boClass, OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID, true);
            searchFieldLocalTitleId.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
                /*List<Field> fieldList = new ArrayList<Field>();
                fieldList.add(searchField);*/
            docSearchRows.add(new Row(Collections.singletonList(searchField)));
            docSearchRows.add(new Row(Collections.singletonList(searchFieldLocalTitleId)));
        }
        if (OleInvoiceDocument.class.isAssignableFrom(docClass)) {
            Class boClass = OleInvoiceDocument.class;
            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, OleInvoiceItem.class.getSimpleName());
            magicFields.put(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID, OleInvoiceItem.class.getSimpleName());
            Class itemClass = OleInvoiceItem.class;


            Field searchResultInvDateField = FieldUtils.getPropertyField(boClass, OleSelectConstant.InvoiceSearch.PO_DIS_INV_DT, true);
            searchResultInvDateField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_DATE);
            docSearchRows.add(new Row(Collections.singletonList(searchResultInvDateField)));


            Field poDocNumbersField = FieldUtils.getPropertyField(boClass, OleSelectConstant.InvoiceSearch.PO_DOC_NUMS, true);
            poDocNumbersField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            Row poNumRow = new Row(Collections.singletonList(poDocNumbersField));
            poNumRow.setHidden(true);
            docSearchRows.add(poNumRow);

            Field itemSearchField = FieldUtils.getPropertyField(itemClass, OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, true);
            itemSearchField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            itemSearchField.setColumnVisible(false);

            Field itemSearchLocalTitleField = FieldUtils.getPropertyField(itemClass, OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID, true);
            itemSearchLocalTitleField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            itemSearchLocalTitleField.setColumnVisible(false);

            docSearchRows.add(new Row(Collections.singletonList(itemSearchField)));
            docSearchRows.add(new Row(Collections.singletonList(itemSearchLocalTitleField)));

            magicFields.put(OleSelectConstant.InvoiceSearch.PO_ID, OleInvoiceItem.class.getSimpleName());

            Field poDocNumField = FieldUtils.getPropertyField(boClass, OleSelectConstant.InvoiceSearch.PO_ID, true);
            poDocNumField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_STRING);
            poDocNumField.setColumnVisible(false);
            docSearchRows.add(new Row(Collections.singletonList(poDocNumField)));

            // JIRA-4919


            Field searchResultInvPayDateField = FieldUtils.getPropertyField(boClass, OleSelectConstant.InvoiceSearch.PO_DIS_INV_PAY_DT, true);
            searchResultInvPayDateField.setFieldDataType(SearchableAttributeConstants.DATA_TYPE_DATE);
            docSearchRows.add(new Row(Collections.singletonList(searchResultInvPayDateField)));

            //magicFields.put(OleSelectConstant.InvoiceSearch.PO_DOC_NUMS, boClass.getSimpleName());

            for (Row row : docSearchRows) {
                List<Field> fields= row.getFields();
                Field field=fields.get(0);
                if(field.getPropertyName().equalsIgnoreCase(OleSelectConstant.InvoiceSearch.ORG_DOC_NUMBER)){
                    field.setColumnVisible(false);
                }
                if(field.getPropertyName().equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_DATE)){
                    field.setColumnVisible(false);
                }
                if(field.getPropertyName().equalsIgnoreCase(OleSelectConstant.InvoiceSearch.INV_PAY_DATE)){
                    field.setColumnVisible(false);
                }
                if(field.getFieldDataType().equalsIgnoreCase(SearchableAttributeConstants.DATA_TYPE_DATE)) {
                    field.setMaxLength(10);
                }
            }
        }

        Row resultType = createSearchResultDisplayTypeRow();
        docSearchRows.add(resultType);
        if ( LOG.isDebugEnabled() ) {
            LOG.debug( "Returning Rows: " + docSearchRows );
        }
        return docSearchRows;
    }





    @Override
    public List<DocumentAttribute> extractDocumentAttributes(ExtensionDefinition extensionDefinition, DocumentWithContent documentWithContent) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("extractDocumentAttributes( " + extensionDefinition + ", " + documentWithContent + " )");
        }
        List<DocumentAttribute> searchAttrValues = super.extractDocumentAttributes(extensionDefinition, documentWithContent);

        String docId = documentWithContent.getDocument().getDocumentId();
        DocumentService docService = SpringContext.getBean(DocumentService.class);
        Document doc = null;
        try {
            doc = docService.getByDocumentHeaderIdSessionless(docId);
        }
        catch (WorkflowException we) {

        }
        if (doc != null) {
            if (doc instanceof AmountTotaling) {
                DocumentAttributeDecimal.Builder searchableAttributeValue = DocumentAttributeDecimal.Builder.create(OLEPropertyConstants.FINANCIAL_DOCUMENT_TOTAL_AMOUNT);
                searchableAttributeValue.setValue(((AmountTotaling) doc).getTotalDollarAmount().bigDecimalValue());
                searchAttrValues.add(searchableAttributeValue.build());
            }

            if (doc instanceof AccountingDocument) {
                AccountingDocument accountingDoc = (AccountingDocument) doc;
                searchAttrValues.addAll(harvestAccountingDocumentSearchableAttributes(accountingDoc));
            }

            boolean indexedLedgerDoc = false;

            if (doc instanceof GeneralLedgerPostingDocument && !indexedLedgerDoc) {
                GeneralLedgerPostingDocument GLPostingDoc = (GeneralLedgerPostingDocument) doc;
                searchAttrValues.addAll(harvestGLPDocumentSearchableAttributes(GLPostingDoc));
            }

            if (doc instanceof PurchasingAccountsPayableDocument) {
                PurchasingAccountsPayableDocument purchasingAccountsPayableDocument = (PurchasingAccountsPayableDocument)doc;
                searchAttrValues.addAll(harvestPurchasingAccountsPayableDocument(purchasingAccountsPayableDocument));
            }

            if (doc instanceof OleLineItemReceivingDocument |
                    doc instanceof OleCorrectionReceivingDocument) {
                ReceivingDocument receivingDocument = (ReceivingDocument)doc;
                searchAttrValues.addAll(harvestReceivingDocument(receivingDocument));
            }
            if(doc instanceof OleInvoiceDocument){
                StringBuffer purchaseOrderDocumentNums = new StringBuffer();
                OleInvoiceDocument invoiceDocument = (OleInvoiceDocument) doc;
              //  KualiDecimal invoicedAmount = KualiDecimal.ZERO;
                for (Object purApItem : invoiceDocument.getItems()) {
                    OleInvoiceItem invoiceItem = (OleInvoiceItem) purApItem;
                    if (invoiceItem.getPurchaseOrderIdentifier() != null) {
                        purchaseOrderDocumentNums.append(invoiceItem.getPurchaseOrderIdentifier().toString() + ",");
                    }
                   /* if (invoiceItem.isDebitItem()) {
                        invoicedAmount = invoicedAmount.add(invoiceItem.getItemListPrice().multiply(new KualiDecimal(invoiceItem.getOleCopiesOrdered())));
                    } else {
                        invoicedAmount = invoicedAmount.subtract(invoiceItem.getItemListPrice().multiply(new KualiDecimal(invoiceItem.getOleCopiesOrdered())));
                    }*/
                }
                /*for(int i=0;i<searchAttrValues.size();i++) {
                    if (searchAttrValues.get(i).getName().equals(OLEPropertyConstants.FIN_DOC_TOT_AMT)) {
                        searchAttrValues.remove(i);
                        DocumentAttributeDecimal.Builder invDebitItemSearchableAttributeValue = DocumentAttributeDecimal.Builder.create(OLEPropertyConstants.FIN_DOC_TOT_AMT);
                        invDebitItemSearchableAttributeValue.setValue(invoicedAmount.bigDecimalValue());
                        searchAttrValues.add(invDebitItemSearchableAttributeValue.build());
                    }
                }*/

                int len = purchaseOrderDocumentNums.lastIndexOf(",");
                if (len > 0) {
                    purchaseOrderDocumentNums.replace(len, len + 1, " ");
                }
                DocumentAttributeString.Builder poDocNumSearchableAttributeValue = DocumentAttributeString.Builder.create(OleSelectConstant.InvoiceSearch.PO_DOC_NUMS);
                poDocNumSearchableAttributeValue.setValue(purchaseOrderDocumentNums.toString());
                searchAttrValues.add(poDocNumSearchableAttributeValue.build());

                DateFormat sourceFormat = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE);

                String invoiceDate = sourceFormat.format(invoiceDocument.getInvoiceDate());
                String invoicePayDate = sourceFormat.format(invoiceDocument.getInvoicePayDate());

                DocumentAttributeString.Builder invDateSearchableAttributeValue = DocumentAttributeString.Builder.create(OleSelectConstant.InvoiceSearch.PO_DIS_INV_DT);
                invDateSearchableAttributeValue.setValue(invoiceDate);
                searchAttrValues.add(invDateSearchableAttributeValue.build());

                DocumentAttributeString.Builder invPayDateSearchableAttributeValue = DocumentAttributeString.Builder.create(OleSelectConstant.InvoiceSearch.PO_DIS_INV_PAY_DT);
                invPayDateSearchableAttributeValue.setValue(invoicePayDate);
                searchAttrValues.add(invPayDateSearchableAttributeValue.build());
            }
        }
        return searchAttrValues;
    }





    @Override
    public List<RemotableAttributeError> validateDocumentAttributeCriteria(ExtensionDefinition extensionDefinition, DocumentSearchCriteria documentSearchCriteria) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("validateDocumentAttributeCriteria( " + extensionDefinition + ", " + documentSearchCriteria + " )");
        }
        // this list is irrelevant. the validation errors are put on the stack in the validationService.
        List<RemotableAttributeError> errors =  super.validateDocumentAttributeCriteria(extensionDefinition, documentSearchCriteria);

        DictionaryValidationService validationService = SpringContext.getBean(DictionaryValidationService.class);
        Map<String,List<String>> paramMap = documentSearchCriteria.getDocumentAttributeValues();
        for (String key : paramMap.keySet()) {
            List<String> values = paramMap.get(key);
            if ( values != null && !values.isEmpty() ) {
                for ( String value : values ) {
                    if (!StringUtils.isEmpty(value)) {
                        if (magicFields.containsKey(key)) {
                            validationService.validateAttributeFormat(magicFields.get(key), key, value, key);
                        }
                    }
                }
            }
        }

        retrieveValidationErrorsFromGlobalVariables(errors);

        return errors;
    };

    /**
     * Harvest chart of accounts code, account number, and organization code as searchable attributes from an accounting document
     * @param accountingDoc the accounting document to pull values from
     * @return a List of searchable values
     */
    protected List<DocumentAttribute> harvestAccountingDocumentSearchableAttributes(AccountingDocument accountingDoc) {
        List<DocumentAttribute> searchAttrValues = new ArrayList<DocumentAttribute>();

        for ( AccountingLine line : (List<AccountingLine>)accountingDoc.getSourceAccountingLines() ) {
            addSearchableAttributesForAccountingLine(searchAttrValues, line);
        }
        for ( AccountingLine line : (List<AccountingLine>)accountingDoc.getTargetAccountingLines() ) {
            addSearchableAttributesForAccountingLine(searchAttrValues, line);
        }

        return searchAttrValues;
    }

    /**
     * Harvest GLPE document type as searchable attributes from a GL posting document
     * @param doc the GLP document to pull values from
     * @return a List of searchable values
     */
    protected List<DocumentAttribute> harvestGLPDocumentSearchableAttributes(GeneralLedgerPostingDocument doc) {
        List<DocumentAttribute> searchAttrValues = new ArrayList<DocumentAttribute>();

        for ( GeneralLedgerPendingEntry glpe : doc.getGeneralLedgerPendingEntries() ) {
            addSearchableAttributesForGLPE(searchAttrValues, glpe);
        }
        return searchAttrValues;
    }

    /**
     * Harvest itemTitle Id searchable attributes from the Purchasing Accounts Payable Document
     * @param purapDoc the PurchasingAccountsPyable Document to pull values from
     * @return a List of searchable values
     */
    protected List<DocumentAttribute> harvestPurchasingAccountsPayableDocument(PurchasingAccountsPayableDocument purapDoc) {
        List<DocumentAttribute> searchAttrValues = new ArrayList<DocumentAttribute>();
        for (Object element : purapDoc.getItems()) {
            PurApItem purapItem = (PurApItem)element;
            addSearchableAttributesForPurApItemTitleId(searchAttrValues, purapItem);
        }
        return searchAttrValues;
    }

    /**
     * Harvest itemTitle Id searchable attributes from the Receiving Document
     * @param receivingDoc the Receiving Document to pull values from
     * @return a List of searchable values
     */
    protected List<DocumentAttribute> harvestReceivingDocument(ReceivingDocument receivingDoc) {
        List<DocumentAttribute> searchAttrValues = new ArrayList<DocumentAttribute>();
        for (Iterator itr = receivingDoc.getItems().iterator(); itr.hasNext();) {
            ReceivingItem receivingItem = (ReceivingItem)itr.next();
            addSearchableAttributesForReceivingItemTitleIds(searchAttrValues, receivingItem);
        }
        return searchAttrValues;
    }

    /**
     * Pulls the default searchable attributes - chart code, account number, and account organization code - from a given accounting line and populates
     * the searchable attribute values in the given list
     * @param searchAttrValues a List of SearchableAttributeValue objects to populate
     * @param accountingLine an AccountingLine to get values from
     */
    protected void addSearchableAttributesForAccountingLine(List<DocumentAttribute> searchAttrValues, AccountingLine accountingLine) {
        DocumentAttributeString.Builder searchableAttributeValue = DocumentAttributeString.Builder.create(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE);
        searchableAttributeValue.setValue(accountingLine.getChartOfAccountsCode());
        searchAttrValues.add(searchableAttributeValue.build());

        searchableAttributeValue = DocumentAttributeString.Builder.create(OLEPropertyConstants.ACCOUNT_NUMBER);
        searchableAttributeValue.setValue(accountingLine.getAccountNumber());
        searchAttrValues.add(searchableAttributeValue.build());

        searchableAttributeValue = DocumentAttributeString.Builder.create(OLEPropertyConstants.ORGANIZATION_CODE);
        searchableAttributeValue.setValue(accountingLine.getAccount().getOrganizationCode());
        searchAttrValues.add(searchableAttributeValue.build());
    }

    /**
     * Pulls the default searchable attribute - financialSystemTypeCode - from a given accounting line and populates
     * the searchable attribute values in the given list
     * @param searchAttrValues a List of SearchableAttributeValue objects to populate
     * @param glpe a GeneralLedgerPendingEntry to get values from
     */
    protected void addSearchableAttributesForGLPE(List<DocumentAttribute> searchAttrValues, GeneralLedgerPendingEntry glpe) {
        DocumentAttributeString.Builder searchableAttributeValue = DocumentAttributeString.Builder.create(OLEPropertyConstants.FINANCIAL_DOCUMENT_TYPE_CODE);
        searchableAttributeValue.setValue(glpe.getFinancialDocumentTypeCode());
        searchAttrValues.add(searchableAttributeValue.build());

    }

    /**
     * Pulls the default searchable attribute - itemTitleId - from a given PurApItem and populates
     * the searchable attribute values in the given list
     * @param searchAttrValues a List of SearchableAttributeValue objects to populate
     * @param purapItem a PurApItem to get values from
     */
    protected void addSearchableAttributesForPurApItemTitleId(List<DocumentAttribute> searchAttrValues, PurApItem purapItem) {
        if (purapItem instanceof OleRequisitionItem ) {
            DocumentAttributeString.Builder searchableAttributeValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID);
            searchableAttributeValue.setValue(((OleRequisitionItem) purapItem).getItemTitleId());

            DocumentAttributeString.Builder searchableAttributeLocalValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID);
            if(((OleRequisitionItem) purapItem).getItemTitleId()!=null){
                searchableAttributeLocalValue.setValue(DocumentUniqueIDPrefix.getDocumentId(((OleRequisitionItem) purapItem).getItemTitleId()));
            }
            searchAttrValues.add(searchableAttributeValue.build());
            searchAttrValues.add(searchableAttributeLocalValue.build());
        }
        else if (purapItem instanceof OlePaymentRequestItem) {
            DocumentAttributeString.Builder searchableAttributeValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID);
            searchableAttributeValue.setValue(((OlePaymentRequestItem)purapItem).getItemTitleId());

            DocumentAttributeString.Builder searchableAttributeLocalValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID);
            if(((OlePaymentRequestItem) purapItem).getItemTitleId()!=null){
                searchableAttributeLocalValue.setValue(DocumentUniqueIDPrefix.getDocumentId(((OlePaymentRequestItem)purapItem).getItemTitleId()));
            }
            searchAttrValues.add(searchableAttributeLocalValue.build());
            searchAttrValues.add(searchableAttributeValue.build());
        }
        else if (purapItem instanceof OlePurchaseOrderItem){
            DocumentAttributeString.Builder searchableAttributeValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID);
            searchableAttributeValue.setValue(((OlePurchaseOrderItem) purapItem).getItemTitleId());
            for(OLELinkPurapDonor oleDonor:((OlePurchaseOrderItem) purapItem).getOleDonors()){

                DocumentAttributeString.Builder searchableAttributeDonorValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.DONOR_CODES);
                searchableAttributeDonorValue.setValue(oleDonor.getDonorCode());
                searchAttrValues.add(searchableAttributeDonorValue.build());
            }
            DocumentAttributeString.Builder searchableAttributeLocalValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID);
            if(((OlePurchaseOrderItem) purapItem).getItemTitleId()!=null){
                searchableAttributeLocalValue.setValue(DocumentUniqueIDPrefix.getDocumentId(((OlePurchaseOrderItem) purapItem).getItemTitleId()));
            }
            searchAttrValues.add(searchableAttributeLocalValue.build());
            searchAttrValues.add(searchableAttributeValue.build());
        }
        else if (purapItem instanceof OleInvoiceItem){
            DocumentAttributeString.Builder searchableAttributeValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID);
            searchableAttributeValue.setValue(((OleInvoiceItem)purapItem).getItemTitleId());

            DocumentAttributeString.Builder searchableAttributeLocalValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID);
            if(((OleInvoiceItem) purapItem).getItemTitleId()!=null){
                searchableAttributeLocalValue.setValue(DocumentUniqueIDPrefix.getDocumentId(((OleInvoiceItem)purapItem).getItemTitleId()));
            }
            searchAttrValues.add(searchableAttributeValue.build());
            searchAttrValues.add(searchableAttributeLocalValue.build());

            if (((OleInvoiceItem)purapItem).getPurchaseOrderIdentifier() != null) {
                DocumentAttributeString.Builder poDocNumSearchableAttributeValue = DocumentAttributeString.Builder.create(OleSelectConstant.InvoiceSearch.PO_ID);
                poDocNumSearchableAttributeValue.setValue(((OleInvoiceItem)purapItem).getPurchaseOrderIdentifier().toString());
                searchAttrValues.add(poDocNumSearchableAttributeValue.build());
            }


        }
    }

    /**
     * Pulls the default searchable attribute - itemTitleId - from a given ReceivingItem and populates
     * the searchable attribute values in the given list
     * @param searchAttrValues a List of SearchableAttributeValue objects to populate
     * @param receivingItem a ReceivingItem to get values from
     */
    protected void addSearchableAttributesForReceivingItemTitleIds(List<DocumentAttribute> searchAttrValues, ReceivingItem receivingItem) {
        if(receivingItem instanceof OleCorrectionReceivingItem) {
            DocumentAttributeString.Builder searchableAttributeValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID);
            searchableAttributeValue.setValue(((OleCorrectionReceivingItem)receivingItem).getItemTitleId());

            DocumentAttributeString.Builder searchableAttributeLocalValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID);
            if(((OleCorrectionReceivingItem) receivingItem).getItemTitleId()!=null){
                searchableAttributeLocalValue.setValue(DocumentUniqueIDPrefix.getDocumentId(((OleCorrectionReceivingItem)receivingItem).getItemTitleId()));
            }
            searchAttrValues.add(searchableAttributeValue.build());
            searchAttrValues.add(searchableAttributeLocalValue.build());
        }
        else if(receivingItem instanceof OleLineItemReceivingItem) {
            DocumentAttributeString.Builder searchableAttributeValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID);
            searchableAttributeValue.setValue(((OleLineItemReceivingItem)receivingItem).getItemTitleId());

            DocumentAttributeString.Builder searchableAttributeLocalValue = DocumentAttributeString.Builder.create(OleSelectConstant.AcquisitionsSearch.ITEM_LOCAL_TITLE_ID);
            if(((OleLineItemReceivingItem) receivingItem).getItemTitleId()!=null){
                searchableAttributeLocalValue.setValue(DocumentUniqueIDPrefix.getDocumentId(((OleLineItemReceivingItem)receivingItem).getItemTitleId()));
            }
            searchAttrValues.add(searchableAttributeLocalValue.build());
            searchAttrValues.add(searchableAttributeValue.build());
        }
    }

    protected Row createSearchResultDisplayTypeRow() {
        Field searchField = new Field(DISPLAY_TYPE_SEARCH_ATTRIBUTE_NAME, DISPLAY_TYPE_SEARCH_ATTRIBUTE_LABEL);
        searchField.setFieldType(Field.RADIO);
        searchField.setIndexedForSearch(false);
        searchField.setBusinessObjectClassName("");
        searchField.setFieldHelpName("");
        searchField.setFieldHelpSummary("");
        searchField.setColumnVisible(false);
        searchField.setFieldValidValues(SEARCH_ResultType_OPTION_LIST);
        searchField.setPropertyValue(DOCUMENT_DISPLAY_TYPE_VALUE);
        searchField.setDefaultValue(DOCUMENT_DISPLAY_TYPE_VALUE);

        return new Row(Collections.singletonList(searchField));
    }


    // RICE20: fixes to allow document search to function until Rice 2.0.1
//    @Override
//    public List<RemotableAttributeField> getSearchFields(ExtensionDefinition extensionDefinition, String documentTypeName) {
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("getSearchFields( " + extensionDefinition + ", " + documentTypeName + " )");
//        }
//        List<Row> searchRows = getSearchingRows(documentTypeName);
//        for ( Row row : searchRows ) {
//            for ( Field field : row.getFields() ) {
//                if ( field.getFieldType().equals(Field.CURRENCY) ) {
//                    field.setFieldType(Field.TEXT);
//                }
//                if ( field.getMaxLength() < 1 ) {
//                    field.setMaxLength(100);
//                }
//            }
//        }
//        return FieldUtils.convertRowsToAttributeFields(searchRows);
//    }
}
