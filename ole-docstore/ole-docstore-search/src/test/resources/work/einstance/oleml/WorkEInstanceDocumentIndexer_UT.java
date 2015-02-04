package work.einstance.oleml;

import org.junit.Test;
import org.kuali.ole.docstore.indexer.solr.DocumentIndexerManagerFactory;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.*;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.work.oleml.WorkEHoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.model.xstream.work.oleml.WorkEInstanceOlemlRecordProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 7/18/13
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkEInstanceDocumentIndexer_UT {

    public void testIndex() {
        Request request = new Request();
        request.setOperation("ingest");
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setId("1");
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.EINSTANCE.getDescription());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.getContent().setContent("<ole:instanceCollection xmlns:ole=\"http://ole.kuali.org/standards/ole-eInstance\">\n" +
                "  <!--1 or more repetitions:-->\n" +
                "  <ole:eInstance>\n" +
                "    <ole:instanceIdentifier>wen-1</ole:instanceIdentifier>\n" +
                "    <!--Zero or more repetitions:-->\n" +
                "    <ole:resourceIdentifier>string</ole:resourceIdentifier>\n" +
                "    <!--Zero or more repetitions:-->\n" +
                "    <ole:formerResourceIdentifier>\n" +
                "      <ole:identifier source=\"string\">\n" +
                "        <ole:identifierValue>string</ole:identifierValue>\n" +
                "      </ole:identifier>\n" +
                "      <ole:identifierType>string</ole:identifierType>\n" +
                "    </ole:formerResourceIdentifier>\n" +
                "    <ole:eHoldings primary=\"string\">\n" +
                "      <ole:holdingsIdentifier>weh-1</ole:holdingsIdentifier>\n" +
                "      <ole:relatedInstanceIdentifier>string</ole:relatedInstanceIdentifier>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:publisher>string</ole:publisher>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:imprint>string</ole:imprint>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:localPersistentLink>http://www.company.org/cum/sonoras</ole:localPersistentLink>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:link>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:text>string</ole:text>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:url>http://www.any.org/ventos/verrantque</ole:url>\n" +
                "      </ole:link>\n" +
                "      <ole:interLibraryLoanAllowed>false</ole:interLibraryLoanAllowed>\n" +
                "      <ole:staffOnlyFlag>false</ole:staffOnlyFlag>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:extentOfOwnership>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:coverages>\n" +
                "          <!--Zero or more repetitions:-->\n" +
                "          <ole:coverage>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:coverageStartDate>string</ole:coverageStartDate>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:coverageStartVolume>string</ole:coverageStartVolume>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:coverageStartIssue>string</ole:coverageStartIssue>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:coverageEndDate>string</ole:coverageEndDate>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:coverageEndVolume>string</ole:coverageEndVolume>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:coverageEndIssue>string</ole:coverageEndIssue>\n" +
                "          </ole:coverage>\n" +
                "        </ole:coverages>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:perpetualAccesses>\n" +
                "          <!--Zero or more repetitions:-->\n" +
                "          <ole:perpetualAccess>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:perpetualAccessStartDate>string</ole:perpetualAccessStartDate>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:perpetualAccessStartVolume>3</ole:perpetualAccessStartVolume>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:perpetualAccessStartIssue>3</ole:perpetualAccessStartIssue>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:perpetualAccessEndDate>string</ole:perpetualAccessEndDate>\n" +
                "            <!--Optional:-->\n" +
                "            <ole:perpetualAccessEndVolume>3</ole:perpetualAccessEndVolume>\n" +
                "            <ole:perpetualAccessEndIssue>string</ole:perpetualAccessEndIssue>\n" +
                "          </ole:perpetualAccess>\n" +
                "        </ole:perpetualAccesses>\n" +
                "      </ole:extentOfOwnership>\n" +
                "      <!--Zero or more repetitions:-->\n" +
                "      <ole:note type=\"string\">string</ole:note>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:donorPublicDisplay>string</ole:donorPublicDisplay>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:donorNote>string</ole:donorNote>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:accessStatus>string</ole:accessStatus>\n" +
                "      <ole:statusDate>string</ole:statusDate>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:vendor>string</ole:vendor>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:orderType>string</ole:orderType>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:orderFormat>string</ole:orderFormat>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:purchaseOrderId>string</ole:purchaseOrderId>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:statisticalSearchingCode>string</ole:statisticalSearchingCode>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:eResourceTitle>string</ole:eResourceTitle>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:eResourceId>string</ole:eResourceId>\n" +
                "      <ole:callNumber>\n" +
                "        <ole:prefix>string</ole:prefix>\n" +
                "        <ole:number>string</ole:number>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:callNumberType>\n" +
                "          <!--Optional:-->\n" +
                "          <ole:codeValue>string</ole:codeValue>\n" +
                "          <!--Optional:-->\n" +
                "          <ole:fullValue>string</ole:fullValue>\n" +
                "          <ole:typeOrSource>\n" +
                "            <ole:pointer>http://www.test.com/metuens/montis</ole:pointer>\n" +
                "            <ole:text>string</ole:text>\n" +
                "          </ole:typeOrSource>\n" +
                "        </ole:callNumberType>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:shelvingOrder>\n" +
                "          <ole:codeValue>string</ole:codeValue>\n" +
                "          <ole:fullValue>string</ole:fullValue>\n" +
                "          <ole:typeOrSource>\n" +
                "            <ole:pointer>http://www.test.com/metuens/montis</ole:pointer>\n" +
                "            <ole:text>string</ole:text>\n" +
                "          </ole:typeOrSource>\n" +
                "        </ole:shelvingOrder>\n" +
                "      </ole:callNumber>\n" +
                "      <ole:invoice>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:fundCode>string</ole:fundCode>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:currentFYCost>string</ole:currentFYCost>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:paymentStatus>string</ole:paymentStatus>\n" +
                "      </ole:invoice>\n" +
                "      <ole:accessInformation>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:numberOfSimultaneousUser>string</ole:numberOfSimultaneousUser>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:proxiedResource>string</ole:proxiedResource>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:accessLocation>string</ole:accessLocation>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:authenticationType>string</ole:authenticationType>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:accessUsername>string</ole:accessUsername>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:accessPassword>string</ole:accessPassword>\n" +
                "      </ole:accessInformation>\n" +
                "      <ole:platform>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:adminUserName>string</ole:adminUserName>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:adminPassword>string</ole:adminPassword>\n" +
                "        <!--Optional:-->\n" +
                "        <ole:adminUrl>http://www.test.com/fremunt/foedere</ole:adminUrl>\n" +
                "        <ole:platformName>string</ole:platformName>\n" +
                "      </ole:platform>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:subscriptionStatus>string</ole:subscriptionStatus>\n" +
                "      <!--Optional:-->\n" +
                "      <ole:location primary=\"string\" status=\"string\">\n" +
                "        <ole:locationLevel>\n" +
                "          <ole:name>string</ole:name>\n" +
                "          <ole:level>string</ole:level>\n" +
                "          <!--Optional:-->\n" +
                "          <ole:locationLevel/>\n" +
                "        </ole:locationLevel>\n" +
                "      </ole:location>\n" +
                "    </ole:eHoldings>\n" +
                "  </ole:eInstance>\n" +
                "</ole:instanceCollection>");

        InstanceCollection instanceCollection = new InstanceCollection();
        EInstance eInstance = new EInstance();
        eInstance.setInstanceIdentifier("wen-1");
        EHoldings eHoldings = new EHoldings();
        eHoldings.setHoldingsIdentifier("weh-1");
        eHoldings.setAccessStatus("access status");
        eHoldings.setImprint("imprint");
        eHoldings.setSubscriptionStatus("subscribtion status");

        Coverage coverage = new Coverage();
        coverage.setCoverageEndDate("end date");
        coverage.setCoverageEndIssue("issue");

        ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();
        Coverages coverages1 = new Coverages();
        coverages1.getCoverage().add(coverage);

        extentOfOwnership.setCoverages(coverages1);

        eHoldings.setExtentOfOwnership(extentOfOwnership);

        eInstance.setEHoldings(eHoldings);

        instanceCollection.getEInstance().add(eInstance);

        List<RequestDocument>  requestDocuments = new ArrayList<>();
        requestDocuments.add(requestDocument);
        request.setRequestDocuments(requestDocuments);

        System.out.println(" ****************************\n \n " + new RequestHandler().toXML(request));

        IndexerService indexerService = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(requestDocument.getCategory(),requestDocument.getType(),requestDocument.getFormat());
        indexerService.indexDocument(requestDocument,true);

    }



}
