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
package org.kuali.rice.kew.api.document.search;

import org.kuali.rice.kew.api.document.DocumentContract;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeContract;

import java.util.List;

/**
 * Defines the contract for a single document result from execution of a document search.  This serves to package the
 * actual document with it's document attributes.
 */
public interface DocumentSearchResultContract {

    /**
     * Returns the document represented by this result.  This should include all information available on the
     * {@code DocumentContract} with the exception of document {@code variables}.  Even if a document has variables
     * defined they will not be included on the document returned from this method.
     *
     * @return the document represented by this result, this will never be null
     */
    DocumentContract getDocument();

    /**
     * Returns an unmodifiable list of objects implementing the {@link DocumentAttributeContract} interface.  These
     * define the various document attributes that have been indexed for the document represented by this result.
     *
     * @return an unmodifiable list containing the document attribute values associated with the document represented
     * by this result, this will never be null but may be empty
     */
    List<? extends DocumentAttributeContract> getDocumentAttributes();

}
