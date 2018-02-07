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
package org.kuali.rice.kew.stats.service;

import java.sql.SQLException;
import java.util.Date;

import org.apache.ojb.broker.accesslayer.LookupException;
import org.kuali.rice.kew.stats.Stats;

/**
 * A service for obtaining various pieces of statistics information about the
 * KEW application.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface StatsService {

    public void NumActiveItemsReport(Stats stats) throws SQLException, LookupException;
    public void DocumentsRoutedReport(Stats stats, Date begDate, Date endDate) throws SQLException, LookupException;
    public void NumberOfDocTypesReport(Stats stats) throws SQLException, LookupException;
    public void NumUsersReport(Stats stats) throws SQLException, LookupException;    
    public void NumInitiatedDocsByDocTypeReport(Stats stats) throws SQLException, LookupException;
    
}
