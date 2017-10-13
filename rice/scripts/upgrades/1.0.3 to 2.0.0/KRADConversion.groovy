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
/**
 * KRADConversion.groovy
 *
 * A groovy script which can be used to updates KNS to KRAD. Generate Attribute
 * definition, Inquiry and Lookup view definitions in one Xml file and Maintenance
 * view definitions in other Xml file. 
 */

import groovy.xml.XmlUtil

if (args.length < 3) { 
    println 'usage: groovy KRADConversion.groovy -object objectName -inputFilePath inputFilePath -outputFilePath outputFilePath'    
}

def objectName 
def inputFilePath
def outputFilePath

def count = 0
for (arg in args) {
  if (arg == '-object') objectName = args[count + 1]
  if (arg == '-inputFilePath') inputFilePath = args[count + 1]
  if (arg == '-outputFilePath') outputFilePath = args[count + 1]
  count++
} 

// Temporary Handling [Provide paramenters values]
if(objectName == null) {
    objectName = 'FiscalOfficer'
    inputFilePath = 'F:/groovy/'
    outputFilePath = 'F:/groovy/new/'
}       
    
Map<String, String> dDPropertyMapping = ["businessObjectClass":"objectClass", "BusinessObjectEntry":"DataObjectEntry", 
"validationPattern":"validCharactersConstraint", "AnyCharacterValidationPattern":"AnyCharacterPatternConstraint",
"AlphaValidationPattern":"AlphaPatternConstraint", "AlphaNumericValidationPattern":"AlphaNumericPatternConstraint",
"CharsetValidationPattern":"CharsetPatternConstraint", "NumericValidationPattern":"NumericPatternConstraint", 
"RegexValidationPattern":"RegexPatternConstraint", "FixedPointValidationPattern":"FixedPointValidationPattern",
"FloatingPointValidationPattern":"FloatingPointPatternConstraint","ZipcodeValidationPattern":"ZipcodePatternConstraint",
"YearValidationPattern":"YearPatternConstraint","TimestampValidationPattern":"TimestampPatternConstraint",
"PhoneNumberValidationPattern":"PhoneUSPatternConstraint","MonthValidationPattern":"MonthPatternConstraint",
"JavaClassValidationPattern":"JavaClassPatternConstraint","EmailAddressValidationPattern":"EmailPatternConstraint",
"DateValidationPattern":"DatePatternConstraint"]
List<String> removePropertyList = ["inquiryDefinition","lookupDefinition","relationships"]

def beanSchema = 'http://www.springframework.org/schema/p'
def oldSchema = 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"'
def dDRoot

try {
    dDRoot = new XmlParser().parse(inputFilePath+objectName+".xml")
} catch(java.io.FileNotFoundException ex) {
    println("Error")
    errorText()
}

def mDRoot

try {
    mDRoot = new XmlParser().parse(inputFilePath+objectName+"MaintenanceDocument.xml")
} catch(java.io.FileNotFoundException ex) {
    mDRoot = null
}

if(mDRoot == null) {
   continueWarningtext()   
}

