package org.kuali.ole.docstore.model.bo;

import org.junit.Test;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/21/13
 * Time: 12:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleDocument_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(OleDocument_UT.class);

    @Test
    public void testOleDocuments() throws Exception {
        List<OleDocument> oleDocumentList = new ArrayList<OleDocument>();
        OleDocument oleDocument = new OleDocument();
        oleDocument.setId("44555654ed");
        oleDocument.setDocCategory(DocCategory.WORK);
        oleDocument.setDocType(DocType.BIB);
        oleDocumentList.add(oleDocument);

        WorkBibDocument workBibDocument = new WorkBibDocument();
        List<String> formatList = new ArrayList<String>();
        formatList.add(DocFormat.MARC.getCode());
        formatList.add(DocFormat.DUBLIN_CORE.getCode());
        formatList.add(DocFormat.DUBLIN_UNQUALIFIED.getCode());
        formatList.add(DocFormat.OLEML.getCode());
        formatList.add(DocFormat.ONIXPL.getCode());
        formatList.add(DocFormat.PDF.getCode());
        formatList.add(DocFormat.DOC.getCode());
        workBibDocument.setFormats(formatList);
        oleDocumentList.add(workBibDocument);


        workBibDocument = new WorkBibDocument();
        WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
        LOG.info(workInstanceDocument.toString());
        List<WorkInstanceDocument> workInstanceDocumentList = new ArrayList<WorkInstanceDocument>();
        WorkHoldingsDocument workHoldingsDocument = new WorkHoldingsDocument();
        LOG.info(workHoldingsDocument.toString());
        WorkItemDocument workItemDocument = new WorkItemDocument();
        LOG.info(workItemDocument.toString());


        List<WorkItemDocument> itemDocumentList = new ArrayList<WorkItemDocument>();
        workHoldingsDocument.setHoldingsIdentifier("2ce77");
        workHoldingsDocument.setLocationName("string");
        workHoldingsDocument.setCallNumber("2232");

        workItemDocument.setItemIdentifier("7dd66");
        workItemDocument.setCallNumber("555664");
        workItemDocument.setLocation("string");
        workItemDocument.setBarcode("445de");
        itemDocumentList.add(workItemDocument);
        workItemDocument = new WorkItemDocument();
        workItemDocument.setItemIdentifier("5548");
        workItemDocument.setCallNumber("44733");
        workInstanceDocument.setInstanceIdentifier("855ud");
        itemDocumentList.add(workItemDocument);
        workInstanceDocument.setHoldingsDocument(workHoldingsDocument);
        workInstanceDocument.setItemDocumentList(itemDocumentList);
        workBibDocument.setInstanceDocument(workInstanceDocument);
        workBibDocument.setAuthor("Leo");
        workBibDocument.setAuthor("Martin");
        List<String> titleList = new ArrayList<String>();
        String title1 = "Theory in practice";
        String title2 = "Seven Essays";
        titleList.add(title1);
        titleList.add(title2);
        List<String> authorList = new ArrayList<String>();
        String author1 = "Edmondo";
        String author2 = "Carol";
        authorList.add(author1);
        authorList.add(author2);
        workBibDocument.setTitles(titleList);
        workBibDocument.setAuthors(authorList);
        List<String> subjectList = new ArrayList<String>();
        String subject1 = "subject1";
        String subject2 = "subject2";
        subjectList.add(subject1);
        subjectList.add(subject2);
        workBibDocument.setSubjects(subjectList);
        List<String> issnList = new ArrayList<String>();
        String issn1 = "10002";
        String issn2 = "10003";
        issnList.add(issn1);
        issnList.add(issn2);
        workBibDocument.setIssns(issnList);
        List<String> isbnList = new ArrayList<String>();
        isbnList.add("221001");
        isbnList.add("221002");
        workBibDocument.setIsbns(isbnList);
        List<String> publicationPlacesList = new ArrayList<String>();
        publicationPlacesList.add("A");
        publicationPlacesList.add("X");
        workBibDocument.setPublicationPlaces(publicationPlacesList);
        List<String> publisherList = new ArrayList<String>();
        publisherList.add("X");
        publisherList.add("Y");
        workBibDocument.setPublishers(publisherList);
        List<String> descriptionsList = new ArrayList<String>();
        descriptionsList.add("X");
        descriptionsList.add("Y");
        workBibDocument.setDescriptions(descriptionsList);
        List<String> publicationDateList = new ArrayList<String>();
        publicationDateList.add("2003");
        publicationDateList.add("1997");
        workBibDocument.setPublicationDates(publicationDateList);
        List<String> editionsList = new ArrayList<String>();
        editionsList.add("1");
        editionsList.add("2");
        workBibDocument.setEditions(editionsList);
        List<String> formGenresList = new ArrayList<String>();
        formGenresList.add("1");
        formGenresList.add("2");
        workBibDocument.setFormGenres(formGenresList);
        List<String> languagesList = new ArrayList<String>();
        languagesList.add("French");
        languagesList.add("German");
        workBibDocument.setLanguages(languagesList);
        formatList = new ArrayList<String>();
        formatList.add(DocFormat.MARC.getCode());
        formatList.add(DocFormat.DUBLIN_CORE.getCode());
        workBibDocument.setFormats(formatList);
        oleDocumentList.add(workBibDocument);
        workBibDocument.setTitle("Language Teaching");
        workBibDocument.setAuthor("Ernest");
        workBibDocument.setPublicationDate("1994");
        workBibDocument.setIsbn("4457778");
        workBibDocument.setPublisher("Oxford University");
        oleDocumentList.add(workBibDocument);


        workInstanceDocument = new WorkInstanceDocument();
        workInstanceDocument.setInstanceIdentifier("77ede888");
        workInstanceDocumentList.add(workInstanceDocument);
        workBibDocument.setWorkInstanceDocumentList(workInstanceDocumentList);

        if (oleDocument.getDocCategory().isEqualTo("input")) {
            LOG.info("equal");
        }
        if (oleDocument.getDocCategory().isEqualTo(DocCategory.WORK.getCode())) {
            LOG.info("Category:" + DocCategory.WORK.getDescription());
        }

        if (DocCategory.AUTHORITY.isEqualTo("authority")) {
            LOG.info(DocCategory.AUTHORITY.getDescription());
        }

        if (oleDocument.getDocCategory().isEqualTo(null)) {
            LOG.info("Category:" + DocCategory.WORK.getDescription());
        }
        if (oleDocument.getDocType().isEqualTo("input")) {
            LOG.info("equal");
        }
        if (oleDocument.getDocType().isEqualTo(DocType.BIB.getCode())) {
            LOG.info("Type:" + DocType.BIB.getDescription());
        }
        if (oleDocument.getDocType().isEqualTo("bibliographic")) {
            LOG.info("Type :" + oleDocument.getDocType().getDescription());
        }
        if (oleDocument.getDocType().isEqualTo(null)) {
            LOG.info("null value");
        }
        if (oleDocument.getDocType().isEqualTo("rrrr")) {
            LOG.info("invalid value");
        }
        OleDocument oleDocument1 = oleDocumentList.get(2);
        LOG.info("Id:" + oleDocument1.getId());

        LOG.info("Category Code:" + oleDocument1.getDocCategory().getCode());
        LOG.info("Category Description:" + oleDocument1.getDocCategory().getDescription());
        LOG.info("Type Code:" + oleDocument1.getDocType().getCode());
        LOG.info("Type Description:" + oleDocument1.getDocType().getDescription());
        WorkBibDocument workBibDocument1 = (WorkBibDocument) oleDocument1;
        WorkInstanceDocument workInstanceDocument1 = workBibDocument1.getInstanceDocument();
        LOG.info("instance id:" + workInstanceDocument1.getInstanceIdentifier());
        LOG.info("category:" + workInstanceDocument1.getDocCategory());

        LOG.info("type:" + workInstanceDocument1.getDocType());
        WorkHoldingsDocument workHoldingsDocument1 = workInstanceDocument1.getHoldingsDocument();
        LOG.info("Holdings identifier:" + workHoldingsDocument1.getHoldingsIdentifier());
        LOG.info("Location:" + workHoldingsDocument1.getLocationName());
        LOG.info("Call Number:" + workHoldingsDocument1.getCallNumber());
        for (WorkItemDocument workItemDocument1 : workInstanceDocument1.getItemDocumentList()) {
            LOG.info("Item Identifier:" + workItemDocument1.getItemIdentifier());
            LOG.info("Call Number:" + workItemDocument1.getCallNumber());
            LOG.info("Location:" + workItemDocument1.getLocation());
            LOG.info("Barcode:" + workItemDocument1.getBarcode());
        }

        LOG.info("Authors:");
        for (String author : workBibDocument1.getAuthors()) {
            LOG.info(author);
        }
        LOG.info("Titles:");
        for (String title : workBibDocument1.getTitles()) {
            LOG.info(title);
        }
        LOG.info("Subjects:");
        for (String subject : workBibDocument1.getSubjects()) {
            LOG.info(subject);
        }
        LOG.info("ISSNs:");
        for (String issn : workBibDocument1.getIssns()) {
            LOG.info(issn);
        }

        LOG.info("ISBNs:");
        for (String isbn : workBibDocument1.getIsbns()) {
            LOG.info(isbn);
        }
        LOG.info("Publication Places:");
        for (String publicationPlace : workBibDocument1.getPublicationPlaces()) {
            LOG.info(publicationPlace);
        }
        LOG.info("Publishers:");
        for (String publisher : workBibDocument1.getPublishers()) {
            LOG.info(publisher);
        }
        LOG.info("Descriptions:");
        for (String description : workBibDocument1.getDescriptions()) {
            LOG.info(description);
        }
        LOG.info("Publication Dates:");
        for (String publicationDate : workBibDocument1.getPublicationDates()) {
            LOG.info(publicationDate);
        }
        LOG.info("Editions:");
        for (String edition : workBibDocument1.getEditions()) {
            LOG.info(edition);
        }
        LOG.info("FormGenres:");
        for (String formGenre : workBibDocument1.getFormGenres()) {
            LOG.info(formGenre);
        }
        LOG.info("Languages:");
        for (String language : workBibDocument1.getLanguages()) {
            LOG.info(language);
        }
        LOG.info("Formats:");
        for (String format : workBibDocument1.getFormats()) {
            LOG.info(format);
        }

        LOG.info("WorkBibDocument2 Details:");
        WorkBibDocument workBibDocument2 = (WorkBibDocument) oleDocumentList.get(1);
        LOG.info("Title:" + workBibDocument2.getTitle());
        LOG.info("Author:" + workBibDocument2.getAuthor());
        LOG.info("Publication Date:" + workBibDocument2.getPublicationDate());
        LOG.info("Isbn:" + workBibDocument2.getIsbn());
        LOG.info("Publisher:" + workBibDocument2.getPublisher());


        LOG.info("workInstanceDocumentList");
        workInstanceDocument = workBibDocument.getWorkInstanceDocumentList().get(0);
        LOG.info("Instance Identifier:" + workInstanceDocument.getInstanceIdentifier());

        WorkBibDocument workBibDocument3 = (WorkBibDocument) oleDocumentList.get(1);
        List<String> formats = workBibDocument3.getFormats();
        if (DocFormat.MARC.isEqualTo(formats.get(0).toString())) {
            LOG.info("Code:" + DocFormat.MARC.getCode());
            LOG.info("Description:" + DocFormat.MARC.getDescription());
        }
        if (DocFormat.DUBLIN_CORE.isEqualTo("dublin_core")) {
            LOG.info("Description:" + DocFormat.DUBLIN_CORE.getDescription());
        }
        if (DocFormat.DUBLIN_UNQUALIFIED.isEqualTo(null)) {
            LOG.info("Description:" + DocFormat.DUBLIN_UNQUALIFIED.getDescription());
        }
        //Added by Sundar Rajakani
        WorkBibDocument workBibDocument4 = new WorkBibDocument();
        workBibDocument4.setTitles(titleList);
        LOG.info(workBibDocument4.getTitle());
        workBibDocument4.setTitles(null);
        workBibDocument4.setTitle(null);

        workBibDocument4.setEditions(editionsList);
        LOG.info(workBibDocument4.getEdition());
        workBibDocument4.setEditions(null);
        LOG.info(workBibDocument4.getEdition());
        workBibDocument4.setEdition(null);

        workBibDocument4.setAuthors(authorList);
        LOG.info(workBibDocument4.getAuthor());
        workBibDocument4.setAuthors(null);
        workBibDocument4.setAuthor(null);

        workBibDocument4.setPublicationDates(publicationDateList);
        LOG.info(workBibDocument4.getPublicationDate());
        workBibDocument4.setPublicationDates(null);
        workBibDocument4.setPublicationDate(null);

        workBibDocument4.setIsbns(isbnList);
        LOG.info(workBibDocument4.getIsbn());
        workBibDocument4.setIsbns(null);
        workBibDocument4.setIsbn(null);

        workBibDocument4.setPublishers(publisherList);
        LOG.info(workBibDocument4.getPublisher());
        workBibDocument4.setPublishers(null);
        workBibDocument4.setPublisher(null);
        LOG.info(workBibDocument4.toString());

        WorkBibDocument workBibDocument5 = new WorkBibDocument();
        workBibDocument5.setEdition("edition");
    }

}
