package org.kuali.ole.select.service;

import java.sql.SQLException;

/**
 * Created by sureshss on 21/2/17.
 */
public interface DocumentUUIDUpdateService {

    public boolean updateCopyRecord(String fromId, String toId) throws SQLException;

}
