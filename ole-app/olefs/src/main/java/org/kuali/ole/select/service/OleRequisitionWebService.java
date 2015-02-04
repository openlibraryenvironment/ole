package org.kuali.ole.select.service;


import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/9/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService(name = "OleRequisitionWebService", targetNamespace = "http://service.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface OleRequisitionWebService {


    @Oneway
    public void updateRequisitionStatus(String documentNumber, String reqStatus);

}
