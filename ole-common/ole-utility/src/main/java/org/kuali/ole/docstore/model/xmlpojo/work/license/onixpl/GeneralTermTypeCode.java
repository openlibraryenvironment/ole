
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
 * <p>Java class for GeneralTermTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="GeneralTermTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:ActionAgainstMisuse"/>
 *     &lt;enumeration value="onixPL:ActionOnTermination"/>
 *     &lt;enumeration value="onixPL:AdmissionToPremises"/>
 *     &lt;enumeration value="onixPL:AllRightsReserved"/>
 *     &lt;enumeration value="onixPL:Amendment"/>
 *     &lt;enumeration value="onixPL:ApplicableCopyrightLaw"/>
 *     &lt;enumeration value="onixPL:AppointmentOfPersonnel"/>
 *     &lt;enumeration value="onixPL:Assignment"/>
 *     &lt;enumeration value="onixPL:AuthorizedUserRecords"/>
 *     &lt;enumeration value="onixPL:ClaimByThirdParty"/>
 *     &lt;enumeration value="onixPL:ClickThroughOverride"/>
 *     &lt;enumeration value="onixPL:ConfidentialInformation"/>
 *     &lt;enumeration value="onixPL:ConfidentialityOfAgreement"/>
 *     &lt;enumeration value="onixPL:ConfidentialityOfPasswords"/>
 *     &lt;enumeration value="onixPL:ConfidentialityOfUserData"/>
 *     &lt;enumeration value="onixPL:CostRecoveryByLicensee"/>
 *     &lt;enumeration value="onixPL:DamageToLicensorsBusiness"/>
 *     &lt;enumeration value="onixPL:DatabaseProtectionOverride"/>
 *     &lt;enumeration value="onixPL:DisputeResolution"/>
 *     &lt;enumeration value="onixPL:EntireAgreement"/>
 *     &lt;enumeration value="onixPL:ForceMajeure"/>
 *     &lt;enumeration value="onixPL:FundingContingency"/>
 *     &lt;enumeration value="onixPL:GoverningLaw"/>
 *     &lt;enumeration value="onixPL:IndemnityAgainstBreach"/>
 *     &lt;enumeration value="onixPL:IntellectualPropertyRights"/>
 *     &lt;enumeration value="onixPL:Jurisdiction"/>
 *     &lt;enumeration value="onixPL:LicenseeIndemnity"/>
 *     &lt;enumeration value="onixPL:LicenseeMerger"/>
 *     &lt;enumeration value="onixPL:LicenseeProvisionOfResources"/>
 *     &lt;enumeration value="onixPL:LicenseeRightsAfterTermination"/>
 *     &lt;enumeration value="onixPL:LicenseeTerminationRight"/>
 *     &lt;enumeration value="onixPL:LicenseeWarranty"/>
 *     &lt;enumeration value="onixPL:LicensorIndemnity"/>
 *     &lt;enumeration value="onixPL:LicensorIntellectualPropertyWarranty"/>
 *     &lt;enumeration value="onixPL:LicensorTerminationRight"/>
 *     &lt;enumeration value="onixPL:LicensorWarrantyDisclaimer"/>
 *     &lt;enumeration value="onixPL:LimitationOfLiability"/>
 *     &lt;enumeration value="onixPL:MemberLeavingConsortium"/>
 *     &lt;enumeration value="onixPL:MonitoringAndDetectionOfMisuse"/>
 *     &lt;enumeration value="onixPL:NonEnforceability"/>
 *     &lt;enumeration value="onixPL:NoPartnership"/>
 *     &lt;enumeration value="onixPL:NoticeProcedure"/>
 *     &lt;enumeration value="onixPL:NotificationOfLicenseeIPAddresses"/>
 *     &lt;enumeration value="onixPL:NotificationOfMisuse"/>
 *     &lt;enumeration value="onixPL:NotificationOfTermsToAuthorizedUsers"/>
 *     &lt;enumeration value="onixPL:NoWaiver"/>
 *     &lt;enumeration value="onixPL:PreventionOfMisuse"/>
 *     &lt;enumeration value="onixPL:Publicity"/>
 *     &lt;enumeration value="onixPL:Renewal"/>
 *     &lt;enumeration value="onixPL:SecurityProtocols"/>
 *     &lt;enumeration value="onixPL:StatutoryRightsAffirmation"/>
 *     &lt;enumeration value="onixPL:SubLicenseTerminationByTerminationOfParentLicense"/>
 *     &lt;enumeration value="onixPL:TerminationByBankruptcy"/>
 *     &lt;enumeration value="onixPL:TerminationByBreach"/>
 *     &lt;enumeration value="onixPL:TerminationByNotice"/>
 *     &lt;enumeration value="onixPL:TerminationWithoutPrejudiceToRights"/>
 *     &lt;enumeration value="onixPL:TermOfAgreement"/>
 *     &lt;enumeration value="onixPL:TermsSurvivingTermination"/>
 *     &lt;enumeration value="onixPL:ThirdPartyRights"/>
 *     &lt;enumeration value="onixPL:TrademarkProtection"/>
 *     &lt;enumeration value="onixPL:UCITAOverride"/>
 *     &lt;enumeration value="onixPL:UseOfDRMTechnology"/>
 *     &lt;enumeration value="onixPL:UseOfDigitalWatermarking"/>
 *     &lt;enumeration value="onixPL:UserFeedback"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "GeneralTermTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum GeneralTermTypeCode {


    /**
     * A license term that specifies conditions relating to action to be taken by a party as a consequence of misuse of licensed content.
     */
    @XmlEnumValue("onixPL:ActionAgainstMisuse")
    ONIX_PL_ACTION_AGAINST_MISUSE("onixPL:ActionAgainstMisuse"),

    /**
     * A license term that specifies action to be taken by a party on termination of a license.
     */
    @XmlEnumValue("onixPL:ActionOnTermination")
    ONIX_PL_ACTION_ON_TERMINATION("onixPL:ActionOnTermination"),

    /**
     * A license term that specifies conditions relating to the admission of a party's personnel to the premises of another party.
     */
    @XmlEnumValue("onixPL:AdmissionToPremises")
    ONIX_PL_ADMISSION_TO_PREMISES("onixPL:AdmissionToPremises"),

    /**
     * A license term that specifies that a licensor retains all intellectual property rights other than those explicitly licensed.
     */
    @XmlEnumValue("onixPL:AllRightsReserved")
    ONIX_PL_ALL_RIGHTS_RESERVED("onixPL:AllRightsReserved"),

    /**
     * A license term that specifies conditions relating to the amendment of the terms of a license.
     */
    @XmlEnumValue("onixPL:Amendment")
    ONIX_PL_AMENDMENT("onixPL:Amendment"),

    /**
     * A license term that specifies the national copyright law that applies to a license.
     */
    @XmlEnumValue("onixPL:ApplicableCopyrightLaw")
    ONIX_PL_APPLICABLE_COPYRIGHT_LAW("onixPL:ApplicableCopyrightLaw"),

    /**
     * A license term that specifies an obligation on a party to a license to appoint persons to perform specified functions related to the license.
     */
    @XmlEnumValue("onixPL:AppointmentOfPersonnel")
    ONIX_PL_APPOINTMENT_OF_PERSONNEL("onixPL:AppointmentOfPersonnel"),

    /**
     * A license term that specifies conditions relating to the assignment or novation of all or part of a license to a third party.
     */
    @XmlEnumValue("onixPL:Assignment")
    ONIX_PL_ASSIGNMENT("onixPL:Assignment"),

    /**
     * A license term that specifies conditions relating to the maintenance and sharing of authorized user records.
     */
    @XmlEnumValue("onixPL:AuthorizedUserRecords")
    ONIX_PL_AUTHORIZED_USER_RECORDS("onixPL:AuthorizedUserRecords"),

    /**
     * A license term that specifies conditions relating to the handling of any claim by a third party relating to licensed content.
     */
    @XmlEnumValue("onixPL:ClaimByThirdParty")
    ONIX_PL_CLAIM_BY_THIRD_PARTY("onixPL:ClaimByThirdParty"),

    /**
     * A license term that specifies that the terms of a license override the terms of any 'click-through' or similar terms carried on the licensor's server.
     */
    @XmlEnumValue("onixPL:ClickThroughOverride")
    ONIX_PL_CLICK_THROUGH_OVERRIDE("onixPL:ClickThroughOverride"),

    /**
     * A license term that specifies an obligation on a party to a license to keep confidential any information that is confidential to the other party.
     */
    @XmlEnumValue("onixPL:ConfidentialInformation")
    ONIX_PL_CONFIDENTIAL_INFORMATION("onixPL:ConfidentialInformation"),

    /**
     * A license term that specifies an obligation to maintain the confidentiality of some or all of the terms of a license.
     */
    @XmlEnumValue("onixPL:ConfidentialityOfAgreement")
    ONIX_PL_CONFIDENTIALITY_OF_AGREEMENT("onixPL:ConfidentialityOfAgreement"),

    /**
     * A license term that specifies conditions relating to the confidentiality of user passwords.
     */
    @XmlEnumValue("onixPL:ConfidentialityOfPasswords")
    ONIX_PL_CONFIDENTIALITY_OF_PASSWORDS("onixPL:ConfidentialityOfPasswords"),

    /**
     * A license term that specifies a licensor's obligation to maintain the confidentiality of user data.
     */
    @XmlEnumValue("onixPL:ConfidentialityOfUserData")
    ONIX_PL_CONFIDENTIALITY_OF_USER_DATA("onixPL:ConfidentialityOfUserData"),

    /**
     * A license term that specifies conditions relating to cost recovery charges made by a licensee.
     */
    @XmlEnumValue("onixPL:CostRecoveryByLicensee")
    ONIX_PL_COST_RECOVERY_BY_LICENSEE("onixPL:CostRecoveryByLicensee"),

    /**
     * A license term that prohibits actions that might damage the licensor's business.
     */
    @XmlEnumValue("onixPL:DamageToLicensorsBusiness")
    ONIX_PL_DAMAGE_TO_LICENSORS_BUSINESS("onixPL:DamageToLicensorsBusiness"),

    /**
     * A license term that (in the US) specifies fair use protections within the context of assertions of database protection or additional proprietary rights related to database content not currently covered by US copyright law.
     */
    @XmlEnumValue("onixPL:DatabaseProtectionOverride")
    ONIX_PL_DATABASE_PROTECTION_OVERRIDE("onixPL:DatabaseProtectionOverride"),

    /**
     * A license term that specifies procedures, short of legal action, to be followed in the event of a dispute between the parties to a license.
     */
    @XmlEnumValue("onixPL:DisputeResolution")
    ONIX_PL_DISPUTE_RESOLUTION("onixPL:DisputeResolution"),

    /**
     * A license term that specifies that a license represents the entire agreement between the parties in respect of the subject of the license.
     */
    @XmlEnumValue("onixPL:EntireAgreement")
    ONIX_PL_ENTIRE_AGREEMENT("onixPL:EntireAgreement"),

    /**
     * A license term that specifies conditions that apply in circumstances outside the control of the parties to a license.
     */
    @XmlEnumValue("onixPL:ForceMajeure")
    ONIX_PL_FORCE_MAJEURE("onixPL:ForceMajeure"),

    /**
     * A license term that specifies conditions relating to a funding contingency arising in a licensee organization.
     */
    @XmlEnumValue("onixPL:FundingContingency")
    ONIX_PL_FUNDING_CONTINGENCY("onixPL:FundingContingency"),

    /**
     * A license term that specifies the governing law that applies to a license.
     */
    @XmlEnumValue("onixPL:GoverningLaw")
    ONIX_PL_GOVERNING_LAW("onixPL:GoverningLaw"),

    /**
     * A license term that specifies indemnities offered by either party against breach of terms contained in a license.
     */
    @XmlEnumValue("onixPL:IndemnityAgainstBreach")
    ONIX_PL_INDEMNITY_AGAINST_BREACH("onixPL:IndemnityAgainstBreach"),

    /**
     * A license term that specifies conditions relating to copyright, database right or other intellectual property rights.
     */
    @XmlEnumValue("onixPL:IntellectualPropertyRights")
    ONIX_PL_INTELLECTUAL_PROPERTY_RIGHTS("onixPL:IntellectualPropertyRights"),

    /**
     * A license term that specifies the jurisdiction that applies to a license.
     */
    @XmlEnumValue("onixPL:Jurisdiction")
    ONIX_PL_JURISDICTION("onixPL:Jurisdiction"),

    /**
     * A license term by which a licensee indemnifies a licensor against third party claims.
     */
    @XmlEnumValue("onixPL:LicenseeIndemnity")
    ONIX_PL_LICENSEE_INDEMNITY("onixPL:LicenseeIndemnity"),

    /**
     * A license term that specifies conditions relating to the merger of the licensee with another organization.
     */
    @XmlEnumValue("onixPL:LicenseeMerger")
    ONIX_PL_LICENSEE_MERGER("onixPL:LicenseeMerger"),

    /**
     * A license term that specifies the licensee's responsibility for provision of resources needed to access and use licensed content.
     */
    @XmlEnumValue("onixPL:LicenseeProvisionOfResources")
    ONIX_PL_LICENSEE_PROVISION_OF_RESOURCES("onixPL:LicenseeProvisionOfResources"),

    /**
     * A license term that specifies rights that continue to be held by a licensee after termination of a license, other than those associated with Continuing Access Terms.
     */
    @XmlEnumValue("onixPL:LicenseeRightsAfterTermination")
    ONIX_PL_LICENSEE_RIGHTS_AFTER_TERMINATION("onixPL:LicenseeRightsAfterTermination"),

    /**
     * The ability of the licensee to terminate a license in whole or in part during a contract period.
     */
    @XmlEnumValue("onixPL:LicenseeTerminationRight")
    ONIX_PL_LICENSEE_TERMINATION_RIGHT("onixPL:LicenseeTerminationRight"),

    /**
     * A license term that specifies a warranty given by a licensee, typically that it has the necessary rights to enter into an agreement.
     */
    @XmlEnumValue("onixPL:LicenseeWarranty")
    ONIX_PL_LICENSEE_WARRANTY("onixPL:LicenseeWarranty"),

    /**
     * A license term by which a licensor indemnifies a licensee against third party claims.
     */
    @XmlEnumValue("onixPL:LicensorIndemnity")
    ONIX_PL_LICENSOR_INDEMNITY("onixPL:LicensorIndemnity"),

    /**
     * A license term by which a licensor warrants that it holds all intellectual property rights required in order to grant a license.
     */
    @XmlEnumValue("onixPL:LicensorIntellectualPropertyWarranty")
    ONIX_PL_LICENSOR_INTELLECTUAL_PROPERTY_WARRANTY("onixPL:LicensorIntellectualPropertyWarranty"),

    /**
     * The ability of the licensor to terminate a license in whole or in part during a contract period.
     */
    @XmlEnumValue("onixPL:LicensorTerminationRight")
    ONIX_PL_LICENSOR_TERMINATION_RIGHT("onixPL:LicensorTerminationRight"),

    /**
     * A license term by which a licensor disclaims warranties in connection with a license.
     */
    @XmlEnumValue("onixPL:LicensorWarrantyDisclaimer")
    ONIX_PL_LICENSOR_WARRANTY_DISCLAIMER("onixPL:LicensorWarrantyDisclaimer"),

    /**
     * A license term that specifies a limitation of a partys liability under a license.
     */
    @XmlEnumValue("onixPL:LimitationOfLiability")
    ONIX_PL_LIMITATION_OF_LIABILITY("onixPL:LimitationOfLiability"),

    /**
     * A license term that specifies conditions relating to a member that leaves a consortium during the term of a license.
     */
    @XmlEnumValue("onixPL:MemberLeavingConsortium")
    ONIX_PL_MEMBER_LEAVING_CONSORTIUM("onixPL:MemberLeavingConsortium"),

    /**
     * A license term that specifies conditions relating to the monitoring and/or detection of misuse of licensed content.
     */
    @XmlEnumValue("onixPL:MonitoringAndDetectionOfMisuse")
    ONIX_PL_MONITORING_AND_DETECTION_OF_MISUSE("onixPL:MonitoringAndDetectionOfMisuse"),

    /**
     * A license term that specifies that the non-enforceability of any part of a license shall not invalidate the rest of the license.
     */
    @XmlEnumValue("onixPL:NonEnforceability")
    ONIX_PL_NON_ENFORCEABILITY("onixPL:NonEnforceability"),

    /**
     * A license term that specifies that no partnership or joint venture is created between the parties.
     */
    @XmlEnumValue("onixPL:NoPartnership")
    ONIX_PL_NO_PARTNERSHIP("onixPL:NoPartnership"),

    /**
     * A license term that specifies the procedure by which notices must be served under a license.
     */
    @XmlEnumValue("onixPL:NoticeProcedure")
    ONIX_PL_NOTICE_PROCEDURE("onixPL:NoticeProcedure"),

    /**
     * A license term that specifies a licensee's obligation to notify IP addresses to be used by the licensor to authenticate authorized users.
     */
    @XmlEnumValue("onixPL:NotificationOfLicenseeIPAddresses")
    ONIX_PL_NOTIFICATION_OF_LICENSEE_IP_ADDRESSES("onixPL:NotificationOfLicenseeIPAddresses"),

    /**
     * A license term that specifies an obligation to notify any misuse of licensed content of which a party becomes aware.
     */
    @XmlEnumValue("onixPL:NotificationOfMisuse")
    ONIX_PL_NOTIFICATION_OF_MISUSE("onixPL:NotificationOfMisuse"),

    /**
     * A license term that specifies a licensee's obligation to provide information to authorized users about the applicable terms of a license.
     */
    @XmlEnumValue("onixPL:NotificationOfTermsToAuthorizedUsers")
    ONIX_PL_NOTIFICATION_OF_TERMS_TO_AUTHORIZED_USERS("onixPL:NotificationOfTermsToAuthorizedUsers"),

    /**
     * A license term that specifies that the failure by a party to enforce any part of a license shall not constitute a waiver of any other part.
     */
    @XmlEnumValue("onixPL:NoWaiver")
    ONIX_PL_NO_WAIVER("onixPL:NoWaiver"),

    /**
     * A license term that specifies conditions relating to the prevention of unauthorized use or any other misuse of licensed content.
     */
    @XmlEnumValue("onixPL:PreventionOfMisuse")
    ONIX_PL_PREVENTION_OF_MISUSE("onixPL:PreventionOfMisuse"),

    /**
     * A license term that specifies conditions relating to publicity, advertising or promotion connected with a license and/or using the name of a party to a license.
     */
    @XmlEnumValue("onixPL:Publicity")
    ONIX_PL_PUBLICITY("onixPL:Publicity"),

    /**
     * A license term that specifies conditions relating to the renewal of a license.
     */
    @XmlEnumValue("onixPL:Renewal")
    ONIX_PL_RENEWAL("onixPL:Renewal"),

    /**
     * A license term that specifies conditions relating to the implementation of security and authentication protocols.
     */
    @XmlEnumValue("onixPL:SecurityProtocols")
    ONIX_PL_SECURITY_PROTOCOLS("onixPL:SecurityProtocols"),

    /**
     * A license term that affirms that a license does not detract from a licensee's statutory rights under copyright law and/or other applicable law.
     */
    @XmlEnumValue("onixPL:StatutoryRightsAffirmation")
    ONIX_PL_STATUTORY_RIGHTS_AFFIRMATION("onixPL:StatutoryRightsAffirmation"),

    /**
     * A license term that specifies conditions relating to the termination of a sub-license as a consequence of termination of the parent license.
     */
    @XmlEnumValue("onixPL:SubLicenseTerminationByTerminationOfParentLicense")
    ONIX_PL_SUB_LICENSE_TERMINATION_BY_TERMINATION_OF_PARENT_LICENSE("onixPL:SubLicenseTerminationByTerminationOfParentLicense"),

    /**
     * A license term that specifies conditions relating to the termination of a license by reason of the bankruptcy or similar default of one of the parties.
     */
    @XmlEnumValue("onixPL:TerminationByBankruptcy")
    ONIX_PL_TERMINATION_BY_BANKRUPTCY("onixPL:TerminationByBankruptcy"),

    /**
     * A license term that specifies conditions relating to the termination of a license by reason of breach by one of the parties.
     */
    @XmlEnumValue("onixPL:TerminationByBreach")
    ONIX_PL_TERMINATION_BY_BREACH("onixPL:TerminationByBreach"),

    /**
     * A license term that specifies conditions relating to the termination of a license by notice given by one of the parties, unrelated to breach or other default.
     */
    @XmlEnumValue("onixPL:TerminationByNotice")
    ONIX_PL_TERMINATION_BY_NOTICE("onixPL:TerminationByNotice"),

    /**
     * A license term that specifies that termination of the license is without prejudice to the rights and remedies of either party.
     */
    @XmlEnumValue("onixPL:TerminationWithoutPrejudiceToRights")
    ONIX_PL_TERMINATION_WITHOUT_PREJUDICE_TO_RIGHTS("onixPL:TerminationWithoutPrejudiceToRights"),

    /**
     * A license term that specifies the intended duration of a license.
     */
    @XmlEnumValue("onixPL:TermOfAgreement")
    ONIX_PL_TERM_OF_AGREEMENT("onixPL:TermOfAgreement"),

    /**
     * A license term that specifies those other terms of a license that will survive termination.
     */
    @XmlEnumValue("onixPL:TermsSurvivingTermination")
    ONIX_PL_TERMS_SURVIVING_TERMINATION("onixPL:TermsSurvivingTermination"),

    /**
     * A license term that specifies conditions relating to rights of enforcement by third parties.
     */
    @XmlEnumValue("onixPL:ThirdPartyRights")
    ONIX_PL_THIRD_PARTY_RIGHTS("onixPL:ThirdPartyRights"),

    /**
     * A license term that specifies conditions relating to the protection of trademarks, logos, proprietary names, etc.
     */
    @XmlEnumValue("onixPL:TrademarkProtection")
    ONIX_PL_TRADEMARK_PROTECTION("onixPL:TrademarkProtection"),

    /**
     * A license term that reflects the licensors agreement to use US state contract law in the event UCITA is ever passed and implemented in the specified governing law state.
     */
    @XmlEnumValue("onixPL:UCITAOverride")
    ONIX_PL_UCITA_OVERRIDE("onixPL:UCITAOverride"),

    /**
     * A license term that specifies conditions relating to the use of Digital Rights Management technology.
     */
    @XmlEnumValue("onixPL:UseOfDRMTechnology")
    ONIX_PL_USE_OF_DRM_TECHNOLOGY("onixPL:UseOfDRMTechnology"),

    /**
     * A license term that specifies conditions relating to the use of digital watermarking.
     */
    @XmlEnumValue("onixPL:UseOfDigitalWatermarking")
    ONIX_PL_USE_OF_DIGITAL_WATERMARKING("onixPL:UseOfDigitalWatermarking"),

    /**
     * A license term that specifies conditions relating to feedback from users of licensed content.
     */
    @XmlEnumValue("onixPL:UserFeedback")
    ONIX_PL_USER_FEEDBACK("onixPL:UserFeedback");
    private final String value;

    GeneralTermTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GeneralTermTypeCode fromValue(String v) {
        for (GeneralTermTypeCode c : GeneralTermTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
