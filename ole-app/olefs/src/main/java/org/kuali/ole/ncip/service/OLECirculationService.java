package org.kuali.ole.ncip.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/21/13
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService(name = "oleCirculationService", targetNamespace = "http://service.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface OLECirculationService {

    public String lookupUser(String patronBarcode, String operator,String agencyId);
    public String lookupUserForNCIP(String patronBarcode, String operator,String agencyId);
    public String getCheckedOutItems(String patronBarcode, String operator) throws Exception;
    public String placeRequest(String patronBarcode, String operator, String itemBarcode,String requestType,String pickUpLocation,String itemLocation);
    public String cancelRequest(String operator,String patronBarcode,String itemBarcode);
    public String renewItem(String patronBarcode, String operator, String itemBarcode);
    public String acceptItem(String patronBarcode, String operator, String itemBarcode, String callNumber, String title, String author, String itemType, String itemLocation, String dateExpires,String requestType,String pickUpLocation);
    public String checkInItem(String patronBarcode, String operator, String itemBarcode,String deleteIndicator);
    public String checkOutItem(String patronBarcode, String operator, String itemBarcode);
    public String getFine(String patronBarcode, String operator) throws Exception;
    public String getHolds(String patronBarcode, String operator) throws Exception;
    public String cancelRequests(String operator, String requestId);
}
