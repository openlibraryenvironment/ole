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
package org.kuali.rice.krad.document;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.uif.view.ViewPresentationControllerBase;

import java.util.HashSet;
import java.util.Set;

/**
* Document presentation controller implementation for transactional documents.
*
* <p>
* Determines what actions are applicable to the given document, irrespective of user
* or other state.  These initial actions are used as inputs for further filtering depending
* on context.
* </p>
*
* @see DocumentPresentationController
* @author Kuali Rice Team (rice.collab@kuali.org)
*/
public class TransactionalDocumentPresentationControllerBase extends DocumentPresentationControllerBase implements TransactionalDocumentPresentationController {
    private static Log LOG = LogFactory.getLog(TransactionalDocumentPresentationControllerBase.class);

}