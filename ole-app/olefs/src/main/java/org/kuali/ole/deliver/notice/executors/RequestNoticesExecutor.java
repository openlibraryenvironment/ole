package org.kuali.ole.deliver.notice.executors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.util.NoticeUtil;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.NoticesExecutor;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.describe.keyvalue.LocationValuesBuilder;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by maheswarang on 6/24/15.
 */
public abstract class RequestNoticesExecutor extends NoticesExecutor {
    private static final Logger LOG = Logger.getLogger(RequestNoticesExecutor.class);
    protected List<OLEDeliverNotice> deliverNotices;
    protected List<OLEDeliverNotice> filteredDeliverNotices = new ArrayList<OLEDeliverNotice>();
    protected RequestEmailContentFormatter requestEmailContentFormatter;
    protected List<OleDeliverRequestBo> deliverRequestBos = new ArrayList<OleDeliverRequestBo>();

    public void setRequestEmailContentFormatter(RequestEmailContentFormatter requestEmailContentFormatter) {
        this.requestEmailContentFormatter = requestEmailContentFormatter;
    }

    public RequestNoticesExecutor(List<OLEDeliverNotice> deliverNotices) {
        this.deliverNotices = deliverNotices;
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


    public abstract String getTitle();

    public abstract String getBody();

    public abstract boolean isValidRequestToSendNotice(OleDeliverRequestBo oleDeliverRequestBo);

    protected abstract void postProcess();



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
        String mailContent = getRequestEmailContentFormatter().generateRequestMailContentForPatron(deliverRequestBos,getTitle(), getBody());
        System.out.println(mailContent);
        return mailContent;
    }


    public void sendMail(String mailContent) {
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




    @Override
    public void run() {

        preProcess();

        String mailContent = generateMailContent();

        System.out.println(mailContent);

        sendMail(mailContent);

        deleteNotices(filteredDeliverNotices);

        saveOLEDeliverNoticeHistory(filteredDeliverNotices,mailContent);

        postProcess();


    }





}
