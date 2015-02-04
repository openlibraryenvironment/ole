package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OleProxyPatronContract;
import org.kuali.ole.deliver.api.OleProxyPatronDefinition;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.Date;

/**
 * OleProxyPatronDocument provides OleProxyPatronDocument information through getter and setter.
 */
public class OleProxyPatronDocument extends PersistableBusinessObjectBase implements OleProxyPatronContract {

    private String oleProxyPatronDocumentId;
    private String proxyPatronId;
    private String olePatronId;
    private Date proxyPatronExpirationDate;
    private Date proxyPatronActivationDate;
    private boolean active;

    private String proxyPatronBarcode;
    private String proxyPatronFirstName;
    private String proxyPatronLastName;
    private String proxyForPatronFirstName;
    private String proxyForPatronLastName;
    //private OlePatronDocument proxyPatron;
    private OlePatronDocument olePatronDocument;

    /**
     * Gets the value of proxyPatronBarcode property
     *
     * @return proxyPatronBarcode
     */
    public String getProxyPatronBarcode() {
        return proxyPatronBarcode;
    }

    /**
     * Sets the value for proxyPatronBarcode property
     *
     * @param proxyPatronBarcode
     */
    public void setProxyPatronBarcode(String proxyPatronBarcode) {
        this.proxyPatronBarcode = proxyPatronBarcode;
    }

    /**
     * Gets the value of proxyPatronFirstName property
     *
     * @return proxyPatronFirstName
     */
    public String getProxyPatronFirstName() {
        return proxyPatronFirstName;
    }

    /**
     * Sets the value for proxyPatronFirstName property
     *
     * @param proxyPatronFirstName
     */
    public void setProxyPatronFirstName(String proxyPatronFirstName) {
        this.proxyPatronFirstName = proxyPatronFirstName;
    }

    /**
     * Gets the value of proxyPatronLastName property
     *
     * @return proxyPatronLastName
     */
    public String getProxyPatronLastName() {
        return proxyPatronLastName;
    }

    /**
     * Sets the value for proxyPatronLastName property
     *
     * @param proxyPatronLastName
     */
    public void setProxyPatronLastName(String proxyPatronLastName) {
        this.proxyPatronLastName = proxyPatronLastName;
    }

    /**
     * Gets the value of oleProxyPatronDocumentId property
     *
     * @return oleProxyPatronDocumentId
     */
    public String getOleProxyPatronDocumentId() {
        return oleProxyPatronDocumentId;
    }

    /**
     * Sets the value for oleProxyPatronDocumentId property
     *
     * @param oleProxyPatronDocumentId
     */
    public void setOleProxyPatronDocumentId(String oleProxyPatronDocumentId) {
        this.oleProxyPatronDocumentId = oleProxyPatronDocumentId;
    }

    /**
     * Gets the value of proxyPatronId property
     *
     * @return proxyPatronId
     */
    public String getProxyPatronId() {
        return proxyPatronId;
    }

    /**
     * Sets the value for proxyPatronId property
     *
     * @param proxyPatronId
     */
    public void setProxyPatronId(String proxyPatronId) {
        this.proxyPatronId = proxyPatronId;
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

   /* public OlePatronDocument getProxyPatron() {
        return proxyPatron;
    }

    public void setProxyPatron(OlePatronDocument proxyPatron) {
        this.proxyPatron = proxyPatron;
    }*/

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
     * Gets the value of proxyPatronExpirationDate property
     *
     * @return proxyPatronExpirationDate
     */
    public Date getProxyPatronExpirationDate() {
        return proxyPatronExpirationDate;
    }

    /**
     * Sets the value for proxyPatronExpirationDate property
     *
     * @param proxyPatronExpirationDate
     */
    public void setProxyPatronExpirationDate(Date proxyPatronExpirationDate) {
        this.proxyPatronExpirationDate = proxyPatronExpirationDate;
    }

    /**
     * Gets the value of proxyPatronActivationDate property
     *
     * @return proxyPatronActivationDate
     */
    public Date getProxyPatronActivationDate() {
        return proxyPatronActivationDate;
    }

    /**
     * Sets the value for proxyPatronActivationDate property
     *
     * @param proxyPatronActivationDate
     */
    public void setProxyPatronActivationDate(Date proxyPatronActivationDate) {
        this.proxyPatronActivationDate = proxyPatronActivationDate;
    }

    /**
     * Gets the value of active property
     *
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value for active property
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the value of proxyForPatronFirstName property
     *
     * @return proxyForPatronFirstName
     */
    public String getProxyForPatronFirstName() {
        return proxyForPatronFirstName;
    }

    /**
     * Sets the value for proxyForPatronFirstName property
     *
     * @param proxyForPatronFirstName
     */
    public void setProxyForPatronFirstName(String proxyForPatronFirstName) {
        this.proxyForPatronFirstName = proxyForPatronFirstName;
    }

    /**
     * Gets the value of proxyForPatronLastName property
     *
     * @return proxyForPatronLastName
     */
    public String getProxyForPatronLastName() {
        return proxyForPatronLastName;
    }

    /**
     * Sets the value for proxyForPatronLastName property
     *
     * @param proxyForPatronLastName
     */
    public void setProxyForPatronLastName(String proxyForPatronLastName) {
        this.proxyForPatronLastName = proxyForPatronLastName;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OleProxyPatronDocument into immutable object OleProxyPatronDefinition
     *
     * @param bo(OleProxyPatronDefinition)
     * @return OleProxyPatronDefinition
     */
    static OleProxyPatronDefinition to(org.kuali.ole.deliver.bo.OleProxyPatronDocument bo) {
        if (bo == null) {
            return null;
        }
        return OleProxyPatronDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OleProxyPatronDefinition into PersistableBusinessObjectBase OleProxyPatronDocument
     *
     * @param im(OleProxyPatronDefinition)
     * @return bo(OlePatronAffiliation)
     */
    public static org.kuali.ole.deliver.bo.OleProxyPatronDocument from(OleProxyPatronDefinition im) {
        if (im == null) {
            return null;
        }

        org.kuali.ole.deliver.bo.OleProxyPatronDocument bo = new org.kuali.ole.deliver.bo.OleProxyPatronDocument();
        bo.oleProxyPatronDocumentId = im.getOleProxyPatronDocumentId();
        bo.olePatronId = im.getOlePatronId();
        bo.proxyPatronId = im.getProxyPatronId();
       /* if (im.getOlePatronDocument() != null) {
            bo.olePatronDocument = OlePatronDocument.from(im.getOlePatronDocument());
        }*/
        bo.active = im.isActive();
        bo.proxyPatronExpirationDate = im.getProxyPatronExpirationDate();
        bo.proxyPatronActivationDate = im.getProxyPatronActivationDate();
        bo.versionNumber = im.getVersionNumber();


        return bo;
    }


    @Override
    public String getId() {
        return this.oleProxyPatronDocumentId;
    }
}
