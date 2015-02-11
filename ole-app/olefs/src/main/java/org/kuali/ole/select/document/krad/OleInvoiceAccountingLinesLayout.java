package org.kuali.ole.select.document.krad;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kuali.ole.coa.businessobject.options.SimpleChartValuesFinder;
import org.kuali.ole.module.purap.businessobject.InvoiceAccount;
import org.kuali.ole.select.businessobject.OlePurchaseOrderAccount;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.form.OLEInvoiceForm;
import org.kuali.rice.core.api.util.KeyValue;
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
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.QuickFinder;

public class OleInvoiceAccountingLinesLayout extends LayoutManagerBase
		implements CollectionLayoutManager {

	private static final long serialVersionUID = 5289870490845303915L;
	private static final Logger LOG = Logger
			.getLogger(OleInvoiceAccountingLinesLayout.class);

	private List<OleInvoiceAccountingLine> accountingLines = new ArrayList<OleInvoiceAccountingLine>();
	private boolean foreignCurrency;
	private List<KeyValue> chartKeyValues;

	private QuickFinder accountNumberQuickfinder;
	private QuickFinder subAccountNumberQuickfinder;
	private QuickFinder financialObjectCodeQuickfinder;
	private QuickFinder financialSubObjectCodeQuickfinder;
	private QuickFinder projectCodeQuickfinder;

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

		chartKeyValues = new SimpleChartValuesFinder().getKeyValues();
		foreignCurrency = ((OleInvoiceDocument) ((OLEInvoiceForm) model)
				.getDocument()).isForeignCurrencyFlag();

		super.performApplyModel(view, model, container);
	}

	private <T extends Component> T createComponent(T prototype, View view,
			Object model, CollectionGroup collectionGroup,
			BindingInfo bindingInfo, String idSuffix, Object currentLine,
			int lineIndex) {

		T rv = ComponentUtils.copy(prototype, idSuffix);

		if (rv instanceof Group)
			((Group) rv).setFieldBindByNamePrefix(bindingInfo
					.getBindByNamePrefix());

		if (rv instanceof QuickFinder) {
			QuickFinder qf = (QuickFinder) rv;
			qf.updateFieldConversions(bindingInfo);
			qf.updateLookupParameters(bindingInfo);
			qf.updateReferencesToRefresh(bindingInfo);
		}

		ComponentUtils.updateContextForLine(rv, currentLine, lineIndex,
				idSuffix);
		view.getViewHelperService().spawnSubLifecyle(view, model, rv,
				collectionGroup, null, UifConstants.ViewPhases.INITIALIZE);

		synchronized (components) {
			components.add(rv);
		}

		return rv;
	}

	@Override
	public void buildLine(View view, Object model,
			CollectionGroup collectionGroup, List<Field> lineFields,
			List<FieldGroup> subCollectionFields, String bindingPath,
			List<Action> actions, String idSuffix, Object currentLine,
			int lineIndex) {

		BindingInfo bi = new BindingInfo();
		bi.setBindByNamePrefix(bindingPath
				.startsWith(UifPropertyPaths.NEW_COLLECTION_LINES) ? bindingPath
				: view.getDefaultBindingObjectPath() + '.' + bindingPath);

		OleInvoiceAccountingLine line = new OleInvoiceAccountingLine();
		line.setLineId(collectionGroup.getId() + idSuffix);
		line.setBindPath(bi.getBindByNamePrefix());

		if (currentLine instanceof OlePurchaseOrderAccount)
			line.setPoAccount((OlePurchaseOrderAccount) currentLine);
		else if (currentLine instanceof InvoiceAccount)
			line.setInvoiceAccount((InvoiceAccount) currentLine);
		else
			throw new UnsupportedOperationException("Unsupported data object "
					+ currentLine.getClass());

		line.setAccountNumberQuickfinder(createComponent(
				accountNumberQuickfinder, view, model, collectionGroup, bi,
				idSuffix, currentLine, lineIndex));
		line.setSubAccountNumberQuickfinder(createComponent(
				subAccountNumberQuickfinder, view, model, collectionGroup, bi,
				idSuffix, currentLine, lineIndex));
		line.setFinancialObjectCodeQuickfinder(createComponent(
				financialObjectCodeQuickfinder, view, model, collectionGroup,
				bi, idSuffix, currentLine, lineIndex));
		line.setFinancialSubObjectCodeQuickfinder(createComponent(
				financialSubObjectCodeQuickfinder, view, model,
				collectionGroup, bi, idSuffix, currentLine, lineIndex));
		line.setProjectCodeQuickfinder(createComponent(projectCodeQuickfinder,
				view, model, collectionGroup, bi, idSuffix, currentLine,
				lineIndex));

		line.setActions(actions);

		synchronized (components) {
			components.addAll(actions);
		}

		synchronized (accountingLines) {
			accountingLines.add(line);
		}
	}

	public List<KeyValue> getChartKeyValues() {
		return chartKeyValues;
	}

	public boolean isForeignCurrency() {
		return foreignCurrency;
	}

	public void setForeignCurrency(boolean foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}

	public QuickFinder getAccountNumberQuickfinder() {
		return accountNumberQuickfinder;
	}

	public void setAccountNumberQuickfinder(QuickFinder accountNumberQuickfinder) {
		this.accountNumberQuickfinder = accountNumberQuickfinder;
	}

	public QuickFinder getSubAccountNumberQuickfinder() {
		return subAccountNumberQuickfinder;
	}

	public void setSubAccountNumberQuickfinder(
			QuickFinder subAccountNumberQuickfinder) {
		this.subAccountNumberQuickfinder = subAccountNumberQuickfinder;
	}

	public QuickFinder getFinancialObjectCodeQuickfinder() {
		return financialObjectCodeQuickfinder;
	}

	public void setFinancialObjectCodeQuickfinder(
			QuickFinder financialObjectCodeQuickfinder) {
		this.financialObjectCodeQuickfinder = financialObjectCodeQuickfinder;
	}

	public QuickFinder getFinancialSubObjectCodeQuickfinder() {
		return financialSubObjectCodeQuickfinder;
	}

	public void setFinancialSubObjectCodeQuickfinder(
			QuickFinder financialSubObjectCodeQuickfinder) {
		this.financialSubObjectCodeQuickfinder = financialSubObjectCodeQuickfinder;
	}

	public QuickFinder getProjectCodeQuickfinder() {
		return projectCodeQuickfinder;
	}

	public void setProjectCodeQuickfinder(QuickFinder projectCodeQuickfinder) {
		this.projectCodeQuickfinder = projectCodeQuickfinder;
	}

	public List<OleInvoiceAccountingLine> getAccountingLines() {
		return accountingLines;
	}

	public void setAccountingLines(
			List<OleInvoiceAccountingLine> accountingLines) {
		this.accountingLines = accountingLines;
	}

	@Override
	public FieldGroup getSubCollectionFieldGroupPrototype() {
		return null;
	}

	@Override
	public List<Component> getComponentPrototypes() {
		List<Component> rv = super.getComponentPrototypes();
		rv.add(accountNumberQuickfinder);
		rv.add(subAccountNumberQuickfinder);
		rv.add(financialObjectCodeQuickfinder);
		rv.add(financialSubObjectCodeQuickfinder);
		rv.add(projectCodeQuickfinder);
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

		OleInvoiceAccountingLinesLayout c = (OleInvoiceAccountingLinesLayout) copy;
		if (accountNumberQuickfinder != null)
			c.setAccountNumberQuickfinder(ComponentUtils
					.copy(accountNumberQuickfinder));
		if (subAccountNumberQuickfinder != null)
			c.setSubAccountNumberQuickfinder(ComponentUtils
					.copy(subAccountNumberQuickfinder));
		if (financialObjectCodeQuickfinder != null)
			c.setFinancialObjectCodeQuickfinder(ComponentUtils
					.copy(financialObjectCodeQuickfinder));
		if (financialSubObjectCodeQuickfinder != null)
			c.setFinancialSubObjectCodeQuickfinder(ComponentUtils
					.copy(financialSubObjectCodeQuickfinder));
		if (projectCodeQuickfinder != null)
			c.setProjectCodeQuickfinder(ComponentUtils
					.copy(projectCodeQuickfinder));

	}

}

