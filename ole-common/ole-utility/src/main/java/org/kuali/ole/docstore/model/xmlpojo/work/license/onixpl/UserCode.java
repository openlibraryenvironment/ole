
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
 * <p>Java class for UserCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UserCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AuthorizedUser"/>
 *     &lt;enumeration value="onixPL:ExternalAcademic"/>
 *     &lt;enumeration value="onixPL:ExternalLibrarian"/>
 *     &lt;enumeration value="onixPL:ExternalStudent"/>
 *     &lt;enumeration value="onixPL:ExternalTeacher"/>
 *     &lt;enumeration value="onixPL:ExternalTeacherInCountryOfLicensee"/>
 *     &lt;enumeration value="onixPL:LibraryUserUnaffiliated"/>
 *     &lt;enumeration value="onixPL:Licensee"/>
 *     &lt;enumeration value="onixPL:LicenseeAlumnus"/>
 *     &lt;enumeration value="onixPL:LicenseeAuxiliary"/>
 *     &lt;enumeration value="onixPL:LicenseeContractor"/>
 *     &lt;enumeration value="onixPL:LicenseeContractorOrganization"/>
 *     &lt;enumeration value="onixPL:LicenseeContractorStaff"/>
 *     &lt;enumeration value="onixPL:LicenseeDistanceLearningStudent"/>
 *     &lt;enumeration value="onixPL:LicenseeExternalStudent"/>
 *     &lt;enumeration value="onixPL:LicenseeFaculty"/>
 *     &lt;enumeration value="onixPL:LicenseeInternalStudent"/>
 *     &lt;enumeration value="onixPL:LicenseeLibrary"/>
 *     &lt;enumeration value="onixPL:LicenseeLibraryStaff"/>
 *     &lt;enumeration value="onixPL:LicenseeNonFacultyStaff"/>
 *     &lt;enumeration value="onixPL:LicenseeResearcher"/>
 *     &lt;enumeration value="onixPL:LicenseeRetiredStaff"/>
 *     &lt;enumeration value="onixPL:LicenseeStaff"/>
 *     &lt;enumeration value="onixPL:LicenseeStudent"/>
 *     &lt;enumeration value="onixPL:OtherTeacherOfAuthorizedUsers"/>
 *     &lt;enumeration value="onixPL:RegulatoryAuthority"/>
 *     &lt;enumeration value="onixPL:ResearchSponsor"/>
 *     &lt;enumeration value="onixPL:ThirdParty"/>
 *     &lt;enumeration value="onixPL:ThirdPartyLibrary"/>
 *     &lt;enumeration value="onixPL:ThirdPartyNonCommercialLibrary"/>
 *     &lt;enumeration value="onixPL:ThirdPartyOrganization"/>
 *     &lt;enumeration value="onixPL:ThirdPartyPerson"/>
 *     &lt;enumeration value="onixPL:WalkInUser"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UserCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UserCode {


    /**
     * An agent that is authorized to use licensed content under a license.
     */
    @XmlEnumValue("onixPL:AuthorizedUser")
    ONIX_PL_AUTHORIZED_USER("onixPL:AuthorizedUser"),

    /**
     * An academic, researcher or research supervisor, not employed as a staff member of a licensee academic institution, who is using the resources of the institution in the furtherance of academic work, and who is permitted to use the institution's library and information services.
     */
    @XmlEnumValue("onixPL:ExternalAcademic")
    ONIX_PL_EXTERNAL_ACADEMIC("onixPL:ExternalAcademic"),

    /**
     * A person not employed as a staff member of a licensee organization, who is engaged in the provision of library services to Authorized Users, and who is permitted to use the licensee's library and/or information services.
     */
    @XmlEnumValue("onixPL:ExternalLibrarian")
    ONIX_PL_EXTERNAL_LIBRARIAN("onixPL:ExternalLibrarian"),

    /**
     * A person who is using the resources of a licensee academic institution in the furtherance of a course of study, but who is not otherwise affiliated to the institution, and who is permitted to use the institution's library and information services..
     */
    @XmlEnumValue("onixPL:ExternalStudent")
    ONIX_PL_EXTERNAL_STUDENT("onixPL:ExternalStudent"),

    /**
     * A person not employed as a staff member of a licensee academic institution who teaches Authorized Users and who is permitted to use the institution's library and information services.
     */
    @XmlEnumValue("onixPL:ExternalTeacher")
    ONIX_PL_EXTERNAL_TEACHER("onixPL:ExternalTeacher"),

    /**
     * A person not employed as a staff member of a licensee academic institution who teaches Authorized Users in the country of the licensee and who is permitted to use the institution's library and information services.
     */
    @XmlEnumValue("onixPL:ExternalTeacherInCountryOfLicensee")
    ONIX_PL_EXTERNAL_TEACHER_IN_COUNTRY_OF_LICENSEE("onixPL:ExternalTeacherInCountryOfLicensee"),

    /**
     * A person not otherwise affiliated with a licensee organization who is permitted by a licensee to use library and information resources.
     */
    @XmlEnumValue("onixPL:LibraryUserUnaffiliated")
    ONIX_PL_LIBRARY_USER_UNAFFILIATED("onixPL:LibraryUserUnaffiliated"),

    /**
     * An agent to whom a license is granted.
     */
    @XmlEnumValue("onixPL:Licensee")
    ONIX_PL_LICENSEE("onixPL:Licensee"),

    /**
     * A person who is a former student of a licensee academic institution and who is permitted to use the institution's library and/or information services.
     */
    @XmlEnumValue("onixPL:LicenseeAlumnus")
    ONIX_PL_LICENSEE_ALUMNUS("onixPL:LicenseeAlumnus"),

    /**
     * A person providing services to, but not employed as a staff member of, a licensee organization, and who is permitted to use the licensee's library and/or information services; including but not limited to a person working as a contractor to a licensee.
     */
    @XmlEnumValue("onixPL:LicenseeAuxiliary")
    ONIX_PL_LICENSEE_AUXILIARY("onixPL:LicenseeAuxiliary"),

    /**
     * A person or organization working as a contractor to a licensee.
     */
    @XmlEnumValue("onixPL:LicenseeContractor")
    ONIX_PL_LICENSEE_CONTRACTOR("onixPL:LicenseeContractor"),

    /**
     * An organization working as a contractor to a licensee.
     */
    @XmlEnumValue("onixPL:LicenseeContractorOrganization")
    ONIX_PL_LICENSEE_CONTRACTOR_ORGANIZATION("onixPL:LicenseeContractorOrganization"),

    /**
     * A person working as a contractor to a licensee or employed by an organization working as a contractor to a licensee, and who is permitted to use the institution's library and/or information services.
     */
    @XmlEnumValue("onixPL:LicenseeContractorStaff")
    ONIX_PL_LICENSEE_CONTRACTOR_STAFF("onixPL:LicenseeContractorStaff"),

    /**
     * A person who is registered with a licensee academic institution as a student at any level, and who is undertaking a course of study by distance learning.
     */
    @XmlEnumValue("onixPL:LicenseeDistanceLearningStudent")
    ONIX_PL_LICENSEE_DISTANCE_LEARNING_STUDENT("onixPL:LicenseeDistanceLearningStudent"),

    /**
     * A person who is not a registered student of a licensee academic institution but who is studying for a qualification awarded by the licensee, and who is permitted to use the institution's library and/or information services.
     */
    @XmlEnumValue("onixPL:LicenseeExternalStudent")
    ONIX_PL_LICENSEE_EXTERNAL_STUDENT("onixPL:LicenseeExternalStudent"),

    /**
     * A person employed as a full-time or part-time academic staff member of a licensee academic institution.
     */
    @XmlEnumValue("onixPL:LicenseeFaculty")
    ONIX_PL_LICENSEE_FACULTY("onixPL:LicenseeFaculty"),

    /**
     * A person who is registered with a licensee academic institution as a student at any level, and who is undertaking a course of study at a licensee site, and not by distance learning.
     */
    @XmlEnumValue("onixPL:LicenseeInternalStudent")
    ONIX_PL_LICENSEE_INTERNAL_STUDENT("onixPL:LicenseeInternalStudent"),

    /**
     * A library that is part of a licensee organization.
     */
    @XmlEnumValue("onixPL:LicenseeLibrary")
    ONIX_PL_LICENSEE_LIBRARY("onixPL:LicenseeLibrary"),

    /**
     * A person employed as a full-time or part-time staff member of a library that is part of a licensee organization.
     */
    @XmlEnumValue("onixPL:LicenseeLibraryStaff")
    ONIX_PL_LICENSEE_LIBRARY_STAFF("onixPL:LicenseeLibraryStaff"),

    /**
     * A person employed as a full-time or part-time non-academic staff member of a licensee academic institution.
     */
    @XmlEnumValue("onixPL:LicenseeNonFacultyStaff")
    ONIX_PL_LICENSEE_NON_FACULTY_STAFF("onixPL:LicenseeNonFacultyStaff"),

    /**
     * A person undertaking research within, and authorized by, a licensee academic institution.
     */
    @XmlEnumValue("onixPL:LicenseeResearcher")
    ONIX_PL_LICENSEE_RESEARCHER("onixPL:LicenseeResearcher"),

    /**
     * A person who has retired from employment as a staff member of a licensee organization, and who is permitted to use the organization's library and/or information services.
     */
    @XmlEnumValue("onixPL:LicenseeRetiredStaff")
    ONIX_PL_LICENSEE_RETIRED_STAFF("onixPL:LicenseeRetiredStaff"),

    /**
     * A person employed as a full-time or part-time staff member of a licensee organization, including (for academic institutions) faculty or teaching staff, library staff, and non-faculty or non-teaching staff.
     */
    @XmlEnumValue("onixPL:LicenseeStaff")
    ONIX_PL_LICENSEE_STAFF("onixPL:LicenseeStaff"),

    /**
     * A person who is registered with a licensee academic institution as a student at any level, including one who is undertaking a course of study by distance learning.
     */
    @XmlEnumValue("onixPL:LicenseeStudent")
    ONIX_PL_LICENSEE_STUDENT("onixPL:LicenseeStudent"),

    /**
     * DEPRECATED - use ExternalTeacher [A person, not employed as a staff member of a licensee academic institution, who teaches Authorized Users, and who is permitted to use the institution's library and/or information services.]
     */
    @XmlEnumValue("onixPL:OtherTeacherOfAuthorizedUsers")
    ONIX_PL_OTHER_TEACHER_OF_AUTHORIZED_USERS("onixPL:OtherTeacherOfAuthorizedUsers"),

    /**
     * A national or international regulatory authority.
     */
    @XmlEnumValue("onixPL:RegulatoryAuthority")
    ONIX_PL_REGULATORY_AUTHORITY("onixPL:RegulatoryAuthority"),

    /**
     * A person or organization that sponsors research at a licensee academic institution.
     */
    @XmlEnumValue("onixPL:ResearchSponsor")
    ONIX_PL_RESEARCH_SPONSOR("onixPL:ResearchSponsor"),

    /**
     * A person or organization that is not a party to, or an authorized user under, a license.
     */
    @XmlEnumValue("onixPL:ThirdParty")
    ONIX_PL_THIRD_PARTY("onixPL:ThirdParty"),

    /**
     * A library that is, or is part of, a third party organization.
     */
    @XmlEnumValue("onixPL:ThirdPartyLibrary")
    ONIX_PL_THIRD_PARTY_LIBRARY("onixPL:ThirdPartyLibrary"),

    /**
     * A library that is, or is part of, a third party non-commercial organization.
     */
    @XmlEnumValue("onixPL:ThirdPartyNonCommercialLibrary")
    ONIX_PL_THIRD_PARTY_NON_COMMERCIAL_LIBRARY("onixPL:ThirdPartyNonCommercialLibrary"),

    /**
     * An organization that is not a party to, or an authorized user under, a license.
     */
    @XmlEnumValue("onixPL:ThirdPartyOrganization")
    ONIX_PL_THIRD_PARTY_ORGANIZATION("onixPL:ThirdPartyOrganization"),

    /**
     * A person that is not a party to, or an authorized user under, a license.
     */
    @XmlEnumValue("onixPL:ThirdPartyPerson")
    ONIX_PL_THIRD_PARTY_PERSON("onixPL:ThirdPartyPerson"),

    /**
     * A person not otherwise affiliated with a licensee organization who is permitted by a licensee to use library and information resources on the licensee's site only.
     */
    @XmlEnumValue("onixPL:WalkInUser")
    ONIX_PL_WALK_IN_USER("onixPL:WalkInUser");
    private final String value;

    UserCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UserCode fromValue(String v) {
        for (UserCode c : UserCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
