package org.kuali.ole.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by sheiks on 27/10/16.
 */
@Component
public class PropertyValueProvider {

    @Autowired
    private Environment env;

    public String getProperty(String key) {
        return env.getProperty(key);
    }
}
