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
package org.kuali.rice.edl.impl.extract;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.edl.framework.extract.DumpDTO;
import org.kuali.rice.edl.framework.extract.FieldDTO;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 *
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@Entity
@Table(name="KREW_EDL_DMP_T")
//@Sequence(name="KREW_EDL_DMP_T", property="docId")
public class Dump {

	private static final long serialVersionUID = -6136544551121011531L;

    @Id
	@Column(name="DOC_HDR_ID")
	private String docId;
	@Column(name="DOC_TYP_NM")
	private String docTypeName;
	@Column(name="DOC_HDR_STAT_CD")
	private String docRouteStatusCode;
	@Column(name="DOC_HDR_MDFN_DT")
	private Timestamp docModificationDate;
	@Column(name="DOC_HDR_CRTE_DT")
	private Timestamp docCreationDate;
	@Column(name="DOC_HDR_TTL")
	private String docDescription;
    @Column(name="DOC_HDR_INITR_PRNCPL_ID")
	private String docInitiatorId;
    @Column(name="CRNT_NODE_NM")
	private String docCurrentNodeName;
    @Version
	@Column(name="VER_NBR")
	private Integer lockVerNbr;

    @OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},mappedBy="dump")
    @Fetch(value = FetchMode.SELECT)
    private List<Fields> fields = new ArrayList<Fields>();

    //@PrePersist
    public void beforeInsert(){
        OrmUtils.populateAutoIncValue(this, KRADServiceLocator.getEntityManagerFactory().createEntityManager());
    }


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


	public List<Fields> getFields() {
		return fields;
	}

	public void setFields(final List<Fields> fields) {
		this.fields = fields;
	}
	
	public static DumpDTO to(Dump dump) {
		if (dump == null) {
			return null;
		}
		DumpDTO dumpDTO = new DumpDTO();
		dumpDTO.setDocCreationDate(dump.getDocCreationDate());
		dumpDTO.setDocCurrentNodeName(dump.getDocCurrentNodeName());
		dumpDTO.setDocDescription(dump.getDocDescription());
		dumpDTO.setDocId(dump.getDocId());
		dumpDTO.setDocInitiatorId(dump.getDocInitiatorId());
		dumpDTO.setDocModificationDate(dump.getDocModificationDate());
		dumpDTO.setDocRouteStatusCode(dump.getDocRouteStatusCode());
		dumpDTO.setDocTypeName(dump.getDocTypeName());
		dumpDTO.setLockVerNbr(dump.getLockVerNbr());
		List<FieldDTO> fields = new ArrayList<FieldDTO>();
		for (Fields field : dump.getFields()) {
			fields.add(Fields.to(field));
		}
		dumpDTO.setFields(fields);
		return dumpDTO;
	}
	
	public static Dump from(DumpDTO dumpDTO) {
		if (dumpDTO == null) {
			return null;
		}
		Dump dump = new Dump();
		dump.setDocCreationDate(dumpDTO.getDocCreationDate());
		dump.setDocCurrentNodeName(dumpDTO.getDocCurrentNodeName());
		dump.setDocDescription(dumpDTO.getDocDescription());
		dump.setDocId(dumpDTO.getDocId());
		dump.setDocInitiatorId(dumpDTO.getDocInitiatorId());
		dump.setDocModificationDate(dumpDTO.getDocModificationDate());
		dump.setDocRouteStatusCode(dumpDTO.getDocRouteStatusCode());
		dump.setDocTypeName(dumpDTO.getDocTypeName());
		dump.setLockVerNbr(dumpDTO.getLockVerNbr());
		List<Fields> fields = new ArrayList<Fields>();
		for (FieldDTO fieldDTO : dumpDTO.getFields()) {
			fields.add(Fields.from(fieldDTO, dump));
		}
		dump.setFields(fields);
		return dump;
	}

}

