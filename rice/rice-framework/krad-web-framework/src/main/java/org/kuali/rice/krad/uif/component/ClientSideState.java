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
package org.kuali.rice.krad.uif.component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that can be used on <code>Component</code> properties to indicate the property
 * value should be exposed in the client and populated back from the client
 *
 * <p>
 * Some components have state that can be altered on the client without making a server call. An
 * example of this is the open state for an <code>Disclosure</code>. When the View is refreshed
 * from the server, the refreshed state needs to reflect the last state before the refresh was made. The
 * framework supports this exposure of state in the client and syncing of the client state to the server
 * component by means of this annotation.
 *
 * During the finalize phase, values for properties that contain this annotation will be pulled and added
 * to the ViewState object that is exposed through JavaScript. The property name/value pair is associated
 * with the component id on the ViewState object so that the state can be updated when the view is refreshed.
 *
 * Properties exposed client side can also be accessed and updated by custom script.
 * e.g.
 * var componentState = ViewState['componentId']; // or ViewState.componentId
 * var propertyValue = componentState['propertyName'];
 * </p>
 *
 * <p>
 * The property will be exposed client side with the identifier given by {@link #variableName()}. If not specified,
 * the name of the property for which the annotation applies will be used
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClientSideState {

    /**
     * Identifier to expose the client side variable as, can be left blank in which case
     * the name of the property the annotation is associated with will be used
     *
     * @return String client side variable name
     */
    public String variableName() default "";

}
