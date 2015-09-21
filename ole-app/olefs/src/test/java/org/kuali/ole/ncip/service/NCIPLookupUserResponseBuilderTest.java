package org.kuali.ole.ncip.service;

import org.extensiblecatalog.ncip.v2.service.LookupUserResponseData;
import org.junit.Test;
import org.kuali.ole.deliver.util.XMLFormatterUtil;
import org.kuali.ole.ncip.util.OLENCIPUtil;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 8/10/15.
 */
public class NCIPLookupUserResponseBuilderTest {

    @Test
    public void addErrorResponse() throws Exception {
        LookupUserResponseData lookupUserResponseData = new LookupUserResponseData();
        OLENCIPUtil olencipUtil = new OLENCIPUtil();

        olencipUtil.processProblems(lookupUserResponseData, "problem", "patronId", "InValid barcode");

        assertNotNull(lookupUserResponseData);

        System.out.println(lookupUserResponseData);


    }

}