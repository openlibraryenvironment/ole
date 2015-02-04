package org.kuali.ole.describe.service;

import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.BibDocumentSearchResult;
import org.kuali.ole.describe.bo.OleBibliographicRecordStatus;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.ole.describe.bo.ImportBibUserPreferences;
import org.kuali.ole.describe.form.ImportBibForm;
import org.kuali.ole.describe.form.WorkInstanceOlemlForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.Items;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 18/12/12
 * Time: 6:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportBibService extends WorkBenchService implements Comparator<BibDocumentSearchResult> {

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();

    public void applyUserPref(ImportBibForm importBibForm) throws Exception {
        ImportBibUserPreferences userPref = importBibForm.getImportBibUserPreferences();
        String cfNum = null;
        if (userPref.getImportType().equalsIgnoreCase("overLayMatchPoint")) {
            BibMarcRecord newMarcRecord = importBibForm.getNewBibMarcRecord();
            // getting MatchPoint value from new marc record and setting existing uuid to control field 001.
            if (newMarcRecord != null) {
                for (ControlField cf : newMarcRecord.getControlFields()) {
                    if (cf != null && cf.getTag().equals("001")) {
                        String cfVal = cf.getValue();
                        cfNum = getNumber(cfVal);
                        importBibForm.getImportBibConfirmReplace().setMatchPoint(cfNum);
                        break;
                    }
                }
                //getting matching existing marc record from solr
                /*DiscoveryHelperService disHelperService = new DiscoveryHelperService();
                List responseFromSOLR = disHelperService.getResponseFromSOLR("035a", cfNum);*/
                String id = null;
                // marc record id field
                SearchParams searchParams = buildSearchParams(cfNum);
                SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                for(SearchResult searchResult : searchResponse.getSearchResults()){
                    for(SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        if(searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode())) {
                            if(searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ID)) {
                                importBibForm.setUuid(searchResultField.getFieldValue());
                                break;
                            }
                        }
                    }
                }
                /*for (Iterator iterator = responseFromSOLR.iterator(); iterator.hasNext(); ) {
                    Map map = (Map) iterator.next();
                    if (map.containsKey(OLEConstants.ID)) {
                        id = (String) map.get(OLEConstants.ID);
                        importBibForm.setUuid(id);
                        break;
                    }
                }*/
                // checkout marc record content from docstore
                BibMarcRecord existingMarcRecord = null;
                if (id != null && id.length() > 0) {
                    Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(id);
                    String marcXml = bib.getContent();
                    BibMarcRecords marcRecords = bibMarcRecordProcessor.fromXML(marcXml);

                    if (marcRecords != null && marcRecords.getRecords() != null
                            && marcRecords.getRecords().size() > 0) {
                        existingMarcRecord = marcRecords.getRecords().get(0);
                        importBibForm.setExistingBibMarcRecord(existingMarcRecord);

                    }
                    if (existingMarcRecord != null) {
                        //converting the workbibmarc record to workbib marc document and setting that to display in confirm replace screen.
                        //TODO : retrieve bib and set to overlay
                        importBibForm.getImportBibConfirmReplace().setOverLayMarcRecord(bib);
                        setUuidToNewMarcRecord(newMarcRecord, importBibForm.getUuid());
                        //applying removal and protected tag settings for marc
                        BibMarcRecord newBibMarcRec = applyUserPrefToMarcRecord(existingMarcRecord, userPref,
                                newMarcRecord);
                        importBibForm.setExistingBibMarcRecord(newBibMarcRec);
                    } else {
                        applyUserPrefToMarcRecord(null, userPref, newMarcRecord);
                        //applying user pref details to new instance
                        applyUsePrefNewInstanceFile(newMarcRecord, userPref, importBibForm);
                        importBibForm.setMessage("There is no record available with match point " + cfNum + " if you click on next, new import will be performed.");
                    }

                }
            }
        } else if (userPref.getImportType().equalsIgnoreCase("overLay")) {
            BibMarcRecord newMarcRecord = importBibForm.getNewBibMarcRecord();
            BibMarcRecord existingMarcRecord = importBibForm.getExistingBibMarcRecord();
            setUuidToNewMarcRecord(newMarcRecord, importBibForm.getUuid());
            BibMarcRecord newBibMarcRec = applyUserPrefToMarcRecord(existingMarcRecord, userPref, newMarcRecord);
//            applyUsePrefNewInstanceFile(newMarcRecord, userPref, importBibForm);
            importBibForm.setExistingBibMarcRecord(newBibMarcRec);
        } else if (userPref.getImportType().equalsIgnoreCase("newImport")) {
            BibMarcRecord newMarcRecord = importBibForm.getNewBibMarcRecord();
            applyUserPrefToMarcRecord(null, userPref, newMarcRecord);
            applyUsePrefNewInstanceFile(newMarcRecord, userPref, importBibForm);
        }
    }

    private void setUuidToNewMarcRecord(BibMarcRecord newMarcRecord, String uuid) {
        for (ControlField cf : newMarcRecord.getControlFields()) {
            if (cf != null && cf.getTag().equals("001")) {
                cf.setValue(uuid);
                newMarcRecord.setControlFields(newMarcRecord.getControlFields());
                break;
            }
        }
    }

    private void applyUsePrefNewInstanceFile(BibMarcRecord newMarcRecord, ImportBibUserPreferences userPref,
                                             ImportBibForm importBibForm) {
        String shelScheme = userPref.getShelvingScheme();
        OleShelvingScheme shelvingScheme = new OleShelvingScheme();
        if (shelScheme != null && shelScheme.length() > 0) {
            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            Map parentCriteria = new HashMap();
            parentCriteria.put("shelvingSchemeCode", shelScheme);
            shelvingScheme = businessObjectService.findByPrimaryKey(OleShelvingScheme.class, parentCriteria);
        }
        List<String> callNumList = new ArrayList<String>();
        callNumList.add(userPref.getCallNumberSource1());
        callNumList.add(userPref.getCallNumberSource2());
        callNumList.add(userPref.getCallNumberSource3());
        String callVal = getCallNumberValue(newMarcRecord, callNumList);

        // creating new Instance
        Instance newInstance = new Instance();
        CallNumber callNumber = new CallNumber();
        callNumber.setNumber(callVal);
        ShelvingScheme shelveScheme = new ShelvingScheme();
        shelveScheme.setCodeValue(shelvingScheme.getShelvingSchemeCode());
        callNumber.setShelvingScheme(shelveScheme);

        // creating new holding
        OleHoldings newHolding = new WorkInstanceOlemlForm().getSelectedHolding();
        newHolding.setCallNumber(callNumber);
        newHolding.setLocation(getLocation(userPref.getPermLocation()));


        // creating new Item
        Item newItem = new Item();
        newItem.setLocation(getLocation(userPref.getTempLocation()));
        if(newItem.getAccessInformation() == null) {
            AccessInformation accessInformation = new AccessInformation();
            Uri uri = new Uri();
            uri.setValue("");
            uri.setResolvable("");
            accessInformation.setUri(uri);
            newItem.setAccessInformation(accessInformation);
        }
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(newItem);
        newInstance.setOleHoldings(newHolding);
        Items items = new Items();
        items.setItem(itemList);
        newInstance.setItems(items);

        // creating new instance collection and setting that to form
        InstanceCollection instanceCollection = new InstanceCollection();
        List<Instance> instanceList = new ArrayList<Instance>();
        instanceList.add(newInstance);
        instanceCollection.setInstance(instanceList);
        importBibForm.setNewInstanceCollection(instanceCollection);
    }

    private Location getLocation(String location) {
        Location locationObj = new Location();
        if (location != null && location.length() > 0) {
            String loc = location.substring(location.lastIndexOf("/") + 1, location.length());

            if (loc != null && loc.length() > 0) {
                BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
                Map parentCriteria = new HashMap();
                parentCriteria.put("locationCode", loc);
                LocationLevel locationLevel = new LocationLevel();
                locationLevel.setName(location);
                locationObj.setLocationLevel(locationLevel);
            }
        }
        return locationObj;
    }

    private String getCallNumberValue(BibMarcRecord newMarcRecord, List<String> callNumList) {
        String callVal = null;
        for (String callNum : callNumList) {
            if (callNum != null && callNum.length() > 0) {
                for (DataField df : newMarcRecord.getDataFields()) {
                    if (df.getTag().equalsIgnoreCase(callNum)) {
                        for (SubField subF : df.getSubFields()) {
                            if (callVal == null) {
                                callVal = subF.getValue();
                            } else {
                                callVal = callVal + " " + subF.getValue();
                            }
                        }
                        return callVal.trim();
                    }

                }
            }
        }
        if (callVal == null) {
            callVal = "";
        }
        return callVal;
    }

    private BibMarcRecord applyUserPrefToMarcRecord(BibMarcRecord existingMarcRecord,
                                                        ImportBibUserPreferences userPref,
                                                        BibMarcRecord newMarcRec) {
        String adminProtectedTags = userPref.getAdminProtectedTags();
        String protectedTags = adminProtectedTags + "," + userPref.getProtectedTags();
        String adminRemovalTags = userPref.getAdminRemovalTags();
        String removalTags = adminRemovalTags + "," + userPref.getRemovalTags();
        boolean isProtected = false;
        //applying removalFields
        applyProtectedNRemovalTags(removalTags, isProtected, existingMarcRecord, newMarcRec);
        isProtected = true;
        // applying protectedFields
        applyProtectedNRemovalTags(protectedTags, isProtected, existingMarcRecord, newMarcRec);
        return newMarcRec;
    }

    private void applyProtectedNRemovalTags(String protectedTags, boolean isProtected,
                                            BibMarcRecord existingMarcRecord, BibMarcRecord newMarcRec) {
        String str[] = protectedTags.split(",");
        for (int i = 0; i < str.length; i++) {
            String field = str[i];
            if (field != null && field.length() >= 3) {
                String dataFieldPref = field.substring(0, 3);
                if (dataFieldPref != null && newMarcRec != null) {
                    applyDataFieldPref(dataFieldPref, existingMarcRecord, newMarcRec, isProtected);
                }
            }
        }
    }

    private void applyDataFieldPref(String dataFieldPref, BibMarcRecord existingMarcRecord,
                                    BibMarcRecord newMarcRec, boolean isProtected) {
        DataField dataField = null;
        if (isProtected && existingMarcRecord != null) {
            //get protected existing dataField from Existing Marc Record
            List<DataField> dataFields = getProtectedTagFromExisting(existingMarcRecord, dataFieldPref);
            if (dataFields != null && dataFields.size() > 0) {
                //remove protected dataField from new Marc Record if existing
                removeProtectedTag(newMarcRec, dataFieldPref);
                //add existing protected dataField to new Marc record
                for (DataField df : dataFields) {
                    newMarcRec.getDataFields().add(df);
                }
            }
        } else if (!isProtected) {
            removeProtectedTag(newMarcRec, dataFieldPref);
        }
    }


    private void removeProtectedTag(BibMarcRecord newMarcRecord, String dataFieldPref) {
        List<DataField> dfList = new ArrayList<DataField>();
        dfList.addAll(newMarcRecord.getDataFields());
        // remove the protected dataField in New Marc record if existing
        for (DataField newDataField : newMarcRecord.getDataFields()) {
            if (newDataField.getTag().equals(dataFieldPref)) {
                dfList.remove(newDataField);
            }
        }
        newMarcRecord.setDataFields(dfList);
    }

    private List<DataField> getProtectedTagFromExisting(BibMarcRecord existingMarcRecord, String dataFieldPref) {
        List<DataField> dataFieldList = new ArrayList<DataField>();
        for (DataField dfExisting : existingMarcRecord.getDataFields()) {
            if (dfExisting.getTag().equals(dataFieldPref)) {
                dataFieldList.add(dfExisting);
            }
        }
        return dataFieldList;
    }

    public String getNumber(String str) {
        String val = "";
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        while (m.find()) {
            val = m.group();
            return val;
        }
        return val;
    }

    public boolean callNumValidation(ImportBibUserPreferences userPreferences) {
        boolean isValid = false;
        List<String> callNumbers = new ArrayList<String>();
        callNumbers.add(userPreferences.getCallNumberSource1());
        callNumbers.add(userPreferences.getCallNumberSource2());
        callNumbers.add(userPreferences.getCallNumberSource3());
        for (String callNum : callNumbers) {
            if (callNum != null && callNum.length() > 0) {
                isValid = true;
                if (callNum.length() == 3 && getValidNumber(callNum)) {
                    isValid = false;
                } else {
                    return isValid;
                }
            }
        }
        return isValid;
    }

    public boolean proNRemTagValidation(ImportBibUserPreferences userPreferences) {
        boolean isValid = false;
        String tags = userPreferences.getProtectedTags() + "," + userPreferences.getRemovalTags();
        String str[] = tags.split(",");
        for (int i = 0; i < str.length; i++) {
            String field = str[i];
            if (field != null && field.length() > 0) {
                isValid = true;
                if (field.length() == 3 && getValidNumber(field)) {
                    isValid = false;
                } else {
                    return isValid;
                }
            }
        }

        return isValid;
    }


    public boolean getValidNumber(String str) {
        Pattern p = Pattern.compile("\\d{3}");
        Matcher m = p.matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }


    public Request buildRequest(BibMarcRecord newWorkBibMarcRecord, InstanceCollection newWorkInstanceOlemlRecord, String status) {
        Request req = new Request();
        String user = GlobalVariables.getUserSession().getPrincipalName();
        req.setUser(user);
        req.setOperation("ingest");
        if (newWorkBibMarcRecord != null) {
            // build marc request document
            List<BibMarcRecord> marcRecList = new ArrayList<BibMarcRecord>();
            marcRecList.add(newWorkBibMarcRecord);
            BibMarcRecords marcRecords = new BibMarcRecords();
            marcRecords.setRecords(marcRecList);
            String marcContent = bibMarcRecordProcessor.toXml(marcRecords);
            RequestDocument reqDoc = new RequestDocument();
            buildRequestDoc("work", "bibliographic", "marc", marcContent, reqDoc, status);
            //build Instance request document
            if (newWorkInstanceOlemlRecord != null) {
                InstanceOlemlRecordProcessor instanceProcessor = new InstanceOlemlRecordProcessor();
                String instContent = instanceProcessor.toXML(newWorkInstanceOlemlRecord);
                RequestDocument instDoc = new RequestDocument();
                buildRequestDoc("work", "instance", "oleml", instContent, instDoc, null);
                List<RequestDocument> linkedRequestDocumentList = new ArrayList<RequestDocument>();
                linkedRequestDocumentList.add(instDoc);
                reqDoc.setLinkedRequestDocuments(linkedRequestDocumentList);
            }
            List<RequestDocument> requestDocumentList = new ArrayList<RequestDocument>();
            requestDocumentList.add(reqDoc);
            req.setRequestDocuments(requestDocumentList);
        }
        return req;
    }

    private void buildRequestDoc(String cat, String type, String format, String content, RequestDocument reqDoc, String status) {
        reqDoc.setId("1");
        reqDoc.setCategory(cat);
        reqDoc.setType(type);
        reqDoc.setFormat(format);
        Content contentObj = new Content();
        contentObj.setContent(content);
        reqDoc.setContent(contentObj);
        if (status != null) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String dateStr = sdf.format(date);
            String user = null;
            if (GlobalVariables.getUserSession() != null && GlobalVariables.getUserSession().getLoggedInUserPrincipalName() != null) {
                user = GlobalVariables.getUserSession().getLoggedInUserPrincipalName();
            }
            AdditionalAttributes additionalAttributes = new AdditionalAttributes();
            BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
            additionalAttributes.setAttribute(AdditionalAttributes.DATE_ENTERED, dateStr);
            additionalAttributes.setAttribute(AdditionalAttributes.CREATED_BY, user);
            Map parentCriteria = new HashMap();
            parentCriteria.put("bibliographicRecordStatusCode", status);
            OleBibliographicRecordStatus bibliographicRecordStatus = boService
                    .findByPrimaryKey(OleBibliographicRecordStatus.class, parentCriteria);
            if (bibliographicRecordStatus != null) {
                additionalAttributes.setAttribute(AdditionalAttributes.STATUS,
                        bibliographicRecordStatus.getBibliographicRecordStatusName());
            } else {
                additionalAttributes.setAttribute(AdditionalAttributes.STATUS, "");
            }
            reqDoc.setAdditionalAttributes(additionalAttributes);
        }
    }

    @Override
    public int compare(BibDocumentSearchResult o1, BibDocumentSearchResult o2) {
        if (o1.getTitle() == null && o2.getTitle() == null) {
            return 0;
        }
        if (o1.getTitle() == null) {
            return 1;
        }
        if (o2.getTitle() == null) {
            return -1;
        }
        return delegate.compare(o1.getTitle(), o2.getTitle());
    }

    private NullComparator delegate = new NullComparator(false);

    public BibTree createBibTree(BibMarcRecord newBibMarcRecord, InstanceCollection newInstanceCollection, String importStatus) {
        BibTree bibTree = new BibTree();
        Bib bib = new BibMarc();


        BibMarcRecords bibMarcRecords=new BibMarcRecords();
        List<BibMarcRecord> bibMarcRecordList=new ArrayList<>();
        bibMarcRecordList.add(newBibMarcRecord);

        bibMarcRecords.setRecords(bibMarcRecordList);
        BibMarcRecordProcessor bibMarcRecordProcessor=new BibMarcRecordProcessor();

        bib.setCategory(DocCategory.WORK.getCode());
        bib.setType(DocType.BIB.getCode());
        bib.setFormat(DocFormat.MARC.getCode());
        bib.setContent(bibMarcRecordProcessor.toXml(bibMarcRecords));

        ItemOlemlRecordProcessor itemOlemlRecordProcessor=new ItemOlemlRecordProcessor();
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();

        org.kuali.ole.docstore.common.document.Item itemOleml=new ItemOleml();
        if(newInstanceCollection != null && newInstanceCollection.getInstance() != null && newInstanceCollection.getInstance().size() > 0) {
            OleHoldings oleHoldings = newInstanceCollection.getInstance().get(0).getOleHoldings();
            Item item = newInstanceCollection.getInstance().get(0).getItems().getItem().get(0);
            itemOleml.setContent(itemOlemlRecordProcessor.toXML(item));
            Holdings holdings = new PHoldings();
            holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));

            HoldingsTree holdingsTree=new HoldingsTree();

            holdingsTree.setHoldings(holdings);
            holdingsTree.getItems().add(itemOleml);
            bibTree.getHoldingsTrees().add(holdingsTree);
        }
        bibTree.setBib(bib);
        return bibTree;
    }

    private SearchParams buildSearchParams(String cfn) {
        SearchParams searchParams = new SearchParams();
        List<SearchCondition>  searchConditions = new ArrayList<>();
        if(StringUtils.isNotEmpty(cfn)) {
            searchConditions.add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField(DocType.BIB.getCode(), "035a", cfn), "AND"));
        }
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(),OLEConstants.ID));
        return searchParams;
    }
}
