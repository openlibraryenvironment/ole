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
package org.kuali.rice.krad.util;

import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.web.form.UifFormManager;
import org.kuali.rice.core.framework.util.ApplicationThreadLocal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Holds all of our thread local variables and accessors for those
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class GlobalVariables {

    private static ThreadLocal<LinkedList<GlobalVariables>> GLOBAL_VARIABLES_STACK = new ApplicationThreadLocal<LinkedList<GlobalVariables>>() {
        protected LinkedList<GlobalVariables> initialValue() {
            LinkedList<GlobalVariables> globalVariablesStack = new LinkedList<GlobalVariables>();
            globalVariablesStack.add(new GlobalVariables());
            return globalVariablesStack;
        }
    };

    private static GlobalVariables getCurrentGlobalVariables() {
        return GLOBAL_VARIABLES_STACK.get().getLast();
    }

    static GlobalVariables pushGlobalVariables() {
        GlobalVariables vars = new GlobalVariables();
        GLOBAL_VARIABLES_STACK.get().add(vars);
        return vars;
    }

    static GlobalVariables popGlobalVariables() {
        return GLOBAL_VARIABLES_STACK.get().removeLast();
    }

    static void reset() {
        LinkedList<GlobalVariables> stack = GLOBAL_VARIABLES_STACK.get();
        stack.clear();
        stack.add(new GlobalVariables());
    }

    private UserSession userSession = null;
    private String hideSessionFromTestsMessage = null;
    private MessageMap messageMap = new MessageMap();
    private Map<String,Object> requestCache = new HashMap<String, Object>();
    private UifFormManager uifFormManager = null;

    private GlobalVariables() {}

    /**
     * @return the UserSession that has been assigned to this thread of execution it is important that this not be called by
     *         anything that lives outside
     */
    public static UserSession getUserSession() {
        GlobalVariables vars = getCurrentGlobalVariables();
        String message = vars.hideSessionFromTestsMessage;
        if (message != null) {
            throw new RuntimeException(message);
        }
        return vars.userSession;
    }

    /**
     * Sets an error message for tests that try to use the session without declaring it.
     * This method should be use by only KualiTestBase, not by other test code and especially not by production code.
     *
     * @param message the detail to throw, or null to allow access to the session
     */
    public static void setHideSessionFromTestsMessage(String message) {
        GlobalVariables vars = getCurrentGlobalVariables();
        vars.hideSessionFromTestsMessage = message;
    }

    /**
     * sets the userSession object into the global variable for this thread
     *
     * @param userSession
     */
    public static void setUserSession(UserSession userSession) {
        GlobalVariables vars = getCurrentGlobalVariables();
        vars.userSession = userSession;
    }

    public static MessageMap getMessageMap() {
        GlobalVariables vars = getCurrentGlobalVariables();
        return vars.messageMap;
    }

    /**
     * Merges a message map into the global variables error map
     * @param messageMap
     */
    public static void mergeErrorMap(MessageMap messageMap) {
        getMessageMap().getErrorMessages().putAll(messageMap.getErrorMessages());
        getMessageMap().getWarningMessages().putAll(messageMap.getWarningMessages());
        getMessageMap().getInfoMessages().putAll(messageMap.getInfoMessages());
    }

    /**
     * Sets a new (clean) MessageMap
     *
     * @param messageMap
     */
    public static void setMessageMap(MessageMap messageMap) {
        GlobalVariables vars = getCurrentGlobalVariables();
        vars.messageMap = messageMap;
    }

    public static Object getRequestCache(String cacheName) {
        GlobalVariables vars = getCurrentGlobalVariables();
        return vars.requestCache.get(cacheName);
    }

    public static void setRequestCache(String cacheName, Object cacheObject) {
        GlobalVariables vars = getCurrentGlobalVariables();
        vars.requestCache.put(cacheName, cacheObject);
    }

    /**
     * Retrieves the {@link org.kuali.rice.krad.web.form.UifFormManager} which can be used to store and remove forms
     * from the session
     *
     * @return UifFormManager
     */
    public static UifFormManager getUifFormManager() {
        GlobalVariables vars = getCurrentGlobalVariables();
        return vars.uifFormManager;
    }

    /**
     * Sets a {@link org.kuali.rice.krad.web.form.UifFormManager} for the current thread
     *
     * @param uifFormManager
     */
    public static void setUifFormManager(UifFormManager uifFormManager) {
        GlobalVariables vars = getCurrentGlobalVariables();
        vars.uifFormManager = uifFormManager;
    }

    /**
     * Clears out GlobalVariable objects with the exception of the UserSession
     */
    public static void clear() {
        GlobalVariables vars = getCurrentGlobalVariables();
        vars.messageMap = new MessageMap();
        vars.requestCache = new HashMap<String,Object>();
    }

    /**
     * Pushes a new GlobalVariables object onto the ThreadLocal GlobalVariables stack, invokes the runnable,
     * and pops the GlobalVariables off in a finally clause
     * @param callable the code to run under a new set of GlobalVariables
     */
    public static <T> T doInNewGlobalVariables(Callable<T> callable) throws Exception {
        return doInNewGlobalVariables(null, callable);
    }

    /**
     * Convenience method that creates a new GlobalVariables stack frame, initialized with the provided
     * UserSession (which may be the previous UserSession).
     * @param userSession the UserSession to initialize the new frame with (may be null)
     * @param callable the code to run under a new set of GlobalVariables
     * @throws Exception
     */
    public static <T> T doInNewGlobalVariables(UserSession userSession, Callable<T> callable) throws Exception {
        try {
            GlobalVariables vars = pushGlobalVariables();
            if (userSession != null) {
                vars.userSession = userSession;
            }
            return callable.call();
        } finally {
            popGlobalVariables();
        }
    }
}