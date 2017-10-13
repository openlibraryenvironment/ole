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
def handleCustomerTypes(conversion, fields){
	def annotation = ''
	try{
	//println 'ojb type:\t' + conversion + '\tannotation\t' + annotation// + '\ttext\t' + text
	if (conversion.contains("OjbCharBooleanConversion")){
		annotation += "@Type(type=\"yes_no\")\n\t"
	} else if (conversion.contains("OjbCharBooleanFieldTFConversion")) {
		annotation += "@Type(type=\"true_false\")\n\t"
	} else if (conversion.contains("OjbCharBooleanFieldAIConversion")) {
		annotation += "@Type(type=\"rice_active_inactive\")\n\t"
	} else if (conversion.contains("OjbKualiHashFieldConversion")) {
		annotation += "@Type(type=\"rice_hash\")\n\t"
	} else if (conversion.contains("OjbKualiEncryptDecryptFieldConversion")) {
		annotation += "@Type(type=\"rice_encrypt_decrypt\")\n\t"
	} else if (conversion.contains("OjbKualiDecimalFieldConversion")) {
		annotation += "@Type(type=\"rice_decimal\")\n\t"
	} else if (conversion.contains("OjbDecimalKualiPercentFieldConversion")) {
		annotation += "@Type(type=\"rice_decimal_percent\")\n\t"
	} else if (conversion.contains("OjbDecimalPercentageFieldConversion")) {
		annotation += "@Type(type=\"rice_decimal_percentage\")\n\t"
	} else if (conversion.contains("OjbKualiIntegerFieldConversion")) {
		annotation += "@Type(type=\"rice_integer\")\n\t"
	} else if (conversion.contains("OjbKualiPercentFieldConversion")) {
		annotation += "@Type(type=\"rice_integer_percent\")\n\t"
	} else if (conversion.contains("OjbKualiIntegerPercentageFieldConversion")) {
		annotation += "@Type(type=\"rice_integer_percentage\")\n\t"
	} 
	//for KFS
	else if (conversion.contains("OjbAccountActiveIndicatorConversion")){
		annotation += "@Type(type=\"yes_no\")\n\t"
	} 
	else if (conversion.contains("OjbCharBooleanFieldInverseConversion")){
		annotation += "@Type(type=\"kfs_inverse_boolean\")\n\t"
	} 
	else if (conversion.contains("OjbBCPositionActiveIndicatorConversion")){
		annotation += "@Type(type=\"kfs_bc_activeindicator\")\n\t"
	} 
	else if (conversion.contains("OjbBudgetConstructionFTEConversion")){
		annotation += "@Type(type=\"kra_bc_fte\")\n\t"
	} 
	else if (conversion.contains("OjbBudgetConstructionPercentTimeConversion")){
		annotation += "@Type(type=\"kra_bc_percenttime\")\n\t"
	} 
	else if (conversion.contains("OjbPendingBCAppointmentFundingActiveIndicatorConversion")){
		annotation += "@Type(type=\"kra_bc_pendingfund_activeindictor\")\n\t"
	} 
	
	//for KRA
	else if (conversion.contains("UnitContactTypeConverter")){
		annotation += "@Type(type=\"kra_unit_contact\")\n\t"
	} 
	else if (conversion.contains("OjbBudgetDecimalFieldConversion")){
		annotation += "@Type(type=\"kra_decimal\")\n\t"
	} 
	else if (conversion.contains("OjbOnOffCampusFlagFieldConversion")){
		annotation += "@Type(type=\"kra_campus_flag\")\n\t"
	} 
	else if (conversion.contains("OjbRateDecimalFieldConversion")){
		annotation += "@Type(type=\"kra_rate_decimal\")\n\t"
	} 

	else {
		println "UNHANDLED CONVERSION FOUND "+fields.column
		println "conversion="+conversion
		println "name="+fields.name
	}
	}
	catch(Exception e){ println(e.getMessage());}
	
	annotation
}



