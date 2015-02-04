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
 * Created by srirams on 29/9/14.
 */
public class OLETitleKeyValueFinder extends UifKeyValuesFinderBase {

    @Override
    public List getKeyValues(ViewModel viewModel) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        /*OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm) viewModel;
        List<OLEEResourceChangeDashBoard> OLEEResourceChangeDashBoards = eResourceChangeDashboardForm.getOleeResourceChangeDashBoardList();
        for (OLEEResourceChangeDashBoard changeDashBoard : OLEEResourceChangeDashBoards) {
            String title = changeDashBoard.getTitle();
            if (StringUtils.isNotEmpty(title))
                keyValues.add(new ConcreteKeyValue(title, title));
        }*/
        return keyValues;
    }
}
