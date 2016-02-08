package org.kuali.ole.docstore.model.xstream.work.bib.marc;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;

import java.io.File;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 12/4/2015.
 */
public class MRCOrMRKToBibMarcRecordConverterTest {

    /*
    * This test is reading small file.*/
    @Test
    public void testConvert() throws Exception {
        File file = new File(getClass().getResource("slabach-test2.mrk").toURI());
        assertNotNull(file);
        String content = FileUtils.readFileToString(file);
        BibMarcRecords bibMarcRecords =  new MRCOrMRKToBibMarcRecordConverter().convert(content);
        assertNotNull(bibMarcRecords);
        assertTrue(CollectionUtils.isNotEmpty(bibMarcRecords.getRecords()));
        System.out.println("Total Record Size : " + bibMarcRecords.getRecords().size());
        for (Iterator<BibMarcRecord> iterator = bibMarcRecords.getRecords().iterator(); iterator.hasNext(); ) {
            BibMarcRecord bibMarcRecord = iterator.next();
            System.out.println("Leader Field : " + bibMarcRecord.getLeader());
            System.out.println("Control Fields : ");
            for (Iterator<ControlField> iterator1 = bibMarcRecord.getControlFields().iterator(); iterator1.hasNext(); ) {
                ControlField controlField = iterator1.next();
                System.out.println("\tTag : " + controlField.getTag() + "   Value : " + controlField.getValue());
            }
            System.out.println("Data Fields : ");
            for (Iterator<DataField> dataFieldIterator = bibMarcRecord.getDataFields().iterator(); dataFieldIterator.hasNext(); ) {
                DataField dataField = dataFieldIterator.next();
                System.out.println("\tTag : " + dataField.getTag() + "   Ind1 : " + dataField.getInd1() + "   Ind2 : " + dataField.getInd2());
                System.out.println("\t\tSub Fields : ");
                for (Iterator<SubField> subFieldIterator = dataField.getSubFields().iterator(); subFieldIterator.hasNext(); ) {
                    SubField subField = subFieldIterator.next();
                    System.out.println("\t\t\tCode : " + subField.getCode() + "   Value : " + subField.getValue());
                }
            }
            System.out.println("*****************************************************************");
        }
    }

    /*
    * This test is reading big file.*/
    @Test
    public void testConvert2() throws Exception {
        File file = new File(getClass().getResource("52400701a.mrk").toURI());
        assertNotNull(file);
        String content = FileUtils.readFileToString(file);
        BibMarcRecords bibMarcRecords =  new MRCOrMRKToBibMarcRecordConverter().convert(content);
        assertNotNull(bibMarcRecords);
        assertTrue(CollectionUtils.isNotEmpty(bibMarcRecords.getRecords()));
        System.out.println("Total Record Size : " + bibMarcRecords.getRecords().size());
        for (Iterator<BibMarcRecord> iterator = bibMarcRecords.getRecords().iterator(); iterator.hasNext(); ) {
            BibMarcRecord bibMarcRecord = iterator.next();
            System.out.println("Leader Field : " + bibMarcRecord.getLeader());
            System.out.println("Control Fields : ");
            for (Iterator<ControlField> iterator1 = bibMarcRecord.getControlFields().iterator(); iterator1.hasNext(); ) {
                ControlField controlField = iterator1.next();
                System.out.println("\tTag : " + controlField.getTag() + "   Value : " + controlField.getValue());
            }
            System.out.println("Data Fields : ");
            for (Iterator<DataField> dataFieldIterator = bibMarcRecord.getDataFields().iterator(); dataFieldIterator.hasNext(); ) {
                DataField dataField = dataFieldIterator.next();
                System.out.println("\tTag : " + dataField.getTag() + "   Ind1 : " + dataField.getInd1() + "   Ind2 : " + dataField.getInd2());
                System.out.println("\t\tSub Fields : ");
                for (Iterator<SubField> subFieldIterator = dataField.getSubFields().iterator(); subFieldIterator.hasNext(); ) {
                    SubField subField = subFieldIterator.next();
                    System.out.println("\t\t\tCode : " + subField.getCode() + "   Value : " + subField.getValue());
                }
            }
            System.out.println("*****************************************************************");
        }
    }
}