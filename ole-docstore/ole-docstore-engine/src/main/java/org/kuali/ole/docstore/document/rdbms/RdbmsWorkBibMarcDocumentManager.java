package org.kuali.ole.docstore.document.rdbms;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.BibliographicRecordHandler;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.RepositoryBrowser;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.rdbms.bo.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecords;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.WorkBibMarcRecordProcessor;
import org.kuali.ole.docstore.validation.DocStoreValidationError;
import org.kuali.ole.docstore.validation.WorkBibMarcRecordValidator;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.pojo.bib.Collection;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 7/16/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsWorkBibMarcDocumentManager extends RdbmsWorkBibDocumentManager {

    private static RdbmsWorkBibMarcDocumentManager ourInstance = null;

    public static RdbmsWorkBibMarcDocumentManager getInstance() {
        if (null == ourInstance) {
            ourInstance = new RdbmsWorkBibMarcDocumentManager();
        }
        return ourInstance;
    }


    protected void modifyDocumentContent(RequestDocument doc, String identifier, BusinessObjectService businessObjectService) {
        String content = doc.getContent().getContent();
        if (content != null && content != "" && content.length() > 0) {
            Pattern pattern = Pattern.compile("tag=\"001\">.*?</controlfield");
            Pattern pattern2 = Pattern.compile("<controlfield.*?tag=\"001\"/>");
            Matcher matcher = pattern.matcher(content);
            Matcher matcher2 = pattern2.matcher(content);
            if (matcher.find()) {
                doc.getContent().setContent(matcher.replaceAll("tag=\"001\">" + identifier + "</controlfield"));
            } else if (matcher2.find()) {
                doc.getContent()
                        .setContent(matcher2.replaceAll("<controlfield tag=\"001\">" + identifier + "</controlfield>"));
            } else {
                int ind = content.indexOf("</leader>") + 9;
                if (ind == 8) {
                    ind = content.indexOf("<leader/>") + 9;
                    if (ind == 8) {
                        ind = content.indexOf("record>") + 7;
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append(content.substring(0, ind));
                sb.append("<controlfield tag=\"001\">");
                sb.append(identifier);
                sb.append("</controlfield>");
                sb.append(content.substring(ind + 1));
                doc.getContent().setContent(sb.toString());
            }
            Map bibMap = new HashMap();
            bibMap.put("bibId", DocumentUniqueIDPrefix.getDocumentId(identifier));
            List<BibRecord> bibRecords = (List<BibRecord>) businessObjectService
                    .findMatching(BibRecord.class, bibMap);
            if (bibRecords != null && bibRecords.size() > 0) {
                BibRecord bibRecord = bibRecords.get(0);
                bibRecord.setContent(doc.getContent().getContent());
                businessObjectService.save(bibRecord);
            }
        }
    }

    protected void validateRequestDocument(RequestDocument requestDocument) throws OleDocStoreException {
        if (requestDocument != null && requestDocument.getContent() != null && StringUtils.isNotEmpty(requestDocument.getContent().getContent())) {
            WorkBibMarcRecordProcessor workBibMarcRecordProcessor = new WorkBibMarcRecordProcessor();

            WorkBibMarcRecords workBibMarcRecords = workBibMarcRecordProcessor.fromXML(requestDocument.getContent().getContent());

            WorkBibMarcRecordValidator wbmRecordValidator = new WorkBibMarcRecordValidator();
            List<List<DocStoreValidationError>> docStoreValidationErrors = new ArrayList<>();
            if (workBibMarcRecords != null) {

                for (WorkBibMarcRecord workBibMarcRecord : workBibMarcRecords.getRecords()) {
                    docStoreValidationErrors.add(wbmRecordValidator.validate(workBibMarcRecord));
                }
            }

            InputStream in = RepositoryBrowser.class.getResourceAsStream("docstore-type.properties");
            Properties categories = wbmRecordValidator.getPropertyValues(in);

            StringBuilder validationErrors = new StringBuilder();
            int i = 0;
            for (List<DocStoreValidationError> docStoreValidationErrorslist : docStoreValidationErrors) {
                for (DocStoreValidationError docStoreValidationError : docStoreValidationErrorslist) {
                    List<String> errorParams = docStoreValidationError.getErrorParams();
                    String param1 = "";
                    String param2 = "";
                    if (errorParams != null) {
                        if (errorParams.size() == 1) {
                            param1 = errorParams.get(0);
                        }
                        if (errorParams.size() == 2) {
                            param1 = errorParams.get(0);
                            param2 = errorParams.get(1);
                        }
                        i++;
                        validationErrors.append(i);
                        validationErrors.append("-");
                        validationErrors.append(categories.get(docStoreValidationError.getErrorId()));
                    }
                }
            }
            for (List<DocStoreValidationError> docStoreValidationErrorslist : docStoreValidationErrors) {
                if(docStoreValidationErrorslist!=null && docStoreValidationErrorslist.size()>0) {
                    throw new OleDocStoreException(validationErrors.toString());

                }
            }
        }
    }

    protected boolean getBibIdFromBibXMLContent(BibRecord bibRecord) {

        boolean isBibIdFlag = true ;
        BibliographicRecordHandler bibliographicRecordHandler = new BibliographicRecordHandler();
        Collection bibCollectionList = bibliographicRecordHandler.fromXML(bibRecord.getContent());
        if (bibCollectionList != null && bibCollectionList.getRecords() != null && bibCollectionList.getRecords().size() > 0) {
            BibliographicRecord bibliographicRecord = bibCollectionList.getRecords().get(0);
            if(bibliographicRecord.getControlfields() != null) {
                for (ControlField controlField : bibliographicRecord.getControlfields()) {
                    if ("001".equals(controlField.getTag()) && validateIdField(controlField.getValue())) {
                        bibRecord.setBibId(controlField.getValue());
                        isBibIdFlag = false;
                        break;
                    }
                }
            }
        }
        return isBibIdFlag;
    }

}
