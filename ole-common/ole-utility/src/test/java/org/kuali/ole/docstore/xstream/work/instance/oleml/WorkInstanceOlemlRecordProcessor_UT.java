package org.kuali.ole.docstore.xstream.work.instance.oleml;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.*;
import org.kuali.ole.docstore.model.xstream.work.instance.oleml.WorkInstanceOlemlRecordProcessor;
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

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotNull;

/**
 * User: tirumalesh.b
 * Date: 10/2/12 Time: 6:48 PM
 */
public class WorkInstanceOlemlRecordProcessor_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(WorkInstanceOlemlRecordProcessor_UT.class);
    InstanceCollection instanceCollection = null;
    private Instance instance = null;
    private Item item = null;
    private OleHoldings holdings = null;
    private SourceHoldings sourceHoldings = null;
    String sampleXmlFilePath = "/org/kuali/ole/instance9.1.1.xml";

    @Before
    public void setUp() {

        ObjectFactory objectFactory = new ObjectFactory();
        instanceCollection = objectFactory.createInstanceCollection();
        List<FormerIdentifier> formerResourceIdentifier = null;
        List<Instance> oleInstanceList = new ArrayList<Instance>();
        ExtentOfOwnership extentOfOwnership = objectFactory.createExtentOfOwnership();
        instance = objectFactory.createInstance();
        instance.setInstanceIdentifier("mockInstacneIdentifier");
        List<String> resourceIdentifiers = instance.getResourceIdentifier();
        if (resourceIdentifiers.isEmpty()) {
            LOG.info("empty values");
            resourceIdentifiers.add("a1000003");
        }
        instance.setResourceIdentifier(resourceIdentifiers);

        List<FormerIdentifier> formerIdentifierList = instance.getFormerResourceIdentifier();

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

        instance.setFormerResourceIdentifier(formerIdentifierList);

        sourceHoldings = objectFactory.createSourceHoldings();
        sourceHoldings.setHoldingsIdentifier("1000003-2-1");


        Extension extension = objectFactory.createExtension();
        if (instance.getExtension() == null) {
            extension.setDisplayLabel("Extension");
            sourceHoldings.setHoldings(extension);
            sourceHoldings.setExtension(extension);
            sourceHoldings.setName("Holdings");
            sourceHoldings.setPrimary("primary");
        }

        LOG.info("Name:" + sourceHoldings.getName());
        LOG.info("Display Label:" + sourceHoldings.getExtension().getDisplayLabel());
        LOG.info("Display Label:" + sourceHoldings.getHoldings().getDisplayLabel());
        LOG.info("Display Label:" + sourceHoldings.getPrimary());
        LOG.info("Holdings Identifier:" + sourceHoldings.getHoldingsIdentifier());
        instance.setSourceHoldings(sourceHoldings);

        Items items = objectFactory.createItems();
        item = objectFactory.createItem();
        List<Item> itemList = new ArrayList<Item>();
        item.setItemIdentifier("200003-2");

        ItemType itemType = objectFactory.createItemType();
        itemType.setCodeValue("IT001");
        itemType.setFullValue("Item 01");
        TypeOrSource typeOrSource = objectFactory.createTypeOrSource();
        typeOrSource.setText("Type");
        typeOrSource.setPointer("P");
        itemType.setTypeOrSource(typeOrSource);
        LOG.info("Text:" + itemType.getTypeOrSource().getText());
        LOG.info("Pointer:" + itemType.getTypeOrSource().getPointer());

        item.setItemType(itemType);
        item.setVendorLineItemIdentifier("V001");
        AccessInformation accessInformation = objectFactory.createAccessInformation();
        accessInformation.setBarcode("100001");
        Uri accessUri = objectFactory.createUri();
        accessUri.setResolvable("true");
        accessInformation.setUri(accessUri);
        Assert.assertNotNull(accessInformation.getUri());
        item.setExtension(extension);
        assertNotNull(item.getExtension());
        item.setBarcodeARSL("ARSL");
        formerIdentifierList = instance.getFormerResourceIdentifier();

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

        item.setFormerIdentifier(formerIdentifierList);
        assertNotNull(item.getFormerIdentifier());
        List<StatisticalSearchingCode> statisticalSearchingCodeList = new ArrayList<StatisticalSearchingCode>();
        StatisticalSearchingCode statisticalSearchingCode = objectFactory.createStatisticalSearchingCode();
        statisticalSearchingCode.setCodeValue("SC001");
        statisticalSearchingCode.setFullValue("SC001");
        typeOrSource = new TypeOrSource();
        typeOrSource.setPointer("P");
        typeOrSource.setText("T");
        statisticalSearchingCode.setTypeOrSource(typeOrSource);
        statisticalSearchingCodeList.add(statisticalSearchingCode);
        item.setStatisticalSearchingCode(statisticalSearchingCodeList);
        assertNotNull(item.getStatisticalSearchingCode());
        item.setCopyNumber("C001");
        item.setVolumeNumber("V001");
        /*item.setDonorNote("donorNote");
        assertNotNull(item.getDonorNote());*/
        item.setVolumeNumberLabel("V001");
        Note note = objectFactory.createNote();
        note.setType("NT");
        note.setValue("NT");
        item.getNote().add(note);
        item.setEnumeration("E");
        item.setChronology("Chronology-1");
        accessInformation = new AccessInformation();
        accessInformation.setBarcode("A-001");
        item.setAccessInformation(accessInformation);
        item.setFund("1000");
        ItemStatus itemStatus = new ItemStatus();
        itemStatus.setCodeValue("In Process");
        itemStatus.setFullValue("In Process");
        item.setItemStatus(itemStatus);
        itemType = new ItemType();
        itemType.setCodeValue("IT001");
        itemType.setFullValue("IT001");
        item.setTemporaryItemType(itemType);
        Location location = objectFactory.createLocation();
        location.setStatus("ACR");
        location.setPrimary("true");
        LocationLevel locationLevel = objectFactory.createLocationLevel();
        locationLevel.setName("Level1");
        locationLevel.setLevel("1");
        location.setLocationLevel(locationLevel);
        locationLevel.setLocationLevel(locationLevel);
        assertNotNull(locationLevel.getLocationLevel());
        item.setLocation(location);
        //item.setDonorPublicDisplay("Donor");
        item.setPrice("1000");
        item.setNumberOfPieces("12");
        item.setItemStatusEffectiveDate("12/12/12");
        item.setCheckinNote("Pending");
        item.setStaffOnlyFlag(true);
        item.setFastAddFlag(true);
        item.setAnalytic("yes");
        item.setResourceIdentifier("IT003");
        itemList.add(item);
        items.setItem(itemList);
        instance.setItems(items);

        items = instance.getItems();
        itemList = items.getItem();

        for (Item item1 : itemList) {
            LOG.info("Item Identifier:" + item1.getItemIdentifier());
            LOG.info("Purchase Order Line Item Identifier:" + item1.getPurchaseOrderLineItemIdentifier());
            LOG.info("Vendor Line Item Identifier:" + item1.getVendorLineItemIdentifier());
            LOG.info("Barcode:" + item1.getAccessInformation().getBarcode());
            LOG.info("Barcode ARSL:" + item1.getBarcodeARSL());
            LOG.info("Identifier:" + item1.getFormerIdentifier().get(0).getIdentifier());
            LOG.info("Code Val:" + item1.getStatisticalSearchingCode().get(0).getCodeValue());
            LOG.info("Full Val:" + item1.getStatisticalSearchingCode().get(0).getFullValue());
            LOG.info("Pointer:" + item1.getStatisticalSearchingCode().get(0).getTypeOrSource().getPointer());
            LOG.info("Text:" + item1.getStatisticalSearchingCode().get(0).getTypeOrSource().getText());
            LOG.info("Copy Label:" + item1.getCopyNumberLabel());
            LOG.info("Volume Number:" + item1.getVolumeNumber());
            //LOG.info("Donor Public Display:" + item1.getDonorPublicDisplay());
            LOG.info("Copy Number:" + item1.getCopyNumber());
            LOG.info("Volume Number Label:" + item1.getVolumeNumberLabel());
            LOG.info("Note Type:" + item1.getNote());
            LOG.info("Note Value:" + item1.getNote());
            LOG.info("Enumeration:" + item1.getEnumeration());
            LOG.info("Chronology:" + item1.getChronology());
            LOG.info("Code Value:" + item1.getItemType().getCodeValue());
            LOG.info("Full Value:" + item1.getItemType().getFullValue());
            LOG.info("Code Value:" + item1.getTemporaryItemType().getCodeValue());
            LOG.info("Fund:" + item1.getFund());
            LOG.info("Price:" + item1.getPrice());
            LOG.info("Number of Pieces:" + item1.getNumberOfPieces());
            LOG.info("Item Status:" + item1.getItemStatus());
            LOG.info("Item Status Effective Date:" + item1.getItemStatusEffectiveDate());
            LOG.info("Check In Note:" + item1.getCheckinNote());
            LOG.info("Is StaffOnlyFlag:" + item1.isStaffOnlyFlag());
            LOG.info("Is FastAddFlag:" + item1.isFastAddFlag());
            LOG.info("Status:" + item1.getLocation().getStatus());
            LOG.info("Primary:" + item1.getLocation().getPrimary());
            LOG.info("Level:" + item1.getLocation().getLocationLevel().getLevel());
            LOG.info("Name:" + item1.getLocation().getLocationLevel().getName());
//            LOG.info("Display Label:" + item1.getExtension().getDisplayLabel());
        }

        holdings = objectFactory.createOleHoldings();
        holdings.setHoldingsIdentifier("30004-1");
        note = new Note();
        note.setValue("Completed");
        extentOfOwnership.setTextualHoldings("textualHoldings1");
        extentOfOwnership.setType("typeOfOwnership1");
        extentOfOwnership.getNote().add(note);
        List<ExtentOfOwnership> extentOfOwnershipList = new ArrayList<ExtentOfOwnership>();
        extentOfOwnershipList.add(extentOfOwnership);
        extentOfOwnership = new ExtentOfOwnership();
        extentOfOwnership.setTextualHoldings("textualHoldings2");
        extentOfOwnershipList.add(extentOfOwnership);
        holdings.setExtentOfOwnership(extentOfOwnershipList);
        note = new Note();
        note.setType("AST");
        note.setType("AST");
        holdings.getNote().add(note);
        holdings.setLocation(location);
        holdings.setReceiptStatus("Completed");
        CallNumber callNumber = objectFactory.createCallNumber();
        callNumber.setItemPart("IT001");
        callNumber.setNumber("8008");
        callNumber.setPrefix("IT");
        callNumber.setType("Valid");
        item.setCallNumber(callNumber);
        assertNotNull(item.getCallNumber());
        holdings.setCallNumber(callNumber);
        holdings.setPrimary("true");

        Item item2 = new Item();
        item2.setStatisticalSearchingCode(null);
        assertNotNull(item2.getStatisticalSearchingCode());
        item2.setFormerIdentifier(null);
        assertNotNull(item2.getFormerIdentifier());
        ShelvingScheme shelvingScheme = objectFactory.createShelvingScheme();
        shelvingScheme.setCodeValue("SC001");
        shelvingScheme.setFullValue("SC001");
        shelvingScheme.setTypeOrSource(typeOrSource);
        assertNotNull(shelvingScheme.getCodeValue());
        assertNotNull(shelvingScheme.getFullValue());
        assertNotNull(shelvingScheme.getTypeOrSource());


        ShelvingOrder shelvingOrder = objectFactory.createShelvingOrder();
        shelvingOrder.setCodeValue("SC002");
        shelvingOrder.setFullValue("SC002");
        shelvingOrder.setTypeOrSource(typeOrSource);
        assertNotNull(shelvingOrder.getTypeOrSource());
        assertNotNull(shelvingOrder.getFullValue());
        assertNotNull(shelvingOrder.getCodeValue());


        callNumber.setClassificationPart("classificationPart");
        callNumber.setShelvingScheme(shelvingScheme);
        callNumber.setShelvingOrder(shelvingOrder);
        assertNotNull(callNumber.getClassificationPart());
        assertNotNull(callNumber.getShelvingOrder());
        assertNotNull(callNumber.getShelvingScheme());

        extentOfOwnershipList = holdings.getExtentOfOwnership();
        for (ExtentOfOwnership extentOfOwnership1 : extentOfOwnershipList) {
            LOG.info("Textual Holdings:" + extentOfOwnership1.getTextualHoldings());
            LOG.info("Type:" + extentOfOwnership1.getType());
        }
        LOG.info("Holdings Identifier:" + holdings.getHoldingsIdentifier());
        LOG.info("Receipt Status:" + holdings.getReceiptStatus());
        LOG.info("Location Status:" + holdings.getLocation().getStatus());
        LOG.info("Item Part:" + holdings.getCallNumber().getItemPart());
        LOG.info("Number:" + holdings.getCallNumber().getNumber());
        LOG.info("Prefix:" + holdings.getCallNumber().getPrefix());
        LOG.info("Type:" + holdings.getCallNumber().getType());
        LOG.info("Primary:" + holdings.getPrimary());
        LOG.info("Value:" + holdings.getExtentOfOwnership().get(0).getNote().get(0).getValue());
        LOG.info("Value:" + holdings.getExtentOfOwnership().get(0).getNote().get(0).getValue());

        instance.setOleHoldings(holdings);
        instance = objectFactory.createInstance();
        instance.setItems(items);
        instance.setResourceIdentifier(resourceIdentifiers);
        instance.setSourceHoldings(sourceHoldings);
        oleInstanceList.add(instance);
        instanceCollection.setInstance(oleInstanceList);
        Assert.assertNotNull(objectFactory.createInstanceCollection(instanceCollection));
    }


    /*   @Before
   public void setUp() {
         super.setUp();
       } catch (Exception e) {
       }
      LOG.info("Running Setup()");
       instanceCollection = new InstanceCollection();
       List<Instance> oleInstanceList = new ArrayList<Instance>();
       instance = new Instance();
       instance.setInstanceIdentifier("mockInstacneIdentifier");

       List<ResourceIdentifier> resourceIdentifiers = new ArrayList<ResourceIdentifier>();
       ResourceIdentifier resourceIdentifier = new ResourceIdentifier();
       resourceIdentifier.setSource("Resource Soruce 1");
       resourceIdentifier.setValue("Resource Value 1");
       resourceIdentifiers.add(resourceIdentifier);
       instance.setResourceIdentifier(resourceIdentifiers);

       List<FormerResourceIdentifiers> formerRIs = new ArrayList<FormerResourceIdentifiers>();
       FormerResourceIdentifiers fri = new FormerResourceIdentifiers();
       fri.setSource("Former Source 1");
       fri.setValue("Former Resource Value 1");
       formerRIs.add(fri);
       instance.setFormerResourceIdentifiers(formerRIs);

       Extension extension = new Extension();
       extension.setDisplayLabel("Extension Display Label 1");
       AdditionalAttributes additionalAttributes = new AdditionalAttributes();
       additionalAttributes.setDateEntered("2012-02-29");
       additionalAttributes.setLastUpdated("2012-03-01");
       additionalAttributes.setFastAddFlag("true");
       additionalAttributes.setSupressFromPublic("false");
       additionalAttributes.setHarvestable("true");
       additionalAttributes.setStatus("n"); // new Record
       List<Object> extensionContents = new ArrayList<Object>();
       extensionContents.add(additionalAttributes);
       extension.setContent(extensionContents);
       List<Extension> extensions = new ArrayList<Extension>();
       extensions.add(extension);
       instance.setExtension(extensions);

       // HOLDINGS : CREATION :START
       holdings = new OleHoldings();
       holdings.setHoldingsIdentifier("holdingsIdentifier");

       FormerIdentifiers formerIdentifiers1 = new FormerIdentifiers();
       formerIdentifiers1.setIdentifier("identifier1");
       formerIdentifiers1.setIdentifierType("identifierType1");
       FormerIdentifiers formerIdentifiers2 = new FormerIdentifiers();
       formerIdentifiers2.setIdentifier("identifier2");
       formerIdentifiers2.setIdentifierType("identifierType2");

       holdings.getFormerIdentifiers().add(formerIdentifiers1);
       holdings.getFormerIdentifiers().add(formerIdentifiers2);
       holdings.setRecordType("recordType");
       holdings.setEncodingLevel("encodingLevel");
       holdings.setReceiptStatus("receiptStatus");
       holdings.setAcquisitionMethod("AquisitionMethod 1");
       holdings.setExpectedAcquisitionEndDate("expectedAcquisitionEndDate");
       holdings.setGeneralRetentionPolicy("generalRetentionPolicy");

       SpecificRetentionPolicy specificRetentionPolicy = new SpecificRetentionPolicy();
       specificRetentionPolicy.setNumberOfUnits("numberOfUnits");
       specificRetentionPolicy.setPolicyType("policyType");
       specificRetentionPolicy.setUnitType("unitType");
       holdings.setSpecificRetentionPolicy(specificRetentionPolicy);

       holdings.setCompleteness("completeness");
       holdings.setCopiesReported("copiesReported");
       holdings.setLendingPolicy("lendingPolicy");
       holdings.setReproductionPolicy("reproductionPolicy");
       holdings.setSeparateOrCompositeReport("separateOrCompositeReport");

       ActionNote actionNote = new ActionNote();
       actionNote.setAction("action 1");
       actionNote.setPrivacy("privacy 1");
       ArrayList<String> actionIdentifications = new ArrayList<String>();
       actionIdentifications.add("action Indentification 1");
       actionIdentifications.add("action Indentification 2");
       actionNote.setActionIdentification(actionIdentifications);

  actionNote.getTimeDateOfAction().add("timeDateOfAction1");
  actionNote.getTimeDateOfAction().add("timeDateOfAction2");

  actionNote.getActionInterval().add("actionInterval1");
  actionNote.getActionInterval().add("actionInterval2");

  actionNote.getContingencyForAction().add("contingencyForAction1");
  actionNote.getContingencyForAction().add("contingencyForAction2");

  actionNote.getAuthorization().add("authorization1");
  actionNote.getAuthorization().add("authorization2");

  actionNote.getJurisdiction().add("jurisdiction1");
  actionNote.getJurisdiction().add("jurisdiction2");

  actionNote.getMethodOfAction().add("methodOfAction1");
  actionNote.getMethodOfAction().add("methodOfAction2");

  actionNote.getSiteOfAction().add("siteOfAction1");
  actionNote.getSiteOfAction().add("siteOfAction2");

  actionNote.getActionAgent().add("actionAgent1");
  actionNote.getActionAgent().add("actionAgent2");

  actionNote.getActionStatus().add("actionStatus1");
  actionNote.getActionStatus().add("actionStatus2");

  actionNote.getActionExtent().add("actionExtent1");
  actionNote.getActionExtent().add("actionExtent2");

  actionNote.getUnitType().add("unitType1");
  actionNote.getUnitType().add("unitType2");

  actionNote.getNonPublicNote().add("nonPublicNote1");
  actionNote.getNonPublicNote().add("nonPublicNote2");

  actionNote.getPublicNote().add("publicNote1");
  actionNote.getPublicNote().add("publicNote2");

  ArrayList<String> sourceOfTerm = new ArrayList<String>();
  sourceOfTerm.add("sourceOfTerm1");
  sourceOfTerm.add("sourceOfTerm2");
  actionNote.setSourceOfTerm(sourceOfTerm);

  actionNote.getActionURI().add("actionURI1");
  actionNote.getActionURI().add("actionURI2");

  actionNote.setMaterialsSpecified("materialsSpecified1");

  InstitutionToWhichFieldApplies institution = new InstitutionToWhichFieldApplies();
  institution.setValue("value20");
  TypeOrSource typeOrSource3 = new TypeOrSource();
  typeOrSource3.setPointer("Action pointer1");
  typeOrSource3.setText("Action text1");
  institution.setTypeOrSource(typeOrSource3);
  actionNote.setInstitutionToWhichFieldApplies(institution);

  holdings.getActionNote().add(actionNote);

  ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();
  extentOfOwnership.setTypeOfOwnership("typeOfOwnership1");
  extentOfOwnership.setTextualHoldings("textualHoldings1");
  extentOfOwnership.setFieldEncodingLevel("fieldEncodingLevel1");
  extentOfOwnership.setTypeOfNotation("typeOfNotation1");

  extentOfOwnership.getNonPublicNote().add("nonPublicNote1");
  extentOfOwnership.getNonPublicNote().add("nonPublicNote2");

  extentOfOwnership.getPublicNote().add("publicNote1");
  extentOfOwnership.getPublicNote().add("publicNote2");

  extentOfOwnership.setExtentSourceOfNotation("extentSourceOfNotation1");
  holdings.getExtentOfOwnership().add(extentOfOwnership);
  extentOfOwnership = new ExtentOfOwnership();
  extentOfOwnership.setTypeOfOwnership("typeOfOwnership2");
  extentOfOwnership.setTextualHoldings("textualHoldings2");
  extentOfOwnership.setFieldEncodingLevel("fieldEncodingLevel2");
  extentOfOwnership.setTypeOfNotation("typeOfNotation2");

  extentOfOwnership.getNonPublicNote().add("nonPublicNote3");
  extentOfOwnership.getNonPublicNote().add("nonPublicNote4");

  extentOfOwnership.getPublicNote().add("publicNote3");
  extentOfOwnership.getPublicNote().add("publicNote4");

  extentOfOwnership.setExtentSourceOfNotation("extentSourceOfNotation2");
  holdings.getExtentOfOwnership().add(extentOfOwnership);

  AlternateGraphicRepresentation alternateGraph = new AlternateGraphicRepresentation();
  alternateGraph.getFieldName().add("fieldName1");
  alternateGraph.getFieldName().add("fieldName2");

  alternateGraph.getFieldValue().add("fieldValue1");
  alternateGraph.getFieldValue().add("fieldValue2");
  holdings.getAlternateGraphicRepresentation().add(alternateGraph);

  alternateGraph = new AlternateGraphicRepresentation();
  alternateGraph.getFieldName().add("fieldName3");
  alternateGraph.getFieldName().add("fieldName4");

  alternateGraph.getFieldValue().add("fieldValue3");
  alternateGraph.getFieldValue().add("fieldValue4");
  holdings.getAlternateGraphicRepresentation().add(alternateGraph);

  Extension ext1 = new Extension();
  ext1.setDisplayLabel("Holding Extension1");
  AdditionalAttributes aa1 = new AdditionalAttributes();
  aa1.setDateEntered("2012-04-11");
  aa1.setLastUpdated("2012-04-02");
  aa1.setSupressFromPublic("false");
  aa1.setFastAddFlag("false");
  aa1.setHarvestable("true");
  aa1.setStatus("n"); // new Record
  ext1.getContent().add(aa1);

  holdings.getExtension().add(ext1);


  instance.setHoldings(holdings);
  // HOLDINGS : CREATION :END

  // ITEM : CREATION :START
  item = new OleItem();

  item.setItemIdentifier("100001022036542365856232");

  item.getPurchaseOrderLineItemIdentifier().add("12678953456465456678946");
  item.getPurchaseOrderLineItemIdentifier().add("254544565654654534343213");
  item.getPurchaseOrderLineItemIdentifier().add("125545632222215465456456");

  item.getVendorLineItemIdentifier().add("vendorLineItemIdentifier1");
  item.getVendorLineItemIdentifier().add("vendorLineItemIdentifier2");

  // Access Information
  AccessInformationType accesInfo = new AccessInformationType();
  accesInfo.setBarcode("1466546456345");
  Uri accessURI = new Uri();
  accessURI.setResolvable(true);
  accesInfo.setUri(accessURI);
  item.setAccessInformation(accesInfo);

  //ElectronicLocationAndAccess
  ElectronicLocationAndAccess electronicLoc = new ElectronicLocationAndAccess();
  electronicLoc.setAccessMethod("accessMethod");
  electronicLoc.setRelationship("relationship");

  electronicLoc.getHostName().add("hostName1");
  electronicLoc.getHostName().add("hostName2");

  electronicLoc.getAccessNumber().add("accessNumber1");
  electronicLoc.getAccessNumber().add("accessNumber2");

  electronicLoc.getCompressionInformation().add("compressionInformation1");
  electronicLoc.getCompressionInformation().add("compressionInformation2");

  electronicLoc.getPath().add("path1");
  electronicLoc.getPath().add("path2");

  electronicLoc.getElectronicName().add("electronicName1");
  electronicLoc.getElectronicName().add("electronicName2");

  electronicLoc.getRequestProcessor().add("requestProcessor1");
  electronicLoc.getRequestProcessor().add("requestProcessor2");

  electronicLoc.getInstruction().add("instruction1");
  electronicLoc.getInstruction().add("instruction2");

  electronicLoc.setBitsPerSecond("bitsPerSecond");
  electronicLoc.setPassword("password");
  electronicLoc.setLogon("logon");

  electronicLoc.getAccessAssistanceContact().add("accessAssistanceContact1");
  electronicLoc.getAccessAssistanceContact().add("accessAssistanceContact2");

  electronicLoc.setLocationHostName("locationHostName");
  electronicLoc.setOperatingSystem("operatingSystem");
  electronicLoc.setPort("port");
  electronicLoc.setElectronicFormatType("electronicFormatType");
  electronicLoc.setSettings("setting");

  electronicLoc.getFileSize().add("fileSize1");
  electronicLoc.getFileSize().add("fileSize2");

  electronicLoc.getTerminalEmulation().add("terminalEmulation1");
  electronicLoc.getTerminalEmulation().add("terminalEmulation2");

  electronicLoc.getHoursAccessMethodAvailable().add("hoursAccessMethodAvailable1");
  electronicLoc.getHoursAccessMethodAvailable().add("hoursAccessMethodAvailable2");

  electronicLoc.getRecordControlNumber().add("recordControlNumber1");
  electronicLoc.getRecordControlNumber().add("recordControlNumber2");

  electronicLoc.getNonPublicNote().add("nonPublicNote1");
  electronicLoc.getNonPublicNote().add("nonPublicNote2");

  electronicLoc.getLinkText().add("linkText1");
  electronicLoc.getLinkText().add("linkText2");

  electronicLoc.getPublicNote().add("publicNote1");
  electronicLoc.getPublicNote().add("publicNote2");

  CodeOrIdentifier codeOrIdentifier = new CodeOrIdentifier();
  codeOrIdentifier.setValue("codeOrIdentifier value");

  TypeOrSource typeOrSource = new TypeOrSource();
  typeOrSource.setPointer("pointer");
  typeOrSource.setText("text");

  codeOrIdentifier.setTypeOrSource(typeOrSource);

  electronicLoc.setAdditionalAccessMethodInformation(codeOrIdentifier);

  electronicLoc.setMaterialsSpecified("materialsSpecified");

  item.setElectronicLocationAndAccess(electronicLoc);
  //End For ElectronicLocationAndAccess

  item.setBarcodeARSL("barcodeARSL");

  FormerIdentifiers formerIdentifiers3 = new FormerIdentifiers();
  formerIdentifiers3.setIdentifier("identifier3");
  formerIdentifiers3.setIdentifierType("identifierType3");
  item.getFormerIdentifiers().add(formerIdentifiers3);

  FormerIdentifiers formerIdentifiers4 = new FormerIdentifiers();
  formerIdentifiers4.setIdentifier("identifier4");
  formerIdentifiers4.setIdentifierType("identifierType4");
  item.getFormerIdentifiers().add(formerIdentifiers4);


  item.getStatisticalSearchingCodes().add("1365466576654167");
  item.getStatisticalSearchingCodes().add("1365466576654168");

  item.setItemType("Book");

  item.setCopyNumber("1");
  item.setCopyNumberLabel("c");
  item.setVolumeNumber("volumeNumber");
  item.setVolumeNumberLabel("volumeNumberLabel");
  item.setPublicNote("PublicNote");
  item.setNonPublicNote("nonPublicNote");
  item.setEnumeration("enumeration");
  item.setChronology("chronology");


  // Location
  PhysicalLocation location = new PhysicalLocation();
  location.getLocationStatus().add("LR");
  location.getLocationStatus().add("LR-1");
  LocationLevel locationLevel = new LocationLevel();
  CodeOrIdentifier levelName = new CodeOrIdentifier();
  levelName.setValue("Level Name 1");
  TypeOrSource typeOrSource1 = new TypeOrSource();
  typeOrSource1.setPointer("Pointer 1");
  typeOrSource1.setText("Text for Level Name 1");
  levelName.setTypeOrSource(typeOrSource1);
  locationLevel.setLevelName(levelName);
  CodeOrIdentifier locationName = new CodeOrIdentifier();
  locationName.setValue("Location Name 1");
  TypeOrSource typeOrSource2 = new TypeOrSource();
  typeOrSource2.setPointer("Location Pointer 1");
  typeOrSource2.setText("Text for Location Name 1");
  locationName.setTypeOrSource(typeOrSource2);
  locationLevel.setLocationName(locationName);
  location.getLocationLevel().add(locationLevel);

  Classification classification1= new Classification();
  classification1.setShelvingScheme("shelvingScheme1");
  classification1.setShelvingOrder("shelvingOrder1");
  classification1.getCallNumberPrefix().add("Call Number Prefix-1");
  classification1.getCallNumberPrefix().add("Call Number Prefix-2");
  classification1.getCallNumberSuffix().add("Call Number Suffix-1");
  classification1.getCallNumberSuffix().add("Call Number Suffix-2");
  classification1.setClassificationPart("classificationPart1");
  classification1.getItemPart().add("itemPart1");
  classification1.getItemPart().add("itemPart2");
  classification1.setClassificationSort("classificationSort1");
  classification1.setClassificationView("classificationView1");
  location.getClassification().add(classification1);
  Classification classification2= new Classification();
  classification2.setShelvingScheme("shelvingScheme2");
  classification2.setShelvingOrder("shelvingOrder3");
  classification2.getCallNumberPrefix().add("2Call Number Prefix-1");
  classification2.getCallNumberPrefix().add("2Call Number Prefix-2");
  classification2.getCallNumberSuffix().add("2Call Number Suffix-1");
  classification2.getCallNumberSuffix().add("2Call Number Suffix-2");
  classification2.setClassificationPart("classificationPart2");
  classification2.getItemPart().add("2itemPart1");
  classification2.getItemPart().add("2itemPart2");
  classification2.setClassificationSort("classificationSort2");
  classification2.setClassificationView("classificationView2");
  location.getClassification().add(classification2);

  location.getFormerShelvingLocation().add("formerShelvingLocation1");
  location.getFormerShelvingLocation().add("formerShelvingLocation2");

  location.getAddress().add("address1");
  location.getAddress().add("address2");
  location.getAddress().add("address3");

  location.getCodedLocationQualifier().add("codedLocationQualifier1");
  location.getCodedLocationQualifier().add("codedLocationQualifier2");

  location.getNoncodedLocationQualifier().add("noncodedLocationQualifier1");
  location.getNoncodedLocationQualifier().add("noncodedLocationQualifier2");

  location.setShelvingControlNumber("shelvingControlNumber1");
  location.setShelvingFormOfTitle("shelvingFormOfTitle1");

  CodeOrIdentifier country = new CodeOrIdentifier();
  country.setValue("country value");
  TypeOrSource countryType = new TypeOrSource();
  countryType.setPointer("countryType pointer1");
  countryType.setText("countryType text1");
  country.setTypeOrSource(countryType);
  location.setCountryCode(country);

  location.getCopyrightArticleFeeCode().add("copyrightArticleFeeCode1");
  location.getCopyrightArticleFeeCode().add("copyrightArticleFeeCode2");

  location.setCopyNumber("123455");
  location.setPieceDesignation("pieceDesignation1");

  location.getNonPublicNote().add("nonPublicNote11");
  location.getNonPublicNote().add("nonPublicNote22");

  location.getPublicNote().add("publicNote11");
  location.getPublicNote().add("publicNote22");

  CodeOrIdentifier classificationId = new CodeOrIdentifier();
  TypeOrSource typeOrSource4 = new TypeOrSource();
  typeOrSource4.setPointer("Classification Pointer -1");
  typeOrSource4.setText("Classification Text -1");
  classificationId.setTypeOrSource(typeOrSource4);
  classificationId.setValue("Classification-1");
  location.setClassificationOrShelvingSchemeSource(classificationId);

  location.setMaterialsSpecified("materialsSpecified1");
  item.getLocation().add(location);

  item.getVendorLineItemIdentifier().add("Vendoer Line Item Identifier-1");

  // Extension
  Extension ext = new Extension();
  ext.setDisplayLabel("display Label --20");
  AdditionalAttributes aa = new AdditionalAttributes();
  aa.setDateEntered("2012-04-10");
  aa.setLastUpdated("2012-04-01");
  aa.setSupressFromPublic("false");
  aa.setFastAddFlag("false");
  aa.setHarvestable("true");
  aa.setStatus("n"); // new Record
  ext.getContent().add(aa);
  item.getExtension().add(ext);
  // ITEM : CREATION :END

  instance.getItem().add(item);
  oleInstanceList.add(instance);
  instanceCollection.setInstanceCollection(oleInstanceList);

}     */

    public void tearDown() {
        LOG.info("Running tearDown()");
    }

    @Test
    public void testGenerateXML() throws Exception {
        WorkInstanceOlemlRecordProcessor oleInstanceConverter = new WorkInstanceOlemlRecordProcessor();
        String xml = oleInstanceConverter.toXML(instanceCollection);
        assertNotNull(xml);
        LOG.info(xml);
        InstanceCollection instanceCollection = oleInstanceConverter.fromXML(xml);
        assertNotNull(instanceCollection);
        LOG.info("instanceCollection" + instanceCollection);

        LOG.info("Regained XML: \n" + oleInstanceConverter.toXML(instanceCollection));
    }

    @Test
    public void testToXMLHoldings() {
        try {
            WorkInstanceOlemlRecordProcessor processor = new WorkInstanceOlemlRecordProcessor();
            String holdingsXml = processor.toXML(holdings);
            String sourceHoldingsXml = processor.toXML(sourceHoldings);
            LOG.info(holdingsXml);
            assertNotNull(holdingsXml);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Holdings Object to Xml Conversion Failed." + e);
        }

    }

    @Test
    public void testToXMLItem() {
        try {
            WorkInstanceOlemlRecordProcessor processor = new WorkInstanceOlemlRecordProcessor();
            String xml = processor.toXML(item);
            LOG.info("XML : \n" + xml);
            assertNotNull(xml);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Iteml Object to Xml Conversion Failed." + e);
        }
    }

    @Test
    public void testGenerateObjectFromXML() throws Exception {
        WorkInstanceOlemlRecordProcessor oleInstanceConverter = new WorkInstanceOlemlRecordProcessor();
        URL url = getClass().getResource(sampleXmlFilePath);
        File file = new File(url.toURI());
        instanceCollection = oleInstanceConverter.fromXML(readFile(file));
        LOG.info("entered");
        for (Instance inst : instanceCollection.getInstance()) {
            LOG.info("Instance Identifier:" + inst.getInstanceIdentifier());
            LOG.info("Resource Identifier:" + inst.getResourceIdentifier());
            LOG.info("Former Resource Identifier:" + inst.getResourceIdentifier());
            LOG.info(inst.getSourceHoldings().getHoldings().getContent().toString());
            LOG.info(inst.getItems().getItem().get(0).getAnalytic().toString());
            LOG.info(inst.getItems().getItem().get(0).getResourceIdentifier());
            LOG.info(inst.getOleHoldings().getPrimary());
        }
        String xml = oleInstanceConverter.toXML(instanceCollection);
        LOG.info("xml isssssssssss" + xml);
    }

    @Test
    public void testFromXMLAndToXmlInstanceCollection() throws Exception {
        WorkInstanceOlemlRecordProcessor oleInstanceConverter = new WorkInstanceOlemlRecordProcessor();
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


    @Test
    public void testInstanceAddAttributesFromXml() {
        URL resource = getClass().getResource(sampleXmlFilePath);
        WorkInstanceOlemlRecordProcessor oleInstanceConverter = new WorkInstanceOlemlRecordProcessor();
        try {
            File file = new File(resource.toURI());
            String instance = readFile(file);
            LOG.info("instance" + instance);
            InstanceCollection instanceCollection = oleInstanceConverter.fromXML(instance);
            LOG.info(instanceCollection.toString());

            OleHoldings oleHolding = instanceCollection.getInstance().get(0).getOleHoldings();
            Item oleItem = instanceCollection.getInstance().get(0).getItems().getItem().get(0);

            AdditionalAttributes addAttributes = (AdditionalAttributes) oleHolding.getExtension().getContent().get(0);
            LOG.info("additional attribute names that are existing" + addAttributes.getAttributeNames());
            addAttributes.setAttribute("createdBy", "HTC GLOBAL SERVICES.");
            String xml = oleInstanceConverter.toXML(instanceCollection);
            LOG.info("HOLDINGS XMl with newly added  additional attribute \n" + xml);

            addAttributes = (AdditionalAttributes) oleItem.getExtension().getContent().get(0);
            LOG.info("additional attribute names that are existing" + addAttributes.getAttributeNames());
            addAttributes.setAttribute("createdBy", "HTC GLOBAL SERVICES.");
            String itemXml = oleInstanceConverter.toXML(instanceCollection);
            LOG.info("ITEM XML with newly added  additional attribute \n" + itemXml);

            // Want to ingest this generated xml "createdBy" attribute should registered in customFileNode.cnd file in Doc-Store
            // Repository should be cleared.

        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
    }

    @Test
    public void testItemValuesFromDefaultXml() {
        URL resource = getClass().getResource("/schema/work/instance/oleml/instance9.1.1.xsd.xml");
        WorkInstanceOlemlRecordProcessor oleInstanceConverter = new WorkInstanceOlemlRecordProcessor();
        try {
            File file = new File(resource.toURI());
            String instance = readFile(file);
//            LOG.info("instance" + instance);
            InstanceCollection instanceCollection = oleInstanceConverter.fromXML(instance);
            LOG.info(instanceCollection.toString());
            Item oleItem = instanceCollection.getInstance().get(0).getItems().getItem().get(0);
            LOG.info("location level is " + oleItem.getLocation().getLocationLevel());
            instanceCollection.setInstance(null);
            Assert.assertNotNull(instanceCollection.getInstance());
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }

    }


}
