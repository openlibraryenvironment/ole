package org.kuali.ole.krad;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.widget.Disclosure;
import org.kuali.rice.krad.uif.widget.Scrollpane;

public class OleFilteredCopyGroup extends Group implements OleComponent {

	private static final long serialVersionUID = 5455906786085674237L;
	private static final Logger LOG = Logger.getLogger(OleFilteredCopyGroup.class);

	private String filterModelProperty;

	public void setFilterModelProperty(String filterModelProperty) {
		this.filterModelProperty = filterModelProperty;
	}

	@Override
	public String getFilterModelProperty() {
		return filterModelProperty;
	}

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

	@Override
	public List<Component> getComponentsForLifecycle() {
		return super.getComponentsForLifecycle();
	}

	@Override
	public List<Component> getComponentPrototypes() {
		return super.getComponentPrototypes();
	}

}

