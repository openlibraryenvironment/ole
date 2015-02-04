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
 * Created by sambasivam on 23/9/14.
 */
public class OLETypeKeyValueFinder extends UifKeyValuesFinderBase {

    @Override
    public List getKeyValues(ViewModel viewModel) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm) viewModel;
        List<OleGokbReview> oleGokbReviews = eResourceChangeDashboardForm.getOleGokbReviewList();
        for(OleGokbReview oleGokbReview : oleGokbReviews){
            String type = oleGokbReview.getType();
            if (StringUtils.isNotEmpty(type))
                keyValues.add(new ConcreteKeyValue(type,type));
        }
        return keyValues;
    }
}
