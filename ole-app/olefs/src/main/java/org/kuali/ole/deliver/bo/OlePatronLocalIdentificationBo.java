package org.kuali.ole.deliver.bo;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.api.OlePatronLocalIdentificationContract;
import org.kuali.ole.deliver.api.OlePatronLocalIdentificationDefinition;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * OlePatronLocalIdentificationBo provides local id of patron through getter and setter.
 */

public class OlePatronLocalIdentificationBo extends PersistableBusinessObjectBase implements OlePatronLocalIdentificationContract {

    private String patronLocalSeqId;
    private String localId;
    private String olePatronId;
    private OlePatronDocument olePatronDocument;

    /**
     * Gets the value of patronLocalSeqId property
     *
     * @return patronLocalSeqId
     */
    public String getPatronLocalSeqId() {
        return patronLocalSeqId;
    }

    /**
     * Sets the value for patronLocalSeqId property
     *
     * @param patronLocalSeqId
     */
    public void setPatronLocalSeqId(String patronLocalSeqId) {
        this.patronLocalSeqId = patronLocalSeqId;
    }

    /**
     * Gets the value of localId property
     *
     * @return localId
     */
    public String getLocalId() {
        return localId;
    }

    /**
     * Sets the value for localId property
     *
     * @param localId
     */
    public void setLocalId(String localId) {
        this.localId = localId;
    }

    /**
     * Gets the value of olePatronId property
     *
     * @return olePatronId
     */
    public String getOlePatronId() {
        return olePatronId;
    }

    /**
     * Sets the value for olePatronId property
     *
     * @param olePatronId
     */
    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    /**
     * Gets the value of olePatronDocument property
     *
     * @return olePatronDocument
     */
    public OlePatronDocument getOlePatronDocument() {
        if (null == olePatronDocument) {
            String patronId = getOlePatronId();
            if (StringUtils.isNotEmpty(patronId)) {
                Map<String, String> parameterMap = new HashMap<>();
                parameterMap.put("olePatronId", patronId);
                olePatronDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, parameterMap);
            }
        }
        return olePatronDocument;
    }

    /**
     * Sets the value for olePatronDocument property
     *
     * @param olePatronDocument
     */
    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OleAddressBo into immutable object OleAddressDefinition
     *
     * @param bo
     * @return OleAddressDefinition
     */
    public static OlePatronLocalIdentificationDefinition to(org.kuali.ole.deliver.bo.OlePatronLocalIdentificationBo bo) {
        if (bo == null) {
            return null;
        }
        return OlePatronLocalIdentificationDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OleAddressDefinition into PersistableBusinessObjectBase OleAddressBo
     *
     * @param imOleAddressDefinition
     * @return bo
     */
    public static org.kuali.ole.deliver.bo.OlePatronLocalIdentificationBo from(OlePatronLocalIdentificationDefinition imOleAddressDefinition) {
        if (imOleAddressDefinition == null) {
            return null;
        }

        org.kuali.ole.deliver.bo.OlePatronLocalIdentificationBo bo = new org.kuali.ole.deliver.bo.OlePatronLocalIdentificationBo();
        bo.patronLocalSeqId = imOleAddressDefinition.getPatronLocalSeqId();
        bo.olePatronId = imOleAddressDefinition.getOlePatronId();
        bo.localId = imOleAddressDefinition.getLocalId();
        bo.versionNumber = imOleAddressDefinition.getVersionNumber();

        return bo;
    }

    @Override
    public String getId() {
        return this.patronLocalSeqId;
    }
}