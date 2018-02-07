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
package org.kuali.rice.devtools.generators.dd;

import org.apache.commons.beanutils.PropertyUtils;
import org.kuali.rice.krad.bo.BusinessObject;

import java.beans.PropertyDescriptor;

/**
 * @deprecated this is for the legacy kns.
 */
@Deprecated
public class MaintDocDDCreator {

    public static void main( String[] args ) throws Exception {
        String className = args[0];
        Class boClass = Class.forName( className );
        PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors( boClass );

        StringBuffer sb = new StringBuffer( 4000 );
        sb.append( "<beans xmlns=\"http://www.springframework.org/schema/beans\"\r\n" + 
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
                "    xmlns:p=\"http://www.springframework.org/schema/p\"\r\n" + 
                "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans\r\n" + 
                "        http://www.springframework.org/schema/beans/spring-beans-2.0.xsd\">\r\n" + 
                "\r\n" + 
                "  <bean id=\"" );
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument\" parent=\"" );
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument-parentBean\" />\r\n" + 
                "\r\n" + 
                "  <bean id=\"" );
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument-parentBean\" abstract=\"true\" parent=\"MaintenanceDocumentEntry\"\r\n" + 
                "        p:businessObjectClass=\"" );
        sb.append( boClass.getName() );
        sb.append( "\"\r\n" );
        sb.append( "        p:maintainableClass=\"" ); 
        sb.append( "org.kuali.core.maintenance.KualiMaintainableImpl" );
        sb.append( "\"\r\n" );
        sb.append( "        p:label=\"" ); 
        sb.append( BeanDDCreator.camelCaseToString( boClass.getSimpleName() ) );
        sb.append( " Maintenance Document\"\r\n" );
        sb.append( "        p:shortLabel=\"" ); 
        sb.append( BeanDDCreator.camelCaseToString( boClass.getSimpleName() ) );
        sb.append( " Maint. Doc.\"\r\n" );
        sb.append( "        p:documentTypeName=\"" ); 
        sb.append( boClass.getSimpleName() + "MaintenanceDocument" );
        sb.append( "\"\r\n" );
        sb.append( "        p:documentTypeCode=\"" ); 
        sb.append( "FILLMEIN" );
        sb.append( "\"\r\n" );
        sb.append( "        p:businessRulesClass=\"" ); 
        sb.append( "org.kuali.core.maintenance.rules.MaintenanceDocumentRuleBase" );
        sb.append( "\"\r\n" );
        sb.append( "        p:preRulesCheckClass=\"" ); 
        sb.append( "FILLMEIN" );
        sb.append( "\"\r\n" );
        sb.append( "        p:documentAuthorizerClass=\"" ); 
        sb.append( "org.kuali.kfs.sys.document.authorization.FinancialSystemMaintenanceDocumentAuthorizerBase" );
        sb.append( "\"\r\n" );
        sb.append( "        p:workflowProperties-ref=\"" ); 
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument-workflowProperties\" >\r\n" );
        sb.append( "\r\n" );

        sb.append( "    <property name=\"authorizations\" >\r\n" + 
        		"      <list>\r\n" + 
        		"        <bean parent=\"AuthorizationDefinition\">\r\n" + 
        		"          <property name=\"action\" value=\"initiate\" />\r\n" + 
        		"          <property name=\"authorizedGroups\" >\r\n" + 
        		"            <set>\r\n" + 
        		"              <value>SY_MAINTENANCE_USERS</value>\r\n" + 
        		"            </set>\r\n" + 
        		"          </property>\r\n" + 
        		"        </bean>\r\n" + 
        		"      </list>\r\n" + 
        		"    </property>\r\n" + 
        		"" );
        sb.append( "    <property name=\"lockingKeys\" >\r\n" + 
        		"      <list>\r\n" + 
        		"        <value>FILLMEIN</value>\r\n" + 
        		"      </list>\r\n" + 
        		"    </property>\r\n" + 
        		"");
        sb.append( "    <property name=\"helpDefinition\" >\r\n" + 
        		"      <bean parent=\"HelpDefinition\" p:parameterClass=\"" );
        sb.append( boClass.getName() );
        sb.append( "\" p:parameterName=\"" );
        sb.append( camelCaseToHelpParm( boClass.getSimpleName() ) );
        sb.append( "\" />\r\n" + 
        		"    </property>\r\n" + 
        		"" );
        sb.append( "    <property name=\"defaultExistenceChecks\" >\r\n" + 
        		"      <list>\r\n" + 
        		"" );
        for ( PropertyDescriptor pd : props ) {
            if ( isReferenceBoProperty(pd)) {
                sb.append( "        <bean parent=\"ReferenceDefinition\"\r\n" + 
                		"              p:attributeName=\"" );
                sb.append( pd.getName() );
                sb.append( "\"\r\n" + 
                		"              p:activeIndicatorAttributeName=\"active\"\r\n" + 
                		"              p:attributeToHighlightOnFail=\"FILLMEIN\" />\r\n" + 
                		"" );
            }
        }
        sb.append( "      </list>\r\n" + 
        		"    </property>\r\n" + 
        		"" );
        
        sb.append( "    <property name=\"maintainableSections\" >\r\n" + 
        		"      <list>\r\n" + 
        		"        <ref bean=\"" );
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument-MainSection\" />\r\n" + 
        		"      </list>\r\n" + 
        		"    </property>\r\n" + 
        		"  </bean>\r\n\r\n");
        
        sb.append( "  <bean id=\"" );
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument-MainSection\" parent=\"" );
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument-MainSection-parentBean\" />\r\n" + 
                "\r\n" + 
                "  <bean id=\"" );
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument-MainSection-parentBean\" abstract=\"true\" parent=\"MaintainableSectionDefinition\"\r\n" + 
                "        p:title=\"" );
        sb.append( BeanDDCreator.camelCaseToString( boClass.getSimpleName() ) );
        sb.append( " Maintenance\" >\r\n" +
        		"    <property name=\"maintainableItems\" >\r\n" + 
        		"      <list>\r\n" + 
        		"" );
        for ( PropertyDescriptor pd : props ) {
            if ( BeanDDCreator.isNormalProperty(pd) && !pd.getName().endsWith("codeAndDescription" ) ) {
                
                sb.append( "        <bean parent=\"MaintainableFieldDefinition\"\r\n" + 
                		"              p:name=\"" );
                sb.append( pd.getName() );
                if ( pd.getName().endsWith("active" ) ) {
                    sb.append( "\"\r\n" + 
                            "              p:defaultValue=\"true\" />\r\n" );
                } else if ( pd.getName().equals("versionNumber" ) ) {
                    sb.append( "\" />\r\n" );
                } else {
                    sb.append( "\"\r\n" + 
                    "              p:required=\"true\" />\r\n" );
                }
            }
        }
        sb.append( "      </list>\r\n" + 
        		"    </property>\r\n" + 
        		"  </bean>\r\n" + 
        		"" );
        sb.append( "  <bean id=\"" );
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument-workflowProperties\" parent=\"" );
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument-workflowProperties-parentBean\" />\r\n" + 
        		"\r\n" + 
        		"  <bean id=\"" );
        sb.append( boClass.getSimpleName() );
        sb.append( "MaintenanceDocument-workflowProperties-parentBean\" abstract=\"true\" parent=\"CommonWorkflow-MaintenanceDocument\" />\r\n" + 
//        		"    <property name=\"workflowPropertyGroups\" >\r\n" + 
//        		"      <list>\r\n" + 
//        		"        <bean parent=\"WorkflowPropertyGroup\">\r\n" + 
//        		"          <property name=\"workflowProperties\" >\r\n" + 
//        		"            <list>\r\n" + 
//        		"              <bean parent=\"WorkflowProperty\" p:path=\"newMaintainableObject.businessObject\" />\r\n" + 
//        		"              <bean parent=\"WorkflowProperty\" p:path=\"oldMaintainableObject.businessObject\" />\r\n" + 
//        		"            </list>\r\n" + 
//        		"          </property>\r\n" + 
//        		"        </bean>\r\n" + 
//        		"      </list>\r\n" + 
//        		"    </property>\r\n" + 
//        		"  </bean>\r\n" + 
        		"</beans>" );
        System.out.println( sb.toString() );
    }
    
    public static String camelCaseToHelpParm( String className ) {
        StringBuffer newName = new StringBuffer( className );
        // lower case the 1st letter
        newName.replace(0, 1, newName.substring(0, 1).toLowerCase());
        // loop through, inserting spaces when cap
        for ( int i = 0; i < newName.length(); i++ ) {
            if ( Character.isUpperCase(newName.charAt(i)) ) {
                newName.insert(i, '_');
                i++;
            }
        }
        return newName.toString().toUpperCase().trim();
    }
    
    public static boolean isReferenceBoProperty( PropertyDescriptor p ) {
        return p.getPropertyType()!= null 
                && BusinessObject.class.isAssignableFrom( p.getPropertyType() )
                && !p.getName().startsWith( "boNote" )
                && !p.getName().startsWith( "extension" )
                && !p.getName().equals( "newCollectionRecord" );
    }
    
}
