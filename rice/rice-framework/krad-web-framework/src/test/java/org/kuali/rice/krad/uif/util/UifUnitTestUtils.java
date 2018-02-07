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
package org.kuali.rice.krad.uif.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.framework.config.property.SimpleConfig;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewAuthorizer;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.uif.widget.Widget;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * Utilities class for establishing a minimal environment for testing operations involving Uif
 * components.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifUnitTestUtils {

    /**
     * Mock person implementation.
     */
    private static class MockPerson implements Person {

        private static final long serialVersionUID = 5330488987382249417L;

        private final String id;

        private MockPerson(String id) {
            this.id = id;
        }

        @Override
        public void refresh() {}

        @Override
        public String getPrincipalId() {
            return id;
        }

        @Override
        public String getPrincipalName() {
            return id;
        }

        @Override
        public String getEntityId() {
            return id;
        }

        @Override
        public String getEntityTypeCode() {
            return null;
        }

        @Override
        public String getFirstName() {
            return "Test";
        }

        @Override
        public String getFirstNameUnmasked() {
            return "Test";
        }

        @Override
        public String getMiddleName() {
            return "User";
        }

        @Override
        public String getMiddleNameUnmasked() {
            return "User";
        }

        @Override
        public String getLastName() {
            return id;
        }

        @Override
        public String getLastNameUnmasked() {
            return id;
        }

        @Override
        public String getName() {
            return "Test User " + id;
        }

        @Override
        public String getNameUnmasked() {
            return "Test User " + id;
        }

        @Override
        public String getEmailAddress() {
            return null;
        }

        @Override
        public String getEmailAddressUnmasked() {
            return null;
        }

        @Override
        public String getAddressLine1() {
            return null;
        }

        @Override
        public String getAddressLine1Unmasked() {
            return null;
        }

        @Override
        public String getAddressLine2() {
            return null;
        }

        @Override
        public String getAddressLine2Unmasked() {
            return null;
        }

        @Override
        public String getAddressLine3() {
            return null;
        }

        @Override
        public String getAddressLine3Unmasked() {
            return null;
        }

        @Override
        public String getAddressCity() {
            return null;
        }

        @Override
        public String getAddressCityUnmasked() {
            return null;
        }

        @Override
        public String getAddressStateProvinceCode() {
            return null;
        }

        @Override
        public String getAddressStateProvinceCodeUnmasked() {
            return null;
        }

        @Override
        public String getAddressPostalCode() {
            return null;
        }

        @Override
        public String getAddressPostalCodeUnmasked() {
            return null;
        }

        @Override
        public String getAddressCountryCode() {
            return null;
        }

        @Override
        public String getAddressCountryCodeUnmasked() {
            return null;
        }

        @Override
        public String getPhoneNumber() {
            return null;
        }

        @Override
        public String getPhoneNumberUnmasked() {
            return null;
        }

        @Override
        public String getCampusCode() {
            return null;
        }

        @Override
        public Map<String, String> getExternalIdentifiers() {
            return null;
        }

        @Override
        public boolean hasAffiliationOfType(String affiliationTypeCode) {
            return false;
        }

        @Override
        public List<String> getCampusCodesForAffiliationOfType(String affiliationTypeCode) {
            return null;
        }

        @Override
        public String getEmployeeStatusCode() {
            return null;
        }

        @Override
        public String getEmployeeTypeCode() {
            return null;
        }

        @Override
        public KualiDecimal getBaseSalaryAmount() {
            return null;
        }

        @Override
        public String getExternalId(String externalIdentifierTypeCode) {
            return null;
        }

        @Override
        public String getPrimaryDepartmentCode() {
            return null;
        }

        @Override
        public String getEmployeeId() {
            return null;
        }

        @Override
        public boolean isActive() {
            return false;
        }
    }

    /**
     * Mock person service implementation.
     */
    private static class MockPersonService implements PersonService {

        @Override
        public Person getPerson(String principalId) {
            return getMockPerson(principalId);
        }

        @Override
        public List<Person> getPersonByExternalIdentifier(String externalIdentifierTypeCode,
                String externalId) {
            return null;
        }

        @Override
        public Person getPersonByPrincipalName(String principalName) {
            return getMockPerson(principalName);
        }

        @Override
        public Person getPersonByEmployeeId(String employeeId) {
            return null;
        }

        @Override
        public List<Person> findPeople(Map<String, String> criteria) {
            return null;
        }

        @Override
        public List<Person> findPeople(Map<String, String> criteria, boolean unbounded) {
            return null;
        }

        @Override
        public Class<? extends Person> getPersonImplementationClass() {
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        public Map<String, String> resolvePrincipalNamesToPrincipalIds(
                org.kuali.rice.krad.bo.BusinessObject businessObject,
                Map<String, String> fieldValues) {
            return null;
        }

        @Override
        public Person updatePersonIfNecessary(String sourcePrincipalId, Person currentPerson) {
            return null;
        }
    }

    /**
     * Get a mock person object for use in a JUnit test case.
     * 
     * @param id The ID to use for principal name, principal ID, and entity ID.
     * @return A mock person with the supplied ID.
     */
    public static Person getMockPerson(String id) {
        return new MockPerson(id);
    }

    /**
     * Establish a Rice configuration providing enough mock services via
     * {@link GlobalResourceLoader} to support the use of KRAD UIF components in unit tests.
     * 
     * @param applicationId The application ID for the fake environment.
     */
    public static void establishMockConfig(String applicationId) {
        SimpleConfig config = new SimpleConfig();
        config.putProperty("application.id", applicationId);
        ConfigContext.init(config);
        GlobalResourceLoader.addResourceLoader(new ResourceLoader() {

            @Override
            public <T> T getObject(ObjectDefinition definition) {
                return null;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> T getService(QName qname) {
                if (KimApiServiceLocator.KIM_PERSON_SERVICE.equals(qname.getLocalPart())) {
                    return (T) new MockPersonService();
                }
                return null;
            }

            @Override
            public void start() throws Exception {}

            @Override
            public void stop() throws Exception {}

            @Override
            public boolean isStarted() {
                return true;
            }

            @Override
            public void addResourceLoader(ResourceLoader resourceLoader) {}

            @Override
            public void addResourceLoaderFirst(ResourceLoader resourceLoader) {}

            @Override
            public ResourceLoader getResourceLoader(QName name) {
                return null;
            }

            @Override
            public List<QName> getResourceLoaderNames() {
                return null;
            }

            @Override
            public List<ResourceLoader> getResourceLoaders() {
                return null;
            }

            @Override
            public void removeResourceLoader(QName name) {}

            @Override
            public void setName(QName name) {}

            @Override
            public QName getName() {
                return new QName("TEST");
            }

            @Override
            public String getContents(String indent, boolean servicePerLine) {
                return null;
            }
        });
    }

    private static class MockViewAuthorizer implements ViewAuthorizer {

        @Override
        public Set<String> getActionFlags(View view, ViewModel model, Person user, Set<String> actions) {
            return new java.util.HashSet<String>();
        }

        @Override
        public Set<String> getEditModes(View view, ViewModel model, Person user, Set<String> editModes) {
            return new java.util.HashSet<String>();
        }

        @Override
        public boolean canOpenView(View view, ViewModel model, Person user) {
            return true;
        }

        @Override
        public boolean canEditView(View view, ViewModel model, Person user) {
            return true;
        }

        @Override
        public boolean canUnmaskField(View view, ViewModel model, DataField field, String propertyName, Person user) {
            return true;
        }

        @Override
        public boolean canPartialUnmaskField(View view, ViewModel model, DataField field, String propertyName,
                Person user) {
            return true;
        }

        @Override
        public boolean canEditField(View view, ViewModel model, Field field, String propertyName, Person user) {
            return true;
        }

        @Override
        public boolean canViewField(View view, ViewModel model, Field field, String propertyName, Person user) {
            return true;
        }

        @Override
        public boolean canEditGroup(View view, ViewModel model, Group group, String groupId, Person user) {
            return true;
        }

        @Override
        public boolean canViewGroup(View view, ViewModel model, Group group, String groupId, Person user) {
            return true;
        }

        @Override
        public boolean canEditWidget(View view, ViewModel model, Widget widget, String widgetId, Person user) {
            return true;
        }

        @Override
        public boolean canViewWidget(View view, ViewModel model, Widget widget, String widgetId, Person user) {
            return true;
        }

        @Override
        public boolean canPerformAction(View view, ViewModel model, Action action, String actionEvent, String actionId,
                Person user) {
            return true;
        }

        @Override
        public boolean canEditLine(View view, ViewModel model, CollectionGroup collectionGroup,
                String collectionPropertyName, Object line, Person user) {
            return true;
        }

        @Override
        public boolean canViewLine(View view, ViewModel model, CollectionGroup collectionGroup,
                String collectionPropertyName, Object line, Person user) {
            return true;
        }

        @Override
        public boolean canEditLineField(View view, ViewModel model, CollectionGroup collectionGroup,
                String collectionPropertyName, Object line, Field field, String propertyName, Person user) {
            return true;
        }

        @Override
        public boolean canViewLineField(View view, ViewModel model, CollectionGroup collectionGroup,
                String collectionPropertyName, Object line, Field field, String propertyName, Person user) {
            return true;
        }

        @Override
        public boolean canPerformLineAction(View view, ViewModel model, CollectionGroup collectionGroup,
                String collectionPropertyName, Object line, Action action, String actionEvent, String actionId,
                Person user) {
            return true;
        }
    }
    
    /**
     * Establish a user session with the given principal name.
     * 
     * <p>
     * This method will use KIM API calls to look up a person with the provided principal name. Use
     * {@link #establishMockConfig(String)} to set up a mock KIM environment if needed.
     * </p>
     * 
     * @param principalName The principal name of the user to establish a session with.
     */
    public static void establishMockUserSession(String principalName) {
        UserSession session = new UserSession(principalName);
        GlobalVariables.setUserSession(session);
    }
    
    /**
     * Get a view authorizer allowing most operations.
     * @return A view authorizer allowing most operations.
     */
    public static ViewAuthorizer getAllowMostViewAuthorizer() {
        return new MockViewAuthorizer();
    }

}
