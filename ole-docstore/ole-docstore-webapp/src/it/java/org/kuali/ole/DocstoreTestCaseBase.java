package org.kuali.ole;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class DocstoreTestCaseBase {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    protected String classesDir;

    @Autowired
    private ConfigurableApplicationContext context;

    protected ConfigurableApplicationContext getContext() {
        return context;
    }

    public DocstoreTestCaseBase() {
        classesDir = getClass().getResource("/").getPath();
    }

    @Test
    public void applicationContextNotNull() throws Exception {
        assertNotNull(context);
    }
}