//Data Dictionary
if(dDRoot != null) {
    // modify groceries: quality items please
    def beans = dDRoot.bean
    println("Total bean : "+ beans.size())
    
    //retrive Business Object Class
    def objectClass = dDRoot.bean.property.find{ it.@name == "businessObjectClass" }
    def objectClassName 
    if(objectClass != null) {
        objectClassName = objectClass.@value
    } else {
        errorText()
    }
    
    //Iterate through each bean
    (0..<beans.size()).each {
        def beanNode = beans[it]
        println("bean id : "+beanNode.@id)
               
        // Changes specific to BusinessObjectEntry and AttributeDefinition
        if(beanNode.@parent in ["BusinessObjectEntry","AttributeDefinition","AttributeReferenceDummy-genericSystemId"]) {                                    
            def isBusinesObjectEntry = false
            if(beanNode.@parent in ["BusinessObjectEntry"]) {
                isBusinesObjectEntry = true
            }
            //Verify Parent Change
            if(dDPropertyMapping[beanNode.@parent.toString()] != null) {                        
                beanNode.@parent = dDPropertyMapping[beanNode.@parent.toString()]
            }
            def properties = beanNode.property
            //println("Total property : "+properties.size())
            
            //Iterate through each property                                            
            (0..<properties.size()).each {
                def propertyNode = properties[it]
                //println("   -->Property name : "+propertyNode.@name)
                if(dDPropertyMapping[propertyNode.@name.toString()] != null) {
                    //println("   -->New Property name : "+dDPropertyMapping[propertyNode.@name.toString()])
                    propertyNode.@name = dDPropertyMapping[propertyNode.@name.toString()]
                }
                if(removePropertyList.contains(propertyNode.@name)) {                        
                    //println("   -->Remove Property name : "+propertyNode.@name)
                    propertyNode.replaceNode({})                        
                }                                
                def propertyBeans = propertyNode.bean
                                
                //Iterate through each propert beans
                (0..<propertyBeans.size()).each {
                    def propertyBeanNode = propertyBeans[it]                    
                    if(dDPropertyMapping[propertyBeanNode.@parent.toString()] != null) {                        
                        propertyBeanNode.@parent = dDPropertyMapping[propertyBeanNode.@parent.toString()]
                    }
                }
                
            }
            
            //Additional logic for Control field 
            def controlProperty = beanNode.property.find{ it.@name == "control" }
            def controlFieldProperty = beanNode.property.find{ it.@name == "controlField" }
            if(controlProperty != null && controlFieldProperty != null) {
                controlProperty.replaceNode({})
            } else if(controlProperty != null) {
                controlProperty.@name = controlProperty.@name + "Field"
                (0..<controlProperty.bean.size()).each {
                    def beanProperty = controlProperty.bean[it]
                    beanProperty.@parent = beanProperty.@parent.replace("Definition","")
                }                
            }                       
            
            if (isBusinesObjectEntry) {
                def titleAttributeBeanNode = beanNode.property.find{ it.@name == "titleAttribute" }
                if(titleAttributeBeanNode != null) {
                    titleAttributeBeanNode.replaceNode {
                        property(name:"titleAttribute", value:titleAttributeBeanNode.@value)
                        property(name:"primaryKeys") {
                            list {
                                value (titleAttributeBeanNode.@value)
                            }
                        }
                    }
                }
            }
           // Changes specific to Inquiry Definition  
        } else if (beanNode.@parent in ["InquiryDefinition"]) {
            def inquiryBeanNode = dDRoot.bean.find{ it.@parent == beanNode.@id }
            if(inquiryBeanNode != null) {
                inquiryBeanNode.replaceNode({})   
            }
            def inquiryParentBeanNode = beanNode
            def titlePropertyNode = inquiryParentBeanNode.property.find{ it.@name == "title"}
            def inquirySectionsPropertyNode = inquiryParentBeanNode.property.find{ it.@name == "inquirySections"}
            beanNode.replaceNode {
                bean(id:"$objectName-InquiryView",parent:"InquiryView") {
                    if(titlePropertyNode != null) {
                        property(name:"title",value:titlePropertyNode.@value)
                    }
                    property(name:"dataObjectClassName",value:objectClassName)
                    if(inquirySectionsPropertyNode != null) {
                        property(name:"Items") {
                            list {
                                (0..<inquirySectionsPropertyNode.list.bean.size()).each {
                                    def innerBeanNode = inquirySectionsPropertyNode.list.bean[it]
                                    // Checking whether the collection or not
                                    def collectionField = innerBeanNode.property.list.bean.find{ it.@parent == "InquiryCollectionDefinition" };
                                    if(collectionField == null) {                                        
                                        def sectionTitlePropertyNode = innerBeanNode.property.find{ it.@name == "title" }
                                        def columnsPropertyNode = innerBeanNode.property.find{ it.@name == "numberOfColumns" }
                                        def inquiryFieldsPropertyNode = innerBeanNode.property.find{ it.@name == "inquiryFields" }
                                        bean(parent:'GroupSectionGridLayout') {
                                            if(sectionTitlePropertyNode != null) {
                                                property(name:"title",value:sectionTitlePropertyNode.@value)
                                            }
                                            if(columnsPropertyNode != null) {
                                                property(name:"layoutManager.numberOfColumns",value:columnsPropertyNode.@value)
                                            }
                                            if(inquiryFieldsPropertyNode != null) {
                                                property(name:"items") {
                                                    list {
                                                        (0..<inquiryFieldsPropertyNode.list.bean.size()).each {
                                                            def innerInquiryFieldsBeanNode = inquiryFieldsPropertyNode.list.bean[it]
                                                            def attributeValue
                                                            innerInquiryFieldsBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("attributeName")){ attributeValue = value } }                                                                                                            
                                                            bean('xmlns:p':beanSchema,parent:'AttributeField','p:propertyName':attributeValue) 
                                                        }
                                                    }
                                                }
                                            }                                            
                                        } 
                                    } else {
                                        def sectionTitlePropertyNode = innerBeanNode.property.find{ it.@name == "title" }                                        
                                        def inquiryFieldsPropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "inquiryFields" }
                                        def businessObjectPropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "businessObjectClass" } 
                                        def attributeNamePropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "attributeName" } 
                                        def summaryTitlePropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "summaryTitle" } 
                                        def summaryFieldsPropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "summaryFields" } 
                                        bean(parent:'CollectionGroupSection') {
                                            if(sectionTitlePropertyNode != null) {
                                                property(name:"title",value:sectionTitlePropertyNode.@value)
                                            }
                                            if(businessObjectPropertyNode != null) {
                                                property(name:"collectionObjectClass",value:businessObjectPropertyNode.@value)
                                            }
                                            if(attributeNamePropertyNode != null) {
                                                property(name:"propertyName",value:attributeNamePropertyNode.@value)
                                            }
                                            if(inquiryFieldsPropertyNode != null) {
                                                property(name:"items") {
                                                    list {
                                                        (0..<inquiryFieldsPropertyNode.list.bean.size()).each {
                                                            def innerInquiryFieldsBeanNode = inquiryFieldsPropertyNode.list.bean[it]
                                                            def attributeValue
                                                            innerInquiryFieldsBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("attributeName")){ attributeValue = value } }                                                                                                            
                                                            bean('xmlns:p':beanSchema,parent:'AttributeField','p:propertyName':attributeValue) 
                                                        }
                                                    }
                                                }
                                            }
                                            if(summaryTitlePropertyNode != null) {
                                                property(name:"layoutManager.summaryTitle",value:summaryTitlePropertyNode.@value)
                                            } 
                                            if(summaryFieldsPropertyNode != null) {
                                                property(name:"layoutManager.summaryFields") {
                                                    list {
                                                        (0..<summaryFieldsPropertyNode.list.bean.size()).each {
                                                            def innerSummaryFieldsBeanNode = summaryFieldsPropertyNode.list.bean[it]
                                                            def attributeValue
                                                            innerSummaryFieldsBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("attributeName")){ attributeValue = value } }                                                                                                            
                                                            value(attributeValue) 
                                                        }
                                                    }
                                                }
                                            }
                                        } 
                                    }
                                    //println("Bean")                                                                     
                                }
                                (0..<inquirySectionsPropertyNode.list.ref.size()).each {
                                    def referenceNode = inquirySectionsPropertyNode.list.ref[it] 
                                    def referenceBeanNode = dDRoot.bean.find{ it.@id == referenceNode.@bean }
                                    def innerBeanNode = referenceBeanNode
                                    if(referenceBeanNode != null) {
                                        referenceBeanNode.replaceNode({})   
                                    }
                                    //println("Reference")
                                    // Checking whether the collection or not
                                    def collectionField = innerBeanNode.property.list.bean.find{ it.@parent == "InquiryCollectionDefinition" };
                                    if(collectionField == null) {                                        
                                        def sectionTitlePropertyNode = innerBeanNode.property.find{ it.@name == "title" }
                                        def columnsPropertyNode = innerBeanNode.property.find{ it.@name == "numberOfColumns" }
                                        def inquiryFieldsPropertyNode = innerBeanNode.property.find{ it.@name == "inquiryFields" }
                                        bean(parent:'GroupSectionGridLayout') {
                                            if(sectionTitlePropertyNode != null) {
                                                property(name:"title",value:sectionTitlePropertyNode.@value)
                                            }
                                            if(columnsPropertyNode != null) {
                                                property(name:"layoutManager.numberOfColumns",value:columnsPropertyNode.@value)
                                            }
                                            if(inquiryFieldsPropertyNode != null) {
                                                 property(name:"items") {
                                                    list {
                                                        (0..<inquiryFieldsPropertyNode.list.bean.size()).each {
                                                            def innerInquiryFieldsBeanNode = inquiryFieldsPropertyNode.list.bean[it]
                                                            def attributeValue
                                                            innerInquiryFieldsBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("attributeName")){ attributeValue = value } }                                                                                                            
                                                            bean('xmlns:p':beanSchema,parent:'AttributeField','p:propertyName':attributeValue) 
                                                        }
                                                    }
                                                 }
                                            }                                            
                                        } 
                                    } else {
                                        def sectionTitlePropertyNode = innerBeanNode.property.find{ it.@name == "title" }                                        
                                        def inquiryFieldsPropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "inquiryFields" }
                                        def businessObjectPropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "businessObjectClass" } 
                                        def attributeNamePropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "attributeName" } 
                                        def summaryTitlePropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "summaryTitle" } 
                                        def summaryFieldsPropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "summaryFields" } 
                                        bean(parent:'CollectionGroupSection') {
                                            if(sectionTitlePropertyNode != null) {
                                                property(name:"title",value:sectionTitlePropertyNode.@value)
                                            }
                                            if(businessObjectPropertyNode != null) {
                                                property(name:"collectionObjectClass",value:businessObjectPropertyNode.@value)
                                            }
                                            if(attributeNamePropertyNode != null) {
                                                property(name:"propertyName",value:attributeNamePropertyNode.@value)
                                            }
                                            if(inquiryFieldsPropertyNode != null) {
                                                property(name:"items") {
                                                    list {
                                                        (0..<inquiryFieldsPropertyNode.list.bean.size()).each {
                                                            def innerInquiryFieldsBeanNode = inquiryFieldsPropertyNode.list.bean[it]
                                                            def attributeValue
                                                            innerInquiryFieldsBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("attributeName")){ attributeValue = value } }                                                                                                            
                                                            bean('xmlns:p':beanSchema,parent:'AttributeField','p:propertyName':attributeValue) 
                                                        }
                                                    }
                                                }
                                            }
                                            if(summaryTitlePropertyNode != null) {
                                                property(name:"layoutManager.summaryTitle",value:summaryTitlePropertyNode.@value)
                                            } 
                                            if(summaryFieldsPropertyNode != null) {
                                                property(name:"layoutManager.summaryFields") {
                                                    list {
                                                        (0..<summaryFieldsPropertyNode.list.bean.size()).each {
                                                            def innerSummaryFieldsBeanNode = summaryFieldsPropertyNode.list.bean[it]
                                                            def attributeValue
                                                            innerSummaryFieldsBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("attributeName")){ attributeValue = value } }                                                                                                            
                                                            value(attributeValue) 
                                                        }
                                                    }
                                                }
                                            }
                                        } 
                                    }
                                }
                            }
                        }
                    }
                }
            }
          // Changes specific to Lookup Definition  
        } else if (beanNode.@parent in ["LookupDefinition"]) {
            def lookupBeanNode = dDRoot.bean.find{ it.@parent == beanNode.@id }
            if(lookupBeanNode != null) {
                lookupBeanNode.replaceNode({})   
            }
            def lookupParentBeanNode = beanNode
            def titlePropertyNode = lookupParentBeanNode.property.find{ it.@name == "title"}
            def lookupFieldsPropertyNode = lookupParentBeanNode.property.find{ it.@name == "lookupFields"}
            def resultFieldsPropertyNode = lookupParentBeanNode.property.find{ it.@name == "resultFields"}
            beanNode.replaceNode {
                bean(id:"$objectName-LookupView",parent:"LookupView") {
                    if(titlePropertyNode != null) {
                        property(name:"title",value:titlePropertyNode.@value)
                    }
                    property(name:"dataObjectClassName",value:objectClassName)
                    if(lookupFieldsPropertyNode != null) {
                        property(name:"criteriaFields") {
                            list {
                                (0..<lookupFieldsPropertyNode.list.bean.size()).each {
                                    def innerBeanNode = lookupFieldsPropertyNode.list.bean[it]
                                    def attributeValue
                                    innerBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("attributeName")){ attributeValue = value } }                                                                                                            
                                    bean('xmlns:p':beanSchema,parent:'LookupCriteriaAttributeField','p:propertyName':attributeValue) 
                                }
                            }
                        }
                    }
                    if(resultFieldsPropertyNode != null) {
                        property(name:"resultFields") {
                            list {
                                (0..<resultFieldsPropertyNode.list.bean.size()).each {
                                    def innerBeanNode = resultFieldsPropertyNode.list.bean[it]
                                    def attributeValue
                                    innerBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("attributeName")){ attributeValue = value } }                                                                                                            
                                    bean('xmlns:p':beanSchema,parent:'AttributeField','p:propertyName':attributeValue) 
                                }
                            }
                        }
                    }
                }
            }                                   
        }
    }

    //Maintenance Document 
    if(mDRoot != null) {        
        beans = mDRoot.bean
        println("MD Total bean : "+ beans.size())        
        //Iterate through each bean
        (0..<beans.size()).each {
            def beanNode = beans[it]
            println("bean id : "+beanNode.@id)
            def parentBeanNode = mDRoot.bean.find{ it.@parent == beanNode.@id }
            if(parentBeanNode != null) {
                //println("Removed bean id : "+parentBeanNode.@id)
                parentBeanNode.replaceNode({})   
            }
            
            if(beanNode.@parent == "MaintenanceDocumentEntry") {
                def maintenanceParentBeanNode = beanNode
                def titlePropertyNode = maintenanceParentBeanNode.property.find{ it.@name == "title"}
                def bOPropertyNode = maintenanceParentBeanNode.property.find{ it.@name == "businessObjectClass"}
                def maintainableSectionsPropertyNode = maintenanceParentBeanNode.property.find{ it.@name == "maintainableSections"}
                def maintainableClassPropertyNode = maintenanceParentBeanNode.property.find{ it.@name == "maintainableClass"}
                def lockingKeysPropertyNode = maintenanceParentBeanNode.property.find{ it.@name == "lockingKeys"}
                def docTypePropertyNode = maintenanceParentBeanNode.property.find{ it.@name == "documentTypeName"}
                def docClassPropertyNode = maintenanceParentBeanNode.property.find{ it.@name == "documentAuthorizerClass"}
                beanNode.replaceNode {
                    bean(id:"$objectName-MaintenanceView",parent:"MaintenanceView") {
                        if(titlePropertyNode != null) {
                            property(name:"title",value:titlePropertyNode.@value)
                        }
                        if(bOPropertyNode != null) {
                            property(name:"dataObjectClassName",value:bOPropertyNode.@value)
                        }
                        if(maintainableSectionsPropertyNode != null) {
                            property(name:"items") {
                                list(merge:"true") {
                                    (0..<maintainableSectionsPropertyNode.list.ref.size()).each {
                                        def referenceNode = maintainableSectionsPropertyNode.list.ref[it] 
                                        def referenceBeanNode = mDRoot.bean.find{ it.@id == referenceNode.@bean }
                                        if(referenceBeanNode != null && referenceBeanNode.@parent != "MaintainableSectionDefinition") {
                                            referenceBeanNode = mDRoot.bean.find{ it.@id == referenceBeanNode.@parent }
                                        }
                                        def innerBeanNode = referenceBeanNode
                                        if(referenceBeanNode != null) {
                                            referenceBeanNode.replaceNode({})   
                                        }
                                        // Checking whether the collection or not
                                        def collectionField = innerBeanNode.property.list.bean.find{ it.@parent == "MaintainableCollectionDefinition" };
                                        if(collectionField == null) {                                        
                                            def sectionTitlePropertyNode = innerBeanNode.property.find{ it.@name == "title" }
                                            def maintainableItemsPropertyNode = innerBeanNode.property.find{ it.@name == "maintainableItems" }
                                            bean(parent:'MaintenanceGroupSectionGridLayout') {
                                                if(sectionTitlePropertyNode != null) {
                                                    property(name:"title",value:sectionTitlePropertyNode.@value)
                                                }                                                    
                                                if(maintainableItemsPropertyNode != null) {
                                                    property(name:"items") {
                                                        list {
                                                            (0..<maintainableItemsPropertyNode.list.bean.size()).each {
                                                                def innerMaintItemsBeanNode = maintainableItemsPropertyNode.list.bean[it]
                                                                def attributeValue
                                                                innerMaintItemsBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("name")){ attributeValue = value } }                                                                                                            
                                                                bean('xmlns:p':beanSchema,parent:'AttributeField','p:propertyName':attributeValue) 
                                                            }
                                                        }
                                                    }
                                                }                                            
                                            } 
                                        } else {
                                            def sectionTitlePropertyNode = innerBeanNode.property.find{ it.@name == "title" }                                        
                                            def maintainableItems = innerBeanNode.property.list.bean.property.find{ it.@name == "maintainableFields" }
                                            def businessObjectPropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "businessObjectClass" } 
                                            def attributeNamePropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "name" } 
                                            def summaryTitlePropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "summaryTitle" } 
                                            def summaryFieldsPropertyNode = innerBeanNode.property.list.bean.property.find{ it.@name == "summaryFields" } 
                                            bean(parent:'MaintenanceCollectionGroupSection') {
                                                if(sectionTitlePropertyNode != null) {
                                                    property(name:"title",value:sectionTitlePropertyNode.@value)
                                                }
                                                if(businessObjectPropertyNode != null) {
                                                    property(name:"collectionObjectClass",value:businessObjectPropertyNode.@value)
                                                }
                                                if(attributeNamePropertyNode != null) {
                                                    property(name:"propertyName",value:attributeNamePropertyNode.@value)
                                                }
                                                if(maintainableItems != null) {
                                                    property(name:"items") {
                                                        list {
                                                            (0..<maintainableItems.list.bean.size()).each {
                                                                def innerInquiryFieldsBeanNode = maintainableItems.list.bean[it]
                                                                def attributeValue
                                                                innerInquiryFieldsBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("name")){ attributeValue = value } }                                                                                                            
                                                                bean('xmlns:p':beanSchema,parent:'AttributeField','p:propertyName':attributeValue) 
                                                            }
                                                        }
                                                    }
                                                }
                                                if(summaryTitlePropertyNode != null) {
                                                    property(name:"layoutManager.summaryTitle",value:summaryTitlePropertyNode.@value)
                                                } 
                                                if(summaryFieldsPropertyNode != null) {
                                                    property(name:"layoutManager.summaryFields") {
                                                        list {
                                                            (0..<summaryFieldsPropertyNode.list.bean.size()).each {
                                                                def innerSummaryFieldsBeanNode = summaryFieldsPropertyNode.list.bean[it]
                                                                def attributeValue
                                                                innerSummaryFieldsBeanNode.attributes().each() { key, value -> if(key.toString().endsWith("name")){ attributeValue = value } }                                                                                                            
                                                                value(attributeValue) 
                                                            }
                                                        }
                                                    }
                                                }
                                            } 
                                        }                                                                 
                                    }
                                }
                            }
                        }                            
                    }                        
                }
                
                beanNode.replaceNode {
                    bean(id:objectName+"MaintenanceDocument", parent:"MaintenanceDocumentEntry") {
                        if(bOPropertyNode != null) {
                            property(name:"businessObjectClass",value:bOPropertyNode.@value)
                        }
                        if(maintainableClassPropertyNode != null) {
                            property(name:"maintainableClass",value:maintainableClassPropertyNode.@value)
                        }
                        if(docTypePropertyNode != null) {
                            property(name:"documentTypeName",value:docTypePropertyNode.@value)
                        }
                        if(docClassPropertyNode != null) {
                            property(name:"documentAuthorizerClass",value:docClassPropertyNode.@value)
                        }
                        if(lockingKeysPropertyNode != null) {
                            property(name:"lockingKeys") {
                                list {
                                    (0..<lockingKeysPropertyNode.list.value.size()).each {
                                        value(lockingKeysPropertyNode.list.value[it].value())
                                    }
                                }
                            }
                        }
                    }
                }                                            
            }
        } 
        
        def writer = new StringWriter()
        XmlUtil.serialize(mDRoot, writer) 
        
        def fileOut 
        try {
            fileOut= new File(outputFilePath+objectName+"MaintenanceDocument.xml")
            def result = writer.toString()        
            result = result.replace("xmlns:p="+"\"$beanSchema\"","")
            result = result.replace(oldSchema, "$oldSchema xmlns:p="+"\"$beanSchema\"")
            fileOut.write(result) 
        } catch(java.io.FileNotFoundException ex) {
            errorText()
        } 
    }
    
    def writer = new StringWriter()
    XmlUtil.serialize(dDRoot, writer) 

    def fileOut 
    try {
        fileOut= new File(outputFilePath+objectName+".xml")         
        def result = writer.toString()        
        result = result.replace("xmlns:p="+"\"$beanSchema\"","")
        result = result.replace(oldSchema, "$oldSchema xmlns:p="+"\"$beanSchema\"")
        fileOut.write(result) 
    } catch(java.io.FileNotFoundException ex) {
        errorText()
    }               
}

def errorText() {
    println("""
==================================================================
               Error - Invalid Parameters
==================================================================
If this is not what you want, please supply more information:
    usage: groovy KRADConversion.groovy -object objectName -inputFilePath inputFilePath -outputFilePath outputFilePath

e.g.
    groovy KRADConversion.groovy -object FiscalOfficer -inputFilePath F:/groovy/ -outputFilePath F:/groovy/new/         
    """)
    input = new BufferedReader(new InputStreamReader(System.in))
    answer = input.readLine()
    System.exit(2)
}

def continueWarningtext() {
    println("""
==================================================================
           WARNING - Missing Maintenance Document File 
==================================================================
If this is not what you want, please supply more information:
    usage: groovy KRADConversion.groovy -object objectName -inputFilePath inputFilePath -outputFilePath outputFilePath

Do you want to continue (yes/no)?
    """)
    input = new BufferedReader(new InputStreamReader(System.in))
    answer = input.readLine()
    if (!"yes".equals(answer.trim().toLowerCase())) {
        System.exit(2)
    }    
}
  