package org.kuali.ole.krad;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.container.PageGroup;
import org.kuali.rice.krad.uif.view.FormView;

public class OleFormView extends FormView {

	private static final long serialVersionUID = 153071280080228636L;
	private static final Logger LOG = Logger.getLogger(OleFormView.class);

	private List<? extends Group> originalItems;

	@Override
	public List<Component> getComponentsForLifecycle() {
		List<Component> rv = super.getComponentsForLifecycle();

		if (LOG.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder("Components for lifecycle:");
			sb.append("\n  currentPageId = ").append(getCurrentPageId());
			sb.append("\n  singlePageView? ").append(isSinglePageView());
			if (rv == null)
				sb.append(" NULL!");
			else
				for (Component comp : rv)
					if (comp == null)
						sb.append("\n     NULL!");
					else
						sb.append("\n   - ").append(comp.getClass())
								.append(" ").append(comp.getId());
			sb.append("\nMy items are:");
			List<? extends Component> items = getItems();
			if (items == null)
				sb.append(" NULL!");
			else
				for (Component comp : items)
					if (comp == null)
						sb.append("\n     NULL!");
					else {
						sb.append("\n   - ").append(comp.getClass())
								.append(" ").append(comp.getId());
						if (rv != null)
							sb.append(" -> ").append(rv.contains(comp));
					}
			LOG.debug(sb);
		}

		return rv;
	}

	@Override
	public void setCurrentPageId(String currentPageId) {
		if (originalItems != null)
			setItems(new ArrayList<Group>());
		super.setCurrentPageId(currentPageId);
	}

	private boolean itemsBusy = false;

	@Override
	public List<? extends Group> getItems() {
		synchronized (this) {
			if (itemsBusy)
				return originalItems;
	
			List<? extends Group> rv = super.getItems();
			if ((rv == null || rv.isEmpty()) && originalItems != null) {
				itemsBusy = true;
				try {
					setItems(rv = OleComponentUtils.filterCurrentPage(
							getCurrentPageId(), originalItems));
				} finally {
					itemsBusy = false;
				}
	
				if (LOG.isDebugEnabled()) {
					StringBuilder sb = new StringBuilder("Lazy init items:");
					if (rv == null)
						sb.append(" NULL!");
					else
						for (Group comp : rv)
							if (comp == null)
								sb.append("\n     NULL!");
							else
								sb.append("\n   - ").append(comp.getClass())
										.append(" ").append(comp.getId());
					LOG.debug(sb, new Throwable());
				}
			}
			return rv;
		}
	}

	/**
	 * Filters {@link PageGroup} instances based on the pageId property if an
	 * active form is available.
	 */
	@Override
	protected <T> void copyProperties(T component) {
		List<? extends Group> srcitems;

		synchronized (this) {
			srcitems = getItems();
			try {
				setItems(null);
				super.copyProperties(component);
			} finally {
				setItems(srcitems);
			}
		}

		OleFormView copyView = (OleFormView) component;
		copyView.setItems(new ArrayList<Group>());
		copyView.originalItems = srcitems;
	}

}

