package org.kuali.ole.loaders.common;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by sheiksalahudeenm on 3/5/15.
 */
public class RestClient {

    public static String jerseryClientGet(String url){
        try {
            Client client = Client.create();
            WebResource webResource = client
                    .resource(url);
            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);
            String output = response.getEntity(String.class);
            return  output;

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
