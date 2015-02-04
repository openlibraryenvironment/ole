package org.kuali.ole.docstore.common.client.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.client.DocstoreClientLocatorService;
import org.kuali.ole.docstore.common.client.DocstoreRestClient;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 12/30/13
 * Time: 6:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreClientLocatorServiceImpl implements DocstoreClientLocatorService {

    private static final Logger LOG = Logger.getLogger(DocstoreClientLocatorServiceImpl.class);

    public boolean hasDocstoreLocalClient()throws Exception{
        boolean isDocstoreClientAvailable = true;
        Class<?> docstoreClientClass = null;
        try{
            docstoreClientClass = Class.forName("org.kuali.ole.docstore.engine.client.DocstoreLocalClient");
        }catch(ClassNotFoundException cnfe){
            LOG.error("Exception ", cnfe);
            isDocstoreClientAvailable = false;
        }
        return isDocstoreClientAvailable;
    }

    public Object getDocstoreLocalClient()throws Exception{
        Class<?> docstoreClientClass = Class.forName("org.kuali.ole.docstore.engine.client.DocstoreLocalClient");
        Object docstoreLocalClient = docstoreClientClass.newInstance();
        return docstoreLocalClient;
    }

    public DocstoreRestClient getRestClient()throws Exception{
        return new DocstoreRestClient();
    }
}
