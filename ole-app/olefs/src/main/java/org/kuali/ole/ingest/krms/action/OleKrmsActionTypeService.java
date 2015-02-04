package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.action.*;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleKrmsActionTypeService extends ActionTypeServiceBase {

    private CreateBibAction createBibAction;
    private UpdateItemAction updateItemAction;
    private UpdateBibExcludingGPFieldAction updateBibExcludingGPFieldAction;
    private OverlayAction overlayAction;

    /**
     *      This method invokes appropriate Bib action based on action definition's name
     * @param actionDefinition
     * @return Action
     */
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter = null;
        if(actionDefinition.getName().equals(OLEConstants.CHECKOUT)){
            parameter= actionDefinition.getAttributes().get(OLEConstants.CHECKOUT);
            return  new OleCheckOutAction(parameter);
        }  else if(actionDefinition.getName().equals(OLEConstants.NOTICE_TYPE)){
            parameter= actionDefinition.getAttributes().get(OLEConstants.NOTICE_TYPE);
            return  new OleUpdateNoticeType(parameter);
        } else if (actionDefinition.getName().equals("createBibAction")) {
            return getCreateBibAction();
        } else if (actionDefinition.getName().equals("updateItemAction")){
            return getUpdateItemAction();
        } else if (actionDefinition.getName().equals("updateBibExcludingGPFieldAction")){
            return getUpdateBibExcludingGPFieldAction();
        } else if(actionDefinition.getName().equals("overlayAction")){
            return getOverlayAction();
        }
        return new OleCheckOutAction(parameter);
    }

    /**
     *    Gets the overlayAction attribute.
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
     * @param overlayAction . The createBibAction to set.
     */
    public void setOverlayAction(OverlayAction overlayAction) {
        this.overlayAction = overlayAction;
    }

    /**
     *    Gets the updateBibExcludingGPFieldAction attribute.
     * @return  updateBibExcludingGPFieldAction.
     */
    public UpdateBibExcludingGPFieldAction getUpdateBibExcludingGPFieldAction() {
        if (null == updateBibExcludingGPFieldAction) {
            updateBibExcludingGPFieldAction = new UpdateBibExcludingGPFieldAction();
        }
        return updateBibExcludingGPFieldAction;
    }

    /**
     *Sets the updateBibExcludingGPFieldAction attribute value.
     * @param updateBibExcludingGPFieldAction . The createBibAction to set.
     */
    public void setUpdateBibExcludingGPFieldAction(UpdateBibExcludingGPFieldAction updateBibExcludingGPFieldAction) {
        this.updateBibExcludingGPFieldAction = updateBibExcludingGPFieldAction;
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

}
