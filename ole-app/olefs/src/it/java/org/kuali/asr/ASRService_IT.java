package org.kuali.asr;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.junit.Test;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 2/13/14
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ASRService_IT {
    //    private static String url = "http://ebsco.htcindia.com/olefs/ilsapi/version";
    //private static String url = "http://localhost:8080/olefs/ilsapi/version";
    private String baseUrl="http://localhost:8080/olefs/asrService/asr";

    @Test
    public void lookupNewASRItem() throws Exception {
        String url =baseUrl+"/lookupNewAsrItems/dev2";
        sendGetRequest(url);
    }

    @Test
    public void lookupNewASRTypeRequest() throws Exception {
        String url =baseUrl+"/lookupAsrRequests/dev2,ASR";
        sendGetRequest(url);
    }

    @Test
    public void lookupASRRequests() throws Exception {
        String url =baseUrl+"/lookupAsrItemHolds/dev2,010";
        sendGetRequest(url);
    }

    @Test
    public void placeRequest() throws Exception {
        String url =baseUrl;
        sendGetRequest(url);
    }

    @Test
    public void cancelRequest() throws Exception {
        String url =baseUrl;
        sendGetRequest(url);
    }

    @Test
    public void removeItem() throws Exception {
        String url =baseUrl;
        sendGetRequest(url);
    }

    @Test
    public void updateRequestStatus() throws Exception {
        String url =baseUrl;
        sendGetRequest(url);
    }

    @Test
    public void receiveTransistOfASRItem() throws Exception {
        String url =baseUrl;
        sendGetRequest(url);
    }

    @Test
    public void updateASRItemStatusAvailable() throws Exception {
        String url =baseUrl;
        sendGetRequest(url);
    }

    @Test
    public void updateASRItemStatusMissing() throws Exception {
        String url =baseUrl;
        sendGetRequest(url);
    }

    @Test
    public void updateASRItemStatusRetrive() throws Exception {
        String url =baseUrl;
        sendGetRequest(url);
    }






    @Test
    public void getVersionFromRemotURL() throws Exception {
        String url = "http://ebsco.htcindia.com/olefs/ilsapi/version";
        sendGetRequest(url);
    }


    @Test
    public void getLookupPatronFromRemotURL() throws Exception {
        String url = "http://ebsco.htcindia.com/olefs/ilsapi/patron/lookupPatron/6010570002006861";
        sendGetRequest(url);
    }


    @Test
    public void getPatronProfileFromRemoteURL() throws Exception {
        String url = "http://ebsco.htcindia.com/olefs/ilsapi/patron/profile/00001497Q";
        sendGetRequest(url);
    }

    @Test
    public void getAccountSummaryFromRemoteURL() throws Exception {
        String url = "http://ebsco.htcindia.com/olefs/ilsapi/patron/account/00001497Q";
        sendGetRequest(url);
    }

    @Test
    public void getCheckOutsFromRemoteURL() throws Exception {
        String url = "http://ebsco.htcindia.com/olefs/ilsapi/patron/account/check-outs/00001497Q";
        sendGetRequest(url);
    }

    @Test
    public void getHoldsFromRemoteURL() throws Exception {
        String url = "http://ebsco.htcindia.com/olefs/ilsapi/patron/account/holds/00001497Q";
        sendGetRequest(url);
    }

    @Test
    public void getFiscalsFromRemoteURL() throws Exception {
        String url = "http://ebsco.htcindia.com/olefs/ilsapi/patron/account/fiscals/00001497Q";
        sendGetRequest(url);
    }

    @Test
    public void getPickUpLocationsFromRemoteURL() throws Exception {
        String url = "http://ebsco.htcindia.com/olefs/ilsapi/ils/pickupLocations";
        sendGetRequest(url);
    }

    @Test
    public void getItemStatusBibIdFromRemoteURL() throws Exception {
        String url = "http://ebsco.htcindia.com/olefs/ilsapi/rtac/itemStatusByBibId";
//        sendGetRequest(url);
    }

    public void sendGetRequest(String request) throws Exception {
        String url = request;
        HttpClient client = new HttpClient();
        // Create a method instance.
        GetMethod method = new GetMethod(url);
        method.addRequestHeader("Accept", "application/json");

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


    public void sendPostRequest(String request) throws Exception {
        String url = request;
        HttpClient client = new HttpClient();
        // Create a method instance.
        PostMethod method = new PostMethod(url);
        method.addRequestHeader("Accept", "application/json");

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
