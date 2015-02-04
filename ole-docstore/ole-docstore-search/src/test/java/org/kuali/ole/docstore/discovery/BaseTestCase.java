package org.kuali.ole.docstore.discovery;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.discovery.util.DiscoveryEnvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * This is the base class for all other test classes.
 * It defines some values related to environment for the test cases.
 * It also has some utility methods that can be used by subclasses.
 *
 * @author Tirumalesh
 *         Date: 22/11/11
 */
public abstract class BaseTestCase {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    protected String classesDir;
    protected String solrHome;
    protected DiscoveryEnvUtil discoveryEnvUtil = new DiscoveryEnvUtil();


    public BaseTestCase() {
        classesDir = getClass().getResource("/").getPath();
        printLogInfo("classesDir = " + new File(classesDir).getAbsolutePath());
        discoveryEnvUtil.initTestEnvironment();
        discoveryEnvUtil.printEnvironment();


 /*       solrHome = classesDir + "/../../../ole-solr-config";
        printLogInfo("solrHome = " + new File(solrHome).getAbsolutePath());

        String logFilePath = classesDir + "log4j.properties";
        PropertyConfigurator.configure(logFilePath);
        printLogInfo("log4j properties file = " + logFilePath);

        System.setProperty("discovery.properties.file", DiscoveryConstants.TEST_DISCOVERY_PROP_FILE);
        System.setProperty(SolrServerManager.SOLR_HOME, solrHome);
*/
        System.setProperty(SolrServerManager.SOLR_SERVER_EMBEDDED, "true");

    }

    protected void printLogInfo(String msg) {
        System.out.println(msg);
        LOG.info(msg);
    }

    protected void setUp() throws Exception {
    }

    protected String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }

    protected QueryResponse executeQuery(String args) throws Exception {
        SolrServer solr = SolrServerManager.getInstance().getSolrServer();
        SolrQuery query = new SolrQuery();
        query.setQuery(args);
        QueryResponse response = solr.query(query);
        return response;
    }

    protected long getRecordCount() throws Exception {
        QueryResponse response = executeQuery("*:*");
        return response.getResults().getNumFound();
    }


    protected void cleanUpData() throws Exception {
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        server.deleteByQuery("*:*");
        server.commit();
    }

    protected static String getMethodName(StackTraceElement e[]) {
        boolean doNext = false;
        String result = "";
        for (StackTraceElement s : e) {
            if (doNext) {
                result = s.getMethodName();
                break;
            }
            doNext = s.getMethodName().equals("getStackTrace");
        }
        return result;
    }

    protected static Method getCallingMethod() throws ClassNotFoundException {
        final Thread t = Thread.currentThread();
        final StackTraceElement[] stackTrace = t.getStackTrace();
        final StackTraceElement ste = stackTrace[2];
        final String methodName = ste.getMethodName();
        final String className = ste.getClassName();
        Class<?> kls = Class.forName(className);
        do {
            for (final Method candidate : kls.getDeclaredMethods()) {
                if (candidate.getName().equals(methodName)) {
                    return candidate;
                }
            }
            kls = kls.getSuperclass();
        } while (kls != null);
        return null;
    }

}
