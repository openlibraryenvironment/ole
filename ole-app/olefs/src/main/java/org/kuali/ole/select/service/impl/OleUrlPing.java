/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.service.impl;

import org.kuali.ole.select.service.impl.exception.DocStoreConnectionException;

import java.net.HttpURLConnection;
import java.net.URL;

public class OleUrlPing {

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleUrlPing.class);

    public String urlPing(String url) {
        String msg = "success";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
        } catch (Exception e) {
            throw new DocStoreConnectionException("Docstore Server Unavailable.", e);
        }
        return msg;
    }


}
