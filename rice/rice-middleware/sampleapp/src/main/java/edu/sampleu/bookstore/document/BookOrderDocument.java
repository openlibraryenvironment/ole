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
package edu.sampleu.bookstore.document;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.krad.document.TransactionalDocumentBase;
import edu.sampleu.bookstore.bo.BookOrder;

/*
 * Transactional Document class file for Book Order.
 */

public class BookOrderDocument extends TransactionalDocumentBase {

	private static final long serialVersionUID = -1856169002927442467L;

	private List<BookOrder> bookOrders = new ArrayList<BookOrder>();

	public List<BookOrder> getBookOrders() {
		return bookOrders;
	}

	public void setBookOrders(List<BookOrder> bookOrders) {
		this.bookOrders = bookOrders;
	}
	
	public void addBookOrder(BookOrder bookOrder) {
		bookOrder.setDocumentId(getDocumentNumber());
		bookOrders.add(bookOrder);
    }
	
	public void removeBookOrder(int deleteIndex) {
		bookOrders.remove(deleteIndex);
	}
	
}
