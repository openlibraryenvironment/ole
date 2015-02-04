package org.kuali.ole.select.keyvalue;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.select.bo.OLEEResourceChangeDashBoard;
import org.kuali.ole.select.form.OLEEResourceChangeDashboardForm;
import org.kuali.ole.select.gokb.OleGokbReview;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srirams on 19/9/14.
 */
public class OLEDetailsKeyValueFinder extends UifKeyValuesFinderBase {

    @Override
    public List getKeyValues(ViewModel viewModel) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm) viewModel;
        List<OleGokbReview> gokbReviews = eResourceChangeDashboardForm.getOleGokbReviewList();
        for(OleGokbReview oleGokbReview : gokbReviews){
            String details = oleGokbReview.getDetails();
            if (StringUtils.isNotEmpty(details))
                keyValues.add(new ConcreteKeyValue(details,details));
        }
        return keyValues;
    }
}
