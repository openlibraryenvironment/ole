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

import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.batch.service.OleRequisitionCreateDocumentService;
import org.kuali.ole.select.batch.service.impl.OleRequisitionCreateDocumentServiceImpl;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.service.PopulateBibInfoService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.ErrorMessage;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.util.AutoPopulatingList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PopulateBibInfoServiceImpl implements PopulateBibInfoService {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PopulateBibInfoServiceImpl.class);
    protected ConfigurationService kualiConfigurationService;
    protected OlePurapService olePurapService;

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    @Override
    public String processBibInfoForCitation(String citationString, BibInfoBean bibInfoBean) throws Exception {
        try {
            String user;
            if (GlobalVariables.getUserSession() != null) {
                user = GlobalVariables.getUserSession().getPrincipalName();
            } else {
                kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
                user = kualiConfigurationService.getPropertyValueAsString("userName");
            }
            GlobalVariables.setUserSession(new UserSession(user));
            BuildCitationBibInfoBean buildCitationBibInfoBean = SpringContext.getBean(BuildCitationBibInfoBean.class);
            bibInfoBean = buildCitationBibInfoBean.getBean(citationString, bibInfoBean);
            if (bibInfoBean.getTitle() == null || "".equalsIgnoreCase(bibInfoBean.getTitle())) {
                return OleSelectConstant.SOAP_CITATION_PARSER_UNREACHABLE;
            }
            bibInfoBean.setRequestSourceUrl(citationString);
            //bibInfoBean.setRequestSource(OleSelectConstant.REQUEST_SRC_TYPE_WEBFORM);
            bibInfoBean.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_WEBFORM);
            bibInfoBean.setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_WEBFORM);
            bibInfoBean = setBibInfoDefaultValues(bibInfoBean);
            OleRequisitionCreateDocumentService createDocument = SpringContext.getBean(OleRequisitionCreateDocumentServiceImpl.class);
            List<BibInfoBean> bibInfoBeanList = getBibInfoBeanList(bibInfoBean);
            String docNumber = createDocument.saveRequisitionDocument(bibInfoBeanList, true);
            return docNumber;
        } catch (Exception e) {
            LOG.error("Exception processing for SOAP citation document creation----" + e.getMessage(), e);
            String errorMessage = null;
            if (GlobalVariables.getMessageMap().hasErrors()) {
                Map<String, AutoPopulatingList<ErrorMessage>> map = GlobalVariables.getMessageMap().getErrorMessages();
                for (Map.Entry<String, AutoPopulatingList<ErrorMessage>> entry : map.entrySet()) {
                    AutoPopulatingList<ErrorMessage> errors = entry.getValue();
                    ErrorMessage error = errors.get(0);
                    String[] params = error.getMessageParameters();
                    errorMessage = params[0];
                }
            }
            return OleSelectConstant.SOAP_EXCEPTION + " - " + errorMessage;
        }
    }

    /**
     * This method sets the default values to the bibInfoBean
     *
     * @param bibInfoBean
     * @return BibInfoBean
     */
    private BibInfoBean setBibInfoDefaultValues(BibInfoBean bibInfoBean) throws Exception {

        // bibInfoBean.setIsbn(kualiConfigurationService.getPropertyValueAsString("isbn"));
        bibInfoBean.setFinancialYear(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.FIN_YEAR));
        bibInfoBean.setChartOfAccountsCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.CHART_OF_ACC_CD));
        bibInfoBean.setOrganizationCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.ORG_CODE));
        bibInfoBean.setDocumentFundingSourceCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.FUND_SRC_CD));
        bibInfoBean.setUseTaxIndicator(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.USE_TAX_IND) == "true" ? true : false);
        bibInfoBean.setDeliveryCampusCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DLVR_CMPS_CD));
        bibInfoBean.setDeliveryBuildingOtherIndicator(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DLVR_BLDNG_OTHR_IND) == "true" ? true : false);
        bibInfoBean.setDeliveryBuildingCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DLVR_BLDNG_CD));
        bibInfoBean.setDeliveryBuildingLine1Address(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DLVR_BLDNG_LN_ADDR));
        bibInfoBean.setDeliveryBuildingRoomNumber(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DLVR_BLDNG_ROOM_NBR));
        bibInfoBean.setDeliveryCityName(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DLVR_CITY_NM));
        bibInfoBean.setDeliveryStateCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DLVR_STATE_CD));
        bibInfoBean.setDeliveryPostalCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DLVR_POSTAL_CD));
        bibInfoBean.setDeliveryCountryCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DLVR_CNTRY_CD));
        bibInfoBean.setDeliveryToName(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DELIVERY_TO_NAME));
