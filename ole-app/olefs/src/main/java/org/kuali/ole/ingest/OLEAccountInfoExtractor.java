package org.kuali.ole.ingest;

import java.util.HashMap;
import java.util.Map;

/**
 * OLEAccountInfoExtractor adds the fund Code OLE as a Map and as well as return the function code based on the given vendorReferenceNumber
 */
public class OLEAccountInfoExtractor {
    private Map<String, HashMap<String, String>> fundCodesForOLE;

    /**
     *  Default constructor of OLEAccountInfoExtractor.
     *  adds the fund Code OLE as a MAP.
     */
    public OLEAccountInfoExtractor() {
        fundCodesForOLE = new HashMap();
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("2947430", "7112");
        fundCodesForOLE.put("43", map1);
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("2947485", "7112");
        fundCodesForOLE.put("13", map2);
        HashMap<String, String> map3 = new HashMap<String, String>();
        map3.put("2947483", "7112");
        fundCodesForOLE.put("74", map3);
        HashMap<String, String> map4 = new HashMap<String, String>();
        map4.put("2947499", "7112");
        fundCodesForOLE.put("98", map4);
        HashMap<String, String> map5 = new HashMap<String, String>();
        map5.put("2947494", "7112");
        fundCodesForOLE.put("65", map5);
        HashMap<String, String> map6 = new HashMap<String, String>();
        map6.put("2947486", "7112");
        fundCodesForOLE.put("19", map6);
        HashMap<String, String> map7 = new HashMap<String, String>();
        map7.put("2947493", "7112");
        fundCodesForOLE.put("64", map7);
        HashMap<String, String> map8 = new HashMap<String, String>();
        map8.put("2947496", "7112");
        fundCodesForOLE.put("93", map8);
        HashMap<String, String> map9 = new HashMap<String, String>();
        map9.put("2947497", "7112");
        fundCodesForOLE.put("94", map9);
        HashMap<String, String> map10 = new HashMap<String, String>();
        map10.put("2947488", "7112");
        fundCodesForOLE.put("35", map10);
        HashMap<String, String> map11 = new HashMap<String, String>();
        map11.put("2947498", "7112");
        fundCodesForOLE.put("95", map11);
        HashMap<String, String> map12 = new HashMap<String, String>();
        map12.put("2947487", "7112");
        fundCodesForOLE.put("21", map12);
        HashMap<String, String> map13 = new HashMap<String, String>();
        map13.put("2947489", "7112");
        fundCodesForOLE.put("37", map13);
        HashMap<String, String> map14 = new HashMap<String, String>();
        map14.put("2947482", "7112");
        fundCodesForOLE.put("27", map14);
        HashMap<String, String> map15 = new HashMap<String, String>();
        map15.put("2947490", "7112");
        fundCodesForOLE.put("50", map15);
        HashMap<String, String> map16 = new HashMap<String, String>();
        map16.put("2947492", "7112");
        fundCodesForOLE.put("53", map16);
        HashMap<String, String> map17 = new HashMap<String, String>();
        map17.put("2947491", "7112");
        fundCodesForOLE.put("51", map17);
        HashMap<String, String> map18 = new HashMap<String, String>();
        map18.put("2947495", "7112");
        fundCodesForOLE.put("86", map18);
    }

    /**
     *  This method returns functionCode for the corresponding vendorReferenceNumber
     * @param vendorReferenceNumber
     * @return  fundCode.
     */
    public HashMap<String, String> buildAccountInfoMap(String vendorReferenceNumber) {
        return fundCodesForOLE.get(vendorReferenceNumber);
    }
}
