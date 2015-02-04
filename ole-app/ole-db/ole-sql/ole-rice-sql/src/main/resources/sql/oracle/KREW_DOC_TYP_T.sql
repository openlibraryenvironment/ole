TRUNCATE TABLE KREW_DOC_TYP_T DROP STORAGE
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Workflow Maintenance Document Type Document','2011','DocumentTypeDocument',0,'1','${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch05s01.html','Workflow Maintenance Document Type Document','6166CBA1BA5D644DE0404F8189D86C09','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Rule Maintenance Document Type Document','2012','RoutingRuleDocument',0,'1','Rule Maintenance Document Type Document','6166CBA1BA5E644DE0404F8189D86C09','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,OBJ_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${ken.url}/DetailView.form','This is the re-usable notification document type that will be used for delivering all notifications with KEW.','2023','KualiNotification',0,'2000','Notification','6166CBA1BA69644DE0404F8189D86C09','org.kuali.rice.ken.postprocessor.kew.NotificationPostProcessor','1',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,OBJ_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${ken.url}/AdministerNotificationRequest.form','Create a New Notification Request','2024','SendNotificationRequest',0,'1','Send Notification Request','6166CBA1BA6A644DE0404F8189D86C09','org.kuali.rice.ken.postprocessor.kew.NotificationSenderFormPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create/edit parameter namespaces','2031','NamespaceMaintenanceDocument',0,'1','${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch04s07.html','Namespace','6166CBA1BA71644DE0404F8189D86C09','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create/edit a parameter type','2032','ParameterTypeMaintenanceDocument',0,'1','${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch04s10.html','Parameter Type Maintenance Document','6166CBA1BA72644DE0404F8189D86C09','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create/edit a parameter detail type','2033','ParameterDetailTypeMaintenanceDocument',0,'1','${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch04s09.html','Parameter Detail Type Maintenance Document','6166CBA1BA73644DE0404F8189D86C09','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create/edit a parameter','2034','ParameterMaintenanceDocument',0,'1','${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch04s08.html','Parameter Maintenance Document','6166CBA1BA74644DE0404F8189D86C09','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,LBL,OBJ_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'KualiDocument','2680','KualiDocument',0,'KualiDocument','6166CBA1BA81644DE0404F8189D86C09','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'Parent Document Type for all Rice Documents','2681','RiceDocument',0,'Rice Document','6166CBA1BA82644DE0404F8189D86C09','2680','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kr.url}/maintenance.do?methodToCall=docHandler','Routing Rule Delegation','2699','RoutingRuleDelegationMaintenanceDocument',0,'Routing Rule Delegation','A6DC8753-AF90-7A01-0EF7-E6D446529668','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kr.url}/maintenance.do?methodToCall=docHandler','2708','CampusMaintenanceDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch08s01.html','CampusMaintenanceDocument','616D94CA-D08D-D036-E77D-4B53DB34CD95','2681','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kr.url}/maintenance.do?methodToCall=docHandler','2709','CampusTypeMaintenanceDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch08s08.html ','CampusTypeMaintenanceDocument','DE0B8588-E459-C07A-87B8-6ACD693AE70C','2681','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kr.url}/maintenance.do?methodToCall=docHandler','2710','CountryMaintenanceDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch08s02.html','CountryMaintenanceDocument','82EDB593-97BA-428E-C6E7-A7F3031CFAEB','2681','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kr.url}/maintenance.do?methodToCall=docHandler','2711','CountyMaintenanceDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch08s03.html','CountyMaintenanceDocument','C972E260-5552-BB63-72E6-A514301B0326','2681','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kr.url}/maintenance.do?methodToCall=docHandler','2712','PostalCodeMaintenanceDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch08s04.html','PostalCodeMaintenanceDocument','B79D1104-BC48-1597-AFBE-773EED31A110','2681','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kr.url}/maintenance.do?methodToCall=docHandler','2713','StateMaintenanceDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch08s05.html','StateMaintenanceDocument','EF2378F6-E770-D7BF-B7F1-C18881E3AFF0','2681','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'2994','IdentityManagementDocument',0,'Undefined','944596CD-A7FC-0DEE-EDE1-52A52BED85CC','2681','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kim.url}/identityManagementRoleDocument.do?methodToCall=docHandler','2995','IdentityManagementRoleDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch07s07.html','Role','FEA8D9DD-0592-0525-B2BD-2F4BA811CF30','2994','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kim.url}/identityManagementGroupDocument.do?methodToCall=docHandler','2996','IdentityManagementGroupDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch07s03.html','Group','D9636763-7749-8F3F-4570-21181E977AE3','2994','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kim.url}/identityManagementPersonDocument.do?methodToCall=docHandler','2997','IdentityManagementPersonDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch07s05.html','Person','14C95FE4-1497-82C6-CBBD-BF16AD81B845','2994','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kr.url}/maintenance.do?methodToCall=docHandler','2998','IdentityManagementReviewResponsibilityMaintenanceDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch07s06.html','Review Responsibility','66413887-3C82-B12D-9563-0A893E8D1910','2994','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kr.url}/maintenance.do?methodToCall=docHandler','2999','IdentityManagementGenericPermissionMaintenanceDocument',0,'${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch07s04.html','Permission','A3AE4787-018E-1F17-6EB6-F2F0F366774F','2994','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.krad.url}/krmsAgendaEditor?methodToCall=docHandler','Create a KRMS Agenda','3000','AgendaEditorMaintenanceDocument',0,'1','KRMS Agenda Editor Maintenance Document','ebd70731-4d33-4c0b-a958-2b9ca047ae07','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.krad.url}/maintenance?methodToCall=docHandler','Create a New Agenda','3001','AgendaMaintenanceDocument',0,'1','Agenda Maintenance Document','3198b708-6e29-4b19-bf35-51473cf8a3d1','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,CUR_IND,DOC_HDLR_URL,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,LBL,OBJ_ID,PARNT_ID,RTE_VER_NBR,VER_NBR)
  VALUES (1,1,'${kr.krad.url}/peopleFlowMaintenance?methodToCall=docHandler','3002','PeopleFlowMaintenanceDocument',0,'PeopleFlowMaintenanceDocument','2c0a1333-f60b-47c4-a9b0-76f32f1ed97d','2681','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.krad.url}/maintenance?methodToCall=docHandler','Create a KRMS Context','3003','ContextMaintenanceDocument',0,'1','KRMS Context Maintenance Document','87413487-8306-4130-b2df-a5d0e42243f9','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.krad.url}/maintenance?methodToCall=docHandler','Create a KRMS Term','3004','TermMaintenanceDocument',0,'1','KRMS Term Maintenance Document','bc83f80b-85c4-40fd-998c-ef53fdd97e3f','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.krad.url}/maintenance?methodToCall=docHandler','Create a KRMS Term Specification','3005','TermSpecificationMaintenanceDocument',0,'1','KRMS Term Specification Maintenance Document','57e7ee1d-e44a-4154-9ba4-ee562c434c98','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
INSERT INTO KREW_DOC_TYP_T (ACTV_IND,BLNKT_APPR_GRP_ID,CUR_IND,DOC_HDLR_URL,DOC_TYP_DESC,DOC_TYP_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,GRP_ID,HELP_DEF_URL,LBL,OBJ_ID,PARNT_ID,POST_PRCSR,RTE_VER_NBR,VER_NBR)
  VALUES (1,'1',1,'${kr.url}/maintenance.do?methodToCall=docHandler','Create/edit a component','3007','ComponentMaintenanceDocument',0,'1','${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch04s09.html','Component Maintenance Document','e9e0086b-30ce-4a9b-a2d6-564772076a1e','2681','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','2',0)
/
