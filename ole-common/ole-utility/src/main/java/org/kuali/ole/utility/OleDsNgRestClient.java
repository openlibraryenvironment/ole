package org.kuali.ole.utility;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.kuali.rice.core.api.config.property.ConfigContext;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsNgRestClient {

    public static final String OLE_DS_NG_API_CONTEXT_PATH = "api/oledsng";

    public static final class Service {
        public static final String OVERLAY_BIB = "processBibOverlay";
        public static final String OVERLAY_HOLDING = "processHoldingOverlay";
        public static final String PROCESS_BIB_HOLDING_ITEM = "processBibHoldingsItems";
        public static final String PROCESS_DELETE_BIBS = "processDeleteBibs";
    }

    public static final class Format {
        public static final String JSON = "json";
    }

    private Client client;

    public String postData(String serviceName, Object content, String format) {
        String url = getServiceAPIUrl() + serviceName;


        WebResource webResource = getClient()
                .resource(url);

        ClientResponse response = webResource.type("application/"+format+";charset=UTF-8")
                .post(ClientResponse.class, content);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }


        return response.getEntity(String.class);

    }

    public Client getClient() {
        if (null == client) {
            client = new Client();
        }
        return client;
    }

    public String getServiceAPIUrl() {
        return getDsNgBaseUrl() + "/" + OLE_DS_NG_API_CONTEXT_PATH + "/";
    }

    public String getDsNgBaseUrl() {
        return ConfigContext.getCurrentContextConfig().getProperty("ole.docstore.url.base");
    }
}
