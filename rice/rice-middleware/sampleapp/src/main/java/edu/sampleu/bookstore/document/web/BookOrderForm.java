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
package edu.sampleu.bookstore.document.web;

import edu.sampleu.bookstore.bo.BookOrder;
import edu.sampleu.bookstore.document.BookOrderDocument;
import org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase;

/*
 * BookOrderForm class file for BookOrder maintenance object
 */
public class BookOrderForm extends KualiTransactionalDocumentFormBase {

	private static final long serialVersionUID = -206564464059467788L;

	private BookOrder newBookOrder;

	public BookOrder getNewBookOrder() {
		return newBookOrder;
	}

	public void setNewBookOrder(BookOrder newBookOrder) {
		this.newBookOrder = newBookOrder;
	}
	
    public BookOrderDocument getBookOrderDocument() {
        return (BookOrderDocument) getDocument();
    }
	
}
