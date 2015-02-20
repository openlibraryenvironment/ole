package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OleAddressContract;
import org.kuali.ole.deliver.api.OleAddressDefinition;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.Date;

/**
 * OleAddressBo provides OleAddressBo information through getter and setter.
 */
public class OleAddressBo extends PersistableBusinessObjectBase implements OleAddressContract {

    private String oleAddressId;
    private String olePatronId;
    private String id;
    private boolean addressVerified;
    private Date addressValidFrom;
    private Date addressValidTo;
    private String addressSource;
    private String oleAddressSourceName;
    private OleAddressSourceBo addressSourceBo = new OleAddressSourceBo();
    private EntityAddressBo entityAddress;
    private OlePatronDocument olePatronDocument;
    private boolean deliverAddress;

    public OleAddressBo() {
        this.setAddressVerified(true);
    }

    /**
     * Gets the value of olePatronDocument property
     *
     * @return olePatronDocument
     */
    public OlePatronDocument getOlePatronDocument() {
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
     * Gets the value of oleAddressId property
     *
     * @return oleAddressId
     */
    public String getOleAddressId() {
        return oleAddressId;
    }

    /**
     * Sets the value for oleAddressId property
     *
     * @param oleAddressId
     */
    public void setOleAddressId(String oleAddressId) {
        this.oleAddressId = oleAddressId;
    }

    /**
     * Gets the value of addressVerified property
     *
     * @return addressVerified
     */
    public boolean isAddressVerified() {
        return addressVerified;
    }

    /**
     * Sets the value for addressVerified property
     *
     * @param addressVerified
     */
    public void setAddressVerified(boolean addressVerified) {
        this.addressVerified = addressVerified;
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
     * Gets the value of addressValidFrom property
     *
     * @return addressValidFrom
     */
    public Date getAddressValidFrom() {
        return addressValidFrom;
    }

    /**
     * Sets the value for addressValidFrom property
     *
     * @param addressValidFrom
     */
    public void setAddressValidFrom(Date addressValidFrom) {
        this.addressValidFrom = addressValidFrom;
    }

    /**
     * Gets the value of addressValidTo property
     *
     * @return addressValidTo
     */
    public Date getAddressValidTo() {
        return addressValidTo;
    }

    /**
     * Sets the value for addressValidTo property
     *
     * @param addressValidTo
     */
    public void setAddressValidTo(Date addressValidTo) {
        this.addressValidTo = addressValidTo;
    }

    /**
     * Gets the value of addressSource property
     *
     * @return addressSource
     */
    public String getAddressSource() {
        return addressSource;
    }

    /**
     * Sets the value for addressSource property
     *
     * @param addressSource
     */
    public void setAddressSource(String addressSource) {
        this.addressSource = addressSource;
    }

    /**
     * Gets the value of addressSourceBo property
     *
     * @return addressSourceBo
     */
    public OleAddressSourceBo getAddressSourceBo() {
        return addressSourceBo;
    }

    /**
     * Sets the value for addressSourceBo property
     *
     * @param addressSourceBo
     */
    public void setAddressSourceBo(OleAddressSourceBo addressSourceBo) {
        this.addressSourceBo = addressSourceBo;
    }

    /**
     * Gets the value of entityAddress property
     *
     * @return entityAddress
     */
    public EntityAddressBo getEntityAddress() {
        return entityAddress;
    }

    /**
     * Sets the value for entityAddress property
     *
     * @param entityAddress
     */
    public void setEntityAddress(EntityAddressBo entityAddress) {
        this.entityAddress = entityAddress;
    }

    /**
     * Gets the value of addressSourceBo property
     *
     * @return addressSourceBo
     */
    public String getOleAddressSourceName() {
        if (addressSourceBo != null) {
            return addressSourceBo.getOleAddressSourceName();
        }
        return null;

    }

    /**
     * This method converts the PersistableBusinessObjectBase OleAddressBo into immutable object OleAddressDefinition
     *
     * @param bo
     * @return OleAddressDefinition
     */
    public static OleAddressDefinition to(org.kuali.ole.deliver.bo.OleAddressBo bo) {
        if (bo == null) {
            return null;
        }
        return OleAddressDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OleAddressDefinition into PersistableBusinessObjectBase OleAddressBo
     *
     * @param imOleAddressDefinition
     * @return bo
     */
    public static org.kuali.ole.deliver.bo.OleAddressBo from(OleAddressDefinition imOleAddressDefinition) {
        if (imOleAddressDefinition == null) {
            return null;
        }

        org.kuali.ole.deliver.bo.OleAddressBo bo = new org.kuali.ole.deliver.bo.OleAddressBo();
        bo.oleAddressId = imOleAddressDefinition.getOleAddressId();
        bo.olePatronId = imOleAddressDefinition.getOlePatronId();
        //bo.olePatron = OlePatronDocument.from(im.getOlePatron());
        bo.id = imOleAddressDefinition.getId();
        bo.addressVerified = imOleAddressDefinition.isAddressVerified();
        bo.addressValidFrom = imOleAddressDefinition.getAddressValidFrom();
        bo.addressValidTo = imOleAddressDefinition.getAddressValidTo();
        bo.addressSource = imOleAddressDefinition.getAddressSource();
        bo.versionNumber = imOleAddressDefinition.getVersionNumber();
        /*if (im.getEntityAddress() != null) {
            bo.entityAddress = EntityAddressBo.from(im.getEntityAddress());
        }*/
        if (imOleAddressDefinition.getAddressSourceBo() != null) {
            bo.addressSourceBo = OleAddressSourceBo.from(imOleAddressDefinition.getAddressSourceBo());
        }

        return bo;
    }

    /**
     * Gets the value of id property
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value for id property
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(boolean deliverAddress) {
        this.deliverAddress = deliverAddress;
    }
}