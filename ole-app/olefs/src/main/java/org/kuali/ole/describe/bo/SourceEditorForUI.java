package org.kuali.ole.describe.bo;

import org.kuali.ole.describe.bo.OleShelvingOrder;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 8/24/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SourceEditorForUI {

    private String leader;
    private List<MarcEditorControlField> controlFields = new ArrayList<MarcEditorControlField>();
    private List<MarcEditorDataField> dataFields = new ArrayList<MarcEditorDataField>();
    private String uuid;
    private String locationName;
    private SourceHoldingsCallNumber sourceHoldingsCallNumber;
    private String primary;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SourceEditorForUI() {
        controlFields.add(new MarcEditorControlField());
        dataFields.add(new MarcEditorDataField());
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public List<MarcEditorControlField> getControlFields() {
        return controlFields;
    }

    public void setControlFields(List<MarcEditorControlField> controlFields) {
        this.controlFields = controlFields;
    }

    public List<MarcEditorDataField> getDataFields() {
        return dataFields;
    }

    public void setDataFields(List<MarcEditorDataField> dataFields) {
        this.dataFields = dataFields;
    }

    public String getLocationName() {
        //TODO: Make common method to get subfields from dataFields
        String marcEditorDataTag;
        String marcEditorDataFieldValue;
        String locationValue = "";
        if (dataFields != null && dataFields.get(0).getTag() != null) {
            for (int i = 0; i < dataFields.size(); i++) {
                marcEditorDataTag = dataFields.get(i).getTag();
                if (marcEditorDataTag.equalsIgnoreCase("852")) {
                    marcEditorDataFieldValue = dataFields.get(i).getValue();
                    StringTokenizer str = new StringTokenizer(marcEditorDataFieldValue, "|");
                    while (str.hasMoreTokens()) {
                        String dataFieldTokenizedVal = str.nextToken();
                        String code = Character.toString(dataFieldTokenizedVal.charAt(0));
                        if (code.equals("a") || code.equals("b") || code.equals("c")) {
                            String value = dataFieldTokenizedVal.substring(1, dataFieldTokenizedVal.length());
                            if (locationValue.equalsIgnoreCase("")) {
                                locationValue = value;
                            } else {
                                locationValue = locationValue + "-" + value;
                            }
                        }
                    }
                    locationName = locationValue;
                }
            }
        }
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public SourceHoldingsCallNumber getSourceHoldingsCallNumber() {
        String marcEditorDataTag;
        String marcEditorDataFieldValue;
        String code = null;
        String dataFieldTokenizedVal = null;
        SourceHoldingsCallNumber sourceHoldingsCallNumber1 = new SourceHoldingsCallNumber();

        if (dataFields != null && dataFields.get(0).getTag() != null) {
            sourceHoldingsCallNumber1.setNumber(buildSubFieldValue("852", "h"));
            sourceHoldingsCallNumber1.setPrefix(buildSubFieldValue("852", "k"));
            if (getDataFieldValue("852") != null) {
                sourceHoldingsCallNumber1.setShelvingScheme(getDataFieldValue("852").getInd1());

                String ind2 = getDataFieldValue("852").getInd2();
                BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
                Map parentCriteria = new HashMap();
                parentCriteria.put("shelvingOrderCode", ind2);
                OleShelvingOrder oleShelvingOrder = businessObjectService.findByPrimaryKey(OleShelvingOrder.class, parentCriteria);
                String shelvingOrder = oleShelvingOrder.getShelvingOrderName();

                sourceHoldingsCallNumber1.setShelvingOrder(shelvingOrder);
            }
        }

        return sourceHoldingsCallNumber1;
    }

    public void setSourceHoldingsCallNumber(SourceHoldingsCallNumber sourceHoldingsCallNumber) {
        this.sourceHoldingsCallNumber = sourceHoldingsCallNumber;
    }

    private String buildSubFieldValue(String tag, String subField) {
        String subFieldValue = "";
        MarcEditorDataField marcEditorDataField = getDataFieldValue(tag);
        if (marcEditorDataField != null) {
            StringTokenizer str = new StringTokenizer(marcEditorDataField.getValue(), "|");
            while (str.hasMoreTokens()) {
                String dataFieldTokenizedVal = str.nextToken();
                String code = Character.toString(dataFieldTokenizedVal.charAt(0));
                if (code.equals(subField)) {
                    subFieldValue = dataFieldTokenizedVal.substring(1, dataFieldTokenizedVal.length());
                }
            }
        }
        return subFieldValue;
    }

    private MarcEditorDataField getDataFieldValue(String tag) {
        String value = "";
        MarcEditorDataField marcEditorDataField = null;
        for (int i = 0; i < dataFields.size(); i++) {
            String tag1 = dataFields.get(i).getTag();
            if (tag1.equalsIgnoreCase(tag)) {
                marcEditorDataField = dataFields.get(i);
            }
        }
        return marcEditorDataField;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        if (primary == null) {
            primary = "false";
        }
        this.primary = primary;
    }
}
