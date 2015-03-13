package org.kuali.ole.loaders.describe;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.loaders.common.FileUtils;
import org.kuali.ole.loaders.common.RestClient;
import org.kuali.ole.loaders.common.service.OLELoaderRestClient;

import java.util.Map;

/**
 * Created by sheiksalahudeenm on 3/5/15.
 */

public class OLELocationTest{

    private static final Logger LOG = Logger.getLogger(OLELocationTest.class);
    private RestClient restClient = new RestClient();
    public String BASE_URL = "http://192.168.55.223:8080/olefs/api/";


    /* --------- Location Services Start ----------- */
    @Test
    public void getLocation() throws Exception {
        LOG.info("Sending request to OLE Rest service to fetch location");
        Map<String,Object> responseMap = OLELoaderRestClient.jerseryClientGet(BASE_URL + "location/967");
        System.out.println("Response Status : \n" + responseMap.get("status"));
        System.out.println("Response Body : \n" + responseMap.get("content"));
    }

    @Test
    public void importLocation() throws Exception {
        String locationInfo = FileUtils.readFileContent("org/kuali/ole/loaders/describe/importLocation.json");
        Map<String,Object> responseMap = OLELoaderRestClient.jerseryClientPost(BASE_URL + "/location", locationInfo);
        System.out.println("Response Status : \n" + responseMap.get("status"));
        Map headerMap = (Map) responseMap.get("header");
        System.out.println("Response Rejected Items : \n" + headerMap.get("X-OLE-Rejected"));
        Assert.assertTrue(StringUtils.isNotBlank((String)responseMap.get("content")));
        LOG.info((String)responseMap.get("content"));
    }

    @Test
    public void updateLocation() throws Exception {
        String locationInfo = FileUtils.readFileContent("org/kuali/ole/loaders/describe/updateLocation.json");
        Map<String,Object> responseMap = OLELoaderRestClient.jerseryClientPut(BASE_URL + "/location/921", locationInfo);
        System.out.println("Response Status : \n" + responseMap.get("status"));
        Assert.assertTrue(StringUtils.isNotBlank((String)responseMap.get("content")));
        LOG.info((String)responseMap.get("content"));
    }

    @Test
    public void getLocationLevel() throws Exception {
        LOG.info("Sending request to OLE Rest service to fetch location");
        Map<String,Object> responseMap = OLELoaderRestClient.jerseryClientGet(BASE_URL + "locationLevel/2");
        System.out.println("Response Status : \n" + responseMap.get("status"));
        Assert.assertTrue(StringUtils.isNotBlank((String)responseMap.get("content")));
        LOG.info((String) responseMap.get("content"));
    }

/*    @Test
    public void importLocationLevel() throws Exception {
        String locationInfo = FileUtils.readFileContent("org/kuali/ole/loaders/describe/importLocationLevel.json");
        Map<String,Object> responseMap = OLELoaderRestClient.jerseryClientPost(BASE_URL + "/locationLevel", locationInfo);
        System.out.println("Response Status : \n" + responseMap.get("status"));
        Assert.assertTrue(StringUtils.isNotBlank((String)responseMap.get("content")));
        LOG.info((String)responseMap.get("content"));
    }

    @Test
    public void updateLocationLevel() throws Exception {
        String locationInfo = FileUtils.readFileContent("org/kuali/ole/loaders/describe/updateLocationLevel.json");
        String response = RestClient.jerseryClientPut(BASE_URL + "/locationLevel/2", locationInfo);
        Assert.assertTrue(StringUtils.isNotBlank(response));
        LOG.info(response);
    }*/

    /* --------- Location Services End ----------- */
}
