package org.kuali.ole.select.gokb.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.GOKBConnectionReader;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by rajeshbabuk on 12/18/14.
 */
public class OleGokbXmlUtil {

    private static final Logger LOG = LoggerFactory.getLogger(OleGokbXmlUtil.class);
    //private static final String COMMON_URL = "https://gokb.k-int.com/gokb/oai/";
    private static final String COMMON_URL = "https://test-gokb.kuali.org/gokb/oai/";
    private static final String PACKAGE_URL = "packages?verb=";
    private static final String TITLE_URL = "titles?verb=";
    private static final String PLATFORM_URL = "platforms?verb=";
    private static final String ORGS_URL = "orgs?verb=";
    private static final String LIST_RECORDS_URL = "listRecords&metadataPrefix=gokb&resumptionToken=||";
    private static final String GET_IDENTIFIER_RECORDS_URL = "getRecord&metadataPrefix=gokb&identifier=";
    private static final String LIST_IDENTIFIERS_URL = "ListIdentifiers&metadataPrefix=gokb&resumptionToken=||";
    private static final String FROM_URL = "&from=";
    private static final String PIPE = "|";
    private static final String GOKB = "gokb";
    private static final String COMPLETE_LIST_SIZE = "//resumptionToken/@completeListSize";
    private static XPath xPath = XPathFactory.newInstance().newXPath();
    /**
     * This method returns the package response from the url.
     * @param endIndex
     * @return
     */
    public static String getPackageResponseXmlFromGokb(int endIndex) {
        // URL : https://gokb.k-int.com/gokb/oai/packages?verb=listRecords&metadataPrefix=gokb&resumptionToken=||3|gokb
        String url = COMMON_URL + PACKAGE_URL + LIST_RECORDS_URL + endIndex + PIPE + GOKB;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting package response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns the title response from the url.
     * @param endIndex
     * @return
     */
    public static String getTitleResponseXmlFromGokb(int endIndex) {
        // URL : https://gokb.k-int.com/gokb/oai/titles?verb=listRecords&metadataPrefix=gokb&resumptionToken=||6|gokb
        String url = COMMON_URL + TITLE_URL + LIST_RECORDS_URL + endIndex + PIPE + GOKB;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting title response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns the platform response from the url.
     * @param endIndex
     * @return
     */
    public static String getPlatformResponseXmlFromGokb(int endIndex) {
        // URL : https://gokb.k-int.com/gokb/oai/platforms?verb=listRecords&metadataPrefix=gokb&resumptionToken=||6|gokb
        String url = COMMON_URL + PLATFORM_URL + LIST_RECORDS_URL + endIndex + PIPE + GOKB;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting platform response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns the organization response from the url.
     * @param endIndex
     * @return
     */
    public static String getOrgsResponseXmlFromGokb(int endIndex) {
        // URL : https://gokb.k-int.com/gokb/oai/orgs?verb=listRecords&metadataPrefix=gokb&resumptionToken=||6|gokb
        String url = COMMON_URL + ORGS_URL + LIST_RECORDS_URL + endIndex + PIPE + GOKB;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting orgs response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns the package response based on the last update time from the url.
     * @param lastUpdatedTime
     * @param endIndex
     * @return
     */
    public static String getPackageResponseXmlFromGokb(String lastUpdatedTime, int endIndex) {
        // URL : https://gokb.k-int.com/gokb/oai/packages?verb=ListIdentifiers&metadataPrefix=gokb&resumptionToken=||6&from=2014-12-01T20:40:01Z
        String url = COMMON_URL + PACKAGE_URL + LIST_IDENTIFIERS_URL + endIndex + FROM_URL + lastUpdatedTime;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting package response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns the platfrom response based on the last update time from the url.
     * @param lastUpdatedTime
     * @param endIndex
     * @return
     */
    public static String getPlatformResponseXmlFromGokb(String lastUpdatedTime, int endIndex) {
        // URL : https://gokb.k-int.com/gokb/oai/platforms?verb=ListIdentifiers&metadataPrefix=gokb&resumptionToken=||6&from=2014-12-01T20:40:01Z
        String url = COMMON_URL + PLATFORM_URL + LIST_IDENTIFIERS_URL + endIndex + FROM_URL + lastUpdatedTime;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting platform response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns the title response based on the last update time from the url.
     * @param lastUpdatedTime
     * @param endIndex
     * @return
     */
    public static String getTitleResponseXmlFromGokb(String lastUpdatedTime, int endIndex) {
        // URL : https://gokb.k-int.com/gokb/oai/titles?verb=ListIdentifiers&metadataPrefix=gokb&resumptionToken=||6&from=2014-12-01T20:40:01Z
        String url = COMMON_URL + TITLE_URL + LIST_IDENTIFIERS_URL + endIndex + FROM_URL + lastUpdatedTime;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting title response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns the organization response based on the last update time from the url.
     * @param lastUpdatedTime
     * @param endIndex
     * @return
     */
    public static String getOrgsResponseXmlFromGokb(String lastUpdatedTime, int endIndex) {
        // URL : https://gokb.k-int.com/gokb/oai/orgs?verb=ListIdentifiers&metadataPrefix=gokb&resumptionToken=||6&from=2014-12-01T20:40:01Z
        String url = COMMON_URL + ORGS_URL + LIST_IDENTIFIERS_URL + endIndex + FROM_URL + lastUpdatedTime;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting orgs response from url", e);
        }
        return responseXml;
    }


    /**
     * This method returns the package response based on the identifier from the url.
     * @param identifier
     * @return
     */
    public static String getPackageResponseXmlFromGokb(String identifier) {
        // URL : https://gokb.k-int.com/gokb/oai/packages?verb=getRecord&metadataPrefix=gokb&identifier=org.gokb.cred.Package:37779
        String url = COMMON_URL + PACKAGE_URL + GET_IDENTIFIER_RECORDS_URL + identifier;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting package identifier response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns the platform response based on the identifier from the url.
     * @param identifier
     * @return
     */
    public static String getPlatformResponseXmlFromGokb(String identifier) {
        // URL : https://gokb.k-int.com/gokb/oai/platforms?verb=getRecord&metadataPrefix=gokb&identifier=org.gokb.cred.Platform:37779
        String url = COMMON_URL + PLATFORM_URL + GET_IDENTIFIER_RECORDS_URL + identifier;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting platform identifier response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns the title response based on the identifier from the url.
     * @param identifier
     * @return
     */
    public static String getTitleResponseXmlFromGokb(String identifier) {
        // URL : https://gokb.k-int.com/gokb/oai/titles?verb=getRecord&metadataPrefix=gokb&identifier=org.gokb.cred.Title:37779
        String url = COMMON_URL + TITLE_URL + GET_IDENTIFIER_RECORDS_URL + identifier;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting title identifier response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns the organization response based on the identifier from the url.
     * @param identifier
     * @return
     */
    public static String getOrgsResponseXmlFromGokb(String identifier) {
        // URL : https://gokb.k-int.com/gokb/oai/orgs?verb=getRecord&metadataPrefix=gokb&identifier=org.gokb.cred.org:37779
        String url = COMMON_URL + ORGS_URL + GET_IDENTIFIER_RECORDS_URL + identifier;
        String responseXml = null;
        try {
            responseXml = GOKBConnectionReader.getInstance().doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            LOG.error("Exception while getting orgs identifier response from url", e);
        }
        return responseXml;
    }

    /**
     * This method returns ElementValue using docContent and xpathExpression..
     * @param docContent
     * @param xpathExpression
     * @return value
     */
    public static NodeList getElementNodeList(String docContent, String xpathExpression) {
        try {
            Document document = XmlHelper.trimXml(new ByteArrayInputStream(docContent.getBytes()));
            NodeList nodeList = (NodeList) xPath.compile(xpathExpression).evaluate(document, XPathConstants.NODESET);
            return nodeList;

        } catch (Exception e) {
            throw new RiceRuntimeException();
        }
    }


    /**
     * This method returns ElementValue using docContent and xpathExpression..
     * @param docContent
     * @param xpathExpression
     * @return value
     */
    private static String getElementValue(String docContent, String xpathExpression) {
        try {
            Document document = XmlHelper.trimXml(new ByteArrayInputStream(docContent.getBytes()));
            String value = (String) xPath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return value;
        } catch (Exception e) {
            throw new RiceRuntimeException();
        }
    }

    /**
     * This method returns the page size from the response.
     * @param responseXml
     * @return
     */
    public static int getPageSizeFromResponse(String responseXml) {
        NodeList resumptionTokenNode = getElementNodeList(responseXml, OLEConstants.OleGokb.RESUMPTION_TOKEN_EXP);
        if (null != resumptionTokenNode && null != resumptionTokenNode.item(0)) {
            String resumptionTokenValue = StringUtils.substringAfter(resumptionTokenNode.item(0).getTextContent(), PIPE + PIPE);
            if (resumptionTokenValue.contains(PIPE + GOKB)) {
                return Integer.parseInt(StringUtils.substringBefore(resumptionTokenValue, PIPE + GOKB));
            } else {
                return Integer.parseInt(resumptionTokenValue);
            }
        }
        return 0;
    }

    /**
     * This method returns the complete list size from the response.
     * @param results
     * @return
     */
    public static int getCompleteListSize(String results) {
        String completeListSize = getElementValue(results, COMPLETE_LIST_SIZE);
        if (StringUtils.isNotBlank(completeListSize)) {
            return Integer.parseInt(completeListSize);
        } else {
            return 0;
        }
    }

    /**
     * This method returns the update dates from the response.
     * @param results
     * @return
     */
    public static List<String> getUpdatedDates(String results) {
        List<String> updatedDates = new ArrayList<>();
        NodeList records = OleGokbXmlUtil.getElementNodeList(results, OLEConstants.OleGokb.RECORD_XPATH_EXP);
        for (int i = 0; i < records.getLength(); i++) {
            NodeList nodeList = records.item(i).getChildNodes();
            for (int j = 0; j < nodeList.getLength(); j++) {
                if (nodeList.item(j).getNodeName().equalsIgnoreCase(OLEConstants.OleGokb.HEADER)) {
                    NodeList headerChildNodes = nodeList.item(j).getChildNodes();
                    for (int k = 0; k < headerChildNodes.getLength(); k++) {
                        if (headerChildNodes.item(k).getNodeName().equalsIgnoreCase(OLEConstants.OleGokb.DATE_STAMP)) {
                            updatedDates.add(headerChildNodes.item(k).getTextContent());
                        }
                    }
                }
            }
        }
        return updatedDates;
    }

    /**
     * This method returns the UTC date format.
     * @return
     */
    private static DateFormat getUTCDateFormat() {
        DateFormat df = new SimpleDateFormat(OLEConstants.OleGokb.UTC_DATE_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone(OLEConstants.OleGokb.UTC_TIME_ZONE));
        return df;
    }

    /**
     * This method returns the timestamp from string.
     * @param date
     * @return
     */
    public static Timestamp getTimeStampFromString(String date) {
        Timestamp timestamp = null;
        try {
            if (StringUtils.isNotBlank(date)) {
                timestamp = new Timestamp(getUTCDateFormat().parse(date).getTime());
            }
        } catch (Exception e) {
            LOG.error("Exception while parsing date" + e);
        }
        return timestamp;
    }

    /**
     * This method returns string from timestamp.
     * @param timestamp
     * @return
     */
    public static String getStringFromTimeStamp(Timestamp timestamp) {
        String utcTime = null;
        try {
            if (timestamp != null) {
                Date date = new Date(timestamp.getTime());
                utcTime = getUTCDateFormat().format(date);
            }
        } catch (Exception e) {
            LOG.error("Exception while formatting date" + e);
        }
        return utcTime;
    }

    /**
     * This method returns identifier from the header.
     * @param headerNode
     * @return
     */
    public static String getIdentifierFromHeader(Node headerNode) {
        return headerNode.getChildNodes().item(0).getTextContent();
    }

}
