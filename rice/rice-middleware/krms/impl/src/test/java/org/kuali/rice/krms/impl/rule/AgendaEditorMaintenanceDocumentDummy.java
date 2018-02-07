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
package org.kuali.rice.krms.impl.rule;

import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteWorkgroup;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectExtension;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.document.authorization.PessimisticLock;
import org.kuali.rice.krad.maintenance.Maintainable;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.util.NoteType;
import org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator;

import java.util.Collection;
import java.util.List;

public class AgendaEditorMaintenanceDocumentDummy implements MaintenanceDocument {
    protected Maintainable oldMaintainableObject;
    protected Maintainable newMaintainableObject;

    public String getXmlDocumentContents() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Maintainable getNewMaintainableObject() {
        return this.newMaintainableObject;
    }

    public Maintainable getOldMaintainableObject() {
        return this.oldMaintainableObject;
    }

    public void setXmlDocumentContents(String documentContents) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setNewMaintainableObject(Maintainable newMaintainableObject) {
        this.newMaintainableObject = newMaintainableObject;
    }

    public void setOldMaintainableObject(Maintainable oldMaintainableObject) {
        this.oldMaintainableObject = oldMaintainableObject;
    }

    public Object getDocumentDataObject() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void populateXmlDocumentContentsFromMaintainables() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void populateMaintainablesFromXmlDocumentContents() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isOldDataObjectInDocument() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isNew() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isEdit() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isNewWithExisting() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isFieldsClearedOnCopy() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setFieldsClearedOnCopy(boolean keysClearedOnCopy) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isDisplayTopicFieldInNotes() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setDisplayTopicFieldInNotes(boolean displayTopicFieldInNotes) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public DocumentHeader getDocumentHeader() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setDocumentHeader(DocumentHeader documentHeader) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getDocumentNumber() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setDocumentNumber(String documentHeaderId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void populateDocumentForRouting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String serializeDocumentToXml() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getXmlForRouteReport() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void doActionTaken(ActionTakenEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void afterActionTaken(ActionType performed, ActionTakenEvent event) {

    }

    public void afterWorkflowEngineProcess(boolean successfullyProcessed) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void beforeWorkflowEngineProcess() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<String> getWorkflowEngineDocumentIdsToLock() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getDocumentTitle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<AdHocRoutePerson> getAdHocRoutePersons() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<AdHocRouteWorkgroup> getAdHocRouteWorkgroups() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAdHocRoutePersons(List<AdHocRoutePerson> adHocRoutePersons) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAdHocRouteWorkgroups(List<AdHocRouteWorkgroup> adHocRouteWorkgroups) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void prepareForSave() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void validateBusinessRules(KualiDocumentEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void prepareForSave(KualiDocumentEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void postProcessSave(KualiDocumentEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void processAfterRetrieve() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean getAllowsCopy() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<KualiDocumentEvent> generateSaveEvents() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public NoteType getNoteType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PersistableBusinessObject getNoteTarget() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addNote(Note note) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Note> getNotes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setNotes(List<Note> notes) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Note getNote(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean removeNote(Note note) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<PessimisticLock> getPessimisticLocks() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void refreshPessimisticLocks() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addPessimisticLock(PessimisticLock lock) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<String> getLockClearningMethodNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getBasePathToDocumentDuringSerialization() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PropertySerializabilityEvaluator getDocumentPropertySerizabilityEvaluator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object wrapDocumentWithMetadataForXmlSerialization() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean useCustomLockDescriptors() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCustomLockDescriptor(Person user) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setVersionNumber(Long versionNumber) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setObjectId(String objectId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public PersistableBusinessObjectExtension getExtension() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setExtension(PersistableBusinessObjectExtension extension) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void refreshNonUpdateableReferences() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void refreshReferenceObject(String referenceObjectName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Collection<PersistableBusinessObject>> buildListOfDeletionAwareLists() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isNewCollectionRecord() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setNewCollectionRecord(boolean isNewCollectionRecord) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void linkEditableUserFields() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getObjectId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getVersionNumber() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
