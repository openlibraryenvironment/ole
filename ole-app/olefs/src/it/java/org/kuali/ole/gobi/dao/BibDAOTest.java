package org.kuali.ole.gobi.dao;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.sys.context.SpringContext;
import org.marc4j.MarcReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.impl.DataFieldImpl;
import org.marc4j.marc.impl.SubfieldImpl;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by pvsubrah on 10/14/15.
 */
public class BibDAOTest extends OLETestCaseBase {

    protected BibDAO bibDAO;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        bibDAO = (BibDAO) SpringContext.getService("bibDAO");
    }


    @Test
    public void crudOperationsBib() throws Exception {

        String bibContent = bibDAO.getBibById("1");

        assertNotNull(bibContent);

        System.out.println(bibContent);

        MarcReader reader = new MarcXmlReader(IOUtils.toInputStream(bibContent));
        Record record = null;
        while (reader.hasNext()) {
            record = reader.next();
            DataField dataField = (DataField) record.getVariableField("980");
            if (null != dataField) {
                record.getDataFields().remove(dataField);
            }
            dataField = new DataFieldImpl();
            dataField.setTag("980");
            dataField.setIndicator1(' ');
            dataField.setIndicator2(' ');

            Subfield sf = new SubfieldImpl();
            sf.setCode('a');
            sf.setData("YBPOrderKey");

            dataField.addSubfield(sf);

            record.getDataFields().add(dataField);
        }

        assertNotNull(record.getVariableField("980"));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MarcWriter marcWriter = new MarcXmlWriter(byteArrayOutputStream);
        marcWriter.write(record);
        marcWriter.close();
        String updatedBibContent = byteArrayOutputStream.toString();
        System.out.println(updatedBibContent);

        int result = bibDAO.saveOrUpdate("1", updatedBibContent);
        assertEquals(result, 1);


        String savedBib = bibDAO.getBibById("1");

        MarcReader reader1 = new MarcXmlReader(IOUtils.toInputStream(savedBib));
        Record record1 = null;
        while (reader1.hasNext()) {
            record1 = reader1.next();
            DataField dataField = (DataField) record1.getVariableField("980");
            assertNotNull(dataField);
        }
    }

}
