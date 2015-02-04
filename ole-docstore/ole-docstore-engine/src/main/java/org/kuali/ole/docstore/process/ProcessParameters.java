/*
 * Copyright 2011 The Kuali Foundation.
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

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to define all Process Parameters for Doc Store Processes.
 *
 * @author Rajesh Chowdary K
 * @created Mar 15, 2012
 */
public abstract class ProcessParameters {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessParameters.class);
    private static String BULK_DEFAULT_USER_IN = "webuser";
    public static final String BULK_DEFAULT_USER = getBulkUser();
    public static final String BULK_DEFUALT_ACTION = "bulkIngest";
    private static final String BULK_INGEST_UPLOAD_DIR_IN = "/opt/docstore/upload/ole-batchUpload";
    public static final String BULK_INGEST_UPLOAD_DIR = getBulkUploadDir();
    private static final long BULK_INGEST_POLL_INTERVAL_IN = 600000;
    public static final long BULK_INGEST_POLL_INTERVAL = getPollInterval();
    private static final long BULK_INGEST_OPTIMIZE_SIZE_IN = 50000;
    public static final long BULK_INGEST_COMMIT_SIZE = getCommitSize();
    public static final long BULK_INGEST_COMMIT_SIZE_IN = 5000;
    public static final long BULK_INGEST_OPTIMIZE_SIZE = getOptimizeSize();
    private static final boolean BULK_INGEST_IS_LINKING_ENABLED_IN = false;
    public static final boolean BULK_INGEST_IS_LINKING_ENABLED = getIsLinkingEnabled();
    private static final boolean IS_UPDATE_ENABLED_IN = true;
    public static final boolean IS_UPDATE_ENABLED = getIsUpdateEnabled();

    private static final int BULK_PROCESSOR_SPLIT_SIZE_IN = 50;
    public static final int BULK_PROCESSOR_SPLIT_SIZE = getSplitSize();
    private static final int BULK_PROCESSOR_THREADS_MIN_IN = 5;
    public static final int BULK_PROCESSOR_THREADS_MIN = getBulkThreadsMin();
    private static final int BULK_PROCESSOR_THREADS_MAX_IN = 200;
    public static final int BULK_PROCESSOR_THREADS_MAX = getBulkThreadsMax();
    private static final boolean BULK_PROCESSOR_MULTI_THREADED_IN = false;
    public static final boolean BULK_PROCESSOR_MULTI_THREADED = getIsMultiThreaded();
    public static final long BULK_PROCESSOR_TIMER_DISPLAY = 10000;
    public static final BulkIngestTimeManager BULK_PROCESSOR_TIME_MANAGER = new BulkIngestTimeManager();
    public static final boolean REBUILD_INDEXING_LINKING = getIsReIndexingLinkingEnabled();

    public static final long BUCKET_SIZE_LEVEL1 = 1000;
    public static final long BUCKET_SIZE_LEVEL2 = 1000;
    public static final long BUCKET_SIZE_LEVEL3 = 1000;
    public static final long BUCKET_SIZE_FILE_NODES = 1000;

    public static final String NODE_LEVEL1 = "l1";
    public static final String NODE_LEVEL2 = "l2";
    public static final String NODE_LEVEL3 = "l3";
    public static final String NODE_INSTANCE = "instanceNode";
    public static final String NODE_HOLDINGS = "holdingsNode";
    public static final String NODE_ITEM = "item";
    public static final String FILE_MARC = "marcFile";
    public static final String FILE_INSTANCE = "instanceFile";
    public static final String FILE_HOLDINGS = "holdingsFile";
    public static final String FILE_ITEM = "itemFile";
    public static final String FILE_SOURCE_HOLDINGS = "sourceHoldingsFile";
    public static final String FILE_PATRON_OLEML = "patronOlemlFile";
    public static final String FILE_OLE = "olefile";
    public static final String FILE_ONIXPL = "onixplFile";
    public static final String FILE = "File";

    public static final List<String> STATIC_NODES = new ArrayList<String>();
    public static final Map<String, Long> BUCKET_SIZES = new HashMap<String, Long>();
    public static final Map<String, Boolean> HAS_REPEATED_CHILD = new HashMap<String, Boolean>();

    static {
        STATIC_NODES.add("/work/bibliographic/marc");
        STATIC_NODES.add("/work/bibliographic/dublin");
        STATIC_NODES.add("/work/bibliographic/dublinunq");
        STATIC_NODES.add("/work/instance/oleml");

        BUCKET_SIZES.put(NODE_LEVEL1, BUCKET_SIZE_LEVEL1);
        BUCKET_SIZES.put(NODE_LEVEL2, BUCKET_SIZE_LEVEL2);
        BUCKET_SIZES.put(NODE_LEVEL3, BUCKET_SIZE_LEVEL3);
        BUCKET_SIZES.put(NODE_INSTANCE, BUCKET_SIZE_LEVEL3);

        HAS_REPEATED_CHILD.put(NODE_LEVEL1, true);
        HAS_REPEATED_CHILD.put(NODE_LEVEL2, true);
        HAS_REPEATED_CHILD.put(NODE_LEVEL3, true);
        HAS_REPEATED_CHILD.put(NODE_INSTANCE, false);
    }

    private static String getBulkUser() {
        try {
            return ConfigContext.getCurrentContextConfig().getProperty("batch.user").trim();
        } catch (Exception e) {
            LOGGER.error("Please set a value for \"batch.user\"", e);
            return BULK_DEFAULT_USER_IN;
        }
    }

    public static String getBulkUploadDir() {
        try {
            return ConfigContext.getCurrentContextConfig().getProperty("batch.upload.dir").trim();
        } catch (Exception e) {
            LOGGER.error("Please set a value for \"batch.upload.dir\"", e);
            return BULK_INGEST_UPLOAD_DIR_IN;
        }
    }

    private static int getSplitSize() {
        try {
            return new Integer(ConfigContext.getCurrentContextConfig().getProperty("batch.split.size").trim()).intValue();
        } catch (Exception e) {
            LOGGER.error("Please set a value for \"batch.split.size\"", e);
            return BULK_PROCESSOR_SPLIT_SIZE_IN;
        }
    }

    private static long getPollInterval() {
        try {
            return (new Long(ConfigContext.getCurrentContextConfig().getProperty("batch.poll.intervel").trim()) * 60L * 1000L);
        } catch (Exception e) {
            LOGGER.error("Please set a value for \"batch.poll.intervel\"", e);
            return BULK_INGEST_POLL_INTERVAL_IN;
        }
    }

    private static long getOptimizeSize() {
        try {
            return (new Long(ConfigContext.getCurrentContextConfig().getProperty("batch.optimize.size").trim()));
        } catch (Exception e) {
            LOGGER.error("Please set a value for \"batch.optimize.size\"", e);
            return BULK_INGEST_OPTIMIZE_SIZE_IN;
        }
    }

    private static int getBulkThreadsMin() {
        try {
            return new Integer(ConfigContext.getCurrentContextConfig().getProperty("batch.threads.min").trim());
        } catch (Exception e) {
            LOGGER.error("Please set an integer value for \"batch.threads.min\"");
            return BULK_PROCESSOR_THREADS_MIN_IN;
        }
    }

    private static int getBulkThreadsMax() {
        try {
            return new Integer(ConfigContext.getCurrentContextConfig().getProperty("batch.threads.max").trim());
        } catch (Exception e) {
            LOGGER.error("Please set an integer value for \"batch.threads.max\"");
            return BULK_PROCESSOR_THREADS_MAX_IN;
        }
    }

    private static boolean getIsMultiThreaded() {
        try {
            return false; // YTI
            // return new Boolean(PropertyUtil.getPropertyUtil().getProperty("batch.threads.multiThreaded").trim());
        } catch (Exception e) {
            LOGGER.error("Please set a value for \"batch.threads.multithreaded\"", e);
            return BULK_PROCESSOR_MULTI_THREADED_IN;
        }
    }

    private static boolean getIsLinkingEnabled() {
        try {
            return new Boolean(ConfigContext.getCurrentContextConfig().getProperty("batch.linking.enabled").trim());
        } catch (Exception e) {
            LOGGER.error("Please set a value for \"batch.linking.enabled\"", e);
            return BULK_INGEST_IS_LINKING_ENABLED_IN;
        }
    }

    private static boolean getIsReIndexingLinkingEnabled() {
        try {
            return new Boolean(ConfigContext.getCurrentContextConfig().getProperty("rebuild.indexing.link.enable").trim());
        } catch (Exception e) {
            LOGGER.error("Please set a value for \"rebuild.indexing.link.enable\"", e);
            return REBUILD_INDEXING_LINKING;
        }
    }

    private static long getCommitSize() {
        try {
            return (new Long(ConfigContext.getCurrentContextConfig().getProperty("batch.commit.size").trim()));
        } catch (Exception e) {
            LOGGER.error("Please set a value for \"batch.commit.size\"", e);
            return BULK_INGEST_COMMIT_SIZE_IN;
        }
    }

    private static boolean getIsUpdateEnabled() {
        try {
            return new Boolean(ConfigContext.getCurrentContextConfig().getProperty("batch.update.enabled").trim());
        } catch (Exception e) {
            LOGGER.error("Please set a value for \"batch.update.enabled\"", e);
            return IS_UPDATE_ENABLED_IN;
        }
    }
}
