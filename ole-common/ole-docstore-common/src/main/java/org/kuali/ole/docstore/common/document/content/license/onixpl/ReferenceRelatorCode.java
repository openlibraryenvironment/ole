
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
 * <p>Java class for ReferenceRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="ReferenceRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:BasedOnOfferIn"/>
 *     &lt;enumeration value="onixPL:BasedOnTermsOf"/>
 *     &lt;enumeration value="onixPL:DerivedFromModel"/>
 *     &lt;enumeration value="onixPL:ReplacedBy"/>
 *     &lt;enumeration value="onixPL:ReplacementFor"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ReferenceRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum ReferenceRelatorCode {


    /**
     * The financial terms of the license are based on an offer set out in the referenced document.
     */
    @XmlEnumValue("onixPL:BasedOnOfferIn")
    ONIX_PL_BASED_ON_OFFER_IN("onixPL:BasedOnOfferIn"),

    /**
     * The overall terms and conditions of the license are based on the terms set out in the referenced document.
     */
    @XmlEnumValue("onixPL:BasedOnTermsOf")
    ONIX_PL_BASED_ON_TERMS_OF("onixPL:BasedOnTermsOf"),

    /**
     * The license is derived from the referenced model license.
     */
    @XmlEnumValue("onixPL:DerivedFromModel")
    ONIX_PL_DERIVED_FROM_MODEL("onixPL:DerivedFromModel"),

    /**
     * The license is replaced by the referenced document
     */
    @XmlEnumValue("onixPL:ReplacedBy")
    ONIX_PL_REPLACED_BY("onixPL:ReplacedBy"),

    /**
     * The license is a replacement for or successor to the referenced document
     */
    @XmlEnumValue("onixPL:ReplacementFor")
    ONIX_PL_REPLACEMENT_FOR("onixPL:ReplacementFor");
    private final String value;

    ReferenceRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReferenceRelatorCode fromValue(String v) {
        for (ReferenceRelatorCode c : ReferenceRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
