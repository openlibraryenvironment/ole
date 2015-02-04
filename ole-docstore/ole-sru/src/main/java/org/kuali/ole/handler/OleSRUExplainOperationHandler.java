package org.kuali.ole.handler;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.bo.explain.*;
import org.kuali.ole.converters.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainOperationHandler {

    /**
     * this method converts OleSRUExplainResponse object to an XML with required name space
     *
     * @param oleSRUExplainResponse object
     * @return xml as a string
     */
    public String toXML(OleSRUExplainResponse oleSRUExplainResponse) {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stringBuffer.append("\n");
        XStream xStream = new XStream();
        xStream.alias("sru:explainResponse", OleSRUExplainResponse.class);
        xStream.aliasField("sru:version", OleSRUExplainResponse.class, "version");
        xStream.aliasField("sru:record", OleSRUExplainResponse.class, "record");
        xStream.aliasField("sru:recordPacking", OleSRUExplainRecord.class, "recordPacking");
        xStream.aliasField("sru:recordSchema", OleSRUExplainRecord.class, "recordSchema");
        xStream.aliasField("sru:recordData", OleSRUExplainRecord.class, "recordData");
        xStream.aliasField("zr:explain", OleSRUExplainRecordData.class, "explain");
        xStream.aliasField("zr:serverInfo", OleSRUExplainOperation.class, "serverInfo");
        xStream.aliasField("zr:databaseInfo", OleSRUExplainOperation.class, "databaseInfo");
        xStream.aliasField("zr:indexInfo", OleSRUExplainOperation.class, "indexInfo");
        xStream.aliasField("zr:schemaInfo", OleSRUExplainOperation.class, "schemaInfo");
        xStream.aliasField("zr:configInfo", OleSRUExplainOperation.class, "configInfo");
        xStream.aliasField("zr:host", OleSRUExplainServerInfo.class, "host");
        xStream.aliasField("zr:port", OleSRUExplainServerInfo.class, "port");
        xStream.aliasField("zr:database", OleSRUExplainServerInfo.class, "database");
        //xStream.aliasField("zr:title", OleSRUExplainDatabaseInfo.class, "title");
        xStream.aliasField("zr:index", OleSRUExplainIndexInfo.class, "index");
        xStream.aliasField("zr:set", OleSRUExplainIndexInfo.class, "set");
        xStream.aliasField("zr:map", OleSRUExplainIndex.class, "indexMap");
        xStream.aliasField("zr:name", OleSRUExplainIndexMap.class, "name");
        xStream.aliasField("zr:schema", OleSRUExplainSchemaInfo.class, "schema");
        xStream.aliasField("zr:title", OleSRUExplainSchema.class, "title");
        xStream.aliasField("zr:default", OleSRUExplainConfigurationInfo.class, "defaultValue");
        xStream.aliasField("zr:setting", OleSRUExplainConfigurationInfo.class, "setting");
        xStream.aliasField("zr:supports", OleSRUExplainConfigurationInfo.class, "supports");
        xStream.registerConverter(new OleSRUExplainServerInfoConverter());
        xStream.registerConverter(new OleSRUExplainDatabaseInfoConverter());
        xStream.registerConverter(new OleSRUExplainIndexInfoConverter());
        xStream.registerConverter(new OleSRUExplainSchemaInfoConverter());
        xStream.registerConverter(new OleSRUExplainIndexMapNameConverter());
        xStream.registerConverter(new OleSRUExplainConfigInfoSupportConverter());
        xStream.registerConverter(new OleSRUExplainConfigInfoSettingConverter());
        xStream.registerConverter(new OleSRUExplainConfigInfoDefaultConverter());
        String xml = xStream.toXML(oleSRUExplainResponse);
        xml = xml.replaceAll("<sru:explainResponse>", "<sru:explainResponse xmlns:sru=\"http://www.loc.gov/zing/srw/\">");
        xml = xml.replaceAll("<zr:explain>", " <zr:explain xmlns:zr=\"http://explain.z3950.org/dtd/2.1/\">");
        stringBuffer.append(xml);
        return stringBuffer.toString();
    }


}
