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
package org.kuali.ole.select.businessobject;

import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.select.service.impl.BatchLoadServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.services.IdentityManagementService;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.exception.DocumentAuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

public class OleLoadSumRecords extends PersistableBusinessObjectBase {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLoadSumRecords.class);

    private Integer acqLoadSumId;
    private String principalId;
    private Integer acqLoadSuccCount;
    private Integer acqLoadFailCount;

    private Integer acqLoadTotCount;
    private Integer acqLoadPoTotCount;
    private String acqLoadDescription;
    private String fileName;
    private BigDecimal profileId;
    private OleLoadProfile profileFile;
    private List<OleLoadFailureRecords> oleLoadFailureRecords;
    private String attachmentLink;
    private byte[] fileContents;
    private static boolean countFlag;
    private Integer acqLoadTotPoCount;
    private String documentNumber;
    private Timestamp loadCreatedDate;
    private Integer acqLoadTotBibCount;
    private String listOfAllBibs;
    private String batchProcessProfileId;

    public String getListOfAllBibs() {
        List bibIDList = SpringContext.getBean(BatchLoadServiceImpl.class).getBibIDList(this.acqLoadSumId.toString());
        return bibIDList.toString();
    }


    public OleLoadSumRecords() {
        countFlag = true;
    }

    public Integer getAcqLoadTotBibCount() {
        return acqLoadTotBibCount;
    }

    public void setAcqLoadTotBibCount(Integer acqLoadTotBibCount) {
        this.acqLoadTotBibCount = acqLoadTotBibCount;
    }


    public List<OleLoadFailureRecords> getOleLoadFailureRecords() {
        return oleLoadFailureRecords;
    }

    public void setOleLoadFailureRecords(List<OleLoadFailureRecords> oleLoadFailureRecords) {
        this.oleLoadFailureRecords = oleLoadFailureRecords;
    }

    public OleLoadProfile getProfileFile() {
        return profileFile;
    }

    public void setProfileFile(OleLoadProfile profileFile) {
        this.profileFile = profileFile;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getFileName() {
        return fileName;
    }

    public Integer getAcqLoadSumId() {
        return acqLoadSumId;
    }

    public void setAcqLoadSumId(Integer acqLoadSumId) {
        this.acqLoadSumId = acqLoadSumId;
    }

    public Integer getAcqLoadSuccCount() {
        return acqLoadSuccCount;
    }

    public void setAcqLoadSuccCount(Integer acqLoadSuccCount) {
        this.acqLoadSuccCount = acqLoadSuccCount;
    }

    public Integer getAcqLoadFailCount() {
        return acqLoadFailCount;
    }

    public void setAcqLoadFailCount(Integer acqLoadFailCount) {
        this.acqLoadFailCount = acqLoadFailCount;
    }

    public Integer getAcqLoadTotCount() {
        return acqLoadTotCount;
    }

    public void setAcqLoadTotCount(Integer acqLoadTotCount) {
        this.acqLoadTotCount = acqLoadTotCount;
    }

    public Integer getAcqLoadPoTotCount() {
        return acqLoadPoTotCount;
    }

    public void setAcqLoadPoTotCount(Integer acqLoadPoTotCount) {
        this.acqLoadPoTotCount = acqLoadPoTotCount;
    }

    public String getAcqLoadDescription() {
        return acqLoadDescription;
    }

    public void setAcqLoadDescription(String acqLoadDescription) {
        this.acqLoadDescription = acqLoadDescription;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BigDecimal getProfileId() {
        return profileId;
    }

    public void setProfileId(BigDecimal profileId) {
        this.profileId = profileId;
    }

    public Integer getAcqLoadTotPoCount() {
        if (this.acqLoadPoTotCount != 0)
            return getPoToalCount(this.acqLoadPoTotCount);
        else
            return this.acqLoadPoTotCount;
    }

    public void setAcqLoadTotPoCount(Integer acqLoadTotPoCount) {
        this.acqLoadTotPoCount = acqLoadTotPoCount;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Timestamp getLoadCreatedDate() {
        return loadCreatedDate;
    }

    public void setLoadCreatedDate(Timestamp loadCreatedDate) {
        this.loadCreatedDate = loadCreatedDate;
    }


    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        return null;
    }

    public byte[] getFileContents() {
        String directoryPath = null;
        try {
            directoryPath = getDirectoryPath() + this.acqLoadSumId + OLEConstants.BATCH_FAILURE_FILE_MRK;
            File file = new File(directoryPath);
            if (file.exists()) {
                InputStream fis = new FileInputStream(file);
                byte[] filecontents = new byte[(int) file.length()];
                fis.read(filecontents);
                return filecontents;
            }
        } catch (Exception ex) {
        }

        return null;
    }

    public String getAttachmentLink() {
        boolean hasPermission = false;
        String documentTypeName = OLEConstants.OleLoadSummary.LOAD_SUMMARY;
        String nameSpaceCode = OLEConstants.OleLoadSummary.LOAD_SUMMARY_NAMESPACE;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside getInquiryUrl documentTypeName   >>>>>>>>>>>>>>>>>" + documentTypeName);
            LOG.debug("Inside getInquiryUrl nameSpaceCode  >>>>>>>>>>>>>>>>>" + nameSpaceCode);
        }
        hasPermission = SpringContext.getBean(IdentityManagementService.class).hasPermission(GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode, OLEConstants.OleLoadSummary.CAN_VIEW_LOAD_SUMMARY);

        if (!hasPermission) {
            if (LOG.isDebugEnabled())
                LOG.debug("Inside getInquiryUrl hasPermission   if>>>>>>>>>>>>>>>>>" + hasPermission);
            throw new DocumentAuthorizationException(GlobalVariables.getUserSession().getPerson().getPrincipalName(), " to edit reuisition document ", "dfsf");
        } else {
            if (this.oleLoadFailureRecords.size() > 0) {
                Properties params = new Properties();
                params.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, OLEConstants.DOWNLOAD_CUSTM_BO_ATTACHMENT_METHOD);
                params.put(OLEConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, OleLoadSumRecords.class.getName());
                params.put("acqLoadSumId", this.acqLoadSumId.toString());
                params.put("fileName", this.acqLoadSumId + OLEConstants.BATCH_FAILURE_FILE_MRK);
                params.put("fileContentType", ".mrk");
                params.put("fileContentBOField", "fileContents");
                return UrlFactory.parameterizeUrl(KRADConstants.INQUIRY_ACTION, params);
            } else
                return null;
        }
    }

    public void setAttachmentLink(String attachmentLink) {
        this.attachmentLink = attachmentLink;
    }

    private String getDirectoryPath() {
        // String directory =
        // SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.STAGING_DIRECTORY_KEY);
        String directoryPath = SpringContext.getBean(BatchLoadServiceImpl.class).getDestinationPath();
        return directoryPath;
    }

    public Integer getPoToalCount(Integer acqLoadPoTotCount) {
        this.acqLoadTotBibCount = this.acqLoadSuccCount;
        int count = 0;
        String reqIds = null;
        try {
            String directoryPath = getDirectoryPath() + this.acqLoadSumId + OLEConstants.BATCH_REQ_ID_FILE;
            File file = new File(directoryPath);
            if (file.exists()) {
                if (countFlag) {
                    InputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    DataInputStream dis = new DataInputStream(bis);
                    reqIds = dis.readLine();
                    countFlag = false;
                    count = SpringContext.getBean(BatchLoadServiceImpl.class).getPoCount(reqIds);
                    if (count == -1) {
                        return count;
                    }
                    this.setAcqLoadFailCount(this.acqLoadTotCount - count);
                    this.setAcqLoadSuccCount(this.acqLoadTotCount - this.acqLoadFailCount);
                    this.acqLoadPoTotCount = count;
                    return count;
                }
            }
        } catch (Exception ex) {
        }
        return acqLoadPoTotCount;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }
}