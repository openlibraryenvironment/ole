/**
 * Copyright 2005-2013 The Kuali Foundation
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
package mocks;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import org.kuali.rice.kew.impl.document.DocumentRefreshQueueImpl;

/**
 * a DocumentRefreshQueueImpl extension that keeps track of the ids for docs that were requeued
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MockDocumentRefreshQueueImpl extends DocumentRefreshQueueImpl {

	private static final Set<String> requeuedDocumentIds = new HashSet<String>();
	private static Lock lock;
	
	public static Set<String> getRequeuedDocumentIds() {
		return Collections.unmodifiableSet(requeuedDocumentIds);
	}
	
	public static void clearRequeuedDocumentIds() {
		requeuedDocumentIds.clear();
	}

	/**
	 * @see org.kuali.rice.kew.impl.document.DocumentRefreshQueueImpl#requeueDocument(java.lang.Long)
	 */
	@Override
	public void refreshDocument(String documentId) {
		requeuedDocumentIds.add(documentId);
		super.refreshDocument(documentId);
	}
}
