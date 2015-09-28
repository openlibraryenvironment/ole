package org.kuali.ole.sip2.sip2Server;

import com.thoughtworks.xstream.XStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.bo.*;
import org.kuali.ole.converter.OLELookupUserConverter;
import org.kuali.ole.request.*;
import org.kuali.ole.response.OLESIP2CheckInResponse;
import org.kuali.ole.response.OLESIP2CheckOutResponse;
import org.kuali.ole.response.OLESIP2PatronInformationResponse;
import org.kuali.ole.response.OLESIP2PatronStatusResponse;
import org.kuali.ole.sip2.response.*;
import org.kuali.ole.sip2.sip2Server.processor.NettyProcessor;
import org.kuali.ole.sip2.sip2Server.processor.PatronInformationNetttyProcessor;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.SocketAddress;
import java.net.URL;
import java.util.*;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOG = Logger.getLogger(NettyServerHandler.class.getName());

    private final String USER_AGENT = "Mozilla/5.0";
    private String serverURL;

    private String clientIP;

    private Map<String, String> lastResponseSendToClient = new HashMap<String, String>();

    private List<NettyProcessor> nettyProcessors;

    protected Properties properties = new Properties();

    String propertiesFileName = "sip2-config.properties";
    InputStream inputStream;
    private String circRestURL = "/rest/circ";

    public NettyServerHandler() {
        // TODO Auto-generated constructor stub
    }

    public NettyServerHandler(String serverURL) {
        this.serverURL = serverURL;
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
        nettyProcessors.add(new PatronInformationNetttyProcessor(properties));
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelRegistered(channelHandlerContext);
        SocketAddress address = channelHandlerContext.channel().remoteAddress();
        clientIP = address.toString();
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object message) throws Exception {


        LOG.info("Entry NettyServerHandler.channelRead(channelHandlerContext, message)");
        ByteBuf byteBuf = (ByteBuf) message;
        String requestMessage = "";
        ChannelFuture channelFuture = null;
        String response = "";

        try {
            LOG.info("NettyServerHandler.channelRead    " + requestMessage);
            requestMessage = byteBuf.toString(CharsetUtil.UTF_8);
            LOG.info("After-CharsetUtil.UTF_8 NettyServerHandler.channelRead    " + requestMessage);

            if (requestMessage != null && !requestMessage.equalsIgnoreCase("")) {

                response = this.processRequest(requestMessage);
                LOG.info("NettyServerHandler.channelRead SIP2 Package :" + response);
            }

            LOG.info("NettyServerHandler.channelRead Client IP Address : " + clientIP);
            LOG.info("NettyServerHandler.channelRead Response Message : " + response);

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

        LOG.info("Exit NettyServerHandler.channelRead(channelHandlerContext, message)");
    }


    public String processRequest(String requestData) {
        String code = requestData.substring(0, 2);
        for (Iterator<NettyProcessor> iterator = nettyProcessors.iterator(); iterator.hasNext(); ) {
            NettyProcessor nettyProcessor = iterator.next();
            if (nettyProcessor.isInterested(code)) {
                return nettyProcessor.process();
            }
        }
        return "";
    }


    public String sendRequestToOle(String requestData, String context) {
        LOG.info("Entry NettyServerHandler.sendRequestToOle(restRequestURL, clientRequest, requestRResponseType)");
        String url = serverURL + circRestURL + context;
        LOG.info("URL  : " + url);
        String response = "";
        try {
            response = this.sendPostRequest(url, requestData);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("NettyServerHandler.sendRequestToOle SIP2 Package :" + response);
        LOG.info("Exit NettyServerHandler.sendRequestToOle(restRequestURL, clientRequest, requestRResponseType)");
        return response;

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
        LOG.info("Entry NettyServerHandler.channelReadComplete(channelHandlerContext)");
        channelHandlerContext.flush();
        LOG.info("Exit NettyServerHandler.channelReadComplete(channelHandlerContext)");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOG.info("Entry NettyServerHandler.exceptionCaught(channelHandlerContext, cause)");
        // TODO Auto-generated method stub
        //super.exceptionCaught(ctx, cause);
        LOG.error("Client (" + clientIP + ") disconnected from server.");
        LOG.info("Exit NettyServerHandler.exceptionCaught(channelHandlerContext, cause)");
    }

    private Object generateCheckoutItemObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("checkOutItem",OLECheckOutItem.class);
        return xStream.fromXML(xml);
    }

    private Object generateCheckInItemObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("checkInItem",OLECheckInItem.class);
        return xStream.fromXML(xml);
    }

    public String sendPostRequest(String url, String requestContent) {
        String responseContent = null;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept", "application/" + "xml");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/" + "xml");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(requestContent);
            wr.flush();
            wr.close();

            InputStream resStream = con.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(resStream));
            String inputLine = null;
            StringBuilder responseContentBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                responseContentBuilder.append(inputLine);
            }
            in.close();
            responseContent = responseContentBuilder.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseContent;
    }

    private String createJSONForCheckoutItemRequest(String patronBarcode, String itemBarcode, String operatorId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("patronBarcode", patronBarcode);
            jsonObject.put("itemBarcode", itemBarcode);
            jsonObject.put("operatorId", operatorId);
            jsonObject.put("requestFormatType", "JSON");
            jsonObject.put("responseFormatType", "XML");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private String createJSONForCheckinItemRequest(String itemBarcode, String operatorId, String deleteIndicator) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("itemBarcode", itemBarcode);
            jsonObject.put("operatorId", operatorId);
            jsonObject.put("deleteIndicator", deleteIndicator);
            jsonObject.put("requestFormatType", "JSON");
            jsonObject.put("responseFormatType", "XML");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private String createJSONForLookupUser(String patronBarcode, String operatorId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("patronBarcode", patronBarcode);
            jsonObject.put("operatorId", operatorId);
            jsonObject.put("requestFormatType", "JSON");
            jsonObject.put("responseFormatType", "XML");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private BigDecimal calculateTotalFineBalance(OLELookupUser oleLookupUser) {
        BigDecimal balanceAmount = new BigDecimal(0);
        if (oleLookupUser.getOleItemFines()!=null){
            List<OLEItemFine> oleItemFineList = oleLookupUser.getOleItemFines().getOleItemFineList();
            if (CollectionUtils.isNotEmpty(oleItemFineList)){
                for (OLEItemFine oleItemFine : oleItemFineList) {
                    balanceAmount = balanceAmount.add(oleItemFine.getBalance());
                }
            }
        }
        return balanceAmount;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
