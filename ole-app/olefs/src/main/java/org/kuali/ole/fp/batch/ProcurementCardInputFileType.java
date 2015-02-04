/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.fp.batch;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.service.AccountService;
import org.kuali.ole.fp.businessobject.ProcurementCardTransaction;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.batch.XmlBatchInputFileTypeBase;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * Batch input type for the procurement card job.
 */
public class ProcurementCardInputFileType extends XmlBatchInputFileTypeBase {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ProcurementCardInputFileType.class);

    private DateTimeService dateTimeService;

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getFileTypeIdentifer()
     */
    public String getFileTypeIdentifer() {
        return OLEConstants.PCDO_FILE_TYPE_INDENTIFIER;
    }

    /**
     * No additional information is added to procurment card batch files.
     * 
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getFileName(org.kuali.rice.kim.api.identity.Person, java.lang.Object,
     *      java.lang.String)
     */
    public String getFileName(String principalName, Object parsedFileContents, String userIdentifier) {
        String fileName = "pcdo_" + principalName;
        if (StringUtils.isNotBlank(userIdentifier)) {
            fileName += "_" + userIdentifier;
        }
        fileName += "_" + dateTimeService.toDateTimeStringForFilename(dateTimeService.getCurrentDate());

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
        if (fileNameParts.length >= 2) {
            return fileNameParts[1];
        }
        return null;
    }

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileType#validate(java.lang.Object)
     */
    public boolean validate(Object parsedFileContents) {
        List<ProcurementCardTransaction> pctrans = (List<ProcurementCardTransaction>)parsedFileContents;
        if (SpringContext.getBean(ParameterService.class).getParameterValueAsBoolean(ProcurementCardCreateDocumentsStep.class, ProcurementCardCreateDocumentsStep.USE_ACCOUNTING_DEFAULT_PARAMETER_NAME)) {
            return true;  // we're using accounting defaults, don't worry about account numbers from the file...
        }

        boolean valid = true;
        // add validation for chartCode-accountNumber, as chartCode is not required in xsd due to accounts-cant-cross-charts option
        AccountService acctserv = SpringContext.getBean(AccountService.class);
        for (ProcurementCardTransaction pctran : pctrans) {
            // if chart code is empty while accounts cannot cross charts, then derive chart code from account number
            if (StringUtils.isEmpty(pctran.getChartOfAccountsCode())) {
                if (acctserv.accountsCanCrossCharts()) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.GLOBAL_ERRORS, OLEKeyConstants.ERROR_BATCH_UPLOAD_FILE_EMPTY_CHART, pctran.getAccountNumber());
                    valid = false;
                }
                else {
                    // accountNumber shall not be empty, otherwise won't pass schema validation
                    Account account = acctserv.getUniqueAccountForAccountNumber(pctran.getAccountNumber());
                    if (account != null) {
                        pctran.setChartOfAccountsCode(account.getChartOfAccountsCode());
                    }       
                    else {
                        GlobalVariables.getMessageMap().putError(OLEConstants.GLOBAL_ERRORS, OLEKeyConstants.ERROR_BATCH_UPLOAD_FILE_INVALID_ACCOUNT, pctran.getAccountNumber());
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getTitleKey()
     */
    public String getTitleKey() {
        return OLEKeyConstants.MESSAGE_BATCH_UPLOAD_TITLE_PCDO;
    }

    /**
     * Gets the dateTimeService attribute.
     */
    public DateTimeService getDateTimeService() {
        return dateTimeService;
    }

    /**
     * Sets the dateTimeService attribute value.
     */
    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

}

