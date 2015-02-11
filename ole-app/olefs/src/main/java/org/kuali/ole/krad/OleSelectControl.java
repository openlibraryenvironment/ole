package org.kuali.ole.krad;

import java.util.List;

import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.control.SelectControl;
import org.kuali.rice.krad.uif.view.View;

public class OleSelectControl extends SelectControl {

	private static final long serialVersionUID = -4788433260315348270L;

	@Override
	public List<KeyValue> getOptions() {
		synchronized (this) {
			return super.getOptions();
		}
	}

	@Override
	public void performApplyModel(View view, Object model, Component parent) {
		synchronized (this) {
			List<KeyValue> options = getOptions();
			try {
				setOptions(null);
				super.performApplyModel(view, model, parent);
			} finally {
				setOptions(options);
			}
		}
	}

}
