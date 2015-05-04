package org.kuali.ole.deliver.circulation;

import com.ibm.icu.impl.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.ole.deliver.service.OleCirculationDeskDetailServiceImpl;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static junit.framework.Assert.assertFalse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Palanivelrajanb on 5/4/2015.
 */
public class OleCirculationDeskMapping_UT {


    private OleCirculationDeskDetailServiceImpl oleCirculationDeskDetailService;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void clearAllCirulationMappingTest() throws Exception{
        OleCirculationDeskDetail oleCirculationDeskDetailOne = new OleCirculationDeskDetail();
        oleCirculationDeskDetailOne.setDefaultLocation(true);
        oleCirculationDeskDetailOne.setCirculationDeskDetailId("1");
        oleCirculationDeskDetailOne.setAllowedLocation(false);
        oleCirculationDeskDetailOne.setOperatorId("eric");

        OleCirculationDeskDetail oleCirculationDeskDetailtwo = new OleCirculationDeskDetail();
        oleCirculationDeskDetailtwo.setDefaultLocation(false);
        oleCirculationDeskDetailtwo.setCirculationDeskDetailId("2");
        oleCirculationDeskDetailtwo.setAllowedLocation(true);
        oleCirculationDeskDetailtwo.setOperatorId("eric");

        ArrayList<OleCirculationDeskDetail> oleCirculationDeskDetails = new ArrayList<OleCirculationDeskDetail>();
        oleCirculationDeskDetails.add(oleCirculationDeskDetailOne);
        oleCirculationDeskDetails.add(oleCirculationDeskDetailtwo);
        oleCirculationDeskDetailService = new OleCirculationDeskDetailServiceImpl();
        List clearList = oleCirculationDeskDetailService.clearAllCirulationMapping(oleCirculationDeskDetails);

        assertFalse(((OleCirculationDeskDetail) clearList.get(0)).isDefaultLocation());
        assertFalse(((OleCirculationDeskDetail) clearList.get(1)).isAllowedLocation());

    }
}
