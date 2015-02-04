package org.kuali.ole.docstore.common.client;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 12/30/13
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocstoreClientLocatorService {

    public boolean hasDocstoreLocalClient()throws Exception;

    public Object getDocstoreLocalClient()throws Exception;

    public DocstoreRestClient getRestClient()throws Exception;
}
