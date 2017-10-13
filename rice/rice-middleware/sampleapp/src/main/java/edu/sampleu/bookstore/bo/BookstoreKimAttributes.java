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
/**
 * 
 */
package edu.sampleu.bookstore.bo;

import java.util.LinkedHashMap;

import org.kuali.rice.kim.bo.impl.KimAttributes;

/**
 * Class to hold all Bookstore application specific KIM attributes
 *
 */
public class BookstoreKimAttributes extends KimAttributes {

	private static final long serialVersionUID = -1095291722496057450L;

	public static final String BOOK_TYPE_CODE = "bookTypeCode";

	protected String bookTypeCode;

	protected BookType bookType;

	 

	public String getBookTypeCode() {
		return bookTypeCode;
	}

	public void setBookTypeCode(String bookTypeCode) {
		this.bookTypeCode = bookTypeCode;
	}

	public BookType getBookType() {
		return bookType;
	}

	public void setBookType(BookType bookType) {
		this.bookType = bookType;
	}

}
