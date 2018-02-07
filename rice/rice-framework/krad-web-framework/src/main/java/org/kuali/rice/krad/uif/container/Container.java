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
package org.kuali.rice.krad.uif.container;

import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Header;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.element.ValidationMessages;
import org.kuali.rice.krad.uif.layout.LayoutManager;
import org.kuali.rice.krad.uif.widget.Helpable;

import java.util.List;
import java.util.Set;

/**
 * Type of component that contains a collection of other components. All
 * templates for {@code Container} components must use a
 * {@code LayoutManager} to render the contained components.
 *
 * Each container has the following parts in addition to the contained components:
 * <ul>
 * <li>{@code HeaderField}</li>
 * <li>Summary {@code Message}</li>
 * <li>Help component</li>
 * <li>Errors container</li>
 * <li>Footer {@code Group}</li>
 * </ul>
 * Container implementations are free to add additional content as needed.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 * @see org.kuali.rice.krad.uif.component.Component
 */
public interface Container extends Component, Helpable {

	/**
     * {@code List} of {@code Component} instances that are held by
     * the container
     *
     * <p>
     * Contained components are rendered within the section template by calling
     * the associated {@code LayoutManager}.
     * </p>
     *
     * @return List component instances
     */
	List<? extends Component> getItems();

    /**
     * Setter for the containers list of components
     *
     * @param items  list of components to set in container
     */
    void setItems(List<? extends Component> items);

	/**
     * {@code Set} of {@code Component} classes that may be placed
     * into the container
     *
     * <p>
     * If an empty or null list is returned, it is assumed the container
     * supports all components. The returned set will be used by dictionary
     * validators and allows renders to make assumptions about the contained
     * components
     * </p>
     *
     * @return Set component classes
     */
	Set<Class<? extends Component>> getSupportedComponents();

	/**
     * {@code LayoutManager} that should be used to layout the components
     * in the container
     *
     * <p>
     * The template associated with the layout manager will be invoked passing
     * in the List of components from the container. This list is exported under
     * the attribute name 'items'
     * </p>
     *
     * @return LayoutManager instance
     */
	LayoutManager getLayoutManager();

	/**
	 * Setter for the containers layout manager
	 *
	 * @param layoutManager
	 */
	void setLayoutManager(LayoutManager layoutManager);

	/**
     * {@code HeaderField} associated with the container
     *
     * <p>
     * Header fields are generally rendered at the beginning of the container to
     * indicate a grouping, although this is determined by the template
     * associated with the container. The actual rendering configuration (style
     * and so on) is configured within the HeaderField instance
     * </p>
     * <p>
     * Header is only rendered if {@code Container#isRenderHeader} is true
     * and getHeader() is not null
     * </p>
     *
     * @return HeaderField instance or Null
     */
	Header getHeader();

	/**
	 * Setter for the containers header field
	 *
	 * @param header
	 */
	void setHeader(Header header);

	/**
     * Footer {@code Group} associated with the container
     *
     * <p>
     * The footer is usually rendered at the end of the container. Often this is
     * a place to put actions (buttons) for the container.
     * </p>
     * <p>
     * Footer is only rendered if {@code Container#isRenderFooter} is true
     * and getFooter is not null
     * </p>
     *
     * @return Group footer instance or Null
     */
	Group getFooter();

	/**
	 * Setter for the containers footer
	 *
	 * @param footer
	 */
	void setFooter(Group footer);

	/**
     * Text for the container that provides a summary description or
     * instructions
     *
     * <p>
     * Text is encapsulated in a {@code Message} that contains
     * rendering configuration.
     * </p>
     * <p>
     * Summary {@code Message} only rendered if this methods does not
     * return null
     * </p>
     *
     * @return Message instance or Null
     */
	Message getInstructionalMessage();

	/**
	 * Setter for the containers summary message field
	 *
	 * @param instructionalMessage
	 */
	void setInstructionalMessage(Message instructionalMessage);

	/**
     * Field that contains the error messages for the container
     *
     * <p>
     * Containers can collect the errors for the contained component and display
     * either all the messages or counts. This {@code Field} is used to
     * render those messages. Styling and other configuration is done through
     * the {@code ValidationMessages}
     * </p>
     *
     * @return ValidationMessages holding the container errors
     */
	ValidationMessages getValidationMessages();

	/**
	 * Setter for the containers errors field
	 *
	 * @param validationMessages
	 */
	void setValidationMessages(ValidationMessages validationMessages);

}
