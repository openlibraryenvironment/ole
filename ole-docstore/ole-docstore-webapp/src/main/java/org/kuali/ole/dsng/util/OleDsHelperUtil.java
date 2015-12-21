package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.OLEItemDonorRecord;
import org.kuali.ole.docstore.model.rdbms.bo.OLEDonorRecord;
import org.kuali.ole.dsng.indexer.BibIndexer;
import org.kuali.ole.dsng.indexer.HoldingIndexer;
import org.kuali.ole.dsng.indexer.ItemIndexer;
import org.kuali.ole.utility.MarcRecordUtil;
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

    public enum LEVEL_NAMES {
        INSTITUTION("Institution"), CAMPUS("Campus"), LIBRARY("Library"), COLLECTION("Collection"), SHELVING("Shelving");
        private String name;

        LEVEL_NAMES(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    };

    private ObjectMapper objectMapper;

    private BibIndexer bibIndexer;

    private HoldingIndexer holdingIndexer;

    private ItemIndexer itemIndexer;

    private BusinessObjectService businessObjectService;

    private MarcRecordUtil marcRecordUtil;

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

    public CallNumberTypeRecord fetchCallNumberTypeRecordById(String callNumberTypeId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("callNumberTypeId", callNumberTypeId);
        List<CallNumberTypeRecord> matching = (List<CallNumberTypeRecord>) getBusinessObjectService().findMatching(CallNumberTypeRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }

    public ItemStatusRecord fetchItemStatusByName(String itemStatusTypeName) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", itemStatusTypeName);
        List<ItemStatusRecord> matching = (List<ItemStatusRecord>) getBusinessObjectService().findMatching(ItemStatusRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }

    public ItemTypeRecord fetchItemTypeByName(String itemTypeName) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", itemTypeName);
        List<ItemTypeRecord> matching = (List<ItemTypeRecord>) getBusinessObjectService().findMatching(ItemTypeRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }

    public OLEItemDonorRecord fetchDonorCodeByCode(String donorCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("donorCode", donorCode);
        List<OLEItemDonorRecord> matching = (List<OLEItemDonorRecord>) getBusinessObjectService().findMatching(OLEItemDonorRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }

    public OLEItemDonorRecord fetchDonorCodeByPublicDisplay(String publicDisplay) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("donorPublicDisplay", publicDisplay);
        List<OLEItemDonorRecord> matching = (List<OLEItemDonorRecord>) getBusinessObjectService().findMatching(OLEItemDonorRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }


    public String formLocation(String locationLevel1, String locationLevel2, String locationLevel3,
                               String locationLevel4, String locationLevel5) {
        StringBuilder location = new StringBuilder();

        if (StringUtils.isNotBlank(locationLevel1)) {
            appendLocationToStringBuilder(location, locationLevel1);
        }
        if (StringUtils.isNotBlank(locationLevel2)) {
            appendLocationToStringBuilder(location, locationLevel2);
        }
        if (StringUtils.isNotBlank(locationLevel3)) {
            appendLocationToStringBuilder(location, locationLevel3);
        }
        if (StringUtils.isNotBlank(locationLevel4)) {
            appendLocationToStringBuilder(location, locationLevel4);
        }
        if (StringUtils.isNotBlank(locationLevel5)) {
            appendLocationToStringBuilder(location, locationLevel5);
        }

        return location.toString();
    }


    public String getStringValueFromJsonObject(JSONObject jsonObject, String key) {
        String returnValue = null;
        try {
            returnValue = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    private void appendLocationToStringBuilder(StringBuilder stringBuilder, String location) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append(FORWARD_SLASH).append(location);
        } else {
            stringBuilder.append(location);
        }
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

    public ItemIndexer getItemIndexer() {
        if(null == itemIndexer) {
            itemIndexer = new ItemIndexer();
        }
        return itemIndexer;
    }

    public void setItemIndexer(ItemIndexer itemIndexer) {
        this.itemIndexer = itemIndexer;
    }

    public BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public MarcRecordUtil getMarcRecordUtil() {
        if(null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }

    public void setMarcRecordUtil(MarcRecordUtil marcRecordUtil) {
        this.marcRecordUtil = marcRecordUtil;
    }
}
