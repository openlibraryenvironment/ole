package org.kuali.ole.describe.krad;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OleProxyPatronDocument;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.field.FieldGroup;
import org.kuali.rice.krad.uif.layout.CollectionLayoutManager;
import org.kuali.rice.krad.uif.layout.LayoutManagerBase;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.Pager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jayabharathreddy on 8/26/15.
 */
public class ProxyPatrenSearchLayout extends LayoutManagerBase implements
        CollectionLayoutManager {

    private static final long serialVersionUID = 5289870490845303915L;
    private static final Logger LOG = Logger.getLogger(OleSearchLayout.class);

    private List<ProxySearchLine> searchLines = new ArrayList<ProxySearchLine>();
    private int totalLines;
    private int displayedLines;


    private int currentPage;
    private int pageSize;
    private List<KeyValue> pageSizeOptions;


    private Pager pager;

    private void setUpPager(CircForm form, CollectionGroup collection) {
        if (pageSize == 0 || pager == null)
            return;

        int total = 0;
        List<OleProxyPatronDocument> oleProxyPatronDocuments = ObjectPropertyUtils.<List<OleProxyPatronDocument>>getPropertyValue(form, collection.getBindingInfo().getBindingPath());
        if (oleProxyPatronDocuments == null)
            return;

        total = oleProxyPatronDocuments.size();


        totalLines = total;

        String extensionKey = collection.getId() + ".currentPage";
        Map<String, Object> extension = form.getExtensionData();
        int lastPage = total / pageSize + (total % pageSize == 0 ? 0 : 1);
        Integer currentPage = (Integer) extension.get(extensionKey);
        if (currentPage == null)
            currentPage = 1;

        String pageNumber = form.getPageNumber();
        if (pageNumber != null)
            switch (pageNumber) {
                case UifConstants.PageRequest.FIRST:
                    currentPage = 1;
                    break;
                case UifConstants.PageRequest.LAST:
                    currentPage = lastPage;
                    break;
                case UifConstants.PageRequest.NEXT:
                    currentPage = Math.min(currentPage + 1, lastPage);
                    break;
                case UifConstants.PageRequest.PREV:
                    currentPage = Math.max(currentPage - 1, 1);
                    break;
                default:
                    try {
                        currentPage = Math.max(1,
                                Math.min(lastPage, Integer.parseInt(pageNumber)));
                    } catch (NumberFormatException e) {
                        LOG.warn("Invalid page number " + form.getPageNumber(), e);
                    }
                    break;
            }
        form.setPageNumber(null);

        pager.setCurrentPage(currentPage);
        pager.setNumberOfPages(lastPage);
        this.currentPage = currentPage;

        synchronized (extension) {
            extension.put(extensionKey, currentPage);
        }
    }

    @Override
    public void performApplyModel(View view, Object model, Container container) {
        CircForm form = (CircForm) model;
        CollectionGroup collection = (CollectionGroup) container;

        List<Integer> pageSizes = DocumentSearchConfig.getPageSizes();
        List<KeyValue> pso = new ArrayList<>(pageSizes.size() + 1);
        pso.add(new ConcreteKeyValue("10", "10"));
        pso.add(new ConcreteKeyValue("25", "25"));
        pso.add(new ConcreteKeyValue("50", "50"));
        pso.add(new ConcreteKeyValue("100", "100"));

        pageSizeOptions = pso;
        if (StringUtils.isNotEmpty(form.getPageSize()))
            pageSize = Integer.parseInt(form.getPageSize());

        setUpPager(form, collection);
        super.performApplyModel(view, model, container);
    }

    @Override
    public void buildLine(View view, Object model,
                          CollectionGroup collectionGroup, List<Field> lineFields,
                          List<FieldGroup> subCollectionFields, String bindingPath,
                          List<Action> actions, String idSuffix, Object currentLine,
                          int lineIndex) {

        OleProxyPatronDocument row = (OleProxyPatronDocument) currentLine;


        if (currentPage > 0
                && (lineIndex < (currentPage - 1) * pageSize || lineIndex >= currentPage
                * pageSize))
            return;

        BindingInfo bi = new BindingInfo();
        bi.setBindByNamePrefix(bindingPath);

        ProxySearchLine line = new ProxySearchLine();
        line.setLineNumber(lineIndex);
        line.setLineId(collectionGroup.getBaseId() + idSuffix);
        line.setBindPath(bi.getBindByNamePrefix());
        line.setRow(row);

        synchronized (searchLines) {
            searchLines.add(line);
        }

        if (LOG.isDebugEnabled())
            LOG.debug("SEARCH LINE: " + line);

        displayedLines++;
    }

    @Override
    public FieldGroup getSubCollectionFieldGroupPrototype() {
        return null;
    }


    public int getPageSize() {
        return pageSize;
    }

    public List<KeyValue> getPageSizeOptions() {
        return pageSizeOptions;
    }

    public void setPageSizeOptions(List<KeyValue> pageSizeOptions) {
        this.pageSizeOptions = pageSizeOptions;
    }

    public int getTotalLines() {
        return totalLines;
    }

    public int getDisplayedLines() {
        return displayedLines;
    }

    public List<ProxySearchLine> getSearchLines() {
        return searchLines;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> rv = super.getComponentsForLifecycle();
        if (pager != null)
            rv.add(pager);
        return rv;
    }

    @Override
    protected <T> void copyProperties(T copy) {
        super.copyProperties(copy);
        ProxyPatrenSearchLayout c = (ProxyPatrenSearchLayout) copy;
        c.pageSize = pageSize;
        if (pager != null)
            c.setPager(ComponentUtils.copy(pager));
    }


}

