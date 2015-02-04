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

import org.apache.struts.upload.FormFile;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.businessobject.OleLoadFailureRecords;
import org.kuali.ole.select.businessobject.OleLoadSumRecords;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface BatchLoadService extends Serializable {

    //public Properties loadPropertiesFromClassPath(String classPath);

    public List<BibInfoBean> getBibInfoBeanList(String xmlString);

    public void saveFailureRecord(List<BibInfoBean> bibFailureRecordsList, Integer loadSumId, BigDecimal errorId);

    public void saveSuccessRecord(OleLoadSumRecords oleLoadSumRecords, int dupRecords, int poSucRecords, int sucRecords, String batchDescription, BigDecimal batchLoadProfile, String fileName);

    public boolean fileSizeValidation(Long fileSize);

    public List getDocIsbnList(List bibIsbnList);

    public int getNoOfDupIsbnRecords(List testList, List bibList);

    public List<BibInfoBean> getIsbnFailureRecordsList(List isbnList, List<BibInfoBean> bibInfoBeanList);

    public List<BibInfoBean> getVendorPoNumberFailureRecordsList(List vendorNumberList, List<BibInfoBean> bibInfoBeanList);

    public OleLoadSumRecords getOleLoadSumRecords(Map loadRecordsMap);

    public List<OleLoadFailureRecords> getOleFailureRecordsList(Map loadRecordsMap);

    public List getVendorPoNumberList(List<BibInfoBean> bibInfoBeanList);

    public List<BibInfoBean> getRequisitionFailureRecords(List reqList, List<BibInfoBean> bibInfoBeanList);

    //public List<BibInfoBean> getPoFailureRecords(List reqList,List<BibInfoBean> bibInfoBeanList);

    public String getDestinationPath();

    public void foundAllDuplicateRecords(List<BibInfoBean> bibInfoBeanList, List<BibInfoBean> reqFailureList, List<BibInfoBean> titleFailureList, List<BibInfoBean> isbnDupFailureList, List<BibInfoBean> vendorPoNumberDupFailureList, List bibIsbnList);

    public Long getFileSize(FormFile upLoadFile);

    public int getDupRecordsCount(List<BibInfoBean> reqFailureList, List<BibInfoBean> titleFailureList, List<BibInfoBean> isbnDupFailureList, List<BibInfoBean> vendorPoNumberDupFailureList);

    public void saveAllFailureRecords(List<BibInfoBean> isbnFailureList, List<BibInfoBean> vendorPoNumberFailureList, List<BibInfoBean> reqFailureList, List<BibInfoBean> titleFailureList, List<BibInfoBean> vendorPoNumberDupFailureList, int acqLoadSumId);

    public List<BibInfoBean> getBibFailureRecordsList(List<BibInfoBean> isbnFailureList, List<BibInfoBean> vendorPoNumberFailureList, List<BibInfoBean> reqFailureList, List<BibInfoBean> titleFailureList, List<BibInfoBean> vendorPoNumberDupFailureList);

    public void createReqIdTextFile(List reqList, Integer acqSumId);

    public void createErrorMrkFile(String failureRawData, Integer acqSumId);

    public List getPOList(String loadSumId);

    public List getBibIDList(String loadSumId);
}
