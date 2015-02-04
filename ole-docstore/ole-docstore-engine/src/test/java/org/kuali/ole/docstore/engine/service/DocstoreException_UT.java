package org.kuali.ole.docstore.engine.service;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.client.DocstoreRestClient;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Jaya Bharath reddy
 * Date: 01/23/14
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreException_UT extends BaseTestCase {
    private DocstoreRestClient restClient = new DocstoreRestClient();

    private static final Logger LOG = LoggerFactory.getLogger(DocstoreException_UT.class);

    @Ignore
    @Test
    public void testCreateBibException() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        bibMarc.setId(null);
        bibMarc.setContent(null);
        try {
            restClient.createBib(bibMarc);
        } catch (DocstoreException e) {
            throw e;
        }

    }
}
