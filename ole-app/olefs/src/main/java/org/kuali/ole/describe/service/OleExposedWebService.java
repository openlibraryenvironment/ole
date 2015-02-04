package org.kuali.ole.describe.service;

import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * OleExposedWebService is the service interface used to expose a certain set of webservices
 */
@WebService(name = "oleExposedWebService", targetNamespace = "http://service.select.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface OleExposedWebService {

    public void addDoctoreResponse(String docstoreResponse);

    @Oneway
    public void createReqAndPO(String oleOrderRecordXMLContent);

    public String getPaymentMethod();
}