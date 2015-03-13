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

    public static Map<String,Object> jerseryClientPost(String url, String requestContent) {
        try {
            Map<String,Object> objectMap = new HashMap<>();
            Client client = Client.create();
            WebResource webResource = client
                    .resource(url);
            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, requestContent);
            objectMap.put("status", response.getStatus());
            objectMap.put("content", response.getEntity(String.class));
            objectMap.put("header", response.getHeaders());
            return  objectMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String,Object> jerseryClientPut(String url, String requestContent) {
        try {
            Map<String,Object> objectMap = new HashMap<>();
            Client client = Client.create();
            WebResource webResource = client
                    .resource(url);
            ClientResponse response = webResource.type("application/json")
                    .put(ClientResponse.class, requestContent);
            objectMap.put("status", response.getStatus());
            objectMap.put("content", response.getEntity(String.class));
            return  objectMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
