package org.kuali.ole.deliver.service;

import org.kuali.ole.alert.bo.AlertConditionAndReceiverInformation;

/**
 * Created by arunag on 12/30/14.
 */
public interface AlertDocumentService {

    public boolean validateRole(AlertConditionAndReceiverInformation alertConditionAndReceiverInformation);
    public boolean validateGroup(AlertConditionAndReceiverInformation alertConditionAndReceiverInformation);
    public boolean validatePerson(AlertConditionAndReceiverInformation alertConditionAndReceiverInformation);
}
