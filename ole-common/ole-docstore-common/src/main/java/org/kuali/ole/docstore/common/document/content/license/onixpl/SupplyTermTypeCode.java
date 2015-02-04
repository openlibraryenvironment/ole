
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
 * <p>Java class for SupplyTermTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="SupplyTermTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:ChangeOfOwnershipOfLicensedResource"/>
 *     &lt;enumeration value="onixPL:ChangesToLicensedContent"/>
 *     &lt;enumeration value="onixPL:CompletenessOfContent"/>
 *     &lt;enumeration value="onixPL:ComplianceWithAccessibilityStandards"/>
 *     &lt;enumeration value="onixPL:ComplianceWithONIX"/>
 *     &lt;enumeration value="onixPL:ComplianceWithOpenURLStandard"/>
 *     &lt;enumeration value="onixPL:ComplianceWithProjectTransferCode"/>
 *     &lt;enumeration value="onixPL:ConcurrencyWithPrintVersion"/>
 *     &lt;enumeration value="onixPL:ContentDelivery"/>
 *     &lt;enumeration value="onixPL:ContentWarranty"/>
 *     &lt;enumeration value="onixPL:MediaWarranty"/>
 *     &lt;enumeration value="onixPL:MetadataSupply"/>
 *     &lt;enumeration value="onixPL:NetworkAccess"/>
 *     &lt;enumeration value="onixPL:ProductDocumentation"/>
 *     &lt;enumeration value="onixPL:PublicationSchedule"/>
 *     &lt;enumeration value="onixPL:ServicePerformance"/>
 *     &lt;enumeration value="onixPL:ServicePerformanceGuarantee"/>
 *     &lt;enumeration value="onixPL:StartOfService"/>
 *     &lt;enumeration value="onixPL:UsageStatistics"/>
 *     &lt;enumeration value="onixPL:UserRegistration"/>
 *     &lt;enumeration value="onixPL:UserSupport"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "SupplyTermTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum SupplyTermTypeCode {


    /**
     * A license term that specifies conditions relating to a change of ownership of a licensed resource during the term of a license.
     */
    @XmlEnumValue("onixPL:ChangeOfOwnershipOfLicensedResource")
    ONIX_PL_CHANGE_OF_OWNERSHIP_OF_LICENSED_RESOURCE("onixPL:ChangeOfOwnershipOfLicensedResource"),

    /**
     * A license term that specifies whether and/or how changes may be made to licensed content to be supplied during the term of a license.
     */
    @XmlEnumValue("onixPL:ChangesToLicensedContent")
    ONIX_PL_CHANGES_TO_LICENSED_CONTENT("onixPL:ChangesToLicensedContent"),

    /**
     * A license term that specifies that licensed electronic content is at least equivalent to the corresponding printed content.
     */
    @XmlEnumValue("onixPL:CompletenessOfContent")
    ONIX_PL_COMPLETENESS_OF_CONTENT("onixPL:CompletenessOfContent"),

    /**
     * A license term that specifies accessibility standards to be followed in the supply of licensed content.
     */
    @XmlEnumValue("onixPL:ComplianceWithAccessibilityStandards")
    ONIX_PL_COMPLIANCE_WITH_ACCESSIBILITY_STANDARDS("onixPL:ComplianceWithAccessibilityStandards"),

    /**
     * A license term that specifies compliance with the ONIX-PL license expression standard.
     */
    @XmlEnumValue("onixPL:ComplianceWithONIX")
    ONIX_PL_COMPLIANCE_WITH_ONIX("onixPL:ComplianceWithONIX"),

    /**
     * A license term that specifies compliance with the OpenURL standard.
     */
    @XmlEnumValue("onixPL:ComplianceWithOpenURLStandard")
    ONIX_PL_COMPLIANCE_WITH_OPEN_URL_STANDARD("onixPL:ComplianceWithOpenURLStandard"),

    /**
     * A license term that specifies compliance with the Code of Practice for Project Transfer.
     */
    @XmlEnumValue("onixPL:ComplianceWithProjectTransferCode")
    ONIX_PL_COMPLIANCE_WITH_PROJECT_TRANSFER_CODE("onixPL:ComplianceWithProjectTransferCode"),

    /**
     * A license term that specifies that the licensed content will be available before, or no later than, the print equivalent.
     */
    @XmlEnumValue("onixPL:ConcurrencyWithPrintVersion")
    ONIX_PL_CONCURRENCY_WITH_PRINT_VERSION("onixPL:ConcurrencyWithPrintVersion"),

    /**
     * A license term that specifies how licensed content will be delivered, eg online or by supply of files to be maintained by the licensee.
     */
    @XmlEnumValue("onixPL:ContentDelivery")
    ONIX_PL_CONTENT_DELIVERY("onixPL:ContentDelivery"),

    /**
     * A license term that guarantees a remedy to the licensee if the quantity or quality of material contained within the product is materially diminished.
     */
    @XmlEnumValue("onixPL:ContentWarranty")
    ONIX_PL_CONTENT_WARRANTY("onixPL:ContentWarranty"),

    /**
     * A license term that specifies a warranty relating to physical media on which delivery is made.
     */
    @XmlEnumValue("onixPL:MediaWarranty")
    ONIX_PL_MEDIA_WARRANTY("onixPL:MediaWarranty"),

    /**
     * A license term that specifies that the licensor will supply catalog records or other metadata describing licensed content.
     */
    @XmlEnumValue("onixPL:MetadataSupply")
    ONIX_PL_METADATA_SUPPLY("onixPL:MetadataSupply"),

    /**
     * A license term that specifies the supply of licensed content by network access to a licensor location.
     */
    @XmlEnumValue("onixPL:NetworkAccess")
    ONIX_PL_NETWORK_ACCESS("onixPL:NetworkAccess"),

    /**
     * A license term that specifies conditions relating to the provision of product documentation by the licensor.
     */
    @XmlEnumValue("onixPL:ProductDocumentation")
    ONIX_PL_PRODUCT_DOCUMENTATION("onixPL:ProductDocumentation"),

    /**
     * A license term that specifies conditions relating to a schedule according to which the licensor is expected to release licensed content.
     */
    @XmlEnumValue("onixPL:PublicationSchedule")
    ONIX_PL_PUBLICATION_SCHEDULE("onixPL:PublicationSchedule"),

    /**
     * A license term that specifies service performance but does not guarantee a particular level.
     */
    @XmlEnumValue("onixPL:ServicePerformance")
    ONIX_PL_SERVICE_PERFORMANCE("onixPL:ServicePerformance"),

    /**
     * A license term that guarantees a stated service performance level.
     */
    @XmlEnumValue("onixPL:ServicePerformanceGuarantee")
    ONIX_PL_SERVICE_PERFORMANCE_GUARANTEE("onixPL:ServicePerformanceGuarantee"),

    /**
     * A license term that specifies conditions relating to the start of a service to be supplied under a license.
     */
    @XmlEnumValue("onixPL:StartOfService")
    ONIX_PL_START_OF_SERVICE("onixPL:StartOfService"),

    /**
     * A license term that specifies conditions relating to the provision of usage statistics.
     */
    @XmlEnumValue("onixPL:UsageStatistics")
    ONIX_PL_USAGE_STATISTICS("onixPL:UsageStatistics"),

    /**
     * A license term that specifies conditions relating to user registration.
     */
    @XmlEnumValue("onixPL:UserRegistration")
    ONIX_PL_USER_REGISTRATION("onixPL:UserRegistration"),

    /**
     * A license term that specifies conditions relating to the provision of user support by the licensor.
     */
    @XmlEnumValue("onixPL:UserSupport")
    ONIX_PL_USER_SUPPORT("onixPL:UserSupport");
    private final String value;

    SupplyTermTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SupplyTermTypeCode fromValue(String v) {
        for (SupplyTermTypeCode c : SupplyTermTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
