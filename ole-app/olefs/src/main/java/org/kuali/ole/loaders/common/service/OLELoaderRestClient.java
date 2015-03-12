package org.kuali.ole.loaders.common.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 3/5/15.
 */
public class OLELoaderRestClient {

    public static Map<String,Object> jerseryClientGet(String url){
        try {
            Map<String,Object> objectMap = new HashMap<>();
            Client client = Client.create();
            WebResource webResource = client
                    .resource(url);
            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);
            objectMap.put("status", response.getStatus());
            objectMap.put("content", response.getEntity(String.class));
            return  objectMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String jerseryClientPost(String url, String requestContent) {
        try {
            Client client = Client.create();
            WebResource webResource = client
                    .resource(url);
            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, requestContent);
            String output = response.getEntity(String.class);
            return  output;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String jerseryClientPut(String url, String requestContent) {
        try {
            Client client = Client.create();
            WebResource webResource = client
                    .resource(url);
            ClientResponse response = webResource.type("application/json")
                    .put(ClientResponse.class, requestContent);
            String output = response.getEntity(String.class);
            return  output;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
