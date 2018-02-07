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
package org.kuali.rice.ksb.messaging.servlet;

import org.apache.commons.collections.EnumerationUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.servlet.ServletController;
import org.apache.cxf.transport.servlet.servicelist.ServiceListGeneratorServlet;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.bus.support.SoapServiceConfiguration;
import org.kuali.rice.ksb.security.SignatureSigningResponseWrapper;
import org.kuali.rice.ksb.security.SignatureVerifyingRequestWrapper;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.springframework.beans.BeansException;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * A {@link DispatcherServlet} which dispatches incoming requests to the appropriate
 * service endpoint.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KSBDispatcherServlet extends DispatcherServlet {

	private static final Logger LOG = Logger.getLogger(KSBDispatcherServlet.class);
	private static final long serialVersionUID = 6790121225857950019L;
    private static final String REMOTING_SERVLET_CONFIG_LOCATION = "classpath:org/kuali/rice/ksb/config/remoting-servlet.xml";

	private KSBHttpInvokerHandler httpInvokerHandler;
	private ServletController cxfServletController;

    @Override
	protected void initFrameworkServlet() throws ServletException, BeansException {
		this.httpInvokerHandler = new KSBHttpInvokerHandler();
		
        Bus bus = KSBServiceLocator.getCXFBus();

        List<Interceptor<? extends Message>> inInterceptors = KSBServiceLocator.getInInterceptors();
        if(inInterceptors != null) {
        	List<Interceptor<? extends Message>> busInInterceptors = bus.getInInterceptors();
        	busInInterceptors.addAll(inInterceptors);
        }
       
        List<Interceptor<? extends Message>> outInterceptors = KSBServiceLocator.getOutInterceptors();
        if(outInterceptors != null) {
        	List<Interceptor<? extends Message>> busOutInterceptors = bus.getOutInterceptors();
        	busOutInterceptors.addAll(outInterceptors);
        }


		HTTPTransportFactory transportFactory = bus.getExtension(HTTPTransportFactory.class);
        if (transportFactory == null) {
            throw new IllegalStateException("Failed to locate HTTPTransportFactory extension on Apache CXF Bus");
        }

        DestinationRegistry destinationRegistry = transportFactory.getRegistry();


		this.cxfServletController = new ServletController(destinationRegistry, getCXFServletConfig(
                this.getServletConfig()), new ServiceListGeneratorServlet(destinationRegistry, bus));

		this.setPublishEvents(false);
	}

    /**
     * This is a workaround after upgrading to CXF 2.7.0 whereby we could no longer just call "setHideServiceList" on
     * the ServletController. Instead, it is now reading this information from the ServletConfig, so wrapping the base
     * ServletContext to return true or false for hide service list depending on whether or not we are in dev mode.
     */
    protected ServletConfig getCXFServletConfig(final ServletConfig baseServletConfig) {
        // disable handling of URLs ending in /services which display CXF generated service lists if we are not in dev mode
        final String shouldHide = Boolean.toString(!ConfigContext.getCurrentContextConfig().getDevMode().booleanValue());
        return new ServletConfig() {
            private static final String HIDE_SERVICE_LIST_PAGE_PARAM = "hide-service-list-page";
            @Override
            public String getServletName() {
                return baseServletConfig.getServletName();
            }
            @Override
            public ServletContext getServletContext() {
                return baseServletConfig.getServletContext();
            }
            @Override
            public String getInitParameter(String parameter) {
                if (HIDE_SERVICE_LIST_PAGE_PARAM.equals(parameter)) {
                    return shouldHide;
                }
                return baseServletConfig.getInitParameter(parameter);
            }
            @Override
            public Enumeration<String> getInitParameterNames() {
                List<String> initParameterNames = EnumerationUtils.toList(baseServletConfig.getInitParameterNames());
                initParameterNames.add(HIDE_SERVICE_LIST_PAGE_PARAM);
                return new Vector<String>(initParameterNames).elements();
            }
        };
    }

    @Override
	protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
		if (handler instanceof HttpRequestHandler) {
			return new HttpRequestHandlerAdapter();
		} else if (handler instanceof Controller) {
			Object unwrappedHandler = ClassLoaderUtils.unwrapFromProxy(handler);
			if (unwrappedHandler instanceof CXFServletControllerAdapter) {
				// TODO this just seems weird as this controller is initially null when it's created, does there need to be some synchronization here?
				((CXFServletControllerAdapter)unwrappedHandler).setController(cxfServletController);
			}			
			return new SimpleControllerHandlerAdapter();
		}
		throw new RiceRuntimeException("handler of type " + handler.getClass().getName() + " is not known and can't be used by " + KSBDispatcherServlet.class.getName());
	}

    @Override
	protected HandlerExecutionChain getHandler(HttpServletRequest request, boolean cache) throws Exception {
		return this.httpInvokerHandler.getHandler(request);
	}

	@Override
	protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		try {
			QName serviceName = this.httpInvokerHandler.getServiceNameFromRequest(request);
			LOG.info("Caught Exception from service " + serviceName, ex);
		} catch (Throwable throwable) {
			LOG.warn("Caught exception attempting to log exception thrown from remotely accessed service", throwable);
		}
		return null;
	}

	/**
	 * Overrides the service method to replace the request and responses with one which will provide input and output streams for
	 * verifying and signing the data.
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (isSecure(request)) {
			super.service(new SignatureVerifyingRequestWrapper(request), new SignatureSigningResponseWrapper(response));
		} else {
			super.service(request, response);
		}
	}

    @Override
    protected WebApplicationContext initWebApplicationContext() {
        setContextConfigLocation(REMOTING_SERVLET_CONFIG_LOCATION);
        return super.initWebApplicationContext();
    }

	protected boolean isSecure(HttpServletRequest request) {
		QName serviceName = this.httpInvokerHandler.getServiceNameFromRequest(request);
		if (LOG.isDebugEnabled()) {
		    LOG.debug("Checking service " + serviceName + " for security enabled");
		}
		Endpoint endpoint = KsbApiServiceLocator.getServiceBus().getEndpoint(serviceName);
		if (endpoint == null) {
			LOG.error("Attempting to acquire non-existent service " + request.getRequestURI());
		    throw new RiceRuntimeException("Attempting to acquire non-existent service.");
		}
		ServiceConfiguration serviceConfiguration = endpoint.getServiceConfiguration();
		if (serviceConfiguration instanceof SoapServiceConfiguration) {
		    return false;
		}
		return serviceConfiguration.getBusSecurity();
	}

}
