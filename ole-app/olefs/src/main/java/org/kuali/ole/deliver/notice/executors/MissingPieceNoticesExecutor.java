package org.kuali.ole.deliver.notice.executors;

import org.apache.log4j.Logger;
import org.kuali.common.util.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.service.MissingPieceEmailContentFormatter;
import org.kuali.ole.deliver.service.NoticeMailContentFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Palanivelrajanb on 11/12/2015.
 */
public class MissingPieceNoticesExecutor extends LoanNoticesExecutor {
    private static final Logger LOG = Logger.getLogger(MissingPieceNoticesExecutor.class);
    private NoticeMailContentFormatter noticeMailContentFormatter;
    private NoticeMailContentFormatter getNoticeMailContentFormatter() {
        if (null == noticeMailContentFormatter) {
            noticeMailContentFormatter = new MissingPieceEmailContentFormatter();
        }
        return noticeMailContentFormatter;
    }

    public void setNoticeMailContentFormatter(NoticeMailContentFormatter noticeMailContentFormatter) {
        this.noticeMailContentFormatter = noticeMailContentFormatter;
    }
    public MissingPieceNoticesExecutor(Map missingNoticeMap) {
        super(missingNoticeMap);
    }

    @Override
    protected String getNoticeType() {
        return OLEConstants.MISSING_PIECE_NOTICE;
    }

    @Override
    protected void postProcess(List<OleLoanDocument> loanDocuments) {

    }

    @Override
    protected void preProcess(List<OleLoanDocument> loanDocuments) {

    }

    @Override
    public void saveLoanDocument() {

    }

    @Override
    public List<OLEDeliverNotice> buildNoticesForDeletion() {
        return null;
    }

    @Override
    public String generateMailContent(List<OleLoanDocument> oleLoanDocuments) {
        String mailContent = getNoticeMailContentFormatter().generateMailContentForPatron(oleLoanDocuments,oleNoticeContentConfigurationBo);
        return mailContent;
    }

    @Override
    public void deleteNotices(List<OLEDeliverNotice> oleDeliverNotices) {

    }

    @Override
    public List<OLEDeliverNoticeHistory> saveOLEDeliverNoticeHistory(List<OLEDeliverNotice> oleDeliverNotices, String mailContent) {
        return null;
    }

    @Override
    public void setOleNoticeContentConfigurationBo() {
        List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBoList = null;
        Map<String,String> noticeConfigurationMap = new HashMap<String,String>();
        noticeConfigurationMap.put("noticeType",OLEConstants.MISSING_PIECE_NOTICE);
        //noticeConfigurationMap.put("noticeName", noticeContentConfigName);
        oleNoticeContentConfigurationBoList= (List<OleNoticeContentConfigurationBo>)getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class,noticeConfigurationMap);
        if(oleNoticeContentConfigurationBoList!=null && oleNoticeContentConfigurationBoList.size()>0){
            oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBoList.get(0);
        }else{
            oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
            oleNoticeContentConfigurationBo.setNoticeTitle(getTitle());
            oleNoticeContentConfigurationBo.setNoticeBody(getBody());
            oleNoticeContentConfigurationBo.setNoticeFooterBody("");
            oleNoticeContentConfigurationBo.setNoticeSubjectLine(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE);
        }
    }

    @Override
    public void setOleNoticeContentConfigurationBo(OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        this.oleNoticeContentConfigurationBo=oleNoticeContentConfigurationBo;
    }

    public String getTitle() {
        String title = OLEConstants.MISSING_PIECE_NOTICE_TITLE;
        return title;
    }


    public String getBody() {
        String body = OLEConstants.MISSING_PIECE_NOTICE_BODY;
        return body;
    }


}
