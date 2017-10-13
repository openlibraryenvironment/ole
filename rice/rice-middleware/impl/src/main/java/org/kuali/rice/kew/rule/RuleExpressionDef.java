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
package org.kuali.rice.kew.rule;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.kew.api.rule.*;
import org.kuali.rice.kew.api.rule.RuleExpression;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;

/**
 * BO for rule expressions 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_RULE_EXPR_T")
//@Sequence(name="KREW_RULE_EXPR_S", property="id")
public class RuleExpressionDef extends PersistableBusinessObjectBase implements RuleExpressionContract {
    
    /**
     * Primary key
     */
    @Id
    @GeneratedValue(generator="KREW_RULE_EXPR_S")
	@GenericGenerator(name="KREW_RULE_EXPR_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RULE_EXPR_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RULE_EXPR_ID")
	private String id;
    /**
     * The type of the expression
     */
    @Column(name="TYP")
	private String type;
    /**
     * The content of the expression
     */
    @Column(name="RULE_EXPR", nullable=true)
	private String expression;
    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the expression
     */
    public String getExpression() {
        return this.expression;
    }
    /**
     * @param expression the expression to set
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * Returns whether the object is an <i>equivalent</i> rule expression, i.e.
     * the type and expression are the same.  This is necessary for rule duplicate
     * detection.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof RuleExpressionDef)) return false;
        RuleExpressionDef arg = (RuleExpressionDef) obj;
        return ObjectUtils.equals(type, arg.getType()) && ObjectUtils.equals(expression, arg.getExpression());
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static org.kuali.rice.kew.api.rule.RuleExpression to(RuleExpressionDef bo) {
        if (bo == null) {
            return null;
        }

        return RuleExpression.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    public static RuleExpressionDef from(RuleExpression im) {
        if (im == null) {
            return null;
        }

        RuleExpressionDef bo = new RuleExpressionDef();
        bo.setId(im.getId());
        bo.setType(im.getType());
        bo.setExpression(im.getExpression());
        bo.setVersionNumber(im.getVersionNumber());
        bo.setObjectId(im.getObjectId());

        return bo;
    }
}
