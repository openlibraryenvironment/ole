/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.ksb.messaging.serviceconnectors;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.api.bus.support.JavaServiceConfiguration;
import org.kuali.rice.ksb.messaging.HttpClientHelper;
import org.kuali.rice.ksb.messaging.KSBHttpInvokerProxyFactoryBean;
import org.kuali.rice.ksb.messaging.KSBHttpInvokerRequestExecutor;
import org.kuali.rice.ksb.security.httpinvoker.AuthenticationCommonsHttpInvokerRequestExecutor;


/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 */
public class HttpInvokerConnector extends AbstractServiceConnector {

	private static final Logger LOG = Logger.getLogger(HttpInvokerConnector.class);

	private HttpClientParams httpClientParams;

	private boolean httpClientInitialized = false;

	private static final String IDLE_CONNECTION_THREAD_INTERVAL_PROPERTY = "ksb.thinClient.idleConnectionThreadInterval";
	private static final String IDLE_CONNECTION_TIMEOUT_PROPERTY = "ksb.thinClient.idleConnectionTimeout";
	private static final String DEFAULT_IDLE_CONNECTION_THREAD_INTERVAL = "7500";
	private static final String DEFAULT_IDLE_CONNECTION_TIMEOUT = "5000";
	private static final String RETRY_SOCKET_EXCEPTION_PROPERTY = "ksb.thinClient.retrySocketException";
	
	private static IdleConnectionTimeoutThread ictt;
	
	public HttpInvokerConnector(final JavaServiceConfiguration serviceConfiguration, final URL alternateEndpointUrl) {
		super(serviceConfiguration, alternateEndpointUrl);
		initializeHttpClientParams();
	}

    @Override
	public JavaServiceConfiguration getServiceConfiguration() {
		return (JavaServiceConfiguration) super.getServiceConfiguration();
	}
	
	public Object getService() {
	    LOG.debug("Getting connector for endpoint " + getActualEndpointUrl());
		KSBHttpInvokerProxyFactoryBean client = new KSBHttpInvokerProxyFactoryBean();
		client.setServiceUrl(getActualEndpointUrl().toExternalForm());
		client.setServiceConfiguration(getServiceConfiguration());
		
		KSBHttpInvokerRequestExecutor executor;
		
		if (getCredentialsSource() != null) {
		    executor = new AuthenticationCommonsHttpInvokerRequestExecutor(getHttpClient(), getCredentialsSource(), getServiceConfiguration());
		} else {
		    executor = new KSBHttpInvokerRequestExecutor(getHttpClient());
		}
		executor.setSecure(getServiceConfiguration().getBusSecurity());
		client.setHttpInvokerRequestExecutor(executor);	
		client.afterPropertiesSet();
		return getServiceProxyWithFailureMode(client.getObject(), getServiceConfiguration());
	}

	/**
	 * Creates a commons HttpClient for service invocation. Config parameters
	 * that start with http.* are used to configure the client.
	 * 
	 * TODO we need to add support for other invocation protocols and
	 * implementations, but for now...
	 */
	public HttpClient getHttpClient() {
		return new HttpClient(this.httpClientParams);
	}

	protected void initializeHttpClientParams() {
		synchronized (HttpInvokerConnector.class) {
		if (! this.httpClientInitialized) {
		    this.httpClientParams = new HttpClientParams();
			configureDefaultHttpClientParams(this.httpClientParams);
			Properties configProps = ConfigContext.getCurrentContextConfig().getProperties();
			for (Iterator<Object> iterator = configProps.keySet().iterator(); iterator.hasNext();) {
				String paramName = (String) iterator.next();
				if (paramName.startsWith("http.")) {
					HttpClientHelper.setParameter(this.httpClientParams, paramName, (String) configProps.get(paramName));
				}
			}
				runIdleConnectionTimeout();
			this.httpClientInitialized = true;
		}
	}
	}

	protected void configureDefaultHttpClientParams(HttpParams params) {
		params.setParameter(HttpClientParams.CONNECTION_MANAGER_CLASS, MultiThreadedHttpConnectionManager.class);
		params.setParameter(HttpMethodParams.COOKIE_POLICY, CookiePolicy.RFC_2109);
		params.setLongParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, 10000);
		Map<HostConfiguration, Integer> maxHostConnectionsMap = new HashMap<HostConfiguration, Integer>();
		maxHostConnectionsMap.put(HostConfiguration.ANY_HOST_CONFIGURATION, new Integer(20));
		params.setParameter(HttpConnectionManagerParams.MAX_HOST_CONNECTIONS, maxHostConnectionsMap);
		params.setIntParameter(HttpConnectionManagerParams.MAX_TOTAL_CONNECTIONS, 20);
		params.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 10000);
		params.setIntParameter(HttpConnectionParams.SO_TIMEOUT, 2*60*1000);
		

		boolean retrySocketException = new Boolean(ConfigContext.getCurrentContextConfig().getProperty(RETRY_SOCKET_EXCEPTION_PROPERTY));
		if (retrySocketException) {
		    LOG.info("Installing custom HTTP retry handler to retry requests in face of SocketExceptions");
		    params.setParameter(HttpMethodParams.RETRY_HANDLER, new CustomHttpMethodRetryHandler());
		}

		
	}
	

	
	/**
	 * Idle connection timeout thread added as a part of the fix for ensuring that 
	 * threads that timed out need to be cleaned or and send back to the pool so that 
	 * other clients can use it.
	 *
	 */
	private void runIdleConnectionTimeout() {
	    if (ictt != null) {
		    String timeoutInterval = ConfigContext.getCurrentContextConfig().getProperty(IDLE_CONNECTION_THREAD_INTERVAL_PROPERTY);
		    if (StringUtils.isBlank(timeoutInterval)) {
			timeoutInterval = DEFAULT_IDLE_CONNECTION_THREAD_INTERVAL;
		    }
		    String connectionTimeout = ConfigContext.getCurrentContextConfig().getProperty(IDLE_CONNECTION_TIMEOUT_PROPERTY);
		    if (StringUtils.isBlank(connectionTimeout)) {
			connectionTimeout = DEFAULT_IDLE_CONNECTION_TIMEOUT;
		    }
		    
		    ictt.addConnectionManager(getHttpClient().getHttpConnectionManager());
		    ictt.setTimeoutInterval(new Integer(timeoutInterval));
		    ictt.setConnectionTimeout(new Integer(connectionTimeout));
		    //start the thread
		    ictt.start();
	    }
	}
	
	public static void shutdownIdleConnectionTimeout() {
		if (ictt != null) {
			try {
				ictt.shutdown();
			} catch (Exception e) {
				LOG.error("Failed to shutdown idle connection thread.", e);
			}
		}
	}
	
	private static final class CustomHttpMethodRetryHandler extends DefaultHttpMethodRetryHandler {

		private static final int MAX_RETRIES = 1;
		
		public CustomHttpMethodRetryHandler() {
			super(MAX_RETRIES, true);
	}

		@Override
	    public boolean retryMethod(HttpMethod method, IOException exception, int executionCount) {
		boolean shouldRetry = super.retryMethod(method, exception, executionCount);
		if (!shouldRetry && executionCount < MAX_RETRIES) {
			if (exception instanceof SocketException) {
				LOG.warn("Retrying request because of SocketException!", exception);
				shouldRetry = true;
			} else if (exception instanceof SocketTimeoutException) {
				LOG.warn("Retrying request because of SocketTimeoutException!", exception);
				shouldRetry = true;
			}
		}
		return shouldRetry;
	    }
	    
	}
	

	
}
