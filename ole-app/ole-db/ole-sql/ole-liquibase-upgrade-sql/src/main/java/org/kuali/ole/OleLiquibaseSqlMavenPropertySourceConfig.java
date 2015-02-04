package org.kuali.ole;

import org.kuali.common.jdbc.JdbcProjectContext;
import org.kuali.common.util.ProjectContext;
import org.kuali.common.util.property.ProjectProperties;
import org.kuali.common.util.spring.ConfigUtils;
import org.kuali.common.util.spring.MavenPropertySourceConfig;

import java.util.List;

/**
 * Created by pvsubrah on 7/15/14.
 */
public class OleLiquibaseSqlMavenPropertySourceConfig extends MavenPropertySourceConfig {

    @Override
    protected List<ProjectProperties> getOtherProjectProperties() {
        ProjectContext jdbc = new JdbcProjectContext();
        ProjectContext oleTestSql = new OleLiquibaseSqlProjectContext();
        return ConfigUtils.getProjectProperties(jdbc, oleTestSql);
    }
}
