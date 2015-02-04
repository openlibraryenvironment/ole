package org.kuali.ole.krad;

import java.util.List;

import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.container.PageGroup;

public class OlePageGroup extends PageGroup {

	private static final long serialVersionUID = 8060450970054897968L;

	/**
	 * Filters {@link OleComponent} instances based on
	 * {@link OleComponent#getFilterModelProperty()}, if the current model is
	 * available.
	 */
	@Override
	protected <T> void copyProperties(T component) {
		List<? extends Component> srcitems = getItems();
		
		// prevent super() from copying items
		setItems(null);
		super.copyProperties(component);
		setItems(srcitems);
		
		Group groupCopy = (Group) component;
		groupCopy.setItems(OleComponentUtils.filterItems(srcitems));
	}

}

