/*
 * Copyright 2006-2013 The Kuali Foundation
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

package org.kuali.rice.krms.test;

/**
 * RuleManagementBaseTestObjectNames use is to provide common consistent literal values for tests in this package
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleManagementBaseTestObjectNames {
    protected String discriminator;

    protected String object0;
    protected String object1;
    protected String object2;
    protected String object3;
    protected String object4;
    protected String object5;
    protected String object6;
    protected String object7;
    protected String object8;

    protected String action;
    protected String action_Id;
    protected String action_Name;
    protected String action_Descr;
    protected String action0;
    protected String action0_Id;
    protected String action0_Name;
    protected String action0_Descr;
    protected String action1;
    protected String action1_Id;
    protected String action1_Name;
    protected String action1_Descr;
    protected String action2;
    protected String action2_Id;
    protected String action2_Name;
    protected String action2_Descr;
    protected String action3;
    protected String action3_Id;
    protected String action3_Name;
    protected String action3_Descr;
    protected String action4;
    protected String action4_Id;
    protected String action4_Name;
    protected String action4_Descr;

    protected String actionAttribute;
    protected String actionAttribute_Key;
    protected String actionAttribute_Value;
    protected String actionAttribute0;
    protected String actionAttribute0_Key;
    protected String actionAttribute0_Value;
    protected String actionAttribute1;
    protected String actionAttribute1_Key;
    protected String actionAttribute1_Value;

    protected String agenda_Id;
    protected String agenda_Name;
    protected String agendaItem_Id;
    protected String agendaItem_0_Id;
    protected String agendaItem_1_Id;
    protected String agendaItem_2_Id;
    protected String agendaItem_3_Id;
    protected String agendaItem_4_Id;
    protected String agendaItem_5_Id;
    protected String agendaItem_6_Id;
    protected String agendaItem_7_Id;

    protected String typeId;
    protected String type0_Id;

    protected String contextId;
    protected String context0_Id;
    protected String context1_Id;
    protected String context2_Id;
    protected String context3_Id;

    protected String contextDescr;
    protected String context0_Descr;
    protected String context1_Descr;
    protected String context2_Descr;
    protected String context3_Descr;

    protected String contextName;
    protected String context0_Name;
    protected String context1_Name;
    protected String context2_Name;
    protected String context3_Name;

    protected String namespaceName;
    protected String namespaceType;

    protected String nlUsage0_Id;
    protected String nlUsage1_Id;
    protected String nlUsage2_Id;
    protected String nlUsage3_Id;

    protected String nlUsage0_Name;
    protected String nlUsage0_Descr;
    protected String nlUsage0_KrmsType;

    protected String proposition_Id;
    protected String proposition_0_Id;
    protected String proposition_1_Id;
    protected String proposition_2_Id;
    protected String proposition_3_Id;
    protected String proposition_Descr;
    protected String proposition_0_Descr;
    protected String proposition_1_Descr;
    protected String proposition_2_Descr;
    protected String proposition_3_Descr;

    protected String referenceObject_0_Id;
    protected String referenceObject_1_Id;
    protected String referenceObject_2_Id;
    protected String referenceObject_3_Id;

    protected String referenceObject_0_DiscriminatorType;
    protected String referenceObject_1_DiscriminatorType;
    protected String referenceObject_2_DiscriminatorType;
    protected String referenceObject_3_DiscriminatorType;

    protected String rule_Id;
    protected String rule_0_Id;
    protected String rule_1_Id;
    protected String rule_2_Id;
    protected String rule_3_Id;
    protected String rule_4_Id;
    protected String rule_5_Id;
    protected String rule_6_Id;
    protected String rule_7_Id;
    protected String rule_0_Name;
    protected String rule_1_Name;
    protected String rule_2_Name;
    protected String rule_3_Name;
    protected String rule_4_Name;
    protected String rule_5_Name;
    protected String rule_6_Name;
    protected String rule_7_Name;

    public RuleManagementBaseTestObjectNames(String classDiscriminator, String testDiscriminator) {
        setTestNames(classDiscriminator, testDiscriminator);
    }

    protected void setTestNames(String classDiscriminator, String testDiscriminator) {
        discriminator =  classDiscriminator + testDiscriminator + "00";
        object0 = classDiscriminator + testDiscriminator + "00";
        object1 = classDiscriminator + testDiscriminator + "01";
        object2 = classDiscriminator + testDiscriminator + "02";
        object3 = classDiscriminator + testDiscriminator + "03";
        object4 = classDiscriminator + testDiscriminator + "04";
        object5 = classDiscriminator + testDiscriminator + "05";
        object6 = classDiscriminator + testDiscriminator + "06";
        object7 = classDiscriminator + testDiscriminator + "07";
        object8 = classDiscriminator + testDiscriminator + "08";

        action = "Action" + object0;
        action_Id = "ActionId" + object0;
        action_Name = "ActionName" + object0;
        action_Descr = "ActionDesc" + object0;
        action0 = "Action" + object0;
        action0_Id = "ActionId" + object0;
        action0_Name = "ActionName" + object0;
        action0_Descr = "ActionDesc" + object0;
        action1 = "Action" + object1;
        action1_Id = "ActionId" + object1;
        action1_Name = "ActionName" + object1;
        action1_Descr = "ActionDesc" + object1;
        action2 = "Action" + object2;
        action2_Id = "ActionId" + object2;
        action2_Name = "ActionName" + object2;
        action2_Descr = "ActionDesc" + object2;
        action3 = "Action" + object3;
        action3_Id = "ActionId" + object3;
        action3_Name = "ActionName" + object3;
        action3_Descr = "ActionDesc" + object3;
        action4 = "Action" + object4;
        action4_Id = "ActionId" + object4;
        action4_Name = "ActionName" + object4;
        action4_Descr = "ActionDesc" + object4;

        actionAttribute = "ActionAttribute" + object0;
        actionAttribute_Key = "ActionAttributeKey" + object0;
        actionAttribute_Value = "ActionAttributeValue" + object0;
        actionAttribute0 = "ActionAttribute" + object0;
        actionAttribute0_Key = "ActionAttributeKey" + object0;
        actionAttribute0_Value = "ActionAttributeValue" + object0;
        actionAttribute1 = "ActionAttribute" + object1;
        actionAttribute1_Key = "ActionAttributeKey" + object1;
        actionAttribute1_Value = "ActionAttributeValue" + object1;

        agenda_Id = "AgendaId" + object0;
        agenda_Name = "AgendaName" + object0;

        agendaItem_Id = "AI" + object0;
        agendaItem_0_Id = "AI" + object0;
        agendaItem_1_Id = "AI" + object1;
        agendaItem_2_Id = "AI" + object2;
        agendaItem_3_Id = "AI" + object3;
        agendaItem_4_Id = "AI" + object4;
        agendaItem_5_Id = "AI" + object5;
        agendaItem_6_Id = "AI" + object6;
        agendaItem_7_Id = "AI" + object7;

        typeId = "TypeId" + object0;
        type0_Id = "TypeId" + object0;

        contextId = "ContextId" + object0;
        context0_Id = "ContextId" + object0;
        context1_Id = "ContextId" + object1;
        context2_Id = "ContextId" + object2;
        context3_Id = "ContextId" + object3;

        contextDescr = "ContextDescr" + object0;
        context0_Descr = "ContextDescr" + object0;
        context1_Descr = "ContextDescr" + object1;
        context2_Descr = "ContextDescr" + object2;
        context3_Descr = "ContextDescr" + object3;

        contextName = "ContextName" + object0;
        context0_Name = "ContextName" + object0;
        context1_Name = "ContextName" + object1;
        context2_Name = "ContextName" + object2;
        context3_Name = "ContextName" + object3;

        namespaceName = "Namespace" + object0;
        namespaceType = "NamespaceType" + object0;

        nlUsage0_Id = "krms.nl." + object0;
        nlUsage1_Id = "krms.nl." + object1;
        nlUsage2_Id = "krms.nl." + object2;
        nlUsage3_Id = "krms.nl." + object3;

        nlUsage0_Name = object0;
        nlUsage0_Descr = "Description-" + object0;
        nlUsage0_KrmsType = object0;

        referenceObject_0_Id = "PA" + object0;
        referenceObject_1_Id = "PA" + object1;
        referenceObject_2_Id = "PA" + object2;
        referenceObject_3_Id = "PA" + object3;

        referenceObject_0_DiscriminatorType = "ParkingAffiliationType";
        referenceObject_1_DiscriminatorType = "ParkingAffiliationType";
        referenceObject_2_DiscriminatorType = "ParkingAffiliationType";
        referenceObject_3_DiscriminatorType = "ParkingAffiliationType";

        rule_Id = "RuleId" + object0;
        rule_0_Id = "RuleId" + object0;
        rule_1_Id = "RuleId" + object1;
        rule_2_Id = "RuleId" + object2;
        rule_3_Id = "RuleId" + object3;
        rule_4_Id = "RuleId" + object4;
        rule_5_Id = "RuleId" + object5;
        rule_6_Id = "RuleId" + object6;
        rule_7_Id = "RuleId" + object7;
        rule_0_Name = "RuleName" + object0;
        rule_1_Name = "RuleName" + object1;
        rule_2_Name = "RuleName" + object2;
        rule_3_Name = "RuleName" + object3;
        rule_4_Name = "RuleName" + object4;
        rule_5_Name = "RuleName" + object5;
        rule_6_Name = "RuleName" + object6;
        rule_7_Name = "RuleName" + object7;

        proposition_Id = "P" + object0;
        proposition_0_Id = "P" + object0;
        proposition_1_Id = "P" + object1;
        proposition_2_Id = "P" + object2;
        proposition_3_Id = "P" + object3;

        proposition_Descr = "P" + object0 + "_simple_proposition";
        proposition_0_Descr = "P" + object0 + "_simple_proposition";
        proposition_1_Descr = "P" + object1 + "_simple_proposition";
        proposition_2_Descr = "P" + object2 + "_simple_proposition";
        proposition_3_Descr = "P" + object3 + "_simple_proposition";
    }
}
