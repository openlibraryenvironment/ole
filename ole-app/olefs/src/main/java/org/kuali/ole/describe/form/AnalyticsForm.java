package org.kuali.ole.describe.form;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 3/3/14
 * Time: 8:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnalyticsForm extends BoundwithForm {

    private boolean showSeriesTree = false;
    private boolean showAnalyticsTree = false;
    private List<String> selectedItemsFromTree2;
    private Set<String> selectedItemsList = new HashSet<String>();
    private List<String> selectedItems;

    public boolean isShowSeriesTree() {
        return showSeriesTree;
    }

    public void setShowSeriesTree(boolean showSeriesTree) {
        this.showSeriesTree = showSeriesTree;
    }

    public boolean isShowAnalyticsTree() {
        return showAnalyticsTree;
    }

    public void setShowAnalyticsTree(boolean showAnalyticsTree) {
        this.showAnalyticsTree = showAnalyticsTree;
    }

    public List<String> getSelectedItemsFromTree2() {
        return selectedItemsFromTree2;
    }

    public void setSelectedItemsFromTree2(List<String> selectedItemsFromTree2) {
        this.selectedItemsFromTree2 = selectedItemsFromTree2;
    }

    public Set<String> getSelectedItemsList() {
        return selectedItemsList;
    }

    public void setSelectedItemsList(Set<String> selectedItemsList) {
        this.selectedItemsList = selectedItemsList;
    }

    public List<String> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<String> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
