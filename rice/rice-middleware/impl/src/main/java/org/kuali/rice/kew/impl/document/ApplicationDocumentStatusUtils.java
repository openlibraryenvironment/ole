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
package org.kuali.rice.kew.impl.document;

import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kew.doctype.ApplicationDocumentStatus;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for assisting with application document status
 */
public class ApplicationDocumentStatusUtils {

    /**
     * @see {@link #getApplicationDocumentStatusCategories(org.kuali.rice.kew.doctype.bo.DocumentType)}
     */
    public static LinkedHashMap<String, List<String>> getApplicationDocumentStatusCategories(String documentTypeName) {
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        return getApplicationDocumentStatusCategories(documentType);
    }

    /**
     * <p>Returns the categories defined for the given DocumentType.  The returned ordered map's keys
     * are the category names, and the values are Lists of the status values within the category.  Ordering is
     * maintained so that the form field can reflect the configuration in the parameter. </p>
     *
     * <p>Note that the hierarchy for the given document type will be walked until application document statuses are
     * found, or the available ancestry is exhausted.</p>
     *
     * @param documentType the document type for which to retrieve the defined application document status categories
     * @return the application document status categories, or an empty map if there are none defined for the given
     * document type
     */
    public static LinkedHashMap<String, List<String>> getApplicationDocumentStatusCategories(DocumentType documentType) {
        LinkedHashMap<String, List<String>> results = new LinkedHashMap<String, List<String>>();

        if (documentType != null) {
            // check the hierarchy until we find an ancestor with app doc statuses configured, or we exhaust the
            // ancestry in which case the docTypeAncestor will be null
//            DocumentType docTypeAncestor = documentType;
//            while (docTypeAncestor != null) {
//                // save a potentially un-needed fetch of the parent doc type fetch
//                if (!CollectionUtils.isEmpty(docTypeAncestor.getValidApplicationStatuses())) {
//                    break;
//                }
//
//                // walk up the hierarchy
//                docTypeAncestor = docTypeAncestor.getParentDocType();
//            }
//
//            if (docTypeAncestor != null) {
            // if you un-comment the hierarchy traversal logic above, you'll also need to replace
            // documentType with docTypeAncestor in this conditional block:
            if (!CollectionUtils.isEmpty(documentType.getValidApplicationStatuses())) {
                    sortBySequenceNumber(documentType.getValidApplicationStatuses());
                // build data structure for groupings and create headings
                for (ApplicationDocumentStatus status : documentType.getValidApplicationStatuses()) {
                    if (status.getCategoryName() != null) {
                        if (!results.containsKey(status.getCategoryName())) {
                             results.put(status.getCategoryName(), new ArrayList<String>());
                        }
                        results.get(status.getCategoryName()).add(status.getStatusName());
                    }
                }
            }
        }
        return results;
    }

    /**
     * <p>Sorts a List of {@link org.kuali.rice.kew.doctype.ApplicationDocumentStatus}es by their sequenceNumber
     * properties.  The sequenceNumber for a status may be null, and this Comparator considers a null sequenceNumber
     * to be &lt; any non-null sequenceNumber.</p>
     *
     * @param statuses the List of statuses to sort
     * @throws IllegalArgumentException if either of the given
     * {@link org.kuali.rice.kew.doctype.ApplicationDocumentStatus} arguments is null.
     *
     * @see Comparator#compare(Object, Object)
     */
    private static void sortBySequenceNumber(List<ApplicationDocumentStatus> statuses) {
        Collections.sort(statuses, new Comparator<ApplicationDocumentStatus>() {
            @Override
            public int compare(ApplicationDocumentStatus o1, ApplicationDocumentStatus o2) {
                if (o1 == null || o2 == null) throw new IllegalArgumentException();
                // cover null sequence number cases
                if (o1.getSequenceNumber() == null) {
                    if (o2.getSequenceNumber() == null) {
                        return 0;
                    } else {
                        return -1; // consider null to always be less than non-null
                    }
                }
                if (o2.getSequenceNumber() == null) {
                    return 1; // consider null to always be less than non-null
                }
                return o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
            }
        });
    }

}
