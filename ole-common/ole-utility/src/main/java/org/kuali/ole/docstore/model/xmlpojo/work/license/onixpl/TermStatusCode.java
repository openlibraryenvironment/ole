
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
 * <p>Java class for TermStatusCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="TermStatusCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:ExplicitNo"/>
 *     &lt;enumeration value="onixPL:ExplicitYes"/>
 *     &lt;enumeration value="onixPL:InterpretedNo"/>
 *     &lt;enumeration value="onixPL:InterpretedYes"/>
 *     &lt;enumeration value="onixPL:No"/>
 *     &lt;enumeration value="onixPL:NotApplicable"/>
 *     &lt;enumeration value="onixPL:Silent"/>
 *     &lt;enumeration value="onixPL:Uncertain"/>
 *     &lt;enumeration value="onixPL:Yes"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "TermStatusCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum TermStatusCode {


    /**
     * The license explicitly excludes the condition specified by a term type.
     */
    @XmlEnumValue("onixPL:ExplicitNo")
    ONIX_PL_EXPLICIT_NO("onixPL:ExplicitNo"),

    /**
     * The license explicitly affirms the condition specified by a term type.
     */
    @XmlEnumValue("onixPL:ExplicitYes")
    ONIX_PL_EXPLICIT_YES("onixPL:ExplicitYes"),

    /**
     * The license is interpreted as excluding the condition specified by a term type.
     */
    @XmlEnumValue("onixPL:InterpretedNo")
    ONIX_PL_INTERPRETED_NO("onixPL:InterpretedNo"),

    /**
     * The license is interpreted as affirming the condition specified by a term type.
     */
    @XmlEnumValue("onixPL:InterpretedYes")
    ONIX_PL_INTERPRETED_YES("onixPL:InterpretedYes"),

    /**
     * The license has been encoded as excluding the condition specified by a term type, but it is not recorded as explicit or interpreted.
     */
    @XmlEnumValue("onixPL:No")
    ONIX_PL_NO("onixPL:No"),

    /**
     * Term status values are not applicable to this term.
     */
    @XmlEnumValue("onixPL:NotApplicable")
    ONIX_PL_NOT_APPLICABLE("onixPL:NotApplicable"),

    /**
     * The license is silent on the condition specified by a term type.
     */
    @XmlEnumValue("onixPL:Silent")
    ONIX_PL_SILENT("onixPL:Silent"),

    /**
     * The license is unclear on the condition specified by a term type.
     */
    @XmlEnumValue("onixPL:Uncertain")
    ONIX_PL_UNCERTAIN("onixPL:Uncertain"),

    /**
     * The license has been encoded as affirming the condition specified by a term type, but it is not recorded as explicit or interpreted.
     */
    @XmlEnumValue("onixPL:Yes")
    ONIX_PL_YES("onixPL:Yes");
    private final String value;

    TermStatusCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TermStatusCode fromValue(String v) {
        for (TermStatusCode c : TermStatusCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
