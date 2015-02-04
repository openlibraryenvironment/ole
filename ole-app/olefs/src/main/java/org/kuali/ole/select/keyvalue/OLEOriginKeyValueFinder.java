package org.kuali.ole.select.keyvalue;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.select.bo.OLEEResourceChangeDashBoard;
import org.kuali.ole.select.form.OLEEResourceChangeDashboardForm;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srirams on 23/9/14.
 */
public class OLEOriginKeyValueFinder extends UifKeyValuesFinderBase {

    @Override
    public List getKeyValues(ViewModel viewModel) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        OLEEResourceChangeDashboardForm acquisitionEResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm) viewModel;
        List<OLEEResourceChangeDashBoard> OLEEResourceChangeDashBoards = acquisitionEResourceChangeDashboardForm.getOleeResourceChangeDashBoardList();
        for(OLEEResourceChangeDashBoard changeDashBoard : OLEEResourceChangeDashBoards){
            String origin = changeDashBoard.getOrigin();
            if (StringUtils.isNotEmpty(origin))
                keyValues.add(new ConcreteKeyValue(origin,origin));
        }
        keyValues.add(new ConcreteKeyValue("", ""));
        return keyValues;
    }
}
