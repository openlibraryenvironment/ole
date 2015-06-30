package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hemalathas on 12/19/14.
 */
public class OLEAccessActivationConfiguration extends PersistableBusinessObjectBase {

    private String accessActivationConfigurationId;
    private String workflowName;
    private String workflowType;
    private String workflowCompletionStatus;
    private List<OLEAccessActivationWorkFlow> accessActivationWorkflowList = new ArrayList<OLEAccessActivationWorkFlow>();
    private boolean active;
    private String selector;

    public String getAccessActivationConfigurationId() {
        return accessActivationConfigurationId;
    }

    public void setAccessActivationConfigurationId(String accessActivationConfigurationId) {
        this.accessActivationConfigurationId = accessActivationConfigurationId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<OLEAccessActivationWorkFlow> getAccessActivationWorkflowList() {
        return accessActivationWorkflowList;
    }

    public void setAccessActivationWorkflowList(List<OLEAccessActivationWorkFlow> accessActivationWorkflowList) {
        this.accessActivationWorkflowList = accessActivationWorkflowList;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(String workflowType) {
        this.workflowType = workflowType;
    }

    public String getWorkflowCompletionStatus() {
        return workflowCompletionStatus;
    }

    public void setWorkflowCompletionStatus(String workflowCompletionStatus) {
        this.workflowCompletionStatus = workflowCompletionStatus;
    }

    @Override
    public List<Collection<PersistableBusinessObject>> buildListOfDeletionAwareLists() {
        List<Collection<PersistableBusinessObject>> collectionList = new ArrayList<>();
        collectionList.add((Collection)getAccessActivationWorkflowList());
        return collectionList;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }
}
