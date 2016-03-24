package org.kuali.ole.deliver.batch;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleRecentlyReturned;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 4/4/13
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleShelvingLagTime {

    private static final Logger LOG = Logger.getLogger(OleShelvingLagTime.class);
    private static final String solrMaxPageSize = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OleCirculationDesk.SOLR_MAX_PAGE_SIZE);

    private BusinessObjectService businessObjectService;

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }


    public void updateStatusIntoAvailableAfterReShelving() throws Exception {
        businessObjectService = getBusinessObjectService();
        LoanProcessor loanProcessor = new LoanProcessor();
        StringBuffer query = new StringBuffer("");
        query.append("(ItemStatus_display:RECENTLY-RETURNED)");
        org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
        org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
        SearchResponse searchResponse = null;
        search_Params.getSearchConditions().add(search_Params.buildSearchCondition("", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_STATUS, "RECENTLY-RETURNED"), ""));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));
        if (StringUtils.isNotBlank(solrMaxPageSize)) {
            search_Params.setPageSize(Integer.parseInt(solrMaxPageSize));
        }
        searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
        String itemUUID = null;
        for (SearchResult searchResult : searchResponse.getSearchResults()) {
            try{
            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                String fieldName = searchResultField.getFieldName();
                String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                    itemUUID = DocumentUniqueIDPrefix.getDocumentId(fieldValue);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("itemUuid", itemUUID);
                    OleRecentlyReturned oleRecentlyReturned = businessObjectService.findByPrimaryKey(OleRecentlyReturned.class, map);
                    if (oleRecentlyReturned != null) {
                        map = new HashMap<String, String>();
                        map.put("circulationDeskId", oleRecentlyReturned.getCirculationDeskId());
                        OleCirculationDesk oleCirculationDesk = businessObjectService.findByPrimaryKey(OleCirculationDesk.class, map);
                        if (oleCirculationDesk != null) {
                            Integer shelvingLagTime = Integer.parseInt(oleCirculationDesk.getShelvingLagTime());
                            if (LOG.isDebugEnabled()){
                                LOG.debug("shelvingLagTime" + shelvingLagTime);
                            }
                            String itemXml = loanProcessor.getItemXML(itemUUID);
                            Item oleItem = loanProcessor.getItemPojo(itemXml);
                            String dateString = oleItem.getItemStatusEffectiveDate();
                            DateFormat formatter = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE_NOTICE);
                            Date checkInDate = (Date) formatter.parse(dateString);
                            String diff = getMinuteDiff(checkInDate, new Date());
                            Integer diffTime = Integer.parseInt(diff);
                            if (LOG.isDebugEnabled()){
                                LOG.debug("diffTime" + diffTime);
                                LOG.debug("shelvingLagTime.compareTo(diffTime)" + shelvingLagTime.compareTo(diffTime));
                            }
                            if (shelvingLagTime.compareTo(diffTime) <= 0) {
                                loanProcessor.updateItemStatus(oleItem, OLEConstants.NOT_CHECK_OUT_STATUS);
                                businessObjectService.delete(oleRecentlyReturned);
                            }
                        }
                    }
                }

            }
          }catch(Exception e){
                LOG.info("Exception occured while processing the item " + itemUUID);
                LOG.error(e,e);
            }

        }
    }


    private String getMinuteDiff(Date dateOne, Date dateTwo) {
        String diff = "";
        long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
        diff = String.format("%d", TimeUnit.MILLISECONDS.toMinutes(timeDiff),
                -TimeUnit.MINUTES.toSeconds(timeDiff));
        return diff;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}
