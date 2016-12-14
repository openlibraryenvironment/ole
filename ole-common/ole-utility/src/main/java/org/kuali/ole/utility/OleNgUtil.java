package org.kuali.ole.utility;

import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.Exchange;
import org.kuali.ole.utility.JSONHelperUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;

/**
 * Created by SheikS on 12/18/2015.
 */
public class OleNgUtil extends JSONHelperUtil {
    private BusinessObjectService businessObjectService;
    private ObjectMapper objectMapper;



    public BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public ObjectMapper getObjectMapper() {
        if(null == objectMapper) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public void addValidationErrorMessageToExchange(Exchange exchange, String validationErrorMessage) {
        ArrayList errorMessages = (ArrayList) exchange.get("validationErrorMessages");
        if(null == errorMessages) {
            errorMessages = new ArrayList<>();
        }
        errorMessages.add(validationErrorMessage);
        exchange.add("validationErrorMessages", errorMessages);
    }
}
