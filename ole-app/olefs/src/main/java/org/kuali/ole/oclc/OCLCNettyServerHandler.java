package org.kuali.ole.oclc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.kuali.ole.service.NettyHandler;

/**
 * Created by angelind on 2/23/16.
 */
@ChannelHandler.Sharable
public class OCLCNettyServerHandler extends NettyHandler {

    private final static Logger LOG = Logger.getLogger(OCLCNettyServerHandler.class.getName());
    private String serverUrl;

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
                //TODO : Need to do process
                response = "This is the response from OCLC Server\r";
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

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.flush();
    }
}
