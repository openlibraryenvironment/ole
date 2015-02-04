package org.kuali.ole.select.lookup;

import org.joda.time.DateTime;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.bo.OLEPurchaseOrderSearch;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

import java.math.BigInteger;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 11/6/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPurchaseOrderSearchLookupableImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEPurchaseOrderSearchLookupableImpl.class);

    private BusinessObjectService businessObjectService;
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }
    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        List<String> bibDetails = new ArrayList<>();
        List <OLEPurchaseOrderSearch> finalOlePurchaseOrderSearch=new ArrayList<>();

        String poId;
        String title;
        String author;
        String vendorName;
        String issn;
        String instanceId;


        poId = searchCriteria.get("poId")!=null? searchCriteria.get("poId") :"";
        title = searchCriteria.get("title")!=null?searchCriteria.get("title") :"";
        author = searchCriteria.get("author")!=null?searchCriteria.get("author"):"";
        issn = searchCriteria.get("issn")!=null?searchCriteria.get("issn"):"";
        vendorName= searchCriteria.get("vendorName")!=null?searchCriteria.get("vendorName"):"";
        instanceId=searchCriteria.get("instanceId")!=null?searchCriteria.get("instanceId"):"";




        if((!poId.contains("*"))&&(!instanceId.contains("*"))){

            boolean docsearch=true;
            DocumentSearchCriteria.Builder docSearchCriteria = DocumentSearchCriteria.Builder.create();
            docSearchCriteria.setDocumentTypeName(PurapConstants.PurapDocTypeCodes.PO_DOCUMENT);
            Map<String, List<String>> fixedParameters=new HashMap<>();
            // fixedParameters.put("displayType", Arrays.asList("document"));
            if(!poId.isEmpty())
                fixedParameters.put("purapDocumentIdentifier",Arrays.asList(poId));
            if(!vendorName.isEmpty())
                fixedParameters.put("vendorName", Arrays.asList(vendorName));
            Map<String, List<String>> attributes = new HashMap<String, List<String>>();
            if (docSearchCriteria != null) {
                if (!fixedParameters.isEmpty()) {
                    for (String propertyField : fixedParameters.keySet()) {
                        if (fixedParameters.get(propertyField) != null) {
                            attributes.put(propertyField, fixedParameters.get(propertyField));
                        }
                    }
                }
            }
            List<DocumentStatus>     documentStatuses=new ArrayList<>();
            documentStatuses.add(DocumentStatus.FINAL);
            docSearchCriteria.setDocumentAttributeValues(attributes);
            Date currentDate = new Date();
            docSearchCriteria.setDateCreatedTo(new DateTime(currentDate));
            docSearchCriteria.setApplicationDocumentStatus(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN);
            //docSearchCriteria.setDocumentStatuses(documentStatuses);
            DocumentSearchCriteria docSearchCriteriaDTO = docSearchCriteria.build();
            DocumentSearchResults components = null;
            components = KEWServiceLocator.getDocumentSearchService().lookupDocuments(GlobalVariables.getUserSession().getPrincipalId(), docSearchCriteriaDTO);
            List<DocumentSearchResult> docSearchResults = components.getSearchResults();

            if (!docSearchResults.isEmpty()) {
                if(!title.isEmpty()||!author.isEmpty()||!issn.isEmpty()) {
                    try {
                        bibDetails=getBibDetails(searchCriteria);
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    if(bibDetails!=null&&bibDetails.size()>0)
                        docsearch=true;
                    else
                        docsearch=false;

                }
                HashMap<String,String> instanceMap = new HashMap<>();
                for (DocumentSearchResult searchResult : docSearchResults) {
                    List<DocumentAttribute> documentAttributeList= searchResult.getDocumentAttributeByName("itemTitleId");
                    for(DocumentAttribute itemTitleId:documentAttributeList){
                        String bibId=((String)itemTitleId.getValue());
                        Map parentCriterial = new HashMap();
                        parentCriterial.put(OLEConstants.BIB_ID,bibId);
                        List<OleCopy> copyList = (List<OleCopy>) businessObjectService.findMatching(OleCopy.class,parentCriterial);
                        if(docsearch && copyList!=null && copyList.size()>0){
                            if(bibDetails!=null&&bibDetails.size()>0){
                                if(bibDetails.contains(bibId)){
                                    getOlePurchaseOrderSearchList(finalOlePurchaseOrderSearch,searchResult,bibId,instanceId,instanceMap);
                                }

                            }else{
                                getOlePurchaseOrderSearchList(finalOlePurchaseOrderSearch,searchResult,bibId,instanceId,instanceMap);
                            }
                        }
                    }
                }
            }
        }
        if(GlobalVariables.getMessageMap().getWarningCount()>0){
            GlobalVariables.getMessageMap().getWarningMessages().clear();
        }
        if (finalOlePurchaseOrderSearch.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, org.kuali.ole.OLEConstants.NO_RECORD_FOUND);
        }


        return finalOlePurchaseOrderSearch;

    }
    private List<String> getBibDetails( Map<String, String> searchCriteria) throws Exception {
        SearchParams searchParams=new SearchParams();
        List<String> bibIdList=new ArrayList<>();
        String title = ((String) searchCriteria.get("title"))!=null?searchCriteria.get("title") :"";
        String author = ((String) searchCriteria.get("author"))!=null?searchCriteria.get("author") :"";
        String issn = ((String) searchCriteria.get("issn"))!=null?searchCriteria.get("issn") :"";
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("",searchParams.buildSearchField("bibliographic","TITLE",title),"AND"));
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("",searchParams.buildSearchField("bibliographic","AUTHOR",author),"AND"));
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField("bibliographic", "ISSN", issn), "AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic","id"));
        SearchResponse searchResponses=getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        for(SearchResult searchResults:searchResponses.getSearchResults()){
            for(SearchResultField searchResultField:searchResults.getSearchResultFields()){
                   if(searchResultField.getDocType().equalsIgnoreCase("bibliographic") && searchResultField.getFieldName().equalsIgnoreCase("id")){
                                 bibIdList.add(searchResultField.getFieldValue());
                }
            }
        }
            return bibIdList;

    }
    private List<OLEPurchaseOrderSearch> getOlePurchaseOrderSearchList(List<OLEPurchaseOrderSearch> finalOlePurchaseOrderSearch, DocumentSearchResult searchResult, String bibId, String instance_Id, HashMap<String, String> instanceMap) {

        try {
            // List<HashMap<String, Object>> documentList = queryService.retriveResults("(DocType:bibliographic) AND (id:" + bibId + ")");
            BibTree bibTree = new BibTree();
            if (bibId != null && bibId != "") {
                bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibId);
            }
            String instance = instance_Id != null ? instance_Id : "";
            OLEPurchaseOrderSearch olePurchaseOrderSearch = new OLEPurchaseOrderSearch();
            olePurchaseOrderSearch.setBibId(bibId);
            olePurchaseOrderSearch.setTitle(bibTree.getBib().getTitle());

            olePurchaseOrderSearch.setAuthor(bibTree.getBib().getAuthor());

            olePurchaseOrderSearch.setIssn(bibTree.getBib().getIssn());

            olePurchaseOrderSearch.setPublisher(bibTree.getBib().getPublisher());

            olePurchaseOrderSearch.setLocalId(DocumentUniqueIDPrefix.getDocumentId(bibId));


            for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                HoldingOlemlRecordProcessor holdingOlemlRecordProcessor=new HoldingOlemlRecordProcessor();
                OleHoldings oleHoldings=holdingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
                Map parentCriterial = new HashMap();
                parentCriterial.put(OLEConstants.BIB_ID, bibId);
                parentCriterial.put(OLEConstants.INSTANCE_ID, holdingsTree.getHoldings().getId());
                List<OleCopy> copyList = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, parentCriterial);
                boolean existingInstance = false;
                for (OleCopy copy : copyList) {
                                if((copy.getSerialReceivingIdentifier()!=null && !copy.getSerialReceivingIdentifier().isEmpty())
                                        || !copy.getPoDocNum().equalsIgnoreCase(searchResult.getDocument().getDocumentId()) ){
                        existingInstance = true;
                        break;
                    }
                }
                parentCriterial = new HashMap();
                parentCriterial.put("poId", ((BigInteger) searchResult.getDocumentAttributeByName("purapDocumentIdentifier").get(0).getValue()).intValue());
                parentCriterial.put(OLEConstants.INSTANCE_ID, holdingsTree.getHoldings().getId());
                List<OLESerialReceivingDocument> oleSerialReceivingDocuments = (List<OLESerialReceivingDocument>) getBusinessObjectService().findMatching(OLESerialReceivingDocument.class, parentCriterial);
                if (oleSerialReceivingDocuments != null && oleSerialReceivingDocuments.size() > 0) {
                    existingInstance = true;
                }
                if (instanceMap.containsKey(holdingsTree.getHoldings().getId())) {
                    existingInstance = true;
                }
                if ((instance.equalsIgnoreCase((String) holdingsTree.getHoldings().getId()) || instance.isEmpty()) && copyList != null && copyList.size() > 0 && !existingInstance) {


                    olePurchaseOrderSearch.setCopyNumber(oleHoldings.getCopyNumber());
                    olePurchaseOrderSearch.setInstanceId((String) holdingsTree.getHoldings().getId());
                    olePurchaseOrderSearch.setCallNumber(oleHoldings.getCallNumber()!=null ? oleHoldings.getCallNumber().getNumber() : null);

                    Map vendorNameMap = new HashMap();
                    List<DocumentAttribute> vendnm = searchResult.getDocumentAttributeByName("vendorName");
                    String ttt = (String) searchResult.getDocumentAttributeByName("vendorName").get(0).getValue();
                    vendorNameMap.put("vendorName", vendnm.get(0).getValue());
                    List<VendorDetail> vendorDetailList = (List) getBusinessObjectService().findMatching(VendorDetail.class, vendorNameMap);
                    if (vendorDetailList != null && vendorDetailList.size() > 0) {

                        olePurchaseOrderSearch.setVendorId(vendorDetailList.get(0).getVendorHeaderGeneratedIdentifier().toString()
                                + "-" + vendorDetailList.get(0).getVendorDetailAssignedIdentifier().toString());
                    }
                    Map vendorAliasMap = new HashMap();
                    vendorAliasMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_HEADER_IDENTIFIER, vendorDetailList.get(0).getVendorHeaderGeneratedIdentifier());
                    vendorAliasMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_DETAIL_IDENTIFIER, vendorDetailList.get(0).getVendorDetailAssignedIdentifier());
                    List<VendorAlias> vendorAliasList = (List) getBusinessObjectService().findMatching(VendorAlias.class, vendorAliasMap);

                    if (vendorAliasList != null && vendorAliasList.size() > 0) {
                        olePurchaseOrderSearch.setVendorAliasName(vendorAliasList.get(0).getVendorAliasName());
                    }

                    olePurchaseOrderSearch.setPoId(((BigInteger) searchResult.getDocumentAttributeByName("purapDocumentIdentifier").get(0).getValue()).intValue());
                    olePurchaseOrderSearch.setPoIdLink(searchResult.getDocument().getDocumentId());
                    olePurchaseOrderSearch.setVendorName((String) searchResult.getDocumentAttributeByName("vendorName").get(0).getValue());
                    instanceMap.put((String) holdingsTree.getHoldings().getId(), (String) holdingsTree.getHoldings().getId());
                    finalOlePurchaseOrderSearch.add(olePurchaseOrderSearch);
                }

            }


        } catch (Exception e) {

            LOG.error("Exception while getting PurchaseOrderSearchList------> " + e);
            e.printStackTrace();

        }

        return finalOlePurchaseOrderSearch;
    }
}