
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
 * <p>Java class for RelatedPlaceCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="RelatedPlaceCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AuthorizedUserSecureRepository"/>
 *     &lt;enumeration value="onixPL:CountryOfLicensee"/>
 *     &lt;enumeration value="onixPL:JORUM"/>
 *     &lt;enumeration value="onixPL:LicenseePremises"/>
 *     &lt;enumeration value="onixPL:LicenseeSecureRepository"/>
 *     &lt;enumeration value="onixPL:RemoteLocation"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "RelatedPlaceCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum RelatedPlaceCode {


    /**
     * A digital repository maintained by an authorized user on a secure network.
     */
    @XmlEnumValue("onixPL:AuthorizedUserSecureRepository")
    ONIX_PL_AUTHORIZED_USER_SECURE_REPOSITORY("onixPL:AuthorizedUserSecureRepository"),

    /**
     * The country in which a licensee is located.
     */
    @XmlEnumValue("onixPL:CountryOfLicensee")
    ONIX_PL_COUNTRY_OF_LICENSEE("onixPL:CountryOfLicensee"),

    /**
     * A central repository of learning and teaching materialfor the UK academic community operating under the name 'JORUM'.
     */
    @XmlEnumValue("onixPL:JORUM")
    ONIX_PL_JORUM("onixPL:JORUM"),

    /**
     * Any premises occupied by a licensee.
     */
    @XmlEnumValue("onixPL:LicenseePremises")
    ONIX_PL_LICENSEE_PREMISES("onixPL:LicenseePremises"),

    /**
     * A digital repository maintained by a licensee on a secure network.
     */
    @XmlEnumValue("onixPL:LicenseeSecureRepository")
    ONIX_PL_LICENSEE_SECURE_REPOSITORY("onixPL:LicenseeSecureRepository"),

    /**
     * A location that is outside of the premises occupied by a licensee or, if a site is specified in a license, outside the specified site.
     */
    @XmlEnumValue("onixPL:RemoteLocation")
    ONIX_PL_REMOTE_LOCATION("onixPL:RemoteLocation");
    private final String value;

    RelatedPlaceCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RelatedPlaceCode fromValue(String v) {
        for (RelatedPlaceCode c : RelatedPlaceCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
