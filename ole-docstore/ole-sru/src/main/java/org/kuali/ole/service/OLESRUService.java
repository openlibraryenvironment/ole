package org.kuali.ole.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 9/4/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService(name = "oleSRUService", targetNamespace = "http://service.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface OLESRUService {
    public String getDetails(String operation,String query,String version,String startRecord,String maximumRecords,String recordPacking,String recordSchema,String sortKeys,String recordXPath,String resultSetTTL,String styleSheet);
}
