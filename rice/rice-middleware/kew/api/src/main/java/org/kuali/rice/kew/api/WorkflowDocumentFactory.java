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
package org.kuali.rice.kew.api;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.kew.api.action.InvalidActionTakenException;
import org.kuali.rice.kew.api.doctype.IllegalDocumentTypeException;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.DocumentUpdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Factory which manufactures WorkflowDocuments.  This is the main entry point for interaction with the
 * Kuali Enterprise Workflow System.
 *
 * The WorkflowDocumentFactory uses the {@link org.kuali.rice.kew.impl.document.WorkflowDocumentProvider} SPI as a strategy
 * for creating WorkflowDocument instances.
 *
 * The provider class is specified in the following file in the class loader: "META-INF/services/org.kuali.rice.kew.api.WorkflowDocument",
 * and should implement the WorkflowDocumentProvider interface.
 */
public final class WorkflowDocumentFactory {

    private static final String CREATE_METHOD_NAME = "createDocument";
    private static final String LOAD_METHOD_NAME = "loadDocument";

    /**
     * A lazy initialization holder class for the Provider.  Allows for
     * thread-safe initialization of shared resource.
     *
     * NOTE: ProviderHolder and its fields are static, therefore there
     * will only be a simple WorkflowDocumentProvider instance so it needs to be
     * thread-safe.
     */
    private static final class ProviderHolder {
        static final Object provider;
        static final Method createMethod;
        static final Method loadMethod;
        static {
            provider = loadProvider();
            createMethod = locateCreateMethod(provider);
            loadMethod = locateLoadMethod(provider);
        }
    }

    /**
     * Creates a new workflow document of the given type with the given initiator.
     * 
     * @param principalId the document initiator
     * @param documentTypeName the document type
     * 
     * @return a WorkflowDocument object through which to interact with the new workflow document
     * 
     * @throws IllegalArgumentException if principalId is null or blank
     * @throws IllegalArgumentException if documentTypeName is null or blank
     * @throws IllegalDocumentTypeException if the document type does not allow for creation of a document,
     * this can occur when the given document type is used only as a parent and has no route path configured
     * @throws InvalidActionTakenException if the caller is not allowed to execute this action
     */
    public static WorkflowDocument createDocument(String principalId, String documentTypeName) {
        return createDocument(principalId, documentTypeName, null, null);
    }

    /**
     * Creates a new workflow document of the given type with the given initiator.
     *
     * @param principalId the document initiator
     * @param documentTypeName the document type
     * @param title the title of the new document
     *
     * @return a WorkflowDocument object through which to interact with the new workflow document
     * 
     * @throws IllegalArgumentException if principalId is null or blank
     * @throws IllegalArgumentException if documentTypeName is null or blank
     * @throws IllegalDocumentTypeException if documentTypeName does not represent a valid document type
     */
    public static WorkflowDocument createDocument(String principalId, String documentTypeName, String title) {
        DocumentUpdate.Builder builder = DocumentUpdate.Builder.create();
        builder.setTitle(title);
        return createDocument(principalId, documentTypeName, builder.build(), null);
    }

    /**
     * Creates a new workflow document of the given type with the given initiator.
     *
     * @param principalId the document initiator
     * @param documentTypeName the document type
     * @param documentUpdate pre-constructed state with which to initialize the document
     * @param documentContentUpdate pre-constructed document content with which to initialize the document
     *
     * @return a WorkflowDocument object through which to interact with the new workflow document
     * 
     * @throws IllegalArgumentException if principalId is null or blank
     * @throws IllegalArgumentException if documentTypeName is null or blank
     * @throws IllegalDocumentTypeException if documentTypeName does not represent a valid document type
     * @see org.kuali.rice.kew.impl.document.WorkflowDocumentProvider#createDocument(String, String, DocumentUpdate, DocumentContentUpdate)
     */
    public static WorkflowDocument createDocument(String principalId, String documentTypeName, DocumentUpdate documentUpdate, DocumentContentUpdate documentContentUpdate) {
        if (StringUtils.isBlank(principalId)) {
            throw new IllegalArgumentException("principalId was null or blank");
        }
        if (StringUtils.isBlank(documentTypeName)) {
            throw new IllegalArgumentException("documentTypeName was null or blank");
        }

        Object workflowDocument = null;

        try {
            workflowDocument = ProviderHolder.createMethod.invoke(ProviderHolder.provider, principalId, documentTypeName, documentUpdate, documentContentUpdate);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("Failed to invoke " + CREATE_METHOD_NAME, e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException)e.getCause();
            }
            throw new ConfigurationException("Failed to invoke " + CREATE_METHOD_NAME, e);
        }

