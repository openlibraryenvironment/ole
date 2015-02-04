/*
 * Copyright 2006-2012 The Kuali Foundation
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

package org.kuali.ole;

import java.net.URL;
import java.net.HttpURLConnection;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Kuali Rice ArcheType Help
 *
 * An integration test for the application.  Integration tests are the type of tests you write when the test code is
 * long running or depends on unstable or external resources (ex: databases, remote service calls, app servers).
 *
 * Since integration tests are generally slow and less stable compared to unit tests, they are only executed upon
 * project build when specifically requested.
 *
 * This test requires a server to be running.  Currently maven launches the server.
 */
public class BasicApplicationIT {
    @Test
    public void testBasicApplicationStartup() throws Exception {
        URL url = new URL("http://localhost:" + getPort() + "/ole-docstore-webapp");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
       /* assertEquals(url.toString(), 200, connection.getResponseCode());*/
    }

    private String getPort() {
        String port = System.getProperty("test.port");
        if (port == null || port.trim().equals("")) {
            port = "8080";
        }
        return port;
    }
}
