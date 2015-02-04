package org.kuali.ole.systemintegration.rest.Controller;

import org.kuali.ole.systemintegration.rest.RestConstants;
import org.kuali.ole.systemintegration.rest.service.DocstoreDataRetrieveService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 4/22/14
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/holdings")
public class InstanceDetailsRestController  {

    @RequestMapping(value = "/tree", method = RequestMethod.GET, headers="Accept=application/json"  )
    @ResponseBody
    public String retrieveHoldingsTreeApplicationJson(@RequestParam(RestConstants.BIB_ID) String []bibId) {
        String instanceResponse = "";
        List<String> bibIdList = new ArrayList<>();
        if(bibId != null){
            bibIdList = Arrays.asList(bibId);
        }
        DocstoreDataRetrieveService oleDocstoreDataRetrieveService = new DocstoreDataRetrieveService();
        instanceResponse = oleDocstoreDataRetrieveService.getHoldingsTree(bibIdList, RestConstants.JSON);
        return instanceResponse;
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET, headers="Accept=application/xml"  )
    @ResponseBody
    public String retrieveHoldingsTreeApplicationXml(@RequestParam(RestConstants.BIB_ID) String []bibId) {
        String instanceResponse = "";
        List<String> bibIdList = new ArrayList<>();
        if(bibId != null){
            bibIdList = Arrays.asList(bibId);
        }
        DocstoreDataRetrieveService oleDocstoreDataRetrieveService = new DocstoreDataRetrieveService();
        instanceResponse = oleDocstoreDataRetrieveService.getHoldingsTree(bibIdList, RestConstants.XML);
        return instanceResponse;
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET, headers="Accept=text/xml"  )
    @ResponseBody
    public String retrieveHoldingsTreeApplicationText(@RequestParam(RestConstants.BIB_ID) String []bibId) {
        String instanceResponse = "";
        List<String> bibIdList = new ArrayList<>();
        if(bibId != null){
            bibIdList = Arrays.asList(bibId);
        }
        DocstoreDataRetrieveService oleDocstoreDataRetrieveService = new DocstoreDataRetrieveService();
        instanceResponse = oleDocstoreDataRetrieveService.getHoldingsTree(bibIdList, RestConstants.XML);
        return instanceResponse;
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public String retrieveHoldingsTreeApplication(@RequestParam(RestConstants.BIB_ID) String []bibId) {
        String instanceResponse = "";
        List<String> bibIdList = new ArrayList<>();
        if(bibId != null){
            bibIdList = Arrays.asList(bibId);
        }
        DocstoreDataRetrieveService oleDocstoreDataRetrieveService = new DocstoreDataRetrieveService();
        instanceResponse = oleDocstoreDataRetrieveService.getHoldingsTree(bibIdList, RestConstants.XML);
        return instanceResponse;
    }
}
