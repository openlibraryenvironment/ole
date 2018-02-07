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
package edu.sampleu.bookstore.rule;

import edu.sampleu.bookstore.bo.BookOrder;
import edu.sampleu.bookstore.document.BookOrderDocument;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.TransactionalDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.List;


/*
 * Business Rule for Book Order Document that follows prior to submit action.
 * Checks that book order/orders is/are not null or Empty and all the attributes are mentioned for each order.
 */

public class BookOrderDocumentRule extends TransactionalDocumentRuleBase {

	private static final String BOOK_ORDERS_PROPERTY_PATH = KRADConstants.DOCUMENT_PROPERTY_NAME + ".bookOrders";
	private static final String NO_BOOK_ORDERS_ERROR_KEY = RiceKeyConstants.ERROR_CUSTOM;
	private static final String ERROR_MESSAGE_NO_ORDERS = "You must add at least one entry to your book order.";
	
	private static final String BOOK_ORDERS_EMPTY_ERROR_KEY = RiceKeyConstants.ERROR_CUSTOM;
	private static final String ERROR_MESSAGE_EMPTY_ORDERS = "You must add attributes to your book order.";
	
	@Override
	protected boolean processCustomRouteDocumentBusinessRules(Document document) {
		
		System.out.println("@@@@ IN RULE CHECK");
		
		// cast the document to a BookOrderDocument
		BookOrderDocument bookOrderDocument = (BookOrderDocument)document;
		
		// get the list of book orders of the book order document
		List<BookOrder> bookOrders = bookOrderDocument.getBookOrders();
		
		// make sure that the list is not empty
		if (bookOrders == null || bookOrders.isEmpty()) {
			GlobalVariables.getMessageMap().putError(BOOK_ORDERS_PROPERTY_PATH, NO_BOOK_ORDERS_ERROR_KEY, ERROR_MESSAGE_NO_ORDERS);
			System.out.println("@@@@ FALSE RULE CHECK");
			return false;
		} else {
			for(BookOrder bookOrder : bookOrders){				
				if(bookOrder.getBookId() == null || bookOrder.getDiscount() == null || bookOrder.getQuantity() == null){
					GlobalVariables.getMessageMap().putError(BOOK_ORDERS_PROPERTY_PATH, BOOK_ORDERS_EMPTY_ERROR_KEY, ERROR_MESSAGE_EMPTY_ORDERS);
					System.out.println("@@@@ FALSE RULE CHECK");
					return false;
				}				
			}
			
		}
		System.out.println("@@@@ TRUE RULE CHECK");
		
		return super.processCustomRouteDocumentBusinessRules(document);
	}

	

	
	
}
