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
package org.kuali.rice.scripts

import groovy.util.logging.Log

/**
 * Handles parsing of java class files
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
public class ClassParserUtils {
    // TODO: consider switching to groovy JavaRecognizer and do AST mapping

    static def defaultClassMap = ["package": "", "author": "", className: "", "accessModifier": "", parentClass: "", interfaces: [], "annotations": [], "imports": [], "members": [], "methods": []]

    /**
     * extracts relevant data from class file (used for action form and classes)
     *
     * @param fileText
     * @param verifyImports
     * @return
     */
    static def parseClassFile(fileText, verifyImports) {
        def classMap = ["package": "", "author": "", className: "", "accessModifier": "", parentClass: "", interfaces: [], "annotations": [], "imports": [], "members": [], "methods": []]

        def annotations = []
        fileText.eachLine { line ->
            if (line =~ /^\s*[\/|\*]/) {
                // comments
                // skip comments
            }
            if (line =~ /^\s*@/) {
                // visitAnnotationDef
                // annotations
                annotations.add(line)
            }
            if (line =~ /^\s*package/) {
                // visitPackageDef
                // packages
                def matchPackage = line =~ /package\s+(.*?);/
                classMap.package = matchPackage[0][1]
            }
            if (line =~ /^\s*import\s+(.*);/) {
                // visitImport
                // imports
                def importClass = parseImport(line, verifyImports)
                if (importClass != null && importClass != "") {
                    classMap.imports.add(importClass)
                }
            } else if (line =~ /^\s*public\s*class\s+/) {
                // visitClassDef
                def classElement = parseClassDeclaration(line, annotations)
                classMap.putAll(classElement)
                annotations = [] // clears annotations so they can accumulate
            } else if (line =~ /^\s*(public|private|protected)\s+.*?\;/) {
                // visitVariableDef
                classMap.members.add(parseFieldDeclaration(line))
                annotations = []
            } else if (line =~ /^\s*(public|private|protected)\s+.*?\(.*?\)/) {
                // visitMethodDef
                def methodElement = parseMethodDeclaration(line, annotations)
                classMap.methods.add(methodElement)
                annotations = []
            }
        }
        return classMap
    }

    // helper functions related to parseClassFile

    static def parseFieldDeclaration(lineText) {
        def field = [accessModifier: "", nonAccessModifiers: [], "fieldType": "", fieldName: "", "annotations": []]
        def matchesField = lineText =~ /^\s*(public|private|protected)((?:\s*(?:static|final|abstract))*)\s+(.*?)\s+(.*?)(?:\s*=\s*(.*?))?\;/
        if (matchesField.size() > 0) {
            log.finer "matches member data " + matchesField[0]
            field.accessModifier = matchesField[0][1]
            field.nonAccessModifiers = matchesField[0][2]?.replaceFirst(/^\s+/, "").split()
            field.fieldType = matchesField[0][3]
            field.fieldName = matchesField[0][4]
            field.fieldValue = matchesField[0][5]
        }
        return field
    }


    static def parseImport(lineText, verifyImports) {
        def importString = ""
        def matchImport = lineText =~ /import\s+(.*?);/
        def importClass = matchImport[0][1]
        // if package exists, add to list
        if (verifyImports) {
            if (null != Package.getPackage(importClass)) {
                importString = importClass
            }
        } else {
            importString = importClass
        }
        return importString
    }

    static def parseMethodDeclaration(lineText, annotations) {
        def methodElement = [accessModifier: "", nonAccessModifiers: [], "returnType": "",
                methodName: "", "parameters": [], "exceptions": [], "annotations": []]

        def matchesMethod = lineText =~ /^\s*(public|private|protected)((?:\s*(?:static|final|abstract))*)\s+(.*?)\s+(.*?)\(((?:.*?\s+.*?\,)*?(?:.*?\s+.*?)?)\)\s*(?:(?:throws\s+(.*?))?)\s*\{/
        if (matchesMethod.size() > 0) {
            log.finer "matches member data " + matchesMethod[0][4]
            matchesMethod[0].each {
                log.finer "matches member data " + it
            }
            methodElement.accessModifier = matchesMethod[0][1]
            methodElement.nonAccessModifiers = matchesMethod[0][2]?.replaceFirst(/^\s+/, "").split()
            methodElement.returnType = matchesMethod[0][3]
            methodElement.methodName = matchesMethod[0][4]
            methodElement.parameters = matchesMethod[0][5]?.tokenize(",")
            methodElement.exceptions = matchesMethod[0][6]?.tokenize(",")
        }
        log.finer "method values are " + methodElement
        return methodElement

    }

    /**
     * parse class declaration into class element (accessModifier, className, parentClass, interfaces)
     *
     * @param lineText
     * @param annotations
     * @return
     */
    static def parseClassDeclaration(lineText, annotations) {
        def classElement = [accessModifier: "", className: "", parentClass: "", interfaces: []]
        def matchClass = lineText =~ /^\s*(public|private|protected)\s+class\s+(.+?)\b/
        if (matchClass.size() > 0) {
            log.finer "found match " + matchClass[0]
            classElement.accessModifier = matchClass[0][1]
            classElement.className = matchClass[0][2]
        }
        def matchExtends = lineText =~ /\bextends\s+(.+?)\b/
        if (matchExtends.size() > 0) {
            classElement.parentClass = matchExtends[0][1]
        }
        def matchImplements = lineText =~ /\bimplements\s+((?:\w+\,\s*)*\w+)\b/
        if (matchImplements.size() > 0) {
            classElement.interfaces.addAll(matchImplements[0][1]?.tokenize(','))
        }

        log.finer "class values are: " + classElement
        return classElement
    }

}