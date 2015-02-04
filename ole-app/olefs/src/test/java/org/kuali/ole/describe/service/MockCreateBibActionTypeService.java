package org.kuali.ole.describe.service;

import org.kuali.ole.ingest.action.ExceptionAction;
import org.kuali.ole.ingest.action.MockCreateBibAction;
import org.kuali.ole.ingest.action.MockUpdateItemAction;
import org.kuali.ole.ingest.action.OverlayAction;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/18/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockCreateBibActionTypeService  extends ActionTypeServiceBase {

    private MockCreateBibAction createBibAction;
    private MockUpdateItemAction updateItemAction;
    private OverlayAction overlayAction;

    /**
     *      This method invokes appropriate Bib action based on action definition's name
     * @param actionDefinition
     * @return Action
     */
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        if (actionDefinition.getName().equals("createBibAction")) {
            return getCreateBibAction();
        } else if (actionDefinition.getName().equals("updateItemAction")){
            return getUpdateItemAction();
        } else if (actionDefinition.getName().equals("exceptionAction")){
            return new ExceptionAction();
        }else if (actionDefinition.getName().equals("overlayAction")){
            return new OverlayAction();
        }
        throw new NullPointerException("No actions defined");
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

    public OverlayAction getOverlayAction() {
        return overlayAction;
    }

    public void setOverlayAction(OverlayAction overlayAction) {
        this.overlayAction = overlayAction;
    }
}
