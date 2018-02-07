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

-- KEN Core Priorities --
INSERT INTO KREN_PRIO_T (PRIO_ID, NM, DESC_TXT, PRIO_ORD, OBJ_ID, VER_NBR) 
	VALUES (1, 'Normal', 'Normal priority', 2, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);
	
INSERT INTO KREN_PRIO_T (PRIO_ID, NM, DESC_TXT, PRIO_ORD, OBJ_ID, VER_NBR) VALUES (2, 'Low', 'A low priority', 3, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1)
;
INSERT INTO KREN_PRIO_T (PRIO_ID, NM, DESC_TXT, PRIO_ORD, OBJ_ID, VER_NBR) VALUES (3, 'High', 'A high priority', 1, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1)
;

-- KEN Content Types
INSERT INTO KREN_CNTNT_TYP_T (CNTNT_TYP_ID, NM, DESC_TXT, NMSPC_CD, XSD, XSL, OBJ_ID,VER_NBR)
VALUES
(1, 'Simple', 'Simple content type', 'notification/ContentTypeSimple',
'<?xml version="1.0" encoding="UTF-8"?>
<!-- This schema describes a simple notification.  It only contains a content
element which is a String...about as simple as one can get -->
<schema xmlns="http://www.w3.org/2001/XMLSchema"
  xmlns:c="ns:notification/common"
  xmlns:cs="ns:notification/ContentTypeSimple"
  targetNamespace="ns:notification/ContentTypeSimple"
  attributeFormDefault="unqualified"
    elementFormDefault="qualified">
  <annotation>
    <documentation xml:lang="en">
      Simple Content Schema
    </documentation>
  </annotation>
  <import namespace="ns:notification/common" schemaLocation="resource:notification/notification-common" />
  <!--  The content element is just a String -->
  <element name="content">
    <complexType>
      <sequence>
        <element name="message" type="c:LongStringType"/>
      </sequence>
    </complexType>
  </element>
</schema>',
'<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
   version="1.0"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:n="ns:notification/ContentTypeSimple"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="ns:notification/ContentTypeSimple resource:notification/ContentTypeSimple"
   exclude-result-prefixes="n xsi">
   <xsl:output method="html" omit-xml-declaration="yes" />
   <xsl:template match="/n:content/n:message">
      <strong>
          <xsl:value-of select="." disable-output-escaping="yes"/>
      </strong>
   </xsl:template>
</xsl:stylesheet>', '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1)
;
INSERT INTO KREN_CNTNT_TYP_T (CNTNT_TYP_ID, NM, DESC_TXT, NMSPC_CD, XSD, XSL, OBJ_ID,VER_NBR)
VALUES
(2, 'Event', 'Event content type', 'notification/ContentTypeEvent',
'<?xml version="1.0" encoding="UTF-8"?>
<!-- This schema defines an generic event notification type in order for it
to be accepted into the system. -->
<schema xmlns="http://www.w3.org/2001/XMLSchema" xmlns:c="ns:notification/common" xmlns:ce="ns:notification/ContentTypeEvent" targetNamespace="ns:notification/ContentTypeEvent" attributeFormDefault="unqualified" elementFormDefault="qualified">
  <annotation>
    <documentation xml:lang="en">Content Event Schema</documentation>
  </annotation>
  <import namespace="ns:notification/common" schemaLocation="resource:notification/notification-common" />
  <!-- The content element describes the content of the notification.  It
  contains a message (a simple String) and a message element -->
  <element name="content">
    <complexType>
      <sequence>
        <element name="message" type="c:LongStringType"/>
        <element ref="ce:event"/>
      </sequence>
    </complexType>
  </element>
  <!-- This is the event element.  It describes a simple event type containing a
  summary, description, location, and start/stop times -->
  <element name="event">
    <complexType>
      <sequence>
        <element name="summary" type="c:NonEmptyShortStringType" />
        <element name="description" type="c:NonEmptyShortStringType" />
        <element name="location" type="c:NonEmptyShortStringType" />
        <element name="startDateTime" type="dateTime" />
        <element name="stopDateTime" type="dateTime" />
      </sequence>
    </complexType>
  </element>
</schema>',
'<?xml version="1.0" encoding="UTF-8"?>
<!-- style sheet declaration: be very careful editing the following, the
     default namespace must be used otherwise elements will not match -->
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:n="ns:notification/ContentTypeEvent"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="ns:notification/ContentTypeEvent resource:notification/ContentTypeEvent"
    exclude-result-prefixes="n xsi">
    <!-- output an html fragment -->
    <xsl:output method="html" indent="yes" />
    <!-- match everything -->
    <xsl:template match="/n:content" >
        <table class="bord-all">
            <xsl:apply-templates />
        </table>
    </xsl:template>
    <!--  match message element in the default namespace and render as strong -->
    <xsl:template match="n:message" >
        <caption>
            <strong><xsl:value-of select="." disable-output-escaping="yes"/></strong>
        </caption>
    </xsl:template>
    <!-- match on event in the default namespace and display all children -->
    <xsl:template match="n:event">
        <tr>
            <td class="thnormal"><strong>Summary: </strong></td>
            <td class="thnormal"><xsl:value-of select="n:summary" /></td>
        </tr>
        <tr>
            <td class="thnormal"><strong>Description: </strong></td>
            <td class="thnormal"><xsl:value-of select="n:description" /></td>
        </tr>
        <tr>
            <td class="thnormal"><strong>Location: </strong></td>
            <td class="thnormal"><xsl:value-of select="n:location" /></td>
        </tr>
        <tr>
            <td class="thnormal"><strong>Start Time: </strong></td>
            <td class="thnormal"><xsl:value-of select="n:startDateTime" /></td>
        </tr>
        <tr>
            <td class="thnormal"><strong>End Time: </strong></td>
            <td class="thnormal"><xsl:value-of select="n:stopDateTime" /></td>
        </tr>
    </xsl:template>
</xsl:stylesheet>' , '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1)
;

