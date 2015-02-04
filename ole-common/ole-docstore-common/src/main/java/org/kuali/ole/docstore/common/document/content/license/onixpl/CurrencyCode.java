
package org.kuali.ole.docstore.common.document.content.license.onixpl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 5/30/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for CurrencyCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="CurrencyCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AED"/>
 *     &lt;enumeration value="AFA"/>
 *     &lt;enumeration value="AFN"/>
 *     &lt;enumeration value="ALL"/>
 *     &lt;enumeration value="AMD"/>
 *     &lt;enumeration value="ANG"/>
 *     &lt;enumeration value="AOA"/>
 *     &lt;enumeration value="ARS"/>
 *     &lt;enumeration value="ATS"/>
 *     &lt;enumeration value="AUD"/>
 *     &lt;enumeration value="AWG"/>
 *     &lt;enumeration value="AZN"/>
 *     &lt;enumeration value="BAM"/>
 *     &lt;enumeration value="BBD"/>
 *     &lt;enumeration value="BDT"/>
 *     &lt;enumeration value="BEF"/>
 *     &lt;enumeration value="BGL"/>
 *     &lt;enumeration value="BGN"/>
 *     &lt;enumeration value="BHD"/>
 *     &lt;enumeration value="BIF"/>
 *     &lt;enumeration value="BMD"/>
 *     &lt;enumeration value="BND"/>
 *     &lt;enumeration value="BOB"/>
 *     &lt;enumeration value="BRL"/>
 *     &lt;enumeration value="BSD"/>
 *     &lt;enumeration value="BTN"/>
 *     &lt;enumeration value="BWP"/>
 *     &lt;enumeration value="BYR"/>
 *     &lt;enumeration value="BZD"/>
 *     &lt;enumeration value="CAD"/>
 *     &lt;enumeration value="CDF"/>
 *     &lt;enumeration value="CHF"/>
 *     &lt;enumeration value="CLP"/>
 *     &lt;enumeration value="CNY"/>
 *     &lt;enumeration value="COP"/>
 *     &lt;enumeration value="CRC"/>
 *     &lt;enumeration value="CSD"/>
 *     &lt;enumeration value="CUC"/>
 *     &lt;enumeration value="CUP"/>
 *     &lt;enumeration value="CVE"/>
 *     &lt;enumeration value="CYP"/>
 *     &lt;enumeration value="CZK"/>
 *     &lt;enumeration value="DEM"/>
 *     &lt;enumeration value="DJF"/>
 *     &lt;enumeration value="DKK"/>
 *     &lt;enumeration value="DOP"/>
 *     &lt;enumeration value="DZD"/>
 *     &lt;enumeration value="EEK"/>
 *     &lt;enumeration value="EGP"/>
 *     &lt;enumeration value="ERN"/>
 *     &lt;enumeration value="ESP"/>
 *     &lt;enumeration value="ETB"/>
 *     &lt;enumeration value="EUR"/>
 *     &lt;enumeration value="FIM"/>
 *     &lt;enumeration value="FJD"/>
 *     &lt;enumeration value="FKP"/>
 *     &lt;enumeration value="FRF"/>
 *     &lt;enumeration value="GBP"/>
 *     &lt;enumeration value="GEL"/>
 *     &lt;enumeration value="GHC"/>
 *     &lt;enumeration value="GIP"/>
 *     &lt;enumeration value="GMD"/>
 *     &lt;enumeration value="GNF"/>
 *     &lt;enumeration value="GRD"/>
 *     &lt;enumeration value="GTQ"/>
 *     &lt;enumeration value="GWP"/>
 *     &lt;enumeration value="GYD"/>
 *     &lt;enumeration value="HKD"/>
 *     &lt;enumeration value="HNL"/>
 *     &lt;enumeration value="HRK"/>
 *     &lt;enumeration value="HTG"/>
 *     &lt;enumeration value="HUF"/>
 *     &lt;enumeration value="IDR"/>
 *     &lt;enumeration value="IEP"/>
 *     &lt;enumeration value="ILS"/>
 *     &lt;enumeration value="INR"/>
 *     &lt;enumeration value="IQD"/>
 *     &lt;enumeration value="IRR"/>
 *     &lt;enumeration value="ISK"/>
 *     &lt;enumeration value="ITL"/>
 *     &lt;enumeration value="JMD"/>
 *     &lt;enumeration value="JOD"/>
 *     &lt;enumeration value="JPY"/>
 *     &lt;enumeration value="KES"/>
 *     &lt;enumeration value="KGS"/>
 *     &lt;enumeration value="KHR"/>
 *     &lt;enumeration value="KMF"/>
 *     &lt;enumeration value="KPW"/>
 *     &lt;enumeration value="KRW"/>
 *     &lt;enumeration value="KWD"/>
 *     &lt;enumeration value="KYD"/>
 *     &lt;enumeration value="KZT"/>
 *     &lt;enumeration value="LAK"/>
 *     &lt;enumeration value="LBP"/>
 *     &lt;enumeration value="LKR"/>
 *     &lt;enumeration value="LRD"/>
 *     &lt;enumeration value="LSL"/>
 *     &lt;enumeration value="LTL"/>
 *     &lt;enumeration value="LUF"/>
 *     &lt;enumeration value="LVL"/>
 *     &lt;enumeration value="LYD"/>
 *     &lt;enumeration value="MAD"/>
 *     &lt;enumeration value="MDL"/>
 *     &lt;enumeration value="MGA"/>
 *     &lt;enumeration value="MGF"/>
 *     &lt;enumeration value="MKD"/>
 *     &lt;enumeration value="MMK"/>
 *     &lt;enumeration value="MNT"/>
 *     &lt;enumeration value="MOP"/>
 *     &lt;enumeration value="MRO"/>
 *     &lt;enumeration value="MTL"/>
 *     &lt;enumeration value="MUR"/>
 *     &lt;enumeration value="MVR"/>
 *     &lt;enumeration value="MWK"/>
 *     &lt;enumeration value="MXN"/>
 *     &lt;enumeration value="MYR"/>
 *     &lt;enumeration value="MZN"/>
 *     &lt;enumeration value="NAD"/>
 *     &lt;enumeration value="NGN"/>
 *     &lt;enumeration value="NIO"/>
 *     &lt;enumeration value="NLG"/>
 *     &lt;enumeration value="NOK"/>
 *     &lt;enumeration value="NPR"/>
 *     &lt;enumeration value="NZD"/>
 *     &lt;enumeration value="OMR"/>
 *     &lt;enumeration value="PAB"/>
 *     &lt;enumeration value="PEN"/>
 *     &lt;enumeration value="PGK"/>
 *     &lt;enumeration value="PHP"/>
 *     &lt;enumeration value="PKR"/>
 *     &lt;enumeration value="PLN"/>
 *     &lt;enumeration value="PTE"/>
 *     &lt;enumeration value="PYG"/>
 *     &lt;enumeration value="QAR"/>
 *     &lt;enumeration value="ROL"/>
 *     &lt;enumeration value="RON"/>
 *     &lt;enumeration value="RUB"/>
 *     &lt;enumeration value="RUR"/>
 *     &lt;enumeration value="RWF"/>
 *     &lt;enumeration value="SAR"/>
 *     &lt;enumeration value="SBD"/>
 *     &lt;enumeration value="SCR"/>
 *     &lt;enumeration value="SDD"/>
 *     &lt;enumeration value="SEK"/>
 *     &lt;enumeration value="SGD"/>
 *     &lt;enumeration value="SHP"/>
 *     &lt;enumeration value="SIT"/>
 *     &lt;enumeration value="SKK"/>
 *     &lt;enumeration value="SLL"/>
 *     &lt;enumeration value="SOS"/>
 *     &lt;enumeration value="SRD"/>
 *     &lt;enumeration value="SRG"/>
 *     &lt;enumeration value="STD"/>
 *     &lt;enumeration value="SVC"/>
 *     &lt;enumeration value="SYP"/>
 *     &lt;enumeration value="SZL"/>
 *     &lt;enumeration value="THB"/>
 *     &lt;enumeration value="TJS"/>
 *     &lt;enumeration value="TMM"/>
 *     &lt;enumeration value="TND"/>
 *     &lt;enumeration value="TOP"/>
 *     &lt;enumeration value="TPE"/>
 *     &lt;enumeration value="TRL"/>
 *     &lt;enumeration value="TRY"/>
 *     &lt;enumeration value="TTD"/>
 *     &lt;enumeration value="TWD"/>
 *     &lt;enumeration value="TZS"/>
 *     &lt;enumeration value="UAH"/>
 *     &lt;enumeration value="UGX"/>
 *     &lt;enumeration value="USD"/>
 *     &lt;enumeration value="UYU"/>
 *     &lt;enumeration value="UZS"/>
 *     &lt;enumeration value="VEB"/>
 *     &lt;enumeration value="VND"/>
 *     &lt;enumeration value="VUV"/>
 *     &lt;enumeration value="WST"/>
 *     &lt;enumeration value="XAF"/>
 *     &lt;enumeration value="XCD"/>
 *     &lt;enumeration value="XOF"/>
 *     &lt;enumeration value="XPF"/>
 *     &lt;enumeration value="YER"/>
 *     &lt;enumeration value="YUM"/>
 *     &lt;enumeration value="ZAR"/>
 *     &lt;enumeration value="ZMK"/>
 *     &lt;enumeration value="ZWD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "CurrencyCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum CurrencyCode {


    /**
     * United Arab Emirates
     */
    AED,

    /**
     * DEPRECATED, replaced by AFN
     */
    AFA,

    /**
     * Afghanistan
     */
    AFN,

    /**
     * Albania
     */
    ALL,

    /**
     * Armenia
     */
    AMD,

    /**
     * Netherlands Antilles
     */
    ANG,

    /**
     * Angola
     */
    AOA,

    /**
     * Argentina
     */
    ARS,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    ATS,

    /**
     * Australia, Christmas Island, Cocos (Keeling) Islands, Heard Island and McDonald Islands, Kiribati, Nauru, Norfolk Island, Tuvalu
     */
    AUD,

    /**
     * Aruba
     */
    AWG,

    /**
     * Azerbaijan
     */
    AZN,

    /**
     * Bosnia & Herzegovina
     */
    BAM,

    /**
     * Barbados
     */
    BBD,

    /**
     * Bangladesh
     */
    BDT,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    BEF,

    /**
     * DEPRECATED, replaced by BGN
     */
    BGL,

    /**
     * Bulgaria
     */
    BGN,

    /**
     * Bahrain
     */
    BHD,

    /**
     * Burundi
     */
    BIF,

    /**
     * Bermuda
     */
    BMD,

    /**
     * Brunei Darussalam
     */
    BND,

    /**
     * Bolivia
     */
    BOB,

    /**
     * Brazil
     */
    BRL,

    /**
     * Bahamas
     */
    BSD,

    /**
     * Bhutan
     */
    BTN,

    /**
     * Botswana
     */
    BWP,

    /**
     * Belarus
     */
    BYR,

    /**
     * Belize
     */
    BZD,

    /**
     * Canada
     */
    CAD,

    /**
     * Congo (Democratic Republic of the)
     */
    CDF,

    /**
     * Switzerland, Liechtenstein
     */
    CHF,

    /**
     * Chile
     */
    CLP,

    /**
     * China
     */
    CNY,

    /**
     * Colombia
     */
    COP,

    /**
     * Costa Rica
     */
    CRC,

    /**
     * Serbia
     */
    CSD,

    /**
     * Cuba (alternative currency)
     */
    CUC,

    /**
     * Cuba
     */
    CUP,

    /**
     * Cape Verde
     */
    CVE,

    /**
     * Cyprus
     */
    CYP,

    /**
     * Czech Republic
     */
    CZK,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    DEM,

    /**
     * Djibouti
     */
    DJF,

    /**
     * Denmark, Faroe Islands, Greenland
     */
    DKK,

    /**
     * Dominican Republic
     */
    DOP,

    /**
     * Algeria
     */
    DZD,

    /**
     * Estonia
     */
    EEK,

    /**
     * Egypt
     */
    EGP,

    /**
     * Eritrea
     */
    ERN,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    ESP,

    /**
     * Ethiopia
     */
    ETB,

    /**
     * Andorra, Austria, Belgium, Finland, France, Fr Guiana, Fr S Territories, Germany, Greece, Guadeloupe, Holy See (Vatican City), Ireland, Italy, Luxembourg, Martinique, Mayotte, Monaco, Netherlands, Portugal, Runion, St Pierre & Miquelon, San Marino, Spain
     */
    EUR,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    FIM,

    /**
     * Fiji
     */
    FJD,

    /**
     * Falkland Islands (Malvinas)
     */
    FKP,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    FRF,

    /**
     * United Kingdom
     */
    GBP,

    /**
     * Georgia
     */
    GEL,

    /**
     * Ghana
     */
    GHC,

    /**
     * Gibraltar
     */
    GIP,

    /**
     * Gambia
     */
    GMD,

    /**
     * Guinea
     */
    GNF,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    GRD,

    /**
     * Guatemala
     */
    GTQ,

    /**
     * Guinea-Bissau
     */
    GWP,

    /**
     * Guyana
     */
    GYD,

    /**
     * Hong Kong
     */
    HKD,

    /**
     * Honduras
     */
    HNL,

    /**
     * Croatia
     */
    HRK,

    /**
     * Haiti
     */
    HTG,

    /**
     * Hungary
     */
    HUF,

    /**
     * Indonesia
     */
    IDR,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    IEP,

    /**
     * Israel
     */
    ILS,

    /**
     * India
     */
    INR,

    /**
     * Iraq
     */
    IQD,

    /**
     * Iran (Islamic Republic of)
     */
    IRR,

    /**
     * Iceland
     */
    ISK,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    ITL,

    /**
     * Jamaica
     */
    JMD,

    /**
     * Jordan
     */
    JOD,

    /**
     * Japan
     */
    JPY,

    /**
     * Kenya
     */
    KES,

    /**
     * Kyrgyzstan
     */
    KGS,

    /**
     * Cambodia
     */
    KHR,

    /**
     * Comoros
     */
    KMF,

    /**
     * Korea (Democratic Peoples Republic of)
     */
    KPW,

    /**
     * Korea (Republic of)
     */
    KRW,

    /**
     * Kuwait
     */
    KWD,

    /**
     * Cayman Islands
     */
    KYD,

    /**
     * Kazakstan
     */
    KZT,

    /**
     * Lao Peoples Democratic Republic
     */
    LAK,

    /**
     * Lebanon
     */
    LBP,

    /**
     * Sri Lanka
     */
    LKR,

    /**
     * Liberia
     */
    LRD,

    /**
     * Lesotho
     */
    LSL,

    /**
     * Lithuania
     */
    LTL,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    LUF,

    /**
     * Latvia
     */
    LVL,

    /**
     * Libyan Arab Jamahiriya
     */
    LYD,

    /**
     * Morocco, Western Sahara
     */
    MAD,

    /**
     * Moldova, Republic of
     */
    MDL,

    /**
     * Madagascar
     */
    MGA,

    /**
     * Madagascar
     */
    MGF,

    /**
     * Macedonia (former Yugoslav Republic of)
     */
    MKD,

    /**
     * Myanmar
     */
    MMK,

    /**
     * Mongolia
     */
    MNT,

    /**
     * Macau
     */
    MOP,

    /**
     * Mauritania
     */
    MRO,

    /**
     * Malta
     */
    MTL,

    /**
     * Mauritius
     */
    MUR,

    /**
     * Maldives
     */
    MVR,

    /**
     * Malawi
     */
    MWK,

    /**
     * Mexico
     */
    MXN,

    /**
     * Malaysia
     */
    MYR,

    /**
     * Mozambique
     */
    MZN,

    /**
     * Namibia
     */
    NAD,

    /**
     * Nigeria
     */
    NGN,

    /**
     * Nicaragua
     */
    NIO,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    NLG,

    /**
     * Norway, Bouvet Island, Svalbard and Jan Mayen
     */
    NOK,

    /**
     * Nepal
     */
    NPR,

    /**
     * New Zealand, Cook Islands, Niue, Pitcairn, Tokelau
     */
    NZD,

    /**
     * Oman
     */
    OMR,

    /**
     * Panama
     */
    PAB,

    /**
     * Peru
     */
    PEN,

    /**
     * Papua New Guinea
     */
    PGK,

    /**
     * Philippines
     */
    PHP,

    /**
     * Pakistan
     */
    PKR,

    /**
     * Poland
     */
    PLN,

    /**
     * Now replaced by the Euro (EUR): use only for historical prices that pre-date the introduction of the Euro
     */
    PTE,

    /**
     * Paraguay
     */
    PYG,

    /**
     * Qatar
     */
    QAR,

    /**
     * Romania
     */
    ROL,

    /**
     * Romania
     */
    RON,

    /**
     * Russian Federation
     */
    RUB,

    /**
     * DEPRECATED, replaced by RUB
     */
    RUR,

    /**
     * Rwanda
     */
    RWF,

    /**
     * Saudi Arabia
     */
    SAR,

    /**
     * Solomon Islands
     */
    SBD,

    /**
     * Seychelles
     */
    SCR,

    /**
     * Sudan
     */
    SDD,

    /**
     * Sweden
     */
    SEK,

    /**
     * Singapore
     */
    SGD,

    /**
     * Saint Helena
     */
    SHP,

    /**
     * Slovenia
     */
    SIT,

    /**
     * Slovakia
     */
    SKK,

    /**
     * Sierra Leone
     */
    SLL,

    /**
     * Somalia
     */
    SOS,

    /**
     * Suriname
     */
    SRD,

    /**
     * DEPRECATED, replaced by SRD
     */
    SRG,

    /**
     * So Tome and Principe
     */
    STD,

    /**
     * El Salvador
     */
    SVC,

    /**
     * Syrian Arab Republic
     */
    SYP,

    /**
     * Swaziland
     */
    SZL,

    /**
     * Thailand
     */
    THB,

    /**
     * Tajikistan
     */
    TJS,

    /**
     * Turkmenistan
     */
    TMM,

    /**
     * Tunisia
     */
    TND,

    /**
     * Tonga
     */
    TOP,

    /**
     * NO LONGER VALID, Timor-Leste now uses the US Dollar
     */
    TPE,

    /**
     * Turkey
     */
    TRL,

    /**
     * Turkey, from 1 January 2005
     */
    TRY,

    /**
     * Trinidad and Tobago
     */
    TTD,

    /**
     * Taiwan (Province of China)
     */
    TWD,

    /**
     * Tanzania (United Republic of)
     */
    TZS,

    /**
     * Ukraine
     */
    UAH,

    /**
     * Uganda
     */
    UGX,

    /**
     * United States, American Samoa, British Indian Ocean Territory, Ecuador, Guam, Marshall Is, Micronesia (Federated States of), Northern Mariana Is, Palau, Puerto Rico, Timor-Leste, Turks & Caicos Is, US Minor Outlying Is, Virgin Is (British), Virgin Is (US)
     */
    USD,

    /**
     * Uruguay
     */
    UYU,

    /**
     * Uzbekistan
     */
    UZS,

    /**
     * Venezuela
     */
    VEB,

    /**
     * Viet Nam
     */
    VND,

    /**
     * Vanuatu
     */
    VUV,

    /**
     * Samoa
     */
    WST,

    /**
     * Cameroon, Central African Republic, Chad, Congo, Equatorial Guinea, Gabon
     */
    XAF,

    /**
     * Anguilla, Antigua and Barbuda, Dominica, Grenada, Montserrat, Saint Kitts and Nevis, Saint Lucia, Saint Vincent and the Grenadines
     */
    XCD,

    /**
     * Benin, Burkina Faso, Cte D'Ivoire, Mali, Niger, Senegal, Togo
     */
    XOF,

    /**
     * French Polynesia, New Caledonia, Wallis and Futuna
     */
    XPF,

    /**
     * Yemen
     */
    YER,

    /**
     * DEPRECATED, replaced by CSD
     */
    YUM,

    /**
     * South Africa
     */
    ZAR,

    /**
     * Zambia
     */
    ZMK,

    /**
     * Zimbabwe
     */
    ZWD;

    public String value() {
        return name();
    }

    public static CurrencyCode fromValue(String v) {
        return valueOf(v);
    }

}
