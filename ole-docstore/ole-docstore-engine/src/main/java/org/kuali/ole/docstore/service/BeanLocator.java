package org.kuali.ole.docstore.service;

import org.kuali.ole.docstore.common.client.DocstoreRestClient;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.document.jcr.JcrDocumentManagerFactory;
import org.kuali.ole.docstore.factory.DocstoreFactory;
import org.kuali.ole.repository.DocumentStoreManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: SG7940
 * Date: 5/16/12
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Helper class to access beans created by Spring.
 */
public class BeanLocator {

    public static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
            "/docstore-springbeans.xml");

    public static DocumentStoreManager getDocumentStoreManager() {

        DocumentStoreManager documentStoreManager = (DocumentStoreManager) applicationContext
                .getBean("documentStoreManagerService");
        return documentStoreManager;
    }

    public static IngestNIndexHandlerService getIngestNIndexHandlerService() {

        IngestNIndexHandlerService ingestNIndexHandlerService = (IngestNIndexHandlerService) applicationContext
                .getBean("ingestNIndexHandlerService");
        return ingestNIndexHandlerService;
    }

    public static Object getBean(String id) {
        return applicationContext.getBean(id);
    }

    public static DocumentServiceImpl getDocumentServiceImpl() {
        DocumentServiceImpl documentServiceImpl = (DocumentServiceImpl) applicationContext
                .getBean("documentService");
        return documentServiceImpl;
    }

    public static JcrDocumentManagerFactory getDocumentManagerFactory() {
        JcrDocumentManagerFactory jcrDocumentManagerFactoryService = (JcrDocumentManagerFactory) applicationContext
                .getBean("documentManagerFactory");
        return jcrDocumentManagerFactoryService;
    }

    public static BulkIngestProcessHandlerService getBulkIngestProcessHandlerService() {
        BulkIngestProcessHandlerService bulkIngestProcessHandlerService
                = (BulkIngestProcessHandlerService) applicationContext.getBean("bulkIngestProcessHandlerService");
        return bulkIngestProcessHandlerService;
    }

    public static DocstoreFactory getDocstoreFactory() {
        DocstoreFactory docstoreFactory = (DocstoreFactory) applicationContext.getBean("docstoreFactory");
        return docstoreFactory;
    }

    public static DocstoreService getDocstoreService() {
        DocstoreService docstoreService = (DocstoreService) applicationContext.getBean("docstoreService");
        return docstoreService;
    }


}
