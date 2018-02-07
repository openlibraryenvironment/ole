/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.devtools.datadictionary;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * This is a description of what this class does - gilesp don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class URLMonitor {
    private static final Log LOG = LogFactory.getLog(URLMonitor.class);

    private final LinkedList<URLContentChangedListener> listeners = new LinkedList<URLContentChangedListener>();
    private final Map<URL, Long> resourceMap = new ConcurrentHashMap();
    private final int reloadIntervalMilliseconds;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public URLMonitor(int reloadIntervalMilliseconds) {
        this.reloadIntervalMilliseconds = reloadIntervalMilliseconds;
    }

    public void shutdownScheduler() {
        scheduler.shutdown();
    }

    public synchronized void addListener(URLContentChangedListener listener) {

        listeners.add(listener);

        if (listeners.size() == 1) {
            scheduler.scheduleAtFixedRate(urlPoller,
				reloadIntervalMilliseconds, reloadIntervalMilliseconds, TimeUnit.MILLISECONDS);
        }
    }

    public void addURI(URL zipUrl) {

        resourceMap.put(zipUrl, getCRC(zipUrl));
    }

    private Long getCRC(URL zipUrl) {
        Long result = -1l;
        try {
            CRC32 crc = new CRC32();
            CheckedInputStream cis = new CheckedInputStream(zipUrl.openStream(), crc);

            byte[] buffer = new byte[1024];
            int length;

            //read the entry from zip file and extract it to disk
            while( (length = cis.read(buffer)) > 0);

            cis.close();

            result = crc.getValue();
        } catch (IOException e) {
            LOG.warn("Unable to calculate CRC, resource doesn't exist?", e);
        }
        return result;
    }

    private final Runnable urlPoller = new Runnable() {

        @Override
        public void run() {
            for (Map.Entry<URL, Long> entry : resourceMap.entrySet()) {
                Long crc = getCRC(entry.getKey());

                if (!entry.getValue().equals(crc)) {
                    entry.setValue(crc);
                    for (URLContentChangedListener listener : listeners) {
                        listener.urlContentChanged(entry.getKey());
                    }
                }
            }
        }
    };

    public static interface URLContentChangedListener {
          public void urlContentChanged(URL url);
    }
}