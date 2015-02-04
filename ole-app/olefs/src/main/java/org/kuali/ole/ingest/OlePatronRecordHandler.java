package org.kuali.ole.ingest;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.ingest.pojo.*;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronDocuments;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * OlePatronRecordHandler builds the Patron based on fileContent and also convert the List of olePatron into Patron xml
 */

public class OlePatronRecordHandler {

    private static final Logger LOG = Logger.getLogger(OlePatronRecordHandler.class);

    /**
     *  This method returns Patron.
     *  This method build the Patron based on fileContent.
     * @param fileContent
     * @return  OlePatronGroup
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    public OlePatronGroup buildPatronFromFileContent(String fileContent) throws URISyntaxException, IOException {
        XStream xStream = new XStream();
        xStream.alias("patronGroup", OlePatronGroup.class);
        xStream.alias("patron", OlePatron.class);
        xStream.alias("name", OleNameTypes.class);
        xStream.alias("postalAddress", OlePatronPostalAddress.class);
        xStream.alias("emailAddress", OlePatronEmailAddress.class);
        xStream.alias("telephoneNumber", OlePatronTelePhoneNumber.class);
        xStream.alias("patronLevelPolicies", OlePatronLevelPolicies.class);
        xStream.alias("addressLine", OleAddressLine.class);
        xStream.alias("note", OlePatronNote.class);
        xStream.alias("affiliation", OlePatronAffiliations.class);
        xStream.alias("employment", OlePatronEmployments.class);
        xStream.aliasField("default", OlePatronPostalAddress.class, "defaults");
        xStream.aliasField("default", OlePatronEmailAddress.class,"defaults");
        xStream.aliasField("default", OlePatronTelePhoneNumber.class,"defaults");
        xStream.addImplicitCollection(OlePatronGroup.class, "patronGroup");
        xStream.addImplicitCollection(OlePatronPostalAddress.class, "addressLinesList",OleAddressLine.class);
        //xStream.addImplicitCollection(OlePatronAffiliations.class, "employments",OlePatronEmployments.class);
        xStream.registerConverter(new OlePatronAddressLineConverter());
        Object object = xStream.fromXML(fileContent);
        return (OlePatronGroup) object;
    }

    /**
     *  This method returns Patron xml.
     *  This method convert the List of olePatron into Patron xml.
     * @param olePatrons
     * @return  stringBuffer
     */
    public String toXML(List<OlePatron> olePatrons) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><patronGroup xmlns=\"http://ole.kuali.org/standards/ole-patron\">\n" );
        stringBuffer.append("\n");
        for(OlePatron olePatron : olePatrons){
            XStream xStream = new XStream();
            xStream.alias("patron", OlePatron.class);
            xStream.alias("name", OleNameTypes.class);
            xStream.alias("postalAddress", OlePatronPostalAddress.class);
            xStream.alias("emailAddress", OlePatronEmailAddress.class);
            xStream.alias("telephoneNumber", OlePatronTelePhoneNumber.class);
            xStream.alias("patronLevelPolicies", OlePatronLevelPolicies.class);
            xStream.alias("addressLine", OleAddressLine.class);
            xStream.alias("note", OlePatronNote.class);
            xStream.alias("affiliation", OlePatronAffiliations.class);
            xStream.alias("employment", OlePatronEmployments.class);
            xStream.aliasField("default", OlePatronPostalAddress.class,"defaults");
            xStream.aliasField("default", OlePatronEmailAddress.class,"defaults");
            xStream.aliasField("default", OlePatronTelePhoneNumber.class,"defaults");
            xStream.addImplicitCollection(OlePatronGroup.class, "patronGroup");
            xStream.addImplicitCollection(OlePatronPostalAddress.class, "addressLinesList",OleAddressLine.class);
            xStream.registerConverter(new OlePatronAddressLineConverter());
            String xml = xStream.toXML(olePatron);
            xml = xml.replaceAll("<patron>","<patron xmlns=\"http://ole.kuali.org/standards/ole-patron\">");
            stringBuffer.append(xml);
            stringBuffer.append("\n");
        }
        stringBuffer.append("\n");
        stringBuffer.append("</patronGroup>");

        return stringBuffer.toString();
    }

    public String generatePatronXML(OlePatronDocuments olePatronDocuments) {
        StringBuffer stringBuffer = new StringBuffer();
        XStream xStream = new XStream();
        xStream.alias("olePatronDocuments", OlePatronDocuments.class);
        xStream.alias("olePatronDocument", OlePatronDocument.class);
        xStream.alias("olePatronId", String.class);
        xStream.alias("barcode",String.class);
        xStream.alias("borrowerType", String.class);
        xStream.alias("firstName", String.class);
        xStream.alias("lastName", String.class);
       // xStream.alias("emailAddress", String.class);
        xStream.alias("activeIndicator", Boolean.class);
        xStream.omitField(OlePatronDocument.class, "emailAddress");
        xStream.omitField(OlePatronDocument.class, "expirationFlag");
        xStream.omitField(OlePatronDocument.class, "generalBlock");
        xStream.omitField(OlePatronDocument.class, "generalBlockNotes");
        xStream.omitField(OlePatronDocument.class, "pagingPrivilege");
        xStream.omitField(OlePatronDocument.class, "courtesyNotice");
        xStream.omitField(OlePatronDocument.class, "deliveryPrivilege");
        xStream.omitField(OlePatronDocument.class, "realPatronCheck");
        xStream.omitField(OlePatronDocument.class,"selfCheckOut");
        xStream.omitField(OlePatronDocument.class, "expirationDate");
        xStream.omitField(OlePatronDocument.class, "activationDate");
        xStream.omitField(OlePatronDocument.class, "middleName");
        xStream.omitField(OlePatronDocument.class, "phoneNumber");
        xStream.omitField(OlePatronDocument.class, "borrowerTypeName");
        xStream.omitField(OlePatronDocument.class, "processMessage");
        xStream.omitField(OlePatronDocument.class, "source");
        xStream.omitField(OlePatronDocument.class, "statisticalCategory");
        xStream.omitField(OlePatronDocument.class, "oleSourceName");
        xStream.omitField(OlePatronDocument.class, "oleStatisticalCategoryName");
        xStream.omitField(OlePatronDocument.class, "patronBillFlag");
        xStream.omitField(OlePatronDocument.class, "proxyPatronId");
        xStream.omitField(OlePatronDocument.class, "patronPhotograph");
        xStream.omitField(OlePatronDocument.class, "loanFlag");
        xStream.omitField(OlePatronDocument.class, "tempCircHistoryFlag");
        xStream.omitField(OlePatronDocument.class, "requestFlag");
        xStream.omitField(OlePatronDocument.class, "upload");
        xStream.omitField(OlePatronDocument.class, "oleLoanDocuments");
        xStream.omitField(OlePatronDocument.class, "phones");
        xStream.omitField(OlePatronDocument.class, "addresses");
        xStream.omitField(OlePatronDocument.class, "oleEntityAddressBo");
        xStream.omitField(OlePatronDocument.class, "name");
        xStream.omitField(OlePatronDocument.class, "oleAddresses");
        xStream.omitField(OlePatronDocument.class, "emails");
        xStream.omitField(OlePatronDocument.class, "notes");
        xStream.omitField(OlePatronDocument.class, "lostBarcodes");
        xStream.omitField(OlePatronDocument.class, "oleBorrowerType");
        xStream.omitField(OlePatronDocument.class, "entity");
        xStream.omitField(OlePatronDocument.class, "sourceBo");
        xStream.omitField(OlePatronDocument.class, "statisticalCategoryBo");
        xStream.omitField(OlePatronDocument.class, "patronAffiliations");
        xStream.omitField(OlePatronDocument.class, "employments");
        xStream.omitField(OlePatronDocument.class, "oleDeliverRequestBos");
        xStream.omitField(OlePatronDocument.class, "oleProxyPatronDocuments");
        xStream.omitField(OlePatronDocument.class, "oleTemporaryCirculationHistoryRecords");
        xStream.omitField(OlePatronDocument.class, "olePatronLocalIds");
        xStream.omitField(OlePatronDocument.class, "oleProxyPatronDocumentList");
        xStream.omitField(OlePatronDocument.class, "patronHomePage");
        xStream.omitField(OlePatronDocument.class, "pointing");
        xStream.omitField(OlePatronDocument.class, "startingIndexExecuted");
        xStream.omitField(OlePatronDocument.class, "activateBarcode");
        xStream.omitField(OlePatronDocument.class, "deactivateBarcode");
        xStream.omitField(OlePatronDocument.class, "lostStatus");
        xStream.omitField(OlePatronDocument.class, "lostDescription");
        xStream.omitField(OlePatronDocument.class, "invalidateBarcode");
        xStream.omitField(OlePatronDocument.class, "reinstateBarcode");
        xStream.omitField(OlePatronDocument.class, "skipBarcodeValidation");
        xStream.omitField(OlePatronDocument.class, "identityService");
        xStream.omitField(OlePatronDocument.class, "barcodeChanged");
        xStream.omitField(OlePatronDocument.class, "barcodeEditable");
        xStream.omitField(OlePatronDocument.class, "popupDialog");
        xStream.omitField(OlePatronDocument.class, "uiMessageType");
        xStream.omitField(OlePatronDocument.class, "patronMessage");
        xStream.omitField(OlePatronDocument.class, "reinstated");
        xStream.omitField(OlePatronDocument.class, "patronBillPayments");
        xStream.omitField(OlePatronDocument.class, "numberOfClaimsReturned");
        xStream.omitField(OlePatronDocument.class, "olePatronEntityViewBo");
        xStream.omitField(OlePatronDocument.class, "patronBillFileName");
        xStream.omitField(OlePatronDocument.class, "viewBillUrl");
        xStream.omitField(OlePatronDocument.class, "createBillUrl");
        xStream.omitField(OlePatronDocument.class, "namePrefix");
        xStream.omitField(OlePatronDocument.class, "nameSuffix");
        xStream.addImplicitCollection(OlePatronDocuments.class, "olePatronDocuments");
        xStream.omitField(OlePatronDocument.class, "deletedPhones");
        xStream.omitField(OlePatronDocument.class, "deletedOleEntityAddressBo");
        xStream.omitField(OlePatronDocument.class, "deletedEmails");
        xStream.omitField(OlePatronDocument.class, "deletedNotes");
        xStream.omitField(OlePatronDocument.class, "deletedPatronAffiliations");
        xStream.omitField(OlePatronDocument.class, "deletedEmployments");
        xStream.omitField(OlePatronDocument.class, "deletedOleProxyPatronDocuments");
        xStream.omitField(OlePatronDocument.class, "deletedOlePatronLocalIds");
        xStream.omitField(OlePatronDocument.class, "showLoanedRecords");
        xStream.omitField(OlePatronDocument.class, "showRequestedItems");
        xStream.omitField(OlePatronDocument.class, "showTemporaryCirculationHistoryRecords");
        String xml = xStream.toXML(olePatronDocuments);
        stringBuffer.append(xml);
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }
}