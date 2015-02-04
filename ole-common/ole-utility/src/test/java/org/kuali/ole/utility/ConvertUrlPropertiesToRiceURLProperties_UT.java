package org.kuali.ole.utility;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ConvertUrlPropertiesToRiceURLProperties_UT {

    @Test
    public void convert() throws Exception {
        URL xmlLocation = ConvertUrlPropertiesToRiceURLProperties_UT.class.getProtectionDomain().getCodeSource().getLocation();
        String propertiesFileLocation=xmlLocation.getFile();
        propertiesFileLocation=propertiesFileLocation.substring(0,propertiesFileLocation.indexOf("/target/",0));
        String location = propertiesFileLocation+"/target/classes/org/kuali/ole/url-rice-properties.xml";
        Properties p = org.kuali.common.util.PropertyUtils.load(location);
        List<String> keys = org.kuali.common.util.PropertyUtils.getSortedKeys(p);
        StringBuilder sb = new StringBuilder();
        sb.append("<config>\n");
        for (String key : keys) {
            String value = p.getProperty(key);
            sb.append("  <param name=\"" + key + "\">" + value + "</param>\n");
        }
        sb.append("</config>\n");
        System.out.println(sb);

    }

}
