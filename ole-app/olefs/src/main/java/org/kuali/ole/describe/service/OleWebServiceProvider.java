package org.kuali.ole.describe.service;

/**
 * OleWebServiceProvider helps in consuming webservices which are exposed in other applications.
 */
public interface OleWebServiceProvider {
    public Object getService(String serviceClassName, String serviceName, String serviceURL);
}
