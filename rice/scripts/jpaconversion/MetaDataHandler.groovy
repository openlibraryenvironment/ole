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
def loadMetaData(repositories, classes, logger){
	
	repositories.each {
		repository -> 
		println 'Parsing repository file: '+repository.toString()
		def xml = new XmlParser().parse(new File(repository))
		def classDescriptors = xml['class-descriptor']
		
		classDescriptors.each { 
			cd -> 
			def classDescriptor = new ClassDescriptor()
			if (!cd.'@table') {
				logger.log "Class descriptor for [${cd.'@class'}] does not have a table defined. Please check it." 
				return
			}
			classDescriptor.tableName = cd.'@table'.toUpperCase()	
			classDescriptor.className = cd.'@class'
			
			def fieldDescriptors = cd['field-descriptor']
			fieldDescriptors.each {
				fd ->
				def field = new Field()
				field.id = fd.'@id'
				field.name = fd.'@name'
				field.column = fd.'@column'
				field.jdbcType = fd.'@jdbc-type'
				field.primarykey = (fd.'@primarykey' == "true")
				field.nullable = fd.'@nullable'
				field.indexed = fd.'@indexed'
				field.autoincrement = (fd.'@autoincrement' == "true")
				field.sequenceName = fd.'@sequence-name'
				field.locking = fd.'@locking'
				field.conversion = fd.'@conversion' 
				field.access = fd.'@access'
				classDescriptor.fields[field.name] = field
				if (field.primarykey) {
					classDescriptor.primaryKeys.add(field)
				}
			}
			
			def referenceDescriptors = cd['reference-descriptor']
			referenceDescriptors.each {
				rd ->
				def referenceDescriptor = new ReferenceDescriptor()
				referenceDescriptor.name = rd.'@name'
				referenceDescriptor.classRef = rd.'@class-ref'
				referenceDescriptor.proxy = rd.'@proxy'
				referenceDescriptor.autoRetrieve = rd.'@auto-retrieve'
				referenceDescriptor.autoUpdate = rd.'@auto-update'
				referenceDescriptor.autoDelete = rd.'@auto-delete'
				
				def foreignKeys = rd['foreignkey']
				foreignKeys.each {
					fk ->
					def key = new Key()
					key.fieldRef = fk.'@field-ref'
					key.fieldIdRef = fk.'@field-id-ref'
					referenceDescriptor.foreignKeys.add(key)
				}
				
				classDescriptor.referenceDescriptors[referenceDescriptor.name] = referenceDescriptor
			} 
			
			def collectionDescriptors = cd['collection-descriptor']
			collectionDescriptors.each {
				colDesc ->
				def collectionDescriptor = new CollectionDescriptor()
				collectionDescriptor.name = colDesc.'@name'
				collectionDescriptor.collectionClass = colDesc.'@collection-class'
				collectionDescriptor.elementClassRef = colDesc.'@element-class-ref'
				collectionDescriptor.orderBy = colDesc.'@orderby'
				collectionDescriptor.sort = colDesc.'@sort'
				collectionDescriptor.indirectionTable = colDesc.'@indirection-table'
				collectionDescriptor.proxy = colDesc.'@proxy'
				collectionDescriptor.autoRetrieve = colDesc.'@auto-retrieve'
				collectionDescriptor.autoUpdate = colDesc.'@auto-update'
				collectionDescriptor.autoDelete = colDesc.'@auto-delete'
				collectionDescriptor.fkPointingToThisClassColumn = colDesc['fk-pointing-to-this-class'].'@column'
				collectionDescriptor.fkPointingToElementClassColumn = colDesc['fk-pointing-to-element-class'].'@column'
				
				def inverseForeignKeys = colDesc['inverse-foreignkey']
				inverseForeignKeys.each {
					ifk ->
					def key = new Key()
					key.fieldRef = ifk.'@field-ref'
					key.fieldIdRef = ifk.'@field-id-ref'
					collectionDescriptor.inverseForeignKeys.add(key)
				}
				
				def orderBys = colDesc['orderby']
				def ordby = new Order_By()
				if(collectionDescriptor.orderBy){
					ordby._name = collectionDescriptor.orderBy
					ordby._sort = collectionDescriptor.sort
					collectionDescriptor.orderByElements.add(ordby)
				}
				orderBys.each{
					odb ->
					ordby._name = odb.@'name'
					ordby._sort = odb.@'sort'
					collectionDescriptor.orderByElements.add(ordby)
					}
				
				classDescriptor.collectionDescriptors[collectionDescriptor.name] = collectionDescriptor
			}                             
			classDescriptor.compoundPrimaryKey = (classDescriptor.primaryKeys.size > 1)
			classes[classDescriptor.className] = classDescriptor
		} 
	}
}