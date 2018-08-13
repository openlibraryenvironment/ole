package org.kuali.ole;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.ControlField;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.MissingPieceItemRecord;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.docstore.common.exception.DocstoreValidationException;
import org.kuali.ole.docstore.common.util.BatchBibTreeDBUtil;
import org.kuali.ole.docstore.common.util.BibMarcUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.RdbmsBibMarcDocumentManager;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord;
import org.kuali.ole.utility.Constants;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.kuali.ole.utility.callnumber.CallNumberType;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jayabharathreddy on 5/6/15.
 */
public class DocstoreTestHelper {

    private HoldingOlemlRecordProcessor workHoldingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();

    private static BibMarcRecordProcessor bibMarcRecordProcessor = null;

    private static BibMarcUtil bibMarcUtil = new BibMarcUtil();


    public static BibMarcRecordProcessor getBibMarcRecordProcessor() {
        if (bibMarcRecordProcessor == null) {
            bibMarcRecordProcessor = new BibMarcRecordProcessor();
        }
        return bibMarcRecordProcessor;
    }

    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void createBibRecord(BibTree bibTree, BibRecord bibRecord) {
        Bib bib = (Bib) bibTree.getBib();
        modifyAdditionalAttributes(bib);
        bibRecord.setContent(bib.getContent());

        bibRecord.setCreatedBy(bib.getCreatedBy());
        bibRecord.setDateCreated(getTimeStampFromString(bib.getCreatedOn()));
        bibRecord.setStatusUpdatedBy(bib.getStatusUpdatedBy());
        if (StringUtils.isNotBlank(bib.getStatusUpdatedOn())) {
            bibRecord.setStatusUpdatedDate(getTimeStampFromString(bib.getStatusUpdatedOn()));
        }
        bibRecord.setFassAddFlag(bib.isFastAdd());
        bibRecord.setFormerId("");
        bibRecord.setSuppressFromPublic(String.valueOf(bib.isPublic()));
        bibRecord.setStatus(bib.getStatus());
        bibRecord.setDateEntered(getTimeStampFromString(bib.getCreatedOn()));
        bibRecord.setStaffOnlyFlag(bib.isStaffOnly());
        bibRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.getPrefix(bib.getCategory(), bib.getType(), bib.getFormat()));
        createBibInfoRecord(bibRecord);
        if (StringUtils.isNotEmpty(bib.getId())) {
            bibRecord.setBibId(bib.getId());
        }

        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
            HoldingsRecord holdingsRecord = createHoldingsRecord((holdingsTree.getHoldings()));

