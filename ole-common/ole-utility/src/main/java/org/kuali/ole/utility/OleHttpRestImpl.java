package org.kuali.ole.utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class OleHttpRestImpl implements OleHttpRest {
    private static final Logger LOG = Logger.getLogger(OleHttpRestImpl.class);

    String urlBase = null;

    /**
     * Set the urlBase, this can be used for regression testing when the ConfigContext
     * hasn't been initialized.
     *
     * @param urlBase URL excluding the trailing slash
     */
    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    /**
     * Return the value set using setUrlBase(urlBase), the property ole.fs.url.base, or
     * "http://localhost:8080/olefs" if there isn't any context.
     * @return the url base
     */
    public String getUrlBase() {
        if (urlBase != null) {
            return urlBase;
        }
        Config config = ConfigContext.getCurrentContextConfig();
        if (config == null) {
            return "http://localhost:8080/olefs";
        }
        return config.getProperty("ole.fs.url.base");
    }

    /**
     * Do a HTTP Get at some rest URL.
     *
     * @param restPath the path starting with "rest/" of the URL where to do the GET
     * @return response to the Get request, null if restPath not found (HTTP 404)
     * @exception IOException  on IO error
     * @exception HttpResponseException on non-2xx status codes
     */
    @Override
    public String get(final String restPath) throws IOException {
        String url = getUrlBase() + "/" + restPath;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(new HttpGet(url));
        InputStream body = response.getEntity().getContent();
        Scanner scanner = new Scanner(body, "UTF-8").useDelimiter("\\A");
        if (!scanner.hasNext()) {
            return "";
        }
        return scanner.next();
    }

    /**
     * Do a HTTP Post at some REST URL.
     *
     * @param restPath the path starting with "rest/" of the URL where to do the POST
     * @param content  request content
     * @param format   format of request and response content,
     *                 e.g. "json", "xml", "html", "text"
     * @return response content
     * @exception IOException  on IO error
     * @exception HttpResponseException on non-2xx status codes
     */
    @Override
    public String post(final String restPath, final String content, final String format)
            throws IOException {
        String url = getUrlBase() + "/" + restPath;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/" + format);
        post.addHeader("Accept",       "application/" + format);
        post.setEntity(new StringEntity(content));
        HttpResponse response = httpClient.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode > 299) {
            throw new HttpResponseException(statusCode,
                    url + " - " +
                    response.getStatusLine().getReasonPhrase());
        }
        InputStream body = response.getEntity().getContent();
        Scanner scanner = new Scanner(body, "UTF-8").useDelimiter("\\A");
        if (!scanner.hasNext()) {
            return "";
        }
        return scanner.next();
    }

    /**
     * Do a HTTP Post in JSON format at some REST URL
     *
     * @param restPath the path starting with "rest/" of the URL where to do the POST
     * @param content  request content
     * @return response content
     * @exception IOException  on IO error
     * @exception HttpResponseException on non-2xx status codes
     */
    @Override
    public String json(final String restPath, final String content)
            throws IOException, HttpResponseException {
        return post(restPath, content, "json");
    }
}
