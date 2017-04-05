package org.kuali.ole.spring;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.*;

/**
 * Created by sheiks on 27/10/16.
 */
public class ApplicationContextProviderTest extends BaseTestCase{
    @Test
    public void getInstance() throws Exception {

        ApplicationContext applicationContext = ApplicationContextProvider.getInstance().getApplicationContext();
        assertNotNull(applicationContext);

    }

}