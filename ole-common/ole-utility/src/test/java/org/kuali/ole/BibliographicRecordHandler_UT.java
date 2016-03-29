package org.kuali.ole;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.pojo.bib.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.*;

import static junit.framework.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/2/12
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibliographicRecordHandler_UT {
    public static final Logger LOG = LoggerFactory.getLogger(BibliographicRecordHandler_UT.class);

    @Test
    public void testExtractBibliographicRecordsFromXML() throws Exception {
        BibliographicRecordHandler bibliographicRecordHandler =
                new BibliographicRecordHandler();

        URL resource = getClass().getResource("input-marc.xml");
        String xmlContent = new FileUtil().readFile(new File(resource.toURI()));

        Collection bibliographicRecordCollection = bibliographicRecordHandler.fromXML(xmlContent);
        assertNotNull(bibliographicRecordCollection);
        List<BibliographicRecord> records = bibliographicRecordCollection.getRecords();
        assertTrue(!records.isEmpty());

    }

    @Test
    public void testExtractBibliographicRecordsFromSpeicalCharsXML() throws Exception {
        BibliographicRecordHandler bibliographicRecordHandler =
                new BibliographicRecordHandler();

        URL resource = getClass().getResource("input-marc-special-chars.xml");
        String xmlContent = new FileUtil().readFile(new File(resource.toURI()));

        Collection bibliographicRecordCollection = bibliographicRecordHandler.fromXML(xmlContent);
        assertNotNull(bibliographicRecordCollection);
        assertTrue(!bibliographicRecordCollection.getRecords().isEmpty());

    }


    @Ignore
    @Test
    public void testExtractBibliographicRecordsForFailedXML() throws Exception {
        //TODO: not sure what happened to the xml; will need to re-validate this test.
        BibliographicRecordHandler bibliographicRecordHandler =
                new BibliographicRecordHandler();

        URL resource = getClass().getResource("failed-marc.xml");
        String xmlContent = new FileUtil().readFile(new File(resource.toURI()));

        Collection bibliographicRecordCollection = bibliographicRecordHandler.fromXML(xmlContent);
        assertNotNull(bibliographicRecordCollection);
        assertTrue(!bibliographicRecordCollection.getRecords().isEmpty());

    }

    @Test
    public void testGenerateXML() throws Exception {

        BibliographicRecordHandler bibliographicRecordHandler =
                new BibliographicRecordHandler();
        BibliographicRecord bibliographicRecord = generateMockBib();
        String xml = bibliographicRecordHandler.generateXML(bibliographicRecord);
        LOG.info(xml);


    }

    private BibliographicRecord generateMockBib() {
        BibliographicRecord bibliographicRecord = new BibliographicRecord();
        bibliographicRecord.setLeader("MOCK_LEADER");

        ControlField marcControlField = new ControlField();
        marcControlField.setTag("001");
        marcControlField.setValue("1223");
        ControlField marcControlField1 = new ControlField();
        marcControlField1.setTag("008");
        marcControlField1.setValue("12323424");
        bibliographicRecord.setControlfields(Arrays.asList(marcControlField, marcControlField1));

        DataField marcDataField = new DataField();
        marcDataField.setTag("020");
        marcDataField.setInd1("");
        marcDataField.setInd1("");
        SubField marcSubField = new SubField();
        marcSubField.setCode("a");
        marcSubField.setValue("CAMBRIDGE INTRODUCTION TO C++ Books & READ**.");
        SubField marcSubField1 = new SubField();
        marcSubField1.setCode("c");
        marcSubField1.setValue("19.99");
        marcDataField.setSubFields(Arrays.asList(marcSubField, marcSubField1));

        DataField marcDataField1 = new DataField();
        marcDataField1.setTag("852");
        marcDataField1.setInd1("");
        marcDataField1.setInd1("");
        SubField marcSubField2 = new SubField();
        marcSubField2.setCode("a");
        marcSubField2.setValue("123");
        SubField marcSubField3 = new SubField();
        marcSubField3.setCode("b");
        marcSubField3.setValue("42657");
        marcDataField1.setSubFields(Arrays.asList(marcSubField2, marcSubField3));
        bibliographicRecord.setDatafields(Arrays.asList(marcDataField, marcDataField1));
        return bibliographicRecord;
    }

}
