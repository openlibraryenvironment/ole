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
package org.kuali.rice.krad.service.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.PersistenceService;
import org.kuali.rice.krad.service.XmlObjectSerializerService;
import org.kuali.rice.krad.service.util.DateTimeConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Service implementation for the XmlObjectSerializer structure. This is the default implementation that gets
 * delivered with Kuali. It utilizes the XStream open source libraries and framework.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XmlObjectSerializerServiceImpl implements XmlObjectSerializerService {
	private static final Log LOG = LogFactory.getLog(XmlObjectSerializerServiceImpl.class);
	
	private PersistenceService persistenceService;
	
	private XStream xstream;
	
	public XmlObjectSerializerServiceImpl() {
		xstream = new XStream(new ProxyAwareJavaReflectionProvider());

        // See http://xstream.codehaus.org/faq.html#Serialization_CGLIB
        // To use a newer version of XStream we may need to do something like this:
//        xstream = new XStream() {
//
//            @Override
//            public ReflectionProvider getReflectionProvider() {
//                return new ProxyAwareJavaReflectionProvider();
//            }
//
//            protected MapperWrapper wrapMapper(MapperWrapper next) {
//                return new CGLIBMapper(next);
//            }
//        };
//        xstream.registerConverter(new CGLIBEnhancedConverter(xstream.getMapper(), xstream.getReflectionProvider()));

		xstream.registerConverter(new ProxyConverter(xstream.getMapper(), xstream.getReflectionProvider() ));
		xstream.addDefaultImplementation(ArrayList.class, ListProxyDefaultImpl.class);
        xstream.registerConverter(new DateTimeConverter());
	}
	
    /**
     * @see org.kuali.rice.krad.service.XmlObjectSerializer#toXml(java.lang.Object)
     */
    public String toXml(Object object) {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug( "toXml(" + object + ") : \n" + xstream.toXML(object) );
    	}
        return xstream.toXML(object);
    }

    /**
     * @see org.kuali.rice.krad.service.XmlObjectSerializer#fromXml(java.lang.String)
     */
    public Object fromXml(String xml) {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug( "fromXml() : \n" + xml );
    	}
    	if ( xml != null ) {
    		xml = xml.replaceAll( "--EnhancerByCGLIB--[0-9a-f]{0,8}", "" );
    	}
        return xstream.fromXML(xml);
    }

    /**
     * This custom converter only handles proxies for BusinessObjects.  List-type proxies are handled by configuring XStream to treat
     * ListProxyDefaultImpl as ArrayLists (see constructor for this service). 
     */
    public class ProxyConverter extends ReflectionConverter {
        public ProxyConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }
        
        @Override
        // since the ReflectionConverter supertype defines canConvert without using a parameterized Class type, we must declare
        // the overridden version the same way
        @SuppressWarnings("unchecked")
        public boolean canConvert(Class clazz) {
            return clazz.getName().contains("CGLIB");
        }

        @Override
        public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext context) {
            super.marshal(getPersistenceService().resolveProxy(obj), writer, context);
        }
        
        // we shouldn't need an unmarshal method because all proxy metadata is taken out of the XML, so we'll reserialize as a base BO. 
    }
    
    public class ProxyAwareJavaReflectionProvider extends PureJavaReflectionProvider {

    	public ProxyAwareJavaReflectionProvider() {
    		super();
    	}
        /**
         * @see com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider#visitSerializableFields(java.lang.Object, com.thoughtworks.xstream.converters.reflection.ReflectionProvider.Visitor)
         */
        @Override
        public void visitSerializableFields(Object object, Visitor visitor) {
            for (Iterator iterator = fieldDictionary.serializableFieldsFor(object.getClass()); iterator.hasNext();) {
                Field field = (Field) iterator.next();
                if (!fieldModifiersSupported(field)) {
                    continue;
                }
                validateFieldAccess(field);
                Object value = null;
                try {
                    value = field.get(object);
                    if (value != null && getPersistenceService().isProxied(value)) {
                        value = getPersistenceService().resolveProxy(value);
                    }
                } catch (IllegalArgumentException e) {
                    throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
                } catch (IllegalAccessException e) {
                    throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
                }
                visitor.visit(field.getName(), field.getType(), field.getDeclaringClass(), value);
            }
        }
        
    }

	public PersistenceService getPersistenceService() {
		if ( persistenceService == null ) {
			persistenceService = KRADServiceLocator.getPersistenceService();
		}
		return persistenceService;
	}

}
