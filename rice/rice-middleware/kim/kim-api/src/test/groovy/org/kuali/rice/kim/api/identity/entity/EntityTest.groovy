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
package org.kuali.rice.kim.api.identity.entity

import org.junit.Test
import org.junit.Assert
import org.kuali.rice.kim.api.test.JAXBAssert

import org.kuali.rice.kim.api.identity.principal.Principal

import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier

import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationContract

import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierTest
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationTest
import org.kuali.rice.kim.api.identity.principal.PrincipalTest

import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoTest
import org.kuali.rice.kim.api.identity.name.EntityName
import org.kuali.rice.kim.api.identity.name.EntityNameTest
import org.kuali.rice.kim.api.identity.employment.EntityEmploymentTest
import org.kuali.rice.kim.api.identity.employment.EntityEmployment
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferencesTest
import org.kuali.rice.kim.api.identity.personal.EntityBioDemographicsTest
import org.kuali.rice.kim.api.identity.personal.EntityBioDemographics
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenship
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipTest
import org.kuali.rice.kim.api.identity.personal.EntityEthnicity
import org.kuali.rice.kim.api.identity.personal.EntityEthnicityTest
import org.kuali.rice.kim.api.identity.residency.EntityResidency
import org.kuali.rice.kim.api.residency.EntityResidencyTest
import org.kuali.rice.kim.api.identity.visa.EntityVisa
import org.kuali.rice.kim.api.identity.visa.EntityVisaTest
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

import org.kuali.rice.kim.api.identity.name.EntityNameContract
import org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo

class EntityTest {

    private static final String ENTITY_ID = "190192";
    private static final Principal PRINCIPAL = PrincipalTest.create()
    private static final EntityTypeContactInfo ENTITY_TYPE = EntityTypeContactInfoTest.create()
    private static final EntityExternalIdentifier EXTERNAL_ID = EntityExternalIdentifierTest.create()
    private static final EntityAffiliation AFFILIATION = EntityAffiliationTest.create()
    private static final EntityName NAME = EntityNameTest.create()
    private static final EntityEmployment EMPLOYMENT = EntityEmploymentTest.create()
    private static final EntityPrivacyPreferences PRIVACY = EntityPrivacyPreferencesTest.create()
    private static final EntityBioDemographics BIO = EntityBioDemographicsTest.create()
    private static final EntityCitizenship CITIZENSHIP = EntityCitizenshipTest.create()
    private static final EntityEthnicity ETHNICITY = EntityEthnicityTest.create()
    private static final EntityResidency RESIDENCY = EntityResidencyTest.create()
    private static final EntityVisa VISA = EntityVisaTest.create()
    private static final String ACTIVE = "true"
    private static final Long VERSION_NUMBER = new Integer(1);
    private static final String OBJECT_ID = UUID.randomUUID();


