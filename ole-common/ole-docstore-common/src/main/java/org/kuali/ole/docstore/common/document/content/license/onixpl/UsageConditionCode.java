
package org.kuali.ole.docstore.common.document.content.license.onixpl;

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
 * <p>Java class for UsageConditionCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UsageConditionCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AccessByAuthorizedUsersOnly"/>
 *     &lt;enumeration value="onixPL:ComplianceWithUSCopyrightLawAndGuidelines"/>
 *     &lt;enumeration value="onixPL:DestructionOfUsedResourceAfterUse"/>
 *     &lt;enumeration value="onixPL:RecordKeepingNotRequired"/>
 *     &lt;enumeration value="onixPL:RecordKeepingRequired"/>
 *     &lt;enumeration value="onixPL:SubjectToFairUse"/>
 *     &lt;enumeration value="onixPL:SubjectToVolumeLimit"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UsageConditionCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UsageConditionCode {


    /**
     * Usage must not allow third party access to licensed content.
     */
    @XmlEnumValue("onixPL:AccessByAuthorizedUsersOnly")
    ONIX_PL_ACCESS_BY_AUTHORIZED_USERS_ONLY("onixPL:AccessByAuthorizedUsersOnly"),

    /**
     * Usage in the USA must comply with United States Copyright Law (17 USC  108, 'Limitations on exclusive rights: Reproduction by libraries and archives') and clause 3 of the Guidelines for the Proviso of Subsection 108 (g)(2) prepared by the National Commission on New Technological Uses of Copyrighted Works.
     */
    @XmlEnumValue("onixPL:ComplianceWithUSCopyrightLawAndGuidelines")
    ONIX_PL_COMPLIANCE_WITH_US_COPYRIGHT_LAW_AND_GUIDELINES("onixPL:ComplianceWithUSCopyrightLawAndGuidelines"),

    /**
     * Usage is subject to the condition that the used resource(s) must be destroyed after use for the purpose(s) permitted under a license.
     */
    @XmlEnumValue("onixPL:DestructionOfUsedResourceAfterUse")
    ONIX_PL_DESTRUCTION_OF_USED_RESOURCE_AFTER_USE("onixPL:DestructionOfUsedResourceAfterUse"),

    /**
     * Usage is not subject to the condition that records are kept by the licensee.
     */
    @XmlEnumValue("onixPL:RecordKeepingNotRequired")
    ONIX_PL_RECORD_KEEPING_NOT_REQUIRED("onixPL:RecordKeepingNotRequired"),

    /**
     * Usage is or may be subject to the condition that records of each usage are kept by the licensee.
     */
    @XmlEnumValue("onixPL:RecordKeepingRequired")
    ONIX_PL_RECORD_KEEPING_REQUIRED("onixPL:RecordKeepingRequired"),

    /**
     * Usage is subject to the Fair Use or Fair Dealing provisions of applicable copyright law (in countries where Fair Use or Fair Dealing applies).
     */
    @XmlEnumValue("onixPL:SubjectToFairUse")
    ONIX_PL_SUBJECT_TO_FAIR_USE("onixPL:SubjectToFairUse"),

    /**
     * Usage is subject to a volume limit specified in the license.
     */
    @XmlEnumValue("onixPL:SubjectToVolumeLimit")
    ONIX_PL_SUBJECT_TO_VOLUME_LIMIT("onixPL:SubjectToVolumeLimit");
    private final String value;

    UsageConditionCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsageConditionCode fromValue(String v) {
        for (UsageConditionCode c : UsageConditionCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
