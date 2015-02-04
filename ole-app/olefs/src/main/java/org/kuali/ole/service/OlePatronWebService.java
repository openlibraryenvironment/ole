package org.kuali.ole.service;


import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


@WebService(name = "OlePatronWebService", targetNamespace = "http://service.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface OlePatronWebService {


    public String getPatronRecords();

}
