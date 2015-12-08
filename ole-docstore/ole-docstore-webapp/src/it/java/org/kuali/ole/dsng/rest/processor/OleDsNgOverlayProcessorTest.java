package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.ole.DocstoreTestCaseBase;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsNgOverlayProcessorTest implements DocstoreConstants {

    @Autowired
    OleDsNgRestAPIProcessor oleDsNgRestAPIProcessor;

    @Test
    public void testProcessOverlayForBib() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(LOCALID_DISPLAY,"10000034");

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        String savedJSON = oleDsNgRestAPIProcessor.processOverlayForBib(jsonArray.toString());
        assertTrue(StringUtils.isNotBlank(savedJSON));
    }
}