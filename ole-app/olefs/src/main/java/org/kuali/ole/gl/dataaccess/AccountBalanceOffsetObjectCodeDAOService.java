package org.kuali.ole.gl.dataaccess;

import java.util.List;
import java.util.Map;

/**
 * Created by premkb on 11/20/15.
 */
public interface AccountBalanceOffsetObjectCodeDAOService {

    public List<Map<String, Object>> getBalanceTable(Map fieldValues);
}
