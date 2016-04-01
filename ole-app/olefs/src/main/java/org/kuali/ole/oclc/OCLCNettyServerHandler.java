package org.kuali.ole.oclc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.service.NettyHandler;
import org.kuali.ole.utility.OleHttpRestClient;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.List;
import java.util.Map;

/**
 * Created by angelind on 2/23/16.
 */
@ChannelHandler.Sharable
public class OCLCNettyServerHandler extends NettyHandler {

    private final static Logger LOG = Logger.getLogger(OCLCNettyServerHandler.class.getName());
    private String serverUrl;
    private BusinessObjectService businessObjectService;

    public OCLCNettyServerHandler(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object message) throws Exception {
        LOG.info("Entry OCLCNettyServerHandler.channelRead(channelHandlerContext, message)");
        ByteBuf byteBuf = (ByteBuf) message;
        String requestMessage = "";
        ChannelFuture channelFuture = null;
        String response = "";

        try {
            LOG.info("OCLCNettyServerHandler.channelRead    " + requestMessage);
            requestMessage = byteBuf.toString(CharsetUtil.UTF_8);
            LOG.info("After-CharsetUtil.UTF_8 OCLCNettyServerHandler.channelRead    " + requestMessage);

            if (requestMessage != null && !requestMessage.equalsIgnoreCase("")) {

                String profileId = getBatchProfileId("OCLC", "Bib Import");
                JSONObject requestObject = new JSONObject();
                requestObject.put("marcContent",requestMessage);
                requestObject.put("profileId",profileId);
                requestObject.put("batchType","Bib Import");

                response = new OleHttpRestClient().sendPostRequest(this.serverUrl + "/rest/batch/submit/api", requestObject.toString(),"json");

                response = response + "\r";

                LOG.info("OCLCNettyServerHandler.channelRead Response Message : " + response);
            }


            if (response != null) {
                channelFuture = channelHandlerContext.write(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
                channelHandlerContext.flush();
            }

            if (channelFuture == null || !channelFuture.isSuccess()) {
                LOG.info("Send Failed: " + channelFuture.cause());
            } else {
                LOG.info("Send Success ");
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            byteBuf.release();
        }
        LOG.info("Exit from OCLCNettyServer");
    }

    private String getBatchProfileId(String profileName, String profileType) {
        Map parameterMap = new HashedMap();
        parameterMap.put(OleNGConstants.BATCH_PROCESS_PROFILE_NAME, profileName);
        parameterMap.put(OleNGConstants.BATCH_PROCESS_PROFILE_TYPE, profileType);
        List<BatchProcessProfile> matching = (List<BatchProcessProfile>) getBusinessObjectService().findMatching(BatchProcessProfile.class, parameterMap);
        if (CollectionUtils.isNotEmpty(matching)) {
            BatchProcessProfile batchProcessProfile = matching.get(0);
            return String.valueOf(batchProcessProfile.getBatchProcessProfileId());
        }
        return null;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.flush();
    }


    public BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}
