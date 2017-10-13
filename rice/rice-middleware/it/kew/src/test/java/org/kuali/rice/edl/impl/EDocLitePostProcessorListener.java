/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.edl.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Listens on a socket and reads EDocLitePostProcessor events
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class EDocLitePostProcessorListener implements Runnable {
    private static final Logger LOG = Logger.getLogger(EDocLitePostProcessorListener.class);
    private static final String KEY = "events";

    private ServerSocket ss;
    private int port;
    private int timesCalled;
    private Map<String, String> context;

    public EDocLitePostProcessorListener(Map<String, String> context) {
        this(8080, context);
    }

    public EDocLitePostProcessorListener(int port, Map<String, String> context) {
        this.port = port;
        this.context = context;
        context.put(KEY, "0");
    }

    public synchronized int getTimesCalled() {
        return timesCalled;
    }

    private synchronized int incrementTimesCalled() {
        timesCalled++;
        return timesCalled;
    }

    public void startListening() throws IOException {
        ss = new ServerSocket(port);
    }

    public void run() {
        StringBuffer response = new StringBuffer();
        while (true) {
            try {
                response.setLength(0);
                Socket s = ss.accept();
                int tc = incrementTimesCalled();
                LOG.debug("Received callback: " + tc);
                context.put(KEY, String.valueOf(tc));
                LOG.debug("CONTEXT events after callback: " + context.get(KEY));
                InputStream input = s.getInputStream();
                byte[] buf = new byte[1024];
                //BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                try {
                    int read;
                    while ((read = input.read(buf)) != -1) {
                        response.append(new String(buf, 0, read));
                    }
                    /*String line;
                    while ((line = input.readLine()) != null) {
                        System.out.println(line);
                    }*/
                } finally {
                    LOG.debug("PostProcessor event: " + response.toString());
                    input.close();
                    s.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
