
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
 * <p>Java class for ExpressionTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="ExpressionTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:LicenseExpression"/>
 *     &lt;enumeration value="onixPL:LicenseTemplate"/>
 *     &lt;enumeration value="onixPL:ModelLicense"/>
 *     &lt;enumeration value="onixPL:PolicyTemplate"/>
 *     &lt;enumeration value="onixPL:ERMI:TemplateEncoding"/>
 *     &lt;enumeration value="onixPL:ERMI:LicenseEncoding"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ExpressionTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum ExpressionTypeCode {


    /**
     * An expression of an individual license between specified parties.
     */
    @XmlEnumValue("onixPL:LicenseExpression")
    ONIX_PL_LICENSE_EXPRESSION("onixPL:LicenseExpression"),

    /**
     * An expression of a license template from which individual licenses may be derived.
     */
    @XmlEnumValue("onixPL:LicenseTemplate")
    ONIX_PL_LICENSE_TEMPLATE("onixPL:LicenseTemplate"),

    /**
     * An expression of a model license from which license templates or individual licenses may be derived.
     */
    @XmlEnumValue("onixPL:ModelLicense")
    ONIX_PL_MODEL_LICENSE("onixPL:ModelLicense"),

    /**
     * An expression of policies for the use of electronic resources, typically the preferred policies of a licensee institution.
     */
    @XmlEnumValue("onixPL:PolicyTemplate")
    ONIX_PL_POLICY_TEMPLATE("onixPL:PolicyTemplate"),

    /**
     * An ERMI encoding of a license template expressed in ONIX-PL.
     */
    @XmlEnumValue("onixPL:ERMI:TemplateEncoding")
    ONIX_PL_ERMI_TEMPLATE_ENCODING("onixPL:ERMI:TemplateEncoding"),

    /**
     * An ERMI encoding of an individual license expressed in ONIX-PL.
     */
    @XmlEnumValue("onixPL:ERMI:LicenseEncoding")
    ONIX_PL_ERMI_LICENSE_ENCODING("onixPL:ERMI:LicenseEncoding");
    private final String value;

    ExpressionTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExpressionTypeCode fromValue(String v) {
        for (ExpressionTypeCode c : ExpressionTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
