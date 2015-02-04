
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
 * <p>Java class for DocumentTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="DocumentTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:Document"/>
 *     &lt;enumeration value="onixPL:WebResource"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "DocumentTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum DocumentTypeCode {


    /**
     * A document in an unspecified form, eg one that may be delivered in printed or digital form.
     */
    @XmlEnumValue("onixPL:Document")
    ONIX_PL_DOCUMENT("onixPL:Document"),

    /**
     * A document that is held as a digital file accessible on a website.
     */
    @XmlEnumValue("onixPL:WebResource")
    ONIX_PL_WEB_RESOURCE("onixPL:WebResource");
    private final String value;

    DocumentTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DocumentTypeCode fromValue(String v) {
        for (DocumentTypeCode c : DocumentTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
