package org.kuali.ole.ncip;

import org.junit.Test;
import org.kuali.ole.OLERestBaseTestCase;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.util.XMLFormatterUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.Assert.*;

/**
 * Created by chenchulakshmig on 8/5/15.
 */
public class OLENCIPService_IT extends OLERestBaseTestCase {

    private String URL = OLEFS_APPLICATION_URL + "/OLENCIPResponder";

    @Test
    public void testLookupUser() {
        String requestContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<NCIPMessage v:version=\"5.6\" xmlns:v=\"http://www.niso.org/2008/ncip\" xmlns=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso.org/schemas/ncip/v2_0/ncip_v2_0.xsd \">\n" +
                "  <LookupUser>\n" +
                "<InitiationHeader>\n" +
                "<FromAgencyId>\n" +
                "<AgencyId>LEHI</AgencyId>\n" +
                "</FromAgencyId>\n" +
                "<ToAgencyId>\n" +
                "<AgencyId>Lehigh University</AgencyId>\n" +
                "</ToAgencyId>\n" +
                "    </InitiationHeader> \n" +
                "     <UserId>\n" +
                "        <AgencyId>LEHI</AgencyId>\n" +
                "        <UserIdentifierValue>6010570002006861</UserIdentifierValue>\n" +
                "    </UserId>\n" +
                "    <UserElementType>Name Information</UserElementType>\n" +
                "    <UserElementType>User Address Information</UserElementType>\n" +
                "    <UserElementType>User Language</UserElementType>\n" +
                "    <UserElementType>User Privilege</UserElementType>\n" +
                "    <UserElementType>User Id</UserElementType>\n" +
                "<LoanedItemsDesired/>\n" +
                "<RequestedItemsDesired/>\n" +
                "<UserFiscalAccountDesired/>\n" +
                "  </LookupUser>\n" +
                "</NCIPMessage>";

        String responseContent = sendPostRequest(URL, requestContent);
        assertNotNull(responseContent);
        assertTrue(responseContent.trim().length() > 0);
        System.out.println("Response Content : " + XMLFormatterUtil.formatContentForPretty(responseContent));
        assertFalse(responseContent.contains("<ns1:Problem>"));
    }

    @Test
    public void testAcceptItem() throws Exception {
        String requestContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<NCIPMessage v:version=\"5.6\" xmlns:v=\"http://www.niso.org/2008/ncip\" xmlns=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso.org/schemas/ncip/v2_0/ncip_v2_0.xsd \">\n" +
                "<AcceptItem>\n" +
                "<InitiationHeader>\n" +
                "<FromAgencyId>\n" +
                "<AgencyId>LEHI</AgencyId>\n" +
                "</FromAgencyId>\n" +
                "<ToAgencyId>\n" +
                "<AgencyId>Lehigh University</AgencyId>\n" +
                "</ToAgencyId>\n" +
                "    </InitiationHeader> \n" +
                " <RequestId>\n" +
                "  <AgencyId>LEHI</AgencyId>\n" +
                "  <RequestIdentifierValue>TST-111</RequestIdentifierValue>\n" +
                " </RequestId>\n" +
                " <RequestedActionType>Hold For Pickup</RequestedActionType>\n" +
                " <UserId>\n" +
                "   <AgencyId>LEHI</AgencyId>\n" +
                "   <UserIdentifierValue>6010570002006861</UserIdentifierValue>\n" +
                " </UserId>\n" +
                " <ItemId>\n" +
                "  <AgencyId>LEHI</AgencyId>\n" +
                "  <ItemIdentifierValue>15</ItemIdentifierValue>\n" +
                " </ItemId>\n" +
                " <ItemOptionalFields>\n" +
                " <BibliographicDescription>\n" +
                " <Author>Author</Author>\n" +
                " <Title>Title</Title>\n" +
                " </BibliographicDescription>\n" +
                " <ItemDescription>\n" +
                " <CallNumber>Call Number</CallNumber>\n" +
                " </ItemDescription>\n" +
                " </ItemOptionalFields>\n" +
                " <PickupLocation>BL_EDUC</PickupLocation>\n" +
                "</AcceptItem>\n" +
                "</NCIPMessage>";

        String responseContent = sendPostRequest(URL, requestContent);
        assertNotNull(responseContent);
        assertTrue(responseContent.trim().length() > 0);
        System.out.println("Response Content : " + XMLFormatterUtil.formatContentForPretty(responseContent));
        assertFalse(responseContent.contains("<Problem>"));
    }

    @Test
    public void testCheckOutItem() throws Exception {
        String requestContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<NCIPMessage v:version=\"5.6\" xmlns:v=\"http://www.niso.org/2008/ncip\" xmlns=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso.org/schemas/ncip/v2_0/ncip_v2_0.xsd \">\n" +
                "<CheckOutItem>\n" +
                "<InitiationHeader>\n" +
                "<FromAgencyId>\n" +
                "<AgencyId>LEHI</AgencyId>\n" +
                "</FromAgencyId>\n" +
                "<ToAgencyId>\n" +
                "<AgencyId>Lehigh University</AgencyId>\n" +
                "</ToAgencyId>\n" +
                "    </InitiationHeader> \n" +
                "<UserId>\n" +
                "  <AgencyId>LEHI</AgencyId>\n" +
                "  <UserIdentifierValue>6010570002006861</UserIdentifierValue>\n" +
                "</UserId>\n" +
                "<ItemId>\n" +
                " <AgencyId>LEHI</AgencyId> \n" +
                " <ItemIdentifierValue>16</ItemIdentifierValue>\n" +
                "</ItemId>\n" +
                "</CheckOutItem>\n" +
                "</NCIPMessage>";

        String responseContent = sendPostRequest(URL, requestContent);
        assertNotNull(responseContent);
        assertTrue(responseContent.trim().length() > 0);
        System.out.println("Response Content : " + XMLFormatterUtil.formatContentForPretty(responseContent));
        assertFalse(responseContent.contains("<Problem>"));
    }

    @Test
    public void testCheckInItem() throws Exception {
        String requestContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<NCIPMessage v:version=\"5.6\" xmlns:v=\"http://www.niso.org/2008/ncip\" xmlns=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso.org/schemas/ncip/v2_0/ncip_v2_0.xsd \">\n" +
                "<CheckInItem>\n" +
                "<InitiationHeader>\n" +
                "<FromAgencyId>\n" +
                "<AgencyId>LEHI</AgencyId>\n" +
                "</FromAgencyId>\n" +
                "<ToAgencyId>\n" +
                "<AgencyId>Lehigh University</AgencyId>\n" +
                "</ToAgencyId>\n" +
                "    </InitiationHeader> \n" +
                "  <ItemId>\n" +
                "    <AgencyId>LEHI</AgencyId> \n" +
                "    <ItemIdentifierValue>18</ItemIdentifierValue>\n" +
                "  </ItemId>\n" +
                "<ItemElementType>Bibliographic Description</ItemElementType>\n" +
                "<ItemElementType>Item Description</ItemElementType>\n" +
                "</CheckInItem>\n" +
                "</NCIPMessage>";

        String responseContent = sendPostRequest(URL, requestContent);
        assertNotNull(responseContent);
        assertTrue(responseContent.trim().length() > 0);
        System.out.println("Response Content : " + XMLFormatterUtil.formatContentForPretty(responseContent));
        assertFalse(responseContent.contains("<Problem>"));
    }

}
