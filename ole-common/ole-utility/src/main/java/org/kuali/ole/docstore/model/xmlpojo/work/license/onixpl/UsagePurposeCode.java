
package org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 5/30/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for UsagePurposeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UsagePurposeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AcademicUse"/>
 *     &lt;enumeration value="onixPL:AdministrativeUse"/>
 *     &lt;enumeration value="onixPL:CommercialUse"/>
 *     &lt;enumeration value="onixPL:DevelopmentUse"/>
 *     &lt;enumeration value="onixPL:EnablingPermittedUse"/>
 *     &lt;enumeration value="onixPL:NonAcademicUse"/>
 *     &lt;enumeration value="onixPL:NonCommercialUse"/>
 *     &lt;enumeration value="onixPL:PersonalUse"/>
 *     &lt;enumeration value="onixPL:ProfessionalUse"/>
 *     &lt;enumeration value="onixPL:PromotingLicensedContent"/>
 *     &lt;enumeration value="onixPL:RegulatoryUse"/>
 *     &lt;enumeration value="onixPL:ResearchUse"/>
 *     &lt;enumeration value="onixPL:TestingLicensedContent"/>
 *     &lt;enumeration value="onixPL:TrainingAuthorizedUsers"/>
 *     &lt;enumeration value="onixPL:UnrestrictedUse"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UsagePurposeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UsagePurposeCode {


    /**
     * Use for the purpose of education, teaching, private study and/or academic research, excluding any commercial use.
     */
    @XmlEnumValue("onixPL:AcademicUse")
    ONIX_PL_ACADEMIC_USE("onixPL:AcademicUse"),

    /**
     * Use for the administration and management of a licensee organization.
     */
    @XmlEnumValue("onixPL:AdministrativeUse")
    ONIX_PL_ADMINISTRATIVE_USE("onixPL:AdministrativeUse"),

    /**
     * Use for any purpose of monetary reward by means of sale, resale, loan, transfer, hire or other form of exploitation of licensed content. The recovery of direct cost of using licensed content by a licensee from an authorized user, or use by a licensee or an authorized user in the course of academic research funded by a commercial organization, does not constitute CommercialUse.
     */
    @XmlEnumValue("onixPL:CommercialUse")
    ONIX_PL_COMMERCIAL_USE("onixPL:CommercialUse"),

    /**
     * Use for development work for the purpose of enabling other usages that are permitted under a license.
     */
    @XmlEnumValue("onixPL:DevelopmentUse")
    ONIX_PL_DEVELOPMENT_USE("onixPL:DevelopmentUse"),

    /**
     * Use solely for the purpose of enabling other usages that are permitted under a license.
     */
    @XmlEnumValue("onixPL:EnablingPermittedUse")
    ONIX_PL_ENABLING_PERMITTED_USE("onixPL:EnablingPermittedUse"),

    /**
     * Use that is not for the purpose of education, teaching, private study and/or academic research, including but not limited to CommercialUse.
     */
    @XmlEnumValue("onixPL:NonAcademicUse")
    ONIX_PL_NON_ACADEMIC_USE("onixPL:NonAcademicUse"),

    /**
     * Use that is not for the purpose of monetary reward, including but not limited to AcademicUse.
     */
    @XmlEnumValue("onixPL:NonCommercialUse")
    ONIX_PL_NON_COMMERCIAL_USE("onixPL:NonCommercialUse"),

    /**
     * Personal use by a single individual only, excluding any resale of licensed content.
     */
    @XmlEnumValue("onixPL:PersonalUse")
    ONIX_PL_PERSONAL_USE("onixPL:PersonalUse"),

    /**
     * Use by an individual in the exercise of his or her profession, excluding any resale of licensed content.
     */
    @XmlEnumValue("onixPL:ProfessionalUse")
    ONIX_PL_PROFESSIONAL_USE("onixPL:ProfessionalUse"),

    /**
     * Use for promoting licensed content to authorized users.
     */
    @XmlEnumValue("onixPL:PromotingLicensedContent")
    ONIX_PL_PROMOTING_LICENSED_CONTENT("onixPL:PromotingLicensedContent"),

    /**
     * Use by or for a regulatory authority for a regulatory purpose, eg in connection with a patent or trademark application.
     */
    @XmlEnumValue("onixPL:RegulatoryUse")
    ONIX_PL_REGULATORY_USE("onixPL:RegulatoryUse"),

    /**
     * Use for purposes of research.
     */
    @XmlEnumValue("onixPL:ResearchUse")
    ONIX_PL_RESEARCH_USE("onixPL:ResearchUse"),

    /**
     * Use for testing licensed content within a licensee organization.
     */
    @XmlEnumValue("onixPL:TestingLicensedContent")
    ONIX_PL_TESTING_LICENSED_CONTENT("onixPL:TestingLicensedContent"),

    /**
     * Use for the purpose of training authorized users.
     */
    @XmlEnumValue("onixPL:TrainingAuthorizedUsers")
    ONIX_PL_TRAINING_AUTHORIZED_USERS("onixPL:TrainingAuthorizedUsers"),

    /**
     * Use for any purpose, without restriction.
     */
    @XmlEnumValue("onixPL:UnrestrictedUse")
    ONIX_PL_UNRESTRICTED_USE("onixPL:UnrestrictedUse");
    private final String value;

    UsagePurposeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsagePurposeCode fromValue(String v) {
        for (UsagePurposeCode c : UsagePurposeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
