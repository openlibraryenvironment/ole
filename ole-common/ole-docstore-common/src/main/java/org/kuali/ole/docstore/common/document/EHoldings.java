package org.kuali.ole.docstore.common.document;


import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for eHoldings complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="eHoldings">
 *   &lt;complexContent>
 *     &lt;extension base="{}holdings">
 *       &lt;sequence>
 *         &lt;element name="accessStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eHoldings", propOrder = {
        "accessStatus"
})

@XmlRootElement(name = "holdingsDoc")
public class EHoldings
        extends Holdings {
    public static final Logger LOG = LoggerFactory.getLogger(EHoldings.class);
    public static final String DESTINATION_FIELD_LINK_TEXT = "Link Text";
    public static final String DESTINATION_FIELD_LINK_URL = "URL";
    public static final String DESTINATION_FIELD_PERSISTENTLINK = "Persistent Link";
    public static final String DESTINATION_FIELD_PUBLIC_DISPLAY_NOTE = "Public Display Note";
    public static final String DESTINATION_FIELD_COVERAGE_START_DATE = "Coverage Start Date";
    public static final String DESTINATION_FIELD_COVERAGE_START_ISSUE = "Coverage Start Issue";
    public static final String DESTINATION_FIELD_COVERAGE_START_VOLUME = "Coverage Start Volume";
    public static final String DESTINATION_FIELD_COVERAGE_END_DATE = "Coverage End Date";
    public static final String DESTINATION_FIELD_COVERAGE_END_ISSUE = "Coverage End Issue";
    public static final String DESTINATION_FIELD_COVERAGE_END_VOLUME = "Coverage End Volume";
    public static final String DESTINATION_FIELD_STATISTICAL_CODE = "Statistical Code";
    public static final String DESTINATION_FIELD_ACCESS_STATUS_CODE = "Access Status";
    public static final String DESTINATION_FIELD_ERESOURCE_NAME = "EResource Name";
    public static final String DESTINATION_FIELD_ERESOURCE_ID = "EResource Id";
    public static final String DESTINATION_FIELD_GOKB_ID = "Gokb Id";
    public static final String DESTINATION_FIELD_IMPRINT = "Imprint";
    public static final String DESTINATION_FIELD_PUBLISHER = "Publisher";
    public static final String PUBLIC="public";
    public static final String ACTIVE="active";
    public static final String INACTIVE="inActive";
    public static final String ELECTRONIC="electronic";

    public static final String ACCESS_STATUS = "ACCESSSTATUS";
    public static final String URL = "URL";
    public static final String PERSISTENT_LINK = "PERSISTENTLINK";
    public static final String LINK_TEXT = "LINKTEXT";
    public static final String PUBLIC_DISPLAY_NOTE = "PUBLICDISPLAYNOTE";
    public static final String COVERAGE_START_DATE = "COVERAGESTARTDATE";
    public static final String COVERAGE_END_DATE = "COVERAGEENDDATE";
    public static final String STATISTICAL_CODE = "STATISTICALCODE";
    public static final String PLATFORM = "PLATFORM";

    protected String accessStatus;


    public EHoldings() {
        holdingsType = ELECTRONIC;
        category = DocCategory.WORK.getCode();
        type = DocType.EHOLDINGS.getCode();
        format = DocFormat.OLEML.getCode();
    }

    /**
     * Gets the value of the accessStatus property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAccessStatus() {
        return accessStatus;
    }

    /**
     * Sets the value of the accessStatus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAccessStatus(String value) {
        this.accessStatus = value;
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        EHoldings holdings = (EHoldings) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(EHoldings.class);
            synchronized (jaxbMarshaller) {
            jaxbMarshaller.marshal(holdings, sw);
            }
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {
        EHoldings holdings = new EHoldings();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(EHoldings.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                holdings = unmarshaller.unmarshal(new StreamSource(input), EHoldings.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return holdings;
    }


    @Override
    public Object deserializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void serializeContent() {

        if (contentObject == null) {
            contentObject = getContentObject();
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
        buildExtentOfOwnerShipAndNoteAndLink(oleHoldings);

        if (docField.equalsIgnoreCase(DESTINATION_FIELD_CALL_NUMBER)) {
            oleHoldings.getCallNumber().setNumber(fieldValue);
            oleHoldings.getCallNumber().getShelvingScheme().setCodeValue("NONE");
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE)) {
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
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LINK_TEXT)) {
            oleHoldings.getLink().get(0).setText(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_LINK_URL)) {
            oleHoldings.getLink().get(0).setUrl(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_PERSISTENTLINK)) {
            oleHoldings.setLocalPersistentLink(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_START_DATE)) {
            oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().get(0).setCoverageStartDate(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_START_VOLUME)) {
            oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().get(0).setCoverageStartVolume(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_START_ISSUE)) {
            oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().get(0).setCoverageStartIssue(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_END_DATE)) {
            oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().get(0).setCoverageEndDate(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_END_ISSUE)) {
            oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().get(0).setCoverageEndIssue(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_END_VOLUME)) {
            oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().get(0).setCoverageEndVolume(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_STATISTICAL_CODE)) {
            oleHoldings.getStatisticalSearchingCode().setCodeValue(fieldValue);
            oleHoldings.getStatisticalSearchingCode().setFullValue(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_PUBLIC_DISPLAY_NOTE)) {
            oleHoldings.getNote().get(0).setValue(fieldValue);
            oleHoldings.getNote().get(0).setType(PUBLIC);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_ACCESS_STATUS_CODE)) {
            if (fieldValue.toLowerCase().equals(ACTIVE)) {
                oleHoldings.setAccessStatus(ACTIVE);
            } else if (fieldValue.equalsIgnoreCase(INACTIVE)) {
                oleHoldings.setAccessStatus(INACTIVE);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_COPY_NUMBER)) {
            oleHoldings.setCopyNumber(fieldValue);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_PLATFORM)) {
            Platform platform = new Platform();
            platform.setPlatformName(fieldValue);
            oleHoldings.setPlatform(platform);
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_CODE) || docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY) || docField.equalsIgnoreCase(DESTINATION_FIELD_DONOR_NOTE)) {
            DonorInfo donorInfo = null;
            if (oleHoldings.getDonorInfo().size() > 0) {
                for (DonorInfo existingDonorInfo : oleHoldings.getDonorInfo()) {
                    buildDonorInfo(docField, fieldValue, oleHoldings, existingDonorInfo, false);
                }
            } else {
                donorInfo = new DonorInfo();
                buildDonorInfo(docField, fieldValue, oleHoldings, donorInfo, false);
            }
        }else if ( docField.equalsIgnoreCase(DESTINATION_FIELD_ERESOURCE_ID)) {
            oleHoldings.setEResourceId(fieldValue);
        }
        else if (docField.equalsIgnoreCase(DESTINATION_FIELD_GOKB_ID)) {
            if(StringUtils.isNotEmpty(fieldValue)) {
                oleHoldings.setGokbIdentifier(Integer.parseInt(fieldValue));
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_IMPRINT)) {
            if (StringUtils.isNotEmpty(fieldValue)) {
                oleHoldings.setImprint(fieldValue);
            }
        } else if (docField.equalsIgnoreCase(DESTINATION_FIELD_PUBLISHER)) {
            if (StringUtils.isNotEmpty(fieldValue)) {
                oleHoldings.setPublisher(fieldValue);
            }
        }
    }

    private void buildDonorInfo(String docField, String fieldValue, OleHoldings oleHoldings, DonorInfo donorInfo, boolean isDefault) {
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
        if (oleHoldings.getDonorInfo() == null || oleHoldings.getDonorInfo().size() == 0) {
            oleHoldings.getDonorInfo().add(donorInfo);
        }
    }

    private void buildExtentOfOwnerShipAndNoteAndLink(OleHoldings oleHoldings) {
        if (oleHoldings.getExtentOfOwnership().size() == 0) {
            ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();
            Coverages coverages = new Coverages();
            Coverage coverage = new Coverage();
            coverages.getCoverage().add(coverage);
            extentOfOwnership.setCoverages(coverages);
            oleHoldings.getExtentOfOwnership().add(extentOfOwnership);
        }
        if (oleHoldings.getNote().size() == 0) {
            Note note = new Note();
            oleHoldings.getNote().add(note);
        }
        if (oleHoldings.getStatisticalSearchingCode() == null) {
            StatisticalSearchingCode statisticalSearchingCode = new StatisticalSearchingCode();
            oleHoldings.setStatisticalSearchingCode(statisticalSearchingCode);
        }

        if(oleHoldings.getLink().size() == 0) {
          oleHoldings.getLink().add(new Link());
        }
    }
}