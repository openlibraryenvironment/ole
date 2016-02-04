package org.kuali.ole.oleng.handler;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.ole.oleng.batch.profile.model.*;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 12/16/2015.
 */
public class BatchProfileRequestHandlerTest_IT {

    @Test
    public void testConvertJsonToDataMapping() throws Exception {

        String jsonString = "{\n" +
                "\t\"dataMappingDocTypes\": [{\n" +
                "\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\"name\": \"Bibliographic\",\n" +
                "\t\t\"$$hashKey\": \"object:31\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdings\",\n" +
                "\t\t\"name\": \"Holdings\",\n" +
                "\t\t\"$$hashKey\": \"object:32\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"item\",\n" +
                "\t\t\"name\": \"Item\",\n" +
                "\t\t\"$$hashKey\": \"object:33\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\"name\": \"EHoldings\",\n" +
                "\t\t\"$$hashKey\": \"object:34\"\n" +
                "\t}],\n" +
                "\t\"dataMappingDocType\": \"bibliographic\",\n" +
                "\t\"dataField\": \"050\",\n" +
                "\t\"subField\": \"a\",\n" +
                "\t\"destinations\": [{\n" +
                "\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\"name\": \"Bibliographic\",\n" +
                "\t\t\"$$hashKey\": \"object:31\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdings\",\n" +
                "\t\t\"name\": \"Holdings\",\n" +
                "\t\t\"$$hashKey\": \"object:32\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"item\",\n" +
                "\t\t\"name\": \"Item\",\n" +
                "\t\t\"$$hashKey\": \"object:33\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\"name\": \"EHoldings\",\n" +
                "\t\t\"$$hashKey\": \"object:34\"\n" +
                "\t}],\n" +
                "\t\"destination\": \"holdings\",\n" +
                "\t\"fields\": [{\n" +
                "\t\t\"id\": \"url\",\n" +
                "\t\t\"name\": \"URL\",\n" +
                "\t\t\"$$hashKey\": \"object:394\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"callnumber\",\n" +
                "\t\t\"name\": \"Call Number\",\n" +
                "\t\t\"$$hashKey\": \"object:395\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"callnumbertype\",\n" +
                "\t\t\"name\": \"Call Number Type\",\n" +
                "\t\t\"$$hashKey\": \"object:396\"\n" +
                "\t}],\n" +
                "\t\"field\": \"callnumber\",\n" +
                "\t\"showRemove\": true,\n" +
                "\t\"$$hashKey\": \"object:452\"\n" +
                "}\n";

        System.out.println(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchProfileDataMapping batchProfileDataMapping = objectMapper.readValue(jsonString, BatchProfileDataMapping.class);
        assertNotNull(batchProfileDataMapping);
        System.out.println(batchProfileDataMapping);
    }

    @Test
    public void testConvertJsonToMatchPoint() throws Exception {

        String jsonString = "{\n" +
                "\t\"matchPointDocTypes\": [{\n" +
                "\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\"name\": \"Bibliographic\",\n" +
                "\t\t\"$$hashKey\": \"object:21\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdings\",\n" +
                "\t\t\"name\": \"Holdings\",\n" +
                "\t\t\"$$hashKey\": \"object:22\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"item\",\n" +
                "\t\t\"name\": \"Item\",\n" +
                "\t\t\"$$hashKey\": \"object:23\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\"name\": \"EHoldings\",\n" +
                "\t\t\"$$hashKey\": \"object:24\"\n" +
                "\t}],\n" +
                "\t\"matchPointDocType\": \"bibliographic\",\n" +
                "\t\"matchPointValue\": \"980 $a\",\n" +
                "\t\"holdingsMatchPoints\": [{\n" +
                "\t\t\"id\": \"callNumber\",\n" +
                "\t\t\"name\": \"Call Number\",\n" +
                "\t\t\"$$hashKey\": \"object:37\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"callNumberPrefix\",\n" +
                "\t\t\"name\": \"Call Number Prefix\",\n" +
                "\t\t\"$$hashKey\": \"object:38\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"callNumberType\",\n" +
                "\t\t\"name\": \"Call Number Type\",\n" +
                "\t\t\"$$hashKey\": \"object:39\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"copyNumber\",\n" +
                "\t\t\"name\": \"Copy Number\",\n" +
                "\t\t\"$$hashKey\": \"object:40\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel1\",\n" +
                "\t\t\"name\": \"Location Level1\",\n" +
                "\t\t\"$$hashKey\": \"object:41\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel2\",\n" +
                "\t\t\"name\": \"Location Level2\",\n" +
                "\t\t\"$$hashKey\": \"object:42\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel3\",\n" +
                "\t\t\"name\": \"Location Level3\",\n" +
                "\t\t\"$$hashKey\": \"object:43\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel4\",\n" +
                "\t\t\"name\": \"Location Level4\",\n" +
                "\t\t\"$$hashKey\": \"object:44\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel5\",\n" +
                "\t\t\"name\": \"Location Level5\",\n" +
                "\t\t\"$$hashKey\": \"object:45\"\n" +
                "\t}],\n" +
                "\t\"itemMatchPoints\": [{\n" +
                "\t\t\"id\": \"holdingsLocationLevel1\",\n" +
                "\t\t\"name\": \"Holdings Location Level1\",\n" +
                "\t\t\"$$hashKey\": \"object:65\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdingsLocationLevel2\",\n" +
                "\t\t\"name\": \"Holdings Location Level2\",\n" +
                "\t\t\"$$hashKey\": \"object:66\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdingsLocationLevel3\",\n" +
                "\t\t\"name\": \"Holdings Location Level3\",\n" +
                "\t\t\"$$hashKey\": \"object:67\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdingsLocationLevel4\",\n" +
                "\t\t\"name\": \"Holdings Location Level4\",\n" +
                "\t\t\"$$hashKey\": \"object:68\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdingsLocationLevel5\",\n" +
                "\t\t\"name\": \"Holdings Location Level5\",\n" +
                "\t\t\"$$hashKey\": \"object:69\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdingsCallNumber\",\n" +
                "\t\t\"name\": \"Holdings Call Number\",\n" +
                "\t\t\"$$hashKey\": \"object:70\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdingsCallNumberPrefix\",\n" +
                "\t\t\"name\": \"Holdings Call Number Prefix\",\n" +
                "\t\t\"$$hashKey\": \"object:71\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdingsCallNumberType\",\n" +
                "\t\t\"name\": \"Holdings Call Number Type\",\n" +
                "\t\t\"$$hashKey\": \"object:72\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdingsCopyNumber\",\n" +
                "\t\t\"name\": \"Holdings Copy Number\",\n" +
                "\t\t\"$$hashKey\": \"object:73\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"callNumber\",\n" +
                "\t\t\"name\": \"Call Number\",\n" +
                "\t\t\"$$hashKey\": \"object:74\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"callNumberPrefix\",\n" +
                "\t\t\"name\": \"Call Number Prefix\",\n" +
                "\t\t\"$$hashKey\": \"object:75\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"callNumberType\",\n" +
                "\t\t\"name\": \"Call Number Type\",\n" +
                "\t\t\"$$hashKey\": \"object:76\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"copyNumber\",\n" +
                "\t\t\"name\": \"Copy Number\",\n" +
                "\t\t\"$$hashKey\": \"object:77\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel1\",\n" +
                "\t\t\"name\": \"Location Level1\",\n" +
                "\t\t\"$$hashKey\": \"object:78\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel2\",\n" +
                "\t\t\"name\": \"Location Level2\",\n" +
                "\t\t\"$$hashKey\": \"object:79\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel3\",\n" +
                "\t\t\"name\": \"Location Level3\",\n" +
                "\t\t\"$$hashKey\": \"object:80\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel4\",\n" +
                "\t\t\"name\": \"Location Level4\",\n" +
                "\t\t\"$$hashKey\": \"object:81\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel5\",\n" +
                "\t\t\"name\": \"Location Level5\",\n" +
                "\t\t\"$$hashKey\": \"object:82\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"barcode\",\n" +
                "\t\t\"name\": \"Item Barcode\",\n" +
                "\t\t\"$$hashKey\": \"object:83\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"type\",\n" +
                "\t\t\"name\": \"Item Type\",\n" +
                "\t\t\"$$hashKey\": \"object:84\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"status\",\n" +
                "\t\t\"name\": \"Item Status\",\n" +
                "\t\t\"$$hashKey\": \"object:85\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"donorCode\",\n" +
                "\t\t\"name\": \"Donor Code\",\n" +
                "\t\t\"$$hashKey\": \"object:86\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"donorPublicDisplay\",\n" +
                "\t\t\"name\": \"Donor Public Display\",\n" +
                "\t\t\"$$hashKey\": \"object:87\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"donorNote\",\n" +
                "\t\t\"name\": \"Donor Note\",\n" +
                "\t\t\"$$hashKey\": \"object:88\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"enumeration\",\n" +
                "\t\t\"name\": \"Enumeration\",\n" +
                "\t\t\"$$hashKey\": \"object:89\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"chronology\",\n" +
                "\t\t\"name\": \"Chronology\",\n" +
                "\t\t\"$$hashKey\": \"object:90\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"vendorLineItemId\",\n" +
                "\t\t\"name\": \"Vendor Line Item Identifier\",\n" +
                "\t\t\"$$hashKey\": \"object:91\"\n" +
                "\t}],\n" +
                "\t\"eHoldingsMatchPoints\": [{\n" +
                "\t\t\"id\": \"callNumber\",\n" +
                "\t\t\"name\": \"Call Number\",\n" +
                "\t\t\"$$hashKey\": \"object:147\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"accessStatus\",\n" +
                "\t\t\"name\": \"Access Status\",\n" +
                "\t\t\"$$hashKey\": \"object:148\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"callNumberType\",\n" +
                "\t\t\"name\": \"Call Number Type\",\n" +
                "\t\t\"$$hashKey\": \"object:149\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel1\",\n" +
                "\t\t\"name\": \"Location Level1\",\n" +
                "\t\t\"$$hashKey\": \"object:150\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel2\",\n" +
                "\t\t\"name\": \"Location Level2\",\n" +
                "\t\t\"$$hashKey\": \"object:151\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel3\",\n" +
                "\t\t\"name\": \"Location Level3\",\n" +
                "\t\t\"$$hashKey\": \"object:152\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel4\",\n" +
                "\t\t\"name\": \"Location Level4\",\n" +
                "\t\t\"$$hashKey\": \"object:153\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"locationLevel5\",\n" +
                "\t\t\"name\": \"Location Level5\",\n" +
                "\t\t\"$$hashKey\": \"object:154\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"url\",\n" +
                "\t\t\"name\": \"URL\",\n" +
                "\t\t\"$$hashKey\": \"object:155\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"persistentLink\",\n" +
                "\t\t\"name\": \"Persistent Link\",\n" +
                "\t\t\"$$hashKey\": \"object:156\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"linkText\",\n" +
                "\t\t\"name\": \"Link Text\",\n" +
                "\t\t\"$$hashKey\": \"object:157\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"donorCode\",\n" +
                "\t\t\"name\": \"Donor Code\",\n" +
                "\t\t\"$$hashKey\": \"object:158\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"donorPublicDisplay\",\n" +
                "\t\t\"name\": \"Donor Public Display\",\n" +
                "\t\t\"$$hashKey\": \"object:159\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"donorNote\",\n" +
                "\t\t\"name\": \"Donor Note\",\n" +
                "\t\t\"$$hashKey\": \"object:160\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"statisticalCode\",\n" +
                "\t\t\"name\": \"Statistical Code\",\n" +
                "\t\t\"$$hashKey\": \"object:161\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"platform\",\n" +
                "\t\t\"name\": \"Platform\",\n" +
                "\t\t\"$$hashKey\": \"object:162\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"publisher\",\n" +
                "\t\t\"name\": \"Publisher\",\n" +
                "\t\t\"$$hashKey\": \"object:163\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"coverageStartDate\",\n" +
                "\t\t\"name\": \"Coverage Start Date\",\n" +
                "\t\t\"$$hashKey\": \"object:164\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"coverageStartIssue\",\n" +
                "\t\t\"name\": \"Coverage Start Issue\",\n" +
                "\t\t\"$$hashKey\": \"object:165\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"coverageStartVolume\",\n" +
                "\t\t\"name\": \"Coverage Start Volume\",\n" +
                "\t\t\"$$hashKey\": \"object:166\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"coverageEndDate\",\n" +
                "\t\t\"name\": \"Coverage End Date\",\n" +
                "\t\t\"$$hashKey\": \"object:167\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"coverageEndIssue\",\n" +
                "\t\t\"name\": \"Coverage End Issue\",\n" +
                "\t\t\"$$hashKey\": \"object:168\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"coverageEndVolume\",\n" +
                "\t\t\"name\": \"Coverage End Volume\",\n" +
                "\t\t\"$$hashKey\": \"object:169\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"eResourceName\",\n" +
                "\t\t\"name\": \"EResource Name\",\n" +
                "\t\t\"$$hashKey\": \"object:170\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"eResourceId\",\n" +
                "\t\t\"name\": \"EResource Id\",\n" +
                "\t\t\"$$hashKey\": \"object:171\"\n" +
                "\t}],\n" +
                "\t\"showRemove\": true,\n" +
                "\t\"$$hashKey\": \"object:444\"\n" +
                "}";

        System.out.println(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchProfileMatchPoint batchProfileMatchPoint = objectMapper.readValue(jsonString, BatchProfileMatchPoint.class);
        assertNotNull(batchProfileMatchPoint);
        System.out.println(batchProfileMatchPoint);
    }

    @Test
    public void testConvertJsonToAddOrOverlay() throws Exception {

        String jsonString = "{\n" +
                "\t\"matchOptions\": [{\n" +
                "\t\t\"id\": \"doMatch\",\n" +
                "\t\t\"name\": \"Do Match\",\n" +
                "\t\t\"$$hashKey\": \"object:251\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"doNotMatch\",\n" +
                "\t\t\"name\": \"Do Not Match\",\n" +
                "\t\t\"$$hashKey\": \"object:252\"\n" +
                "\t}],\n" +
                "\t\"matchOption\": \"doMatch\",\n" +
                "\t\"addOrOverlayDocTypes\": [{\n" +
                "\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\"name\": \"Bibliographic\",\n" +
                "\t\t\"$$hashKey\": \"object:259\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdings\",\n" +
                "\t\t\"name\": \"Holdings\",\n" +
                "\t\t\"$$hashKey\": \"object:260\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"item\",\n" +
                "\t\t\"name\": \"Item\",\n" +
                "\t\t\"$$hashKey\": \"object:261\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\"name\": \"EHoldings\",\n" +
                "\t\t\"$$hashKey\": \"object:262\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"po\",\n" +
                "\t\t\"name\": \"Purchase Order\",\n" +
                "\t\t\"$$hashKey\": \"object:263\"\n" +
                "\t}],\n" +
                "\t\"addOrOverlayDocType\": \"holdings\",\n" +
                "\t\"operations\": [{\n" +
                "\t\t\"id\": \"add\",\n" +
                "\t\t\"name\": \"Add\",\n" +
                "\t\t\"$$hashKey\": \"object:276\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"overlay\",\n" +
                "\t\t\"name\": \"Overlay\",\n" +
                "\t\t\"$$hashKey\": \"object:277\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"discard\",\n" +
                "\t\t\"name\": \"Discard\",\n" +
                "\t\t\"$$hashKey\": \"object:278\"\n" +
                "\t}],\n" +
                "\t\"operation\": \"overlay\",\n" +
                "\t\"poOperations\": [{\n" +
                "\t\t\"id\": \"create\",\n" +
                "\t\t\"name\": \"Create PO if matched\",\n" +
                "\t\t\"$$hashKey\": \"object:298\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"update\",\n" +
                "\t\t\"name\": \"Update PO if matched\",\n" +
                "\t\t\"$$hashKey\": \"object:299\"\n" +
                "\t}],\n" +
                "\t\"bibStatuses\": [{\n" +
                "\t\t\"id\": \"none\",\n" +
                "\t\t\"name\": \"None\",\n" +
                "\t\t\"$$hashKey\": \"object:305\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"cataloguing\",\n" +
                "\t\t\"name\": \"Cataloguing\",\n" +
                "\t\t\"$$hashKey\": \"object:306\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"catalogued\",\n" +
                "\t\t\"name\": \"Catalogued\",\n" +
                "\t\t\"$$hashKey\": \"object:307\"\n" +
                "\t}],\n" +
                "\t\"bibStatus\": \"none\",\n" +
                "\t\"addOperations\": [{\n" +
                "\t\t\"id\": \"deleteAll\",\n" +
                "\t\t\"name\": \"Delete all existing and add\",\n" +
                "\t\t\"$$hashKey\": \"object:316\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"keepAll\",\n" +
                "\t\t\"name\": \"Keep all existing and add\",\n" +
                "\t\t\"$$hashKey\": \"object:317\"\n" +
                "\t}],\n" +
                "\t\"addOperation\": \"deleteAll\",\n" +
                "\t\"addItems\": false,\n" +
                "\t\"showRemove\": true,\n" +
                "\t\"$$hashKey\": \"object:444\"\n" +
                "}";

        System.out.println(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchProfileAddOrOverlay profileAddOrOverlay = objectMapper.readValue(jsonString, BatchProfileAddOrOverlay.class);
        assertNotNull(profileAddOrOverlay);
        System.out.println(profileAddOrOverlay);
    }

    @Test
    public void testConvertJsonToFieldOperationy() throws Exception {

        String jsonString = "{\n" +
                " \t\"fieldOperationTypes\": [{\n" +
                " \t\t\"id\": \"global\",\n" +
                " \t\t\"name\": \"Globally Protected Field\",\n" +
                " \t\t\"$$hashKey\": \"object:251\"\n" +
                " \t}, {\n" +
                " \t\t\"id\": \"profile\",\n" +
                " \t\t\"name\": \"Profile Protected Field\",\n" +
                " \t\t\"$$hashKey\": \"object:252\"\n" +
                " \t}, {\n" +
                " \t\t\"id\": \"delete\",\n" +
                " \t\t\"name\": \"Delete Field\",\n" +
                " \t\t\"$$hashKey\": \"object:253\"\n" +
                " \t}, {\n" +
                " \t\t\"id\": \"rename\",\n" +
                " \t\t\"name\": \"Rename Field\",\n" +
                " \t\t\"$$hashKey\": \"object:254\"\n" +
                " \t}],\n" +
                " \t\"fieldOperationType\": \"global\",\n" +
                " \t\"dataField\": \"245\",\n" +
                " \t\"subField\": \"a\",\n" +
                " \t\"showRemove\": true,\n" +
                " \t\"$$hashKey\": \"object:452\"\n" +
                " }";

        System.out.println(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchProfileFieldOperation batchProfileFieldOperation = objectMapper.readValue(jsonString, BatchProfileFieldOperation.class);
        assertNotNull(batchProfileFieldOperation);
        System.out.println(batchProfileFieldOperation);
    }

    @Test
    public void testConvertJsonToDataTransformation() throws Exception {

        String jsonString = "{\n" +
                "\t\"dataTransformationDocTypes\": [{\n" +
                "\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\"name\": \"Bibliographic\",\n" +
                "\t\t\"$$hashKey\": \"object:21\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"holdings\",\n" +
                "\t\t\"name\": \"Holdings\",\n" +
                "\t\t\"$$hashKey\": \"object:22\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"item\",\n" +
                "\t\t\"name\": \"Item\",\n" +
                "\t\t\"$$hashKey\": \"object:23\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\"name\": \"EHoldings\",\n" +
                "\t\t\"$$hashKey\": \"object:24\"\n" +
                "\t}],\n" +
                "\t\"dataTransformationDocType\": \"bibliographic\",\n" +
                "\t\"dataField\": \"980\",\n" +
                "\t\"subField\": \"a\",\n" +
                "\t\"transformers\": [{\n" +
                "\t\t\"id\": \"regex\",\n" +
                "\t\t\"name\": \"Regex Pattern Transformer\",\n" +
                "\t\t\"$$hashKey\": \"object:352\"\n" +
                "\t}, {\n" +
                "\t\t\"id\": \"\",\n" +
                "\t\t\"name\": \"\",\n" +
                "\t\t\"$$hashKey\": \"object:353\"\n" +
                "\t}],\n" +
                "\t\"transformer\": \"regex\",\n" +
                "\t\"expression\": \"ocn\",\n" +
                "\t\"showRemove\": true,\n" +
                "\t\"$$hashKey\": \"object:454\"\n" +
                "}";

        System.out.println(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchProfileDataTransformer batchProfileDataTransformer = objectMapper.readValue(jsonString, BatchProfileDataTransformer.class);
        assertNotNull(batchProfileDataTransformer);
        System.out.println(batchProfileDataTransformer);
    }

    @Test
    public void testConvertJsonToProfile() throws Exception {

        String jsonString = "{\n" +
                "\t\"mainSection\": {\n" +
                "\t\t\"title\": \"Main Section\",\n" +
                "\t\t\"collapsed\": true,\n" +
                "\t\t\"profileName\": \"Test Name\",\n" +
                "\t\t\"profileDescription\": \"Description\"\n" +
                "\t},\n" +
                "\t\"batchProfileMatchPointList\": [{\n" +
                "\t\t\"title\": \"Match Points\",\n" +
                "\t\t\"matchPointDocTypes\": [{\n" +
                "\t\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\t\"name\": \"Bibliographic\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdings\",\n" +
                "\t\t\t\"name\": \"Holdings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"item\",\n" +
                "\t\t\t\"name\": \"Item\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\t\"name\": \"EHoldings\"\n" +
                "\t\t}],\n" +
                "\t\t\"matchPointDocType\": \"bibliographic\",\n" +
                "\t\t\"holdingsMatchPoints\": [{\n" +
                "\t\t\t\"id\": \"callNumber\",\n" +
                "\t\t\t\"name\": \"Call Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumberPrefix\",\n" +
                "\t\t\t\"name\": \"Call Number Prefix\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumberType\",\n" +
                "\t\t\t\"name\": \"Call Number Type\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"copyNumber\",\n" +
                "\t\t\t\"name\": \"Copy Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel1\",\n" +
                "\t\t\t\"name\": \"Location Level1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel2\",\n" +
                "\t\t\t\"name\": \"Location Level2\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel3\",\n" +
                "\t\t\t\"name\": \"Location Level3\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel4\",\n" +
                "\t\t\t\"name\": \"Location Level4\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel5\",\n" +
                "\t\t\t\"name\": \"Location Level5\"\n" +
                "\t\t}],\n" +
                "\t\t\"itemMatchPoints\": [{\n" +
                "\t\t\t\"id\": \"holdingsLocationLevel1\",\n" +
                "\t\t\t\"name\": \"Holdings Location Level1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsLocationLevel2\",\n" +
                "\t\t\t\"name\": \"Holdings Location Level2\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsLocationLevel3\",\n" +
                "\t\t\t\"name\": \"Holdings Location Level3\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsLocationLevel4\",\n" +
                "\t\t\t\"name\": \"Holdings Location Level4\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsLocationLevel5\",\n" +
                "\t\t\t\"name\": \"Holdings Location Level5\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsCallNumber\",\n" +
                "\t\t\t\"name\": \"Holdings Call Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsCallNumberPrefix\",\n" +
                "\t\t\t\"name\": \"Holdings Call Number Prefix\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsCallNumberType\",\n" +
                "\t\t\t\"name\": \"Holdings Call Number Type\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsCopyNumber\",\n" +
                "\t\t\t\"name\": \"Holdings Copy Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumber\",\n" +
                "\t\t\t\"name\": \"Call Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumberPrefix\",\n" +
                "\t\t\t\"name\": \"Call Number Prefix\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumberType\",\n" +
                "\t\t\t\"name\": \"Call Number Type\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"copyNumber\",\n" +
                "\t\t\t\"name\": \"Copy Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel1\",\n" +
                "\t\t\t\"name\": \"Location Level1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel2\",\n" +
                "\t\t\t\"name\": \"Location Level2\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel3\",\n" +
                "\t\t\t\"name\": \"Location Level3\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel4\",\n" +
                "\t\t\t\"name\": \"Location Level4\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel5\",\n" +
                "\t\t\t\"name\": \"Location Level5\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"barcode\",\n" +
                "\t\t\t\"name\": \"Item Barcode\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"type\",\n" +
                "\t\t\t\"name\": \"Item Type\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"status\",\n" +
                "\t\t\t\"name\": \"Item Status\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorCode\",\n" +
                "\t\t\t\"name\": \"Donor Code\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorPublicDisplay\",\n" +
                "\t\t\t\"name\": \"Donor Public Display\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorNote\",\n" +
                "\t\t\t\"name\": \"Donor Note\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"enumeration\",\n" +
                "\t\t\t\"name\": \"Enumeration\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"chronology\",\n" +
                "\t\t\t\"name\": \"Chronology\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"vendorLineItemId\",\n" +
                "\t\t\t\"name\": \"Vendor Line Item Identifier\"\n" +
                "\t\t}],\n" +
                "\t\t\"eHoldingsMatchPoints\": [{\n" +
                "\t\t\t\"id\": \"callNumber\",\n" +
                "\t\t\t\"name\": \"Call Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"accessStatus\",\n" +
                "\t\t\t\"name\": \"Access Status\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumberType\",\n" +
                "\t\t\t\"name\": \"Call Number Type\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel1\",\n" +
                "\t\t\t\"name\": \"Location Level1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel2\",\n" +
                "\t\t\t\"name\": \"Location Level2\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel3\",\n" +
                "\t\t\t\"name\": \"Location Level3\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel4\",\n" +
                "\t\t\t\"name\": \"Location Level4\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel5\",\n" +
                "\t\t\t\"name\": \"Location Level5\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"url\",\n" +
                "\t\t\t\"name\": \"URL\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"persistentLink\",\n" +
                "\t\t\t\"name\": \"Persistent Link\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"linkText\",\n" +
                "\t\t\t\"name\": \"Link Text\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorCode\",\n" +
                "\t\t\t\"name\": \"Donor Code\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorPublicDisplay\",\n" +
                "\t\t\t\"name\": \"Donor Public Display\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorNote\",\n" +
                "\t\t\t\"name\": \"Donor Note\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"statisticalCode\",\n" +
                "\t\t\t\"name\": \"Statistical Code\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"platform\",\n" +
                "\t\t\t\"name\": \"Platform\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"publisher\",\n" +
                "\t\t\t\"name\": \"Publisher\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageStartDate\",\n" +
                "\t\t\t\"name\": \"Coverage Start Date\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageStartIssue\",\n" +
                "\t\t\t\"name\": \"Coverage Start Issue\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageStartVolume\",\n" +
                "\t\t\t\"name\": \"Coverage Start Volume\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageEndDate\",\n" +
                "\t\t\t\"name\": \"Coverage End Date\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageEndIssue\",\n" +
                "\t\t\t\"name\": \"Coverage End Issue\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageEndVolume\",\n" +
                "\t\t\t\"name\": \"Coverage End Volume\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eResourceName\",\n" +
                "\t\t\t\"name\": \"EResource Name\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eResourceId\",\n" +
                "\t\t\t\"name\": \"EResource Id\"\n" +
                "\t\t}],\n" +
                "\t\t\"collapsed\": true,\n" +
                "\t\t\"matchPointValue\": null\n" +
                "\t}, {\n" +
                "\t\t\"matchPointDocTypes\": [{\n" +
                "\t\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\t\"name\": \"Bibliographic\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdings\",\n" +
                "\t\t\t\"name\": \"Holdings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"item\",\n" +
                "\t\t\t\"name\": \"Item\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\t\"name\": \"EHoldings\"\n" +
                "\t\t}],\n" +
                "\t\t\"matchPointDocType\": \"bibliographic\",\n" +
                "\t\t\"matchPointValue\": \"980 $a\",\n" +
                "\t\t\"holdingsMatchPoints\": [{\n" +
                "\t\t\t\"id\": \"callNumber\",\n" +
                "\t\t\t\"name\": \"Call Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumberPrefix\",\n" +
                "\t\t\t\"name\": \"Call Number Prefix\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumberType\",\n" +
                "\t\t\t\"name\": \"Call Number Type\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"copyNumber\",\n" +
                "\t\t\t\"name\": \"Copy Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel1\",\n" +
                "\t\t\t\"name\": \"Location Level1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel2\",\n" +
                "\t\t\t\"name\": \"Location Level2\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel3\",\n" +
                "\t\t\t\"name\": \"Location Level3\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel4\",\n" +
                "\t\t\t\"name\": \"Location Level4\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel5\",\n" +
                "\t\t\t\"name\": \"Location Level5\"\n" +
                "\t\t}],\n" +
                "\t\t\"itemMatchPoints\": [{\n" +
                "\t\t\t\"id\": \"holdingsLocationLevel1\",\n" +
                "\t\t\t\"name\": \"Holdings Location Level1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsLocationLevel2\",\n" +
                "\t\t\t\"name\": \"Holdings Location Level2\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsLocationLevel3\",\n" +
                "\t\t\t\"name\": \"Holdings Location Level3\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsLocationLevel4\",\n" +
                "\t\t\t\"name\": \"Holdings Location Level4\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsLocationLevel5\",\n" +
                "\t\t\t\"name\": \"Holdings Location Level5\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsCallNumber\",\n" +
                "\t\t\t\"name\": \"Holdings Call Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsCallNumberPrefix\",\n" +
                "\t\t\t\"name\": \"Holdings Call Number Prefix\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsCallNumberType\",\n" +
                "\t\t\t\"name\": \"Holdings Call Number Type\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdingsCopyNumber\",\n" +
                "\t\t\t\"name\": \"Holdings Copy Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumber\",\n" +
                "\t\t\t\"name\": \"Call Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumberPrefix\",\n" +
                "\t\t\t\"name\": \"Call Number Prefix\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumberType\",\n" +
                "\t\t\t\"name\": \"Call Number Type\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"copyNumber\",\n" +
                "\t\t\t\"name\": \"Copy Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel1\",\n" +
                "\t\t\t\"name\": \"Location Level1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel2\",\n" +
                "\t\t\t\"name\": \"Location Level2\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel3\",\n" +
                "\t\t\t\"name\": \"Location Level3\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel4\",\n" +
                "\t\t\t\"name\": \"Location Level4\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel5\",\n" +
                "\t\t\t\"name\": \"Location Level5\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"barcode\",\n" +
                "\t\t\t\"name\": \"Item Barcode\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"type\",\n" +
                "\t\t\t\"name\": \"Item Type\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"status\",\n" +
                "\t\t\t\"name\": \"Item Status\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorCode\",\n" +
                "\t\t\t\"name\": \"Donor Code\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorPublicDisplay\",\n" +
                "\t\t\t\"name\": \"Donor Public Display\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorNote\",\n" +
                "\t\t\t\"name\": \"Donor Note\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"enumeration\",\n" +
                "\t\t\t\"name\": \"Enumeration\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"chronology\",\n" +
                "\t\t\t\"name\": \"Chronology\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"vendorLineItemId\",\n" +
                "\t\t\t\"name\": \"Vendor Line Item Identifier\"\n" +
                "\t\t}],\n" +
                "\t\t\"eHoldingsMatchPoints\": [{\n" +
                "\t\t\t\"id\": \"callNumber\",\n" +
                "\t\t\t\"name\": \"Call Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"accessStatus\",\n" +
                "\t\t\t\"name\": \"Access Status\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callNumberType\",\n" +
                "\t\t\t\"name\": \"Call Number Type\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel1\",\n" +
                "\t\t\t\"name\": \"Location Level1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel2\",\n" +
                "\t\t\t\"name\": \"Location Level2\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel3\",\n" +
                "\t\t\t\"name\": \"Location Level3\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel4\",\n" +
                "\t\t\t\"name\": \"Location Level4\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"locationLevel5\",\n" +
                "\t\t\t\"name\": \"Location Level5\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"url\",\n" +
                "\t\t\t\"name\": \"URL\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"persistentLink\",\n" +
                "\t\t\t\"name\": \"Persistent Link\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"linkText\",\n" +
                "\t\t\t\"name\": \"Link Text\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorCode\",\n" +
                "\t\t\t\"name\": \"Donor Code\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorPublicDisplay\",\n" +
                "\t\t\t\"name\": \"Donor Public Display\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"donorNote\",\n" +
                "\t\t\t\"name\": \"Donor Note\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"statisticalCode\",\n" +
                "\t\t\t\"name\": \"Statistical Code\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"platform\",\n" +
                "\t\t\t\"name\": \"Platform\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"publisher\",\n" +
                "\t\t\t\"name\": \"Publisher\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageStartDate\",\n" +
                "\t\t\t\"name\": \"Coverage Start Date\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageStartIssue\",\n" +
                "\t\t\t\"name\": \"Coverage Start Issue\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageStartVolume\",\n" +
                "\t\t\t\"name\": \"Coverage Start Volume\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageEndDate\",\n" +
                "\t\t\t\"name\": \"Coverage End Date\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageEndIssue\",\n" +
                "\t\t\t\"name\": \"Coverage End Issue\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"coverageEndVolume\",\n" +
                "\t\t\t\"name\": \"Coverage End Volume\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eResourceName\",\n" +
                "\t\t\t\"name\": \"EResource Name\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eResourceId\",\n" +
                "\t\t\t\"name\": \"EResource Id\"\n" +
                "\t\t}],\n" +
                "\t\t\"showRemove\": true\n" +
                "\t}],\n" +
                "\t\"batchProfileAddOrOverlayList\": [{\n" +
                "\t\t\"title\": \"Matching, Add and Overlay\",\n" +
                "\t\t\"matchOptions\": [{\n" +
                "\t\t\t\"id\": \"doMatch\",\n" +
                "\t\t\t\"name\": \"Do Match\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"doNotMatch\",\n" +
                "\t\t\t\"name\": \"Do Not Match\"\n" +
                "\t\t}],\n" +
                "\t\t\"matchOption\": \"doMatch\",\n" +
                "\t\t\"addOrOverlayDocTypes\": [{\n" +
                "\t\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\t\"name\": \"Bibliographic\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdings\",\n" +
                "\t\t\t\"name\": \"Holdings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"item\",\n" +
                "\t\t\t\"name\": \"Item\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\t\"name\": \"EHoldings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"po\",\n" +
                "\t\t\t\"name\": \"Purchase Order\"\n" +
                "\t\t}],\n" +
                "\t\t\"addOrOverlayDocType\": \"bibliographic\",\n" +
                "\t\t\"operations\": [{\n" +
                "\t\t\t\"id\": \"add\",\n" +
                "\t\t\t\"name\": \"Add\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"overlay\",\n" +
                "\t\t\t\"name\": \"Overlay\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"discard\",\n" +
                "\t\t\t\"name\": \"Discard\"\n" +
                "\t\t}],\n" +
                "\t\t\"operation\": \"add\",\n" +
                "\t\t\"bibDonotMatchOperations\": [{\n" +
                "\t\t\t\"id\": \"add\",\n" +
                "\t\t\t\"name\": \"Add\"\n" +
                "\t\t}],\n" +
                "\t\t\"doNotMatchOperations\": [{\n" +
                "\t\t\t\"id\": \"add\",\n" +
                "\t\t\t\"name\": \"Add\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"discard\",\n" +
                "\t\t\t\"name\": \"Discard\"\n" +
                "\t\t}],\n" +
                "\t\t\"poOperations\": [{\n" +
                "\t\t\t\"id\": \"create\",\n" +
                "\t\t\t\"name\": \"Create PO if matched\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"update\",\n" +
                "\t\t\t\"name\": \"Update PO if matched\"\n" +
                "\t\t}],\n" +
                "\t\t\"addOperations\": [{\n" +
                "\t\t\t\"id\": \"deleteAll\",\n" +
                "\t\t\t\"name\": \"Delete all existing and add\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"keepAll\",\n" +
                "\t\t\t\"name\": \"Keep all existing and add\"\n" +
                "\t\t}],\n" +
                "\t\t\"addOperation\": \"deleteAll\",\n" +
                "\t\t\"bibStatuses\": [{\n" +
                "\t\t\t\"id\": \"none\",\n" +
                "\t\t\t\"name\": \"None\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"cataloguing\",\n" +
                "\t\t\t\"name\": \"Cataloguing\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"catalogued\",\n" +
                "\t\t\t\"name\": \"Catalogued\"\n" +
                "\t\t}],\n" +
                "\t\t\"bibStatus\": \"none\",\n" +
                "\t\t\"addItems\": false,\n" +
                "\t\t\"collapsed\": true\n" +
                "\t}, {\n" +
                "\t\t\"matchOptions\": [{\n" +
                "\t\t\t\"id\": \"doMatch\",\n" +
                "\t\t\t\"name\": \"Do Match\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"doNotMatch\",\n" +
                "\t\t\t\"name\": \"Do Not Match\"\n" +
                "\t\t}],\n" +
                "\t\t\"matchOption\": \"doMatch\",\n" +
                "\t\t\"addOrOverlayDocTypes\": [{\n" +
                "\t\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\t\"name\": \"Bibliographic\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdings\",\n" +
                "\t\t\t\"name\": \"Holdings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"item\",\n" +
                "\t\t\t\"name\": \"Item\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\t\"name\": \"EHoldings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"po\",\n" +
                "\t\t\t\"name\": \"Purchase Order\"\n" +
                "\t\t}],\n" +
                "\t\t\"addOrOverlayDocType\": \"bibliographic\",\n" +
                "\t\t\"operations\": [{\n" +
                "\t\t\t\"id\": \"add\",\n" +
                "\t\t\t\"name\": \"Add\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"overlay\",\n" +
                "\t\t\t\"name\": \"Overlay\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"discard\",\n" +
                "\t\t\t\"name\": \"Discard\"\n" +
                "\t\t}],\n" +
                "\t\t\"operation\": \"overlay\",\n" +
                "\t\t\"poOperations\": [{\n" +
                "\t\t\t\"id\": \"create\",\n" +
                "\t\t\t\"name\": \"Create PO if matched\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"update\",\n" +
                "\t\t\t\"name\": \"Update PO if matched\"\n" +
                "\t\t}],\n" +
                "\t\t\"bibStatuses\": [{\n" +
                "\t\t\t\"id\": \"none\",\n" +
                "\t\t\t\"name\": \"None\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"cataloguing\",\n" +
                "\t\t\t\"name\": \"Cataloguing\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"catalogued\",\n" +
                "\t\t\t\"name\": \"Catalogued\"\n" +
                "\t\t}],\n" +
                "\t\t\"bibStatus\": \"cataloguing\",\n" +
                "\t\t\"addOperations\": [{\n" +
                "\t\t\t\"id\": \"deleteAll\",\n" +
                "\t\t\t\"name\": \"Delete all existing and add\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"keepAll\",\n" +
                "\t\t\t\"name\": \"Keep all existing and add\"\n" +
                "\t\t}],\n" +
                "\t\t\"addOperation\": \"deleteAll\",\n" +
                "\t\t\"addItems\": false,\n" +
                "\t\t\"showRemove\": true\n" +
                "\t}],\n" +
                "\t\"batchProfileFieldOperationList\": [{\n" +
                "\t\t\"title\": \"Field Operations\",\n" +
                "\t\t\"fieldOperationTypes\": [{\n" +
                "\t\t\t\"id\": \"global\",\n" +
                "\t\t\t\"name\": \"Globally Protected Field\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"profile\",\n" +
                "\t\t\t\"name\": \"Profile Protected Field\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"delete\",\n" +
                "\t\t\t\"name\": \"Delete Field\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"rename\",\n" +
                "\t\t\t\"name\": \"Rename Field\"\n" +
                "\t\t}],\n" +
                "\t\t\"fieldOperationType\": \"global\",\n" +
                "\t\t\"collapsed\": true,\n" +
                "\t\t\"dataField\": null,\n" +
                "\t\t\"subField\": null,\n" +
                "\t\t\"ind1\": null,\n" +
                "\t\t\"ind2\": null\n" +
                "\t}, {\n" +
                "\t\t\"fieldOperationTypes\": [{\n" +
                "\t\t\t\"id\": \"global\",\n" +
                "\t\t\t\"name\": \"Globally Protected Field\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"profile\",\n" +
                "\t\t\t\"name\": \"Profile Protected Field\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"delete\",\n" +
                "\t\t\t\"name\": \"Delete Field\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"rename\",\n" +
                "\t\t\t\"name\": \"Rename Field\"\n" +
                "\t\t}],\n" +
                "\t\t\"fieldOperationType\": \"global\",\n" +
                "\t\t\"dataField\": \"245\",\n" +
                "\t\t\"subField\": \"a\",\n" +
                "\t\t\"showRemove\": true\n" +
                "\t}],\n" +
                "\t\"batchProfileDataMappingList\": [{\n" +
                "\t\t\"title\": \"Data Mappings\",\n" +
                "\t\t\"dataMappingDocTypes\": [{\n" +
                "\t\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\t\"name\": \"Bibliographic\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdings\",\n" +
                "\t\t\t\"name\": \"Holdings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"item\",\n" +
                "\t\t\t\"name\": \"Item\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\t\"name\": \"EHoldings\"\n" +
                "\t\t}],\n" +
                "\t\t\"dataMappingDocType\": \"bibliographic\",\n" +
                "\t\t\"destinations\": [{\n" +
                "\t\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\t\"name\": \"Bibliographic\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdings\",\n" +
                "\t\t\t\"name\": \"Holdings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"item\",\n" +
                "\t\t\t\"name\": \"Item\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\t\"name\": \"EHoldings\"\n" +
                "\t\t}],\n" +
                "\t\t\"destination\": null,\n" +
                "\t\t\"fields\": [{\n" +
                "\t\t\t\"id\": \"url\",\n" +
                "\t\t\t\"name\": \"URL\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callnumber\",\n" +
                "\t\t\t\"name\": \"Call Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callnumbertype\",\n" +
                "\t\t\t\"name\": \"Call Number Type\"\n" +
                "\t\t}],\n" +
                "\t\t\"collapsed\": true,\n" +
                "\t\t\"dataField\": null,\n" +
                "\t\t\"subField\": null,\n" +
                "\t\t\"field\": null,\n" +
                "\t\t\"ind1\": null,\n" +
                "\t\t\"ind2\": null\n" +
                "\t}, {\n" +
                "\t\t\"dataMappingDocTypes\": [{\n" +
                "\t\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\t\"name\": \"Bibliographic\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdings\",\n" +
                "\t\t\t\"name\": \"Holdings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"item\",\n" +
                "\t\t\t\"name\": \"Item\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\t\"name\": \"EHoldings\"\n" +
                "\t\t}],\n" +
                "\t\t\"dataMappingDocType\": \"bibliographic\",\n" +
                "\t\t\"dataField\": \"050\",\n" +
                "\t\t\"subField\": \"a\",\n" +
                "\t\t\"destinations\": [{\n" +
                "\t\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\t\"name\": \"Bibliographic\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdings\",\n" +
                "\t\t\t\"name\": \"Holdings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"item\",\n" +
                "\t\t\t\"name\": \"Item\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\t\"name\": \"EHoldings\"\n" +
                "\t\t}],\n" +
                "\t\t\"destination\": \"holdings\",\n" +
                "\t\t\"fields\": [{\n" +
                "\t\t\t\"id\": \"url\",\n" +
                "\t\t\t\"name\": \"URL\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callnumber\",\n" +
                "\t\t\t\"name\": \"Call Number\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"callnumbertype\",\n" +
                "\t\t\t\"name\": \"Call Number Type\"\n" +
                "\t\t}],\n" +
                "\t\t\"field\": \"callnumber\",\n" +
                "\t\t\"showRemove\": true\n" +
                "\t}],\n" +
                "\t\"batchProfileDataTransformerList\": [{\n" +
                "\t\t\"title\": \"Data Transformations\",\n" +
                "\t\t\"dataTransformationDocTypes\": [{\n" +
                "\t\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\t\"name\": \"Bibliographic\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdings\",\n" +
                "\t\t\t\"name\": \"Holdings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"item\",\n" +
                "\t\t\t\"name\": \"Item\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\t\"name\": \"EHoldings\"\n" +
                "\t\t}],\n" +
                "\t\t\"dataTransformationDocType\": \"bibliographic\",\n" +
                "\t\t\"transformers\": [{\n" +
                "\t\t\t\"id\": \"regex\",\n" +
                "\t\t\t\"name\": \"Regex Pattern Transformer\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"\",\n" +
                "\t\t\t\"name\": \"\"\n" +
                "\t\t}],\n" +
                "\t\t\"transformer\": \"regex\",\n" +
                "\t\t\"collapsed\": true,\n" +
                "\t\t\"dataField\": null,\n" +
                "\t\t\"expression\": null,\n" +
                "\t\t\"ind1\": null,\n" +
                "\t\t\"ind2\": null,\n" +
                "\t\t\"subField\": null\n" +
                "\t}, {\n" +
                "\t\t\"dataTransformationDocTypes\": [{\n" +
                "\t\t\t\"id\": \"bibliographic\",\n" +
                "\t\t\t\"name\": \"Bibliographic\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"holdings\",\n" +
                "\t\t\t\"name\": \"Holdings\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"item\",\n" +
                "\t\t\t\"name\": \"Item\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"eHoldings\",\n" +
                "\t\t\t\"name\": \"EHoldings\"\n" +
                "\t\t}],\n" +
                "\t\t\"dataTransformationDocType\": \"bibliographic\",\n" +
                "\t\t\"dataField\": \"001\",\n" +
                "\t\t\"transformers\": [{\n" +
                "\t\t\t\"id\": \"regex\",\n" +
                "\t\t\t\"name\": \"Regex Pattern Transformer\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"\",\n" +
                "\t\t\t\"name\": \"\"\n" +
                "\t\t}],\n" +
                "\t\t\"transformer\": \"regex\",\n" +
                "\t\t\"expression\": \"ocn\",\n" +
                "\t\t\"showRemove\": true\n" +
                "\t}],\n" +
                "\t\"description\": \"Profile Description\",\n" +
                "\t\"profileName\": \"Profile Name\"\n" +
                "}";

        System.out.println(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchProcessProfile batchProcessProfile = objectMapper.readValue(jsonString, BatchProcessProfile.class);
        assertNotNull(batchProcessProfile);
        System.out.println(batchProcessProfile);
    }

    @Test
    public void tesPrepareAllCallNumberTypes() throws IOException {
        List<OleShelvingScheme> oleShelvingSchemes = new ArrayList<>();

        OleShelvingScheme oleShelvingScheme = new OleShelvingScheme();
        oleShelvingScheme.setShelvingSchemeCode("code");
        oleShelvingScheme.setShelvingSchemeName("name");
        oleShelvingSchemes.add(oleShelvingScheme);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getSerializationConfig().addMixInAnnotations(OleShelvingScheme.class, PersistableBusinessObjectBase.class);
        objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        String jsonString = objectMapper.defaultPrettyPrintingWriter().writeValueAsString(oleShelvingSchemes);
        assertNotNull(jsonString);
        System.out.println(jsonString);
    }
}