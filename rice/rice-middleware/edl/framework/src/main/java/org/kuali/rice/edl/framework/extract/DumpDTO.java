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
package org.kuali.rice.edl.framework.extract;

import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kew.api.KewApiConstants;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DumpDTO implements Serializable {

	private static final long serialVersionUID = -6136544551121011531L;

	private String docId;
	private String docTypeName;
	private String docRouteStatusCode;
	private Timestamp docModificationDate;
	private Timestamp docCreationDate;
	private String docDescription;
	private String docInitiatorId;
	private String docCurrentNodeName;
	private Integer lockVerNbr;

    private List<FieldDTO> fields = new ArrayList<FieldDTO>();

	public Timestamp getDocCreationDate() {
		return docCreationDate;
	}
	public void setDocCreationDate(final Timestamp docCreationDate) {
		this.docCreationDate = docCreationDate;
	}
	public String getDocCurrentNodeName() {
		return docCurrentNodeName;
	}
	public void setDocCurrentNodeName(final String docCurrentNodeName) {
		this.docCurrentNodeName = docCurrentNodeName;
	}
	public String getDocDescription() {
		return docDescription;
	}
	public void setDocDescription(final String docDescription) {
		this.docDescription = docDescription;
	}
	public String getDocId() {
		return docId;
	}
	public String getDocInitiatorId() {
		return docInitiatorId;
	}
	public void setDocInitiatorId(final String docInitiatorId) {
		this.docInitiatorId = docInitiatorId;
	}
	public Timestamp getDocModificationDate() {
		return docModificationDate;
	}
	public void setDocModificationDate(final Timestamp docModificationDate) {
		this.docModificationDate = docModificationDate;
	}
	public String getDocRouteStatusCode() {
		return docRouteStatusCode;
	}
	public void setDocRouteStatusCode(final String docRouteStatusCode) {
		this.docRouteStatusCode = docRouteStatusCode;
	}
	public String getDocTypeName() {
		return docTypeName;
	}
	public void setDocTypeName(final String docTypeName) {
		this.docTypeName = docTypeName;
	}
	public Integer getLockVerNbr() {
		return lockVerNbr;
	}
	public void setLockVerNbr(final Integer lockVerNbr) {
		this.lockVerNbr = lockVerNbr;
	}
    public String getFormattedCreateDateTime() {
        long time = getDocCreationDate().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat(KewApiConstants.TIMESTAMP_DATE_FORMAT_PATTERN2);
        return dateFormat.format(date);
    }

    public String getFormattedCreateDate() {
        long time = getDocCreationDate().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        DateFormat dateFormat = RiceConstants.getDefaultDateFormat();
        return dateFormat.format(date);
    }
	public void setDocId(final String docId) {
		this.docId = docId;
	}


	public List<FieldDTO> getFields() {
		return fields;
	}

	public void setFields(final List<FieldDTO> fields) {
		this.fields = fields;
	}

}

