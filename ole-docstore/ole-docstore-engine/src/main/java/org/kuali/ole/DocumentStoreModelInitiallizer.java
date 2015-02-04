package org.kuali.ole;

import org.kuali.ole.docstore.model.xmlpojo.config.DocumentCategory;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentConfig;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentType;

import javax.jcr.Node;
import javax.jcr.Session;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 1/10/12
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentStoreModelInitiallizer {

        /*public void init() throws Exception {
            RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
            List<OleDocStoreData> oleDocStoreDatas = repositoryBrowser.browseDataSetup();
            RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
            Session session = repositoryManager.getSession("documentStoreModelInitiallizer","init");
            Node rootNode = session.getRootNode();

            for (Iterator<OleDocStoreData> iterator = oleDocStoreDatas.iterator(); iterator.hasNext(); ) {
                OleDocStoreData oleDocStoreData = iterator.next();
                String category = oleDocStoreData.getCategory();
                Node categoryNode;
                if (!rootNode.hasNode(category)) {
                    categoryNode = rootNode.addNode(category, "nt:unstructured");
                    categoryNode.setProperty("nodeType", "folder");
                    categoryNode.addMixin("mix:referenceable");
                } else {
                    categoryNode = rootNode.getNode(category);
                }
                List<String> doctypes = oleDocStoreData.getDoctypes();
                for (Iterator<String> stringIterator = doctypes.iterator(); stringIterator.hasNext(); ) {
                    String docType = stringIterator.next();
                    Node typeNode;
                    if (!categoryNode.hasNode(docType)) {
                        typeNode = categoryNode.addNode(docType, "nt:unstructured");
                        typeNode.setProperty("nodeType", "folder");
                        typeNode.addMixin("mix:referenceable");
                    } else {
                        typeNode = categoryNode.getNode(docType);
                    }
                }
            }
            session.save();
            repositoryManager.logout(session);
        }*/

    public void init() throws Exception {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession("documentStoreModelInitiallizer", "init");
        Node rootNode = session.getRootNode();
        DocumentConfig documentConfig = DocumentConfig.getInstance();
        for (DocumentCategory documentCategory : documentConfig.getDocumentCategories()) {
            String category = documentCategory.getId();
            Node categoryNode;
            if (!rootNode.hasNode(category)) {
                categoryNode = rootNode.addNode(category, "nt:unstructured");
                categoryNode.setProperty("nodeType", "folder");
                categoryNode.addMixin("mix:referenceable");
            } else {
                categoryNode = rootNode.getNode(category);
            }
            for (DocumentType documentType : documentCategory.getDocumentTypes()) {
                String docType = documentType.getId();
                if (!((docType.equals("item") || docType.equals("holdings")))) {
                    Node typeNode;
                    if (!categoryNode.hasNode(docType)) {
                        typeNode = categoryNode.addNode(docType, "nt:unstructured");
                        typeNode.setProperty("nodeType", "folder");
                        typeNode.addMixin("mix:referenceable");
                    } else {
                        typeNode = categoryNode.getNode(docType);
                    }
                }
            }
        }
        session.save();
        repositoryManager.logout(session);
    }
}
