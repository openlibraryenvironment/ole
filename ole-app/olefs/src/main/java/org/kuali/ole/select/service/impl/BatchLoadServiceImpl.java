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
package org.kuali.ole.select.service.impl;

import org.apache.struts.upload.FormFile;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.service.BatchLoadService;
import org.kuali.ole.select.service.BibInfoService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;


public class BatchLoadServiceImpl implements BatchLoadService {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BatchLoadServiceImpl.class);
    /**
     *
     * This method Loading the properties file
     * @param classPath
     * @return
     */
   /* public Properties loadPropertiesFromClassPath(String classPath) {

        ClassPathResource classPathResource = new ClassPathResource(classPath);
        Properties properties = new Properties();
        try {
            properties.load(classPathResource.getInputStream());
        }
        catch (IOException e) {
            throw new RuntimeException("Invalid class path: " + classPath + e);
        }
        return properties;
    }*/

    /**
     * This method... retrieve the BibInfoBean values as a list based on parameter
     *
     * @param filePath
     * @return BibInfoBean as List
     */
    @Override
    public List<BibInfoBean> getBibInfoBeanList(String xmlString) {
        List<BibInfoBean> bibInfoBeanList = new ArrayList<BibInfoBean>();
        BuildVendorBibInfoBean buildVendorBibInfoBean = new BuildVendorBibInfoBean();
        bibInfoBeanList = buildVendorBibInfoBean.getBibInfoList(xmlString);
        return bibInfoBeanList;
    }

    /**
     * This method saving the OleLoadFailureRecords
     *
     * @param bibFailureRecordsList
     * @param loadSumId
     * @param errorId
     */
    @Override
    public void saveFailureRecord(List<BibInfoBean> bibFailureRecordsList, Integer loadSumId, BigDecimal errorId) {
        for (int i = 0; i < bibFailureRecordsList.size(); i++) {
            BibInfoBean bibInfoBean = bibFailureRecordsList.get(i);
            OleLoadFailureRecords oleLoadFailureRecords = new OleLoadFailureRecords();
            oleLoadFailureRecords.setAcqLoadSumId(loadSumId);
            oleLoadFailureRecords.setErrorId(errorId);
            oleLoadFailureRecords.setIsbn(bibInfoBean.getIsbn());
            oleLoadFailureRecords.setVendorId(bibInfoBean.getYbp());
            oleLoadFailureRecords.setTitle(bibInfoBean.getTitle());
            SpringContext.getBean(BusinessObjectService.class).save(oleLoadFailureRecords);
        }
    }

    /**
     * This method. saving the oleLoadSumRecords
     *
     * @param oleLoadSumRecords
     * @param dupRecords
     * @param sucRecords
     */
    @Override
    public void saveSuccessRecord(OleLoadSumRecords oleLoadSumRecords, int dupRecords, int poSucRecords, int sucRecords, String batchDiscription, BigDecimal batchLoadProfile, String fileName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("dupRecords =====================" + dupRecords);
            LOG.debug("sucRecords =====================" + sucRecords);
            LOG.debug("poSucRecords =====================" + poSucRecords);
        }
        oleLoadSumRecords.setAcqLoadDescription(batchDiscription);
        oleLoadSumRecords.setProfileId(batchLoadProfile);
        oleLoadSumRecords.setFileName(fileName);
        oleLoadSumRecords.setAcqLoadFailCount(dupRecords);
        oleLoadSumRecords.setAcqLoadSuccCount(sucRecords);
        oleLoadSumRecords.setAcqLoadTotCount(dupRecords + sucRecords);
        oleLoadSumRecords.setAcqLoadPoTotCount(poSucRecords);
        oleLoadSumRecords.setPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
        SpringContext.getBean(BusinessObjectService.class).save(oleLoadSumRecords);

    }

    /**
     * This method for fileSize validation
     *
     * @param fileSize
     * @return boolean
     */
    @Override
    public boolean fileSizeValidation(Long fileSize) {
        ConfigurationService kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);

        String maxFileSizeString = getParameter(OleSelectPropertyConstants.STAFF_UPLOAD_MAXFILESIZE);
        Long maxFileSize = Long.parseLong(maxFileSizeString);

        if (LOG.isDebugEnabled()) {
            LOG.debug("defaultFileSize =====================" + maxFileSize);
            LOG.debug("filesize =====================" + fileSize);
            //LOG.debug("filesizeInMB ====================="+filesizeInMB);
        }

        if (fileSize > maxFileSize && fileSize != 0) {
            GlobalVariables.getMessageMap().putError(OLEConstants.GLOBAL_ERRORS, OLEKeyConstants.ERROR_BATCH_UPLOAD_FILE_SIZE, new Long(maxFileSize).toString() + "KB");
            return true;
        }

        return false;
    }

    /**
     * This method... retrieving the docstore info list based on bibinfo IsbnList
     *
     * @param bibIsbnList
     * @return
     */

    @Override
    public List getDocIsbnList(List bibIsbnList) {
        List docIsbnList = new ArrayList(0);
        List<DocInfoBean> docInfoBeanList = new ArrayList<DocInfoBean>(0);
        try {
            BibInfoService bibInfoService = SpringContext.getBean(BibInfoServiceImpl.class);
            docInfoBeanList = bibInfoService.getResult(bibIsbnList);
            for (int j = 0; j < docInfoBeanList.size(); j++) {
                if (docInfoBeanList.get(j).getIsbn_display() != null) {
                    docIsbnList.add(docInfoBeanList.get(j).getIsbn_display());
                }
            }
        } catch (Exception ex) {
        }

        return docIsbnList;
    }

    /**
     * This method... check the no..of duplicate records based on isbn and VendorPO
     *
     * @param testList
     * @param bibList
     * @return No..of duplicate recodrs
     */

    @Override
    public int getNoOfDupIsbnRecords(List testIsbnList, List bibIsbnList) {
        HashSet temp = new HashSet();
        temp.addAll(testIsbnList);
        testIsbnList.clear();
        testIsbnList.addAll(temp);
        int dupRecords = 0;
        for (int i = 0; i < testIsbnList.size(); i++) {
            for (int j = 0; j < bibIsbnList.size(); j++) {
                if (testIsbnList.get(i).equals(bibIsbnList.get(j))) {
                    dupRecords++;
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("NO..OF duplicate records -------- >  " + dupRecords);
        }
        return dupRecords;
    }

    /**
     * This method for find out the duplicate records based on isbn
     *
     * @param isbnList
     * @param bibInfoBeanList
     * @return bibinfoBean list
     */
    @Override
    public List<BibInfoBean> getIsbnFailureRecordsList(List isbnList, List<BibInfoBean> bibInfoBeanList) {
        List<BibInfoBean> bibFailureRecordsList = new ArrayList<BibInfoBean>(0);
        for (int i = 0; i < isbnList.size(); i++) {
            for (int j = 0; j < bibInfoBeanList.size(); j++) {
                String isbn = bibInfoBeanList.get(j).getIsbn();
                if (isbn != null && !("".equals(isbn))) {
                    if (isbn.equals(isbnList.get(i).toString())) {
                        bibInfoBeanList.get(j).setFailure(bibInfoBeanList.get(j).getIsbn() + " is Duplicate ISBN value");
                        bibFailureRecordsList.add(bibInfoBeanList.get(j));
                        bibInfoBeanList.remove(j);
                        j--;
                    }
                }
            }
        }

        return bibFailureRecordsList;
    }

    /**
     * This method for find out the duplicate records based on VendorPO
     *
     * @param VendorPOList
     * @param bibInfoBeanList
     * @return bibInfoBean list
     */
    @Override
    public List<BibInfoBean> getVendorPoNumberFailureRecordsList(List vendorNumberList, List<BibInfoBean> bibInfoBeanList) {
        List<BibInfoBean> bibFailureRecordsList = new ArrayList<BibInfoBean>(0);
        for (int i = 0; i < vendorNumberList.size(); i++) {
            for (int j = 0; j < bibInfoBeanList.size(); j++) {
                String vendorPoValue = bibInfoBeanList.get(j).getYbp();
                if (vendorPoValue.equals(vendorNumberList.get(i).toString())) {
                    bibInfoBeanList.get(j).setFailure(vendorPoValue + " is Duplicate Vendor Purchase Order value");
                    bibFailureRecordsList.add(bibInfoBeanList.get(j));
                    bibInfoBeanList.remove(j);
                    j--;
                }
            }
        }

        return bibFailureRecordsList;
    }

    /**
     * This method...retrieve the OleLoadSumRecords based on id
     *
     * @param loadRecordsMap
     * @return OleLoadSumRecords
     */
    @Override
    public OleLoadSumRecords getOleLoadSumRecords(Map loadRecordsMap) {
        OleLoadSumRecords oleLoadSumRecords = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(OleLoadSumRecords.class, loadRecordsMap);

        Map findProfileMap = new HashMap();
        findProfileMap.put("profileId", oleLoadSumRecords.getProfileId());
        OleLoadProfile oleLoadProfile = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(OleLoadProfile.class, findProfileMap);

        oleLoadSumRecords.setProfileFile(oleLoadProfile);

        return oleLoadSumRecords;
    }

    /**
     * This method... retrieve the OleLoadFailureRecords based on id
     *
     * @param loadRecordsMap
     * @return List of OleLoadFailureRecords
     */
    @Override
    public List<OleLoadFailureRecords> getOleFailureRecordsList(Map loadRecordsMap) {
        Collection<OleLoadFailureRecords> oleLoadFailureRecordsList = SpringContext.getBean(BusinessObjectService.class).findMatching(OleLoadFailureRecords.class, loadRecordsMap);
        for (OleLoadFailureRecords record : oleLoadFailureRecordsList) {
            if (record.getErrorId().equals(new BigDecimal(5))) {
                record.setError(OLEConstants.BATCH_ISBN_DUPLICATE_FOUND);
            } else if (record.getErrorId().equals(new BigDecimal(1))) {
                record.setError(OLEConstants.BATCH_VNO_DUPLICATE_FOUND);
            } else if (record.getErrorId().equals(new BigDecimal(3))) {
                record.setError(OLEConstants.BATCH_LOAD_FAILD_FOUND);
            } else if (record.getErrorId().equals(new BigDecimal(6))) {
                record.setError(OLEConstants.BATCH_VNO_NOT_FOUND);
            } else if (record.getErrorId().equals(new BigDecimal(7))) {
                record.setError(OLEConstants.BAD_BFN_NO_FOUND);
            } else if (record.getErrorId().equals(new BigDecimal(8))) {
                record.setError(OLEConstants.BAD_CONTROLL_LINE_FOUND);
            } else if (record.getErrorId().equals(new BigDecimal(9))) {
                record.setError(OLEConstants.APO_RULE_FAILED);
            } else {
                record.setError(OLEConstants.BATCH_TITLE_FAILD_FOUND);
            }
        }
        return new ArrayList<OleLoadFailureRecords>(oleLoadFailureRecordsList);
    }

    /**
     * This method... retrieve the OleRequisitionDocument
     *
     * @return List of OleRequisitionDocument
     */
    @Override
    public List getVendorPoNumberList(List<BibInfoBean> bibInfoBeanList) {
        List bibVendorNumberList = new ArrayList(0);
        for (int i = 0; i < bibInfoBeanList.size(); i++) {
            bibVendorNumberList.add(bibInfoBeanList.get(i).getYbp());
        }
        List vendorNumberList = new ArrayList(0);
        //List<OleRequisitionDocument> oleRequisitionDocumentList=new ArrayList<OleRequisitionDocument>(0);
        clearSystemCache();
        Map map = new HashMap();
        for (int i = 0; i < bibVendorNumberList.size(); i++) {
            if (bibVendorNumberList.get(i) != null && !("".equals(bibVendorNumberList.get(i)))) {
                map.put("vendorPoNumber", bibVendorNumberList.get(i));
            }
            int count = SpringContext.getBean(BusinessObjectService.class).countMatching(OleRequisitionDocument.class, map);
            if (count != 0) {
                vendorNumberList.add(bibVendorNumberList.get(i));
            }
        }

       /*Collection docList = SpringContext.getBean(KeyValuesService.class).findAll(OleRequisitionDocument.class);*/

      /* for(int i=0;i<oleRequisitionDocumentList.size();i++)
            if(oleRequisitionDocumentList.get(i).getVendorPoNumber()!=null)
                vendorNumberList.add(oleRequisitionDocumentList.get(i).getVendorPoNumber());*/


        return vendorNumberList;
    }

    /**
     * This method... retrieve the failure Requisition List
     *
     * @return List of OleRequisitionDocument
     */
    @Override
    public List<BibInfoBean> getRequisitionFailureRecords(List reqList, List<BibInfoBean> bibInfoBeanList) {
        List<BibInfoBean> reqFailureList = new ArrayList<BibInfoBean>(0);

        for (int i = 0; i < reqList.size(); i++) {
            if (reqList.get(i) == null) {
                bibInfoBeanList.get(i).setFailure("Requisition Creation Load Error Problem");
                reqFailureList.add(bibInfoBeanList.get(i));
                bibInfoBeanList.remove(i);
                i--;
            }
        }

        return reqFailureList;
    }
    /**
     *
     * This method... retrieve the failure Requisition List
     * @return List of OleRequisitionDocument
     */
   /*public List<BibInfoBean> getPoFailureRecords(List reqList,List<BibInfoBean> bibInfoBeanList)
   {
       List<BibInfoBean> poFailureList=new ArrayList<BibInfoBean>(0);
       Collection docList = SpringContext.getBean(KeyValuesService.class).findAll(OlePurchaseOrderDocument.class);
       List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList=new ArrayList<OlePurchaseOrderDocument>(docList);
       for(int i=0;i<reqList.size();i++)
       {    for(int j=0;j<olePurchaseOrderDocumentList.size()&&i<bibInfoBeanList.size();j++)
             if((reqList.get(i).toString()).equals((olePurchaseOrderDocumentList.get(j).getRequisitionIdentifier().toString())))
             {
                 bibInfoBeanList.get(i).setFailure("PO Creation Load Error Problem");
                 poFailureList.add(bibInfoBeanList.get(i));
                 bibInfoBeanList.remove(i);
                 i--;
             }

       }

       return poFailureList;
   }*/

    /**
     * This method for destination path
     * * @return boolean
     */
    @Override
    public String getDestinationPath() {
        ConfigurationService kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        //Properties properties = loadPropertiesFromClassPath(OLEConstants.LOAD_FILE_PROPERTIES);
        String destinationPath = kualiConfigurationService.getPropertyValueAsString(OLEConstants.STAGING_DIRECTORY_KEY) + getParameter(OleSelectPropertyConstants.STAFF_UPLOAD_DESTINATIONPATH);
        File dirCheck = (new File(destinationPath));
        boolean isDir = dirCheck.exists();
        if (LOG.isDebugEnabled()) {
            LOG.debug("dirCheck =====================" + dirCheck);
        }
        if (!isDir) {
            dirCheck.mkdir();
        }
        return destinationPath;
    }

    public Integer getPoCount(String reqIds) {
        int count = 0;
        List reqList = Arrays.asList(reqIds.split(","));
        clearSystemCache();
        Map map = new HashMap();

        for (int i = 0; i < reqList.size(); i++) {
            try {
                map.put(OLEConstants.REQ_IDENTIFIER, reqList.get(i));

                /*
                 * Collection poDocList = SpringContext.getBean(BusinessObjectService.class).findMatching(
                 * OlePurchaseOrderDocument.class, map); if (0 < poDocList.size()) { OlePurchaseOrderDocument po =
                 * ((List<OlePurchaseOrderDocument>) poDocList).get(0); if
                 * (reqList.get(i).toString().equals(po.getRequisitionIdentifier().toString())) { String status =
                 * getDocumentStatus(po.getDocumentNumber()); if ("FINAL".equals(status)) { count++; } if
                 * (!OLEConstants.EXCEPTION.equals(status) && !OLEConstants.FINAL.equals(status)) { return -1; } } }
                 */

                int poCount = SpringContext.getBean(BusinessObjectService.class).countMatching(
                        OlePurchaseOrderDocument.class, map);
                if (poCount != 0) {
                    count++;
                } else {
                    map.clear();
                    map.put(OLEConstants.PUR_DOC_IDENTIFIER, reqList.get(i));
                    Collection reqDocList = SpringContext.getBean(BusinessObjectService.class).findMatching(
                            OleRequisitionDocument.class, map);
                    if (0 < reqDocList.size()) {
                        OleRequisitionDocument req = ((List<OleRequisitionDocument>) reqDocList).get(0);
                        if (reqList.get(i).toString().equals(req.getPurapDocumentIdentifier().toString())) {
                            String status = getDocumentStatus(req.getDocumentNumber());
                            if (!OLEConstants.EXCEPTION.equals(status) && !OLEConstants.FINAL.equals(status)) {
                                return -1;
                            }
                        }

                    }
                }
            } catch (Exception e) {
                LOG.error("Exception while processing PO Count ====" + e);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("NO..Of PO Created Count --- >  " + count);
        }
        return count;
    }

    public void clearSystemCache() {
//       SpringContext.getBean(CacheService.class).clearSystemCaches();
    }

    @Override
    public void foundAllDuplicateRecords(List<BibInfoBean> bibInfoBeanList, List<BibInfoBean> reqFailureList, List<BibInfoBean> titleFailureList, List<BibInfoBean> isbnDupFailureList, List<BibInfoBean> vendorPoNumberDupFailureList, List bibIsbnList) {
        for (int i = 0; i < bibInfoBeanList.size(); i++) {
            if ("".equals(bibInfoBeanList.get(i).getAccountNumber()) || bibInfoBeanList.get(i).getAccountNumber() == null
                    || "".equals(bibInfoBeanList.get(i).getObjectCode()) || bibInfoBeanList.get(i).getObjectCode() == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(" Account No ---------->>> " + bibInfoBeanList.get(i).getAccountNumber() + "\n Object code ------->> " + bibInfoBeanList.get(i).getObjectCode());
                }
                bibInfoBeanList.get(i).setFailure(OLEConstants.BATCH_LOAD_FAILD_FOUND);
                reqFailureList.add(bibInfoBeanList.get(i));
                bibInfoBeanList.remove(i);
                i--;
            } else if ("".equals(bibInfoBeanList.get(i).getTitle()) || bibInfoBeanList.get(i).getTitle() == null) {
                bibInfoBeanList.get(i).setFailure(OLEConstants.BATCH_TITLE_FAILD_FOUND);
                titleFailureList.add(bibInfoBeanList.get(i));
                bibInfoBeanList.remove(i);
                i--;
            } else if ("".equals(bibInfoBeanList.get(i).getYbp()) || bibInfoBeanList.get(i).getYbp() == null) {
                bibInfoBeanList.get(i).setFailure("Vendor Purchase Order value is Null");
                vendorPoNumberDupFailureList.add(bibInfoBeanList.get(i));
                bibInfoBeanList.remove(i);
                i--;
            } else {
                if (bibIsbnList.contains(bibInfoBeanList.get(i).getIsbn())) {
                    bibInfoBeanList.get(i).setFailure(bibInfoBeanList.get(i).getIsbn() + " is Duplicate ISBN value");
                    isbnDupFailureList.add(bibInfoBeanList.get(i));
                    bibInfoBeanList.remove(i);
                    i--;
                } else {
                    bibIsbnList.add(bibInfoBeanList.get(i).getIsbn());
                }
            }
        }
    }

    @Override
    public Long getFileSize(FormFile upLoadedFile) {
        Long filesize = new Long(0);
        if (!"".equals(upLoadedFile.getFileName())) {
            String fileName = upLoadedFile.getFileName();
            int fileExtensionCount = fileName.indexOf(".");
            String extension = fileName.substring(fileExtensionCount + 1);
            fileName = fileName.substring(0, fileExtensionCount);
            filesize = new Long(upLoadedFile.getFileSize());
        }

        return filesize;
    }

    @Override
    public int getDupRecordsCount(List<BibInfoBean> reqFailureList, List<BibInfoBean> titleFailureList, List<BibInfoBean> isbnDupFailureList, List<BibInfoBean> vendorPoNumberDupFailureList) {
        int dupRecords = 0;
        dupRecords += reqFailureList.size();
        dupRecords += titleFailureList.size();
        dupRecords += isbnDupFailureList.size();
        dupRecords += vendorPoNumberDupFailureList.size();
        return dupRecords;
    }

    @Override
    public void saveAllFailureRecords(List<BibInfoBean> isbnFailureList, List<BibInfoBean> vendorPoNumberFailureList, List<BibInfoBean> reqFailureList, List<BibInfoBean> titleFailureList, List<BibInfoBean> vendorPoNumberDupFailureList, int acqLoadSumId) {
        saveFailureRecord(isbnFailureList, acqLoadSumId, new BigDecimal(5));
        saveFailureRecord(vendorPoNumberFailureList, acqLoadSumId, new BigDecimal(1));
        saveFailureRecord(reqFailureList, acqLoadSumId, new BigDecimal(3));
        saveFailureRecord(titleFailureList, acqLoadSumId, new BigDecimal(4));
        saveFailureRecord(vendorPoNumberDupFailureList, acqLoadSumId, new BigDecimal(6));
    }

    @Override
    public List<BibInfoBean> getBibFailureRecordsList(List<BibInfoBean> isbnFailureList, List<BibInfoBean> vendorPoNumberFailureList, List<BibInfoBean> reqFailureList, List<BibInfoBean> titleFailureList, List<BibInfoBean> vendorPoNumberDupFailureList) {
        List<BibInfoBean> bibFailureRecordsList = new ArrayList<BibInfoBean>(0);
        //if(!isbnFailureList.isEmpty())
        bibFailureRecordsList.addAll(isbnFailureList);
        // if(!ybpFailureList.isEmpty())
        bibFailureRecordsList.addAll(vendorPoNumberFailureList);
        // if(!reqFailureList.isEmpty())
        bibFailureRecordsList.addAll(reqFailureList);
        // if(!poFailureList.isEmpty())
        // bibFailureRecordsList.addAll(poFailureList);
        bibFailureRecordsList.addAll(titleFailureList);
        bibFailureRecordsList.addAll(vendorPoNumberDupFailureList);
        return bibFailureRecordsList;
    }

    @Override
    public void createReqIdTextFile(List reqList, Integer acqSumId) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(getDestinationPath() + acqSumId + OLEConstants.BATCH_REQ_ID_FILE));
            StringBuffer reqIds = new StringBuffer();
            for (int i = 0; i < reqList.size(); i++) {
                reqIds.append(reqList.get(i).toString() + ",");
            }
            out.write(reqIds.toString());
            out.close();
        } catch (Exception ex) {
        }

    }

    @Override
    public void createErrorMrkFile(String failureRawData, Integer acqSumId) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(getDestinationPath() + acqSumId + OLEConstants.BATCH_FAILURE_FILE_MRK));
            out.write(failureRawData);
            out.close();
        } catch (Exception ex) {
        }

    }


    @Override
    public List getPOList(String loadSumId) {

        String directoryPath = getDestinationPath() + loadSumId + OLEConstants.BATCH_REQ_ID_FILE;
        File file = new File(directoryPath);
        List poList = new ArrayList();
        try {
            if (file.exists()) {
                InputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                String reqIds = dis.readLine();
                List reqList = Arrays.asList(reqIds.split(","));
                clearSystemCache();
                Map map = new HashMap();
                for (int i = 0; i < reqList.size(); i++) {
                    map.put("requisitionIdentifier", reqList.get(i));
                    Collection<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = SpringContext.getBean(BusinessObjectService.class).findMatching(OlePurchaseOrderDocument.class, map);
                    if (!olePurchaseOrderDocumentList.isEmpty() && olePurchaseOrderDocumentList.iterator().next() != null) {
                        poList.add(olePurchaseOrderDocumentList.iterator().next().getDocumentNumber());
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while getting POList----> " + ex);
        }
        return poList;
    }


    @Override
    public List getBibIDList(String loadSumId) {

        String directoryPath = getDestinationPath() + loadSumId + OLEConstants.BATCH_REQ_ID_FILE;
        File file = new File(directoryPath);
        List bibIDList = new ArrayList();
        try {
            if (file.exists()) {
                InputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                String reqIds = dis.readLine();
                List reqList = Arrays.asList(reqIds.split(","));
                clearSystemCache();
                Map map = new HashMap();
                for (int i = 0; i < reqList.size(); i++) {
                    map.put("purapDocumentIdentifier", reqList.get(i));
                    Collection<OleRequisitionDocument> oleRequisitionDocumentList = SpringContext.getBean(BusinessObjectService.class).findMatching(OleRequisitionDocument.class, map);
                    if (!oleRequisitionDocumentList.isEmpty() && oleRequisitionDocumentList.iterator().next() != null) {
                        List<OleRequisitionItem> oleRequisitionItemList = oleRequisitionDocumentList.iterator().next().getItems();
                        for (int j = 0; j < oleRequisitionItemList.size(); j++) {
                            OleRequisitionItem oleRequisitionItem = oleRequisitionItemList.get(j);
                            bibIDList.add(oleRequisitionItem.getItemTitleId());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while getting BibIDList----> ", ex);
        }
        return bibIDList;
    }

    private String getDocumentStatus(String docNumber) {
        try {
            Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(
                    GlobalVariables.getUserSession().getPerson().getPrincipalId());
            WorkflowDocument workFlowDocument = KRADServiceLocatorWeb.getWorkflowDocumentService()
                    .loadWorkflowDocument(docNumber, principalPerson);
            return workFlowDocument.getStatus().toString();
        } catch (Exception e) {
            LOG.error("Exception while getting document status---->" + e);
        }
        return null;
    }

    public String getParameter(String name){
        ParameterKey parameterKey = ParameterKey.create(org.kuali.ole.OLEConstants.APPL_ID, org.kuali.ole.OLEConstants.SELECT_NMSPC, org.kuali.ole.OLEConstants.SELECT_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }
}
