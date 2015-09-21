package org.kuali.ole.sip2.sip2Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.sip2.response.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OLENettyServerHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOG = Logger.getLogger(OLENettyServerHandler.class.getName());
    private static String INSTITUTION = "OLE";
    private String serverURL;
    private String circulationService;

    /*private String clientName;
    private String clientLocation;*/
    private String clientIP;
    private String loginUserId;

    private Map<String, String> lastResponseSendToClient = new HashMap<String, String>();

    protected Properties properties = new Properties();

    String propertiesFileName = "sip2-config.properties";
    InputStream inputStream;

    public OLENettyServerHandler() {
        // TODO Auto-generated constructor stub
    }

    public OLENettyServerHandler(String serverURL, String circulationService) {
        this.serverURL = serverURL;
        this.circulationService = circulationService;
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
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelRegistered(channelHandlerContext);
        SocketAddress address = channelHandlerContext.channel().remoteAddress();
        clientIP = address.toString();
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object message) throws Exception {


        LOG.info("Entry OLENettyServerHandler.channelRead(channelHandlerContext, message)");
        ByteBuf byteBuf = (ByteBuf) message;
        String requestMessage = "";
        ChannelFuture channelFuture = null;
        String response = "";

        try {/*
            SocketAddress address = channelHandlerContext.channel().remoteAddress();
            clientIP = address.toString();*/
            LOG.info("OLENettyServerHandler.channelRead    "+requestMessage);
            requestMessage = byteBuf.toString(CharsetUtil.UTF_8);
            LOG.info("After-CharsetUtil.UTF_8 OLENettyServerHandler.channelRead    "+requestMessage);

            if (requestMessage != null && !requestMessage.equalsIgnoreCase("") ) {

                /*if(properties.getProperty("sip2.checkSum.turnedOn").equalsIgnoreCase("true")){
                    if(requestMessage.startsWith("99") && !requestMessage.contains("AY")){
                        StringBuilder builder = new StringBuilder();
                        builder.append("96AZ");
                        builder.append(MessageUtil.computeChecksum(builder.toString()));
                        response= builder.toString() + '\r';
                    }else if(!requestMessage.startsWith("99") && !requestMessage.contains("|AY")){
                        StringBuilder builder = new StringBuilder();
                        builder.append("96AZ");
                        builder.append(MessageUtil.computeChecksum(builder.toString()));
                        response= builder.toString() + '\r';
                    }else{
                        response = this.processRequest(requestMessage);
                        LOG.info("OLENettyServerHandler.channelRead SIP2 Package :"+response);
                    }
                }else{*/
                    response = this.processRequest(requestMessage);
                    LOG.info("OLENettyServerHandler.channelRead SIP2 Package :"+response);
               // }

            }

            LOG.info("OLENettyServerHandler.channelRead Client IP Address : " + clientIP);
            LOG.info("OLENettyServerHandler.channelRead Response Message : " + response);

            if (response != null) {
               // String[] responseArray = response.split("\\$|$");

                //for (String responseMessage : responseArray) {
                    channelFuture = channelHandlerContext.write(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
                    channelHandlerContext.flush();
                    if (StringUtils.isNotBlank(response.trim())) {
                        lastResponseSendToClient.put(clientIP, response);
                    }
                //}
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

        LOG.info("Exit OLENettyServerHandler.channelRead(channelHandlerContext, message)");

    }


    public String processRequest(String requestData) {
        String response = "";
        //SC Status
        LOG.info("OLENettyServerHandler.processRequest Client IP Address : " + clientIP);
        LOG.info("Request Message : " + requestData);


        if (properties != null) {
            String code = requestData.substring(0, 2);

            switch (code) {
                case "99":
                    LOG.info("Request Type :  SC Status Request");
                    response = this.sendRequestToOle(requestData, loginUserId);
                    break;
                case "97":


                    LOG.info("Request Type :  Request ACS Resend");

                    if (lastResponseSendToClient.containsKey(clientIP)) {

                        response = this.removeSeqNumFromResponse(lastResponseSendToClient.get(clientIP))/* + "$|$"*/;
                       /* if (requestData.length() > 2) {
                            response = response + requestData.replace("97", "96");
                        } else {
                            response = response + "96";
                        }*/
                    }else{
                        StringBuilder builder = new StringBuilder();
                        builder.append("96AZ");
                        builder.append(MessageUtil.computeChecksum(builder.toString()));
                        response= builder.toString() + '\r';
                    }
                    break;
                case "93":
                    LOG.info("Request Type :  Login Request");
                    if (properties.getProperty("sip2.service.login").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                        if (response.charAt(2) == '1') {
                            this.loginUserId = getLoginUserId(requestData);
                        }
                    } else {
                        OLESIP2LoginTurnedOffResponse olesip2LoginTurnedOffResponse = new OLESIP2LoginTurnedOffResponse();
                        response = olesip2LoginTurnedOffResponse.getOLESIP2LoginTurnedOffResponse(requestData);
                    }
                    break;
                case "09":
                    LOG.info("Request Type :  Checkin Request");
                    if (properties.getProperty("sip2.service.checkIn").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2CheckInTurnedOffResponse olesip2CheckInTurnedOffResponse = new OLESIP2CheckInTurnedOffResponse();
                        response = olesip2CheckInTurnedOffResponse.getOLESIP2CheckInTurnedOffResponse(requestData);
                    }
                    break;
                case "11":

                    LOG.info("Request Type :  Checkout Request");
                    if (properties.getProperty("sip2.service.checkOut").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2CheckOutTurnedOffResponse olesip2CheckOutTurnedOffResponse = new OLESIP2CheckOutTurnedOffResponse();
                        response = olesip2CheckOutTurnedOffResponse.getOLESIP2CheckOutTurnedOffResponse(requestData);
                    }

                    break;
                case "17":
                    LOG.info("Request Type :  Item Information");
                    if (properties.getProperty("sip2.service.itemInformation").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2ItemInfoTurnedOffResponse olesip2ItemInfoTurnedOffResponse = new OLESIP2ItemInfoTurnedOffResponse();
                        response = olesip2ItemInfoTurnedOffResponse.getOLESIP2ItemInfoTurnedOffResponse(requestData);
                    }

                    break;
                case "23":
                    LOG.info("Request Type :  Patron Status Request");
                    if (properties.getProperty("sip2.service.patronStatus").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2PatronStatusTurnedOffResponse patronStatusTurnedOffResponse = new OLESIP2PatronStatusTurnedOffResponse();
                        response = patronStatusTurnedOffResponse.getOLESIP2PatronStatusTurnedOffResponse(requestData, "Patron Status Request");
                    }

                    break;
                case "63":
                    LOG.info("Request Type :  Patron Information");
                    if (properties.getProperty("sip2.service.patronInformation").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2PatronInformationTurnedOffResponse patronInformationTurnedOffResponse = new OLESIP2PatronInformationTurnedOffResponse();
                        response = patronInformationTurnedOffResponse.getOLESIP2PatronInformationTurnedOffResponse(requestData);
                    }


                    break;
                case "01":
                    LOG.info("Request Type :  Patron Block");
                    if (properties.getProperty("sip2.service.blockPatron").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2PatronStatusTurnedOffResponse patronStatusTurnedOffResponse = new OLESIP2PatronStatusTurnedOffResponse();
                        response = patronStatusTurnedOffResponse.getOLESIP2PatronStatusTurnedOffResponse(requestData, "Patron Block Request");
                    }

                    break;

                case "25":

                    LOG.info("Request Type :  Patron Enable");
                    if (properties.getProperty("sip2.service.patronEnable").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2PatronEnableTurnedOffResponse patronEnableTurnedOffResponse = new OLESIP2PatronEnableTurnedOffResponse();
                        response = patronEnableTurnedOffResponse.getOLESIP2PatronEnableTurnedOffResponse(requestData);
                    }
                    break;
                case "35":
                    LOG.info("Request Type :  End Patron Session");
                    //response =this.endPatronSessionResponse();
                    if (properties.getProperty("sip2.service.endPatronSession").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2EndPatronSessionTurnedOffResponse endPatronSessionTurnedOffResponse = new OLESIP2EndPatronSessionTurnedOffResponse();
                        response = endPatronSessionTurnedOffResponse.getOLESIP2EndPatronSessionTurnedOffResponse(requestData);
                    }
                    break;
                case "29":

                    LOG.info("Request Type :  Renew");
                    if (properties.getProperty("sip2.service.renew").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2RenewTurnedOffResponse renewTurnedOffResponse = new OLESIP2RenewTurnedOffResponse();
                        response = renewTurnedOffResponse.getOLESIP2RenewTurnedOffResponse(requestData);
                    }
                    break;
                case "15":
                    LOG.info("Request Type :  Hold");
                    if (properties.getProperty("sip2.service.hold").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2HoldTurnedOffResponse holdTurnedOffResponse = new OLESIP2HoldTurnedOffResponse();
                        response = holdTurnedOffResponse.getOLESIP2HoldTurnedOffResponse(requestData);
                    }

                    break;
                case "37":
                    LOG.info("Request Type :  Fee Paid Message");
                    if (properties.getProperty("sip2.service.feePaid").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2FeePaidTurnedOffResponse olesip2FeePaidTurnedOffResponse = new OLESIP2FeePaidTurnedOffResponse();
                        response = olesip2FeePaidTurnedOffResponse.getOLESIP2FeePaidTurnedOffResponse(requestData);
                    }

                    break;
                case "65":

                    LOG.info("Request Type :  Renew All");
                    if (properties.getProperty("sip2.service.renewAll").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);

                    } else {
                        OLESIP2RenewAllTurnedOffResponse renewAllTurnedOffResponse = new OLESIP2RenewAllTurnedOffResponse();
                        response = renewAllTurnedOffResponse.getOLESIP2RenewAllTurnedOffResponse(requestData);
                    }

                    break;
                case "19":

                    LOG.info("Request Type :  Item Status Update");
                    if (properties.getProperty("sip2.service.itemStatusUpdate").equalsIgnoreCase("yes")) {
                        response = this.sendRequestToOle(requestData, loginUserId);
                    } else {
                        OLESIP2ItemStatusUpdateTurnedOffResponse itemStatusUpdateTurnedOffResponse = new OLESIP2ItemStatusUpdateTurnedOffResponse();
                        response = itemStatusUpdateTurnedOffResponse.getOLESIP2ItemStatusUpdateTurnedOffResponse(requestData);
                    }

                    break;


                default:
                    LOG.info("Request Type :  *****Not a valid SIP2 request");
                    StringBuilder builder = new StringBuilder();


                    builder.append("96AZ");
                    builder.append(MessageUtil.computeChecksum(builder.toString()));


                    response= builder.toString() + '\r';

                    break;
            }


        }
        LOG.info("OLENettyServerHandler.processRequest SIP2 Package :"+response);

        LOG.info("Exit OLENettyServerHandler.analysisRequestType(String requestData)");
        return response;
    }


    public String sendRequestToOle(String requestData, String loginUserId) {
        LOG.info("Entry OLENettyServerHandler.sendRequestToOle(restRequestURL, clientRequest, requestRResponseType)");
        String url = "";
        try {
            if (loginUserId != null && !loginUserId.equalsIgnoreCase("")) {
                url = serverURL + circulationService + "sipService&requestData=" + URLEncoder.encode(requestData, "UTF-8") + "&loginUser=" + loginUserId;
            } else {
                url = serverURL + circulationService + "sipService&requestData=" + URLEncoder.encode(requestData, "UTF-8");
            }
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ;
        LOG.info("URL  : " + url);
        String response = "";
        try {
            response = this.httpGet(url, requestData);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("OLENettyServerHandler.sendRequestToOle SIP2 Package :"+response);
        LOG.info("Exit OLENettyServerHandler.sendRequestToOle(restRequestURL, clientRequest, requestRResponseType)");
        return response;

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
        LOG.info("Entry OLENettyServerHandler.channelReadComplete(channelHandlerContext)");
        channelHandlerContext.flush();
        LOG.info("Exit OLENettyServerHandler.channelReadComplete(channelHandlerContext)");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOG.info("Entry OLENettyServerHandler.exceptionCaught(channelHandlerContext, cause)");
        // TODO Auto-generated method stub
        //super.exceptionCaught(ctx, cause);
        LOG.error("Client (" + clientIP + ") disconnected from server.");
        LOG.info("Exit OLENettyServerHandler.exceptionCaught(channelHandlerContext, cause)");
    }


    public String httpGet(String url, String requestData) {
        LOG.info("Entry OLENettyServerHandler.httpGet(url, requestRResponseType)");


        String response = null;
        CloseableHttpClient httpclient = null;
        HttpGet httpget = null;
        ResponseHandler<String> responseHandler = null;
        try {

            httpclient = HttpClients.createDefault();

            httpget = new HttpGet(url);
            LOG.info("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException {
                    int status = response.getStatusLine().getStatusCode();
                    //if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    try {
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                    /*} else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }*/
                }
            };
            response = httpclient.execute(httpget, responseHandler);
        } catch (Exception ex) {

            if (requestData.startsWith("99")) {

                response = this.scStatusNegativeResponse(requestData);
            } else {
                ex.printStackTrace();
            }

        } finally {
            try {
                httpclient.close();
                if (requestData.startsWith("99") && !response.startsWith("98")) {

                    response = this.scStatusNegativeResponse(requestData);


                } else {
                    return response;
                }

            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
        LOG.info("OLENettyServerHandler.httpGet SIP2 Package :  "+response);
        LOG.info("Exit OLENettyServerHandler.httpGet(url, requestRResponseType)");
        return response;
    }

    private String scStatusNegativeResponse(String requestData) {

        LOG.info("Entry OLENettyServerHandler.scStatusNegativeResponse()");
        requestData = requestData.trim();

        StringBuilder builder = new StringBuilder();
        builder.append(98);
        builder.append("N");
        builder.append("N");
        builder.append("N");
        builder.append("N");
        builder.append("N");
        builder.append("N");
        builder.append("010");
        builder.append("003");
        builder.append(MessageUtil.getSipDateTime());
        builder.append("2.00");
        builder.append("AO");
        builder.append("InId");
        builder.append("|AM");
        builder.append(INSTITUTION);

        if (requestData.length() == 19) {
            if (requestData.substring(10, 12).equalsIgnoreCase("AY")) {
                builder.append("|AY");
                builder.append(requestData.substring(12, 15));
                builder.append(MessageUtil.computeChecksum(builder.toString()));

            }
        }


        LOG.info("Exit OLENettyServerHandler.scStatusNegativeResponse()  "+builder.toString());
        return builder.toString() + '\r';
    }


    private String removeSeqNumFromResponse(String lastResponse) {
        LOG.info("Inside OLENettyServerHandler.removeSeqNumFromResponse");
        StringBuilder builder = new StringBuilder();

        if (lastResponse.contains("|AY")) {
            int indexOfSeqNum = lastResponse.indexOf("|AY");
            String oldRespose = lastResponse.replace(lastResponse.substring(indexOfSeqNum), "");
            builder.append(oldRespose);
            builder.append("|AZ"+MessageUtil.computeChecksum(oldRespose));
        }
        LOG.info("Inside OLENettyServerHandler.removeSeqNumFromResponse   "+builder.toString());
        return builder.toString() + '\r';

    }

    public String getLoginUserId(String requestData) {

        String[] requestDataArray = requestData.split("\\|");
        String loginUser = "";
        try {
            for (String data : requestDataArray) {
                if (data.startsWith("93")) {
                    loginUser = data.substring(6);
                    break;
                }
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return loginUser;
    }


}
