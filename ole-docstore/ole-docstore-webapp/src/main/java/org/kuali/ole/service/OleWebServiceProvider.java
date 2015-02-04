package org.kuali.ole.service;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/22/12
 * Time: 6:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleWebServiceProvider {
    public Object getService(String serviceClassName, String serviceName, String serviceURL);
}
