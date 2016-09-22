package org.kuali.ole.dsng.rest.handler.bib;

import org.junit.Test;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 3/21/2016.
 */
public class BibStatusOverlayOpsHandlerTest {

    @Test
    public void testIsValid() throws Exception {
        BibRecord bibRecord = new BibRecord();
        bibRecord.setStatus(null);
        BibStatusOverlayOpsHandler bibStatusOverlayOpsHandler = new BibStatusOverlayOpsHandler();

        //Use case 1
        boolean isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.EQUAL_TO, Collections.singletonList("status1"), bibRecord);
        assertFalse(isValid);

        //Use Case2
        bibRecord.setStatus("");
        isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.EQUAL_TO, Collections.singletonList("status1"), bibRecord);
        assertFalse(isValid);

        //Use Case3
        bibRecord.setStatus("status1");
        isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.EQUAL_TO, Collections.singletonList("status1"), bibRecord);
        assertTrue(isValid);

        //Use case 4
        bibRecord.setStatus("status1");
        isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.EQUAL_TO, Collections.singletonList((String)null), bibRecord);
        assertFalse(isValid);

        //Use Case5
        bibRecord.setStatus("status1");
        isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.EQUAL_TO, Collections.singletonList("status1"), bibRecord);
        assertTrue(isValid);

        //Use Case6
        bibRecord.setStatus("status1");
        isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.EQUAL_TO, Collections.singletonList("status1"), bibRecord);
        assertTrue(isValid);

        //Use case 7
        bibRecord.setStatus(null);
        isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.NOT_EQUAL_TO, Collections.singletonList("status1"), bibRecord);
        assertTrue(isValid);

        //Use Case8
        bibRecord.setStatus("");
        isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.NOT_EQUAL_TO, Collections.singletonList("status1"), bibRecord);
        assertTrue(isValid);

        //Use Case9
        bibRecord.setStatus("status1");
        isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.NOT_EQUAL_TO, Collections.singletonList("status1"), bibRecord);
        assertFalse(isValid);

        //Use case10
        bibRecord.setStatus("status1");
        isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.NOT_EQUAL_TO, Collections.singletonList((String)null), bibRecord);
        assertTrue(isValid);

        //Use Case11
        bibRecord.setStatus("status1");
        isValid = bibStatusOverlayOpsHandler.isValid(OleNGConstants.NOT_EQUAL_TO, Collections.singletonList(""), bibRecord);
        assertTrue(isValid);

    }
}