/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.kew.api.note;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;

@WebService(name = "noteService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface NoteService {

    @WebMethod(operationName = "getNotes")
    @WebResult(name = "notes")
    @XmlElementWrapper(name = "notes", required = true)
    @XmlElement(name = "note", required = false)
    List<Note> getNotes(@WebParam(name = "documentId") String documentId) throws RiceIllegalArgumentException;

    @WebMethod(operationName = "getNote")
    @WebResult(name = "note")
    @XmlElement(name = "note", required = false)
    Note getNote(@WebParam(name = "noteId") String noteId) throws RiceIllegalArgumentException;

    @WebMethod(operationName = "createNote")
    @WebResult(name = "note")
    @XmlElement(name = "note", required = true)
    Note createNote(@WebParam(name = "note") Note note) throws RiceIllegalArgumentException;

    @WebMethod(operationName = "update")
    @WebResult(name = "note")
    @XmlElement(name = "note", required = true)
    Note updateNote(@WebParam(name = "note") Note note) throws RiceIllegalArgumentException;

    @WebMethod(operationName = "deleteNote")
    @WebResult(name = "note")
    @XmlElement(name = "note", required = true)
    Note deleteNote(@WebParam(name = "noteId") String noteId) throws RiceIllegalArgumentException;

}
