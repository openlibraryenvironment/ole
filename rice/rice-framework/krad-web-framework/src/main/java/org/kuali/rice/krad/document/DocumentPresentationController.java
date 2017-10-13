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


/**
 * Determines what actions are applicable to the given document, irrespective of user
 * or other state.  These initial actions are used as inputs for further filtering depending
 * on context.
 * @see DocumentAuthorizer
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentPresentationController {

    public boolean canInitiate(String documentTypeName);

    public boolean canEdit(Document document);

    public boolean canAnnotate(Document document);

    public boolean canReload(Document document);

    public boolean canClose(Document document);

    public boolean canSave(Document document);

    public boolean canRoute(Document document);

    public boolean canCancel(Document document);

    public boolean canCopy(Document document);

    public boolean canPerformRouteReport(Document document);

    public boolean canAddAdhocRequests(Document document);

    public boolean canBlanketApprove(Document document);

    public boolean canApprove(Document document);

    public boolean canDisapprove(Document document);

    public boolean canSendAdhocRequests(Document document);

    public boolean canSendNoteFyi(Document document);

    public boolean canEditDocumentOverview(Document document);

    public boolean canFyi(Document document);

    public boolean canAcknowledge(Document document);

    public boolean canComplete(Document document);

    /**
     * @since 2.1
     */
    public boolean canRecall(Document document);
}
