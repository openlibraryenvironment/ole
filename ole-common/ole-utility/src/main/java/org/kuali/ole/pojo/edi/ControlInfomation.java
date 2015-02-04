package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ControlInfomation {
    // private Control control;
    private List<Control> control = new ArrayList<Control>();

    public void addControlField(Control control) {
        if (!this.control.contains(control)) {
            this.control.add(control);
        }
    }

    public List<Control> getControl() {
        return control;
    }

    public void setControl(List<Control> control) {
        this.control = control;
    }
}
