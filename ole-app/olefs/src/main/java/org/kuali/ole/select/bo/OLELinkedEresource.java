package org.kuali.ole.select.bo;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by srirams on 30/9/14.
 */
public class OLELinkedEresource extends PersistableBusinessObjectBase {

    private String linkedEresourceId;

    private String linkedERSIdentifier;

    private String oleERSIdentifier;

    private String relationShipType;

    private String chainString;

    private boolean removeRelationShip;

    private OLEEResourceRecordDocument oleeResourceRecordDocument;

    public String getLinkedEresourceId() {
        return linkedEresourceId;
    }

    public void setLinkedEresourceId(String linkedEresourceId) {
        this.linkedEresourceId = linkedEresourceId;
    }

    public String getLinkedERSIdentifier() {
        return linkedERSIdentifier;
    }

    public void setLinkedERSIdentifier(String linkedERSIdentifier) {
        this.linkedERSIdentifier = linkedERSIdentifier;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getRelationShipType() {
        return relationShipType;
    }

    public void setRelationShipType(String relationShipType) {
        this.relationShipType = relationShipType;
    }

    public OLEEResourceRecordDocument getOleeResourceRecordDocument() {
        Map eResMap = new HashMap();
        eResMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, this.linkedERSIdentifier);
        oleeResourceRecordDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, eResMap);
        return oleeResourceRecordDocument;
    }

    public void setOleeResourceRecordDocument(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        this.oleeResourceRecordDocument = oleeResourceRecordDocument;
    }

    public boolean isRemoveRelationShip() {
        return removeRelationShip;
    }

    public void setRemoveRelationShip(boolean removeRelationShip) {
        this.removeRelationShip = removeRelationShip;
    }

    public String getChainString() {
        return chainString;
    }

    public void setChainString(String chainString) {
        this.chainString = chainString;
    }
}