    private static final String XML = """
            <entity xmlns="http://rice.kuali.org/kim/v2_0">
                <id>${ENTITY_ID}</id>
                <principals>
                    <principal>
                        <principalId>${PRINCIPAL.principalId}</principalId>
                        <principalName>${PRINCIPAL.principalName}</principalName>
                        <entityId>${PRINCIPAL.entityId}</entityId>
                        <active>${PRINCIPAL.active}</active>
                        <versionNumber>${PRINCIPAL.versionNumber}</versionNumber>
                        <objectId>${PRINCIPAL.objectId}</objectId>
                    </principal>
                </principals>
                <entityTypeContactInfos>
                    <entityTypeContactInfo>
                        <entityId>${ENTITY_TYPE.entityId}</entityId>
                        <entityTypeCode>${ENTITY_TYPE.entityTypeCode}</entityTypeCode>
                        <entityType>
                            <code>${ENTITY_TYPE.entityType.code}</code>
                            <name>${ENTITY_TYPE.entityType.name}</name>
                            <sortCode>${ENTITY_TYPE.entityType.sortCode}</sortCode>
                            <active>${ENTITY_TYPE.entityType.active}</active>
                            <versionNumber>${ENTITY_TYPE.entityType.versionNumber}</versionNumber>
                            <objectId>${ENTITY_TYPE.entityType.objectId}</objectId>
                        </entityType>
                        <addresses>
                            <address>
                                <id>${ENTITY_TYPE.addresses.get(0).id}</id>
                                <entityTypeCode>${ENTITY_TYPE.addresses.get(0).entityTypeCode}</entityTypeCode>
                                <entityId>${ENTITY_TYPE.addresses.get(0).entityId}</entityId>
                                <addressType>
                                    <code>${ENTITY_TYPE.addresses.get(0).addressType.code}</code>
                                    <name>${ENTITY_TYPE.addresses.get(0).addressType.name}</name>
                                    <sortCode>${ENTITY_TYPE.addresses.get(0).addressType.sortCode}</sortCode>
                                    <active>${ENTITY_TYPE.addresses.get(0).addressType.active}</active>
                                    <versionNumber>${ENTITY_TYPE.addresses.get(0).addressType.versionNumber}</versionNumber>
                                    <objectId>${ENTITY_TYPE.addresses.get(0).addressType.objectId}</objectId>
                                </addressType>
                                <attentionLine>${ENTITY_TYPE.addresses.get(0).attentionLine}</attentionLine>
                                <line1>${ENTITY_TYPE.addresses.get(0).line1}</line1>
                                <line2>${ENTITY_TYPE.addresses.get(0).line2}</line2>
                                <line3>${ENTITY_TYPE.addresses.get(0).line3}</line3>
                                <city>${ENTITY_TYPE.addresses.get(0).city}</city>
                                <stateProvinceCode>${ENTITY_TYPE.addresses.get(0).stateProvinceCode}</stateProvinceCode>
                                <postalCode>${ENTITY_TYPE.addresses.get(0).postalCode}</postalCode>
                                <countryCode>${ENTITY_TYPE.addresses.get(0).countryCode}</countryCode>
                                <attentionLineUnmasked>${ENTITY_TYPE.addresses.get(0).attentionLineUnmasked}</attentionLineUnmasked>
                                <line1Unmasked>${ENTITY_TYPE.addresses.get(0).line1Unmasked}</line1Unmasked>
                                <line2Unmasked>${ENTITY_TYPE.addresses.get(0).line2Unmasked}</line2Unmasked>
                                <line3Unmasked>${ENTITY_TYPE.addresses.get(0).line3Unmasked}</line3Unmasked>
                                <cityUnmasked>${ENTITY_TYPE.addresses.get(0).cityUnmasked}</cityUnmasked>
                                <stateProvinceCodeUnmasked>${ENTITY_TYPE.addresses.get(0).stateProvinceCodeUnmasked}</stateProvinceCodeUnmasked>
                                <postalCodeUnmasked>${ENTITY_TYPE.addresses.get(0).postalCodeUnmasked}</postalCodeUnmasked>
                                <countryCodeUnmasked>${ENTITY_TYPE.addresses.get(0).countryCodeUnmasked}</countryCodeUnmasked>
                                <addressFormat>${ENTITY_TYPE.addresses.get(0).addressFormat}</addressFormat>
                                <modifiedDate>${ENTITY_TYPE.addresses.get(0).modifiedDate}</modifiedDate>
                                <validatedDate>${ENTITY_TYPE.addresses.get(0).validatedDate}</validatedDate>
                                <valid>${ENTITY_TYPE.addresses.get(0).validated}</valid>
                                <noteMessage>${ENTITY_TYPE.addresses.get(0).noteMessage}</noteMessage>
                                <suppressAddress>${ENTITY_TYPE.addresses.get(0).suppressAddress}</suppressAddress>
                                <defaultValue>${ENTITY_TYPE.addresses.get(0).defaultValue}</defaultValue>
                                <active>${ENTITY_TYPE.addresses.get(0).active}</active>
                                <versionNumber>${ENTITY_TYPE.addresses.get(0).versionNumber}</versionNumber>
                                <objectId>${ENTITY_TYPE.addresses.get(0).objectId}</objectId>
                            </address>
                        </addresses>
                        <emailAddresses>
                            <emailAddress>
                                <id>${ENTITY_TYPE.emailAddresses.get(0).id}</id>
                                <entityTypeCode>${ENTITY_TYPE.emailAddresses.get(0).entityTypeCode}</entityTypeCode>
                                <entityId>${ENTITY_TYPE.emailAddresses.get(0).entityId}</entityId>
                                <emailType>
                                    <code>${ENTITY_TYPE.emailAddresses.get(0).emailType.code}</code>
                                    <name>${ENTITY_TYPE.emailAddresses.get(0).emailType.name}</name>
                                    <sortCode>${ENTITY_TYPE.emailAddresses.get(0).emailType.sortCode}</sortCode>
                                    <active>${ENTITY_TYPE.emailAddresses.get(0).emailType.active}</active>
                                    <versionNumber>${ENTITY_TYPE.emailAddresses.get(0).emailType.versionNumber}</versionNumber>
                                    <objectId>${ENTITY_TYPE.emailAddresses.get(0).emailType.objectId}</objectId>
                                </emailType>
                                <emailAddress>${ENTITY_TYPE.emailAddresses.get(0).emailAddress}</emailAddress>
                                <emailAddressUnmasked>${ENTITY_TYPE.emailAddresses.get(0).emailAddressUnmasked}</emailAddressUnmasked>
                                <suppressEmail>${ENTITY_TYPE.emailAddresses.get(0).suppressEmail}</suppressEmail>
                                <defaultValue>${ENTITY_TYPE.emailAddresses.get(0).defaultValue}</defaultValue>
                                <active>${ENTITY_TYPE.emailAddresses.get(0).active}</active>
                                <versionNumber>${ENTITY_TYPE.emailAddresses.get(0).versionNumber}</versionNumber>
                                <objectId>${ENTITY_TYPE.emailAddresses.get(0).objectId}</objectId>
                            </emailAddress>
                        </emailAddresses>
                        <phoneNumbers>
                            <phoneNumber>
                                <id>${ENTITY_TYPE.phoneNumbers.get(0).id}</id>
                                <entityTypeCode>${ENTITY_TYPE.phoneNumbers.get(0).entityTypeCode}</entityTypeCode>
                                <entityId>${ENTITY_TYPE.phoneNumbers.get(0).entityId}</entityId>
                                <phoneType>
                                    <code>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.code}</code>
                                    <name>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.name}</name>
                                    <sortCode>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.sortCode}</sortCode>
                                    <active>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.active}</active>
                                    <versionNumber>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.versionNumber}</versionNumber>
                                    <objectId>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.objectId}</objectId>
                                </phoneType>
                                <countryCode>${ENTITY_TYPE.phoneNumbers.get(0).countryCode}</countryCode>
                                <phoneNumber>${ENTITY_TYPE.phoneNumbers.get(0).phoneNumber}</phoneNumber>
                                <extensionNumber>${ENTITY_TYPE.phoneNumbers.get(0).extensionNumber}</extensionNumber>
                                <formattedPhoneNumber>${ENTITY_TYPE.phoneNumbers.get(0).formattedPhoneNumber}</formattedPhoneNumber>
                                <countryCodeUnmasked>${ENTITY_TYPE.phoneNumbers.get(0).countryCodeUnmasked}</countryCodeUnmasked>
                                <phoneNumberUnmasked>${ENTITY_TYPE.phoneNumbers.get(0).phoneNumberUnmasked}</phoneNumberUnmasked>
                                <extensionNumberUnmasked>${ENTITY_TYPE.phoneNumbers.get(0).extensionNumberUnmasked}</extensionNumberUnmasked>
                                <formattedPhoneNumberUnmasked>${ENTITY_TYPE.phoneNumbers.get(0).formattedPhoneNumberUnmasked}</formattedPhoneNumberUnmasked>
                                <suppressPhone>${ENTITY_TYPE.phoneNumbers.get(0).suppressPhone}</suppressPhone>
                                <defaultValue>${ENTITY_TYPE.phoneNumbers.get(0).defaultValue}</defaultValue>
                                <active>${ENTITY_TYPE.phoneNumbers.get(0).active}</active>
                                <versionNumber>${ENTITY_TYPE.phoneNumbers.get(0).versionNumber}</versionNumber>
                                <objectId>${ENTITY_TYPE.phoneNumbers.get(0).objectId}</objectId>
                            </phoneNumber>
                        </phoneNumbers>
                        <defaultAddress>
                            <id>${ENTITY_TYPE.addresses.get(0).id}</id>
                            <entityTypeCode>${ENTITY_TYPE.addresses.get(0).entityTypeCode}</entityTypeCode>
                            <entityId>${ENTITY_TYPE.addresses.get(0).entityId}</entityId>
                            <addressType>
                                <code>${ENTITY_TYPE.addresses.get(0).addressType.code}</code>
                                <name>${ENTITY_TYPE.addresses.get(0).addressType.name}</name>
                                <sortCode>${ENTITY_TYPE.addresses.get(0).addressType.sortCode}</sortCode>
                                <active>${ENTITY_TYPE.addresses.get(0).addressType.active}</active>
                                <versionNumber>${ENTITY_TYPE.addresses.get(0).addressType.versionNumber}</versionNumber>
                                <objectId>${ENTITY_TYPE.addresses.get(0).addressType.objectId}</objectId>
                            </addressType>
                            <attentionLine>${ENTITY_TYPE.addresses.get(0).attentionLine}</attentionLine>
                            <line1>${ENTITY_TYPE.addresses.get(0).line1}</line1>
                            <line2>${ENTITY_TYPE.addresses.get(0).line2}</line2>
                            <line3>${ENTITY_TYPE.addresses.get(0).line3}</line3>
                            <city>${ENTITY_TYPE.addresses.get(0).city}</city>
                            <stateProvinceCode>${ENTITY_TYPE.addresses.get(0).stateProvinceCode}</stateProvinceCode>
                            <postalCode>${ENTITY_TYPE.addresses.get(0).postalCode}</postalCode>
                            <countryCode>${ENTITY_TYPE.addresses.get(0).countryCode}</countryCode>
                            <attentionLineUnmasked>${ENTITY_TYPE.addresses.get(0).attentionLineUnmasked}</attentionLineUnmasked>
                            <line1Unmasked>${ENTITY_TYPE.addresses.get(0).line1Unmasked}</line1Unmasked>
                            <line2Unmasked>${ENTITY_TYPE.addresses.get(0).line2Unmasked}</line2Unmasked>
                            <line3Unmasked>${ENTITY_TYPE.addresses.get(0).line3Unmasked}</line3Unmasked>
                            <cityUnmasked>${ENTITY_TYPE.addresses.get(0).cityUnmasked}</cityUnmasked>
                            <stateProvinceCodeUnmasked>${ENTITY_TYPE.addresses.get(0).stateProvinceCodeUnmasked}</stateProvinceCodeUnmasked>
                            <postalCodeUnmasked>${ENTITY_TYPE.addresses.get(0).postalCodeUnmasked}</postalCodeUnmasked>
                            <countryCodeUnmasked>${ENTITY_TYPE.addresses.get(0).countryCodeUnmasked}</countryCodeUnmasked>
                            <addressFormat>${ENTITY_TYPE.addresses.get(0).addressFormat}</addressFormat>
                            <modifiedDate>${ENTITY_TYPE.addresses.get(0).modifiedDate}</modifiedDate>
                            <validatedDate>${ENTITY_TYPE.addresses.get(0).validatedDate}</validatedDate>
                            <valid>${ENTITY_TYPE.addresses.get(0).validated}</valid>
                            <noteMessage>${ENTITY_TYPE.addresses.get(0).noteMessage}</noteMessage>
                            <suppressAddress>${ENTITY_TYPE.addresses.get(0).suppressAddress}</suppressAddress>
                            <defaultValue>${ENTITY_TYPE.addresses.get(0).defaultValue}</defaultValue>
                            <active>${ENTITY_TYPE.addresses.get(0).active}</active>
                            <versionNumber>${ENTITY_TYPE.addresses.get(0).versionNumber}</versionNumber>
                            <objectId>${ENTITY_TYPE.addresses.get(0).objectId}</objectId>
                        </defaultAddress>
                        <defaultEmailAddress>
                            <id>${ENTITY_TYPE.emailAddresses.get(0).id}</id>
                            <entityTypeCode>${ENTITY_TYPE.emailAddresses.get(0).entityTypeCode}</entityTypeCode>
                            <entityId>${ENTITY_TYPE.emailAddresses.get(0).entityId}</entityId>
                            <emailType>
                                <code>${ENTITY_TYPE.emailAddresses.get(0).emailType.code}</code>
                                <name>${ENTITY_TYPE.emailAddresses.get(0).emailType.name}</name>
                                <sortCode>${ENTITY_TYPE.emailAddresses.get(0).emailType.sortCode}</sortCode>
                                <active>${ENTITY_TYPE.emailAddresses.get(0).emailType.active}</active>
                                <versionNumber>${ENTITY_TYPE.emailAddresses.get(0).emailType.versionNumber}</versionNumber>
                                <objectId>${ENTITY_TYPE.emailAddresses.get(0).emailType.objectId}</objectId>
                            </emailType>
                            <emailAddress>${ENTITY_TYPE.emailAddresses.get(0).emailAddress}</emailAddress>
                            <emailAddressUnmasked>${ENTITY_TYPE.emailAddresses.get(0).emailAddressUnmasked}</emailAddressUnmasked>
                            <suppressEmail>${ENTITY_TYPE.emailAddresses.get(0).suppressEmail}</suppressEmail>
                            <defaultValue>${ENTITY_TYPE.emailAddresses.get(0).defaultValue}</defaultValue>
                            <active>${ENTITY_TYPE.emailAddresses.get(0).active}</active>
                            <versionNumber>${ENTITY_TYPE.emailAddresses.get(0).versionNumber}</versionNumber>
                            <objectId>${ENTITY_TYPE.emailAddresses.get(0).objectId}</objectId>
                        </defaultEmailAddress>
                        <defaultPhoneNumber>
                            <id>${ENTITY_TYPE.phoneNumbers.get(0).id}</id>
                            <entityTypeCode>${ENTITY_TYPE.phoneNumbers.get(0).entityTypeCode}</entityTypeCode>
                            <entityId>${ENTITY_TYPE.phoneNumbers.get(0).entityId}</entityId>
                            <phoneType>
                                <code>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.code}</code>
                                <name>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.name}</name>
                                <sortCode>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.sortCode}</sortCode>
                                <active>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.active}</active>
                                <versionNumber>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.versionNumber}</versionNumber>
                                <objectId>${ENTITY_TYPE.phoneNumbers.get(0).phoneType.objectId}</objectId>
                            </phoneType>
                            <countryCode>${ENTITY_TYPE.phoneNumbers.get(0).countryCode}</countryCode>
                            <phoneNumber>${ENTITY_TYPE.phoneNumbers.get(0).phoneNumber}</phoneNumber>
                            <extensionNumber>${ENTITY_TYPE.phoneNumbers.get(0).extensionNumber}</extensionNumber>
                            <formattedPhoneNumber>${ENTITY_TYPE.phoneNumbers.get(0).formattedPhoneNumber}</formattedPhoneNumber>
                            <countryCodeUnmasked>${ENTITY_TYPE.phoneNumbers.get(0).countryCodeUnmasked}</countryCodeUnmasked>
                            <phoneNumberUnmasked>${ENTITY_TYPE.phoneNumbers.get(0).phoneNumberUnmasked}</phoneNumberUnmasked>
                            <extensionNumberUnmasked>${ENTITY_TYPE.phoneNumbers.get(0).extensionNumberUnmasked}</extensionNumberUnmasked>
                            <formattedPhoneNumberUnmasked>${ENTITY_TYPE.phoneNumbers.get(0).formattedPhoneNumberUnmasked}</formattedPhoneNumberUnmasked>
                            <suppressPhone>${ENTITY_TYPE.phoneNumbers.get(0).suppressPhone}</suppressPhone>
                            <defaultValue>${ENTITY_TYPE.phoneNumbers.get(0).defaultValue}</defaultValue>
                            <active>${ENTITY_TYPE.phoneNumbers.get(0).active}</active>
                            <versionNumber>${ENTITY_TYPE.phoneNumbers.get(0).versionNumber}</versionNumber>
                            <objectId>${ENTITY_TYPE.phoneNumbers.get(0).objectId}</objectId>
                        </defaultPhoneNumber>
                        <versionNumber>${ENTITY_TYPE.versionNumber}</versionNumber>
                        <objectId>${ENTITY_TYPE.objectId}</objectId>
                        <active>${ENTITY_TYPE.active}</active>
                    </entityTypeContactInfo>
                </entityTypeContactInfos>
                <externalIdentifiers>
                    <externalIdentifier>
                        <id>${EXTERNAL_ID.id}</id>
                        <entityId>${EXTERNAL_ID.entityId}</entityId>
                        <externalIdentifierTypeCode>${EXTERNAL_ID.externalIdentifierTypeCode}</externalIdentifierTypeCode>
                        <externalIdentifierType>
                            <code>${EXTERNAL_ID.externalIdentifierType.code}</code>
                            <name>${EXTERNAL_ID.externalIdentifierType.name}</name>
                            <sortCode>${EXTERNAL_ID.externalIdentifierType.sortCode}</sortCode>
                            <active>${EXTERNAL_ID.externalIdentifierType.active}</active>
                            <encryptionRequired>${EXTERNAL_ID.externalIdentifierType.encryptionRequired}</encryptionRequired>
                            <versionNumber>${EXTERNAL_ID.externalIdentifierType.versionNumber}</versionNumber>
                            <objectId>${EXTERNAL_ID.externalIdentifierType.objectId}</objectId>
                        </externalIdentifierType>
                        <externalId>${EXTERNAL_ID.externalId}</externalId>
                        <versionNumber>${EXTERNAL_ID.versionNumber}</versionNumber>
                        <objectId>${EXTERNAL_ID.objectId}</objectId>
                    </externalIdentifier>
                </externalIdentifiers>
                <affiliations>
                    <affiliation>
                        <id>${AFFILIATION.id}</id>
                        <entityId>${AFFILIATION.entityId}</entityId>
                        <affiliationType>
                            <code>${AFFILIATION.affiliationType.code}</code>
                            <name>${AFFILIATION.affiliationType.name}</name>
                            <sortCode>${AFFILIATION.affiliationType.sortCode}</sortCode>
                            <active>${AFFILIATION.affiliationType.active}</active>
                            <employmentAffiliationType>${AFFILIATION.affiliationType.employmentAffiliationType}</employmentAffiliationType>
                            <versionNumber>${AFFILIATION.affiliationType.versionNumber}</versionNumber>
                            <objectId>${AFFILIATION.affiliationType.objectId}</objectId>
                        </affiliationType>
                        <campusCode>${AFFILIATION.campusCode}</campusCode>
                        <defaultValue>${AFFILIATION.defaultValue}</defaultValue>
                        <active>${AFFILIATION.active}</active>
                        <versionNumber>${AFFILIATION.versionNumber}</versionNumber>
                        <objectId>${AFFILIATION.objectId}</objectId>
                    </affiliation>
                </affiliations>
                <names>
                    <name>
                        <id>${NAME.id}</id>
                        <entityId>${NAME.entityId}</entityId>
                        <nameType>
                            <code>${NAME.nameType.code}</code>
                            <name>${NAME.nameType.name}</name>
                            <sortCode>${NAME.nameType.sortCode}</sortCode>
                            <active>${NAME.nameType.active}</active>
                            <versionNumber>${NAME.nameType.versionNumber}</versionNumber>
                            <objectId>${NAME.nameType.objectId}</objectId>
                        </nameType>
                        <namePrefix>${NAME.namePrefix}</namePrefix>
                        <nameTitle>${NAME.nameTitle}</nameTitle>
                        <firstName>${NAME.firstName}</firstName>
                        <middleName>${NAME.middleName}</middleName>
                        <lastName>${NAME.lastName}</lastName>
                        <nameSuffix>${NAME.nameSuffix}</nameSuffix>
                        <compositeName>${NAME.compositeName}</compositeName>
                        <namePrefixUnmasked>${NAME.namePrefixUnmasked}</namePrefixUnmasked>
                        <nameTitleUnmasked>${NAME.nameTitleUnmasked}</nameTitleUnmasked>
                        <firstNameUnmasked>${NAME.firstNameUnmasked}</firstNameUnmasked>
                        <middleNameUnmasked>${NAME.middleNameUnmasked}</middleNameUnmasked>
                        <lastNameUnmasked>${NAME.lastNameUnmasked}</lastNameUnmasked>
                        <nameSuffixUnmasked>${NAME.nameSuffixUnmasked}</nameSuffixUnmasked>
                        <compositeNameUnmasked>${NAME.compositeNameUnmasked}</compositeNameUnmasked>
                        <noteMessage>${NAME.noteMessage}</noteMessage>
                        <nameChangedDate>${NAME.nameChangedDate}</nameChangedDate>
                        <suppressName>${NAME.suppressName}</suppressName>
                        <defaultValue>${NAME.defaultValue}</defaultValue>
                        <active>${NAME.active}</active>
                        <versionNumber>${NAME.versionNumber}</versionNumber>
                        <objectId>${NAME.objectId}</objectId>
                    </name>
                </names>
                <employmentInformation>
                    <employment>
                        <id>${EMPLOYMENT.id}</id>
                        <entityId>${EMPLOYMENT.entityId}</entityId>
                        <employeeId>${EMPLOYMENT.employeeId}</employeeId>
                        <employmentRecordId>${EMPLOYMENT.employmentRecordId}</employmentRecordId>
                        <entityAffiliation>
                            <id>${EMPLOYMENT.entityAffiliation.id}</id>
                            <entityId>${EMPLOYMENT.entityAffiliation.entityId}</entityId>
                            <affiliationType>
                                <code>${EMPLOYMENT.entityAffiliation.affiliationType.code}</code>
                                <name>${EMPLOYMENT.entityAffiliation.affiliationType.name}</name>
                                <sortCode>${EMPLOYMENT.entityAffiliation.affiliationType.sortCode}</sortCode>
                                <active>${EMPLOYMENT.entityAffiliation.affiliationType.active}</active>
                                <employmentAffiliationType>${EMPLOYMENT.entityAffiliation.affiliationType.employmentAffiliationType}</employmentAffiliationType>
                                <versionNumber>${EMPLOYMENT.entityAffiliation.affiliationType.versionNumber}</versionNumber>
                                <objectId>${EMPLOYMENT.entityAffiliation.affiliationType.objectId}</objectId>
                            </affiliationType>
                            <campusCode>${EMPLOYMENT.entityAffiliation.campusCode}</campusCode>
                            <defaultValue>${EMPLOYMENT.entityAffiliation.defaultValue}</defaultValue>
                            <active>${EMPLOYMENT.entityAffiliation.active}</active>
                            <versionNumber>${EMPLOYMENT.entityAffiliation.versionNumber}</versionNumber>
                            <objectId>${EMPLOYMENT.entityAffiliation.objectId}</objectId>
                        </entityAffiliation>
                        <employeeStatus>
                            <code>${EMPLOYMENT.employeeStatus.code}</code>
                            <name>${EMPLOYMENT.employeeStatus.name}</name>
                            <sortCode>${EMPLOYMENT.employeeStatus.sortCode}</sortCode>
                            <active>${EMPLOYMENT.employeeStatus.active}</active>
                            <versionNumber>${EMPLOYMENT.employeeStatus.versionNumber}</versionNumber>
                            <objectId>${EMPLOYMENT.employeeStatus.objectId}</objectId>
                        </employeeStatus>
                        <employeeType>
                            <code>${EMPLOYMENT.employeeType.code}</code>
                            <name>${EMPLOYMENT.employeeType.name}</name>
                            <sortCode>${EMPLOYMENT.employeeType.sortCode}</sortCode>
                            <active>${EMPLOYMENT.employeeType.active}</active>
                            <versionNumber>${EMPLOYMENT.employeeType.versionNumber}</versionNumber>
                            <objectId>${EMPLOYMENT.employeeType.objectId}</objectId>
                        </employeeType>
                        <primaryDepartmentCode>${EMPLOYMENT.primaryDepartmentCode}</primaryDepartmentCode>
                        <baseSalaryAmount>${EMPLOYMENT.baseSalaryAmount}</baseSalaryAmount>
                        <primary>${EMPLOYMENT.primary}</primary>
                        <versionNumber>${EMPLOYMENT.versionNumber}</versionNumber>
                        <objectId>${EMPLOYMENT.objectId}</objectId>
                        <active>${EMPLOYMENT.active}</active>
                    </employment>
                </employmentInformation>
                <privacyPreferences>
                    <entityId>${PRIVACY.entityId}</entityId>
                    <suppressName>${PRIVACY.suppressName}</suppressName>
                    <suppressAddress>${PRIVACY.suppressAddress}</suppressAddress>
                    <suppressEmail>${PRIVACY.suppressEmail}</suppressEmail>
                    <suppressPhone>${PRIVACY.suppressPhone}</suppressPhone>
                    <suppressPersonal>${PRIVACY.suppressPersonal}</suppressPersonal>
                    <versionNumber>${PRIVACY.versionNumber}</versionNumber>
                    <objectId>${PRIVACY.objectId}</objectId>
                </privacyPreferences>
                <bioDemographics>
                    <entityId>${BIO.entityId}</entityId>
                    <deceasedDate>${BIO.deceasedDate}</deceasedDate>
                    <birthDate>${BIO.birthDate}</birthDate>
                    <genderCode>${BIO.genderCode}</genderCode>
                    <genderChangeCode>${BIO.genderChangeCode}</genderChangeCode>
                    <maritalStatusCode>${BIO.maritalStatusCode}</maritalStatusCode>
                    <primaryLanguageCode>${BIO.primaryLanguageCode}</primaryLanguageCode>
                    <secondaryLanguageCode>${BIO.secondaryLanguageCode}</secondaryLanguageCode>
                    <birthCountry>${BIO.birthCountry}</birthCountry>
                    <birthStateProvinceCode>${BIO.birthStateProvinceCode}</birthStateProvinceCode>
                    <birthCity>${BIO.birthCity}</birthCity>
                    <geographicOrigin>${BIO.geographicOrigin}</geographicOrigin>
                    <birthDateUnmasked>${BIO.birthDateUnmasked}</birthDateUnmasked>
                    <genderCodeUnmasked>${BIO.genderCodeUnmasked}</genderCodeUnmasked>
                    <genderChangeCodeUnmasked>${BIO.genderChangeCodeUnmasked}</genderChangeCodeUnmasked>
                    <maritalStatusCodeUnmasked>${BIO.maritalStatusCodeUnmasked}</maritalStatusCodeUnmasked>
                    <primaryLanguageCodeUnmasked>${BIO.primaryLanguageCodeUnmasked}</primaryLanguageCodeUnmasked>
                    <secondaryLanguageCodeUnmasked>${BIO.secondaryLanguageCodeUnmasked}</secondaryLanguageCodeUnmasked>
                    <birthCountryUnmasked>${BIO.birthCountryUnmasked}</birthCountryUnmasked>
                    <birthStateProvinceCodeUnmasked>${BIO.birthStateProvinceCodeUnmasked}</birthStateProvinceCodeUnmasked>
                    <birthCityUnmasked>${BIO.birthCityUnmasked}</birthCityUnmasked>
                    <geographicOriginUnmasked>${BIO.geographicOriginUnmasked}</geographicOriginUnmasked>
                    <noteMessage>${BIO.noteMessage}</noteMessage>
                    <suppressPersonal>${BIO.suppressPersonal}</suppressPersonal>
                    <versionNumber>${BIO.versionNumber}</versionNumber>
                    <objectId>${BIO.objectId}</objectId>
                </bioDemographics>
                <citizenships>
                    <citizenship>
                        <id>${CITIZENSHIP.id}</id>
                        <entityId>${CITIZENSHIP.entityId}</entityId>
                        <status>
                            <code>${CITIZENSHIP.status.code}</code>
                            <name>${CITIZENSHIP.status.name}</name>
                            <sortCode>${CITIZENSHIP.status.sortCode}</sortCode>
                            <active>${CITIZENSHIP.status.active}</active>
                            <versionNumber>${CITIZENSHIP.status.versionNumber}</versionNumber>
                            <objectId>${CITIZENSHIP.status.objectId}</objectId>
                        </status>
                        <countryCode>${CITIZENSHIP.countryCode}</countryCode>
                        <startDate>${CITIZENSHIP.startDate.toString()}</startDate>
                        <endDate>${CITIZENSHIP.endDate.toString()}</endDate>
                        <versionNumber>${CITIZENSHIP.versionNumber}</versionNumber>
                        <objectId>${CITIZENSHIP.objectId}</objectId>
                        <active>${CITIZENSHIP.active}</active>
                    </citizenship>
                </citizenships>
                <defaultAffiliation>
                    <id>${AFFILIATION.id}</id>
                    <entityId>${AFFILIATION.entityId}</entityId>
                    <affiliationType>
                        <code>${AFFILIATION.affiliationType.code}</code>
                        <name>${AFFILIATION.affiliationType.name}</name>
                        <sortCode>${AFFILIATION.affiliationType.sortCode}</sortCode>
                        <active>${AFFILIATION.affiliationType.active}</active>
                        <employmentAffiliationType>${AFFILIATION.affiliationType.employmentAffiliationType}</employmentAffiliationType>
                        <versionNumber>${AFFILIATION.affiliationType.versionNumber}</versionNumber>
                        <objectId>${AFFILIATION.affiliationType.objectId}</objectId>
                    </affiliationType>
                    <campusCode>${AFFILIATION.campusCode}</campusCode>
                    <defaultValue>${AFFILIATION.defaultValue}</defaultValue>
                    <active>${AFFILIATION.active}</active>
                    <versionNumber>${AFFILIATION.versionNumber}</versionNumber>
                    <objectId>${AFFILIATION.objectId}</objectId>
                </defaultAffiliation>
                <defaultName>
                    <id>${NAME.id}</id>
                    <entityId>${NAME.entityId}</entityId>
                    <nameType>
                        <code>${NAME.nameType.code}</code>
                        <name>${NAME.nameType.name}</name>
                        <sortCode>${NAME.nameType.sortCode}</sortCode>
                        <active>${NAME.nameType.active}</active>
                        <versionNumber>${NAME.nameType.versionNumber}</versionNumber>
                        <objectId>${NAME.nameType.objectId}</objectId>
                    </nameType>
                    <namePrefix>${NAME.namePrefix}</namePrefix>
                    <nameTitle>${NAME.nameTitle}</nameTitle>
                    <firstName>${NAME.firstName}</firstName>
                    <middleName>${NAME.middleName}</middleName>
                    <lastName>${NAME.lastName}</lastName>
                    <nameSuffix>${NAME.nameSuffix}</nameSuffix>
                    <compositeName>${NAME.compositeName}</compositeName>
                    <namePrefixUnmasked>${NAME.namePrefixUnmasked}</namePrefixUnmasked>
                    <nameTitleUnmasked>${NAME.nameTitleUnmasked}</nameTitleUnmasked>
                    <firstNameUnmasked>${NAME.firstNameUnmasked}</firstNameUnmasked>
                    <middleNameUnmasked>${NAME.middleNameUnmasked}</middleNameUnmasked>
                    <lastNameUnmasked>${NAME.lastNameUnmasked}</lastNameUnmasked>
                    <nameSuffixUnmasked>${NAME.nameSuffixUnmasked}</nameSuffixUnmasked>
                    <compositeNameUnmasked>${NAME.compositeNameUnmasked}</compositeNameUnmasked>
                    <noteMessage>${NAME.noteMessage}</noteMessage>
                    <nameChangedDate>${NAME.nameChangedDate}</nameChangedDate>
                    <suppressName>${NAME.suppressName}</suppressName>
                    <defaultValue>${NAME.defaultValue}</defaultValue>
                    <active>${NAME.active}</active>
                    <versionNumber>${NAME.versionNumber}</versionNumber>
                    <objectId>${NAME.objectId}</objectId>
                </defaultName>
                <ethnicities>
                    <ethnicity>
                        <id>${ETHNICITY.id}</id>
                        <entityId>${ETHNICITY.entityId}</entityId>
                        <ethnicityCode>${ETHNICITY.ethnicityCode}</ethnicityCode>
                        <ethnicityCodeUnmasked>${ETHNICITY.ethnicityCodeUnmasked}</ethnicityCodeUnmasked>
                        <subEthnicityCode>${ETHNICITY.subEthnicityCode}</subEthnicityCode>
                        <subEthnicityCodeUnmasked>${ETHNICITY.subEthnicityCodeUnmasked}</subEthnicityCodeUnmasked>
                        <suppressPersonal>${ETHNICITY.suppressPersonal}</suppressPersonal>
                        <versionNumber>${ETHNICITY.versionNumber}</versionNumber>
                        <objectId>${ETHNICITY.objectId}</objectId>
                    </ethnicity>
                </ethnicities>
                <residencies>
                    <residency>
                        <id>${RESIDENCY.id}</id>
                        <entityId>${RESIDENCY.entityId}</entityId>
                        <determinationMethod>${RESIDENCY.determinationMethod}</determinationMethod>
                        <inState>${RESIDENCY.inState}</inState>
                        <versionNumber>${RESIDENCY.versionNumber}</versionNumber>
                        <objectId>${RESIDENCY.objectId}</objectId>
                    </residency>
                </residencies>
                <visas>
                    <visa>
                        <id>${VISA.id}</id>
                        <entityId>${VISA.entityId}</entityId>
                        <visaTypeKey>${VISA.visaTypeKey}</visaTypeKey>
                        <visaEntry>${VISA.visaEntry}</visaEntry>
                        <visaId>${VISA.visaId}</visaId>
                        <versionNumber>${VISA.versionNumber}</versionNumber>
                        <objectId>${VISA.objectId}</objectId>
                    </visa>
                </visas>
                <versionNumber>${VERSION_NUMBER}</versionNumber>
                <objectId>${OBJECT_ID}</objectId>
                <active>${ACTIVE}</active>
            </entity>
        """


    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_entityId_whitespace() {
        Entity.Builder builder = Entity.Builder.create();
        builder.setId(" ")
    }

