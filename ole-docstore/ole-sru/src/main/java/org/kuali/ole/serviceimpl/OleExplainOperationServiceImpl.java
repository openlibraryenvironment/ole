package org.kuali.ole.serviceimpl;

import org.kuali.ole.OleSRUConstants;
import org.kuali.ole.bo.explain.*;
import org.kuali.ole.handler.OleSRUExplainOperationHandler;
import org.kuali.ole.service.OleDiagnosticsService;
import org.kuali.ole.service.OleExplainOperationService;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleExplainOperationServiceImpl implements OleExplainOperationService {


    public OleSRUExplainOperationHandler oleSRUExplainOperationHandler;
    public OleDiagnosticsService oleDiagnosticsService;
    private Config currentContextConfig;

    public OleExplainOperationServiceImpl() {
        oleSRUExplainOperationHandler = new OleSRUExplainOperationHandler();
        oleDiagnosticsService = new OleDiagnosticsServiceImpl();
    }

    /**
     * will generate the explain service response xml
     *
     * @param reqParamMap
     * @return explain xml response as a string
     */
    public String getExplainResponse(Map reqParamMap) {
        OleSRUExplainResponse oleSRUExplainResponse = new OleSRUExplainResponse();
        oleSRUExplainResponse.setVersion((String) reqParamMap.get(OleSRUConstants.VERSION));
        OleSRUExplainRecord oleSRUExplainRecord = getExplainRecord(reqParamMap);
        oleSRUExplainResponse.setRecord(oleSRUExplainRecord);
        if (OleSRUConstants.RECORD_PACK_XML.equalsIgnoreCase((String) reqParamMap.get(OleSRUConstants.RECORD_PACKING))) {
            String explainXML = oleSRUExplainOperationHandler.toXML(oleSRUExplainResponse);
            String styleSheet = (String) reqParamMap.get(OleSRUConstants.STYLE_SHEET);
            if (styleSheet != null) {
                explainXML = explainXML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n <?xml-stylesheet type=\"text/xsl\" href=\"" + styleSheet + "\"?>");
            }
            return explainXML;
        } else if (OleSRUConstants.RECORD_PACK_STRING.equalsIgnoreCase((String) reqParamMap.get(OleSRUConstants.RECORD_PACKING))) {
            return oleSRUExplainResponse.toString();
        }
        return null;
    }

    /**
     * will fetch the values from the property file
     *
     * @return OleSRUExplainRecord object
     */
    public OleSRUExplainRecord getExplainRecord(Map reqParamMap) {
        OleSRUExplainRecord oleSRUExplainRecord = new OleSRUExplainRecord();
        oleSRUExplainRecord.setRecordPacking(OleSRUConstants.RECORD_PACK_XML);
        oleSRUExplainRecord.setRecordSchema(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_RECORD_SCHEMA));
        OleSRUExplainRecordData oleSRUExplainRecordData = getExplainRecordData(reqParamMap);
        oleSRUExplainRecord.setRecordData(oleSRUExplainRecordData);
        return oleSRUExplainRecord;
    }

    private Config getCurrentContextConfig() {
        if (null == currentContextConfig) {
            currentContextConfig = ConfigContext.getCurrentContextConfig();
        }
        return currentContextConfig;
    }

    public void setCurrentContextConfig(Config currentContextConfig) {
        this.currentContextConfig = currentContextConfig;
    }

    /**
     * will fetch the values from the property file
     *
     * @return OleSRUExplainRecordData object
     */
    public OleSRUExplainRecordData getExplainRecordData(Map reqParamMap) {
        OleSRUExplainRecordData oleSRUExplainRecordData = new OleSRUExplainRecordData();
        OleSRUExplainOperation oleSRUExplainOperation = new OleSRUExplainOperation();
        OleSRUExplainServerInfo oleSRUExplainServerInfo = getExplainServerInfo();
        OleSRUExplainDatabaseInfo oleSRUExplainDatabaseInfo = getExplainDatabaseInfo();
        OleSRUExplainConfigurationInfo oleSRUExplainConfigurationInfo = getExplainConfigInfo();
        OleSRUExplainIndexInfo oleSRUExplainIndexInfo = getExplainIndexInfo();
        OleSRUExplainSchemaInfo oleSRUExplainSchemaInfo = getExplainSchemaInfo();
        oleSRUExplainOperation.setServerInfo(oleSRUExplainServerInfo);
        oleSRUExplainOperation.setDatabaseInfo(oleSRUExplainDatabaseInfo);
        oleSRUExplainOperation.setIndexInfo(oleSRUExplainIndexInfo);
        oleSRUExplainOperation.setSchemaInfo(oleSRUExplainSchemaInfo);
        oleSRUExplainOperation.setConfigInfo(oleSRUExplainConfigurationInfo);
        if (reqParamMap.containsKey(OleSRUConstants.EXTRA_REQ_DATA_KEY))
            oleSRUExplainOperation.setExtraRequestData(getExtraReqDataInfo(reqParamMap));
        oleSRUExplainRecordData.setExplain(oleSRUExplainOperation);
        return oleSRUExplainRecordData;
    }

    /**
     * will fetch the values from the property file
     *
     * @return oleSRUExplainServerInfo object
     */
    public OleSRUExplainServerInfo getExplainServerInfo() {
        OleSRUExplainServerInfo oleSRUExplainServerInfo = new OleSRUExplainServerInfo();
        oleSRUExplainServerInfo.setDatabase(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_SERVER_DATABASE_INFO));
        oleSRUExplainServerInfo.setHost(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_SERVER_HOST));
        oleSRUExplainServerInfo.setMethod(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_SERVER_METHOD));
        oleSRUExplainServerInfo.setPort(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_SERVER_PORT));
        oleSRUExplainServerInfo.setProtocol(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_SERVER_PROTOCOL));
        oleSRUExplainServerInfo.setTransport(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_SERVER_TRANSPORT));
        oleSRUExplainServerInfo.setVersion(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_SERVER_VERSION));
        oleSRUExplainServerInfo.setValue("");
        return oleSRUExplainServerInfo;
    }

    /**
     * will fetch the values from the property file
     *
     * @return oleSRUExplainDatabaseInfo object
     */
    public OleSRUExplainDatabaseInfo getExplainDatabaseInfo() {
        OleSRUExplainDatabaseInfo oleSRUExplainDatabaseInfo = new OleSRUExplainDatabaseInfo();
        OleSRUExplainDatabaseTitle oleSRUExplainDatabaseTitle = new OleSRUExplainDatabaseTitle();
        oleSRUExplainDatabaseTitle.setLang(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_DATABASE_LANG));
        oleSRUExplainDatabaseTitle.setPrimary(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_DATABASE_PRIMARY));
        oleSRUExplainDatabaseTitle.setValue("");
        oleSRUExplainDatabaseInfo.setTitle(oleSRUExplainDatabaseTitle);
        return oleSRUExplainDatabaseInfo;
    }

    /**
     * will fetch the values from the property file
     *
     * @return oleSRUExplainIndexInfo object
     */
    public OleSRUExplainIndexInfo getExplainIndexInfo() {
        OleSRUExplainIndexInfo oleSRUExplainIndexInfo = new OleSRUExplainIndexInfo();
        OleSRUExplainIndex oleSRUExplainIndex = new OleSRUExplainIndex();
        OleSRUExplainIndexSet oleSRUExplainIndexSet = new OleSRUExplainIndexSet();
        oleSRUExplainIndexSet.setName(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_INDEX_SET_NAME));
        oleSRUExplainIndexSet.setIdentifier(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_INDEX_SET_IDENTIFIER));
        OleSRUExplainIndexMapName oleSRUExplainIndexMapName = new OleSRUExplainIndexMapName();
        oleSRUExplainIndexMapName.setSet(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_INDEX_MAP_NAME_SET));
        oleSRUExplainIndexMapName.setValue(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_INDEX_MAP_NAME_VALUE));
        OleSRUExplainIndexMap oleSRUExplainIndexMap = new OleSRUExplainIndexMap();
        oleSRUExplainIndexMap.setName(oleSRUExplainIndexMapName);
        oleSRUExplainIndex.setIndexMap(oleSRUExplainIndexMap);
        oleSRUExplainIndexInfo.setIndex(oleSRUExplainIndex);
        oleSRUExplainIndexInfo.setSet(oleSRUExplainIndexSet);
