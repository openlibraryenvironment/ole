package org.kuali.ole.select.document.service;

/**
 * Created by IntelliJ IDEA.
 * User: balakumaranm
 * Date: 4/12/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleWebServiceProvider {
    public Object getService(String serviceClassName, String serviceName, String serviceURL);
}
