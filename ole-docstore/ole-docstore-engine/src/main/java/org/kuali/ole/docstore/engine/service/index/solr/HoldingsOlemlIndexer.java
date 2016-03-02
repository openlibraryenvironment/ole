package org.kuali.ole.docstore.engine.service.index.solr;

import java.io.IOException;
import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreIndexException;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.engine.service.DocstoreServiceImpl;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.utility.XMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */

public class HoldingsOlemlIndexer extends DocstoreSolrIndexService implements DocstoreConstants {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingsOlemlIndexer.class);
    private static HoldingsOlemlIndexer holdingsOlemlIndexer = null;

    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();

    public static XMLUtility xmlUtility = new XMLUtility();

    public static HoldingsOlemlIndexer getInstance() {
        if (holdingsOlemlIndexer == null) {
            holdingsOlemlIndexer = new HoldingsOlemlIndexer();
        }
        return holdingsOlemlIndexer;
    }

    protected void buildSolrInputDocument(Object object, List<SolrInputDocument> solrInputDocuments) {
        Holdings holdings = (Holdings) object;

        SolrInputDocument solrDocForHolding = getSolrInputFieldsForHoldings(holdings);

        Date date = new Date();
        solrDocForHolding.addField(DATE_ENTERED, date);
        solrDocForHolding.addField(CREATED_BY, holdings.getCreatedBy());

        solrDocForHolding.addField(BIB_IDENTIFIER, holdings.getBib().getId());

        List<SolrDocument> solrBibDocs = getSolrDocumentBySolrId(holdings.getBib().getId());

        if(solrBibDocs != null && solrBibDocs.size() > 0) {
            SolrDocument bibSolrDoc = solrBibDocs.get(0);
            addBibInfoForHoldingsOrItems(solrDocForHolding, bibSolrDoc);

            //add holdings to bib
            addInstIdToBib(holdings.getId(), solrInputDocuments, bibSolrDoc);
        }

        solrInputDocuments.add(solrDocForHolding);
    }

    @Override
    public void createTree(Object object) {
        List<SolrInputDocument> solrInputDocuments = new ArrayList<>();

        HoldingsTree holdingsTree = (HoldingsTree) object;
        String bibId = holdingsTree.getHoldings().getBib().getId();

        SolrInputDocument holdingsSolrInputDoc = getSolrInputFieldsForHoldings(holdingsTree.getHoldings());

        Date date = new Date();
        holdingsSolrInputDoc.addField(CREATED_BY, holdingsTree.getHoldings().getUpdatedBy());
        holdingsSolrInputDoc.addField(DATE_ENTERED, date);

        holdingsSolrInputDoc.addField(BIB_IDENTIFIER, bibId);

        List<org.kuali.ole.docstore.common.document.Item>  itemDocuments = holdingsTree.getItems();
        List<String> itemIds = new ArrayList<>();

        holdingsSolrInputDoc.addField(ITEM_IDENTIFIER, itemIds);
        solrInputDocuments.add(holdingsSolrInputDoc);
        List<SolrDocument> solrBibDocs = getSolrDocumentBySolrId(bibId);

        if(solrBibDocs != null && solrBibDocs.size() > 0) {
            SolrDocument bibSolrDoc = solrBibDocs.get(0);
            addBibInfoForHoldingsOrItems(holdingsSolrInputDoc, bibSolrDoc);
            addHoldingsInfoToBib(holdingsSolrInputDoc,bibSolrDoc);
            //add holdings to bib
            addInstIdToBib(holdingsTree.getHoldings().getId(), solrInputDocuments, bibSolrDoc);
        }

        ItemOlemlIndexer itemOlemlIndexer = ItemOlemlIndexer.getInstance();
        for(org.kuali.ole.docstore.common.document.Item itemDocument : itemDocuments) {
            itemIds.add(itemDocument.getId());
            SolrInputDocument itemSolrInputDoc = itemOlemlIndexer.getSolrInputFieldsForItem(itemDocument);

            itemSolrInputDoc.addField(HOLDINGS_IDENTIFIER, holdingsTree.getHoldings().getId());
            itemSolrInputDoc.addField(BIB_IDENTIFIER, bibId);

            addBibInfoForHoldingsOrItems(itemSolrInputDoc, holdingsSolrInputDoc);
            addHoldingsInfoToItem(itemSolrInputDoc, holdingsSolrInputDoc);
            solrInputDocuments.add(itemSolrInputDoc);
        }
        indexSolrDocuments(solrInputDocuments, true);
    }

    protected SolrInputDocument getSolrInputFieldsForHoldings(Holdings holdings,SolrInputDocument solrDocForHolding) {
        OleHoldings oleHoldings = null;
        if(StringUtils.isNotEmpty(holdings.getContent())) {
            oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
//            solrDocForHolding.addField(ALL_TEXT, xmlUtility.getAllContentText(holdings.getContent()));

        } else if(holdings.getContentObject() != null) {
            oleHoldings = holdings.getContentObject();
//            solrDocForHolding.addField(ALL_TEXT, xmlUtility.getAllContentText(holdingOlemlRecordProcessor.toXML(oleHoldings)));

        }

        if(oleHoldings != null && oleHoldings.getGokbIdentifier() != null) {
            solrDocForHolding.addField(GOKB_IDENTIFIER, oleHoldings.getGokbIdentifier());
        }

        setCommonFields(holdings, solrDocForHolding);

        solrDocForHolding.addField(RECEIPT_STATUS_SEARCH, oleHoldings.getReceiptStatus());
        solrDocForHolding.addField(RECEIPT_STATUS_DISPLAY, oleHoldings.getReceiptStatus());

        if (oleHoldings.getCopyNumber() != null) {
            solrDocForHolding.addField(COPY_NUMBER_SEARCH, oleHoldings.getCopyNumber());
            solrDocForHolding.addField(COPY_NUMBER_LABEL_SEARCH, oleHoldings.getCopyNumber());
            solrDocForHolding.addField(COPY_NUMBER_DISPLAY, oleHoldings.getCopyNumber());
            solrDocForHolding.addField(COPY_NUMBER_LABEL_DISPLAY, oleHoldings.getCopyNumber());
        }
        if (oleHoldings.getCallNumber() != null) {
            solrDocForHolding.addField(CALL_NUMBER_TYPE_SEARCH, oleHoldings.getCallNumber().getType());
            solrDocForHolding.addField(CALL_NUMBER_SEARCH, oleHoldings.getCallNumber().getNumber());
            solrDocForHolding.setField(CALL_NUMBER_SORT, oleHoldings.getCallNumber().getNumber());
            solrDocForHolding.addField(ITEM_PART_SEARCH, oleHoldings.getCallNumber().getItemPart());
            solrDocForHolding.addField(CALL_NUMBER_PREFIX_SEARCH, oleHoldings.getCallNumber().getPrefix());
            solrDocForHolding.addField(CLASSIFICATION_PART_SEARCH, oleHoldings.getCallNumber().getClassificationPart());

            solrDocForHolding.addField(ITEM_PART_DISPLAY, oleHoldings.getCallNumber().getItemPart());
            solrDocForHolding.addField(CALL_NUMBER_TYPE_DISPLAY, oleHoldings.getCallNumber().getType());
            solrDocForHolding.addField(CALL_NUMBER_DISPLAY, oleHoldings.getCallNumber().getNumber());
            solrDocForHolding.addField(CALL_NUMBER_PREFIX_DISPLAY, oleHoldings.getCallNumber().getPrefix());
            solrDocForHolding.addField(CLASSIFICATION_PART_DISPLAY, oleHoldings.getCallNumber().getClassificationPart());
            String shelvingSchemeCodeValue = "";
            if (oleHoldings.getCallNumber().getShelvingScheme() != null) {
                shelvingSchemeCodeValue = oleHoldings.getCallNumber().getShelvingScheme().getCodeValue();
                solrDocForHolding.addField(SHELVING_SCHEME_VALUE_SEARCH, oleHoldings.getCallNumber().getShelvingScheme().getFullValue());
                solrDocForHolding.addField(SHELVING_SCHEME_CODE_SEARCH, oleHoldings.getCallNumber().getShelvingScheme().getCodeValue());
                solrDocForHolding.addField(SHELVING_SCHEME_VALUE_DISPLAY, oleHoldings.getCallNumber().getShelvingScheme().getFullValue());
                solrDocForHolding.addField(SHELVING_SCHEME_CODE_DISPLAY, oleHoldings.getCallNumber().getShelvingScheme().getCodeValue());
            }
            String shelvingOrder = null;
            if (oleHoldings.getCallNumber().getShelvingOrder() != null && StringUtils.isNotEmpty(oleHoldings.getCallNumber().getShelvingOrder().getFullValue())) {
                shelvingOrder = oleHoldings.getCallNumber().getShelvingOrder().getFullValue();
            } else {
                if (StringUtils.isNotEmpty(oleHoldings.getCallNumber().getNumber()) && oleHoldings.getCallNumber().getNumber().trim().length() > 0) {
                    shelvingOrder = buildSortableCallNumber(oleHoldings.getCallNumber().getNumber(), shelvingSchemeCodeValue);
                }
            }
            if (StringUtils.isNotEmpty(shelvingOrder)) {
                shelvingOrder = shelvingOrder.replaceAll(" ", "-");
                solrDocForHolding.addField(SHELVING_ORDER_SORT, shelvingOrder + holdings.getId());
                solrDocForHolding.addField(SHELVING_ORDER_SEARCH, shelvingOrder);
                solrDocForHolding.addField(SHELVING_ORDER_DISPLAY, shelvingOrder);
            }
        }
        StringBuffer loactionLevelStr = new StringBuffer(" ");
        if (oleHoldings != null && oleHoldings.getLocation() != null &&
                oleHoldings.getLocation().getLocationLevel() != null) {
            StringBuffer locationName = new StringBuffer();
            StringBuffer locationLevel = new StringBuffer();
            Location location = oleHoldings.getLocation();
            buildLocationNameAndLocationLevel(location, locationName, locationLevel);
            buildLocationName(location, solrDocForHolding,loactionLevelStr);
            solrDocForHolding.addField(LOCATION_LEVEL_SEARCH, locationName.toString().replaceAll("-",""));
            solrDocForHolding.addField(LOCATION_LEVEL_NAME_SEARCH, locationLevel.toString());
            solrDocForHolding.addField(LOCATION_LEVEL_DISPLAY, locationName.toString());
            solrDocForHolding.addField(LOCATION_LEVEL_NAME_DISPLAY, locationLevel.toString());
            solrDocForHolding.addField(LOCATION_LEVEL_SORT, locationName.toString());
        }
        if(holdings.getHoldingsType().equalsIgnoreCase(PHoldings.PRINT)) {
            solrDocForHolding.addField(DOC_TYPE, DocType.HOLDINGS.getCode());
            indexPHoldingsInformation(oleHoldings, solrDocForHolding);
        }
        else {
            solrDocForHolding.addField(DOC_TYPE, DocType.EHOLDINGS.getCode());
            indexEHoldingsInfomation(oleHoldings, solrDocForHolding);
        }

        solrDocForHolding.addField(ALL_TEXT, getAllTextValueForHoldings(oleHoldings) + loactionLevelStr.toString());
        return solrDocForHolding;
    }

    protected SolrInputDocument getSolrInputFieldsForHoldings(Holdings holdings) {
        SolrInputDocument solrDocForHolding = new SolrInputDocument();
        getSolrInputFieldsForHoldings(holdings,solrDocForHolding);
        return solrDocForHolding;
    }

    private void indexEHoldingsInfomation(OleHoldings oleHoldings, SolrInputDocument solrDocForHolding) {

        for (Note holdingNote : oleHoldings.getNote()) {
            solrDocForHolding.addField(HOLDING_NOTE_SEARCH, holdingNote.getValue());
            solrDocForHolding.addField(HOLDING_NOTE_DISPLAY, holdingNote.getValue());
        }
        solrDocForHolding.addField(ACCESS_STATUS_DISPLAY, oleHoldings.getAccessStatus());
        solrDocForHolding.addField(ACCESS_STATUS_SEARCH, oleHoldings.getAccessStatus());
        //solrDocForHolding.addField(ACCESS_STATUS_SEARCH, oleHoldings.getAccessStatus());
        for (DonorInfo donorInfo : oleHoldings.getDonorInfo()) {
            solrDocForHolding.addField(DONOR_CODE_SEARCH, donorInfo.getDonorCode());
            solrDocForHolding.addField(DONOR_CODE_DISPLAY, donorInfo.getDonorCode());
            solrDocForHolding.addField(DONOR_PUBLIC_DISPLAY, donorInfo.getDonorPublicDisplay());
            solrDocForHolding.addField(DONOR_PUBLIC_SEARCH, donorInfo.getDonorPublicDisplay());
            solrDocForHolding.addField(DONOR_NOTE_DISPLAY, donorInfo.getDonorNote());
            solrDocForHolding.addField(DONOR_NOTE_SEARCH, donorInfo.getDonorNote());
        }
        if (oleHoldings.getStatisticalSearchingCode() != null) {
            solrDocForHolding.addField(STATISTICAL_SEARCHING_CODE_VALUE_SEARCH, oleHoldings.getStatisticalSearchingCode().getFullValue());
            solrDocForHolding.addField(STATISTICAL_SEARCHING_CODE_VALUE_DISPLAY, oleHoldings.getStatisticalSearchingCode().getCodeValue());
        }

       /* solrDocForHolding.addField(PUBLISHER_SEARCH, oleHoldings.getPublisher());*/
        solrDocForHolding.addField(E_PUBLISHER_DISPLAY, oleHoldings.getPublisher());
        solrDocForHolding.addField(E_PUBLISHER_SEARCH, oleHoldings.getPublisher());

        solrDocForHolding.addField(IMPRINT_SEARCH, oleHoldings.getImprint());
        solrDocForHolding.addField(IMPRINT_DISPLAY, oleHoldings.getImprint());

        if (oleHoldings.getPlatform() != null) {
            solrDocForHolding.addField(ADMIN_URL_DISPLAY, oleHoldings.getPlatform().getAdminUrl());
            solrDocForHolding.addField(ADMIN_URL_SEARCH, oleHoldings.getPlatform().getAdminUrl());
            solrDocForHolding.addField(PLATFORM_DISPLAY, oleHoldings.getPlatform().getPlatformName());
            solrDocForHolding.addField(PLATFORM_SEARCH, oleHoldings.getPlatform().getPlatformName());
            solrDocForHolding.addField(ADMIN_USERNAME_DISPLAY, oleHoldings.getPlatform().getAdminUserName());
            solrDocForHolding.addField(ADMIN_USERNAME_SEARCH, oleHoldings.getPlatform().getAdminUserName());
            solrDocForHolding.addField(ADMIN_PASSWORD_DISPLAY, oleHoldings.getPlatform().getAdminPassword());
            solrDocForHolding.addField(ADMIN_PASSWORD_SEARCH, oleHoldings.getPlatform().getAdminPassword());
        }

        solrDocForHolding.addField(PUBLIC_NOTE_DISPLAY, oleHoldings.getDonorPublicDisplay());

        if (oleHoldings.getLink() != null) {
            for (Link link : oleHoldings.getLink()) {
                if(StringUtils.isNotEmpty(link.getUrl())){
                    solrDocForHolding.addField(URL_DISPLAY, link.getUrl());
                }
                if(StringUtils.isNotEmpty(link.getUrl())){
                    solrDocForHolding.addField(URL_SEARCH, link.getUrl());
                }
                if(StringUtils.isNotEmpty(link.getText())){
                    solrDocForHolding.addField(LINK_TEXT_DISPLAY, link.getText());
                }
                if(StringUtils.isNotEmpty(link.getText())){
                    solrDocForHolding.addField(LINK_TEXT_SEARCH, link.getText());
                }
            }
            /*solrDocForHolding.addField(URL_SEARCH, oleHoldings.getLink().getUrl());
            solrDocForHolding.addField(LINK_TEXT_DISPLAY,oleHoldings.getLink().getText());
            solrDocForHolding.addField(LINK_TEXT_SEARCH,oleHoldings.getLink().getText());*/
        }

        if (oleHoldings.getSubscriptionStatus() != null) {
            solrDocForHolding.addField(SUBSCRIPTION_DISPLAY, oleHoldings.getSubscriptionStatus());
            solrDocForHolding.addField(SUBSCRIPTION_SEARCH, oleHoldings.getSubscriptionStatus());

        }
        if (oleHoldings.getHoldingsAccessInformation() != null) {
            solrDocForHolding.addField(ACCESS_USERNAME_DISPLAY, oleHoldings.getHoldingsAccessInformation().getAccessUsername());
            solrDocForHolding.addField(ACCESS_USERNAME_SEARCH, oleHoldings.getHoldingsAccessInformation().getAccessUsername());
            solrDocForHolding.addField(ACCESS_PASSWORD_DISPLAY, oleHoldings.getHoldingsAccessInformation().getAccessPassword());
            solrDocForHolding.addField(ACCESS_PASSWORD_SEARCH, oleHoldings.getHoldingsAccessInformation().getAccessPassword());
            solrDocForHolding.addField(AUTHENTICATION_DISPLAY, oleHoldings.getHoldingsAccessInformation().getAuthenticationType());
            solrDocForHolding.addField(AUTHENTICATION_SEARCH, oleHoldings.getHoldingsAccessInformation().getAuthenticationType());
            solrDocForHolding.addField(PROXIED_DISPLAY, oleHoldings.getHoldingsAccessInformation().getProxiedResource());
            solrDocForHolding.addField(PROXIED_SEARCH, oleHoldings.getHoldingsAccessInformation().getProxiedResource());
            solrDocForHolding.addField(NUMBER_OF_SIMULTANEOUS_USERS_DISPLAY, oleHoldings.getHoldingsAccessInformation().getNumberOfSimultaneousUser());
            solrDocForHolding.addField(NUMBER_OF_SIMULTANEOUS_USERS_SEARCH, oleHoldings.getHoldingsAccessInformation().getNumberOfSimultaneousUser());
            solrDocForHolding.addField(ACCESS_LOCATION_DISPLAY, oleHoldings.getHoldingsAccessInformation().getAccessLocation());
            solrDocForHolding.addField(ACCESS_LOCATION_SEARCH, oleHoldings.getHoldingsAccessInformation().getAccessLocation());
        }
        if (oleHoldings.getLocalPersistentLink() != null) {
            solrDocForHolding.addField(PERSIST_LINK_SEARCH, oleHoldings.getLocalPersistentLink());
            solrDocForHolding.addField(PERSIST_LINK_DISPLAY, oleHoldings.getLocalPersistentLink());
        }
        if (oleHoldings.isInterLibraryLoanAllowed()) {
            solrDocForHolding.addField(ILL_DISPLAY, oleHoldings.isInterLibraryLoanAllowed());
            solrDocForHolding.addField(ILL_SEARCH, oleHoldings.isInterLibraryLoanAllowed());
        }
        if (oleHoldings.getExtentOfOwnership() != null && oleHoldings.getExtentOfOwnership().size() > 0) {
            if (null != oleHoldings.getExtentOfOwnership().get(0).getCoverages() && null != oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage() && oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().size() > 0) {
                List<Coverage> coverageList = oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage();
                for (Coverage coverage : coverageList) {
                    if (StringUtils.isNotBlank(coverage.getCoverageStartDate()) || StringUtils.isNotBlank(coverage.getCoverageEndDate())) {
                        solrDocForHolding.addField(E_INSTANCE_COVERAGE_DATE, coverage.getCoverageStartDate() + "-" + coverage.getCoverageEndDate());
                    } else if (null != oleHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses() && null != oleHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess() && oleHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().size() > 0) {
                        List<PerpetualAccess> perpetualAccessList = oleHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess();
                        for (PerpetualAccess perpetualAccess : perpetualAccessList) {
                            if (StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDate()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDate())) {
                                solrDocForHolding.addField(E_INSTANCE_PERPETUAL_ACCESS_DATE, perpetualAccess.getPerpetualAccessStartDate() + "-" + perpetualAccess.getPerpetualAccessEndDate());
                            }
                        }
                    }
                }
            }
        }

    }

    private void indexPHoldingsInformation(OleHoldings oleHoldings, SolrInputDocument solrDocForHolding) {
        for (Note holdingNote : oleHoldings.getNote()) {
            solrDocForHolding.addField(HOLDING_NOTE_SEARCH, holdingNote.getValue());
            solrDocForHolding.addField(HOLDING_NOTE_DISPLAY, holdingNote.getValue());
        }
        for (Uri uri : oleHoldings.getUri()) {
            solrDocForHolding.addField(URI_SEARCH, uri.getValue());
            solrDocForHolding.addField(URI_DISPLAY, uri.getValue());
        }
        if (oleHoldings.getExtentOfOwnership() != null && oleHoldings.getExtentOfOwnership().size()>0) {
            List<ExtentOfOwnership> extentOfOwnershipList = new ArrayList<ExtentOfOwnership>();
            for (int extentOfOwnership = 0; extentOfOwnership<oleHoldings.getExtentOfOwnership().size();extentOfOwnership++) {
                extentOfOwnershipList.add(oleHoldings.getExtentOfOwnership().get(extentOfOwnership));
            }
            for (ExtentOfOwnership extentOfOwnership :extentOfOwnershipList) {
                for (int note = 0; note<extentOfOwnership.getNote().size(); note++) {
                    solrDocForHolding.addField(EXTENT_OF_OWNERSHIP_NOTE_VALUE_DISPLAY,extentOfOwnership.getNote().get(note).getValue());
                    solrDocForHolding.addField(EXTENT_OF_OWNERSHIP_NOTE_TYPE_DISPLAY,extentOfOwnership.getNote().get(note).getType());
                }
                solrDocForHolding.addField(EXTENT_OF_OWNERSHIP_TYPE_DISPLAY, extentOfOwnership.getType());
            }
        }
        if (oleHoldings.getHoldingsAccessInformation() != null) {
            solrDocForHolding.addField(AUTHENTICATION_DISPLAY, oleHoldings.getHoldingsAccessInformation().getAuthenticationType());
            solrDocForHolding.addField(PROXIED_DISPLAY, oleHoldings.getHoldingsAccessInformation().getProxiedResource());
            solrDocForHolding.addField(NUMBER_OF_SIMULTANEOUS_USERS_DISPLAY, oleHoldings.getHoldingsAccessInformation().getNumberOfSimultaneousUser());
            solrDocForHolding.addField(ACCESS_LOCATION_DISPLAY, oleHoldings.getHoldingsAccessInformation().getAccessLocation());
        }
    }

    protected void setCommonFields(Holdings holdings, SolrInputDocument solrDocForHolding) {

        solrDocForHolding.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDocForHolding.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
        solrDocForHolding.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(holdings.getId()));
        solrDocForHolding.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(holdings.getId()));
        solrDocForHolding.addField(ID, holdings.getId());
        solrDocForHolding.addField(HOLDINGS_IDENTIFIER, holdings.getId());
        solrDocForHolding.addField(UNIQUE_ID, holdings.getId());
        solrDocForHolding.addField(STAFF_ONLY_FLAG, holdings.isStaffOnly());
        solrDocForHolding.addField(IS_SERIES, holdings.isSeries());


    }


    /*protected void updateRecordInSolr(Object object, List<SolrInputDocument> solrInputDocuments) {
        Holdings holdings = (Holdings) object;
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        List<SolrDocument> solrDocumentList = getSolrDocumentBySolrId(holdings.getId());
        SolrDocument holdingsSolrDocument = solrDocumentList.get(0);
        if (holdingsSolrDocument != null) {
//            holdings.setCreatedBy((String) holdingsSolrDocument.getFieldValue(DATE_ENTERED));
//            holdings.setCreatedOn((String) holdingsSolrDocument.getFieldValue(CREATED_BY));

            oleHoldings.setHoldingsIdentifier(holdings.getId());
            Object itemIdentifier = holdingsSolrDocument.getFieldValue(ITEM_IDENTIFIER);
            List<String> reId = new ArrayList<String>();
            if (holdingsSolrDocument.getFieldValue(BIB_IDENTIFIER) != null && holdingsSolrDocument
                    .getFieldValue(BIB_IDENTIFIER) instanceof List) {
                List<String> idList = (List<String>) holdingsSolrDocument.getFieldValue(BIB_IDENTIFIER);
                for (String bibId : idList) {
                    reId.add(bibId);
                }
            } else if (holdingsSolrDocument.getFieldValue(BIB_IDENTIFIER) != null && holdingsSolrDocument
                    .getFieldValue(BIB_IDENTIFIER) instanceof String) {
                reId.add((String) holdingsSolrDocument.getFieldValue(BIB_IDENTIFIER));
            }

            SolrInputDocument solrDocForHolding = getSolrInputFieldsForHoldings(holdings);

            addBibInfoForHoldingsOrItems(solrDocForHolding, holdingsSolrDocument);

            List<String> itemIds = new ArrayList<String>();
            if(itemIdentifier != null) {
                if(itemIdentifier instanceof String) {
                    itemIds.add((String) itemIdentifier);
                }
                else {
                    itemIds.addAll((List<String>) itemIdentifier);
                }
            }

            for (String itemId : itemIds) {

                List<SolrDocument> itemDocumentList = getSolrDocumentBySolrId(itemId);
                SolrDocument itemSolrDocument = itemDocumentList.get(0);
                SolrInputDocument itemSolrInputDocument = new SolrInputDocument();
                buildSolrInputDocFromSolrDoc(itemSolrDocument, itemSolrInputDocument);
                removeFieldFromSolrInputDocument(itemSolrInputDocument);
                addBibInfoForHoldingsOrItems(itemSolrInputDocument, solrDocForHolding);
                addHoldingsInfoToItem(itemSolrInputDocument, solrDocForHolding);
                solrInputDocuments.add(itemSolrInputDocument);
            }
        }
    }*/

    protected void updateRecordInSolr(Object object, List<SolrInputDocument> solrInputDocuments) {
        LOG.info("HoldingsOlemlIndexer class");
        Holdings holdings = (Holdings) object;
        List<SolrDocument> solrDocumentList = getSolrDocumentBySolrId(holdings.getId());
        SolrDocument holdingsSolrDocument = solrDocumentList.get(0);

        if (holdingsSolrDocument != null) {
            Object itemIdentifier = holdingsSolrDocument.getFieldValue(ITEM_IDENTIFIER);
            SolrInputDocument solrDocForHolding = getSolrInputFieldsForHoldings(holdings);
            addBibInfoForHoldingsOrItems(solrDocForHolding, holdingsSolrDocument);

            DocstoreService docstoreService = new DocstoreServiceImpl();
            HoldingsTree holdingsTree = docstoreService.retrieveHoldingsTree(holdings.getId());
            ItemOlemlIndexer itemOlemlIndexer = ItemOlemlIndexer.getInstance();
            for (Item item : holdingsTree.getItems()) {
                itemOlemlIndexer.updateRecordInSolrForItem(item, solrInputDocuments, solrDocForHolding);
            }

            solrDocForHolding.addField(BIB_IDENTIFIER, holdings.getBib().getId());
            solrDocForHolding.addField(ITEM_IDENTIFIER, itemIdentifier);
            Date date = new Date();
            solrDocForHolding.addField(UPDATED_BY, holdings.getUpdatedBy());
            solrDocForHolding.addField(DATE_UPDATED, date);
            solrInputDocuments.add(solrDocForHolding);
            addHoldingsDetailsToBib(solrInputDocuments,solrDocForHolding,holdings.getBib().getId());
        }
    }

    private void addHoldingsDetailsToBib(List<SolrInputDocument> solrInputDocuments, SolrInputDocument solrInputDocument, Object bibs) {
        String bibId = (String) bibs;
        SolrDocument solrBibDocument = getSolrDocumentByUUID(bibId);
        Collection<Object> bibValues = solrBibDocument.getFieldValues(URI_SEARCH);
        setStaffOnly(solrInputDocuments, solrInputDocument, bibId, solrBibDocument);
        Object holdigsValue = solrInputDocument.getFieldValue(URI_SEARCH);
        if (bibValues != null) {
            for (Object bibValue : bibValues) {
                if (holdigsValue!=null && !((String) bibValue).equalsIgnoreCase(((String) holdigsValue))) {
                    solrBibDocument.addField(URI_SEARCH, solrInputDocument.getFieldValue(URI_SEARCH));
                }
            }
        }

    }

    private void setStaffOnly(List<SolrInputDocument> solrInputDocuments, SolrInputDocument solrInputDocument, String bibId, SolrDocument solrBibDocument) {
        boolean allStaffOnly = true;
        Collection<Object> holdingsIdentifier=solrBibDocument.getFieldValues("holdingsIdentifier");
        if (holdingsIdentifier.size() == 1) {
            solrBibDocument.setField("staffOnlyFlag", solrInputDocument.getFieldValue("staffOnlyFlag"));
            solrInputDocuments.add(buildSolrInputDocFromSolrDoc(solrBibDocument));
            allStaffOnly = false;
        } else if (holdingsIdentifier.size() > 1) {
            String currentHoldingsIdentifier = (String) solrInputDocument.getFieldValue("holdingsIdentifier");
            boolean currentStaffOnly = (boolean) solrInputDocument.getFieldValue("staffOnlyFlag");
            for (Object holdingsIdObj : holdingsIdentifier) {
                String holdingsId = (String) holdingsIdObj;
                SolrDocument solrHoldingsDocument = getSolrDocumentByUUID(bibId);
                if (currentHoldingsIdentifier.equalsIgnoreCase(holdingsId)) {
                    if (!currentStaffOnly) {
                        allStaffOnly = false;
                    }
                } else {
                    if (!Boolean.parseBoolean((String) solrHoldingsDocument.getFieldValue("staffOnlyFlag"))) {
                        allStaffOnly = false;
                    }
                }

            }
        }

        if (allStaffOnly) {
            solrBibDocument.setField("staffOnlyFlag", solrInputDocument.getFieldValue("staffOnlyFlag"));
            solrInputDocuments.add(buildSolrInputDocFromSolrDoc(solrBibDocument));
        }
    }


    protected void processHoldingSolrDocumentForUpdate(Object object, List<SolrInputDocument> solrInputDocuments, SolrInputDocument solrDocForHolding) {
        LOG.info("HoldingsOlemlIndexer class");
        Holdings holdings = (Holdings) object;
        List<SolrDocument> solrDocumentList = getSolrDocumentBySolrId(holdings.getId());
        if (CollectionUtils.isNotEmpty(solrDocumentList)) {
            SolrDocument holdingsSolrDocument = solrDocumentList.get(0);

            if (holdingsSolrDocument != null) {
                Object itemIdentifier = holdingsSolrDocument.getFieldValue(ITEM_IDENTIFIER);
                solrDocForHolding = getSolrInputFieldsForHoldings(holdings, solrDocForHolding);
                addBibInfoForHoldingsOrItems(solrDocForHolding, holdingsSolrDocument);

                DocstoreService docstoreService = new DocstoreServiceImpl();
                HoldingsTree holdingsTree = docstoreService.retrieveHoldingsTree(holdings.getId());
                ItemOlemlIndexer itemOlemlIndexer = ItemOlemlIndexer.getInstance();
                for (Item item : holdingsTree.getItems()) {
                    itemOlemlIndexer.updateRecordInSolrForItem(item, solrInputDocuments, solrDocForHolding);
                }

                solrDocForHolding.addField(BIB_IDENTIFIER, holdings.getBib().getId());
                solrDocForHolding.addField(ITEM_IDENTIFIER, itemIdentifier);
                Date date = new Date();
                solrDocForHolding.addField(UPDATED_BY, holdings.getUpdatedBy());
                solrDocForHolding.addField(DATE_UPDATED, date);
                solrInputDocuments.add(solrDocForHolding);
            }
        }
    }

    /**
     * Cheking all input document and modifying if it contains record to be mofiied
     * other wise get it from solar and modify and add it to  input documents
     *
     * @param solrServer
     * @param id
     * @param solrInputDocumentList
     * @throws IOException
     * @throws SolrServerException
     */
    protected void processRecord(SolrServer solrServer, String id, List<SolrInputDocument> solrInputDocumentList) throws IOException, SolrServerException {
        // To find document exists in   incoming
        boolean isDocumentExists = false;
        SolrInputDocument solrInputDocumentExists = null;
        for (SolrInputDocument solrInputDocument : solrInputDocumentList) {
            List<String> ids = new ArrayList<>();
            SolrInputField docType = solrInputDocument.get("DocType");
            if (docType.getValue().equals("bibliographic")) {
                SolrInputField holdingsIds = solrInputDocument.get(HOLDINGS_IDENTIFIER);
                Object object = holdingsIds.getValue();
                if (object instanceof List) {
                    ids.addAll((List) object);
                } else {
                    ids.add((String) object);
                }
                for (Object holdingsId : ids) {
                    if (holdingsId.equals(id)) {
                        solrInputDocumentExists = solrInputDocument;
                        isDocumentExists = true;
                        break;
                    }
                }
            }
        }
        if (!isDocumentExists) {
            processingDocument(solrServer, id, solrInputDocumentList);
        } else {
            solrInputDocumentExists.getFieldValues(HOLDINGS_IDENTIFIER).remove(id);
        }
    }

    /**
     *  Processing document
     * @param solrServer
     * @param id
     * @param solrInputDocumentList
     * @throws SolrServerException
     */
    private void processingDocument(SolrServer solrServer, String id, List<SolrInputDocument> solrInputDocumentList) throws SolrServerException {
        String query = "holdingsIdentifier:" + id + " AND " + "(DocType:bibliographic)";
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        QueryResponse response = solrServer.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        for (SolrDocument bibSolrDocument : solrDocumentList) {
            List<String> instanceIdentifierList = new ArrayList<String>();
            Object instanceIdentifier = bibSolrDocument.getFieldValue("holdingsIdentifier");
            if (instanceIdentifier instanceof List) {
                instanceIdentifierList = (List<String>) bibSolrDocument.getFieldValue("holdingsIdentifier");
                if (instanceIdentifierList.contains(id)) {
                    instanceIdentifierList.remove(id);
                    bibSolrDocument.setField("holdingsIdentifier", instanceIdentifierList);
                }
            } else if (instanceIdentifier instanceof String) {
                String instId = (String) instanceIdentifier;
                if (instId.equalsIgnoreCase(id)) {
                    bibSolrDocument.removeFields("holdingsIdentifier");
                }
            }
            solrInputDocumentList.add(new BibMarcIndexer().buildSolrInputDocFromSolrDoc(bibSolrDocument));
        }
    }

    /**
     * Deleting records and processing records to commit
     * @param solrServer
     * @param id
     * @throws IOException
     * @throws SolrServerException
     */
    protected void deleteRecordInSolr(SolrServer solrServer, String id) throws IOException, SolrServerException {
        List<SolrInputDocument> solrInputDocumentList = new ArrayList<SolrInputDocument>();
        String query = "(holdingsIdentifier:" + id + " AND DocFormat:oleml)";
        UpdateResponse updateResponse = solrServer.deleteByQuery(query);
        query = "(id:" + id + " AND DocType:holdings)";
        solrServer.deleteByQuery(query);
        processingDocument(solrServer, id, solrInputDocumentList);
        indexSolrDocuments(solrInputDocumentList, true);
    }



    public List<SolrDocument> getSolrDocumentBySolrId(String uniqueId) {
        QueryResponse response = null;
        String result = null;
        try {
            String args = "(" + UNIQUE_ID + ":" + uniqueId + ")";
            SolrServer solr = SolrServerManager.getInstance().getSolrServer();
            SolrQuery query = new SolrQuery();
            query.setQuery(args);
            response = solr.query(query);
        } catch (Exception e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException(e.getMessage());
        }
        return response.getResults();
    }


    protected void modifySolrDocForDestination(String destinationBibId, List<String> holdingsIds, List<SolrInputDocument> solrInputDocumentListFinal) {
        updateSolrDocForDestination(destinationBibId, holdingsIds, solrInputDocumentListFinal);
        /*SolrDocument destBibSolrDocument = getSolrDocumentByUUID(destinationBibId);
        destBibSolrDocument.addField("holdingsIdentifier", holdingsIds);
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        buildSolrInputDocFromSolrDoc(destBibSolrDocument, solrInputDocument);
        solrInputDocumentListFinal.add(solrInputDocument);*/
    }

    protected void updateSolrDocForDestination(String destinationBibId, List<String> holdingsIds, List<SolrInputDocument> solrInputDocumentListFinal){

        //Add holdings id to destination bib
        SolrInputDocument destBibInputSolrDocument = new SolrInputDocument();
        destBibInputSolrDocument.addField(AtomicUpdateConstants.UNIQUE_ID, destinationBibId);
        SolrDocument destBibSolrDocument = getSolrDocumentByUUID(destinationBibId);
        addHoldingsDataToBib(destBibSolrDocument,destBibInputSolrDocument,holdingsIds);
        addItemDataToBib(destBibSolrDocument,destBibInputSolrDocument,holdingsIds);
        solrInputDocumentListFinal.add(destBibInputSolrDocument);
    }

    private void addHoldingsDataToBib(SolrDocument destBibSolrDocument, SolrInputDocument destBibInputSolrDocument, List<String> holdingsIds){

        //Get existing holdings ids form bib
        Object holdingsField = destBibSolrDocument.get(HOLDINGS_IDENTIFIER);
        List<String> holdingsIdsList = new ArrayList<>();
        if(holdingsField instanceof String){
            String holdingsIdentifier = (String) holdingsField;
            holdingsIdsList.add(holdingsIdentifier);
        } else if (holdingsField instanceof List) {
            List<String> holdingsIdentifierList = (List<String>) holdingsField;
            holdingsIdsList.addAll(holdingsIdentifierList);
        }

        //Add the holdings ids being transfered to the bib, along with the existing holdings ids
        if(!holdingsIdsList.contains(holdingsIds.get(0))){
            holdingsIdsList.addAll(holdingsIds);
        }
        Map holdingsIdsMap = new HashMap();
        holdingsIdsMap.put(AtomicUpdateConstants.SET, holdingsIdsList);
        destBibInputSolrDocument.setField(HOLDINGS_IDENTIFIER, holdingsIdsMap);
    }

    private void addItemDataToBib(SolrDocument destBibSolrDocument, SolrInputDocument destBibInputSolrDocument, List<String> holdingsIds){
        //Get Existing item ids form bib
        Object itemField = destBibSolrDocument.get(ITEM_IDENTIFIER);
        List<String> itemIdsList = new ArrayList<>();
        if(itemField instanceof String){
            String itemIdentifier = (String) itemField;
            itemIdsList.add(itemIdentifier);
        } else if(itemField instanceof List) {
            List<String> itemIdentifierList = (List<String>) itemField;
            itemIdsList.addAll(itemIdentifierList);
        }

        //Get item ids from the holdings being transfered and add to destination bib document, along with the existing item ids.
        SolrDocumentList holdingsSolrDocumentList = getSolrDocumentByUUIDs(holdingsIds);
        for(SolrDocument solrDocument : holdingsSolrDocumentList){
            Object object = solrDocument.get(ITEM_IDENTIFIER);
            if (object instanceof String) {
                String itemIdentifier = (String) object;
                if(!itemIdsList.contains(itemIdentifier)){
                    itemIdsList.add(itemIdentifier);
                }
            } else if (object instanceof List) {
                List<String> itemIdentifierList = (List<String>) object;
                for(String id:itemIdentifierList){
                    if(!itemIdsList.contains(id)){
                        itemIdsList.add(id);
                    }
                }
            }
        }
        Map<String, List<String>> itemIdsMap = new HashMap<>();
        itemIdsMap.put(AtomicUpdateConstants.SET, itemIdsList);
        destBibInputSolrDocument.setField(ITEM_IDENTIFIER, itemIdsMap);
    }

    protected void updateSolrDocForSource(List<String> holdingsIds, String bibId, List<SolrInputDocument> solrInputDocumentList) {
        SolrDocumentList holdingsSolrDocumentList = getSolrDocumentByUUIDs(holdingsIds);
        SolrDocument destBibSolrDocument = getSolrDocumentByUUID(bibId);
        for (SolrDocument holdingsSolrDocument : holdingsSolrDocumentList) {

            //For each holdings being transfered set the destination bib id
            String sourceBibIdentifier = (String) holdingsSolrDocument.getFieldValue(BIB_IDENTIFIER);
            String sourceHoldingsIdentifier = (String) holdingsSolrDocument.getFieldValue(ID);
            SolrInputDocument holdingsSolrInputDocument = new SolrInputDocument();
            holdingsSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, sourceHoldingsIdentifier);
            Map bibMap = new HashMap();
            bibMap.put(AtomicUpdateConstants.SET, bibId);
            holdingsSolrInputDocument.setField(BIB_IDENTIFIER, bibMap);
            setBibInfoForHoldingsOrItems(holdingsSolrInputDocument, destBibSolrDocument);
            solrInputDocumentList.add(holdingsSolrInputDocument);

            //For each holdings being transfered remove the holdings ids being transfered from the source bib
            SolrInputDocument bibSolrInputDocument = new SolrInputDocument();
            bibSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, sourceBibIdentifier);
            Map<String, String> holdingsIdsMap = new HashMap<String, String>();
            holdingsIdsMap.put(AtomicUpdateConstants.REMOVE, sourceHoldingsIdentifier);
            bibSolrInputDocument.addField(HOLDINGS_IDENTIFIER, holdingsIdsMap);

            //For each holdings being transfered remove the items ids of the associated holdings being transfered from the source bib
            Object object = holdingsSolrDocument.getFieldValue(ITEM_IDENTIFIER);
            List<String> itemIds = new ArrayList<>();
            if (object instanceof String) {
                String itemIdentifier = (String) object;
                itemIds.add(itemIdentifier);
                SolrInputDocument itemSolrInputDocument = new SolrInputDocument();
                itemSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, itemIdentifier);
                itemSolrInputDocument.setField(BIB_IDENTIFIER, bibMap);
                setBibInfoForHoldingsOrItems(itemSolrInputDocument, destBibSolrDocument);
                solrInputDocumentList.add(itemSolrInputDocument);
            } else if (object instanceof List) {
                List<String> itemIdentifierList = (List<String>) object;
                for(String itemIdentifier : itemIdentifierList){
                    SolrInputDocument itemSolrInputDocument = new SolrInputDocument();
                    itemSolrInputDocument.addField(AtomicUpdateConstants.UNIQUE_ID, itemIdentifier);
                    itemSolrInputDocument.setField(BIB_IDENTIFIER, bibMap);
                    setBibInfoForHoldingsOrItems(itemSolrInputDocument, destBibSolrDocument);
                    solrInputDocumentList.add(itemSolrInputDocument);
                }
                itemIds.addAll(itemIdentifierList);
            }
            Map<String, List<String>> itemIdMap = new HashMap<String, List<String>>();
            itemIdMap.put(AtomicUpdateConstants.REMOVE, itemIds);
            bibSolrInputDocument.addField(ITEM_IDENTIFIER, itemIdMap);
            solrInputDocumentList.add(bibSolrInputDocument);
        }

    }

    protected void modifySolrDocForSource(List<String> holdingsIds, String bibId, List<SolrInputDocument> solrInputDocumentList) {
        updateSolrDocForSource(holdingsIds, bibId, solrInputDocumentList);
        /*String sourceBibIdentifier = "";
        SolrDocumentList holdingsSolrDocumentList = getSolrDocumentByUUIDs(holdingsIds);
        for (SolrDocument holdingsSolrDocument : holdingsSolrDocumentList) {
            sourceBibIdentifier = (String) holdingsSolrDocument.getFieldValue("bibIdentifier");
            String sourceHoldingsIdentifier = (String) holdingsSolrDocument.getFieldValue("id");
            removeHoldingsInSourceBib(sourceBibIdentifier, sourceHoldingsIdentifier ,solrInputDocumentList);

            Object object = holdingsSolrDocument.getFieldValue("itemIdentifier");
            //Bib can have more than one item identifier. If there is one item identifier solr will return item identifier string. If there are more than one item identifier solr returns
            //list of item identifiers
            if (object instanceof String) {
                String itemIdentifier = (String) object;
                SolrDocument itemSolrDocument = getSolrDocumentByUUID(itemIdentifier);
                itemSolrDocument.setField("bibIdentifier", bibId);
                SolrInputDocument solrInputDocument = new SolrInputDocument();
                buildSolrInputDocFromSolrDoc(itemSolrDocument, solrInputDocument);
                solrInputDocumentList.add(solrInputDocument);
            } else if (object instanceof List) {
                List<String> itemIdentifierList = (List<String>) object;
                SolrDocumentList itemSolrDocumentList = getSolrDocumentByUUIDs(itemIdentifierList);
                for (SolrDocument itemSolrDocument : itemSolrDocumentList) {
                    itemSolrDocument.setField("bibIdentifier", bibId);
                    SolrInputDocument solrInputDocument = new SolrInputDocument();
                    buildSolrInputDocFromSolrDoc(itemSolrDocument, solrInputDocument);
                    solrInputDocumentList.add(solrInputDocument);
                }
            }

            holdingsSolrDocument.setField("bibIdentifier", bibId);
            SolrInputDocument solrInputDocument = new SolrInputDocument();
            buildSolrInputDocFromSolrDoc(holdingsSolrDocument, solrInputDocument);
            solrInputDocumentList.add(solrInputDocument);
        }*/

    }

    private void removeHoldingsInSourceBib(String bibIdentifier,String holdingsId ,List<SolrInputDocument> solrInputDocumentList) {
        SolrDocument bibSolrDocument = getSolrDocumentByUUID(bibIdentifier);
        Object field = bibSolrDocument.getFieldValue("holdingsIdentifier");
        List bibIdentifierList = null;
        if (field instanceof String) {
            String holdingsIdentifier = (String) bibSolrDocument.getFieldValue("holdingsIdentifier");
            bibIdentifierList = new ArrayList<>();
            bibIdentifierList.add(holdingsIdentifier);

        } else if (field instanceof List) {
            bibIdentifierList = (List) bibSolrDocument.getFieldValue("holdingsIdentifier");
        }
        bibIdentifierList.remove(holdingsId);
        bibSolrDocument.setField("holdingsIdentifier", bibIdentifierList);

        SolrInputDocument solrInputDocument = new SolrInputDocument();
        buildSolrInputDocFromSolrDoc(bibSolrDocument, solrInputDocument);
        solrInputDocumentList.add(solrInputDocument);

    }

    public void addInstIdToBib(String holdingsId, List<SolrInputDocument> solrInputDocuments, SolrDocument bibSolrDoc) {
        if (bibSolrDoc.getFieldValue("holdingsIdentifier") != null) {
            if (bibSolrDoc.getFieldValue("holdingsIdentifier") instanceof List) {
                List instIdList = (List) bibSolrDoc.getFieldValue("holdingsIdentifier");
                instIdList.add(holdingsId);
                bibSolrDoc.setField("holdingsIdentifier", instIdList);
            } else if (bibSolrDoc.getFieldValue("holdingsIdentifier") instanceof String) {
                String instId = (String) bibSolrDoc.getFieldValue("holdingsIdentifier");
                List<String> instIdList = new ArrayList<String>();
                instIdList.add(instId);
                instIdList.add(holdingsId);
                bibSolrDoc.setField("holdingsIdentifier", instIdList);
            }
        } else {
            bibSolrDoc.setField("holdingsIdentifier", holdingsId);
        }
        solrInputDocuments.add(buildSolrInputDocFromSolrDoc(bibSolrDoc));

    }

    public String getAllTextValueForHoldings(OleHoldings oleHoldings) {
        StringBuffer sb = new StringBuffer();
        String holdingsIdentifier = oleHoldings.getHoldingsIdentifier();
        String accessStatus = oleHoldings.getAccessStatus();
        String copyNumber = oleHoldings.getCopyNumber();
        String donorNote = oleHoldings.getDonorNote();
        String donorPublicDisplay = oleHoldings.getDonorPublicDisplay();
        String eResourceId = oleHoldings.getEResourceId();
        String holdingsType = oleHoldings.getHoldingsType();
        String imprint = oleHoldings.getImprint();
        String localPersistentLink = oleHoldings.getLocalPersistentLink();
        String primary = oleHoldings.getPrimary();
        String publisher = oleHoldings.getPublisher();
        String receiptStatus = oleHoldings.getReceiptStatus();
        String statusDate = oleHoldings.getStatusDate();
        String subscriptionStatus = oleHoldings.getSubscriptionStatus();


        appendData(sb, holdingsIdentifier);
        appendData(sb, accessStatus);
        appendData(sb, copyNumber);
        appendData(sb, donorNote);
        appendData(sb, donorPublicDisplay);
        appendData(sb, eResourceId);
        appendData(sb, holdingsType);
        appendData(sb, imprint);
        appendData(sb, localPersistentLink);
        appendData(sb, primary);
        appendData(sb, publisher);
        appendData(sb, receiptStatus);
        appendData(sb, statusDate);
        appendData(sb, subscriptionStatus);

        for (ExtentOfOwnership extentOfOwnership : oleHoldings.getExtentOfOwnership()) {
            if (extentOfOwnership != null) {
                String textualHoldings = extentOfOwnership.getTextualHoldings();
                String type = extentOfOwnership.getType();
                appendData(sb, textualHoldings);
                appendData(sb, type);
                Coverages coverages = extentOfOwnership.getCoverages();
                PerpetualAccesses perpetualAccesses = extentOfOwnership.getPerpetualAccesses();
                if(coverages != null) {
                    for (Coverage coverage : coverages.getCoverage()) {
                        if (coverage != null) {
                            String coverageEndDate = coverage.getCoverageEndDate();
                            String coverageEndDateFormat = coverage.getCoverageEndDateFormat();
                            String coverageEndDateString = coverage.getCoverageEndDateString();
                            String coverageEndIssue = coverage.getCoverageEndIssue();
                            String coverageEndVolume = coverage.getCoverageEndVolume();
                            String coverageStartDate = coverage.getCoverageStartDate();
                            String coverageStartVolume = coverage.getCoverageStartVolume();
                            String coverageStartIssue = coverage.getCoverageStartIssue();
                            String coverageStartDateFormat = coverage.getCoverageStartDateFormat();
                            String coverageStartDateString = coverage.getCoverageStartDateString();
                            appendData(sb, coverageEndDate);
                            appendData(sb, coverageEndDateFormat);
                            appendData(sb, coverageEndDateString);
                            appendData(sb, coverageEndIssue);
                            appendData(sb, coverageEndVolume);
                            appendData(sb, coverageStartDate);
                            appendData(sb, coverageStartVolume);
                            appendData(sb, coverageStartIssue);
                            appendData(sb, coverageStartDateFormat);
                            appendData(sb, coverageStartDateString);
                        }

                    }

                }
                if(perpetualAccesses != null) {
                    for (PerpetualAccess perpetualAccess : perpetualAccesses.getPerpetualAccess()) {

                        if (perpetualAccess != null) {
                            String perpetualAccessEndDate = perpetualAccess.getPerpetualAccessEndDate();
                            String perpetualAccessEndDateFormat = perpetualAccess.getPerpetualAccessEndDateFormat();
                            String perpetualAccessEndDateString = perpetualAccess.getPerpetualAccessEndDateString();
                            String perpetualAccessEndIssue = perpetualAccess.getPerpetualAccessEndIssue();
                            String perpetualAccessEndVolume = perpetualAccess.getPerpetualAccessEndVolume();
                            String perpetualAccessStartDate = perpetualAccess.getPerpetualAccessStartDate();
                            String perpetualAccessStartDateFormat = perpetualAccess.getPerpetualAccessStartDateFormat();
                            String perpetualAccessStartDateString = perpetualAccess.getPerpetualAccessStartDateString();
                            String perpetualAccessStartIssue = perpetualAccess.getPerpetualAccessStartIssue();
                            String perpetualAccessStartVolume = perpetualAccess.getPerpetualAccessStartVolume();

                            appendData(sb, perpetualAccessEndDate);
                            appendData(sb, perpetualAccessEndDateFormat);
                            appendData(sb, perpetualAccessEndDateString);
                            appendData(sb, perpetualAccessEndIssue);
                            appendData(sb, perpetualAccessEndVolume);
                            appendData(sb, perpetualAccessStartDate);
                            appendData(sb, perpetualAccessStartDateFormat);
                            appendData(sb, perpetualAccessStartDateString);
                            appendData(sb, perpetualAccessStartIssue);
                            appendData(sb, perpetualAccessStartVolume);
                        }

                    }

                }

                for (Note note : extentOfOwnership.getNote()) {
                    if (note != null) {
                        String noteType = note.getType();
                        String noteValue = note.getValue();
                        appendData(sb, noteType);
                        appendData(sb, noteValue);
                    }
                }
            }
        }

        CallNumber callNumber = oleHoldings.getCallNumber();
        if(callNumber!=null && StringUtils.isNotEmpty(callNumber.getNumber())) {
            String number = callNumber.getNumber();
            String prefix = callNumber.getPrefix();
            String classificationPart = callNumber.getClassificationPart();
            String itemPart = callNumber.getItemPart();
            String type = callNumber.getType();
            appendData(sb, number);
            appendData(sb, prefix);
            appendData(sb, classificationPart);
            appendData(sb, itemPart);
            appendData(sb, type);
            if(callNumber.getShelvingScheme() != null) {
                ShelvingScheme shelvingScheme = callNumber.getShelvingScheme();
                String shelvingSchemeCodeValue = shelvingScheme.getCodeValue();
                String shelvingSchemeFullValue = shelvingScheme.getFullValue();
                appendData(sb, shelvingSchemeCodeValue);
                appendData(sb, shelvingSchemeFullValue);
                if(callNumber.getShelvingScheme().getTypeOrSource() != null) {
                    String shelvingSchemePointer = shelvingScheme.getTypeOrSource().getPointer();
                    String shelvingSchemeText = shelvingScheme.getTypeOrSource().getText();
                    appendData(sb, shelvingSchemePointer);
                    appendData(sb, shelvingSchemeText);
                }
            }

            if(callNumber.getShelvingOrder() != null) {
                ShelvingOrder shelvingOrder = callNumber.getShelvingOrder();
                String shelvingOrderCodeValue = shelvingOrder.getCodeValue();
                String shelvingOrderFullValue = shelvingOrder.getFullValue();
                appendData(sb, shelvingOrderCodeValue);
                appendData(sb, shelvingOrderFullValue);
                if (callNumber.getShelvingOrder().getTypeOrSource() != null) {
                    String shelvingOrderPointer = shelvingOrder.getTypeOrSource().getPointer();
                    String shelvingOrderText = shelvingOrder.getTypeOrSource().getText();
                    appendData(sb, shelvingOrderPointer);
                    appendData(sb, shelvingOrderText);
                }

            }
        }

        for (DonorInfo donorInfo : oleHoldings.getDonorInfo()) {
            if(donorInfo != null) {
                String donorCode = donorInfo.getDonorCode();
                String donorInfoNote = donorInfo.getDonorNote();
                String donorInfoPublicDisplay = donorInfo.getDonorPublicDisplay();
                appendData(sb, donorCode);
                appendData(sb, donorInfoNote);
                appendData(sb, donorInfoPublicDisplay);
            }
        }

        HoldingsAccessInformation holdingsAccessInformation = oleHoldings.getHoldingsAccessInformation();
        if(holdingsAccessInformation != null) {
            String accessLocation = holdingsAccessInformation.getAccessLocation();
            String accessPassword = holdingsAccessInformation.getAccessPassword();
            String accessUsername = holdingsAccessInformation.getAccessUsername();
            String materialsSpecified = holdingsAccessInformation.getMaterialsSpecified();
            String firstIndicator = holdingsAccessInformation.getFirstIndicator();
            String secondIndicator = holdingsAccessInformation.getSecondIndicator();
            String authenticationType = holdingsAccessInformation.getAuthenticationType();
            String numberOfSimultaneousUser = holdingsAccessInformation.getNumberOfSimultaneousUser();
            String proxiedResource = holdingsAccessInformation.getProxiedResource();
            appendData(sb, accessLocation);
            appendData(sb, accessPassword);
            appendData(sb, accessUsername);
            appendData(sb, authenticationType);
            appendData(sb, numberOfSimultaneousUser);
            appendData(sb, proxiedResource);
            appendData(sb, materialsSpecified);
            appendData(sb, firstIndicator);
            appendData(sb, secondIndicator);
        }

        for (Link link : oleHoldings.getLink()) {
            if(link != null) {
                String text = link.getText();
                String url = link.getUrl();
                appendData(sb, text);
                appendData(sb, url);
            }
        }

        for (Note note : oleHoldings.getNote()) {
            if(note != null) {
                String noteValue = note.getValue();
                String noteType = note.getType();
                appendData(sb, noteValue);
                appendData(sb, noteType);
            }

        }
        Platform platform = oleHoldings.getPlatform();
        if(platform != null) {
            String adminPassword = platform.getAdminPassword();
            String adminUrl = platform.getAdminUrl();
            String adminUserName = platform.getAdminUserName();
            String platformName = platform.getPlatformName();
            appendData(sb, adminPassword);
            appendData(sb, adminUrl);
            appendData(sb, adminUserName);
            appendData(sb, platformName);
        }

        StatisticalSearchingCode statisticalSearchingCode = oleHoldings.getStatisticalSearchingCode();
        if(statisticalSearchingCode != null) {
            String codeValue = statisticalSearchingCode.getCodeValue();
            String fullValue = statisticalSearchingCode.getFullValue();
            appendData(sb, codeValue);
            appendData(sb, fullValue);
            if(statisticalSearchingCode.getTypeOrSource() != null) {
                String text = statisticalSearchingCode.getTypeOrSource().getText();
                String pointer = statisticalSearchingCode.getTypeOrSource().getPointer();
                appendData(sb, text);
                appendData(sb, pointer);
            }
        }

        for (Uri uri : oleHoldings.getUri()) {
            if(uri != null) {
                String value = uri.getValue();
                String resolvable = uri.getResolvable();
                appendData(sb, value);
                appendData(sb, resolvable);
            }
        }

        buildLocationNameAndLocationLevel(oleHoldings.getLocation(), sb, sb);
        return sb.toString();
    }

}