/*        bibInfoBean.setVendorCode(properties.getProperty("userName"));
        bibInfoBean.setVendorCustomerNumber(properties.getProperty("userName"));*/
        bibInfoBean.setUom(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.UOM));
        bibInfoBean.setItemTypeCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.ITEM_TYPE_CD));
        bibInfoBean.setListprice(new Double(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.LIST_PRICE)));
        bibInfoBean.setQuantity(new Long(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.QTY)));
        bibInfoBean.setPurchaseOrderTransmissionMethodCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.PO_TRNS_MTH_CD));
        bibInfoBean.setPurchaseOrderCostSourceCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.PO_CST_SRC_CD));
        bibInfoBean.setRequestorPersonName(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_NAME));
        bibInfoBean.setRequestorPersonPhoneNumber(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_PHONE_NUMBER));
        bibInfoBean.setRequestorPersonEmailAddress(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_EMAIL_ADDRESS));
        bibInfoBean.setLocation(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.LOCATION));
        bibInfoBean.setOrganizationAutomaticPurchaseOrderLimit(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.ORG_PO_LMT));
        bibInfoBean.setPurchaseOrderAutomaticIndicator(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.PURCHASE_ORDER_AUTOMATIC_INDICATIOR) == "true" ? true : false);
        bibInfoBean.setReceivingDocumentRequiredIndicator(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.RCV_REQ_INT) == "true" ? true : false);
        bibInfoBean.setPaymentRequestPositiveApprovalIndicator(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.PREQ_APPRL_INT) == "true" ? true : false);
        /*bibInfoBean.setChart(properties.getProperty("chart"));
        bibInfoBean.setAccountNumber(properties.getProperty("accountNumber"));
        bibInfoBean.setObjectCode(properties.getProperty("objectCode"));
        bibInfoBean.setPercent(new Long(properties.getProperty("percent")));*/
        if (LOG.isDebugEnabled()) {
            LOG.debug("---------------Billing Name from property--------->" + getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_NM));
        }
        bibInfoBean.setBillingName(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_NM));
        bibInfoBean.setBillingCityName(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_CITY_NM));
        bibInfoBean.setBillingCountryCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_CNTRY_CD));
        bibInfoBean.setBillingLine1Address(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_LIN_ADDR));
        bibInfoBean.setBillingPhoneNumber(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_PHN_NBR));
        bibInfoBean.setBillingPostalCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_POSTAL_CD));
        bibInfoBean.setBillingStateCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_STATE_CD));
       /* bibInfoBean.setLicensingRequirementIndicator(kualiConfigurationService.getPropertyValueAsBoolean("licensingRequirementIndicator"));*/
        /*bibInfoBean.setLicensingRequirementCode(kualiConfigurationService.getPropertyValueAsString("licensingRequirementCode"));*/
        return bibInfoBean;
    }


    @Override
    public String processBibInfoForOperURL(String openUrlString, BibInfoBean bibInfoBean) throws Exception {
        try {
            String user;
            if (GlobalVariables.getUserSession() != null) {
                user = GlobalVariables.getUserSession().getPrincipalName();
            } else {
                kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
                user = kualiConfigurationService.getPropertyValueAsString("userName");
            }
            GlobalVariables.setUserSession(new UserSession(user));
            BuildOpenUrlBibInfoBean buildOpenUrlBibInfoBean = SpringContext.getBean(BuildOpenUrlBibInfoBean.class);
            bibInfoBean = buildOpenUrlBibInfoBean.getBean(bibInfoBean, openUrlString);
            if (bibInfoBean.getTitle() == null || "".equalsIgnoreCase(bibInfoBean.getTitle())) {
                return OleSelectConstant.SOAP_INVALID_OPENURL;
            }
            bibInfoBean.setRequestSourceUrl(openUrlString);
            //bibInfoBean.setRequestSource(OleSelectConstant.REQUEST_SRC_TYPE_WEBFORM);
            bibInfoBean.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_WEBFORM);
            bibInfoBean.setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_WEBFORM);
            bibInfoBean = setBibInfoDefaultValues(bibInfoBean);
            OleRequisitionCreateDocumentService createDocument = SpringContext.getBean(OleRequisitionCreateDocumentServiceImpl.class);
            List<BibInfoBean> bibInfoBeanList = getBibInfoBeanList(bibInfoBean);
            String docNumber = createDocument.saveRequisitionDocument(bibInfoBeanList, true);
            return docNumber;
        } catch (Exception e) {
            LOG.error("Exception processing for SOAP citation document creation----" + e.getMessage(), e);
            String errorMessage = null;
            if (GlobalVariables.getMessageMap().hasErrors()) {
                Map<String, AutoPopulatingList<ErrorMessage>> map = GlobalVariables.getMessageMap().getErrorMessages();
                for (Map.Entry<String, AutoPopulatingList<ErrorMessage>> entry : map.entrySet()) {
                    AutoPopulatingList<ErrorMessage> errors = entry.getValue();
                    ErrorMessage error = errors.get(0);
                    String[] params = error.getMessageParameters();
                    errorMessage = params[0];
                }
            }
            return OleSelectConstant.SOAP_EXCEPTION + " - " + errorMessage;
        }
    }


    @Override
    public String processBibInfoForForm(BibInfoBean bibInfoBean, String title, String author, String edition, String series, String publisher, String placeOfPublication, String yearOfPublication, String standardNumber, String typeOfStandardNumber,
                                        String routeRequesterReceipt) throws Exception {
        try {
            String user;
/*            if(GlobalVariables.getUserSession()!=null){
                user = GlobalVariables.getUserSession().getPrincipalName();
            }else{*/
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
            user = kualiConfigurationService.getPropertyValueAsString("userName");
            // }
            if (LOG.isDebugEnabled()){
                LOG.debug("userName in processBibInfoForForm>>>>>>>>>>>>>>" + user);
            }
            GlobalVariables.setUserSession(new UserSession(user));
            BuildFormBibInfoBean buildFormBibInfoBean = SpringContext.getBean(BuildFormBibInfoBean.class);
            bibInfoBean = buildFormBibInfoBean.getBean(bibInfoBean, title, author, edition, series, publisher, placeOfPublication, yearOfPublication, standardNumber, typeOfStandardNumber, routeRequesterReceipt);
            bibInfoBean = setBibInfoDefaultValues(bibInfoBean);
            //bibInfoBean.setRequestSource(OleSelectConstant.REQUEST_SRC_TYPE_WEBFORM);
            bibInfoBean.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_WEBFORM);
            bibInfoBean.setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_WEBFORM);
            OleRequisitionCreateDocumentService createDocument = SpringContext.getBean(OleRequisitionCreateDocumentServiceImpl.class);
            List<BibInfoBean> bibInfoBeanList = getBibInfoBeanList(bibInfoBean);
            String docNumber = createDocument.saveRequisitionDocument(bibInfoBeanList, true);
            return docNumber;
        } catch (Exception e) {
            LOG.error("Exception processing for SOAP form document creation----" + e.getMessage(), e);
            String errorMessage = null;
            if (GlobalVariables.getMessageMap().hasErrors()) {
                Map<String, AutoPopulatingList<ErrorMessage>> map = GlobalVariables.getMessageMap().getErrorMessages();
                for (Map.Entry<String, AutoPopulatingList<ErrorMessage>> entry : map.entrySet()) {
                    AutoPopulatingList<ErrorMessage> errors = entry.getValue();
                    ErrorMessage error = errors.get(0);
                    String[] params = error.getMessageParameters();
                    errorMessage = params[0];
                }
            }
            return OleSelectConstant.SOAP_EXCEPTION + " - " + errorMessage;
        }
    }

    private List<BibInfoBean> getBibInfoBeanList(BibInfoBean bibInfoBean) throws Exception {
        return Collections.singletonList(bibInfoBean);

    }

    public ConfigurationService getConfigurationService() {
        return kualiConfigurationService;
    }

    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }


}
