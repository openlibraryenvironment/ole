package org.kuali.ole.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 12/21/11
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);

    public static String postData(String target, String content)
            throws Exception {

        String response = "";
        URL url = new URL(target);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        Writer w = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        w.write(content);
        w.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        String temp;
        while ((temp = in.readLine()) != null) {
            response += temp + "\n";
        }
        temp = null;
        in.close();
        LOG.debug("Server response: " + response);
        return response;

    }

}
