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
package org.kuali.rice.krad.web.filter;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.ClassLoaderUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A filter which at runtime reads a series of filter configurations, constructs
 * and initializes those filters, and invokes them when it is invoked. This
 * allows runtime user configuration of arbitrary filters in the webapp context.
 *
 * <p>
 * Note : filter mapping order numbers must be unique across all filters. Filter exclusions will do a regex match
 * against the full path of the request.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BootstrapFilter implements Filter {
	private static final Logger LOG = Logger.getLogger(BootstrapFilter.class);

	private static final String FILTER_PREFIX = "filter.";

	private static final String CLASS_SUFFIX = ".class";

	private static final String FILTER_MAPPING_PREFIX = "filtermapping.";

    private static final String FILTER_EXCLUDE_PREFIX = "filterexclude.";

	private FilterConfig config;

	private final Map<String, Filter> filters = new HashMap<String, Filter>();

	private final SortedSet<FilterMapping> filterMappings = new TreeSet<FilterMapping>();

    private final Map<String, ArrayList<String>> filterExclusions = new HashMap<String, ArrayList<String>>();

	private boolean initted = false;

	public void init(FilterConfig cfg) throws ServletException {
		this.config = cfg;
	}

	private void addFilter(String name, String classname, Map<String, String> props) throws ServletException {
		LOG.debug("Adding filter: " + name + "=" + classname);
		Object filterObject = GlobalResourceLoader.getResourceLoader().getObject(new ObjectDefinition(classname));
		if (filterObject == null) {
			throw new ServletException("Filter '" + name + "' class not found: " + classname);

		}
		if (!(filterObject instanceof Filter)) {
			LOG.error("Class '" + filterObject.getClass() + "' does not implement servlet javax.servlet.Filter");
			return;
		}
		Filter filter = (Filter) filterObject;
		BootstrapFilterConfig fc = new BootstrapFilterConfig(config.getServletContext(), name);
		for (Map.Entry<String, String> entry : props.entrySet()) {
			String key = entry.getKey().toString();
			final String prefix = FILTER_PREFIX + name + ".";
			if (!key.startsWith(prefix) || key.equals(FILTER_PREFIX + name + CLASS_SUFFIX)) {
				continue;
			}
			String paramName = key.substring(prefix.length());
			fc.addInitParameter(paramName, entry.getValue());
		}
		try {
			filter.init(fc);
			filters.put(name, filter);
		} catch (ServletException se) {
			LOG.error("Error initializing filter: " + name + " [" + classname + "]", se);
		}
	}

	private void addFilterMapping(String filterName, String orderNumber, String value) {
		filterMappings.add(new FilterMapping(filterName, orderNumber, value));
	}

    /**
     * Adds an exclusion to the exclusion list for a filter
     *
     * <p>
     * If this is the first exclusion to be added, the list will be created and added to the exclusion map
     * </p>
     *
     * @param filterName - name of the filter
     * @param exclusion - exclusion string
     */
    private void addFilterExclusion(String filterName, String exclusion) {
        if (filterExclusions.containsKey(filterName)) {            
            filterExclusions.get(filterName).add(exclusion);
        } else {
            filterExclusions.put(filterName, new ArrayList(Arrays.asList(exclusion)));
        }
    }

	private synchronized void init() throws ServletException {
		if (initted) {
			return;
		}
		LOG.debug("initializing...");
		Config cfg = ConfigContext.getCurrentContextConfig();
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Map<String, String> p = new HashMap<String, String>((Map) cfg.getProperties());
		
		for (Map.Entry<String, String> entry : p.entrySet()) {
			String key = entry.getKey().toString();
			if (key.startsWith(FILTER_MAPPING_PREFIX)) {
				String[] values = key.split("\\.");
				if (values.length != 2 && values.length != 3) {
					throw new ServletException("Invalid filter mapping defined.  Should contain 2 or 3 pieces in the form of filtermapping.<<filter name>>.<<order number>> with the last piece optional.");
				}
				String filterName = values[1];
				String orderNumber = (values.length == 2 ? "0" : values[2]);
				String value = entry.getValue();
				addFilterMapping(filterName, orderNumber, value);
			} else if (key.startsWith(FILTER_PREFIX) && key.endsWith(CLASS_SUFFIX)) {
				String name = key.substring(FILTER_PREFIX.length(), key.length() - CLASS_SUFFIX.length());
				String value = entry.getValue();
				// ClassLoader cl =
				// SpringServiceLocator.getPluginRegistry().getInstitutionPlugin().getClassLoader();
				// addFilter(name, value, cl, p);
				addFilter(name, value, p);
			} else if (key.startsWith(FILTER_EXCLUDE_PREFIX)) {
                String[] values = key.split("\\.");
                if (values.length != 2 && values.length != 3) {
                    throw new ServletException("Invalid filter mapping defined.  Should contain 2 or 3 pieces in the form of filterexclusion.<<filter name>>.<<number>> with the last piece optional.");
                }
                String filterName = values[1];
                String value = entry.getValue();
                addFilterExclusion(filterName, value);
            }
		}
		// do a diff log a warn if any filter has no mappings
		for (String filterName : filters.keySet()) {
			if (!hasFilterMapping(filterName)) {
				LOG.warn("NO FILTER MAPPING DETECTED.  Filter " + filterName + " has no mapping and will not be called.");
			}
		}
		initted = true;
	}

	private boolean hasFilterMapping(String filterName) {
		for (FilterMapping filterMapping : filterMappings) {
			if (filterMapping.getFilterName().equals(filterName)) {
				return true;
			}
		}
		return false;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    LOG.debug("Begin BootstrapFilter...");
		init();
		// build the filter chain and execute it
		if (!filterMappings.isEmpty() && request instanceof HttpServletRequest) {
			chain = buildChain((HttpServletRequest) request, chain);
		}
		LOG.debug("...ending BootstrapFilter preperation, executing BootstrapFilter Chain.");
		chain.doFilter(request, response);

	}

	private FilterChain buildChain(HttpServletRequest request, FilterChain targetChain) {
		BootstrapFilterChain chain = new BootstrapFilterChain(targetChain, ClassLoaderUtils.getDefaultClassLoader());
		String requestPath = request.getServletPath();
		for (FilterMapping mapping : filterMappings) {
			Filter filter = filters.get(mapping.getFilterName());
			if (!chain.containsFilter(filter) && matchFiltersURL(mapping.getUrlPattern(), requestPath) 
                    && !excludeFilter(mapping.getFilterName(), request.getRequestURL().toString())) {
                chain.addFilter(filter);
			}
		}
		return chain;
	}

    /**
     * Returns true if the path matches the exclusion regex
     *
     * @param filterName - filter name
     * @param requestPath - full path of request
     * @return boolean
     */
    private boolean excludeFilter(String filterName, String requestPath) {
        if (filterExclusions.containsKey(filterName)) {
            for (String exclusionString : filterExclusions.get(filterName)) {
                if (requestPath.matches(exclusionString)) {
                    return true;
                }
            }
        }
        return false;    
    }

	public void destroy() {
		for (Filter filter : filters.values()) {
			try {
				filter.destroy();
			} catch (Exception e) {
				LOG.error("Error destroying filter: " + filter, e);
			}
		}
	}

	/**
	 * This method was borrowed from the Tomcat codebase.
	 */
	private boolean matchFiltersURL(String urlPattern, String requestPath) {

		if (requestPath == null) {
			return (false);
		}

		// Match on context relative request path
		if (urlPattern == null) {
			return (false);
		}

		// Case 1 - Exact Match
		if (urlPattern.equals(requestPath)) {
			return (true);
		}

		// Case 2 - Path Match ("/.../*")
		if (urlPattern.equals("/*") || urlPattern.equals("*")) {
			return (true);
		}
		if (urlPattern.endsWith("/*")) {
			if (urlPattern.regionMatches(0, requestPath, 0, urlPattern.length() - 2)) {
				if (requestPath.length() == (urlPattern.length() - 2)) {
					return (true);
				} else if ('/' == requestPath.charAt(urlPattern.length() - 2)) {
					return (true);
				}
			}
			return (false);
		}

		// Case 3 - Extension Match
		if (urlPattern.startsWith("*.")) {
			int slash = requestPath.lastIndexOf('/');
			int period = requestPath.lastIndexOf('.');
			if ((slash >= 0) && (period > slash) && (period != requestPath.length() - 1) && ((requestPath.length() - period) == (urlPattern.length() - 1))) {
				return (urlPattern.regionMatches(2, requestPath, period + 1, urlPattern.length() - 2));
			}
		}

		// Case 4 - "Default" Match
		return (false); // NOTE - Not relevant for selecting filters

	}

}

