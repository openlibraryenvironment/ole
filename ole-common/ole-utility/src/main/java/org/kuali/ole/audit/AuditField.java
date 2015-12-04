package org.kuali.ole.audit;

import java.lang.annotation.*;

/**
 * Created by maheswarang on 11/6/15.
 */
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditField {
}
