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
package edu.sampleu.bookstore.maintenance;

import java.util.List;
import java.util.Map;

import org.kuali.rice.kns.maintenance.KualiMaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import edu.sampleu.bookstore.bo.Account;
import edu.sampleu.bookstore.bo.Author;
import edu.sampleu.bookstore.bo.Book;

/**
 * maintainableClass for Book document. 
 * Action to be taken before saving the BO
 */

public class BookMaintainable extends KualiMaintainableImpl {

	
	private static final long serialVersionUID = 1L;

	@Override
	public void saveBusinessObject() {
		// TODO Auto-generated method stub
		Book book = (Book) this.getBusinessObject();
	

		// /-----------------------------------------------------///
		// /-----------------------------------------------------///
		// /---IF author has to be edited from Book Document-----///
		// /---follow the below mentioned code else comment out--///
		// /-----------------------------------------------------///
		// /-----------------------------------------------------///
		List<Author> authors = book.getAuthors();
		for (Author author : authors) {
			Account account = (Account) author.getExtension();
			if (account != null && account.getAuthorId() == null) {
				author.setExtension(null);
			}

			KRADServiceLocator.getBusinessObjectService().save(author);

			if (account != null && account.getAuthorId() == null) {
				account.setAuthorId(author.getAuthorId());
				KRADServiceLocator.getBusinessObjectService().save(account);
			}
		}

		book.setAuthors(authors);
		KRADServiceLocator.getBusinessObjectService().save(book);

	}

	@Override
	public void processAfterCopy(MaintenanceDocument document,
			Map<String, String[]> parameters) {
		super.processAfterCopy(document, parameters);
		Book book = ((Book) document.getNewMaintainableObject()
				.getDataObject());
		book.setIsbn(null);
	}

}
