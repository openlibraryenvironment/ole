package org.kuali.ole.utility;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Doing an HTTP REST GET request.
 */
public class OleHttpRestGetImpl implements OleHttpRestGet {
    String urlBase = null;

    /**
     * Set the urlBase, this can be used for regression testing when the ConfigContext
     * hasn't been initialized.
     *
     * @param urlBase URL excluding the trailing slash
     */
    void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    /**
     * Return the property ole.fs.url.base, or the value previously set using setUrlBase(urlBase).
     * @return the url base
     */
    public String getUrlBase() {
        if (urlBase != null) {
            return urlBase;
        }
        urlBase = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
        return urlBase;
    }

    /**
     * FIXME: hardcoded credentials??
     * @return
     */
    private UsernamePasswordCredentials getCredentials() {
        return new UsernamePasswordCredentials("ole-quickstart", "");
    }

    @Override
    public String rest(String restPath) throws IOException {
        String url = getUrlBase() + "/" + restPath;
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, getCredentials());
        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
        HttpResponse response = httpClient.execute(new HttpGet(url));
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
            return null;
        }
        InputStream body = response.getEntity().getContent();
        Scanner scanner = new Scanner(body, "UTF-8").useDelimiter("\\A");
        if (!scanner.hasNext()) {
            return "";
        }
        return scanner.next();
    }
}