/**
 * A filter chain that invokes a series of filters with which it was
 * initialized, and then delegates to a target filterchain.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class BootstrapFilterChain implements FilterChain {

	private final List<Filter> filters = new LinkedList<Filter>();

	private final FilterChain target;

	private Iterator<Filter> filterIterator;

	private ClassLoader originalClassLoader;

	public BootstrapFilterChain(FilterChain target, ClassLoader originalClassLoader) {
		this.target = target;
		this.originalClassLoader = originalClassLoader;
	}

	public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		if (filterIterator == null) {
			filterIterator = filters.iterator();
		}
		if (filterIterator.hasNext()) {
			(filterIterator.next()).doFilter(request, response, this);
		} else {
			// reset the CCL to the original classloader before calling the non
			// workflow configured filter - this makes it so our
			// CCL is the webapp classloader in workflow action classes and the
			// code they call
			Thread.currentThread().setContextClassLoader(originalClassLoader);
			target.doFilter(request, response);
		}
	}

	public void addFilter(Filter filter) {
		filters.add(filter);
	}

	public boolean containsFilter(Filter filter) {
		return filters.contains(filter);
	}

	public boolean isEmpty() {
		return filters.isEmpty();
	}

}

/**
 * Borrowed from spring-mock.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class BootstrapFilterConfig implements FilterConfig {

	private final ServletContext servletContext;

	private final String filterName;

	private final Map<String, String> initParameters = new HashMap<String, String>();

	public BootstrapFilterConfig() {
		this(null, "");
	}

	public BootstrapFilterConfig(String filterName) {
		this(null, filterName);
	}

	public BootstrapFilterConfig(ServletContext servletContext) {
		this(servletContext, "");
	}

	public BootstrapFilterConfig(ServletContext servletContext, String filterName) {
		this.servletContext = servletContext;
		this.filterName = filterName;
	}

	public String getFilterName() {
		return filterName;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void addInitParameter(String name, String value) {
		this.initParameters.put(name, value);
	}

	public String getInitParameter(String name) {
		return this.initParameters.get(name);
	}

	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(this.initParameters.keySet());
	}

}

class FilterMapping implements Comparable<FilterMapping> {

	private String filterName;

	private String orderValue;

	private String urlPattern;

	public FilterMapping(String filterName, String orderValue, String urlPattern) {
		this.filterName = filterName;
		this.orderValue = orderValue;
		this.urlPattern = urlPattern;
	}

	public int compareTo(FilterMapping object) {
		return orderValue.compareTo(object.orderValue);
	}

	public String getFilterName() {
		return filterName;
	}

	public String getOrderValue() {
		return orderValue;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

}
