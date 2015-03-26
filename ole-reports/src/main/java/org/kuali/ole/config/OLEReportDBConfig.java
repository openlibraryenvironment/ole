package org.kuali.ole.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

import org.apache.commons.lang.StringUtils;

import java.util.Properties;

/**
 * Created by chenchulakshmig on 3/4/15.
 */
public class OLEReportDBConfig {

    private static final Logger LOG = Logger.getLogger(OLEReportDBConfig.class);
    Properties prop;

    public Properties getProp() {
        if (prop == null) {
            prop = new Properties();
            try {
                String environment = System.getProperty("environment");
                LOG.info(" Environment : " + environment);
                String path = System.getProperty("user.home") + File.separator + "kuali" + File.separator + "main" + File.separator + environment;
                LOG.info(" Path for common-config.xml file  : " + path);
                File file = new File(path + File.separator + "common-config.xml");
                if (file.exists() && file.isFile()) {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = null;
                    dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(file);
                    NodeList nList = doc.getElementsByTagName("param");
                    for (int i = 0; i < nList.getLength(); i++) {
                        Node nNode = nList.item(i);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) nNode;
                            if (element != null) {
                                String key = element.getAttribute("name");
                                String value = element.getFirstChild().getNodeValue();
                                prop.setProperty(key, value);
                            }
                        }
                    }
                    setAdditionalPropeties();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    private void setAdditionalPropeties() {
        String dbaVendor = prop.getProperty("db.vendor");
        if (StringUtils.isNotBlank(dbaVendor) && dbaVendor.equalsIgnoreCase("oracle")) {
            prop.setProperty("report.dba.url", prop.getProperty("oracle.dba.url"));
            prop.setProperty("report.dba.username", prop.getProperty("jdbc.username"));
            prop.setProperty("report.dba.password", prop.getProperty("jdbc.username"));
            prop.setProperty("report.dba.driver", "oracle.jdbc.driver.OracleDriver");
        } else if (StringUtils.isNotBlank(dbaVendor) && dbaVendor.equalsIgnoreCase("mysql")) {
            prop.setProperty("report.dba.url", prop.getProperty("mysql.dba.url") + File.separator + prop.getProperty("jdbc.username"));
            prop.setProperty("report.dba.username", prop.getProperty("mysql.dba.username"));
            prop.setProperty("report.dba.password", prop.getProperty("mysql.dba.password"));
            prop.setProperty("report.dba.driver", "com.mysql.jdbc.Driver");
        }
    }

    public String getPropertyByKey(String key) {
        return getProp().getProperty(key);
    }
}
