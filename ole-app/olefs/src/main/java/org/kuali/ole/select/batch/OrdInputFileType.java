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
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.batch.BatchInputFileTypeBase;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.exception.ParseException;
import org.kuali.rice.core.api.datetime.DateTimeService;

import java.io.File;

public class OrdInputFileType extends BatchInputFileTypeBase {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OrdInputFileType.class);

    private DateTimeService dateTimeService;

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getFileTypeIdentifer()
     */
    public String getFileTypeIdentifer() {
        return OLEConstants.ORD_FILE_TYPE_INDENTIFIER;
    }

    /**
     * No additional information is added to bibinfo batch files.
     *
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getFileName(org.kuali.rice.kim.api.identity.Person, java.lang.Object,
     *      java.lang.String)
     */
    public String getFileName(String principalName, Object parsedFileContents, String userIdentifier, String destinationPath) {
        LOG.debug("Inside OrdInputFileType.getFileName method..");
        String fileName = "ordinfo_" + principalName;
        if (StringUtils.isNotBlank(userIdentifier)) {
            fileName += "_" + userIdentifier;
        }

        // destinationPath = "staging/select/vendortransmissionfiles";
        // LOG.info("--------------file.Separator---------------"+os.toUpperCase());

        String os = System.getProperty("os.name");
        if (LOG.isDebugEnabled())
            LOG.debug("--------------file.Separator---------------" + os.toUpperCase());
        String separator = "";
        if (os.toUpperCase().contains("WIN")) {
            separator = "\\";
        } else {
            separator = "/";
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("--------------destinationPath---------------" + destinationPath);
        }
        destinationPath = destinationPath.replaceAll("\\" + separator, "__");

        if (LOG.isDebugEnabled()) {
            LOG.debug("--------------separator---------------" + separator);
            LOG.debug("--------------destinationPath 1---------------" + destinationPath);
        }


        fileName += "--" + destinationPath;
        fileName += "--" + dateTimeService.toDateTimeStringForFilename(dateTimeService.getCurrentDate());

        // remove spaces in filename
        fileName = StringUtils.remove(fileName, " ");

        return fileName;
    }

    /**
     * Empty function
     *
     * @param user               who uploaded the file
     * @param parsedFileContents represents collector batch object
     * @param userIdentifier     user identifier for user who uploaded file
     * @param destination        path selected by user for storing the ole format xml.
     * @return String returns null
     */
    public String getFileName(String principalName, Object parsedFileContents, String userIdentifier) {
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
        LOG.debug("Inside OrdInputFileType.validate method..");
        //List<BibInfoBean> requisitiontrans = (List<BibInfoBean>)parsedFileContents;
        boolean valid = true;

        // add validation for chartCode-accountNumber, as chartCode is not required in xsd due to accounts-cant-cross-charts option
        AccountService acctserv = SpringContext.getBean(AccountService.class);
        /*for (BibInfoBean requisitiontran : requisitiontrans) {
          // add the validation code here
        }*/

        return valid;
    }

    /**
     * @see org.kuali.ole.sys.batch.BatchInputFileType#getTitleKey()
     */
    public String getTitleKey() {
        return OLEKeyConstants.MESSAGE_BATCH_UPLOAD_TITLE_ORD;
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
     * @see org.kuali.ole.sys.batch.BatchInputFileType#process(java.lang.String, java.lang.Object)
     */
    public void process(String fileName, Object parsedFileContents) {
        // default impl does nothing
    }


    @Override
    public Object parse(byte[] fileByteContent) throws ParseException {
        return fileByteContent;
    }

}
