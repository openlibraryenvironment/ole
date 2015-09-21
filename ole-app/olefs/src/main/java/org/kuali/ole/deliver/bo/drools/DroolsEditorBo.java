package org.kuali.ole.deliver.bo.drools;

import org.apache.commons.lang3.StringUtils;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pvsubrah on 7/7/15.
 */
public class DroolsEditorBo extends PersistableBusinessObjectBase {

    private String editorId;
    private String editorType;
    private String fileName;
    private List<DroolsRuleBo> droolsRuleBos = new ArrayList<>();
    private List<DroolsRuleBo> renewRuleBos = new ArrayList<>();
    private List<DroolsRuleBo> noticesRuleBos = new ArrayList<>();
    private List<DroolsRuleBo> checkoutRuleBos = new ArrayList<>();
    private List<DroolsRuleBo> checkinRuleBos = new ArrayList<>();


    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getEditorType() {
        return editorType;
    }

    public void setEditorType(String editorType) {
        this.editorType = editorType;
    }

    public List<DroolsRuleBo> getDroolsRuleBos() {
        return droolsRuleBos;
    }

    public void setDroolsRuleBos(List<DroolsRuleBo> droolsRuleBos) {
        this.droolsRuleBos = droolsRuleBos;
    }

    @Override
    public List<Collection<PersistableBusinessObject>> buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.add(getDroolsRuleBos());
        managedLists.add(getCheckoutRuleBos());
        managedLists.add(getCheckinRuleBos());
        managedLists.add(getRenewRuleBos());
        managedLists.add(getNoticesRuleBos());
        return managedLists;
    }

    public List<DroolsRuleBo> getRenewRuleBos() {
        return renewRuleBos;
    }

    public void setRenewRuleBos(List<DroolsRuleBo> renewRuleBos) {
        this.renewRuleBos = renewRuleBos;
    }

    public List<DroolsRuleBo> getNoticesRuleBos() {
        return noticesRuleBos;
    }

    public void setNoticesRuleBos(List<DroolsRuleBo> noticesRuleBos) {
        this.noticesRuleBos = noticesRuleBos;
    }

    public List<DroolsRuleBo> getCheckoutRuleBos() {
        return checkoutRuleBos;
    }

    public void setCheckoutRuleBos(List<DroolsRuleBo> checkoutRuleBos) {
        this.checkoutRuleBos = checkoutRuleBos;
    }

    public List<DroolsRuleBo> getCheckinRuleBos() {
        return checkinRuleBos;
    }

    public void setCheckinRuleBos(List<DroolsRuleBo> checkinRuleBos) {
        this.checkinRuleBos = checkinRuleBos;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}