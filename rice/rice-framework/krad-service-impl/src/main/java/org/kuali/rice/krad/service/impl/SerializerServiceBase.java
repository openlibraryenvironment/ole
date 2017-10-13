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
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl;
import org.apache.ojb.broker.core.proxy.ProxyHelper;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentSerializerService;
import org.kuali.rice.krad.service.PersistenceService;
import org.kuali.rice.krad.service.SerializerService;
import org.kuali.rice.krad.service.XmlObjectSerializerService;
import org.kuali.rice.krad.service.util.DateTimeConverter;
import org.kuali.rice.krad.util.documentserializer.AlwaysTruePropertySerializibilityEvaluator;
import org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator;
import org.kuali.rice.krad.util.documentserializer.PropertyType;
import org.kuali.rice.krad.util.documentserializer.SerializationState;
import org.springframework.util.AutoPopulatingList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of the {@link DocumentSerializerService}.  If no &lt;workflowProperties&gt; have been defined in the
 * data dictionary for a document type (i.e. {@link Document#getDocumentPropertySerizabilityEvaluator()} returns an instance of 
 * {@link AlwaysTruePropertySerializibilityEvaluator}), then this service will revert to using the {@link XmlObjectSerializerService}
 * bean, which was the old way of serializing a document for routing.  If workflowProperties are defined, then this implementation
 * will selectively serialize items.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class SerializerServiceBase implements SerializerService  {
//	private static final Log LOG = LogFactory.getLog(SerializerServiceBase.class);
    
    protected PersistenceService persistenceService;
    protected XmlObjectSerializerService xmlObjectSerializerService;
    
    protected XStream xstream;
    protected ThreadLocal<SerializationState> serializationStates;
    protected ThreadLocal<PropertySerializabilityEvaluator> evaluators;
    
    public SerializerServiceBase() {
        serializationStates = new ThreadLocal<SerializationState>();
        evaluators = new ThreadLocal<PropertySerializabilityEvaluator>();
        
        xstream = new XStream(new ProxyAndStateAwareJavaReflectionProvider());
        xstream.registerConverter(new ProxyConverter(xstream.getMapper(), xstream.getReflectionProvider() ));
        xstream.addDefaultImplementation(ArrayList.class, ListProxyDefaultImpl.class);
        xstream.addDefaultImplementation(AutoPopulatingList.class, ListProxyDefaultImpl.class);
        xstream.registerConverter(new AutoPopulatingListConverter(xstream.getMapper()));
        xstream.registerConverter(new DateTimeConverter());
    }
        
    public class ProxyConverter extends ReflectionConverter {
        public ProxyConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }
        public boolean canConvert(Class clazz) {
            return clazz.getName().contains("CGLIB") || clazz.getName().equals("org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl");
        }

        public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext context) {
            if (obj instanceof ListProxyDefaultImpl) { 
                List copiedList = new ArrayList(); 
                List proxiedList = (List) obj; 
                for (Iterator iter = proxiedList.iterator(); iter.hasNext();) { 
                    copiedList.add(iter.next()); 
                } 
                context.convertAnother( copiedList );
            } 
            else { 
                super.marshal(getPersistenceService().resolveProxy(obj), writer, context);
            }           
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            return null;
        }
    }
    
    public class ProxyAndStateAwareJavaReflectionProvider extends PureJavaReflectionProvider {
        @Override
        public void visitSerializableFields(Object object, Visitor visitor) {
            SerializationState state = serializationStates.get();
            PropertySerializabilityEvaluator evaluator = evaluators.get();
            
            for (Iterator iterator = fieldDictionary.serializableFieldsFor(object.getClass()); iterator.hasNext();) {
                Field field = (Field) iterator.next();
                if (!fieldModifiersSupported(field)) {
                    continue;
                }
                
                if (ignoreField(field)) {
                    continue;
                }
                
                validateFieldAccess(field);
                
                initializeField(object, field);
                
                Object value = null;
                try {
                    value = field.get(object);
                } catch (IllegalArgumentException e) {
                    throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
                } catch (IllegalAccessException e) {
                    throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
                }
                
                if (evaluator.isPropertySerializable(state, object, field.getName(), value)) {
                    if (value != null && ProxyHelper.isProxy(value)) {
                        // resolve proxies after we determine that it's serializable
                        value = getPersistenceService().resolveProxy(value);
                    }
                    PropertyType propertyType = evaluator.determinePropertyType(value);
                    state.addSerializedProperty(field.getName(), propertyType);
                    visitor.visit(field.getName(), field.getType(), field.getDeclaringClass(), value);
                    state.removeSerializedProperty();
                }
            }
        }
        
        protected boolean ignoreField(Field field) {
            return false;
        }
        
        protected void initializeField(Object object, Field field) {
        }
    }

    public PersistenceService getPersistenceService() {
        return this.persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
    
    public XmlObjectSerializerService getXmlObjectSerializerService() {
        return this.xmlObjectSerializerService;
    }

    public void setXmlObjectSerializerService(XmlObjectSerializerService xmlObjectSerializerService) {
        this.xmlObjectSerializerService = xmlObjectSerializerService;
    }
    
    protected SerializationState createNewDocumentSerializationState(Document document) {
        return new SerializationState();
    }
    
    public class AutoPopulatingListConverter extends CollectionConverter {

    	public AutoPopulatingListConverter(Mapper mapper){
    		super(mapper);
    	}

        @Override
    	public boolean canConvert(Class clazz) {
    		return clazz.equals(AutoPopulatingList.class);
        }

    }

    
}

