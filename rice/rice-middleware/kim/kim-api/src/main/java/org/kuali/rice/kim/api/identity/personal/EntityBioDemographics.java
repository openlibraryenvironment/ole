/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kim.api.identity.personal;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@XmlRootElement(name = EntityBioDemographics.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityBioDemographics.Constants.TYPE_NAME, propOrder = {
    EntityBioDemographics.Elements.ENTITY_ID,
    EntityBioDemographics.Elements.DECEASED_DATE,
    EntityBioDemographics.Elements.BIRTH_DATE,
    EntityBioDemographics.Elements.AGE,
    EntityBioDemographics.Elements.GENDER_CODE,
    EntityBioDemographics.Elements.GENDER_CHANGE_CODE,
    EntityBioDemographics.Elements.MARITAL_STATUS_CODE,
    EntityBioDemographics.Elements.PRIMARY_LANGUAGE_CODE,
    EntityBioDemographics.Elements.SECONDARY_LANGUAGE_CODE,
    EntityBioDemographics.Elements.BIRTH_COUNTRY,
    EntityBioDemographics.Elements.BIRTH_STATE_PROVINCE_CODE,
    EntityBioDemographics.Elements.BIRTH_CITY,
    EntityBioDemographics.Elements.GEOGRAPHIC_ORIGIN,
    EntityBioDemographics.Elements.BIRTH_DATE_UNMASKED,
    EntityBioDemographics.Elements.GENDER_CODE_UNMASKED,
    EntityBioDemographics.Elements.GENDER_CHANGE_CODE_UNMASKED,
    EntityBioDemographics.Elements.MARITAL_STATUS_CODE_UNMASKED,
    EntityBioDemographics.Elements.PRIMARY_LANGUAGE_CODE_UNMASKED,
    EntityBioDemographics.Elements.SECONDARY_LANGUAGE_CODE_UNMASKED,
    EntityBioDemographics.Elements.BIRTH_COUNTRY_UNMASKED,
    EntityBioDemographics.Elements.BIRTH_STATE_PROVINCE_CODE_UNMASKED,
    EntityBioDemographics.Elements.BIRTH_CITY_UNMASKED,
    EntityBioDemographics.Elements.GEOGRAPHIC_ORIGIN_UNMASKED,
    EntityBioDemographics.Elements.NOTE_MESSAGE,
    EntityBioDemographics.Elements.SUPPRESS_PERSONAL,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityBioDemographics extends AbstractDataTransferObject
    implements EntityBioDemographicsContract
{
    private static final Logger LOG = Logger.getLogger(EntityBioDemographics.class);

    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;
    @XmlElement(name = Elements.DECEASED_DATE, required = false)
    private final String deceasedDate;
    @XmlElement(name = Elements.BIRTH_DATE, required = false)
    private final String birthDate;
    @XmlElement(name = Elements.GENDER_CODE, required = false)
    private final String genderCode;
    @XmlElement(name = Elements.GENDER_CHANGE_CODE, required = false)
    private final String genderChangeCode;
    @XmlElement(name = Elements.MARITAL_STATUS_CODE, required = false)
    private final String maritalStatusCode;
    @XmlElement(name = Elements.PRIMARY_LANGUAGE_CODE, required = false)
    private final String primaryLanguageCode;
    @XmlElement(name = Elements.SECONDARY_LANGUAGE_CODE, required = false)
    private final String secondaryLanguageCode;
    @XmlElement(name = Elements.BIRTH_COUNTRY, required = false)
    private final String birthCountry;
    @XmlElement(name = Elements.BIRTH_STATE_PROVINCE_CODE, required = false)
    private final String birthStateProvinceCode;
    @XmlElement(name = Elements.BIRTH_CITY, required = false)
    private final String birthCity;
    @XmlElement(name = Elements.GEOGRAPHIC_ORIGIN, required = false)
    private final String geographicOrigin;

    @XmlElement(name = Elements.BIRTH_DATE_UNMASKED, required = false)
    private final String birthDateUnmasked;
    @XmlElement(name = Elements.GENDER_CODE_UNMASKED, required = false)
    private final String genderCodeUnmasked;
    @XmlElement(name = Elements.GENDER_CHANGE_CODE_UNMASKED, required = false)
    private final String genderChangeCodeUnmasked;
    @XmlElement(name = Elements.MARITAL_STATUS_CODE_UNMASKED, required = false)
    private final String maritalStatusCodeUnmasked;
    @XmlElement(name = Elements.PRIMARY_LANGUAGE_CODE_UNMASKED, required = false)
    private final String primaryLanguageCodeUnmasked;
    @XmlElement(name = Elements.SECONDARY_LANGUAGE_CODE_UNMASKED, required = false)
    private final String secondaryLanguageCodeUnmasked;
    @XmlElement(name = Elements.BIRTH_COUNTRY_UNMASKED, required = false)
    private final String birthCountryUnmasked;
    @XmlElement(name = Elements.BIRTH_STATE_PROVINCE_CODE_UNMASKED, required = false)
    private final String birthStateProvinceCodeUnmasked;
    @XmlElement(name = Elements.BIRTH_CITY_UNMASKED, required = false)
    private final String birthCityUnmasked;
    @XmlElement(name = Elements.GEOGRAPHIC_ORIGIN_UNMASKED, required = false)
    private final String geographicOriginUnmasked;

    @XmlElement(name = Elements.NOTE_MESSAGE, required = false)
    private final String noteMessage;
    @XmlElement(name = Elements.SUPPRESS_PERSONAL, required = false)
    private final boolean suppressPersonal;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private EntityBioDemographics() {
        this.entityId = null;
        this.deceasedDate = null;
        this.birthDate = null;
        this.genderCode = null;
        this.genderChangeCode = null;
        this.maritalStatusCode = null;
        this.primaryLanguageCode = null;
        this.secondaryLanguageCode = null;
        this.birthCountry = null;
        this.birthStateProvinceCode = null;
        this.birthCity = null;
        this.geographicOrigin = null;

        this.birthDateUnmasked = null;
        this.genderCodeUnmasked = null;
        this.genderChangeCodeUnmasked = null;
        this.maritalStatusCodeUnmasked = null;
        this.primaryLanguageCodeUnmasked = null;
        this.secondaryLanguageCodeUnmasked = null;
        this.birthCountryUnmasked = null;
        this.birthStateProvinceCodeUnmasked = null;
        this.birthCityUnmasked = null;
        this.geographicOriginUnmasked = null;

        this.noteMessage = null;
        this.suppressPersonal = false;
        this.versionNumber = null;
        this.objectId = null;
    }

    private EntityBioDemographics(Builder builder) {
        this.entityId = builder.getEntityId();
        this.deceasedDate = builder.getDeceasedDate();
        this.birthDate = builder.getBirthDate();
        this.genderCode = builder.getGenderCode();
        this.genderChangeCode = builder.getGenderChangeCode();
        this.maritalStatusCode = builder.getMaritalStatusCode();
        this.primaryLanguageCode = builder.getPrimaryLanguageCode();
        this.secondaryLanguageCode = builder.getSecondaryLanguageCode();
        this.birthCountry = builder.getBirthCountry();
        this.birthStateProvinceCode = builder.getBirthStateProvinceCode();
        this.birthCity = builder.getBirthCity();
        this.geographicOrigin = builder.getGeographicOrigin();

        this.birthDateUnmasked = builder.getBirthDateUnmasked();
        this.genderCodeUnmasked = builder.getGenderCodeUnmasked();
        this.genderChangeCodeUnmasked = builder.getGenderChangeCodeUnmasked();
        this.maritalStatusCodeUnmasked = builder.getMaritalStatusCodeUnmasked();
        this.primaryLanguageCodeUnmasked = builder.getPrimaryLanguageCodeUnmasked();
        this.secondaryLanguageCodeUnmasked = builder.getSecondaryLanguageCodeUnmasked();
        this.birthCountryUnmasked = builder.getBirthCountryUnmasked();
        this.birthStateProvinceCodeUnmasked = builder.getBirthStateProvinceCodeUnmasked();
        this.birthCityUnmasked = builder.getBirthCityUnmasked();
        this.geographicOriginUnmasked = builder.getGeographicOriginUnmasked();

        this.noteMessage = builder.getNoteMessage();
        this.suppressPersonal = builder.isSuppressPersonal();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public String getEntityId() {
        return this.entityId;
    }

    @Override
    public String getDeceasedDate() {
        return this.deceasedDate;
    }

    @Override
    public String getBirthDate() {
        return this.birthDate;
    }

    @Override
    @XmlElement(name = Elements.AGE, required = true)
    public Integer getAge() {
        return calculateAge(this.birthDate, this.deceasedDate, isSuppressPersonal());
    }

    @Override
    public String getGenderCode() {
        return this.genderCode;
    }

    @Override
    public String getGenderChangeCode() {
        return this.genderChangeCode;
    }

    @Override
    public String getMaritalStatusCode() {
        return this.maritalStatusCode;
    }

    @Override
    public String getPrimaryLanguageCode() {
        return this.primaryLanguageCode;
    }

    @Override
    public String getSecondaryLanguageCode() {
        return this.secondaryLanguageCode;
    }

    @Override
    public String getBirthCountry() {
        return this.birthCountry;
    }

    @Override
    public String getBirthStateProvinceCode() {
        return this.birthStateProvinceCode;
    }

    @Override
    public String getBirthCity() {
        return this.birthCity;
    }

    @Override
    public String getGeographicOrigin() {
        return this.geographicOrigin;
    }

    @Override
    public String getBirthDateUnmasked() {
        return this.birthDateUnmasked;
    }

    @Override
    public String getGenderCodeUnmasked() {
        return this.genderCodeUnmasked;
    }

    @Override
    public String getGenderChangeCodeUnmasked() {
        return this.genderChangeCodeUnmasked;
    }
    
    @Override
    public String getMaritalStatusCodeUnmasked() {
        return this.maritalStatusCodeUnmasked;
    }

    @Override
    public String getPrimaryLanguageCodeUnmasked() {
        return this.primaryLanguageCodeUnmasked;
    }

    @Override
    public String getSecondaryLanguageCodeUnmasked() {
        return this.secondaryLanguageCodeUnmasked;
    }

    @Override
    public String getBirthCountryUnmasked() {
        return this.birthCountryUnmasked;
    }

    @Override
    public String getBirthStateProvinceCodeUnmasked() {
        return this.birthStateProvinceCodeUnmasked;
    }

    @Override
    public String getBirthCityUnmasked() {
        return this.birthCityUnmasked;
    }

    @Override
    public String getGeographicOriginUnmasked() {
        return this.geographicOriginUnmasked;
    }

    @Override
    public String getNoteMessage() {
        return this.noteMessage;
    }

    @Override
    public boolean isSuppressPersonal() {
        return this.suppressPersonal;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    /**
     * Helper to parse the birth date for age calculation
     * @param birthDate the birth date in EntityBioDemographicsContract BIRTH_DATE_FORMAT format
     * @param deceasedDate the deceased date in EntityBioDemographicsContract DECEASED_DATE_FORMAT format
     * @param suppressPersonal whether personal information is being suppressed
     * @return the age in years or null if unavailable, suppressed, or an error occurs during calculation
     */
    private static Integer calculateAge(String birthDate, String deceasedDate, boolean suppressPersonal) {
        if (birthDate != null && ! suppressPersonal) {
            Date parsedBirthDate;
            try {
                parsedBirthDate = new SimpleDateFormat(BIRTH_DATE_FORMAT).parse(birthDate);
            } catch (ParseException pe) {
                LOG.error("Error parsing EntityBioDemographics birth date: '" + birthDate + "'", pe);
                return null;
            }
            DateTime endDate;
            if (deceasedDate != null) {
                try {
                   endDate = new DateTime(new SimpleDateFormat(BIRTH_DATE_FORMAT).parse(deceasedDate));
                } catch (ParseException pe) {
                    LOG.error("Error parsing EntityBioDemographics deceased date: '" + deceasedDate+ "'", pe);
                    return null;
                }
            } else {
                endDate = new DateTime();
            }
            return Years.yearsBetween(new DateTime(parsedBirthDate), endDate).getYears();
        }
        return null;
    }

    /**
     * A builder which can be used to construct {@link EntityBioDemographics} instances.  Enforces the constraints of the {@link EntityBioDemographicsContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityBioDemographicsContract
    {

        private String entityId;
        private String deceasedDate;
        private String birthDate;
        private String genderCode;
        private String maritalStatusCode;
        private String primaryLanguageCode;
        private String secondaryLanguageCode;
        private String birthCountry;
        private String birthStateProvinceCode;
        private String birthCity;
        private String geographicOrigin;
        private String genderChangeCode;
        private String noteMessage;
        private boolean suppressPersonal;
        private Long versionNumber;
        private String objectId;

        private Builder(String entityId, String genderCode) {
            setEntityId(entityId);
            setGenderCode(genderCode);
        }

        public static Builder create(String entityId, String genderCode) {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder(entityId, genderCode);
        }

        public static Builder create(EntityBioDemographicsContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getEntityId(), contract.getGenderCode());
            builder.setDeceasedDate(contract.getDeceasedDate());
            builder.setBirthDate(contract.getBirthDate());
            builder.setMaritalStatusCode(contract.getMaritalStatusCode());
            builder.setPrimaryLanguageCode(contract.getPrimaryLanguageCode());
            builder.setSecondaryLanguageCode(contract.getSecondaryLanguageCode());
            builder.setBirthCountry(contract.getBirthCountry());
            builder.setBirthStateProvinceCode(contract.getBirthStateProvinceCode());
            builder.setBirthCity(contract.getBirthCity());
            builder.setGeographicOrigin(contract.getGeographicOrigin());
            builder.setGenderChangeCode(contract.getGenderChangeCode());
            builder.setNoteMessage(contract.getNoteMessage());
            builder.setSuppressPersonal(contract.isSuppressPersonal());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public EntityBioDemographics build() {
            return new EntityBioDemographics(this);
        }

        @Override
        public String getEntityId() {
            return this.entityId;
        }

        @Override
        public String getDeceasedDate() {
            return this.deceasedDate;
        }

        @Override
        public String getBirthDate() {
            if (isSuppressPersonal()) {
                return KimConstants.RESTRICTED_DATA_MASK;
            }
            return this.birthDate;
        }

        @Override
        public Integer getAge() {
            return calculateAge(this.birthDate, this.deceasedDate, isSuppressPersonal());
        }

        @Override
        public String getGenderCode() {
            if (isSuppressPersonal()) {
                return KimConstants.RESTRICTED_DATA_MASK;
            }
            return this.genderCode;
        }

        @Override
        public String getGenderChangeCode() {
            if (isSuppressPersonal()) {
                return KimConstants.RESTRICTED_DATA_MASK;
            }
            return this.genderChangeCode;
        }

        @Override
        public String getMaritalStatusCode() {
            if (isSuppressPersonal()) {
                return KimConstants.RESTRICTED_DATA_MASK;
            }
            return this.maritalStatusCode;
        }

        @Override
        public String getPrimaryLanguageCode() {
            if (isSuppressPersonal()) {
                return KimConstants.RESTRICTED_DATA_MASK;
            }
            return this.primaryLanguageCode;
        }

        @Override
        public String getSecondaryLanguageCode() {
            if (isSuppressPersonal()) {
                return KimConstants.RESTRICTED_DATA_MASK;
            }
            return this.secondaryLanguageCode;
        }

        @Override
        public String getBirthCountry() {
            if (isSuppressPersonal()) {
                return KimConstants.RESTRICTED_DATA_MASK;
            }
            return this.birthCountry;
        }

        @Override
        public String getBirthStateProvinceCode() {
            if (isSuppressPersonal()) {
                return KimConstants.RESTRICTED_DATA_MASK;
            }
            return this.birthStateProvinceCode;
        }

        @Override
        public String getBirthCity() {
            if (isSuppressPersonal()) {
                return KimConstants.RESTRICTED_DATA_MASK;
            }
            return this.birthCity;
        }

        @Override
        public String getGeographicOrigin() {
            if (isSuppressPersonal()) {
                return KimConstants.RESTRICTED_DATA_MASK;
            }
            return this.geographicOrigin;
        }

        @Override
        public String getBirthDateUnmasked() {
            return this.birthDate;
        }

        @Override
        public String getGenderCodeUnmasked() {
            return this.genderCode;
        }

        @Override
        public String getGenderChangeCodeUnmasked() {
            return this.genderChangeCode;
        }

        @Override
        public String getMaritalStatusCodeUnmasked() {
            return this.maritalStatusCode;
        }

        @Override
        public String getPrimaryLanguageCodeUnmasked() {
            return this.primaryLanguageCode;
        }

        @Override
        public String getSecondaryLanguageCodeUnmasked() {
            return this.secondaryLanguageCode;
        }

        @Override
        public String getBirthCountryUnmasked() {
            return this.birthCountry;
        }

        @Override
        public String getBirthStateProvinceCodeUnmasked() {
            return this.birthStateProvinceCode;
        }

        @Override
        public String getBirthCityUnmasked() {
            return this.birthCity;
        }

        @Override
        public String getGeographicOriginUnmasked() {
            return this.geographicOrigin;
        }

        @Override
        public String getNoteMessage() {
            return this.noteMessage;
        }

        @Override
        public boolean isSuppressPersonal() {
            return this.suppressPersonal;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        public void setEntityId(String entityId) {
            if (StringUtils.isEmpty(entityId)) {
                throw new IllegalArgumentException("id is empty");
            }
            this.entityId = entityId;
        }

        public void setDeceasedDate(String deceasedDate) {
            if (deceasedDate != null) {
                SimpleDateFormat format = new SimpleDateFormat(DECEASED_DATE_FORMAT);
                try{
                    format.parse(deceasedDate);
                    this.deceasedDate = deceasedDate;
                }
                catch(ParseException e) {
                    throw new IllegalArgumentException("deceasedDate is not of the format 'yyyy-MM-DD'");
                }
            }
        }

        public void setBirthDate(String birthDate) {
            if (birthDate != null) {
                SimpleDateFormat format = new SimpleDateFormat(BIRTH_DATE_FORMAT);
                try{
                    format.parse(birthDate);
                    this.birthDate = birthDate;
                }
                catch(ParseException e) {
                    throw new IllegalArgumentException("birthDate is not of the format 'yyyy-MM-DD'");
                }
            }
        }

        public void setDeceasedDate(Date deceasedDate) {
            this.deceasedDate = new SimpleDateFormat(DECEASED_DATE_FORMAT).format(deceasedDate);
        }

        public void setBirthDate(Date birthDate) {
            this.birthDate = new SimpleDateFormat(BIRTH_DATE_FORMAT).format(birthDate);
        }

        public void setGenderCode(String genderCode) {
            if (StringUtils.isEmpty(genderCode)) {
                throw new IllegalArgumentException("genderCode is empty");
            }
            this.genderCode = genderCode;
        }

        public void setGenderChangeCode(String genderChangeCode) {
            this.genderChangeCode = genderChangeCode;
        }

        public void setMaritalStatusCode(String maritalStatusCode) {
            this.maritalStatusCode = maritalStatusCode;
        }

        public void setPrimaryLanguageCode(String primaryLanguageCode) {
            this.primaryLanguageCode = primaryLanguageCode;
        }

        public void setSecondaryLanguageCode(String secondaryLanguageCode) {
            this.secondaryLanguageCode = secondaryLanguageCode;
        }

        public void setBirthCountry(String birthCountry) {
            this.birthCountry = birthCountry;
        }

        public void setBirthStateProvinceCode(String birthStateProvinceCode) {
            this.birthStateProvinceCode = birthStateProvinceCode;
        }

        public void setBirthCity(String birthCity) {
            this.birthCity = birthCity;
        }

        public void setGeographicOrigin(String geographicOrigin) {
            this.geographicOrigin = geographicOrigin;
        }

        private void setNoteMessage(String noteMessage) {
            this.noteMessage = noteMessage;
        }

        private void setSuppressPersonal(boolean suppressPersonal) {
            this.suppressPersonal = suppressPersonal;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "entityBioDemographics";
        final static String TYPE_NAME = "EntityBioDemographicsType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ENTITY_ID = "entityId";
        final static String DECEASED_DATE = "deceasedDate";
        final static String BIRTH_DATE = "birthDate";
        final static String AGE = "age";
        final static String GENDER_CODE = "genderCode";
        final static String MARITAL_STATUS_CODE = "maritalStatusCode";
        final static String PRIMARY_LANGUAGE_CODE = "primaryLanguageCode";
        final static String SECONDARY_LANGUAGE_CODE = "secondaryLanguageCode";
        final static String BIRTH_COUNTRY = "birthCountry";
        final static String BIRTH_STATE_PROVINCE_CODE = "birthStateProvinceCode";
        final static String BIRTH_CITY = "birthCity";
        final static String GEOGRAPHIC_ORIGIN = "geographicOrigin";
        final static String BIRTH_DATE_UNMASKED = "birthDateUnmasked";
        final static String GENDER_CODE_UNMASKED = "genderCodeUnmasked";
        final static String MARITAL_STATUS_CODE_UNMASKED = "maritalStatusCodeUnmasked";
        final static String PRIMARY_LANGUAGE_CODE_UNMASKED = "primaryLanguageCodeUnmasked";
        final static String SECONDARY_LANGUAGE_CODE_UNMASKED = "secondaryLanguageCodeUnmasked";
        final static String BIRTH_COUNTRY_UNMASKED = "birthCountryUnmasked";
        final static String BIRTH_STATE_PROVINCE_CODE_UNMASKED = "birthStateProvinceCodeUnmasked";
        final static String BIRTH_CITY_UNMASKED = "birthCityUnmasked";
        final static String GEOGRAPHIC_ORIGIN_UNMASKED = "geographicOriginUnmasked";
        final static String GENDER_CHANGE_CODE = "genderChangeCode";
        final static String GENDER_CHANGE_CODE_UNMASKED = "genderChangeCodeUnmasked";
        final static String NOTE_MESSAGE = "noteMessage";
        final static String SUPPRESS_PERSONAL = "suppressPersonal";

    }

}