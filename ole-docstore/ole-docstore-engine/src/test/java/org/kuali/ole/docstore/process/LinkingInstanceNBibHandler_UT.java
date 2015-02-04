package org.kuali.ole.docstore.process;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.service.DocumentIngester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Created with IntelliJ IDEA.
 * User: SS10304
 * Date: 1/3/13
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkingInstanceNBibHandler_UT extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(LinkingInstanceNBibHandler_UT.class);

    @Test
    public void testLinkingInstanceNBibHandler() throws Exception {
        LinkingInstanceNBibHandler linkingInstanceNBibHandler = LinkingInstanceNBibHandler.getInstance(DocCategory.WORK.getCode(), DocType.BIB.getCode(), DocFormat.MARC.getCode());
        LOG.info("IsRunning :" + linkingInstanceNBibHandler.isRunning());
        linkingInstanceNBibHandler.startProcess();
        linkingInstanceNBibHandler.run();
        LOG.info("IsRunning :" + linkingInstanceNBibHandler.isRunning());
    }
}
