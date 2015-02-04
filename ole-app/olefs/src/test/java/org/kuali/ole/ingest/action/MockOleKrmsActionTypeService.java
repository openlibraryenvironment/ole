package org.kuali.ole.ingest.action;

import org.kuali.ole.ingest.krms.action.OleCheckOutAction;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/18/12
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockOleKrmsActionTypeService extends ActionTypeServiceBase {

    private MockCreateBibAction createBibAction;
    private MockUpdateItemAction updateItemAction;
    //private OverlayAction overlayAction;
    private MockDeleteReplaceAction deleteReplaceAction;

    /**
     *      This method invokes appropriate Bib action based on action definition's name
     * @param actionDefinition
     * @return Action
     */
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter = null;
        if(actionDefinition.getName().equals("checkout")){
            parameter= actionDefinition.getAttributes().get("checkout");
            return  new OleCheckOutAction(parameter);
        } else if (actionDefinition.getName().equals("createBibAction")) {
            return getCreateBibAction();
        } else if (actionDefinition.getName().equals("updateItemAction")){
            return getUpdateItemAction();
        } else if (actionDefinition.getName().equals("deleteReplaceAction")){
            return getDeleteReplaceAction();
        }
        return new OleCheckOutAction(parameter);
    }

    public MockCreateBibAction getCreateBibAction() {
        return createBibAction;
    }

    public void setCreateBibAction(MockCreateBibAction createBibAction) {
        this.createBibAction = createBibAction;
    }

    public MockUpdateItemAction getUpdateItemAction() {
        return updateItemAction;
    }

    public void setUpdateItemAction(MockUpdateItemAction updateItemAction) {
        this.updateItemAction = updateItemAction;
    }

    public MockDeleteReplaceAction getDeleteReplaceAction() {
        return deleteReplaceAction;
    }

    public void setDeleteReplaceAction(MockDeleteReplaceAction deleteReplaceAction) {
        this.deleteReplaceAction = deleteReplaceAction;
    }
}
