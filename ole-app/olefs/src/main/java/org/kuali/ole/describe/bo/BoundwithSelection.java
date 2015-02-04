package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 12/3/12
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class BoundwithSelection extends PersistableBusinessObjectBase
        implements Serializable {
    protected boolean selectTree1;
    protected boolean selectTree2;
    protected boolean boundWithTree;
    private String title;
    private String selectedInstance;

    public boolean isSelectTree1() {
        return selectTree1;
    }

    public void setSelectTree1(boolean selectTree1) {
        this.selectTree1 = selectTree1;
    }

    public boolean isSelectTree2() {
        return selectTree2;
    }

    public void setSelectTree2(boolean selectTree2) {
        this.selectTree2 = selectTree2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isBoundWithTree() {
        return boundWithTree;
    }

    public void setBoundWithTree(boolean boundWithTree) {
        this.boundWithTree = boundWithTree;
    }

    public String getSelectedInstance() {
        return selectedInstance;
    }

    public void setSelectedInstance(String selectedInstance) {
        this.selectedInstance = selectedInstance;
    }
}

