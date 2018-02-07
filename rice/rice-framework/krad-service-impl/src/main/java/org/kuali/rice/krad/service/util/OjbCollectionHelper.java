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
package org.kuali.rice.krad.service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * Helper object to deal with persisting collections
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class OjbCollectionHelper {
	private static final Logger LOG = Logger.getLogger(OjbCollectionHelper.class);

    /**
     * OJB RemovalAwareLists do not survive through the response/request lifecycle. This method is a work-around to forcibly remove
     * business objects that are found in Collections stored in the database but not in memory.
     * 
     * @param orig
     * @param id
     * @param template
     */
    public void processCollections(OjbCollectionAware template, PersistableBusinessObject orig, PersistableBusinessObject copy) {
        if (copy == null) {
            return;
        }
        
        List<Collection<PersistableBusinessObject>> originalCollections = orig.buildListOfDeletionAwareLists();

        if (originalCollections != null && !originalCollections.isEmpty()) {
            /*
             * Prior to being saved, the version in the database will not yet reflect any deleted collections. So, a freshly
             * retrieved version will contain objects that need to be removed:
             */
            try {
                List<Collection<PersistableBusinessObject>> copyCollections = copy.buildListOfDeletionAwareLists();
                int size = originalCollections.size();

                if (copyCollections.size() != size) {
                    throw new RuntimeException("size mismatch while attempting to process list of Collections to manage");
                }

                for (int i = 0; i < size; i++) {
                    Collection<PersistableBusinessObject> origSource = originalCollections.get(i);
                    Collection<PersistableBusinessObject> copySource = copyCollections.get(i);
                    List<PersistableBusinessObject> list = findUnwantedElements(copySource, origSource);
                    cleanse(template, origSource, list);
                }
            }
            catch (ObjectRetrievalFailureException orfe) {
                // object wasn't found, must be pre-save
            }
        }
    }
    
    /**
     * OJB RemovalAwareLists do not survive through the response/request lifecycle. This method is a work-around to forcibly remove
     * business objects that are found in Collections stored in the database but not in memory.
     * 
     * @param orig
     * @param id
     * @param template
     */
    public void processCollections2(OjbCollectionAware template, PersistableBusinessObject orig, PersistableBusinessObject copy) {
        // if copy is null this is the first time we are saving the object, don't have to worry about updating collections
        if (copy == null) {
            return;
        }
        
        List<Collection<PersistableBusinessObject>> originalCollections = orig.buildListOfDeletionAwareLists();

        if (originalCollections != null && !originalCollections.isEmpty()) {
            /*
             * Prior to being saved, the version in the database will not yet reflect any deleted collections. So, a freshly
             * retrieved version will contain objects that need to be removed:
             */
            try {
                List<Collection<PersistableBusinessObject>> copyCollections = copy.buildListOfDeletionAwareLists();
                int size = originalCollections.size();

                if (copyCollections.size() != size) {
                    throw new RuntimeException("size mismatch while attempting to process list of Collections to manage");
                }

                for (int i = 0; i < size; i++) {
                    Collection<PersistableBusinessObject> origSource = originalCollections.get(i);
                    Collection<PersistableBusinessObject> copySource = copyCollections.get(i);
                    List<PersistableBusinessObject> list = findUnwantedElements(copySource, origSource);
                    cleanse(template, origSource, list);
                }
            }
            catch (ObjectRetrievalFailureException orfe) {
                // object wasn't found, must be pre-save
            }
        }
    }

    /**
     * This method deletes unwanted objects from the database as well as from the given input List
     * 
     * @param origSource - list containing unwanted business objects
     * @param unwantedItems - business objects to be permanently removed
     * @param template
     */
    private void cleanse(OjbCollectionAware template, Collection<PersistableBusinessObject> origSource, List<PersistableBusinessObject> unwantedItems) {
        if (unwantedItems.size() > 0) {
        	for (PersistableBusinessObject unwantedItem : unwantedItems) {
            	if ( LOG.isDebugEnabled() ) {
            		LOG.debug( "cleansing " + unwantedItem);
            	}
                template.getPersistenceBrokerTemplate().delete(unwantedItem);
            }
        }

    }

    /**
     * This method identifies items in the first List that are not contained in the second List. It is similar to the (optional)
     * java.util.List retainAll method.
     * 
     * @param fromList
     * @param controlList
     * @return true iff one or more items were removed
     */
    private List<PersistableBusinessObject> findUnwantedElements(Collection<PersistableBusinessObject> fromList, Collection<PersistableBusinessObject> controlList) {
        List<PersistableBusinessObject> toRemove = new ArrayList<PersistableBusinessObject>();

        for (PersistableBusinessObject fromObject : fromList) {
        	if (!ObjectUtils.collectionContainsObjectWithIdentitcalKey(controlList, fromObject)) {
                toRemove.add(fromObject);
            }
        }
        return toRemove;
    }
}
