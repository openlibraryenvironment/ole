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
package org.kuali.rice.krad.devtools.doclet;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * A JavaDoc tools doclet class that generates a properties file containing information about
 * any @BeanTag classes @BeanTagAttribute properties
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KRADLibraryPropertiesDoclet {

    // TODO : remove hard coded arguments
    public static void main(String[] args) {
        Main.execute(new String[]{"-doclet", "org.kuali.rice.krad.demo.uif.library.tools.KRADLibraryPropertiesDoclet",
                "-sourcepath", "C:/Java/Projects/Rice/Trunk/krad/krad-web-framework/src/main/java", "-subpackages",
                "org.kuali.rice.krad.uif:org.kuali.rice.krad.datadictionary.validation.constraint"});
    }

    /*
     * The method that will be called by the JavaDoc tool when executing this doclet
     *
     * @param root - the RootDoc containing all the class information as specified by the javadoc call arguments
     * @return boolean
     */
    public static boolean start(RootDoc root) {
        storeToPropertyFile(root);
        return true;
    }

    /*
     * Writes all BeanTag properties to a propties file with their getter javadoc description
     *
     * @param root- the RootDoc containing all the class information as specified by the javadoc call arguments
     */
    private static void storeToPropertyFile(RootDoc root) {
        ClassDoc[] classes = root.classes();
        SortedProperties prop = new SortedProperties();

        for (ClassDoc classDoc : classes) {

            // Check only BeanTag classes
            if (isAnnotatedWith(classDoc, "org.kuali.rice.krad.datadictionary.parse.BeanTag",
                    "org.kuali.rice.krad.datadictionary.parse.BeanTags")) {

                String className = classDoc.qualifiedName();
                String classDescription = classDoc.commentText();
                prop.setProperty(className, classDescription);

                MethodDoc[] methods = classDoc.methods();

                for (MethodDoc methodDoc : methods) {

                    // Check only getters that i BeanTagAttribute annotated
                    if (methodDoc.parameters().length == 0 &&
                            (methodDoc.name().startsWith("get") || methodDoc.name().startsWith("is")) &&
                            !(methodDoc.name().equals("get") || methodDoc.name().equals("is")) &&
                            isAnnotatedWith(methodDoc, "org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute")) {
                        String methodName;

                        if (methodDoc.name().startsWith("get")) {
                            methodName = methodDoc.name().replaceFirst("get", "");
                        } else {
                            methodName = methodDoc.name().replaceFirst("is", "");
                        }
                        String propertyName = Character.toLowerCase(methodName.charAt(0)) + (methodName.length() > 1 ?
                                methodName.substring(1) : "");
                        String propertyType = methodDoc.returnType().typeName();
                        String propertyDescription = getDocText(methodDoc, root);

                        prop.setProperty(className + "|" + propertyName + "|" + propertyType, propertyDescription);
                    }
                }
            }
        }

        // TODO : remove hard coding of filepath
        try {
            prop.store(new FileOutputStream("C:/Java/Projects/Rice/Trunk/sampleapp/src/main/resources/"
                    + "org/kuali/rice/devtools/krad/documentation/PropertiesDescription.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Returns the javadoc text for a method, when it is a @see comment the referenced methods documentation will
     * be returned
     *
     * @param method
     * @return String
     */
    private static String getDocText(MethodDoc method, RootDoc root) {

        if (method.commentText() != null && !method.commentText().equals("")) {
            return method.commentText();
        }

        String docText = method.getRawCommentText();

        if (docText != null && docText.contains("@see")) {
            String classMethodName = docText.replace("@see", "").replace("\n", "").replace(" ", "");
            String className = classMethodName.substring(0, classMethodName.indexOf("#"));
            String methodName = classMethodName.substring(classMethodName.indexOf("#") + 1).replace("()", "");
            ClassDoc classDoc = root.classNamed(className);

            if (classDoc == null) {
                System.err.println("warning - Comment on "
                        + method.toString()
                        + " does not have valid fully qualified "
                        + "method in @see annotation.\n"
                        + docText);
                return "";
            }
            MethodDoc methodDoc = getNoParamMethodFromClassDocByName(classDoc, methodName);
            return methodDoc.commentText();
        }

        return "";
    }

    /*
     * Finds the no arguments method's MethodDoc on a class with a specified name
     *
     * @param classDoc
     * @param methodName
     * @return MethodDoc
     */
    private static MethodDoc getNoParamMethodFromClassDocByName(ClassDoc classDoc, String methodName) {
        MethodDoc[] methods = classDoc.methods();

        for (MethodDoc methodDoc : methods) {

            if (methodDoc.name().equals(methodName) && methodDoc.parameters().length == 0) {
                return methodDoc;
            }
        }
        return null;
    }

    /*
     * Checks if a specific ClassDoc or MethodDoc is annotated with the specified annotations
     *
     * @param elementDoc
     * @param tagString
     * @return
     */
    private static boolean isAnnotatedWith(ProgramElementDoc elementDoc, String... tagString) {
        AnnotationDesc[] annotations = elementDoc.annotations();

        for (AnnotationDesc annotation : annotations) {

            if (Arrays.asList(tagString).contains(annotation.annotationType().toString())) {
                return true;
            }
        }

        return false;
    }

    /*
     * This class keeps properties sorted in order to make the file easier to read
     */
    static class SortedProperties extends Properties {

        public Enumeration keys() {
            Enumeration keysEnum = super.keys();
            Vector<String> keyList = new Vector<String>();

            while (keysEnum.hasMoreElements()) {
                keyList.add((String) keysEnum.nextElement());
            }
            Collections.sort(keyList);
            return keyList.elements();
        }
    }

}
