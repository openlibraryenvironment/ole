package org.kuali.ole.dsng.rest.controller;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.util.OleDsNGSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;

/**
 * Created by SheikS on 12/3/2015.
 */
public class OleDsNGRestSearchController extends OleDsNGRestAPIController {

    @Autowired
    private OleDsNGSearchUtil oleDsNGSearchUtil;

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.RETRIEVE_BIB_BY_ID, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String retrieveBibById(@RequestBody String body) throws Exception {
        JSONObject jsonObject = new JSONObject(body);
        String query = (String) jsonObject.getString("query");
        BibRecord bibRecord = oleDsNGSearchUtil.retrieveBibBasedOnMatchPoints(query);
        String matchedBibString = getObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(bibRecord);
        return matchedBibString;
    }
}
