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
package org.apache.ojb.broker.platforms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.util.sequence.AbstractSequenceManager;
import org.apache.ojb.broker.util.sequence.SequenceManagerException;

public class KualiMySQLSequenceManagerImpl extends AbstractSequenceManager {

	public KualiMySQLSequenceManagerImpl(PersistenceBroker broker) {
		super(broker);
	}

	@Override
	protected long getUniqueLong(FieldDescriptor arg0)
			throws SequenceManagerException {
		PersistenceBroker broker = getBrokerForClass();

		Statement stmt = null;
		Long seqNumber = null;
		final String sequenceName = arg0.getSequenceName();

		try {
			//FIXME: should we be closing this connection in a finally block?
			Connection c = broker.serviceConnectionManager().getConnection();
			stmt = c.createStatement();
			String sql = "INSERT INTO " + sequenceName + " VALUES (NULL);";
			stmt.executeUpdate(sql);
			sql = "SELECT LAST_INSERT_ID()";
			
			//FIXME: should we be closing this result set in a finally block?
			ResultSet rs = stmt.executeQuery(sql);
			if (rs != null) {
				rs.first();
				seqNumber = rs.getLong(1);
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to execute for sequence name: " + sequenceName, e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				throw new RuntimeException("Unable to close statement for sequence name: " + sequenceName, e);
			}
		}

		return seqNumber;
	}

}
