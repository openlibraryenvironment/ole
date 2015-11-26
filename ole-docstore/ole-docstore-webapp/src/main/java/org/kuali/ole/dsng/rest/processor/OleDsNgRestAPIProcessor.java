package org.kuali.ole.dsng.rest.processor;

import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.IOException;

/**
 * Created by SheikS on 11/25/2015.
 */
public class OleDsNgRestAPIProcessor {

    private BusinessObjectService businessObjectService;

    public String createBib(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BibRecord bibRecord = objectMapper.readValue(jsonBody, BibRecord.class);
        getBusinessObjectService().save(bibRecord);
        return objectMapper.writeValueAsString(bibRecord);
    }

    private BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}

