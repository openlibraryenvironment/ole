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
package org.kuali.rice.kew.rule;

/**
 * Helper class that encapsulates a qualified rolename, and can encode and decode to String representation.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class QualifiedRoleName {
    private final String baseRoleName;
    private final String qualifier;

    public QualifiedRoleName(String baseRoleName) {
        this(baseRoleName, null);
    }
    public QualifiedRoleName(String baseRoleName, String qualifier) {
        this.baseRoleName = baseRoleName;
        this.qualifier = qualifier;
    }
    public String getBaseRoleName() {
        return baseRoleName;
    }
    public String getQualifier() {
        return qualifier;
    }
    public String encode() {
        if (qualifier == null) {
            return baseRoleName;
        } else {
            return baseRoleName + '\0' + qualifier;
        }
    }

    public static QualifiedRoleName parse(String qualifiedRoleName) {
        String[] parts = qualifiedRoleName.split("\0", 2);
        return new QualifiedRoleName(parts[0], parts.length > 1 ? parts[1] : null);
    }

    public String toString() {
        return "[QualifiedRoleName: baseRoleName=" + baseRoleName +
                                 ", qualifier=" + qualifier + "]";
    }
}
