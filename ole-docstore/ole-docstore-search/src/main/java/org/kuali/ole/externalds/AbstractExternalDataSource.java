package org.kuali.ole.externalds;

import org.kuali.ole.docstore.OleException;
import org.kuali.ole.docstore.common.search.SearchParams;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 2/19/13
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractExternalDataSource implements ExternalDataSource {

    @Override
    public List<String> searchForBibs(SearchParams searchParams, DataSourceConfig dataSourceConfigInfo) throws Exception, OleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
