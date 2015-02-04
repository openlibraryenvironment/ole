/*
 * Copyright 2006 The Kuali Foundation
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
package org.kuali.ole.fp.document.web.struts;

import static org.kuali.ole.fp.document.validation.impl.ProcurementCardDocumentRuleConstants.DISPUTE_URL_PARM_NM;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.fp.businessobject.CapitalAssetInformation;
import org.kuali.ole.fp.businessobject.ProcurementCardTargetAccountingLine;
import org.kuali.ole.fp.businessobject.ProcurementCardTransactionDetail;
import org.kuali.ole.fp.document.CapitalAssetEditable;
import org.kuali.ole.fp.document.ProcurementCardDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.TargetAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.authorization.AccountingLineAuthorizer;
import org.kuali.ole.sys.document.datadictionary.AccountingLineGroupDefinition;
import org.kuali.ole.sys.document.datadictionary.FinancialSystemTransactionalDocumentEntry;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * This class is the form class for the ProcurementCard document. This method extends the parent KualiTransactionalDocumentFormBase
 * class which contains all of the common form methods and form attributes needed by the Procurment Card document.
 */
public class ProcurementCardForm extends CapitalAccountingLinesFormBase implements CapitalAssetEditable{
    protected static final long serialVersionUID = 1L;
    protected List<ProcurementCardTargetAccountingLine> newTargetLines;
    protected List<Boolean> transactionCreditCardNumbersViewStatus;
    protected final static String TARGET_ACCOUNTING_LINE_GROUP_NAME = "target";
    
    protected List<CapitalAssetInformation> capitalAssetInformation;

    /**
     * Override to accomodate multiple target lines.
     * 
     * @see org.kuali.rice.kns.web.struts.pojo.PojoForm#populate(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void populate(HttpServletRequest request) {
        super.populate(request);

        //
        // now run through all of the accounting lines and make sure they've been uppercased and populated appropriately

        // handle new accountingLine, if one is being added
        final String methodToCall = this.getMethodToCall();
        final Map parameterMap = request.getParameterMap();
        if (StringUtils.isNotBlank(methodToCall)) {
            if (methodToCall.equals(OLEConstants.INSERT_SOURCE_LINE_METHOD)) {
                populateSourceAccountingLine(getNewSourceLine(), OLEPropertyConstants.NEW_SOURCE_LINE, parameterMap);
            }

            if (methodToCall.equals(OLEConstants.INSERT_TARGET_LINE_METHOD)) {
                // This is the addition for the override: Handle multiple accounting lines ...
                for (Iterator newTargetLinesIter = getNewTargetLines().iterator(); newTargetLinesIter.hasNext();) {
                    TargetAccountingLine targetAccountingLine = (TargetAccountingLine) newTargetLinesIter.next();
                    populateTargetAccountingLine(targetAccountingLine, OLEPropertyConstants.NEW_TARGET_LINE, parameterMap);
                }
            }
        }

        // don't call populateAccountingLines if you are copying or errorCorrecting a document,
        // since you want the accountingLines in the copy to be "identical" to those in the original
        if (!StringUtils.equals(methodToCall, OLEConstants.COPY_METHOD) && !StringUtils.equals(methodToCall, OLEConstants.ERRORCORRECT_METHOD)) {
            populateAccountingLines(parameterMap);
        }

        setDocTypeName(discoverDocumentTypeName());
    }

    /**
     * Constructs a ProcurmentCardForm instance and sets up the appropriately casted document. Also, the newSourceLine needs to be
     * the extended ProcurementCardSourceAccountingLine, for the additional trans line nbr.
     */
    public ProcurementCardForm() {
        super();
        
        this.newTargetLines = new ArrayList<ProcurementCardTargetAccountingLine>();
       // buildNewTargetAccountingLines();
        capitalAssetInformation = new ArrayList<CapitalAssetInformation>();
    }

    public void buildNewTargetAccountingLines(int transactionsCount) {
        for (int i=0; i < transactionsCount; i++) {
            ProcurementCardTargetAccountingLine newLine = new ProcurementCardTargetAccountingLine();
            newLine.setTransactionContainerIndex(i);
            this.newTargetLines.add(i, newLine);
        }
    }
    
    @Override
    protected String getDefaultDocumentTypeName() {
        return "OLE_PCDO";
    }
    
