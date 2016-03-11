package org.kuali.ole.oleng.resolvers;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.handler.CreateReqAndPOServiceHandler;
import org.kuali.ole.oleng.service.OleNGRequisitionService;
import org.kuali.ole.oleng.util.OleNGPOHelperUtil;
import org.marc4j.marc.Record;

import java.util.*;

/**
 * Created by SheikS on 1/13/2016.
 */
public abstract class OrderProcessHandler {
    private  OleNGRequisitionService oleNGRequisitionService;

    private static final Logger LOG = Logger.getLogger(OrderProcessHandler.class);

    public abstract boolean isInterested(String type);

    public abstract CreateReqAndPOBaseServiceHandler getCreateReqOrPOServiceHandler();

    public List<Integer> processOrder(Map<String, Record> recordMap, BatchProcessProfile batchProcessProfile, CreateReqAndPOServiceHandler orderRequestHandler) throws Exception {
        List<Integer> poIds = new ArrayList<>();
        OleNGPOHelperUtil oleNGPOHelperUtil = OleNGPOHelperUtil.getInstance();

        String requisitionForTitlesOption = batchProcessProfile.getRequisitionForTitlesOption();

        boolean multiTitle = false;
        if(StringUtils.isNotBlank(requisitionForTitlesOption) &&
                requisitionForTitlesOption.equalsIgnoreCase("One Requisition With All Titles")) {
            multiTitle = true;
        }
        if(multiTitle) {
            Set<String> bibIds = recordMap.keySet();
            Integer purapIdentifier = (Integer) oleNGPOHelperUtil.processReqAndPo(bibIds, batchProcessProfile, getCreateReqOrPOServiceHandler());
            if (null != purapIdentifier) {
                poIds.add(purapIdentifier);
            }
        } else {
            Set<String> bibIds = recordMap.keySet();
            for (Iterator<String> iterator = bibIds.iterator(); iterator.hasNext(); ) {
                String bibId = iterator.next();
                Integer purapIdentifier = (Integer) oleNGPOHelperUtil.processReqAndPo(Collections.singleton(bibId), batchProcessProfile, getCreateReqOrPOServiceHandler());
                if (null != purapIdentifier) {
                    poIds.add(purapIdentifier);
                }
            }
        }
        return poIds;
    }

    public OleNGRequisitionService getOleNGRequisitionService() {
        return oleNGRequisitionService;
    }

    public void setOleNGRequisitionService(OleNGRequisitionService oleNGRequisitionService) {
        this.oleNGRequisitionService = oleNGRequisitionService;
    }
}
