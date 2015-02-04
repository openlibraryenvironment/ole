package org.kuali.ole;

import org.junit.Test;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleEditorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/11/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleEditorResponseHandler_UT {
    public static final Logger LOG = LoggerFactory.getLogger(OleEditorResponseHandler_UT.class);
    private String xmlContent = "";

    //TODO
    @Test
    public void testToXML() throws Exception {
        OleEditorResponseHandler handler = new OleEditorResponseHandler();
        OleEditorResponse response = new OleEditorResponse();
        response.setTokenId("3000_0");
        OleBibRecord record = new OleBibRecord();
        record.setBibUUID("2345-6789");
        record.setLinkedInstanceId("123-34567");
        Map bibAssociatedFieldsValueMap = new HashMap();
        bibAssociatedFieldsValueMap.put("author", "mock_author");
        bibAssociatedFieldsValueMap.put("title", "mock_title");
        record.setBibAssociatedFieldsValueMap(bibAssociatedFieldsValueMap);
        response.setOleBibRecord(record);
        LOG.info(handler.toXML(response));
        //
        if (record.getBibAssociatedFieldsValueMap() != null) {
            LOG.info("BIbValueMap is not empty");
        }
        if (response.getTokenId() != null) {
            LOG.info(response.getTokenId());
        }
    }


    //TODO
    @Test
    public void testFrom() throws Exception {
        OleEditorResponseHandler oleEditorResponseHandler =
                new OleEditorResponseHandler();

        URL resource = getClass().getResource("OleEditorResponse.xml");
        String xmlContent = new FileUtil().readFile(new File(resource.toURI()));

        OleEditorResponse oleEditorResponse = oleEditorResponseHandler.fromXML(xmlContent);
        assertNotNull(oleEditorResponse);
        assertEquals(oleEditorResponse.getOleBibRecord().getBibUUID(), "2345-6789");
        assertEquals(oleEditorResponse.getOleBibRecord().getLinkedInstanceId(), "123-34567");
    }
}
