package org.kuali.ole;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.discovery.util.DiscoveryEnvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/18/13
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:org/kuali/ole/BootStrapSpringBeans.xml")
//@ContextConfiguration("file:ole-docstore/ole-docstore-webapp/src/main/resources/org/kuali/ole/BootStrapSpringBeans.xml")
public class SpringBaseTestCase {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    protected String classesDir;
    protected String solrHome;
    protected DiscoveryEnvUtil discoveryEnvUtil = new DiscoveryEnvUtil();


    public SpringBaseTestCase() {
        classesDir = getClass().getResource("/").getPath();
//        printLogInfo("classesDir = " + new File(classesDir).getAbsolutePath());
        discoveryEnvUtil.initTestEnvironment();
        discoveryEnvUtil.printEnvironment();
//        System.setProperty(SolrServerManager.SOLR_SERVER_EMBEDDED, "true");
    }
        @Autowired
    private ApplicationContext context;

    protected ApplicationContext getContext() {
        return context;
    }

    @Test
    public void applicationContextNotNull() throws Exception {
        assertNotNull(context);
    }
}
