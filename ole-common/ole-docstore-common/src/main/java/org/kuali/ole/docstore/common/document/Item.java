package org.kuali.ole.docstore.common.document;

import com.thoughtworks.xstream.converters.ConversionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;

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
 * <p>Java class for item complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="item">
 *   &lt;complexContent>
 *     &lt;extension base="{}docstoreDocument">
 *       &lt;sequence>
 *         &lt;element name="callNumberType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="callNumberPrefix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="itemStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="volumeNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="itemType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chronology" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enumeration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="copyNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="barcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shelvingOrder" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="callNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="holding" type="{}holdings" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item", propOrder = {
        "callNumberType",
        "callNumberPrefix",
        "itemStatus",
        "volumeNumber",
        "itemType",
        "locationName",
        "chronology",
        "enumeration",
        "copyNumber",
        "barcode",
        "shelvingOrder",
        "location",
        "callNumber",
        "holding",
        "isAnalytic",
        "holdings"
})
@XmlRootElement(name = "itemDoc")

public class Item
        extends DocstoreDocument implements Comparable<Item> {

    private static final Logger LOG = Logger.getLogger(Item.class);
    public static final String CALL_NUMBER = "CALLNUMBER";
    public static final String CALL_NUMBER_TYPE = "CALLNUMBERTYPE";
    public static final String CALL_NUMBER_PREFIX = "CALLNUMBERPREFIX";
    public static final String HOLDINGS_CALL_NUMBER = "HOLDINGSCALLNUMBER";
    public static final String HOLDINGS_CALL_NUMBER_TYPE = "HOLDINGSCALLNUMBERTYPE";
    public static final String HOLDINGS_CALL_NUMBER_PREFIX = "HOLDINGSCALLNUMBERPREFIX";
    public static final String LOCATION = "LOCATION";
    public static final String HOLDINGS_LOCATION_LEVEL1 = "HOLDINGSLOCATIONLEVEL1";
    public static final String HOLDINGS_LOCATION_LEVEL2 = "HOLDINGSLOCATIONLEVEL2";
    public static final String HOLDINGS_LOCATION_LEVEL3 = "HOLDINGSLOCATIONLEVEL3";
    public static final String HOLDINGS_LOCATION_LEVEL4 = "HOLDINGSLOCATIONLEVEL4";
    public static final String HOLDINGS_LOCATION_LEVEL5 = "HOLDINGSLOCATIONLEVEL5";
    public static final String VENDOR_LINE_ITEM_IDENTIFIER = "VENDORLINEITEMIDENTIFIER";
    public static final String SHELVING_ORDER = "SHELVINGORDER";
    public static final String ITEM_BARCODE = "ITEMBARCODE";
    public static final String COPY_NUMBER = "COPYNUMBER";
    public static final String HOLDINGS_COPY_NUMBER = "HOLDINGSCOPYNUMBER";
    public static final String ENUMERATION = "ENUMERATION";
    public static final String CHRONOLOGY = "CHRONOLOGY";
    public static final String ITEM_TYPE = "ITEMTYPE";
    public static final String VOLUME_NUMBER = "VOLUMENUMBER";
    public static final String ITEM_STATUS = "ITEMSTATUS";
    public static final String ITEMIDENTIFIER = "ITEMIDENTIFIER";
    public static final String DONOR_CODE = "DONORCODE";
    public static final String DONOR_NOTE = "DONORNOTE";
    public static final String DONOR_PUBLIC = "DONORPUBLICDISPLAY";
    public static final String DESTINATION_FIELD_ITEM_ITEM_BARCODE = "Item Barcode";
    public static final String DESTINATION_FIELD_CALL_NUMBER = "Call Number";
    public static final String DESTINATION_FIELD_COPY_NUMBER = "Copy Number";
    public static final String DESTINATION_FIELD_CALL_NUMBER_TYPE = "Call Number Type";
    public static final String DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX = "Call Number Prefix";
    public static final String DESTINATION_ITEM_TYPE = "Item Type";
    public static final String DESTINATION_ITEM_STATUS = "Item Status";
    public static final String DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER = "Holdings Call Number";
    public static final String DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_TYPE = "Holdings Call Number Type";
    public static final String DESTINATION_FIELD_ITEM_HOLDINGS_COPY_NUMBER = "Holdings Copy Number";
    public static final String DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_PREFIX = "Holdings Call Number Prefix";
    public static final String DESTINATION_FIELD_LOCATION_LEVEL_1 = "Location Level1";
    public static final String DESTINATION_FIELD_LOCATION_LEVEL_2 = "Location Level2";
    public static final String DESTINATION_FIELD_LOCATION_LEVEL_3 = "Location Level3";
    public static final String DESTINATION_FIELD_LOCATION_LEVEL_4 = "Location Level4";
    public static final String DESTINATION_FIELD_LOCATION_LEVEL_5 = "Location Level5";
    public static final String DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_1 = "Holdings Location Level1";
    public static final String DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_2 = "Holdings Location Level2";
    public static final String DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_3 = "Holdings Location Level3";
    public static final String DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_4 = "Holdings Location Level4";
    public static final String DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_5 = "Holdings Location Level5";
    public static final String DESTINATION_FIELD_ITEM_VENDOR_LINE_ITEM_IDENTIFIER = "Vendor Line Item Identifier";
    public static final String DUE_DATE_TIME = "DUE_DATE_TIME";
    public static final String NO_OF_RENEWAL = "NO_OF_RENEWAL";

    @XmlTransient
    private String holdingsCallNumber;
    @XmlTransient
    private String holdingsCallNumberType;
    @XmlTransient
    private String holdingsCopyNumber;
    @XmlTransient
    private String holdingsCallNumberPrefix;
    @XmlTransient
    private String holdingsLocationLevel1;
    @XmlTransient
    private String holdingsLocationLevel2;
    @XmlTransient
    private String holdingsLocationLevel3;
    @XmlTransient
    private String holdingsLocationLevel4;
    @XmlTransient
    private String holdingsLocationLevel5;


    @XmlTransient
    protected boolean dataMappingFlag;

    public boolean isDataMappingFlag() {
        return dataMappingFlag;
    }

    public void setDataMappingFlag(boolean dataMappingFlag) {
        this.dataMappingFlag = dataMappingFlag;
    }

    protected String callNumberType;
    protected String callNumberPrefix;
    protected String itemStatus;
    protected String volumeNumber;
    protected String itemType;
    protected String locationName;
    protected String chronology;
    protected String enumeration;
    protected String copyNumber;
    protected String barcode;
    protected String shelvingOrder;
    protected String location;
    protected String callNumber;
    @XmlElement(name = "holdingsDoc")
    protected Holdings holding;
    @XmlElement(name = "holdingsDocs")
    protected List<Holdings> holdings;
    protected boolean isAnalytic = false;
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

    public String getLevel1Location() {
        return level1Location;
    }

    public void setLevel1Location(String level1Location) {
        this.level1Location = level1Location;
    }

    public String getLevel5Location() {
        return level5Location;
    }

    public void setLevel5Location(String level5Location) {
        this.level5Location = level5Location;
    }

    public String getLevel4Location() {
        return level4Location;
    }

    public void setLevel4Location(String level4Location) {
        this.level4Location = level4Location;
    }

    public String getLevel3Location() {
        return level3Location;
    }

    public void setLevel3Location(String level3Location) {
        this.level3Location = level3Location;
    }

    public String getLevel2Location() {
        return level2Location;
    }

    public void setLevel2Location(String level2Location) {
        this.level2Location = level2Location;
    }

    public Item() {
        category = DocCategory.WORK.getCode();
        type = DocType.ITEM.getCode();
        format = DocFormat.OLEML.getCode();
    }

    public String getLocationLevel1() {
        return locationLevel1;
    }

    public void setLocationLevel1(String locationLevel1) {
        this.locationLevel1 = locationLevel1;
    }

    public String getLocationLevel2() {
        return locationLevel2;
    }

    public void setLocationLevel2(String locationLevel2) {
        this.locationLevel2 = locationLevel2;
    }

    public String getLocationLevel3() {
        return locationLevel3;
    }

    public void setLocationLevel3(String locationLevel3) {
        this.locationLevel3 = locationLevel3;
    }

    public String getLocationLevel4() {
        return locationLevel4;
    }

    public void setLocationLevel4(String locationLevel4) {
        this.locationLevel4 = locationLevel4;
    }

    public String getLocationLevel5() {

        return locationLevel5;
    }

    public void setLocationLevel5(String locationLevel5) {
        this.locationLevel5 = locationLevel5;
    }

    /**
     * Gets the value of the callNumberType property.
     *
     * @return possible object is
     *         {@link String }
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
     * Gets the value of the callNumberPrefix property.
     *
     * @return possible object is
     *         {@link String }
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
     * Gets the value of the itemStatus property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getItemStatus() {
        return itemStatus;
    }

    /**
     * Sets the value of the itemStatus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setItemStatus(String value) {
        this.itemStatus = value;
    }

    /**
     * Gets the value of the volumeNumber property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getVolumeNumber() {
        return volumeNumber;
    }

    /**
     * Sets the value of the volumeNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVolumeNumber(String value) {
        this.volumeNumber = value;
    }

    /**
     * Gets the value of the itemType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Sets the value of the itemType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setItemType(String value) {
        this.itemType = value;
    }

    /**
     * Gets the value of the locationName property.
     *
     * @return possible object is
     *         {@link String }
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
     * Gets the value of the chronology property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getChronology() {
        return chronology;
    }

    /**
     * Sets the value of the chronology property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setChronology(String value) {
        this.chronology = value;
    }

    /**
     * Gets the value of the enumeration property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEnumeration() {
        return enumeration;
    }

    /**
     * bib.java
     * Sets the value of the enumeration property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEnumeration(String value) {
        this.enumeration = value;
    }

    /**
     * Gets the value of the copyNumber property.
     *
     * @return possible object is
     *         {@link String }
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
     * Gets the value of the barcode property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Sets the value of the barcode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBarcode(String value) {
        this.barcode = value;
    }

    /**
     * Gets the value of the shelvingOrder property.
     *
     * @return possible object is
     *         {@link String }
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
     * Gets the value of the location property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the callNumber property.
     *
     * @return possible object is
     *         {@link String }
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
     * Gets the value of the holding property.
     *
     * @return possible object is
     *         {@link Holdings }
     */
    public Holdings getHolding() {
        return holding;
    }

    /**
     * Sets the value of the holding property.
     *
     * @param value allowed object is
     *              {@link Holdings }
     */
    public void setHolding(Holdings value) {
        this.holding = value;
    }

    public boolean isAnalytic() {
        return isAnalytic;
    }

    public void setAnalytic(boolean analytic) {
        isAnalytic = analytic;
    }

    public List<Holdings> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<Holdings> holdings) {
        this.holdings = holdings;
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        Item item = (Item) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(Item.class);
            synchronized (jaxbMarshaller) {
            jaxbMarshaller.marshal(item, sw);
            }
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {
        Item item = new Item();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(Item.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                item = unmarshaller.unmarshal(new StreamSource(input), Item.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return item;
    }

    @Override
    public Object deserializeContent(Object object) {


        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object deserializeContent(String content) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String serializeContent(Object object) {
        if (object == null) {
            object = getContentObject();
        }
        LocationLevel locationLevel1 = new LocationLevel();
        LocationLevel locationLevel2 = new LocationLevel();
        LocationLevel locationLevel3 = new LocationLevel();
        LocationLevel locationLevel4 = new LocationLevel();
        LocationLevel locationLevel5 = new LocationLevel();

        locationLevel1.setName(getLocationLevel1());
        locationLevel2.setName(getLocationLevel2());
        locationLevel3.setName(getLocationLevel3());
        locationLevel4.setName(getLocationLevel4());
        locationLevel5.setName(getLocationLevel5());
        locationLevel1.setLocationLevel(locationLevel2);
        locationLevel2.setLocationLevel(locationLevel3);
        locationLevel3.setLocationLevel(locationLevel4);
        locationLevel4.setLocationLevel(locationLevel5);
        Location locationPojo = new Location();
        locationPojo.setLocationLevel(locationLevel1);
        org.kuali.ole.docstore.common.document.content.instance.Item itemPojo = (org.kuali.ole.docstore.common.document.content.instance.Item) object;
        itemPojo.setLocation(locationPojo);
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        String itemXml = itemOlemlRecordProcessor.toXML(itemPojo);
        return itemXml;
    }

    public void serializeContent() {
        if (contentObject == null) {
            return;
        }
        Location locationPojo = buildLocationObj();
        org.kuali.ole.docstore.common.document.content.instance.Item itemPojo = (org.kuali.ole.docstore.common.document.content.instance.Item) contentObject;
        itemPojo.setLocation(locationPojo);
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        content = itemOlemlRecordProcessor.toXML(itemPojo);
    }

    public void setField(String docField, String fieldValue) {
        org.kuali.ole.docstore.common.document.content.instance.Item itemPojo = (org.kuali.ole.docstore.common.document.content.instance.Item) getContentObject();


        if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_ITEM_BARCODE)) {
            if (itemPojo.getAccessInformation() == null) {
                AccessInformation accessInformation = new AccessInformation();
                itemPojo.setAccessInformation(accessInformation);
            }
            itemPojo.getAccessInformation().setBarcode(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER)) {

            buildItemCallNumber(itemPojo);
            itemPojo.getCallNumber().setNumber(fieldValue);
            String callNumberType = itemPojo.getCallNumber().getShelvingScheme().getCodeValue();
            if(StringUtils.isEmpty(callNumberType) || callNumberType.equals(NO_INFO_CALL_NUMBER_TYPE_CODE)) {
                itemPojo.getCallNumber().getShelvingScheme().setCodeValue("NONE");
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER_TYPE)) {
            buildItemCallNumber(itemPojo);
            itemPojo.getCallNumber().getShelvingScheme().setCodeValue(fieldValue);
            setDataMappingFlag(true);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
            buildItemCallNumber(itemPojo);
            itemPojo.getCallNumber().setPrefix(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COPY_NUMBER)) {
            itemPojo.setCopyNumber(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_ITEM_TYPE)) {
            ItemType itemType1 = new ItemType();
            itemType1.setCodeValue(fieldValue);
            itemPojo.setItemType(itemType1);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER)) {
            setHoldingsCallNumber(fieldValue);
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
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_TYPE)) {
            setHoldingsCallNumberType(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_1)) {
            setHoldingsLocationLevel1(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_2)) {
            setHoldingsLocationLevel2(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_3)) {
            setHoldingsLocationLevel3(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_4)) {
            setHoldingsLocationLevel4(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_5)) {
            setHoldingsLocationLevel5(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_COPY_NUMBER)) {
            setHoldingsCopyNumber(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_PREFIX)) {
            setHoldingsCallNumberPrefix(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_ITEM_STATUS)) {
            ItemStatus itemStatus1 = new ItemStatus();
            itemStatus1.setCodeValue(fieldValue);
            itemPojo.setItemStatus(itemStatus1);
        } else if (docField.equalsIgnoreCase(ENUMERATION)) {
            itemPojo.setEnumeration(fieldValue);
        } else if (docField.equalsIgnoreCase(CHRONOLOGY)) {
            itemPojo.setChronology(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_VENDOR_LINE_ITEM_IDENTIFIER)) {
            itemPojo.setVendorLineItemIdentifier(fieldValue);
        } else if (docField.equalsIgnoreCase(DUE_DATE_TIME)) {
            itemPojo.setDueDateTime(fieldValue);
        } else if (docField.equalsIgnoreCase(NO_OF_RENEWAL)) {
            itemPojo.setNumberOfRenew(Integer.valueOf(fieldValue));
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_CODE) || docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY) || docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_NOTE)) {
            DonorInfo donorInfo = null;
            if (itemPojo.getDonorInfo() != null && itemPojo.getDonorInfo().size() > 0) {
                for (DonorInfo existingDonorInfo : itemPojo.getDonorInfo()) {
                  buildDonorInfo(docField, fieldValue, itemPojo, existingDonorInfo, false);
                }
            } else {
                donorInfo = new DonorInfo();
                buildDonorInfo(docField, fieldValue, itemPojo, donorInfo, false);
            }
        }
    }

    private void buildDonorInfo(String docField, String fieldValue, org.kuali.ole.docstore.common.document.content.instance.Item itemPojo, DonorInfo donorInfo, boolean isDefault) {
        if (docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_CODE)) {
            if (donorInfo.getDonorCode() == null) {
                donorInfo.setDonorCode(fieldValue);
            } else if (!isDefault) {
                donorInfo.setDonorCode(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY)) {
            if (donorInfo.getDonorPublicDisplay() == null) {
                donorInfo.setDonorPublicDisplay(fieldValue);
            } else if (!isDefault) {
                donorInfo.setDonorPublicDisplay(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_NOTE)) {
            if (donorInfo.getDonorNote() == null) {
                donorInfo.setDonorNote(fieldValue);
            } else if (!isDefault) {
                donorInfo.setDonorNote(fieldValue);
            }
        }
        if (itemPojo.getDonorInfo() == null || itemPojo.getDonorInfo().size() == 0) {
            itemPojo.getDonorInfo().add(donorInfo);
        }
    }

    private void buildItemCallNumber(org.kuali.ole.docstore.common.document.content.instance.Item itemPojo) {
        if (itemPojo.getCallNumber() == null) {
            CallNumber callNumberPojo = new CallNumber();
            ShelvingOrder shelvingOrder1 = new ShelvingOrder();
            ShelvingScheme shelvingScheme1 = new ShelvingScheme();
            callNumberPojo.setShelvingScheme(shelvingScheme1);
            callNumberPojo.setShelvingOrder(shelvingOrder1);
            itemPojo.setCallNumber(callNumberPojo);
        } else {
            if (itemPojo.getCallNumber().getShelvingOrder() == null) {
                ShelvingOrder shelvingOrder1 = new ShelvingOrder();
                itemPojo.getCallNumber().setShelvingOrder(shelvingOrder1);
            }
            if (itemPojo.getCallNumber().getShelvingScheme() == null) {
                ShelvingScheme shelvingScheme1 = new ShelvingScheme();
                itemPojo.getCallNumber().setShelvingScheme(shelvingScheme1);
            }
        }
    }

    public Object getContentObject() {
        if (contentObject == null) {
            contentObject = new org.kuali.ole.docstore.common.document.content.instance.Item();
            if (content != null) {
                ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
                contentObject = itemOlemlRecordProcessor.fromXML(content);
            }
        }
        return contentObject;
    }


    public  Location buildLocationObj() {

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


    @Override
    public int compareTo(Item o) {
        return this.getSortedValue().compareTo(o.getSortedValue());
    }


    public void setItem(Item itemIncoming) throws ConversionException {
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item incomingItemContent = itemOlemlRecordProcessor.fromXML(itemIncoming.getContent());
        org.kuali.ole.docstore.common.document.content.instance.Item existingItemContent = itemOlemlRecordProcessor.fromXML(this.getContent());

        this.setId(existingItemContent.getItemIdentifier());

        if (StringUtils.isNotBlank(itemIncoming.getUpdatedBy())) {
            this.updatedBy = itemIncoming.getUpdatedBy();
        } else {
            this.updatedBy = "UNKNOWN";
        }
        if (incomingItemContent.getLocation() != null) {
            existingItemContent.setLocation(incomingItemContent.getLocation());
        }
        if (incomingItemContent.getCallNumber() != null) {
            existingItemContent.setCallNumber(incomingItemContent.getCallNumber());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getChronology())) {
            existingItemContent.setChronology(incomingItemContent.getChronology());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getEnumeration())) {
            existingItemContent.setEnumeration(incomingItemContent.getEnumeration());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getCopyNumber())) {
            existingItemContent.setCopyNumber(incomingItemContent.getCopyNumber());
        }
        if (incomingItemContent.getItemStatus() != null) {
            existingItemContent.setItemStatus(incomingItemContent.getItemStatus());
        }
        if (incomingItemContent.getItemType() != null) {
            existingItemContent.setItemType(incomingItemContent.getItemType());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getBarcodeARSL())) {
            existingItemContent.setBarcodeARSL(incomingItemContent.getBarcodeARSL());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getVolumeNumber())) {
            existingItemContent.setVolumeNumber(incomingItemContent.getVolumeNumber());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getCheckinNote())) {
            existingItemContent.setCheckinNote(incomingItemContent.getCheckinNote());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getCurrentBorrower())) {
            existingItemContent.setCurrentBorrower(incomingItemContent.getCurrentBorrower());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getClaimsReturnedFlagCreateDate())) {
            existingItemContent.setClaimsReturnedFlagCreateDate(incomingItemContent.getClaimsReturnedFlagCreateDate());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getAnalytic())) {
            existingItemContent.setAnalytic(incomingItemContent.getAnalytic());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getDueDateTime())) {
            existingItemContent.setDueDateTime(incomingItemContent.getDueDateTime());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getDamagedItemNote())) {
            existingItemContent.setDamagedItemNote(incomingItemContent.getDamagedItemNote());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getItemStatusEffectiveDate())) {
            existingItemContent.setItemStatusEffectiveDate(incomingItemContent.getItemStatusEffectiveDate());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getMissingPieceEffectiveDate())) {
            existingItemContent.setMissingPieceEffectiveDate(incomingItemContent.getMissingPieceEffectiveDate());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getClaimsReturnedNote())) {
            existingItemContent.setClaimsReturnedNote(incomingItemContent.getClaimsReturnedNote());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getProxyBorrower())) {
            existingItemContent.setProxyBorrower(incomingItemContent.getProxyBorrower());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getMissingPieceFlagNote())) {
            existingItemContent.setMissingPieceFlagNote(incomingItemContent.getMissingPieceFlagNote());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getMissingPiecesCount())) {
            existingItemContent.setMissingPiecesCount(incomingItemContent.getMissingPiecesCount());
        }
        if (incomingItemContent.getAccessInformation() != null) {
            if(StringUtils.isNotBlank(incomingItemContent.getAccessInformation().getBarcode())) {
                existingItemContent.getAccessInformation().setBarcode(incomingItemContent.getAccessInformation().getBarcode());
            }
            if(incomingItemContent.getAccessInformation().getUri()!=null){
                existingItemContent.getAccessInformation().setUri(incomingItemContent.getAccessInformation().getUri());
            }
        }
        if (incomingItemContent.getTemporaryItemType() != null) {
            existingItemContent.setTemporaryItemType(incomingItemContent.getTemporaryItemType());
        }
        if (incomingItemContent.getStatisticalSearchingCode() != null) {
            existingItemContent.setStatisticalSearchingCode(incomingItemContent.getStatisticalSearchingCode());
        }
        if (incomingItemContent.getNumberOfCirculations() != null) {
            existingItemContent.setNumberOfCirculations(incomingItemContent.getNumberOfCirculations());
        }
        if (incomingItemContent.getDonorInfo() != null) {
            existingItemContent.setDonorInfo(incomingItemContent.getDonorInfo());
        }
        if (incomingItemContent.getMissingPieceItemRecordList() != null) {
            existingItemContent.setMissingPieceItemRecordList(incomingItemContent.getMissingPieceItemRecordList());
        }
        if(incomingItemContent.getItemClaimsReturnedRecords() != null){
            existingItemContent.setItemClaimsReturnedRecords(incomingItemContent.getItemClaimsReturnedRecords());
        }
        if(incomingItemContent.getItemDamagedRecords() != null){
            existingItemContent.setItemDamagedRecords(incomingItemContent.getItemDamagedRecords());
        }
        if (incomingItemContent.getHighDensityStorage() != null) {
            existingItemContent.setHighDensityStorage(incomingItemContent.getHighDensityStorage());
        }
        if (incomingItemContent.getNote() != null) {
            existingItemContent.setNote(incomingItemContent.getNote());
        }
        if (incomingItemContent.getFormerIdentifier() != null) {
            existingItemContent.setFormerIdentifier(incomingItemContent.getFormerIdentifier());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getVolumeNumberLabel())) {
            existingItemContent.setVolumeNumberLabel(incomingItemContent.getVolumeNumberLabel());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getVendorLineItemIdentifier())) {
            existingItemContent.setVendorLineItemIdentifier(incomingItemContent.getVendorLineItemIdentifier());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getPurchaseOrderLineItemIdentifier())) {
            existingItemContent.setPurchaseOrderLineItemIdentifier(incomingItemContent.getPurchaseOrderLineItemIdentifier());
        }
        if (StringUtils.isNotBlank(incomingItemContent.getResourceIdentifier())) {
            existingItemContent.setResourceIdentifier(incomingItemContent.getResourceIdentifier());
        }
        if(itemIncoming.getContent() != null &&  itemIncoming.getContent().contains("<staffOnlyFlag>")){
            existingItemContent.setStaffOnlyFlag(incomingItemContent.isStaffOnlyFlag());
            this.setStaffOnly(incomingItemContent.isStaffOnlyFlag());
        }
        /*if (incomingItemContent.isStaffOnlyFlag() != existingItemContent.isStaffOnlyFlag()) {
            existingItemContent.setStaffOnlyFlag(incomingItemContent.isStaffOnlyFlag());
            this.setStaffOnly(incomingItemContent.isStaffOnlyFlag());

        }*/
        this.setContent(itemOlemlRecordProcessor.toXML(existingItemContent));
    }


    public void setDefaultField(String docField, String fieldValue) {

        org.kuali.ole.docstore.common.document.content.instance.Item itemPojo = (org.kuali.ole.docstore.common.document.content.instance.Item) getContentObject();


        if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_ITEM_BARCODE)) {
            if (itemPojo.getAccessInformation() == null) {
                AccessInformation accessInformation = new AccessInformation();
                itemPojo.setAccessInformation(accessInformation);
            }

            if (itemPojo.getAccessInformation().getBarcode() == null) {
                itemPojo.getAccessInformation().setBarcode(fieldValue);
            }

        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER)) {

            buildItemCallNumber(itemPojo);
            itemPojo.getCallNumber().setNumber(fieldValue);
            String callNumberType = itemPojo.getCallNumber().getShelvingScheme().getCodeValue();
            if(StringUtils.isEmpty(callNumberType)) {
                itemPojo.getCallNumber().getShelvingScheme().setCodeValue("NONE");
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER_TYPE) ) {
            buildItemCallNumber(itemPojo);
            if (itemPojo.getCallNumber().getShelvingScheme().getCodeValue() == null || !isDataMappingFlag()) {
                itemPojo.getCallNumber().getShelvingScheme().setCodeValue(fieldValue);
            }

        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
            buildItemCallNumber(itemPojo);
            if (itemPojo.getCallNumber().getPrefix() == null) {
                itemPojo.getCallNumber().setPrefix(fieldValue);
            }

        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COPY_NUMBER)) {
            if (itemPojo.getCopyNumber() == null) {
                itemPojo.setCopyNumber(fieldValue);
            }

        } else if (docField.equalsIgnoreCase(DESTINATION_ITEM_TYPE)) {
            ItemType itemType1 = new ItemType();
            itemType1.setCodeValue(fieldValue);
            itemPojo.setItemType(itemType1);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER)) {
            setHoldingsCallNumber(fieldValue);

        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_1)) {
            if (getLevel1Location() == null) {
                setLocationLevel1(fieldValue);
            }

        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_2)) {
            if (getLevel2Location() == null) {
                setLocationLevel2(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_3)) {
            if (getLevel3Location() == null) {
                setLocationLevel3(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_4)) {
            if (getLevel4Location() == null) {
                setLocationLevel4(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LOCATION_LEVEL_5)) {
            if (getLevel5Location() == null) {
                setLocationLevel5(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_TYPE)) {
            setHoldingsCallNumberType(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_1)) {
            setHoldingsLocationLevel1(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_2)) {
            setHoldingsLocationLevel2(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_3)) {
            setHoldingsLocationLevel3(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_4)) {
            setHoldingsLocationLevel4(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_5)) {
            setHoldingsLocationLevel5(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_COPY_NUMBER)) {
            setHoldingsCopyNumber(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_PREFIX)) {
            setHoldingsCallNumberPrefix(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_ITEM_STATUS)) {
            if(itemPojo.getItemStatus() == null){
                ItemStatus itemStatus1 = new ItemStatus();
                itemStatus1.setCodeValue(fieldValue);
                itemPojo.setItemStatus(itemStatus1);
            }
        } else if (docField.equalsIgnoreCase(ENUMERATION)) {
            if (itemPojo.getEnumeration() == null) {
                itemPojo.setEnumeration(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(CHRONOLOGY)) {
            if (itemPojo.getChronology() == null) {
                itemPojo.setChronology(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(VENDOR_LINE_ITEM_IDENTIFIER)) {
            if (itemPojo.getVendorLineItemIdentifier() == null) {
                itemPojo.setVendorLineItemIdentifier(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_CODE) || docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY) || docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_NOTE)) {
            DonorInfo donorInfo = null;
            if (itemPojo.getDonorInfo().size() > 0) {
                for (DonorInfo donorInfo1 : itemPojo.getDonorInfo()) {
                    buildDonorInfo(docField, fieldValue, itemPojo, donorInfo1, true);
                }
            } else {
                donorInfo = new DonorInfo();
                buildDonorInfo(docField, fieldValue, itemPojo, donorInfo, true);
            }
        }

    }


    public String getItemHoldingsDataMappingValue(Map<String, String> itemDocFields) {

        StringBuilder itemDataValue = new StringBuilder();


        if (itemDocFields.containsKey(DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER) && getHoldingsCallNumber() != null) {
            itemDataValue.append(getHoldingsCallNumber());
        }

        if (itemDocFields.containsKey(DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_PREFIX) && getHoldingsCallNumberPrefix() != null) {
            itemDataValue.append(getHoldingsCallNumberPrefix());
        }
        if (itemDocFields.containsKey(DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_TYPE) && getHoldingsCallNumberType() != null) {
            itemDataValue.append(getHoldingsCallNumberType());
        }

        if (itemDocFields.containsKey(DESTINATION_FIELD_ITEM_HOLDINGS_COPY_NUMBER) &&  getHoldingsCopyNumber() != null) {
            itemDataValue.append(getHoldingsCopyNumber());
        }

        if (itemDocFields.containsKey(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_1) && getHoldingsLocationLevel1() != null) {
            itemDataValue.append(getHoldingsLocationLevel1());
        }
        if (itemDocFields.containsKey(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_2) && getHoldingsLocationLevel2() != null) {
            itemDataValue.append(getHoldingsLocationLevel2());
        }
        if (itemDocFields.containsKey(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_3) && getHoldingsLocationLevel3() != null) {
            itemDataValue.append(getHoldingsLocationLevel3());
        }
        if (itemDocFields.containsKey(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_4) && getHoldingsLocationLevel4() != null) {
            itemDataValue.append(getHoldingsLocationLevel4());
        }
        if (itemDocFields.containsKey(DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_5) && getHoldingsLocationLevel5() != null) {
            itemDataValue.append(getHoldingsLocationLevel5());

        }
        return itemDataValue.toString();
    }


    public String getHoldingsCallNumber() {
        return holdingsCallNumber;
    }

    public void setHoldingsCallNumber(String holdingsCallNumber) {
        this.holdingsCallNumber = holdingsCallNumber;
    }

    public String getHoldingsCallNumberType() {
        return holdingsCallNumberType;
    }

    public void setHoldingsCallNumberType(String holdingsCallNumberType) {
        this.holdingsCallNumberType = holdingsCallNumberType;
    }

    public String getHoldingsCopyNumber() {
        return holdingsCopyNumber;
    }

    public void setHoldingsCopyNumber(String holdingsCopyNumber) {
        this.holdingsCopyNumber = holdingsCopyNumber;
    }

    public String getHoldingsCallNumberPrefix() {
        return holdingsCallNumberPrefix;
    }

    public void setHoldingsCallNumberPrefix(String holdingsCallNumberPrefix) {
        this.holdingsCallNumberPrefix = holdingsCallNumberPrefix;
    }

    public String getHoldingsLocationLevel1() {
        return holdingsLocationLevel1;
    }

    public void setHoldingsLocationLevel1(String holdingsLocationLevel1) {
        this.holdingsLocationLevel1 = holdingsLocationLevel1;
    }

    public String getHoldingsLocationLevel2() {
        return holdingsLocationLevel2;
    }

    public void setHoldingsLocationLevel2(String holdingsLocationLevel2) {
        this.holdingsLocationLevel2 = holdingsLocationLevel2;
    }

    public String getHoldingsLocationLevel3() {
        return holdingsLocationLevel3;
    }

    public void setHoldingsLocationLevel3(String holdingsLocationLevel3) {
        this.holdingsLocationLevel3 = holdingsLocationLevel3;
    }

    public String getHoldingsLocationLevel4() {
        return holdingsLocationLevel4;
    }

    public void setHoldingsLocationLevel4(String holdingsLocationLevel4) {
        this.holdingsLocationLevel4 = holdingsLocationLevel4;
    }

    public String getHoldingsLocationLevel5() {
        return holdingsLocationLevel5;
    }

    public void setHoldingsLocationLevel5(String holdingsLocationLevel5) {
        this.holdingsLocationLevel5 = holdingsLocationLevel5;
    }

    public void buildLocationLevels(org.kuali.ole.docstore.common.document.content.instance.Item item) {
        if (item != null) {
            if (item.getLocation() != null) {
                LocationLevel locationLevel = item.getLocation().getLocationLevel();
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
        if (locationLevel.getLevel().equalsIgnoreCase(level)) return locationLevel.getName();
        return getLocationLevelName(locationLevel.getLocationLevel(), level);
    }
}
