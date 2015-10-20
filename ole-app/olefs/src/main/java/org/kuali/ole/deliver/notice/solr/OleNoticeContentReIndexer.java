package org.kuali.ole.deliver.notice.solr;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created by sheiksalahudeenm on 10/20/15.
 */
public class OleNoticeContentReIndexer {

    private SolrRequestReponseHandler solrRequestReponseHandler;

    public UpdateResponse reindexNoticeContent(){
        UpdateResponse updateResponse = null;
        List<OLEDeliverNoticeHistory> deliverNoticeHistories = (List<OLEDeliverNoticeHistory>) KRADServiceLocator.getBusinessObjectService().findAll(OLEDeliverNoticeHistory.class);
        if(CollectionUtils.isNotEmpty(deliverNoticeHistories)){
            List<SolrInputDocument> solrInputDocuments = buildSolrInputDocuments(deliverNoticeHistories);
            if(CollectionUtils.isNotEmpty(solrInputDocuments)){
                updateResponse = getSolrRequestReponseHandler().updateSolr(solrInputDocuments);
            }
        }
        return updateResponse;
    }

    private List<SolrInputDocument> buildSolrInputDocuments(List<OLEDeliverNoticeHistory> oleDeliverNoticeHistories) {
        List<SolrInputDocument> solrInputDocuments = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(oleDeliverNoticeHistories)){
            for (Iterator<OLEDeliverNoticeHistory> iterator = oleDeliverNoticeHistories.iterator(); iterator.hasNext(); ) {
                OLEDeliverNoticeHistory oleDeliverNoticeHistory = iterator.next();
                byte[] noticeContent = oleDeliverNoticeHistory.getNoticeContent();
                String patronId = oleDeliverNoticeHistory.getPatronId();
                OlePatronDocument patronDocument = getPatronById(patronId);
                if (null != noticeContent && null != patronDocument) {
                    SolrInputDocument solrInputDocument = new SolrInputDocument();
                    solrInputDocument.addField("DocType", oleDeliverNoticeHistory.getNoticeType());
                    solrInputDocument.addField("DocFormat", "Email");
                    solrInputDocument.addField("noticeType", oleDeliverNoticeHistory.getNoticeType());
                    String content = new String(noticeContent);
                    solrInputDocument.addField("noticeContent", content);
                    solrInputDocument.addField("patronBarcode", patronDocument.getBarcode());
                    Date dateSent = oleDeliverNoticeHistory.getNoticeSentDate();
                    solrInputDocument.addField("dateSent", dateSent);
                    solrInputDocument.addField("deskLocation", "TODO");
                    solrInputDocument.addField("uniqueId", patronId + dateSent.getTime());
                    List<String> itemBarcodes = getItemBarcodes(content);
                    if(CollectionUtils.isNotEmpty(itemBarcodes)){
                        for (Iterator<String> stringIterator = itemBarcodes.iterator(); stringIterator.hasNext(); ) {
                            String itemBarcode = stringIterator.next();
                            solrInputDocument.addField("itemBarcode", itemBarcode);
                        }
                    }
                    solrInputDocuments.add(solrInputDocument);
                }
            }
        }
        return solrInputDocuments;
    }

    public OlePatronDocument getPatronById(String patronId) {
        Map patronMap = new HashMap();
        patronMap.put("olePatronId", patronId);
        return KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, patronMap);
    }

    private SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if (null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    public List<String> getItemBarcodes(String noticeContent){
        Document html = Jsoup.parse(noticeContent);
        Element body = html.body();
        Elements tables = body.select("table");

        List<String> itemBarcodes = new ArrayList<>();

        for (Iterator<Element> iterator = tables.iterator(); iterator.hasNext(); ) {
            Element table = iterator.next();
            Elements tr = table.select("tr");
            for (Iterator<Element> elementIterator = tr.iterator(); elementIterator.hasNext(); ) {
                Element trElement = elementIterator.next();
                Elements tdElements = trElement.select("td");
                for (Iterator<Element> iterator1 = tdElements.iterator(); iterator1.hasNext(); ) {
                    Element tdElement = iterator1.next();
                    if(tdElement.text().contains("Item Barcode")){
                        String itemBarcode = tdElement.parent().child(1).text();
                        itemBarcodes.add(itemBarcode);
                    }
                }

            }
        }

        return itemBarcodes;

    }
}
