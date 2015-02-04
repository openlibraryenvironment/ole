package org.kuali.ole.docstore.coverage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.kuali.ole.FileUtilForRawMarc;
import org.kuali.ole.MarcXMLGenerator;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.model.bo.*;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.config.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.*;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.OleHoldings;
import org.kuali.ole.docstore.model.xstream.ingest.IngestDocumentHandler;
import org.kuali.ole.docstore.utility.*;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.utility.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.Property;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: vk8458
 * Date: 11/30/12
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CodeCoverage_UT
        extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(CodeCoverage_UT.class);

    @Test
    public void testCoverage() throws Exception {
        LOG.info("testing");
    }

    // org.kuali.ole.docstore.model.xmlpojo.work.bib.marc    - WorkBibMarcRecords_UT


    //  org.kuali.ole.docstore.model.bo       - OleDocument_UT
    //  org.kuali.ole.docstore.model.enums    - OleDocument_UT


    //org.kuali.ole.docstore.utility  - BulkIngestStatistics_UT


    //org.kuali.ole.docstore.utility  - DocStoreSettingsUtil_UT


    //org.kuali.ole.docstore.utility    - XMLUtility_UT


    // org.kuali.ole.converter
    // MarcXMLConverter_UT


    // org.kuali.ole.converter
    // MarcXMLConverter_UT


}
