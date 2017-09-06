package org.kuali.ole.ncip.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.sql.Date;

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

    public String lookupUser(String patronBarcode, String operator, String agencyId, boolean isSIP2Request);
    public String lookupUserForNCIP(String patronBarcode, String operator,String agencyId);
    public String getCheckedOutItems(String patronBarcode, String operator) throws Exception;
    public String placeRequest(String patronBarcode, String operator, String itemBarcode, String itemIdentifier, String requestType, String pickUpLocation, String itemLocation, String bibId, String requestLevel, Date requestExpiryDate, String requestNote);
    public String overridePlaceRequest(String patronBarcode, String operator, String itemBarcode, String requestType, String pickUpLocation, String itemLocation, String bibId, String requestLevel, Date requestExpiryDate, String requestNote);

    public String cancelRequest(String operator, String patronBarcode, String itemBarcode);
    public String renewItem(String patronBarcode, String operator, String itemBarcode, boolean isSIP2Request);
    public String renewItemList(String patronBarcode, String operator, String itemBarcode, boolean isSIP2Request);
    public String acceptItem(String patronBarcode, String operator, String itemBarcode, String callNumber, String title, String author, String itemType, String itemLocation, String dateExpires, String requestType, String pickUpLocation);
    public String checkInItem(String patronBarcode, String operator, String itemBarcode, String deleteIndicator, boolean isSIP2Request);
    public String checkOutItem(String patronBarcode, String operator, String itemBarcode, boolean isSIP2Request);
    public String getFine(String patronBarcode, String operator) throws Exception;
    public String getHolds(String patronBarcode, String operator) throws Exception;
    public String cancelRequests(String operator, String requestId);
}
