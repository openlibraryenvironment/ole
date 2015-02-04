package org.kuali.ole.deliver.bo;

import org.kuali.ole.alert.bo.AlertField;
import org.kuali.ole.alert.document.OlePersistableBusinessObjectBase;
import org.kuali.ole.deliver.api.OleDeliverRequestTypeContract;
import org.kuali.ole.deliver.api.OleDeliverRequestTypeDefinition;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/8/12
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestType extends OlePersistableBusinessObjectBase implements OleDeliverRequestTypeContract {
    private String requestTypeId;
    @AlertField
    private String requestTypeCode;
    @AlertField
    private String requestTypeName;
    private String requestTypeDescription;
    private boolean active;

    public String getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(String requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public String getRequestTypeDescription() {
        return requestTypeDescription;
    }

    public void setRequestTypeDescription(String requestTypeDescription) {
        this.requestTypeDescription = requestTypeDescription;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String getId() {
        return this.requestTypeId;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OleDeliverRequestType into immutable object OleDeliverRequestTypeDefinition
     *
     * @param bo
     * @return OleDeliverRequestTypeDefinition
     */
    public static OleDeliverRequestTypeDefinition to(OleDeliverRequestType bo) {
        if (bo == null) {
            return null;
        }
        return OleDeliverRequestTypeDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OleDeliverRequestTypeDefinition into PersistableBusinessObjectBase OleDeliverRequestType
     *
     * @param imOleDeliverRequestTypeDefinition
     *
     * @return bo
     */
    public static OleDeliverRequestType from(OleDeliverRequestTypeDefinition imOleDeliverRequestTypeDefinition) {
        if (imOleDeliverRequestTypeDefinition == null) {
            return null;
        }

        OleDeliverRequestType bo = new OleDeliverRequestType();
        bo.requestTypeId = imOleDeliverRequestTypeDefinition.getRequestTypeId();
        bo.requestTypeCode = imOleDeliverRequestTypeDefinition.getRequestTypeCode();
        bo.requestTypeDescription = imOleDeliverRequestTypeDefinition.getRequestTypeDescription();
        bo.requestTypeName = imOleDeliverRequestTypeDefinition.getRequestTypeName();
        bo.active = imOleDeliverRequestTypeDefinition.isActive();
        bo.versionNumber = imOleDeliverRequestTypeDefinition.getVersionNumber();

        return bo;
    }
}
