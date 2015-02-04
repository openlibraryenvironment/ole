package org.kuali.ole.krad;

import org.kuali.rice.krad.uif.component.Component;

/**
 * Defines additional component properties for supporting KRAD pages in OLE.
 */
public interface OleComponent extends Component {

	/**
	 * Gets a form property name, which if resolves to {@code true} on the form,
	 * will result in this component being omitted from copy and lifecycle
	 * operations.
	 * 
	 * @return form property name, or null for no property-based filtering
	 */
	String getFilterModelProperty();

}

