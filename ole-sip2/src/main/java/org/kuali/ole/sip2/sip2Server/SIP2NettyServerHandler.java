package org.kuali.ole.sip2.sip2Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.service.NettyHandler;
import org.kuali.ole.sip2.sip2Server.processor.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketAddress;
import java.util.*;



@ChannelHandler.Sharable
public class SIP2NettyServerHandler extends NettyHandler {

    private final static Logger LOG = Logger.getLogger(SIP2NettyServerHandler.class.getName());

    private String clientIP;

    private Map<String, String> lastResponseSendToClient = new HashMap<String, String>();

    private List<NettyProcessor> nettyProcessors;

    protected Properties properties = new Properties();

    String propertiesFileName = "sip2-config.properties";
    InputStream inputStream;

    public SIP2NettyServerHandler() {
        // TODO Auto-generated constructor stub
    }

    public SIP2NettyServerHandler(String serverURL) {
        inputStream = getClass().getClassLoader().getResourceAsStream("sip2-config.properties");

        try {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propertiesFileName + "' not found in the classpath");
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        nettyProcessors = new ArrayList<>();
        nettyProcessors.add(new ScStatusNettyProcessor(properties));
        nettyProcessors.add(new RequestAcsResendNettyProcessor(lastResponseSendToClient, clientIP));
        nettyProcessors.add(new LoginNettyProcessor(properties, serverURL));
        nettyProcessors.add(new CheckInNetttyProcessor(properties, serverURL));
        nettyProcessors.add(new CheckoutNetttyProcessor(properties, serverURL));
        nettyProcessors.add(new ItemInformationNettyProcessor(properties, serverURL));
        nettyProcessors.add(new PatronStatusNettyProcessor(properties, serverURL));
        nettyProcessors.add(new PatronInformationNettyProcessor(properties, serverURL));
        nettyProcessors.add(new PatronBlockNettyProcessor(properties, serverURL));
        nettyProcessors.add(new PatronEnableNettyProcessor(properties, serverURL));
        nettyProcessors.add(new EndPatronSessionNettyProcessor(properties));
        nettyProcessors.add(new RenewNettyProcessor(properties, serverURL));
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelRegistered(channelHandlerContext);
        SocketAddress address = channelHandlerContext.channel().remoteAddress();
        clientIP = address.toString();
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object message) throws Exception {
        LOG.info("Entry SIP2NettyServerHandler.channelRead(channelHandlerContext, message)");
        ByteBuf byteBuf = (ByteBuf) message;
        String requestMessage = "";
        ChannelFuture channelFuture = null;
        String response = "";

        try {
            LOG.info("SIP2NettyServerHandler.channelRead    " + requestMessage);
            requestMessage = byteBuf.toString(CharsetUtil.UTF_8);
            LOG.info("After-CharsetUtil.UTF_8 SIP2NettyServerHandler.channelRead    " + requestMessage);

            if (requestMessage != null && !requestMessage.equalsIgnoreCase("")) {

                response = this.processRequest(requestMessage);
                LOG.info("SIP2NettyServerHandler.channelRead SIP2 Package :" + response);
            }

            LOG.info("SIP2NettyServerHandler.channelRead Client IP Address : " + clientIP);
            LOG.info("SIP2NettyServerHandler.channelRead Response Message : " + response);

            if (response != null) {

                channelFuture = channelHandlerContext.write(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
                channelHandlerContext.flush();
                if (StringUtils.isNotBlank(response.trim())) {
                    lastResponseSendToClient.put(clientIP, response);
                }

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

        LOG.info("Exit SIP2NettyServerHandler.channelRead(channelHandlerContext, message)");
    }


    public String processRequest(String requestData) {
        if (properties != null) {
            String code = requestData.substring(0, 2);
            for (Iterator<NettyProcessor> iterator = nettyProcessors.iterator(); iterator.hasNext(); ) {
                NettyProcessor nettyProcessor = iterator.next();
                if (nettyProcessor.isInterested(code)) {
                    if (nettyProcessor.isServiceTurnedOn()) {
                        return nettyProcessor.process(requestData);
                    } else {
                        return nettyProcessor.getResponseForServiceTurnedOff(requestData);
                    }
                }
            }
            return processResponseForInvalidCode();
        }
        return "";
    }

    private String processResponseForInvalidCode() {
        LOG.info("Request Type :  *****Not a valid SIP2 request");
        StringBuilder builder = new StringBuilder();
        builder.append("96AZ");
        builder.append(MessageUtil.computeChecksum(builder.toString()));
        return builder.toString() + '\r';
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
        LOG.info("Entry SIP2NettyServerHandler.channelReadComplete(channelHandlerContext)");
        channelHandlerContext.flush();
        LOG.info("Exit SIP2NettyServerHandler.channelReadComplete(channelHandlerContext)");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOG.info("Entry SIP2NettyServerHandler.exceptionCaught(channelHandlerContext, cause)");
        // TODO Auto-generated method stub
        //super.exceptionCaught(ctx, cause);
        LOG.error("Client (" + clientIP + ") disconnected from server.");
        LOG.info("Exit SIP2NettyServerHandler.exceptionCaught(channelHandlerContext, cause)");
    }


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
