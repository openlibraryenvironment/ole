package org.kuali.ole.batch;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileDataMappingOptionsBo;
import org.kuali.ole.batch.ingest.BatchProcessBibImport;
import org.kuali.ole.batch.marc.OLEMarcReader;
import org.kuali.ole.batch.marc.OLEMarcXmlReader;
import org.kuali.ole.batch.bo.xstream.OLEBatchProcessProfileRecordProcessor;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.marc4j.marc.Record;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;

import java.io.*;


import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 1/27/14
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchProcessImport_UT {
    private OLEBatchProcessProfileRecordProcessor oleBatchProcessProfileRecordProcessor;

    public OLEBatchProcessProfileRecordProcessor getOLEBatchProcessProfileRecordProcessor() {
        if (null == oleBatchProcessProfileRecordProcessor) {
            oleBatchProcessProfileRecordProcessor = new OLEBatchProcessProfileRecordProcessor();
        }
        return oleBatchProcessProfileRecordProcessor;
    }

    @Ignore
    @Test
    public void createBibMarc() throws Exception {

        BibMarcRecords bibMarcRecords = new BibMarcRecords();
        for(int i=0;i<5;i++){
        BibMarcRecord bibMarcRecord= new BibMarcRecord();
        ControlField controlField = new ControlField();
        controlField.setTag("008");
        controlField.setValue("testdfsdfsdf");
        bibMarcRecord.addControlFields(controlField);
        DataField dataField = new DataField();
        dataField.setInd1(" ");
        dataField.setInd2(" ");
        dataField.setTag("245");
        SubField subField = new SubField();
        subField.setCode("a");
        subField.setValue("test");
        List<SubField> subFields = new ArrayList<>();
        subFields.add(subField);
        dataField.setSubFields(subFields);
        bibMarcRecord.setLeader("aaaaaaaaaaaaaaaaaaaaaaa");
        bibMarcRecord.addDataFields(dataField);
        bibMarcRecords.getRecords().add(bibMarcRecord);
        }



        BatchProcessBibImport batchProcessBibImport = new BatchProcessBibImport();
       // batchProcessBibImport.processBatch(bibMarcRecords.getRecords());

    }



    @Test
    public void testSort() {
        List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBos = new ArrayList<>();

        OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo = new OLEBatchProcessProfileDataMappingOptionsBo();

        oleBatchProcessProfileDataMappingOptionsBo.setDataTypeDestinationField("holdings");
        oleBatchProcessProfileDataMappingOptionsBo.setDestinationField("callNumber");
        oleBatchProcessProfileDataMappingOptionsBo.setPriority(1);

        OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo1 = new OLEBatchProcessProfileDataMappingOptionsBo();

        oleBatchProcessProfileDataMappingOptionsBo1.setDataTypeDestinationField("item");
        oleBatchProcessProfileDataMappingOptionsBo1.setDestinationField("callNumber");
        oleBatchProcessProfileDataMappingOptionsBo1.setPriority(1);

        OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo2 = new OLEBatchProcessProfileDataMappingOptionsBo();

        oleBatchProcessProfileDataMappingOptionsBo2.setDataTypeDestinationField("holdings");
        oleBatchProcessProfileDataMappingOptionsBo2.setDestinationField("callNumber");
        oleBatchProcessProfileDataMappingOptionsBo2.setPriority(4);

        OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo3 = new OLEBatchProcessProfileDataMappingOptionsBo();

        oleBatchProcessProfileDataMappingOptionsBo3.setDataTypeDestinationField("holdings");
        oleBatchProcessProfileDataMappingOptionsBo3.setDestinationField("callNumber");
        oleBatchProcessProfileDataMappingOptionsBo3.setPriority(3);

        OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo4 = new OLEBatchProcessProfileDataMappingOptionsBo();

        oleBatchProcessProfileDataMappingOptionsBo4.setDataTypeDestinationField("item");
        oleBatchProcessProfileDataMappingOptionsBo4.setDestinationField("callNumber");
        oleBatchProcessProfileDataMappingOptionsBo4.setPriority(2);

        OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo5 = new OLEBatchProcessProfileDataMappingOptionsBo();

        oleBatchProcessProfileDataMappingOptionsBo5.setDataTypeDestinationField("eholdings");
        oleBatchProcessProfileDataMappingOptionsBo5.setDestinationField("callNumber");
        oleBatchProcessProfileDataMappingOptionsBo5.setPriority(3);

        OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo6 = new OLEBatchProcessProfileDataMappingOptionsBo();

        oleBatchProcessProfileDataMappingOptionsBo6.setDataTypeDestinationField("eholdings");
        oleBatchProcessProfileDataMappingOptionsBo6.setDestinationField("callNumber");
        oleBatchProcessProfileDataMappingOptionsBo6.setPriority(2);


        oleBatchProcessProfileDataMappingOptionsBos.add(oleBatchProcessProfileDataMappingOptionsBo);
        oleBatchProcessProfileDataMappingOptionsBos.add(oleBatchProcessProfileDataMappingOptionsBo1);
        oleBatchProcessProfileDataMappingOptionsBos.add(oleBatchProcessProfileDataMappingOptionsBo2);
        oleBatchProcessProfileDataMappingOptionsBos.add(oleBatchProcessProfileDataMappingOptionsBo3);
        oleBatchProcessProfileDataMappingOptionsBos.add(oleBatchProcessProfileDataMappingOptionsBo4);
        oleBatchProcessProfileDataMappingOptionsBos.add(oleBatchProcessProfileDataMappingOptionsBo5);
        oleBatchProcessProfileDataMappingOptionsBos.add(oleBatchProcessProfileDataMappingOptionsBo6);

        System.out.println(oleBatchProcessProfileDataMappingOptionsBos+"\n\n");
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(0).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(0).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(1).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(1).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(2).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(2).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(3).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(3).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(4).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(4).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(5).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(5).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(6).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(6).getDataTypeDestinationField());


        java.util.Collections.sort(oleBatchProcessProfileDataMappingOptionsBos);

        System.out.println(oleBatchProcessProfileDataMappingOptionsBos+"\n\n");
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(0).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(0).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(1).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(1).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(2).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(2).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(3).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(3).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(4).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(4).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(5).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(5).getDataTypeDestinationField());
        System.out.println(oleBatchProcessProfileDataMappingOptionsBos.get(6).getPriority() + " " +oleBatchProcessProfileDataMappingOptionsBos.get(6).getDataTypeDestinationField());



    }

    @Test
    public void generateFileForBibImport1() {
        String filePath = System.getProperty("user.home");
        String fileName = "10Marc";
        boolean writeMarc = Boolean.TRUE;
        boolean writeMarcXml = Boolean.TRUE;
        int numOfRecordsInFile = 10;

        generateFile(filePath, fileName, writeMarc, writeMarcXml, numOfRecordsInFile);
    }

    @Test
    public void generateFileForBibImport2() {
        String filePath = System.getProperty("user.home");
        String fileName = "100Marc";
        boolean writeMarc = Boolean.TRUE;
        boolean writeMarcXml = Boolean.TRUE;
        int numOfRecordsInFile = 100;

        generateFile(filePath, fileName, writeMarc, writeMarcXml, numOfRecordsInFile);
    }

    @Test
    public void generateFileForBibImport3() {
        String filePath = System.getProperty("user.home");
        String fileName = "10KMarc";
        boolean writeMarc = Boolean.TRUE;
        boolean writeMarcXml = Boolean.TRUE;
        int numOfRecordsInFile = 10000;

        generateFile(filePath, fileName, writeMarc, writeMarcXml, numOfRecordsInFile);
    }

    @Test
    public void generateFileForBibImport4() {
        String filePath = System.getProperty("user.home");
        String fileName = "100KMarc";
        boolean writeMarc = Boolean.TRUE;
        boolean writeMarcXml = Boolean.TRUE;
        int numOfRecordsInFile = 100000;

        generateFile(filePath, fileName, writeMarc, writeMarcXml, numOfRecordsInFile);
    }

    public void generateFile(String filePath, String fileName, boolean writeMarc, boolean writeMarcXml, int numOfRecordsInFile) {
        BibTree bibTree = getBibTreeForBibImport();
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        List<BibMarcRecord> bibRecords = new ArrayList<BibMarcRecord>();
        List<String> bibMarcRecordList = new ArrayList<String>();
        for (int i = 1; i <= numOfRecordsInFile; i++) {
            Bib bib = bibTree.getBib();
            BibMarcRecord bibMarcRecord = getBibMarcRecord(bib.getContent(), bibMarcRecordProcessor);
            List<DataField> dataFields = bibMarcRecord.getDataFields();
            bibMarcRecord.getControlFields().get(0).setValue("1000" + i);
            bibMarcRecord.getDataFields().get(0).getSubFields().get(0).setValue("Test Record" + i);
            DataField dataField1 = new DataField();
            SubField subField1 = new SubField();
            subField1.setCode("a");
            subField1.setValue("PQ 00" + i);
            dataField1.setTag("949");
            dataField1.getSubFields().add(subField1);
            DataField dataField2 = new DataField();
            SubField subField2 = new SubField();
            subField2.setCode("i");
            subField2.setValue(String.valueOf(i));
            dataField2.setTag("949");
            dataField2.getSubFields().add(subField2);
            dataFields.add(dataField1);
            dataFields.add(dataField2);
            bibRecords.add(bibMarcRecord);
        }
        bibMarcRecordList.add(bibMarcRecordProcessor.generateXML(bibRecords));
        if (writeMarc && writeMarcXml) {
            generateMarcXml(fileName, filePath, bibMarcRecordList);
            generateMarcFromXml(fileName, filePath, bibMarcRecordList);
        } else if (writeMarc && !writeMarcXml) {
            generateMarcFromXml(fileName, filePath, bibMarcRecordList);
        } else if (!writeMarc && writeMarcXml) {
            generateMarcXml(fileName, filePath, bibMarcRecordList);
        }
    }

    public BibTree getBibTreeForBibImport() {
        BibTree bibTree = new BibTree();
        return (BibTree) bibTree.deserialize(getXmlAsString("/org/kuali/ole/batch/bibTreeDocument/ImportBibTree.xml"));
    }

    private BibMarcRecord getBibMarcRecord(String content, BibMarcRecordProcessor bibMarcRecordProcessor) {
        BibMarcRecord bibMarcRecord = null;
        BibMarcRecords marcRecords = bibMarcRecordProcessor.fromXML(content);
        List<BibMarcRecord> bibMarcRecordList = marcRecords.getRecords();
        Iterator<BibMarcRecord> bibMarcRecordListIterator = bibMarcRecordList.iterator();
        if (bibMarcRecordListIterator.hasNext()) {
            bibMarcRecord = bibMarcRecordListIterator.next();
        }
        return bibMarcRecord;
    }

    public void generateMarcXml(String fileName, String filePath, List<String> bibMarcRecordList) {
        File file = new File(filePath + FileSystems.getDefault().getSeparator() + fileName + ".xml");
        try {
            FileUtils.writeLines(file, "UTF-8", bibMarcRecordList, true);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void generateMarcFromXml(String fileName, String filePath, List<String> bibMarcRecordList) {
        StopWatch timer = new StopWatch();
        timer.start();
        File fileToWrite = new File(filePath + FileSystems.getDefault().getSeparator() + fileName + ".mrc");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileToWrite, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        MarcWriter writer = new MarcStreamWriter(fileOutputStream, "UTF-8");
        for (String bibContent : bibMarcRecordList) {
            InputStream input = new ByteArrayInputStream(bibContent.getBytes());
            Record record = null;
            try {
                OLEMarcReader marcXmlReader = new OLEMarcXmlReader(input);
                while (marcXmlReader.hasNext()) {
                    if (marcXmlReader.hasErrors()) {
                        marcXmlReader.next();
                        marcXmlReader.clearErrors();
                        continue;
                    }
                    record = marcXmlReader.next();
                    writer.write(record);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        writer.close();
        timer.stop();
    }

    @Test
    public void testBatchProcessProfileProcessor1() throws Exception {
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = getOLEBatchProcessProfileBo1();
        Assert.assertEquals("Test_Bib_Profile_1", oleBatchProcessProfileBo.getBatchProcessProfileName());
        String profileXML = getOLEBatchProcessProfileRecordProcessor().toXml(oleBatchProcessProfileBo);
        System.out.println(profileXML);
        OLEBatchProcessProfileBo oleBatchProcessProfileBo1 = getOLEBatchProcessProfileRecordProcessor().fromXML(profileXML);
        Assert.assertNotNull(oleBatchProcessProfileBo1);
        Assert.assertEquals("Test_Bib_Profile_1", oleBatchProcessProfileBo1.getBatchProcessProfileName());
    }

    @Test
    public void testBatchProcessProfileProcessor2() throws Exception {
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = getOLEBatchProcessProfileBo2();
        Assert.assertEquals("Test_Bib_Profile_2", oleBatchProcessProfileBo.getBatchProcessProfileName());
        String profileXML = getOLEBatchProcessProfileRecordProcessor().toXml(oleBatchProcessProfileBo);
        System.out.println(profileXML);
        OLEBatchProcessProfileBo oleBatchProcessProfileBo1 = getOLEBatchProcessProfileRecordProcessor().fromXML(profileXML);
        Assert.assertNotNull(oleBatchProcessProfileBo1);
        Assert.assertEquals("Test_Bib_Profile_2", oleBatchProcessProfileBo1.getBatchProcessProfileName());
    }

    public OLEBatchProcessProfileBo getOLEBatchProcessProfileBo1() throws Exception {
        String profileXML = getXmlAsString("/org/kuali/ole/batch/bibImportProfiles/BibImportProfile1.xml");
        return getOLEBatchProcessProfileRecordProcessor().fromXML(profileXML);
    }

    public OLEBatchProcessProfileBo getOLEBatchProcessProfileBo2() {
        String profileXML = getXmlAsString("/org/kuali/ole/batch/bibImportProfiles/BibImportProfile2.xml");
        return getOLEBatchProcessProfileRecordProcessor().fromXML(profileXML);
    }

    public String getXmlAsString(String filePath){
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource(filePath).toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    private OLEBatchProcessProfileBo getOLEBatchProcessProfileBo() {
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = new OLEBatchProcessProfileBo();
        oleBatchProcessProfileBo.setBatchProcessProfileId("1");
        oleBatchProcessProfileBo.setBatchProcessProfileDesc("Mock Desc");
        oleBatchProcessProfileBo.setBatchProcessProfileName("Mock Profile");
        oleBatchProcessProfileBo.setBatchProcessProfileType("Batch Export");
        List<OLEBatchProcessProfileFilterCriteriaBo> filterCriteriaBoList = new ArrayList<>();
        OLEBatchProcessProfileFilterCriteriaBo filterCriteriaBo = new OLEBatchProcessProfileFilterCriteriaBo();
        filterCriteriaBo.setFilterId("1");
        filterCriteriaBo.setDataType("BibMarc");
        filterCriteriaBo.setFilterFieldName("test");
        filterCriteriaBoList.add(filterCriteriaBo);
        oleBatchProcessProfileBo.setOleBatchProcessProfileFilterCriteriaList(filterCriteriaBoList);
        List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsList = new ArrayList<OLEBatchProcessProfileMappingOptionsBo>();
        OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo = new OLEBatchProcessProfileMappingOptionsBo();
        List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = new ArrayList<OLEBatchProcessProfileDataMappingOptionsBo>();
        OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo = new OLEBatchProcessProfileDataMappingOptionsBo();
        oleBatchProcessProfileDataMappingOptionsBo.setDataType("BibMarc");
        oleBatchProcessProfileDataMappingOptionsBo.setSourceField("CallNumber");
        oleBatchProcessProfileDataMappingOptionsBo.setDataTypeDestinationField("BibMarc");
        oleBatchProcessProfileDataMappingOptionsBo.setDestinationField("245 $a");
        oleBatchProcessProfileDataMappingOptionsBoList.add(oleBatchProcessProfileDataMappingOptionsBo);
        oleBatchProcessProfileMappingOptionsBo.setOleBatchProcessProfileDataMappingOptionsBoList(oleBatchProcessProfileDataMappingOptionsBoList);
        oleBatchProcessProfileMappingOptionsList.add(oleBatchProcessProfileMappingOptionsBo);
        oleBatchProcessProfileBo.setOleBatchProcessProfileMappingOptionsList(oleBatchProcessProfileMappingOptionsList);
        List<OLEBatchGloballyProtectedField> oleBatchGloballyProtectedFieldList = new ArrayList<OLEBatchGloballyProtectedField>();
        OLEBatchGloballyProtectedField oleBatchGloballyProtectedField = new OLEBatchGloballyProtectedField();
        oleBatchGloballyProtectedField.setId("1");
        oleBatchGloballyProtectedField.setGloballyProtectedFieldId("1");
        oleBatchGloballyProtectedField.setTag("999");
        oleBatchGloballyProtectedFieldList.add(oleBatchGloballyProtectedField);
        oleBatchProcessProfileBo.setOleBatchGloballyProtectedFieldList(oleBatchGloballyProtectedFieldList);
        return oleBatchProcessProfileBo;
    }

}
