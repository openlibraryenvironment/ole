package org.kuali.ole.sip2.sip2Server.processor;

import java.util.Properties;

/**
 * Created by pvsubrah on 9/28/15.
 */
public class PatronInformationNetttyProcessor extends NettyProcessor {

    private final Properties properties;

    public PatronInformationNetttyProcessor(Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("63") && properties.getProperty("sip2.service.patronInformation").equalsIgnoreCase("yes");
    }

    @Override
    public String process() {
        String response = null;

        return response;

    }


    public Properties getProperties() {
        return properties;
    }
}
