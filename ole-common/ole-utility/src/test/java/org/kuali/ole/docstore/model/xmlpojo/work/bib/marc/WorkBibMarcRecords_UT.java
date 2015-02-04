package org.kuali.ole.docstore.model.xmlpojo.work.bib.marc;

import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/21/13
 * Time: 12:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkBibMarcRecords_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(WorkBibMarcRecords_UT.class);

    @Test
    public void testWorkBibMarcRecords() throws Exception {

        WorkBibMarcRecords workBibMarcRecords = new WorkBibMarcRecords();
        List<WorkBibMarcRecord> workBibMarcRecordList = new ArrayList<WorkBibMarcRecord>();
        List<ControlField> controlFieldList = new ArrayList<ControlField>();
        List<DataField> dataFieldList = new ArrayList<DataField>();
        WorkBibMarcRecord workBibMarcRecord = new WorkBibMarcRecord();
        workBibMarcRecord.setLeader("test");
        ControlField marcControlField = new ControlField();
        marcControlField.setTag("001");
        marcControlField.setValue("1223");
        ControlField marcControlField1 = new ControlField("ts");
        marcControlField1.setTag("001");
        marcControlField1.setValue("1223");

        if (marcControlField.equals(marcControlField1)) {
            LOG.info("ControlFields are equal");
        }
        DataField marcDataField1 = new DataField("aa");
        marcDataField1.setTag("500");
        marcDataField1.setInd1("");
        marcDataField1.setInd2("");
        SubField marcSubField1 = new SubField("bb");
        marcSubField1.setCode("a");
        marcSubField1.setValue("688");
        SubField marcSubField2 = new SubField();
        marcSubField2.setCode("b");
        marcSubField2.setValue("7225");
        marcDataField1.addAllSubFields(Arrays.asList(marcSubField1, marcSubField2));
        marcDataField1.setSubFields(Arrays.asList(marcSubField1, marcSubField2));

        controlFieldList.add(marcControlField);
        dataFieldList.add(marcDataField1);
        workBibMarcRecord.setControlFields(controlFieldList);
        workBibMarcRecord.setDataFields(dataFieldList);
        workBibMarcRecordList.add(workBibMarcRecord);

        workBibMarcRecord = new WorkBibMarcRecord();
        workBibMarcRecord.setLeader("test");
        ControlField marcControlField2 = new ControlField();
        marcControlField2.setTag("005");
        marcControlField2.setValue("1665");
        ControlField marcControlField3 = new ControlField("ts");
        marcControlField3.setTag("007");
        marcControlField3.setValue("447774");
        DataField marcDataField2 = new DataField("aa");
        marcDataField2.setTag("500");
        marcDataField2.setInd1("");
        marcDataField2.setInd2("");
        SubField marcSubField3 = new SubField("bb");
        marcSubField3.setCode("a");
        marcSubField3.setValue("688");
        SubField marcSubField4 = new SubField();
        marcSubField4.setCode("b");
        marcSubField4.setValue("7225");
        workBibMarcRecord.getControlFields().add(marcControlField3);
        marcDataField2.setSubFields(Arrays.asList(marcSubField3, marcSubField4));
        workBibMarcRecord.addMarcDataField(marcDataField2);
        workBibMarcRecord.getDataFields().add(marcDataField2);
        workBibMarcRecordList.add(workBibMarcRecord);
        workBibMarcRecords.setRecords(workBibMarcRecordList);

        if (marcDataField1.equals(marcDataField2)) {
            LOG.info("equals");
        }
        List<WorkBibMarcRecord> workBibMarcRecordResultList = workBibMarcRecords.getRecords();
        if (workBibMarcRecord.getDataFieldForTag("78231") != null) {
            LOG.info("Datafield exists");
        }
        for (WorkBibMarcRecord workBibMarcRecordResult : workBibMarcRecordResultList) {
            LOG.info("Leader:" + workBibMarcRecordResult.getLeader());
            List<ControlField> controlFieldList1 = workBibMarcRecordResult.getControlFields();
            for (ControlField controlField : controlFieldList1) {
                LOG.info("Control Field Tag:" + controlField.getTag());
                LOG.info("Control Field Value:" + controlField.getValue());
            }
            List<DataField> dataFieldList1 = workBibMarcRecordResult.getDataFields();
            LOG.info("size:" + dataFieldList1.size());
            for (DataField dataField : dataFieldList1) {
                LOG.info("Data Field Ind1:" + dataField.getInd1());
                LOG.info("Data Field Ind2:" + dataField.getInd2());
                List<SubField> subFieldList = dataField.getSubFields();
                for (SubField subField : subFieldList) {
                    LOG.info("Sub Field Code:" + subField.getCode());
                    LOG.info("Sub Field Value:" + subField.getValue());
                }
            }

        }

        LOG.info("Data Field for Tag");
        DataField dataField = workBibMarcRecord.getDataFieldForTag("500");
        LOG.info("Data Field Ind1:" + dataField.getInd1());
        LOG.info("Data Field Ind2:" + dataField.getInd2());
        LOG.info(String.valueOf(marcControlField.hashCode()));
        LOG.info(String.valueOf(marcDataField1.hashCode()));
        LOG.info(String.valueOf(marcSubField1.hashCode()));
        workBibMarcRecord.setControlFields(null);
        workBibMarcRecord.setDataFields(null);
        assertNotNull(workBibMarcRecord.getControlFields());
        assertNotNull(workBibMarcRecord.getDataFields());

    }

}
