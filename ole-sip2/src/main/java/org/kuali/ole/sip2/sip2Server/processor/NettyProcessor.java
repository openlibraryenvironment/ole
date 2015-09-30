package org.kuali.ole.sip2.sip2Server.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.bo.OLEItemFine;
import org.kuali.ole.bo.OLELookupUser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by pvsubrah on 9/28/15.
 */
public abstract class NettyProcessor {

    private final static Logger LOG = Logger.getLogger(NettyProcessor.class.getName());

    private final String USER_AGENT = "Mozilla/5.0";
    private String circRestURL = "/rest/circ";

    public abstract boolean isInterested(String code);

    public abstract String process(String requestData);

    public abstract boolean isServiceTurnedOn();

    public abstract String getResponseForServiceTurnedOff(String requestData);

    public String postRequest(String requestData, String context, String serverURL) {
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

    private String sendPostRequest(String url, String requestContent) {
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

    public BigDecimal calculateTotalFineBalance(OLELookupUser oleLookupUser) {
        BigDecimal balanceAmount = new BigDecimal(0);
        if (oleLookupUser.getOleItemFines() != null) {
            List<OLEItemFine> oleItemFineList = oleLookupUser.getOleItemFines().getOleItemFineList();
            if (CollectionUtils.isNotEmpty(oleItemFineList)) {
                for (OLEItemFine oleItemFine : oleItemFineList) {
                    balanceAmount = balanceAmount.add(oleItemFine.getBalance());
                }
            }
        }
        return balanceAmount;
    }
}