-- NOTIFICATION_PRODUCERS --
INSERT INTO KREN_PRODCR_T
(PRODCR_ID, NM, DESC_TXT, CNTCT_INFO, OBJ_ID, VER_NBR)
VALUES
(101, 'Test Producer #1', 'First Producer for Unit Tests', 'producer_1_and_2@127.0.0.1', '31ba4224-7ea3-102c-97b6-ed716fdaf540',1);

INSERT INTO KREN_PRODCR_T
(PRODCR_ID, NM, DESC_TXT, CNTCT_INFO, OBJ_ID,VER_NBR)
VALUES
(102, 'Test Producer #2', 'Second Producer for Unit Tests', 'producer_1_and_2@127.0.0.1', '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_PRODCR_T
(PRODCR_ID, NM, DESC_TXT, CNTCT_INFO, OBJ_ID,VER_NBR)
VALUES
(103, 'Test Producer #3', 'Third Producer for Unit Tests', 'producer_3@127.0.0.1', '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_PRODCR_T
(PRODCR_ID, NM, DESC_TXT, CNTCT_INFO, OBJ_ID,VER_NBR)
VALUES
(104, 'Test Producer #4', 'Fourth Producer for Unit Tests', 'producer_4@127.0.0.1', '31ba4224-7ea3-102c-97b6-ed716fdaf540',1);

INSERT INTO KREN_PRODCR_T
(PRODCR_ID, NM, DESC_TXT, CNTCT_INFO, OBJ_ID,VER_NBR)
VALUES
(105, 'Notification System', 'This producer represents messages sent from the general message sending form.', 'admins-notsys@127.0.0.1', '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

-- NOTIFICATION_CHANNELS --

INSERT INTO KREN_CHNL_T
(CHNL_ID, NM, DESC_TXT, SUBSCRB_IND, OBJ_ID,VER_NBR)
VALUES
(101, 'Test Channel #1', 'First Channel for Unit Tests', 'N', '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_CHNL_T
(CHNL_ID, NM, DESC_TXT, SUBSCRB_IND, OBJ_ID, VER_NBR)
VALUES
(102, 'Test Channel #2', 'Second Channel for Unit Tests', 'Y', '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

-- NOTIFICATION_CHANNEL_PRODUCERS --
INSERT INTO KREN_CHNL_PRODCR_T
(CHNL_ID, PRODCR_ID)
VALUES
(101, 103);

INSERT INTO KREN_CHNL_PRODCR_T
(CHNL_ID, PRODCR_ID)
VALUES
(102, 103);

INSERT INTO KREN_CHNL_PRODCR_T
(CHNL_ID, PRODCR_ID)
VALUES
(102, 104);

INSERT INTO KREN_CHNL_PRODCR_T
(CHNL_ID, PRODCR_ID)
VALUES
(101, 105);

INSERT INTO KREN_CHNL_PRODCR_T
(CHNL_ID, PRODCR_ID)
VALUES
(102, 105);

-- NOTIFICATIONS --
INSERT INTO KREN_NTFCTN_T
(NTFCTN_ID, DELIV_TYP, CRTE_DTTM, SND_DTTM, AUTO_RMV_DTTM, PRIO_ID , CNTNT,
CNTNT_TYP_ID , CHNL_ID , PRODCR_ID, PROCESSING_FLAG, LOCKD_DTTM, OBJ_ID, VER_NBR )
VALUES
(1, 'FYI', {d '2005-12-31'}, {d '2005-12-31'}, {d '3000-12-31'}, 1,
'<content xmlns="ns:notification/ContentSimple" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="ns:notification/ContentSimple resource:notification/ContentSimple"><message>Check this out!</message></content>',
1, 101, 103, 'RESOLVED', NULL, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_NTFCTN_T
(NTFCTN_ID, DELIV_TYP, CRTE_DTTM, SND_DTTM, AUTO_RMV_DTTM, PRIO_ID , CNTNT,
CNTNT_TYP_ID , CHNL_ID , PRODCR_ID, PROCESSING_FLAG, LOCKD_DTTM , OBJ_ID, VER_NBR)
VALUES
(2, 'ACK', {d '2005-12-31'}, {d '2005-12-31'},{d '3000-12-31'}, 2,
'<content xmlns="ns:notification/ContentEvent" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="ns:notification/ContentEvent resource:notification/ContentEvent"><message>CCC presents The Strokes at Cornell</message><!-- an event that it happening on campus --><event><summary>CCC presents The Strokes at Cornell</summary><description>blah blah blah</description><location>Barton Hall</location><startDateTime>2006-01-01T00:00:00</startDateTime><stopDateTime>2007-01-01T00:00:00</stopDateTime></event></content>',
2, 102, 104, 'RESOLVED', NULL, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

-- the following notifications and recipients list (along with deliverer preferences) are relied upon by the NotificationMessageDeliveryResolverServiceImplTest

INSERT INTO KREN_NTFCTN_T
(NTFCTN_ID, DELIV_TYP, CRTE_DTTM, SND_DTTM, AUTO_RMV_DTTM, PRIO_ID , CNTNT,
CNTNT_TYP_ID , CHNL_ID , PRODCR_ID, PROCESSING_FLAG, LOCKD_DTTM, OBJ_ID, VER_NBR )
VALUES
(3, 'FYI', {d '2005-12-31'}, {d '2005-12-31'},{d '2006-12-30'}, 1,
'<content xmlns="ns:notification/ContentEvent" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="ns:notification/ContentEvent resource:notification/ContentEvent"><message>CCC presents The Strokes at Cornell</message><!-- an event that it happening on campus --><event><summary>CCC presents The Strokes at Cornell</summary><description>blah blah blah</description><location>Barton Hall</location><startDateTime>2006-01-01T00:00:00</startDateTime><stopDateTime>2007-01-01T00:00:00</stopDateTime></event></content>',
2, 101, 103, 'UNRESOLVED', NULL, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_NTFCTN_T
(NTFCTN_ID, DELIV_TYP, CRTE_DTTM, SND_DTTM, AUTO_RMV_DTTM, PRIO_ID , CNTNT,
CNTNT_TYP_ID , CHNL_ID , PRODCR_ID, PROCESSING_FLAG, LOCKD_DTTM, OBJ_ID, VER_NBR )
VALUES
(4, 'FYI', {d '2005-12-31'}, {d '2005-12-31'},{d '3000-12-31'}, 1,
'<content xmlns="ns:notification/ContentEvent" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="ns:notification/ContentEvent resource:notification/ContentEvent"><message>CCC presents The Strokes at Cornell</message><!-- an event that it happening on campus --><event><summary>CCC presents The Strokes at Cornell</summary><description>blah blah blah</description><location>Barton Hall</location><startDateTime>2006-01-01T00:00:00</startDateTime><stopDateTime>2007-01-01T00:00:00</stopDateTime></event></content>',
2, 101, 103, 'UNRESOLVED', NULL, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

-- NOTIFICATION_RECIPIENTS --
INSERT INTO KREN_RECIP_T
(RECIP_ID, NTFCTN_ID, PRNCPL_ID, RECIP_TYP_CD)
VALUES
(1, 1, 'testuser5', 'P');

INSERT INTO KREN_RECIP_T
(RECIP_ID, NTFCTN_ID, PRNCPL_ID, RECIP_TYP_CD)
VALUES
(2, 1, 'testuser6', 'P');

INSERT INTO KREN_RECIP_T
(RECIP_ID, NTFCTN_ID, PRNCPL_ID, RECIP_TYP_CD)
VALUES
(3, 2, 'testuser4', 'P');

INSERT INTO KREN_RECIP_T
(RECIP_ID, NTFCTN_ID, PRNCPL_ID, RECIP_TYP_CD)
VALUES
(4, 2, 'testuser6', 'P');

INSERT INTO KREN_RECIP_T
(RECIP_ID, NTFCTN_ID, PRNCPL_ID, RECIP_TYP_CD)
VALUES
(5, 2, 'testuser5', 'P');

INSERT INTO KREN_RECIP_T
(RECIP_ID, NTFCTN_ID, PRNCPL_ID, RECIP_TYP_CD)
VALUES
(6, 3, '2003', 'G');

INSERT INTO KREN_RECIP_T
(RECIP_ID, NTFCTN_ID, PRNCPL_ID, RECIP_TYP_CD)
VALUES
(7, 3, 'testuser1', 'P');


INSERT INTO KREN_RECIP_T
(RECIP_ID, NTFCTN_ID, PRNCPL_ID, RECIP_TYP_CD)
VALUES
(8, 4, '2003', 'G');

INSERT INTO KREN_RECIP_T
(RECIP_ID, NTFCTN_ID, PRNCPL_ID, RECIP_TYP_CD)
VALUES
(9, 4, 'testuser1', 'P');

-- NOTIFICATION_SENDERS --
INSERT INTO KREN_SNDR_T
(SNDR_ID, NTFCTN_ID, NM, OBJ_ID, VER_NBR )
VALUES
(1, 1, 'John Fereira', '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_SNDR_T
(SNDR_ID, NTFCTN_ID, NM, OBJ_ID, VER_NBR)
VALUES
(2, 1, 'Aaron Godert', '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_SNDR_T
(SNDR_ID, NTFCTN_ID, NM, OBJ_ID, VER_NBR )
VALUES
(3, 2, 'Aaron Hamid', '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

-- the following NOTIFICATION_MSG_DELIVS are used by NotificationMessageDeliveryDispatchServiceImplTest
-- if this list is changed, verify that the test is updated to reflect expected results

-- NOTIFICATION_MSG_DELIVS --
INSERT INTO KREN_NTFCTN_MSG_DELIV_T
(NTFCTN_MSG_DELIV_ID, NTFCTN_ID, RECIP_ID, STAT_CD, LOCKD_DTTM, OBJ_ID, VER_NBR)
VALUES
(1, 1, 'testuser5', 'UNDELIVERED', NULL, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_NTFCTN_MSG_DELIV_T
(NTFCTN_MSG_DELIV_ID, NTFCTN_ID, RECIP_ID, STAT_CD, LOCKD_DTTM, OBJ_ID, VER_NBR)
VALUES
(2, 1, 'testuser6', 'UNDELIVERED', NULL, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_NTFCTN_MSG_DELIV_T
(NTFCTN_MSG_DELIV_ID, NTFCTN_ID, RECIP_ID, STAT_CD, LOCKD_DTTM, OBJ_ID, VER_NBR)
VALUES
(3, 2, 'testuser4', 'UNDELIVERED', NULL, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_NTFCTN_MSG_DELIV_T
(NTFCTN_MSG_DELIV_ID, NTFCTN_ID, RECIP_ID, STAT_CD, LOCKD_DTTM, OBJ_ID, VER_NBR)
VALUES
(4, 2, 'testuser6', 'UNDELIVERED', NULL, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);

INSERT INTO KREN_NTFCTN_MSG_DELIV_T
(NTFCTN_MSG_DELIV_ID, NTFCTN_ID, RECIP_ID, STAT_CD, LOCKD_DTTM, OBJ_ID, VER_NBR)
VALUES
(6, 2, 'testuser5', 'UNDELIVERED', NULL, '31ba4224-7ea3-102c-97b6-ed716fdaf540', 1);