            List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
            for (Item item : holdingsTree.getItems()) {
                ItemRecord itemRecord = createItemRecord(item);
                itemRecords.add(itemRecord);
            }
            holdingsRecord.setItemRecords(itemRecords);
            bibRecord.getHoldingsRecords().add(holdingsRecord);
        }

    }

    private ItemRecord createItemRecord(Item itemDocument) {
        ItemRecord itemRecord = new ItemRecord();
        String holdingsId = "";

        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item item = itemOlemlRecordProcessor.fromXML(itemDocument.getContent());

        processCallNumber(item);

        itemRecord.setStaffOnlyFlag(itemDocument.isStaffOnly());
        itemRecord.setHoldingsId(DocumentUniqueIDPrefix.getDocumentId(holdingsId));
        itemRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML);
        itemRecord.setCreatedBy(itemDocument.getCreatedBy());
        itemRecord.setCreatedDate(createdDate());
        setItemProperties(itemRecord, item);
        String content = itemOlemlRecordProcessor.toXML(item);
        return itemRecord;
    }

    protected void createBibInfoRecord(BibRecord bibRecord) {
        BibMarcRecords bibMarcRecords = getBibMarcRecordProcessor().fromXML(bibRecord.getContent());
        if (bibMarcRecords != null && bibMarcRecords.getRecords() != null && bibMarcRecords.getRecords().size() > 0) {
            Map<String, String> dataFields = bibMarcUtil.buildDataValuesForBibInfo(bibMarcRecords.getRecords().get(0));
            BibInfoRecord bibInfoRecord = new BibInfoRecord();
            bibInfoRecord.setTitle(BatchBibTreeDBUtil.truncateData(dataFields.get(BibMarcUtil.TITLE_DISPLAY), 4000));
            bibInfoRecord.setAuthor(BatchBibTreeDBUtil.truncateData(dataFields.get(BibMarcUtil.AUTHOR_DISPLAY), 4000));
            bibInfoRecord.setPublisher(BatchBibTreeDBUtil.truncateData(dataFields.get(BibMarcUtil.PUBLISHER_DISPLAY), 4000));
            String isbn = BatchBibTreeDBUtil.truncateData(dataFields.get(BibMarcUtil.ISBN_DISPLAY), 100);
            String issn = BatchBibTreeDBUtil.truncateData(dataFields.get(BibMarcUtil.ISSN_DISPLAY), 100);
            if (StringUtils.isNotEmpty(isbn)) {
                bibInfoRecord.setIsxn(isbn);
            } else {
                bibInfoRecord.setIsxn(issn);
            }
            bibRecord.setBibInfoRecord(bibInfoRecord);
        }
    }


    public HoldingsRecord createHoldingsRecord(Holdings holdings) {
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        String bibId = "";
        holdingsRecord.setCreatedBy(holdings.getCreatedBy());
        holdingsRecord.setCreatedDate(createdDate());
        String content = holdings.getContent();
        OleHoldings oleHoldings = workHoldingOlemlRecordProcessor.fromXML(content);
        holdingsRecord.setStaffOnlyFlag(holdings.isStaffOnly());
        holdingsRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(bibId));
        holdingsRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML);
        processCallNumber(oleHoldings);
        holdingsRecord.setHoldingsType(holdings.getHoldingsType());
        setHoldingsCommonInformation(oleHoldings, holdingsRecord);
        if (holdings instanceof PHoldings) {
            setPHoldingsInformation(holdingsRecord, oleHoldings);
        } else {
            setEHoldingsInformation(oleHoldings, holdingsRecord);
        }
        return holdingsRecord;
    }


    public boolean getBibIdFromBibXMLContent(BibRecord bibRecord) {
        boolean isBibIdFlag = true;
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(bibRecord.getContent());
        if (bibMarcRecords != null && bibMarcRecords.getRecords() != null && bibMarcRecords.getRecords().size() > 0) {
            BibMarcRecord bibMarcRecord = bibMarcRecords.getRecords().get(0);
            if (bibMarcRecord.getControlFields() != null) {
                for (ControlField controlField : bibMarcRecord.getControlFields()) {
                    if ("001".equals(controlField.getTag()) && validateIdField(controlField.getValue())) {
                        bibRecord.setBibId(controlField.getValue());
                        Map parentCriteria1 = new HashMap();
                        parentCriteria1.put("bibId", controlField.getValue());
                        List<BibRecord> bibRecords = (List<BibRecord>) getBusinessObjectService().findMatching(BibRecord.class, parentCriteria1);
                        if (bibRecords == null && bibRecords.size() > 0) {
                            throw new DocstoreValidationException(DocstoreResources.BIB_ID_ALREADY_EXISTS, DocstoreResources.BIB_ID_ALREADY_EXISTS);
                        }
                        isBibIdFlag = false;
                        break;
                    }
                }
            }
        }
        return isBibIdFlag;
    }


    public void modifyAdditionalAttributes(Bib bib) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        String user = bib.getCreatedBy();
        String statusFromReqDoc = "";
        statusFromReqDoc = bib.getStatus();
        bib.setCreatedOn(dateStr);
        bib.setCreatedBy(user);
        //Add statusUpdatedBy and statusUpdatedOn if input request is having non empty status field
        if (StringUtils.isNotEmpty(statusFromReqDoc)) {
            bib.setStatusUpdatedBy(user);
            bib.setStatusUpdatedOn(dateStr);
        }
        add003Field(bib);
    }

    protected void add003Field(Bib bib) {
        String content = bib.getContent();
        BibMarcRecordProcessor bibMarcRecordProcessor = RdbmsBibMarcDocumentManager.getBibMarcRecordProcessor();
        BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(content);
        ListIterator<ControlField> iterator = bibMarcRecords.getRecords().get(0).getControlFields().listIterator();
        boolean organizationCodeAvailable = false;
        String organizationCode = ConfigContext.getCurrentContextConfig().getProperty("organization.marc.code");
        String controlField003 = BibMarcRecord.TAG_003;
        while (iterator.hasNext()) {
            ControlField controlField = iterator.next();

            // Check and update to local 003
            if (controlField.getTag().equals(controlField003)) {
                if (!controlField.getValue().equals(organizationCode)) {
                    controlField.setValue(organizationCode);
                }
                organizationCodeAvailable = true;
            }
        }

        // Add local 003
        if (!organizationCodeAvailable) {
            ControlField controlField = new ControlField();
            controlField.setTag(controlField003);
            controlField.setValue(organizationCode);
            iterator.add(controlField);
        }
        content = bibMarcRecordProcessor.generateXML(bibMarcRecords.getRecords());
        bib.setContent(content);
    }

    public Timestamp getTimeStampFromString(String date) {
        DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        Timestamp createdDate = null;
        try {
            if (StringUtils.isNotBlank(date)) {
                createdDate = new Timestamp(df.parse(date).getTime());
            }
        } catch (ParseException e) {
            System.out.println("Exception : " + e);
        }
        return createdDate;
    }


    public boolean validateIdField(String bibId) {
        if (StringUtils.isNotEmpty(bibId)) {
            String idPattern = "[0-9]+";
            Matcher match = Pattern.compile(idPattern).matcher(bibId);
            return match.matches();
        }
        return false;
    }

    public Timestamp createdDate() {
        String createDateStr = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Timestamp createdDate = null;
        try {
            createdDate = new Timestamp(df.parse(createDateStr).getTime());
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
        return createdDate;
    }

    public void processCallNumber(OleHoldings oleHolding) {
        if (oleHolding != null && oleHolding.getCallNumber() != null) {
            //validateCallNumber(oleHolding.getCallNumber());
            CallNumber cNum = oleHolding.getCallNumber();
            computeCallNumberType(cNum);
            if (cNum.getNumber() != null && cNum.getNumber().trim().length() > 0) {
                //Build sortable key if a valid call number exists
                String value = "";
                value = buildSortableCallNumber(cNum.getNumber(), cNum.getShelvingScheme().getCodeValue());
                if (cNum.getShelvingOrder() == null) {
                    cNum.setShelvingOrder(new ShelvingOrder());
                }
                cNum.getShelvingOrder().setFullValue(value);
            }
        }
    }


    public void computeCallNumberType(CallNumber callNumber) {
        Set<String> validCallNumberTypeSet = CallNumberType.validCallNumberTypeCodeSet;
        if (callNumber != null) {
            if (callNumber.getShelvingScheme() != null) {
                String callNumberTypeCode = callNumber.getShelvingScheme().getCodeValue();
                String callNumberTypeName = "";
                //If call number type code is valid
                if ((StringUtils.isNotEmpty(callNumberTypeCode)) && (validCallNumberTypeSet
                        .contains(callNumberTypeCode))) {
                    callNumberTypeName = CallNumberType.valueOf(callNumberTypeCode).getDescription();
                    callNumber.getShelvingScheme().setFullValue(callNumberTypeName);
                }
            }
        }
    }

    protected String buildSortableCallNumber(String callNumber, String codeValue) {
        String shelvingOrder = "";
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
           org.solrmarc.callnum.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                shelvingOrder = callNumberObj.getShelfKey();
            }
        }
        return shelvingOrder;
    }


    public void setHoldingsCommonInformation(OleHoldings oleHoldings, HoldingsRecord holdingsRecord) {
        StringBuffer locationLevel = new StringBuffer("");
        holdingsRecord.setLocation(getLocation(oleHoldings.getLocation(), locationLevel));
        if (holdingsRecord.getStaffOnlyFlag() != null) {
            oleHoldings.setStaffOnlyFlag(holdingsRecord.getStaffOnlyFlag());
        }
        holdingsRecord.setLocationLevel(locationLevel.toString());
        if (oleHoldings.getCallNumber() != null) {
            CallNumber callNumber = oleHoldings.getCallNumber();
            holdingsRecord.setCallNumberPrefix(callNumber.getPrefix());
            holdingsRecord.setCallNumber(callNumber.getNumber());

            if (callNumber.getShelvingOrder() != null) {
                holdingsRecord.setShelvingOrder(callNumber.getShelvingOrder().getFullValue());
            }
            if (callNumber.getShelvingScheme() != null) {
                CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getShelvingScheme());
                holdingsRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());
                holdingsRecord.setCallNumberTypeRecord(callNumberTypeRecord);
            }

        }
        holdingsRecord.setContent("mock content");
        holdingsRecord.setExtentOfOwnerShipRecords(null);
        holdingsRecord.setHoldingsUriRecords(null);
        holdingsRecord.setHoldingsNoteRecords(null);
        if (oleHoldings.getCopyNumber() != null) {
            holdingsRecord.setCopyNumber(oleHoldings.getCopyNumber());
        }

        holdingsRecord.setHoldingsNoteRecords(saveHoldingNoteRecords(oleHoldings.getNote()));

    }


    protected String getLocation(Location location, StringBuffer locationLevel) {
        StringBuffer locationName = new StringBuffer("");
        if (location != null && location.getLocationLevel() != null) {
            locationName = locationName.append(location.getLocationLevel().getName());
            locationLevel = locationLevel.append(location.getLocationLevel().getLevel());

            if (location.getLocationLevel().getLocationLevel() != null) {
                locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getName());
                locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLevel());

                if (location.getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                    locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getName());
                    locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                    if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                        locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                        locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                        if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                            locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                            locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());
                        }
                    }
                }
            }
        }
        return locationName.toString();
    }

    protected CallNumberTypeRecord saveCallNumberTypeRecord(ShelvingScheme scheme) {

        Map callMap = new HashMap();
        if (scheme.getCodeValue() != null && scheme.getCodeValue().equalsIgnoreCase("none")) {
            scheme.setCodeValue("NOINFO");
        }
        callMap.put("code", scheme.getCodeValue());
        List<CallNumberTypeRecord> callNumberTypeRecords = (List<CallNumberTypeRecord>) getBusinessObjectService().findMatching(CallNumberTypeRecord.class, callMap);
        if (callNumberTypeRecords.size() == 0) {
            if (scheme.getCodeValue() != null && !"".equals(scheme.getCodeValue())) {
                CallNumberTypeRecord callNumberTypeRecord = new CallNumberTypeRecord();
                callNumberTypeRecord.setCode(scheme.getCodeValue());
                callNumberTypeRecord.setName(scheme.getFullValue());
                getBusinessObjectService().save(callNumberTypeRecord);
                return callNumberTypeRecord;
            } else
                return null;
        }
        return callNumberTypeRecords.get(0);
    }

    protected BibRecord modifyDocumentContent(Bib bib, BibRecord bibRecord) {
        String content = bib.getContent();
        if (content != null && content != "" && content.length() > 0) {
            Pattern pattern = Pattern.compile("tag=\"001\">.*?</controlfield");
            Pattern pattern2 = Pattern.compile("<controlfield.*?tag=\"001\"/>");
            Matcher matcher = pattern.matcher(content);
            Matcher matcher2 = pattern2.matcher(content);
            if (matcher.find()) {
                bib.setContent(matcher.replaceAll("tag=\"001\">" + bibRecord.getBibId() + "</controlfield"));
            } else if (matcher2.find()) {
                bib.setContent(matcher2.replaceAll("<controlfield tag=\"001\">" + bibRecord.getBibId() + "</controlfield>"));
            } else {
                int ind = content.indexOf("</leader>") + 9;
                if (ind == 8) {
                    ind = content.indexOf("<leader/>") + 9;
                    if (ind == 8) {
                        ind = content.indexOf("record>") + 7;
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append(content.substring(0, ind));
                sb.append("<controlfield tag=\"001\">");
                sb.append(bibRecord.getBibId());
                sb.append("</controlfield>");
                sb.append(content.substring(ind + 1));
                bib.setContent(sb.toString());
            }
            bibRecord.setContent(bib.getContent());

        }
        return bibRecord;
    }

    /**
     * @param extentOfOwnershipList
     */
    protected void savePHoldingsExtentOfOwnerShip(List<ExtentOfOwnership> extentOfOwnershipList) {

        if (extentOfOwnershipList != null && extentOfOwnershipList.size() > 0) {
            for (int i = 0; i < extentOfOwnershipList.size(); i++) {
                ExtentOfOwnerShipRecord extentOfOwnerShipRecord = new ExtentOfOwnerShipRecord();
                ExtentOfOwnership extentOfOwnership = extentOfOwnershipList.get(i);
                ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecord = saveExtentOfOwnerShipType(extentOfOwnership.getType());
                extentOfOwnerShipRecord.setExtOfOwnerShipTypeId(extentOfOwnerShipTypeRecord != null ? extentOfOwnerShipTypeRecord.getExtOfOwnerShipTypeId() : null);
                extentOfOwnerShipRecord.setText(extentOfOwnership.getTextualHoldings());

                extentOfOwnerShipRecord.setExtentOfOwnerShipTypeRecord(extentOfOwnerShipTypeRecord);
                extentOfOwnerShipRecord.setOrd(i + 1);
                getBusinessObjectService().save(extentOfOwnerShipRecord);
                List<Note> notes = extentOfOwnership.getNote();
                if (notes.size() > 0) {
                    extentOfOwnerShipRecord.setExtentNoteRecords(saveExtentNoteRecord(extentOfOwnerShipRecord.getExtOfOwnerShipId(), notes));
                }
            }
        }

    }

    protected ExtentOfOwnerShipTypeRecord saveExtentOfOwnerShipType(String type) {
        Map map = new HashMap();
        map.put("code", type);
        List<ExtentOfOwnerShipTypeRecord> extentOfOwnerShipTypeRecords = (List<ExtentOfOwnerShipTypeRecord>) getBusinessObjectService().findMatching(ExtentOfOwnerShipTypeRecord.class, map);
        if (extentOfOwnerShipTypeRecords.size() == 0) {
            if (type != null && !"".equals(type)) {
                ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecord = new ExtentOfOwnerShipTypeRecord();
                extentOfOwnerShipTypeRecord.setCode(type);
                extentOfOwnerShipTypeRecord.setName(type);
                return extentOfOwnerShipTypeRecord;
            } else {
                return null;
            }
        }
        return extentOfOwnerShipTypeRecords.get(0);
    }

    protected List<ExtentNoteRecord> saveExtentNoteRecord(String extOfOwnerShipID, List<Note> notes) {
        List<ExtentNoteRecord> extentNoteRecords = new ArrayList();
        if (notes != null && notes.size() > 0) {
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                if (note.getType() != null && ("public".equalsIgnoreCase(note.getType()) || "nonPublic".equalsIgnoreCase(note.getType()))) {
                    ExtentNoteRecord noteRecord = new ExtentNoteRecord();
                    noteRecord.setExtOfOwnerShipId(extOfOwnerShipID);
                    noteRecord.setType(note.getType());
                    noteRecord.setNote(note.getValue());
                    extentNoteRecords.add(noteRecord);
                }
            }
        }
        return extentNoteRecords;
    }


    protected List<HoldingsUriRecord> saveAccessUriRecord(List<Uri> uriList) {
        List<HoldingsUriRecord> holdingsUriRecordList = new ArrayList<HoldingsUriRecord>();
        if (uriList.size() > 0) {
            for (int i = 0; i < uriList.size(); i++) {
                Uri accessUri = uriList.get(i);
                if (StringUtils.isNotBlank(accessUri.getValue())) {
                    HoldingsUriRecord holdingsUriRecord = new HoldingsUriRecord();
                    Uri uri = uriList.get(i);
                    holdingsUriRecord.setText(uri.getValue());
                    holdingsUriRecordList.add(holdingsUriRecord);
                }
            }
        }
        return holdingsUriRecordList;
    }

    private void setPHoldingsInformation(HoldingsRecord holdingsRecord, OleHoldings oleHoldings) {
        if (oleHoldings.getReceiptStatus() != null) {
            ReceiptStatusRecord receiptStatusRecord = saveReceiptStatusRecord(oleHoldings.getReceiptStatus());
            holdingsRecord.setReceiptStatusId(receiptStatusRecord == null ? null : receiptStatusRecord.getReceiptStatusId());
            holdingsRecord.setReceiptStatusRecord(receiptStatusRecord);
        }

        savePHoldingsExtentOfOwnerShip(oleHoldings.getExtentOfOwnership());
        holdingsRecord.setHoldingsUriRecords(saveAccessUriRecord(oleHoldings.getUri()));
    }


    protected ReceiptStatusRecord saveReceiptStatusRecord(String receiptStatus) {
        Map map = new HashMap();
        map.put("code", receiptStatus);
        List<ReceiptStatusRecord> receiptStatusRecords = (List<ReceiptStatusRecord>) getBusinessObjectService().findMatching(ReceiptStatusRecord.class, map);
        if (receiptStatusRecords.size() == 0) {
            map = new HashMap();
            map.put("name", receiptStatus);
            receiptStatusRecords = (List<ReceiptStatusRecord>) getBusinessObjectService().findMatching(ReceiptStatusRecord.class, map);
        }
        if (receiptStatusRecords.size() == 0) {
            if (receiptStatus != null && !"".equals(receiptStatus)) {
                ReceiptStatusRecord receiptStatusRecord = new ReceiptStatusRecord();
                receiptStatusRecord.setCode(receiptStatus);
                receiptStatusRecord.setName(receiptStatus);
                return receiptStatusRecord;
            } else {
                return null;
            }
        }
        return receiptStatusRecords.get(0);
    }


    private void setEHoldingsInformation(OleHoldings oleHoldings, HoldingsRecord holdingsRecord) {

        if (oleHoldings.getHoldingsAccessInformation() != null) {
            AuthenticationTypeRecord authenticationTypeRecord = saveAuthenticationType(oleHoldings.getHoldingsAccessInformation().getAuthenticationType());
            holdingsRecord.setAuthenticationTypeId(authenticationTypeRecord.getAuthenticationTypeId());
            holdingsRecord.setAccessPassword(oleHoldings.getHoldingsAccessInformation().getAccessPassword());
            holdingsRecord.setAccessUserName(oleHoldings.getHoldingsAccessInformation().getAccessUsername());
            if (oleHoldings.getHoldingsAccessInformation() != null && StringUtils.isNotEmpty(oleHoldings.getHoldingsAccessInformation().getNumberOfSimultaneousUser())) {
                holdingsRecord.setNumberSimultaneousUsers(oleHoldings.getHoldingsAccessInformation().getNumberOfSimultaneousUser());
            }
            holdingsRecord.setProxiedResource(oleHoldings.getHoldingsAccessInformation().getProxiedResource());
        }
        holdingsRecord.setAccessStatus(oleHoldings.getAccessStatus());
        if (oleHoldings.getLink() != null) {
            saveLink(oleHoldings.getLink());
        }
        holdingsRecord.setImprint(oleHoldings.getImprint() != null ? oleHoldings.getImprint() : "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Timestamp statusDate = null;
        try {
            if (oleHoldings.getStatusDate() != null) {
                statusDate = new Timestamp(sdf.parse(oleHoldings.getStatusDate()).getTime());

            }
        } catch (Exception e) {
            // LOG.error("Exception : ", e);
        }
        holdingsRecord.setStatusDate(statusDate);

        holdingsRecord.seteResourceId(oleHoldings.getEResourceId());
        holdingsRecord.setPublisherId(oleHoldings.getPublisher());
        holdingsRecord.setLocalPersistentUri(oleHoldings.getLocalPersistentLink());

        holdingsRecord.setAllowIll(oleHoldings.isInterLibraryLoanAllowed());
        holdingsRecord.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
        if (oleHoldings.getPlatform() != null) {
            holdingsRecord.setPlatform(oleHoldings.getPlatform().getPlatformName());
            holdingsRecord.setAdminUrl(oleHoldings.getPlatform().getAdminUrl());
            holdingsRecord.setAdminPassword(oleHoldings.getPlatform().getAdminPassword());
            holdingsRecord.setAdminUserName(oleHoldings.getPlatform().getAdminUserName());
        }

        if (oleHoldings.getStatisticalSearchingCode() != null) {
            HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord = saveHoldingsStatisticalSearchCode(oleHoldings.getStatisticalSearchingCode());
        }
        if (oleHoldings.getExtentOfOwnership() != null) {
            saveEHoldingsExtentOfOwnerShip(oleHoldings.getExtentOfOwnership());
        }
        if (oleHoldings.getHoldingsAccessInformation() != null && oleHoldings.getHoldingsAccessInformation().getAccessLocation() != null) {
            HoldingsAccessLocation holdingsAccessLocation = saveHoldingsAccessLocation(oleHoldings.getHoldingsAccessInformation().getAccessLocation());

        }
        if (oleHoldings.getDonorInfo() != null && oleHoldings.getDonorInfo().size() >= 0) {
            saveDonorList(oleHoldings.getDonorInfo());
        }

    }


    private AuthenticationTypeRecord saveAuthenticationType(String authenticationType) {
        Map map = new HashMap();
        map.put("code", authenticationType);
        AuthenticationTypeRecord authenticationTypeRecord = getBusinessObjectService().findByPrimaryKey(AuthenticationTypeRecord.class, map);

        if (authenticationTypeRecord == null) {
            authenticationTypeRecord = new AuthenticationTypeRecord();
            authenticationTypeRecord.setCode(authenticationType);
            authenticationTypeRecord.setName(authenticationType);
        }
        return authenticationTypeRecord;
    }


    private void saveLink(List<Link> links) {

        List<HoldingsUriRecord> holdingsUriRecords = new ArrayList();

        for (Link link : links) {
            if (StringUtils.isNotBlank(link.getText()) || StringUtils.isNotBlank(link.getUrl())) {
                HoldingsUriRecord holdingsUriRecord = new HoldingsUriRecord();
                holdingsUriRecord.setText(link.getText());
                holdingsUriRecord.setUri(link.getUrl());
                holdingsUriRecords.add(holdingsUriRecord);
            }

        }
    }

    private HoldingsStatisticalSearchRecord saveHoldingsStatisticalSearchCode(StatisticalSearchingCode statisticalSearchingCode) {
        if (StringUtils.isNotEmpty(statisticalSearchingCode.getCodeValue())) {
            StatisticalSearchRecord statisticalSearchRecord = saveStatisticalSearchRecord(statisticalSearchingCode);
            HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord = null;

            if (statisticalSearchRecord != null) {
                holdingsStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
            }

            return holdingsStatisticalSearchRecord;
        }
        return null;
    }


    protected StatisticalSearchRecord saveStatisticalSearchRecord(StatisticalSearchingCode statisticalSearchingCode) {
        Map map = new HashMap();
        map.put("code", statisticalSearchingCode.getCodeValue());
        List<StatisticalSearchRecord> statisticalSearchRecords = (List<StatisticalSearchRecord>) getBusinessObjectService().findMatching(StatisticalSearchRecord.class, map);
        if (statisticalSearchRecords.size() == 0) {
            if (statisticalSearchingCode.getCodeValue() != null && !"".equals(statisticalSearchingCode.getCodeValue())) {
                StatisticalSearchRecord statisticalSearchRecord = new StatisticalSearchRecord();
                statisticalSearchRecord.setCode(statisticalSearchingCode.getCodeValue());
                statisticalSearchRecord.setName(statisticalSearchingCode.getFullValue());
                getBusinessObjectService().save(statisticalSearchRecord);
                return statisticalSearchRecord;
            } else {
                return null;
            }
        } else {
            return statisticalSearchRecords.get(0);
        }
    }

    private void saveEHoldingsExtentOfOwnerShip(List<ExtentOfOwnership> extentOfOwnerships) {
        if (extentOfOwnerships != null && extentOfOwnerships.size() > 0) {
            ExtentOfOwnership extentOfOwnership = extentOfOwnerships.get(0);
            if (extentOfOwnership.getCoverages() != null) {
                saveCoverageRecord(extentOfOwnership.getCoverages());
            }
            if (extentOfOwnership.getPerpetualAccesses() != null) {
                savePerpetualAccessRecord(extentOfOwnership.getPerpetualAccesses());
            }
        }

    }


    private void saveCoverageRecord(Coverages coverages) {

        List<EInstanceCoverageRecord> coverageList = new ArrayList();
        for (Coverage coverage : coverages.getCoverage()) {
            if (StringUtils.isNotBlank(coverage.getCoverageStartDateFormat()) || StringUtils.isNotBlank(coverage.getCoverageStartDateString()) || StringUtils.isNotBlank(coverage.getCoverageStartVolume())
                    || StringUtils.isNotBlank(coverage.getCoverageStartIssue()) || StringUtils.isNotBlank(coverage.getCoverageEndDateFormat()) || StringUtils.isNotBlank(coverage.getCoverageEndDateString())
                    || StringUtils.isNotBlank(coverage.getCoverageEndVolume()) || StringUtils.isNotBlank(coverage.getCoverageEndIssue())) {
                EInstanceCoverageRecord eInstanceCoverageRecord = new EInstanceCoverageRecord();
                if (StringUtils.isNotBlank(coverage.getCoverageStartDateFormat()) || StringUtils.isNotBlank(coverage.getCoverageStartDateString()) || StringUtils.isNotBlank(coverage.getCoverageStartVolume())
                        || StringUtils.isNotBlank(coverage.getCoverageStartIssue())) {
                    if (StringUtils.isNotEmpty(coverage.getCoverageStartDate())) {
                        eInstanceCoverageRecord.setCoverageStartDate(coverage.getCoverageStartDate());
                    }
                    eInstanceCoverageRecord.setCoverageStartVolume(coverage.getCoverageStartVolume());
                    eInstanceCoverageRecord.setCoverageStartIssue(coverage.getCoverageStartIssue());
                }
                if (StringUtils.isNotBlank(coverage.getCoverageEndDateFormat()) || StringUtils.isNotBlank(coverage.getCoverageEndDateString())
                        || StringUtils.isNotBlank(coverage.getCoverageEndVolume()) || StringUtils.isNotBlank(coverage.getCoverageEndIssue())) {

                    if (StringUtils.isNotEmpty(coverage.getCoverageEndDate())) {
                        eInstanceCoverageRecord.setCoverageEndDate(coverage.getCoverageEndDate());
                    }
                    eInstanceCoverageRecord.setCoverageEndVolume(coverage.getCoverageEndVolume());
                    eInstanceCoverageRecord.setCoverageEndIssue(coverage.getCoverageEndIssue());
                }
                coverageList.add(eInstanceCoverageRecord);
            }
        }

    }


    private void savePerpetualAccessRecord(PerpetualAccesses perpetualAccesses) {


        List<EInstancePerpetualAccessRecord> perpetualAccessList = new ArrayList();
        for (PerpetualAccess perpetualAccess : perpetualAccesses.getPerpetualAccess()) {
            if (StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDateFormat()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDateString()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartVolume())
                    || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartIssue()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDateFormat()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDateString())
                    || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndVolume()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndIssue())) {
                EInstancePerpetualAccessRecord eInstancePerpetualAccess = new EInstancePerpetualAccessRecord();

                if (StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDateFormat()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDateString()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartVolume())
                        || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartIssue())) {
                    if (StringUtils.isNotEmpty(perpetualAccess.getPerpetualAccessStartDate())) {
                        eInstancePerpetualAccess.setPerpetualAccessStartDate(perpetualAccess.getPerpetualAccessStartDate());
                    }
                    eInstancePerpetualAccess.setPerpetualAccessStartVolume(perpetualAccess.getPerpetualAccessStartVolume());
                    eInstancePerpetualAccess.setPerpetualAccessStartIssue(perpetualAccess.getPerpetualAccessStartIssue());
                }
                if (StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDateFormat()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDateString())
                        || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndVolume()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndIssue())) {
                    if (StringUtils.isNotEmpty(perpetualAccess.getPerpetualAccessEndDate())) {
                        eInstancePerpetualAccess.setPerpetualAccessEndDate(perpetualAccess.getPerpetualAccessEndDate());
                    }
                    eInstancePerpetualAccess.setPerpetualAccessEndVolume(perpetualAccess.getPerpetualAccessEndVolume());
                    eInstancePerpetualAccess.setPerpetualAccessEndIssue(perpetualAccess.getPerpetualAccessEndIssue());
                }
                perpetualAccessList.add(eInstancePerpetualAccess);
            }
        }

    }

    private HoldingsAccessLocation saveHoldingsAccessLocation(String accessLocation) {

        HoldingsAccessLocation holdingsAccessLocation = new HoldingsAccessLocation();
        AccessLocation accessLocation1 = saveAccessLocation(accessLocation);
        holdingsAccessLocation.setAccessLocationId(accessLocation1.getAccessLocationId());
        return holdingsAccessLocation;
    }


    private AccessLocation saveAccessLocation(String accessLocation) {
        Map map = new HashMap();
        map.put("code", accessLocation);
        AccessLocation accessLocation1 = getBusinessObjectService().findByPrimaryKey(AccessLocation.class, map);

        if (accessLocation1 == null) {
            accessLocation1 = new AccessLocation();
            accessLocation1.setCode(accessLocation);
            getBusinessObjectService().save(accessLocation1);
        }

        return accessLocation1;
    }

    protected void saveDonorList(List<DonorInfo> donorslist) {
        if (donorslist.size() > 0) {
            List<OLEHoldingsDonorRecord> oleHoldingsDonorRecords = new ArrayList<OLEHoldingsDonorRecord>();
            for (int i = 0; i < donorslist.size(); i++) {
                DonorInfo donorinfo = donorslist.get(i);
                if (donorinfo.getDonorCode() != null) {
                    OLEHoldingsDonorRecord oleHoldingsDonorRecord = new OLEHoldingsDonorRecord();
                    oleHoldingsDonorRecord.setDonorPublicDisplay(donorinfo.getDonorPublicDisplay());
                    oleHoldingsDonorRecord.setDonorCode(donorinfo.getDonorCode());
                    oleHoldingsDonorRecord.setDonorNote(donorinfo.getDonorNote());
                    oleHoldingsDonorRecords.add(oleHoldingsDonorRecord);
                }
            }

        }
    }


    protected List<HoldingsNoteRecord> saveHoldingNoteRecords(List<Note> noteList) {

        List<HoldingsNoteRecord> holdingsNoteRecordList = new ArrayList<HoldingsNoteRecord>();
        ;
        if (noteList.size() > 0) {
            for (int i = 0; i < noteList.size(); i++) {
                Note note = noteList.get(i);
                if (StringUtils.isNotBlank(note.getValue())) {
                    if (note.getType() != null && ("public".equalsIgnoreCase(note.getType()) || "nonPublic".equalsIgnoreCase(note.getType()))) {
                        HoldingsNoteRecord holdingsNoteRecord = new HoldingsNoteRecord();
                        holdingsNoteRecord.setType(note.getType());
                        holdingsNoteRecord.setNote(note.getValue());
                        holdingsNoteRecordList.add(holdingsNoteRecord);
                    }
                }

            }
        }
        return holdingsNoteRecordList;
    }


    protected void processCallNumber(org.kuali.ole.docstore.common.document.content.instance.Item itemDocument) {
        if (itemDocument != null && itemDocument.getCallNumber() != null) {
            validateCallNumber(itemDocument.getCallNumber());
            CallNumber cNum = itemDocument.getCallNumber();
            computeCallNumberType(cNum);
            if (cNum.getNumber() != null && cNum.getNumber().trim().length() > 0) {
                //Build sortable key if a valid call number exists
                String callNumber = cNum.getNumber();
                callNumber = appendItemInfoToCallNumber(itemDocument, callNumber);
                String value = "";
                value = buildSortableCallNumber(callNumber, cNum.getShelvingScheme().getCodeValue());
                if (cNum.getShelvingOrder() == null) {
                    cNum.setShelvingOrder(new ShelvingOrder());
                }
                cNum.getShelvingOrder().setFullValue(value);
            }
        }
    }

    private String appendItemInfoToCallNumber(org.kuali.ole.docstore.common.document.content.instance.Item item, String callNumber) {
        if (item.getEnumeration() != null && item.getEnumeration().trim().length() > 0) {
            callNumber = callNumber + " " + item.getEnumeration().trim();
        }
        if (item.getChronology() != null && item.getChronology().trim().length() > 0) {
            callNumber = callNumber + " " + item.getChronology().trim();

        }
        if (item.getCopyNumber() != null && item.getCopyNumber().trim().length() > 0) {
            callNumber = callNumber + " " + item.getCopyNumber().trim();
        }
        return callNumber;
    }

    protected void validateCallNumber(CallNumber cNum) {
        validateCNumNCNumType(cNum);
        validateShelvingOrderNCNum(cNum);
    }

    private void validateShelvingOrderNCNum(CallNumber cNum) {
        if (cNum.getShelvingOrder() != null && cNum.getShelvingOrder().getFullValue() != null &&
                cNum.getShelvingOrder().getFullValue().trim().length() > 0) {
            if (!(cNum.getNumber() != null && cNum.getNumber().length() > 0)) {
                throw new DocstoreValidationException(DocstoreResources.CALL_NUMBER_INFO, DocstoreResources.CALL_NUMBER_INFO);
            }
        }
    }

    private void validateCNumNCNumType(CallNumber cNum) {
        String callNumber = "";
        String callNumberType = "";
        // Get callNumber and callNumberType
        if (cNum != null) {
            callNumber = cNum.getNumber();
            if (cNum.getShelvingScheme() != null) {
                callNumberType = cNum.getShelvingScheme().getCodeValue();
            }
        }
        // Check if CallNumber is present
        if (StringUtils.isNotBlank(callNumber)) {
            // Check if callNumberType is empty or #
            if ((callNumberType == null) || (callNumberType.length() == 0) || callNumber.equalsIgnoreCase("none")) {
                cNum.getShelvingScheme().setCodeValue("NOINFO");
            }
        }
    }


    private void setItemProperties(ItemRecord itemRecord, org.kuali.ole.docstore.common.document.content.instance.Item item) {
        List<org.kuali.ole.docstore.common.document.content.instance.MissingPieceItemRecord> missingPieceItemRecordsList = item.getMissingPieceItemRecordList();
        List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> itemMissingPieceRecordList = new ArrayList();

        itemRecord.setBarCodeArsl(item.getBarcodeARSL());
        saveMissingPieceRecord(itemRecord, item, missingPieceItemRecordsList, itemMissingPieceRecordList);

        if (item.getCallNumber() != null) {

            CallNumber callNumber = item.getCallNumber();
            itemRecord.setCallNumberPrefix(callNumber.getPrefix());
            itemRecord.setCallNumber(callNumber.getNumber());
            if (callNumber.getShelvingOrder() != null) {
                itemRecord.setShelvingOrder(callNumber.getShelvingOrder().getFullValue());
            }
            if (callNumber.getShelvingScheme() != null) {
                CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getShelvingScheme());
                itemRecord.setCallNumberTypeRecord(callNumberTypeRecord);
                itemRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());
            }
        }
        if (item.getAccessInformation() != null) {
            itemRecord.setBarCode(item.getAccessInformation().getBarcode());
        }
        if (item.getItemType() != null) {
            ItemTypeRecord itemTypeRecord = saveItemTypeRecord(item.getItemType());
            itemRecord.setItemTypeId(itemTypeRecord == null ? null : itemTypeRecord.getItemTypeId());
            itemRecord.setItemTypeRecord(itemTypeRecord);
        }
        if (item.getTemporaryItemType() != null) {
            ItemTypeRecord tempItemTypeRecord = saveItemTypeRecord(item.getTemporaryItemType());
            itemRecord.setTempItemTypeId(tempItemTypeRecord == null ? null : tempItemTypeRecord.getItemTypeId());
            itemRecord.setItemTempTypeRecord(tempItemTypeRecord);
        }
        itemRecord.setChronology(item.getChronology());
        itemRecord.setCopyNumber(item.getCopyNumber());
        itemRecord.setEnumeration(item.getEnumeration());

        itemRecord.setNumberOfPieces(item.getNumberOfPieces());
        itemRecord.setPurchaseOrderItemLineId(item.getPurchaseOrderLineItemIdentifier());
        itemRecord.setVendorLineItemId(item.getVendorLineItemIdentifier());
        itemRecord.setFund(item.getFund());
        if (StringUtils.isNotEmpty(item.getPrice())) {
            itemRecord.setPrice(item.getPrice());
        }
        itemRecord.setCheckInNote(item.getCheckinNote());
        itemRecord.setFastAddFlag(item.isFastAddFlag() ? Boolean.TRUE : Boolean.FALSE);

        itemRecord.setClaimsReturnedFlag(item.isClaimsReturnedFlag());


        String claimsReturnDate = item.getClaimsReturnedFlagCreateDate();
        if (claimsReturnDate != null) {
            String[] claimsReturnDateArray = claimsReturnDate.split(" ");
            if (claimsReturnDateArray.length == 1 && claimsReturnDateArray[0] != "") {
                claimsReturnDate = claimsReturnDate + " 00:00:00";
                claimsReturnsCreateDateItem(item, itemRecord, claimsReturnDate);
            } else if (claimsReturnDateArray.length > 1) {
                claimsReturnsCreateDateItem(item, itemRecord, claimsReturnDate);
            } else {
                itemRecord.setClaimsReturnedFlagCreateDate(null);
            }
        } else {
            itemRecord.setClaimsReturnedFlagCreateDate(null);
        }

        saveItemsClaimsRecord(itemRecord, item);

        String dueDateItem = item.getDueDateTime();
        if (dueDateItem != null) {
            //Timestamp timestamp = new Timestamp(item.getDueDateTime().toGregorianCalendar().getTimeInMillis());
            //itemRecord.setDueDateTime(timestamp);
            String[] dueDateItemArray = dueDateItem.split(" ");
            if (dueDateItemArray.length == 1 && dueDateItemArray[0] != "") {
                dueDateItem = dueDateItem + " 00:00:00";
                dueDateTime(item, itemRecord, dueDateItem);
            } else if (dueDateItemArray.length > 1) {
                dueDateTime(item, itemRecord, dueDateItem);
            } else {
                itemRecord.setDueDateTime(null);
            }
        } else {
            itemRecord.setDueDateTime(null);
        }
        String checkOutDateItem = item.getCheckOutDateTime();
        if (checkOutDateItem != null) {
            String[] dueDateItemArray = checkOutDateItem.split(" ");
            if (dueDateItemArray.length == 1 && dueDateItemArray[0] != "") {
                checkOutDateItem = checkOutDateItem + " 00:00:00";
                itemRecord.setCheckOutDateTime(convertDateToTimeStamp(checkOutDateItem));
            } else if (dueDateItemArray.length > 1) {
                itemRecord.setCheckOutDateTime(convertDateToTimeStamp(checkOutDateItem));
            } else {
                itemRecord.setCheckOutDateTime(null);
            }
        } else {
            itemRecord.setCheckOutDateTime(null);
        }

        itemRecord.setClaimsReturnedNote(item.getClaimsReturnedNote());
        itemRecord.setProxyBorrower(item.getProxyBorrower());
        itemRecord.setCurrentBorrower(item.getCurrentBorrower());

        saveItemDamagedRecord(itemRecord, item);

        itemRecord.setDamagedItemNote(item.getDamagedItemNote());
        itemRecord.setItemDamagedStatus(item.isItemDamagedStatus());
        itemRecord.setFastAddFlag(item.isFastAddFlag());
        String effectiveDateForItem = item.getItemStatusEffectiveDate();
        if (effectiveDateForItem != null) {
            String[] effectiveDateForItemArray = effectiveDateForItem.split(" ");
            if (effectiveDateForItemArray.length == 1 && effectiveDateForItemArray[0] != "") {
                effectiveDateForItem = effectiveDateForItem + " 00:00:00";
                effectiveDateItem(item, itemRecord, effectiveDateForItem);
            } else if (effectiveDateForItemArray.length > 1) {
                effectiveDateItem(item, itemRecord, effectiveDateForItem);
            } else {
                itemRecord.setEffectiveDate(null);
            }
        } else {
            itemRecord.setEffectiveDate(null);
        }

        if (item.getItemStatus() != null) {
            ItemStatusRecord itemStatusRecord = saveItemStatusRecord(item.getItemStatus().getCodeValue());
            itemRecord.setItemStatusId(itemStatusRecord == null ? null : itemStatusRecord.getItemStatusId());
            itemRecord.setItemStatusRecord(itemStatusRecord);
        }
        StringBuffer locationLevel = new StringBuffer("");
        itemRecord.setLocation(getLocation(item.getLocation(), locationLevel));
        itemRecord.setLocationLevel(locationLevel.toString());
        //itemRecord.setFormerIdentifierRecords(null);
        itemRecord.setLocationsCheckinCountRecords(null);
        itemRecord.setItemNoteRecords(null);
        if (item.getAccessInformation() != null && item.getAccessInformation().getUri() != null) {
            itemRecord.setUri(item.getAccessInformation().getUri().getValue());
        }
        if (item.isMissingPieceFlag()) {
            if (item.getMissingPieceEffectiveDate() != null && !item.getMissingPieceEffectiveDate().equalsIgnoreCase("")) {
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date parsedDate = null;
                try {
                    parsedDate = df.parse(item.getMissingPieceEffectiveDate());
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                Timestamp timestamp = new Timestamp(parsedDate.getTime());
                itemRecord.setMissingPieceEffectiveDate(timestamp);
            } else {
                Timestamp timestamp = new Timestamp((new Date()).getTime());
                itemRecord.setMissingPieceEffectiveDate(timestamp);
            }
            if (item.getMissingPiecesCount() != null) {
                itemRecord.setMissingPiecesCount(item.getMissingPiecesCount());
            }
            itemRecord.setMissingPieceFlagNote(item.getMissingPieceFlagNote());
        }
        itemRecord.setMissingPieceFlag(item.isMissingPieceFlag());


        if (item.getStatisticalSearchingCode() != null) {
            ItemStatisticalSearchRecord itemStatisticalSearchRecord = saveItemStatisticalSearchCodeItem(item.getStatisticalSearchingCode());
            List<ItemStatisticalSearchRecord> statisticalSearchRecords = new ArrayList();
            statisticalSearchRecords.add(itemStatisticalSearchRecord);
            itemRecord.setItemStatisticalSearchRecords((statisticalSearchRecords));
        }
        if (item.getFormerIdentifier() != null && item.getFormerIdentifier().size() > 0 && item.getFormerIdentifier().get(0).getIdentifier() != null) {
            saveFormerIdentifierRecords(item.getFormerIdentifier());
        }
        if (item.getNote() != null && item.getNote().size() > 0) {
            itemRecord.setItemNoteRecords(saveItemNoteRecord(item.getNote()));
        }
        if (item.getNumberOfCirculations() != null && item.getNumberOfCirculations().getCheckInLocation() != null && item.getNumberOfCirculations().getCheckInLocation().size() > 0) {
            saveCheckInLocationRecord(item.getNumberOfCirculations().getCheckInLocation());
        }
        if (item.getDonorInfo() != null && item.getDonorInfo().size() >= 0) {
            saveDonorListItem(item.getDonorInfo());
        }
        itemRecord.setNumberOfRenew(item.getNumberOfRenew());

    }

    private void saveItemDamagedRecord(ItemRecord itemRecord, org.kuali.ole.docstore.common.document.content.instance.Item item) {
        List<org.kuali.ole.docstore.common.document.content.instance.ItemDamagedRecord> itemDamagedRecordList = item.getItemDamagedRecords();
        List<ItemDamagedRecord> itemDamagedRecords = new ArrayList();
        if (!CollectionUtils.isEmpty(itemDamagedRecordList) && item.isItemDamagedStatus()) {

            for (org.kuali.ole.docstore.common.document.content.instance.ItemDamagedRecord itemDamagedRecord : itemDamagedRecordList) {
                ItemDamagedRecord damagedRecord = new ItemDamagedRecord();
                damagedRecord.setDamagedItemNote(itemDamagedRecord.getDamagedItemNote());
                damagedRecord.setPatronBarcode(itemDamagedRecord.getPatronBarcode());
                damagedRecord.setOperatorId(itemDamagedRecord.getOperatorId());
                String itemDamagedDate = itemDamagedRecord.getDamagedItemDate();
                if (itemDamagedDate != null && !itemDamagedDate.equalsIgnoreCase("")) {
                    String[] itemClaimsReturnDateArray = itemDamagedDate.split(" ");
                    if (itemClaimsReturnDateArray.length == 1 && itemClaimsReturnDateArray[0] != "") {
                        itemDamagedDate = itemDamagedDate + " 00:00:00";
                        itemDamagedCreateDateTime(itemDamagedRecord, damagedRecord, itemDamagedDate);
                    } else if (itemClaimsReturnDateArray.length > 1) {
                        itemDamagedCreateDateTime(itemDamagedRecord, damagedRecord, itemDamagedDate);
                    } else {
                        damagedRecord.setDamagedItemDate(null);
                    }
                } else {
                    damagedRecord.setDamagedItemDate(null);
                }
                itemDamagedRecords.add(damagedRecord);
            }
            if (itemDamagedRecords != null && itemDamagedRecords.size() > 0) {
                itemRecord.setItemDamagedRecords(itemDamagedRecords);
            }
        }
    }

    private void saveItemsClaimsRecord(ItemRecord itemRecord, org.kuali.ole.docstore.common.document.content.instance.Item item) {
        List<org.kuali.ole.docstore.common.document.content.instance.ItemClaimsReturnedRecord> itemClaimsReturnedRecordList = item.getItemClaimsReturnedRecords();
        List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords = new ArrayList();
        if (!CollectionUtils.isEmpty(itemClaimsReturnedRecordList) && item.isClaimsReturnedFlag()) {
            for (org.kuali.ole.docstore.common.document.content.instance.ItemClaimsReturnedRecord itemClaimsReturnedRecord : itemClaimsReturnedRecordList) {
                ItemClaimsReturnedRecord claimsReturnedRecord = new ItemClaimsReturnedRecord();
                claimsReturnedRecord.setClaimsReturnedNote(itemClaimsReturnedRecord.getClaimsReturnedNote());
                claimsReturnedRecord.setClaimsReturnedPatronBarcode(itemClaimsReturnedRecord.getClaimsReturnedPatronBarcode());
                claimsReturnedRecord.setClaimsReturnedOperatorId(itemClaimsReturnedRecord.getClaimsReturnedOperatorId());
                String itemClaimsReturnDate = itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate();
                if (itemClaimsReturnDate != null && !itemClaimsReturnDate.equalsIgnoreCase("")) {
                    String[] itemClaimsReturnDateArray = itemClaimsReturnDate.split(" ");
                    if (itemClaimsReturnDateArray.length == 1 && itemClaimsReturnDateArray[0] != "") {
                        itemClaimsReturnDate = itemClaimsReturnDate + " 00:00:00";
                        itemClaimsReturnsCreateDateTime(itemClaimsReturnedRecord, claimsReturnedRecord, itemClaimsReturnDate);
                    } else if (itemClaimsReturnDateArray.length > 1) {
                        itemClaimsReturnsCreateDateTime(itemClaimsReturnedRecord, claimsReturnedRecord, itemClaimsReturnDate);
                    } else {
                        claimsReturnedRecord.setClaimsReturnedFlagCreateDate(null);
                    }
                } else {
                    claimsReturnedRecord.setClaimsReturnedFlagCreateDate(null);
                }
                itemClaimsReturnedRecords.add(claimsReturnedRecord);
            }
            if (itemClaimsReturnedRecords != null && itemClaimsReturnedRecords.size() > 0) {
                itemRecord.setItemClaimsReturnedRecords(itemClaimsReturnedRecords);
            }
        }
    }

    private void saveMissingPieceRecord(ItemRecord itemRecord, org.kuali.ole.docstore.common.document.content.instance.Item item, List<MissingPieceItemRecord> missingPieceItemRecordsList, List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> itemMissingPieceRecordList) {
        if (item.isMissingPieceFlag()) {
            if (CollectionUtils.isNotEmpty(missingPieceItemRecordsList)) {

                for (MissingPieceItemRecord missingPieceItemRecord : missingPieceItemRecordsList) {
                    org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord missingPieceRecord = new org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord();
                    missingPieceRecord.setMissingPieceFlagNote(missingPieceItemRecord.getMissingPieceFlagNote());
                    missingPieceRecord.setMissingPieceCount(missingPieceItemRecord.getMissingPieceCount());
                    missingPieceRecord.setPatronBarcode(missingPieceItemRecord.getPatronBarcode());
                    missingPieceRecord.setOperatorId(missingPieceItemRecord.getOperatorId());
                    missingPieceRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(missingPieceItemRecord.getItemId()));
                    if (missingPieceItemRecord.getMissingPieceDate() != null && !missingPieceItemRecord.getMissingPieceDate().equalsIgnoreCase("")) {
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        Date parsedDate = null;
                        try {
                            parsedDate = df.parse(missingPieceItemRecord.getMissingPieceDate());
                        } catch (ParseException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        Timestamp timestamp = new Timestamp(parsedDate.getTime());
                        missingPieceRecord.setMissingPieceDate(timestamp);
                    } else {
                        Timestamp timestamp = new Timestamp((new Date()).getTime());
                        missingPieceRecord.setMissingPieceDate(timestamp);
                    }
                    if (item.getMissingPiecesCount() != null) {
                        missingPieceRecord.setMissingPieceCount(missingPieceItemRecord.getMissingPieceCount());
                    }
                    if (missingPieceRecord != null) {
                        itemMissingPieceRecordList.add(missingPieceRecord);
                    }
                }
                if (itemMissingPieceRecordList != null) {
                    itemRecord.setMissingPieceItemRecordList(itemMissingPieceRecordList);
                }

            }

        }
    }

    protected ItemTypeRecord saveItemTypeRecord(ItemType itemType) {
        Map map = new HashMap();
        map.put("code", itemType.getCodeValue());
        List<ItemTypeRecord> itemTypeRecords = (List<ItemTypeRecord>) getBusinessObjectService().findMatching(ItemTypeRecord.class, map);
        if (itemTypeRecords.size() == 0) {
            if (itemType.getCodeValue() != null && !"".equals(itemType.getCodeValue())) {
                ItemTypeRecord itemTypeRecord = new ItemTypeRecord();
                itemTypeRecord.setCode(itemType.getCodeValue());
                itemTypeRecord.setName(itemType.getFullValue());
                getBusinessObjectService().save(itemTypeRecord);
                return itemTypeRecord;
            } else {
                return null;
            }
        }
        return itemTypeRecords.get(0);

    }


    private void claimsReturnsCreateDateItem(org.kuali.ole.docstore.common.document.content.instance.Item item, ItemRecord itemRecord, String claimReturnCreateDate) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Timestamp claimReturnCreateDate1 = null;
        try {
            if (!"".equals(item.getClaimsReturnedFlagCreateDate()) && item.getClaimsReturnedFlagCreateDate() != null) {
                claimReturnCreateDate1 = new Timestamp(df.parse(claimReturnCreateDate).getTime());
                itemRecord.setClaimsReturnedFlagCreateDate(claimReturnCreateDate1);
            }
        } catch (Exception e) {
            //LOG.error("Effective Date for Item" + e);
        }
    }

    //This method is for setting the date value for claimsReturnedFlagCreateDate in ItemClaimsReturnedRecord class
    private void itemClaimsReturnsCreateDateTime(org.kuali.ole.docstore.common.document.content.instance.ItemClaimsReturnedRecord itemClaimsReturnedRecord, ItemClaimsReturnedRecord claimsReturnedRecord, String claimReturnCreateDate) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Timestamp claimReturnCreateDate1 = null;
        try {
            if (!"".equals(itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate()) && itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate() != null) {
                claimReturnCreateDate1 = new Timestamp(df.parse(claimReturnCreateDate).getTime());
                claimsReturnedRecord.setClaimsReturnedFlagCreateDate(claimReturnCreateDate1);
            }
        } catch (Exception e) {
            //   LOG.error("Claims Returned Date for Item" + e);
        }
    }

    private ItemStatisticalSearchRecord saveItemStatisticalSearchCode(List<StatisticalSearchingCode> statisticalSearchingCodes) {
        if (statisticalSearchingCodes != null && statisticalSearchingCodes.size() > 0) {
            StatisticalSearchRecord statisticalSearchRecord = saveStatisticalSearchRecordItem(statisticalSearchingCodes);
            List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords = new ArrayList<ItemStatisticalSearchRecord>();
            ItemStatisticalSearchRecord itemStatisticalSearchRecord = null;

            if (itemStatisticalSearchRecords != null && itemStatisticalSearchRecords.size() > 0) {
                itemStatisticalSearchRecord = itemStatisticalSearchRecords.get(0);
            } else {
                itemStatisticalSearchRecord = new ItemStatisticalSearchRecord();
            }
            if (statisticalSearchRecord != null) {
                itemStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
                itemStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);
            }
            return itemStatisticalSearchRecord;
        }
        return null;
    }

    protected ItemStatusRecord saveItemStatusRecord(String itemStatus) {
        Map map = new HashMap();
        map.put("code", itemStatus);
        List<ItemStatusRecord> itemStatusRecords = (List<ItemStatusRecord>) getBusinessObjectService().findMatching(ItemStatusRecord.class, map);
        if (itemStatusRecords.size() == 0) {
            if (itemStatus != null && !"".equals(itemStatus)) {
                ItemStatusRecord itemStatusRecord = new ItemStatusRecord();
                itemStatusRecord.setCode(itemStatus);
                itemStatusRecord.setName(itemStatus);
                getBusinessObjectService().save(itemStatusRecord);
                return itemStatusRecord;
            } else {
                return null;
            }
        }
        return itemStatusRecords.get(0);
    }

    protected void saveFormerIdentifierRecords(List<FormerIdentifier> formerIdentifierList) {
        List<FormerIdentifierRecord> formerIdentifierRecordList = new ArrayList<FormerIdentifierRecord>();
        if (formerIdentifierList.size() > 0) {
            List<FormerIdentifierRecord> formerIdentifierRecords = new ArrayList<FormerIdentifierRecord>();
            for (int i = 0; i < formerIdentifierList.size(); i++) {
                FormerIdentifier formerIdentifier = formerIdentifierList.get(i);
                if (formerIdentifier.getIdentifier() != null && formerIdentifier.getIdentifier().getIdentifierValue() != null && !"".equals(formerIdentifier.getIdentifier().getIdentifierValue())) {

                    FormerIdentifierRecord formerIdentifierRecord = new FormerIdentifierRecord();
                    if (i < formerIdentifierRecordList.size()) {
                        formerIdentifierRecord = formerIdentifierRecordList.get(i);
                    }
                    formerIdentifierRecord.setType(formerIdentifier.getIdentifierType());
                    if (formerIdentifier.getIdentifier() != null)
                        formerIdentifierRecord.setValue(formerIdentifier.getIdentifier().getIdentifierValue());
                    formerIdentifierRecords.add(formerIdentifierRecord);
                }
            }


            if (formerIdentifierRecordList.size() > formerIdentifierList.size()) {
                getBusinessObjectService().delete(formerIdentifierRecordList.subList(formerIdentifierList.size() - 1, formerIdentifierRecordList.size()));
            }
        }
    }

    private void dueDateTime(org.kuali.ole.docstore.common.document.content.instance.Item item, ItemRecord itemRecord, String dueDateTime) {
        Timestamp dueDateTime1 = convertDateToTimeStamp(dueDateTime);
        itemRecord.setDueDateTime(dueDateTime1);
    }

    public Timestamp convertDateToTimeStamp(String dateString) {
        Timestamp dueDateTime1 = null;
        String DATE_FORMAT_AM_PM_REGX = "^(1[0-2]|0[1-9])/(3[0|1]|[1|2][0-9]|0[1-9])/[0-9]{4}(\\s)(00|1[012]|0[1-9]):[0-5][0-9]:[0-5][0-9]?(?i)(am|pm)";
        String DATE_FORMAT_HH_MM_SS_REGX = "^(1[0-2]|0[1-9])/(3[0|1]|[1|2][0-9]|0[1-9])/[0-9]{4}(\\s)((([1|0][0-9])|([2][0-4]))):[0-5][0-9]:[0-5][0-9]$";
        if (StringUtils.isNotBlank(dateString) && dateString.matches(DATE_FORMAT_AM_PM_REGX)) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ssa");
            try {
                if (!"".equals(dateString) && dateString != null) {
                    dueDateTime1 = new Timestamp(df.parse(dateString).getTime());
                }
            } catch (Exception e) {
                //LOG.error("Effective Date for Item" + e);
            }
            return dueDateTime1;
        } else if (StringUtils.isNotBlank(dateString) && dateString.matches(DATE_FORMAT_HH_MM_SS_REGX)) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            try {
                if (!"".equals(dateString) && dateString != null) {
                    dueDateTime1 = new Timestamp(df.parse(dateString).getTime());
                }
            } catch (Exception e) {
                // LOG.error("Effective Date for Item" + e);
            }
            return dueDateTime1;
        } else {
            return null;
        }
    }

    private void effectiveDateItem(org.kuali.ole.docstore.common.document.content.instance.Item item, ItemRecord itemRecord, String effectiveDateForItem) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Timestamp effectiveDate = null;
        try {
            if (!"".equals(item.getItemStatusEffectiveDate()) && item.getItemStatusEffectiveDate() != null) {
                effectiveDate = new Timestamp(df.parse(effectiveDateForItem).getTime());
                itemRecord.setEffectiveDate(effectiveDate);
            }

        } catch (Exception e) {
            //LOG.error("Effective Date for Item" + e);
        }
    }


    private void itemDamagedCreateDateTime(org.kuali.ole.docstore.common.document.content.instance.ItemDamagedRecord itemDamagedRecord, ItemDamagedRecord damagedRecord, String damagedCreateDate) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Timestamp damagedCreateDate1 = null;
        try {
            if (!"".equals(itemDamagedRecord.getDamagedItemDate()) && itemDamagedRecord.getDamagedItemDate() != null) {
                damagedCreateDate1 = new Timestamp(df.parse(damagedCreateDate).getTime());
                damagedRecord.setDamagedItemDate(damagedCreateDate1);
            }
        } catch (Exception e) {
            //LOG.error("Item Damaged Date for Item" + e);
        }
    }


    protected List<ItemNoteRecord> saveItemNoteRecord(List<Note> noteList) {
        List<ItemNoteRecord> itemNoteRecordList = new ArrayList<ItemNoteRecord>();
        if (noteList.size() > 0) {
            for (int i = 0; i < noteList.size(); i++) {
                Note note = noteList.get(i);
                if (note.getType() != null && ("public".equalsIgnoreCase(note.getType()) || "nonPublic".equalsIgnoreCase(note.getType()))) {
                    ItemNoteRecord itemNoteRecord = new ItemNoteRecord();
                    itemNoteRecord.setType(note.getType());
                    itemNoteRecord.setNote(note.getValue());
                    itemNoteRecordList.add(itemNoteRecord);
                }
            }
        }
        return itemNoteRecordList;
    }

    protected void saveCheckInLocationRecord(List<CheckInLocation> checkInLocationList) {
        List<LocationsCheckinCountRecord> locationsCheckinCountRecordList = new ArrayList<LocationsCheckinCountRecord>();
        if (locationsCheckinCountRecordList != null && locationsCheckinCountRecordList.size() > 0) {
            LocationsCheckinCountRecord locationsCheckinCountRecord = locationsCheckinCountRecordList.get(0);
            CheckInLocation checkInLocation = checkInLocationList.get(0);
            locationsCheckinCountRecord.setLocationCount(checkInLocation.getCount());
            locationsCheckinCountRecord.setLocationName(checkInLocation.getName());
            locationsCheckinCountRecord.setLocationInhouseCount(checkInLocation.getInHouseCount());
        } else {
            CheckInLocation checkInLocation = checkInLocationList.get(0);
            LocationsCheckinCountRecord locationsCheckinCountRecord = new LocationsCheckinCountRecord();
            locationsCheckinCountRecord.setLocationCount(checkInLocation.getCount());
            locationsCheckinCountRecord.setLocationName(checkInLocation.getName());
            locationsCheckinCountRecord.setLocationInhouseCount(checkInLocation.getInHouseCount());
        }
    }


    protected void saveDonorListItem(List<DonorInfo> donorslist) {
        if (donorslist.size() > 0) {
            List<OLEItemDonorRecord> oleItemDonorRecords = new ArrayList<OLEItemDonorRecord>();
            for (int i = 0; i < donorslist.size(); i++) {
                DonorInfo donorinfo = donorslist.get(i);
                if (donorinfo.getDonorCode() != null ) {
                    OLEItemDonorRecord oleItemDonorRecord = new OLEItemDonorRecord();
                    oleItemDonorRecord.setDonorPublicDisplay(donorinfo.getDonorPublicDisplay());
                    oleItemDonorRecord.setDonorCode(donorinfo.getDonorCode());
                    oleItemDonorRecord.setDonorNote(donorinfo.getDonorNote());
                    oleItemDonorRecords.add(oleItemDonorRecord);
                }
            }
        }
    }

    protected StatisticalSearchRecord saveStatisticalSearchRecordItem(List<StatisticalSearchingCode> statisticalSearchingCodes) {
        if (statisticalSearchingCodes.size() > 0) {
            Map map = new HashMap();
            map.put("code", statisticalSearchingCodes.get(0).getCodeValue());
            List<StatisticalSearchRecord> statisticalSearchRecords = (List<StatisticalSearchRecord>) getBusinessObjectService().findMatching(StatisticalSearchRecord.class, map);
            if (statisticalSearchRecords.size() == 0) {
                if (statisticalSearchingCodes.get(0).getCodeValue() != null && !"".equals(statisticalSearchingCodes.get(0).getCodeValue())) {
                    StatisticalSearchRecord statisticalSearchRecord = new StatisticalSearchRecord();
                    statisticalSearchRecord.setCode(statisticalSearchingCodes.get(0).getCodeValue());
                    statisticalSearchRecord.setName(statisticalSearchingCodes.get(0).getFullValue());
                    getBusinessObjectService().save(statisticalSearchRecord);
                    return statisticalSearchRecord;
                } else {
                    return null;
                }
            }
            return statisticalSearchRecords.get(0);
        }
        return null;
    }


    private ItemStatisticalSearchRecord saveItemStatisticalSearchCodeItem(List<StatisticalSearchingCode> statisticalSearchingCodes) {
        ItemStatisticalSearchRecord itemStatisticalSearchRecord = new ItemStatisticalSearchRecord();
        if (statisticalSearchingCodes != null && statisticalSearchingCodes.size() > 0) {
            StatisticalSearchRecord statisticalSearchRecord = saveStatisticalSearchRecord(statisticalSearchingCodes);
            List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords =new ArrayList<ItemStatisticalSearchRecord>();


            if(itemStatisticalSearchRecords != null && itemStatisticalSearchRecords.size() > 0) {
                itemStatisticalSearchRecord = itemStatisticalSearchRecords.get(0);
            }
            if(statisticalSearchRecord != null) {
                itemStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
                itemStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);
            }


        }
        return itemStatisticalSearchRecord;
    }

    protected StatisticalSearchRecord saveStatisticalSearchRecord(List<StatisticalSearchingCode> statisticalSearchingCodes) {
        if (statisticalSearchingCodes.size() > 0) {
            Map map = new HashMap();
            map.put("code", statisticalSearchingCodes.get(0).getCodeValue());
            List<StatisticalSearchRecord> statisticalSearchRecords = (List<StatisticalSearchRecord>) getBusinessObjectService().findMatching(StatisticalSearchRecord.class, map);
            if (statisticalSearchRecords.size() == 0) {
                if (statisticalSearchingCodes.get(0).getCodeValue() != null && !"".equals(statisticalSearchingCodes.get(0).getCodeValue())) {
                    StatisticalSearchRecord statisticalSearchRecord = new StatisticalSearchRecord();
                    statisticalSearchRecord.setCode(statisticalSearchingCodes.get(0).getCodeValue());
                    statisticalSearchRecord.setName(statisticalSearchingCodes.get(0).getFullValue());
                    getBusinessObjectService().save(statisticalSearchRecord);
                    return statisticalSearchRecord;
                } else {
                    return null;
                }
            }
            return statisticalSearchRecords.get(0);
        }
        return null;
    }

}


