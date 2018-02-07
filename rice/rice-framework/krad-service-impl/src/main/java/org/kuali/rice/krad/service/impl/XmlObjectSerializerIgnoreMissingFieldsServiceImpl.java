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
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl;
import org.kuali.rice.krad.service.PersistenceService;
import org.kuali.rice.krad.service.util.DateTimeConverter;

import java.util.ArrayList;

/**
 * Service implementation for the XmlObjectSerializer structure. This is the default implementation that gets
 * delivered with Kuali. It utilizes the XStream open source libraries and framework
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XmlObjectSerializerIgnoreMissingFieldsServiceImpl extends XmlObjectSerializerServiceImpl {
	private static final Log LOG = LogFactory.getLog(XmlObjectSerializerIgnoreMissingFieldsServiceImpl.class);

	private PersistenceService persistenceService;

	private XStream xstream;

	public XmlObjectSerializerIgnoreMissingFieldsServiceImpl() {

        xstream = new XStream(new ProxyAwareJavaReflectionProvider()) {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn,
                            String fieldName) {
                        if (definedIn == Object.class) {
                            return false;
                        }
                      return super.shouldSerializeMember(definedIn, fieldName);
                   }
               };
           }
       };

		xstream.registerConverter(new ProxyConverter(xstream.getMapper(), xstream.getReflectionProvider() ));
		xstream.addDefaultImplementation(ArrayList.class, ListProxyDefaultImpl.class);
        xstream.registerConverter(new DateTimeConverter());
	}

    /**
     * @see org.kuali.rice.krad.service.XmlObjectSerializer#fromXml(java.lang.String)
     *
     *  Fields on the XML that do not exist on the class will be ignored.
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
}
