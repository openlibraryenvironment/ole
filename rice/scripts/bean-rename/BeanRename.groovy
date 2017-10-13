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
def currentDir = new File(".");

// NOTICE: The copy right at the beginning of beanReplacements.txt must be removed before
// running this program

def inputFile = new File("beanReplacements.txt");
def replaceMap = new HashMap();

inputFile.splitEachLine(',') {fields ->
    replaceMap.put(fields[0], fields[1]);
}

def backupFile;
def fileText;

currentDir.eachFileRecurse(
        {file ->
            if (file.isFile() && file.name.endsWith(".xml")) {
                fileText = file.text;
                //backupFile = new File(file.path + ".bak");
                //backupFile.write(fileText);
                replaceMap.each {
                    fileText = fileText.replaceAll("parent=\"" + it.key + "\"", "parent=\"" + it.value + "\"");
                    fileText = fileText.replaceAll("ref bean=\"" + it.key + "\"", "ref bean=\"" + it.value + "\"");
                    fileText = fileText.replaceAll("id=\"" + it.key + "\"", "id=\"" + it.value + "\"");
                    fileText = fileText.replaceAll("-ref=\"" + it.key + "\"", "-ref=\"" + it.value + "\"");
                    fileText = fileText.replaceAll("parent=\"" + it.key + "-parentBean\"", "parent=\"" + it.value + "-parentBean\"");
                    fileText = fileText.replaceAll("id=\"" + it.key + "-parentBean\"", "id=\"" + it.value + "-parentBean\"");
                    fileText = fileText.replaceAll("ref bean=\"" + it.key + "-parentBean\"", "ref bean=\"" + it.value + "-parentBean\"");

                    // for properties check p namespace and property tag, also check contains for nested
                    def propertyMatcher = "(p:(\\w+\\.)*)${it.key}([\\s=\\.]+)";
                    fileText = fileText.replaceAll(/${propertyMatcher}/, '$1' + it.value + '$3');

                    propertyMatcher = "(property name=\"(\\w+\\.)*)${it.key}([\\.\"]+)";
                    fileText = fileText.replaceAll(/${propertyMatcher}/, '$1' + it.value + '$3');
                }
                file.write(fileText);
            }
        }
);
