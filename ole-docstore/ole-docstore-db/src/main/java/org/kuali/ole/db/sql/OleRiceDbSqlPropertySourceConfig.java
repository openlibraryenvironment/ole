package org.kuali.ole.db.sql;

import org.kuali.common.jdbc.JdbcProjectContext;
import org.kuali.common.util.ProjectContext;
import org.kuali.common.util.spring.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;

@Configuration
public class OleRiceDbSqlPropertySourceConfig {

    @Bean
    public PropertySource<?> springPropertySource() {
        ProjectContext project = new OleRiceDbProjectContext();
        ProjectContext other = new JdbcProjectContext();
        return SpringUtils.getGlobalPropertySource(project, other);
    }

}
