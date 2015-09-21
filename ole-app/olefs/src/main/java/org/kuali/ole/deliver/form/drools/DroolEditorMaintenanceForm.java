package org.kuali.ole.deliver.form.drools;

import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;

/**
 * Created by sheiksalahudeenm on 7/10/15.
 */
public class DroolEditorMaintenanceForm extends MaintenanceDocumentForm {
    private boolean showGeneralCheckSection;
    private boolean showCheckoutSection;
    private boolean showRenewCheckSection;
    private boolean showNoticeSection;
    private boolean showCheckinSection;
    private boolean showFooterSection;
    private boolean disableAddEditor;
    private boolean editSection;

    public boolean isShowGeneralCheckSection() {
        return showGeneralCheckSection;
    }

    public void setShowGeneralCheckSection(boolean showGeneralCheckSection) {
        this.showGeneralCheckSection = showGeneralCheckSection;
    }

    public boolean isShowCheckoutSection() {
        return showCheckoutSection;
    }

    public void setShowCheckoutSection(boolean showCheckoutSection) {
        this.showCheckoutSection = showCheckoutSection;
    }

    public boolean isShowRenewCheckSection() {
        return showRenewCheckSection;
    }

    public void setShowRenewCheckSection(boolean showRenewCheckSection) {
        this.showRenewCheckSection = showRenewCheckSection;
    }

    public boolean isShowNoticeSection() {
        return showNoticeSection;
    }

    public void setShowNoticeSection(boolean showNoticeSection) {
        this.showNoticeSection = showNoticeSection;
    }

    public boolean isShowCheckinSection() {
        return showCheckinSection;
    }

    public void setShowCheckinSection(boolean showCheckinSection) {
        this.showCheckinSection = showCheckinSection;
    }

    public void resetFlagValues(){
        showGeneralCheckSection = false;
        showCheckoutSection = false;
        showRenewCheckSection = false;
        showNoticeSection = false;
        showCheckinSection = false;
        showFooterSection = false;
    }

    public boolean isShowFooterSection() {
        return showFooterSection;
    }

    public void setShowFooterSection(boolean showFooterSection) {
        this.showFooterSection = showFooterSection;
    }

    public boolean isDisableAddEditor() {
        return disableAddEditor;
    }

    public void setDisableAddEditor(boolean disableAddEditor) {
        this.disableAddEditor = disableAddEditor;
    }

    public boolean isEditSection() {
        return editSection;
    }

    public void setEditSection(boolean editSection) {
        this.editSection = editSection;
    }
}
