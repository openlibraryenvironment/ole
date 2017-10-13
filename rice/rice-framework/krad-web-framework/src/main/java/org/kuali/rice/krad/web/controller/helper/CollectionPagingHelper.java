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
package org.kuali.rice.krad.web.controller.helper;

import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * StackedPagingHelper contains method(s) to help determine the correct page display information during a request
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CollectionPagingHelper {

    /**
     * Process the paging request by determining the displayStart value based on the page requested
     *
     * @param view the current view
     * @param collectionId the collection id
     * @param form the form
     * @param page the page requested (can be a number, prev, next, first, last)
     */
    public void processPagingRequest(View view, String collectionId, UifFormBase form, String page) {

        // avoid blowing the stack if the session expired
        if (view != null) {

            // only one concurrent request per view please
            synchronized (view) {
                CollectionGroup oldCollectionGroup = (CollectionGroup) view.getViewIndex().getComponentById(
                        collectionId);
                List<Object> modelCollection = ObjectPropertyUtils.getPropertyValue(form,
                        oldCollectionGroup.getBindingInfo().getBindingPath());

                int displayStart = oldCollectionGroup.getDisplayStart();
                int displayLength = oldCollectionGroup.getDisplayLength();

                // Adjust displayStart based on the page requested
                if (page.equals(UifConstants.PageRequest.FIRST)) {
                    displayStart = 0;
                } else if (page.equals(UifConstants.PageRequest.PREV)) {
                    displayStart = displayStart - displayLength;
                } else if (page.equals(UifConstants.PageRequest.NEXT)) {
                    displayStart = displayStart + displayLength;
                } else if (page.equals(UifConstants.PageRequest.LAST)) {
                    int lastPageSize = modelCollection.size() % displayLength;
                    if (lastPageSize != 0){
                        displayStart = modelCollection.size() - lastPageSize;
                    }
                    else{
                        displayStart = modelCollection.size() - displayLength;
                    }
                } else {
                    displayStart = ((Integer.parseInt(page.trim()) - 1) * displayLength);
                }

                // The displayStart value must be saved to the form and retrieved later during the applyModel phase
                // of the StackedLayout
                form.getExtensionData().put(collectionId + UifConstants.PageRequest.DISPLAY_START_PROP, new Integer(
                        displayStart));
            }
        }
    }
}
