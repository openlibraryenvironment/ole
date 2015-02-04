package org.kuali.ole.describe.service;


import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/18/12
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockDocstoreHelperService {

    private static final String DOCSTORE_URL = "docstore.url";
    private final String ROLLBACK_DATA_FROM_DOCSTORE = "docAction=deleteWithLinkedDocs&requestContent=";

    public void rollbackData(String xmlForRollback) throws Exception {
        String queryString = ROLLBACK_DATA_FROM_DOCSTORE + URLEncoder.encode(xmlForRollback, "UTF-8");
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        postData(docstoreURL, queryString + queryString);
    }

    public static String postData(String target, String content) throws Exception {
        String response = "";
        URL url = new URL(target);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        Writer w = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        w.write(content);
        w.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String temp;
        while ((temp = in.readLine()) != null) {
            response += temp + "\n";
        }
        in.close();
        return response;
    }
}
