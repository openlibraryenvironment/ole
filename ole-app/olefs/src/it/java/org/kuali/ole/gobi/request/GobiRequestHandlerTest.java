package org.kuali.ole.gobi.request;

import org.junit.Test;
import org.kuali.ole.gobi.GobiRequest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class GobiRequestHandlerTest {

    @Test
    public void testUnmarshal() throws Exception {
        String requestContent = "<PurchaseOrder>\n" +
                "  <CustomerDetail>\n" +
                "    <BaseAccount>5095ABC</BaseAccount>\n" +
                "    <SubAccount>509516DEF</SubAccount>\n" +
                "  </CustomerDetail>\n" +
                "  <Order>\n" +
                "    <UnlistedPrintMonograph>\n" +
                "      <collection>\n" +
                "        <record>\n" +
                "          <leader>00425nYm a2200145z  4500</leader>\n" +
                "          <controlfield tag=\"001\"></controlfield>\n" +
                "          <controlfield tag=\"003\">NhCcYBP</controlfield>\n" +
                "          <controlfield tag=\"005\">20150813163651.6</controlfield>\n" +
                "          <controlfield tag=\"008\">150813q2012    xx ||||||||||||||   eng d</controlfield>\n" +
                "          <datafield tag=\"020\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">9789042927001</subfield>\n" +
                "          </datafield>\n" +
                "          <datafield tag=\"035\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">(OCoLC)810414855</subfield>\n" +
                "          </datafield>\n" +
                "          <datafield tag=\"040\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">NhCcYBP</subfield>\n" +
                "            <subfield code=\"c\">NhCcYBP</subfield>\n" +
                "          </datafield>\n" +
                "          <datafield tag=\"100\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">MEER, L. BOUKE VAN DER</subfield>\n" +
                "          </datafield>\n" +
                "          <datafield tag=\"245\" ind1=\"0\" ind2=\"0\">\n" +
                "            <subfield code=\"a\">Ostia speaks :</subfield>\n" +
                "            <subfield code=\"b\">inscriptions, buildings and spaces in Rome's main port</subfield>\n" +
                "          </datafield>\n" +
                "          <datafield tag=\"260\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">Leuven, Belgium :</subfield>\n" +
                "            <subfield code=\"b\">PEETERS PUBLISHERS,</subfield>\n" +
                "            <subfield code=\"c\">2012.</subfield>\n" +
                "          </datafield>\n" +
                "        </record>\n" +
                "      </collection>\n" +
                "      <OrderDetail>\n" +
                "        <BatchPONumber />\n" +
                "        <ItemPONumber>99950118456</ItemPONumber>\n" +
                "        <FundCode>ori</FundCode>\n" +
                "        <MappedFundCode />\n" +
                "        <OrderNotes />\n" +
                "        <OtherLocalId />\n" +
                "        <Location>RECA</Location>\n" +
                "        <Quantity>1</Quantity>\n" +
                "        <YBPOrderKey>99950118456</YBPOrderKey>\n" +
                "        <OrderPlaced>2012-09-20T11:42:06</OrderPlaced>\n" +
                "        <ListPrice>\n" +
                "          <Amount>35.45</Amount>\n" +
                "          <Currency>USD</Currency>\n" +
                "        </ListPrice>\n" +
                "      </OrderDetail>\n" +
                "    </UnlistedPrintMonograph>\n" +
                "  </Order>\n" +
                "</PurchaseOrder>";
        GobiRequest gobiRequest = new GobiRequestHandler().unmarshal(requestContent);
        assertNotNull(gobiRequest);
        assertFalse(gobiRequest.getPurchaseOrder().getCustomerDetail().getBaseAccount().equalsIgnoreCase("0"));
        assertFalse(gobiRequest.getPurchaseOrder().getCustomerDetail().getSubAccount().equalsIgnoreCase("0"));
        System.out.println("Base Account : " + gobiRequest.getPurchaseOrder().getCustomerDetail().getBaseAccount());
        System.out.println("Sub Account : " + gobiRequest.getPurchaseOrder().getCustomerDetail().getSubAccount());
    }
}