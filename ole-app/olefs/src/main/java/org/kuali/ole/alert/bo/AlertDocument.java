package org.kuali.ole.alert.bo;

import org.kuali.ole.alert.bo.AlertConditionAndReceiverInformation;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 8/12/14.
 */
public class AlertDocument extends PersistableBusinessObjectBase {

    private String alertDocumentId;

    private String documentTypeId;

    private String documentTypeName;

    private String documentClassName;

    private String alertDocumentCreatorId;

    private String alertDocumentCreatorName;

    private boolean repeatable;

    private AlertDocumentType alertDocumentType;

  /*  private List<AlertFieldValueMapping> alertFieldValueMappings=new ArrayList<>();*/

    private List<AlertConditionAndReceiverInformation> alertConditionAndReceiverInformations=new ArrayList<>();


    public String getAlertDocumentId() {
        return alertDocumentId;
    }

    public void setAlertDocumentId(String alertDocumentId) {
        this.alertDocumentId = alertDocumentId;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public String getAlertDocumentCreatorId() {
        return alertDocumentCreatorId;
    }

    public void setAlertDocumentCreatorId(String alertDocumentCreatorId) {
        this.alertDocumentCreatorId = alertDocumentCreatorId;
    }

/*    public List<AlertFieldValueMapping> getAlertFieldValueMappings() {
        return alertFieldValueMappings;
    }

    public void setAlertFieldValueMappings(List<AlertFieldValueMapping> alertFieldValueMappings) {
        this.alertFieldValueMappings = alertFieldValueMappings;
    }*/

    public List<AlertConditionAndReceiverInformation> getAlertConditionAndReceiverInformations() {
        return alertConditionAndReceiverInformations;
    }

    public void setAlertConditionAndReceiverInformations(List<AlertConditionAndReceiverInformation> alertConditionAndReceiverInformations) {
        this.alertConditionAndReceiverInformations = alertConditionAndReceiverInformations;
    }

    public String getDocumentClassName() {
          if(documentClassName == null && alertDocumentType!=null){
              documentClassName = alertDocumentType.getAlertDocumentClass();
          }
        return documentClassName;
    }

    public void setDocumentClassName(String documentClassName) {
        this.documentClassName = documentClassName;
    }

    public String getAlertDocumentCreatorName() {
        if(alertDocumentCreatorId != null){
           Person person =  KimApiServiceLocator.getPersonService().getPerson(alertDocumentCreatorId);
        alertDocumentCreatorName = person.getName();
        }
        return alertDocumentCreatorName;
    }

    public void setAlertDocumentCreatorName(String alertDocumentCreatorName) {
        this.alertDocumentCreatorName = alertDocumentCreatorName;
    }

    public AlertDocumentType getAlertDocumentType() {
        return alertDocumentType;
    }

    public void setAlertDocumentType(AlertDocumentType alertDocumentType) {
        this.alertDocumentType = alertDocumentType;
    }

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }
}
