package org.kuali.ole.ingest;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * User: peris
 * Date: 12/26/12
 * Time: 11:31 AM
 */
public class OverlayIngestProfileBuilder_UT {
    @Test
    public void buildOverlayProfile() throws Exception {
        OverlayIngestProfileBuilder overlayIngestProfileBuilder = new OverlayIngestProfileBuilder();
        assertNotNull(overlayIngestProfileBuilder);
        overlayIngestProfileBuilder.createOverlayProfile("overlay-ingest-profile.xml");
    }
}
