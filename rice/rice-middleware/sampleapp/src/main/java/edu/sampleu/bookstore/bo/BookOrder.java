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
package edu.sampleu.bookstore.bo;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiPercent;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;


/**
 * BookOrder Business Object class file.
 */
public class BookOrder extends PersistableBusinessObjectBase {

	private static final long serialVersionUID = -3602318476248146613L;

	private Long id;
	private String documentId;
	private Long bookId;
	private Integer quantity;
	private KualiPercent discount;
	private KualiDecimal unitPrice;
	private KualiDecimal totalPrice;

	private Book book;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public KualiPercent getDiscount() {
		return discount;
	}

	public void setDiscount(KualiPercent discount) {
		this.discount = discount;
	}

	public KualiDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(KualiDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public KualiDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(KualiDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	 

}
