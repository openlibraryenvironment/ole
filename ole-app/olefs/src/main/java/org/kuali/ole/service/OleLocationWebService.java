package org.kuali.ole.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/21/13
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService(name = "OleLocationWebService", targetNamespace = "http://service.select.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface OleLocationWebService {

    public List<String> getItemLocation();

    public List<String> getItemType();
}
