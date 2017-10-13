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
package edu.sampleu.bookstore.document.attribs;

import org.kuali.rice.kew.api.KewApiConstants;

/**
 * SearchableAttribute on book rating
 */
public class BookRatingSearchableAttribute extends XPathSearchableAttribute {
    public BookRatingSearchableAttribute() {
        super("book_rating", KewApiConstants.SearchableAttributeConstants.DATA_TYPE_LONG, "//newMaintainableObject/businessObject/rating/value/text()", "Book Rating");
    }
}
