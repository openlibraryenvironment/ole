package org.kuali.ole;

import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/24/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUConstants {

    //SRU request parameter names

    public static final String OperationType = "operation";
    public static final String VERSION = "version";
    public static final String QUERY = "query";
    public static final String START_RECORD = "startRecord";
    public static final String MAXIMUM_RECORDS = "maximumRecords";
    public static final String RECORD_PACKING = "recordPacking";
    public static final String STYLE_SHEET = "stylesheet";
    public static final String RECORD_SCHEMA = "recordSchema";
    public static final String SORTKEYS = "sortKeys";
    public static final String RECORD_XPATH = "recordXPath";
    public static final String RESULTSET_TTL = "resultSetTTL";
    public static final String SRU_VERSION="1.2";
    public static final String SRU_STAFF_ONLY_FLAG = "false";

    //SRU request parameters constants

    public static final String SEARCH_RETRIEVE = "searchRetrieve";
    public static final String EXPLAIN = "explain";
    public static final String MAXRECORD = "maximum.record.size";
    public static final String STARTRECORD = "minimum.record.size";
    public static final String RECORD_PACK_XML = "xml";
    public static final String RECORD_PACK_STRING = "string";
    public static final String DC_RECORD_SCHEMA = "dc";
    public static final String DUBLIN_RECORD_SCHEMA = "dublin";
    public static final String TITLE_SORT_KEYS = "title";
    public static final String MARC_RECORD_SCHEMA = "marcxml";

    //Extra Request Data Constants
    public static final String EXTRA_REQ_DATA_KEY = "extraReqDataKey";
    public static final String EXTRA_REQ_DATA_VALUE = "extraReqDataValue";
    public static final String EXTRA_REQ_DATA_XML_NAMESPACE = "extra.req.data.xml.namespace";
    //Diagnostics message constants

    public static final String SERVER_DIAGNOSTIC_MSG = "server.diagnostic.message";
    public static final String NORECORDS_DIAGNOSTIC_MSG = "norecords.diagnostic.message";
    public static final String INVALID_QUERY_DIAGNOSTIC_MSG = "invalid.query.diagnostic.message";
    public static final String OPERATION_REQUIRED_FIELD_DIAGNOSTIC_MSG = "operation.required.field.diagnostic.message";
    public static final String VERSION_REQUIRED_FIELD_DIAGNOSTIC_MSG = "version.required.field.diagnostic.message";
    public static final String QUERY_REQUIRED_FIELD_DIAGNOSTIC_MSG = "query.required.field.diagnostic.message";
    public static final String INVALID_OperationType = "invalid.operation.type";
    public static final String INVALID_RECORD_PACKING = "invalid.record.packing";
    public static final String INVALID_RECORD_SCHEMA = "invalid.record.schema";
    public static final String START_RECORD_UNMATCH = "start.record.unMatch";
    public static final String SEARCH_PROCESS_FAILED= "search.process.failed";


    //Explain operation

    public static final String EXPLAIN_RECORD_SCHEMA = "explain.record.schema";
    public static final String EXPLAIN_SERVER_DATABASE_INFO = "explain.server.database.info";
    public static final String EXPLAIN_SERVER_HOST = "explain.server.host";
    public static final String EXPLAIN_SERVER_METHOD = "explain.server.method";
    public static final String EXPLAIN_SERVER_PORT = "explain.server.port";
    public static final String EXPLAIN_SERVER_PROTOCOL = "explain.server.protocol";
    public static final String EXPLAIN_SERVER_TRANSPORT = "explain.server.transport";
    public static final String EXPLAIN_SERVER_VERSION = "explain.server.version";
    public static final String EXPLAIN_DATABASE_LANG = "explain.database.lang";
    public static final String EXPLAIN_DATABASE_PRIMARY = "explain.database.primary";
    public static final String EXPLAIN_INDEX_SET_NAME = "explain.index.set.name";
    public static final String EXPLAIN_INDEX_SET_IDENTIFIER = "explain.index.set.identifier";
    public static final String EXPLAIN_INDEX_MAP_NAME_SET = "explain.index.map.name.set";
    public static final String EXPLAIN_INDEX_MAP_NAME_VALUE = "explain.index.map.name.value";
    public static final String EXPLAIN_SCHEMA_TITLE = "explain.schema.title";
    public static final String EXPLAIN_SCHEMA_NAME = "explain.schema.name";
    public static final String EXPLAIN_SCHEMA_IDENTIFIER = "explain.schema.identifier";


    public static final String INVALID_PARAM = "invalidParam";

    public static final String MARC = "marc";

    public static final String OPAC_RECORD = "OPAC";

    public static final String UN_SUPPORTED_PARAM = "unSupported.parameter";
    public static final String UN_SUPPORTED_PARAM_VALUE = "unSupported.parameter.value";
    public static final String MANDATORY="mandatory.parameter.missing";
    public static final String UNSUPPORT_VERSION="unSupport.version";
    public static final String ITEM_STATUS_AVAILABLE = "AVAILABLE";
    public static final String ITEM_STATUS_ONHOLD = "ONHOLD";

    public static final String MARC_SCHEMA="marcxml";
    public static final String DC_SCHEMA="dc";
    public static final String MARC_RECORD_RESPONSE_SCHEMA = "info:srw/schema/1/marcxml-v1.1";
    public static final String DC_RECORD_RESPONSE_SCHEMA="info:srw/schema/1/dc-v1.1";
    public static final String OPAC_RECORD_RESPONSE_SCHEMA="info:srw/schema/1/opacxml-v1.0";
    public static final String NUMBER_OF_REORDS="numberOfRecords";
    public static final String BIBLIOGRAPHIC ="bibliographic";
     public static final String TITLE_SEARCH ="Title_search";
    public static final String AUTHOR_SEARCH ="Author_search";
    public static final String PUBLISHER_SEARCH ="Publisher_search";
    public static final String PUBLICATION_DATE_SEARCH ="PublicationDate_search";
    public static final String ISBN_SEARCH ="ISBN_search";
    public static final String ISSN_SEARCH ="ISSN_search";
    public static final String LOCALID_SEARCH ="LocalId_search";
    public static final String SUBJECT_SEARCH ="Subject_search";
    public static final String TITLE="title";
    public static final String AUTHOR="author";
    public static final String PUBLISHER="publisher";
    public static final String PUBLICATION_DATE="publicationDate";
    public static final String PUB_DATE="date";
    public static final String ISBN="isbn";
    public static final String ISSN="issn";
    public static final String LOCALID="id";
    public static final String SUBJECT ="subject";
    public static final String CHOICE = "keyword";
    public static final String OCLC = "oclc";
    public static final String CS_OCLC = "dc.oclc";
    public static final String OCLC_SEARCH = "mdf_035a";
    public static final String CS_TITLE="dc.title";
    public static final String CS_AUTHOR="dc.creator";
    public static final String CS_PUBLISHER="dc.publisher";
    public static final String CS_PUBLICATION_DATE="dc.date";
    public static final String CS_PUB_DATE="bib.dateIssued";
    public static final String CS_ISBN="bath.isbn";
    public static final String CS_ISSN="bath.issn";
    public static final String CS_LOCALID="rec.id";
    public static final String CS_SUBJECT ="dc.subject";
    public static final String CS_CHOICE = "cql.serverChoice";
    public static final String ALL_TEXT="all_text";
    public static final String LOCAL_LOCATION= getParameter("SRU_LOCAL_LOCATION_LEVEL");
    public static final String SHELVING_LOCATION= getParameter("SRU_SHELVING_LOCATION_LEVEL");
    public static final String BOOLEAN_FIELD_TRUE_FORMAT = getParameter("BOOLEAN_FIELD_TRUE_FORMAT");
    public static final String BOOLEAN_FIELD_FALSE_FORMAT = getParameter("BOOLEAN_FIELD_FALSE_FORMAT");



    public static final String SRU_AVAILABLE_STATUSES = getParameter("SRU_AVAILABLE_STATUSES");
    public static final String SRU_ON_HOLD_STATUSES = getParameter("SRU_ON_HOLD_STATUSES");

    public static String getParameter(String name) {
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put("namespaceCode", "OLE-DESC");
            criteriaMap.put("componentCode", "Describe");
            criteriaMap.put("name", name);
            List<ParameterBo> parametersList = (List<ParameterBo>) KRADServiceLocator.getBusinessObjectService().findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
        }
        return parameter;
    }
}