def handleTypes(conversion, annotationMap, annotationKey , /*textKey,*/ fields){
	try{
	def annotation = annotationMap.get(annotationKey);
	//def text = annotationList.get(textKey);
	//println 'ojb type:\t' + conversion + '\tannotation\t' + annotation// + '\ttext\t' + text
	if (conversion.contains("OjbCharBooleanConversion")){
		annotation += "@Type(type=\"yes_no\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} else if (conversion.contains("OjbCharBooleanFieldTFConversion")) {
		annotation += "@Type(type=\"true_false\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} else if (conversion.contains("OjbCharBooleanFieldAIConversion")) {
		annotation += "@Type(type=\"rice_active_inactive\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} else if (conversion.contains("OjbKualiHashFieldConversion")) {
		annotation += "@Type(type=\"rice_hash\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} else if (conversion.contains("OjbKualiEncryptDecryptFieldConversion")) {
		annotation += "@Type(type=\"rice_encrypt_decrypt\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} else if (conversion.contains("OjbKualiDecimalFieldConversion")) {
		annotation += "@Type(type=\"rice_decimal\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} else if (conversion.contains("OjbDecimalKualiPercentFieldConversion")) {
		annotation += "@Type(type=\"rice_decimal_percent\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} else if (conversion.contains("OjbDecimalPercentageFieldConversion")) {
		annotation += "@Type(type=\"rice_decimal_percentage\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} else if (conversion.contains("OjbKualiIntegerFieldConversion")) {
		annotation += "@Type(type=\"rice_integer\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} else if (conversion.contains("OjbKualiPercentFieldConversion")) {
		annotation += "@Type(type=\"rice_integer_percent\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} else if (conversion.contains("OjbKualiIntegerPercentageFieldConversion")) {
		annotation += "@Type(type=\"rice_integer_percentage\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} 
	//for KFS
	else if (conversion.contains("OjbAccountActiveIndicatorConversion")){
		annotation += "@Type(type=\"yes_no\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} 
	else if (conversion.contains("OjbCharBooleanFieldInverseConversion")){
		annotation += "@Type(type=\"kfs_inverse_boolean\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} 
	else if (conversion.contains("OjbBCPositionActiveIndicatorConversion")){
		annotation += "@Type(type=\"kfs_bc_activeindicator\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} 
	else if (conversion.contains("OjbBudgetConstructionFTEConversion")){
		annotation += "@Type(type=\"kra_bc_fte\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} 
	else if (conversion.contains("OjbBudgetConstructionPercentTimeConversion")){
		annotation += "@Type(type=\"kra_bc_percenttime\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} 
	else if (conversion.contains("OjbPendingBCAppointmentFundingActiveIndicatorConversion")){
		annotation += "@Type(type=\"kra_bc_pendingfund_activeindictor\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} 
	
	//for KRA
	else if (conversion.contains("UnitContactTypeConverter")){
		annotation += "@Type(type=\"kra_unit_contact\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} 
	else if (conversion.contains("OjbBudgetDecimalFieldConversion")){
		annotation += "@Type(type=\"kra_decimal\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} 
	else if (conversion.contains("OjbOnOffCampusFlagFieldConversion")){
		annotation += "@Type(type=\"kra_campus_flag\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Typee")
	} 
	else if (conversion.contains("OjbRateDecimalFieldConversion")){
		annotation += "@Type(type=\"kra_rate_decimal\")\n\t"
		//text = addOtherImport(text, "org.hibernate.annotations.Type")
	} 

	else {
		println "UNHANDLED CONVERSION FOUND "+fields.column
		println "conversion="+conversion
		println "name="+fields.name
	}
	
	//println '----ojb type:\t' + conversion + '\tannotation\t' + annotation 
	annotationMap.put(annotationKey, annotation);
	//annotationList.put(textKey, text);
	
	//println '++++ojb type:\t' + conversion + '\tannotation\t' + annotationMap.get(annotationKey); 
	}
	catch(Exception e){ println(e.getMessage());}
}

def addOtherImport(javaText, importText) {
	importText = "import ${importText};"
	if (!javaText.contains(importText)) {
		javaText = javaText.replaceFirst("package.*;", "\$0\n" + importText)
	}
	javaText 
}