    /**
     * @return The retreived APC string used for the dispute url.
     */
    public String getDisputeURL() {
        return SpringContext.getBean(ParameterService.class).getParameterValueAsString(ProcurementCardDocument.class, DISPUTE_URL_PARM_NM);
    }


    /**
     * @return Returns the newTargetLines.
     */
    public List getNewTargetLines() {
        return newTargetLines;
    }

    /**
     * @param newTargetLines The newTargetLines to set.
     */
    public void setNewTargetLines(List newTargetLines) {
        this.newTargetLines = newTargetLines;
    }
    
    /**
     * @see org.kuali.ole.fp.document.CapitalAssetEditable#getCapitalAssetInformation()
     */
    public List<CapitalAssetInformation> getCapitalAssetInformation() {
        return this.capitalAssetInformation;
    }

    /**
     * @see org.kuali.ole.fp.document.CapitalAssetEditable#setCapitalAssetInformation(org.kuali.ole.fp.businessobject.CapitalAssetInformation)
     */
    public void setCapitalAssetInformation(List<CapitalAssetInformation> capitalAssetInformation) {
        this.capitalAssetInformation = capitalAssetInformation;        
    }
    
    /**
     * @return an array, parallel to the ProcurementCardDocument#getTransactionEntries, which holds whether the
     * current user can see the credit card number or not
     */
    public List<Boolean> getTransactionCreditCardNumbersViewStatus() {
        if (this.transactionCreditCardNumbersViewStatus == null) {
            populateTransactionCreditCardNumbersViewStatuses();
        }
        return transactionCreditCardNumbersViewStatus;
    }
    
    /**
     * populates an array, parallel to the ProcurementCardDocument#getTransactionEntries, which holds whether the
     * current user can see the credit card number or not - based on if any of the accounting lines are editable to
     * the user or not...
     */
    protected void populateTransactionCreditCardNumbersViewStatuses() {
        final AccountingLineAuthorizer accountingLineAuthorizer = getAccountingLineAuthorizerForDocument();
        final Person currentUser = GlobalVariables.getUserSession().getPerson();
        transactionCreditCardNumbersViewStatus = new ArrayList<Boolean>();
        
        for (Object transactionEntryAsObject : ((ProcurementCardDocument)getDocument()).getTransactionEntries()) {
            final ProcurementCardTransactionDetail transactionDetail = (ProcurementCardTransactionDetail)transactionEntryAsObject;
            Boolean canEditAnyAccountingLine = Boolean.FALSE;
                
            int count = 0;
            while (!canEditAnyAccountingLine.booleanValue() && count < transactionDetail.getTargetAccountingLines().size()) {
                final TargetAccountingLine accountingLine = (TargetAccountingLine)transactionDetail.getTargetAccountingLines().get(count);
                if (accountingLineAuthorizer.hasEditPermissionOnAccountingLine(((ProcurementCardDocument)getDocument()), accountingLine, getAccountingLineCollectionName(), currentUser, getDocumentActions().containsKey(KRADConstants.KUALI_ACTION_CAN_EDIT))) {
                    canEditAnyAccountingLine = Boolean.TRUE;
                }
                count += 1;
            }
            transactionCreditCardNumbersViewStatus.add(canEditAnyAccountingLine);
        }
    }
    
    /**
     * @return the accounting line authorizer for the target lines of this document
     */
    protected AccountingLineAuthorizer getAccountingLineAuthorizerForDocument() {
        final DataDictionaryService dataDictionaryService = SpringContext.getBean(DataDictionaryService.class);
        final DataDictionary dataDictionary = dataDictionaryService.getDataDictionary();
        final String documentTypeCode = dataDictionaryService.getDocumentTypeNameByClass(this.getDocument().getClass());
        final FinancialSystemTransactionalDocumentEntry documentEntry = (FinancialSystemTransactionalDocumentEntry)dataDictionary.getDocumentEntry(documentTypeCode);
        final AccountingLineGroupDefinition targetAccountingLineGroupDefinition = documentEntry.getAccountingLineGroups().get(ProcurementCardForm.TARGET_ACCOUNTING_LINE_GROUP_NAME);
        return targetAccountingLineGroupDefinition.getAccountingLineAuthorizer();
    }
    
    /**
     * @return the name of the accounting line collection for the permission check
     */
    protected String getAccountingLineCollectionName() {
        // we'll just cheat...
        return "targetAccountingLines";
    }
}
