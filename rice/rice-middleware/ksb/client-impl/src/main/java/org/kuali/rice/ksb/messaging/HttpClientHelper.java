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
package org.kuali.rice.ksb.messaging;

import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.params.HostParams;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.kuali.rice.core.api.util.ClassLoaderUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Contains some utility methods for dealing with configuration of the
 * Commons HttpClient library.  Specifically, HttpClient parameters are
 * typed, so we can't just pipe the String values from or configuration
 * through.  Instead we need to know the type of all the different
 * HttpClient parameters and set the parameter accordingly.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class HttpClientHelper {

	/**
	 * A Map which defines the type for all non-String parameters for HttpClient.
	 */
	private static final Map<String, Class<?>> PARAM_TYPE_MAP = new HashMap<String, Class<?>>();
	static {
		PARAM_TYPE_MAP.put(HttpMethodParams.PROTOCOL_VERSION, HttpVersion.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.UNAMBIGUOUS_STATUS_LINE, Boolean.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.SINGLE_COOKIE_HEADER, Boolean.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.STRICT_TRANSFER_ENCODING, Boolean.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.REJECT_HEAD_BODY, Boolean.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, Integer.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.USE_EXPECT_CONTINUE, Boolean.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.WARN_EXTRA_INPUT, Boolean.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.STATUS_LINE_GARBAGE_LIMIT, Integer.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.SO_TIMEOUT, Integer.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.RETRY_HANDLER, HttpMethodRetryHandler.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.DATE_PATTERNS, Collection.class);
		PARAM_TYPE_MAP.put(HttpMethodParams.BUFFER_WARN_TRIGGER_LIMIT, Integer.class);
		PARAM_TYPE_MAP.put(HttpConnectionParams.SO_TIMEOUT, Integer.class);
		PARAM_TYPE_MAP.put(HttpConnectionParams.TCP_NODELAY, Boolean.class);
		PARAM_TYPE_MAP.put(HttpConnectionParams.SO_SNDBUF, Integer.class);
		PARAM_TYPE_MAP.put(HttpConnectionParams.SO_RCVBUF, Integer.class);
		PARAM_TYPE_MAP.put(HttpConnectionParams.SO_LINGER, Integer.class);
		PARAM_TYPE_MAP.put(HttpConnectionParams.CONNECTION_TIMEOUT, Integer.class);
		PARAM_TYPE_MAP.put(HttpConnectionParams.STALE_CONNECTION_CHECK, Boolean.class);
		PARAM_TYPE_MAP.put(HttpConnectionManagerParams.MAX_HOST_CONNECTIONS, Map.class);
		PARAM_TYPE_MAP.put(HttpConnectionManagerParams.MAX_TOTAL_CONNECTIONS, Integer.class);
		PARAM_TYPE_MAP.put(HostParams.DEFAULT_HEADERS, Collection.class);
		PARAM_TYPE_MAP.put(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, Long.class);
		PARAM_TYPE_MAP.put(HttpClientParams.CONNECTION_MANAGER_CLASS, Class.class);
		PARAM_TYPE_MAP.put(HttpClientParams.PREEMPTIVE_AUTHENTICATION, Boolean.class);
		PARAM_TYPE_MAP.put(HttpClientParams.REJECT_RELATIVE_REDIRECT, Boolean.class);
		PARAM_TYPE_MAP.put(HttpClientParams.MAX_REDIRECTS, Integer.class);
		PARAM_TYPE_MAP.put(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, Boolean.class);
	}
	
	public static void setParameter(HttpParams params, String paramName, String paramValue) {
		Class<?> paramType = getParameterType(paramName);
		if (paramType.equals(Boolean.class)) {
			params.setBooleanParameter(paramName, Boolean.parseBoolean(paramValue));
		} else if (paramType.equals(Integer.class)) {
			params.setIntParameter(paramName, Integer.parseInt(paramValue));
		} else if (paramType.equals(Long.class)) {
			params.setLongParameter(paramName, Long.parseLong(paramValue));
		} else if (paramType.equals(Double.class)) {
			params.setDoubleParameter(paramName, Double.parseDouble(paramValue));
		} else if (paramType.equals(String.class)) {
			params.setParameter(paramName, paramValue);
		} else if (paramType.equals(Class.class)) {
			try {
				Class<?> configuredClass = Class.forName(paramValue, true, ClassLoaderUtils.getDefaultClassLoader());
				params.setParameter(paramName, configuredClass);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Could not locate the class needed to configure the HttpClient.", e);
			}
		} else {
			throw new RuntimeException("Attempted to configure an HttpClient parameter '" + paramName + "' " +
					"of a type not supported through Workflow configuration: " + paramType.getName());
		}
	}
	
	/**
	 * Returns the expected type of the given HttpClient parameter.  String is the default.
	 */
	public static Class getParameterType(String parameterName) {
		Class<?> parameterType = PARAM_TYPE_MAP.get(parameterName);
		if (parameterType == null) {
			parameterType = String.class;
		}
		return parameterType;
	}
	
}
