package org.kuali.ole;

import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.Item;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.OleHoldings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/10/12
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleInstanceRecordHandler_UT {

    public static final Logger LOG = LoggerFactory.getLogger(OleInstanceRecordHandler_UT.class);

    @Test
    public void testGenerateInstanceXML() throws Exception {
        OleInstanceRecordHandler oleInstanceRecordHandler = new OleInstanceRecordHandler();
        String xml = oleInstanceRecordHandler.generateXML(new OleHoldings(), new Item());
        assertNotNull(xml);
        LOG.info(xml);
    }

}
