package org.kuali.ole;

import org.junit.Test;
import org.kuali.ole.pojo.ProfileAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/10/12
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleHoldingsRecordHandler_UT {
    public static final Logger LOG = LoggerFactory.getLogger(OleHoldingsRecordHandler_UT.class);

    @Test
    public void testGenerateInstanceXML() throws Exception {
        OleHoldingsRecordHandler oleHoldingsRecordHandler = new OleHoldingsRecordHandler();
        ProfileAttribute attr1 = new ProfileAttribute();
        attr1.setAgendaName("mockAgenda");
        attr1.setAttributeName("encodingLevel");
        attr1.setAttributeValue("1");
        ProfileAttribute attr2 = new ProfileAttribute();
        //
        attr2.setSystemValue(null);
        if (attr2.getSystemValue() != null) {
            LOG.info(attr2.getSystemValue());
        }
        attr2.setId(null);
        if (attr2.getId() != null) {
            LOG.info(attr2.getId().toString());
        }
        attr2.setAgendaName("mockAgenda");
        if (attr2.getAgendaName() != null) {
            LOG.info(attr2.getAgendaName());
        }
        attr2.setAttributeName("recordType");
        attr2.setAttributeValue("u");
        if (attr2.getAttributeValue() != null) {
            LOG.info(attr2.getAttributeValue());
        }

        String xml = oleHoldingsRecordHandler.generateXML(Arrays.asList(attr1, attr2));
        assertNotNull(xml);
        LOG.info(xml);


    }

}
