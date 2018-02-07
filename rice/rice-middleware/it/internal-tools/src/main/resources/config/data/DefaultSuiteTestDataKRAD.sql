--
-- Copyright 2005-2013 The Kuali Foundation
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

INSERT INTO KRCR_NMSPC_T(NMSPC_CD, OBJ_ID, VER_NBR, NM, ACTV_IND, APPL_ID)
  VALUES('KR-NS', '53680C68F595AD9BE0404F8189D80A6C', 1, 'Kuali Nervous System', 'Y', NULL)
/
INSERT INTO KRCR_NMSPC_T(NMSPC_CD, OBJ_ID, VER_NBR, NM, ACTV_IND, APPL_ID)
  VALUES('KUALI', '5ADF18B6D4817954E0404F8189D85002', 1, 'Kuali Systems', 'Y', NULL)
/
INSERT INTO KRCR_NMSPC_T(NMSPC_CD, OBJ_ID, VER_NBR, NM, ACTV_IND, APPL_ID)
  VALUES('KR-WKFLW', '5E1D690C419B3E2EE0404F8189D82677', 0, 'Workflow', 'Y', NULL)
/
INSERT INTO KRCR_NMSPC_T(NMSPC_CD, OBJ_ID, VER_NBR, NM, ACTV_IND, APPL_ID)
  VALUES('KR-IDM', '61645D045B0005D7E0404F8189D849B1', 1, 'Identity Management', 'Y', NULL)
/
INSERT INTO KRCR_NMSPC_T(NMSPC_CD, OBJ_ID, VER_NBR, NM, ACTV_IND, APPL_ID)
  VALUES('KR-NTFCN', '5B960CFDBB360FDFE0404F8189D83CBD', 1, 'Notification', 'Y', NULL)
/
INSERT INTO KRCR_NMSPC_T(NMSPC_CD, OBJ_ID, VER_NBR, NM, ACTV_IND, APPL_ID)
  VALUES('KR-BUS', '5B960CFDBB370FDFE0404F8189D83CBD', 1, 'Service Bus', 'Y', NULL)
/
INSERT INTO KRCR_NMSPC_T(NMSPC_CD, OBJ_ID, VER_NBR, NM, ACTV_IND, APPL_ID)
  VALUES('KR-SYS', '5B960CFDBB390FDFE0404F8189D83CBD', 1, 'Enterprise Infrastructure', 'Y', NULL)
/

-- Parameters Types: extracted from DBA using following statement
-- select * from KRCR_PARM_TYP_T

