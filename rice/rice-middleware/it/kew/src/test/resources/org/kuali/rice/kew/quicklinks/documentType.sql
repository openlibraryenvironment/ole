--
-- Copyright 2005-2011 The Kuali Foundation
--
-- Licensed under the Educational Community License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.opensource.org/licenses/ecl2.php
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',0,'docHandler is now mandatory...','Workflow Maintenance Document',2010,'EDENSERVICE-DOCS',0,'1','Workflow Maintenance Document','none','2',6,'B169E3D4890B4A9293E46CE58385B522');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${workflow.url}/DocumentType.do?methodToCall=docHandler','Workflow Maintenance Document Type Document',2011,'EDENSERVICE-DOCS.DocumentType',0,'1','Workflow Maintenance Document Type Document',2682,'edu.iu.uis.eden.doctype.DocumentTypePostProcessor','2',2,'B169E3D4890B4A9293E46CE58385B523');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${workflow.url}/Rule.do?methodToCall=docHandler','Rule Maintenance Document Type Document',2012,'EDENSERVICE-DOCS.RuleDocument',0,'1','Rule Maintenance Document Type Document',2682,'org.kuali.rice.kew.rule.RulePostProcessor','2',2,'B169E3D4890B4A9293E46CE58385B524');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${workflow.url}/Workgroup.do?methodToCall=docHandler','Document to generate a new workgroup',2013,'EDENSERVICE-DOCS.WKGRPREQ',0,'1','Add/modify EDEN workgroup',2682,'org.kuali.rice.kew.workgroup.WorkgroupPostProcessor','1',2,'B169E3D4890B4A9293E46CE58385B525');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${workflow.url}/RemoveReplace.do?methodToCall=docHandler','A document to remove or replace users within rules and workgroups.',2014,'EDENSERVICE-DOCS.RemoveReplaceUser',0,'1','Remove/Replace User',2682,'org.kuali.rice.kew.removereplace.RemoveReplacePostProcessor','1',2,'B169E3D4890B4A9293E46CE58385B526');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a Principal',2015,'KIMPrincipalMaintenanceDocument',0,'1','KIM Principal Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B527');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a Group',2016,'KIMGroupMaintenanceDocument',0,'1','KIM Group Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B52');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a Principal',2017,'KIMRoleMaintenanceDocument',0,'1','KIM Role Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B529');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a Attribute Type',2018,'KIMAttributeTypeMaintenanceDocument',0,'1','KIM Attribute Type Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B52A');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a Namespace',2019,'KIMNamespaceMaintenanceDocument',0,'1','KIM Namespace Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B52B');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a EntityType',2020,'KIMEntityTypeMaintenanceDocument',0,'1','KIM EntityType Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B52C');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a Entity',2021,'KIMEntityMaintenanceDocument',0,'1','KIM Entity Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B52D');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a Group Type',2022,'KIMGroupTypeMaintenanceDocument',0,'1','KIM Group Type Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B52E');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${ken.url}/DetailView.form','This is the re-usable notification document type that will be used for delivering all notifications with KEW.',2023,'KualiNotification',0,'2000','Notification','org.kuali.rice.ken.postprocessor.kew.NotificationPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B52F');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${ken.url}/AdministerNotificationRequest.form','Create a New Notification Request',2024,'SendNotificationRequest',0,'1','Send Notification Request','org.kuali.rice.ken.postprocessor.kew.NotificationSenderFormPostProcessor','2',1,'B169E3D4890B4A9293E46CE58385B530');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a Organization',2025,'KualiOrganizationMaintenanceDocument',0,'1','Kuali Organization Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B531');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a Organization Category',2026,'KualiOrganizationCategoryMaintenanceDocument',0,'1','Kuali Organization Category Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B532');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create or Update a Organization Context',2027,'KualiOrganizationContextMaintenanceDocument',0,'1','Kuali Organization Context Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1',1,'B169E3D4890B4A9293E46CE58385B533');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',0,'${application.url}/travelDocument2.do?methodToCall=docHandler','Create a New Travel Request',2028,'TravelRequest',0,'1','Travel Request','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',2,'B169E3D4890B4A9293E46CE58385B534');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create a New Travel Account Maintenance Document',2029,'TravelAccountMaintenanceDocument',0,'1','Travel Account Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',1,'B169E3D4890B4A9293E46CE58385B535');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create a New Travel Fiscal Officer',2030,'FiscalOfficerMaintenanceDocument',0,'1','Travel Fiscal Officer','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',1,'B169E3D4890B4A9293E46CE58385B536');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create/edit parameter namespaces',2031,'ParameterNamespaceMaintenanceDocument',0,'1','Parameter Namespace Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',1,'B169E3D4890B4A9293E46CE58385B537');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create/edit a parameter type',2032,'ParameterTypeMaintenanceDocument',0,'1','Parameter Type Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',1,'B169E3D4890B4A9293E46CE58385B538');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create/edit a parameter detail type',2033,'ParameterDetailTypeMaintenanceDocument',0,'1','Parameter Detail Type Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',1,'B169E3D4890B4A9293E46CE58385B539');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create/edit a parameter',2034,'ParameterMaintenanceDocument',0,'1','Parameter Maintenance Document','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',1,'B169E3D4890B4A9293E46CE58385B53A');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create a Rice User',2035,'RiceUserMaintenanceDocument',0,'1','Create a Rice User','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',1,'B169E3D4890B4A9293E46CE58385B53B');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_PLCY,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'NONE',0,'${workflow.url}/EDocLite','eDoc.Example1 Parent Doctype',2204,'eDoc.Example1.ParentDoctype',0,'2200','eDoc.Example1 Parent Document','org.kuali.rice.kew.edl.EDocLitePostProcessor','2',3,'B169E3D4890B4A9293E46CE58385B53C');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_PLCY,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'NONE',0,'${workflow.url}/EDocLite','eDoc.Example1 Request DocumentType',2205,'eDoc.Example1Doctype',0,'2200','eDoc.Example1 Request DocumentType',2206,'org.kuali.rice.kew.edl.EDocLitePostProcessor','2',3,'B169E3D4890B4A9293E46CE58385B53D');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_PLCY,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,PREV_DOC_TYP_VER_NBR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'NONE',0,'${workflow.url}/EDocLite','eDoc.Example1 Parent Doctype',2206,'eDoc.Example1.ParentDoctype',1,'2200','eDoc.Example1 Parent Document','org.kuali.rice.kew.edl.EDocLitePostProcessor',2204,'2',3,'B169E3D4890B4A9293E46CE58385B53E');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_PLCY,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,PARNT_ID,POST_PRCSR,PREV_DOC_TYP_VER_NBR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'NONE',0,'${workflow.url}/EDocLite','eDoc.Example1 Request DocumentType',2215,'eDoc.Example1Doctype',1,'2200','eDoc.Example1 Request DocumentType',2217,'org.kuali.rice.kew.edl.EDocLitePostProcessor',2205,'2',4,'B169E3D4890B4A9293E46CE58385B53F');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_PLCY,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,PREV_DOC_TYP_VER_NBR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'NONE',0,'${workflow.url}/EDocLite','eDoc.Example1 Parent Doctype',2216,'eDoc.Example1.ParentDoctype',2,'2200','eDoc.Example1 Parent Document','org.kuali.rice.kew.edl.EDocLitePostProcessor',2206,'2',2,'B169E3D4890B4A9293E46CE58385B540');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_PLCY,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,PREV_DOC_TYP_VER_NBR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'NONE',1,'${workflow.url}/EDocLite','eDoc.Example1 Parent Doctype',2217,'eDoc.Example1.ParentDoctype',3,'2200','eDoc.Example1 Parent Document','org.kuali.rice.kew.edl.EDocLitePostProcessor',2216,'2',3,'B169E3D4890B4A9293E46CE58385B541');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',0,'none','SampleThinClientDocument',2280,'SampleThinClientDocument',0,'1','SampleThinClientDocument','org.kuali.rice.kew.postprocessor.DefaultPostProcessor','2',2,'B169E3D4890B4A9293E46CE58385B542');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,POST_PRCSR,PREV_DOC_TYP_VER_NBR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'none','SampleThinClientDocument',2282,'SampleThinClientDocument',1,'1','SampleThinClientDocument','org.kuali.rice.kew.postprocessor.DefaultPostProcessor',2280,'2',1,'B169E3D4890B4A9293E46CE58385B543');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_PLCY,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,PARNT_ID,POST_PRCSR,PREV_DOC_TYP_VER_NBR,RTE_VER_NBR,APPL_ID,VER_NBR,OBJ_ID)
 VALUES (1,'NONE',0,'${workflow.url}/EDocLite','eDoc.Example1 Request DocumentType',2320,'eDoc.Example1Doctype',2,'2200','eDoc.Example1 Request DocumentType',2217,'org.kuali.rice.kew.edl.EDocLitePostProcessor',2215,'2','FooBar',2,'B169E3D4890B4A9293E46CE58385B544');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,NOTIFY_ADDR,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'http://localhost:8080/tvl-dev/WorkflowTripDochandler.do','Travel Trip Reimbursement Document',2360,'TravelTripReimbursement',0,'1','Travel Trip Reimbursement','travelbl@indiana.edu','edu.iu.uis.tvl.workflow.routing.TravelPostProcessor','2',1,'B169E3D4890B4A9293E46CE58385B545');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_PLCY,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,PARNT_ID,POST_PRCSR,PREV_DOC_TYP_VER_NBR,RTE_VER_NBR,APPL_ID,VER_NBR,OBJ_ID)
 VALUES (1,'NONE',1,'${workflow.url}/EDocLite','eDoc.Example1 Request DocumentType',2440,'eDoc.Example1Doctype',3,'2200','eDoc.Example1 Request DocumentType',2217,'org.kuali.rice.kew.edl.EDocLitePostProcessor',2320,'2','FooBar',1,'B169E3D4890B4A9293E46CE58385B546');
