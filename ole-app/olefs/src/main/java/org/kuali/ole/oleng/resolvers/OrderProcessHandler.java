package org.kuali.ole.oleng.resolvers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.Exchange;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.service.OleNGMemorizeService;
import org.kuali.ole.oleng.service.OleNGRequisitionService;
import org.kuali.ole.oleng.util.OleNGPOHelperUtil;

import java.util.*;

/**
 * Created by SheikS on 1/13/2016.
 */
public abstract class OrderProcessHandler {
    private  OleNGRequisitionService oleNGRequisitionService;
    private OleNGPOHelperUtil oleNGPOHelperUtil;

    private static final Logger LOG = Logger.getLogger(OrderProcessHandler.class);

    public abstract boolean isInterested(String type);

    public abstract CreateReqAndPOBaseServiceHandler getCreateReqOrPOServiceHandler();

    public Map<Integer, Set<Integer>> processOrder(List<RecordDetails> recordDetailsList, BatchProcessProfile batchProcessProfile,
                                                   OleNGMemorizeService oleNGMemorizeService, Exchange exchange) throws Exception {
        Map<Integer, Set<Integer>> poIdsMap = new HashMap<>();

        String requisitionForTitlesOption = batchProcessProfile.getRequisitionForTitlesOption();

        boolean multiTitle = false;
        if(StringUtils.isNotBlank(requisitionForTitlesOption) &&
                requisitionForTitlesOption.equalsIgnoreCase("One Requisition With All Titles")) {
            multiTitle = true;
        }
        CreateReqAndPOBaseServiceHandler createReqOrPOServiceHandler = getCreateReqOrPOServiceHandler();
        createReqOrPOServiceHandler.setOleNGMemorizeService(oleNGMemorizeService);
        if(multiTitle) {
            Map<Integer, Set<Integer>> poIdMap = getOleNGPOHelperUtil().processReqAndPo(recordDetailsList, batchProcessProfile,
                    createReqOrPOServiceHandler, exchange);
            poIdsMap.putAll(poIdMap);
        } else {
            for (Iterator<RecordDetails> iterator = recordDetailsList.iterator(); iterator.hasNext(); ) {
                RecordDetails recordDetails = iterator.next();
                Thread.sleep(getSleepTimeForOrderProcess());
                Map<Integer, Set<Integer>> poIdMap = getOleNGPOHelperUtil().processReqAndPo(Collections.singletonList(recordDetails),
                        batchProcessProfile, createReqOrPOServiceHandler, exchange);
                poIdsMap.putAll(poIdMap);
            }
        }
        return poIdsMap;
    }

    public OleNGRequisitionService getOleNGRequisitionService() {
        return oleNGRequisitionService;
    }

    public void setOleNGRequisitionService(OleNGRequisitionService oleNGRequisitionService) {
        this.oleNGRequisitionService = oleNGRequisitionService;
    }

    public int getSleepTimeForOrderProcess() {
        String parameterValue = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants.DESCRIBE_COMPONENT, OleNGConstants.SLEEP_TIME_FOR_ORDER_PROCESS);
        if(NumberUtils.isDigits(parameterValue)) {
            return Integer.valueOf(parameterValue);
        }
        return 1000;
    }

    public OleNGPOHelperUtil getOleNGPOHelperUtil() {
        if(null == oleNGPOHelperUtil) {
            oleNGPOHelperUtil = new OleNGPOHelperUtil();
        }
        return oleNGPOHelperUtil;
    }

    public void setOleNGPOHelperUtil(OleNGPOHelperUtil oleNGPOHelperUtil) {
        this.oleNGPOHelperUtil = oleNGPOHelperUtil;
    }
}
