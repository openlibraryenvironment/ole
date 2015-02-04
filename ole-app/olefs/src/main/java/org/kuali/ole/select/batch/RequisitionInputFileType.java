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
package org.kuali.ole.select.batch;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.coa.service.AccountService;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.batch.XmlBatchInputFileTypeBase;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.exception.ParseException;
import org.kuali.rice.core.api.datetime.DateTimeService;

import java.io.File;
import java.util.List;

public class RequisitionInputFileType extends XmlBatchInputFileTypeBase {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RequisitionInputFileType.class);

    private DateTimeService dateTimeService;

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getFileTypeIdentifer()
     */
    public String getFileTypeIdentifer() {
        return OLEConstants.REQUISITION_FILE_TYPE_INDENTIFIER;
    }

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getTitleKey()
     */
    public String getTitleKey() {
        return OLEKeyConstants.MESSAGE_BATCH_UPLOAD_TITLE_REQUISITION;
    }

    public String getFileName(String principalName, Object parsedFileContents, String userIdentifier, String destinationPath) {
        return null;
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

    /**
     * No additional information is added to bibinfo batch files.
     *
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getFileName(org.kuali.rice.kim.api.identity.Person, java.lang.Object,
     *      java.lang.String)
     */
    public String getFileName(String principalName, Object parsedFileContents, String userIdentifier) {
        LOG.debug("Inside RequisitionInputFileType.getFileName method..");
        String fileName = "requisitioninfo_" + principalName;
        if (StringUtils.isNotBlank(userIdentifier)) {
            fileName += "_" + userIdentifier;
        }
        fileName += "_" + dateTimeService.toDateTimeStringForFilename(dateTimeService.getCurrentDate());

        // remove spaces in filename
        fileName = StringUtils.remove(fileName, " ");

        return fileName;
    }


    /**
     * @see org.kuali.ole.sys.batch.XmlBatchInputFileTypeBase#parse(byte[])
     */
    @Override
    public Object parse(byte[] fileByteContent) throws ParseException {
        String testStr = new String();
        LOG.debug("RequisitionInputFileType.parse()");
        return super.parse(fileByteContent);
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
        LOG.debug("Inside RequisitionInputFileType.validate method..");
        List<BibInfoBean> requisitiontrans = (List<BibInfoBean>) parsedFileContents;
        boolean valid = true;

        // add validation for chartCode-accountNumber, as chartCode is not required in xsd due to accounts-cant-cross-charts option
        AccountService acctserv = SpringContext.getBean(AccountService.class);
        for (BibInfoBean requisitiontran : requisitiontrans) {
            // add the validation code here
        }

        return valid;
    }


}