/*        oleSRUExplainIndexSet.setName("bath");
        oleSRUExplainIndexSet.setIdentifier("info:srw/cql-context-set/1/dc-v1.1");    */
        oleSRUExplainIndexSet.setValue("");
        return oleSRUExplainIndexInfo;
    }

    /**
     * will fetch the values from the property file
     *
     * @return oleSRUExplainSchemaInfo object
     */
    public OleSRUExplainSchemaInfo getExplainSchemaInfo() {
        OleSRUExplainSchemaInfo oleSRUExplainSchemaInfo = new OleSRUExplainSchemaInfo();
        OleSRUExplainSchema oleSRUExplainSchema = new OleSRUExplainSchema();
        oleSRUExplainSchema.setTitle(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_SCHEMA_TITLE));
        oleSRUExplainSchema.setName(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_SCHEMA_NAME));
        oleSRUExplainSchema.setIdentifier(getCurrentContextConfig().getProperty(OleSRUConstants.EXPLAIN_SCHEMA_IDENTIFIER));
        oleSRUExplainSchema.setValue("");
        oleSRUExplainSchemaInfo.setSchema(oleSRUExplainSchema);
        return oleSRUExplainSchemaInfo;
    }

    /**
     * will fetch the values from the property file
     *
     * @return OleSRUExplainConfigurationInfo object
     */
    public OleSRUExplainConfigurationInfo getExplainConfigInfo() {
        OleSRUExplainConfigurationInfo oleSRUExplainConfigurationInfo = new OleSRUExplainConfigurationInfo();
        OleSRUExplainConfigDefaultTagField oleSRUExplainConfigDefaultTagField = new OleSRUExplainConfigDefaultTagField();
        OleSRUExplainConfigSettingTagField oleSRUExplainConfigSettingTagField = new OleSRUExplainConfigSettingTagField();
        OleSRUExplainConfigSupportTagField oleSRUExplainConfigSupportTagField = new OleSRUExplainConfigSupportTagField();
        oleSRUExplainConfigDefaultTagField.setType("numberOfRecords");
        oleSRUExplainConfigDefaultTagField.setValue(Integer.parseInt(getCurrentContextConfig().getProperty(OleSRUConstants.STARTRECORD)));
        oleSRUExplainConfigurationInfo.setDefaultValue(oleSRUExplainConfigDefaultTagField);
        oleSRUExplainConfigSettingTagField.setType("maximumRecords");
        oleSRUExplainConfigSettingTagField.setValue(Integer.parseInt(getCurrentContextConfig().getProperty(OleSRUConstants.MAXRECORD)));
        oleSRUExplainConfigurationInfo.setSetting(oleSRUExplainConfigSettingTagField);
        oleSRUExplainConfigSupportTagField.setType("proximity");
        oleSRUExplainConfigSupportTagField.setValue("");
        oleSRUExplainConfigurationInfo.setSupports(oleSRUExplainConfigSupportTagField);
        return oleSRUExplainConfigurationInfo;
    }

    public String getExtraReqDataInfo(Map reqParamMap) {

        return "<theo:" + reqParamMap.get(OleSRUConstants.EXTRA_REQ_DATA_KEY) + " xmlns:theo=\"" + getCurrentContextConfig().getProperty(OleSRUConstants.EXTRA_REQ_DATA_XML_NAMESPACE) + "\">\n" +
                reqParamMap.get(OleSRUConstants.EXTRA_REQ_DATA_VALUE) + "\n" +
                "</theo:" + reqParamMap.get(OleSRUConstants.EXTRA_REQ_DATA_KEY) + ">";
    }

}
