package org.kuali.ole.deliver.notice.executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.notice.NoticeSolrInputDocumentGenerator;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;
import org.kuali.ole.deliver.service.NoticesExecutor;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.util.*;

/**
 * Created by maheswarang on 6/24/15.
 */
public abstract class RequestNoticesExecutor extends NoticesExecutor {
    private static final Logger LOG = Logger.getLogger(RequestNoticesExecutor.class);
    protected List<OLEDeliverNotice> deliverNotices;
    protected String noticeContentConfigName;
    protected String operatorId;
    protected List<OLEDeliverNotice> filteredDeliverNotices = new ArrayList<OLEDeliverNotice>();
    protected RequestEmailContentFormatter requestEmailContentFormatter;
    protected List<OleDeliverRequestBo> deliverRequestBos = new ArrayList<OleDeliverRequestBo>();


    protected OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo;
    private NoticeSolrInputDocumentGenerator noticeSolrInputDocumentGenerator;
    private SolrRequestReponseHandler solrRequestReponseHandler;

    public void setRequestEmailContentFormatter(RequestEmailContentFormatter requestEmailContentFormatter) {
        this.requestEmailContentFormatter = requestEmailContentFormatter;
    }

    public RequestNoticesExecutor(Map requestMap) {
        this.deliverNotices = (List<OLEDeliverNotice>) requestMap.get(OLEConstants.DELIVER_NOTICES);
        this.noticeContentConfigName = (String) requestMap.get(OLEConstants.NOTICE_CONTENT_CONFIG_NAME);
        this.operatorId = (String) requestMap.get(OLEConstants.OPTR_ID);
    }

    public abstract RequestEmailContentFormatter getRequestEmailContentFormatter();


    public List<OLEDeliverNotice> getDeliverNotices() {
        return deliverNotices;
    }

    public void setDeliverNotices(List<OLEDeliverNotice> deliverNotices) {
        this.deliverNotices = deliverNotices;
    }

    public List<OleDeliverRequestBo> getDeliverRequestBos() {
        return deliverRequestBos;
    }

    public void setDeliverRequestBos(List<OleDeliverRequestBo> deliverRequestBos) {
        this.deliverRequestBos = deliverRequestBos;
    }

    public List<OLEDeliverNotice> getFilteredDeliverNotices() {
        return filteredDeliverNotices;
    }

    public void setFilteredDeliverNotices(List<OLEDeliverNotice> filteredDeliverNotices) {
        this.filteredDeliverNotices = filteredDeliverNotices;
    }


    public abstract String getType();

    public abstract String getTitle();

    public abstract String getBody();

    public abstract boolean isValidRequestToSendNotice(OleDeliverRequestBo oleDeliverRequestBo);

    protected abstract void postProcess();

    public abstract void setOleNoticeContentConfigurationBo();


    private void preProcess() {
        if(deliverNotices !=null && deliverNotices.size()>0){
            for(OLEDeliverNotice oleDeliverNotice : deliverNotices){
                setItemInformations(oleDeliverNotice.getOleDeliverRequestBo());
                if(isValidRequestToSendNotice(oleDeliverNotice.getOleDeliverRequestBo())){
                deliverRequestBos.add(oleDeliverNotice.getOleDeliverRequestBo());
                filteredDeliverNotices.add(oleDeliverNotice);
                }
            }
        }

    }


    public String generateMailContent() {
        String mailContent = getRequestEmailContentFormatter().generateMailContentForPatron(deliverRequestBos, oleNoticeContentConfigurationBo);
        System.out.println(mailContent);
        return mailContent;
    }


    public void sendMail(String mailContent) {
        if (CollectionUtils.isNotEmpty(deliverRequestBos)) {
            OlePatronDocument olePatron = deliverRequestBos.get(0).getOlePatron();
            try {
                EntityTypeContactInfoBo entityTypeContactInfoBo = olePatron.getEntity()
                        .getEntityTypeContactInfos().get(0);
                String emailAddress = getPatronHomeEmailId(entityTypeContactInfoBo) != null ?
                        getPatronHomeEmailId(entityTypeContactInfoBo) : "";

                if (deliverRequestBos.size() == 1) {
                    sendMailsToPatron(emailAddress, mailContent, deliverRequestBos.get(0).getItemLocation());
                } else {
                    sendMailsToPatron(emailAddress, mailContent, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public NoticeSolrInputDocumentGenerator getNoticeSolrInputDocumentGenerator() {
        if (null == noticeSolrInputDocumentGenerator) {
            noticeSolrInputDocumentGenerator = new NoticeSolrInputDocumentGenerator();
        }
        return noticeSolrInputDocumentGenerator;
    }

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if (null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    @Override
    public void run() {

        preProcess();

        setOleNoticeContentConfigurationBo();

        String mailContent = generateMailContent();

        if (StringUtils.isNotBlank(mailContent)) {
            System.out.println(mailContent);

            sendMail(mailContent);

            saveOLEDeliverNoticeHistory(filteredDeliverNotices, mailContent);

            getSolrRequestReponseHandler().updateSolr(org.kuali.common.util.CollectionUtils.singletonList(
                    getNoticeSolrInputDocumentGenerator().getSolrInputDocument(
                            buildMapForIndexToSolr(getType(),mailContent, deliverRequestBos))));

            postProcess();
        }


    }

    public Map buildMapForIndexToSolr(String noticeType, String noticeContent, List<OleDeliverRequestBo> oleDeliverRequestBos) {
        Map parameterMap = new HashMap();
        parameterMap.put("DocType", noticeType);
        parameterMap.put("DocFormat", "Email");
        parameterMap.put("noticeType", noticeType);
        parameterMap.put("noticeContent", noticeContent);
        String patronBarcode = oleDeliverRequestBos.get(0).getOlePatron().getBarcode();
        String patronId = oleDeliverRequestBos.get(0).getOlePatron().getOlePatronId();
        parameterMap.put("patronBarcode", patronBarcode);
        Date dateSent = new Date();
        parameterMap.put("dateSent", dateSent);
        parameterMap.put("uniqueId", patronId+ dateSent.getTime());
        List<String> itemBarcodes = new ArrayList<>();
        for (Iterator<OleDeliverRequestBo> iterator = oleDeliverRequestBos.iterator(); iterator.hasNext(); ) {
            OleDeliverRequestBo oleDeliverRequestBo = iterator.next();
            String itemBarcode = oleDeliverRequestBo.getItemId();
            itemBarcodes.add(itemBarcode);
        }
        parameterMap.put("itemBarcodes",itemBarcodes);
        return parameterMap;
    }


}
