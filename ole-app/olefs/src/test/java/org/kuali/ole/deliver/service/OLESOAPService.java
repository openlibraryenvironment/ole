package org.kuali.ole.deliver.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sheiksalahudeenm on 5/5/15.
 */
public class OLESOAPService {

    public static String sendSoapRequest(String url,String requestContent){
        String responseString = "";
        try {
            URL oURL = new URL(url);
            HttpURLConnection con = (HttpURLConnection) oURL.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setDoOutput(true);
            OutputStream reqStream = con.getOutputStream();
            reqStream.write(requestContent.getBytes());
            InputStream resStream = con.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(resStream));
            String line = null;
            StringBuilder responseContentBuilder = new StringBuilder();
            while((line = in.readLine()) != null) {
                responseContentBuilder.append(line);
            }
            responseString = responseContentBuilder.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseString;
    }
}
