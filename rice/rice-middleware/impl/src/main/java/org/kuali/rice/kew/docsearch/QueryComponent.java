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
package org.kuali.rice.kew.docsearch;

public class QueryComponent {

	private String selectSql = "";
	private String fromSql = "";
	private String whereSql = "";
	
    public QueryComponent() {
        super();
    }

    /**
     * @param selectSql
     * @param fromSql
     * @param whereSql
     */
    public QueryComponent(String selectSql, String fromSql, String whereSql) {
        super();
        this.selectSql = selectSql;
        this.fromSql = fromSql;
        this.whereSql = whereSql;
    }

    /**
	 * @return Returns the tables.
	 */
	public String getFromSql() {
		return fromSql;
	}

	/**
	 * @return Returns the sql.
	 */
	public String getSelectSql() {
		return selectSql;
	}

	/**
	 * @return Returns the tempSql.
	 */
	public String getWhereSql() {
		return whereSql;
	}

	/**
	 * @param tables The tables to set.
	 */
	public void setFromSql(String tables) {
		this.fromSql = tables;
	}

	/**
	 * @param sql The sql to set.
	 */
	public void setSelectSql(String sql) {
		this.selectSql = sql;
	}

	/**
	 * @param tempSql The tempSql to set.
	 */
	public void setWhereSql(String tempSql) {
		this.whereSql = tempSql;
	}
	
}
