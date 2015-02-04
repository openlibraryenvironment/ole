
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
 * <p>Java class for DocumentTimePointRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="DocumentTimePointRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:LatestRevisionDate"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "DocumentTimePointRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum DocumentTimePointRelatorCode {


    /**
     * The document was last revised on the date specified as a related time point.
     */
    @XmlEnumValue("onixPL:LatestRevisionDate")
    ONIX_PL_LATEST_REVISION_DATE("onixPL:LatestRevisionDate");
    private final String value;

    DocumentTimePointRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DocumentTimePointRelatorCode fromValue(String v) {
        for (DocumentTimePointRelatorCode c : DocumentTimePointRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
