
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
 * <p>Java class for LicenseAgentRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="LicenseAgentRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AuthorizedUsers"/>
 *     &lt;enumeration value="onixPL:Licensee"/>
 *     &lt;enumeration value="onixPL:LicenseeConsortium"/>
 *     &lt;enumeration value="onixPL:LicenseeLicenseAdministrator"/>
 *     &lt;enumeration value="onixPL:LicenseeRepresentative"/>
 *     &lt;enumeration value="onixPL:LicenseeRepresentativeSignatory"/>
 *     &lt;enumeration value="onixPL:LicenseeSignatory"/>
 *     &lt;enumeration value="onixPL:LicensingAgent"/>
 *     &lt;enumeration value="onixPL:Licensor"/>
 *     &lt;enumeration value="onixPL:LicensorLicenseAdministrator"/>
 *     &lt;enumeration value="onixPL:LicensorSignatory"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LicenseAgentRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum LicenseAgentRelatorCode {


    /**
     * An authorized user or class of authorized users
     */
    @XmlEnumValue("onixPL:AuthorizedUsers")
    ONIX_PL_AUTHORIZED_USERS("onixPL:AuthorizedUsers"),

    /**
     * A licensee under a license.
     */
    @XmlEnumValue("onixPL:Licensee")
    ONIX_PL_LICENSEE("onixPL:Licensee"),

    /**
     * A consortium of which licensees under a license are members.
     */
    @XmlEnumValue("onixPL:LicenseeConsortium")
    ONIX_PL_LICENSEE_CONSORTIUM("onixPL:LicenseeConsortium"),

    /**
     * A licensee's administrator for the license.
     */
    @XmlEnumValue("onixPL:LicenseeLicenseAdministrator")
    ONIX_PL_LICENSEE_LICENSE_ADMINISTRATOR("onixPL:LicenseeLicenseAdministrator"),

    /**
     * A representative of the licensee(s) authorized to sign the license (usually when licensees are members of a consortium).
     */
    @XmlEnumValue("onixPL:LicenseeRepresentative")
    ONIX_PL_LICENSEE_REPRESENTATIVE("onixPL:LicenseeRepresentative"),

    /**
     * The person who signed a license on behalf of a licensee's authorized representative.
     */
    @XmlEnumValue("onixPL:LicenseeRepresentativeSignatory")
    ONIX_PL_LICENSEE_REPRESENTATIVE_SIGNATORY("onixPL:LicenseeRepresentativeSignatory"),

    /**
     * A person who signed a license on behalf of the licensee.
     */
    @XmlEnumValue("onixPL:LicenseeSignatory")
    ONIX_PL_LICENSEE_SIGNATORY("onixPL:LicenseeSignatory"),

    /**
     * A third party who facilitates a licensing process (usually a subscription agent).
     */
    @XmlEnumValue("onixPL:LicensingAgent")
    ONIX_PL_LICENSING_AGENT("onixPL:LicensingAgent"),

    /**
     * A licensor under a license.
     */
    @XmlEnumValue("onixPL:Licensor")
    ONIX_PL_LICENSOR("onixPL:Licensor"),

    /**
     * A licensor's administrator for the license.
     */
    @XmlEnumValue("onixPL:LicensorLicenseAdministrator")
    ONIX_PL_LICENSOR_LICENSE_ADMINISTRATOR("onixPL:LicensorLicenseAdministrator"),

    /**
     * A person who signed a license on behalf of the licensor.
     */
    @XmlEnumValue("onixPL:LicensorSignatory")
    ONIX_PL_LICENSOR_SIGNATORY("onixPL:LicensorSignatory");
    private final String value;

    LicenseAgentRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LicenseAgentRelatorCode fromValue(String v) {
        for (LicenseAgentRelatorCode c : LicenseAgentRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
