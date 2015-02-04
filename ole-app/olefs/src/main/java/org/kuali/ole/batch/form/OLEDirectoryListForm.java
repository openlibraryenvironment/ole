package org.kuali.ole.batch.form;

import org.kuali.ole.batch.bo.OLEListFileLocation;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 6/11/14.
 */
public class OLEDirectoryListForm extends UifFormBase {
    private List<OLEListFileLocation> oleListFileLocationList;

    public List<OLEListFileLocation> getOleListFileLocationList() {
        return oleListFileLocationList;
    }

    public void setOleListFileLocationList(List<OLEListFileLocation> oleListFileLocationList) {
        this.oleListFileLocationList = oleListFileLocationList;
    }
}
