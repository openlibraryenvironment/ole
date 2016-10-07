package org.kuali.ole;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.batch.service.OLEBatchSchedulerService;
import org.kuali.ole.select.service.OleTransmissionService;
import org.kuali.ole.sys.batch.service.SchedulerService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.service.impl.DocumentServiceImpl;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by sheiksalahudeenm on 7/4/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/kuali/ole/TestBootStrapSpringBeans.xml" })
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)
@Transactional
public class OLETestCaseBase {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    protected String classesDir;

    @Autowired
    private ConfigurableApplicationContext context;

    protected ConfigurableApplicationContext getContext() {
        return context;
    }

    public OLETestCaseBase() {
        classesDir = getClass().getResource("/").getPath();
    }

    @Before
    public void setUp() throws Exception {
        SpringContext.setApplicationContext(context);
        SpringContext.finishInitializationAfterRiceStartup();

        ConfigurationService configurationService = GlobalResourceLoader.getService("kualiConfigurationService");
        if (configurationService.getPropertyValueAsBoolean("use.quartz.scheduling")) {
            try {
                LOG.info("Attempting to initialize the SchedulerService");
                SpringContext.getBean(SchedulerService.class).initialize();
                SpringContext.getBean(OLEBatchSchedulerService.class).initialize();
                LOG.info("Starting the Quartz scheduler");
                SpringContext.getBean(Scheduler.class).start();
            } catch (NoSuchBeanDefinitionException e) {
                LOG.warn("Not initializing the scheduler because there is no scheduler bean");
            } catch ( Exception ex ) {
                LOG.error("Caught Exception while starting the scheduler", ex);
            }
        }
        DocumentServiceImpl documentService = (DocumentServiceImpl) SpringContext.getBean("documentService");
        documentService.setDocumentDao((DocumentDao) SpringContext.getBean("documentDao"));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void applicationContextNotNull() throws Exception {
        assertNotNull(context);
    }
}
