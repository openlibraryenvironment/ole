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
package edu.sampleu.bookstore.kew;

import edu.sampleu.bookstore.bo.Book;
import edu.sampleu.bookstore.bo.BookstoreKimAttributes;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.Maintainable;
import org.kuali.rice.kns.workflow.attribute.QualifierResolverBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Qualifier Resolver for Book Type
 *
 */
public class BookTypeQualifierResolver extends QualifierResolverBase {

	/* (non-Javadoc)
	 * @see org.kuali.rice.kew.role.QualifierResolver#resolve(org.kuali.rice.kew.engine.RouteContext)
	 */
	public List<Map<String, String>> resolve(RouteContext context) {
        List<Map<String, String>> qualifiers = new ArrayList<Map<String, String>>();
        MaintenanceDocument doc = (MaintenanceDocument) getDocument(context);
		Maintainable maint = doc.getNewMaintainableObject();
		Book book = (Book) maint.getDataObject();
		if (StringUtils.isNotEmpty(book.getTypeCode())) {
			qualifiers.add(Collections.singletonMap(BookstoreKimAttributes.BOOK_TYPE_CODE, book.getTypeCode()));
			decorateWithCommonQualifiers(qualifiers, context, null);
		}
		else {
            Map<String, String> basicQualifier = new HashMap<String, String>();
            qualifiers.add(basicQualifier);
		}
		return qualifiers;
	}

}
