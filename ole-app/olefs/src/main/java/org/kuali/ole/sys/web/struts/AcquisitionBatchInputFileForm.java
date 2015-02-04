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
package org.kuali.ole.sys.web.struts;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.select.businessobject.OleLoadFailureRecords;
import org.kuali.ole.select.businessobject.OleLoadSumRecords;
import org.kuali.ole.select.document.AcquisitionBatchInputFileDocument;
import org.kuali.ole.sys.businessobject.AcquisitionBatchUpload;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase;
import org.kuali.rice.kns.web.ui.HeaderField;
import org.kuali.rice.kew.api.WorkflowDocument;

/**
 * Struts action form for the batch upload screen.
 */
public class AcquisitionBatchInputFileForm extends KualiTransactionalDocumentFormBase {
    private FormFile uploadFile;
    private AcquisitionBatchUpload acquisitionBatchUpload;
    //private List<KeyLabelPair> userFiles;
   // private String url;
    private OleLoadSumRecords oleLoadSumRecords;
    private List<OleLoadFailureRecords> oleLoadFailureRecordsList;
    private String fileName;
    private String titleKey;
    //private String attachmentLink;
    //private String fileContents;
   
    /*public String getAttachmentLink() {
        return attachmentLink;
    }

    public void setAttachmentLink(String attachmentLink) {
        this.attachmentLink = attachmentLink;
    }*/

    /**
     * Gets the batchUpload attribute.
     */
    public AcquisitionBatchUpload getAcquisitionBatchUpload() {
        return acquisitionBatchUpload;
    }

    /**
     * Sets the batchUpload attribute value.
     */
    public void setAcquisitionBatchUpload(AcquisitionBatchUpload batchUpload) {
        this.acquisitionBatchUpload = batchUpload;
    }

    /**
     * Gets the uploadFile attribute.
     */
    public FormFile getUploadFile() {
        return uploadFile;
    }

    /**
     * Sets the uploadFile attribute value.
     */
    public void setUploadFile(FormFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    /**
     * Gets the userFiles attribute.
     */
   /* public List<KeyLabelPair> getUserFiles() {
        return userFiles;
    }

    *//**
     * Sets the userFiles attribute value.
     *//*
    public void setUserFiles(List<KeyLabelPair> userFiles) {
        this.userFiles = userFiles;
    }*/

    /**
     * Gets the titleKey attribute.
     */
    public String getTitleKey() {
        return titleKey;
    }

    /**
     * Sets the titleKey attribute value.
     */
    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    /**
     * Gets the url attribute. 
     * @return Returns the url.
     */
   /* public String getUrl() {
        return url;
    }*/

    /**
     * Sets the url attribute value.
     * @param url The url to set.
     */
    /*public void setUrl(String url) {
        this.url = url;
    }*/
   
    public OleLoadSumRecords getOleLoadSumRecords() {
        return oleLoadSumRecords;
    }

    public void setOleLoadSumRecords(OleLoadSumRecords oleLoadSumRecords) {
        this.oleLoadSumRecords = oleLoadSumRecords;
    }

    public List<OleLoadFailureRecords> getOleLoadFailureRecordsList() {
        return oleLoadFailureRecordsList;
    }

    public void setOleLoadFailureRecordsList(List<OleLoadFailureRecords> oleLoadFailureRecordsList) {
        this.oleLoadFailureRecordsList = oleLoadFailureRecordsList;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
 
    @Override
    public String getRefreshCaller(){
        return "refreshCaller";
    }
    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }
    
    public AcquisitionBatchInputFileForm(){
        super();
        this.acquisitionBatchUpload = new AcquisitionBatchUpload();
        setDocTypeName("OLE_ACQBTHUPLOAD");
        setDocument(new AcquisitionBatchInputFileDocument());
    }
    
    /*public String getFileContents() {
        return fileContents;
    }

    public void setFileContents(String fileContents) {
        this.fileContents = fileContents;
    }*/
    
    /**
    * KRAD Conversion: Performs customization of an header fields.
    * 
    * Use of data dictionary for bo RequisitionDocument.
    */
    public void populateHeaderFields(WorkflowDocument workflowDocument) {
        super.populateHeaderFields(workflowDocument);
        if (ObjectUtils.isNotNull(this.oleLoadSumRecords)) {
           String displayValue=getDocInfo().get(3).getDisplayValue();
           getDocInfo().remove(3);
           getDocInfo().add(new HeaderField("DataDictionary.OleLoadSumRecords.attributes.loadCreatedDate", displayValue));
           getDocInfo().add(new HeaderField("DataDictionary.OleLoadSumRecords.attributes.acqLoadSumId", this.oleLoadSumRecords.getAcqLoadSumId().toString()));
           
       }
        
    }
    
}
