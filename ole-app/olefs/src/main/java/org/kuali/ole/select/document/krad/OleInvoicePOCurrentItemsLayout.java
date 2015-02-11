package org.kuali.ole.select.document.krad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kuali.ole.module.purap.businessobject.options.DiscountTypeValuesFinder;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.form.OLEInvoiceForm;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifPropertyPaths;
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

public class OleInvoicePOCurrentItemsLayout extends LayoutManagerBase implements
		CollectionLayoutManager {

	private static final long serialVersionUID = 5289870490845303915L;
	private static final Logger LOG = Logger
			.getLogger(OleInvoicePOCurrentItemsLayout.class);

	@SuppressWarnings("unchecked")
	private static final List<KeyValue> DISCOUNT_KEY_VALUES = new DiscountTypeValuesFinder()
			.getKeyValues();

	private List<OleInvoicePOCurrentItemsLine> currentItemLines = new ArrayList<OleInvoicePOCurrentItemsLine>();
	private Group rowDetailsGroup;
	private boolean foreignCurrency;

	private List<Component> components = new ArrayList<Component>();

	@Override
	public void performApplyModel(View view, Object model, Container container) {
		if (LOG.isDebugEnabled())
			LOG.debug(container
					+ " ro is "
					+ container.isReadOnly()
					+ " "
					+ ((CollectionGroup) container).isRenderAddLine()
					+ " "
					+ ((CollectionGroup) container)
							.isRenderAddBlankLineButton());

		foreignCurrency = ((OleInvoiceDocument) ((OLEInvoiceForm) model)
				.getDocument()).isForeignCurrencyFlag();

		super.performApplyModel(view, model, container);
	}

	private Group getRowDetails(OLEInvoiceForm form, String idSuffix,
			int lineIndex, CollectionGroup collection, View view,
			String bindingPath, OlePurchaseOrderItem item) {
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
	public void buildLine(View view, Object model,
			CollectionGroup collectionGroup, List<Field> lineFields,
			List<FieldGroup> subCollectionFields, String bindingPath,
			List<Action> actions, String idSuffix, Object currentLine,
			int lineIndex) {
		OlePurchaseOrderItem poItem = (OlePurchaseOrderItem) currentLine;
		OleInvoicePOCurrentItemsLine line = new OleInvoicePOCurrentItemsLine();
		line.setLineId(collectionGroup.getBaseId() + idSuffix);
		line.setBindPath(bindingPath
				.startsWith(UifPropertyPaths.NEW_COLLECTION_LINES) ? bindingPath
				: view.getDefaultBindingObjectPath() + '.' + bindingPath);
		line.setPoItem(poItem);
		line.setRowDetails(getRowDetails((OLEInvoiceForm) model, idSuffix,
				lineIndex, collectionGroup, view, bindingPath, poItem));

		synchronized (currentItemLines) {
			currentItemLines.add(line);
		}
	}

	@Override
	public FieldGroup getSubCollectionFieldGroupPrototype() {
		return null;
	}

	public List<KeyValue> getDiscountKeyValues() {
		return DISCOUNT_KEY_VALUES;
	}

	public List<OleInvoicePOCurrentItemsLine> getCurrentItemLines() {
		return currentItemLines;
	}

	public void setCurrentItemLines(
			List<OleInvoicePOCurrentItemsLine> currentItemLines) {
		this.currentItemLines = currentItemLines;
	}

	public boolean isForeignCurrency() {
		return foreignCurrency;
	}

	public void setForeignCurrency(boolean foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}

	public Group getRowDetailsGroup() {
		return rowDetailsGroup;
	}

	public void setRowDetailsGroup(Group rowDetailsGroup) {
		this.rowDetailsGroup = rowDetailsGroup;
	}

	@Override
	public List<Component> getComponentPrototypes() {
		List<Component> rv = super.getComponentPrototypes();
		rv.add(rowDetailsGroup);
		return rv;
	}

	@Override
	public List<Component> getComponentsForLifecycle() {
		List<Component> rv = super.getComponentsForLifecycle();
		if (components != null)
			rv.addAll(components);
		return rv;
	}

	@Override
	protected <T> void copyProperties(T copy) {
		super.copyProperties(copy);

		OleInvoicePOCurrentItemsLayout c = (OleInvoicePOCurrentItemsLayout) copy;
		if (rowDetailsGroup != null)
			c.setRowDetailsGroup(ComponentUtils.copy(rowDetailsGroup));

	}

}

