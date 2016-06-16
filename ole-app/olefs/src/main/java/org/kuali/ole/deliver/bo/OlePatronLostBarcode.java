package org.kuali.ole.deliver.bo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.api.OlePatronLostBarcodeContract;
import org.kuali.ole.deliver.api.OlePatronLostBarcodeDefinition;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OlePatronDocument provides OlePatronDocument information through getter and setter.
 */
public class OlePatronLostBarcode extends PersistableBusinessObjectBase implements OlePatronLostBarcodeContract {

    private String olePatronLostBarcodeId;
    private String olePatronId;
    private Timestamp invalidOrLostBarcodeEffDate;
    private String invalidOrLostBarcodeNumber;
    private OlePatronDocument olePatronDocument;
    private boolean revertBarcode;
    private String status;
    private String description;
    private String operatorId;
    private String operatorName;
    private boolean active;
    public String getOlePatronLostBarcodeId() {
        return olePatronLostBarcodeId;
    }

    public void setOlePatronLostBarcodeId(String olePatronLostBarcodeId) {
        this.olePatronLostBarcodeId = olePatronLostBarcodeId;
    }

    public String getOlePatronId() {
        return olePatronId;
    }

    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    @Override
    public Timestamp getInvalidOrLostBarcodeEffDate() {
        return invalidOrLostBarcodeEffDate;
    }

    public void setInvalidOrLostBarcodeEffDate(Timestamp invalidOrLostBarcodeEffDate) {
        this.invalidOrLostBarcodeEffDate = invalidOrLostBarcodeEffDate;
    }

    public String getInvalidOrLostBarcodeNumber() {
        return invalidOrLostBarcodeNumber;
    }

    public void setInvalidOrLostBarcodeNumber(String invalidOrLostBarcodeNumber) {
        this.invalidOrLostBarcodeNumber = invalidOrLostBarcodeNumber;
    }

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

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OlePatronNotes into immutable object OlePatronNotesDefinition
     *
     * @param bo
     * @return OlePatronNotesDefinition
     */
    public static OlePatronLostBarcodeDefinition to(org.kuali.ole.deliver.bo.OlePatronLostBarcode bo) {
        if (bo == null) {
            return null;
        }
        return OlePatronLostBarcodeDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OlePatronNotesDefinition into PersistableBusinessObjectBase OlePatronNotes
     *
     * @param im
     * @return bo
     */
    public static org.kuali.ole.deliver.bo.OlePatronLostBarcode from(OlePatronLostBarcodeDefinition im) {
        if (im == null) {
            return null;
        }

        org.kuali.ole.deliver.bo.OlePatronLostBarcode bo = new org.kuali.ole.deliver.bo.OlePatronLostBarcode();
        bo.olePatronLostBarcodeId = im.getOlePatronLostBarcodeId();
        bo.olePatronId = im.getOlePatronId();
        //bo.olePatron = OlePatronDocument.from(im.getOlePatron());
        bo.invalidOrLostBarcodeEffDate = im.getInvalidOrLostBarcodeEffDate();
        bo.invalidOrLostBarcodeNumber = im.getInvalidOrLostBarcodeNumber();
        bo.versionNumber = im.getVersionNumber();

        return bo;
    }

    public boolean isRevertBarcode() {
        return revertBarcode;
    }

    public void setRevertBarcode(boolean revertBarcode) {
        this.revertBarcode = revertBarcode;
    }

    @Override
    public String getId() {
        return this.olePatronLostBarcodeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOperatorId() {
        getOperatorName();
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        if(!StringUtils.isNotBlank(operatorName) && operatorId != null) {
            Person people = SpringContext.getBean(PersonService.class).getPerson(operatorId);
            operatorName = people.getPrincipalName();
        }
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}