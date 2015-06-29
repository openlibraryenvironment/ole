package org.kuali.ole.describe.krad;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OLEBibSearchResultDisplayRow;
import org.kuali.ole.deliver.bo.OLEHoldingsSearchResultDisplayRow;
import org.kuali.ole.deliver.bo.OLEItemSearchResultDisplayRow;
import org.kuali.ole.deliver.form.OLEDeliverItemSearchForm;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.field.FieldGroup;
import org.kuali.rice.krad.uif.layout.CollectionLayoutManager;
import org.kuali.rice.krad.uif.layout.LayoutManagerBase;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.Pager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jayabharathreddy on 6/25/15.
 */
public class OleDeliverSearchLayout extends LayoutManagerBase implements
        CollectionLayoutManager {

    private static final long serialVersionUID = 5289870490845303915L;
    private static final Logger LOG = Logger.getLogger(OleDeliverSearchLayout.class);

    private List<OleItemSearchLines> searchLines = new ArrayList<>();
    private int totalLines;
    private int displayedLines;
    private int pageSize;
    private Pager pager;
    private List<KeyValue> pageSizeOptions;
    private Group rowDetailsGroup;
    private List<Component> components = new ArrayList<>();

    private void setUpPager(OLEDeliverItemSearchForm form, CollectionGroup collection) {
        pageSize = form.getPageSize();
        if (pager == null)
            return;

        if (pageSize == 0) {
            pager.setNumberOfPages(0);
            return;
        }

        totalLines = form.getTotalRecordCount();

        int lastPage = totalLines / pageSize
                + (totalLines % pageSize == 0 ? 0 : 1);

        SearchParams params = form.getSearchParams();
        int currentPage = ((params == null ? 0 : params.getStartIndex()) / pageSize) + 1;
        String pageNumber = form.getPageNumber();

        if (LOG.isDebugEnabled())
            LOG.debug("Setting up pager with page size " + pageSize
                    + ", total lines " + totalLines + ", last page " + lastPage
                    + ", page number " + pageNumber + ", current page "
                    + currentPage);

        pager.setCurrentPage(currentPage);
        pager.setNumberOfPages(lastPage);
    }

    @Override
    public void performApplyModel(View view, Object model, Container container) {
        OLEDeliverItemSearchForm form = (OLEDeliverItemSearchForm) model;
        CollectionGroup collection = (CollectionGroup) container;
        List<Integer> pageSizes = DocumentSearchConfig.getPageSizes();
        List<KeyValue> pso = new ArrayList<>(pageSizes.size() + 1);
        for (Integer ps : pageSizes) {
            String pss = Integer.toString(ps);
            pso.add(new ConcreteKeyValue(pss, pss));
        }
        pageSizeOptions = pso;
        setUpPager(form, collection);
        super.performApplyModel(view, model, container);
    }

    @Override
    public void buildLine(View view, Object model,
                          CollectionGroup collectionGroup, List<Field> lineFields,
                          List<FieldGroup> subCollectionFields, String bindingPath,
                          List<Action> actions, String idSuffix, Object currentLine,
                          int lineIndex) {

        OLEBibSearchResultDisplayRow row = (OLEBibSearchResultDisplayRow) currentLine;

        BindingInfo bi = new BindingInfo();
        bi.setBindByNamePrefix(bindingPath);

        OleItemSearchLines line = new OleItemSearchLines();

        line.setLineNumber(lineIndex);
        line.setLineId(collectionGroup.getBaseId() + idSuffix);
        line.setBindPath(bi.getBindByNamePrefix());
        line.setDeliverRow(row);
        line.setRowDetails(getRowDetails((OLEDeliverItemSearchForm) model, idSuffix,
                lineIndex, collectionGroup, view, bindingPath, row));

        synchronized (searchLines) {
            searchLines.add(line);
        }

        if (LOG.isDebugEnabled())
            LOG.debug("SEARCH LINE: " + line);

        for (OLEHoldingsSearchResultDisplayRow oleHoldingsSearchResultDisplayRow : row.getOleHoldingsSearchResultDisplayRowList()) {
            displayedLines = displayedLines + oleHoldingsSearchResultDisplayRow.getOleItemSearchResultDisplayRowList().size();
        }

    }


    private Group getRowDetails(OLEDeliverItemSearchForm form, String idSuffix,
                                int lineIndex, CollectionGroup collection, View view,
                                String bindingPath, OLEBibSearchResultDisplayRow item) {
        String lineId = collection.getBaseId() + idSuffix;
        String selectedRowDetails = form.getSelectRowDetails();
        String extensionKey = lineId + ".rowDetails";
        Map<String, Object> extension = form.getExtensionData();
        if (lineId.equals(selectedRowDetails))
            synchronized (extension) {
                if (form.isShowSelectedRowDetails())
                    extension.put(extensionKey, true);
                else
                    extension.remove(extensionKey);
            }

        if (!Boolean.TRUE.equals(extension.get(extensionKey))) {
            if (LOG.isDebugEnabled())
                LOG.debug("Omitting row details " + extensionKey + ", "
                        + form.getSelectRowDetails() + " "
                        + form.isShowSelectedRowDetails());
            return null;
        }

        if (LOG.isDebugEnabled())
            LOG.debug("Including row details " + extensionKey + ", "
                    + form.getSelectRowDetails() + " "
                    + form.isShowSelectedRowDetails());

        Group rowDetails = ComponentUtils.copy(rowDetailsGroup, idSuffix);
        rowDetails.setFieldBindByNamePrefix(bindingPath);
        ComponentUtils.updateContextForLine(rowDetails, item, lineIndex,
                idSuffix);
        view.getViewHelperService().spawnSubLifecyle(view, form, rowDetails,
                collection, null, UifConstants.ViewPhases.INITIALIZE);

        synchronized (components) {
            components.add(rowDetails);
        }

        return rowDetails;
    }

    @Override
    public FieldGroup getSubCollectionFieldGroupPrototype() {
        return null;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalLines() {
        return totalLines;
    }

    public int getDisplayedLines() {
        return displayedLines;
    }

    public List<OleItemSearchLines> getSearchLines() {
        return searchLines;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<KeyValue> getPageSizeOptions() {
        return pageSizeOptions;
    }

    public Group getRowDetailsGroup() {
        return rowDetailsGroup;
    }

    public void setRowDetailsGroup(Group rowDetailsGroup) {
        this.rowDetailsGroup = rowDetailsGroup;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> rv = super.getComponentsForLifecycle();
        if (components != null)
            rv.addAll(components);
        if (pager != null)
            rv.add(pager);
        return rv;
    }

    @Override
    protected <T> void copyProperties(T copy) {
        super.copyProperties(copy);
        OleDeliverSearchLayout c = (OleDeliverSearchLayout) copy;
        if (rowDetailsGroup != null)
            c.setRowDetailsGroup(ComponentUtils.copy(rowDetailsGroup));
        if (pager != null)
            c.setPager(ComponentUtils.copy(pager));
    }

    @Override
    public List<Component> getComponentPrototypes() {
        List<Component> rv = super.getComponentPrototypes();
        rv.add(rowDetailsGroup);
        return rv;
    }

}

