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
@org.hibernate.annotations.TypeDefs ({
	@org.hibernate.annotations.TypeDef(
			name="rice_encrypt_decrypt",
			typeClass=HibernateKualiEncryptDecryptUserType.class
	),
	@org.hibernate.annotations.TypeDef(
			name="rice_active_inactive",
			typeClass=HibernateKualiCharBooleanAIType.class
	),
	@org.hibernate.annotations.TypeDef(
			name="rice_hash",
			typeClass=HibernateKualiHashType.class
	),
	@org.hibernate.annotations.TypeDef(
			name="rice_decimal",
			typeClass=HibernateKualiDecimalFieldType.class
	),
	@org.hibernate.annotations.TypeDef(
			name="rice_decimal_percent",
			typeClass=HibernateKualiDecimalPercentFieldType.class
	),
	@org.hibernate.annotations.TypeDef(
			name="rice_decimal_percentage",
			typeClass=HibernateKualiDecimalPercentageFieldType.class
	),
	@org.hibernate.annotations.TypeDef(
			name="rice_percent",
			typeClass=HibernateKualiPercentFieldType.class
	),
	/*@org.hibernate.annotations.TypeDef(
		name="rice_percentage",
		typeClass=org.kuali.rice.krad.util.HibernateKualiPercentageFieldType.class
	),*/
	@org.hibernate.annotations.TypeDef(
			name="rice_integer",
			typeClass=HibernateKualiIntegerFieldType.class
	),
	@org.hibernate.annotations.TypeDef(
			name="rice_integer_percentage",
			typeClass=HibernateKualiIntegerPercentageFieldType.class
	)
})

package org.kuali.rice.core.framework.persistence.jpa.type;