/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.test;

import org.junit.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class TestUtilities {
	
	private static Thread exceptionThreader;
	
	private TestUtilities() {
		throw new UnsupportedOperationException("do not call");
	}
	
	/**
     * Waits "indefinately" for the exception routing thread to terminate.
     *
     * This actually doesn't wait forever but puts an upper bound of 5 minutes
     * on the time to wait for the exception routing thread to complete.  If a
     * document cannot go into exception routing within 5 minutes  then we got
     * problems.
     */
    public static void waitForExceptionRouting() {
    	waitForExceptionRouting(5*60*1000);
    }

    public static void waitForExceptionRouting(long milliseconds) {
    	try {
    		Thread thread = getExceptionThreader();
    		if (thread == null) {
    			throw new IllegalStateException("No exception thread was established, likely message is not being processed for exception routing.");
    		}
    		thread.join(milliseconds);
    	} catch (InterruptedException e) {
    		Assert.fail("This thread was interuppted while waiting for exception routing.");
    	}
    	if (getExceptionThreader().isAlive()) {
    		Assert.fail("Document was not put into exception routing within the specified amount of time " + milliseconds);
    	}
    }

    public static Thread getExceptionThreader() {
        return exceptionThreader;
    }

    public static void setExceptionThreader(Thread exceptionThreader) {
        TestUtilities.exceptionThreader = exceptionThreader;
    }

    protected static boolean contains(Class[] list, Class target) {
        for (Class c: list) {
            if (c.getName().equals(target.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method facilitates using annotations in a unit test class hierarchy.  We walk up the class hierarchy
     * and on each class, looking for the presence of any of the specified annotation types.  If the particular class
     * defines one of the annotation types, it is marked for handling.  Once any single target annotation is found
     * on the class, it is marked and no further annotations are inspected.
     * 
     * If the annotation defines an 'overrideSuperClasses' method, and this method returns false, then processing
     * continues up the class hierarchy.  Otherwise processing stops when the first annotation is found.  Note that
     * this feature only makes sense when specifying a single annotation type.
     * 
     * After a list of classes in descending hierarchy order is compiled, the list is iterated over (again, in
     * descending hierarchy order) and if the class is not already present in the caller-supplied list of classes
     * already handled by the caller, the class is added to a list of classes that need to be handled by the caller,
     * which is then returned to the caller.
     * 
     * It is the caller's responsibility to handle the returned classes, and store them in some internal list which it may
     * give back to this method in the future.
     * 
     * @throws Exception if there is a problem in reflection on an Annotation object
     */
    public static List<Class> getHierarchyClassesToHandle(Class testClass, Class[] annotationClasses, Set<String> classesHandled) throws Exception {
        List<Class> classesThatNeedHandling = new ArrayList<Class>();
        // get a list of all classes the current class extends from that use the PerSuiteUnitTestData annotation
        List<Class> classesToCheck = new ArrayList<Class>();
        // here we get the list apart checking the annotations to support the perSuiteDataLoaderLifecycleNamesRun variable better
        {
            Class clazz = testClass;
            superClassLoop: while (!clazz.getName().equals(Object.class.getName())) {
                for (Annotation annotation : clazz.getDeclaredAnnotations()) {
                    // if this isn't one of the annotations we're interested in, move on
                    if (!contains(annotationClasses, annotation.annotationType())) {
                        continue;
                    }

                    // this class should be processed because it contains an annotation we are interested in
                    classesToCheck.add(0, clazz);
                    
                    // now check to see if annotation overrides super class implementations
                    if (annotationOverridesSuperClass(annotation)) {
                        // we're done here
                        break superClassLoop;
                    }
                    // we just added the class to classes to check, we don't need to add it again
                    // so just stop looking at annotations in this particular class
                    break;
                }
                clazz = clazz.getSuperclass();
            }
        }
        
        for (Class clazz: classesToCheck) {
            if (!classesHandled.contains(clazz.getName())) {
                classesThatNeedHandling.add(clazz);
            }
        }
        return classesThatNeedHandling;
    }
    
    /**
     * Determines whether an annotation should override the same type of annotation on a superclass,
     * by using reflection to invoke the 'overrideSuperClasses' method on the annotation if it exists.
     * If the annotation does not supply this method, the default is true.
     * @param annotation the annotation to inspect
     * @return whether this annotation overrides any annotations of similar type in super classes
     * @throws Exception if an error occurs during reflection
     */
    protected static boolean annotationOverridesSuperClass(Annotation annotation) throws Exception {
        boolean overrides = true; // default is to just override
        Method m = null;;
        try {
            m = annotation.getClass().getMethod("overrideSuperClasses", null);
        } catch (NoSuchMethodException nsme) {
            // do nothing
        }
        if (m != null) {
            Object result = m.invoke(annotation, (Object[]) null);
            if (result instanceof Boolean) {
                overrides = (Boolean) result;
            } else {
                throw new RuntimeException("Annotation 'overrideSuperClasses' did not return Boolean value");
            }
        }
        return overrides;
    }
}
