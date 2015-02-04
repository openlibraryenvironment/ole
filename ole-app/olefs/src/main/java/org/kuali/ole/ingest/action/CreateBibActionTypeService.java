package org.kuali.ole.ingest.action;

import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

/**
 *  CreateBibActionTypeService is used to load the action according to action type service.
 */
public class CreateBibActionTypeService extends ActionTypeServiceBase {
    private CreateBibAction createBibAction;
    private UpdateItemAction updateItemAction;
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

    /**
     *    Gets the createBibAction attribute.
     * @return  createBibAction.
     */
    public CreateBibAction getCreateBibAction() {
        if (null == createBibAction) {
            createBibAction = new CreateBibAction();
        }
        return createBibAction;
    }

    /**
     *Sets the createBibAction attribute value.
     * @param createBibAction . The createBibAction to set.
     */
    public void setCreateBibAction(CreateBibAction createBibAction) {
        this.createBibAction = createBibAction;
    }

    /**
     *  Gets the updateItemAction attribute.
     * @return  updateItemAction.
     */
    public UpdateItemAction getUpdateItemAction() {
        if (null == updateItemAction) {
            updateItemAction = new UpdateItemAction();
        }
        return updateItemAction;
    }

    /**
     *Sets the updateItemAction attribute value.
     * @param updateItemAction . The updateItemAction to set
     */
    public void setUpdateItemAction(UpdateItemAction updateItemAction) {
        this.updateItemAction = updateItemAction;
    }

    /**
     *  Gets the overlayAction attribute.
     * @return  overlayAction.
     */
    public OverlayAction getOverlayAction() {
        if (null == overlayAction) {
            overlayAction = new OverlayAction();
        }
        return overlayAction;
    }

    /**
     *Sets the overlayAction attribute value.
     * @param overlayAction . The overlayAction to set
     */
    public void setUpdateItemAction(OverlayAction overlayAction) {
        this.overlayAction = overlayAction;
    }
}
