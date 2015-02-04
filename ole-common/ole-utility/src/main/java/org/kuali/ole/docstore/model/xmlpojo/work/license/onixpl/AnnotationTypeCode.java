
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
 * <p>Java class for AnnotationTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="AnnotationTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AcknowledgmentToInclude"/>
 *     &lt;enumeration value="onixPL:AcknowledgementWording"/>
 *     &lt;enumeration value="onixPL:AmbiguityInLicenseText"/>
 *     &lt;enumeration value="onixPL:ErrorInLicenseText"/>
 *     &lt;enumeration value="onixPL:Interpretation"/>
 *     &lt;enumeration value="onixPL:ReferencedText"/>
 *     &lt;enumeration value="onixPL:SpecialConditions"/>
 *     &lt;enumeration value="onixPL:VariationsFromModel"/>
 *     &lt;enumeration value="onixPL:ERMI:ArchiveCopyFormat"/>
 *     &lt;enumeration value="onixPL:ERMI:CitationRequirementDetail"/>
 *     &lt;enumeration value="onixPL:ERMI:ConcurrentUserNote"/>
 *     &lt;enumeration value="onixPL:ERMI:DistanceEducationNote"/>
 *     &lt;enumeration value="onixPL:ERMI:DistanceEducationStatus"/>
 *     &lt;enumeration value="onixPL:ERMI:LocalText"/>
 *     &lt;enumeration value="onixPL:ERMI:MaintenanceWindow"/>
 *     &lt;enumeration value="onixPL:ERMI:Note"/>
 *     &lt;enumeration value="onixPL:ERMI:OtherUseRestrictionNote"/>
 *     &lt;enumeration value="onixPL:ERMI:OtherUserRestrictionNote"/>
 *     &lt;enumeration value="onixPL:ERMI:PerpetualAccessHoldings"/>
 *     &lt;enumeration value="onixPL:ERMI:TerminationRequirementsNote"/>
 *     &lt;enumeration value="onixPL:ERMI:TermsNote"/>
 *     &lt;enumeration value="onixPL:ERMI:Text"/>
 *     &lt;enumeration value="onixPL:ERMI:UptimeGuarantee"/>
 *     &lt;enumeration value="onixPL:ERMI:Value"/>
 *     &lt;enumeration value="onixPL:ERMI:WalkInUserTermNote"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "AnnotationTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum AnnotationTypeCode {


    /**
     * Text stating elements that must be included in an acknowledgement when an extract from licensed content is reproduced, but not precise wording.
     */
    @XmlEnumValue("onixPL:AcknowledgmentToInclude")
    ONIX_PL_ACKNOWLEDGMENT_TO_INCLUDE("onixPL:AcknowledgmentToInclude"),

    /**
     * Wording specified for an acknowledgment when an extract from licensed content is reproduced.
     */
    @XmlEnumValue("onixPL:AcknowledgementWording")
    ONIX_PL_ACKNOWLEDGEMENT_WORDING("onixPL:AcknowledgementWording"),

    /**
     * Note pointing out an ambiguity in license wording.
     */
    @XmlEnumValue("onixPL:AmbiguityInLicenseText")
    ONIX_PL_AMBIGUITY_IN_LICENSE_TEXT("onixPL:AmbiguityInLicenseText"),

    /**
     * Note pointing out an error in license wording, numbering or cross-referencing.
     */
    @XmlEnumValue("onixPL:ErrorInLicenseText")
    ONIX_PL_ERROR_IN_LICENSE_TEXT("onixPL:ErrorInLicenseText"),

    /**
     * Note explaining an interpretation of license wording.
     */
    @XmlEnumValue("onixPL:Interpretation")
    ONIX_PL_INTERPRETATION("onixPL:Interpretation"),

    /**
     * Text quoted from a referenced document other than a license document.
     */
    @XmlEnumValue("onixPL:ReferencedText")
    ONIX_PL_REFERENCED_TEXT("onixPL:ReferencedText"),

    /**
     * Note describing special conditions to which attention is drawn.
     */
    @XmlEnumValue("onixPL:SpecialConditions")
    ONIX_PL_SPECIAL_CONDITIONS("onixPL:SpecialConditions"),

    /**
     * Note describing variations from a model license on which a license is based.
     */
    @XmlEnumValue("onixPL:VariationsFromModel")
    ONIX_PL_VARIATIONS_FROM_MODEL("onixPL:VariationsFromModel"),

    /**
     * Text or local values describing the form in which archival content would be delivered for continuing use after termination. Used only in a Continuing Access Term of type 'Archive Copy'.
     */
    @XmlEnumValue("onixPL:ERMI:ArchiveCopyFormat")
    ONIX_PL_ERMI_ARCHIVE_COPY_FORMAT("onixPL:ERMI:ArchiveCopyFormat"),

    /**
     * A specification of the required or recommended form of citation. Used only for an annotation to a License Grant.
     */
    @XmlEnumValue("onixPL:ERMI:CitationRequirementDetail")
    ONIX_PL_ERMI_CITATION_REQUIREMENT_DETAIL("onixPL:ERMI:CitationRequirementDetail"),

    /**
     * Notes about the number of concurrent users permitted under a license. Used only for an annotation to a Usage Term of type 'Access'.
     */
    @XmlEnumValue("onixPL:ERMI:ConcurrentUserNote")
    ONIX_PL_ERMI_CONCURRENT_USER_NOTE("onixPL:ERMI:ConcurrentUserNote"),

    /**
     * Information which qualifies a permissions statement on Distance Education. Used only for an annotation to an Agent Definition for Authorized Users.
     */
    @XmlEnumValue("onixPL:ERMI:DistanceEducationNote")
    ONIX_PL_ERMI_DISTANCE_EDUCATION_NOTE("onixPL:ERMI:DistanceEducationNote"),

    /**
     * An annotation specifying whether use for Distance Education is permitted. The Annotation Text should comprise one of the values defined for Usage Term Status. Used only as an Annotation to an Agent Definition for Authorized Users.
     */
    @XmlEnumValue("onixPL:ERMI:DistanceEducationStatus")
    ONIX_PL_ERMI_DISTANCE_EDUCATION_STATUS("onixPL:ERMI:DistanceEducationStatus"),

    /**
     * Text used to define Authorized Users as an annotation to an Agent Definition when the ERMI Local Authorized User Definition flag has the value 'Y'.
     */
    @XmlEnumValue("onixPL:ERMI:LocalText")
    ONIX_PL_ERMI_LOCAL_TEXT("onixPL:ERMI:LocalText"),

    /**
     * The recurring period of time reserved by the product provider for technical maintenance activities, during which online access may be unavailable. Used only for an annotation to a General Term of type 'Service Performance Guarantee'.
     */
    @XmlEnumValue("onixPL:ERMI:MaintenanceWindow")
    ONIX_PL_ERMI_MAINTENANCE_WINDOW("onixPL:ERMI:MaintenanceWindow"),

    /**
     * Information which qualifies a specific license term and populates the corresponding ERMI term note element. Used for an annotation in any ONIX-PL term that maps directly to and from a corresponding ERMI term.
     */
    @XmlEnumValue("onixPL:ERMI:Note")
    ONIX_PL_ERMI_NOTE("onixPL:ERMI:Note"),

    /**
     * Additional information about other use restrictions not adequately described elsewhere. Used only for an annotation to a License Grant.
     */
    @XmlEnumValue("onixPL:ERMI:OtherUseRestrictionNote")
    ONIX_PL_ERMI_OTHER_USE_RESTRICTION_NOTE("onixPL:ERMI:OtherUseRestrictionNote"),

    /**
     * Additional information about other user restrictions not adequately described elsewhere. Used only for an annotation to a License Grant.
     */
    @XmlEnumValue("onixPL:ERMI:OtherUserRestrictionNote")
    ONIX_PL_ERMI_OTHER_USER_RESTRICTION_NOTE("onixPL:ERMI:OtherUserRestrictionNote"),

    /**
     * The dates of coverage for which perpetual rights are available and agreed upon in the legal contract. Used only in a Continuing Access Term of type 'Continuing Online Access'.
     */
    @XmlEnumValue("onixPL:ERMI:PerpetualAccessHoldings")
    ONIX_PL_ERMI_PERPETUAL_ACCESS_HOLDINGS("onixPL:ERMI:PerpetualAccessHoldings"),

    /**
     * A clarification of the termination requirements and what certification of the requirement activities is necessary. Used only in a General Term of type 'Action On Termination'.
     */
    @XmlEnumValue("onixPL:ERMI:TerminationRequirementsNote")
    ONIX_PL_ERMI_TERMINATION_REQUIREMENTS_NOTE("onixPL:ERMI:TerminationRequirementsNote"),

    /**
     * Notes about the terms as a whole. Used only for an annotation to a License Grant.
     */
    @XmlEnumValue("onixPL:ERMI:TermsNote")
    ONIX_PL_ERMI_TERMS_NOTE("onixPL:ERMI:TermsNote"),

    /**
     * Text used to specify a term in cases where ERMI defines a term content as 'Text'.
     */
    @XmlEnumValue("onixPL:ERMI:Text")
    ONIX_PL_ERMI_TEXT("onixPL:ERMI:Text"),

    /**
     * The specific percentage of up-time guaranteed for the product being licensed, and the context for that percentage. Used only for an annotation to a General Term of type 'Service Performance Guarantee'.
     */
    @XmlEnumValue("onixPL:ERMI:UptimeGuarantee")
    ONIX_PL_ERMI_UPTIME_GUARANTEE("onixPL:ERMI:UptimeGuarantee"),

    /**
     * An ERMI-defined value used to qualify a term, eg General / IP only / other or No / All / Financial only / All but user terms. Applicable values depend on the term type. Used only in General Terms of type 'Licensor Indemnity', 'Confidentiality Of Agreement', 'ERMI: Licensee Termination Right' and 'ERMI: Licensor Termination Right'.
     */
    @XmlEnumValue("onixPL:ERMI:Value")
    ONIX_PL_ERMI_VALUE("onixPL:ERMI:Value"),

    /**
     * Information which qualifies the status or permitted actions of Walk-In Users. Used only for an annotation to an Agent Definition for Authorized Users.
     */
    @XmlEnumValue("onixPL:ERMI:WalkInUserTermNote")
    ONIX_PL_ERMI_WALK_IN_USER_TERM_NOTE("onixPL:ERMI:WalkInUserTermNote");
    private final String value;

    AnnotationTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AnnotationTypeCode fromValue(String v) {
        for (AnnotationTypeCode c : AnnotationTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
