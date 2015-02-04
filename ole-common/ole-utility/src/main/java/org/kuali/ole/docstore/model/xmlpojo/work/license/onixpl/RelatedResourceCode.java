
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
 * <p>Java class for RelatedResourceCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="RelatedResourceCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AcademicWork"/>
 *     &lt;enumeration value="onixPL:AcademicWorkIncludingLicensedContent"/>
 *     &lt;enumeration value="onixPL:AcknowledgmentOfSource"/>
 *     &lt;enumeration value="onixPL:AuthoredContent"/>
 *     &lt;enumeration value="onixPL:AuthoredContentPeerReviewedCopy"/>
 *     &lt;enumeration value="onixPL:AuthorizedUserOwnWork"/>
 *     &lt;enumeration value="onixPL:CatalogOrInformationSystem"/>
 *     &lt;enumeration value="onixPL:CombinedWorkIncludingLicensedContent"/>
 *     &lt;enumeration value="onixPL:CompleteArticle"/>
 *     &lt;enumeration value="onixPL:CompleteBook"/>
 *     &lt;enumeration value="onixPL:CompleteChapter"/>
 *     &lt;enumeration value="onixPL:CompleteIssue"/>
 *     &lt;enumeration value="onixPL:CopyrightNotice"/>
 *     &lt;enumeration value="onixPL:CopyrightNoticesOrDisclaimers"/>
 *     &lt;enumeration value="onixPL:CoursePackPrinted"/>
 *     &lt;enumeration value="onixPL:CourseReservePrinted"/>
 *     &lt;enumeration value="onixPL:DataFromLicensedContent"/>
 *     &lt;enumeration value="onixPL:DerivedWork"/>
 *     &lt;enumeration value="onixPL:DigitalInstructionalMaterial"/>
 *     &lt;enumeration value="onixPL:DigitalInstructionalMaterialIncludingLicensedContent"/>
 *     &lt;enumeration value="onixPL:DigitalInstructionalMaterialWithLinkToLicensedContent"/>
 *     &lt;enumeration value="onixPL:DownloadedLicensedContent"/>
 *     &lt;enumeration value="onixPL:ImagesInLicensedContent"/>
 *     &lt;enumeration value="onixPL:LicensedContent"/>
 *     &lt;enumeration value="onixPL:LicensedContentBriefExcerpt"/>
 *     &lt;enumeration value="onixPL:LicensedContentMetadata"/>
 *     &lt;enumeration value="onixPL:LicensedContentPart"/>
 *     &lt;enumeration value="onixPL:LicensedContentPartDigital"/>
 *     &lt;enumeration value="onixPL:LicensedContentPartPrinted"/>
 *     &lt;enumeration value="onixPL:LicenseeContent"/>
 *     &lt;enumeration value="onixPL:LinkToLicensedContent"/>
 *     &lt;enumeration value="onixPL:MaterialForPresentation"/>
 *     &lt;enumeration value="onixPL:PrintedInstructionalMaterial"/>
 *     &lt;enumeration value="onixPL:SpecialNeedsInstructionalMaterial"/>
 *     &lt;enumeration value="onixPL:TrainingMaterial"/>
 *     &lt;enumeration value="onixPL:UserContent"/>
 *     &lt;enumeration value="onixPL:ERMI:CoursePackElectronic"/>
 *     &lt;enumeration value="onixPL:ERMI:CourseReserveElectronic"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "RelatedResourceCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum RelatedResourceCode {


    /**
     * A work such as an assignment, portfolio, thesis or dissertation, in printed or digital form, produced in the course of study or research, and intended for personal use and library deposit.
     */
    @XmlEnumValue("onixPL:AcademicWork")
    ONIX_PL_ACADEMIC_WORK("onixPL:AcademicWork"),

    /**
     * A work such as an assignment, portfolio, thesis or dissertation, in printed or digital form, produced in the course of study or research, and intended for personal use and library deposit, that includes an extract from licensed content.
     */
    @XmlEnumValue("onixPL:AcademicWorkIncludingLicensedContent")
    ONIX_PL_ACADEMIC_WORK_INCLUDING_LICENSED_CONTENT("onixPL:AcademicWorkIncludingLicensedContent"),

    /**
     * An acknowledgment of the source of an extract from licensed content.
     */
    @XmlEnumValue("onixPL:AcknowledgmentOfSource")
    ONIX_PL_ACKNOWLEDGMENT_OF_SOURCE("onixPL:AcknowledgmentOfSource"),

    /**
     * A part of licensed content of which an authorized user is an author.
     */
    @XmlEnumValue("onixPL:AuthoredContent")
    ONIX_PL_AUTHORED_CONTENT("onixPL:AuthoredContent"),

    /**
     * A final text of an article which is included in licensed content and of which an authorized user is an author; including revisions made as part of peer review, but not a direct copy of licensed content.
     */
    @XmlEnumValue("onixPL:AuthoredContentPeerReviewedCopy")
    ONIX_PL_AUTHORED_CONTENT_PEER_REVIEWED_COPY("onixPL:AuthoredContentPeerReviewedCopy"),

    /**
     * A work created by an authorized user either for personal use or for publication.
     */
    @XmlEnumValue("onixPL:AuthorizedUserOwnWork")
    ONIX_PL_AUTHORIZED_USER_OWN_WORK("onixPL:AuthorizedUserOwnWork"),

    /**
     * A database forming part of a library or other information system, such as a library catalog, a union catalog or a search engine index.
     */
    @XmlEnumValue("onixPL:CatalogOrInformationSystem")
    ONIX_PL_CATALOG_OR_INFORMATION_SYSTEM("onixPL:CatalogOrInformationSystem"),

    /**
     * A work that combines part(s) of licensed content with other content.
     */
    @XmlEnumValue("onixPL:CombinedWorkIncludingLicensedContent")
    ONIX_PL_COMBINED_WORK_INCLUDING_LICENSED_CONTENT("onixPL:CombinedWorkIncludingLicensedContent"),

    /**
     * The complete content of an article from a journal or similar publication.
     */
    @XmlEnumValue("onixPL:CompleteArticle")
    ONIX_PL_COMPLETE_ARTICLE("onixPL:CompleteArticle"),

    /**
     * The complete content of a book or similar publication.
     */
    @XmlEnumValue("onixPL:CompleteBook")
    ONIX_PL_COMPLETE_BOOK("onixPL:CompleteBook"),

    /**
     * The complete content of a chapter from a book or similar publication.
     */
    @XmlEnumValue("onixPL:CompleteChapter")
    ONIX_PL_COMPLETE_CHAPTER("onixPL:CompleteChapter"),

    /**
     * The complete content of an issue of a journal or similar publication.
     */
    @XmlEnumValue("onixPL:CompleteIssue")
    ONIX_PL_COMPLETE_ISSUE("onixPL:CompleteIssue"),

    /**
     * A notice specifying copyright or related intellectual property rights in licensed content.
     */
    @XmlEnumValue("onixPL:CopyrightNotice")
    ONIX_PL_COPYRIGHT_NOTICE("onixPL:CopyrightNotice"),

    /**
     * Any copyright notices, attributions or acknowledgements of authorship or ownership of intellectual property, or disclaimers carried in licensed content.
     */
    @XmlEnumValue("onixPL:CopyrightNoticesOrDisclaimers")
    ONIX_PL_COPYRIGHT_NOTICES_OR_DISCLAIMERS("onixPL:CopyrightNoticesOrDisclaimers"),

    /**
     * A compilation of printed resources made to support a course of instruction, and reproduced for distribution to and use by students and faculty registered for or teaching on the course; see also PrintedInstructionalMaterial, where no distinction is made between course pack and reserve.
     */
    @XmlEnumValue("onixPL:CoursePackPrinted")
    ONIX_PL_COURSE_PACK_PRINTED("onixPL:CoursePackPrinted"),

    /**
     * A collection of printed materials placed in a controlled circulation area of a library for reserve reading in conjunction with a specific course of instruction; see also PrintedInstructionalMaterial, where no distinction is made between course pack and reserve.
     */
    @XmlEnumValue("onixPL:CourseReservePrinted")
    ONIX_PL_COURSE_RESERVE_PRINTED("onixPL:CourseReservePrinted"),

    /**
     * Data and/or information that is contained in licensed content.
     */
    @XmlEnumValue("onixPL:DataFromLicensedContent")
    ONIX_PL_DATA_FROM_LICENSED_CONTENT("onixPL:DataFromLicensedContent"),

    /**
     * A work that is derived wholly or partly from licensed content.
     */
    @XmlEnumValue("onixPL:DerivedWork")
    ONIX_PL_DERIVED_WORK("onixPL:DerivedWork"),

    /**
     * A digital learning object or course reserve made to support a course of instruction, and accessible to students and faculty registered for or teaching on the course.
     */
    @XmlEnumValue("onixPL:DigitalInstructionalMaterial")
    ONIX_PL_DIGITAL_INSTRUCTIONAL_MATERIAL("onixPL:DigitalInstructionalMaterial"),

    /**
     * A digital learning object or course reserve that includes an extract from licensed content.
     */
    @XmlEnumValue("onixPL:DigitalInstructionalMaterialIncludingLicensedContent")
    ONIX_PL_DIGITAL_INSTRUCTIONAL_MATERIAL_INCLUDING_LICENSED_CONTENT("onixPL:DigitalInstructionalMaterialIncludingLicensedContent"),

    /**
     * A digital learning object or course reserve that includes a hyperlink (or links) to licensed content.
     */
    @XmlEnumValue("onixPL:DigitalInstructionalMaterialWithLinkToLicensedContent")
    ONIX_PL_DIGITAL_INSTRUCTIONAL_MATERIAL_WITH_LINK_TO_LICENSED_CONTENT("onixPL:DigitalInstructionalMaterialWithLinkToLicensedContent"),

    /**
     * Licensed content which has been downloaded and stored electronically.
     */
    @XmlEnumValue("onixPL:DownloadedLicensedContent")
    ONIX_PL_DOWNLOADED_LICENSED_CONTENT("onixPL:DownloadedLicensedContent"),

    /**
     * Images forming part of licensed content
     */
    @XmlEnumValue("onixPL:ImagesInLicensedContent")
    ONIX_PL_IMAGES_IN_LICENSED_CONTENT("onixPL:ImagesInLicensedContent"),

    /**
     * Material that is licensed under a license, or any part of such material.
     */
    @XmlEnumValue("onixPL:LicensedContent")
    ONIX_PL_LICENSED_CONTENT("onixPL:LicensedContent"),

    /**
     * A brief excerpt from licensed content; this may be a figure or table and/or a short piece of text.
     */
    @XmlEnumValue("onixPL:LicensedContentBriefExcerpt")
    ONIX_PL_LICENSED_CONTENT_BRIEF_EXCERPT("onixPL:LicensedContentBriefExcerpt"),

    /**
     * Metadata describing licensed content, whether derived or supplied separately from licensed content.
     */
    @XmlEnumValue("onixPL:LicensedContentMetadata")
    ONIX_PL_LICENSED_CONTENT_METADATA("onixPL:LicensedContentMetadata"),

    /**
     * A part of licensed content that is reasonably required to be extracted and/or copied for a usage permitted under a license.
     */
    @XmlEnumValue("onixPL:LicensedContentPart")
    ONIX_PL_LICENSED_CONTENT_PART("onixPL:LicensedContentPart"),

    /**
     * A digital copy of a LicensedContentPart.
     */
    @XmlEnumValue("onixPL:LicensedContentPartDigital")
    ONIX_PL_LICENSED_CONTENT_PART_DIGITAL("onixPL:LicensedContentPartDigital"),

    /**
     * A printed copy of a LicensedContentPart.
     */
    @XmlEnumValue("onixPL:LicensedContentPartPrinted")
    ONIX_PL_LICENSED_CONTENT_PART_PRINTED("onixPL:LicensedContentPartPrinted"),

    /**
     * Content that is owned and/or held by a licensee and/or its authorized users.
     */
    @XmlEnumValue("onixPL:LicenseeContent")
    ONIX_PL_LICENSEE_CONTENT("onixPL:LicenseeContent"),

    /**
     * A hyperlink to licensed content.
     */
    @XmlEnumValue("onixPL:LinkToLicensedContent")
    ONIX_PL_LINK_TO_LICENSED_CONTENT("onixPL:LinkToLicensedContent"),

    /**
     * Material that is used in a presentation or performance to an audience.
     */
    @XmlEnumValue("onixPL:MaterialForPresentation")
    ONIX_PL_MATERIAL_FOR_PRESENTATION("onixPL:MaterialForPresentation"),

    /**
     * A compilation of printed resources made to support a particular course of study, which may be reproduced for distribution to and use by students and faculty registered for or teaching on the course; see also CoursePackPrinted and CourseReservePrinted, if a distinction is drawn between these two types.
     */
    @XmlEnumValue("onixPL:PrintedInstructionalMaterial")
    ONIX_PL_PRINTED_INSTRUCTIONAL_MATERIAL("onixPL:PrintedInstructionalMaterial"),

    /**
     * A compilation of resources made to support a particular course of instruction, in a format specific to students with special needs, such as the blind or visually impaired.
     */
    @XmlEnumValue("onixPL:SpecialNeedsInstructionalMaterial")
    ONIX_PL_SPECIAL_NEEDS_INSTRUCTIONAL_MATERIAL("onixPL:SpecialNeedsInstructionalMaterial"),

    /**
     * Printed or digital material used to train authorized users in the use of licensed content, whether supplied by a licensor or created by a licensee or authorized user.
     */
    @XmlEnumValue("onixPL:TrainingMaterial")
    ONIX_PL_TRAINING_MATERIAL("onixPL:TrainingMaterial"),

    /**
     * Content that is owned and/or held by an authorized user.
     */
    @XmlEnumValue("onixPL:UserContent")
    ONIX_PL_USER_CONTENT("onixPL:UserContent"),

    /**
     * A compilation of resources assembled in an electronic format by faculty members for use by students in a class for purposes of instruction.
     */
    @XmlEnumValue("onixPL:ERMI:CoursePackElectronic")
    ONIX_PL_ERMI_COURSE_PACK_ELECTRONIC("onixPL:ERMI:CoursePackElectronic"),

    /**
     * An electronic copy of licensed materials stored on a secure network.
     */
    @XmlEnumValue("onixPL:ERMI:CourseReserveElectronic")
    ONIX_PL_ERMI_COURSE_RESERVE_ELECTRONIC("onixPL:ERMI:CourseReserveElectronic");
    private final String value;

    RelatedResourceCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RelatedResourceCode fromValue(String v) {
        for (RelatedResourceCode c : RelatedResourceCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
