package org.kuali.ole.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by chenchulakshmig on 3/4/15.
 */
public class OLEBirtDBConfig {

    Properties prop;

    public Properties getProp() {
        if (prop == null) {
            prop = new Properties();
            InputStream input = null;
            try {
                URL resource = getClass().getResource("/org/kuali/ole/config/birt-db-config.properties");
                File file = new File(resource.toURI());
                input = new FileInputStream(file);
                prop.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    public String getPropertyByKey(String key) {
        return getProp().getProperty(key);
    }
}
