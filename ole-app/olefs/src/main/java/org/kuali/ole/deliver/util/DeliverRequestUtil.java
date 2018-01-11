package org.kuali.ole.deliver.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleDeliverBatchServiceImpl;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleDeliverRequestHistoryRecord;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.*;

/**
 * Created by pvsubrah on 9/1/15.
 */
public class DeliverRequestUtil extends OLEUtil {
    private static final Logger LOG = Logger.getLogger(DeliverRequestUtil.class);


    public boolean cancelDocument(String itemBarcode, OlePatronDocument patronDocument, OleLoanDocument oleLoanDocument, String operatorId) {
        boolean cancelResult = true;
        OleDeliverRequestBo oleDeliverRequestBo = ItemInfoUtil.getInstance().getRequestByPatronId(patronDocument.getOlePatronId(), itemBarcode);
        if (null != oleDeliverRequestBo) {
            //1. Create Temp history record and delete
            String itemId = oleDeliverRequestBo.getItemId();
            String requestOutCome = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_FULFILLED);
            String loanTransactionNumber = oleDeliverRequestBo.getLoanTransactionRecordNumber();

            OleDeliverRequestHistoryRecord requestHistoryRecord = createRequestHistoryRecord(oleDeliverRequestBo, operatorId, loanTransactionNumber, requestOutCome);

            //2. Delete the existing request
            try {
                getBusinessObjectService().delete(oleDeliverRequestBo);

                //3. Reorder Q
                HashMap<String, Object> map = new HashMap<>();
                map.put("itemId", itemId);
                List<OleDeliverRequestBo> oleDeliverRequestBos =
                        (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, map, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);

                for (Iterator<OleDeliverRequestBo> iterator = oleDeliverRequestBos.iterator(); iterator.hasNext(); ) {
                    OleDeliverRequestBo deliverRequestBo = iterator.next();
                    deliverRequestBo.setBorrowerQueuePosition(deliverRequestBo.getBorrowerQueuePosition() == 1? deliverRequestBo.getBorrowerQueuePosition() : deliverRequestBo.getBorrowerQueuePosition() - 1);
                }

                //Save the re-ordered Q
                if (CollectionUtils.isNotEmpty(oleDeliverRequestBos)) {
                    getBusinessObjectService().save(oleDeliverRequestBos);
                }

                getBusinessObjectService().save(requestHistoryRecord);

                //4: Prepare cancellation notice
          //      List<OleNoticeBo> noticeBos = generateCancelNotices(patronDocument, oleLoanDocument);

                //5: Send cancel notice;
            //    sendCancelNotice(noticeBos);

            } catch (Exception e) {
                e.printStackTrace();
                cancelResult = false;
            }
        }
        return cancelResult;

    }


    private OleDeliverRequestHistoryRecord createRequestHistoryRecord(OleDeliverRequestBo oleDeliverRequestBo, String operatorId, String loanTransactionNumber, String requestOutCome) {
        LOG.debug("Inside createRequestHistoryRecord");

        OleDeliverRequestHistoryRecord oleDeliverRequestHistoryRecord = new OleDeliverRequestHistoryRecord();
        oleDeliverRequestHistoryRecord.setRequestId(oleDeliverRequestBo.getRequestId());
        oleDeliverRequestHistoryRecord.setPatronId(oleDeliverRequestBo.getOlePatron() != null ? oleDeliverRequestBo.getOlePatron().getOlePatronId() : null);
        oleDeliverRequestHistoryRecord.setItemBarcode(oleDeliverRequestBo.getItemId());
        oleDeliverRequestHistoryRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(oleDeliverRequestBo.getItemUuid()));
        oleDeliverRequestHistoryRecord.setArchiveDate(new java.sql.Date(System.currentTimeMillis()));
        oleDeliverRequestHistoryRecord.setPickUpLocationCode(oleDeliverRequestBo.getPickUpLocationCode());
        oleDeliverRequestHistoryRecord.setCreateDate(oleDeliverRequestBo.getCreateDate());
        if (StringUtils.isNotBlank(operatorId)) {
            oleDeliverRequestHistoryRecord.setOperatorId(operatorId);
        } else {
            oleDeliverRequestHistoryRecord.setOperatorId(" ");
        }
        oleDeliverRequestHistoryRecord.setDeliverRequestTypeCode(oleDeliverRequestBo.getRequestTypeCode());
        oleDeliverRequestHistoryRecord.setPoLineItemNumber("");
        oleDeliverRequestHistoryRecord.setLoanTransactionId(loanTransactionNumber);
        oleDeliverRequestHistoryRecord.setRequestOutComeStatus(requestOutCome);
        return oleDeliverRequestHistoryRecord;
    }

    public List<OleNoticeBo> generateCancelNotices(OlePatronDocument patronDocument, OleLoanDocument oleLoanDocument) throws Exception {
        List<OleNoticeBo> oleNoticeBos = new ArrayList<>();

        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        oleNoticeBo.setNoticeName(OLEConstants.CANCELLATION_NOTICE);

        oleNoticeBo.setPatronName(patronDocument.getPatronName());
        oleNoticeBo.setPatronAddress(null != patronDocument.getPreferredAddress() ? patronDocument.getPreferredAddress() : "");
        oleNoticeBo.setPatronEmailAddress(null != patronDocument.getEmail() ? patronDocument.getEmail() : "");
        //TODO: Populate phone number
        oleNoticeBo.setPatronPhoneNumber("");


        oleNoticeBo.setAuthor(oleLoanDocument.getAuthor());
        oleNoticeBo.setItemCallNumber(oleLoanDocument.getItemCallNumber());
        oleNoticeBo.setItemShelvingLocation(oleLoanDocument.getLocation());
        oleNoticeBo.setItemId(oleLoanDocument.getItemId());
        oleNoticeBo.setTitle(oleLoanDocument.getTitle());
        oleNoticeBo.setOleItem(oleLoanDocument.getOleItem());
        if (oleNoticeBo.getPatronEmailAddress() != null && !oleNoticeBo.getPatronEmailAddress().isEmpty()) {
            oleNoticeBos.add(oleNoticeBo);
        }
        return oleNoticeBos;
    }

    public void sendCancelNotice(List<OleNoticeBo> oleNoticeBos) throws Exception {
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        for (OleNoticeBo oleNoticeBo : oleNoticeBos) {
            List list = oleDeliverBatchService.getNoticeForPatron(oleNoticeBos);
            String content = list.toString();
            content = content.replace('[', ' ');
            content = content.replace(']', ' ');
            if (!content.trim().equals("")) {
                OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                if (replyToEmail != null) {
                    oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.CANCELLATION_NOTICE), new EmailBody(content), true);
                } else {
                    String fromAddress = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.NOTICE_FROM_MAIL);
                    if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                        fromAddress = OLEConstants.KUALI_MAIL;
                    }
                    oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.CANCELLATION_NOTICE), new EmailBody(content), true);
                }
            }
        }
    }


}
