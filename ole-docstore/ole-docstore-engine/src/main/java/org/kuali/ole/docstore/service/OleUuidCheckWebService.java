package org.kuali.ole.docstore.service;


import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created with IntelliJ IDEA.
 * User:?
 * Date: 5/22/12
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService(name = "oleUuidCheckWebService", targetNamespace = "http://service.select.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface OleUuidCheckWebService {

    public String checkUuidExsistence(String uuid);
}
