-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: bootstrap_krim_role_perm_data.xml
-- *********************************************************************

-- Lock Database
-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_ATTR_DEFN_T::ole
INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE22', 'OLE22', '1', 'chartOfAccountsCode', '', 'Y', 'OLE-COA', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE23', 'OLE23', '1', 'accountNumber', '', 'Y', 'OLE-COA', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE24', 'OLE24', '1', 'organizationCode', '', 'Y', 'OLE-COA', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE25', 'OLE25', '1', 'descendHierarchy', '', 'Y', 'OLE-COA', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE26', 'OLE26', '1', 'fromAmount', '', 'Y', 'OLE-COA', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE27', 'OLE27', '1', 'toAmount', '', 'Y', 'OLE-COA', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE28', 'OLE28', '1', 'accountingLineOverrideCode', '', 'Y', 'OLE-COA', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE30', 'OLE30', '1', 'subFundGroupCode', '', 'Y', 'OLE-COA', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE31', 'OLE31', '1', 'purchasingCommodityCode', '', 'Y', 'OLE-VND', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE32', 'OLE32', '1', 'contractManagerCode', '', 'Y', 'OLE-VND', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE33', 'OLE33', '1', 'customerProfileId', '', 'Y', 'OLE-PDP', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE35', 'OLE35', '1', 'sensitiveDataCode', '', 'Y', 'OLE-PURAP', 'org.kuali.ole.module.purap.identity.PurapKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE36', 'OLE36', '1', 'vendorTypeCode', '', 'Y', 'OLE-VND', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE38', 'OLE38', '1', 'disbursementVoucherPaymentMethodCode', '', 'Y', 'OLE-FP', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE39', 'OLE39', '1', 'subAccountNumber', '', 'Y', 'OLE-COA', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLE45', 'OLE45', '1', 'documentSensitive', '', 'Y', 'OLE-PURAP', 'org.kuali.ole.module.purap.identity.PurapKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLESEC100', 'OLESEC100', '1', 'operator', '', 'Y', 'OLE-SEC', 'org.kuali.ole.sec.identity.SecKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLESEC101', 'OLESEC101', '1', 'propertyValue', '', 'Y', 'OLE-SEC', 'org.kuali.ole.sec.identity.SecKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLESEC102', 'OLESEC102', '1', 'constraintCode', '', 'Y', 'OLE-SEC', 'org.kuali.ole.sec.identity.SecKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLESEC103', 'OLESEC103', '1', 'overrideDeny', '', 'Y', 'OLE-SEC', 'org.kuali.ole.sec.identity.SecKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLEMI6886-ATTRDEF', 'OLEMI6886-ATTRDEF', '1', 'achTransactionTypeCode', '', 'Y', 'OLE-PDP', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO KRIM_ATTR_DEFN_T (KIM_ATTR_DEFN_ID, OBJ_ID, VER_NBR, NM, LBL, ACTV_IND, NMSPC_CD, CMPNT_NM) VALUES ('OLECNTRB199-ATTRDEF1', 'OLECNTRB199-ATTRDEF1', '1', 'filePath', '', 'Y', 'OLE-SYS', 'org.kuali.ole.sys.identity.OleKimAttributes')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ATTR_DEFN_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 1, '7:a5aa5c4164588ac2c613dd48c4483f75', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_TYP_T::ole
INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE25', 'OLE25', '1', 'Chart', '{OLE}chartRoleTypeService', 'Y', 'OLE-COA')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE26', 'OLE26', '1', 'Account', '{OLE}accountRoleTypeService', 'Y', 'OLE-COA')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE27', 'OLE27', '1', 'Organization', '{OLE}organizationRoleTypeService', 'Y', 'OLE-COA')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE28', 'OLE28', '1', 'Organization: Optionally Hierarchical', '{OLE}organizationOptionalHierarchyQualifierRoleTypeService', 'Y', 'OLE-SYS')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE29', 'OLE29', '1', 'Organization: Always Hierarchical & Document Type', '{OLE}organizationHierarchyReviewRoleTypeService', 'Y', 'OLE-SYS')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE30', 'OLE30', '1', 'Organization: Always Hierarchical Document Type & Accounting', '{OLE}accountingOrganizationHierarchyReviewRoleTypeService', 'Y', 'OLE-SYS')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE31', 'OLE31', '1', 'Sub-Fund & Document Type', '{OLE}subFundReviewRoleTypeService', 'Y', 'OLE-COA')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE32', 'OLE32', '1', 'Customer', '{OLE}customerRoleTypeService', 'Y', 'OLE-PDP')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE33', 'OLE33', '1', 'Commodity & Document Type', '{OLE}commodityReviewRoleTypeService', 'Y', 'OLE-VND')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE34', 'OLE34', '1', 'Contract Manager', '{OLE}contractManagerRoleTypeService', 'Y', 'OLE-VND')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE35', 'OLE35', '1', 'Sensitive Data', '{OLE}sensitiveDataRoleTypeService', 'Y', 'OLE-PURAP')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE36', 'OLE36', '1', 'Financial System User', '{OLE}financialSystemUserRoleTypeService', 'Y', 'OLE-SYS')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE37', 'OLE37', '1', 'Vendor Type', '{OLE}vendorTypeRoleTypeService', 'Y', 'OLE-VND')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE39', 'OLE39', '1', 'Derived Role: Account', '{OLE}accountDerivedRoleTypeService', 'Y', 'OLE-COA')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE40', 'OLE40', '1', 'Derived Role: Chart', '{OLE}chartDerivedRoleTypeService', 'Y', 'OLE-COA')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE41', 'OLE41', '1', 'Derived Role: Employee', '{OLE}employeeDerivedRoleTypeService', 'Y', 'OLE-SYS')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE46', 'OLE46', '1', 'Payment Method', '{OLE}paymentMethodRoleTypeService', 'Y', 'OLE-FP')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE47', 'OLE47', '1', 'Sub-Account & Document Type', '{OLE}subAccountReviewRoleTypeService', 'Y', 'OLE-COA')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE48', 'OLE48', '1', 'Derived Role: Purchasing Document', '{OLE}relatedDocumentDerivedRoleTypeService', 'Y', 'OLE-PURAP')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE51', 'OLE51', '1', 'Financial System Document Type', '{OLE}financialSystemDocumentTypePermissionTypeService', 'Y', 'OLE-SYS')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE58', 'OLE58', '1', 'Derived Role: Permission (Modify Batch Job)', '{OLE}batchJobModifierRoleTypeService', 'Y', 'OLE-SYS')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE61', 'OLE61', '1', 'Derived Role: Payment Request Hold / Cancel Initiator', '{OLE}paymentRequestHoldCancelInitiatorDerivedRoleTypeService', 'Y', 'OLE-PURAP')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE62', 'OLE62', '1', 'Sensitive Document Indicator', '{OLE}potentiallySensitiveDocumentRoleTypeService', 'Y', 'OLE-PURAP')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE65', 'OLE65', '1', 'Derived Role: Cash Receipt Initiator', '{OLE}cashReceiptInitiatorDerivedRoleTypeService', 'Y', 'OLE-FP')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE68', 'OLE68', '1', 'Organization Group', '{OLE}organizationGroupTypeService', 'Y', 'OLE-COA')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLE69', 'OLE69', '1', 'Derived Role: Accounts Payable Document Reviewer', '{OLE}accountsPayableDocumentDerivedRoleTypeService', 'Y', 'OLE-PURAP')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLESEC1', 'OLESEC1', '1', 'Attribute Name & Document Type', '{OLE}securityAttributeDocTypePermissionTypeService', 'Y', 'OLE-SEC')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLESEC2', 'OLESEC2', '1', 'Attribute Name', '{OLE}securityAttributePermissionTypeService', 'Y', 'OLE-SEC')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLESEC3', 'OLESEC3', '1', 'Attribute Name & Namespace', '{OLE}securityAttributeNamespacePermissionTypeService', 'Y', 'OLE-SEC')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLESEC4', 'OLESEC4', '1', 'Constraint Operator & Value', '{OLE}securityAttributeRoleTypeService', 'Y', 'OLE-SEC')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLEMI4553-1', 'OLEMI4553-1', '1', 'Exclude Single Actor Separation Of Duties', '{OLE}excludeSingleActorSeparationOfDutiesRoleTypeService', 'Y', 'OLE-SYS')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLEMI6886-TYP', 'OLEMI6886-TYP', '1', 'Payee ACH Account', '{OLE}payeeACHAccountRoleTypeService', 'Y', 'OLE-PDP')
/

INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) VALUES ('OLECNTRB199-TYP1', 'OLECNTRB199-TYP1', '1', 'Namespace or File Path', '{OLE}namespaceOrFilePathPermissionTypeService', 'Y', 'OLE-SYS')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_TYP_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 2, '7:850774528e297831ee499237cef596f0', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_TYP_ATTR_T::ole
INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE110', 'OLE110', '1', 'a', 'OLE62', 'OLE45', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE113', 'OLE113', '1', 'a', 'OLE68', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE114', 'OLE114', '1', 'b', 'OLE68', 'OLE24', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE40', 'OLE40', '1', 'a', 'OLE25', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE41', 'OLE41', '1', 'a', 'OLE26', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE42', 'OLE42', '1', 'b', 'OLE26', 'OLE23', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE43', 'OLE43', '1', 'a', 'OLE27', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE44', 'OLE44', '1', 'b', 'OLE27', 'OLE24', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE45', 'OLE45', '1', 'a', 'OLE28', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE46', 'OLE46', '1', 'b', 'OLE28', 'OLE24', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE47', 'OLE47', '1', 'c', 'OLE28', 'OLE25', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE48', 'OLE48', '1', 'b', 'OLE29', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE49', 'OLE49', '1', 'c', 'OLE29', 'OLE24', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE50', 'OLE50', '1', 'a', 'OLE29', '13', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE51', 'OLE51', '1', 'b', 'OLE30', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE52', 'OLE52', '1', 'c', 'OLE30', 'OLE24', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE53', 'OLE53', '1', 'a', 'OLE30', '13', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE54', 'OLE54', '1', 'd', 'OLE30', 'OLE26', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE55', 'OLE55', '1', 'e', 'OLE30', 'OLE27', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE56', 'OLE56', '1', 'f', 'OLE30', 'OLE28', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE57', 'OLE57', '1', 'b', 'OLE31', 'OLE30', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE58', 'OLE58', '1', 'a', 'OLE32', 'OLE33', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE59', 'OLE59', '1', 'b', 'OLE33', 'OLE31', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE60', 'OLE60', '1', 'a', 'OLE34', 'OLE32', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE61', 'OLE61', '1', 'a', 'OLE35', 'OLE35', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE62', 'OLE62', '1', 'a', 'OLE36', '4', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE63', 'OLE63', '1', 'b', 'OLE36', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE64', 'OLE64', '1', 'c', 'OLE36', 'OLE24', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE65', 'OLE65', '1', 'a', 'OLE37', 'OLE36', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE67', 'OLE67', '1', 'c', 'OLE33', '12', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE69', 'OLE69', '1', 'a', 'OLE31', '13', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE70', 'OLE70', '1', 'a', 'OLE33', '13', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE75', 'OLE75', '1', 'a', 'OLE46', 'OLE38', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE76', 'OLE76', '1', 'b', 'OLE47', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE77', 'OLE77', '1', 'c', 'OLE47', 'OLE23', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE78', 'OLE78', '1', 'e', 'OLE47', 'OLE39', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE79', 'OLE79', '1', 'a', 'OLE47', '13', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE82', 'OLE82', '1', 'a', 'OLE51', '13', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE85', 'OLE85', '1', 'a', 'OLE39', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE86', 'OLE86', '1', 'b', 'OLE39', 'OLE23', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE87', 'OLE87', '1', 'a', 'OLE40', 'OLE22', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLE92', 'OLE92', '1', 'a', 'OLE48', '42', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLESEC1001', 'OLESEC1001', '1', 'a', 'OLESEC1', '13', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLESEC1002', 'OLESEC1002', '1', 'b', 'OLESEC1', '6', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLESEC1003', 'OLESEC1003', '1', 'a', 'OLESEC2', '6', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLESEC1004', 'OLESEC1004', '1', 'a', 'OLESEC3', '4', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLESEC1005', 'OLESEC1005', '1', 'b', 'OLESEC3', '6', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLESEC1006', 'OLESEC1006', '1', 'a', 'OLESEC4', 'OLESEC102', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLESEC1007', 'OLESEC1007', '1', 'b', 'OLESEC4', 'OLESEC100', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLESEC1008', 'OLESEC1008', '1', 'c', 'OLESEC4', 'OLESEC101', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLESEC1009', 'OLESEC1009', '1', 'd', 'OLESEC4', 'OLESEC103', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLESEC1010', 'OLESEC1010', '1', 'd', 'OLE47', 'OLE24', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLEMI6886-TYPEATTR', 'OLEMI6886-TYPEATTR', '1', 'a', 'OLEMI6886-TYP', 'OLEMI6886-ATTRDEF', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLECNTRB199-TYPATTR1', 'OLECNTRB199-TYPATTR1', '1', 'a', 'OLECNTRB199-TYP1', '4', 'Y')
/

INSERT INTO KRIM_TYP_ATTR_T (KIM_TYP_ATTR_ID, OBJ_ID, VER_NBR, SORT_CD, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ACTV_IND) VALUES ('OLECNTRB199-TYPATTR2', 'OLECNTRB199-TYPATTR2', '1', 'b', 'OLECNTRB199-TYP1', 'OLECNTRB199-ATTRDEF1', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_TYP_ATTR_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 3, '7:1dc11b335853a2efe26bc88399a972e9', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_ROLE_T::ole
INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE7', 'OLE7', '1', 'Organization Reviewer', 'OLE-SYS', 'An optional role that allows users to receive workflow action requests for documents of a specified type that include a specified chart and organization (including the organization hierarchy). ', 'OLE29', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE9', 'OLE9', '1', 'Account Supervisor', 'OLE-SYS', 'This role derives its members from the Account Supervisor field on the Account. Account Supervisors receive workflow action requests for Asset and Asset Retirement Global documents. ', 'OLE39', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE11', 'OLE11', '1', 'Cash Manager', 'OLE-FP', 'Users authorized to work the Cash Management Document and verify Cash Receipt documents for a given campus.', '17', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE12', 'OLE12', '1', 'Disbursement Manager', 'OLE-FP', 'Users who receive workflow action requests for Disbursement Vouchers based on the campus code associated with the initiator of the document.', '17', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE13', 'OLE13', '1', 'Service Bill Processor', 'OLE-FP', 'Users authorized to use the Service Billing document and enter specified accounts on the Income side of the document.', 'OLE26', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE15', 'OLE15', '1', 'Travel Manager', 'OLE-FP', 'Users who receive workflow action requests for Disbursement Vouchers for travel payment reasons and can edit the accounting line and Non-Employee Travel Expense or Pre-Paid Travel Expenses tabs.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE16', 'OLE16', '1', 'Treasury Manager', 'OLE-SYS', 'Users who can use the Electronic Fund Transfer screen and use DI or YEDI documents to claim those funds.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE17', 'OLE17', '1', 'Interdepartmental Billing Processor', 'OLE-GL', 'Users authorized to use the Collector Upload screen.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE18', 'OLE18', '1', 'Customer Contact', 'OLE-PDP', 'Users associated with PDP customers that can use the Payment File Batch Upload screen and have basic PDP inquiry access. ', 'OLE32', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE19', 'OLE19', '1', 'Manager', 'OLE-PDP', 'Users who can cancel or hold payments reset locked format processes and view unmasked bank routing and account numbers in PDP.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE20', 'OLE20', '1', 'Processor', 'OLE-PDP', 'Users who can set payments for immediate pay and use the Format Checks/ACH screen in PDP.', '17', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE22', 'OLE22', '1', 'Accounts Payable Processor', 'OLE-PURAP', 'Accounts Payable users who can initiate Payment Requests and Credit Memo documents. They also have several permissions related to processing these document types and receive workflow action requests for them. ', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE23', 'OLE23', '1', 'Commodity Reviewer', 'OLE-PURAP', 'Users who receive workflow action requests for Purchasing transactional documents that contain a specific commodity code and campus combination.', 'OLE33', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE24', 'OLE24', '1', 'Content Reviewer', 'OLE-PURAP', 'Users who receive incomplete Requisition documents for completion for a given Chart and Organization.', 'OLE27', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE25', 'OLE25', '1', 'Contract Manager', 'OLE-PURAP', 'Contract Managers review and approve Purchase Order documents. A Purchase Order is assigned to a given Contract Manager for their review and approval.', 'OLE34', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE26', 'OLE26', '1', 'Purchasing Processor', 'OLE-PURAP', 'This role represents central or campus Purchasing staff. They have additional permissions for and receive action requests for most Purchasing document types as well as receiving action requests for Disbursement Vouchers paying PO Type Vendors.', '17', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE27', 'OLE27', '1', 'Sensitive Data Viewer', 'OLE-PURAP', 'Users authorized to view OLE-PURAP documents identified with a specific Sensitive Data Code. ', 'OLE35', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE28', 'OLE28', '1', 'Accounting Reviewer', 'OLE-SYS', 'An optional role that allows users to receive workflow action requests for documents of a specified type that contain accounts belonging to a specified chart and organization (including the organization hierarchy) and within a certain dollar amount or involving a specified override code.', 'OLE30', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE29', 'OLE29', '1', 'Accounts Payable Manager', 'OLE-SYS', 'Users with manager-level access to Accounts Payable documents. This includes the ability to hold or cancel (or remove those states) from Payment Request and Credit Memo documents. ', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE30', 'OLE30', '1', 'Accounts Receivable Lockbox Manager', 'OLE-SYS', 'Central Accounts Receivable staff that receive workflow action requests for Cash Control and Lockbox documents. They can also use the Electronic Fund Transfer screen and claim those funds using a Cash Control document.', 'OLE27', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE31', 'OLE31', '1', 'Accounts Receivable Manager', 'OLE-SYS', 'Users that manage the OLE-AR module. This role has no inherent permissions or responsibilities.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE32', 'OLE32', '1', 'Active Faculty or Staff', 'OLE-SYS', 'A role that uses the Affiliation Type and Employee Status on a Principal record to determine if a user is an active faculty or staff employee. These users can initiate some OLE-PURAP documents and inquire into certain OLE screens.', 'OLE41', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE33', 'OLE33', '1', 'Active Professional Employee', 'OLE-SYS', 'A role that uses the Employee Status (A L or P) and Employee Type (P) to determine that a given Principal represents a professional staff employee. These users are allowed to be Account Supervisors or Account Managers on Accounts.', 'OLE41', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE34', 'OLE34', '1', 'Asset Manager', 'OLE-SYS', 'Central Capital Assets staff capable of taking restricted actions on Assets  including retiring or transferring non-moveable assets.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE35', 'OLE35', '1', 'Asset Processor', 'OLE-SYS', 'Central Capital Assets staff capable of applying asset payments  using OLE-CAB and adding negative payments. This role contains permissions to modify restricted asset fields and to override the defined capitalization threshold.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE36', 'OLE36', '1', 'University Administration Budget Manager', 'OLE-SYS', 'An optional role created to receive action requests for Budget Adjustment documents at the Organization Route Node. Intended to receive requests for the top level chart and organization (thus receiving all Budget Adjustment documents).', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE37', 'OLE37', '1', 'Chart Manager', 'OLE-SYS', 'Defines users responsible for managing the chart data for a given Chart of Accounts code. They may initiate Global Object Code and Organization Reversion maintenance documents and modify the Campus and Organization Plant Chart Code and Account on Organization documents.', 'OLE25', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE38', 'OLE38', '1', 'Contracts & Grants Manager', 'OLE-SYS', 'Central contract and grant staff that have special permissions related to Effort Certification. They can override the edit that prevents transferring salary for an open effort reporting period and receive workflow action requests for Effort Certification Recreates.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE40', 'OLE40', '1', 'Contracts & Grants Project Director', 'OLE-SYS', 'This role defines the list of users that may be selected as Project Directors on the Proposal or Award document.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE41', 'OLE41', '1', 'Fiscal Officer', 'OLE-SYS', 'This role derives its members from the Fiscal Officer field on the Account. Fiscal Officers receive workflow action requests for most transactional documents and have edit permissions that allow them to change accounting lines involving their accounts.', 'OLE39', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE42', 'OLE42', '1', 'Fiscal Officer Primary Delegate', 'OLE-SYS', 'This role derives its members from the Primary delegates defined in the Account Delegate table in OLE.', 'OLE39', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE43', 'OLE43', '1', 'Fiscal Officer Secondary Delegate', 'OLE-SYS', 'This role derives its members from the Secondary delegates defined in the Account Delegate table in OLE.', 'OLE39', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE44', 'OLE44', '1', 'Manager', 'OLE-SYS', 'This role represents a collection of all the OLE module manager roles and has permission to initiate simple maintenance documents and restricted documents such as the JV and LLJV. These users also have the ability to blanket approve most document types and assign roles and permissions for all OLE namespaces.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE45', 'OLE45', '1', 'Operations', 'OLE-SYS', 'This role represents a very select central processing function allowed to run OLE batch jobs  initiate GLCP and LLCP documents and upload Enterprise Feed and Procurement Card files.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE46', 'OLE46', '1', 'Plant Fund Accountant', 'OLE-SYS', 'This role manages the plant fund functions associated with OLE-CAM and has special permissions related to assets in support of these functions. It can also edit the Organization and Campus Plant Chart and Account fields on the Organization document.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE47', 'OLE47', '1', 'Purchasing Manager', 'OLE-SYS', 'Users that manage the OLE-PURAP module. This role can take the resend action on Purchase Order documents.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE48', 'OLE48', '1', 'Sub-Fund Reviewer', 'OLE-SYS', 'Users who receive workflow action requests for documents that include accounts belonging to particular sub-funds groups.', 'OLE31', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE49', 'OLE49', '1', 'Tax Identification Number User', 'OLE-SYS', 'Users with a need to view unmasked Tax ID numbers. They can also modify the tax number associated with AR customer records and PURAP vendor records.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE50', 'OLE50', '1', 'Tax Manager', 'OLE-SYS', 'Represents a central tax area that receives workflow action requests for DVs  Payment Requests  and POs involving payments to non-resident aliens or employees. They can also edit the Tax tabs on the DV and Payment Request documents.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE51', 'OLE51', '1', 'Technical Administrator', 'OLE-SYS', 'A technical administrator that is specific to the OLE system. This role has no inherent permissions or responsibilities.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE53', 'OLE53', '1', 'University Chart Manager', 'OLE-SYS', 'This role derives its members from the OLE Chart table. It is used to determine the Chart Manager of the top level Chart in the organization hierarchy. This role receives workflow action requests for Chart documents and has the ability to edit the organization and campus Plant Chart and Account fields on the Organization document.', 'OLE40', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE54', 'OLE54', '1', 'User', 'OLE-SYS', 'The basic role that grants users access to OLE. It gives users the ability to initiate most documents and use inquiries and search screens. Users are qualified by namespace  chart and organization. If these fields are not defined the chart and organization are inherited from the Department ID on the users'' Principal record.', 'OLE36', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE55', 'OLE55', '1', 'Workflow Administrator', 'OLE-SYS', 'Users capable of taking superuser action on OLE documents and blanket approving some document types not available to the OLE-SYS Manager role.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE56', 'OLE56', '1', 'Reviewer', 'OLE-VND', 'This role receives workflow action requests for the Vendor document.', 'OLE37', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE62', 'OLE62', '1', 'System User', 'OLE-SYS', 'This role represents the OLE System User  that is the user ID the system uses when it takes programmed actions (such as auto-initiating or approving documents such as the PCDO and PO).', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE65', 'OLE65', '1', 'Regional Budget Manager', 'OLE-SYS', 'An optional role created to receive action requests for Budget Adjustment documents at the Organization route Node. Intended to receive only requests associated with regional campus charts and organizations.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE68', 'OLE68', '1', 'Sub-Account Reviewer', 'OLE-PURAP', 'Users who wish to receive workflow action requests for OLE-PURAP documents that involve a specific account number and sub-account number.', 'OLE47', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE69', 'OLE69', '1', 'Source Document Router', 'OLE-PURAP', 'Identifies the user who routed the source document (Requisition) for a OLE-PURAP document.', 'OLE48', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE70', 'OLE70', '1', 'Disbursement Method Reviewer', 'OLE-FP', 'Users who receive workflow action requests for Disbursement Vouchers with specified payment methods and can edit the accounting lines and Wire Transfer and Foreign Draft tabs.', 'OLE46', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE72', 'OLE72', '1', 'Award Project Director', 'OLE-SYS', 'This role is derived from the accounts appearing on an Effort Certification document. OLE finds the most recent award associated with each account and routes workflow action requests to the Project Director''s associated with the accounts on the Effort Certification document. ', 'OLE39', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE79', 'OLE79', '1', 'Budget Reviewer', 'OLE-PURAP', 'Central administration users charged with reviewing Purchase Order documents that exceed an account''s sufficient funds balance.', 'OLE25', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE82', 'OLE82', '1', 'Batch Job Modifier', 'OLE-SYS', 'This role is derived from users with the Modify Batch Job permission. They are able to use the Schedule lookup. ', 'OLE58', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE84', 'OLE84', '1', 'Potentially Sensitive Document User', 'OLE-PURAP', 'This role houses other roles and indicates which of those can view OLE-PURAP documents that have been identified as potentially sensitive. ', 'OLE62', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE85', 'OLE85', '1', 'Sensitive Related Document Initiator Or Reviewer', 'OLE-PURAP', 'A role that derives the users who initiated or received a workflow action request for a sensitive OLE-PURAP document.', 'OLE48', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE86', 'OLE86', '1', 'Payment Request Hold / Cancel Initiator', 'OLE-PURAP', 'This role derives users who placed a Payment Request or Credit Memo on hold or canceled it in order to determine who can remove those actions. ', 'OLE61', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE92', 'OLE92', '1', 'Cash Receipt Initiator', 'OLE-FP', 'Users authorized to initiate Cash Receipt documents. This role exists to exclude Cash Managers from being able to initiate Cash Receipt documents. You do not need to add explicit members to this role to accomplish this exclusion.', 'OLE65', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE93', 'OLE93', '1', 'Active Employee & Financial System User', 'OLE-SYS', 'A role that uses the Employee Status (A L or P) along with the presence of the OLE-SYS User role to determine that a given Principal represents an employee with OLE access. These users are allowed to be fiscal Officers on Accounts.', 'OLE41', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE94', 'OLE94', '1', 'Active Professional Employee & Financial System User', 'OLE-SYS', 'A role that uses the Employee Status (A L or P) and Employee Type (P)  along with the presence of the OLE-SYS User role to determine that a given Principal represents a professional staff employee with OLE access. These users are allowed to be fiscal Officers on Accounts.', 'OLE41', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE96', 'OLE96', '1', 'Financial Processing Manager', 'OLE-FP', 'Users that manage the OLE-FP module. This role has no inherent permissions or responsibilities.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE98', 'OLE98', '1', 'Accounts Payable Document Reviewer', 'OLE-PURAP', 'Users who receive workflow action requests for Accounts Payable transactional documents.', 'OLE69', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLESYS1', 'OLESYS7145', '1', 'YearEnd', 'OLE-SYS', 'Year End', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLEMI4553-2', 'OLEMI4553-2', '1', 'Exclude Single Actor Reviewer', 'OLE-VND', '', 'OLEMI4553-1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLEMI6886-ROLE', 'OLEMI6886-ROLE', '1', 'Payee ACH Accounts Administrator', 'OLE-PDP', 'Administration staff who can view and update Payee ACH Account records and the legacy history of a certain ACH Transaction Type.', 'OLEMI6886-TYP', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLECNTRB789-ROLE1', 'OLECNTRB789-ROLE1', '1', 'Workstudy Reviewer', 'OLE-SYS', 'Workstudy Reviewer Role', '1', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ROLE_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 4, '7:65005d762d1c3979c947b0f5b8b3d32e', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_ROLE_MBR_T::ole
INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1308', 'OLE1308', 'OLE17', 'OLE45', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1326', 'OLE1326', 'OLE18', 'OLE20', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1327', 'OLE1327', 'OLE18', 'OLE47', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1328', 'OLE1328', 'OLE18', 'OLE47', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1329', 'OLE1329', 'OLE18', 'OLE47', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1330', 'OLE1330', 'OLE18', 'OLE47', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1331', 'OLE1331', 'OLE18', 'OLE47', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1332', 'OLE1332', 'OLE18', 'OLE47', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1333', 'OLE1333', 'OLE18', 'OLE47', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1334', 'OLE1334', 'OLE18', 'OLE47', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1309', 'OLE1309', 'OLE19', 'OLE45', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1640', 'OLE1640', 'OLE20', 'OLE19', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1310', 'OLE1310', 'OLE22', 'OLE29', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1311', 'OLE1311', 'OLE26', 'OLE47', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1354', 'OLE1354', 'OLE26', 'OLE25', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1339', 'OLE1339', 'OLE28', 'OLE36', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1312', 'OLE1312', 'OLE35', 'OLE34', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1313', 'OLE1313', 'OLE35', 'OLE46', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1314', 'OLE1314', 'OLE44', 'OLE45', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1315', 'OLE1315', 'OLE44', 'OLE31', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1316', 'OLE1316', 'OLE44', 'OLE35', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1317', 'OLE1317', 'OLE44', 'OLE38', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1318', 'OLE1318', 'OLE44', 'OLE29', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1319', 'OLE1319', 'OLE44', 'OLE47', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1325', 'OLE1325', 'OLE44', 'OLE36', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1355', 'OLE1355', 'OLE44', 'OLE53', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1703', 'OLE1703', 'OLE44', 'OLE50', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1704', 'OLE1704', 'OLE44', 'OLE96', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1655', 'OLE1655', 'OLE53', 'OLE37', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1337', 'OLE1337', 'OLE56', 'OLE22', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1324', 'OLE1324', '63', 'OLE51', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1348', 'OLE1348', 'OLE70', 'OLE22', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1349', 'OLE1349', 'OLE70', 'OLE16', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1606', 'OLE1606', 'OLE84', 'OLE22', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1607', 'OLE1607', 'OLE84', 'OLE26', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1608', 'OLE1608', 'OLE84', 'OLE27', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1609', 'OLE1609', 'OLE84', 'OLE85', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1610', 'OLE1610', 'OLE84', 'OLE54', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1611', 'OLE1611', 'OLE84', 'OLE32', 'R')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1612', 'OLE1612', 'OLE55', 'olequickstart', 'P')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLEGOBIAPI1', 'OLEGOBIAPI1', 'OLE10058', 'gobiapi', 'P')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLEGOBIAPI2', 'OLEGOBIAPI2', 'OLE10077', 'gobiapi', 'P')
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLEGOBIAPI3', 'OLEGOBIAPI3', 'OLE10085', 'gobiapi', 'P')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ROLE_MBR_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 5, '7:12a4ca847d88828c1113658b638c40a5', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_ROLE_MBR_ATTR_DATA_T::ole
INSERT INTO KRIM_ROLE_MBR_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, ROLE_MBR_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE4040', 'OLE4040', '1', 'OLE1311', '17', '12', '*')
/

INSERT INTO KRIM_ROLE_MBR_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, ROLE_MBR_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE4041', 'OLE4041', '1', 'OLE1354', '17', '12', '*')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ROLE_MBR_ATTR_DATA_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 6, '7:3cefccf9ae9d93b08211d7fef7f808c7', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_PERM_TMPL_T::ole
INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE41', 'OLE41', '1', 'OLE-SYS', 'Modify Accounting Lines', '', '52', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE48', 'OLE48', '1', 'OLE-SYS', 'Edit Bank Code', '', '3', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE50', 'OLE50', '1', 'OLE-SYS', 'Administer Batch File', '', 'OLECNTRB199-TYP1', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE6', 'OLE6', '1', 'OLE-SYS', 'Error Correct Document', '', 'OLE51', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE7', 'OLE7', '1', 'OLE-SYS', 'Claim Electronic Payment', '', 'OLE51', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLESEC1001', 'OLESEC1001', '1', 'OLE-SEC', 'View Document with Field Value', '', 'OLESEC1', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLESEC1002', 'OLESEC1002', '1', 'OLE-SEC', 'View Accounting Line with Field Value', '', 'OLESEC1', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLESEC1003', 'OLESEC1003', '1', 'OLE-SEC', 'View Notes/Attachments with Field Value', '', 'OLESEC1', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLESEC1004', 'OLESEC1004', '1', 'OLE-SEC', 'Edit Document with Field Value', '', 'OLESEC1', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLESEC1005', 'OLESEC1005', '1', 'OLE-SEC', 'Edit Accounting Line with Field Value', '', 'OLESEC1', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLESEC1006', 'OLESEC1006', '1', 'OLE-SEC', 'Lookup with Field Value', '', 'OLESEC2', 'Y')
/

INSERT INTO KRIM_PERM_TMPL_T (PERM_TMPL_ID, OBJ_ID, VER_NBR, NMSPC_CD, NM, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLESEC1007', 'OLESEC1007', '1', 'OLE-SEC', 'Balance Inquiry with Field Value', '', 'OLESEC3', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_PERM_TMPL_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 7, '7:d47713d5d506e6d4aef79e9e137ee4e0', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_PERM_T::ole
INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE55', 'OLE55', '1', '10', 'OLE-COA', 'Initiate Document GOBJ', 'Authorizes the initiation of the Global Object Code Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE56', 'OLE56', '1', '10', 'OLE-COA', 'Initiate Document ORGR', 'Authorizes the initiation of the Organization Reversion Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE58', 'OLE58', '1', '1', 'OLE-COA', 'Edit Inactive Account', 'Users who can edit Accounts that are inactive.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE59', 'OLE59', '1', '1', 'OLE-COA', 'Serve As Account Manager', 'Identifies users that can be Account Managers', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE60', 'OLE60', '1', '26', 'OLE-COA', 'Modify Maintenance Document Field SubAccount a21SubAccount', 'Authorizes users to edit the Sub-Account Type Code on the Sub-Account document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE61', 'OLE61', '1', '26', 'OLE-COA', 'Modify Maintenance Document Field Organization organizationPlantChartCode', 'Users who can edit the Organization Plant Chart Code on the Organization document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE62', 'OLE62', '1', '26', 'OLE-COA', 'Modify Maintenance Document Field Organization organizationPlantAccountNumber', 'Users who can edit the Organization Plant Account Number on the Organization document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE63', 'OLE63', '1', '26', 'OLE-COA', 'Modify Maintenance Document Field Organization campusPlantChartCode', 'Users who can edit the Campus Plant Chart Code on the Organization document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE64', 'OLE64', '1', '26', 'OLE-COA', 'Modify Maintenance Document Field Organization campusPlantAccountNumber', 'Users who can edit the Campus Plant Account Number on the Organization document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE66', 'OLE66', '1', 'OLE7', 'OLE-FP', 'Claim Electronic Payment DI', 'Users who can use a Distribution of Income and Expense document to claim Electronic Payments.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE67', 'OLE67', '1', 'OLE7', 'OLE-FP', 'Claim Electronic Payment YEDI', 'Users who can use a Year End Distribution of Income and Expense document to claim Electronic Payments.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE69', 'OLE69', '1', '10', 'OLE-FP', 'Initiate Document CMD', 'Authorizes the initiation of the Cash Management Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE70', 'OLE70', '1', '10', 'OLE-FP', 'Initiate Document SB', 'Authorizes the initiation of the Service Billing Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE71', 'OLE71', '1', '10', 'OLE-FP', 'Initiate Document JV', 'Authorizes the initiation of the Journal Voucher Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE72', 'OLE72', '1', '10', 'OLE-FP', 'Initiate Document PCDO', 'Authorizes the initiation of the  Procurement Card Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE73', 'OLE73', '1', '27', 'OLE-FP', 'Full Unmask Field ProcurementCardHolder transactionCreditCardNumber', 'Authorizes users to view the entire Credit Card number on the Procurement Card document and Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE74', 'OLE74', '1', '33', 'OLE-FP', 'Upload Batch Input File(s) procurementCardInputFileType', 'Authorizes users to access the Procurement Card Upload page.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE75', 'OLE75', '1', '10', 'OLE-GL', 'Initiate Document GLCP', 'Authorizes the initiation of the  General Ledger Correction Process Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE76', 'OLE76', '1', '33', 'OLE-GL', 'Upload Batch Input File(s) collectorXmlInputFileType', 'Authorizes user to access the Collector XML Upload page.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE77', 'OLE77', '1', '33', 'OLE-GL', 'Upload Batch Input File(s) enterpriseFeederFileSetType', 'Authorizes user to access the Enterprise Feed Upload page.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80', 'OLE80', '1', '1', 'OLE-PDP', 'Set as Immmediate Pay', 'Authorizes users to set and remove immediate status on payments in PDP.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE81', 'OLE81', '1', '10', 'OLE-PDP', 'Initiate Document PDSM', 'Authorizes the initiation of Pre-Disbursement Processor Simple Maintenance Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE82', 'OLE82', '1', '1', 'OLE-PDP', 'Cancel Payment', 'Authorizes users to take the Cancel action on payments in PDP.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE83', 'OLE83', '1', '1', 'OLE-PDP', 'Format', 'Authorizes users to access and run the Format Checks /ACH screen in PDP', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE84', 'OLE84', '1', '1', 'OLE-PDP', 'Hold Payment / Remove Non-Tax Payment Hold', 'Authorizes users to hold payments and remove non-tax related holds on payments in PDP.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE86', 'OLE86', '1', '1', 'OLE-PDP', 'Remove Format Lock', 'Users who can reset a format process in PDP.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE87', 'OLE87', '1', '1', 'OLE-PDP', 'Remove Payment Tax Hold', 'Authorizes users to remove payments held for tax review in PDP.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE88', 'OLE88', '1', '24', 'OLE-PDP', 'Inquire Into Records KFS-PDP', 'Allows users to access Pre Disbursement Processor inquiries.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE89', 'OLE89', '1', '23', 'OLE-PDP', 'Look Up Records KFS-PDP', 'Allow users to access Pre Disbursement Processor lookups.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE90', 'OLE90', '1', '33', 'OLE-PDP', 'Upload Batch Input File(s) paymentInputFileType', 'Allows access to the Manually Upload Payment File screen in PDP.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE91', 'OLE91', '1', '4', 'OLE-PURAP', 'Blanket Approve Document PREQ', 'Allows access to the Blanket Approval button on the Payment Request Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE92', 'OLE92', '1', '10', 'OLE-PURAP', 'Initiate Document REQS', 'Authorizes the initiation of the Requisition Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE93', 'OLE93', '1', '10', 'OLE-PURAP', 'Initiate Document PUR', 'Authorizes the initiation of Purchasing Transactional Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE94', 'OLE94', '1', '10', 'OLE-PURAP', 'Initiate Document PO', 'Authorizes the initiation of the Purchase Order Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE95', 'OLE95', '1', '10', 'OLE-PURAP', 'Initiate Document POC', 'Authorizes the initiation of the Purchase Order Close Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE96', 'OLE96', '1', '10', 'OLE-PURAP', 'Initiate Document PORT', 'Authorizes the initiation of the Purchase Order Retransmit Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE97', 'OLE97', '1', '10', 'OLE-PURAP', 'Initiate Document PREQ', 'Authorizes the initiation of the Payment Request Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE98', 'OLE98', '1', '10', 'OLE-PURAP', 'Initiate Document RCV', 'Authorizes the initiation of Receiving Transactional Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE99', 'OLE99', '1', '10', 'OLE-PURAP', 'Initiate Document EIRT', 'Authorizes the initiation of the Electronic Invoice Reject Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE100', 'OLE100', '1', '16', 'OLE-PURAP', 'Edit Document REQS PreRoute', 'Edit permission for the Requisition document prior to the document being submitted.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE101', 'OLE101', '1', '16', 'OLE-PURAP', 'Edit Document PO PreRoute', 'Edit permission for the Purchase Order document prior to the document being submitted.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE102', 'OLE102', '1', '16', 'OLE-PURAP', 'Edit Document AP PreRoute', 'Edit permission for Accounts Payable Transactional documents prior to the document being submitted.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE103', 'OLE103', '1', '16', 'OLE-PURAP', 'Edit Document RCV PreRoute', 'Edit permission for Receiving Transactional documents prior to the document being submitted.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE107', 'OLE107', '1', 'OLE7', 'OLE-SYS', 'Claim Electronic Payment', 'Allows access to the Electronic Fund Transfer interface for the claiming of electronic funds.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE108', 'OLE108', '1', 'OLE6', 'OLE-SYS', 'Error Correct Document KFST', 'Allows access to the Error Correction button on OLE Transactional documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE113', 'OLE113', '1', '3', 'OLE-SYS', 'Administer Routing for Document KFS', 'Allows users to open OLE documents via the Super search option in Document Search and take Administrative workflow actions on them (such as approving the document  approving individual requests  or sending the document to a specified route node).', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE114', 'OLE114', '1', '4', 'OLE-SYS', 'Blanket Approve Document KFS', 'Allows access to the Blanket Approval button on OLE Financial System Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE115', 'OLE115', '1', '10', 'OLE-SYS', 'Initiate Document KFS', 'Authorizes the initiation of Financial System Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE116', 'OLE116', '1', '10', 'OLE-SYS', 'Initiate Document FSSM', 'Authorizes the initiation of Financial System Simple Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE117', 'OLE117', '1', '10', 'OLE-SYS', 'Initiate Document BANK', 'Authorizes the initiation of the Bank Maintenance Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE119', 'OLE119', '1', '35', 'OLE-SYS', 'Assign Role KFS*', 'Authorizes users to modify the information on the Assignees Tab of the Role Document and the Roles section of the Membership Tab on the Person Document for roles with a Module Code beginning with OLE.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE120', 'OLE120', '1', '36', 'OLE-SYS', 'Grant Permission KFS*', 'Authorizes users to modify the information on the Permissions tab of the Role Document for roles with a module code beginning with OLE.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE121', 'OLE121', '1', '37', 'OLE-SYS', 'Grant Responsibility KFS*', 'Authorizes users to modify the information on the Responsibility tab of the Role Document for roles with a Module Code that begins with OLE.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE122', 'OLE122', '1', '38', 'OLE-SYS', 'Populate Group KFS*', 'Authorizes users to modify the information on the Assignees Tab of the Group Document and the Group section of the Membership Tab on the Person Document for groups with namespaces beginning with OLE.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE128', 'OLE128', '1', '27', 'OLE-SYS', 'Full Unmask Field Bank bankAccountNumber', 'Authorizes users to view the entire bank account number on the Bank document and Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE129', 'OLE129', '1', '24', 'OLE-SYS', 'Inquire Into Records KFS*', 'Allows users to access OLE inquiries.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE130', 'OLE130', '1', '23', 'OLE-SYS', 'Look Up Records KFS*', 'Allow users to access OLE lookups.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE131', 'OLE131', '1', '34', 'OLE-SYS', 'Maintain System Parameter KFS*', 'Authorizes users to initiate and edit the Parameter document for pameters with a module code beginning with OLE.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE132', 'OLE132', '1', '32', 'OLE-SYS', 'Modify Batch Job KFS*', 'Allows users to access and run Batch Jobs associated with OLE modules via the Schedule link.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE133', 'OLE133', '1', '40', 'OLE-SYS', 'Open Document KFS', 'Authorizes users to open OLE Financial System Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE134', 'OLE134', '1', '28', 'OLE-SYS', 'Partial Unmask Field Bank bankAccountNumber', 'Authorizes users to view the last four-digits of the bank account number on the Bank document and Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE135', 'OLE135', '1', '29', 'OLE-SYS', 'Use Screen KFS*', 'Allows users access to screens in the OLE that are not documents  lookups  inquiries  or batch uploads.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE136', 'OLE136', '1', '10', 'OLE-VND', 'Initiate Document PVEN', 'Authorizes users to initiate the Vendor Maintenance Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE137', 'OLE137', '1', '27', 'OLE-VND', 'Full Unmask Field VendorDetail vendorHeader.vendorTaxNumber', 'Authorizes users to view the entire Tax Number on the Vendor Maintenance Document and Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE138', 'OLE138', '1', '26', 'OLE-VND', 'Modify Maintenance Document Field VendorDetail vendorHeader.vendorTaxNumber', 'Authorizes users to modify the Tax Number on a Vendor Maintenance Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE139', 'OLE139', '1', '44', 'OLE-VND', 'Modify Maintenance Document Section VendorDetail vendorContracts', 'Authorizes users to see and edit the Contracts Tab on the Vendor Maintenance Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE169', 'OLE169', '1', '15', 'OLE-SYS', 'Save Document KFS', 'Authorizes user to save documents answering to the FinancialSystemDocument parent document Type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE184', 'OLE184', '1', '44', 'OLE-VND', 'Modify Maintenance Document Section VendorDetail vendorCommodities', 'Allows users to modify the Vendor Commodity Codes tab on the Vendor document. ', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE185', 'OLE185', '1', '16', 'OLE-FP', 'Edit Document CMD PreRoute', 'Authorizes users who can edit the Cash Management Document prior to it being submitted for routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE187', 'OLE187', '1', '27', 'OLE-FP', 'Full Unmask Field DisbursementPayee taxNumber', 'Allows users to view the tax id in the Disbursement Voucher.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE189', 'OLE189', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines SB PreRoute sourceAccountingLines', 'Allows users to modify the Target accounting lines on a Procurement Card Document that is at the Account Full Entry Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE190', 'OLE190', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines FP Account sourceAccountingLines', 'Allows users to modify the Source accounting lines on documents answering to the parent document Financial Processing Transactional Document when a document is at the Account Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE191', 'OLE191', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines FP Account targetAccountingLines', 'Allows users to modify the Target accounting lines on documents answering to the parent document Financial Processing Transactional Document when a document is at the Account Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE192', 'OLE192', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines DV Campus sourceAccountingLines.financialObjectCode', 'Allows users to modify the object code of Source accounting lines on a Disbursement Voucher document that is at the Campus Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE193', 'OLE193', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines DV Tax sourceAccountingLines.amount', 'Allows users to modify the amount of Source accounting lines on a Disbursement Voucher document that is at the Tax Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE194', 'OLE194', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines DV Travel sourceAccountingLines.amount', 'Allows users to modify the amount of Source accounting lines on a Disbursement Voucher document that is at the Travel Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE195', 'OLE195', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines DV PaymentMethod sourceAccountingLines.amount', 'Allows users to modify the amount of Source accounting lines on a Disbursement Voucher document that is at the Payment Method Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE198', 'OLE198', '1', 'OLE41', 'OLE-PURAP', 'Modify Accounting Lines PUR Account items.sourceAccountingLines', 'Allows users to modify the Source accounting lines on documents answering to the parent document Purchasing Transactional Document when a document is at the Account Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE199', 'OLE199', '1', 'OLE41', 'OLE-PURAP', 'Modify Accounting Lines AP Account items.sourceAccountingLines', 'Allows users to modify the Source accounting lines on documents answering to the parent document Accounts PayableTransactional Document when a document is at the Account Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE209', 'OLE209', '1', '31', 'OLE-FP', 'Use Transactional Document DV taxEntry', 'Users who can complete the Nonresident Alien Tax Tab on the Disbursement Voucher document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE210', 'OLE210', '1', '31', 'OLE-FP', 'Use Transactional Document DV frnEntry', 'Users who can modify the Foreign Draft tab and disbursement amount on Disbursement Voucher documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE211', 'OLE211', '1', '31', 'OLE-FP', 'Use Transactional Document DV wireEntry', 'Users who can modify Wire Transfer tab and disbursement amount on Disbursement Voucher documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE212', 'OLE212', '1', '31', 'OLE-FP', 'Use Transactional Document DV travelEntry', 'Users who can modify Non-Employee Travel Expense tab  disbursement amount on Disbursement Voucher documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE217', 'OLE217', '1', '31', 'OLE-PURAP', 'Use Transactional Document AP editPreExtract', 'Users who can edit specific data on a Payment Request or Credit Memo before the document is extracted to PDP.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE220', 'OLE220', '1', 'OLE48', 'OLE-SYS', 'Edit Bank Code OpenLibraryEnvironmentTransactionalDocument', 'Users who can edit the Bank Code field on documents answering to the parent document Financial System Transactional Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE233', 'OLE233', '1', '28', 'OLE-PDP', 'Partial Unmask Field PayeeACHAccount bankAccountNumber', 'Authorizes users to view the last four-digits of the bank account number on the Payee ACH document and Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE234', 'OLE234', '1', '27', 'OLE-PDP', 'Full Unmask Field ACHBank bankRoutingNumber', 'Authorizes users to view the entire bank routing number on the ACH Bank document and Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE235', 'OLE235', '1', '27', 'OLE-PDP', 'Full Unmask Field PayeeACHAccount bankAccountNumber', 'Authorizes users to view the entire bank account number on the Payee ACH document and Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE236', 'OLE236', '1', '27', 'OLE-PDP', 'Full Unmask Field PayeeACHAccount bankRoutingNumber', 'Authorizes users to view the entire bank routing number on the Payee ACH document and Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE238', 'OLE238', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines FP PreRoute sourceAccountingLines', 'Allows users to modify the Source accounting lines on documents answering to the parent document Accounts Financial Processing Transactional Document when a document has not yet been submitted for routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE239', 'OLE239', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines FP PreRoute targetAccountingLines', 'Allows users to modify the Target accounting lines on documents answering to the parent document Financial Processing Transactional Document when a document has not yet been submitted for routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE241', 'OLE241', '1', 'OLE41', 'OLE-PURAP', 'Modify Accounting Lines PUR PreRoute items.sourceAccountingLines', 'Allows users to modify the Source accounting lines on documents answering to the parent document Purchasing Transactional Document when a document has not yet been submitted for routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE242', 'OLE242', '1', 'OLE41', 'OLE-PURAP', 'Modify Accounting Lines AP PreRoute items.sourceAccountingLines', 'Allows users to modify the Source accounting lines on documents answering to the parent document Accounts PayableTransactional Document when a document has not yet been submitted for routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE256', 'OLE256', '1', '23', 'OLE-SYS', 'Look Up Records BatchJobStatus', 'Allow users to access the Batch File lookup.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE257', 'OLE257', '1', '29', 'OLE-PURAP', 'Use Screen org.kuali.ole.module.purap.web.struts.B2BAction', 'Authorizes users to take the Shop Catalogs (or B2B) action.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE258', 'OLE258', '1', '45', 'OLE-PURAP', 'Add Note / Attachment PREQ Invoice Image', 'Users who can add notes and attachments to the Payment Request document when it is at the Invoice Attachment route node.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE260', 'OLE260', '1', '46', 'OLE-PURAP', 'View Note / Attachment PREQ Invoice Image', 'Authorizes users to view attachments with a type of "Invoice Image" on Payment Request documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE263', 'OLE263', '1', '47', 'OLE-SYS', 'Delete Note / Attachment OLE FALSE', 'Authorizes users to delete notes and attachments created by any user on documents answering to the Financial System Document parent document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE268', 'OLE268', '1', '16', 'OLE-PURAP', 'Edit Document PREQ R', 'Authorizes users who can edit Payment Request Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE269', 'OLE269', '1', '40', 'OLE-PURAP', 'Open Document PRAP', 'Authorizes users to open Purchasing Accounts Payable Transactional documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE270', 'OLE270', '1', '29', 'OLE-PURAP', 'Use Screen org.kuali.kfs.module.purap.document.web.struts.PrintAction', 'Authorizes users to take the Print action on a Purchase Order.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE271', 'OLE271', '1', '29', 'OLE-SYS', 'Use Screen org.kuali.kfs.sys.web.struts.ElectronicFundTransferAction', 'Allows users to access the Electronic Funds Transfer screen.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE272', 'OLE272', '1', '31', 'OLE-PURAP', 'Use Transactional Document PO printPurchaseOrder', 'Users who can print a Purchase Order document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE273', 'OLE273', '1', '31', 'OLE-PURAP', 'Use Transactional Document PO previewPrintPurchaseOrder', 'Users who can preview a Purchase Order document before printing it.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE274', 'OLE274', '1', '31', 'OLE-PURAP', 'Use Transactional Document PO assignSensitiveData', 'Users who can assign sensitive data to a Purchase Order document which locks down who is allowed to view the PO and its related documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE275', 'OLE275', '1', '31', 'OLE-PURAP', 'Use Transactional Document PO resendPurchaseOrder', 'Users who can resend Purchase Order cxml to the B2B integrator.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE276', 'OLE276', '1', '31', 'OLE-PURAP', 'Use Transactional Document PREQ requestPaymentRequestCancel', 'Users authorized to take the Request Cancel action on Payment Request documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE277', 'OLE277', '1', '31', 'OLE-PURAP', 'Use Transactional Document PREQ paymentRequestHoldCancelRemoval', 'Authorizes users to remove Holds or Cancels on Payment Request documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE280', 'OLE280', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines PCDO AccountFullEdit sourceAccountingLines', 'Allows users to modify the Source accounting lines on a Procurement Card Document that is at the Account Full Entry Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE281', 'OLE281', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines PCDO AccountFullEdit targetAccountingLines', 'Allows users to modify the Target accounting lines on a Procurement Card Document that is at the Account Full Entry Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE282', 'OLE282', '1', '31', 'OLE-PURAP', 'Use Transactional Document AP managerCancel', 'Users who can cancel Payment Request or Credit Memo documents at a manager level.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE283', 'OLE283', '1', '31', 'OLE-PURAP', 'Use Transactional Document AP processorCancel', 'Users who can cancel Payment Request or Credit Memo documents at a processor level.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE284', 'OLE284', '1', '40', 'OLE-PURAP', 'Open Document ACM', 'Authorizes users to open the Contract Manager Assignment Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE285', 'OLE285', '1', '29', 'OLE-PDP', 'Use Screen KFS-PDP', 'Allows users to access all Pre-Disbursement Processor screens.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE286', 'OLE286', '1', '31', 'OLE-PURAP', 'Use Transactional Document PREQ requestPaymentRequestHold', 'Authorizes users to put Payment Request documents on Hold.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE287', 'OLE287', '1', '29', 'OLE-GL', 'Use Screen org.kuali.kfs.gl.web.struts.BalanceInquiryAction', 'Allows users to access Balance Inquiry screens. ', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE291', 'OLE291', '1', '40', 'OLE-PURAP', 'Open Document EIRT', 'Authorizes users to open the Electronic Invoice Reject Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE292', 'OLE292', '1', '33', 'OLE-PURAP', 'Upload Batch Input File(s) electronicInvoiceInputFileType', 'Authorizes users to upload eInvoice files.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE293', 'OLE293', '1', '31', 'OLE-PURAP', 'Use Transactional Document CM requestVendorCreditMemoHold', 'Authorizes users to put Credit Memo documents on Hold.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE294', 'OLE294', '1', '31', 'OLE-PURAP', 'Use Transactional Document CM vendorCreditMemoHoldRemoval', 'Authorizes users to remove a Hold from Credit Memo documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE301', 'OLE301', '1', '29', 'OLE-GL', 'Use Screen org.kuali.kfs.sys.web.struts.KualiBalanceInquiryReportMenuAction', 'Allows users to see menu of balance inquiries after hitting "balance inquiry" button on an accounting line in an accounting document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE308', 'OLE308', '1', 'OLE41', 'OLE-PURAP', 'Modify Accounting Lines REQS Initiator items.sourceAccountingLines', 'Allows users to modify the Source accounting lines on a Requisition Document that is at the Initiator Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE313', 'OLE313', '1', '45', 'OLE-PURAP', 'Add Note / Attachment PO Contracts', 'Authorizes users to add notes and attachments with a type of "Contracts" to the Purchase Order document. ', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE314', 'OLE314', '1', '46', 'OLE-PURAP', 'View Note / Attachment PO Contracts', 'Authorizes users to view attachments with a type of "Contract" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE315', 'OLE315', '1', '45', 'OLE-PURAP', 'Add Note / Attachment PO Contract Ammendments', 'Authorizes users to add notes and attachments with a type of "Contract" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE316', 'OLE316', '1', '46', 'OLE-PURAP', 'View Note / Attachment PO Contract Ammendments', 'Authorizes users to view attachments with a type of "Contract Amendments" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE317', 'OLE317', '1', '45', 'OLE-PURAP', 'Add Note / Attachment PO Quotes', 'Authorizes users to add notes and attachments with a type of "Quotes" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE318', 'OLE318', '1', '46', 'OLE-PURAP', 'View Note / Attachment PO Quotes', 'Authorizes users to view attachments with a type of "Quotes" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE319', 'OLE319', '1', '45', 'OLE-PURAP', 'Add Note / Attachment PO RFPs', 'Authorizes users to add notes and attachments with a type of "RFPs" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE320', 'OLE320', '1', '46', 'OLE-PURAP', 'View Note / Attachment PO RFPs', 'Authorizes users to view attachments with a type of "RFPs" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE321', 'OLE321', '1', '45', 'OLE-PURAP', 'Add Note / Attachment PO RFP Responses', 'Authorizes users to add notes and attachments with a type of "RFP" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE322', 'OLE322', '1', '46', 'OLE-PURAP', 'View Note / Attachment PO RFP Responses', 'Authorizes users to view attachments with a type of "RFP" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE323', 'OLE323', '1', '45', 'OLE-PURAP', 'Add Note / Attachment PO Other - Restricted', 'Authorizes users to add notes or attachments with a type of "Other-Restricted" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE324', 'OLE324', '1', '46', 'OLE-PURAP', 'View Note / Attachment PO Other - Restricted', 'Authorizes users to view attachments with a type of "Other-Restricted" on Purchase Order documents and documents answering to that document type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE325', 'OLE325', '1', '45', 'OLE-PURAP', 'Add Note / Attachment CM Credit Memo Image', 'Authorizes users to add notes or attachments with a type of "Credit Memo Image" on Credit Memo documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE326', 'OLE326', '1', '46', 'OLE-PURAP', 'View Note / Attachment CM Credit Memo Image', 'Authorizes users to view attachments with a type of "Credit Memo Image" on Credit Memo documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE328', 'OLE328', '1', 'OLE41', 'OLE-PURAP', 'Modify Accounting Lines REQS Organization items.sourceAccountingLines', 'Allows users to modify the Source accounting lines on a Requisition Document that is at the Organization Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE329', 'OLE329', '1', '29', 'OLE-FP', 'Use Screen org.kuali.kfs.fp.document.web.struts.CashManagementStatusAction', 'Allows access to the Cash Management Document screen that a user sees when they try to initiate and a document is already open for their campus. The screen provides them a link to the existing document for their campus.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE330', 'OLE330', '1', '29', 'OLE-FP', 'Use Screen org.kuali.kfs.fp.document.web.struts.DepositWizardAction', 'Allows access to the Cash Management Document screen from which a user can create new deposits.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE331', 'OLE331', '1', '29', 'OLE-FP', 'Use Screen org.kuali.kfs.fp.web.struts.CashDrawerCorrectionAction', 'Allows access to the Cash Management Document screen from which a user can correct amounts on a closed cash drawer.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE335', 'OLE335', '1', '26', 'OLE-VND', 'Modify Maintenance Document Field VendorDetail vendorHeader.vendorTaxTypeCode', 'Authorizes users to modify the Tax Type Code on a Vendor Maintenance Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE350', 'OLE350', '1', '10', 'OLE-FP', 'Initiate Document CR', 'Authorizes the initiation of the Cash Receipt Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE351', 'OLE351', '1', '1', 'OLE-COA', 'Serve As Account Supervisor', 'Identifies users that can be Account Supervisors.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE352', 'OLE352', '1', '1', 'OLE-COA', 'Serve As Fiscal Officer', 'Identifies users that can be Account Fiscal Officers.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE353', 'OLE353', '1', '1', 'OLE-COA', 'Serve As Fiscal Officer Delegate', 'Identifies users that can be Account Fiscal Officer Delegates.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE354', 'OLE354', '1', '29', 'OLE-FP', 'Use Screen org.kuali.kfs.fp.document.web.struts.DisbursementVoucherHelpAction', '???', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE355', 'OLE355', '1', 'OLE41', 'OLE-PURAP', 'Modify Accounting Lines POA NewUnorderedItems items.sourceAccountingLines', 'Allows users to modify the Source accounting lines on a Purchase Order Amendment Document that is at the New Unordered Items Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE356', 'OLE356', '1', '10', 'OLE-PURAP', 'Initiate Document CM', 'Authorizes the initiation of the Credit Memo Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE358', 'OLE358', '1', 'OLE41', 'OLE-FP', 'Modify Accounting Lines DV Travel sourceAccountingLines.financialObjectCode', 'Allows users to modify the object code of Source accounting lines on a Disbursement Voucher Document that is at the Travel Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE361', 'OLE361', '1', '10', 'OLE-FP', 'Initiate Document CDS', 'Authorizes the initiation of the Cash Drawer Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE362', 'OLE362', '1', 'OLE50', 'OLE-SYS', 'View Batch File(s) KFS*', 'Authorizes users to view batch files using the Batch File lookup screen.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE363', 'OLE363', '1', '23', 'OLE-PDP', 'Look Up Records KFS-PDP PurchasingPaymentDetail', 'Allow users to access Pre Disbursement Processor lookups.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE364', 'OLE364', '1', '27', 'OLE-VND', 'Full Unmask Field VendorDetail vendorHeader.vendorTaxTypeCode', 'Authorizes users to view the entire Tax Number on the Vendor Maintenance Document and Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE365', 'OLE365', '1', '16', 'OLE-PURAP', 'Edit Document PREQ P', 'Authorizes users who can edit Payment Request Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE366', 'OLE366', '1', '16', 'OLE-PURAP', 'Edit Document PREQ F', 'Authorizes users who can edit Payment Request Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE367', 'OLE367', '1', '16', 'OLE-PURAP', 'Edit Document CM P', 'Authorizes users who can edit Credit Memo Documents that are in PROCESSED status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE368', 'OLE368', '1', '16', 'OLE-PURAP', 'Edit Document CM F', 'Authorizes users who can edit Credit Memo Documents that are in FINAL status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE372', 'OLE372', '1', 'OLE48', 'OLE-FP', 'Edit Bank Code CMD', 'Users who can edit the Bank Code field on the Cash Management Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE373', 'OLE373', '1', 'OLE48', 'OLE-PURAP', 'Edit Bank Code PREQ', 'Users who can edit the Bank Code field on the Payment Request Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE374', 'OLE374', '1', 'OLE48', 'OLE-FP', 'Edit Bank Code DV', 'Users who can edit the Bank Code field on the Disbursement Voucher Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE379', 'OLE379', '1', '10', 'OLE-SYS', 'Initiate Document IdentityManagementGroupDocument', 'Authorizes the initiation of the KIM Group document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE380', 'OLE380', '1', '10', 'OLE-COA', 'Initiate Document GORV', 'Authorizes the initiation of the Organization Reversion Global Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE381', 'OLE381', '1', '27', 'OLE-FP', 'Full Unmask Field DisbursementVoucherWireTransfer disbVchrPayeeAccountNumber', 'Authorizes users to view the entire bank account number on the Wire Transfer tab of the Disbursement Voucher document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE382', 'OLE382', '1', '16', 'OLE-SYS', 'Edit Document FSSM AdHoc R', 'Authorizes users to edit Simple Maintenance document at the AdHoc route node.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE383', 'OLE383', '1', '16', 'OLE-SYS', 'Edit Document KFSM AdHoc R', 'Authorizes users to edit Complex Maintenance document at the AdHoc route node.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE385', 'OLE385', '1', '33', 'OLE-GL', 'Upload Batch Input File(s) collectorFlatFileInputFileType', 'Authorizes user to access the Collector Flat File Upload page.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE386', 'OLE386', '1', '27', 'OLE-PDP', 'Full Unmask Field PaymentGroup achBankRoutingNbr', 'Authorizes users to view the entire bank routing number on the Payment Detail Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE387', 'OLE387', '1', '27', 'OLE-PDP', 'Full Unmask Field AchAccountNumber achBankAccountNbr', 'Authorizes users to view the entire bank account number on the Payment Detail Inquiry.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE390', 'OLE390', '1', '9', 'OLE-SYS', 'Ad Hoc Review Document KFS A', 'Authorizes users to take the Approve action on OLE documents Ad Hoc routed to them.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE391', 'OLE391', '1', '9', 'OLE-SYS', 'Ad Hoc Review Document KFS F', 'Authorizes users to take the FYI action on OLE documents Ad Hoc routed to them.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE392', 'OLE392', '1', '9', 'OLE-SYS', 'Ad Hoc Review Document KFS K', 'Authorizes users to take the Acknowledge action on OLE documents Ad Hoc routed to them.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE1100', 'OLE1100', '1', '3', 'OLE-SYS', 'Administer Routing for Document KFST', 'Allows users to open Financial System Documents via the Super search option in Document Search and take Administrative workflow actions on them (such as approving the document  approving individual requests  or sending the document to a specified route node).', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE1101', 'OLE1101', '1', '2', 'OLE-SYS', 'Copy Document KFS', 'Allows access to the Copy button on OLE Financial System Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE1103', 'OLE1103', '1', '51', 'OLE-SYS', 'Add Message to Route Log KFS', 'Allows user to enter a message on the route log', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE1500', 'OLE1500', '1', '33', 'OLE-PURAP', 'Upload Batch Input File(s) ', 'Authorizes users to access the Marc File Upload page.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE1501', 'OLE1501', '1', '1', 'OLE-VND', 'Inactivate Vendor', 'Authorizes users to inactivate the present vendor.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLESEC6001', 'OLESEC6001', '1', '10', 'OLE-SEC', 'Initiate Document AccessSecuritySimpleMaintenanceDocument', 'Authorizes the initiation of Access Security Simple Maintenance Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLESEC6002', 'OLESEC6002', '1', '23', 'OLE-SEC', 'Look Up Records KFS-SEC', 'Authorizes the lookup of Access Security records.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLESEC6003', 'OLESEC6003', '1', '24', 'OLE-SEC', 'Inquire Into Records KFS-SEC', 'Authorizes inquiry of Access Security records.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLESEC6004', 'OLESEC6004', '1', '16', 'OLE-SEC', 'Edit Document AccessSecuritySimpleMaintenanceDocument PreRoute', 'Authorizes edit of Access Security Simple Maintenance Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEPURAP6940', 'OLEPURAP6940', '1', '27', 'OLE-PURAP', 'Unmask PO # On PO Document', 'Authorizes users to view the entire Purchase Order Number on the PO Document and Inquiry', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLESYS7145a', 'OLESYS7145a', '1', '1', 'OLE-SYS', 'View Accounting Period', 'Users who can view the accounting period field when enabled for year end processing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLESYS7145b', 'OLESYS7145b', '1', '1', 'OLE-SYS', 'Edit Accounting Period', 'Users who can edit the accounting period field when enabled for year end processing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLESYS6007', 'OLESYS6007', '1', '31', 'OLE-FP', 'Allow DV Immediate Disbursement', 'Allow Disbursement Voucher to be marked for immediate disbursement by the Pre-Disbursement Processor.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI6886-PRM1', 'OLEMI6886-PRM1', '1', '42', 'OLE-PDP', 'Maintain ACH Accounts', 'Allows to view and update Payee ACH Accounts of a certain transaction type.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI6886-PRM2', 'OLEMI6886-PRM2', '1', '23', 'OLE-PDP', 'Lookup ACH Accounts', 'Allows the lookup of Payee ACH Account records of certain transaction type(s).', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI6886-PRM3', 'OLEMI6886-PRM3', '1', '10', 'OLE-PDP', 'Initiate ACH Account Document', 'Allows initiation the PDP Payee ACH Account maintenance document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLECNTRB162-PRM', 'OLECNTRB162-PRM', '1', '33', 'OLE-VND', 'Use Vendor Exclude File Upload Screen', 'Authorizes users to access the Vendor Exclude File Upload page.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLECNTRB199-P1', 'OLECNTRB199-P1', '1', '33', 'OLE-SYS', 'Upload semaphoreInputFileTypeError', 'Authorizes users to access the Batch Semaphore File Upload page.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLECNTRB199-P2', 'OLECNTRB199-P2', '1', 'OLE50', 'OLE-SYS', 'Administer Batch File OLE-SYS batchContainer', 'Authorizes users to view the batch files using the Batch File lookup screen', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI8342-PRM', 'OLEMI8342-PRM', '1', '44', 'OLE-COA', 'Modify Maintenance Document Section SubAccount indirectCostRecoveryAccounts', 'Authorizes users to edit the IndirectCostRecoveryAccounts section on the Sub-Account document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI8944-PRM', 'OLEMI8944-PRM', '1', 'OLE41', 'OLE-PURAP', 'Modify Accounting Lines OLE_PUR Tax itemsourceAccountingLines', 'Allows users to modify the Source accounting lines on documents answering to the parent document Purchasing Transactional Document when a document is at the Tax Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI8893-PRM1', 'OLEMI8893-PRM1', '1', '68', 'OLE-FP', 'Recall Document OLEFinancialProcessingTransactionalDocument', 'Enable Recall Document functionality for OLEFinancialProcessingTransactionalDocument documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI8893-PRM2', 'OLEMI8893-PRM2', '1', '68', 'OLE-SYS', 'Recall Document OLE', 'Recall Document functionality for OLE documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI9071-PRM1', 'OLEMI9071-PRM1', '1', '49', 'OLE-SYS', 'Send Complete Request OLE', 'Authorizes users to send Complete ad hoc requests for OLE Documents', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLECNTRB65-PRM1', 'OLECNTRB65-PRM1', '1', '1', 'KR-SYS', 'Access Locked Module', 'Allow access to modules  even when locked.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI9598-PRM1', 'OLEMI9598-PRM1', '1', 'KR1000', 'OLE-SYS', 'Super User Approve Single Action Request OLE', 'Allows users to super user approve single action requests on the super user tab for OLE Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI9598-PRM2', 'OLEMI9598-PRM2', '1', 'KR1001', 'OLE-SYS', 'Super User Approve Document OLE', 'Allows users to super user approve documents on the super user tab for OLE Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEMI9598-PRM3', 'OLEMI9598-PRM3', '1', 'KR1002', 'OLE-SYS', 'Super User Disapprove Document OLE', 'Allows users to super user disapprove documents on the super user tab for OLE Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE393', 'OLE393', '1', '4', 'OLE-PURAP', 'Blanket Approve Document PRQS', 'Allows access to the Blanket Approval button on the Invoice Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE394', 'OLE394', '1', '10', 'OLE-PURAP', 'Initiate Document PRQS', 'Authorizes the initiation of the Invoice Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE395', 'OLE395', '1', '45', 'OLE-PURAP', 'Add Note / Attachment PRQS Invoice Image', 'Users who can add notes and attachments to the Invoice document when it is at the Invoice Attachment route node.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE396', 'OLE396', '1', '46', 'OLE-PURAP', 'View Note / Attachment PRQS Invoice Image', 'Authorizes users to view attachments with a type of "Invoice Image" on Invoice documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE397', 'OLE397', '1', '16', 'OLE-PURAP', 'Edit Document PRQS R', 'Authorizes users who can edit Invoice Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE398', 'OLE398', '1', '31', 'OLE-PURAP', 'Use Transactional Document PRQS requestInvoiceCancel', 'Users authorized to take the Request Cancel action on Invoice Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE399', 'OLE399', '1', '31', 'OLE-PURAP', 'Use Transactional Document PRQS InvoiceHoldCancelRemoval', 'Authorizes users to remove Holds or Cancels on Invoice Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE400', 'OLE400', '1', '31', 'OLE-PURAP', 'Use Transactional Document PRQS requestInvoiceHold', 'Authorizes users to put Invoice Documents on Hold.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE401', 'OLE401', '1', '16', 'OLE-PURAP', 'Edit Document PRQS P', 'Authorizes users who can edit Invoice Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE402', 'OLE402', '1', '16', 'OLE-PURAP', 'Edit Document PRQS F', 'Authorizes users who can edit Invoice Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE403', 'OLE403', '1', 'OLE48', 'OLE-PURAP', 'Edit Bank Code PRQS', 'Users who can edit the Bank Code field on the Invoice Document.', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_PERM_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 8, '7:d30af055d0f097148d8273f8c8c248fe', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_PERM_ATTR_DATA_T::ole
INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE123', 'OLE123', '1', 'OLE100', '8', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE245', 'OLE245', '1', 'OLE100', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE124', 'OLE124', '1', 'OLE101', '8', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE246', 'OLE246', '1', 'OLE101', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE125', 'OLE125', '1', 'OLE102', '8', '13', 'OLE_AP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE247', 'OLE247', '1', 'OLE102', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE126', 'OLE126', '1', 'OLE103', '8', '13', 'OLE_RCV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE248', 'OLE248', '1', 'OLE103', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE132', 'OLE132', '1', 'OLE108', 'OLE51', '13', 'OpenLibraryEnvironmentTransactionalDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10000', 'OLE10000', '1', 'OLE1100', '3', '13', 'OpenLibraryEnvironmentTransactionalDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE141', 'OLE141', '1', 'OLE113', '3', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE142', 'OLE142', '1', 'OLE114', '3', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE143', 'OLE143', '1', 'OLE115', '3', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE144', 'OLE144', '1', 'OLE116', '3', '13', 'OpenLibraryEnvironmentSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE145', 'OLE145', '1', 'OLE117', '3', '13', 'OLE_BANK')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE149', 'OLE149', '1', 'OLE119', '18', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE150', 'OLE150', '1', 'OLE120', '19', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE151', 'OLE151', '1', 'OLE121', '20', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE152', 'OLE152', '1', 'OLE122', '21', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE162', 'OLE162', '1', 'OLE128', '11', '5', 'Bank')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE163', 'OLE163', '1', 'OLE128', '11', '6', 'bankAccountNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE164', 'OLE164', '1', 'OLE129', '10', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE165', 'OLE165', '1', 'OLE130', '10', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE166', 'OLE166', '1', 'OLE131', '16', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE167', 'OLE167', '1', 'OLE132', '15', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE168', 'OLE168', '1', 'OLE133', '3', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE169', 'OLE169', '1', 'OLE134', '11', '5', 'Bank')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE170', 'OLE170', '1', 'OLE134', '11', '6', 'bankAccountNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE171', 'OLE171', '1', 'OLE135', '12', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE172', 'OLE172', '1', 'OLE136', '3', '13', 'OLE_PVEN')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE174', 'OLE174', '1', 'OLE137', '11', '5', 'VendorDetail')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE175', 'OLE175', '1', 'OLE137', '11', '6', 'vendorHeader.vendorTaxNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE176', 'OLE176', '1', 'OLE138', '11', '5', 'VendorDetail')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE177', 'OLE177', '1', 'OLE138', '11', '6', 'vendorHeader.vendorTaxNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE178', 'OLE178', '1', 'OLE139', '57', '5', 'VendorDetail')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE179', 'OLE179', '1', 'OLE139', '57', '44', 'vendorContracts')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE222', 'OLE222', '1', 'OLE169', '8', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE250', 'OLE250', '1', 'OLE184', '57', '5', 'VendorDetail')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE251', 'OLE251', '1', 'OLE184', '57', '44', 'vendorCommodities')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE252', 'OLE252', '1', 'OLE185', '8', '13', 'OLE_CMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE253', 'OLE253', '1', 'OLE185', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE257', 'OLE257', '1', 'OLE187', '11', '5', 'DisbursementPayee')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE258', 'OLE258', '1', 'OLE187', '11', '6', 'taxNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE265', 'OLE265', '1', 'OLE189', '52', '13', 'OLE_SB')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE266', 'OLE266', '1', 'OLE189', '52', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE267', 'OLE267', '1', 'OLE189', '52', '6', 'sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE268', 'OLE268', '1', 'OLE190', '52', '13', 'OLEFinancialProcessingSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE269', 'OLE269', '1', 'OLE190', '52', '16', 'Account')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE270', 'OLE270', '1', 'OLE190', '52', '6', 'sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE271', 'OLE271', '1', 'OLE191', '52', '13', 'OLEFinancialProcessingSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE272', 'OLE272', '1', 'OLE191', '52', '16', 'Account')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE273', 'OLE273', '1', 'OLE191', '52', '6', 'targetAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE274', 'OLE274', '1', 'OLE192', '52', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE275', 'OLE275', '1', 'OLE192', '52', '16', 'Campus')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE276', 'OLE276', '1', 'OLE192', '52', '6', 'sourceAccountingLines.financialObjectCode')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE277', 'OLE277', '1', 'OLE193', '52', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE278', 'OLE278', '1', 'OLE193', '52', '16', 'Tax')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE279', 'OLE279', '1', 'OLE193', '52', '6', 'sourceAccountingLines.amount')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE280', 'OLE280', '1', 'OLE194', '52', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE281', 'OLE281', '1', 'OLE194', '52', '16', 'Travel')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE282', 'OLE282', '1', 'OLE194', '52', '6', 'sourceAccountingLines.amount')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE283', 'OLE283', '1', 'OLE195', '52', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE284', 'OLE284', '1', 'OLE195', '52', '16', 'PaymentMethod')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE285', 'OLE285', '1', 'OLE195', '52', '6', 'sourceAccountingLines.amount')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE292', 'OLE292', '1', 'OLE198', '52', '13', 'OLE_PUR')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE293', 'OLE293', '1', 'OLE198', '52', '16', 'Account')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE294', 'OLE294', '1', 'OLE198', '52', '6', 'items.sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE295', 'OLE295', '1', 'OLE199', '52', '13', 'OLE_AP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE296', 'OLE296', '1', 'OLE199', '52', '16', 'Account')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE297', 'OLE297', '1', 'OLE199', '52', '6', 'items.sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE306', 'OLE306', '1', 'OLE209', '14', '10', 'taxEntry')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE527', 'OLE527', '1', 'OLE209', '14', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE307', 'OLE307', '1', 'OLE210', '14', '10', 'frnEntry')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE528', 'OLE528', '1', 'OLE210', '14', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE308', 'OLE308', '1', 'OLE211', '14', '10', 'wireEntry')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE529', 'OLE529', '1', 'OLE211', '14', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE309', 'OLE309', '1', 'OLE212', '14', '10', 'travelEntry')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE530', 'OLE530', '1', 'OLE212', '14', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE314', 'OLE314', '1', 'OLE217', '14', '10', 'editPreExtract')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE535', 'OLE535', '1', 'OLE217', '14', '13', 'OLE_AP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE317', 'OLE317', '1', 'OLE220', '3', '13', 'OpenLibraryEnvironmentTransactionalDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE327', 'OLE327', '1', 'OLE233', '11', '5', 'PayeeACHAccount')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE328', 'OLE328', '1', 'OLE233', '11', '6', 'bankAccountNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE329', 'OLE329', '1', 'OLE234', '11', '5', 'ACHBank')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE330', 'OLE330', '1', 'OLE234', '11', '6', 'bankRoutingNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE331', 'OLE331', '1', 'OLE235', '11', '5', 'PayeeACHAccount')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE332', 'OLE332', '1', 'OLE235', '11', '6', 'bankAccountNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE333', 'OLE333', '1', 'OLE236', '11', '5', 'PayeeACHAccount')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE334', 'OLE334', '1', 'OLE236', '11', '6', 'bankRoutingNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE337', 'OLE337', '1', 'OLE238', '52', '13', 'OLEFinancialProcessingSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE338', 'OLE338', '1', 'OLE238', '52', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE339', 'OLE339', '1', 'OLE238', '52', '6', 'sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE340', 'OLE340', '1', 'OLE239', '52', '13', 'OLEFinancialProcessingSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE341', 'OLE341', '1', 'OLE239', '52', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE342', 'OLE342', '1', 'OLE239', '52', '6', 'targetAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE346', 'OLE346', '1', 'OLE241', '52', '13', 'OLE_PUR')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE347', 'OLE347', '1', 'OLE241', '52', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE348', 'OLE348', '1', 'OLE241', '52', '6', 'items.sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE349', 'OLE349', '1', 'OLE242', '52', '13', 'OLE_AP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE350', 'OLE350', '1', 'OLE242', '52', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE351', 'OLE351', '1', 'OLE242', '52', '6', 'items.sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE372', 'OLE372', '1', 'OLE256', '10', '5', 'BatchJobStatus')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE373', 'OLE373', '1', 'OLE257', '12', '2', 'org.kuali.ole.module.purap.web.struts.B2BAction')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE374', 'OLE374', '1', 'OLE258', '9', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE375', 'OLE375', '1', 'OLE258', '9', '9', 'Invoice Image')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE377', 'OLE377', '1', 'OLE260', '9', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE378', 'OLE378', '1', 'OLE260', '9', '9', 'Invoice Image')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE382', 'OLE382', '1', 'OLE263', '59', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE383', 'OLE383', '1', 'OLE263', '59', '8', 'FALSE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE391', 'OLE391', '1', 'OLE268', '8', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE392', 'OLE392', '1', 'OLE268', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE393', 'OLE393', '1', 'OLE269', '3', '13', 'OLEPurchasingAccountsPayableTransactionalDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE394', 'OLE394', '1', 'OLE270', '12', '2', 'org.kuali.ole.module.purap.document.web.struts.PrintAction')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE395', 'OLE395', '1', 'OLE271', '12', '2', 'org.kuali.ole.sys.web.struts.ElectronicFundTransferAction')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE396', 'OLE396', '1', 'OLE272', '14', '10', 'printPurchaseOrder')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE538', 'OLE538', '1', 'OLE272', '14', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE397', 'OLE397', '1', 'OLE273', '14', '10', 'previewPrintPurchaseOrder')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE539', 'OLE539', '1', 'OLE273', '14', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE398', 'OLE398', '1', 'OLE274', '14', '10', 'assignSensitiveData')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE540', 'OLE540', '1', 'OLE274', '14', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE399', 'OLE399', '1', 'OLE275', '14', '10', 'resendPurchaseOrder')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE541', 'OLE541', '1', 'OLE275', '14', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE400', 'OLE400', '1', 'OLE276', '14', '10', 'requestPaymentRequestCancel')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE542', 'OLE542', '1', 'OLE276', '14', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE401', 'OLE401', '1', 'OLE277', '14', '10', 'paymentRequestHoldCancelRemoval')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE543', 'OLE543', '1', 'OLE277', '14', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE406', 'OLE406', '1', 'OLE280', '52', '13', 'OLE_PCDO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE407', 'OLE407', '1', 'OLE280', '52', '16', 'AccountFullEdit')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE408', 'OLE408', '1', 'OLE280', '52', '6', 'sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE409', 'OLE409', '1', 'OLE281', '52', '13', 'OLE_PCDO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE410', 'OLE410', '1', 'OLE281', '52', '16', 'AccountFullEdit')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE411', 'OLE411', '1', 'OLE281', '52', '6', 'targetAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE412', 'OLE412', '1', 'OLE282', '14', '10', 'managerCancel')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE545', 'OLE545', '1', 'OLE282', '14', '13', 'OLE_AP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE413', 'OLE413', '1', 'OLE283', '14', '10', 'processorCancel')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE546', 'OLE546', '1', 'OLE283', '14', '13', 'OLE_AP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE414', 'OLE414', '1', 'OLE284', '3', '13', 'OLE_ACM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE415', 'OLE415', '1', 'OLE285', '12', '4', 'OLE-PDP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE416', 'OLE416', '1', 'OLE286', '14', '10', 'requestPaymentRequestHold')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE547', 'OLE547', '1', 'OLE286', '14', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE417', 'OLE417', '1', 'OLE287', '12', '2', 'org.kuali.ole.gl.web.struts.BalanceInquiryAction')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE420', 'OLE420', '1', 'OLE291', '3', '13', 'OLE_EIRT')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE421', 'OLE421', '1', 'OLE292', '15', '1', 'electronicInvoiceInputFileType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE422', 'OLE422', '1', 'OLE293', '14', '10', 'requestVendorCreditMemoHold')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE548', 'OLE548', '1', 'OLE293', '14', '13', 'OLE_CM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE423', 'OLE423', '1', 'OLE294', '14', '10', 'vendorCreditMemoHoldRemoval')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE549', 'OLE549', '1', 'OLE294', '14', '13', 'OLE_CM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE425', 'OLE425', '1', 'OLE301', '12', '2', 'org.kuali.ole.sys.web.struts.KualiBalanceInquiryReportMenuAction')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE436', 'OLE436', '1', 'OLE308', '52', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE437', 'OLE437', '1', 'OLE308', '52', '6', 'items.sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE438', 'OLE438', '1', 'OLE308', '52', '16', 'Initiator')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE447', 'OLE447', '1', 'OLE313', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE448', 'OLE448', '1', 'OLE313', '9', '9', 'Contracts')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE449', 'OLE449', '1', 'OLE314', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE450', 'OLE450', '1', 'OLE314', '9', '9', 'Contracts')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE451', 'OLE451', '1', 'OLE315', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE452', 'OLE452', '1', 'OLE315', '9', '9', 'Contract Ammendments')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE453', 'OLE453', '1', 'OLE316', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE454', 'OLE454', '1', 'OLE316', '9', '9', 'Contract Ammendments')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE455', 'OLE455', '1', 'OLE317', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE456', 'OLE456', '1', 'OLE317', '9', '9', 'Quotes')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE457', 'OLE457', '1', 'OLE318', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE458', 'OLE458', '1', 'OLE318', '9', '9', 'Quotes')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE459', 'OLE459', '1', 'OLE319', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE460', 'OLE460', '1', 'OLE319', '9', '9', 'RFPs')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE461', 'OLE461', '1', 'OLE320', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE462', 'OLE462', '1', 'OLE320', '9', '9', 'RFPs')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE463', 'OLE463', '1', 'OLE321', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE464', 'OLE464', '1', 'OLE321', '9', '9', 'RFP Responses')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE465', 'OLE465', '1', 'OLE322', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE466', 'OLE466', '1', 'OLE322', '9', '9', 'RFP Responses')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE467', 'OLE467', '1', 'OLE323', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE468', 'OLE468', '1', 'OLE323', '9', '9', 'Other - Restricted')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE469', 'OLE469', '1', 'OLE324', '9', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE470', 'OLE470', '1', 'OLE324', '9', '9', 'Other - Restricted')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE471', 'OLE471', '1', 'OLE325', '9', '13', 'OLE_CM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE472', 'OLE472', '1', 'OLE325', '9', '9', 'Credit Memo Image')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE473', 'OLE473', '1', 'OLE326', '9', '13', 'OLE_CM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE474', 'OLE474', '1', 'OLE326', '9', '9', 'Credit Memo Image')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE433', 'OLE433', '1', 'OLE328', '52', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE434', 'OLE434', '1', 'OLE328', '52', '6', 'items.sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE435', 'OLE435', '1', 'OLE328', '52', '16', 'Organization')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE475', 'OLE475', '1', 'OLE329', '12', '2', 'org.kuali.ole.fp.document.web.struts.CashManagementStatusAction')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE476', 'OLE476', '1', 'OLE330', '12', '2', 'org.kuali.ole.fp.document.web.struts.DepositWizardAction')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE477', 'OLE477', '1', 'OLE331', '12', '2', 'org.kuali.ole.fp.web.struts.CashDrawerCorrectionAction')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE484', 'OLE484', '1', 'OLE335', '11', '5', 'VendorDetail')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE485', 'OLE485', '1', 'OLE335', '11', '6', 'vendorHeader.vendorTaxTypeCode')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE510', 'OLE510', '1', 'OLE350', '3', '13', 'OLE_CR')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE511', 'OLE511', '1', 'OLE354', '12', '2', 'org.kuali.ole.fp.document.web.struts.DisbursementVoucherHelpAction')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE512', 'OLE512', '1', 'OLE355', '52', '13', 'OLE_POA')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE513', 'OLE513', '1', 'OLE355', '52', '16', 'NewUnorderedItems')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE514', 'OLE514', '1', 'OLE355', '52', '6', 'items.sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE515', 'OLE515', '1', 'OLE356', '3', '13', 'OLE_CM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE517', 'OLE517', '1', 'OLE358', '52', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE518', 'OLE518', '1', 'OLE358', '52', '16', 'Travel')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE519', 'OLE519', '1', 'OLE358', '52', '6', 'sourceAccountingLines.financialObjectCode')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE522', 'OLE522', '1', 'OLE361', '3', '13', 'OLE_CDS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE523', 'OLE523', '1', 'OLE362', 'OLECNTRB199-TYP1', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE550', 'OLE550', '1', 'OLE363', '10', '4', 'OLE-PDP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE551', 'OLE551', '1', 'OLE363', '10', '5', 'PurchasingPaymentDetail')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE552', 'OLE552', '1', 'OLE364', '11', '5', 'VendorDetail')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE553', 'OLE553', '1', 'OLE364', '11', '6', 'vendorHeader.vendorTaxTypeCode')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE554', 'OLE554', '1', 'OLE365', '8', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE555', 'OLE555', '1', 'OLE365', '8', '15', 'P')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE556', 'OLE556', '1', 'OLE366', '8', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE557', 'OLE557', '1', 'OLE366', '8', '15', 'F')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE558', 'OLE558', '1', 'OLE367', '8', '13', 'OLE_CM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE559', 'OLE559', '1', 'OLE367', '8', '15', 'P')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE560', 'OLE560', '1', 'OLE368', '8', '13', 'OLE_CM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE561', 'OLE561', '1', 'OLE368', '8', '15', 'F')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE569', 'OLE569', '1', 'OLE372', '3', '13', 'OLE_CMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE570', 'OLE570', '1', 'OLE373', '3', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE571', 'OLE571', '1', 'OLE374', '3', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE574', 'OLE574', '1', 'OLE379', '3', '13', 'IdentityManagementGroupDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE575', 'OLE575', '1', 'OLE380', '3', '13', 'OLE_GORV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE576', 'OLE576', '1', 'OLE381', '11', '5', 'DisbursementVoucherWireTransfer')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE577', 'OLE577', '1', 'OLE381', '11', '6', 'disbVchrPayeeAccountNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE578', 'OLE578', '1', 'OLE382', '8', '13', 'OpenLibraryEnvironmentSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE580', 'OLE580', '1', 'OLE382', '8', '16', 'AdHoc')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE582', 'OLE582', '1', 'OLE382', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE579', 'OLE579', '1', 'OLE383', '8', '13', 'OpenLibraryEnvironmentComplexMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE581', 'OLE581', '1', 'OLE383', '8', '16', 'AdHoc')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE583', 'OLE583', '1', 'OLE383', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE586', 'OLE586', '1', 'OLE385', '15', '1', 'collectorFlatFileInputFileType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE587', 'OLE587', '1', 'OLE386', '11', '5', 'PaymentGroup')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE588', 'OLE588', '1', 'OLE386', '11', '6', 'achBankRoutingNbr')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE589', 'OLE589', '1', 'OLE387', '11', '5', 'AchAccountNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE590', 'OLE590', '1', 'OLE387', '11', '6', 'achBankAccountNbr')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE591', 'OLE591', '1', 'OLE390', '5', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE592', 'OLE592', '1', 'OLE390', '5', '14', 'A')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE593', 'OLE593', '1', 'OLE391', '5', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE594', 'OLE594', '1', 'OLE391', '5', '14', 'F')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE595', 'OLE595', '1', 'OLE392', '5', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE596', 'OLE596', '1', 'OLE392', '5', '14', 'K')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE74', 'OLE74', '1', 'OLE55', '3', '13', 'OLE_GOBJ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE75', 'OLE75', '1', 'OLE56', '3', '13', 'OLE_ORGR')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE79', 'OLE79', '1', 'OLE60', '11', '5', 'SubAccount')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80', 'OLE80', '1', 'OLE60', '11', '6', 'a21SubAccount')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE81', 'OLE81', '1', 'OLE61', '11', '5', 'Organization')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE82', 'OLE82', '1', 'OLE61', '11', '6', 'organizationPlantChartCode')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE83', 'OLE83', '1', 'OLE62', '11', '5', 'Organization')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE84', 'OLE84', '1', 'OLE62', '11', '6', 'organizationPlantAccountNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE85', 'OLE85', '1', 'OLE63', '11', '5', 'Organization')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE86', 'OLE86', '1', 'OLE63', '11', '6', 'campusPlantChartCode')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE87', 'OLE87', '1', 'OLE64', '11', '5', 'Organization')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE88', 'OLE88', '1', 'OLE64', '11', '6', 'campusPlantAccountNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE90', 'OLE90', '1', 'OLE66', 'OLE51', '13', 'OLE_DI')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE91', 'OLE91', '1', 'OLE67', 'OLE51', '13', 'OLE_YEDI')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE93', 'OLE93', '1', 'OLE69', '3', '13', 'OLE_CMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE94', 'OLE94', '1', 'OLE70', '3', '13', 'OLE_SB')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE95', 'OLE95', '1', 'OLE71', '3', '13', 'OLE_JV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE96', 'OLE96', '1', 'OLE72', '3', '13', 'OLE_PCDO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE97', 'OLE97', '1', 'OLE73', '11', '5', 'ProcurementCardHolder')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE98', 'OLE98', '1', 'OLE73', '11', '6', 'transactionCreditCardNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE100', 'OLE100', '1', 'OLE74', '15', '1', 'procurementCardInputFileType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE101', 'OLE101', '1', 'OLE75', '3', '13', 'OLE_GLCP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE103', 'OLE103', '1', 'OLE76', '15', '1', 'collectorXmlInputFileType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE105', 'OLE105', '1', 'OLE77', '15', '1', 'enterpriseFeederFileSetType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE108', 'OLE108', '1', 'OLE81', '3', '13', 'OLEPreDisbursementProcessorSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE110', 'OLE110', '1', 'OLE88', '10', '4', 'OLE-PDP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE111', 'OLE111', '1', 'OLE89', '10', '4', 'OLE-PDP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE113', 'OLE113', '1', 'OLE90', '15', '1', 'paymentInputFileType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE114', 'OLE114', '1', 'OLE91', '3', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE115', 'OLE115', '1', 'OLE92', '3', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE116', 'OLE116', '1', 'OLE93', '3', '13', 'OLE_PUR')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE117', 'OLE117', '1', 'OLE94', '3', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE118', 'OLE118', '1', 'OLE95', '3', '13', 'OLE_POC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE119', 'OLE119', '1', 'OLE96', '3', '13', 'OLE_PORT')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE120', 'OLE120', '1', 'OLE97', '3', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE121', 'OLE121', '1', 'OLE98', '3', '13', 'OLE_RCV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE122', 'OLE122', '1', 'OLE99', '3', '13', 'OLE_EIRT')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10001', 'OLE10001', '1', 'OLE1101', '3', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10003', 'OLE10003', '1', 'OLE1103', '3', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE1500', 'OLE1500', '1', 'OLE1500', '15', '1', 'marcInputFileType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLESEC6001-1', 'OLESEC6001-1', '1', 'OLESEC6001', '3', '13', 'OLEAccessSecuritySimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLESEC6002-1', 'OLESEC6002-1', '1', 'OLESEC6002', '10', '4', 'OLE-SEC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLESEC6003-1', 'OLESEC6003-1', '1', 'OLESEC6003', '10', '4', 'OLE-SEC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLESEC6004-1', 'OLESEC6004-1', '1', 'OLESEC6004', '8', '13', 'OLEAccessSecuritySimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLESEC6004-2', 'OLESEC6004-2', '1', 'OLESEC6004', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEPURAP6940-01', 'OLEPURAP6940-01', '1', 'OLEPURAP6940', '11', '5', 'PurchaseOrderDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEPURAP6940-02', 'OLEPURAP6940-02', '1', 'OLEPURAP6940', '11', '6', 'purapDocumentIdentifier')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLESYS6007-1', 'OLESYS6007-1', '1', 'OLESYS6007', '14', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLESYS6007-2', 'OLESYS6007-2', '1', 'OLESYS6007', '14', '10', 'immediateDisbursementEntryMode')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI6886-AT1', 'OLEMI6886-AT1', '1', 'OLEMI6886-PRM1', '56', '13', 'OLE_PAAT')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI6886-AT2', 'OLEMI6886-AT2', '1', 'OLEMI6886-PRM1', '56', '7', 'false')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI6886-AT3', 'OLEMI6886-AT3', '1', 'OLEMI6886-PRM2', '10', '5', 'PayeeACHAccount')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI6886-AT4', 'OLEMI6886-AT4', '1', 'OLEMI6886-PRM2', '10', '4', 'OLE-PDP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI6886-AT5', 'OLEMI6886-AT5', '1', 'OLEMI6886-PRM3', '3', '13', 'OLE_PAAT')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLECNTRB162-AT1', 'OLECNTRB162-AT1', '1', 'OLECNTRB162-PRM', '15', '1', 'vendorExcludeInputFileType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLECNTRB199-PATD', 'OLECNTRB199-PATD', '1', 'OLECNTRB199-P1', '15', '1', 'semaphoreInputFileTypeError')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLECNTRB199-PAD2', 'OLECNTRB199-PAD2', '1', 'OLECNTRB199-P2', 'OLECNTRB199-TYP1', '4', 'OLE-SYS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLECNTRB199-PAD3', 'OLECNTRB199-PAD3', '1', 'OLECNTRB199-P2', 'OLECNTRB199-TYP1', 'OLECNTRB199-ATTRDEF1', 'staging/sys/batchContainer/*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI8342-PRMATT1', 'OLEMI8342-PRMATT1', '1', 'OLEMI8342-PRM', '57', '44', 'indirectCostRecoveryAccounts')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI8342-PRMATT2', 'OLEMI8342-PRMATT2', '1', 'OLEMI8342-PRM', '57', '5', 'SubAccount')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI8944-PRMATT1', 'OLEMI8944-PRMATT1', '1', 'OLEMI8944-PRM', '52', '6', 'items.sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI8944-PRMATT2', 'OLEMI8944-PRMATT2', '1', 'OLEMI8944-PRM', '52', '16', 'Tax')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI8944-PRMATT3', 'OLEMI8944-PRMATT3', '1', 'OLEMI8944-PRM', '52', '13', 'OLE_PUR')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI8893-PRM1ATT1', 'OLEMI8893-PRM1ATT1', '1', 'OLEMI8893-PRM1', '8', '13', 'OLEFinancialProcessingTransactionalDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI8893-PRM2ATT1', 'OLEMI8893-PRM2ATT1', '1', 'OLEMI8893-PRM2', '8', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI9598-PRM1ATT', 'OLEMI9598-PRM1ATT', '1', 'OLEMI9598-PRM1', 'KR1000', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI9598-PRM2ATT', 'OLEMI9598-PRM2ATT', '1', 'OLEMI9598-PRM2', 'KR1000', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEMI9598-PRM3ATT', 'OLEMI9598-PRM3ATT', '1', 'OLEMI9598-PRM3', 'KR1000', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE601', 'OLE601', '1', 'OLE395', '9', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE602', 'OLE602', '1', 'OLE395', '9', '9', 'Invoice Image')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE603', 'OLE603', '1', 'OLE396', '9', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE604', 'OLE604', '1', 'OLE396', '9', '9', 'Invoice Image')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE605', 'OLE605', '1', 'OLE397', '8', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE606', 'OLE606', '1', 'OLE397', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE607', 'OLE607', '1', 'OLE398', '14', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE608', 'OLE608', '1', 'OLE398', '14', '10', 'requestInvoiceCancel')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE609', 'OLE609', '1', 'OLE399', '14', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE610', 'OLE610', '1', 'OLE399', '14', '10', 'InvoiceHoldCancelRemoval')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE611', 'OLE611', '1', 'OLE400', '14', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE612', 'OLE612', '1', 'OLE400', '14', '10', 'requestInvoiceHold')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE613', 'OLE613', '1', 'OLE401', '8', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE614', 'OLE614', '1', 'OLE401', '8', '15', 'P')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE615', 'OLE615', '1', 'OLE402', '8', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE616', 'OLE616', '1', 'OLE402', '8', '15', 'F')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE617', 'OLE617', '1', 'OLE403', '3', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE618', 'OLE618', '1', 'OLE393', '3', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE619', 'OLE619', '1', 'OLE394', '3', '13', 'OLE_PRQS')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_PERM_ATTR_DATA_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 9, '7:28fed94200b0d2f55f35ded3df446031', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_RSP_T::ole
INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE1', 'OLE1', '1', '1', 'OLE-SYS', 'Review OLE OrganizationHierarchy', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE107', 'OLE107', '1', '1', 'OLE-PURAP', 'Review POA Account', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE108', 'OLE108', '1', '1', 'OLE-PURAP', 'Review CM Account', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE109', 'OLE109', '1', '1', 'OLE-PURAP', 'Review EIRT Management', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE113', 'OLE113', '1', '1', 'OLE-PURAP', 'Review REQS Initiator', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE117', 'OLE117', '1', '1', 'OLE-SYS', 'Review IdentityManagementDocument GroupType', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE12', 'OLE12', '1', '1', 'OLE-SYS', 'Review OLE Account', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE120', 'OLE120', '1', '1', 'OLE-FP', 'Review PCDO Account', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE123', 'OLE123', '1', '1', 'OLE-SYS', 'Review IdentityManagementDocument RoleType', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE124', 'OLE124', '1', '1', 'OLE-COA', 'Review GDLG Account', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE26', 'OLE26', '1', '1', 'OLE-SYS', 'Review OLE Chart', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE29', 'OLE29', '1', '1', 'OLE-SYS', 'Review ACCT Account', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE3', 'OLE3', '1', '1', 'OLE-PURAP', 'Review REQS Organization', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE31', 'OLE31', '1', '1', 'OLE-SYS', 'Review OLE Award', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE4', 'OLE4', '1', '1', 'OLE-SYS', 'Review OLET AccountingOrganizationHierarchy', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE44', 'OLE44', '1', '1', 'OLE-FP', 'Review CR CashManagement', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE46', 'OLE46', '1', '1', 'OLE-FP', 'Review DV Campus', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE48', 'OLE48', '1', '1', 'OLE-FP', 'Review DV Purchasing', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE49', 'OLE49', '1', '1', 'OLE-FP', 'Review DV Tax', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE50', 'OLE50', '1', '1', 'OLE-FP', 'Review DV Travel', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE51', 'OLE51', '1', '1', 'OLE-FP', 'Review DV PaymentMethod', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE6', 'OLE6', '1', '1', 'OLE-PURAP', 'Review PUR Commodity', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE7', 'OLE7', '1', '1', 'OLE-SYS', 'Review OLE SubFund', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE71', 'OLE71', '1', '1', 'OLE-PURAP', 'Review AP ImageAttachment', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE73', 'OLE73', '1', '1', 'OLE-PURAP', 'Review PREQ Receiving', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE74', 'OLE74', '1', '1', 'OLE-PURAP', 'Review PREQ Tax', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE75', 'OLE75', '1', '1', 'OLE-PURAP', 'Review POA NewUnorderedItems', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE76', 'OLE76', '1', '1', 'OLE-PURAP', 'Review PO Budget', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE78', 'OLE78', '1', '1', 'OLE-PURAP', 'Review PO ContractManagement', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80', 'OLE80', '1', '1', 'OLE-PURAP', 'Review PO Tax', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE81', 'OLE81', '1', '1', 'OLE-PURAP', 'Review PORH AccountsPayable', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE83', 'OLE83', '1', '1', 'OLE-PURAP', 'Review OLET SubAccount', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE85', 'OLE85', '1', '1', 'OLE-PURAP', 'Review REQS SeparationOfDuties', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE87', 'OLE87', '1', '2', 'OLE-SYS', 'Resolve Exception', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE91', 'OLE91', '1', '1', 'OLE-VND', 'Review PVEN Initiator', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE92', 'OLE92', '1', '1', 'OLE-VND', 'Review PVEN Management', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE94', 'OLE94', '1', '1', 'OLE-PURAP', 'Review RCVL OutstandingTransactions', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE99', 'OLE99', '1', '1', 'OLE-FP', 'Review PCDO AccountFullEdit', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE126', 'OLEFP7037', '1', '1', 'OLE-FP', 'Review DV SeparationOfDuties', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE101', 'OLE101', '1', '1', 'OLE-PURAP', 'Review PRQS Receiving', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE102', 'OLE102', '1', '1', 'OLE-PURAP', 'Review PRQS Tax', '', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_RSP_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 10, '7:8a926429808c0f245cf9c600902cbc4b', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_RSP_ATTR_DATA_T::ole
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE313', 'OLE313', '1', 'OLE1', '7', '16', 'OrganizationHierarchy')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE314', 'OLE314', '1', 'OLE1', '7', '13', 'OLE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE315', 'OLE315', '1', 'OLE1', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE316', 'OLE316', '1', 'OLE1', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE392', 'OLE392', '1', 'OLE107', '7', '16', 'Account')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE393', 'OLE393', '1', 'OLE107', '7', '13', 'OLE_POA')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE394', 'OLE394', '1', 'OLE107', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE395', 'OLE395', '1', 'OLE107', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE396', 'OLE396', '1', 'OLE108', '7', '16', 'Account')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE397', 'OLE397', '1', 'OLE108', '7', '13', 'OLE_CM')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE398', 'OLE398', '1', 'OLE108', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE399', 'OLE399', '1', 'OLE108', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE400', 'OLE400', '1', 'OLE109', '7', '16', 'Management')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE401', 'OLE401', '1', 'OLE109', '7', '13', 'OLE_EIRT')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE402', 'OLE402', '1', 'OLE109', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE403', 'OLE403', '1', 'OLE109', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE416', 'OLE416', '1', 'OLE113', '7', '13', 'OLE_REQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE417', 'OLE417', '1', 'OLE113', '7', '16', 'Initiator')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE418', 'OLE418', '1', 'OLE113', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE419', 'OLE419', '1', 'OLE113', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE432', 'OLE432', '1', 'OLE117', '7', '13', 'IdentityManagementDocument')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE433', 'OLE433', '1', 'OLE117', '7', '16', 'GroupType')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE434', 'OLE434', '1', 'OLE117', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE435', 'OLE435', '1', 'OLE117', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE436', 'OLE436', '1', 'OLE117', '7', '46', 'OLE68')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10', 'OLE10', '1', 'OLE12', '7', '13', 'OLE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE11', 'OLE11', '1', 'OLE12', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE12', 'OLE12', '1', 'OLE12', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE9', 'OLE9', '1', 'OLE12', '7', '16', 'Account')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE445', 'OLE445', '1', 'OLE120', '7', '13', 'OLE_PCDO')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE446', 'OLE446', '1', 'OLE120', '7', '16', 'Account')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE447', 'OLE447', '1', 'OLE120', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE448', 'OLE448', '1', 'OLE120', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE449', 'OLE449', '1', 'OLE123', '7', '13', 'IdentityManagementDocument')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE450', 'OLE450', '1', 'OLE123', '7', '16', 'RoleType')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE451', 'OLE451', '1', 'OLE123', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE452', 'OLE452', '1', 'OLE123', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE453', 'OLE453', '1', 'OLE123', '7', '46', '29')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE462', 'OLE462', '1', 'OLE124', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE463', 'OLE463', '1', 'OLE124', '7', '16', 'Account')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE464', 'OLE464', '1', 'OLE124', '7', '13', 'OLE_GDLG')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE465', 'OLE465', '1', 'OLE124', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE65', 'OLE65', '1', 'OLE26', '7', '16', 'Chart')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE66', 'OLE66', '1', 'OLE26', '7', '13', 'OLE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE67', 'OLE67', '1', 'OLE26', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE68', 'OLE68', '1', 'OLE26', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE77', 'OLE77', '1', 'OLE29', '7', '16', 'Account')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE78', 'OLE78', '1', 'OLE29', '7', '13', 'OLE_ACCT')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE79', 'OLE79', '1', 'OLE29', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80', 'OLE80', '1', 'OLE29', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE305', 'OLE305', '1', 'OLE3', '7', '16', 'Organization')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE306', 'OLE306', '1', 'OLE3', '7', '13', 'OLE_REQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE307', 'OLE307', '1', 'OLE3', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE308', 'OLE308', '1', 'OLE3', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE85', 'OLE85', '1', 'OLE31', '7', '16', 'Award')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE86', 'OLE86', '1', 'OLE31', '7', '13', 'OLE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE87', 'OLE87', '1', 'OLE31', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE88', 'OLE88', '1', 'OLE31', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE321', 'OLE321', '1', 'OLE4', '7', '16', 'AccountingOrganizationHierarchy')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE322', 'OLE322', '1', 'OLE4', '7', '13', 'OpenLibraryEnvironmentTransactionalDocument')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE323', 'OLE323', '1', 'OLE4', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE324', 'OLE324', '1', 'OLE4', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE137', 'OLE137', '1', 'OLE44', '7', '16', 'CashManagement')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE138', 'OLE138', '1', 'OLE44', '7', '13', 'OLE_CR')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE139', 'OLE139', '1', 'OLE44', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE140', 'OLE140', '1', 'OLE44', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE145', 'OLE145', '1', 'OLE46', '7', '16', 'Campus')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE146', 'OLE146', '1', 'OLE46', '7', '13', 'OLE_DV')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE147', 'OLE147', '1', 'OLE46', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE148', 'OLE148', '1', 'OLE46', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE153', 'OLE153', '1', 'OLE48', '7', '16', 'Purchasing')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE154', 'OLE154', '1', 'OLE48', '7', '13', 'OLE_DV')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE155', 'OLE155', '1', 'OLE48', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE156', 'OLE156', '1', 'OLE48', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE157', 'OLE157', '1', 'OLE49', '7', '16', 'Tax')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE158', 'OLE158', '1', 'OLE49', '7', '13', 'OLE_DV')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE159', 'OLE159', '1', 'OLE49', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE160', 'OLE160', '1', 'OLE49', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE161', 'OLE161', '1', 'OLE50', '7', '16', 'Travel')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE162', 'OLE162', '1', 'OLE50', '7', '13', 'OLE_DV')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE163', 'OLE163', '1', 'OLE50', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE164', 'OLE164', '1', 'OLE50', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE165', 'OLE165', '1', 'OLE51', '7', '16', 'PaymentMethod')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE166', 'OLE166', '1', 'OLE51', '7', '13', 'OLE_DV')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE167', 'OLE167', '1', 'OLE51', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE168', 'OLE168', '1', 'OLE51', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE289', 'OLE289', '1', 'OLE6', '7', '16', 'Commodity')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE290', 'OLE290', '1', 'OLE6', '7', '13', 'OLE_PUR')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE291', 'OLE291', '1', 'OLE6', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE292', 'OLE292', '1', 'OLE6', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE317', 'OLE317', '1', 'OLE7', '7', '16', 'SubFund')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE318', 'OLE318', '1', 'OLE7', '7', '13', 'OLE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE319', 'OLE319', '1', 'OLE7', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE320', 'OLE320', '1', 'OLE7', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE245', 'OLE245', '1', 'OLE71', '7', '16', 'ImageAttachment')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE246', 'OLE246', '1', 'OLE71', '7', '13', 'OLE_AP')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE247', 'OLE247', '1', 'OLE71', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE248', 'OLE248', '1', 'OLE71', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE253', 'OLE253', '1', 'OLE73', '7', '16', 'Receiving')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE254', 'OLE254', '1', 'OLE73', '7', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE255', 'OLE255', '1', 'OLE73', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE256', 'OLE256', '1', 'OLE73', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE257', 'OLE257', '1', 'OLE74', '7', '16', 'Tax')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE258', 'OLE258', '1', 'OLE74', '7', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE259', 'OLE259', '1', 'OLE74', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE260', 'OLE260', '1', 'OLE74', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE261', 'OLE261', '1', 'OLE75', '7', '16', 'NewUnorderedItems')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE262', 'OLE262', '1', 'OLE75', '7', '13', 'OLE_POA')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE263', 'OLE263', '1', 'OLE75', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE264', 'OLE264', '1', 'OLE75', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE265', 'OLE265', '1', 'OLE76', '7', '16', 'Budget')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE266', 'OLE266', '1', 'OLE76', '7', '13', 'OLE_PO')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE267', 'OLE267', '1', 'OLE76', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE268', 'OLE268', '1', 'OLE76', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE273', 'OLE273', '1', 'OLE78', '7', '16', 'ContractManagement')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE274', 'OLE274', '1', 'OLE78', '7', '13', 'OLE_PO')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE275', 'OLE275', '1', 'OLE78', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE276', 'OLE276', '1', 'OLE78', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE281', 'OLE281', '1', 'OLE80', '7', '16', 'Tax')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE282', 'OLE282', '1', 'OLE80', '7', '13', 'OLE_PO')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE283', 'OLE283', '1', 'OLE80', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE284', 'OLE284', '1', 'OLE80', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE285', 'OLE285', '1', 'OLE81', '7', '16', 'AccountsPayable')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE286', 'OLE286', '1', 'OLE81', '7', '13', 'OLE_PORH')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE287', 'OLE287', '1', 'OLE81', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE288', 'OLE288', '1', 'OLE81', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE293', 'OLE293', '1', 'OLE83', '7', '16', 'SubAccount')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE294', 'OLE294', '1', 'OLE83', '7', '13', 'OpenLibraryEnvironmentTransactionalDocument')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE295', 'OLE295', '1', 'OLE83', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE296', 'OLE296', '1', 'OLE83', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE301', 'OLE301', '1', 'OLE85', '7', '16', 'SeparationOfDuties')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE302', 'OLE302', '1', 'OLE85', '7', '13', 'OLE_REQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE303', 'OLE303', '1', 'OLE85', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE304', 'OLE304', '1', 'OLE85', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE310', 'OLE310', '1', 'OLE87', '54', '13', 'OLE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE325', 'OLE325', '1', 'OLE91', '7', '16', 'Initiator')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE326', 'OLE326', '1', 'OLE91', '7', '13', 'OLE_PVEN')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE327', 'OLE327', '1', 'OLE91', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE328', 'OLE328', '1', 'OLE91', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE329', 'OLE329', '1', 'OLE92', '7', '16', 'Management')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE330', 'OLE330', '1', 'OLE92', '7', '13', 'OLE_PVEN')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE331', 'OLE331', '1', 'OLE92', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE332', 'OLE332', '1', 'OLE92', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE337', 'OLE337', '1', 'OLE94', '7', '13', 'OLE_RCVL')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE338', 'OLE338', '1', 'OLE94', '7', '16', 'OutstandingTransactions')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE339', 'OLE339', '1', 'OLE94', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE340', 'OLE340', '1', 'OLE94', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE360', 'OLE360', '1', 'OLE99', '7', '13', 'OLE_PCDO')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE361', 'OLE361', '1', 'OLE99', '7', '16', 'AccountFullEdit')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE362', 'OLE362', '1', 'OLE99', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE363', 'OLE363', '1', 'OLE99', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEFP7037A', 'OLEFP7037A', '1', 'OLE126', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEFP7037B', 'OLEFP7037B', '1', 'OLE126', '7', '16', 'SeparationOfDuties')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEFP7037C', 'OLEFP7037C', '1', 'OLE126', '7', '13', 'OLE_DV')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEFP7037D', 'OLEFP7037D', '1', 'OLE126', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE341', 'OLE341', '1', 'OLE101', '7', '16', 'Receiving')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE342', 'OLE342', '1', 'OLE101', '7', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE343', 'OLE343', '1', 'OLE101', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE344', 'OLE344', '1', 'OLE101', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE345', 'OLE345', '1', 'OLE102', '7', '16', 'Tax')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE346', 'OLE346', '1', 'OLE102', '7', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE347', 'OLE347', '1', 'OLE102', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE348', 'OLE348', '1', 'OLE102', '7', '40', 'FALSE')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_RSP_ATTR_DATA_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 11, '7:cfca77e72bd1d6486c31f45820be96f8', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_ROLE_PERM_T::ole
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE100', 'OLE100', 'OLE45', 'OLE77', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE1004', 'OLE1004', 'OLE49', '306', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE103', 'OLE103', 'OLE20', 'OLE80', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE104', 'OLE104', 'OLE45', 'OLE81', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE105', 'OLE105', 'OLE19', 'OLE82', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE106', 'OLE106', 'OLE20', 'OLE83', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE107', 'OLE107', 'OLE19', 'OLE84', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE109', 'OLE109', 'OLE19', 'OLE86', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE110', 'OLE110', 'OLE50', 'OLE87', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE1106', 'OLE1106', 'OLE62', 'OLE1100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE1107', 'OLE1107', 'OLE55', 'OLE1100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE1108', 'OLE1108', '95', 'OLE1101', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE1111', 'OLE1111', '61', 'OLE1103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE113', 'OLE113', 'OLE19', 'OLE89', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE114', 'OLE114', 'OLE18', 'OLE89', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE115', 'OLE115', 'OLE18', 'OLE90', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE116', 'OLE116', 'OLE44', 'OLE91', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE117', 'OLE117', 'OLE62', 'OLE91', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE118', 'OLE118', 'OLE32', 'OLE92', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE119', 'OLE119', 'OLE54', 'OLE92', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE121', 'OLE121', 'OLE26', 'OLE93', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE123', 'OLE123', 'OLE26', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE124', 'OLE124', 'OLE62', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE125', 'OLE125', 'OLE32', 'OLE95', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE126', 'OLE126', 'OLE54', 'OLE95', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE127', 'OLE127', 'OLE32', 'OLE96', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE128', 'OLE128', 'OLE54', 'OLE96', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE129', 'OLE129', 'OLE22', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE130', 'OLE130', 'OLE32', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE131', 'OLE131', 'OLE54', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE132', 'OLE132', 'OLE62', 'OLE99', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE133', 'OLE133', 'OLE32', 'OLE100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE134', 'OLE134', 'OLE54', 'OLE100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE136', 'OLE136', 'OLE26', 'OLE101', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE137', 'OLE137', 'OLE22', 'OLE102', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE138', 'OLE138', 'OLE32', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE139', 'OLE139', 'OLE54', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE143', 'OLE143', 'OLE16', 'OLE107', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE144', 'OLE144', '95', 'OLE108', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE151', 'OLE151', 'OLE55', 'OLE113', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE152', 'OLE152', 'OLE44', 'OLE114', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE153', 'OLE153', 'OLE54', 'OLE115', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE154', 'OLE154', 'OLE44', 'OLE116', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE155', 'OLE155', 'OLE45', 'OLE117', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE157', 'OLE157', 'OLE44', 'OLE119', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE158', 'OLE158', 'OLE44', 'OLE120', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE159', 'OLE159', 'OLE44', 'OLE121', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE160', 'OLE160', 'OLE54', 'OLE122', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE167', 'OLE167', 'OLE45', 'OLE128', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE168', 'OLE168', 'OLE32', 'OLE129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE169', 'OLE169', 'OLE54', 'OLE129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE170', 'OLE170', 'OLE32', 'OLE130', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE171', 'OLE171', 'OLE54', 'OLE130', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE172', 'OLE172', 'OLE44', 'OLE131', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE173', 'OLE173', 'OLE45', 'OLE132', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE174', 'OLE174', 'OLE54', 'OLE133', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE175', 'OLE175', '61', 'OLE133', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE176', 'OLE176', 'OLE44', 'OLE134', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE177', 'OLE177', 'OLE32', 'OLE136', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE178', 'OLE178', 'OLE54', 'OLE136', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE179', 'OLE179', 'OLE49', 'OLE137', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE180', 'OLE180', 'OLE49', 'OLE138', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE182', 'OLE182', 'OLE26', 'OLE139', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE192', 'OLE192', 'OLE55', '148', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE209', 'OLE209', 'OLE44', '163', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE214', 'OLE214', '66', 'OLE169', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE215', 'OLE215', 'OLE62', 'OLE169', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE230', 'OLE230', 'OLE44', '183', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE232', 'OLE232', 'OLE26', 'OLE184', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE233', 'OLE233', 'OLE11', 'OLE185', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE235', 'OLE235', 'OLE49', 'OLE187', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE236', 'OLE236', 'OLE19', 'OLE87', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE239', 'OLE239', 'OLE13', 'OLE189', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE240', 'OLE240', 'OLE41', 'OLE190', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE241', 'OLE241', 'OLE41', 'OLE191', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE242', 'OLE242', '66', 'OLE192', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE243', 'OLE243', '66', 'OLE193', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE244', 'OLE244', '66', 'OLE194', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE245', 'OLE245', '66', 'OLE195', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE248', 'OLE248', 'OLE41', 'OLE198', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE249', 'OLE249', 'OLE41', 'OLE199', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE270', 'OLE270', 'OLE50', 'OLE209', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE271', 'OLE271', 'OLE70', 'OLE210', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE272', 'OLE272', 'OLE70', 'OLE211', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE273', 'OLE273', 'OLE15', 'OLE212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE280', 'OLE280', 'OLE22', 'OLE217', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE284', 'OLE284', 'OLE16', 'OLE220', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE299', 'OLE299', 'OLE44', 'OLE233', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE300', 'OLE300', 'OLE19', 'OLE234', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE301', 'OLE301', 'OLE45', 'OLE235', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE302', 'OLE302', 'OLE19', 'OLE236', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE305', 'OLE305', '66', 'OLE239', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE307', 'OLE307', '66', 'OLE241', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE308', 'OLE308', '66', 'OLE242', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE311', 'OLE311', '66', 'OLE238', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE316', 'OLE316', 'OLE45', 'OLE73', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE321', 'OLE321', 'OLE41', 'OLE212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE508', 'OLE508', 'OLE82', 'OLE256', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE509', 'OLE509', 'OLE54', 'OLE257', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE510', 'OLE510', 'OLE32', 'OLE257', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE511', 'OLE511', 'OLE22', 'OLE258', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE513', 'OLE513', 'OLE22', 'OLE260', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE514', 'OLE514', 'OLE26', 'OLE260', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE518', 'OLE518', 'OLE44', 'OLE263', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE524', 'OLE524', 'OLE22', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE525', 'OLE525', '59', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE526', 'OLE526', 'OLE84', 'OLE269', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE527', 'OLE527', 'OLE26', 'OLE270', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE528', 'OLE528', 'OLE30', 'OLE271', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE529', 'OLE529', 'OLE16', 'OLE271', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE530', 'OLE530', 'OLE26', 'OLE272', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE531', 'OLE531', '89', 'OLE272', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE532', 'OLE532', 'OLE26', 'OLE273', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE533', 'OLE533', 'OLE26', 'OLE274', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE534', 'OLE534', 'OLE47', 'OLE275', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE535', 'OLE535', '59', 'OLE276', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE536', 'OLE536', 'OLE86', 'OLE277', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE537', 'OLE537', 'OLE29', 'OLE277', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE540', 'OLE540', '66', 'OLE280', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE541', 'OLE541', '66', 'OLE281', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE542', 'OLE542', 'OLE29', 'OLE282', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE543', 'OLE543', 'OLE22', 'OLE283', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE544', 'OLE544', 'OLE26', 'OLE284', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE545', 'OLE545', 'OLE20', 'OLE285', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE546', 'OLE546', 'OLE22', 'OLE286', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE547', 'OLE547', '59', 'OLE286', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE549', 'OLE549', 'OLE54', 'OLE287', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE551', 'OLE551', '60', 'OLE101', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE554', 'OLE554', 'OLE22', 'OLE291', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE555', 'OLE555', 'OLE62', 'OLE136', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE556', 'OLE556', 'OLE22', 'OLE292', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE557', 'OLE557', 'OLE22', 'OLE293', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE558', 'OLE558', 'OLE29', 'OLE294', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE559', 'OLE559', 'OLE86', 'OLE294', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE563', 'OLE563', 'OLE44', '149', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE565', 'OLE565', 'OLE44', '298', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE567', 'OLE567', 'OLE44', '299', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE571', 'OLE571', 'OLE54', 'OLE301', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE579', 'OLE579', 'OLE44', '306', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE580', 'OLE580', '66', 'OLE328', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE581', 'OLE581', '66', 'OLE308', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE594', 'OLE594', 'OLE26', 'OLE313', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE595', 'OLE595', 'OLE26', 'OLE314', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE596', 'OLE596', 'OLE26', 'OLE315', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE597', 'OLE597', 'OLE26', 'OLE316', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE598', 'OLE598', 'OLE26', 'OLE317', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE599', 'OLE599', 'OLE26', 'OLE318', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE600', 'OLE600', 'OLE26', 'OLE319', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE601', 'OLE601', 'OLE26', 'OLE320', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE602', 'OLE602', 'OLE26', 'OLE321', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE603', 'OLE603', 'OLE26', 'OLE322', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE604', 'OLE604', 'OLE26', 'OLE323', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE605', 'OLE605', 'OLE26', 'OLE324', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE606', 'OLE606', 'OLE22', 'OLE325', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE607', 'OLE607', 'OLE22', 'OLE326', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE608', 'OLE608', 'OLE26', 'OLE326', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE611', 'OLE611', 'OLE62', 'OLE95', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE612', 'OLE612', 'OLE11', 'OLE329', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE613', 'OLE613', 'OLE11', 'OLE330', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE614', 'OLE614', 'OLE11', 'OLE331', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE619', 'OLE619', 'OLE49', 'OLE335', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE649', 'OLE649', 'OLE92', 'OLE350', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE650', 'OLE650', 'OLE33', 'OLE351', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE651', 'OLE651', 'OLE94', 'OLE352', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE652', 'OLE652', 'OLE93', 'OLE353', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE653', 'OLE653', 'OLE62', 'OLE102', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE654', 'OLE654', 'OLE54', 'OLE354', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE656', 'OLE656', '66', 'OLE355', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE657', 'OLE657', 'OLE62', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE658', 'OLE658', 'OLE22', 'OLE356', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE661', 'OLE661', '66', 'OLE358', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE662', 'OLE662', '83', 'OLE354', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE664', 'OLE664', 'OLE11', 'OLE361', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE665', 'OLE665', 'OLE44', 'OLE362', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE666', 'OLE666', 'OLE54', 'OLE363', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE667', 'OLE667', 'OLE32', 'OLE363', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE668', 'OLE668', 'OLE54', 'OLE88', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE669', 'OLE669', 'OLE32', 'OLE88', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE670', 'OLE670', 'OLE49', 'OLE364', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE671', 'OLE671', 'OLE22', 'OLE365', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE672', 'OLE672', 'OLE22', 'OLE366', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE673', 'OLE673', 'OLE22', 'OLE367', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE674', 'OLE674', 'OLE22', 'OLE368', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE680', 'OLE680', 'OLE11', 'OLE372', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE681', 'OLE681', 'OLE22', 'OLE373', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE682', 'OLE682', 'OLE22', 'OLE374', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE693', 'OLE693', 'OLE54', 'OLE379', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE694', 'OLE694', 'OLE37', 'OLE380', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE696', 'OLE696', '60', 'OLE381', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE697', 'OLE697', 'OLE70', 'OLE381', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE698', 'OLE698', '59', 'OLE382', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE699', 'OLE699', '59', 'OLE383', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE703', 'OLE703', 'OLE50', 'OLE260', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE707', 'OLE707', 'OLE17', 'OLE385', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE708', 'OLE708', 'OLE50', 'OLE212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE710', 'OLE710', 'OLE19', 'OLE386', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE711', 'OLE711', 'OLE19', 'OLE387', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE712', 'OLE712', 'OLE54', 'OLE390', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE713', 'OLE713', 'OLE54', 'OLE391', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE714', 'OLE714', 'OLE32', 'OLE391', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE715', 'OLE715', 'OLE54', 'OLE392', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE716', 'OLE716', 'OLE32', 'OLE392', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE719', 'OLE719', 'OLE98', 'OLE260', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE721', 'OLE721', 'OLE98', 'OLE326', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE722', 'OLE722', 'OLE70', 'OLE212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE74', 'OLE74', 'OLE37', 'OLE55', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE75', 'OLE75', 'OLE37', 'OLE56', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE77', 'OLE77', 'OLE44', 'OLE58', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE78', 'OLE78', 'OLE33', 'OLE59', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE79', 'OLE79', 'OLE39', 'OLE60', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE80', 'OLE80', 'OLE37', 'OLE61', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE81', 'OLE81', 'OLE46', 'OLE61', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE82', 'OLE82', 'OLE37', 'OLE62', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE83', 'OLE83', 'OLE46', 'OLE62', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE84', 'OLE84', 'OLE37', 'OLE63', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE85', 'OLE85', 'OLE46', 'OLE63', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE86', 'OLE86', 'OLE37', 'OLE64', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE87', 'OLE87', 'OLE46', 'OLE64', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE89', 'OLE89', 'OLE16', 'OLE66', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE90', 'OLE90', 'OLE16', 'OLE67', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE93', 'OLE93', 'OLE11', 'OLE69', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE94', 'OLE94', 'OLE13', 'OLE70', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE95', 'OLE95', 'OLE44', 'OLE71', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE96', 'OLE96', 'OLE62', 'OLE72', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE97', 'OLE97', 'OLE45', 'OLE74', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE98', 'OLE98', 'OLE45', 'OLE75', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE99', 'OLE99', 'OLE17', 'OLE76', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE1500', 'OLE1500', 'OLE54', 'OLE1500', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE1501', 'OLE1501', 'OLE26', 'OLE1501', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC6001-1', 'OLESEC6001-1', '51', 'OLESEC6001', 'N')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC6002-1', 'OLESEC6002-1', '51', 'OLESEC6002', 'N')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC6003-1', 'OLESEC6003-1', '51', 'OLESEC6003', 'N')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC6004-1', 'OLESEC6004-1', '51', 'OLESEC6004', 'N')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC6001-2', 'OLESEC6001-2', '44', 'OLESEC6001', 'N')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC6002-2', 'OLESEC6002-2', '44', 'OLESEC6002', 'N')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC6003-2', 'OLESEC6003-2', '44', 'OLESEC6003', 'N')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC6004-2', 'OLESEC6004-2', '44', 'OLESEC6004', 'N')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLEPURAP6940-01', 'OLEPURAP6940-01', '26', 'OLEPURAP6940', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESYS7145a-01', 'OLESYS7145a-01', '54', 'OLESYS7145a', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESYS7145a-02', 'OLESYS7145a-02', 'OLESYS1', 'OLESYS7145a', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESYS7145b-01', 'OLESYS7145b-01', '54', 'OLESYS7145b', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESYS7145b-02', 'OLESYS7145b-02', '22', 'OLESYS7145b', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESYS7145b-03', 'OLESYS7145b-03', '16', 'OLESYS7145b', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESYS7145b-04', 'OLESYS7145b-04', 'OLESYS1', 'OLESYS7145b', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESYS6007-1', 'OLESYS6007-1', '12', 'OLESYS6007', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESYS6007-2', 'OLESYS6007-2', '45', 'OLESYS6007', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLEMI6886-RLPRM1', 'OLEMI6886-RLPRM1', 'OLEMI6886-ROLE', 'OLEMI6886-PRM1', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLEMI6886-RLPRM2', 'OLEMI6886-RLPRM2', 'OLEMI6886-ROLE', 'OLEMI6886-PRM2', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLEMI6886-RLPRM3', 'OLEMI6886-RLPRM3', 'OLEMI6886-ROLE', 'OLEMI6886-PRM3', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLEMI6886-RLPRM4', 'OLEMI6886-RLPRM4', '45', 'OLEMI6886-PRM3', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLECNTRB162-RLPRM1', 'OLECNTRB162-RLPRM1', '56', 'OLECNTRB162-PRM', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLECNTRB199-RP1', 'OLECNTRB199-RP1', '51', 'OLECNTRB199-P1', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLECNTRB199-RP2', 'OLECNTRB199-RP2', '51', 'OLECNTRB199-P2', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE50-2', 'OLE50-2', '54', '820', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE50-3', 'OLE50-3', '32', '820', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE50-4', 'OLE50-4', '54', '819', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE50-5', 'OLE50-5', '32', '819', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE50-6', 'OLE50-6', '54', '821', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE50-7', 'OLE50-7', '32', '821', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLEMI8342-RLPRM', 'OLEMI8342-RLPRM', '39', 'OLEMI8342-PRM', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLECNTRB65-RLPRM1', 'OLECNTRB65-RLPRM1', '45', 'OLECNTRB65-PRM1', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLEMI9598-RLPRM1', 'OLEMI9598-RLPRM1', '45', 'OLEMI9598-PRM1', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLEMI9598-RLPRM2', 'OLEMI9598-RLPRM2', '45', 'OLEMI9598-PRM2', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLEMI9598-RLPRM3', 'OLEMI9598-RLPRM3', 'OLE55', 'OLEMI9598-PRM3', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE731', 'OLE731', 'OLE44', 'OLE393', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE732', 'OLE732', 'OLE62', 'OLE393', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE733', 'OLE733', 'OLE22', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE734', 'OLE734', 'OLE62', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE735', 'OLE735', 'OLE22', 'OLE395', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE736', 'OLE736', 'OLE22', 'OLE396', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE737', 'OLE737', 'OLE26', 'OLE396', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE738', 'OLE738', 'OLE50', 'OLE396', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE739', 'OLE739', 'OLE98', 'OLE396', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE740', 'OLE740', 'OLE22', 'OLE397', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE741', 'OLE741', '59', 'OLE397', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE742', 'OLE742', '59', 'OLE398', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE743', 'OLE743', 'OLE86', 'OLE399', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE744', 'OLE744', 'OLE29', 'OLE399', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE745', 'OLE745', 'OLE22', 'OLE400', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE746', 'OLE746', '59', 'OLE400', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE747', 'OLE747', 'OLE22', 'OLE401', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE748', 'OLE748', 'OLE22', 'OLE402', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE749', 'OLE749', 'OLE22', 'OLE403', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ROLE_PERM_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 12, '7:0b16f0fe8d108d5092a78a0563eb6797', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_ROLE_RSP_T::ole
INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1000', 'OLE1000', 'OLE11', 'OLE44', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1001', 'OLE1001', 'OLE12', 'OLE46', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1002', 'OLE1002', 'OLE15', 'OLE50', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1004', 'OLE1004', 'OLE22', 'OLE71', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1006', 'OLE1006', 'OLE22', 'OLE81', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1009', 'OLE1009', 'OLE25', 'OLE78', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1011', 'OLE1011', 'OLE26', 'OLE48', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1012', 'OLE1012', 'OLE28', 'OLE4', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1015', 'OLE1015', 'OLE37', 'OLE26', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1017', 'OLE1017', 'OLE9', 'OLE29', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1024', 'OLE1024', 'OLE39', 'OLE31', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1037', 'OLE1037', 'OLE41', 'OLE12', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1062', 'OLE1062', 'OLE44', 'OLE87', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1063', 'OLE1063', 'OLE48', 'OLE7', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1065', 'OLE1065', 'OLE50', 'OLE49', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1066', 'OLE1066', 'OLE50', 'OLE74', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1067', 'OLE1067', 'OLE50', 'OLE80', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1068', 'OLE1068', 'OLE53', 'OLE26', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1070', 'OLE1070', 'OLE41', 'OLE29', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1077', 'OLE1077', 'OLE55', 'OLE85', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1079', 'OLE1079', '60', 'OLE91', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1082', 'OLE1082', 'OLE7', 'OLE1', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1083', 'OLE1083', 'OLE70', 'OLE51', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1089', 'OLE1089', 'OLE79', 'OLE76', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1092', 'OLE1092', 'OLE68', 'OLE83', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1099', 'OLE1099', 'OLE41', 'OLE99', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1107', 'OLE1107', 'OLE41', 'OLE107', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1108', 'OLE1108', 'OLE41', 'OLE108', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1109', 'OLE1109', 'OLE22', 'OLE109', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1113', 'OLE1113', '60', 'OLE113', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1118', 'OLE1118', 'OLE7', 'OLE117', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1121', 'OLE1121', 'OLE41', 'OLE120', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1124', 'OLE1124', 'OLE7', 'OLE123', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1125', 'OLE1125', 'OLE41', 'OLE124', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLEFP7037A', 'OLEFP7037A', 'OLE55', 'OLE126', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLEMI4553-4', 'OLEMI4553-4', 'OLEMI4553-2', 'OLE92', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1120', 'OLE1120', 'OLE50', 'OLE102', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ROLE_RSP_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 13, '7:d47f877aa1e22701faaf60f91676fa48', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_KRIM_ROLE_RSP_ACTN_T::ole
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE101', 'OLE101', 'A', '1', 'F', '*', 'OLE1004', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE104', 'OLE104', 'A', '1', 'F', '*', 'OLE1066', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE106', 'OLE106', 'A', '1', 'A', '*', 'OLE1089', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE108', 'OLE108', 'A', '1', 'F', '*', 'OLE1009', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE110', 'OLE110', 'A', '1', 'F', '*', 'OLE1067', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE111', 'OLE111', 'F', '1', 'F', '*', 'OLE1006', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE113', 'OLE113', 'A', '1', 'F', '*', 'OLE1077', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE114', 'OLE114', 'A', '1', 'F', '*', 'OLE1062', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE115', 'OLE115', 'F', '1', 'F', '*', 'OLE1079', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE124', 'OLE124', 'A', '1', 'A', '*', 'OLE1099', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE132', 'OLE132', 'F', '1', 'A', '*', 'OLE1107', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE133', 'OLE133', 'F', '1', 'A', '*', 'OLE1108', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE134', 'OLE134', 'A', '1', 'F', '*', 'OLE1109', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE138', 'OLE138', 'A', '1', 'F', '*', 'OLE1113', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE169', 'OLE169', 'A', '1', 'A', '*', 'OLE1121', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE201', 'OLE201', 'F', '1', 'A', '*', 'OLE1125', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE34', 'OLE34', 'A', '1', 'A', '*', 'OLE1037', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE47', 'OLE47', 'A', '1', 'F', '*', 'OLE1015', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE48', 'OLE48', 'A', '2', 'F', '*', 'OLE1068', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE52', 'OLE52', 'F', '1', 'F', '*', 'OLE1017', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE53', 'OLE53', 'A', '2', 'A', '*', 'OLE1070', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE56', 'OLE56', 'A', '1', 'F', '*', 'OLE1024', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE74', 'OLE74', 'A', '1', 'F', '*', 'OLE1000', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE76', 'OLE76', 'A', '1', 'F', '*', 'OLE1001', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE78', 'OLE78', 'A', '1', 'F', '*', 'OLE1011', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE79', 'OLE79', 'A', '1', 'F', '*', 'OLE1065', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80', 'OLE80', 'A', '1', 'F', '*', 'OLE1002', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE81', 'OLE81', 'A', '1', 'F', '*', 'OLE1083', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLEFP7037A', 'OLEFP7037A', 'A', '1', 'F', '*', 'OLEFP7037A', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE82', 'OLE82', 'A', '1', 'F', '*', 'OLE1120', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ROLE_RSP_ACTN_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 14, '7:5413d9f7b8822f1c117ed23af1d4e556', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_KRIM_PERM_T::ole
INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10004', 'OLE10004', '1', '23', 'OLE-SELECT', 'Requisition LookUp', 'Lookup of Requisition Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10005', 'OLE10005', '1', '16', 'OLE-SELECT', 'Requisition Edit', 'Editing the Requisition Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10006', 'OLE10006', '2', '14', 'OLE-SELECT', 'Requisition Cancel', 'Cancelling Requisition Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10007', 'OLE10007', '1', '8', 'OLE-SELECT', 'Requisition Approve', 'Approving the Requisition Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10008', 'OLE10008', '1', '1', 'OLE-SELECT', 'Assign To Others', 'can search  take & assign requisitions to selectors', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10009', 'OLE10009', '1', '16', 'OLE-PURAP', 'PurchaseOrder Edit', 'Editing PurchaseOrder Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10010', 'OLE10010', '1', '8', 'OLE-PURAP', 'PurchaseOrder Approve', 'Approving PurchaseOrder on Condition', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10011', 'OLE10011', '1', '16', 'OLE-PURAP', 'PurchaseOrder Amendment Edit', 'Editing the Amend PurchaseOrder Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10012', 'OLE10012', '1', '14', 'OLE-PURAP', 'PurchaseOrder Amendment Cancel', 'Cancelling the Amend PurchaseOrder', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10013', 'OLE10013', '1', '1', 'OLE-PURAP', 'PurchaseOrder Amendment Acknowledge', 'Acknowledging for PurchaseOrder Amend Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10014', 'OLE10014', '1', '42', 'OLE-PURAP', 'PurchaseOrder Amendment Creation', 'Creating a PurchaseOrder Amendment Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10015', 'OLE10015', '1', '10', 'OLE-VND', 'Create Vendor', 'Creating a new Vendor', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10016', 'OLE10016', '1', '8', 'OLE-VND', 'Vendor Approval', 'Approving the vendor which is new', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10017', 'OLE10017', '2', '16', 'OLE-VND', 'Edit Vendor', 'Editing the Vendor which is created', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10018', 'OLE10018', '1', '42', 'OLE-PURAP', 'PurchaseOrder Creation', 'Creating Purchase Order', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10019', 'OLE10019', '1', '14', 'OLE-PURAP', 'PurchaseOrder Cancel', 'Cancelling PurchaseOrder Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10020', 'OLE10020', '1', '8', 'OLE-PURAP', 'PurchaseOrder Approve Conditional', 'Approving PurchaseOrder Document depending on amount', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10021', 'OLE10021', '1', '26', 'OLE-VND', 'Edit Discounts', 'Authorizes users to modify Discount on Vendor Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10022', 'OLE10022', '1', '1', 'OLE-SELECT', 'Load Error', 'Authorizes users to reloads  routes the Document for action', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10023', 'OLE10023', '1', '1', 'OLE-SELECT', 'Load Duplicate Check', 'Authorizes users to check for Duplicate Records', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10024', 'OLE10024', '1', '1', 'OLE-SELECT', 'Load Fund Check', 'Authorizes users to check the Funds and routes the reports to corresponding selectors', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10025', 'OLE10025', '1', '1', 'OLE-PURAP', 'Transmit PO', 'Transmitting the PurchaseOrder Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10026', 'OLE10026', '1', '1', 'OLE-PURAP', 'Retransmit PurchaseOrder', 'Authorizes users to Retransmit the PurchaseOrder Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10027', 'OLE10027', '1', '1', 'OLE-SELECT', 'Setup New Users', 'Authorizes Users to add New Users', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10028', 'OLE10028', '2', '8', 'OLE-SELECT', 'Condition on Line Item', 'Authorizes users to send FYI to ACQ  Manager', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10029', 'OLE10029', '1', '8', 'OLE-SELECT', 'New Vendor', 'Authorizes Users to send FYI to ACQ Manager when a new Vendor is Created', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10030', 'OLE10030', '1', '1', 'OLE-SELECT', 'No Selector in Load', 'Authorizes user to route the Document if no Selector identifed', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10031', 'OLE10031', '1', '1', 'OLE-SELECT', 'Requisition Auto', 'System Generated', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10032', 'OLE10032', '1', '1', 'OLE-SELECT', 'Staff Pickup', 'Authorizes Users to send to any Selectors', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10035', 'OLE10035', '2', '24', 'OLE-SELECT', 'Inquire Into Records', 'Authorizes users to Inquire Records', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10036', 'OLE10036', '2', '40', 'OLE-SELECT', 'Open Document', 'Authorizes users to open Vendor Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10041', 'OLE10041', '1', '10', 'OLE-SELECT', 'Initiate OLE Documents', 'Authorizes the initiation of Financial System Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10042', 'OLE10042', '6', '42', 'OLE-SELECT', 'Initiate Vendor Document', 'Authorizes users to Create Vendor Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10043', 'OLE10043', '3', '42', 'OLE-SELECT', 'Edit Vendor Document', 'Authorizes users to Edit Vendor Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10044', 'OLE10044', '1', '10', 'OLE-SELECT', 'Initiate Receiving Line Items Document', 'Authorizes the initiation of Receiving Line Items Transactional Documents.', 'N')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10045', 'OLE10045', '1', '16', 'OLE-SELECT', 'Edit Receiving Line Items Document', 'Edit permission for Receiving Line Items Transactional documents prior to the document being submitted.', 'N')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10046', 'OLE10046', '1', '4', 'OLE-SELECT', 'Blanket Approve Receiving Line Items  Document', 'Allows access to the Blanket Approval button on OLE Receiving Line Item Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10047', 'OLE10047', '1', '40', 'OLE-SELECT', 'Open Receiving Line Items Document', 'Authorizes users to open Receiving Line Items document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10048', 'OLE10048', '1', '4', 'OLE-SELECT', 'Blanket Approve RCVC Document', 'Allows access to the Blanket Approval button on OLE Receiving Line Item Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10049', 'OLE10049', '1', '10', 'OLE-SELECT', 'Initiate Payment Request Document', 'Authorizes the initiation of the Payment Request Document.', 'N')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10050', 'OLE10050', '1', '16', 'OLE-SELECT', 'Edit ENROUTE Payment Request Document', 'Authorizes users who can edit Payment Request Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10051', 'OLE10051', '1', '16', 'OLE-SELECT', 'Edit PROCESSED Payment Request Document', 'Authorizes users who can edit Payment Request Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10052', 'OLE10052', '1', '16', 'OLE-SELECT', 'Edit FINAL Payment Request Document', 'Authorizes users who can edit Payment Request Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10053', 'OLE10053', '1', 'OLE41', 'OLE-SELECT', 'Modify PREQ Account Accounting Lines', 'Allows users to modify the Source accounting lines on documents answering to the parent document Accounts PayableTransactional Document when a document is at the Account Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10054', 'OLE10054', '1', '31', 'OLE-SELECT', 'Cancel Payment Request', 'Users authorized to take the Request Cancel action on Payment Request documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10055', 'OLE10055', '1', '1', 'OLE-SELECT', 'Add New Line Item', 'Authorizes the User to add new Line Item while receiving PO', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10056', 'OLE10056', '1', '16', 'OLE-SELECT', 'Close Purchase Order', 'Authorizes the User Close PO', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10057', 'OLE10057', '1', '16', 'OLE-SELECT', 'Edit Payment Request Document', 'Edit permission Payment Request document prior to the document being submitted.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10060', 'OLE10060', '2', '1', 'OLE-SELECT', 'Firm Type Requisition Edit', 'Editing the Firm Type Requisition Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10061', 'OLE10061', '2', '1', 'OLE-SELECT', 'Firm Type PurchaseOrder Edit', 'Editing Firm Type PurchaseOrder Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10062', 'OLE10062', '2', '16', 'OLE-SELECT', 'Firm Type PurchaseOrder Amendment Edit', 'Editing the Firm Tpye Amend PurchaseOrder Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10063', 'OLE10063', '2', '1', 'OLE-SELECT', 'Edit Own Requisition Document', 'Edit permission for own Requisition document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10064', 'OLE10064', '1', '1', 'OLE-SELECT', 'Edit Own Assigned Requisition Document', 'Edit permission for own or Assigned Requisition document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10065', 'OLE10065', '1', '16', 'OLE-SELECT', 'Edit Void Document', 'Edit permission for the Purchase Order Void document prior to the document being submitted.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10066', 'OLE10066', '1', '16', 'OLE-SELECT', 'Edit Split Document', 'Edit permission for the Purchase Order Split document prior to the document being submitted.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10067', 'OLE10067', '1', '16', 'OLE-SELECT', 'Edit Reopen Document', 'Edit permission for the Purchase Order Reopen document prior to the document being submitted.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10068', 'OLE10068', '3', '16', 'OLE-SELECT', 'Print Action', 'Authorizes users to take the Print action on a Purchase Order.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10069', 'OLE10069', '3', '16', 'OLE-SELECT', 'Print Purchase Order', 'Users who can print a Purchase Order document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10070', 'OLE10070', '3', '16', 'OLE-SELECT', 'Preview Purchase Order', 'Users who can preview a Purchase Order document before printing it.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10071', 'OLE10071', '1', '1', 'OLE-SELECT', 'Other Type Requisition Edit', 'Editing the Other Order Type Requisition Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10100', 'OLE10100', '1', '33', 'OLE-PURAP', 'Upload ordInputFileType', 'Authorizes users to access and upload the ordinary File.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10108', 'OLE10108', '1', '1', 'OLE-SELECT', 'Search Load Summary', 'Authorizes the User to add Search Load Summary', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10109', 'OLE10109', '1', '16', 'OLE-SELECT', 'View Load Summary', 'Authorizes the User to add View Load Summary', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10110', 'OLE10110', '1', '1', 'OLE-SELECT', 'Other Type PurchaseOrder Edit', 'Editing the Other Order Type Purchase Order Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10111', 'OLE10111', '1', '16', 'OLE-SELECT', 'Other Type PurchaseOrder Amendment Edit', 'Editing the Other Order Type Purchase Order Amendment Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10112', 'OLE10112', '1', '16', 'OLE-SELECT', 'Edit Requisition Document', '', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10115', 'OLE10115', '1', '10', 'OLE-PURAP', 'Initiate Order Queue Document', 'Authorizes the initiation of Order Queue Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10116', 'OLE10116', '1', '10', 'OLE-SELECT', 'Initiate Receiving Document', 'Authorizes the initiation of Receiving Documents.', 'N')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10117', 'OLE10117', '1', '16', 'OLE-SELECT', 'Edit Receiving Document', 'Authorizes the edit of Receiving Documents.', 'N')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10118', 'OLE10118', '1', '16', 'OLE-SELECT', 'Edit Receiving Queue Document', 'Authorizes the edit of Receiving Queue Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10119', 'OLE10119', '1', '10', 'OLE-SELECT', 'Initiate Receiving Queue Document', 'Authorizes the initiation of Receiving Queue Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10120', 'OLE10120', '1', '1', 'OLE-SELECT', 'Assign own Requisition', 'Assign own Requisition', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10121', 'OLE10121', '1', '4', 'OLE-SELECT', 'Blanket Approve Vendor Document', 'Allows access to the Blanket Approval button on OLE Vendor Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10122', 'OLE10122', '1', '4', 'OLE-SELECT', 'Blanket Approve PO Amendment Document', 'Allows access to the Blanket Approval button on POA Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80141', 'OLE80141', '1', '1', 'OLE-SELECT', 'Create Vendor Division', 'Authorizes users to Edit Vendor Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80142', 'OLE80142', '1', '1', 'OLE-SELECT', 'Deactivate Vendor', 'Authorizes users to deactivate Vendor Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80155', 'OLE80155', '1', '1', 'OLE-SELECT', 'Delete UnApproved PREQ Document', 'This Permission is used to delete the unapproved Payment Request Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80156', 'OLE80156', '1', '1', 'OLE-SELECT', 'Delete Approved PREQ Document', 'This Permission is used to delete the approved Payment Request Documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80157', 'OLE80157', '1', '10', 'OLE-SELECT', 'Initiate Document OLE_DI', 'This Permission is for Initiating the Distribution of Income and Expense Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80158', 'OLE80158', '1', '10', 'OLE-SELECT', 'Initiate Document OLE_DV', 'This Permission is for Initiating the Disbursement Voucher Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80159', 'OLE80159', '1', '16', 'OLE-SELECT', 'Edit Document OLE_DV PreRoute', 'This Permission is for the Disbursement Voucher document in the PreRoute state.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80160', 'OLE80160', '1', '16', 'OLE-SELECT', 'Edit Document OLE_DV ENROUTE', 'This Permission is for the Disbursement Voucher Document which is in Enroute state.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80161', 'OLE80161', '1', '16', 'OLE-SELECT', 'Edit Document OLE_CM PreRoute', 'This Permission is for the Credit Memo which is in PreRoute state.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80162', 'OLE80162', '1', '10', 'OLE-SELECT', 'Initiate Document OLE_GEC', 'Initiate Permission of the General Error Correction Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80163', 'OLE80163', '1', '1', 'OLE-SELECT', 'Edit Vendor Linking Number', 'Allows users to edit Vendor Linking Number.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80164', 'OLE80164', '1', '10', 'OLE-SELECT', 'Initiate Budget Document', 'Allows to create Budget Adjustment document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80165', 'OLE80165', '1', '16', 'OLE-SELECT', 'Edit Budget Document', 'Allows to edit Budget Adjustment document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80166', 'OLE80166', '1', '10', 'OLE-SELECT', 'Initiate Transfer Fund Document', 'Allows to create Transfer Fund document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80167', 'OLE80167', '1', '16', 'OLE-SELECT', 'Edit Transfer Fund Document', 'Allows to edit Transfer Fund document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80168', 'OLE80168', '1', '10', 'OLE-SELECT', 'Initiate Deposit Document', 'Allows to create Deposit Adjustment document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80169', 'OLE80169', '1', '16', 'OLE-SELECT', 'Edit Deposit Document', 'Allows to edit Deposit Adjustment document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80170', 'OLE80170', '1', '1', 'OLE-SELECT', 'Edit Restriction', 'Allows users to edit restriction on accounts.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80171', 'OLE80171', '1', '1', 'OLE-SELECT', 'Edit Sufficient fund check', 'Allows users to edit Sufficient fund check on accounts.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80172', 'OLE80172', '1', '1', 'OLE-SELECT', 'Upload Budget', 'Allows users to upload budget file.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80333', 'OLE80333', '1', '5', 'OLE-SELECT', 'Route Document.', 'Authorizers the users to route the Payment request Document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80510', 'OLE80510', '1', '4', 'OLE-SELECT', 'Approve OLE_REQS', 'Allow users to approve and cancel requisition document.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80511', 'OLE80511', '1', '10', 'OLE-SELECT', 'Initiate Document(PURAP)', 'Allow users to Create the Purchasing Accounts Payable Simple Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80512', 'OLE80512', '1', '16', 'OLE-SELECT', 'Edit Document(PURAP)', 'Allow users to Edit the Purchasing Accounts Payable Simple Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80513', 'OLE80513', '1', '10', 'OLE-SELECT', 'Initiate Document(COA)', 'Allow users to Create the  Chart Simple Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80514', 'OLE80514', '1', '16', 'OLE-SELECT', 'Edit Document(COA)', 'Allow users to Edit the Chart Simple Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80515', 'OLE80515', '1', '10', 'OLE-SELECT', 'Initiate Document(RCV)', 'Allow users to Create the  Receiving Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80516', 'OLE80516', '1', '16', 'OLE-SELECT', 'Edit Document(RCV)', 'Allow users to Edit the Receiving Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80517', 'OLE80517', '1', '10', 'OLE-SELECT', 'Initiate Document(Invoice)', 'Allow users to Create the  Invoicing Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80518', 'OLE80518', '1', '16', 'OLE-SELECT', 'Edit Document(Invoice)', 'Allow users to Edit the Invoicing Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80519', 'OLE80519', '1', '10', 'OLE-SELECT', 'Initiate Document(Complex RCV)', 'Allow users to Create the  Complex Receiving Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80520', 'OLE80520', '1', '16', 'OLE-SELECT', 'Edit Document(Complex RCV)', 'Allow users to Edit the Complex Receiving Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80521', 'OLE80521', '1', '10', 'OLE-SELECT', 'Initiate Document (Complex PURAP)', 'Allow users to Create the Purchasing Accounts Payable Complex Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80522', 'OLE80522', '1', '16', 'OLE-SELECT', 'Edit Document (Complex PURAP)', 'Allow users to Edit the Purchasing Accounts Payable Complex Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80523', 'OLE80523', '1', '10', 'OLE-SELECT', 'Initiate Document (Complex COA)', 'Allow users to Create the  Chart Complex Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80524', 'OLE80524', '1', '16', 'OLE-SELECT', 'Edit Document (Complex COA)', 'Allow users to Edit the Chart Complex Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10123', 'OLE10123', '1', '10', 'OLE-SELECT', 'Initiate Invoice Document', 'Authorizes the initiation of the Invoice Document.', 'N')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10124', 'OLE10124', '1', '16', 'OLE-SELECT', 'Edit ENROUTE Invoice Document', 'Authorizes users who can edit Invoice Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10125', 'OLE10125', '1', '16', 'OLE-SELECT', 'Edit PROCESSED Invoice Document', 'Authorizes users who can edit Invoice Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10126', 'OLE10126', '1', '16', 'OLE-SELECT', 'Edit FINAL Invoice Document', 'Authorizes users who can edit Invoice Documents that are in ENROUTE status.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10127', 'OLE10127', '1', 'OLE41', 'OLE-SELECT', 'Modify PRQS Account Accounting Lines', 'Allows users to modify the Source accounting lines on documents answering to the parent document Accounts PayableTransactional Document when a document is at the Account Node of routing.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10128', 'OLE10128', '1', '31', 'OLE-SELECT', 'Cancel Invoice', 'Users authorized to take the Request Cancel action on Invoice documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10129', 'OLE10129', '1', '16', 'OLE-SELECT', 'Edit Invoice Document', 'Edit permission Invoice document prior to the document being submitted.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10130', '1203', '1', '10', 'OLE-PTRN', 'Initiate Borrower Document', 'Initiate Borrower Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10131', '1204', '1', '23', 'KR-NS', 'Look up Borrower Document', 'Look up Borrower Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10132', '1206', '1', '24', 'KR-NS', 'Inquire Borrower Document', 'Inquire Borrower Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10133', '1014', '1', '57', 'KR-KRAD', 'Edit Location Level Name Field', 'Editing Location Level Name Field', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10134', '1015', '1', '23', 'KR-KRAD', 'View Location Level', 'View Location Level Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10135', 'OLE100051', '1', '1', 'OLE-LIC', 'License Request workflow initial filter', 'License Request workflow initial filter', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10136', 'OLE100061', '1', '1', 'OLE-LIC', 'Full License Request workflow', 'Full License Request workflow', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10137', 'OLE100071', '1', '1', 'OLE-LIC', 'License Request secondary filter', 'License Request secondary filter', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10138', '1008', '1', '10', 'OLE-PTRN', 'Initiate AddressType Document', 'Initiate AddressType Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10139', '1009', '1', '16', 'OLE-PTRN', 'Edit AddressType Document', 'Edit AddressType Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10140', '1010', '1', '10', 'OLE-PTRN', 'Initiate EmailType Document', 'Initiate EmailType Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10141', '1011', '1', '16', 'OLE-PTRN', 'Edit EmailType Document', 'Edit EmailType Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10142', '1012', '1', '10', 'OLE-PTRN', 'Initiate PhoneType Document', 'Initiate PhoneType Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10143', '1013', '1', '16', 'OLE-PTRN', 'Edit PhoneType Document', 'Edit PhoneType Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10144', '1016', '1', '10', 'OLE-CAT', 'Initiate Access Method Document', 'Initiate Access Method Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10145', '1017', '1', '10', 'OLE-CAT', 'Initiate Acquisition Method Document', 'Initiate Acquisition Method Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10146', '1018', '1', '10', 'OLE-CAT', 'Initiate Action Document', 'Initiate Action Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10147', '1019', '1', '10', 'OLE-CAT', 'Initiate Completeness Document', 'Initiate Completeness Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10148', '1020', '1', '10', 'OLE-CAT', 'Initiate Country Codes Document', 'Initiate Country Codes Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10149', '1021', '1', '10', 'OLE-CAT', 'Initiate Location Status Document', 'Initiate Location Status Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10150', '1022', '1', '10', 'OLE-CAT', 'Initiate Shelving Order Document', 'Initiate Shelving Order Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10151', '1023', '1', '10', 'OLE-CAT', 'Initiate Shelving Scheme Document', 'Initiate Shelving Scheme Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10152', '1024', '1', '10', 'OLE-CAT', 'Initiate Specific Retention Policy Document', 'Initiate Specific Retention Policy Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10153', '1025', '1', '10', 'OLE-CAT', 'Initiate Seperate Or Composite Report Document', 'Initiate Seperate Or Composite Report Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10154', '1026', '1', '10', 'OLE-CAT', 'Initiate Record Type Document', 'Initiate Record Type Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10155', '1027', '1', '10', 'OLE-CAT', 'Initiate Lending Policy Document', 'Initiate Lending Policy Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10156', '1028', '1', '10', 'OLE-CAT', 'Initiate Instance Item Type Document', 'Initiate Instance Item Type Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10157', '1029', '1', '10', 'OLE-CAT', 'Initiate Specific Retention Policy Type Unit Document', 'Initiate Specific Retention Policy Type Unit Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10158', '1030', '1', '10', 'OLE-CAT', 'Initiate Source Of Term Document', 'Initiate Source Of Term Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10159', '1031', '1', '10', 'OLE-CAT', 'Initiate Notation Type Document', 'Initiate Notation Type Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10160', '1032', '1', '10', 'OLE-CAT', 'Initiate Privacy Document', 'Initiate Privacy Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10161', '1033', '1', '10', 'OLE-CAT', 'Initiate Receipt Status Document', 'Initiate Receipt Status Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10162', '1034', '1', '10', 'OLE-CAT', 'Initiate ELARelatioshipDocument Document', 'Initiate ELARelatioshipDocument Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10163', '1035', '1', '10', 'OLE-CAT', 'Initiate EncodingLevelDocument Document', 'Initiate EncodingLevelDocument Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10164', '1036', '1', '10', 'OLE-CAT', 'Initiate FieldEncodingLevelDocument Document', 'Initiate FieldEncodingLevelDocument Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10165', '1037', '1', '10', 'OLE-CAT', 'Initiate GeneralRetentionPolicyDocument Document', 'Initiate GeneralRetentionPolicyDocument Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10166', '1038', '1', '10', 'OLE-CAT', 'Initiate Statistical Searching Codes Document', 'Initiate Statistical Searching Codes Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10167', '1039', '1', '10', 'OLE-CAT', 'Initiate Type Of Ownership Document', 'Initiate Type Of Ownership Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10168', '1040', '1', '10', 'OLE-CAT', 'Initiate Reproduction Policy Document', 'Initiate Reproduction Policy Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10169', 'OLE1001', '1', '10', 'OLE-LIC', 'Initiate Checklist Document', 'Initiate Checklist Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10170', 'OLE1002', '1', '16', 'OLE-LIC', 'Edit Checklist Document', 'Edit Checklist Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10171', 'OLE1003', '1', '10', 'OLE-LIC', 'Initiate License Request Status Document', 'Initiate License Request Status Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10172', 'OLE1004', '1', '16', 'OLE-LIC', 'Edit License Request Status Document', 'Edit License Request Status Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10173', 'OLE1005', '1', '10', 'OLE-LIC', 'Initiate Current Location Document', 'Initiate Current Location Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10174', 'OLE1006', '1', '16', 'OLE-LIC', 'Edit Current Location Document', 'Edit Current Location Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10175', 'OLE1007', '1', '10', 'OLE-LIC', 'Initiate License Request Type Document', 'Initiate License Request Type Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10176', 'OLE1008', '1', '16', 'OLE-LIC', 'Edit License Request Type Document', 'Edit License Request Type Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10177', 'OLE1009', '1', '10', 'OLE-LIC', 'Initiate Agreement DocType Document', 'Initiate Agreement DocType Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10178', 'OLE1010', '1', '16', 'OLE-LIC', 'Edit Agreement DocType Document', 'Edit Agreement DocType Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10179', 'OLE1011', '1', '10', 'OLE-LIC', 'Initiate Agreement Method Document', 'Initiate Agreement Method Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10180', 'OLE1012', '1', '16', 'OLE-LIC', 'Edit Agreement Method Document', 'Edit Agreement Method Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10181', 'OLE1013', '1', '10', 'OLE-LIC', 'Initiate Agreement Status Document', 'Initiate Agreement Status Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10182', 'OLE1014', '1', '16', 'OLE-LIC', 'Edit Agreement Status Document', 'Edit Agreement Status Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10183', 'OLE1015', '1', '10', 'OLE-LIC', 'Initiate Agreement Type Document', 'Initiate Agreement Type Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10184', 'OLE1016', '1', '16', 'OLE-LIC', 'Edit Agreement Type Document', 'Edit Agreement Type Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10185', 'OLE1017', '1', '4', 'OLE-LIC', 'Blanket Approve', 'Blanket Approve for License Request Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10187', '1101', '1', '1', 'OLE-PTRN', 'Can Override Loan', 'Can Override the Loan Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10188', '1102', '1', '1', 'OLE-PTRN', 'Mapping Circulation Desk', 'This is for mapping the circulation Desk with the operator', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10190', 'OLE1018', '1', '16', 'OLE-LIC', 'Edit License Request Document', 'Editing the License Request document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10191', 'OLE1019', '1', '15', 'OLE-LIC', 'Save License Request Document', 'Save the License Request document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10192', '2100', '1', '1', 'OLE-DLVR', 'Can Remove Note', 'Can Remove Check-in Note', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KR1000', 'D0C2892F47EB9142E0406E0AC31D23D6', '1', '8', 'KUALI', 'Take Requested Complete Action', 'Authorizes users to take the Complete action on documents routed to them.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRLLMD1', 'ED104', '1', '10', 'OLE-SELECT', 'Initiate Location Level Maintenance Document', 'Initiate Location Level Maintenance Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRLMD1', 'ED102', '1', '42', 'OLE-SELECT', 'Edit Location Maintenance Document', 'Edit Location Maintenance Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRLMD2', 'ED103', '1', '42', 'OLE-SELECT', 'Edit Location Maintenance Document for Shelving Location Administrator', 'Edit Location Maintenance Document for Shelving Location Administrator', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10003', 'CDD8032915D6BF1FE040F90A05B94313', '1', '58', 'KR-SAP', 'View Kitchen Sink Group', 'Allows users to view the group in kitchen sink page 9.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10004', 'CDD8032915DABF1FE040F90A05B94313', '1', '58', 'KR-SAP', 'View Kitchen Sink Page', 'Allows users to view page 9 in the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10005', 'CDD8032915DEBF1FE040F90A05B94313', '1', '59', 'KR-SAP', 'Edit Kitchen Sink Group', 'Allows users to edit the group in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10006', 'CDD8032915E2BF1FE040F90A05B94313', '1', '56', 'KR-SAP', 'View Kitchen Sink Field', 'Allows users to view the field in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10007', 'CDD8032915E6BF1FE040F90A05B94313', '1', '57', 'KR-SAP', 'Edit Kitchen Sink Field', 'Allows users to edit the field in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10008', 'CDD8032915EABF1FE040F90A05B94313', '1', '56', 'KR-SAP', 'View Kitchen Sink Field Group', 'Allows users to view the field group in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10009', 'CDD8032915EEBF1FE040F90A05B94313', '1', '57', 'KR-SAP', 'Edit Kitchen Sink Field Group', 'Allows users to edit the field group in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10010', 'CDD8032915F2BF1FE040F90A05B94313', '1', '62', 'KR-SAP', 'Perform Kitchen Sink Action', 'Allows users to perform the save action in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10011', 'CDD8032915F6BF1FE040F90A05B94313', '1', '60', 'KR-SAP', 'View Kitchen Sink Widget', 'Allows users to view the quickfinder widget in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10012', 'CDD8032915FABF1FE040F90A05B94313', '1', '63', 'KR-SAP', 'View Kitchen Sink Line', 'Allows users to view the collection line in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10013', 'CDD8032915FEBF1FE040F90A05B94313', '1', '64', 'KR-SAP', 'Edit Kitchen Sink Line', 'Allows users to edit the collection line in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10014', 'CDD803291602BF1FE040F90A05B94313', '1', '65', 'KR-SAP', 'View Kitchen Sink Line Field', 'Allows users to view the collection line field in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10015', 'CDD803291607BF1FE040F90A05B94313', '1', '66', 'KR-SAP', 'Edit Kitchen Sink Line Field', 'Allows users to edit the collection line field in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('KRSAP10016', 'CDD80329160CBF1FE040F90A05B94313', '1', '67', 'KR-SAP', 'Perform Kitchen Sink Line Action', 'Allows users to perform the delete line action in page 9 of the kitchen sink', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEIITMD', 'ED1012', '1', '42', 'OLE-SELECT', 'Edit Item Type Maintenance Document', 'Edit Item Type Maintenance Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLEITAS', 'ED1013', '1', '10', 'OLE-SELECT', 'Initiate Item Availability Maintenance Document', 'Initiate Item Availability Maintenance Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLERSMD', 'ED1014', '1', '42', 'OLE-SELECT', 'Edit Receipt Status Maintenance Document', 'Edit Receipt Status Maintenance Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLESSMD', 'ED1011', '1', '42', 'OLE-SELECT', 'Edit Call Number Type Maintenance Document', 'Edit Call Number Type Maintenance Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLESTSRC', 'ED1015', '1', '42', 'OLE-SELECT', 'Edit Statistical Searching Codes Maintenance Document', 'Edit Statistical Searching Codes Maintenance Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLETYPO', 'ED1016', '1', '42', 'OLE-SELECT', 'Edit Type Of Ownership Maintenance Document', 'Edit Type Of Ownership Maintenance Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10193', '3100', '1', '58', 'OLE-SELECT', 'Show Serials Receiving Record From Holding', 'Allows users to view Serial Receiving record from Holdings record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10195', 'OLE90071', '1', '62', 'OLE-PTRN', 'Create fastadd', 'User can create an item record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10196', 'OLE90072', '1', '42', 'OLE-PTRN', 'Create/Update Circulation desk', 'User can create and maintain circulation desk', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10197', 'OLE90073', '1', '58', 'OLE-PTRN', 'Display Existing loan', 'User can display existing loan', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10198', 'OLE90074', '1', '62', 'OLE-PTRN', 'Update due date', 'User can update due date.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10199', 'OLE90075', '1', '58', 'OLE-PTRN', 'Display request', 'User can display request', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10200', 'OLE90076', '1', '58', 'OLE-DLVR', 'Update request queue', 'User can update request queue', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10201', 'OLE90077', '1', '62', 'OLE-PTRN', 'Can Override', 'User can override.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10202', 'OLE90078', '1', '62', 'OLE-PTRN', 'Post payment', 'The posted amount will be detected from the bill amount.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10203', 'OLE90079', '1', '62', 'OLE-PTRN', 'Post forgive', 'The posted amount will be detected from the bill amount.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10205', 'OLE90081', '1', '59', 'OLE-PTRN', 'Create/Update proxy patron', 'User can create/update proxy patron', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10206', 'OLE90082', '1', '59', 'OLE-PTRN', 'Can backdate check-in time/date', 'User can backdate check-in time/date.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10207', 'OLE90083', '1', '59', 'OLE-PTRN', 'EDITOR_ITEM_INFORMATION', 'User can update item record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10208', 'OLE90084', '1', '59', 'OLE-PTRN', 'Update patron address only', 'User can update patron address', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10209', 'OLE90085', '1', '58', 'OLE-PTRN', 'Display Current loan', 'User can display current loaned item', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10210', 'OLE90086', '1', '1', 'OLE-PTRN', 'Display Bill', 'User can display bill', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10211', 'OLE90087', '1', '1', 'OLE-PTRN', 'Create/update Bill', 'User can create/update bill', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10212', 'OLE90088', '1', '1', 'OLE-PTRN', 'Can update item status', 'Can update item status', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10214', 'OLE90090', '1', '42', 'OLE-PTRN', 'Edit Request Document', 'Edit Request Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE404', 'OLE404', '1', '1', 'OLE-SELECT', 'SERIAL_RECEIVING_SEARCH', 'Allows user to perform serial receiving search', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10223', 'OLE70004', '1', '58', 'OLE-CAT', 'DESC_WORKBENCH_EXPORT_XML', 'Exports requested record to requestXml', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10224', 'OLE70005', '1', '1', 'OLE-CAT', 'DESC_WORKBENCH_SEARCH', 'User can search the bib/holding/item record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10225', 'OLE70006', '1', '1', 'OLE-CAT', 'MARC_EDITOR_ADD_BIB', 'User can create a bib record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10226', 'OLE70007', '1', '62', 'OLE-CAT', 'MARC_EDITOR_EDIT_BIB', 'User can edit a bib record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10227', 'OLE70008', '1', '62', 'OLE-CAT', 'MARC_EDITOR_DELETE_BIB', 'User can delete a bib record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10228', 'OLE70009', '1', '57', 'OLE-CAT', 'MARC_EDITOR_BIB_STATUS', 'User can edit bib status for a bib record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10229', 'OLE70010', '1', '59', 'OLE-CAT', 'EDITOR_HOLDINGS_CALLNUMBER', 'User can view holdings call number', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10230', 'OLE70011', '1', '59', 'OLE-CAT', 'EDITOR_HOLDINGS_LOCATION', 'User can view holdings location', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10231', 'OLE70012', '1', '59', 'OLE-CAT', 'EDITOR_ITEM_CALLNUMBERANDLOCATION', 'User can view item callnumber and  location', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10232', 'OLE70014', '1', '62', 'OLE-CAT', 'INSTANCE_EDITOR_ADD_INSTANCE', 'User can add instance for a bib record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10233', 'OLE70015', '1', '62', 'OLE-CAT', 'INSTANCE_EDITOR_EDIT_INSTANCE', 'User can edit instance for a bib record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10234', 'OLE70016', '1', '62', 'OLE-CAT', 'INSTANCE_EDITOR_EDIT_ITEM', 'User can edit item for a bib record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10235', 'OLE70017', '1', '1', 'OLE-CAT', 'INSTANCE_EDITOR_ADD_ITEM', 'User can add item record ', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10236', 'OLE70018', '1', '1', 'OLE-CAT', 'INSTANCE_EDITOR_DELETE_INSTANCE', 'User can delete instance record ', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10237', 'OLE70019', '1', '1', 'OLE-CAT', 'INSTANCE_EDITOR_DELETE_ITEM', 'User can delete item record ', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10238', 'OLE70020', '1', '1', 'OLE-CAT', 'CALL_NUMBER_BROWSE', 'Allows user to perform call number searches', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10239', 'OLE70021', '1', '1', 'OLE-CAT', 'TRANSFER_HOLDING_OR_ITEM', 'Allows user to perform transfer holding/item searches', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10240', 'OLE70022', '1', '1', 'OLE-CAT', 'BOUND_WITH', 'Allows user to perform bound holding/item', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10241', 'OLE70023', '1', '58', 'OLE-CAT', 'INGEST_BIB_LOCAL', 'Allows user to import bib locally', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10242', 'OLE70024', '1', '58', 'OLE-CAT', 'INGEST_BIB_EXTERNAL', 'Allows user to import bib from external', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10243', 'OLE70025', '1', '1', 'OLE-CAT', 'BATCH_PROCESS_IMPORT', 'Allows user to import bib through batch process', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10244', 'OLE70026', '1', '1', 'OLE-CAT', 'BATCH_PROCESS_EXPORT', 'Allows user to export batch through batch process', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10269', '10090', '1', '10', 'OLE-SELECT', 'Initiate E-Resource Record', 'Allows user to initiate the E-Resource record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10270', '10091', '1', '16', 'OLE-SELECT', 'Edit E-Resource Record', 'Allows user to edit the E-Resource record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10271', 'OLE70052', '1', '1', 'OLE-CAT', 'BATCH_PROCESS_DELETE', 'Allows user to delete a batch through batch process', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE9001', 'OLE9001', '1', '52', 'OLE', 'Maintain KRMS Agenda', 'Allows creation and modification of agendas via the agenda editor', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10272', 'OLE70053', '1', '16', 'OLE-SELECT', 'Edit Item Availability Maintenance Document', 'Edit Item Availability Maintenance Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10273', 'OLE70054', '1', '62', 'OLE-CAT', 'INSTANCE_EDITOR_DELETE_EINSTANCE', 'User can delete an eHoldings record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10274', 'OLE70055', '1', '62', 'OLE-CAT', 'MARC_EDITOR_ADD_EINSTANCE', 'User can add an eholding record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10275', 'OLE70056', '1', '62', 'OLE-CAT', 'INSTANCE_EDITOR_EDIT_EINSTANCE', 'User can edit an eholding record', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10276', 'OLE70057', '1', '42', 'OLE-PTRN', 'Initiate Patron Document', 'Authorizes users to Create Patron Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10277', 'OLE70058', '1', '42', 'OLE-PTRN', 'Edit Patron Document', 'Authorizes users to Edit Patron Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10278', 'OLE70059', '1', '62', 'OLE-CAT', 'INSTANCE_EDITOR_EINSTANCE_RESTORE_OLE-DEFAULTS', 'Authorizes users to restore ole defaults', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10279', 'OLE70060', '1', '16', 'OLE-SELECT', 'Edit Serial Receiving Document', 'Authorizes users to Edit Serial Receiving Document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10280', 'OLE70061', '1', '1', 'OLE-SELECT', 'Link Existing Bib', 'Allows users to link existing bib to requisition', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10281', 'OLE70062', '1', '4', 'OLE-SELECT', 'Blanket Approve CM Document', 'Allows User to blanket approve a CM document', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10282', 'OLE70063', '1', '59', 'OLE-PTRN', 'Update patron overview', 'User can update Barcode Borrower Type Upload Image etc. ', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10283', 'OLE70064', '1', '59', 'OLE-PTRN', 'Update patron name only', 'User can update patron name', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10284', 'OLE70065', '1', '59', 'OLE-PTRN', 'Update patron phone only', 'User can update patron phone type  phone number extension number details etc. ', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10285', 'OLE70066', '1', '59', 'OLE-PTRN', 'Update patron email only', 'User can update patron email address', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10286', 'OLE70067', '1', '59', 'OLE-PTRN', 'Update patron affiliation', 'User can update patron affiliation details', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10287', 'OLE70068', '1', '59', 'OLE-PTRN', 'Update patron library policies', 'User can update library policies', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10288', 'OLE70069', '1', '58', 'OLE-PTRN', 'Display Loaned records', 'User can view loaned records', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10289', 'OLE70070', '1', '58', 'OLE-PTRN', 'Display Temporary Circulation History records', 'User can view temporary circulation history records', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10290', 'OLE70071', '1', '59', 'OLE-PTRN', 'Update Patron Notes', 'User can update patron notes.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10291', 'OLE70072', '1', '58', 'OLE-PTRN', 'Display Proxy Patron', 'User can view proxy patron.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10292', 'OLE70073', '1', '59', 'OLE-PTRN', 'Update Patron Local Id', 'User can update patron''s local identification.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10293', 'OLE70074', '1', '59', 'OLE-PTRN', 'Display InvalidOrLost Barcode', 'User can view invalid or lost barcode.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10294', 'OLE70075', '1', '59', 'OLE-CAT', 'EDITOR_HOLDINGS_EXTENT_OF_OWNERSHIP', 'User can update extent of ownership', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10295', 'OLE70076', '1', '10', 'OLE-CAT', 'Initiate Batch Process Profile', 'User can initiate batch process profile', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10296', 'OLE70077', '1', '16', 'OLE-CAT', 'Edit Batch Process Profile', 'User can initiate batch process profile', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10298', 'OLE70079', '1', '1', 'OLE-DLVR', 'Patron record expired', 'Allows users to override if patron record is expired.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10299', 'OLE70080', '1', '1', 'OLE-DLVR', 'Proxy patron record expired', 'Allows users to override if proxy patron record is expired.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10300', 'OLE70081', '1', '1', 'OLE-DLVR', 'Patron has a general block', 'Allows users to override if a patron record has a general block.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10301', 'OLE70082', '1', '1', 'OLE-DLVR', 'Patron has max # of items checked out', 'Allows users to override if a patron has checked out maximum number of items.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10302', 'OLE70083', '1', '1', 'OLE-DLVR', 'Patron has max # of checked out items marked ''claimed returned''', 'Allows users to override if a patron has checked out maximum number of items marked ''claimed returned''.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10303', 'OLE70084', '1', '1', 'OLE-DLVR', 'Patron has max amount of overdue fines', 'Allows users to override if a patron has maximum amount of overdue fines.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10304', 'OLE70085', '1', '1', 'OLE-DLVR', 'Patron has max amount of replacement fees', 'Allows users to override if a patron has maximum amount of replacement fees.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10305', 'OLE70086', '1', '1', 'OLE-DLVR', 'Patron has max amount of all charges', 'Allows users to override if a patron has maximum amount of all charges.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10306', 'OLE70087', '1', '1', 'OLE-DLVR', 'Patron has max # of overdue items checked out', 'Allows users to override if a patron has checked out maximum number of overdue items.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10307', 'OLE70088', '1', '1', 'OLE-DLVR', 'Patron has max # of recalled overdue items checked out', 'Allows users to override if a patron has checked out maximum number of recalled overdue items.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10308', 'OLE70089', '1', '1', 'OLE-DLVR', 'Item on hold for another patron', 'Allows users to override if an item is on hold for another patron.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10309', 'OLE70090', '1', '1', 'OLE-DLVR', 'Renewal limit reached', 'Allows users to override if renewal has reached its maximum.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10311', 'OLE70092', '1', '1', 'OLE-DLVR', 'Patron has at least one item overdue for more than n days', 'Allows users to override if patron has at least one item overdue for more than n days', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10312', 'OLE70093', '1', '1', 'OLE-DLVR', 'Patron has at least one recalled item overdue for more than n days', 'Allows users to override if patron has at least one recalled item overdue for more than n days', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10313', 'OLE70094', '1', '1', 'OLE-DLVR', 'Item currently loaned to another patron', 'Allows users to override if an item is currently loaned to another patron', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10314', 'OLE70095', '1', '1', 'OLE-DLVR', 'Item has pending request queue where patron presenting item is NOT the patron in queue position ''1''', 'Allows users to override if patron presenting item is NOT the patron in queue position ''1''', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10315', 'OLE70096', '1', '1', 'OLE-DLVR', 'Item not at circ desk authorized to service the items shelving location', 'Allows users to override if an item is not at a circ desk authorized to service the items shelving location', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10316', 'OLE70097', '1', '58', 'OLE-CAT', 'Global Edit of Holdings/Items/EHoldings', 'Allows users to edit multiple OLE Instances by making the same changes to all affected OLE Instances', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10317', 'OLE70098', '1', '1', 'OLE-CAT', 'Create Analytics', 'Allows users to Create ''analytics'' links between a series holdings record and one or more analytics holdings records.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10318', 'OLE70099', '1', '66', 'OLE-SELECT', 'Edit Enumeration Main Receipt History', 'Allows user to edit Enumeration field in Main Receipt History in serial receiving record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10319', 'OLE70100', '1', '66', 'OLE-SELECT', 'Edit Enumeration Supplementary Receipt History', 'Allows user to edit Enumeration field in Supplementary Receipt History in serial receiving record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10320', 'OLE70101', '1', '66', 'OLE-SELECT', 'Edit Enumeration Index Receipt History', 'Allows user to edit Enumeration field in Index Receipt History in serial receiving record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10321', 'OLE70102', '1', '66', 'OLE-SELECT', 'Edit Chronology Main Receipt History', 'Allows user to edit Chronology field in Main Receipt History in serial receiving record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10322', 'OLE70103', '1', '66', 'OLE-SELECT', 'Edit Chronology Supplementary Receipt History', 'Allows user to edit Chronology field in Supplementary Receipt History in serial receiving record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10323', 'OLE70104', '1', '66', 'OLE-SELECT', 'Edit Chronology Index Receipt History', 'Allows user to edit Chronology field in Index Receipt History in serial receiving record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10324', 'OLE70105', '1', '1', 'OLE-DLVR', 'Item contains the pending request', 'Allows users to override if item contains pending request.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10325', 'OLE70106', '1', '62', 'OLE-PTRN', 'Perform Fast Add Item Operation', 'Allows users to perform fast add item operation', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10326', 'OLE10326', '1', '10', 'OLE-SELECT', 'Initiate Platform Record', 'Allows user to initiate the Platform record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10327', 'OLE10327', '1', '16', 'OLE-SELECT', 'Edit Platform Record', 'Allows user to edit the Platform record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10328', 'OLE10328', '1', '62', 'OLE-SELECT', 'Delete E-Resource Record', 'Allows user to delete the E-Resource record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10329', 'OLE10329', '1', '62', 'OLE-SELECT', 'Delete E-Resource Instance', 'Allows user to delete the selected Instances under E-Resource record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10330', 'OLE10330', '1', '10', 'OLE-SYS', 'Initiate and Edit Fund Code', 'Authorizes the initiation and edit of Fund code Simple Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10500', 'OLE10500', '1', '15', 'OLE-SELECT', 'Save E-Resource Record', 'Allows user to Save the E-Resource record.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10501', 'OLE10501', '1', '10', 'OLE-SELECT', 'Initiate and Edit Problem Type', 'Authorizes the initiation and edit of Problem type Simple Maintenance documents.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10502', 'OLE10502', '1', '62', 'OLE-PTRN', 'Perform Credit Transfer', 'The posted amount will be credited from the bill amount or refund from the bill amount.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10503', 'OLE10503', '1', '62', 'OLE-PTRN', 'Perforn Debit Transfer', 'The posted amount will be transferred from the bill amount or refund from the bill amount.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10504', 'OLE10504', '1', '62', 'OLE-PTRN', 'Perform credit Payment', 'The posted amount will be credited from the bill amount or refund from the bill amount.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10505', 'OLE10505', '1', '62', 'OLE-PTRN', 'Perform refund', 'The posted amount will be transferred from the bill amount or refund from the bill amount.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10506', 'OLE10506', '1', '62', 'OLE-PTRN', 'Perform accept fine', 'The posted amount will be credited from the bill amount or refund from the bill amount.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE3010502', 'OLE3010502', '1', '1', 'OLE-CAT', 'MARC_EDITOR_COPY_BIB', 'user to Copy a bibliographic record will be created', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_PERM_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 15, '7:6fe93f1f17ab9abf8666350289712bf3', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_KRIM_PERM_ATTR_DATA_T::ole
INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10004', 'OLE10004', '1', 'OLE10006', '8', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10025', 'OLE10025', '1', 'OLE10006', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10005', 'OLE10005', '1', 'OLE10007', '4', '14', 'A')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10006', 'OLE10006', '1', 'OLE10009', '8', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10007', 'OLE10007', '2', 'OLE10009', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10008', 'OLE10008', '1', 'OLE10010', '4', '14', 'A')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10009', 'OLE10009', '1', 'OLE10011', '8', '13', 'OLE_POA')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10010', 'OLE10010', '1', 'OLE10012', '8', '13', 'OLE_POA')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10011', 'OLE10011', '1', 'OLE10014', '56', '13', 'OLE_POA')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10012', 'OLE10012', '1', 'OLE10015', '56', '13', 'OLE_PVEN')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10013', 'OLE10013', '1', 'OLE10016', '4', '14', 'A')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10014', 'OLE10014', '1', 'OLE10017', '8', '13', 'OLE_PVEN')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10015', 'OLE10015', '1', 'OLE10018', '56', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10016', 'OLE10016', '1', 'OLE10019', '8', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10017', 'OLE10017', '1', 'OLE10020', '4', '14', 'A')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10018', 'OLE10018', '2', 'OLE10021', '11', '6', 'vendorDiscountPercentage')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10019', 'OLE10019', '1', 'OLE10021', '11', '5', 'VendorCustomerNumber')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10020', 'OLE10020', '1', 'OLE10028', '4', '14', 'F')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10021', 'OLE10021', '1', 'OLE10029', '4', '14', 'F')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10023', 'OLE10023', '1', 'OLE10035', '10', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10024', 'OLE10024', '1', 'OLE10036', '3', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10026', 'OLE10026', '1', 'OLE10041', '3', '13', 'OLE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10027', 'OLE10027', '1', 'OLE10042', '56', '13', 'OLE_PVEN')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10028', 'OLE10028', '2', 'OLE10042', '56', '7', 'FALSE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10029', 'OLE10029', '2', 'OLE10043', '56', '13', 'OLE_PVEN')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10030', 'OLE10030', '1', 'OLE10043', '56', '7', 'TRUE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10031', 'OLE10031', '6', 'OLE10004', '10', '4', 'OLE*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10032', 'OLE10032', '6', 'OLE10005', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10033', 'OLE10033', '3', 'OLE10005', '8', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10034', 'OLE10034', '3', 'OLE10005', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10035', 'OLE10035', '1', 'OLE10044', '3', '13', 'OLE_RCVL')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10036', 'OLE10036', '1', 'OLE10045', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10037', 'OLE10037', '1', 'OLE10045', '8', '13', 'OLE_RCVL')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10038', 'OLE10038', '1', 'OLE10046', '3', '13', 'OLE_RCVL')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10039', 'OLE10039', '1', 'OLE10047', '3', '13', 'OLE_RCVC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10040', 'OLE10040', '1', 'OLE10048', '3', '13', 'OLE_RCVC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10041', 'OLE10041', '1', 'OLE10049', '3', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10042', 'OLE10042', '1', 'OLE10050', '8', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10043', 'OLE10043', '1', 'OLE10050', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10044', 'OLE10044', '1', 'OLE10051', '8', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10045', 'OLE10045', '1', 'OLE10051', '8', '15', 'P')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10046', 'OLE10046', '1', 'OLE10052', '8', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10047', 'OLE10047', '1', 'OLE10052', '8', '15', 'F')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10048', 'OLE10048', '1', 'OLE10053', '52', '6', 'items.sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10049', 'OLE10049', '1', 'OLE10053', '52', '16', 'Account')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10050', 'OLE10050', '1', 'OLE10053', '52', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10051', 'OLE10051', '1', 'OLE10054', '14', '10', 'requestPaymentRequestCancel')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10052', 'OLE10052', '1', 'OLE10054', '14', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10053', 'OLE10053', '1', 'OLE10055', '14', '13', 'OLE_RCVL')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10054', 'OLE10054', '1', 'OLE10056', '8', '13', 'OLE_POC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10055', 'OLE10055', '1', 'OLE10057', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10056', 'OLE10056', '1', 'OLE10057', '8', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10060', 'OLE10060', '2', 'OLE10060', '8', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10061', 'OLE10061', '2', 'OLE10061', '8', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10062', 'OLE10062', '2', 'OLE10062', '8', '13', 'OLE_POA')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10063', 'OLE10063', '2', 'OLE10063', '8', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10064', 'OLE10064', '1', 'OLE10064', '8', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10065', 'OLE10065', '1', 'OLE10065', '8', '13', 'OLE_POV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10066', 'OLE10066', '1', 'OLE10066', '8', '13', 'OLE_POSP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10067', 'OLE10067', '1', 'OLE10067', '8', '13', 'OLE_POR')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10068', 'OLE10068', '2', 'OLE10068', '12', '2', 'org.kuali.ole.module.purap.document.web.struts.PrintAction')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10069', 'OLE10069', '2', 'OLE10069', '14', '10', 'printPurchaseOrder')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10070', 'OLE10070', '3', 'OLE10069', '14', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10071', 'OLE10071', '2', 'OLE10070', '14', '10', 'previewPrintPurchaseOrder')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10072', 'OLE10072', '3', 'OLE10070', '14', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10073', 'OLE10073', '1', 'OLE10068', '8', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10074', 'OLE10074', '1', 'OLE10071', '8', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10075', 'OLE10075', '1', 'OLE10100', '15', '1', 'ordInputFileType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10076', 'OLE10076', '1', 'OLE10108', '8', '13', 'OLE_LOADSUM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10077', 'OLE10077', '1', 'OLE10109', '8', '13', 'OLE_LOADSUM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10078', 'OLE10078', '1', 'OLE10110', '8', '13', 'OLE_PO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10079', 'OLE10079', '1', 'OLE10111', '8', '13', 'OLE_POA')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10082', 'OLE10082', '1', 'OLE10115', '3', '13', 'OLE_ORDQU')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10083', 'OLE10083', '1', 'OLE10116', '3', '13', 'OLE_RCV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10084', 'OLE10084', '1', 'OLE10117', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10085', 'OLE10085', '1', 'OLE10117', '8', '13', 'OLE_RCV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10086', 'OLE10086', '1', 'OLE10118', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10087', 'OLE10087', '1', 'OLE10118', '8', '13', 'RCG_QUEUESEARCH')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10088', 'OLE10088', '1', 'OLE10119', '3', '13', 'RCG_QUEUESEARCH')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10089', 'OLE10089', '1', 'OLE10121', '3', '13', 'OLE_PVEN')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10090', 'OLE10090', '1', 'OLE10122', '3', '13', 'OLE_POA')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80138', 'OLE80138', '1', 'OLE80157', '3', '13', 'OLE_DI')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80139', 'OLE80139', '1', 'OLE80158', '3', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80140', 'OLE80140', '1', 'OLE80159', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80141', 'OLE80141', '1', 'OLE80159', '8', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80142', 'OLE80142', '1', 'OLE80160', '8', '13', 'OLE_DV')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80143', 'OLE80143', '1', 'OLE80160', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80144', 'OLE80144', '1', 'OLE80161', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80145', 'OLE80145', '1', 'OLE80161', '8', '13', 'OLE_CM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80146', 'OLE80146', '1', 'OLE80162', '3', '13', 'OLE_GEC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80147', 'OLE80147', '1', 'OLE80164', '3', '13', 'OLE_BA')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80148', 'OLE80148', '1', 'OLE80165', '8', '13', 'OLE_BA')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80149', 'OLE80149', '1', 'OLE80166', '3', '13', 'OLE_TF')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80150', 'OLE80150', '1', 'OLE80167', '8', '13', 'OLE_TF')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80151', 'OLE80151', '1', 'OLE80168', '3', '13', 'OLE_AD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80152', 'OLE80152', '1', 'OLE80169', '8', '13', 'OLE_AD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80314', 'OLE80314', '1', 'OLE80511', '3', '13', 'OLEPurchasingAccountsPayableSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80315', 'OLE80315', '1', 'OLE80512', '8', '13', 'OLEPurchasingAccountsPayableSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80316', 'OLE80316', '1', 'OLE80512', '8', '16', 'AdHoc')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80317', 'OLE80317', '1', 'OLE80512', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80318', 'OLE80318', '1', 'OLE80513', '3', '13', 'OLEChartSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80319', 'OLE80319', '1', 'OLE80514', '8', '13', 'OLEChartSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80320', 'OLE80320', '1', 'OLE80514', '8', '16', 'AdHoc')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80321', 'OLE80321', '1', 'OLE80514', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80322', 'OLE80322', '1', 'OLE80515', '3', '13', 'OLEReceivingSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80323', 'OLE80323', '1', 'OLE80516', '8', '13', 'OLEReceivingSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80324', 'OLE80324', '1', 'OLE80516', '8', '16', 'AdHoc')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80325', 'OLE80325', '1', 'OLE80516', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80326', 'OLE80326', '1', 'OLE80517', '3', '13', 'OLEInvoicingSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80327', 'OLE80327', '1', 'OLE80518', '8', '13', 'OLEInvoicingSimpleMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80328', 'OLE80328', '1', 'OLE80518', '8', '16', 'AdHoc')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80329', 'OLE80329', '1', 'OLE80518', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80330', 'OLE80330', '1', 'OLE80519', '3', '13', 'OLEReceivingComplexMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80331', 'OLE80331', '1', 'OLE80520', '8', '13', 'OLEReceivingComplexMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80332', 'OLE80332', '1', 'OLE80520', '8', '16', 'AdHoc')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80333', 'OLE80333', '1', 'OLE80520', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80334', 'OLE80334', '1', 'OLE80521', '3', '13', 'OLEPurchasingAccountsPayableComplexMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80335', 'OLE80335', '1', 'OLE80522', '8', '13', 'OLEPurchasingAccountsPayableComplexMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80336', 'OLE80336', '1', 'OLE80522', '8', '16', 'AdHoc')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80337', 'OLE80337', '1', 'OLE80522', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80338', 'OLE80338', '1', 'OLE80523', '3', '13', 'OLEChartComplexMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80339', 'OLE80339', '1', 'OLE80524', '8', '13', 'OLEChartComplexMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80340', 'OLE80340', '1', 'OLE80524', '8', '16', 'AdHoc')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80341', 'OLE80341', '1', 'OLE80524', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10123', 'OLE10123', '1', 'OLE10123', '3', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10124', 'OLE10124', '1', 'OLE10124', '8', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10125', 'OLE10125', '1', 'OLE10124', '8', '15', 'R')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10126', 'OLE10126', '1', 'OLE10125', '8', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10127', 'OLE10127', '1', 'OLE10125', '8', '15', 'P')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10128', 'OLE10128', '1', 'OLE10126', '8', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10129', 'OLE10129', '1', 'OLE10126', '8', '15', 'F')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10130', 'OLE10130', '1', 'OLE10127', '52', '6', 'items.sourceAccountingLines')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10131', 'OLE10131', '1', 'OLE10127', '52', '16', 'Account')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10132', 'OLE10132', '1', 'OLE10127', '52', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10133', 'OLE10133', '1', 'OLE10128', '14', '10', 'requestPaymentRequestCancel')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10134', 'OLE10134', '1', 'OLE10128', '14', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10135', 'OLE10135', '1', 'OLE10129', '8', '16', 'PreRoute')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10136', 'OLE10136', '1', 'OLE10129', '8', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10137', '1205', '1', 'OLE10130', '3', '13', 'OleBorrowerTypeMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10138', '1206', '1', 'OLE10131', '10', '4', 'KR-NS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10139', '1207', '1', 'OLE10131', '10', '5', 'OleBorrowerType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10140', '1208', '1', 'OLE10132', '10', '4', 'KR-NS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10141', '1209', '1', 'OLE10132', '10', '5', 'OleBorrowerType')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10142', '1015', '1', 'OLE10133', '68', '47', 'OleLocationLevel-levelName')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10143', '1016', '1', 'OLE10133', '70', '6', 'levelName')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10144', '1017', '1', 'OLE10134', '10', '5', 'OleLocationLevel')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10145', '1018', '1', 'OLE10134', '10', '4', 'OLE-PTRN')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10146', '1009', '1', 'OLE10138', '3', '13', 'OLE_ADDR_TYP_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10147', '1010', '1', 'OLE10139', '8', '13', 'OLE_ADDR_TYP_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10148', '1011', '1', 'OLE10140', '3', '13', 'OLE_EMAIL_TYP_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10149', '1012', '1', 'OLE10141', '8', '13', 'OLE_EMAIL_TYP_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10150', '1013', '1', 'OLE10142', '3', '13', 'OLE_PH_TYP_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10151', '1014', '1', 'OLE10143', '8', '13', 'OLE_PH_TYP_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10152', '1019', '1', 'OLE10144', '3', '13', 'OLE_ACSM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10153', '1020', '1', 'OLE10145', '3', '13', 'OLE_ACQM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10154', '1021', '1', 'OLE10146', '3', '13', 'OLE_ACT')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10155', '1022', '1', 'OLE10147', '3', '13', 'OLE_CMPLT')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10156', '1023', '1', 'OLE10148', '3', '13', 'OLE_CNTC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10157', '1024', '1', 'OLE10149', '3', '13', 'OLE_LSMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10158', '1025', '1', 'OLE10150', '3', '13', 'OLE_SOMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10159', '1026', '1', 'OLE10151', '3', '13', 'OLE_SSMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10160', '1027', '1', 'OLE10152', '3', '13', 'OLE_SRPTMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10161', '1028', '1', 'OLE10153', '3', '13', 'OLE_SOCRMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10162', '1029', '1', 'OLE10154', '3', '13', 'OLE_RTMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10163', '1030', '1', 'OLE10155', '3', '13', 'OLE_LPMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10164', '1032', '1', 'OLE10157', '3', '13', 'OLE_SRPTYMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10165', '1033', '1', 'OLE10158', '3', '13', 'OLE_SOTMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10166', '1034', '1', 'OLE10159', '3', '13', 'OLE_NTMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10167', '1035', '1', 'OLE10160', '3', '13', 'OLE_PMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10168', '1036', '1', 'OLE10161', '3', '13', 'OLE_RSMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10169', '1037', '1', 'OLE10162', '3', '13', 'OLE_ELARD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10170', '1038', '1', 'OLE10163', '3', '13', 'OLE_ELD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10171', '1039', '1', 'OLE10164', '3', '13', 'OLE_FELD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10172', '1040', '1', 'OLE10165', '3', '13', 'OLE_GRPD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10173', '1041', '1', 'OLE10166', '3', '13', 'OLE_STSRC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10174', '1042', '1', 'OLE10167', '3', '13', 'OLE_TYPO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10175', '1043', '1', 'OLE10168', '3', '13', 'OLE_RPMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10176', 'OLE1020', '1', 'OLE10169', '3', '13', 'OLE_CHKLST')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10177', 'OLE1021', '1', 'OLE10170', '8', '13', 'OLE_CHKLST')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10178', 'OLE1022', '1', 'OLE10171', '3', '13', 'OLE_LRS_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10179', 'OLE1023', '1', 'OLE10172', '8', '13', 'OLE_LRS_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10180', 'OLE1024', '1', 'OLE10173', '3', '13', 'OLE_CUR_LOC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10181', 'OLE1025', '1', 'OLE10174', '8', '13', 'OLE_CUR_LOC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10182', 'OLE1026', '1', 'OLE10175', '3', '13', 'OLE_LIC_REQS_TYP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10183', 'OLE1027', '1', 'OLE10176', '8', '13', 'OLE_LIC_REQS_TYP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10184', 'OLE1028', '1', 'OLE10177', '3', '13', 'OLE_AGR_DOC_TYP_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10185', 'OLE1029', '1', 'OLE10178', '8', '13', 'OLE_AGR_DOC_TYP_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10186', 'OLE1030', '1', 'OLE10179', '3', '13', 'OLE_AGR_MTH_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10187', 'OLE1031', '1', 'OLE10180', '8', '13', 'OLE_AGR_MTH_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10188', 'OLE1032', '1', 'OLE10181', '3', '13', 'OLE_AGR_STAT_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10189', 'OLE1033', '1', 'OLE10182', '8', '13', 'OLE_AGR_STAT_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10190', 'OLE1034', '1', 'OLE10183', '3', '13', 'OLE_AGR_TYP_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10191', 'OLE1035', '1', 'OLE10184', '8', '13', 'OLE_AGR_TYP_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10192', 'OLE1036', '1', 'OLE10185', '3', '13', 'LicenseRequestDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10194', 'OLE1037', '1', 'OLE10190', '3', '13', 'LicenseRequestDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10195', 'OLE1038', '1', 'OLE10191', '3', '13', 'LicenseRequestDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10196', '1031', '1', 'OLE10156', '3', '13', 'OLE_IITMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KR1000', 'D0C2892F47EC9142E0406E0AC31D23D6', '1', 'KR1000', '4', '14', 'C')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRLLMD1', 'ED104', '1', 'KRLLMD1', '3', '13', 'OLE_LLMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRLMD1', 'ED102', '1', 'KRLMD1', '56', '13', 'OLE_LMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRLMD2', 'ED103', '1', 'KRLMD2', '56', '13', 'OLE_LMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP882', 'CDD8032915D7BF1FE040F90A05B94313', '1', 'KRSAP10003', '71', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP883', 'CDD8032915D8BF1FE040F90A05B94313', '1', 'KRSAP10003', '71', '51', 'UifCompView-SecureGroupView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP884', 'CDD8032915DBBF1FE040F90A05B94313', '1', 'KRSAP10004', '71', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP885', 'CDD8032915DCBF1FE040F90A05B94313', '1', 'KRSAP10004', '71', '51', 'UifCompView-Page9')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP886', 'CDD8032915DFBF1FE040F90A05B94313', '1', 'KRSAP10005', '71', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP887', 'CDD8032915E0BF1FE040F90A05B94313', '1', 'KRSAP10005', '71', '51', 'UifCompView-SecureGroupEdit')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP888', 'CDD8032915E3BF1FE040F90A05B94313', '1', 'KRSAP10006', '70', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP889', 'CDD8032915E4BF1FE040F90A05B94313', '1', 'KRSAP10006', '70', '6', 'field6')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP890', 'CDD8032915E7BF1FE040F90A05B94313', '1', 'KRSAP10007', '70', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP891', 'CDD8032915E8BF1FE040F90A05B94313', '1', 'KRSAP10007', '70', '6', 'field7')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP892', 'CDD8032915EBBF1FE040F90A05B94313', '1', 'KRSAP10008', '70', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP893', 'CDD8032915ECBF1FE040F90A05B94313', '1', 'KRSAP10008', '70', '50', 'UifCompView-SecureFieldGroup1')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP894', 'CDD8032915EFBF1FE040F90A05B94313', '1', 'KRSAP10009', '70', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP895', 'CDD8032915F0BF1FE040F90A05B94313', '1', 'KRSAP10009', '70', '50', 'UifCompView-SecureFieldGroup2')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP896', 'CDD8032915F3BF1FE040F90A05B94313', '1', 'KRSAP10010', '73', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP897', 'CDD8032915F4BF1FE040F90A05B94313', '1', 'KRSAP10010', '73', '48', 'save')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP898', 'CDD8032915F7BF1FE040F90A05B94313', '1', 'KRSAP10011', '72', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP899', 'CDD8032915F8BF1FE040F90A05B94313', '1', 'KRSAP10011', '72', '52', 'UifCompView-SecureWidget')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP900', 'CDD8032915FBBF1FE040F90A05B94313', '1', 'KRSAP10012', '71', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP901', 'CDD8032915FCBF1FE040F90A05B94313', '1', 'KRSAP10012', '71', '49', 'list1')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP902', 'CDD8032915FFBF1FE040F90A05B94313', '1', 'KRSAP10013', '71', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP903', 'CDD803291600BF1FE040F90A05B94313', '1', 'KRSAP10013', '71', '49', 'list2')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP904', 'CDD803291603BF1FE040F90A05B94313', '1', 'KRSAP10014', '74', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP905', 'CDD803291604BF1FE040F90A05B94313', '1', 'KRSAP10014', '74', '49', 'list3')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP906', 'CDD803291605BF1FE040F90A05B94313', '1', 'KRSAP10014', '74', '6', 'field2')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP907', 'CDD803291608BF1FE040F90A05B94313', '1', 'KRSAP10015', '74', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP908', 'CDD803291609BF1FE040F90A05B94313', '1', 'KRSAP10015', '74', '49', 'list3')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP909', 'CDD80329160ABF1FE040F90A05B94313', '1', 'KRSAP10015', '74', '6', 'field3')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP910', 'CDD80329160DBF1FE040F90A05B94313', '1', 'KRSAP10016', '75', '47', 'UifCompView*')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP911', 'CDD80329160EBF1FE040F90A05B94313', '1', 'KRSAP10016', '75', '49', 'list4')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('KRSAP912', 'CDD80329160FBF1FE040F90A05B94313', '1', 'KRSAP10016', '75', '48', 'delete')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEIITMD', 'ED1012', '1', 'OLEIITMD', '56', '13', 'OLE_IITMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLEITAS', 'ED1013', '1', 'OLEITAS', '3', '13', 'OLE_ITAS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLERSMD', 'ED1014', '1', 'OLERSMD', '56', '13', 'OLE_RSMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLESSMD', 'ED1011', '1', 'OLESSMD', '56', '13', 'OLE_SSMD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLESTSRC', 'ED1015', '1', 'OLESTSRC', '56', '13', 'OLE_STSRC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLETYPO', 'ED1016', '1', 'OLETYPO', '56', '13', 'OLE_TYPO')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10197', '10065', '1', 'OLE10193', '71', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10198', '10066', '1', 'OLE10193', '71', '51', 'OLESerialReceivingTab')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10201', 'OLE9069', '1', 'OLE10195', '73', '47', 'PatronItemView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10202', 'OLE9070', '1', 'OLE10195', '73', '48', 'openFastAdd')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10203', 'OLE9071', '1', 'OLE10196', '56', '13', 'OLE_CIRC_DESK_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10204', 'OLE9072', '1', 'OLE10197', '71', '47', 'PatronItemView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10205', 'OLE9073', '1', 'OLE10197', '71', '51', 'Patron-ExistingLoanItemListSection-HorizontalBoxSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10206', 'OLE9074', '1', 'OLE10198', '73', '47', 'PatronItemView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10207', 'OLE9075', '1', 'OLE10198', '73', '48', 'editDueDate')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10208', 'OLE9076', '1', 'OLE10199', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10209', 'OLE9077', '1', 'OLE10199', '71', '51', 'OlePatronDocument-PatronRequestedRecords')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10210', 'OLE9078', '1', 'OLE10200', '71', '47', 'DeliverRequestSearch')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10211', 'OLE9079', '1', 'OLE10200', '71', '51', 'DeliverRequestSearch-buttons')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10212', 'OLE9080', '1', 'OLE10201', '73', '47', 'PatronItemView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10213', 'OLE9081', '1', 'OLE10201', '73', '48', 'loan')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10214', 'OLE9082', '1', 'OLE10202', '73', '47', 'BillView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10215', 'OLE9083', '1', 'OLE10202', '73', '48', 'accept')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10216', 'OLE9084', '1', 'OLE10203', '73', '47', 'BillView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10217', 'OLE9085', '1', 'OLE10203', '73', '48', 'payment')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10220', 'OLE9088', '1', 'OLE10205', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10221', 'OLE9089', '1', 'OLE10205', '71', '51', 'OlePatronDocument-ProxySection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10222', 'OLE9090', '1', 'OLE10206', '71', '47', 'ReturnItemView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10223', 'OLE9091', '1', 'OLE10206', '71', '51', 'CheckInDateTime-HorizontalBoxSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10224', 'OLE9092', '1', 'OLE10207', '71', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10225', 'OLE9093', '1', 'OLE10207', '71', '51', 'OleItemInformation')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10226', 'OLE9094', '1', 'OLE10208', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10227', 'OLE9095', '1', 'OLE10208', '71', '51', 'OlePatronDocument-Address')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10228', 'OLE9096', '1', 'OLE10209', '71', '47', 'PatronItemView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10229', 'OLE9097', '1', 'OLE10209', '71', '51', 'Patron-LoanItemListSection-HorizontalBoxSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10231', 'OLE9099', '1', 'OLE10214', '56', '13', 'OLE_DLVR_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10240', 'OLE1509', '1', 'OLE10227', '73', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10241', 'OLE1510', '1', 'OLE10227', '73', '48', 'deleteVerify')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10242', 'OLE1511', '1', 'OLE10228', '70', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10243', 'OLE1512', '1', 'OLE10228', '70', '50', 'StatusFieldSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10244', 'OLE1520', '1', 'OLE10241', '71', '47', 'ImportBibView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10245', 'OLE1521', '1', 'OLE10241', '71', '51', 'ImportFromLocal')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10246', 'OLE1522', '1', 'OLE10242', '71', '47', 'ImportBibView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10247', 'OLE1523', '1', 'OLE10242', '71', '51', 'ImportFromExternalDataSource')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10248', 'OLE1524', '1', 'OLE10229', '71', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10249', 'OLE1525', '1', 'OLE10229', '71', '51', 'OleCallNumberInformation')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10250', 'OLE1526', '1', 'OLE10230', '71', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10251', 'OLE1527', '1', 'OLE10230', '71', '51', 'OleLocationInformationSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10252', 'OLE1528', '1', 'OLE10231', '71', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10253', 'OLE1529', '1', 'OLE10231', '71', '51', 'OleItemsLocationAndCallNumberInformation')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10254', 'OLE1532', '1', 'OLE10232', '73', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10255', 'OLE1533', '1', 'OLE10232', '73', '48', 'addInstance')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10256', 'OLE1534', '1', 'OLE10226', '73', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10257', 'OLE1535', '1', 'OLE10226', '73', '48', 'edit')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10274', 'OLE1548', '1', 'OLE10233', '73', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10275', 'OLE1549', '1', 'OLE10233', '73', '48', 'edit')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10276', 'OLE1550', '1', 'OLE10234', '73', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10277', 'OLE1551', '1', 'OLE10234', '73', '48', 'edit')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10314', '10070', '1', 'OLE10269', '3', '13', 'OLE_ERS_DOC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10315', '10071', '1', 'OLE10270', '8', '13', 'OLE_ERS_DOC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE9001', 'OLE9001', '1', 'OLE9001', '67', '4', 'KR-TEST')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10316', 'OLE1552', '1', 'OLE10272', '8', '13', 'OLE_ITAS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10317', 'OLE1553', '1', 'OLE10273', '73', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10318', 'OLE1554', '1', 'OLE10273', '73', '48', 'deleteEInstance')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10319', 'OLE1555', '1', 'OLE10274', '73', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10320', 'OLE1556', '1', 'OLE10274', '73', '48', 'addEInstance')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10321', 'OLE1557', '1', 'OLE10275', '73', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10322', 'OLE1558', '1', 'OLE10275', '73', '48', 'save')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10323', 'OLE1559', '1', 'OLE10276', '56', '13', 'OLE_PTRN')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10324', 'OLE1560', '1', 'OLE10276', '56', '7', 'FALSE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10325', 'OLE1561', '1', 'OLE10277', '56', '13', 'OLE_PTRN')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10326', 'OLE1562', '1', 'OLE10277', '56', '7', 'TRUE')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10327', 'OLE1563', '1', 'OLE10278', '73', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10328', 'OLE1564', '1', 'OLE10278', '73', '48', 'restoreOLEDefaults')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10329', 'OLE1565', '1', 'OLE10279', '8', '13', 'OLE_SER_RECV_REC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10330', 'OLE1566', '1', 'OLE80510', '3', '13', 'OLE_REQS')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10331', 'OLE1567', '1', 'OLE10281', '3', '13', 'OLE_CM')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10332', 'OLE1568', '1', 'OLE10282', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10333', 'OLE1569', '1', 'OLE10282', '71', '51', 'OlePatronDocument-OverviewSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10334', 'OLE1570', '1', 'OLE10283', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10335', 'OLE1571', '1', 'OLE10283', '71', '51', 'OlePatronDocument-Name')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10336', 'OLE1572', '1', 'OLE10284', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10337', 'OLE1573', '1', 'OLE10284', '71', '51', 'OlePatronDocument-Phone')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10338', 'OLE1574', '1', 'OLE10285', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10339', 'OLE1575', '1', 'OLE10285', '71', '51', 'OlePatronDocument-Email')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10340', 'OLE1576', '1', 'OLE10286', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10341', 'OLE1577', '1', 'OLE10286', '71', '51', 'OlePatronDocument-Affiliation')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10342', 'OLE1578', '1', 'OLE10287', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10343', 'OLE1579', '1', 'OLE10287', '71', '51', 'OlePatronDocument-LibraryPoliciesSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10344', 'OLE1580', '1', 'OLE10288', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10345', 'OLE1581', '1', 'OLE10288', '71', '51', 'OlePatronDocument-PatronLoanedRecords')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10346', 'OLE1582', '1', 'OLE10289', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10347', 'OLE1583', '1', 'OLE10289', '71', '51', 'OlePatronDocument-TemporaryCirculationHistoryRecords')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10348', 'OLE1584', '1', 'OLE10290', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10349', 'OLE1585', '1', 'OLE10290', '71', '51', 'OlePatronDocument-NotesSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10350', 'OLE1586', '1', 'OLE10291', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10351', 'OLE1587', '1', 'OLE10291', '71', '51', 'OlePatronDocument-ProxyForSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10352', 'OLE1588', '1', 'OLE10292', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10353', 'OLE1589', '1', 'OLE10292', '71', '51', 'OlePatronDocument-PatronLocalIdSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10354', 'OLE1590', '1', 'OLE10293', '71', '47', 'OlePatronDocument-MaintenanceView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10355', 'OLE1591', '1', 'OLE10293', '71', '51', 'OlePatronDocument-InvalidOrLostBarcodeSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10356', 'OLE1592', '1', 'OLE10294', '71', '47', 'EditorView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10357', 'OLE1593', '1', 'OLE10294', '71', '51', 'OleExtentOfOwnershipSection')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10358', 'OLE1594', '1', 'OLE10295', '3', '13', 'OLE_BTCH_PRCS_PRFL')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10359', 'OLE1595', '1', 'OLE10296', '8', '13', 'OLE_BTCH_PRCS_PRFL')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10360', 'OLE1596', '1', 'OLE10316', '71', '47', 'GlobalEditView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10361', 'OLE1597', '1', 'OLE10316', '71', '51', 'GlobalEditView-Button-Section')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10362', 'OLE1598', '1', 'OLE10318', '74', '47', 'OLESerialReceivingView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10363', 'OLE1599', '1', 'OLE10318', '74', '49', 'mainSerialReceivingHistoryList')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10364', 'OLE1600', '1', 'OLE10318', '74', '6', 'enumerationCaption')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10365', 'OLE1601', '1', 'OLE10319', '74', '47', 'OLESerialReceivingView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10366', 'OLE1602', '1', 'OLE10319', '74', '49', 'supplementSerialReceivingHistoryList')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10367', 'OLE1603', '1', 'OLE10319', '74', '6', 'enumerationCaption')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10368', 'OLE1604', '1', 'OLE10320', '74', '47', 'OLESerialReceivingView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10369', 'OLE1605', '1', 'OLE10320', '74', '49', 'indexSerialReceivingHistoryList')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10370', 'OLE1606', '1', 'OLE10320', '74', '6', 'enumerationCaption')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10371', 'OLE1607', '1', 'OLE10321', '74', '47', 'OLESerialReceivingView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10372', 'OLE1608', '1', 'OLE10321', '74', '49', 'mainSerialReceivingHistoryList')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10373', 'OLE1609', '1', 'OLE10321', '74', '6', 'chronologyCaption')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10374', 'OLE1610', '1', 'OLE10322', '74', '47', 'OLESerialReceivingView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10375', 'OLE1611', '1', 'OLE10322', '74', '49', 'supplementSerialReceivingHistoryList')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10376', 'OLE1612', '1', 'OLE10322', '74', '6', 'chronologyCaption')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10377', 'OLE1613', '1', 'OLE10323', '74', '47', 'OLESerialReceivingView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10378', 'OLE1614', '1', 'OLE10323', '74', '49', 'indexSerialReceivingHistoryList')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10379', 'OLE1615', '1', 'OLE10323', '74', '6', 'chronologyCaption')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10380', 'OLE1616', '1', 'OLE10325', '73', '47', 'circView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10381', 'OLE1617', '1', 'OLE10325', '73', '48', 'openFastAdd')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10382', 'OLE10382', '1', 'OLE10326', '3', '13', 'OLE_PLTFRM_DOC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10383', 'OLE10383', '1', 'OLE10327', '8', '13', 'OLE_PLTFRM_DOC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10384', 'OLE10384', '1', 'OLE10328', '73', '47', 'OLEEResourceRecordView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10385', 'OLE10385', '1', 'OLE10328', '73', '48', 'delete')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10386', 'OLE10386', '1', 'OLE10329', '73', '47', 'OLEEResourceRecordView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10387', 'OLE10387', '1', 'OLE10329', '73', '48', 'deleteInstance')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10388', 'OLE10388', '1', 'OLE10330', '3', '13', 'OLE_FUND_CD_MD')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10500', 'OLE10500', '1', 'OLE10500', '8', '13', 'OLE_ERS_DOC')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10501', 'OLE10501', '1', 'OLE10501', '3', '13', 'OLE_PRBLM_TYP')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10700', 'OLE10700', '1', 'OLE10502', '73', '48', 'creditTransfer')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10701', 'OLE10701', '1', 'OLE10502', '73', '47', 'BillView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10702', 'OLE10702', '1', 'OLE10503', '73', '48', 'debitTransfer')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10703', 'OLE10703', '1', 'OLE10503', '73', '47', 'BillView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10704', 'OLE10704', '1', 'OLE10504', '73', '48', 'credit')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10705', 'OLE10705', '1', 'OLE10504', '73', '47', 'BillView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10706', 'OLE10706', '1', 'OLE10505', '73', '48', 'refund')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10707', 'OLE10707', '1', 'OLE10505', '73', '47', 'BillView')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10708', 'OLE10708', '1', 'OLE10506', '73', '48', 'accept')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10709', 'OLE10709', '1', 'OLE10506', '73', '47', 'BillView')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_PERM_ATTR_DATA_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 16, '7:85c6e74772e7493629ee2fa3cd2590c0', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_KRIM_RSP_T::ole
INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990002', 'OLE10002', '1', '1', 'OLE-SELECT', 'Review OLE_REQS ShowDuplicateRecords', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990009', 'OLE10009', '3', '1', 'OLE_SELECT', 'Review OLE_PVEN ForInformation', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990016', 'OLE10016', '1', '1', 'OLE-SELECT', 'Review OLE_REQS GreaterLineItem', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990018', 'OLE10018', '1', '1', 'OLE-SELECT', 'Review OLE_POA OrderChange', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990019', 'OLE10019', '1', '1', 'OLE-SELECT', 'Review OLE_REQS NewVendor', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990020', 'OLE990020', '1', '1', 'OLE-SELECT', 'Review OLE_RCVL ApproveReceiveLineItem', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990021', 'OLE990021', '1', '1', 'OLE-SELECT', 'Review OLE_RCVC ApproveReceiveLineItemCorrection', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990022', 'OLE990022', '1', '1', 'OLE-SELECT', 'Review OLE_PREQ Payment', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990023', 'OLE990023', '1', '1', 'OLE-SELECT', 'Review OLE_REQS NoVendor', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990024', 'OLE990024', '1', '1', 'OLE-PURAP', 'Review OLE_POA Tax', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990025', 'OLE990025', '1', '1', 'OLE-SELECT', 'Review OLE_ACQBTHUPLOAD Approval', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80004', '80004', '1', '1', 'OLE-VND', 'Review OLE_PMCC CommodityCodeApprover', 'Approving the Commodity Code Document', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80006', '80006', '3', '1', 'OLE-VND', 'Review OLE_PMCS CostSourceApprover', 'Approving the Cost Source Document', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80008', '80008', '1', '1', 'OLE-VND', 'Review OLE_PMCT ContactTypeApprover', 'Approving ContactType Document', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80009', '80009', '1', '1', 'OLE-PURAP', 'Review OLE_PMC CategoryApprover', 'Approver for Category Document', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80010', '80010', '1', '1', 'OLE-PURAP', 'Review OLE_EXCTYP ExceptionTypeApprover', 'Approver for Exception Type Document', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80012', '80012', '1', '1', 'OLE-PURAP', 'Review OLE_FTMAT FormatTypeApprover', 'Approving Format Type', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80013', '80013', '1', '1', 'OLE-VND', 'Review OLE_NOTETYP NoteTypeApprover', 'Approving Note Type Document', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80015', '80015', '1', '1', 'OLE-PURAP', 'Review OLE_PPOT PurchaseOrderTypeApprover', 'Approving OrderType Document', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80018', '80018', '1', '1', 'OLE-VND', 'Review OLE_OIS OrderItemApprover', 'Approving OrderItemStatus Document', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80019', '80019', '1', '1', 'OLE-VND', 'Review OLE_RSTMAT RequestSourceTypeApprover', 'Approving RequestSourceType Document', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80020', '80020', '1', '1', 'OLE-PURAP', 'Review OLE_PMIPS ItemPriceSourceApprover', 'Approving ItemPriceSource Document', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE80021', '80021', '1', '1', 'OLE-SELECT', 'Review OLE_ACQBTHUPLOAD ForFYI', 'For FYI', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990026', 'OLE990026', '1', '1', 'OLE-SELECT', 'Review OLE_REQS LicenseRequest', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990027', 'OLE990027', '1', '1', 'OLE-SELECT', 'Review OLE_CM PrepaidInvoiceType', 'Approving the Credit Memo Document with Prepaid Invoice Type', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990028', 'OLE990028', '1', '1', 'OLE-SELECT', 'Review OLE_CM NoPrepaidInvoiceType', 'Approving the Credit Memo Document with Invoice Type other than ''Prepaid''', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990029', 'OLE990029', '1', '1', 'OLE-SELECT', 'Review OLE_CM PaymentMethod', 'Approving the document Vendor Credit Memo containing the Payment Method', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990030', 'OLE990030', '1', '1', 'OLE-SELECT', 'Review OLE_PREQ PrepaidInvoiceType', 'Approving the Payment Request Document with Prepaid Invoice Type', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990031', 'OLE990031', '1', '1', 'OLE-SELECT', 'Review OLE_PREQ NoPrepaidInvoiceType', 'Approving the Payment  Document with Invoice Type other than ''Prepaid''', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990032', 'OLE990032', '1', '1', 'OLE-SELECT', 'Review OLE_DI FinancialAccount', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990033', 'OLE990033', '1', '1', 'OLE-SELECT', 'Review OLE_DI VendorDepositAccount', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990034', 'OLE990034', '1', '1', 'OLE-SELECT', 'Review OLE_DV ClearingAccountType', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990035', 'OLE990035', '1', '1', 'OLE-SELECT', 'Review OLE_DV Payment', 'Approving the Disbursement Document with the Payment Method', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990036', 'OLE990036', '1', '1', 'OLE-SELECT', 'Review OLE_DV InvoiceType', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990037', 'OLE990037', '1', '1', 'OLE-SELECT', 'Review OLE_GEC FinancialAccount', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990038', 'OLE990038', '1', '1', 'OLE-SELECT', 'Review OLE_GEC VendorDepositAccount', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990039', 'OLE990039', '1', '1', 'OLE-SELECT', 'Review OLE_PREQ VendorDepositAccount', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990040', 'OLE990040', '1', '1', 'OLE-SELECT', 'Review OLE_CM FiscalOfficerReview', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990041', 'OLE990041', '1', '1', 'OLE-SELECT', 'Review OLE_ACCT DepositAccount', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990042', 'OLE990042', '1', '1', 'OLE-SELECT', 'Review OLE_PREQ SeparationOfDuties', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990043', 'OLE990043', '1', '1', 'OLE-SELECT', 'Review OLE_BA BudgetAdjustment', 'For FYI', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990044', 'OLE990044', '1', '1', 'OLE-SELECT', 'Review OLE_TF TransferFund', 'For FYI', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990045', 'OLE990045', '1', '1', 'OLE-PURAP', 'Review Payment Request Budget', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990046', 'OLE990046', '1', '1', 'OLE-SELECT', 'Review Requisition Budget', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990047', 'OLE990047', '1', '1', 'OLE-SELECT', 'Review POA Budget', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990048', 'OLE990048', '1', '1', 'OLE-SELECT', 'Review REQ FYI Budget', 'For FYI', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990049', 'OLE990049', '1', '1', 'OLE-SELECT', 'Review PO FYI Budget', 'For FYI', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990050', 'OLE990050', '1', '1', 'OLE-SELECT', 'Review POA FYI Budget', 'For FYI', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990051', 'OLE990051', '1', '1', 'OLE-SELECT', 'Review PREQ FYI Budget', 'For FYI', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990052', 'OLE990052', '1', '1', 'OLE-SELECT', 'Review OLE_PRQS Payment', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990053', 'OLE990053', '1', '1', 'OLE-SELECT', 'Review OLE_PRQS PrepaidInvoiceType', 'Approving the Invoice Document with Prepaid Invoice Type', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990054', 'OLE990054', '1', '1', 'OLE-SELECT', 'Review OLE_PRQS NoPrepaidInvoiceType', 'Approving the Invoice Document with Invoice Type other than ''Prepaid''', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990055', 'OLE990055', '1', '1', 'OLE-SELECT', 'Review OLE_PRQS VendorDepositAccount', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990056', 'OLE990056', '1', '1', 'OLE-SELECT', 'Review OLE_PRQS SeparationOfDuties', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990057', 'OLE990057', '1', '1', 'OLE-PURAP', 'Review Invoice Budget', '', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990058', 'OLE990058', '1', '1', 'OLE-SELECT', 'Review PRQS FYI Budget', 'For FYI', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990059', 'OLE990059', '1', '1', 'OLE-SELECT', 'Review E-Resource Acquisition', 'This Responsibility has the Approval of E-Resource Document on Acquisition node', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990060', 'OLE990060', '1', '1', 'OLE-SELECT', 'Review E-Resource Cataloger', 'This Responsibility has the Approval of E-Resource Document on Cataloger node', 'Y')
/

INSERT INTO KRIM_RSP_T (RSP_ID, OBJ_ID, VER_NBR, RSP_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE990061', 'OLE990061', '1', '1', 'OLE-SELECT', 'Review Serial Receiving Operator', 'This Responsibility has the Approval of Serial Receiving Document on Operator node', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_RSP_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 17, '7:aa9a7d7424f59a90073d82101696be1d', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_KRIM_RSP_ATTR_DATA_T::ole
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10026', 'OLE10026', '1', 'OLE990002', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10027', 'OLE10027', '1', 'OLE990002', '7', '16', 'ShowDuplicateRecords')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10028', 'OLE10028', '1', 'OLE990002', '7', '13', 'OLE_REQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10029', 'OLE10029', '1', 'OLE990002', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10038', 'OLE10038', '3', 'OLE990009', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10039', 'OLE10039', '3', 'OLE990009', '7', '16', 'ForInformation')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10040', 'OLE10040', '3', 'OLE990009', '7', '13', 'OLE_PVEN')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10041', 'OLE10041', '3', 'OLE990009', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10057', 'OLE10057', '1', 'OLE990016', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10058', 'OLE10058', '1', 'OLE990016', '7', '16', 'GreaterLineItem')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10059', 'OLE10059', '1', 'OLE990016', '7', '13', 'OLE_REQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10060', 'OLE10060', '1', 'OLE990016', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10065', 'OLE10065', '1', 'OLE990018', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10066', 'OLE10066', '1', 'OLE990018', '7', '16', 'OrderChange')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10067', 'OLE10067', '1', 'OLE990018', '7', '13', 'OLE_POA')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10068', 'OLE10068', '1', 'OLE990018', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10069', 'OLE10069', '1', 'OLE990019', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10070', 'OLE10070', '1', 'OLE990019', '7', '16', 'NewVendor')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10071', 'OLE10071', '1', 'OLE990019', '7', '13', 'OLE_REQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10072', 'OLE10072', '1', 'OLE990019', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10073', 'OLE10073', '1', 'OLE990020', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10074', 'OLE10074', '1', 'OLE990020', '7', '16', 'ApproveReceiveLineItem')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10075', 'OLE10075', '1', 'OLE990020', '7', '13', 'OLE_RCVL')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10076', 'OLE10076', '1', 'OLE990020', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10077', 'OLE10077', '1', 'OLE990021', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10078', 'OLE10078', '1', 'OLE990021', '7', '16', 'ApproveReceiveLineItemCorrection')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10079', 'OLE10079', '1', 'OLE990021', '7', '13', 'OLE_RCVC')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10080', 'OLE10080', '1', 'OLE990021', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10081', 'OLE10081', '1', 'OLE990022', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10082', 'OLE10082', '1', 'OLE990022', '7', '16', 'Payment')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10083', 'OLE10083', '1', 'OLE990022', '7', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10084', 'OLE10084', '1', 'OLE990022', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10085', 'OLE10085', '1', 'OLE990023', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10086', 'OLE10086', '1', 'OLE990023', '7', '16', 'NoVendor')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10087', 'OLE10087', '1', 'OLE990023', '7', '13', 'OLE_REQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10088', 'OLE10088', '1', 'OLE990023', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10089', 'OLE10089', '1', 'OLE990024', '7', '16', 'Tax')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10090', 'OLE10090', '1', 'OLE990024', '7', '13', 'OLE_POA')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10091', 'OLE10091', '1', 'OLE990024', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10092', 'OLE10092', '1', 'OLE990024', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10093', 'OLE10093', '1', 'OLE990025', '7', '16', 'Approval')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10094', 'OLE10094', '1', 'OLE990025', '7', '13', 'OLE_ACQBTHUPLOAD')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10095', 'OLE10095', '1', 'OLE990025', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10096', 'OLE10096', '1', 'OLE990025', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80066', 'OLE80066', '1', 'OLE80004', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80067', 'OLE80067', '1', 'OLE80004', '7', '16', 'CommodityCodeApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80068', 'OLE80068', '1', 'OLE80004', '7', '13', 'OLE_PMCC')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80069', 'OLE80069', '1', 'OLE80004', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80070', 'OLE80070', '3', 'OLE80006', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80071', 'OLE80071', '3', 'OLE80006', '7', '16', 'CostSourceApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80072', 'OLE80072', '3', 'OLE80006', '7', '13', 'OLE_PMCS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80073', 'OLE80073', '3', 'OLE80006', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80074', 'OLE80074', '1', 'OLE80008', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80075', 'OLE80075', '1', 'OLE80008', '7', '16', 'ContactTypeApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80076', 'OLE80076', '1', 'OLE80008', '7', '13', 'OLE_PMCT')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80077', 'OLE80077', '1', 'OLE80008', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80078', 'OLE80078', '1', 'OLE80009', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80079', 'OLE80079', '1', 'OLE80009', '7', '16', 'CategoryApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80080', 'OLE80080', '1', 'OLE80009', '7', '13', 'OLE_PMC')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80081', 'OLE80081', '1', 'OLE80009', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80082', 'OLE80082', '1', 'OLE80010', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80083', 'OLE80083', '1', 'OLE80010', '7', '16', 'ExceptionTypeApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80084', 'OLE80084', '1', 'OLE80010', '7', '13', 'OLE_EXCTYP')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80085', 'OLE80085', '1', 'OLE80010', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80086', 'OLE80086', '1', 'OLE80012', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80087', 'OLE80087', '1', 'OLE80012', '7', '16', 'FormatTypeApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80088', 'OLE80088', '1', 'OLE80012', '7', '13', 'OLE_FTMAT')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80089', 'OLE80089', '1', 'OLE80012', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80090', 'OLE80090', '1', 'OLE80013', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80091', 'OLE80091', '1', 'OLE80013', '7', '16', 'NoteTypeApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80092', 'OLE80092', '1', 'OLE80013', '7', '13', 'OLE_NOTETYP')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80093', 'OLE80093', '1', 'OLE80013', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80094', 'OLE80094', '1', 'OLE80015', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80095', 'OLE80095', '1', 'OLE80015', '7', '16', 'PurchaseOrderTypeApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80096', 'OLE80096', '1', 'OLE80015', '7', '13', 'OLE_PPOT')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80097', 'OLE80097', '1', 'OLE80015', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80098', 'OLE80098', '1', 'OLE80018', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80099', 'OLE80099', '1', 'OLE80018', '7', '16', 'OrderItemApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80100', 'OLE80100', '1', 'OLE80018', '7', '13', 'OLE_OIS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80101', 'OLE80101', '1', 'OLE80018', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80102', 'OLE80102', '1', 'OLE80019', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80103', 'OLE80103', '1', 'OLE80019', '7', '16', 'RequestSourceTypeApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80104', 'OLE80104', '1', 'OLE80019', '7', '13', 'OLE_RSTMAT')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80105', 'OLE80105', '1', 'OLE80019', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80106', 'OLE80106', '1', 'OLE80020', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80107', 'OLE80107', '1', 'OLE80020', '7', '16', 'ItemPriceSourceApprover')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80108', 'OLE80108', '1', 'OLE80020', '7', '13', 'OLE_PMIPS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80109', 'OLE80109', '1', 'OLE80020', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80110', 'OLE80110', '1', 'OLE80021', '7', '16', 'ForFYI')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80111', 'OLE80111', '1', 'OLE80021', '7', '13', 'OLE_ACQBTHUPLOAD')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80112', 'OLE80112', '1', 'OLE80021', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE80113', 'OLE80113', '1', 'OLE80021', '7', '40', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10097', 'OLE10097', '1', 'OLE990026', '7', '13', 'OLE_REQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10098', 'OLE10098', '1', 'OLE990026', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10099', 'OLE10099', '1', 'OLE990026', '7', '41', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10100', 'OLE10100', '1', 'OLE990026', '7', '16', 'LicenseRequest')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10101', 'OLE10101', '1', 'OLE990027', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10102', 'OLE10102', '1', 'OLE990027', '7', '16', 'PrepaidInvoiceType')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10103', 'OLE10103', '1', 'OLE990027', '7', '13', 'OLE_CM')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10104', 'OLE10104', '1', 'OLE990027', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10105', 'OLE10105', '1', 'OLE990028', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10106', 'OLE10106', '1', 'OLE990028', '7', '16', 'NoPrepaidInvoiceType')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10107', 'OLE10107', '1', 'OLE990028', '7', '13', 'OLE_CM')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10108', 'OLE10108', '1', 'OLE990028', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10109', 'OLE10109', '1', 'OLE990029', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10110', 'OLE10110', '1', 'OLE990029', '7', '16', 'PaymentMethod')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10111', 'OLE10111', '1', 'OLE990029', '7', '13', 'OLE_CM')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10112', 'OLE10112', '1', 'OLE990029', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10113', 'OLE10113', '1', 'OLE990030', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10114', 'OLE10114', '1', 'OLE990030', '7', '16', 'PrepaidInvoiceType')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10115', 'OLE10115', '1', 'OLE990030', '7', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10116', 'OLE10116', '1', 'OLE990030', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10117', 'OLE10117', '1', 'OLE990031', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10118', 'OLE10118', '1', 'OLE990031', '7', '16', 'NoPrepaidInvoiceType')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10119', 'OLE10119', '1', 'OLE990031', '7', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10120', 'OLE10120', '1', 'OLE990031', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10121', 'OLE10121', '1', 'OLE990032', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10122', 'OLE10122', '1', 'OLE990032', '7', '16', 'FinancialAccount')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10123', 'OLE10123', '1', 'OLE990032', '7', '13', 'OLE_DI')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10124', 'OLE10124', '1', 'OLE990032', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10125', 'OLE10125', '1', 'OLE990033', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10126', 'OLE10126', '1', 'OLE990033', '7', '16', 'VendorDepositAccount')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10127', 'OLE10127', '1', 'OLE990033', '7', '13', 'OLE_DI')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10128', 'OLE10128', '1', 'OLE990033', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10129', 'OLE10129', '1', 'OLE990034', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10130', 'OLE10130', '1', 'OLE990034', '7', '16', 'ClearingAccountType')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10131', 'OLE10131', '1', 'OLE990034', '7', '13', 'OLE_DV')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10132', 'OLE10132', '1', 'OLE990034', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10133', 'OLE10133', '1', 'OLE990035', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10134', 'OLE10134', '1', 'OLE990035', '7', '16', 'Payment')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10135', 'OLE10135', '1', 'OLE990035', '7', '13', 'OLE_DV')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10136', 'OLE10136', '1', 'OLE990035', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10137', 'OLE10137', '1', 'OLE990036', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10138', 'OLE10138', '1', 'OLE990036', '7', '16', 'InvoiceType')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10139', 'OLE10139', '1', 'OLE990036', '7', '13', 'OLE_DV')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10140', 'OLE10140', '1', 'OLE990036', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10141', 'OLE10141', '1', 'OLE990037', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10142', 'OLE10142', '1', 'OLE990037', '7', '16', 'FinancialAccount')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10143', 'OLE10143', '1', 'OLE990037', '7', '13', 'OLE_GEC')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10144', 'OLE10144', '1', 'OLE990037', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10145', 'OLE10145', '1', 'OLE990038', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10146', 'OLE10146', '1', 'OLE990038', '7', '16', 'VendorDepositAccount')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10147', 'OLE10147', '1', 'OLE990038', '7', '13', 'OLE_GEC')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10148', 'OLE10148', '1', 'OLE990038', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10149', 'OLE10149', '1', 'OLE990039', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10150', 'OLE10150', '1', 'OLE990039', '7', '16', 'VendorDepositAccount')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10151', 'OLE10151', '1', 'OLE990039', '7', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10152', 'OLE10152', '1', 'OLE990039', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10153', 'OLE10153', '1', 'OLE990040', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10154', 'OLE10154', '1', 'OLE990040', '7', '16', 'FiscalOfficerReview')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10155', 'OLE10155', '1', 'OLE990040', '7', '13', 'OLE_CM')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10156', 'OLE10156', '1', 'OLE990040', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10157', 'OLE10157', '1', 'OLE990041', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10158', 'OLE10158', '1', 'OLE990041', '7', '16', 'DepositAccount')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10159', 'OLE10159', '1', 'OLE990041', '7', '13', 'OLE_ACCT')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10160', 'OLE10160', '1', 'OLE990041', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10161', 'OLE10161', '1', 'OLE990042', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10162', 'OLE10162', '1', 'OLE990042', '7', '16', 'SeparationOfDuties')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10163', 'OLE10163', '1', 'OLE990042', '7', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10164', 'OLE10164', '1', 'OLE990042', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10165', 'OLE10165', '1', 'OLE990043', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10166', 'OLE10166', '1', 'OLE990043', '7', '16', 'BudgetAdjustment')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10167', 'OLE10167', '1', 'OLE990043', '7', '13', 'OLE_BA')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10168', 'OLE10168', '1', 'OLE990043', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10169', 'OLE10169', '1', 'OLE990044', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10170', 'OLE10170', '1', 'OLE990044', '7', '16', 'TransferFund')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10171', 'OLE10171', '1', 'OLE990044', '7', '13', 'OLE_TF')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10172', 'OLE10172', '1', 'OLE990044', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10173', 'OLE10173', '1', 'OLE990045', '7', '16', 'Budget')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10174', 'OLE10174', '1', 'OLE990045', '7', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10175', 'OLE10175', '1', 'OLE990045', '7', '40', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10176', 'OLE10176', '1', 'OLE990045', '7', '41', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10177', 'OLE10177', '1', 'OLE990046', '7', '16', 'Budget')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10178', 'OLE10178', '1', 'OLE990046', '7', '13', 'OLE_REQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10179', 'OLE10179', '1', 'OLE990046', '7', '40', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10180', 'OLE10180', '1', 'OLE990046', '7', '41', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10181', 'OLE10181', '1', 'OLE990047', '7', '16', 'Budget')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10182', 'OLE10182', '1', 'OLE990047', '7', '13', 'OLE_POA')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10183', 'OLE10183', '1', 'OLE990047', '7', '40', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10184', 'OLE10184', '1', 'OLE990047', '7', '41', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10185', 'OLE10185', '1', 'OLE990048', '7', '16', 'FYIBudget')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10186', 'OLE10186', '1', 'OLE990048', '7', '13', 'OLE_REQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10187', 'OLE10187', '1', 'OLE990048', '7', '40', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10188', 'OLE10188', '1', 'OLE990048', '7', '41', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10189', 'OLE10189', '1', 'OLE990049', '7', '16', 'FYIBudget')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10190', 'OLE10190', '1', 'OLE990049', '7', '13', 'OLE_PO')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10191', 'OLE10191', '1', 'OLE990049', '7', '40', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10192', 'OLE10192', '1', 'OLE990049', '7', '41', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10193', 'OLE10193', '1', 'OLE990050', '7', '16', 'FYIBudget')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10194', 'OLE10194', '1', 'OLE990050', '7', '13', 'OLE_POA')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10195', 'OLE10195', '1', 'OLE990050', '7', '40', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10196', 'OLE10196', '1', 'OLE990050', '7', '41', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10197', 'OLE10197', '1', 'OLE990051', '7', '16', 'FYIBudget')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10198', 'OLE10198', '1', 'OLE990051', '7', '13', 'OLE_PREQ')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10199', 'OLE10199', '1', 'OLE990051', '7', '40', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10200', 'OLE10200', '1', 'OLE990051', '7', '41', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10201', 'OLE10201', '1', 'OLE990052', '7', '41', 'FALSE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10202', 'OLE10202', '1', 'OLE990052', '7', '16', 'Payment')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10203', 'OLE10203', '1', 'OLE990052', '7', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10204', 'OLE10204', '1', 'OLE990052', '7', '40', 'TRUE')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10205', 'OLE10205', '1', 'OLE990053', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10206', 'OLE10206', '1', 'OLE990053', '7', '16', 'PrepaidInvoiceType')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10207', 'OLE10207', '1', 'OLE990053', '7', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10208', 'OLE10208', '1', 'OLE990053', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10209', 'OLE10209', '1', 'OLE990054', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10210', 'OLE10210', '1', 'OLE990054', '7', '16', 'NoPrepaidInvoiceType')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10211', 'OLE10211', '1', 'OLE990054', '7', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10212', 'OLE10212', '1', 'OLE990054', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10213', 'OLE10213', '1', 'OLE990055', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10214', 'OLE10214', '1', 'OLE990055', '7', '16', 'VendorDepositAccount')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10215', 'OLE10215', '1', 'OLE990055', '7', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10216', 'OLE10216', '1', 'OLE990055', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10217', 'OLE10217', '1', 'OLE990056', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10218', 'OLE10218', '1', 'OLE990056', '7', '16', 'SeparationOfDuties')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10219', 'OLE10219', '1', 'OLE990056', '7', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10220', 'OLE10220', '1', 'OLE990056', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10221', 'OLE10221', '1', 'OLE990057', '7', '16', 'Budget')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10222', 'OLE10222', '1', 'OLE990057', '7', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10223', 'OLE10223', '1', 'OLE990057', '7', '40', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10224', 'OLE10224', '1', 'OLE990057', '7', '41', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10225', 'OLE10225', '1', 'OLE990058', '7', '16', 'FYIBudget')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10226', 'OLE10226', '1', 'OLE990058', '7', '13', 'OLE_PRQS')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10227', 'OLE10227', '1', 'OLE990058', '7', '40', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10228', 'OLE10228', '1', 'OLE990058', '7', '41', 'true')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10229', 'OLE10229', '1', 'OLE990059', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10230', 'OLE10230', '1', 'OLE990059', '7', '16', 'AcquisitionStaff')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10231', 'OLE10231', '1', 'OLE990059', '7', '13', 'OLE_ERS_DOC')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10232', 'OLE10232', '1', 'OLE990059', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10233', 'OLE10233', '1', 'OLE990060', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10234', 'OLE10234', '1', 'OLE990060', '7', '16', 'Cataloger')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10235', 'OLE10235', '1', 'OLE990060', '7', '13', 'OLE_ERS_DOC')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10236', 'OLE10236', '1', 'OLE990060', '7', '40', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10237', 'OLE10237', '1', 'OLE990061', '7', '41', 'false')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10238', 'OLE10238', '1', 'OLE990061', '7', '16', 'ReceivingOperator')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10239', 'OLE10239', '1', 'OLE990061', '7', '13', 'OLE_SER_RECV_REC')
/

INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, RSP_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLE10240', 'OLE10240', '1', 'OLE990061', '7', '40', 'false')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_RSP_ATTR_DATA_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 18, '7:3dc6ea5cfb0a12819984c557de8b20fc', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_LOAD_OLE_KRIM_ROLE_T::ole
INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10000', 'OLE10000', '1', 'OLE_User', 'OLE-SELECT', 'This role allows the User to search the Documents', 'OLE36', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10001', 'OLE10001', '2', 'OLE_SYS', 'OLE-SELECT', 'This Role is used for Library Support System', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10002', 'OLE10002', '1', 'OLE_Requestor', 'OLE-SELECT', 'This Role is for Requestor OLE', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10003', 'OLE10003', '3', 'OLE_Load', 'OLE-SELECT', 'This Role is assigned to admin for Loading', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10004', 'OLE10004', '4', 'OLE_Selectors', 'OLE-SELECT', 'This role for Selectors who take action on the Document', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10005', 'OLE10005', '4', 'OLE_Super-Selectors', 'OLE-SELECT', 'This Role can assign actions to Super Selector', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10006', 'OLE10006', '3', 'OLE_Acquisitions', 'OLE-SELECT', 'This Role is for Acquisitions Staff', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10012', 'OLE10012', '9', 'OLE_ACQ-Mgr', 'OLE-SELECT', 'This Role is for Acquisitions Managers', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10013', 'OLE10013', '1', 'OLE_App-Mgr', 'OLE-SELECT', 'This Role is for Approval Plan By Manager', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10014', 'OLE10014', '2', 'OLE_ERMS', 'OLE-SELECT', 'This Role is for Staffs for Licensing', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10016', 'OLE10016', '1', 'OLE_RCV', 'OLE-SELECT', 'This Role is for Receiving Staff', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10017', 'OLE10017', '1', 'OLE_License-Mgr', 'OLE-SELECT', 'This Role authorises the Users to allow Licensing', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10018', 'OLE10018', '2', 'OLE_Fund -Mgr', 'OLE-SELECT', 'This Role allows to take Action on Funds', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10019', 'OLE10019', '7', 'OLE-Receive-Mgr', 'OLE-SELECT', 'Line Item Receiving Manager Role', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10020', 'OLE10020', '7', 'OLE-Invoicing', 'OLE-SELECT', 'OLE-Invoicing', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10021', 'OLE10021', '5', 'OLE-Payment', 'OLE-SELECT', 'OLE-Payment', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10022', 'OLE10022', '4', 'OLE_Selectors-serial', 'OLE-SELECT', 'This role for Selectors who take action on the Document', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10023', 'OLE10023', '3', 'OLE_Acquisitions-serial', 'OLE-SELECT', 'This Role is for Acquisitions Staff', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10024', 'OLE10024', '9', 'OLE_ACQ-Mgr-serial', 'OLE-SELECT', 'This Role is for Acquisitions Managers', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10025', 'OLE10025', '1', 'OLE_ORDQU-User', 'OLE-SELECT', 'Order Queue User Role', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10026', 'OLE10026', '1', 'Acquisitions-AQ1', 'OLE-SELECT', 'This Role is for View only document(Not for edit)', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10027', 'OLE10027', '1', 'Acquisitions-AQ2', 'OLE-SELECT', 'This Role is for low-level staff with specific needs only. This role is able to create and edit selected functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10028', 'OLE10028', '1', 'Acquisitions-AQ3', 'OLE-SELECT', 'This Role is for staff performing function.This role is able to create and edit all functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10029', 'OLE10029', '1', 'Acquisitions-AQ4', 'OLE-SELECT', 'This Role is for when you want to restrict deletion to high-level staff. This role is able to create edit or delete all functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10030', 'OLE10030', '1', 'Acquisitions-AQ5', 'OLE-SELECT', 'This role can perform functions and assign appropriate security level to others.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10031', 'OLE10031', '1', 'Receiving-AQ1', 'OLE-SELECT', 'This Role is for View only document(Not for edit)', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10032', 'OLE10032', '1', 'Receiving-AQ2', 'OLE-SELECT', 'This Role is for low-level staff with specific needs only. This role is able to create and edit selected functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10033', 'OLE10033', '1', 'Receiving-AQ3', 'OLE-SELECT', 'This Role is for staff performing function. This role is able to create and edit all functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10034', 'OLE10034', '1', 'Receiving-AQ4', 'OLE-SELECT', 'This Role is for when you want to restrict deletion to high-level staff. This role is able to create edit or delete all functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10035', 'OLE10035', '1', 'Accounting-AQ1', 'OLE-SELECT', 'This Role is for View only document(Not for edit)', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10036', 'OLE10036', '1', 'Accounting-AQ2', 'OLE-SELECT', 'This Role is for low-level staff with specific needs only. This role is able to create and edit selected functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10037', 'OLE10037', '1', 'Accounting-AQ3', 'OLE-SELECT', 'This Role is for staff performing function. This role is able to create and edit all functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10038', 'OLE10038', '1', 'Accounting-AQ4', 'OLE-SELECT', 'This Role is for when you want to restrict deletion to high-level staff. This role is able to create edit or delete all functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10039', 'OLE10039', '1', 'Financial-AQ1', 'OLE-SELECT', 'This Role is for View only document(Not for edit)', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10040', 'OLE10040', '1', 'Financial-AQ2', 'OLE-SELECT', 'This Role is for low-level staff with specific needs only. This role is able to create and edit selected functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10041', 'OLE10041', '1', 'Financial-AQ3', 'OLE-SELECT', 'This Role is for staff performing function. This role is able to create and edit all functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10042', 'OLE10042', '1', 'Financial-AQ4', 'OLE-SELECT', 'This Role is for when you want to restrict deletion to high-level staff. This role is able to create edit or delete all functions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10043', 'OLE10043', '1', 'Financial-AQ5', 'OLE-SELECT', 'This role can perform functions and assign appropriate security level to others.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10044', 'OLE10044', '1', 'OLE_Prepayment', 'OLE-SELECT', 'This Role perform the approval and security on the Documents DV CM DI Payment Request and the GEC.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10045', 'OLE10045', '1', 'OLE_Accounting', 'OLE-SELECT', 'This Role perform the approval on the DI and GEC document.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10046', '1000', '1', 'Patron Manager', 'OLE-PTRN', 'All the Permissions of Patron and BorrowerType', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10047', '1001', '1', 'Full Circulation Attendent', 'OLE-PTRN', 'Edit Patron Document', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10048', '1002', '1', 'Limited Circulation Attendent', 'OLE-PTRN', 'View Patron Document no access to borrowerType', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10049', '1003', '1', 'Location Administrator', 'OLE-PTRN', 'Edit and View the Location Level Document', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10050', '1004', '1', 'Shelving Location Administrator', 'OLE-PTRN', 'View Location Level Document', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10051', 'OLE100011', '1', 'OLE_Signatory', 'OLE-LIC', 'This role is a person with the authority to sign a license. Note: this role may not be used within OLE if license must be signed by an external party (outside of OLE).', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10052', 'OLE100021', '1', 'OLE_LicenseManager', 'OLE-LIC', 'Group of users who actually do the work of license negotiation used for routing purposes.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10053', 'OLE100031', '1', 'OLE_Licenses', 'OLE-LIC', 'OLE_Licenses are the assignees for Requisitions requiring licenses or license negotiations. This role is the “owner” of the license agreement  and has full edit  attachment  routing  and status change permissions.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10054', 'OLE100041', '1', 'OLE_LicenseReviewer', 'OLE-LIC', 'This role is a person with the authority to sign a license.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10055', 'OLE100051', '1', 'OLE_Licensing_Approver', 'OLE-LIC', 'OLE_Licensing_Approver will receive License Requests in status “Complete” as final step in workflow.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10056', 'OLE100061', '1', 'OLE_LicenseViewer', 'OLE-LIC', 'License documents and Search on Licenses have very limited permissons. This role can view only Agreements Agreement docs License Requests.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10057', 'OLE100071', '1', 'OLE_LicenseConfiguration', 'OLE-LIC', 'This is a licensing superuser with authority to set default system configuration VALUES  such as the default notification period after inactivity.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10058', '1005', '1', 'OLE_Cataloging_Admin', 'OLE-CAT', 'Initiate Catalogue Document', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10059', 'OLE10008', '1', 'OLE_LicenseAssignee', 'OLE-LIC', 'This role will have the persons who can assign the License Request Document.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10060', '1100', '1', 'Operator', 'OLE-PTRN', 'Operator', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10061', '1101', '1', 'Unit Manager', 'OLE-PTRN', 'UnitManager', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10062', '1102', '1', 'Deliver Admin', 'OLE-PTRN', 'Deliver Admin', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10063', '2100', '1', 'Operator', 'OLE-DLVR', 'Operator', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10064', 'OLE10010', '1', 'OLE_Selector', 'OLE-SELECT', 'This Role edits  creates and close/cancel the E-Resource records if it is currently assigned to this group and also can begin the Trial ', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10065', 'OLE10011', '1', 'OLE_Head', 'OLE-SELECT', 'This Role performs can create edit view but not delete E-Resource Records.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10066', 'OLE100131', '1', 'OLE_Acquisitions-select', 'OLE-SELECT', 'This Role performs can create edit view but not delete E-Resource Records.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10067', 'OLE100141', '1', 'Collection Management', 'OLE-SELECT', 'This Role can close/cancel E-Resource Record if it is currently assigned to this group and Can approve/route to Acquisitions for purchase.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10068', 'OLE100151', '1', 'OLE_E-Resource', 'OLE-SELECT', 'This role Can create edit delete E-Resource Record start any sub-workflow link to GOKb particular focus is on Activation and using the AccessDashboard.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10069', 'OLE100161', '1', 'OLE_Cataloger', 'OLE-SELECT', 'This role can only view E-Resource Record.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10070', 'OLE100171', '1', 'OLE_Circ_Staff', 'OLE-SELECT', 'This role can only view E-Resource Record.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10071', 'OLE100181', '1', 'OLE_E-ResourceManager', 'OLE-SELECT', 'This role manages site-wide configurations like values for the E-Resource status and time periods for alerts.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('KR1000', 'CFBAA6F5F0D25D07E0406E0AC31D229B', '1', 'GuestRole', 'KUALI', 'This role is used for no login guest users.', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('KR1001', 'D0C2892F47ED9142E0406E0AC31D23D6', '1', 'Complete Request Recipient', 'KR-WKFLW', 'This role derives its members from users with an complete action request in the route log of a given document.', '42', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('KRSAP10003', 'CDD8032915D1BF1FE040F90A05B94313', '1', 'Sample App Admin', 'KR-SAP', 'Test role for the sample app', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('KRSAP10004', 'CDD8032915D3BF1FE040F90A05B94313', '1', 'Sample App Users', 'KR-SAP', 'Test role for the sample app', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10072', '10029', '1', 'Operator', 'OLE-SELECT', 'Operator', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10073', 'OLE30000', '1', 'Super Circulation Supervisor', 'OLE-PTRN', 'Super Circulation Supervisor', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10074', 'OLE30001', '1', 'Circulation Supervisor', 'OLE-PTRN', 'Circulation Supervisor', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10075', 'OLE30002', '1', 'Circ Desk Attendant I', 'OLE-PTRN', 'Circ Desk Attendant I', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10076', 'OLE30003', '1', 'Circ Desk Attendant II', 'OLE-PTRN', 'Circ Desk Attendant II', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10077', 'OLE100001', '1', 'Cataloging Super User', 'OLE-CAT', 'Authorized for all Describe functions; permission to administer cataloging in all work units', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10078', 'OLE100002', '1', 'Cataloger Supervisor', 'OLE-CAT', 'Authorized for all Describe functions; permission to administer cataloging within assigned work unit(s)', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10079', 'OLE100003', '1', 'Cataloger 1', 'OLE-CAT', 'Authorized for all Describe functions; permission to administer cataloging within assigned work unit', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10080', 'OLE100004', '1', 'Cataloger 2', 'OLE-CAT', 'Authorized for all Describe functions except batch imports and bound-withs; permission to administer cataloging within assigned work unit', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10081', 'OLE100005', '1', 'Temporary (Student) Staff Cataloger  1', 'OLE-CAT', 'Authorized to create and edit holdings/items within assigned work unit', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10082', 'OLE100006', '1', 'Full Cataloging', 'OLE-CAT', 'Authorized for all Describe functions without limitation by ownership work unit  bibliographic status or any other field-level permission restriction', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10083', 'OLE100007', '1', 'Batch Cataloging', 'OLE-CAT', 'Authorized to do batch imports and exports', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10084', 'OLE100008', '1', 'Describe Read only', 'OLE-CAT', 'Authorized only to view records in Describe', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10085', 'OLE100009', '1', 'Cataloger', 'OLE-CAT', 'Authorized to perform Global Edit of Holdings/Items/EHoldings and Analytics', '1', 'Y')
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10086', 'OLE100010', '1', 'OLE_Serial-Receiving', 'OLE-SELECT', 'Authorized to edit Serial Receiving record.', '1', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_OLE_KRIM_ROLE_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 19, '7:3471ae355e53b60b31df87f34c1b63a6', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_KRIM_ROLE_PERM_T::ole
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10000', 'OLE10000', 'OLE10000', 'OLE10004', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10005', 'OLE10005', 'OLE10004', 'OLE10005', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10006', 'OLE10006', 'OLE10004', 'OLE10006', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10007', 'OLE10007', 'OLE10004', 'OLE10009', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10008', 'OLE10008', 'OLE10004', 'OLE10011', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10009', 'OLE10009', 'OLE10004', 'OLE10012', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10010', 'OLE10010', 'OLE10004', 'OLE10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10011', 'OLE10011', 'OLE10004', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10013', 'OLE10013', 'OLE10005', 'OLE10005', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10014', 'OLE10014', 'OLE10005', 'OLE10006', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10015', 'OLE10015', 'OLE10005', 'OLE10009', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10016', 'OLE10016', 'OLE10005', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10018', 'OLE10018', 'OLE10006', 'OLE10005', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10019', 'OLE10019', 'OLE10006', 'OLE10006', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10020', 'OLE10020', 'OLE10006', 'OLE10009', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10021', 'OLE10021', 'OLE10006', 'OLE10015', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10022', 'OLE10022', 'OLE10006', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10024', 'OLE10024', 'OLE10012', 'OLE10005', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10025', 'OLE10025', 'OLE10012', 'OLE10006', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10026', 'OLE10026', 'OLE10012', 'OLE10009', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10027', 'OLE10027', 'OLE10012', 'OLE10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10028', 'OLE10028', 'OLE10012', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10031', 'OLE10031', 'OLE10014', 'OLE10015', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10032', 'OLE10032', 'OLE10014', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10037', 'OLE10037', 'OLE10016', 'OLE10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10038', 'OLE10038', 'OLE10016', 'OLE10011', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10039', 'OLE10039', 'OLE10016', 'OLE10012', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10040', 'OLE10040', 'OLE10016', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10041', 'OLE10041', 'OLE10005', 'OLE10023', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10042', 'OLE10042', 'OLE10005', 'OLE10032', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10043', 'OLE10043', 'OLE10012', 'OLE10021', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10044', 'OLE10044', 'OLE10014', 'OLE10021', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10046', 'OLE10046', 'OLE10017', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10047', 'OLE10047', 'OLE10017', 'OLE10021', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10048', 'OLE10048', 'OLE10017', 'OLE10015', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10050', 'OLE10050', 'OLE10001', 'OLE10027', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10051', 'OLE10051', 'OLE10001', 'OLE10022', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10052', 'OLE10052', 'OLE10001', 'OLE10031', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10053', 'OLE10053', 'OLE10001', 'OLE10018', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10054', 'OLE10054', 'OLE10003', 'OLE10022', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10055', 'OLE10055', 'OLE10003', 'OLE10032', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10056', 'OLE10056', 'OLE10004', 'OLE10032', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10057', 'OLE10057', 'OLE10004', 'OLE10023', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10058', 'OLE10058', 'OLE10005', 'OLE10030', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10059', 'OLE10059', 'OLE10005', 'OLE10008', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10060', 'OLE10060', 'OLE10006', 'OLE10011', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10061', 'OLE10061', 'OLE10006', 'OLE10012', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10062', 'OLE10062', 'OLE10006', 'OLE10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10063', 'OLE10063', 'OLE10006', 'OLE10026', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10064', 'OLE10064', 'OLE10018', 'OLE10024', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10065', 'OLE10065', 'OLE10000', 'OLE10035', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10078', 'OLE10078', 'OLE10000', 'OLE10036', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10096', 'OLE10096', 'OLE10000', 'OLE10041', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10097', 'OLE10097', 'OLE10004', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10100', 'OLE10100', 'OLE10017', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10101', 'OLE10101', 'OLE10014', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10102', 'OLE10102', 'OLE10006', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10103', 'OLE10103', 'OLE10012', 'OLE10043', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10104', 'OLE10104', 'OLE10016', 'OLE10043', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10105', 'OLE10105', 'OLE10004', 'OLE10043', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10106', 'OLE10106', 'OLE54', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10107', 'OLE10107', 'OLE54', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10108', 'OLE10108', 'OLE10005', 'OLE10043', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10152', 'OLE10152', 'OLE26', 'OLE10011', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10153', 'OLE10153', 'OLE26', 'OLE10012', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10154', 'OLE10154', 'OLE26', 'OLE10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10109', 'OLESEC10109', 'OLE10000', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10110', 'OLESEC10110', 'OLE10000', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10111', 'OLESEC10111', 'OLE10000', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10112', 'OLESEC10112', 'OLE10001', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10113', 'OLESEC10113', 'OLE10001', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10114', 'OLESEC10114', 'OLE10001', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10115', 'OLESEC10115', 'OLE10002', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10116', 'OLESEC10116', 'OLE10002', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10117', 'OLESEC10117', 'OLE10002', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10118', 'OLESEC10118', 'OLE10003', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10119', 'OLESEC10119', 'OLE10003', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10120', 'OLESEC10120', 'OLE10003', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10121', 'OLESEC10121', 'OLE10004', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10122', 'OLESEC10122', 'OLE10004', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10123', 'OLESEC10123', 'OLE10004', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10124', 'OLESEC10124', 'OLE10005', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10125', 'OLESEC10125', 'OLE10005', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10126', 'OLESEC10126', 'OLE10005', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10127', 'OLESEC10127', 'OLE10006', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10128', 'OLESEC10128', 'OLE10006', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10129', 'OLESEC10129', 'OLE10006', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10130', 'OLESEC10130', 'OLE10012', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10131', 'OLESEC10131', 'OLE10012', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10132', 'OLESEC10132', 'OLE10012', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10133', 'OLESEC10133', 'OLE10013', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10134', 'OLESEC10134', 'OLE10013', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10135', 'OLESEC10135', 'OLE10013', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10136', 'OLESEC10136', 'OLE10014', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10137', 'OLESEC10137', 'OLE10014', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10138', 'OLESEC10138', 'OLE10014', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10139', 'OLESEC10139', 'OLE10016', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10140', 'OLESEC10140', 'OLE10016', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10141', 'OLESEC10141', 'OLE10016', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10142', 'OLESEC10142', 'OLE10017', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10143', 'OLESEC10143', 'OLE10017', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10144', 'OLESEC10144', 'OLE10017', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10145', 'OLESEC10145', 'OLE10018', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10146', 'OLESEC10146', 'OLE10018', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10147', 'OLESEC10147', 'OLE10018', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10148', 'OLESEC10148', 'OLE44', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10149', 'OLESEC10149', 'OLE44', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10150', 'OLESEC10150', 'OLE44', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10151', 'OLESEC10151', '59', 'OLESEC6004', 'N')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10155', 'OLE10155', 'OLE62', '168', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10156', 'OLE10156', 'OLE10019', 'OLE10044', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10157', 'OLE10157', 'OLE10019', 'OLE10045', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10158', 'OLE10158', 'OLE10020', 'OLE10044', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10159', 'OLE10159', 'OLE10020', 'OLE10045', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10160', 'OLE10160', 'OLE10019', 'OLE10047', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10161', 'OLE10161', 'OLE10020', 'OLE10047', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10162', 'OLE10162', 'OLE10019', 'OLE10046', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10163', 'OLE10163', 'OLE10016', 'OLE10044', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10164', 'OLE10164', 'OLE10016', 'OLE10045', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10165', 'OLE10165', 'OLE10016', 'OLE10047', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10166', 'OLE10166', 'OLE10016', 'OLE10046', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10167', 'OLE10167', 'OLE10016', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10168', 'OLE10168', 'OLE10016', 'OLE10050', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10169', 'OLE10169', 'OLE10016', 'OLE10051', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10170', 'OLE10170', 'OLE10016', 'OLE10052', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10171', 'OLE10171', 'OLE10019', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10172', 'OLE10172', 'OLE10019', 'OLE10050', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10173', 'OLE10173', 'OLE10019', 'OLE10051', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10174', 'OLE10174', 'OLE10019', 'OLE10052', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10175', 'OLE10175', 'OLE10021', 'OLE10053', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10176', 'OLE10176', 'OLE10021', 'OLE10054', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10177', 'OLE10177', 'OLE10020', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10178', 'OLE10178', 'OLE10020', 'OLE10050', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10179', 'OLE10179', 'OLE10020', 'OLE10051', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10180', 'OLE10180', 'OLE10020', 'OLE10052', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10181', 'OLE10181', 'OLE10020', 'OLE10053', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10182', 'OLE10182', 'OLE10020', 'OLE10054', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10183', 'OLE10183', 'OLE10016', 'OLE10055', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10184', 'OLE10184', 'OLE10019', 'OLE10055', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10185', 'OLE10185', 'OLE10019', 'OLE10011', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10186', 'OLE10186', 'OLE10019', 'OLE10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10187', 'OLE10187', 'OLE10020', 'OLE10011', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10188', 'OLE10188', 'OLE10020', 'OLE10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10189', 'OLE10189', 'OLE10019', 'OLE10048', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10190', 'OLE10190', 'OLE10021', 'OLE10056', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10191', 'OLE10191', 'OLE10019', 'OLE10057', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10192', 'OLE10192', 'OLE10016', 'OLE10057', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10193', 'OLE10193', 'OLE10020', 'OLE10057', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10200', 'OLE10200', 'OLE10004', 'OLE10060', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10201', 'OLE10201', 'OLE10004', 'OLE10061', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10202', 'OLE10202', 'OLE10004', 'OLE10062', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10203', 'OLE10203', 'OLE10006', 'OLE10060', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10204', 'OLE10204', 'OLE10006', 'OLE10061', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10205', 'OLE10205', 'OLE10006', 'OLE10062', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10207', 'OLE10207', 'OLE10006', 'OLE10061', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10208', 'OLE10208', 'OLE10006', 'OLE10062', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10209', 'OLE10209', 'OLE10012', 'OLE10060', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10210', 'OLE10210', 'OLE10012', 'OLE10061', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10211', 'OLE10211', 'OLE10012', 'OLE10062', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10212', 'OLE10212', 'OLE10002', 'OLE10063', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10213', 'OLE10213', 'OLE10004', 'OLE10064', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10214', 'OLE10214', 'OLE10012', 'OLE10065', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10215', 'OLE10215', 'OLE10012', 'OLE10066', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10216', 'OLE10216', 'OLE10012', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10217', 'OLE10217', 'OLE10006', 'OLE10066', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10218', 'OLE10218', 'OLE10006', 'OLE10068', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10219', 'OLE10219', 'OLE10006', 'OLE10069', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10220', 'OLE10220', 'OLE10006', 'OLE10070', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10221', 'OLE10221', 'OLE10012', 'OLE10068', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10222', 'OLE10222', 'OLE10012', 'OLE10069', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10223', 'OLE10223', 'OLE10012', 'OLE10070', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10224', 'OLE10224', 'OLE10012', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10225', 'OLE10225', 'OLE10006', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10226', 'OLE10226', 'OLE10022', 'OLE10071', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10227', 'OLE10227', 'OLE10023', 'OLE10071', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10228', 'OLE10228', 'OLE10024', 'OLE10071', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10229', 'OLE10229', 'OLE10012', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10230', 'OLE10230', 'OLE10012', 'OLE10015', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10231', 'OLE10231', 'OLE10006', 'OLE10066', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10232', 'OLE10232', 'OLE10006', 'OLE10100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10233', 'OLE10233', 'OLE10003', 'OLE10100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10234', 'OLE10234', 'OLE10012', 'OLE10100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10235', 'OLE10235', 'OLE10003', 'OLE10108', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10236', 'OLE10236', 'OLE10003', 'OLE10109', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10237', 'OLE10237', 'OLE10012', 'OLE10108', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10238', 'OLE10238', 'OLE10012', 'OLE10109', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10239', 'OLE10239', 'OLE10005', 'OLE10108', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10240', 'OLE10240', 'OLE10005', 'OLE10109', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10241', 'OLE10241', 'OLE10006', 'OLE10108', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10242', 'OLE10242', 'OLE10006', 'OLE10109', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10243', 'OLE10243', 'OLE10004', 'OLE10108', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10244', 'OLE10244', 'OLE62', 'OLE10060', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10245', 'OLE10245', 'OLE62', 'OLE10061', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10246', 'OLE10246', 'OLE62', 'OLE10062', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10247', 'OLE10247', 'OLE62', 'OLE10071', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10248', 'OLE10248', 'OLE10024', 'OLE10110', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10249', 'OLE10249', 'OLE10024', 'OLE10111', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10250', 'OLE10250', 'OLE10023', 'OLE10110', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10251', 'OLE10251', 'OLE10023', 'OLE10111', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10252', 'OLE10252', 'OLE10022', 'OLE10110', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10253', 'OLE10253', 'OLE10022', 'OLE10111', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10259', 'OLE10259', 'OLE41', 'OLE10060', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10260', 'OLE10260', 'OLE41', 'OLE10061', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10261', 'OLE10261', 'OLE41', 'OLE10062', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10262', 'OLE10262', 'OLE41', 'OLE10110', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10263', 'OLE10263', 'OLE41', 'OLE10111', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10264', 'OLE10264', 'OLE41', 'OLE10071', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10265', 'OLE10265', 'OLE54', 'OLE10100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10266', 'OLE10266', 'OLE54', 'OLE10111', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10267', 'OLE10267', 'OLE54', 'OLE10071', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10268', 'OLE10268', 'OLE54', 'OLE10060', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10269', 'OLE10269', 'OLE54', 'OLE10061', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10270', 'OLE10270', 'OLE54', 'OLE10062', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10271', 'OLE10271', 'OLE62', 'OLE10110', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10272', 'OLE10272', 'OLE62', 'OLE10111', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10273', 'OLE10273', 'OLE10022', 'OLE10005', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10274', 'OLE10274', 'OLE10022', 'OLE10006', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10275', 'OLE10275', 'OLE10022', 'OLE10009', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10276', 'OLE10276', 'OLE10022', 'OLE10011', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10277', 'OLE10277', 'OLE10022', 'OLE10012', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10278', 'OLE10278', 'OLE10022', 'OLE10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10279', 'OLE10279', 'OLE10022', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10280', 'OLE10280', 'OLE10022', 'OLE10032', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10281', 'OLE10281', 'OLE10022', 'OLE10023', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10282', 'OLE10282', 'OLE10022', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10283', 'OLE10283', 'OLE10022', 'OLE10043', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10284', 'OLE10284', 'OLE10022', 'OLE10071', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10285', 'OLE10285', 'OLE10022', 'OLE10110', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10286', 'OLE10286', 'OLE10022', 'OLE10111', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10287', 'OLE10287', 'OLE10022', 'OLE10064', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10288', 'OLE10288', 'OLE10022', 'OLE10108', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10289', 'OLE10289', 'OLE10023', 'OLE10005', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10290', 'OLE10290', 'OLE10023', 'OLE10006', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10291', 'OLE10291', 'OLE10023', 'OLE10009', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10292', 'OLE10292', 'OLE10023', 'OLE10015', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10293', 'OLE10293', 'OLE10023', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10294', 'OLE10294', 'OLE10023', 'OLE10011', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10295', 'OLE10295', 'OLE10023', 'OLE10012', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10296', 'OLE10296', 'OLE10023', 'OLE10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10297', 'OLE10297', 'OLE10023', 'OLE10026', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10298', 'OLE10298', 'OLE10023', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10299', 'OLE10299', 'OLE10023', 'OLE10071', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10300', 'OLE10300', 'OLE10023', 'OLE10110', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10301', 'OLE10301', 'OLE10023', 'OLE10111', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10302', 'OLE10302', 'OLE10023', 'OLE10061', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10303', 'OLE10303', 'OLE10023', 'OLE10062', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10304', 'OLE10304', 'OLE10023', 'OLE10066', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10305', 'OLE10305', 'OLE10023', 'OLE10068', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10306', 'OLE10306', 'OLE10023', 'OLE10069', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10307', 'OLE10307', 'OLE10023', 'OLE10070', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10308', 'OLE10308', 'OLE10023', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10309', 'OLE10309', 'OLE10023', 'OLE10066', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10310', 'OLE10310', 'OLE10023', 'OLE10100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10311', 'OLE10311', 'OLE10023', 'OLE10108', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10312', 'OLE10312', 'OLE10023', 'OLE10109', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10313', 'OLE10313', 'OLE10024', 'OLE10005', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10314', 'OLE10314', 'OLE10024', 'OLE10006', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10315', 'OLE10315', 'OLE10024', 'OLE10009', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10316', 'OLE10316', 'OLE10024', 'OLE10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10317', 'OLE10317', 'OLE10024', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10318', 'OLE10318', 'OLE10024', 'OLE10021', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10319', 'OLE10319', 'OLE10024', 'OLE10043', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10323', 'OLE10323', 'OLE10024', 'OLE10065', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10324', 'OLE10324', 'OLE10024', 'OLE10066', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10325', 'OLE10325', 'OLE10024', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10326', 'OLE10326', 'OLE10024', 'OLE10068', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10327', 'OLE10327', 'OLE10024', 'OLE10069', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10328', 'OLE10328', 'OLE10024', 'OLE10070', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10329', 'OLE10329', 'OLE10024', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10330', 'OLE10330', 'OLE10024', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10331', 'OLE10331', 'OLE10024', 'OLE10015', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10332', 'OLE10332', 'OLE10024', 'OLE10100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10333', 'OLE10333', 'OLE10024', 'OLE10108', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10334', 'OLE10334', 'OLE10024', 'OLE10109', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10335', 'OLE10335', 'OLE10004', 'OLE10115', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10336', 'OLE10336', 'OLE10005', 'OLE10115', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10337', 'OLE10337', 'OLE10006', 'OLE10115', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10338', 'OLE10338', 'OLE10012', 'OLE10115', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10339', 'OLE10339', 'OLE10012', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10340', 'OLE10340', 'OLE10016', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10341', 'OLE10341', 'OLE10019', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10342', 'OLE10342', 'OLE10012', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10343', 'OLE10343', 'OLE10016', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10344', 'OLE10344', 'OLE10019', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10345', 'OLE10345', 'OLE10012', 'OLE10118', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10346', 'OLE10346', 'OLE10016', 'OLE10118', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10347', 'OLE10347', 'OLE10019', 'OLE10118', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10348', 'OLE10348', 'OLE10012', 'OLE10119', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10349', 'OLE10349', 'OLE10016', 'OLE10119', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10350', 'OLE10350', 'OLE10019', 'OLE10119', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10351', 'OLE10351', 'OLE10012', 'OLE10008', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10352', 'OLE10352', 'OLE10004', 'OLE10009', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10353', 'OLE10353', 'OLE10004', 'OLE10120', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10354', 'OLE10354', 'OLE10005', 'OLE10120', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10355', 'OLE10355', 'OLE10012', 'OLE10056', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10356', 'OLE10356', 'OLE10024', 'OLE10056', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10357', 'OLE10357', 'OLE10020', 'OLE10015', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10358', 'OLE10358', 'OLE10020', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10359', 'OLE10359', 'OLE10020', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10360', 'OLE10360', 'OLE10020', 'OLE10043', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10362', 'OLE10362', 'OLE10012', 'OLE10121', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10363', 'OLE10363', 'OLE10012', 'OLE10122', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10364', 'OLE10364', '63', 'OLE92', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10365', 'OLE10365', '63', 'OLE100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10366', 'OLE10366', 'OLE10016', 'OLE325', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10367', 'OLE10367', 'OLE10016', 'OLE326', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10368', 'OLE10368', 'OLE10016', 'OLE356', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10369', 'OLE10369', 'OLE10016', 'OLE293', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10370', 'OLE10370', 'OLE10016', 'OLE294', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10371', 'OLE10371', 'OLE10016', 'OLE367', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10372', 'OLE10372', 'OLE10016', 'OLE368', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10373', 'OLE10373', 'OLE10019', 'OLE325', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10374', 'OLE10374', 'OLE10019', 'OLE326', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10375', 'OLE10375', 'OLE10019', 'OLE356', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10376', 'OLE10376', 'OLE10019', 'OLE293', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10377', 'OLE10377', 'OLE10019', 'OLE294', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10378', 'OLE10378', 'OLE10019', 'OLE367', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10379', 'OLE10379', 'OLE10019', 'OLE368', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10380', 'OLE10380', 'OLE10020', 'OLE325', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10381', 'OLE10381', 'OLE10020', 'OLE326', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10382', 'OLE10382', 'OLE10020', 'OLE356', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10383', 'OLE10383', 'OLE10020', 'OLE293', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10384', 'OLE10384', 'OLE10020', 'OLE294', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10385', 'OLE10385', 'OLE10020', 'OLE367', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10386', 'OLE10386', 'OLE10020', 'OLE368', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10387', 'OLE10387', 'OLE10027', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10388', 'OLE10388', 'OLE10027', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10391', 'OLE10391', 'OLE10028', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10392', 'OLE10392', 'OLE10028', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10395', 'OLE10395', 'OLE10029', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10396', 'OLE10396', 'OLE10029', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10399', 'OLE10399', 'OLE10030', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10152', 'OLESEC10152', 'OLE10022', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10153', 'OLESEC10153', 'OLE10022', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10154', 'OLESEC10154', 'OLE10022', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10155', 'OLESEC10155', 'OLE10023', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10156', 'OLESEC10156', 'OLE10023', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10157', 'OLESEC10157', 'OLE10023', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10158', 'OLESEC10158', 'OLE10024', 'OLESEC6001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10159', 'OLESEC10159', 'OLE10024', 'OLESEC6002', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLESEC10160', 'OLESEC10160', 'OLE10024', 'OLESEC6003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE81000', 'OLE81000', '63', 'OLE130', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE110000', 'OLE110000', 'OLE10003', 'OLE1500', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10400', 'OLE10400', 'OLE10030', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10403', 'OLE10403', 'OLE10032', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10404', 'OLE10404', 'OLE10032', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10407', 'OLE10407', 'OLE10033', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10408', 'OLE10408', 'OLE10033', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10411', 'OLE10411', 'OLE10034', 'OLE98', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10412', 'OLE10412', 'OLE10034', 'OLE103', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10415', 'OLE10415', 'OLE10028', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10416', 'OLE10416', 'OLE10028', 'OLE10043', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10417', 'OLE10417', 'OLE10028', 'OLE80141', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10418', 'OLE10418', 'OLE10028', 'OLE80142', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10419', 'OLE10419', 'OLE10029', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10420', 'OLE10420', 'OLE10029', 'OLE10043', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10421', 'OLE10421', 'OLE10029', 'OLE80141', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10422', 'OLE10422', 'OLE10029', 'OLE80142', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10423', 'OLE10423', 'OLE10030', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10424', 'OLE10424', 'OLE10030', 'OLE10043', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10425', 'OLE10425', 'OLE10030', 'OLE80141', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10426', 'OLE10426', 'OLE10030', 'OLE80142', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10451', 'OLE10451', 'OLE10033', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10452', 'OLE10452', 'OLE10034', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10453', 'OLE10453', 'OLE10036', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10454', 'OLE10454', 'OLE10037', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10455', 'OLE10455', 'OLE10038', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10456', 'OLE10456', 'OLE10036', 'OLE10050', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10457', 'OLE10457', 'OLE10037', 'OLE10050', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10458', 'OLE10458', 'OLE10038', 'OLE10050', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10459', 'OLE10459', 'OLE10036', 'OLE10057', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10460', 'OLE10460', 'OLE10037', 'OLE10057', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10461', 'OLE10461', 'OLE10038', 'OLE10057', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10465', 'OLE10465', 'OLE10038', 'OLE10051', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10466', 'OLE10466', 'OLE10044', 'OLE80158', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10467', 'OLE10467', 'OLE10044', 'OLE80159', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10468', 'OLE10468', 'OLE10044', 'OLE80160', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10469', 'OLE10469', 'OLE10044', 'OLE80157', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10470', 'OLE10470', 'OLE10044', 'OLE80162', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10471', 'OLE10471', 'OLE10021', 'OLE80157', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10472', 'OLE10472', 'OLE10021', 'OLE80162', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10473', 'OLE10473', 'OLE10000', 'OLE80158', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10474', 'OLE10474', '60', 'OLE80159', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10475', 'OLE10475', '59', 'OLE80160', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10476', 'OLE10476', '97', 'OLE80160', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10477', 'OLE10477', 'OLE62', 'OLE80161', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10478', 'OLE10478', 'OLE62', 'OLE80161', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10479', 'OLE10479', 'OLE22', 'OLE80161', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10480', 'OLE10480', 'OLE10021', 'OLE80161', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10481', 'OLE10481', 'OLE10021', 'OLE367', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10482', 'OLE10482', 'OLE10021', 'OLE368', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10483', 'OLE10483', 'OLE10021', 'OLE356', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10484', 'OLE10484', 'OLE10044', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10485', 'OLE10485', 'OLE10044', 'OLE10050', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10486', 'OLE10486', 'OLE10044', 'OLE10051', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10487', 'OLE10487', 'OLE10044', 'OLE10052', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10488', 'OLE10488', 'OLE10044', 'OLE10057', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10489', 'OLE10489', 'OLE10044', 'OLE10015', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10490', 'OLE10490', 'OLE10044', 'OLE10017', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10491', 'OLE10491', 'OLE10044', 'OLE10042', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10492', 'OLE10492', 'OLE10028', 'OLE80163', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10493', 'OLE10493', 'OLE10029', 'OLE80163', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10494', 'OLE10494', 'OLE10030', 'OLE80163', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10495', 'OLE10495', 'OLE10042', 'OLE80164', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10496', 'OLE10496', 'OLE10042', 'OLE80165', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10497', 'OLE10497', 'OLE10042', 'OLE80166', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10498', 'OLE10498', 'OLE10042', 'OLE80167', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10499', 'OLE10499', 'OLE10042', 'OLE80168', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10500', 'OLE10500', 'OLE10042', 'OLE80169', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10501', 'OLE10501', 'OLE10042', 'OLE80170', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10502', 'OLE10502', 'OLE10042', 'OLE80171', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10503', 'OLE10503', 'OLE10043', 'OLE80164', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10504', 'OLE10504', 'OLE10043', 'OLE80165', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10505', 'OLE10505', 'OLE10043', 'OLE80166', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10506', 'OLE10506', 'OLE10043', 'OLE80167', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10507', 'OLE10507', 'OLE10043', 'OLE80168', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10508', 'OLE10508', 'OLE10043', 'OLE80169', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10509', 'OLE10509', 'OLE10043', 'OLE80170', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10510', 'OLE10510', 'OLE10043', 'OLE80171', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10511', 'OLE10511', 'OLE10043', 'OLE80172', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11004', 'OLE11004', 'OLE10033', 'OLE10057', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11005', 'OLE11005', 'OLE10034', 'OLE10057', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11572', 'OLE11572', 'OLE10028', 'OLE80510', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11573', 'OLE11573', 'OLE10029', 'OLE80510', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11574', 'OLE11574', 'OLE10030', 'OLE80510', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11575', 'OLE11575', 'OLE10029', 'OLE80511', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11576', 'OLE11576', 'OLE10030', 'OLE80511', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11577', 'OLE11577', 'OLE10029', 'OLE80512', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11578', 'OLE11578', 'OLE10030', 'OLE80512', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11579', 'OLE11579', 'OLE10038', 'OLE80513', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11580', 'OLE11580', 'OLE10042', 'OLE80513', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11581', 'OLE11581', 'OLE10043', 'OLE80513', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11582', 'OLE11582', 'OLE10038', 'OLE80514', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11583', 'OLE11583', 'OLE10042', 'OLE80514', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11584', 'OLE11584', 'OLE10043', 'OLE80514', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11585', 'OLE11585', '59', 'OLE80512', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11586', 'OLE11586', '59', 'OLE80514', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11587', 'OLE11587', 'OLE44', 'OLE80511', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11588', 'OLE11588', 'OLE44', 'OLE80513', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11589', 'OLE11589', 'OLE44', 'OLE80515', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11590', 'OLE11590', 'OLE10029', 'OLE80515', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11591', 'OLE11591', 'OLE10030', 'OLE80515', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11592', 'OLE11592', 'OLE10034', 'OLE80515', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11593', 'OLE11593', '59', 'OLE80516', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11594', 'OLE11594', 'OLE10029', 'OLE80516', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11595', 'OLE11595', 'OLE10030', 'OLE80516', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11596', 'OLE11596', 'OLE10034', 'OLE80516', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11597', 'OLE11597', 'OLE44', 'OLE80517', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11598', 'OLE11598', 'OLE10037', 'OLE80517', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11599', 'OLE11599', 'OLE10038', 'OLE80517', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11600', 'OLE11600', 'OLE10041', 'OLE80517', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11601', 'OLE11601', 'OLE10043', 'OLE80517', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11602', 'OLE11602', '59', 'OLE80518', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11603', 'OLE11603', 'OLE10037', 'OLE80518', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11604', 'OLE11604', 'OLE10038', 'OLE80518', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11605', 'OLE11605', 'OLE10041', 'OLE80518', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11606', 'OLE11606', 'OLE10043', 'OLE80518', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11607', 'OLE11607', 'OLE44', 'OLE80519', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11608', 'OLE11608', 'OLE10029', 'OLE80519', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11609', 'OLE11609', 'OLE10030', 'OLE80519', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11610', 'OLE11610', 'OLE10034', 'OLE80519', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11611', 'OLE11611', '59', 'OLE80520', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11612', 'OLE11612', 'OLE10029', 'OLE80520', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11613', 'OLE11613', 'OLE10030', 'OLE80520', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11614', 'OLE11614', 'OLE10034', 'OLE80520', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11615', 'OLE11615', 'OLE44', 'OLE80521', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11616', 'OLE11616', 'OLE10029', 'OLE80521', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11617', 'OLE11617', 'OLE10030', 'OLE80521', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11618', 'OLE11618', '59', 'OLE80522', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11619', 'OLE11619', 'OLE10029', 'OLE80522', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11620', 'OLE11620', 'OLE10030', 'OLE80522', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11621', 'OLE11621', 'OLE10000', 'OLE80523', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11622', 'OLE11622', 'OLE10038', 'OLE80523', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11623', 'OLE11623', 'OLE10042', 'OLE80523', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11624', 'OLE11624', 'OLE10043', 'OLE80523', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11625', 'OLE11625', '59', 'OLE80524', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11626', 'OLE11626', 'OLE10038', 'OLE80524', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11627', 'OLE11627', 'OLE10042', 'OLE80524', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11628', 'OLE11628', 'OLE10043', 'OLE80524', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11629', 'OLE11629', 'OLE54', 'OLE80166', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11630', 'OLE11630', 'OLE10000', 'OLE80166', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11631', 'OLE11631', 'OLE54', 'OLE80164', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11632', 'OLE11632', 'OLE10000', 'OLE80164', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE11633', 'OLE11633', 'OLE54', 'OLE80523', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10601', 'OLE10601', 'OLE10016', 'OLE10124', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10602', 'OLE10602', 'OLE10019', 'OLE10124', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10603', 'OLE10603', 'OLE10020', 'OLE10124', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10604', 'OLE10604', 'OLE10036', 'OLE10124', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10605', 'OLE10605', 'OLE10037', 'OLE10124', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10606', 'OLE10606', 'OLE10038', 'OLE10124', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10607', 'OLE10607', 'OLE10044', 'OLE10124', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10608', 'OLE10608', 'OLE10016', 'OLE10125', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10609', 'OLE10609', 'OLE10019', 'OLE10125', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10610', 'OLE10610', 'OLE10020', 'OLE10125', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10611', 'OLE10611', 'OLE10038', 'OLE10125', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10612', 'OLE10612', 'OLE10044', 'OLE10125', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10613', 'OLE10613', 'OLE10016', 'OLE10126', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10614', 'OLE10614', 'OLE10019', 'OLE10126', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10615', 'OLE10615', 'OLE10020', 'OLE10126', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10616', 'OLE10616', 'OLE10044', 'OLE10126', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10617', 'OLE10617', 'OLE10021', 'OLE10127', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10618', 'OLE10618', 'OLE10020', 'OLE10127', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10619', 'OLE10619', 'OLE10021', 'OLE10128', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10620', 'OLE10620', 'OLE10020', 'OLE10128', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10621', 'OLE10621', 'OLE10019', 'OLE10129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10622', 'OLE10622', 'OLE10016', 'OLE10129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10623', 'OLE10623', 'OLE10020', 'OLE10129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10624', 'OLE10624', 'OLE10036', 'OLE10129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10625', 'OLE10625', 'OLE10037', 'OLE10129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10626', 'OLE10626', 'OLE10038', 'OLE10129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10627', 'OLE10627', 'OLE10044', 'OLE10129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10628', 'OLE10628', 'OLE10033', 'OLE10129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10629', 'OLE10629', 'OLE10034', 'OLE10129', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10630', 'OLE10630', 'OLE10016', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10631', 'OLE10631', 'OLE10044', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10632', 'OLE10632', 'OLE10019', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10633', 'OLE10633', 'OLE10020', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10634', 'OLE10634', 'OLE10033', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10635', 'OLE10635', 'OLE10034', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10636', 'OLE10636', 'OLE10036', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10637', 'OLE10637', 'OLE10037', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10638', 'OLE10638', 'OLE10038', 'OLE394', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10639', '1203', 'OLE10046', 'OLE10130', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10640', '1204', 'OLE10046', 'OLE10131', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10641', '1205', 'OLE10046', 'OLE10132', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10642', '1209', 'OLE10047', 'OLE10131', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10643', '1210', 'OLE10047', 'OLE10132', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10644', '1019', 'OLE10049', 'OLE10133', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10645', '1020', 'OLE10050', 'OLE10134', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10646', '1021', 'OLE10049', 'OLE10134', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10647', 'OLE100051', 'OLE10053', 'OLE10135', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10648', 'OLE100061', 'OLE10057', 'OLE10136', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10649', 'OLE100071', 'OLE10051', 'OLE10137', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10650', '1013', 'OLE10046', 'OLE10138', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10651', '1014', 'OLE10046', 'OLE10139', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10652', '1015', 'OLE10046', 'OLE10140', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10653', '1016', 'OLE10046', 'OLE10141', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10654', '1017', 'OLE10046', 'OLE10142', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10655', '1018', 'OLE10046', 'OLE10143', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10656', '1022', 'OLE10058', 'OLE10144', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10657', '1023', 'OLE10058', 'OLE10145', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10658', '1024', 'OLE10058', 'OLE10146', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10659', '1025', 'OLE10058', 'OLE10147', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10660', '1026', 'OLE10058', 'OLE10148', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10661', '1027', 'OLE10058', 'OLE10149', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10662', '1028', 'OLE10058', 'OLE10150', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10663', '1029', 'OLE10058', 'OLE10151', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10664', '1030', 'OLE10058', 'OLE10152', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10665', '1031', 'OLE10058', 'OLE10153', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10666', '1032', 'OLE10058', 'OLE10154', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10667', '1033', 'OLE10058', 'OLE10155', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10668', '1034', 'OLE10058', 'OLE10156', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10669', '1035', 'OLE10058', 'OLE10157', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10670', '1036', 'OLE10058', 'OLE10158', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10671', '1037', 'OLE10058', 'OLE10159', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10672', '1038', 'OLE10058', 'OLE10160', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10673', '1039', 'OLE10058', 'OLE10161', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10674', '1040', 'OLE10058', 'OLE10162', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10675', '1041', 'OLE10058', 'OLE10163', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10676', '1042', 'OLE10058', 'OLE10164', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10677', '1043', 'OLE10058', 'OLE10165', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10678', '1044', 'OLE10058', 'OLE10166', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10679', '1045', 'OLE10058', 'OLE10167', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10680', '1046', 'OLE10058', 'OLE10168', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10681', 'OLE1040', 'OLE10057', 'OLE10169', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10682', 'OLE1041', 'OLE10057', 'OLE10170', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10683', 'OLE1042', 'OLE10057', 'OLE10171', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10684', 'OLE1043', 'OLE10057', 'OLE10172', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10685', 'OLE1044', 'OLE10057', 'OLE10173', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10686', 'OLE1045', 'OLE10057', 'OLE10174', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10687', 'OLE1046', 'OLE10057', 'OLE10175', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10688', 'OLE1047', 'OLE10057', 'OLE10176', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10689', 'OLE1048', 'OLE10057', 'OLE10177', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10690', 'OLE1049', 'OLE10057', 'OLE10178', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10691', 'OLE1050', 'OLE10057', 'OLE10179', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10692', 'OLE1051', 'OLE10057', 'OLE10180', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10693', 'OLE1052', 'OLE10057', 'OLE10181', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10694', 'OLE1053', 'OLE10057', 'OLE10182', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10695', 'OLE1054', 'OLE10057', 'OLE10183', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10696', 'OLE1055', 'OLE10057', 'OLE10184', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10697', 'OLE1056', 'OLE10055', 'OLE10185', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10700', '1102', 'OLE10061', 'OLE10187', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10701', '1103', 'OLE10062', 'OLE10188', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10703', 'OLE1057', 'OLE10052', 'OLE10190', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10704', 'OLE1058', 'OLE10053', 'OLE10190', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10705', 'OLE1059', 'OLE10054', 'OLE10190', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10706', 'OLE1060', 'OLE10057', 'OLE10190', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10707', 'OLE1061', 'OLE10052', 'OLE10191', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10708', 'OLE1062', 'OLE10053', 'OLE10191', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10709', 'OLE1063', 'OLE10054', 'OLE10191', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10710', 'OLE1064', 'OLE10057', 'OLE10191', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10711', '2100', 'OLE10063', 'OLE10192', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KR1000', 'D0C2892F47EE9142E0406E0AC31D23D6', 'KR1001', 'KR1000', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KR1001', 'D0C2892F47EF9142E0406E0AC31D23D6', 'KR1001', '181', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1003', 'CDD8032915D9BF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10003', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1004', 'CDD8032915DDBF1FE040F90A05B94313', 'KRSAP10004', 'KRSAP10004', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1005', 'CDD8032915E1BF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10005', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1006', 'CDD8032915E5BF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10006', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1007', 'CDD8032915E9BF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10007', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1008', 'CDD8032915EDBF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10008', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1009', 'CDD8032915F1BF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10009', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1010', 'CDD8032915F5BF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10010', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1011', 'CDD8032915F9BF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10011', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1012', 'CDD8032915FDBF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10012', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1013', 'CDD803291601BF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10013', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1014', 'CDD803291606BF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10014', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1015', 'CDD80329160BBF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10015', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('KRSAP1016', 'CDD803291610BF1FE040F90A05B94313', 'KRSAP10003', 'KRSAP10016', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('R1011', 'R1011', 'OLE10058', 'OLESSMD', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('R1012', 'R1012', 'OLE10058', 'OLEIITMD', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('R1013', 'R1013', 'OLE10058', 'OLEITAS', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('R1014', 'R1014', 'OLE10058', 'OLERSMD', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('R1015', 'R1015', 'OLE10058', 'OLESTSRC', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('R1016', 'R1016', 'OLE10058', 'OLETYPO', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('R102', 'R102', 'OLE10049', 'KRLMD1', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('R103', 'R103', 'OLE10050', 'KRLMD1', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('R104', 'R104', 'OLE10049', 'KRLLMD1', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10712', '3103', 'OLE10086', 'OLE10193', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10716', 'OLE2085', 'OLE10073', 'OLE10195', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10717', 'OLE2086', 'OLE10074', 'OLE10195', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10718', 'OLE2087', 'OLE10076', 'OLE10195', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10719', 'OLE2088', 'OLE10073', 'OLE10196', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10720', 'OLE2089', 'OLE10073', 'OLE10197', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10721', 'OLE2090', 'OLE10074', 'OLE10197', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10722', 'OLE2091', 'OLE10076', 'OLE10197', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10723', 'OLE2092', 'OLE10073', 'OLE10198', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10724', 'OLE2093', 'OLE10074', 'OLE10198', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10725', 'OLE2094', 'OLE10076', 'OLE10198', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10726', 'OLE2095', 'OLE10073', 'OLE10199', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10727', 'OLE2096', 'OLE10074', 'OLE10199', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10728', 'OLE2097', 'OLE10076', 'OLE10199', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10729', 'OLE2098', 'OLE10073', 'OLE10200', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10730', 'OLE2099', 'OLE10074', 'OLE10200', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10731', 'OLE20100', 'OLE10076', 'OLE10200', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10732', 'OLE20101', 'OLE10073', 'OLE10201', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10733', 'OLE20102', 'OLE10074', 'OLE10201', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10734', 'OLE20103', 'OLE10075', 'OLE10201', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10735', 'OLE20104', 'OLE10076', 'OLE10201', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10736', 'OLE20105', 'OLE10073', 'OLE10202', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10737', 'OLE20106', 'OLE10074', 'OLE10202', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10738', 'OLE20107', 'OLE10076', 'OLE10202', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10739', 'OLE20108', 'OLE10073', 'OLE10203', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10740', 'OLE20109', 'OLE10074', 'OLE10203', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10741', 'OLE20110', 'OLE10076', 'OLE10203', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10746', 'OLE20115', 'OLE10073', 'OLE10205', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10747', 'OLE20116', 'OLE10074', 'OLE10205', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10748', 'OLE20117', 'OLE10076', 'OLE10205', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10749', 'OLE20118', 'OLE10073', 'OLE10206', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10750', 'OLE20119', 'OLE10074', 'OLE10206', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10751', 'OLE20120', 'OLE10075', 'OLE10206', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10752', 'OLE20121', 'OLE10076', 'OLE10206', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10753', 'OLE20122', 'OLE10073', 'OLE10207', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10754', 'OLE20123', 'OLE10074', 'OLE10207', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10755', 'OLE20124', 'OLE10076', 'OLE10207', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10756', 'OLE20125', 'OLE10073', 'OLE10208', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10757', 'OLE20126', 'OLE10074', 'OLE10208', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10758', 'OLE20127', 'OLE10075', 'OLE10208', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10759', 'OLE20128', 'OLE10076', 'OLE10208', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10760', 'OLE20129', 'OLE10073', 'OLE10209', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10761', 'OLE20130', 'OLE10074', 'OLE10209', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10762', 'OLE20131', 'OLE10076', 'OLE10209', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10763', 'OLE20132', 'OLE10073', 'OLE10210', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10764', 'OLE20133', 'OLE10074', 'OLE10210', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10765', 'OLE20134', 'OLE10076', 'OLE10210', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10766', 'OLE20135', 'OLE10073', 'OLE10211', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10767', 'OLE20136', 'OLE10074', 'OLE10211', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10768', 'OLE20137', 'OLE10076', 'OLE10211', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10769', 'OLE20138', 'OLE10073', 'OLE10212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10770', 'OLE20139', 'OLE10074', 'OLE10212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10771', 'OLE20140', 'OLE10076', 'OLE10212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10776', 'OLE20145', 'OLE10073', 'OLE10214', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10777', 'OLE20146', 'OLE10074', 'OLE10214', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10778', 'OLE20147', 'OLE10076', 'OLE10214', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10795', 'OLE20168', 'OLE10075', 'OLE10195', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10812', 'OLE7022', 'OLE10077', 'OLE10223', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10813', 'OLE7023', 'OLE10078', 'OLE10223', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10814', 'OLE7024', 'OLE10079', 'OLE10223', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10815', 'OLE7025', 'OLE10082', 'OLE10223', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10816', 'OLE7026', 'OLE10077', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10817', 'OLE7027', 'OLE10078', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10818', 'OLE7028', 'OLE10079', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10819', 'OLE7029', 'OLE10080', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10820', 'OLE7030', 'OLE10081', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10821', 'OLE7031', 'OLE10082', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10822', 'OLE7032', 'OLE10083', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10823', 'OLE7033', 'OLE10084', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10824', 'OLE7034', 'OLE10077', 'OLE10225', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10825', 'OLE7035', 'OLE10078', 'OLE10225', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10826', 'OLE7036', 'OLE10079', 'OLE10225', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10827', 'OLE7037', 'OLE10080', 'OLE10225', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10828', 'OLE7038', 'OLE10081', 'OLE10225', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10829', 'OLE7039', 'OLE10082', 'OLE10225', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10830', 'OLE7040', 'OLE10077', 'OLE10226', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10831', 'OLE7041', 'OLE10078', 'OLE10226', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10832', 'OLE7042', 'OLE10079', 'OLE10226', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10833', 'OLE7043', 'OLE10080', 'OLE10226', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10834', 'OLE7044', 'OLE10081', 'OLE10226', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10835', 'OLE7045', 'OLE10082', 'OLE10226', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10836', 'OLE7046', 'OLE10077', 'OLE10227', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10837', 'OLE7047', 'OLE10078', 'OLE10227', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10838', 'OLE7048', 'OLE10079', 'OLE10227', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10839', 'OLE7049', 'OLE10080', 'OLE10227', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10840', 'OLE7050', 'OLE10082', 'OLE10227', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10841', 'OLE7051', 'OLE10077', 'OLE10228', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10842', 'OLE7052', 'OLE10078', 'OLE10228', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10843', 'OLE7053', 'OLE10079', 'OLE10228', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10844', 'OLE7054', 'OLE10080', 'OLE10228', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10845', 'OLE7055', 'OLE10077', 'OLE10229', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10846', 'OLE7056', 'OLE10078', 'OLE10229', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10847', 'OLE7057', 'OLE10079', 'OLE10229', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10848', 'OLE7058', 'OLE10080', 'OLE10229', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10849', 'OLE7059', 'OLE10081', 'OLE10229', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10850', 'OLE7060', 'OLE10077', 'OLE10230', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10851', 'OLE7061', 'OLE10078', 'OLE10230', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10852', 'OLE7062', 'OLE10079', 'OLE10230', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10853', 'OLE7063', 'OLE10080', 'OLE10230', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10854', 'OLE7064', 'OLE10081', 'OLE10230', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10855', 'OLE7065', 'OLE10077', 'OLE10231', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10856', 'OLE7066', 'OLE10078', 'OLE10231', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10857', 'OLE7067', 'OLE10079', 'OLE10231', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10858', 'OLE7068', 'OLE10080', 'OLE10231', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10859', 'OLE7070', 'OLE10077', 'OLE10207', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10860', 'OLE7071', 'OLE10078', 'OLE10207', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10861', 'OLE7072', 'OLE10079', 'OLE10207', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10862', 'OLE7073', 'OLE10080', 'OLE10207', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10863', 'OLE7074', 'OLE10081', 'OLE10207', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10864', 'OLE7075', 'OLE10077', 'OLE10233', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10865', 'OLE7076', 'OLE10078', 'OLE10233', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10866', 'OLE7077', 'OLE10079', 'OLE10233', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10867', 'OLE7078', 'OLE10080', 'OLE10233', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10868', 'OLE7079', 'OLE10081', 'OLE10233', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10869', 'OLE7080', 'OLE10082', 'OLE10233', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10870', 'OLE7081', 'OLE10077', 'OLE10234', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10871', 'OLE7082', 'OLE10078', 'OLE10234', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10872', 'OLE7083', 'OLE10079', 'OLE10234', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10873', 'OLE7084', 'OLE10080', 'OLE10234', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10874', 'OLE7085', 'OLE10081', 'OLE10234', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10875', 'OLE7086', 'OLE10082', 'OLE10234', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10876', 'OLE7090', 'OLE10077', 'OLE10235', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10877', 'OLE7091', 'OLE10078', 'OLE10235', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10878', 'OLE7092', 'OLE10079', 'OLE10235', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10879', 'OLE7093', 'OLE10080', 'OLE10235', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10880', 'OLE7094', 'OLE10081', 'OLE10235', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10881', 'OLE7095', 'OLE10082', 'OLE10235', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10882', 'OLE7096', 'OLE10077', 'OLE10236', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10883', 'OLE7097', 'OLE10078', 'OLE10236', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10884', 'OLE7098', 'OLE10079', 'OLE10236', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10885', 'OLE7099', 'OLE10080', 'OLE10236', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10886', 'OLE70100', 'OLE10082', 'OLE10236', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10887', 'OLE70101', 'OLE10077', 'OLE10237', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10888', 'OLE70102', 'OLE10078', 'OLE10237', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10889', 'OLE70103', 'OLE10079', 'OLE10237', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10890', 'OLE70104', 'OLE10080', 'OLE10237', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10891', 'OLE70105', 'OLE10082', 'OLE10237', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10892', 'OLE70106', 'OLE10077', 'OLE10238', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10893', 'OLE70107', 'OLE10078', 'OLE10238', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10894', 'OLE70108', 'OLE10079', 'OLE10238', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10895', 'OLE70109', 'OLE10080', 'OLE10238', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10896', 'OLE70110', 'OLE10081', 'OLE10238', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10897', 'OLE70111', 'OLE10082', 'OLE10238', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10898', 'OLE70112', 'OLE10083', 'OLE10238', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10899', 'OLE70113', 'OLE10084', 'OLE10238', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10900', 'OLE70114', 'OLE10077', 'OLE10239', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10901', 'OLE70115', 'OLE10078', 'OLE10239', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10902', 'OLE70116', 'OLE10079', 'OLE10239', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10903', 'OLE70117', 'OLE10080', 'OLE10239', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10904', 'OLE70118', 'OLE10082', 'OLE10239', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10905', 'OLE70119', 'OLE10077', 'OLE10240', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10906', 'OLE70120', 'OLE10078', 'OLE10240', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10907', 'OLE70121', 'OLE10079', 'OLE10240', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10908', 'OLE70122', 'OLE10082', 'OLE10240', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10909', 'OLE70123', 'OLE10077', 'OLE10241', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10910', 'OLE70124', 'OLE10078', 'OLE10241', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10911', 'OLE70125', 'OLE10079', 'OLE10241', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10912', 'OLE70126', 'OLE10080', 'OLE10241', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10913', 'OLE70127', 'OLE10082', 'OLE10241', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10914', 'OLE70128', 'OLE10077', 'OLE10242', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10915', 'OLE70129', 'OLE10078', 'OLE10242', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10916', 'OLE70130', 'OLE10079', 'OLE10242', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10917', 'OLE70131', 'OLE10080', 'OLE10242', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10918', 'OLE70132', 'OLE10082', 'OLE10242', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10919', 'OLE70133', 'OLE10077', 'OLE10243', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10920', 'OLE70134', 'OLE10078', 'OLE10243', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10921', 'OLE70135', 'OLE10082', 'OLE10243', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10922', 'OLE70136', 'OLE10083', 'OLE10243', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10923', 'OLE70137', 'OLE10077', 'OLE10244', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10924', 'OLE70138', 'OLE10078', 'OLE10244', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10925', 'OLE70139', 'OLE10082', 'OLE10244', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10926', 'OLE70140', 'OLE10083', 'OLE10244', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10927', 'OLE70141', 'OLE10077', 'OLE10232', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10928', 'OLE70142', 'OLE10078', 'OLE10232', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10929', 'OLE70143', 'OLE10079', 'OLE10232', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10930', 'OLE70144', 'OLE10080', 'OLE10232', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10931', 'OLE70145', 'OLE10081', 'OLE10232', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10932', 'OLE70146', 'OLE10082', 'OLE10232', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12094', '10090', 'OLE10064', 'OLE10269', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12095', '10091', 'OLE10065', 'OLE10269', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12096', '10092', 'OLE10066', 'OLE10269', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12097', '10093', 'OLE10067', 'OLE10269', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12098', '10094', 'OLE10068', 'OLE10269', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12099', '10095', 'OLE10064', 'OLE10270', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12100', '10096', 'OLE10065', 'OLE10270', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12101', '10097', 'OLE10066', 'OLE10270', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12102', '10098', 'OLE10067', 'OLE10270', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12103', '10099', 'OLE10068', 'OLE10270', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12104', 'OLE70309', 'OLE10077', 'OLE10271', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12105', 'OLE70310', 'OLE10078', 'OLE10271', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12106', 'OLE70311', 'OLE10082', 'OLE10271', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12107', 'OLE70312', 'OLE10083', 'OLE10271', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE9001', 'OLE9001', '98', 'OLE9001', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12109', 'OLE70313', 'OLE10082', 'OLE10228', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12110', 'OLE70314', 'OLE10083', 'OLE10228', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12111', 'OLE70315', 'OLE10084', 'OLE10228', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12112', 'OLE70316', 'OLE10082', 'OLE10229', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12113', 'OLE70317', 'OLE10082', 'OLE10230', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12114', 'OLE70318', 'OLE10082', 'OLE10231', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12115', 'OLE70319', 'OLE10082', 'OLE10207', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12116', 'OLE70320', 'OLE10077', 'OLE10212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12117', 'OLE70321', 'OLE10078', 'OLE10212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12118', 'OLE70322', 'OLE10079', 'OLE10212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12119', 'OLE70323', 'OLE10080', 'OLE10212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12120', 'OLE70324', 'OLE10081', 'OLE10212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12121', 'OLE70325', 'OLE10082', 'OLE10212', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12122', 'OLE70326', 'OLE10058', 'OLE10272', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12123', 'OLE70327', 'OLE10077', 'OLE10273', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12124', 'OLE70328', 'OLE10078', 'OLE10273', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12125', 'OLE70329', 'OLE10079', 'OLE10273', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12126', 'OLE70330', 'OLE10080', 'OLE10273', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12128', 'OLE70332', 'OLE10082', 'OLE10273', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12130', 'OLE70334', 'OLE10077', 'OLE10274', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12131', 'OLE70335', 'OLE10078', 'OLE10274', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12132', 'OLE70336', 'OLE10079', 'OLE10274', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12133', 'OLE70337', 'OLE10080', 'OLE10274', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12134', 'OLE70338', 'OLE10081', 'OLE10274', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12135', 'OLE70339', 'OLE10082', 'OLE10274', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12137', 'OLE70341', 'OLE10077', 'OLE10275', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12138', 'OLE70342', 'OLE10078', 'OLE10275', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12139', 'OLE70343', 'OLE10079', 'OLE10275', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12140', 'OLE70344', 'OLE10080', 'OLE10275', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12141', 'OLE70345', 'OLE10081', 'OLE10275', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12142', 'OLE70346', 'OLE10082', 'OLE10275', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12144', 'OLE70348', 'OLE10073', 'OLE10276', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12145', 'OLE70349', 'OLE10074', 'OLE10276', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12146', 'OLE70350', 'OLE10076', 'OLE10276', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12147', 'OLE70351', 'OLE10075', 'OLE10277', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12148', 'OLE70352', 'OLE10077', 'OLE10278', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12149', 'OLE70353', 'OLE10078', 'OLE10278', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12150', 'OLE70354', 'OLE10079', 'OLE10278', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12151', 'OLE70355', 'OLE10080', 'OLE10278', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12152', 'OLE70356', 'OLE10081', 'OLE10278', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12153', 'OLE70357', 'OLE10082', 'OLE10278', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12154', 'OLE70358', 'OLE10086', 'OLE10279', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12156', 'OLE70360', 'OLE10027', 'OLE10280', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12157', 'OLE70361', 'OLE10028', 'OLE10280', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12158', 'OLE70362', 'OLE10029', 'OLE10280', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12159', 'OLE70363', 'OLE10030', 'OLE10280', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12160', 'OLE70364', 'OLE10027', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12161', 'OLE70365', 'OLE10028', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12162', 'OLE70366', 'OLE10029', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12163', 'OLE70367', 'OLE10030', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12164', 'OLE70368', 'OLE10040', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12165', 'OLE70369', 'OLE10041', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12166', 'OLE70370', 'OLE10042', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12167', 'OLE70371', 'OLE10043', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12168', 'OLE70372', 'OLE10028', 'OLE10122', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12169', 'OLE70373', 'OLE10029', 'OLE10122', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12170', 'OLE70374', 'OLE10027', 'OLE92', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12171', 'OLE70375', 'OLE10028', 'OLE92', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12172', 'OLE70376', 'OLE10029', 'OLE92', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12173', 'OLE70377', 'OLE10030', 'OLE92', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12174', 'OLE70378', 'OLE10027', 'OLE100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12175', 'OLE70379', 'OLE10028', 'OLE100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12176', 'OLE70380', 'OLE10029', 'OLE100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12177', 'OLE70381', 'OLE10030', 'OLE100', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12178', 'OLE70382', 'OLE10027', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12179', 'OLE70383', 'OLE10028', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12180', 'OLE70384', 'OLE10029', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12181', 'OLE70385', 'OLE10030', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12182', 'OLE70386', 'OLE10040', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12183', 'OLE70387', 'OLE10041', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12184', 'OLE70388', 'OLE10042', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12185', 'OLE70389', 'OLE10043', 'OLE97', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12186', 'OLE70390', 'OLE10027', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12187', 'OLE70391', 'OLE10028', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12188', 'OLE70392', 'OLE10029', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12189', 'OLE70393', 'OLE10030', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12190', 'OLE70394', 'OLE10040', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12191', 'OLE70395', 'OLE10041', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12192', 'OLE70396', 'OLE10042', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12193', 'OLE70397', 'OLE10043', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12194', 'OLE70398', 'OLE10033', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12195', 'OLE70399', 'OLE10034', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12196', 'OLE70400', 'OLE10037', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12197', 'OLE70401', 'OLE10038', 'OLE94', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12198', 'OLE70402', 'OLE10033', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12199', 'OLE70403', 'OLE10034', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12200', 'OLE70404', 'OLE10037', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12201', 'OLE70405', 'OLE10038', 'OLE268', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12202', 'OLE70406', 'OLE10027', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12203', 'OLE70407', 'OLE10028', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12204', 'OLE70408', 'OLE10029', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12205', 'OLE70409', 'OLE10030', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12206', 'OLE70410', 'OLE10033', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12207', 'OLE70411', 'OLE10034', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12208', 'OLE70412', 'OLE10037', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12209', 'OLE70413', 'OLE10038', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12210', 'OLE70414', 'OLE10040', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12211', 'OLE70415', 'OLE10041', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12212', 'OLE70416', 'OLE10042', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12213', 'OLE70417', 'OLE10043', 'OLE10067', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12214', 'OLE70418', 'OLE10036', 'OLE356', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12215', 'OLE70419', 'OLE10037', 'OLE356', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12216', 'OLE70420', 'OLE10038', 'OLE356', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12217', 'OLE70421', 'OLE10036', 'OLE80161', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12218', 'OLE70422', 'OLE10037', 'OLE80161', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12219', 'OLE70423', 'OLE10038', 'OLE80161', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12220', 'OLE70424', 'OLE10036', 'OLE10281', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12221', 'OLE70425', 'OLE10037', 'OLE10281', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12222', 'OLE70426', 'OLE10038', 'OLE10281', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12223', 'OLE70427', 'OLE10086', 'OLE404', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12229', 'OLE70433', 'OLE10036', 'OLE80158', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12230', 'OLE70434', 'OLE10037', 'OLE80158', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12231', 'OLE70435', 'OLE10038', 'OLE80158', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12232', 'OLE70436', 'OLE10036', 'OLE80160', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12233', 'OLE70437', 'OLE10037', 'OLE80160', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12234', 'OLE70438', 'OLE10038', 'OLE80160', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12235', 'OLE70439', 'OLE10073', 'OLE10282', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12236', 'OLE70440', 'OLE10074', 'OLE10282', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12237', 'OLE70441', 'OLE10076', 'OLE10282', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12238', 'OLE70442', 'OLE10073', 'OLE10283', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12239', 'OLE70443', 'OLE10074', 'OLE10283', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12240', 'OLE70444', 'OLE10076', 'OLE10283', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12241', 'OLE70445', 'OLE10073', 'OLE10284', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12242', 'OLE70446', 'OLE10074', 'OLE10284', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12243', 'OLE70447', 'OLE10076', 'OLE10284', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12244', 'OLE70448', 'OLE10073', 'OLE10285', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12245', 'OLE70449', 'OLE10074', 'OLE10285', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12246', 'OLE70450', 'OLE10076', 'OLE10285', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12247', 'OLE70451', 'OLE10073', 'OLE10286', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12248', 'OLE70452', 'OLE10074', 'OLE10286', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12249', 'OLE70453', 'OLE10076', 'OLE10286', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12250', 'OLE70454', 'OLE10073', 'OLE10287', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12251', 'OLE70455', 'OLE10074', 'OLE10287', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12252', 'OLE70456', 'OLE10076', 'OLE10287', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12253', 'OLE70457', 'OLE10073', 'OLE10288', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12254', 'OLE70458', 'OLE10074', 'OLE10288', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12255', 'OLE70459', 'OLE10076', 'OLE10288', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12256', 'OLE70460', 'OLE10073', 'OLE10289', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12257', 'OLE70461', 'OLE10074', 'OLE10289', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12258', 'OLE70462', 'OLE10076', 'OLE10289', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12259', 'OLE70463', 'OLE10073', 'OLE10290', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12260', 'OLE70464', 'OLE10074', 'OLE10290', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12261', 'OLE70465', 'OLE10076', 'OLE10290', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12262', 'OLE70466', 'OLE10073', 'OLE10291', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12263', 'OLE70467', 'OLE10074', 'OLE10291', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12264', 'OLE70468', 'OLE10076', 'OLE10291', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12265', 'OLE70469', 'OLE10073', 'OLE10292', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12266', 'OLE70470', 'OLE10074', 'OLE10292', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12267', 'OLE70471', 'OLE10076', 'OLE10292', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12268', 'OLE70472', 'OLE10073', 'OLE10293', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12269', 'OLE70473', 'OLE10074', 'OLE10293', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12270', 'OLE70474', 'OLE10076', 'OLE10293', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12271', 'OLE70475', 'OLE10077', 'OLE10294', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12272', 'OLE70476', 'OLE10078', 'OLE10294', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12273', 'OLE70477', 'OLE10079', 'OLE10294', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12274', 'OLE70478', 'OLE10082', 'OLE10294', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12275', 'OLE70479', 'OLE10073', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12276', 'OLE70480', 'OLE10074', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12277', 'OLE70481', 'OLE10076', 'OLE10224', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12278', 'OLE70482', 'OLE10073', 'OLE10234', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12279', 'OLE70483', 'OLE10074', 'OLE10234', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12280', 'OLE70484', 'OLE10076', 'OLE10234', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12281', 'OLE70485', 'OLE10077', 'OLE10295', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12282', 'OLE70486', 'OLE10078', 'OLE10295', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12283', 'OLE70487', 'OLE10079', 'OLE10295', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12284', 'OLE70488', 'OLE10080', 'OLE10295', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12285', 'OLE70489', 'OLE10081', 'OLE10295', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12286', 'OLE70490', 'OLE10082', 'OLE10295', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12287', 'OLE70491', 'OLE10083', 'OLE10295', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12288', 'OLE70492', 'OLE10077', 'OLE10296', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12289', 'OLE70493', 'OLE10078', 'OLE10296', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12290', 'OLE70494', 'OLE10079', 'OLE10296', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12291', 'OLE70495', 'OLE10080', 'OLE10296', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12292', 'OLE70496', 'OLE10081', 'OLE10296', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12293', 'OLE70497', 'OLE10082', 'OLE10296', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12294', 'OLE70498', 'OLE10083', 'OLE10296', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12296', 'OLE70500', 'OLE10073', 'OLE10298', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12297', 'OLE70501', 'OLE10074', 'OLE10298', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12298', 'OLE70502', 'OLE10076', 'OLE10298', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12299', 'OLE70503', 'OLE10073', 'OLE10299', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12300', 'OLE70504', 'OLE10074', 'OLE10299', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12301', 'OLE70505', 'OLE10076', 'OLE10299', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12302', 'OLE70506', 'OLE10073', 'OLE10300', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12303', 'OLE70507', 'OLE10074', 'OLE10300', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12304', 'OLE70508', 'OLE10076', 'OLE10300', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12305', 'OLE70509', 'OLE10073', 'OLE10301', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12306', 'OLE70510', 'OLE10074', 'OLE10301', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12307', 'OLE70511', 'OLE10075', 'OLE10301', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12308', 'OLE70512', 'OLE10076', 'OLE10301', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12309', 'OLE70513', 'OLE10073', 'OLE10302', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12310', 'OLE70514', 'OLE10074', 'OLE10302', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12311', 'OLE70515', 'OLE10076', 'OLE10302', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12312', 'OLE70516', 'OLE10073', 'OLE10303', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12313', 'OLE70517', 'OLE10074', 'OLE10303', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12314', 'OLE70518', 'OLE10076', 'OLE10303', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12315', 'OLE70519', 'OLE10073', 'OLE10304', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12316', 'OLE70520', 'OLE10074', 'OLE10304', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12317', 'OLE70521', 'OLE10076', 'OLE10304', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12318', 'OLE70522', 'OLE10073', 'OLE10305', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12319', 'OLE70523', 'OLE10074', 'OLE10305', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12320', 'OLE70524', 'OLE10076', 'OLE10305', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12321', 'OLE70525', 'OLE10073', 'OLE10306', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12322', 'OLE70526', 'OLE10074', 'OLE10306', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12323', 'OLE70527', 'OLE10076', 'OLE10306', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12324', 'OLE70528', 'OLE10073', 'OLE10307', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12325', 'OLE70529', 'OLE10074', 'OLE10307', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12326', 'OLE70530', 'OLE10076', 'OLE10307', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12327', 'OLE70531', 'OLE10073', 'OLE10308', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12328', 'OLE70532', 'OLE10074', 'OLE10308', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12329', 'OLE70533', 'OLE10073', 'OLE10309', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12330', 'OLE70534', 'OLE10074', 'OLE10309', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12333', 'OLE70537', 'OLE10073', 'OLE10311', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12334', 'OLE70538', 'OLE10074', 'OLE10311', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12335', 'OLE70539', 'OLE10073', 'OLE10312', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12336', 'OLE70540', 'OLE10074', 'OLE10312', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12337', 'OLE70541', 'OLE10073', 'OLE10313', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12338', 'OLE70542', 'OLE10074', 'OLE10313', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12339', 'OLE70543', 'OLE10076', 'OLE10311', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12340', 'OLE70544', 'OLE10076', 'OLE10312', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12341', 'OLE70545', 'OLE10073', 'OLE10314', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12342', 'OLE70546', 'OLE10074', 'OLE10314', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12343', 'OLE70547', 'OLE10073', 'OLE10315', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12344', 'OLE70548', 'OLE10074', 'OLE10315', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12345', 'OLE70549', 'OLE10085', 'OLE10316', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12346', 'OLE70550', 'OLE10085', 'OLE10317', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12347', 'OLE70551', 'OLE10086', 'OLE10318', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12348', 'OLE70552', 'OLE10086', 'OLE10319', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12349', 'OLE70553', 'OLE10086', 'OLE10320', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12350', 'OLE70554', 'OLE10086', 'OLE10321', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12351', 'OLE70555', 'OLE10086', 'OLE10322', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12352', 'OLE70556', 'OLE10086', 'OLE10323', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12353', 'OLE70557', 'OLE10073', 'OLE10324', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12354', 'OLE12354', 'OLE10074', 'OLE10324', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12355', 'OLE12355', 'OLE10073', 'OLE10325', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12356', 'OLE12356', 'OLE10074', 'OLE10325', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12357', 'OLE12357', 'OLE10076', 'OLE10325', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12358', 'OLE70558', 'OLE10068', 'OLE10327', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12359', 'OLE70559', 'OLE10071', 'OLE10326', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12360', 'OLE12360', 'OLE10071', 'OLE10327', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12361', 'OLE12361', 'OLE10006', 'OLE10326', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12362', 'OLE12362', 'OLE10006', 'OLE10327', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12363', 'OLE12363', 'OLE10064', 'OLE10328', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12364', 'OLE12364', 'OLE10065', 'OLE10328', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12365', 'OLE12365', 'OLE10066', 'OLE10328', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12366', 'OLE12366', 'OLE10067', 'OLE10328', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12367', 'OLE12367', 'OLE10068', 'OLE10328', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12368', 'OLE12308', 'OLE10064', 'OLE10329', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12369', 'OLE12369', 'OLE10065', 'OLE10329', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12370', 'OLE12370', 'OLE10066', 'OLE10329', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12371', 'OLE12371', 'OLE10067', 'OLE10329', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12372', 'OLE12372', 'OLE10068', 'OLE10329', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12373', 'OLE70575', 'OLE44', 'OLE10330', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12500', 'OLE12500', 'OLE10064', 'OLE10500', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12501', 'OLE12501', 'OLE10065', 'OLE10500', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12502', 'OLE12502', 'OLE10066', 'OLE10500', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12503', 'OLE12503', 'OLE10067', 'OLE10500', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12504', 'OLE12504', 'OLE10068', 'OLE10500', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12505', 'OLE12505', 'OLE44', 'OLE10501', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10800', 'OLE10800', 'OLE10076', 'OLE10502', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10801', 'OLE10801', 'OLE10076', 'OLE10503', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10802', 'OLE10802', 'OLE10074', 'OLE10504', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10803', 'OLE10803', 'OLE10074', 'OLE10505', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10804', 'OLE10804', 'OLE10073', 'OLE10502', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10805', 'OLE10805', 'OLE10073', 'OLE10503', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10806', 'OLE10806', 'OLE10073', 'OLE10504', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10807', 'OLE10807', 'OLE10073', 'OLE10505', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE10808', 'OLE10808', 'OLE10073', 'OLE10506', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12506', 'OLE12506', 'OLE10077', 'OLE3010502', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12507', 'OLE12507', 'OLE10078', 'OLE3010502', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12508', 'OLE12508', 'OLE10079', 'OLE3010502', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12509', 'OLE12509', 'OLE10080', 'OLE3010502', 'Y')
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12510', 'OLE12510', 'OLE10082', 'OLE3010502', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_ROLE_PERM_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 20, '7:1963452f72b687a2de16f6e8b1074898', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_KRIM_ROLE_RSP_T::ole
INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10002', 'OLE10002', 'OLE10012', 'OLE990009', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10005', 'OLE10005', 'OLE10012', 'OLE990016', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10006', 'OLE10006', 'OLE10003', 'OLE990002', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10011', 'OLE10011', 'OLE10017', 'OLE990009', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10014', 'OLE10014', 'OLE10004', 'OLE990018', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10015', 'OLE10015', 'OLE10012', 'OLE990019', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10016', 'OLE10016', 'OLE10016', 'OLE990020', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10017', 'OLE10017', 'OLE10019', 'OLE990020', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10018', 'OLE10018', 'OLE10019', 'OLE990021', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10019', 'OLE10019', 'OLE10021', 'OLE990022', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10020', 'OLE10020', 'OLE10022', 'OLE990018', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10022', 'OLE10022', 'OLE10024', 'OLE990016', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10023', 'OLE10023', 'OLE10024', 'OLE990019', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10024', 'OLE10024', 'OLE10012', 'OLE990023', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10029', 'OLE10029', 'OLE10012', 'OLE990024', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE10032', 'OLE10032', 'OLE10003', 'OLE990025', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80001', 'OLE80001', 'OLE10021', 'OLE80004', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80002', 'OLE80002', 'OLE10012', 'OLE80006', 'N')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80003', 'OLE80003', 'OLE10012', 'OLE80008', 'N')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80004', 'OLE80004', 'OLE10012', 'OLE80009', 'N')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80005', 'OLE80005', 'OLE10019', 'OLE80010', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80006', 'OLE80006', 'OLE10012', 'OLE80012', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80007', 'OLE80007', 'OLE10012', 'OLE80008', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80008', 'OLE80008', 'OLE10012', 'OLE80006', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80009', 'OLE80009', 'OLE10012', 'OLE80009', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80010', 'OLE80010', 'OLE10012', 'OLE80013', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80011', 'OLE80011', 'OLE10012', 'OLE80015', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80012', 'OLE80012', 'OLE10012', 'OLE80018', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80013', 'OLE80013', 'OLE10012', 'OLE80019', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80014', 'OLE80014', 'OLE10012', 'OLE80020', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE80015', 'OLE80015', 'OLE10003', 'OLE80021', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1126', 'OLE1126', 'OLE10004', 'OLE990026', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1127', 'OLE1127', 'OLE10044', 'OLE990034', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1128', 'OLE1128', 'OLE10021', 'OLE990035', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1129', 'OLE1129', 'OLE10020', 'OLE990036', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1130', 'OLE1130', 'OLE10044', 'OLE990030', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1131', 'OLE1131', 'OLE10020', 'OLE990031', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1132', 'OLE1132', 'OLE10044', 'OLE990027', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1133', 'OLE1133', 'OLE10020', 'OLE990028', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1134', 'OLE1134', 'OLE10021', 'OLE990029', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1135', 'OLE1135', 'OLE10045', 'OLE990032', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1136', 'OLE1136', 'OLE10045', 'OLE990037', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1137', 'OLE1137', 'OLE41', 'OLE990033', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1138', 'OLE1138', 'OLE41', 'OLE990038', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1139', 'OLE1139', 'OLE10044', 'OLE990039', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1140', 'OLE1140', 'OLE41', 'OLE990040', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1141', 'OLE1141', 'OLE10044', 'OLE990041', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1142', 'OLE1142', 'OLE55', 'OLE990042', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1143', 'OLE1143', 'OLE10042', 'OLE990043', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1144', 'OLE1144', 'OLE10043', 'OLE990043', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1145', 'OLE1145', 'OLE10042', 'OLE990044', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1146', 'OLE1146', 'OLE10043', 'OLE990044', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1147', 'OLE1147', 'OLE79', 'OLE990045', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1148', 'OLE1148', 'OLE79', 'OLE990046', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1149', 'OLE1149', 'OLE79', 'OLE990047', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1150', 'OLE1150', 'OLE79', 'OLE990048', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1151', 'OLE1151', 'OLE79', 'OLE990049', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1152', 'OLE1152', 'OLE79', 'OLE990050', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1153', 'OLE1153', 'OLE79', 'OLE990051', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1154', 'OLE1154', 'OLE10021', 'OLE990052', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1155', 'OLE1155', 'OLE10044', 'OLE990053', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1156', 'OLE1156', 'OLE10020', 'OLE990054', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1157', 'OLE1157', 'OLE10044', 'OLE990055', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1158', 'OLE1158', 'OLE55', 'OLE990056', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1159', 'OLE1159', 'OLE79', 'OLE990057', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1160', 'OLE1160', 'OLE79', 'OLE990058', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1161', 'OLE1161', 'OLE10066', 'OLE990059', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1162', 'OLE1162', 'OLE10069', 'OLE990060', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1163', 'OLE1163', 'OLE10072', 'OLE990061', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1164', 'OLE1164', 'OLE10019', 'OLE73', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_T (ROLE_RSP_ID, OBJ_ID, ROLE_ID, RSP_ID, ACTV_IND) VALUES ('OLE1165', 'OLE1165', 'OLE10019', 'OLE101', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_ROLE_RSP_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 21, '7:d68cd5a8ecde7caeb6afe987a13f1333', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_KRIM_ROLE_RSP_ACTN_T::ole
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10002', 'OLE10002', 'A', '1', 'F', '*', 'OLE10002', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10005', 'OLE10005', 'F', '1', 'A', '*', 'OLE10005', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10006', 'OLE10006', 'F', '1', 'A', '*', 'OLE10006', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10011', 'OLE10011', 'A', '1', 'F', '*', 'OLE10011', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10014', 'OLE10014', 'F', '1', 'A', '*', 'OLE10014', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10015', 'OLE10015', 'F', '1', 'A', '*', 'OLE10015', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10016', 'OLE10016', 'A', '1', 'F', '*', 'OLE10016', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10017', 'OLE10017', 'A', '1', 'F', '*', 'OLE10017', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10018', 'OLE10018', 'A', '1', 'F', '*', 'OLE10018', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10019', 'OLE10019', 'A', '1', 'F', '*', 'OLE10019', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10020', 'OLE10020', 'F', '1', 'A', '*', 'OLE10020', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10022', 'OLE10022', 'F', '1', 'A', '*', 'OLE10022', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10023', 'OLE10023', 'F', '1', 'A', '*', 'OLE10023', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10024', 'OLE10024', 'A', '1', 'F', '*', 'OLE10024', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10026', 'OLE10026', 'A', '1', 'F', '*', 'OLE10026', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10027', 'OLE10027', 'A', '1', 'F', '*', 'OLE10027', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10028', 'OLE10028', 'A', '1', 'F', '*', 'OLE10028', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10029', 'OLE10029', 'A', '1', 'F', '*', 'OLE10029', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10030', 'OLE10030', 'A', '1', 'F', '*', 'OLE10030', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10031', 'OLE10031', 'A', '1', 'F', '*', 'OLE10031', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10032', 'OLE10032', 'A', '1', 'F', '*', 'OLE10032', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80006', 'OLE80006', 'A', '1', 'F', 'OLE10079', '*', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80007', 'OLE80007', 'A', '1', 'F', 'OLE10086', '*', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80008', 'OLE80008', 'A', '1', 'F', '*', 'OLE80005', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80009', 'OLE80009', 'A', '1', 'F', '*', 'OLE80006', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80010', 'OLE80010', 'A', '1', 'F', '*', 'OLE80010', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80011', 'OLE80011', 'A', '1', 'F', '*', 'OLE80011', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80012', 'OLE80012', 'A', '1', 'F', '*', 'OLE80012', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80013', 'OLE80013', 'A', '1', 'F', '*', 'OLE80013', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80014', 'OLE80014', 'A', '1', 'F', '*', 'OLE80014', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80015', 'OLE80015', 'A', '1', 'F', '*', 'OLE80007', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80016', 'OLE80016', 'A', '1', 'F', '*', 'OLE80008', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80017', 'OLE80017', 'A', '1', 'F', '*', 'OLE80009', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80018', 'OLE80018', 'A', '1', 'F', '*', 'OLE80001', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80019', 'OLE80019', 'F', '1', 'F', 'OLE10022', '*', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80020', 'OLE80020', 'F', '1', 'F', 'OLE10046', '*', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE80021', 'OLE80021', 'F', '1', 'F', 'OLE10059', '*', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10033', 'OLE10033', 'A', '1', 'F', '*', 'OLE1126', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10034', 'OLE10034', 'A', '1', 'F', '*', 'OLE1127', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10035', 'OLE10035', 'A', '1', 'F', '*', 'OLE1128', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10036', 'OLE10036', 'A', '1', 'F', '*', 'OLE1129', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10037', 'OLE10037', 'A', '1', 'F', '*', 'OLE1130', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10038', 'OLE10038', 'A', '1', 'F', '*', 'OLE1131', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10039', 'OLE10039', 'A', '1', 'F', '*', 'OLE1132', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10040', 'OLE10040', 'A', '1', 'F', '*', 'OLE1133', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10041', 'OLE10041', 'A', '1', 'F', '*', 'OLE1134', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10042', 'OLE10042', 'A', '1', 'F', '*', 'OLE1135', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10043', 'OLE10043', 'A', '1', 'F', '*', 'OLE1136', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10044', 'OLE10044', 'A', '1', 'F', '*', 'OLE1137', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10045', 'OLE10045', 'A', '1', 'F', '*', 'OLE1138', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10046', 'OLE10046', 'A', '1', 'F', '*', 'OLE1139', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10047', 'OLE10047', 'A', '1', 'F', '*', 'OLE1140', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10048', 'OLE10048', 'F', '1', 'F', 'OLE10115', '*', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10049', 'OLE10049', 'F', '1', 'F', 'OLE10114', '*', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10050', 'OLE10050', 'A', '1', 'F', '*', 'OLE1142', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10051', 'OLE10051', 'F', '1', 'F', '*', 'OLE1143', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10052', 'OLE10052', 'F', '1', 'F', '*', 'OLE1144', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10053', 'OLE10053', 'F', '1', 'F', '*', 'OLE1145', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10054', 'OLE10054', 'F', '1', 'F', '*', 'OLE1146', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10055', 'OLE10055', 'A', '1', 'A', '*', 'OLE1147', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10056', 'OLE10056', 'A', '1', 'A', '*', 'OLE1148', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10057', 'OLE10057', 'A', '1', 'A', '*', 'OLE1149', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10058', 'OLE10058', 'F', '1', 'A', '*', 'OLE1150', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10059', 'OLE10059', 'F', '1', 'A', '*', 'OLE1151', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10060', 'OLE10060', 'F', '1', 'A', '*', 'OLE1152', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10061', 'OLE10061', 'F', '1', 'A', '*', 'OLE1153', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10062', 'OLE10062', 'A', '1', 'F', '*', 'OLE1154', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10063', 'OLE10063', 'A', '1', 'F', '*', 'OLE1155', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10064', 'OLE10064', 'A', '1', 'F', '*', 'OLE1156', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10065', 'OLE10065', 'A', '1', 'A', '*', 'OLE1157', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10066', 'OLE10066', 'A', '1', 'F', '*', 'OLE1158', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10067', 'OLE10067', 'A', '1', 'A', '*', 'OLE1159', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10068', 'OLE10068', 'F', '1', 'A', '*', 'OLE1160', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10069', 'OLE10069', 'A', '1', 'A', '*', 'OLE1161', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10070', 'OLE10070', 'A', '1', 'A', '*', 'OLE1162', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10071', 'OLE10071', 'A', '1', 'F', '*', 'OLE1163', 'N')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10072', 'OLE10072', 'A', '1', 'F', '*', 'OLE1164', 'Y')
/

INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID, OBJ_ID, ACTN_TYP_CD, PRIORITY_NBR, ACTN_PLCY_CD, ROLE_MBR_ID, ROLE_RSP_ID, FRC_ACTN) VALUES ('OLE10073', 'OLE10073', 'A', '1', 'F', '*', 'OLE1165', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_ROLE_RSP_ACTN_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 22, '7:a29556cfc0318f8c88632228598decec', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_role_perm_data.xml::OLE_KRIM_ROLE_MBR_T::ole
INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_ROLE_MBR_T', 'ole', 'bootstrap_krim_role_perm_data.xml', SYSTIMESTAMP, 23, '7:baf090ef58a6e8d594b6ad51b2808196', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Release Database Lock
-- Release Database Lock
