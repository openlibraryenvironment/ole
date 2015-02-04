package org.kuali.ole.externalds;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 2/19/13
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExternalDataSourceFactory {
    private static ExternalDataSourceFactory externalDataSourceFactory = new ExternalDataSourceFactory();

    public static ExternalDataSourceFactory getInstance() {
        return externalDataSourceFactory;
    }

    public ExternalDataSource getExternalDataSource(DataSourceConfig dataSourceConfig) {
        ExternalDataSource externalDataSource = new Z3950DataSource();
        return externalDataSource;
    }
}
