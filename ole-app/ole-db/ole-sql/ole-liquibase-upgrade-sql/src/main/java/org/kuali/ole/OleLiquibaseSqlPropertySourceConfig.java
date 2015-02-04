package org.kuali.ole;

import org.kuali.common.jdbc.JdbcProjectContext;
import org.kuali.common.util.ProjectContext;
import org.kuali.common.util.spring.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.PropertySource;

/**
 * Created by pvsubrah on 7/15/14.
 */
public class OleLiquibaseSqlPropertySourceConfig {
    @Bean
    public PropertySource<?> springPropertySource() {
        ProjectContext project = new OleLiquibaseSqlProjectContext();
        ProjectContext other = new JdbcProjectContext();
        return SpringUtils.getGlobalPropertySource(project, other);
    }
}
