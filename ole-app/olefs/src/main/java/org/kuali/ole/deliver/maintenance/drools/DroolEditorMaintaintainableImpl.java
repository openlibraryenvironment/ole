package org.kuali.ole.deliver.maintenance.drools;

import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.Collection;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 7/7/15.
 */
public class DroolEditorMaintaintainableImpl extends MaintainableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DroolEditorMaintaintainableImpl.class);


    @Override
    public void processCollectionAddLine(View view, Object model, String collectionPath) {
        CollectionGroup collectionGroup = view.getViewIndex().getCollectionGroupByPath(collectionPath);
        if (collectionGroup == null) {
            super.logAndThrowRuntime("Unable to get collection group component for path: " + collectionPath);
        }

        // get the collection instance for adding the new line
        Collection<Object> collection = ObjectPropertyUtils.getPropertyValue(model, collectionPath);
        if (collection == null) {
            super.logAndThrowRuntime("Unable to get collection property from model for path: " + collectionPath);
        }

        // now get the new line we need to add
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object addLine = ObjectPropertyUtils.getPropertyValue(model, addLinePath);
        if (addLine == null) {
            super.logAndThrowRuntime("Add line instance not found for path: " + addLinePath);
        }

        super.processBeforeAddLine(view, collectionGroup, model, addLine);

        // validate the line to make sure it is ok to add
        boolean isValidLine = super.performAddLineValidation(view, collectionGroup, model, addLine);
        if (isValidLine) {
            // TODO: should check to see if there is an add line method on the
            // collection parent and if so call that instead of just adding to
            // the collection (so that sequence can be set)
            super.addLine(collection, addLine, collectionGroup.getAddLinePlacement().equals(
                    UifConstants.Position.TOP.name()));

            // make a new instance for the add line
            collectionGroup.initializeNewCollectionLine(view, model, collectionGroup, true);
        }

        ((UifFormBase) model).getAddedCollectionItems().add(addLine);

        super.processAfterAddLine(view, collectionGroup, model, addLine, isValidLine);
    }


    public void processCollectionAddLineForFine(View view, Object model, String collectionPath) {
        CollectionGroup collectionGroup = view.getViewIndex().getCollectionGroupByPath(collectionPath);
        if (collectionGroup == null) {
            super.logAndThrowRuntime("Unable to get collection group component for path: " + collectionPath);
        }

        // get the collection instance for adding the new line
        Collection<Object> collection = ObjectPropertyUtils.getPropertyValue(model, collectionPath);
        if (collection == null) {
            super.logAndThrowRuntime("Unable to get collection property from model for path: " + collectionPath);
        }

        // now get the new line we need to add
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        if(addLinePath.startsWith("droolsRuleBos[")){
            addLinePath = "newCollectionLines['document.newMaintainableObject.dataObject." + (addLinePath.replace("[","_")).replace("]","_") + ".finesAndLimitsBoList']";
        }
        Object addLine = ObjectPropertyUtils.getPropertyValue(model, addLinePath);
        if (addLine == null) {
            super.logAndThrowRuntime("Add line instance not found for path: " + addLinePath);
        }

        super.processBeforeAddLine(view, collectionGroup, model, addLine);

        // validate the line to make sure it is ok to add
        boolean isValidLine = super.performAddLineValidation(view, collectionGroup, model, addLine);
        if (isValidLine) {
            // TODO: should check to see if there is an add line method on the
            // collection parent and if so call that instead of just adding to
            // the collection (so that sequence can be set)
            super.addLine(collection, addLine, collectionGroup.getAddLinePlacement().equals(
                    UifConstants.Position.TOP.name()));

            // make a new instance for the add line
            collectionGroup.initializeNewCollectionLine(view, model, collectionGroup, true);
        }

        ((UifFormBase) model).getAddedCollectionItems().add(addLine);

        super.processAfterAddLine(view, collectionGroup, model, addLine, isValidLine);
    }
}
