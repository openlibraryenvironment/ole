package org.kuali.asr;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.asr.bo.*;
import org.kuali.asr.handler.ResponseHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 2/13/14
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ASRRequestProcessor_IT {
    private final String USER_AGENT = "Mozilla/5.0";

    private Logger LOG = org.apache.log4j.Logger.getLogger(ASRRequestProcessor_IT.class);
    private String baseUrl="http://localhost:8080/olefs/asrService/asr";
    ResponseHandler responseHandler =  new ResponseHandler();

    @Test
    public void lookupNewASRItem() throws Exception {
        String format="json";
        String url =baseUrl+"/lookupNewAsrItems/dev2";
        sendGetRequest(url,format);
    }

    @Test
    public void lookupNewASRTypeRequest() throws Exception {
        String format="json";
        String url =baseUrl+"/lookupAsrRequests/dev2,ASR";
        sendGetRequest(url,format);
    }

    @Test
    public void lookupASRRequests() throws Exception {
        String format="json";
        String url =baseUrl+"/lookupAsrItemHolds/dev2,010";
        sendGetRequest(url,format);
    }

    @Test
    public void placeRequest() throws Exception {
        String format="json";
        String url =baseUrl+"/placeASRTypeRequest";
        PlaceASRItemRequestBo placeASRItemRequestBo = new PlaceASRItemRequestBo();
        placeASRItemRequestBo.setItemBarcode("0101");
        placeASRItemRequestBo.setOperatorId("dev2");
        placeASRItemRequestBo.setPatronId("00100055U");
        placeASRItemRequestBo.setPickUpLocation("BL_EDUC");
        String parameter;
        if(format.equals("json"))
            parameter=responseHandler.marshalObjectToJson(placeASRItemRequestBo);
        else
            parameter=responseHandler.marshalObjectToXml(placeASRItemRequestBo);

        /*String parameter = "<placeASRRequest>\n" +
                "<itemBarcode>0101</itemBarcode>\n" +
                "<patronId>00100055U</patronId>\n" +
                "<operatorId>dev2</operatorId>\n" +
                "<pickUpLocation>BL_EDUC</pickUpLocation>\n" +
                "</placeASRRequest>";*/
        sendPostRequest(url, parameter,format);
    }

    @Test
    public void cancelRequest() throws Exception {
        String format="json";
        String url =baseUrl+"/cancelHold/2,dev2";
        sendDeleteRequest(url,format);
    }

    @Test
    public void removeItem() throws Exception {
        String format="json";
        String url =baseUrl+"/removeASRItem/0103";
        sendDeleteRequest(url,format);
    }

    @Test
    public void updateRequestStatus() throws Exception {
        String format="json";
        String url =baseUrl+"/updateASRTypeRequest";
        UpdateASRRequestStatusBo updateASRRequestStatusBo = new UpdateASRRequestStatusBo();
        updateASRRequestStatusBo.setHoldId("1");
        updateASRRequestStatusBo.setOperatorId("dev2");
        updateASRRequestStatusBo.setStatus("3");
        String parameter;
        if(format.equals("json"))
            parameter=responseHandler.marshalObjectToJson(updateASRRequestStatusBo);
        else
            parameter=responseHandler.marshalObjectToXml(updateASRRequestStatusBo);
        /*String parameter = "<updateASRRequestStatus>\n" +
                "<holdId>1</holdId>\n" +
                "<status>3</status>\n" +
                "<operatorId>dev2</operatorId>\n" +
                "</updateASRRequestStatus>";*/
        sendPostRequest(url, parameter, format);
    }

    @Test
    public void receiveTransistOfASRItem() throws Exception {
        String format="json";
        String url =baseUrl;
        ReceiveTransitRequestBo receiveTransitRequestBo = new ReceiveTransitRequestBo();
        receiveTransitRequestBo.setBarcode("0101");
        receiveTransitRequestBo.setOperatorId("dev2");
        String parameter;
        if(format.equals("json"))
            parameter=responseHandler.marshalObjectToJson(receiveTransitRequestBo);
        else
            parameter=responseHandler.marshalObjectToXml(receiveTransitRequestBo);

        /*String parameter = "<receiveTransitRequest>\n" +
                "        <barcode>0101</barcode>\n" +
                "        <operatorId>dev2</operatorId>\n" +
                "        </receiveTransitRequest>";*/
        sendPostRequest(url, parameter,"");
    }

    @Test
    public void updateASRItemStatusAvailable() throws Exception {
        String format="json";
        String url =baseUrl+"/updateASRItemStatusAvailable";
        UpdateASRItemRequestBo updateASRItemRequestBo = new UpdateASRItemRequestBo();
        updateASRItemRequestBo.setItemBarcode("0101");
        updateASRItemRequestBo.setItemStatus("csa");
        updateASRItemRequestBo.setOperatorId("dev2");
        String parameter;
        if(format.equals("json"))
            parameter=responseHandler.marshalObjectToJson(updateASRItemRequestBo);
        else
            parameter=responseHandler.marshalObjectToXml(updateASRItemRequestBo);

       /* String parameter = "<updateASRItem>\n" +
                "<itemBarcode>0101</itemBarcode>\n" +
                "<itemStatus>csp</itemStatus>\n" +
                "<operatorId>dev2</operatorId>\n" +
                "</updateASRItem>";*/
        sendPostRequest(url, parameter,format);
    }

    @Test
    public void updateASRItemStatusMissing() throws Exception {
        String format="json";
        String url =baseUrl+"/updateASRItemStatusMissing";
        UpdateASRItemStatusBo updateASRItemStatusBo = new UpdateASRItemStatusBo();
        updateASRItemStatusBo.setItemBarcode("0102");
        updateASRItemStatusBo.setItemStatus("csn");
        updateASRItemStatusBo.setOperatorId("dev2");
        String parameter;
        if(format.equals("json"))
            parameter=responseHandler.marshalObjectToJson(updateASRItemStatusBo);
        else
            parameter=responseHandler.marshalObjectToXml(updateASRItemStatusBo);

       /* String parameter = "<updateASRItemStatus>\n" +
                "<itemBarcode>0102</itemBarcode>\n" +
                "<itemStatus>csn</itemStatus>\n" +
                "<operatorId>dev2</operatorId>\n" +
                "</updateASRItemStatus>";*/
        sendPostRequest(url,parameter,format);
    }

    @Test
    public void updateASRItemStatusRetrive() throws Exception {
        String format="json";
        String url =baseUrl+"/updateASRItemStatusBeingRetrieved";
        UpdateASRItemStatusBo updateASRItemStatusBo = new UpdateASRItemStatusBo();
        updateASRItemStatusBo.setItemBarcode("0102");
        updateASRItemStatusBo.setItemStatus("csn");
        updateASRItemStatusBo.setOperatorId("dev2");
        String parameter;
        if(format.equals("json"))
            parameter=responseHandler.marshalObjectToJson(updateASRItemStatusBo);
        else
            parameter=responseHandler.marshalObjectToXml(updateASRItemStatusBo);
        /*String parameter = "<updateASRItemStatus>\n" +
                "<itemBarcode>0103</itemBarcode>\n" +
                "<itemStatus>csp</itemStatus>\n" +
                "<operatorId>dev2</operatorId>\n" +
                "</updateASRItemStatus>";*/
        sendPostRequest(url, parameter,format);
    }

    public void sendGetRequest(String request,String format) throws Exception {
        String url = request;
        HttpClient client = new HttpClient();
        // Create a method instance.
        GetMethod method = new GetMethod(url);
        method.addRequestHeader("Accept", "application/"+format);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            // Execute the method.
            int statusCode = 0;
            try {
                statusCode = client.executeMethod(method);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            System.out.println(new String(responseBody));

        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
    }


    private void sendPostRequest(String url, String urlParameters,String format) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept", "application/" + format);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/"+format);

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());

    }

    public void sendDeleteRequest(String request,String format) throws Exception {
        String url = request;
        HttpClient client = new HttpClient();
        // Create a method instance.
        DeleteMethod method = new DeleteMethod(url);
        method.addRequestHeader("Accept", "application/"+format);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            // Execute the method.
            int statusCode = 0;
            try {
                statusCode = client.executeMethod(method);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            System.out.println(new String(responseBody));

        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
    }


}
