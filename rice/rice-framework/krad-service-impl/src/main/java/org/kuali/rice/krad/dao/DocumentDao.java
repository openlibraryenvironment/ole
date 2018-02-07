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
package org.kuali.rice.krad.dao;

import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentAdHocService;

import java.util.List;

/**
 * This is the data access interface for Document objects.
 * 
 */
public interface DocumentDao {

	public <T extends Document> T save(T document);

	public <T extends Document> T findByDocumentHeaderId(Class<T> clazz, String id);

	public <T extends Document> List<T> findByDocumentHeaderIds(Class<T> clazz, List<String> idList);

	public BusinessObjectDao getBusinessObjectDao();

	public DocumentAdHocService getDocumentAdHocService();

}
