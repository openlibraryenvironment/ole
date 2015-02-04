package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sambasivam on 18/11/14.
 */
public class OLEEResourceRecordDocumentKeyValues extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        Collection<OLEEResourceRecordDocument> oleeResourceRecordDocuments = (List) KRADServiceLocator.getBusinessObjectService().findAll(OLEEResourceRecordDocument.class);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OLEEResourceRecordDocument oleeResourceRecordDocument : oleeResourceRecordDocuments) {
            try {
                WorkflowDocument workflowDocument = KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(oleeResourceRecordDocument.getDocumentNumber(), principalPerson);
                if (workflowDocument != null) {
                    keyValues.add(new ConcreteKeyValue(oleeResourceRecordDocument.getOleERSIdentifier(), oleeResourceRecordDocument.getTitle()));
                }
            } catch (WorkflowException e) {
                e.printStackTrace();
            }
        }
        return keyValues;
    }
}

