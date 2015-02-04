package org.kuali.ole.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created with IntelliJ IDEA.
 * User: arjuns
 * Date: 7/10/13
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService(name = "OleLoanWebService", targetNamespace = "http://service.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)

public interface OleLoanDocumentWebService {

    public String getItemDueDate(String itemUuId);

    public boolean getItemRenewable(String itemUuId, String itemType, String location);

    public String getItemInformation(String itemUUID,String itemType,String itemShelvingLocation,String shelvingLocation,String localLocation);

}
