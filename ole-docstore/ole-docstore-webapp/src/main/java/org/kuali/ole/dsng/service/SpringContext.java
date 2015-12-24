package org.kuali.ole.dsng.service;

import org.springframework.context.ApplicationContext;

/**
 * Created by SheikS on 12/23/2015.
 */
public class SpringContext {

    public static Object getBean(String beanId) {
        ApplicationContext applicationContext = ApplicationContextProvider.getInstance().getApplicationContext();
        return applicationContext.getBean(beanId);
    }
}
