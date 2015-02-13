package org.kuali.ole.docstore.common.document;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>Java class for holdings complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="holdings">
 *   &lt;complexContent>
 *     &lt;extension base="{}docstoreDocument">
 *       &lt;sequence>
 *         &lt;element name="copyNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shelvingOrder" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="callNumberPrefix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="callNumberType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="callNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="holdingsType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bib" type="{}bib" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "holdings", propOrder = {
        "copyNumber",
        "locationName",
        "shelvingOrder",
        "callNumberPrefix",
        "callNumberType",
        "callNumber",
        "holdingsType",
        "bib",
        "isBoundWithBib",
        "bibs",
        "isSeries"
})
@XmlRootElement

public class Holdings
        extends DocstoreDocument implements Comparable<Holdings> {
    private static final Logger LOG = Logger.getLogger(Holdings.class);

    public static final String LOCATION_NAME = "LOCATIONNAME";
    public static final String LOCATION_LEVEL1 = "LOCATIONLEVEL1";
    public static final String LOCATION_LEVEL2 = "LOCATIONLEVEL2";
    public static final String LOCATION_LEVEL3 = "LOCATIONLEVEL3";
    public static final String LOCATION_LEVEL4 = "LOCATIONLEVEL4";
    public static final String LOCATION_LEVEL5 = "LOCATIONLEVEL5";
    public static final String CALL_NUMBER = "CALLNUMBER";
    public static final String CALLNUMBER_PREFIX = "CALLNUMBERPREFIX";
    public static final String CALLNUMBER_TYPE = "CALLNUMBERTYPE";
    public static final String COPY_NUMBER = "COPYNUMBER";
    public static final String BIB_TITLE = "BIBTITLE";
    public static final String HOLDINGSIDENTIFIER = "HOLDINGSIDENTIFIER";
    public static final String DESTINATION_FIELD_CALL_NUMBER = "Call Number";
    public static final String DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE = "Call Number Type";
    public static final String DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX = "Call Number Prefix";
    public static final String DESTINATION_FIELD_LOCATION_LEVEL_1 = "Location Level1";
    public static final String DESTINATION_FIELD_LOCATION_LEVEL_2 = "Location Level2";
    public static final String DESTINATION_FIELD_LOCATION_LEVEL_3 = "Location Level3";
    public static final String DESTINATION_FIELD_LOCATION_LEVEL_4 = "Location Level4";
    public static final String DESTINATION_FIELD_LOCATION_LEVEL_5 = "Location Level5";
    public static final String DESTINATION_FIELD_COPY_NUMBER = "Copy Number";
    public static final String DESTINATION_FIELD_PLATFORM = "Platform";
    protected String copyNumber;
    protected String locationName;
    protected String shelvingOrder;
    protected String callNumberPrefix;
    protected String callNumberType;
    protected String callNumber;
    protected String holdingsType;
    @XmlTransient
    protected String locationLevel1;
    @XmlTransient
    protected String locationLevel2;
    @XmlTransient
    protected String locationLevel3;
    @XmlTransient
    protected String locationLevel4;
    @XmlTransient
    protected String locationLevel5;

    @XmlTransient
    protected String level1Location;
    @XmlTransient
    protected String level2Location;
    @XmlTransient
    protected String level3Location;
    @XmlTransient
    protected String level4Location;
    @XmlTransient
    protected String level5Location;
    @XmlElement(name = "bibDoc")
    protected Bib bib;
    protected boolean isBoundWithBib;
    @XmlElement(name = "bibDocs")
    protected Bibs bibs;
    protected boolean isSeries = false;

    @XmlTransient
    protected boolean dataMappingFlag;

    public boolean isDataMappingFlag() {
        return dataMappingFlag;
    }

    public void setDataMappingFlag(boolean dataMappingFlag) {
        this.dataMappingFlag = dataMappingFlag;
    }

    public Holdings() {
        category = DocCategory.WORK.getCode();
        type = DocType.HOLDINGS.getCode();
        format = DocFormat.OLEML.getCode();
    }

    public OleHoldings getContentObject() {
        if (contentObject == null) {
            contentObject = new OleHoldings();
            ((OleHoldings) contentObject).setHoldingsType(getHoldingsType());
            if (content != null) {
                HoldingOlemlRecordProcessor recordProcessor = new HoldingOlemlRecordProcessor();
                contentObject = recordProcessor.fromXML(content);
            }
        }

        return (OleHoldings) contentObject;
    }

    public String getLevel1Location() {
        return level1Location;
    }

    public void setLevel1Location(String level1Location) {
        this.level1Location = level1Location;
    }

    public String getLevel2Location() {
        return level2Location;
    }

    public void setLevel2Location(String level2Location) {
        this.level2Location = level2Location;
    }

    public String getLevel3Location() {
        return level3Location;
    }

    public void setLevel3Location(String level3Location) {
        this.level3Location = level3Location;
    }

    public String getLevel4Location() {
        return level4Location;
    }

    public void setLevel4Location(String level4Location) {
        this.level4Location = level4Location;
    }

    public String getLevel5Location() {
        return level5Location;
    }

    public void setLevel5Location(String level5Location) {
        this.level5Location = level5Location;
    }

    public String getLocationLevel5() {
        return locationLevel5;
    }

    public void setLocationLevel5(String locationLevel5) {
        this.locationLevel5 = locationLevel5;
    }

    public String getLocationLevel4() {
        return locationLevel4;
    }

    public void setLocationLevel4(String locationLevel4) {
        this.locationLevel4 = locationLevel4;
    }

    public String getLocationLevel3() {
        return locationLevel3;
    }

    public void setLocationLevel3(String locationLevel3) {
        this.locationLevel3 = locationLevel3;
    }

    public String getLocationLevel2() {
        return locationLevel2;
    }

    public void setLocationLevel2(String locationLevel2) {
        this.locationLevel2 = locationLevel2;
    }

    public String getLocationLevel1() {
        return locationLevel1;
    }

    public void setLocationLevel1(String locationLevel1) {
        this.locationLevel1 = locationLevel1;
    }

    /**
     * Gets the value of the copyNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCopyNumber() {
        return copyNumber;
    }

    /**
     * Sets the value of the copyNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCopyNumber(String value) {
        this.copyNumber = value;
    }

    /**
     * Gets the value of the locationName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Sets the value of the locationName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocationName(String value) {
        this.locationName = value;
    }

    /**
     * Gets the value of the shelvingOrder property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getShelvingOrder() {
        return shelvingOrder;
    }

    /**
     * Sets the value of the shelvingOrder property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setShelvingOrder(String value) {
        this.shelvingOrder = value;
    }

    /**
     * Gets the value of the callNumberPrefix property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCallNumberPrefix() {
        return callNumberPrefix;
    }

    /**
     * Sets the value of the callNumberPrefix property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCallNumberPrefix(String value) {
        this.callNumberPrefix = value;
    }

    /**
     * Gets the value of the callNumberType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCallNumberType() {
        return callNumberType;
    }

    /**
     * Sets the value of the callNumberType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCallNumberType(String value) {
        this.callNumberType = value;
    }

    /**
     * Gets the value of the callNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCallNumber() {
        return callNumber;
    }

    /**
     * Sets the value of the callNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCallNumber(String value) {
        this.callNumber = value;
    }

    /**
     * Gets the value of the holdingsType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getHoldingsType() {
        return holdingsType;
    }

    /**
     * Sets the value of the holdingsType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHoldingsType(String value) {
        this.holdingsType = value;
    }

    /**
     * Gets the value of the bib property.
     *
     * @return possible object is
     * {@link Bib }
     */
    public Bib getBib() {
        return bib;
    }

    /**
     * Sets the value of the bib property.
     *
     * @param value allowed object is
     *              {@link Bib }
     */
    public void setBib(Bib value) {
        this.bib = value;
    }

    public boolean isBoundWithBib() {
        return isBoundWithBib;
    }

    public void setBoundWithBib(boolean boundWithBib) {
        isBoundWithBib = boundWithBib;
    }

    public Bibs getBibs() {
        return bibs;
    }

    public void setBibs(Bibs bibs) {
        this.bibs = bibs;
    }

    public boolean isSeries() {
        return isSeries;
    }

    public void setSeries(boolean series) {
        isSeries = series;
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        Holdings holdings = (Holdings) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Holdings.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(holdings, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {

        JAXBElement<Holdings> holdingsElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Holdings.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            holdingsElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), Holdings.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holdingsElement.getValue();
    }

    public Object deserializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object deserializeContent(String content) {
        HoldingOlemlRecordProcessor recordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = recordProcessor.fromXML(content);
        return oleHoldings;
    }

    @Override
    public String serializeContent(Object object) {
        OleHoldings oleHoldings = (OleHoldings) object;
        HoldingOlemlRecordProcessor recordProcessor = new HoldingOlemlRecordProcessor();
        String content = recordProcessor.toXML(oleHoldings);
        return content;
    }

    public void serializeContent() {
        if (contentObject == null) {
            return;
        }

        OleHoldings oleHoldings = (OleHoldings) contentObject;
        Location locationPojo = buildLocationObj();

        oleHoldings.setLocation(locationPojo);
        HoldingOlemlRecordProcessor recordProcessor = new HoldingOlemlRecordProcessor();
        content = recordProcessor.toXML(oleHoldings);
    }

    public void setField(String docField, String fieldValue) {

        OleHoldings oleHoldings = getContentObject();
        buildItemCallNumber(oleHoldings);


        if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER)) {
            oleHoldings.getCallNumber().setNumber(fieldValue);
            String callNumberType = oleHoldings.getCallNumber().getShelvingScheme().getCodeValue();
            if (StringUtils.isEmpty(callNumberType) || callNumberType.equals(NO_INFO_CALL_NUMBER_TYPE_CODE)) {
                oleHoldings.getCallNumber().getShelvingScheme().setCodeValue("NONE");
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE)) {
            setDataMappingFlag(true);
            oleHoldings.getCallNumber().getShelvingScheme().setCodeValue(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
            oleHoldings.getCallNumber().setPrefix(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_1)) {
            setLocationLevel1(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_2)) {
            setLocationLevel2(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_3)) {
            setLocationLevel3(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_4)) {
            setLocationLevel4(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_5)) {
            setLocationLevel5(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COPY_NUMBER)) {
            oleHoldings.setCopyNumber(fieldValue);
        }
    }


    public void setDefaultField(String docField, String fieldValue) {

        OleHoldings oleHoldings = getContentObject();
        buildItemCallNumber(oleHoldings);


        if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER)) {
            if (oleHoldings.getCallNumber().getNumber() == null) {
                oleHoldings.getCallNumber().setNumber(fieldValue);
                String callNumberType = oleHoldings.getCallNumber().getShelvingScheme().getCodeValue();
                if (StringUtils.isEmpty(callNumberType) || callNumberType.equals(NO_INFO_CALL_NUMBER_TYPE_CODE)) {
                    oleHoldings.getCallNumber().getShelvingScheme().setCodeValue("NONE");
                }
            }

        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE)) {

            if (oleHoldings.getCallNumber().getShelvingScheme().getCodeValue() == null || !isDataMappingFlag()) {
                oleHoldings.getCallNumber().getShelvingScheme().setCodeValue(fieldValue);
            }

        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
            if (oleHoldings.getCallNumber().getPrefix() == null) {
                oleHoldings.getCallNumber().setPrefix(fieldValue);
            }

        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_1)) {
            if (getLocationLevel1() == null) {
                setLocationLevel1(fieldValue);
            }

        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_2)) {
            if (getLocationLevel2() == null) {
                setLocationLevel2(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_3)) {
            if (getLocationLevel3() == null) {
                setLocationLevel3(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_4)) {
            if (getLocationLevel4() == null) {
                setLocationLevel4(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_5)) {
            if (getLocationLevel5() == null) {
                setLocationLevel5(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COPY_NUMBER)) {
            if (oleHoldings.getCopyNumber() == null) {
                oleHoldings.setCopyNumber(fieldValue);
            }

        }
    }

    public void buildItemCallNumber(OleHoldings oleHoldings) {
        if (oleHoldings.getCallNumber() == null) {
            CallNumber callNumberPojo = new CallNumber();
            ShelvingOrder shelvingOrder1 = new ShelvingOrder();
            ShelvingScheme shelvingScheme1 = new ShelvingScheme();
            callNumberPojo.setShelvingScheme(shelvingScheme1);
            callNumberPojo.setShelvingOrder(shelvingOrder1);
            oleHoldings.setCallNumber(callNumberPojo);
        } else {
            if (oleHoldings.getCallNumber().getShelvingOrder() == null) {
                ShelvingOrder shelvingOrder1 = new ShelvingOrder();
                oleHoldings.getCallNumber().setShelvingOrder(shelvingOrder1);
            }
            if (oleHoldings.getCallNumber().getShelvingScheme() == null) {
                ShelvingScheme shelvingScheme1 = new ShelvingScheme();
                oleHoldings.getCallNumber().setShelvingScheme(shelvingScheme1);
            }
        }
    }

    public Location buildLocationObj() {

        LocationLevel locationLevel1 = new LocationLevel();
        locationLevel1.setName(getLocationLevel1());
        locationLevel1.setLevel(getLevel1Location());
        LocationLevel locationLevel2 = new LocationLevel();
        locationLevel2.setName(getLocationLevel2());
        locationLevel2.setLevel(getLevel2Location());
        LocationLevel locationLevel3 = new LocationLevel();
        locationLevel3.setName(getLocationLevel3());
        locationLevel3.setLevel(getLevel3Location());
        LocationLevel locationLevel4 = new LocationLevel();
        locationLevel4.setName(getLocationLevel4());
        locationLevel4.setLevel(getLevel4Location());
        LocationLevel locationLevel5 = new LocationLevel();
        locationLevel5.setName(getLocationLevel5());
        locationLevel5.setLevel(getLevel5Location());

        List<LocationLevel> locationLevels = new ArrayList<>();

        if (StringUtils.isNotEmpty(locationLevel1.getName())) {
            locationLevels.add(locationLevel1);
        }
        if (StringUtils.isNotEmpty(locationLevel2.getName())) {
            locationLevels.add(locationLevel2);
        }
        if (StringUtils.isNotEmpty(locationLevel3.getName())) {
            locationLevels.add(locationLevel3);
        }
        if (StringUtils.isNotEmpty(locationLevel4.getName())) {
            locationLevels.add(locationLevel4);
        }
        if (StringUtils.isNotEmpty(locationLevel5.getName())) {
            locationLevels.add(locationLevel5);
        }

        int locationLevelSize = locationLevels.size();

        for (int i = 0; i < locationLevelSize; i++) {
            if ((locationLevelSize - 1) != i) {
                locationLevels.get(i).setLocationLevel(locationLevels.get(i + 1));
            }
        }

        Location locationPojo = new Location();
        if (locationLevels.size() > 0) {
            locationPojo.setLocationLevel(locationLevels.get(0));
        }

        return locationPojo;
    }


    public String getHoldingsDataMappingValue(Map<String, String> holdingsDocFields) {

        OleHoldings oleHoldings = (OleHoldings) contentObject;

        StringBuilder holdingsDataValue = new StringBuilder();
        if (holdingsDocFields.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER) && oleHoldings.getCallNumber().getNumber() != null) {
            holdingsDataValue.append(oleHoldings.getCallNumber().getNumber());
        }

        if (holdingsDocFields.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_PREFIX) && oleHoldings.getCallNumber().getPrefix() != null) {
            holdingsDataValue.append(oleHoldings.getCallNumber().getPrefix());
        }
        if (holdingsDocFields.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_TYPE) && oleHoldings.getCallNumber().getType() != null) {
            holdingsDataValue.append(oleHoldings.getCallNumber().getType());
        }

        if (holdingsDocFields.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_COPY_NUMBER) && oleHoldings.getCopyNumber() != null) {
            holdingsDataValue.append(oleHoldings.getCopyNumber());
        }

        buildLocationLevels(oleHoldings);
        if (holdingsDocFields.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_1) && getLocationLevel1() != null) {
            holdingsDataValue.append(getLocationLevel1());
        }
        if (holdingsDocFields.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_2) && getLocationLevel2() != null) {
            holdingsDataValue.append(getLocationLevel2());
        }
        if (holdingsDocFields.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_3) && getLocationLevel3() != null) {
            holdingsDataValue.append(getLocationLevel3());
        }
        if (holdingsDocFields.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_4) && getLocationLevel4() != null) {
            holdingsDataValue.append(getLocationLevel4());
        }
        if (holdingsDocFields.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_5) && getLocationLevel5() != null) {
            holdingsDataValue.append(getLocationLevel5());

        }
        return holdingsDataValue.toString();
    }

    private void buildLocationLevels(OleHoldings oleHoldings) {
        if (oleHoldings != null) {
            if (oleHoldings.getLocation() != null) {
                LocationLevel locationLevel = oleHoldings.getLocation().getLocationLevel();
                setLocationLevel1(getLocationLevelName(locationLevel, level1Location));
                setLocationLevel2(getLocationLevelName(locationLevel, level2Location));
                setLocationLevel3(getLocationLevelName(locationLevel, level3Location));
                setLocationLevel4(getLocationLevelName(locationLevel, level4Location));
                setLocationLevel5(getLocationLevelName(locationLevel, level5Location));
            }
        }
    }


    private String getLocationLevelName(LocationLevel locationLevel, String level) {
        if (locationLevel == null || org.apache.commons.lang.StringUtils.isEmpty(locationLevel.getLevel())) return null;
        if (locationLevel.getLevel().toUpperCase().startsWith(level)) return locationLevel.getName();
        return getLocationLevelName(locationLevel.getLocationLevel(), level);
    }

    @Override
    public int compareTo(Holdings o) {
        return this.getSortedValue().compareTo(o.getSortedValue());
    }
}