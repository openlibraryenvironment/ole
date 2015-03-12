package org.kuali.ole.loaders.describe;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.loaders.common.FileUtils;
import org.kuali.ole.loaders.common.RestClient;

/**
 * Created by sheiksalahudeenm on 3/5/15.
 */

public class OLEItemTypeTest {

    private static final Logger LOG = Logger.getLogger(OLEItemTypeTest.class);
    private RestClient restClient = new RestClient();
    public String BASE_URL = "http://192.168.55.223:8080/olefs/api/";


    @Test
    public void getLocation() throws Exception {
        LOG.info("Sending request to OLE Rest service to fetch location");
        String response = RestClient.jerseryClientGet(BASE_URL + "location/967");
        System.out.println("Response : \n" + response);

    }

    @Test
    public void getAllLocation() throws Exception {
        LOG.info("Sending request to OLE Rest service to fetch location");
        String response = RestClient.jerseryClientGet(BASE_URL + "location");
        System.out.println("Response : \n" + response);

    }

    @Test
    public void importLocation() throws Exception {
        String locationInfo = FileUtils.readFileContent("org/kuali/ole/loaders/describe/importLocation.json");
        String response = RestClient.jerseryClientPost(BASE_URL + "/location", locationInfo);
        Assert.assertTrue(StringUtils.isNotBlank(response));
        LOG.info(response);

    }

    @Test
    public void updateLocation() throws Exception {
        String locationInfo = FileUtils.readFileContent("org/kuali/ole/loaders/describe/importLocation.json");
        String response = RestClient.jerseryClientPut(BASE_URL + "/location/967", locationInfo);
        Assert.assertTrue(StringUtils.isNotBlank(response));
        LOG.info(response);

    }

    @Test
    public void getLocationLevel() throws Exception {
        LOG.info("Sending request to OLE Rest service to fetch location");
        String response = RestClient.jerseryClientGet(BASE_URL + "locationLevel/2");
        System.out.println("Response : \n" + response);

    }

    @Test
    public void importLocationLevel() throws Exception {
        String locationInfo = FileUtils.readFileContent("org/kuali/ole/loaders/describe/importLocationLevel.json");
        String response = RestClient.jerseryClientPost(BASE_URL + "/locationLevel", locationInfo);
        Assert.assertTrue(StringUtils.isNotBlank(response));
        LOG.info(response);

    }
}
