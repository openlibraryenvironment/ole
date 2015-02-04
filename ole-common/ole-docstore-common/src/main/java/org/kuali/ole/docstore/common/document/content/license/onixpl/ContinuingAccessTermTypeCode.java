
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
 * <p>Java class for ContinuingAccessTermTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="ContinuingAccessTermTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:ArchiveCopy"/>
 *     &lt;enumeration value="onixPL:ContinuingAccess"/>
 *     &lt;enumeration value="onixPL:NotificationOfDarkArchive"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ContinuingAccessTermTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum ContinuingAccessTermTypeCode {


    /**
     * A license term that specifies that a licensor will directly or indirectly provide the licensee with an archive copy of licensed content for continued use after termination of a license.
     */
    @XmlEnumValue("onixPL:ArchiveCopy")
    ONIX_PL_ARCHIVE_COPY("onixPL:ArchiveCopy"),

    /**
     * A license term that specifies that a licensor will provide the licensee with continuing access to licensed content after termination of a license, either through its own online service or otherwise.
     */
    @XmlEnumValue("onixPL:ContinuingAccess")
    ONIX_PL_CONTINUING_ACCESS("onixPL:ContinuingAccess"),

    /**
     * A license term that requires the licensor to notify the location of any dark archive services where licensed content is deposited.
     */
    @XmlEnumValue("onixPL:NotificationOfDarkArchive")
    ONIX_PL_NOTIFICATION_OF_DARK_ARCHIVE("onixPL:NotificationOfDarkArchive");
    private final String value;

    ContinuingAccessTermTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ContinuingAccessTermTypeCode fromValue(String v) {
        for (ContinuingAccessTermTypeCode c : ContinuingAccessTermTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
