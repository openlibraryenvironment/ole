TRUNCATE TABLE KRMS_TYP_T DROP STORAGE
/
INSERT INTO KRMS_TYP_T (ACTV,NM,NMSPC_CD,SRVC_NM,TYP_ID,VER_NBR)
  VALUES ('Y','Notify PeopleFlow','KR-RULE','notificationPeopleFlowActionTypeService','1000',1)
/
INSERT INTO KRMS_TYP_T (ACTV,NM,NMSPC_CD,SRVC_NM,TYP_ID,VER_NBR)
  VALUES ('Y','Route to PeopleFlow','KR-RULE','approvalPeopleFlowActionTypeService','1001',1)
/
INSERT INTO KRMS_TYP_T (ACTV,NM,NMSPC_CD,SRVC_NM,TYP_ID,VER_NBR)
  VALUES ('Y','Validation Rule','KR-RULE','validationRuleTypeService','1002',1)
/
INSERT INTO KRMS_TYP_T (ACTV,NM,NMSPC_CD,SRVC_NM,TYP_ID,VER_NBR)
  VALUES ('Y','Validation Action','KR-RULE','validationActionTypeService','1003',1)
/
