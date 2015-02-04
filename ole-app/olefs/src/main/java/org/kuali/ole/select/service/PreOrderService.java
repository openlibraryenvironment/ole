/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "preOrderService", targetNamespace = "http://service.select.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PreOrderService {

    public String createPreOrderForCitation(String citationString, String routeRequestorReceipt, String requestorsNote, String requestorsFirstName,
                                            String requestorsLastName, String requestorsAddress1, String requestorsAddress2, String requestorsCity, String requestorsState,
                                            String requestersZipCode, String requestorsCountryCode, String requestorsPhone, String requestorsEmail,
                                            String requestorsSMS, String requestorType) throws Exception;

    public String createPreOrderForOpenURL(String openUrlString, String routeRequesterReceipt, String requestorsNote, String requestorsFirstName,
                                           String requestorsLastName, String requestorsAddress1, String requestorsAddress2, String requestorsCity, String requestorsState,
                                           String requestorsZipCode, String requestorsCountryCode, String requestorsPhone, String requestorsEmail,
                                           String requestorsSMS, String requestorType) throws Exception;

    public String createPreOrderForForm(String title, String author, String edition, String series, String publisher,
                                        String placeOfPublication, String yearOfPublication, String standardNumber, String typeOfStandardNumber,
                                        String routeRequestorReceipt, String requestorsNote, String requestorsFirstName,
                                        String requestorsLastName, String requestorsAddress1, String requestorsAddress2, String requestorsCity, String requestorsState,
                                        String requestorsZipCode, String requestorsCountryCode, String requestorsPhone, String requestorsEmail,
                                        String requestorsSMS, String requestorType) throws Exception;

}
