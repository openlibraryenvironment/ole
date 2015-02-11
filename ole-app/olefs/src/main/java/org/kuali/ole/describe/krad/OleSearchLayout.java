package org.kuali.ole.describe.krad;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kuali.ole.describe.bo.SearchResultDisplayFields;
import org.kuali.ole.describe.bo.SearchResultDisplayRow;
import org.kuali.ole.describe.form.OLESearchForm;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
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
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.Pager;

public class OleSearchLayout extends LayoutManagerBase implements
		CollectionLayoutManager {

	private static final long serialVersionUID = 5289870490845303915L;
	private static final Logger LOG = Logger.getLogger(OleSearchLayout.class);

	private List<OleSearchLine> searchLines = new ArrayList<OleSearchLine>();
	private int totalLines;
	private int displayedLines;

	private List<KeyValue> pageSizeOptions;
	private int pageSize;

	private SearchResultDisplayFields searchResultDisplayFields;

	private Pager pager;

	private void setUpPager(OLESearchForm form, CollectionGroup collection) {
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
		OLESearchForm form = (OLESearchForm) model;
		CollectionGroup collection = (CollectionGroup) container;

		searchResultDisplayFields = form.getSearchResultDisplayFields();

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

		SearchResultDisplayRow row = (SearchResultDisplayRow) currentLine;

		BindingInfo bi = new BindingInfo();
		bi.setBindByNamePrefix(bindingPath);

		OleSearchLine line = new OleSearchLine();
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

	public SearchResultDisplayFields getSearchResultDisplayFields() {
		return searchResultDisplayFields;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<KeyValue> getPageSizeOptions() {
		return pageSizeOptions;
	}

	public int getTotalLines() {
		return totalLines;
	}

	public int getDisplayedLines() {
		return displayedLines;
	}

	public List<OleSearchLine> getSearchLines() {
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

		OleSearchLayout c = (OleSearchLayout) copy;
		if (pager != null)
			c.setPager(ComponentUtils.copy(pager));
	}

}

