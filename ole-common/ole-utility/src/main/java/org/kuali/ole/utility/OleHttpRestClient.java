package org.kuali.ole.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleHttpRestClient {

    private final String USER_AGENT = "Mozilla/5.0";

    public String sendPostRequest(String url, String requestContent, String format) {
        String responseContent = null;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept", "application/" + format);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/"+format);

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
}
