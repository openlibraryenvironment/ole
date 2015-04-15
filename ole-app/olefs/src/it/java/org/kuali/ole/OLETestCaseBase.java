package org.kuali.ole;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by sheiksalahudeenm on 7/4/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/kuali/ole/TestBootStrapSpringBeans.xml" })
public class OLETestCaseBase {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    protected String classesDir;


    public OLETestCaseBase() {
        classesDir = getClass().getResource("/").getPath();
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