INSERT INTO KRCR_PARM_TYP_T(PARM_TYP_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('AUTH', '53680C68F593AD9BE0404F8189D80A6C', 1, 'Authorization', 'Y')
/
INSERT INTO KRCR_PARM_TYP_T(PARM_TYP_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('CONFG', '53680C68F591AD9BE0404F8189D80A6C', 1, 'Config', 'Y')
/
INSERT INTO KRCR_PARM_TYP_T(PARM_TYP_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('HELP', '53680C68F594AD9BE0404F8189D80A6C', 1, 'Help', 'Y')
/
INSERT INTO KRCR_PARM_TYP_T(PARM_TYP_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('VALID', '53680C68F592AD9BE0404F8189D80A6C', 1, 'Document Validation', 'Y')
/

-- Parameters Detail Types: extracted from DBA using following statement
-- select * from KRCR_CMPNT_T

INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-NS', 'All', '53680C68F596AD9BE0404F8189D80A6C', 1, 'All', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-NS', 'Batch', '53680C68F597AD9BE0404F8189D80A6C', 1, 'Batch', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-NS', 'Document', '53680C68F598AD9BE0404F8189D80A6C', 1, 'Document', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-NS', 'Lookup', '53680C68F599AD9BE0404F8189D80A6C', 1, 'Lookup', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-NS', 'PurgePendingAttachmentsStep', '5A689075D3577AEBE0404F8189D80321', 1, 'Purge Pending Attachments Step', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-NS', 'PurgeSessionDocumentsStep', '5A689075D3567AEBE0404F8189D80321', 1, 'Purge Session Documents Step', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-NS', 'ScheduleStep', '5A689075D3587AEBE0404F8189D80321', 1, 'Schedule Step', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'ActionList', '1821D8BAB21E498F9FB1ECCA25C37F9B', 1, 'Action List', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'Backdoor', 'F7E44233C2C440FFB1A399548951160A', 1, 'Backdoor', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'DocumentSearch', '18695E69ED0D4FBE8B084FCA8066D21C', 1, 'Document Search ', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'EDocLite  ', '51DD5B9FACDD4EDAA9CA8D53A82FCCCA', 1, 'eDocLite', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'Feature', 'BBD9976498A4441F904013004F3D70B3', 1, 'Feature', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'GlobalReviewer', 'C21B0C6229144F6FBC52A10A38E51E3B', 1, 'Global Reviewer', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'Mailer', '5DB9D1433E214325BE380C82762A223B', 1, 'Mailer', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'Note', '868D39EC269B4402B3136C74C2342F22', 1, 'Note', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'QuickLink', '3E26DA76458A46D68CBAF209DA036157', 1, 'Quick Link', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'RouteQueue', 'D4F6DDEF69B24265AA2A170A62A1CADB', 1, 'Route Queue', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'Route', '583C2D3562D44DBAA5FEA998EB601DC9', 1, 'Routing', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'Rule', 'FC831215ED534549845BCE2C59B16FD9', 1, 'Rule', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'RuleService', 'A8FBD6D9A72347CFBB47994C35A45A5F', 1, 'Rule Service', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'RuleTemplate', 'FB2565730CB74E3C9077A8B8CF3E4618', 1, 'Rule Template', 'Y')
/
INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND)
  VALUES('KR-WKFLW', 'Workgroup', 'D04AFB1812E34723ABEB64986AC61DC9', 1, 'Workgroup', 'Y')
/

-- System Parameters: extracted from DBA using following statement
-- select * from KRCR_PARM_T

INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Batch', 'ACTIVE_FILE_TYPES', '5A689075D35E7AEBE0404F8189D80321', 1, 'CONFG', 'collectorInputFileType;procurementCardInputFileType;enterpriseFeederFileSetType;assetBarcodeInventoryInputFileType;customerLoadInputFileType', 'Batch file types that are active options for the file upload screen.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'CHECK_ENCRYPTION_SERVICE_OVERRIDE_IND', '53680C68F59AAD9BE0404F8189D80A6C', 1, 'CONFG', 'Y', 'Flag for enabling/disabling (Y/N) the demonstration encryption check.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'ScheduleStep', 'CUTOFF_TIME', '5A689075D35C7AEBE0404F8189D80321', 1, 'CONFG', '02:00:00:AM', 'Controls when the daily batch schedule should terminate. The scheduler service implementation compares the start time of the schedule job from quartz with this time on day after the schedule job started running.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'ScheduleStep', 'CUTOFF_TIME_NEXT_DAY_IND', '5A689075D35D7AEBE0404F8189D80321', 1, 'CONFG', 'Y', 'Controls whether when the system is comparing the schedule start day & time with the scheduleStep_CUTOFF_TIME parameter, it considers the specified time to apply to the day after the schedule starts.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Document', 'DEFAULT_CAN_PERFORM_ROUTE_REPORT_IND', '53680C68F59EAD9BE0404F8189D80A6C', 1, 'CONFG', 'N', 'If Y, the Route Report button will be displayed on the document actions bar if the document is using the default DocumentAuthorizerBase.getDocumentActionFlags to set the canPerformRouteReport property of the returned DocumentActionFlags instance.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'ENABLE_DIRECT_INQUIRIES_IND', '53680C68F59BAD9BE0404F8189D80A6C', 1, 'CONFG', 'Y', 'Flag for enabling/disabling direct inquiries on screens that are drawn by the nervous system (i.e. lookups and maintenance documents)', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'ENABLE_FIELD_LEVEL_HELP_IND', '53680C68F59CAD9BE0404F8189D80A6C', 1, 'CONFG', 'Y', 'Indicates whether field level help links are enabled on lookup pages and documents.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Document', 'EXCEPTION_GROUP', '53680C68F59FAD9BE0404F8189D80A6C', 1, 'AUTH', 'KR-WKFLW:WorkflowAdmin', 'The workgroup to which a user must be assigned to perform actions on documents in exception routing status.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'PurgePendingAttachmentsStep', 'MAX_AGE', '5A689075D35A7AEBE0404F8189D80321', 1, 'CONFG', '86400', 'Pending attachments are attachments that do not yet have a permanent link with the associated Business Object (BO). These pending attachments are stored in the attachments.pending.directory (defined in the configuration service). If the BO is never persisted, then this attachment will become orphaned (i.e. not associated with any BO), but will remain in this directory. The PurgePendingAttachmentsStep batch step deletes these pending attachment files that are older than the value of this parameter. The unit of this value is seconds. Do not set this value too short, as this will cause problems attaching files to BOs.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Document', 'MAX_FILE_SIZE_ATTACHMENT', '53680C68F5A0AD9BE0404F8189D80A6C', 1, 'CONFG', '5M', 'Maximum attachment upload size for the application. Used by KualiDocumentFormBase. Must be an integer, optionally followed by "K", "M", or "G".', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'MAX_FILE_SIZE_DEFAULT_UPLOAD', '53680C68F59DAD9BE0404F8189D80A6C', 1, 'CONFG', '5M', 'Maximum file upload size for the application. Used by PojoFormBase. Must be an integer, optionally followed by "K", "M", or "G". Only used if no other upload limits are in effect.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Lookup', 'MULTIPLE_VALUE_RESULTS_EXPIRATION_SECONDS', '53680C68F5A3AD9BE0404F8189D80A6C', 1, 'CONFG', '86400', 'Lookup results may continue to be persisted in the DB long after they are needed. This parameter represents the maximum amount of time, in seconds, that the results will be allowed to persist in the DB before they are deleted from the DB.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Lookup', 'MULTIPLE_VALUE_RESULTS_PER_PAGE', '53680C68F5A6AD9BE0404F8189D80A6C', 1, 'CONFG', '100', 'Maximum number of rows that will be displayed on a look-up results screen.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'PurgeSessionDocumentsStep', 'NUMBER_OF_DAYS_SINCE_LAST_UPDATE', '5A689075D3597AEBE0404F8189D80321', 1, 'CONFG', '1', 'Determines the age of the session document records that the the step will operate on, e.g. if this param is set to 4, the rows with a last update timestamp older that 4 days prior to when the job is running will be deleted.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Document', 'PESSIMISTIC_LOCK_ADMIN_GROUP', '53680C68F5A5AD9BE0404F8189D80A6C', 1, 'AUTH', 'KFS:KUALI_ROLE_SUPERVISOR', 'Workgroup which can perform admin deletion and lookup functions for Pessimistic Locks.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Lookup', 'RESULTS_DEFAULT_MAX_COLUMN_LENGTH', '53680C68F5A7AD9BE0404F8189D80A6C', 1, 'CONFG', '70', 'If a maxLength attribute has not been set on a lookup result field in the data dictionary, then the result column''s max length will be the value of this parameter. Set this parameter to 0 for an unlimited default length or a positive value (i.e. greater than 0) for a finite max length.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Lookup', 'RESULTS_LIMIT', '53680C68F5A8AD9BE0404F8189D80A6C', 1, 'CONFG', '200', 'Maximum number of results returned in a look-up query.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Batch', 'SCHEDULE_ADMIN_GROUP', '5A689075D35F7AEBE0404F8189D80321', 1, 'CONFG', 'KR-WKFLW:WorkflowAdmin', 'The workgroup to which a user must be assigned to modify batch jobs.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Document', 'SEND_NOTE_WORKFLOW_NOTIFICATION_ACTIONS', '53680C68F5A1AD9BE0404F8189D80A6C', 1, 'CONFG', 'K', 'Some documents provide the functionality to send notes to another user using a workflow FYI or acknowledge functionality. This parameter specifies the default action that will be used when sending notes. This parameter should be one of the following 2 values: "K" for acknowledge or "F" for fyi. Depending on the notes and workflow service implementation, other values may be possible.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Document', 'SESSION_TIMEOUT_WARNING_MESSAGE_TIME', '53680C68F5A4AD9BE0404F8189D80A6C', 1, 'CONFG', '5', 'The number of minutes before a session expires that user should be warned when a document uses pessimistic locking.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'ScheduleStep', 'STATUS_CHECK_INTERVAL', '5A689075D35B7AEBE0404F8189D80321', 1, 'CONFG', '30000', 'Time in milliseconds that the scheduleStep should wait between iterations.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'Document', 'SUPERVISOR_GROUP', '53680C68F5A2AD9BE0404F8189D80A6C', 1, 'AUTH', 'KR-WKFLW:WorkflowAdmin', 'Workgroup which can perform almost any function within Kuali.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'nate', 'EEB06292-B9B4-001D-596E-51332C8D37A2', 1, 'CONFG', 'val', 'desc', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'ActionList', 'ACTION_LIST_DOCUMENT_POPUP_IND', '290E45BA032F4F4FB423CE5F78AC52E1', 1, 'CONFG', 'Y', 'Flag to specify if clicking on a Document ID from the Action List will load the Document in a new window.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'All', 'KIM_PRIORITY_ON_DOC_TYP_PERMS_IND', '5C731F2968A3689AE0404F8189D86653', 1, 'CONFG', 'N', 'Flag for enabling/disabling document type permission checks to use KIM Permissions as priority over Document Type policies.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'ActionList', 'EMAIL_NOTIFICATION_TEST_ADDRESS ', '340789CDF30F4252A1A2A42AD39B90B2', 1, 'CONFG', NULL, 'Default email address used for testing.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'ActionList', 'ACTION_LIST_ROUTE_LOG_POPUP_IND', '967B0311A5E94F7191B2C544FA7DE095', 1, 'CONFG', 'N', 'Flag to specify if clicking on a Route Log from the Action List will load the Route Log in a new window.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'ActionList', 'PAGE_SIZE_THROTTLE', '2CE075BC0C59435CA6DEFF724492DE3F', 1, 'CONFG', NULL, 'Throttles the number of results returned on all users Action Lists, regardless of their user preferences.  This is intended to be used in a situation where excessively large Action Lists are causing performance issues.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'ActionList', 'SEND_EMAIL_NOTIFICATION_IND', 'A87659E198214A8B90BE5BEF41630411', 1, 'CONFG', 'N', 'Flag to determine whether or not to send email notification.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'All', 'APPLICATION_CONTEXT', '396623E27D0649FCB6E7E7CD45F32E13', 1, 'CONFG', 'en-dev', 'Web application context name of the application.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Backdoor', 'SHOW_BACK_DOOR_LOGIN_IND', '9BD6785416434C4D9E5F05AF077DB9B7', 1, 'CONFG', 'Y', 'Flag to show the backdoor login.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Rule', 'DELEGATE_LIMIT', '21EA54B9A9E846709E76C176DE0AF47C', 1, 'CONFG', '20', 'Specifies that maximum number of delegation rules that will be displayed on a Rule inquiry before the screen shows a count of delegate rules and provides a link for the user to show them.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Rule', 'RULE_LOCKING_ON_IND', '88167F03AAD0474281908E03CC681C06', 1, 'CONFG', 'Y', 'Defines whether rule locking it enabled.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Backdoor', 'TARGET_FRAME_NAME', 'AD71949E2CCF422D941AAA9D4CB44D10', 1, 'CONFG', 'iframe_51148', 'Defines the target iframe name that the KEW internal portal uses for its menu links.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Mailer', 'FROM_ADDRESS', '700AB6A6E23740D0B3E00E02A8FB6347', 1, 'CONFG', 'rice.test@kuali.org', 'Default from email address for notifications.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'All', 'MAXIMUM_NODES_BEFORE_RUNAWAY', '4656B6E7E9844E2C9E2255014AFC86B5', 1, 'CONFG', NULL, 'The maximum number of nodes the workflow engine will process before it determines the process is a runaway process.  This is prevent infinite "loops" in the workflow engine.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Notification', 'NOTIFY_GROUPS', '08280F2575904F3586CF48BB97907506', 1, 'CONFG', NULL, 'Defines a group name (in the format "namespace:name") which contains members who should never receive "notifications" action requests from KEW.', 'D', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'DocumentSearch', 'FETCH_MORE_ITERATION_LIMIT', 'D43459D143FC46C6BF83C71AC2383B76', 1, 'CONFG', NULL, 'Limit of fetch more iterations for document searches.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'DocumentSearch', 'DOCUMENT_SEARCH_POPUP_IND', 'E78100F6F14C4932B54F7719FA5C27E9', 1, 'CONFG', 'Y', 'Flag to specify if clicking on a Document ID from Document Search will load the Document in a new window.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'DocumentSearch', 'DOCUMENT_SEARCH_ROUTE_LOG_POPUP_IND', '632680DDE9A7478CBD379FAF90C7AE72', 1, 'CONFG', 'N', 'Flag to specify if clicking on a Route Log from Document Search will load the Route Log in a new window.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'DocumentSearch', 'RESULT_CAP', 'E324D85082184EB6967537B3EE1F655B', 1, 'CONFG', NULL, 'Maximum number of documents to return from a search.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'DocumentType', 'DOCUMENT_TYPE_SEARCH_INSTRUCTION', '7ADC4995AB7E47299A13A5B66E495683', 1, 'CONFG', 'Enter document type information below and click search.', 'Instructions for searching document types.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'EDocLite', 'DEBUG_TRANSFORM_IND', '68B2EA08E13A4FF3B9EDBD5415818C93', 1, 'CONFG', 'N', 'Defines whether the debug transform is enabled for eDcoLite.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'EDocLite', 'USE_XSLTC_IND', 'FCAEE745A7E64AF5982937C47EBC2698', 1, 'CONFG', 'N', 'Defines whether XSLTC is used for eDocLite.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Feature', 'IS_LAST_APPROVER_ACTIVATE_FIRST_IND', 'BEBDBCFA74A5458EADE2CF075FFF206E', 1, 'CONFG', NULL, 'A flag to specify whether the WorkflowInfo.isLastApproverAtNode(...) API method attempts to active requests first, prior to execution.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'GlobalReviewer', 'REPLACE_INSTRUCTION', '0594B51D2619468294D084F24DA25A03', 1, 'CONFG', 'Enter the reviewer to replace.', 'Instructions for replacing a reviewer.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'ActionList', 'HELP_DESK_NAME_GROUP', 'BD2EA23177374930B2E97C6F7AC819DA', 1, 'CONFG', 'KR-WKFLW:WorkflowAdmin', 'The name of the group who has access to the "Help Desk" feature on the Action List.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Note', 'NOTE_CREATE_NEW_INSTRUCTION', '09217B953D1F4265B1106291925B8F08', 1, 'CONFG', 'Create or modify note information.', 'Instructions for creating a new note.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'QuickLinks', 'RESTRICT_DOCUMENT_TYPES', '5292CFD9A0EA48BEB22A2EB3B3BD3CDA', 1, 'CONFG', NULL, 'Comma seperated list of Document Types to exclude from the Rule Quicklinks.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Rule', 'RULE_CACHE_REQUEUE_DELAY', '8AE796DB88484468830A8879630CCF5D', 1, 'CONFG', '5000', 'Amount of time after a rule change is made before the rule cache update message is sent.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Rule', 'CUSTOM_DOCUMENT_TYPES', 'BDE964269F2743338C00A4326B676195', 1, 'CONFG', NULL, 'Defines custom Document Type processes to use for certain types of routing rules.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Rule', 'RULE_CREATE_NEW_INSTRUCTION', '83F4AE3D84C948B99118D602574B4E72', 1, 'CONFG', 'Please select a rule template and document type.', 'Instructions for creating a new rule.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Rule', 'ROUTE_LOG_POPUP_IND', '1C0C01E55A90472EAF65941ACE9DDCA2', 1, 'CONFG', 'F', 'Flag to specify if clicking on a Route Log from a Routing Rule inquiry will load the Route Log in a new window.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Rule', 'RULE_SEARCH_INSTRUCTION', 'E390513347EA44AD87923C391D1645F2', 1, 'CONFG', 'Use fields below to search for rules.', 'Instructions for the rule search.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'Rule', 'GENERATE_ACTION_REQUESTS_IND', '96868C896B4B4A8BA87AD20E42948431', 1, 'CONFG', 'Y', 'Flag to determine whether or not a change to a routing rule should be applied retroactively to existing documents.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'RuleTemplate', 'RULE_TEMPLATE_CREATE_NEW_INSTRUCTION', '09ECF812733D499C906ACBE17F13AFEE', 1, 'CONFG', 'Enter a rule template name and description. Please select all necessary rule attributes for this template.', 'Instructions for creating new rule templates.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'RuleTemplate', 'RULE_TEMPLATE_SEARCH_INSTRUCTION', '983690D9FD3244BAB1EF6ED7CCAF63EF', 1, 'CONFG', 'Use fields below to search for rule templates.', 'Instructions for the rule template search.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-WKFLW', 'All', 'SHOW_ATTACHMENTS_IND', '8A37388A2D7A46EF9E6BF3FA8D08A03A', 1, 'CONFG', 'Y', 'Flag to specify whether or not a file upload box is displayed for KEW notes which allows for uploading of an attachment with the note.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-IDM', 'EntityNameImpl', 'PREFIXES', '61645D045B0105D7E0404F8189D849B1', 1, 'CONFG', 'Ms;Mrs;Mr;Dr', NULL, 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-IDM', 'EntityNameImpl', 'SUFFIXES', '61645D045B0205D7E0404F8189D849B1', 1, 'CONFG', 'Jr;Sr;Mr;Md', NULL, 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'STRING_TO_DATE_FORMATS', '664F8ABEC722DBCDE0404F8189D85427', 1, 'CONFG', 'MM/dd/yyyy hh:mm a;MM/dd/yy;MM/dd/yyyy;MM-dd-yy;MMddyy;MMMM dd;yyyy;MM/dd/yy HH:mm:ss;MM/dd/yyyy HH:mm:ss;MM-dd-yy HH:mm:ss;MMddyy HH:mm:ss;MMMM dd HH:mm:ss;yyyy HH:mm:ss', 'A semi-colon delimted list of strings representing date formats that the DateTimeService will use to parse dates when DateTimeServiceImpl.convertToSqlDate(String) or DateTimeServiceImpl.convertToDate(String) is called. Note that patterns will be applied in the order listed (and the first applicable one will be used). For a more technical description of how characters in the parameter value will be interpreted, please consult the javadocs for java.text.SimpleDateFormat. Any changes will be applied when the application is restarted.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'DATE_TO_STRING_FORMAT_FOR_FILE_NAME', '664F8ABEC723DBCDE0404F8189D85427', 1, 'CONFG', 'yyyyMMdd', 'A single date format string that the DateTimeService will use to format dates to be used in a file name when DateTimeServiceImpl.toDateStringForFilename(Date) is called. For a more technical description of how characters in the parameter value will be interpreted, please consult the javadocs for java.text.SimpleDateFormat. Any changes will be applied when the application is restarted.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'TIMESTAMP_TO_STRING_FORMAT_FOR_FILE_NAME', '664F8ABEC724DBCDE0404F8189D85427', 1, 'CONFG', 'yyyyMMdd-HH-mm-ss-S', 'A single date format string that the DateTimeService will use to format a date and time string to be used in a file name when DateTimeServiceImpl.toDateTimeStringForFilename(Date) is called.. For a more technical description of how characters in the parameter value will be interpreted, please consult the javadocs for java.text.SimpleDateFormat. Any changes will be applied when the application is restarted.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'DATE_TO_STRING_FORMAT_FOR_USER_INTERFACE', '664F8ABEC725DBCDE0404F8189D85427', 1, 'CONFG', 'MM/dd/yyyy', 'A single date format string that the DateTimeService will use to format a date to be displayed on a web page. For a more technical description of how characters in the parameter value will be interpreted, please consult the javadocs for java.text.SimpleDateFormat. Any changes will be applied when the application is restarted.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'TIMESTAMP_TO_STRING_FORMAT_FOR_USER_INTERFACE', '664F8ABEC726DBCDE0404F8189D85427', 1, 'CONFG', 'MM/dd/yyyy hh:mm a', 'A single date format string that the DateTimeService will use to format a date and time to be displayed on a web page. For a more technical description of how characters in the parameter value will be interpreted, please consult the javadocs for java.text.SimpleDateFormat. Any changes will be applied when the application is restarted.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'STRING_TO_TIMESTAMP_FORMATS', '664F8ABEC727DBCDE0404F8189D85427', 1, 'CONFG', 'MM/dd/yyyy hh:mm a;MM/dd/yy;MM/dd/yyyy;MM-dd-yy;MMddyy;MMMM dd;yyyy;MM/dd/yy HH:mm:ss;MM/dd/yyyy HH:mm:ss;MM-dd-yy HH:mm:ss;MMddyy HH:mm:ss;MMMM dd HH:mm:ss;yyyy HH:mm:ss', 'A semi-colon delimted list of strings representing date formats that the DateTimeService will use to parse date and times when DateTimeServiceImpl.convertToDateTime(String) or DateTimeServiceImpl.convertToSqlTimestamp(String) is called. Note that patterns will be applied in the order listed (and the first applicable one will be used). For a more technical description of how characters in the parameter value will be interpreted, please consult the javadocs for java.text.SimpleDateFormat. Any changes will be applied when the application is restarted.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES ('KR-NS', 'All', 'DEFAULT_LOCALE_CODE', '664F8ABEC727DBCDE0404F8189D85428', 1, 'CONFG', 'en-US', 'The locale code that should be used within the application when otherwise not specified.', 'A', 'KUALI')
/
INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
  VALUES('KR-NS', 'All', 'SENSITIVE_DATA_PATTERNS_WARNING_IND', '8A37388A2D7A46EF9E6BF3FA8D08A044', 1, 'CONFG', 'Y', 'Warn For Sensitive Data.', 'A', 'KUALI')
/
insert into KRLC_CNTRY_T(postal_cntry_cd, obj_id, ver_nbr, postal_cntry_nm, pstl_cntry_rstrc_ind, actv_ind)
	values('US', 'C20A15A9C0354C108C71CA3AFED063AF', 1, 'UNITED STATES', 'N', 'Y')
/
insert into KRLC_ST_T(postal_state_cd, postal_cntry_cd, obj_id, ver_nbr, postal_state_nm, actv_ind)
	values('AZ','US', 'E68FB90D901741C69B4B73FE46CD8BDC', 1, 'ARIZONA', 'Y')
/
insert into KRLC_ST_T(postal_state_cd, postal_cntry_cd, obj_id, ver_nbr, postal_state_nm, actv_ind)
	values('CA','US', '18ADEA4EF2B94E0C84E4E8318B7EC338', 1, 'CALIFORNIA', 'Y')
/
insert into KRLC_CNTY_T (county_cd, state_cd, postal_cntry_cd, obj_id, ver_nbr, county_nm, actv_ind)
	values('COCONINO','AZ','US', 'F2D5FA4E49F34B509A4AD5F97BB502E6', 1, 'COCONINO', 'Y')
/
insert into KRLC_CNTY_T (county_cd, state_cd, postal_cntry_cd, obj_id, ver_nbr, county_nm, actv_ind)
	values('VENTURA','CA','US', '575FA755FA59451DB81CE0A3FD621D2F', 1, 'VENTURA', 'Y')
/
