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
package org.kuali.ole.select.batch.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.batch.service.OleRequisitionCreateDocumentService;
import org.kuali.ole.select.batch.service.RequisitionLoadTransactionsService;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.service.impl.BuildVendorBibInfoBean;
import org.kuali.ole.sys.batch.service.BatchInputFileService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RequisitionLoadTransactionsServiceImpl implements RequisitionLoadTransactionsService, InitializingBean {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RequisitionLoadTransactionsServiceImpl.class);

    protected BatchInputFileService batchInputFileService;
    protected OleRequisitionCreateDocumentService oleRequisitionCreateDocumentService;
    protected BuildVendorBibInfoBean buildVendorBibInfoBean;
    protected Properties properties = null;
    protected ConfigurationService kualiConfigurationService;

    protected String determinePurchaseOrderTransmissionMethod() {

        return SpringContext.getBean(ParameterService.class).getParameterValueAsString(RequisitionDocument.class, PurapParameterConstants.PURAP_DEFAULT_PO_TRANSMISSION_CODE);
    }

    public BatchInputFileService getBatchInputFileService() {
        return batchInputFileService;
    }

    public void setBatchInputFileService(BatchInputFileService batchInputFileService) {
        this.batchInputFileService = batchInputFileService;
    }

    public OleRequisitionCreateDocumentService getOleRequisitionCreateDocumentService() {
        return oleRequisitionCreateDocumentService;
    }


    public void setOleRequisitionCreateDocumentService(OleRequisitionCreateDocumentService oleRequisitionCreateDocumentService) {
        this.oleRequisitionCreateDocumentService = oleRequisitionCreateDocumentService;
    }

    public BuildVendorBibInfoBean getBuildVendorBibInfoBean() {
        return buildVendorBibInfoBean;
    }

    public void setBuildVendorBibInfoBean(BuildVendorBibInfoBean buildVendorBibInfoBean) {
        this.buildVendorBibInfoBean = buildVendorBibInfoBean;
    }

    public ConfigurationService getConfigurationService() {
        return kualiConfigurationService;
    }

    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //properties = loadPropertiesFromClassPath("org/kuali/ole/select/batch/service/impl/bibinfo.properties");
    }

    /**
     * To load the XML File for the vendor.
     *
     * @param fileName String
     * @return boolean
     */
    public boolean loadRequisitionFile(String fileName) {

        String userName = kualiConfigurationService.getPropertyValueAsString("userName");
        GlobalVariables.setUserSession(new UserSession(userName));
        try {
            List<BibInfoBean> bibInfoBeanList = new ArrayList<BibInfoBean>();
            bibInfoBeanList = buildVendorBibInfoBean.getBibInfoList(fileName);
            setRequisitionParameterValue(bibInfoBeanList);
            saveRequisitionDocument(bibInfoBeanList);
        } catch (WorkflowException we) {
            LOG.error("failed to create a new RequisitionDocument instance" + we, we);
        } catch (Exception ex) {
            LOG.error("faild to create the requisition document in RequisitionLoadTransactionsServiceImpl " + fileName, ex);
            throw new RuntimeException("parsing error " + fileName + " " + ex.getMessage(), ex);
        }

        return true;

    }

    private List<BibInfoBean> setRequisitionParameterValue(List<BibInfoBean> bibInfoBeanList) {
        for (BibInfoBean bibInfoBean : bibInfoBeanList) {
            //bibInfoBean.setRequestSource(OleSelectConstant.REQUEST_SRC_TYPE_BATCHINGEST);
            bibInfoBean.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_MANUALINGEST);
            bibInfoBean.setRequestorType(OleSelectConstant.BATCHINGEST_REQUEST);
            bibInfoBean.setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_BATCHINGEST);
        }
        return bibInfoBeanList;
    }

    /**
     * Set the values for the Requisition Document and save.
     *
     * @param bibInfoBeanList ArrayList
     */
    public List saveRequisitionDocument(List<BibInfoBean> bibInfoBeanList) throws Exception {
        boolean vendorRecordMappingFlag = false;
        List reqList = new ArrayList(0);
        String vendorRecordMappingProperty = getParameter("VENDOR_RECORD_TO_REQUISITION_MAPPING");
        if (vendorRecordMappingProperty.equalsIgnoreCase("TRUE"))
            vendorRecordMappingFlag = true;
        oleRequisitionCreateDocumentService.saveRequisitionDocument(bibInfoBeanList, vendorRecordMappingFlag);
        return (List) oleRequisitionCreateDocumentService.getReqList();

    }


    /**
     * To load the property file for the given path.
     *
     * @param classPath String
     * @return properties
     */
/*    public static Properties loadPropertiesFromClassPath(String classPath) {
        ClassPathResource classPathResource = new ClassPathResource(classPath);

        Properties properties = new Properties();
        try {
            properties.load(classPathResource.getInputStream());
        }
        catch (IOException e) {
            throw new RuntimeException("Invalid class path: " + classPath + e,e);
        }

        return properties;
    }*/

    public String getParameter(String name){
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID,OLEConstants.SELECT_NMSPC,OLEConstants.SELECT_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }

}
