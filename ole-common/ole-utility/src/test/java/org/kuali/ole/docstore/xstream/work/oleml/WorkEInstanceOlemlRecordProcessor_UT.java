package org.kuali.ole.docstore.xstream.work.oleml;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.*;
import org.kuali.ole.docstore.model.xstream.work.oleml.WorkEInstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 7/19/13
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkEInstanceOlemlRecordProcessor_UT
        extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(WorkEInstanceOlemlRecordProcessor_UT.class);
    InstanceCollection instanceCollection = null;
    private EInstance eInstance = null;
    private EHoldings eHoldings = null;
    String sampleXmlFilePath = "/org/kuali/ole/ole-einstance-latestv1.xsd.xml";

    @Before
    public void setUp() {

        ObjectFactory objectFactory = new ObjectFactory();
        instanceCollection = new InstanceCollection();
        eHoldings = objectFactory.createEHoldings();
        List<FormerIdentifier> formerResourceIdentifier = null;
        List<EInstance> oleInstanceList = new ArrayList<EInstance>();
        ExtentOfOwnership extentOfOwnership = objectFactory.createExtentOfOwnership();
        eInstance = objectFactory.createEInstance();
        eInstance.setInstanceIdentifier("mockInstacneIdentifier");
        List<String> resourceIdentifiers = eInstance.getResourceIdentifier();
        if (resourceIdentifiers.isEmpty()) {
            LOG.info("empty values");
            resourceIdentifiers.add("a1000003");
        }
        //  eInstance.setResourceIdentifier(resourceIdentifiers);

        List<FormerIdentifier> formerIdentifierList = eInstance.getFormerResourceIdentifier();

        if (formerIdentifierList.isEmpty()) {
            FormerIdentifier formerIdentifier = objectFactory.createFormerIdentifier();
            Identifier identifier = objectFactory.createIdentifier();
            identifier.setIdentifierValue("1000003-2");
            identifier.setSource("IUHoldings");
            LOG.info("Identifier Val:" + identifier.getIdentifierValue());
            LOG.info("Source:" + identifier.getSource());

            formerIdentifier.setIdentifier(identifier);
            formerIdentifier.setIdentifierType("Ty 001");
            formerIdentifierList.add(formerIdentifier);
            LOG.info("Indentifier Source:" + formerIdentifier.getIdentifier().getSource());
            LOG.info("Indentifier Type:" + formerIdentifier.getIdentifierType());
        }

        // eInstance.setFormerResourceIdentifier(formerIdentifierList);


        formerIdentifierList = eInstance.getFormerResourceIdentifier();

        if (formerIdentifierList.isEmpty()) {
            FormerIdentifier formerIdentifier = objectFactory.createFormerIdentifier();
            Identifier identifier = objectFactory.createIdentifier();
            identifier.setIdentifierValue("1000003-2");
            identifier.setSource("IUHoldings");
            LOG.info("Identifier Val:" + identifier.getIdentifierValue());
            LOG.info("Source:" + identifier.getSource());

            formerIdentifier.setIdentifier(identifier);
            formerIdentifier.setIdentifierType("Ty 001");
            formerIdentifierList.add(formerIdentifier);
            LOG.info("Indentifier Source:" + formerIdentifier.getIdentifier().getSource());
            LOG.info("Indentifier Type:" + formerIdentifier.getIdentifierType());
        }
        List<String> statisticalCodeList = new ArrayList<>();


        statisticalCodeList.add("a1000003");

        eHoldings.setStatisticalSearchingCode("");
        TypeOrSource typeOrSource = new TypeOrSource();
        typeOrSource.setPointer("P");
        typeOrSource.setText("T");
        List<Note> noteList = new ArrayList<>();
        Note note = objectFactory.createNote();
        note.setType("NT");
        note.setValue("NT");
        noteList.add(note);
//        eHoldings.setNote(noteList);

        eHoldings = objectFactory.createEHoldings();
        eHoldings.setHoldingsIdentifier("30004-1");
        note = new Note();
        note.setValue("Completed");
        List<ExtentOfOwnership> extentOfOwnershipList = new ArrayList<>();
        extentOfOwnershipList.add(extentOfOwnership);

        extentOfOwnership = new ExtentOfOwnership();
        extentOfOwnershipList.add(extentOfOwnership);
        note = new Note();
        note.setType("AST");
        note.setType("AST");
        eHoldings.getNote().add(note);

        CallNumber callNumber = objectFactory.createCallNumber();
        callNumber.setNumber("8008");
        callNumber.setPrefix("IT");
        eHoldings.setCallNumber(callNumber);
        assertNotNull(eHoldings.getCallNumber());
        eHoldings.setCallNumber(callNumber);
        eHoldings.setPrimary("true");


        List<String> shelvingOrderList = new ArrayList<>();
        ShelvingOrder shelvingOrder = objectFactory.createShelvingOrder();
        shelvingOrderList.add("SC002");
        shelvingOrderList.add("SC002");


//        callNumber.setShelvingOrder(shelvingOrderList);

//        assertNotNull(callNumber.getShelvingOrder());


//        extentOfOwnershipList = eHoldings.getExtentOfOwnership();
        for (ExtentOfOwnership extentOfOwnership1 : extentOfOwnershipList) {
        }
        LOG.info("Holdings Identifier:" + eHoldings.getHoldingsIdentifier());
        LOG.info("Number:" + eHoldings.getCallNumber().getNumber());
        LOG.info("Prefix:" + eHoldings.getCallNumber().getPrefix());
        LOG.info("Primary:" + eHoldings.getPrimary());

        List<Coverage> coverageList = new ArrayList<>();
        List<Coverages> coveragesList = new ArrayList<>();

        Coverages coverages = new Coverages();
        Coverage coverage = objectFactory.createCoverage();
        List<String> coverageEndDateList = new ArrayList<>();
        coverageEndDateList.add("1995");
//        coverage.setCoverageEndDate(coverageEndDateList);
        coverageList.add(coverage);
//        coverages.setCoverage(coverageList);
        coveragesList.add(coverages);
//        extentOfOwnership.setCoverages(coveragesList);
        extentOfOwnershipList.add(extentOfOwnership);
//        eHoldings.setExtentOfOwnership(extentOfOwnershipList); /

        eInstance.setEHoldings(eHoldings);
        //eInstance = objectFactory.createEInstance();
        // eInstance.setResourceIdentifier(statisticalCEodeList);
        oleInstanceList.add(eInstance);
        instanceCollection.getEInstance().addAll(oleInstanceList);
        Assert.assertNotNull(objectFactory.createInstanceCollection(instanceCollection));
    }


    public void tearDown() {
        LOG.info("Running tearDown()");
    }

    @Test
    public void testGenerateXML() throws Exception {
        WorkEInstanceOlemlRecordProcessor oleInstanceConverter = new WorkEInstanceOlemlRecordProcessor();
        String xml = oleInstanceConverter.toXML(instanceCollection);
        assertNotNull(xml);
        LOG.info(xml);
        InstanceCollection instanceCollection1 = oleInstanceConverter.fromXML(xml);
        assertNotNull(instanceCollection1);
        LOG.info("instanceCollection" + instanceCollection1);

        LOG.info("Regained XML: \n" + oleInstanceConverter.toXML(instanceCollection1));
    }


    @Test
    public void testGenerateObjectFromXML() throws Exception {
        WorkEInstanceOlemlRecordProcessor oleInstanceConverter = new WorkEInstanceOlemlRecordProcessor();
        URL url = getClass().getResource(sampleXmlFilePath);
        File file = new File(url.toURI());
        instanceCollection = oleInstanceConverter.fromXML(readFile(file));
        LOG.info("entered");
        for (EInstance inst : instanceCollection.getEInstance()) {
            LOG.info("Instance Identifier:" + inst.getInstanceIdentifier());
            LOG.info("Resource Identifier:" + inst.getResourceIdentifier());
            LOG.info("Former Resource Identifier:" + inst.getResourceIdentifier());
            LOG.info(inst.getEHoldings().getPrimary());
        }
        String xml = oleInstanceConverter.toXML(instanceCollection);
        LOG.info("xml-->" + xml);
    }

    @Test
    public void testFromXMLAndToXmlInstanceCollection() throws Exception {
        WorkEInstanceOlemlRecordProcessor oleInstanceConverter = new WorkEInstanceOlemlRecordProcessor();
        URL url = getClass().getResource(sampleXmlFilePath);
        File file = new File(url.toURI());
        InstanceCollection instanceCollection = oleInstanceConverter.fromXML(readFile(file));
        assertNotNull(instanceCollection);
        String xml = oleInstanceConverter.toXML(instanceCollection);
        LOG.info("Generated XML : " + xml);
        LOG.info("GENERATED XML: ");
        LOG.info(xml);
        assertNotNull(xml);
    }

    private String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }


}

