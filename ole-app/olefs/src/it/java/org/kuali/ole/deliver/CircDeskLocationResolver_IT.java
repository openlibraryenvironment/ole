package org.kuali.ole.deliver;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.docstore.common.document.content.instance.LocationLevel;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;


/**
 * Created by Palanivelrajanb on 4/20/2015.
 */
public class CircDeskLocationResolver_IT extends OLETestCaseBase{

    private final String itemLocation = "BED-STACKS";
    private final String locationLevelName = "Library";
    private CircDeskLocationResolver circDeskLocationResolver;


    @Test
    public void getReplyToEmailTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        String replyToEmail = circDeskLocationResolver.getReplyToEmail(itemLocation);
        assertNotNull(replyToEmail);
    }

    @Test
    public void getCirculationDeskTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleCirculationDesk oleCirculationDesk = circDeskLocationResolver.getCirculationDesk(itemLocation);
        assertNotNull(oleCirculationDesk);
    }

    @Test
    public void getLocationByLocationCodeTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleLocation oleLocation = circDeskLocationResolver.getLocationByLocationCode(itemLocation);
        assertNotNull(oleLocation);
    }

    @Test
    public void getCirculationDeskByLocationIdTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleLocation oleLocation = circDeskLocationResolver.getLocationByLocationCode(itemLocation);
        OleCirculationDesk oleCirculationDesk = circDeskLocationResolver.getCirculationDeskByLocationId(oleLocation.getLocationId());
        assertNotNull(oleCirculationDesk);
    }

    @Test
    public void circulationDeskLocationsTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleCirculationDesk oleCirculationDesk = circDeskLocationResolver.getCirculationDesk(itemLocation);
        String operatorsCirculationLocation = circDeskLocationResolver.circulationDeskLocations(oleCirculationDesk);
        assertNotNull(operatorsCirculationLocation);
    }

    @Test
    public void getOleCirculationDeskTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleCirculationDesk circulationDesk = circDeskLocationResolver.getCirculationDesk(itemLocation);
        OleCirculationDesk oleCirculationDesk = circDeskLocationResolver.getOleCirculationDesk(circulationDesk.getCirculationDeskId());
        assertNotNull(oleCirculationDesk);
    }

    @Test
    public void getLocationLevelByNameTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleLocationLevel oleLocationLevel = circDeskLocationResolver.getLocationLevelByName(locationLevelName);
        assertNotNull(oleLocationLevel);
    }

    @Test
    public void getLocationMapTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        Map location = circDeskLocationResolver.getLocationMap(itemLocation);
        assertNotNull(location);
    }

    @Test
    public void createLocationLevelTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        LocationLevel locationLevel = circDeskLocationResolver.createLocationLevel("B-EDUC/BED-STACKS",new LocationLevel());
        assertNotNull(locationLevel);
    }

    @Test
    public void getCirculationDeskByLocationCodeTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleCirculationDesk oleCirculationDesk = circDeskLocationResolver.getCirculationDeskByLocationCode("BL_EDUC");
        assertNotNull(oleCirculationDesk);
    }

    @Test
    public void getOleCirculationDeskLocationByLocationIdTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleLocation oleLocation = circDeskLocationResolver.getLocationByLocationCode(itemLocation);
        OleCirculationDeskLocation oleCirculationDeskLocation = circDeskLocationResolver.getOleCirculationDeskLocationByLocationId(oleLocation.getLocationId());
        assertNotNull(oleCirculationDeskLocation);
    }

    @Test
    public void getLocationByParentIdAndLocationCodeTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleLocation parentLocation = circDeskLocationResolver.getLocationByLocationCode("B-EDUC");
        OleLocation oleLocation = circDeskLocationResolver.getLocationByParentIdAndLocationCode(parentLocation.getLocationId(), itemLocation);
        assertNotNull(oleLocation);
    }

    @Test
    public void getFullPathLocationByNameTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleLocation oleLocation = circDeskLocationResolver.getLocationByLocationCode(itemLocation);
        String fullPathLocationName = circDeskLocationResolver.getFullPathLocationByName(oleLocation);
        assertNotNull(fullPathLocationName);
    }

    @Test
    public void getFullPathLocationTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        OleLocation oleLocation = circDeskLocationResolver.getLocationByLocationCode(itemLocation);
        String fullLocation = circDeskLocationResolver.getFullPathLocation(oleLocation, circDeskLocationResolver.getLocationLevelIds());
        assertNotNull(fullLocation);
    }

    @Test
    public void getLocationLevelIdsTest() throws Exception{
        circDeskLocationResolver = new CircDeskLocationResolver();
        List locationIdList = circDeskLocationResolver.getLocationLevelIds();
        assertNotNull(locationIdList);
    }
}
