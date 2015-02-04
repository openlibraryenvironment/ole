/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.docstore.process;

import org.kuali.ole.utility.DateTimeUtil;

/**
 * Class Bulk Ingest Time Manager.
 *
 * @author Rajesh Chowdary K
 * @created Apr 4, 2012
 */
public class BulkIngestTimeManager {

    private long recordsCount = 0;
    private long ingestingTimer = 0;
    private long indexingTimer = 0;
    private long processTimer = 0;

    public void reset() {
        recordsCount = 0;
        ingestingTimer = 0;
        indexingTimer = 0;
        processTimer = 0;
    }

    @Override
    public String toString() {
        return "Bulk Ingest Process Timer(" + recordsCount + "):\tIngesting Time: "
                + DateTimeUtil.formatTime(ingestingTimer) + "\tIndexing Time: " + DateTimeUtil.formatTime(indexingTimer)
                + "\tTotal Process Time: " + DateTimeUtil.formatTime(processTimer);
    }

    public long getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(long recordsCount) {
        this.recordsCount = recordsCount;
    }

    public long getIngestingTimer() {
        return ingestingTimer;
    }

    public void setIngestingTimer(long ingestingTimer) {
        this.ingestingTimer = ingestingTimer;
    }

    public long getIndexingTimer() {
        return indexingTimer;
    }

    public void setIndexingTimer(long indexingTimer) {
        this.indexingTimer = indexingTimer;
    }

    public long getProcessTimer() {
        return processTimer;
    }

    public void setProcessTimer(long processTimer) {
        this.processTimer = processTimer;
    }

}