--Following 2 insert statements are redundant and cause problems due to these document types already being ingested in DefaultTestData.xml
--INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,LBL,POST_PRCSR,RTE_VER_NBR,VER_NBR,OBJ_ID)
--  VALUES (1,1,'KualiDocument',2680,'KualiDocument',0,'KualiDocument','none','2',2,'B169E3D4890B4A9293E46CE58385B547');
--INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,LBL,PARNT_ID,RTE_VER_NBR,VER_NBR,OBJ_ID)
--  VALUES (1,1,'Parent Document Type for all Rice Documents',2681,'RiceDocument',0,'Rice Document',2680,'2',3,'B169E3D4890B4A9293E46CE58385B548');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,PARNT_ID,POST_PRCSR,PREV_DOC_TYP_VER_NBR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'docHandler is now mandatory...','Workflow Maintenance Document',2682,'EDENSERVICE-DOCS',1,'1','Workflow Maintenance Document',2681,'none',2010,'2',1,'B169E3D4890B4A9293E46CE58385B549');
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,PARNT_ID,POST_PRCSR,PREV_DOC_TYP_VER_NBR,RTE_VER_NBR,VER_NBR,OBJ_ID)
 VALUES (1,'1',1,'${application.url}/travelDocument2.do?methodToCall=docHandler','Create a New Travel Request',2683,'TravelRequest',1,'1','Travel Request',2681,'org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor',2028,'2',1,'B169E3D4890B4A9293E46CE58385B54A');
