package org.kuali.ole.ingest.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/2/13
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayField implements Serializable {

    private List<String> field;

    public List<String> getField() {
        return field;
    }

    public void setField(List<String> field) {
        this.field = field;
    }
}