        if (!(workflowDocument instanceof WorkflowDocument)) {
            throw new ConfigurationException("Created document is not a proper instance of " + WorkflowDocument.class + ", was instead " + workflowDocument.getClass());
        }
        return (WorkflowDocument)workflowDocument;
    }

    /**
     * Loads an existing workflow document.
     * @param principalId the principal id under which to perform document actions
     * @param documentId the id of the document to load
     *
     * @return a WorkflowDocument object through which to interact with the loaded workflow document
     *
     * @throws IllegalArgumentException if principalId is null or blank
     * @throws IllegalArgumentException if documentTypeName is null or blank
     * @throws IllegalDocumentTypeException if the specified document type is not active
     * @throws IllegalDocumentTypeException if the specified document type does not support document
     *         creation (in other words, it's a document type that is only used as a parent)
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     * @see org.kuali.rice.kew.impl.document.WorkflowDocumentProvider#loadDocument(String, String)
     */
    public static WorkflowDocument loadDocument(String principalId, String documentId) {
        if (StringUtils.isBlank(principalId)) {
            throw new IllegalArgumentException("principalId was null or blank");
        }
        if (StringUtils.isBlank(documentId)) {
            throw new IllegalArgumentException("documentId was null or blank");
        }

        Object workflowDocument = null;

        try {
            workflowDocument = ProviderHolder.loadMethod.invoke(ProviderHolder.provider, principalId, documentId);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("Failed to invoke " + LOAD_METHOD_NAME, e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException)e.getCause();
            }
            throw new ConfigurationException("Failed to invoke " + LOAD_METHOD_NAME, e);
        }

        if (!(workflowDocument instanceof WorkflowDocument)) {
            throw new ConfigurationException("Loaded document is not a proper instance of " + WorkflowDocument.class + ", was instead " + workflowDocument.getClass());
        }
        return (WorkflowDocument)workflowDocument;
    }

    /**
     * Loads a global WorkflowDocumentProvider implementation
     * @return the WorkflowDocumentProvider
     */
    private static Object loadProvider() {
        String providerClassName = null;
        String resource = null;
        try {
            resource = new StringBuilder().append("META-INF/services/").append(WorkflowDocument.class.getName()).toString();
            final InputStream resourceStream = ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream(resource.toString());
            if (resourceStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream, "UTF-8"));
                providerClassName = reader.readLine().trim();
                reader.close();
                Class<?> providerClass = Class.forName(providerClassName);
                return newInstance(providerClass);
            } else {
                throw new ConfigurationException("Failed to locate a services definition file at " + resource);
            }
        } catch (IOException e) {
            throw new ConfigurationException("Failure processing services definition file at " + resource, e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Failed to load provider class: " + providerClassName, e);
        }
    }

    private static Object newInstance(Class<?> providerClass) {
        try {
            return providerClass.newInstance();
        } catch (InstantiationException e) {
            throw new ConfigurationException("Failed to instantiate provider class: " + providerClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("Failed to instantiate provider class: " + providerClass.getName(), e);
        }
    }

    private static Method locateCreateMethod(Object provider) {
        try {
            return provider.getClass().getMethod(CREATE_METHOD_NAME, String.class, String.class, DocumentUpdate.class, DocumentContentUpdate.class);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("Failed to locate valid createDocument method signature on provider class: " + provider.getClass().getName(), e);
        } catch (SecurityException e) {
            throw new ConfigurationException("Encountered security issue when attempting to access createDocument method on provider class: " + provider.getClass().getName(), e);
        }
    }

    private static Method locateLoadMethod(Object provider) {
        try {
            return provider.getClass().getMethod(LOAD_METHOD_NAME, String.class, String.class);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("Failed to locate valid createDocument method signature on provider class: " + provider.getClass().getName(), e);
        } catch (SecurityException e) {
            throw new ConfigurationException("Encountered security issue when attempting to access createDocument method on provider class: " + provider.getClass().getName(), e);
        }
    }

}
