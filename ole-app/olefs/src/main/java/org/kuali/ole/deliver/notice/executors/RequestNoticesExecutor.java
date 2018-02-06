package org.kuali.ole.deliver.notice.executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.notice.NoticeSolrInputDocumentGenerator;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;
import org.kuali.ole.deliver.service.NoticesExecutor;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.KRADServiceLocator;

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
            getItemTypeNameAndDesc();
            for(OLEDeliverNotice oleDeliverNotice : deliverNotices){
                if(oleDeliverNotice.getOleDeliverRequestBo()!=null) {
                    setItemInformations(oleDeliverNotice.getOleDeliverRequestBo());
                    if (isValidRequestToSendNotice(oleDeliverNotice.getOleDeliverRequestBo())) {
                        deliverRequestBos.add(oleDeliverNotice.getOleDeliverRequestBo());
                        filteredDeliverNotices.add(oleDeliverNotice);
                    }
                }
            }
        }

    }


    public String generateMailContent() {
        String mailContent = getRequestEmailContentFormatter().generateMailContentForPatron(deliverRequestBos, oleNoticeContentConfigurationBo);
        return mailContent;
    }


    public void sendMail(String mailContent,String mailSubject) {
        if (CollectionUtils.isNotEmpty(deliverRequestBos)) {
            OlePatronDocument olePatron = deliverRequestBos.get(0).getOlePatron();
            try {
                EntityTypeContactInfoBo entityTypeContactInfoBo = olePatron.getEntity()
                        .getEntityTypeContactInfos().get(0);
                String emailAddress = getPatronHomeEmailId(entityTypeContactInfoBo) != null ?
                        getPatronHomeEmailId(entityTypeContactInfoBo) : "";

                if (deliverRequestBos.size() == 1) {
                    sendMailsToPatron(emailAddress, mailContent, deliverRequestBos.get(0).getItemLocation(),mailSubject);
                } else {
                    sendMailsToPatron(emailAddress, mailContent, null,mailSubject);
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

        if (StringUtils.isNotBlank(mailContent)  && !mailContent.contains("FreeMarker template error")) {

            if (noticeContentConfigName!=null &&noticeContentConfigName.equals(OLEConstants.ON_HOLD_EXP_NOTICE)) {
                if (getParameterValue(OLEConstants.HOLD_COUR_NOT_TYP).equals(OLEConstants.EMAIL_NOT_TYP)) {
                    sendMail(mailContent,oleNoticeContentConfigurationBo.getNoticeSubjectLine());
                }
            } else {
                sendMail(mailContent,oleNoticeContentConfigurationBo.getNoticeSubjectLine());
            }



            saveOLEDeliverNoticeHistory(filteredDeliverNotices, mailContent);
            if(CollectionUtils.isNotEmpty(deliverRequestBos) && deliverRequestBos.size()>0) {
                getSolrRequestReponseHandler().updateSolr(org.kuali.common.util.CollectionUtils.singletonList(
                        getNoticeSolrInputDocumentGenerator().getSolrInputDocument(
                                buildMapForIndexToSolr(getType(), mailContent, deliverRequestBos))));
            }
            postProcess();
        }


    }

    public Map buildMapForIndexToSolr(String noticeType, String noticeContent, List<OleDeliverRequestBo> oleDeliverRequestBos) {
        Map parameterMap = new HashMap();
        parameterMap.put("DocType", noticeType);
        parameterMap.put("DocFormat", "Email");
        parameterMap.put("noticeType", noticeType);
        parameterMap.put("noticeContent", noticeContent);
        ItemRecord itemRecord = new CircUtilController().getItemRecordByBarcode(oleDeliverRequestBos.get(0).getItemId());
        String patronId = (itemRecord != null ? itemRecord.getCurrentBorrower() : null  );
        OlePatronDocument olePatron = null;
        if(patronId != null) {
            Map<String, String> parameterMap1 = new HashMap<>();
            parameterMap1.put("olePatronId", patronId);
            olePatron = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, parameterMap1);
        }
        String patronBarcode = "";
        if(olePatron!=null){
            patronBarcode = olePatron.getBarcode();
        }else{
            patronBarcode = oleDeliverRequestBos.get(0).getOlePatron()!=null ? oleDeliverRequestBos.get(0).getOlePatron().getBarcode() : "";
            patronId = oleDeliverRequestBos.get(0).getOlePatron()!=null ? oleDeliverRequestBos.get(0).getOlePatron().getOlePatronId() : null;
        }
        parameterMap.put("patronBarcode", patronBarcode);
        Date dateSent = new Date();
        parameterMap.put("dateSent", dateSent);
        parameterMap.put("uniqueId", patronId!=null ? patronId+ dateSent.getTime() : dateSent.getTime());
        List<String> itemBarcodes = new ArrayList<>();
        for (Iterator<OleDeliverRequestBo> iterator = oleDeliverRequestBos.iterator(); iterator.hasNext(); ) {
            OleDeliverRequestBo oleDeliverRequestBo = iterator.next();
            String itemBarcode = oleDeliverRequestBo.getItemId();
            itemBarcodes.add(itemBarcode);
        }
        parameterMap.put("itemBarcodes",itemBarcodes);
        return parameterMap;
    }

    public String getParameterValue(String key) {
        ParameterKey parameterKey = ParameterKey.create(org.kuali.ole.OLEConstants.APPL_ID, org.kuali.ole.OLEConstants.DLVR_NMSPC,"Deliver" , key);
        //org.kuali.ole.OLEConstants.DELIVER_COMPONENT
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }


}
