package org.kuali.ole.utility;

import org.kuali.rice.core.api.config.property.ConfigContext;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsNgRestClient {

    public static final String OLE_DS_NG_API_CONTEXT_PATH = "api/oledsng/";

    private OleHttpRestClient oleHttpRestClient;

    public String postData(String serviceName, String content,String format) {
        String url = getServiceAPIUrl() + serviceName;
        return getOleHttpRestClient().sendPostRequest(url,content,format);
    }

    public OleHttpRestClient getOleHttpRestClient() {
        if(null == oleHttpRestClient) {
            oleHttpRestClient = new OleHttpRestClient();
        }
        return oleHttpRestClient;
    }

    public void setOleHttpRestClient(OleHttpRestClient oleHttpRestClient) {
        this.oleHttpRestClient = oleHttpRestClient;
    }

    public String getServiceAPIUrl() {
        return getDsNgBaseUrl() + "/" + OLE_DS_NG_API_CONTEXT_PATH + "/";
    }

    public String getDsNgBaseUrl() {
        return ConfigContext.getCurrentContextConfig().getProperty("ole.docstore.url.base");
    }
}
