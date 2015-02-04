package org.kuali.ole.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Map;

@WebService(name = "OleLoanWebService", targetNamespace = "http://service.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)

public interface OleLoanDocumentWebService {

    public String getItemDueDate(String itemUuId);

    public boolean getItemRenewable(String itemUuId, String itemType, String location);

    public String getItemInformation(String itemUUID,String itemType,String itemShelvingLocation,String shelvingLocation,String localLocation);

}
