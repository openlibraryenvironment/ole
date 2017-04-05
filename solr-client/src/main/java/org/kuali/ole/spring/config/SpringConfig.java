package org.kuali.ole.spring.config;

import org.kuali.ole.spring.cache.MemoizerAspect;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sheiks on 10/11/16.
 */
@Configuration
public class SpringConfig {

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new CustomScopeRegisteringBeanFactoryPostProcessor();
    }

    @Bean
    public MemoizerAspect memoizerAspect() {
        return new MemoizerAspect();
    }

}
