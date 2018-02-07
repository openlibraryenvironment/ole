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
package org.kuali.rice.krad.service;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.document.authorization.PessimisticLock;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service interface for documents to use the Pessimistic Locking mechanism
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface PessimisticLockService {

    /**
     * This method deletes the given lock object
     *
     * @param id - the id of the lock to delete
     */
    public void delete(String id);

    /**
     * This method will generate a new {@link PessimisticLock} object with a 'document'
     * lock descriptor
     *
     * @param documentNumber - the document number of the document associated with the new lock
     * @return the newly generated document descriptor {@link PessimisticLock}
     */
    public PessimisticLock generateNewLock(String documentNumber);

    /**
     * This method will generate a new {@link PessimisticLock} object with a lock descriptor of
     * the given parameter
     *
     * @param documentNumber - the document number of the document associated with the new lock
     * @param lockDescriptor - the lock descriptor the new PessimisticLock object should contain
     * @return the newly generated {@link PessimisticLock} containing the given lockDescriptor
     */
    public PessimisticLock generateNewLock(String documentNumber, String lockDescriptor);

    /**
     * This method will generate a new {@link PessimisticLock} object with a 'document'
     * lock descriptor
     *
     * @param documentNumber - the document number of the document associated with the new lock
     * @param user - the user to set on the new lock being generated
     * @return the newly generated document descriptor {@link PessimisticLock}
     */
    public PessimisticLock generateNewLock(String documentNumber, Person user);

    /**
     * This method will generate a new {@link PessimisticLock} object with a lock descriptor of
     * the given parameter
     *
     * @param documentNumber - the document number of the document associated with the new lock
     * @param lockDescriptor - the lock descriptor the new PessimisticLock object should contain
     * @param user - the user to set on the new lock being generated
     * @return the newly generated {@link PessimisticLock} containing the given lockDescriptor
     */
    public PessimisticLock generateNewLock(String documentNumber, String lockDescriptor, Person user);

    /**
     * This method gets all locks associated with the given document number
     *
     * @param documentNumber - the document number of the document requiring locks
     * @return an empty list if no locks are found or the list of {@link PessimisticLock} objects
     * found for the given documentNumber
     */
    public List<PessimisticLock> getPessimisticLocksForDocument(String documentNumber);

    /**
     * Return all locks associated with the given session id
     *
     * @param sessionId - the session id
     * @return an empty list of no locks are found or the list of {@link PessimisticLock} objects
     * found for the given sessionId
     */
    public List<PessimisticLock> getPessimisticLocksForSession(String sessionId);
    
    /**
     * This method is used to identify who is an admin user for {@link PessimisticLock} objects
     *
     * @param user - user to verify as admin
     * @return true if the given use is an admin user or false if not
     */
    public boolean isPessimisticLockAdminUser(Person user);

    /**
     * This method will release all locks in the given list that are owned by the given user
     *
     * @param locks - locks to release if owned by given user
     * @param user - user to check for lock ownership
     */
    public void releaseAllLocksForUser(List<PessimisticLock> locks, Person user);

    /**
     * This method will release all locks in the given list that are owned by the given user that have a matching lock
     * descriptor value
     *
     * @param locks - locks to release if owned by given user
     * @param user - user to check for lock ownership
     * @param lockDescriptor - lock descriptor value to match locks against
     */
    public void releaseAllLocksForUser(List<PessimisticLock> locks, Person user, String lockDescriptor);

    /**
     * This method saves the given lock object
     *
     */
    public PessimisticLock save(PessimisticLock lock);

    /**
     * @param document - the document locks are to be established against or by
     * @param editMode - the editMode returned by the method {@link #getEditMode(Document, Person)}
     * @param user - the user locks are being established for
     * @return New map generated by locking logic combined with passed in parameter editMode.  Map contains keys
     *         AuthorizationConstants.EditMode value (String) which indicates what operations the user is currently
     *         allowed to take on that document.  This may be a modified list of
     */
    public Map establishLocks(Document document, Map editMode, Person user);

    /**
     * @param document - the document to create the lock against and add the lock to
     */
    public void establishWorkflowPessimisticLocking(Document document);

    /**
     * @param document - document to release locks from
     */
    public void releaseWorkflowPessimisticLocking(Document document);

    /**
     * @param document
     * @param user
     * @return Set of actions are permitted the given user on the given document
     */
    public Set getDocumentActions(Document document, Person user, Set<String> documentActions);

}

