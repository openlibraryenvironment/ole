
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
 * <p>Java class for UsageTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UsageTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:Access"/>
 *     &lt;enumeration value="onixPL:AccessByRobot"/>
 *     &lt;enumeration value="onixPL:Copy"/>
 *     &lt;enumeration value="onixPL:Deposit"/>
 *     &lt;enumeration value="onixPL:DepositInPerpetuity"/>
 *     &lt;enumeration value="onixPL:Include"/>
 *     &lt;enumeration value="onixPL:MakeAvailable"/>
 *     &lt;enumeration value="onixPL:MakeDerivedWork"/>
 *     &lt;enumeration value="onixPL:MakeDigitalCopy"/>
 *     &lt;enumeration value="onixPL:MakeTemporaryDigitalCopy"/>
 *     &lt;enumeration value="onixPL:Modify"/>
 *     &lt;enumeration value="onixPL:PrintCopy"/>
 *     &lt;enumeration value="onixPL:ProvideIntegratedAccess"/>
 *     &lt;enumeration value="onixPL:ProvideIntegratedIndex"/>
 *     &lt;enumeration value="onixPL:RemoveObscureOrModify"/>
 *     &lt;enumeration value="onixPL:Sell"/>
 *     &lt;enumeration value="onixPL:SupplyCopy"/>
 *     &lt;enumeration value="onixPL:SystematicallyCopy"/>
 *     &lt;enumeration value="onixPL:Use"/>
 *     &lt;enumeration value="onixPL:UseForDataMining"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UsageTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UsageTypeCode {


    /**
     * Access, retrieve, display and/or view a resource.
     */
    @XmlEnumValue("onixPL:Access")
    ONIX_PL_ACCESS("onixPL:Access"),

    /**
     * Access a resource by using an automated process, such as a web robot, spider, crawler, wanderer or accelerator, to download content.
     */
    @XmlEnumValue("onixPL:AccessByRobot")
    ONIX_PL_ACCESS_BY_ROBOT("onixPL:AccessByRobot"),

    /**
     * Make a copy of a resource (in any form).
     */
    @XmlEnumValue("onixPL:Copy")
    ONIX_PL_COPY("onixPL:Copy"),

    /**
     * Deposit a copy of a resource in a defined place.
     */
    @XmlEnumValue("onixPL:Deposit")
    ONIX_PL_DEPOSIT("onixPL:Deposit"),

    /**
     * Deposit in perpetuity a copy of a resource in a defined place.
     */
    @XmlEnumValue("onixPL:DepositInPerpetuity")
    ONIX_PL_DEPOSIT_IN_PERPETUITY("onixPL:DepositInPerpetuity"),

    /**
     * Include a copy of a resource as part of another defined resource.
     */
    @XmlEnumValue("onixPL:Include")
    ONIX_PL_INCLUDE("onixPL:Include"),

    /**
     * Make a resource available or accessible in any form, by supply of printed or digital copies or by posting on a network or by any other means.
     */
    @XmlEnumValue("onixPL:MakeAvailable")
    ONIX_PL_MAKE_AVAILABLE("onixPL:MakeAvailable"),

    /**
     * Use a resource to create a derived work (in any form and by any means).
     */
    @XmlEnumValue("onixPL:MakeDerivedWork")
    ONIX_PL_MAKE_DERIVED_WORK("onixPL:MakeDerivedWork"),

    /**
     * Make and save a digital copy of a resource.
     */
    @XmlEnumValue("onixPL:MakeDigitalCopy")
    ONIX_PL_MAKE_DIGITAL_COPY("onixPL:MakeDigitalCopy"),

    /**
     * Make a temporary digital copy of a resource.
     */
    @XmlEnumValue("onixPL:MakeTemporaryDigitalCopy")
    ONIX_PL_MAKE_TEMPORARY_DIGITAL_COPY("onixPL:MakeTemporaryDigitalCopy"),

    /**
     * Modify the content or presentation of a resource.
     */
    @XmlEnumValue("onixPL:Modify")
    ONIX_PL_MODIFY("onixPL:Modify"),

    /**
     * Make a printed copy of a resource.
     */
    @XmlEnumValue("onixPL:PrintCopy")
    ONIX_PL_PRINT_COPY("onixPL:PrintCopy"),

    /**
     * Provide integrated access to a resource together with other resources.
     */
    @XmlEnumValue("onixPL:ProvideIntegratedAccess")
    ONIX_PL_PROVIDE_INTEGRATED_ACCESS("onixPL:ProvideIntegratedAccess"),

    /**
     * Provide an integrated index to a resource together with other resources.
     */
    @XmlEnumValue("onixPL:ProvideIntegratedIndex")
    ONIX_PL_PROVIDE_INTEGRATED_INDEX("onixPL:ProvideIntegratedIndex"),

    /**
     * Remove, obscure or modify a resource.
     */
    @XmlEnumValue("onixPL:RemoveObscureOrModify")
    ONIX_PL_REMOVE_OBSCURE_OR_MODIFY("onixPL:RemoveObscureOrModify"),

    /**
     * Sell, resell, loan or hire a resource for payment.
     */
    @XmlEnumValue("onixPL:Sell")
    ONIX_PL_SELL("onixPL:Sell"),

    /**
     * Make a copy of a resource (in any form), and supply it to another party.
     */
    @XmlEnumValue("onixPL:SupplyCopy")
    ONIX_PL_SUPPLY_COPY("onixPL:SupplyCopy"),

    /**
     * Systematically make copies of the whole or any parts of a resource (in any form).
     */
    @XmlEnumValue("onixPL:SystematicallyCopy")
    ONIX_PL_SYSTEMATICALLY_COPY("onixPL:SystematicallyCopy"),

    /**
     * Use a resource in any manner and for any purpose.
     */
    @XmlEnumValue("onixPL:Use")
    ONIX_PL_USE("onixPL:Use"),

    /**
     * Use a resource for the purpose of deriving information by automated data mining and/or text mining processes.
     */
    @XmlEnumValue("onixPL:UseForDataMining")
    ONIX_PL_USE_FOR_DATA_MINING("onixPL:UseForDataMining");
    private final String value;

    UsageTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsageTypeCode fromValue(String v) {
        for (UsageTypeCode c : UsageTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
