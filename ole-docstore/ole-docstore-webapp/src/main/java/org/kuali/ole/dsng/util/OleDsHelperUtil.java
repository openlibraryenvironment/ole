package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.dsng.indexer.BibIndexer;
import org.kuali.ole.dsng.indexer.HoldingIndexer;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.marc4j.marc.Record;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SheikS on 11/30/2015.
 */
public class OleDsHelperUtil implements DocstoreConstants {

    private ObjectMapper objectMapper;

    private BibIndexer bibIndexer;

    private HoldingIndexer holdingIndexer;

    private BusinessObjectService businessObjectService;

    public String buildSortableCallNumber(String callNumber, String codeValue) {
        String shelvingOrder = "";
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.solrmarc.callnum.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                shelvingOrder = callNumberObj.getShelfKey();
            }
        }
        return shelvingOrder;
    }

    public String getNormalizedEnumeration(String enumation) {
        if (enumation.contains(".")) {
            StringBuffer resultBuf = new StringBuffer();
            String[] splitEnum = enumation.split("\\.");
            if (splitEnum.length > 1) {
                String enumerationNo = splitEnum[1];
                String enumBufAfterDot = null;
                String enumBufAfterSpecial = null;
                String normalizedEnum = null;
                if (enumerationNo != null && (enumerationNo.trim().length() > 0)) {
                    int pos = 0;
                    boolean numCheck = false;
                    for (int i = 0; i < enumerationNo.length(); i++) {
                        char c = enumerationNo.charAt(i);
                        String convertedEnum = String.valueOf(c);
                        if (convertedEnum.matches("[0-9]")) {
                            if (Character.isDigit(c)) {
                                pos = i;
                                numCheck = true;
                            } else {
                                break;
                            }
                        } else {
                            if (pos == 0 && numCheck == false) {
                                return enumation;
                            }
                            break;
                        }
                    }
                    enumBufAfterDot = enumerationNo.substring(0, pos + 1);
                    normalizedEnum = normalizeFloatForEnumeration(enumBufAfterDot, 5);
                    enumBufAfterSpecial = enumerationNo.substring(pos + 1);
                    splitEnum[1] = normalizedEnum + enumBufAfterSpecial;
                }
                for (int j = 0; j < splitEnum.length; j++) {
                    resultBuf.append(splitEnum[j]);
                    resultBuf.append(".");
                }

                return resultBuf.substring(0, resultBuf.length() - 1).toString();
            } else {
                return enumation;
            }
        } else {
            return enumation;
        }
    }

    public String normalizeFloatForEnumeration(String floatStr, int digitsB4) {
        String replacString = floatStr.replaceAll("[^a-zA-Z0-9]+", "");
        double value = Double.valueOf(replacString).doubleValue();
        String formatStr = getFormatString(digitsB4);
        DecimalFormat normFormat = new DecimalFormat(formatStr);
        String norm = normFormat.format(value);
        if (norm.endsWith("."))
            norm = norm.substring(0, norm.length() - 1);
        return norm;
    }

    private String getFormatString(int numDigits) {
        StringBuilder b4 = new StringBuilder();
        if (numDigits < 0)
            b4.append("############");
        else if (numDigits > 0) {
            for (int i = 0; i < numDigits; i++) {
                b4.append('0');
            }
        }
        return b4.toString();
    }


    public void appendData(StringBuffer stringBuffer, String data) {
        if(StringUtils.isNotEmpty(data)) {
            stringBuffer.append(data);
            stringBuffer.append(" ");
        }
    }

    public void buildLocationLevels(String locationName, String locationLevel, SolrInputDocument solrInputDocument, StringBuffer loactionLevelStr) {
        if (StringUtils.isNotBlank(locationLevel)) {
            if (LOCATION_LEVEL_INSTITUTION.equalsIgnoreCase(locationLevel)) {
                solrInputDocument.addField(LEVEL1LOCATION_DISPLAY, locationName);
                solrInputDocument.addField(LEVEL1LOCATION_SEARCH, locationName);
                appendData(loactionLevelStr,locationName.replace("-",""));
            } else if (LOCATION_LEVEL_CAMPUS.equalsIgnoreCase(locationLevel)) {
                solrInputDocument.addField(LEVEL2LOCATION_DISPLAY, locationName);
                solrInputDocument.addField(LEVEL2LOCATION_SEARCH, locationName);
                appendData(loactionLevelStr,locationName.replace("-",""));
            } else if (LOCATION_LEVEL_LIBRARY.equalsIgnoreCase(locationLevel)) {
                solrInputDocument.addField(LEVEL3LOCATION_DISPLAY, locationName);
                solrInputDocument.addField(LEVEL3LOCATION_SEARCH, locationName);
                appendData(loactionLevelStr,locationName.replace("-",""));
            } else if (LOCATION_LEVEL_COLLECTION.equalsIgnoreCase(locationLevel)) {
                solrInputDocument.addField(LEVEL4LOCATION_DISPLAY, locationName);
                solrInputDocument.addField(LEVEL4LOCATION_SEARCH, locationName);
                appendData(loactionLevelStr,locationName.replace("-",""));
            } else if (LOCATION_LEVEL_SHELVING.equalsIgnoreCase(locationLevel) || LOCATION_LEVEL_SHELVING_1.equalsIgnoreCase(locationLevel)) {
                solrInputDocument.addField(LEVEL5LOCATION_DISPLAY, locationName);
                solrInputDocument.addField(LEVEL5LOCATION_SEARCH, locationName);
                appendData(loactionLevelStr,locationName.replace("-",""));
            }
        }
    }

    public List<Record> processMarcContent(String rawMarcContent) {
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        return  marcXMLConverter.convertRawMarchToMarc(rawMarcContent);
    }

    public CallNumberTypeRecord fetchCallNumberTypeRecordByName(String callNumberTypeName) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", callNumberTypeName);
        List<CallNumberTypeRecord> matching = (List<CallNumberTypeRecord>) getBusinessObjectService().findMatching(CallNumberTypeRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }


    public ObjectMapper getObjectMapper() {
        if(null == objectMapper) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public HoldingIndexer getHoldingIndexer() {
        if(null == holdingIndexer) {
            holdingIndexer = new HoldingIndexer();
        }
        return holdingIndexer;
    }

    public void setHoldingIndexer(HoldingIndexer holdingIndexer) {
        this.holdingIndexer = holdingIndexer;
    }

    public BibIndexer getBibIndexer() {
        if(null == bibIndexer) {
            bibIndexer = new BibIndexer();

        }
        return bibIndexer;
    }

    public void setBibIndexer(BibIndexer bibIndexer) {
        this.bibIndexer = bibIndexer;
    }

    public BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}
