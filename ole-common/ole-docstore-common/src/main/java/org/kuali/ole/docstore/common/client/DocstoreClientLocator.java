package org.kuali.ole.docstore.common.client;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 1/6/14
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreClientLocator {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocstoreClientLocator.class);
    private DocstoreClient docstoreClient = null;
    public DocstoreClientLocator(){
        // TODO: Remove this line after fixing the logic for getDocstoreClient.
        docstoreClient = new DocstoreRestClient();
    }
    public DocstoreClient getDocstoreClient()throws Exception{
        if (docstoreClient !=null) {
            return docstoreClient;
        } else {
            LOG.info("docstoreClient is null");
            // Check whether docstore is local (Look for docstore local service).
            try{
                Class<?> docstoreServiceClass = Class.forName("org.kuali.ole.docstore.engine.service.DocstoreServiceImpl");
                // Docstore is local. So create a local client.
                docstoreClient = (DocstoreClient) Class.forName("org.kuali.ole.docstore.engine.client.DocstoreLocalClient").newInstance();
                // TODO: Catch instantiation exception and throw DocstoreException.
                LOG.info("DocstoreLocalClient successfully instantiated");
                // Loginfo  "Docstore local client created".
            }catch(ClassNotFoundException cnfe){
                // Docstore is remote. So create a rest client.
                docstoreClient = new DocstoreRestClient();
                LOG.info("DocstoreRestClient successfully instantiated");
            }
        }
        return docstoreClient;
    }
}
