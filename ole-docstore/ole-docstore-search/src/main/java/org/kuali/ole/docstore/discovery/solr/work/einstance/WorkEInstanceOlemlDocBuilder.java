package org.kuali.ole.docstore.discovery.solr.work.einstance;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.discovery.solr.work.bib.DocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.bib.WorkBibCommonFields;
import org.kuali.ole.docstore.discovery.solr.work.bib.marc.WorkBibMarcDocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.instance.WorkInstanceCommonFields;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.*;
import org.kuali.ole.docstore.model.xstream.work.oleml.WorkEHoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.model.xstream.work.oleml.WorkEInstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.utility.XMLUtility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 7/18/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkEInstanceOlemlDocBuilder extends DocBuilder implements WorkBibCommonFields, WorkInstanceCommonFields, WorkEInstanceCommonFilds {


    public void buildSolrInputDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments) {
        if (requestDocument != null && requestDocument.getOperation() != null && "checkIn"
                .equalsIgnoreCase(requestDocument.getOperation())) {
            updateRecordContentInSolr(requestDocument, solrInputDocuments);
        } else if (requestDocument != null) {
            buildSolrInputDocuments(requestDocument, solrInputDocuments);
        }
    }

    public void buildSolrInputDocuments(RequestDocument requestDocument,
                                        List<SolrInputDocument> solrInputDocuments) {
        InstanceCollection instanceCollection = new WorkEInstanceOlemlRecordProcessor().fromXML(requestDocument.getContent().getContent());
        if (instanceCollection != null) {
            for (EInstance eInstance : instanceCollection.getEInstance()) {
                buildSolrInputDocumentsForEInstance(eInstance, solrInputDocuments);
                buildSolrInputDocumentsForEHoldings(eInstance, solrInputDocuments, requestDocument);
            }
        }

    }

    public void buildSolrInputDocumentsForEInstance(EInstance eInstance, List<SolrInputDocument> solrInputDocuments) {
        SolrInputDocument solrDocForInstance = new SolrInputDocument();
        solrDocForInstance.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDocForInstance.addField(DOC_TYPE, DocType.EINSTANCE.getCode());
        solrDocForInstance.addField(DOC_FORMAT, DocFormat.OLEML.getCode());

        for (String rid : eInstance.getResourceIdentifier()) {
            WorkBibMarcDocBuilder marcDocBuilder = new WorkBibMarcDocBuilder();
            marcDocBuilder.addInstIdToBib(rid, eInstance.getInstanceIdentifier(), solrInputDocuments);
            solrDocForInstance.addField(BIB_IDENTIFIER, rid);
        }
        solrDocForInstance.addField(HOLDINGS_IDENTIFIER, eInstance.getEHoldings().getHoldingsIdentifier());
        solrDocForInstance.addField(ID, eInstance.getInstanceIdentifier());
        solrDocForInstance.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(eInstance.getInstanceIdentifier()));
        solrDocForInstance.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(eInstance.getInstanceIdentifier()));
        solrDocForInstance.addField(UNIQUE_ID, eInstance.getInstanceIdentifier());

        solrInputDocuments.add(solrDocForInstance);
    }


    public void buildSolrInputDocumentsForEHoldings(EInstance eInstance, List<SolrInputDocument> solrInputDocuments, RequestDocument requestDocument) {

        SolrInputDocument solrDocForEInstance = new SolrInputDocument();
        Date date = new Date();
        if(requestDocument.getOperation() != null && requestDocument.getOperation().equals("checkIn")) {
            solrDocForEInstance.addField(DATE_UPDATED, date);
        }
        XMLUtility xmlUtility = new XMLUtility();
        WorkEInstanceOlemlRecordProcessor workEInstanceOlemlRecordProcessor = new WorkEInstanceOlemlRecordProcessor();

        EHoldings eHoldings = eInstance.getEHoldings();
        String eHoldingsXml = workEInstanceOlemlRecordProcessor.toXML(eHoldings);
        solrDocForEInstance.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDocForEInstance.addField(DOC_TYPE, DocType.EHOLDINGS.getCode());
        solrDocForEInstance.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
        solrDocForEInstance.addField(ID, eHoldings.getHoldingsIdentifier());

        solrDocForEInstance.addField(ALL_TEXT, xmlUtility.getAllContentText(eHoldingsXml));

        solrDocForEInstance.addField(STAFF_ONLY_FLAG, eHoldings.isStaffOnlyFlag());
        solrDocForEInstance.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(eHoldings.getHoldingsIdentifier()));
        solrDocForEInstance.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(eHoldings.getHoldingsIdentifier()));


        solrDocForEInstance.addField(INSTANCE_IDENTIFIER, eInstance.getInstanceIdentifier());

        solrDocForEInstance.addField(ACCESS_STATUS_DISPLAY, eHoldings.getAccessStatus());
        solrDocForEInstance.addField(ACCESS_STATUS_SEARCH, eHoldings.getAccessStatus());

        solrDocForEInstance.addField(STATISTICAL_SEARCHING_CODE_VALUE_SEARCH, eHoldings.getStatisticalSearchingCode());
        solrDocForEInstance.addField(STATISTICAL_SEARCHING_CODE_VALUE_DISPLAY, eHoldings.getStatisticalSearchingCode());

        solrDocForEInstance.addField(PUBLISHER_SEARCH, eHoldings.getPublisher());

        solrDocForEInstance.addField(IMPRINT_SEARCH, eHoldings.getImprint());
        solrDocForEInstance.addField(IMPRINT_DISPLAY, eHoldings.getImprint());
        solrDocForEInstance.addField(ERESOURCE_NAME_DISPLAY, eHoldings.getEResourceTitle());


        StringBuffer locationName = new StringBuffer();
        StringBuffer locationLevel = new StringBuffer();
        Location location = eHoldings.getLocation();
        if (location != null && location.getLocationLevel() != null) {
            buildLocationNameAndLocationLevel(location, locationName, locationLevel);
            solrDocForEInstance.addField(LOCATION_LEVEL_SEARCH, locationName.toString());
            solrDocForEInstance.addField(LOCATION_LEVEL_NAME_SEARCH, locationLevel.toString());
            solrDocForEInstance.addField(LOCATION_LEVEL_DISPLAY, locationName.toString());
            solrDocForEInstance.addField(LOCATION_LEVEL_NAME_DISPLAY, locationLevel.toString());
        }

        solrDocForEInstance.addField(SUBSCRIPTION_SEARCH, eHoldings.getSubscriptionStatus());
        solrDocForEInstance.addField(SUBSCRIPTION_STATUS_DISPLAY, eHoldings.getSubscriptionStatus());

        if (eHoldings.getCallNumber() != null) {
            solrDocForEInstance.addField(CALL_NUMBER_SEARCH, eHoldings.getCallNumber().getNumber());
            solrDocForEInstance.addField(CALL_NUMBER_PREFIX_SEARCH, eHoldings.getCallNumber().getPrefix());
            if (eHoldings.getCallNumber().getCallNumberType() != null) {
                solrDocForEInstance.addField(CALL_NUMBER_TYPE_SEARCH, eHoldings.getCallNumber().getCallNumberType().getFullValue());
            }
            if (eHoldings.getCallNumber().getShelvingOrder() != null) {
                solrDocForEInstance.addField(SHELVING_ORDER_SEARCH, eHoldings.getCallNumber().getShelvingOrder().getFullValue());
            }
        }
        if (eHoldings.getPlatform() != null) {
            solrDocForEInstance.addField(URL_SEARCH, eHoldings.getPlatform().getAdminUrl());
            solrDocForEInstance.addField(PLATFORM_DISPLAY, eHoldings.getPlatform().getPlatformName());
            solrDocForEInstance.addField(PLATFORM_SEARCH, eHoldings.getPlatform().getPlatformName());
        }
        if (eHoldings.getAccessInformation() != null) {
            solrDocForEInstance.addField(AUTHENTICATION_SEARCH, eHoldings.getAccessInformation().getAuthenticationType());
            solrDocForEInstance.addField(PROXIED_SEARCH, eHoldings.getAccessInformation().getProxiedResource());
            solrDocForEInstance.addField(NUMBER_OF_SIMULTANEOUS_USERS_SEARCH, eHoldings.getAccessInformation().getNumberOfSimultaneousUser());
            solrDocForEInstance.addField(ACCESS_LOCATION_SEARCH, eHoldings.getAccessInformation().getAccessLocation());
        }
        solrDocForEInstance.addField(PUBLIC_NOTE_DISPLAY, eHoldings.getDonorPublicDisplay());
        solrDocForEInstance.addField(E_PUBLISHER_DISPLAY, eHoldings.getPublisher());
        if (eHoldings.getLink() != null) {
            solrDocForEInstance.addField(URL_DISPLAY, eHoldings.getLink().getUrl());
        }
        for (String rid : eInstance.getResourceIdentifier()) {
            solrDocForEInstance.addField(BIB_IDENTIFIER, rid);
        }
        if(eHoldings.getExtentOfOwnership() != null && eHoldings.getExtentOfOwnership().getCoverages() != null && eHoldings.getExtentOfOwnership().getCoverages().getCoverage() != null) {
            List<Coverage> coverageList = eHoldings.getExtentOfOwnership().getCoverages().getCoverage();
            for(Coverage coverage : coverageList) {
                solrDocForEInstance.addField(E_INSTANCE_COVERAGE_DATE,coverage.getCoverageStartDate() + "-"+coverage.getCoverageEndDate() );
            }
        }

        solrInputDocuments.add(solrDocForEInstance);
    }

    public void updateRecordContentInSolr(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments) {
        IndexerService indexerService = getIndexerService(requestDocument);
        SolrDocument solrDocument = new SolrDocument();
        List<SolrDocument> solrDocumentList = new ArrayList<SolrDocument>();
        if (requestDocument.getId() != null && requestDocument.getId().length() > 0) {
            solrDocumentList = indexerService.getSolrDocumentBySolrId(requestDocument.getUuid());
            if (solrDocumentList != null && solrDocumentList.size() > 0) {
                solrDocument = solrDocumentList.get(0);
            }
            if (requestDocument.getType().equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
                updateSolrDocument(requestDocument, solrInputDocuments, solrDocument);
            }
        }
    }

    private void updateSolrDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments, SolrDocument solrDocument) {
        if (requestDocument.getType().equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
            String holdingsXml = requestDocument.getContent().getContent();
            WorkEHoldingOlemlRecordProcessor recordProcessor = new WorkEHoldingOlemlRecordProcessor();
            EHoldings holdingContent = recordProcessor.fromXML(holdingsXml);
            EInstance eInstance = new EInstance();
            eInstance.setEHoldings(holdingContent);
            if (solrDocument != null) {
                if (solrDocument.getFieldValue(INSTANCE_IDENTIFIER) != null && solrDocument
                        .getFieldValue(INSTANCE_IDENTIFIER) instanceof List) {
                    List<String> instanceIdList = (List<String>) solrDocument.getFieldValue(INSTANCE_IDENTIFIER);
                    for (String instanceId : instanceIdList) {
                        eInstance.setInstanceIdentifier(instanceId);
                    }
                } else if (solrDocument.getFieldValue(INSTANCE_IDENTIFIER) != null && solrDocument
                        .getFieldValue(INSTANCE_IDENTIFIER) instanceof String) {
                    eInstance.setInstanceIdentifier((String) solrDocument.getFieldValue(INSTANCE_IDENTIFIER));
                }
                holdingContent.setHoldingsIdentifier(requestDocument.getId());
                List<String> reId = new ArrayList<String>();
                if (solrDocument.getFieldValue(BIB_IDENTIFIER) != null && solrDocument
                        .getFieldValue(BIB_IDENTIFIER) instanceof List) {
                    List<String> idList = (List<String>) solrDocument.getFieldValue(BIB_IDENTIFIER);
                    for (String bibId : idList) {
                        reId.add(bibId);
                    }
                } else if (solrDocument.getFieldValue(BIB_IDENTIFIER) != null && solrDocument
                        .getFieldValue(BIB_IDENTIFIER) instanceof String) {
                    reId.add((String) solrDocument.getFieldValue(BIB_IDENTIFIER));
                }
                eInstance.getResourceIdentifier().addAll(reId);
            }
            buildSolrInputDocumentsForEHoldings(eInstance, solrInputDocuments, requestDocument);
        }

    }

    private void buildLocationNameAndLocationLevel(Location location, StringBuffer locationName, StringBuffer locationLevel) {
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

}
