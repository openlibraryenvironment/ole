package org.kuali.ole.deliver.inquiry;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.datadictionary.exception.UnknownBusinessClassAttributeException;
import org.kuali.rice.krad.inquiry.InquirableImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/24/12
 * Time: 5:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestInquirableImpl extends InquirableImpl {
    public OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
    private DocstoreUtil docstoreUtil;
    public DocstoreUtil getDocstoreUtil() {

        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);

        }
        return docstoreUtil;
    }



    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDeliverRequestInquirableImpl.class);

    @Override
    public Object retrieveDataObject(Map<String, String> parameters) {
        if (dataObjectClass == null) {
            LOG.error("Data object class must be set in inquirable before retrieving the object");
            throw new RuntimeException("Data object class must be set in inquirable before retrieving the object");
        }

        // build list of key values from the map parameters
        List<String> pkPropertyNames = getDataObjectMetaDataService().listPrimaryKeyFieldNames(dataObjectClass);

        // some classes might have alternate keys defined for retrieving
        List<List<String>> alternateKeyNames = this.getAlternateKeysForClass(dataObjectClass);

        // add pk set as beginning so it will be checked first for match
        alternateKeyNames.add(0, pkPropertyNames);

        List<String> dataObjectKeySet = retrieveKeySetFromMap(alternateKeyNames, parameters);
        if ((dataObjectKeySet == null) || dataObjectKeySet.isEmpty()) {
            LOG.warn("Matching key set not found in request for class: " + getDataObjectClass());

            return null;
        }

        // found key set, now build map of key values pairs we can use to retrieve the object
        Map<String, Object> keyPropertyValues = new HashMap<String, Object>();
        for (String keyPropertyName : dataObjectKeySet) {
            String keyPropertyValue = parameters.get(keyPropertyName);

            // uppercase value if needed
            Boolean forceUppercase = Boolean.FALSE;
            try {
                forceUppercase = getDataDictionaryService().getAttributeForceUppercase(dataObjectClass,
                        keyPropertyName);
            } catch (UnknownBusinessClassAttributeException ex) {
                // swallowing exception because this check for ForceUppercase would
                // require a DD entry for the attribute, and we will just set force uppercase to false
                LOG.warn("Data object class "
                        + dataObjectClass
                        + " property "
                        + keyPropertyName
                        + " should probably have a DD definition.", ex);
            }

            if (forceUppercase.booleanValue() && (keyPropertyValue != null)) {
                keyPropertyValue = keyPropertyValue.toUpperCase();
            }

            // check security on key field
            if (getDataObjectAuthorizationService().attributeValueNeedsToBeEncryptedOnFormsAndLinks(dataObjectClass,
                    keyPropertyName)) {
                try {
                    keyPropertyValue = getEncryptionService().decrypt(keyPropertyValue);
                } catch (GeneralSecurityException e) {
                    LOG.error("Data object class "
                            + dataObjectClass
                            + " property "
                            + keyPropertyName
                            + " should have been encrypted, but there was a problem decrypting it.", e);
                    throw new RuntimeException("Data object class "
                            + dataObjectClass
                            + " property "
                            + keyPropertyName
                            + " should have been encrypted, but there was a problem decrypting it.", e);
                }
            }

            keyPropertyValues.put(keyPropertyName, keyPropertyValue);
        }

        // now retrieve the object based on the key set
        Object dataObject = null;

        ModuleService moduleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(
                getDataObjectClass());
        if (moduleService != null && moduleService.isExternalizable(getDataObjectClass())) {
            dataObject = moduleService.getExternalizableBusinessObject(getDataObjectClass().asSubclass(
                    ExternalizableBusinessObject.class), keyPropertyValues);
        } else if (BusinessObject.class.isAssignableFrom(getDataObjectClass())) {
            dataObject = getBusinessObjectService().findByPrimaryKey(getDataObjectClass().asSubclass(
                    BusinessObject.class), keyPropertyValues);
        }
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) dataObject;
        if (oleDeliverRequestBo.getOlePatron() != null && oleDeliverRequestBo.getOlePatron().getEntity() != null && oleDeliverRequestBo.getOlePatron().getEntity().getNames() != null && oleDeliverRequestBo.getOlePatron().getEntity().getNames().size() > 0) {
            EntityNameBo nameBo = oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0);
            oleDeliverRequestBo.getOlePatron().setPatronName(oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
        }
        EntityNameBo entityNameBo = getEntityNameBo(oleDeliverRequestBo.getBorrowerId());
        if (oleDeliverRequestBo.getBorrowerId() != null && entityNameBo != null) {
            oleDeliverRequestBo.setFirstName(entityNameBo.getFirstName());
            oleDeliverRequestBo.setLastName(entityNameBo.getLastName());
        }
        EntityNameBo entityNameBo1 = getEntityNameBo(oleDeliverRequestBo.getProxyBorrowerId());
        if (oleDeliverRequestBo.getProxyBorrowerId() != null && entityNameBo1 != null) {
            oleDeliverRequestBo.setProxyBorrowerName(entityNameBo1.getFirstName() + entityNameBo1.getLastName());
            oleDeliverRequestBo.setProxyBorrowerFirstName(entityNameBo1.getFirstName());
            oleDeliverRequestBo.setProxyBorrowerLastName(entityNameBo1.getLastName());
        }
        if (oleDeliverRequestBo.getOperatorCreateId() != null && !oleDeliverRequestBo.getOperatorCreateId().isEmpty()) {
            oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR);
        } else if (oleDeliverRequestBo.getProxyBorrowerId() != null && !oleDeliverRequestBo.getProxyBorrowerId().isEmpty()) {
            oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_PROXY_PATRON);
        } else {
            oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_PATRON);
        }
        // oleDeliverRequestBo =  oleDeliverRequestDocumentHelperService.processItem(oleDeliverRequestBo);
        getDocstoreUtil().isItemAvailableInDocStore(oleDeliverRequestBo);
        return oleDeliverRequestBo;
    }

    public EntityNameBo getEntityNameBo(String entityId) {
        EntityBo entityBo = getEntity(entityId);
        if (entityBo != null) {
            if (entityBo.getNames() != null && entityBo.getNames().size() > 0) {
                return entityBo.getNames().get(0);
            }
        }
        return null;
    }

    public EntityBo getEntity(String entityId) {
        Map<String, String> entityMap = new HashMap<>();
        entityMap.put("id", entityId);
        List<EntityBo> entityBoList = (List<EntityBo>) KRADServiceLocator.getBusinessObjectService().findMatching(EntityBo.class, entityMap);
        if (entityBoList.size() > 0)
            return entityBoList.get(0);
        return null;
    }

}