    @Test
    void test_copy() {
        def o1 = Entity.Builder.create().build();
        def o2 = Entity.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        Entity.Builder.create();
    }

    @Test
	public void testXmlMarshaling() {
	  JAXBContext jc = JAXBContext.newInstance(Entity.class)
	  Marshaller marshaller = jc.createMarshaller()
	  StringWriter sw = new StringWriter()

	  Entity entity = this.create()
	  marshaller.marshal(entity,sw)
	  String xml = sw.toString()
      println(xml)

	}

    @Test
    public void test_Xml_Marshal_Unmarshal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, Entity.class)
    }

    private create() {
        List<Principal> testPrincipals = Collections.singletonList(EntityTest.PRINCIPAL)

        List<EntityTypeContactInfo> testEntityTypes = Collections.singletonList(EntityTest.ENTITY_TYPE)

        List<EntityExternalIdentifier> testExternalIdentifiers = Collections.singletonList(EntityTest.EXTERNAL_ID)

        List<EntityAffiliation> testAffiliations = Collections.singletonList(EntityTest.AFFILIATION)

        List<EntityName> testNames = Collections.singletonList(EntityTest.NAME)

        List<EntityEmployment> testEmploymentInformation = Collections.singletonList(EntityTest.EMPLOYMENT)

        EntityPrivacyPreferences testPrivacyPreferences = EntityTest.PRIVACY
        EntityBioDemographics testBioDemographics = EntityTest.BIO

        List<EntityCitizenship> testCitizenships = Collections.singletonList(EntityTest.CITIZENSHIP)

        List<EntityEthnicity> testEthnicities = Collections.singletonList(EntityTest.ETHNICITY);

        List<EntityResidency> testResidencies = Collections.singletonList(EntityTest.RESIDENCY)

        List<EntityVisa> testVisas = Collections.singletonList(EntityTest.VISA)

        return Entity.Builder.create(new EntityContract() {

            def String id = EntityTest.ENTITY_ID
            def List<Principal> getPrincipals() {return testPrincipals }
            def List<EntityTypeContactInfo> getEntityTypeContactInfos() { return testEntityTypes }
            def List<EntityExternalIdentifier> getExternalIdentifiers() {return testExternalIdentifiers }
            def List<EntityAffiliation> getAffiliations() { return testAffiliations }
            def List<EntityName> getNames() { return testNames }
            def List<EntityEmployment> getEmploymentInformation() { return testEmploymentInformation }
            def EntityPrivacyPreferences getPrivacyPreferences() { return testPrivacyPreferences }
            def EntityBioDemographics getBioDemographics() { return testBioDemographics }
            def List<EntityCitizenship> getCitizenships() { return testCitizenships }
            def EntityTypeContactInfo getEntityTypeContactInfoByTypeCode(String entityTypeCode) {
                return this.getEntityTypeContactInfoByTypeCode(entityTypeCode)
            }
            def EntityEmploymentContract getPrimaryEmployment() {
                return EntityTest.EMPLOYMENT
            }
            EntityAffiliationContract getDefaultAffiliation() {
                return EntityTest.AFFILIATION
            }
            EntityExternalIdentifierContract getEntityExternalIdentifier(String externalIdentifierTypeCode) {
                return this.getEntityExternalIdentifier(externalIdentifierTypeCode)
            }
            EntityNameContract getDefaultName() {
                return EntityTest.NAME
            }
            def List<EntityEthnicity> getEthnicities() { return testEthnicities }
            def List<EntityResidency> getResidencies() { return testResidencies }
            def List<EntityVisa> getVisas() { return testVisas }
            def boolean active = EntityTest.ACTIVE.toBoolean()
            def Long versionNumber = EntityTest.VERSION_NUMBER;
            def String objectId = EntityTest.OBJECT_ID
        }).build()
    }
    
}
