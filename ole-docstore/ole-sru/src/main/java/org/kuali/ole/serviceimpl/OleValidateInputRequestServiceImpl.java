package org.kuali.ole.serviceimpl;

import org.kuali.ole.OleSRUConstants;
import org.kuali.ole.service.OleValidateInputRequestService;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleValidateInputRequestServiceImpl implements OleValidateInputRequestService {

    /**
     * to validate whether the required parameters of corresponding operation is present or not.
     *
     * @param reqParamMap
     * @return boolean
     */
    public String inputRequestValidation(Map reqParamMap) {

        String operationType = (String) reqParamMap.get(OleSRUConstants.OperationType);
        String version = (String) reqParamMap.get(OleSRUConstants.VERSION);
        String query = (String) reqParamMap.get(OleSRUConstants.QUERY);
        String recordPacking = (String) reqParamMap.get(OleSRUConstants.RECORD_PACKING);
        String recordSchema = (String) reqParamMap.get(OleSRUConstants.RECORD_SCHEMA);

        if (operationType == null || "".equals(operationType))
            return OleSRUConstants.OPERATION_REQUIRED_FIELD_DIAGNOSTIC_MSG;

        if (OleSRUConstants.SEARCH_RETRIEVE.equalsIgnoreCase(operationType)) {
            if (version == null || "".equals(version))
                return OleSRUConstants.VERSION_REQUIRED_FIELD_DIAGNOSTIC_MSG;
            else if (query == null || "".equals(query))
                return OleSRUConstants.QUERY_REQUIRED_FIELD_DIAGNOSTIC_MSG;

        } else if (OleSRUConstants.EXPLAIN.equalsIgnoreCase(operationType)) {
            if (version == null || "".equals(version))
                return OleSRUConstants.VERSION_REQUIRED_FIELD_DIAGNOSTIC_MSG;

        } else
            return OleSRUConstants.INVALID_OperationType;

        if (!(OleSRUConstants.RECORD_PACK_XML.equalsIgnoreCase(recordPacking) || OleSRUConstants.RECORD_PACK_STRING.equalsIgnoreCase(recordPacking)))
            return OleSRUConstants.INVALID_RECORD_PACKING;

        if (recordSchema != null && OleSRUConstants.SEARCH_RETRIEVE.equalsIgnoreCase(operationType)) {
            if (!(OleSRUConstants.DC_RECORD_SCHEMA.equalsIgnoreCase(recordSchema)) && !(OleSRUConstants.MARC_RECORD_SCHEMA.equalsIgnoreCase(recordSchema)) && !(OleSRUConstants.OPAC_RECORD.equalsIgnoreCase(recordSchema))) {
                return OleSRUConstants.INVALID_RECORD_SCHEMA;
            }
        }
        if (recordSchema.equals(OleSRUConstants.MARC_RECORD_SCHEMA)) {
            reqParamMap.put(OleSRUConstants.RECORD_SCHEMA, OleSRUConstants.MARC);
        }
        if (recordSchema.equals(OleSRUConstants.DC_RECORD_SCHEMA)) {
            reqParamMap.put(OleSRUConstants.RECORD_SCHEMA, OleSRUConstants.DUBLIN_RECORD_SCHEMA);
        }
        return null;
    }
}