package org.kuali.ole.krad;

import org.kuali.rice.krad.uif.container.CollectionGroup;

public class OleCollectionGroup extends CollectionGroup implements OleComponent {

	private static final long serialVersionUID = -1162541596613913679L;
	
	private String filterModelProperty;

	@Override
	public String getFilterModelProperty() {
		return filterModelProperty;
	}

	public void setFilterModelProperty(String filterModelProperty) {
		this.filterModelProperty = filterModelProperty;
	}

}

