package org.kuali.ole.select.document.service;


import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/9/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService(name = "OleLicenseRequestService", targetNamespace = "http://service.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface OleLicenseRequestWebService {


    public String createLicenseRequest(String documentnumber, String licenseRequestContent);

    public String getLicenseRequestDocNumber(String reqDocNum);

}
