package org.kuali.ole.describe.service.impl;

import org.junit.Test;
import org.kuali.ole.OleEditorResponseHandler;
import org.kuali.ole.describe.service.OleWebServiceProvider;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.select.service.OleExposedWebService;
import org.kuali.ole.select.service.impl.OleExposedWebServiceImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.HashMap;

import static junit.framework.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/15/12
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleWebServiceProviderImpl_UT {
    private String editorResponseXMLForOLE;

    @Test
    public void testGetService() {
        OleWebServiceProvider oleWebServiceProvider = new OleWebServiceProviderImpl();
        OleExposedWebService oleExposedWebService = new OleExposedWebServiceImpl();

        OleEditorResponse oleEditorResponse = new OleEditorResponse();
        OleBibRecord oleBibRecord = new OleBibRecord();
        oleBibRecord.setBibUUID("12324235");
        oleBibRecord.setLinkedInstanceId("12252qwd253");
        HashMap bibAssociatedFieldsValueMap = new HashMap();
        bibAssociatedFieldsValueMap.put("title_search", "mockTitle");
        bibAssociatedFieldsValueMap.put("author_display", "mockAuthor");
        oleBibRecord.setBibAssociatedFieldsValueMap(bibAssociatedFieldsValueMap);
        oleEditorResponse.setOleBibRecord(oleBibRecord);
        oleEditorResponse.setTokenId("1234");

        editorResponseXMLForOLE = new OleEditorResponseHandler().toXML(oleEditorResponse);

        try {
            oleExposedWebService.addDoctoreResponse(editorResponseXMLForOLE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        System.out.println("successfully sent message to OLE with the Editor response");

    }

    public String getURL() {
        String url = ConfigContext.getCurrentContextConfig().getProperty("oleExposedWebService.url");
        return url;
    }
}
