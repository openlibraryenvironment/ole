package org.kuali.ole.pojo.bib;

import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;

import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ?
 * Date: 12/15/12
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibliographicNamedFieldsBean_UT {

    @Test
    public void testBibliographicNamedFieldsBean() {
        BibliographicNamedFieldsBean bibliographicNamedFieldsBean = new BibliographicNamedFieldsBean();
        bibliographicNamedFieldsBean.setAuthor("Sandburg");
        bibliographicNamedFieldsBean.setTitle("Arithmetic");
        bibliographicNamedFieldsBean.setDescription("description");
        bibliographicNamedFieldsBean.setPublisher("sa publication");

        String author = bibliographicNamedFieldsBean.getAuthor();
        assertNotNull(author);
        String title = bibliographicNamedFieldsBean.getTitle();
        assertNotNull(title);
        String description = bibliographicNamedFieldsBean.getDescription();
        assertNotNull(description);
        String publisher = bibliographicNamedFieldsBean.getPublisher();
        assertNotNull(publisher);


    }

    @Test
    public void testBibliographicNamedFieldsBean1() {
        BibliographicNamedFieldsBean bibliographicNamedFieldsBean = new BibliographicNamedFieldsBean();

        bibliographicNamedFieldsBean.setBibliographicRecord(generateMockBib());
        assertNotNull(bibliographicNamedFieldsBean);
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
