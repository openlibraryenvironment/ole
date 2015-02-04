package org.kuali.ole.describe.controller;

import java.util.HashMap;
import java.util.Map;

import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;

/**
 * Creates a singleton instance of an editor based on the given parameters.
 * User: tirumalesh.b
 * Date: 28/12/12 Time: 12:51 PM
 */
public class DocumentEditorFactory {
    private static DocumentEditorFactory documentEditorFactory = new DocumentEditorFactory();

    private Map<String, DocumentEditor> documentEditorMap = new HashMap<String, DocumentEditor>();

    public static DocumentEditorFactory getInstance() {
        return documentEditorFactory;
    }

    private DocumentEditorFactory() {
        initDocumentEditorMap();
    }

    private void initDocumentEditorMap() {
        String key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.MARC.getCode();
        documentEditorMap.put(key, WorkBibMarcEditor.getInstance());

        key = DocCategory.WORK.getCode() + DocType.INSTANCE.getCode() + DocFormat.OLEML.getCode();
        documentEditorMap.put(key, WorkHoldingsOlemlEditor.getInstance());

        key = DocCategory.WORK.getCode() + DocType.HOLDINGS.getCode() + DocFormat.OLEML.getCode();
        documentEditorMap.put(key, WorkHoldingsOlemlEditor.getInstance());

//        key = DocCategory.WORK.getCode() + DocType.SOURCEHOLDINGS.getCode() + DocFormat.OLEML.getCode();
//        documentEditorMap.put(key, WorkInstanceOlemlEditor.getInstance());

        key = DocCategory.WORK.getCode() + DocType.ITEM.getCode() + DocFormat.OLEML.getCode();
        documentEditorMap.put(key, WorkItemOlemlEditor.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.DUBLIN_CORE.getCode();
        documentEditorMap.put(key, WorkBibDublinEditor.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.DUBLIN_UNQUALIFIED.getCode();
        documentEditorMap.put(key, WorkBibDublinEditor.getInstance());

        key = DocCategory.WORK.getCode() + DocType.EINSTANCE.getCode() + DocFormat.OLEML.getCode();
        documentEditorMap.put(key, WorkEInstanceOlemlEditor.getInstance());

        key = DocCategory.WORK.getCode() + DocType.EHOLDINGS.getCode() + DocFormat.OLEML.getCode();
        documentEditorMap.put(key, WorkEInstanceOlemlEditor.getInstance());
    }

    public DocumentEditor getDocumentEditor(String docCategory, String docType, String docFormat) {
        return documentEditorMap.get(docCategory + docType + docFormat);
    }

}
