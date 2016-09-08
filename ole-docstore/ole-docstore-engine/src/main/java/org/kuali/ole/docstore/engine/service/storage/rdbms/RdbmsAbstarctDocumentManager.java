package org.kuali.ole.docstore.engine.service.storage.rdbms;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.DocStoreConstants;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.docstore.common.exception.DocstoreValidationException;
import org.kuali.ole.docstore.service.OleUuidCheckWebService;
import org.kuali.ole.docstore.service.OleWebServiceProvider;
import org.kuali.ole.docstore.service.impl.OleWebServiceProviderImpl;
import org.kuali.ole.utility.Constants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RdbmsAbstarctDocumentManager implements DocumentManager {
    private static final Logger LOG = LoggerFactory.getLogger(RdbmsAbstarctDocumentManager.class);
    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    protected Timestamp createdDate() {
        String createDateStr = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Timestamp createdDate = null;
        try {
            createdDate = new Timestamp(df.parse(createDateStr).getTime());
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        return createdDate;
    }

    public Timestamp getTimeStampFromString(String date) {
        DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        Timestamp createdDate = null;
        try {
            if (StringUtils.isNotBlank(date)) {
                createdDate = new Timestamp(df.parse(date).getTime());
            }
        } catch (ParseException e) {
            LOG.error("Exception : ", e);
        }
        return createdDate;
    }

    @Override
    public List<Object> retrieve(List<String> id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    protected void addDataToLabel(StringBuilder label, String data) {
        if (data.length() > 0) {
            if (label.length() > 0) {
                label.append("-");
            }
            label.append(data);
        }
    }

    protected Map getBibMap(String id) {
        Map bibMap = new HashMap();
        bibMap.put("bibId", DocumentUniqueIDPrefix.getDocumentId(id));
        return bibMap;
    }

    protected Map getItemMap(String id) {
        Map itemMap = new HashMap();
        itemMap.put("itemId", DocumentUniqueIDPrefix.getDocumentId(id));
        return itemMap;
    }

    protected Map getHoldingsMap(String holdingsId) {
        Map map = new HashMap();
        map.put("holdingsId", DocumentUniqueIDPrefix.getDocumentId(holdingsId));
        return map;
    }

    /**
     * This method checks the existence of linked documents for the passed uuids. If exists throws exception with appropriate error message.
     * @param uuids
     * @param uuidCount
     */
    protected void checkUuidsToDelete(String uuids, int uuidCount) {
        OleUuidCheckWebService oleUuidCheckWebService = null;
        try {
            oleUuidCheckWebService = getOleUuidCheckWebService();
        } catch (Exception e) {
            LOG.error("Deletion failed: Exception while connecting to uuid check web service to verify the existence of documents.", e);
            throw new DocstoreValidationException(DocstoreResources.SERVICE_CONNECTION_FAILED, DocstoreResources.SERVICE_CONNECTION_FAILED);
        }
        String responseString = oleUuidCheckWebService.checkUuidExsistence(uuids);
        if (StringUtils.isNotBlank(responseString)) {
            String[] responseSplit = responseString.split("\\|");
            String[] responseUuids = null;
            if (2 == responseSplit.length) {
                if (responseSplit[0] != null && responseSplit[1] != null) {
                    if (StringUtils.isNotEmpty(responseSplit[0])) {
                        responseUuids = responseSplit[0].split(DocStoreConstants.COMMA);
                    } else {
                        responseUuids = new String[0];
                    }
                    if (uuidCount > responseUuids.length) {
                        String deleteMessage = getDeleteMessageByLink(responseSplit[1]);
                        DocstoreException docstoreException = new DocstoreValidationException(deleteMessage, deleteMessage);
                        throw docstoreException;
                    }
                }
            }
        }
    }

    /**
     * This method returns the appropriate message for the linked documents.
     * @param links
     * @return
     */
    private String getDeleteMessageByLink(String links) {
        if (links.contains(DocStoreConstants.COPY)) {
            return DocstoreResources.COPY_DELETE_MESSAGE;
        } else if (links.contains(DocStoreConstants.REQUISITION)) {
            return DocstoreResources.REQUISITION_DELETE_MESSAGE;
        } else if (links.contains(DocStoreConstants.PURCHASE_ORDER)) {
            return DocstoreResources.PURCHASE_ORDER_DELETE_MESSAGE;
        } else if (links.contains(DocStoreConstants.PAYMENT_REQUEST)) {
            return DocstoreResources.PAYMENT_REQUEST_DELETE_MESSAGE;
        } else if (links.contains(DocStoreConstants.LINE_ITEM)) {
            return DocstoreResources.LINE_ITEM_DELETE_MESSAGE;
        } else if (links.contains(DocStoreConstants.LOAN)) {
            return DocstoreResources.LOAN_DELETE_MESSAGE;
        } else if (links.contains(DocStoreConstants.REQUEST)) {
            return DocstoreResources.REQUEST_DELETE_MESSAGE;
        } else if (links.contains(DocStoreConstants.SERIAL_RECEIVING)) {
            return DocstoreResources.SERIAL_RECEIVING_DELETE_MESSAGE;
        }
        return "";
    }

    /**
     * This method returns org.kuali.ole.docstore.service.OleUuidCheckWebService
     * @return
     * @throws Exception
     */
    private OleUuidCheckWebService getOleUuidCheckWebService() throws Exception {
        String serviceURL = ConfigContext.getCurrentContextConfig().getProperty(DocStoreConstants.UUID_CHECK_WEB_SERVICE_URL);
        LOG.info(" uuidCheckServiceURL --------> " + serviceURL);
        OleWebServiceProvider oleWebServiceProvider = new OleWebServiceProviderImpl();
        return (OleUuidCheckWebService) oleWebServiceProvider.getService(DocStoreConstants.UUID_CHECK_WEB_SERVICE_CLASS, DocStoreConstants.UUID_CHECK_WEB_SERVICE, serviceURL);
    }

    public String getParameter(String applicationId, String namespace, String componentId, String parameterName) {
        ParameterKey parameterKey = ParameterKey.create(applicationId, namespace, componentId, parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }

    public boolean isAuditRequired() {
        return ParameterValueResolver.getInstance().getParameterAsBoolean(OleNGConstants
                .APPL_ID_OLE, OleNGConstants.DESC_NMSPC, OleNGConstants.DESCRIBE_COMPONENT, OleNGConstants.PROCESS_AUDIT_FOR_BIB_HOLDINGS_ITEM);
    }

}
