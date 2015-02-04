package org.kuali.ole.service;


import org.kuali.ole.select.bo.OleLicenseRequestBo;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/9/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
/*@WebService(name = "OleLicenseRequestWebService", targetNamespace = "http://service.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)*/
public interface OleLicenseRequestWebService {


    public OleLicenseRequestBo createLicenseRequest(String documentNumber, String licenseRequestContent);

    // public String getLicenseRequestDocNumber(String reqDocNum);

}