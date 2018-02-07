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
package org.kuali.rice.krad.web.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A session listener that detects when a non-serializable attributes is added to session.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NonSerializableSessionListener implements HttpSessionAttributeListener {
    private static final Log LOG = LogFactory.getLog(NonSerializableSessionListener.class);
    private static final String ENABLE_SERIALIZATION_CHECK = "enableSerializationCheck";
    private Boolean serializationCheckEnabled;

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        logSerializationViolations(se, "added");
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        //do nothing
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        logSerializationViolations(se, "replaced");
    }

    /**
     * Tests and logs serialization violations in non-production environments
     */
    private void logSerializationViolations(HttpSessionBindingEvent se, String action) {
        if (!productionEnvironmentDetected() && isSerializationCheckEnabled()) {
            checkSerialization(se, action);
        }
    }

    /**
     * Determines whether we are running in a production environment.  Factored out for testability.
     */
    private static boolean productionEnvironmentDetected() {
        Config c = ConfigContext.getCurrentContextConfig();
        return c != null && c.isProductionEnvironment();
    }

    /**
     * Determines whether we are running in a production environment.  Factored out for testability.
     */
    private Boolean isSerializationCheckEnabled() {
        if (serializationCheckEnabled == null) {
            Config c = ConfigContext.getCurrentContextConfig();
            serializationCheckEnabled = c != null && c.getBooleanProperty(ENABLE_SERIALIZATION_CHECK);
        }
        return serializationCheckEnabled;
    }



    /**
     * Tests whether the attribute value is serializable and logs an error if it isn't.  Note, this can be expensive
     * so we avoid it in production environments.
     * @param se the session binding event
     * @param action the listener event for logging purposes (added or replaced)
     */
    protected void checkSerialization(final HttpSessionBindingEvent se, String action) {
        final Object o = se.getValue();
        if(o != null) {
            if (!isSerializable(o)) {
                LOG.error("Attribute of class " + o.getClass().getName() + " with name " + se.getName() + " from source " + se.getSource().getClass().getName() + " was " + action + " to session and does not implement " + Serializable.class.getName());
            } else if (!canBeSerialized((Serializable) o)){
                LOG.error("Attribute of class " + o.getClass().getName() + " with name " + se.getName() + " from source " + se.getSource().getClass().getName() + " was " + action + " to session and cannot be Serialized");
            }
        }
    }

    /**
     * Simply tests whether the object implements the Serializable interface
     */
    private static boolean isSerializable(Object o) {
        return o instanceof Serializable;
    }

    /**
     * Performs an expensive test of serializability by attempting to serialize the object graph
     */
    private static boolean canBeSerialized(Serializable o) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream out = null;
        try {
            baos = new ByteArrayOutputStream(512);
            out = new ObjectOutputStream(baos);
            out.writeObject((Serializable) o);
            return true;
        } catch (IOException e) {
            LOG.warn("error serializing object" , e);
        } finally {
            try {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        LOG.warn("error closing stream" , e);
                    }
                }
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                         LOG.warn("error closing stream" , e);
                    }
                }
            }
        }

        return false;
    }
}