package org.kuali.ole.ingest;

import org.junit.Test;
import org.kuali.ole.OLETranscationalRecordGenerator;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.converter.OLEEDIConverter;
import org.kuali.ole.ingest.pojo.ProfileAttributeBo;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.pojo.edi.EDIOrders;
import org.kuali.ole.pojo.edi.LineItemOrder;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/18/12
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class OleTxRecordBuilder_IT extends SpringBaseTestCase {
    @Test
    @Transactional
    public void testBuild() throws Exception {
        URL ediResource = getClass().getResource("iu.edi");
        File ediFile = new File(ediResource.toURI());
        String ediXML = new OLEEDIConverter().convertToXML(new FileUtil().readFile(ediFile));

        EDIOrders ediOrders = new OLETranscationalRecordGenerator().fromXml(ediXML);
        LineItemOrder lineItemOrder = ediOrders.getOrders().get(0).getLineItemOrder().get(0);

        ProfileAttributeBo profileAttributeBo = new ProfileAttributeBo();
        profileAttributeBo.setAgendaName("MOCK_AGENDA");
        profileAttributeBo.setAttributeName("chartCode");
        profileAttributeBo.setAttributeValue("BL");
        profileAttributeBo.setAttributeName("itemChartCode");
        profileAttributeBo.setAttributeValue("BL");
        OleTxRecord oleTxRecord =
                OleTxRecordBuilder.getInstance().build(lineItemOrder, ediOrders.getOrders().get(0));

        Map<String, String> accountInfo = OleTxRecordBuilder.getInstance().getAccountInfo(lineItemOrder);
        assertNotNull(oleTxRecord);
        String accountNumber=null;
        String objectCode=null;
        for(String accNo:accountInfo.keySet()){
            accountNumber=accNo;
            objectCode=accountInfo.get(accNo);
            break;
        }
        assertNotNull(accountNumber);
        assertNotNull(objectCode);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Object Code: " + objectCode);


    }
}
