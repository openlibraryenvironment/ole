package org.kuali.ole.select.document.krad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kuali.ole.module.purap.businessobject.options.DiscountTypeValuesFinder;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.form.OLEInvoiceForm;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifPropertyPaths;
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
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.Pager;
import org.kuali.rice.krad.uif.widget.QuickFinder;

public class OleInvoiceItemsLayout extends LayoutManagerBase implements
		CollectionLayoutManager {

	private static final long serialVersionUID = 5289870490845303915L;
	private static final Logger LOG = Logger
			.getLogger(OleInvoiceItemsLayout.class);

	@SuppressWarnings("unchecked")
	private static final List<KeyValue> DISCOUNT_KEY_VALUES = new DiscountTypeValuesFinder()
			.getKeyValues();

	private List<OleInvoiceItemsLine> itemLines = new ArrayList<OleInvoiceItemsLine>();
	private int filteredLines;
	private int displayedLines;
	private int totalLines;
	private boolean foreignCurrency;

	private int pageSize;
	private int currentPage;
	private Pager pager;
	private QuickFinder relinkQuickfinder;
	private Group rowDetailsGroup;
	private String actionRefreshId;

	private List<Component> components = new ArrayList<Component>();

	private void setUpPager(OLEInvoiceForm form, CollectionGroup collection) {
		if (pageSize == 0 || pager == null)
			return;

		int total = 0;
		for (OleInvoiceItem item : ObjectPropertyUtils
				.<List<OleInvoiceItem>> getPropertyValue(form, collection
						.getBindingInfo().getBindingPath()))
			if ("Qty".equals(item.getItemType().getItemTypeDescription()))
				total++;
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

	private Group getRowDetails(OLEInvoiceForm form, String idSuffix,
			int lineIndex, CollectionGroup collection, View view,
			String bindingPath, OleInvoiceItem item) {
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
	public void performApplyModel(View view, Object model, Container container) {
		OLEInvoiceForm form = (OLEInvoiceForm) model;
		CollectionGroup collection = (CollectionGroup) container;

		setUpPager(form, collection);

		if (LOG.isDebugEnabled())
			LOG.debug(collection
					+ " ro is "
					+ collection.isReadOnly()
					+ " "
					+ ((CollectionGroup) collection).isRenderAddLine()
					+ " "
					+ ((CollectionGroup) collection)
							.isRenderAddBlankLineButton());

		foreignCurrency = ((OleInvoiceDocument) ((OLEInvoiceForm) model)
				.getDocument()).isForeignCurrencyFlag();

		super.performApplyModel(view, model, container);
	}

	@Override
	public void buildLine(View view, Object model,
			CollectionGroup collectionGroup, List<Field> lineFields,
			List<FieldGroup> subCollectionFields, String bindingPath,
			List<Action> actions, String idSuffix, Object currentLine,
			int lineIndex) {

		OleInvoiceItem item = (OleInvoiceItem) currentLine;
		if (!"Qty".equals(item.getItemType().getItemTypeDescription())) {
			filteredLines++;
			return;
		}

		if (currentPage > 0
				&& (lineIndex < (currentPage - 1) * pageSize || lineIndex >= currentPage
						* pageSize))
			return;

		OLEInvoiceForm form = (OLEInvoiceForm) model;

		BindingInfo bi = new BindingInfo();
		bi.setBindByNamePrefix(bindingPath
				.startsWith(UifPropertyPaths.NEW_COLLECTION_LINES) ? bindingPath
				: view.getDefaultBindingObjectPath() + '.' + bindingPath);

		OleInvoiceItemsLine line = new OleInvoiceItemsLine();
		line.setLineNumber(lineIndex);
		line.setLineId(collectionGroup.getBaseId() + idSuffix);
		line.setBindPath(bi.getBindByNamePrefix());
		line.setItem(item);
		line.setRowDetails(getRowDetails(form, idSuffix, lineIndex,
				collectionGroup, view, bindingPath, item));

		line.setActions(actions);

		WorkflowDocument workflowDocument = form.getDocument()
				.getDocumentHeader().getWorkflowDocument();
		if ((workflowDocument.isInitiated() || workflowDocument.isSaved() || workflowDocument
				.isEnroute())
				&& (item.getItemTitleId() == null || item
						.getTempPurchaseOrderIdentifier() == null)) {
			QuickFinder relinkQuickfinder = ComponentUtils.copy(
					this.relinkQuickfinder, idSuffix);
			relinkQuickfinder.updateFieldConversions(bi);
			relinkQuickfinder.updateLookupParameters(bi);
			relinkQuickfinder.updateReferencesToRefresh(bi);
			ComponentUtils.updateContextForLine(relinkQuickfinder, currentLine,
					lineIndex, idSuffix);
			view.getViewHelperService().spawnSubLifecyle(view, model,
					relinkQuickfinder, collectionGroup, null,
					UifConstants.ViewPhases.INITIALIZE);
			synchronized (components) {
				components.add(relinkQuickfinder);
			}
			line.setRelinkQuickfinder(relinkQuickfinder);
		}

		if (actionRefreshId != null)
			for (Action action : actions) {
				action.setRefreshId(actionRefreshId);
				if (LOG.isDebugEnabled())
					LOG.debug("Action " + action.getId() + " refresh ID = "
							+ action.getRefreshId() + ", jump to "
							+ action.getJumpToIdAfterSubmit());
			}

		synchronized (components) {
			components.addAll(actions);
		}

		synchronized (itemLines) {
			itemLines.add(line);
		}

		displayedLines++;
	}

	public List<KeyValue> getDiscountKeyValues() {
		return DISCOUNT_KEY_VALUES;
	}

	@Override
	public FieldGroup getSubCollectionFieldGroupPrototype() {
		return null;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getFilteredLines() {
		return filteredLines;
	}

	public int getDisplayedLines() {
		return displayedLines;
	}

	public int getTotalLines() {
		return totalLines;
	}

	public List<OleInvoiceItemsLine> getItemLines() {
		return itemLines;
	}

	public boolean isForeignCurrency() {
		return foreignCurrency;
	}

	public void setForeignCurrency(boolean foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}

	public QuickFinder getRelinkQuickfinder() {
		return relinkQuickfinder;
	}

	public void setRelinkQuickfinder(QuickFinder relinkQuickfinder) {
		this.relinkQuickfinder = relinkQuickfinder;
	}

	public Group getRowDetailsGroup() {
		return rowDetailsGroup;
	}

	public void setRowDetailsGroup(Group rowDetailsGroup) {
		this.rowDetailsGroup = rowDetailsGroup;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String getActionRefreshId() {
		return actionRefreshId;
	}

	public void setActionRefreshId(String actionRefreshId) {
		this.actionRefreshId = actionRefreshId;
	}

	@Override
	public List<Component> getComponentPrototypes() {
		List<Component> rv = super.getComponentPrototypes();
		rv.add(rowDetailsGroup);
		rv.add(relinkQuickfinder);
		return rv;
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

		OleInvoiceItemsLayout c = (OleInvoiceItemsLayout) copy;
		c.pageSize = pageSize;
		c.actionRefreshId = actionRefreshId;
		if (rowDetailsGroup != null)
			c.setRowDetailsGroup(ComponentUtils.copy(rowDetailsGroup));
		if (relinkQuickfinder != null)
			c.setRelinkQuickfinder(ComponentUtils.copy(relinkQuickfinder));
		if (pager != null)
			c.setPager(ComponentUtils.copy(pager));
	}

}

