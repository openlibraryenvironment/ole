package org.kuali.ole.docstore.process;

import org.junit.Test;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/4/12
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkingInstanceNBibHandler_AT {

    private static final Logger LOG = LoggerFactory.getLogger(LinkingInstanceNBibHandler_AT.class);
    @Test
    public void testRebuildIndexesForMarc() {
        try {
            LinkingInstanceNBibHandler link = LinkingInstanceNBibHandler
                    .getInstance(DocCategory.WORK.getCode(), DocType.INSTANCE.getDescription(),
                            DocFormat.OLEML.getCode());
            link.run();
        } catch (Exception e) {
            LOG.info("Exception :", e);
            fail();
        }

    }
}
