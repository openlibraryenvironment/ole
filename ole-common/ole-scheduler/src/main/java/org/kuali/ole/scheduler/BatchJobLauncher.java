package org.kuali.ole.scheduler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class BatchJobLauncher {

    private String profileId;
    private String batchType;
    private String url;

    public BatchJobLauncher(String profileName, String batchType, String url) {
	this.profileId = profileName;
	this.batchType = batchType;
	this.url = url;
    }

    public void start() {
	System.out.println("--- start batch job ---");
	HttpEntity entity = MultipartEntityBuilder.create()
		.setMode(HttpMultipartMode.STRICT)
		.addBinaryBody("file", new File("src/test/resources/sample-marc"), ContentType.APPLICATION_OCTET_STREAM, "sample-marc.xml")
		.addTextBody("profileName", profileId, ContentType.TEXT_PLAIN)		
		.addTextBody("batchType", batchType, ContentType.TEXT_PLAIN)		
		.build();
	HttpPost request = new HttpPost(url);
	request.setEntity(entity);
	CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("ole-quickstart", ""));
	CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
		.build();
	try {
	    HttpResponse response = httpClient.execute(request);
	    
	    // for debugging
//	    InputStream body = response.getEntity().getContent();
//	    String inputStreamString = new Scanner(body,"UTF-8").useDelimiter("\\A").next();
//	    System.out.println(inputStreamString);	    
//	    System.out.println(response.getStatusLine());
	    
	} catch (ClientProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}