package org.kuali.ole.service;

import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.uif.view.View;

import java.util.List;

/**
 * OlePatronLookupView removes the action links(edit/copy) from the result fields
 */
public class OlePatronLookupView extends LookupView {
    /**
     * This method will remove the action links(edit/copy) from the result fields
     * @param view
     * @param model
     * @param parent
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {

        super.performApplyModel(view, model, parent);
        if (!isSuppressActions() && isShowMaintenanceLinks()) {

          //  ((List<Field>) getResultsGroup().getItems()).remove(getResultsActionsFieldGroup());
            ((List<Field>) getResultsGroup().getItems()).remove(0);
        }

    }
}
