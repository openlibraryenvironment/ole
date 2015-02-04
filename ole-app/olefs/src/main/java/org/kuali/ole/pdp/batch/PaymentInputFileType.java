/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.pdp.batch;

import java.io.File;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.service.AccountService;
import org.kuali.ole.pdp.PdpConstants;
import org.kuali.ole.pdp.PdpKeyConstants;
import org.kuali.ole.pdp.businessobject.LoadPaymentStatus;
import org.kuali.ole.pdp.businessobject.PaymentAccountDetail;
import org.kuali.ole.pdp.businessobject.PaymentDetail;
import org.kuali.ole.pdp.businessobject.PaymentFileLoad;
import org.kuali.ole.pdp.businessobject.PaymentGroup;
import org.kuali.ole.pdp.service.PaymentFileService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.batch.XmlBatchInputFileTypeBase;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;

/**
 * Batch input type for the PDP payment file.
 */
public class PaymentInputFileType extends XmlBatchInputFileTypeBase {
    private DateTimeService dateTimeService;
    private PaymentFileService paymentFileService;

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getFileName(org.kuali.rice.kim.api.identity.Person, java.lang.Object,
     *      java.lang.String)
     */
    public String getFileName(String principalName, Object parsedFileContents, String fileUserIdentifer) {
        Timestamp currentTimestamp = dateTimeService.getCurrentTimestamp();

        String fileName = PdpConstants.PDP_FILE_UPLOAD_FILE_PREFIX  + "_" + principalName;
        if (StringUtils.isNotBlank(fileUserIdentifer)) {
            fileName += "_" + StringUtils.remove(fileUserIdentifer, " ");
        }
        fileName += "_" + dateTimeService.toString(currentTimestamp, "yyyyMMdd_HHmmss");
        
        // remove spaces in filename
        fileName = StringUtils.remove(fileName, " ");

        return fileName;
    }
    
    /**
     * Empty function 
     * @param user who uploaded the file
     * @param parsedFileContents represents collector batch object
     * @param userIdentifier user identifier for user who uploaded file
     * @param destination path selected by user for storing the ole format xml.
     * @return String returns null
     */
    public String getFileName(String principalName, Object parsedFileContents, String userIdentifier,String destinationPath) {
        return null;
    }

    
    public String getAuthorPrincipalName(File file) {
        String[] fileNameParts = StringUtils.split(file.getName(), "_");
        if (fileNameParts.length > 3) {
            return fileNameParts[2];
        }
        return null;
    }

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getFileTypeIdentifer()
     */
    public String getFileTypeIdentifer() {
        return PdpConstants.PAYMENT_FILE_TYPE_INDENTIFIER;
    }

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileType#validate(java.lang.Object)
     */
    public boolean validate(Object parsedFileContents) {
        PaymentFileLoad paymentFile = (PaymentFileLoad) parsedFileContents;
        
        // add validation for chartCode-accountNumber, as chartCode is not required in xsd due to accounts-cant-cross-charts option        
        AccountService acctserv = SpringContext.getBean(AccountService.class);        
        boolean validAccounts = true;
        if (paymentFile.getPaymentGroups() != null) {
            for (PaymentGroup payGroup : paymentFile.getPaymentGroups()) {
                if (payGroup.getPaymentDetails() == null) continue;
                for (PaymentDetail payDetail : payGroup.getPaymentDetails()) {
                    if (payDetail.getAccountDetail() == null) continue;
                    for (PaymentAccountDetail acctDetail : payDetail.getAccountDetail()) {
                        // if chart code is empty while accounts cannot cross charts, then derive chart code from account number
                        if (StringUtils.isEmpty(acctDetail.getFinChartCode())) {
                            if (acctserv.accountsCanCrossCharts()) {
                                GlobalVariables.getMessageMap().putError(OLEConstants.GLOBAL_ERRORS, OLEKeyConstants.ERROR_BATCH_UPLOAD_FILE_EMPTY_CHART, acctDetail.getAccountNbr());
                                validAccounts = false;
                            }
                            else {
                                // accountNumber shall not be empty, otherwise won't pass schema validation
                                Account account = acctserv.getUniqueAccountForAccountNumber(acctDetail.getAccountNbr());
                                if (account != null) {
                                    acctDetail.setFinChartCode(account.getChartOfAccountsCode());
                                }       
                                else {
                                    GlobalVariables.getMessageMap().putError(OLEConstants.GLOBAL_ERRORS, OLEKeyConstants.ERROR_BATCH_UPLOAD_FILE_INVALID_ACCOUNT, acctDetail.getAccountNbr());
                                    validAccounts = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        paymentFileService.doPaymentFileValidation(paymentFile, GlobalVariables.getMessageMap());
        return validAccounts && paymentFile.isPassedValidation();
    }

    /**
     * @see org.kuali.ole.sys.batch.BatchInputType#getTitleKey()
     */
    public String getTitleKey() {
        return PdpKeyConstants.MESSAGE_BATCH_UPLOAD_TITLE_PAYMENT;
    }

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileTypeBase#process(java.lang.String, java.lang.Object)
     */
    @Override
    public void process(String fileName, Object parsedFileContents) {
        PaymentFileLoad paymentFile = (PaymentFileLoad) parsedFileContents;
        if (paymentFile.isPassedValidation()) {
            // collect various information for status of load
            LoadPaymentStatus status = new LoadPaymentStatus();
            status.setMessageMap(new MessageMap());

            paymentFileService.loadPayments(paymentFile, status, fileName);
            paymentFileService.createOutputFile(status, fileName);
        }
    }

    /**
     * Sets the paymentFileService attribute value.
     * 
     * @param paymentFileService The paymentFileService to set.
     */
    public void setPaymentFileService(PaymentFileService paymentFileService) {
        this.paymentFileService = paymentFileService;
    }

    /**
     * Sets the dateTimeService attribute value.
     * 
     * @param dateTimeService The dateTimeService to set.
     */
    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }
}

