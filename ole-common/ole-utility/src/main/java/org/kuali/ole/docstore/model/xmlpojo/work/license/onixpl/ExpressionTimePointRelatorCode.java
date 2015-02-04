
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
 * <p>Java class for ExpressionTimePointRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="ExpressionTimePointRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:PreparedOn"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ExpressionTimePointRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum ExpressionTimePointRelatorCode {


    /**
     * The date on which a license expression was prepared.
     */
    @XmlEnumValue("onixPL:PreparedOn")
    ONIX_PL_PREPARED_ON("onixPL:PreparedOn");
    private final String value;

    ExpressionTimePointRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExpressionTimePointRelatorCode fromValue(String v) {
        for (ExpressionTimePointRelatorCode c : ExpressionTimePointRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
