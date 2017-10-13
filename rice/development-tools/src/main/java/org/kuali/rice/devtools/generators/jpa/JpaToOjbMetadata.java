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
package org.kuali.rice.devtools.generators.jpa;

import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class JpaToOjbMetadata {

	public static void main( String[] args ) throws ClassNotFoundException {
		
		Class<? extends PersistableBusinessObjectBase> clazz =
                (Class<? extends PersistableBusinessObjectBase>) Class.forName(args[0]);

		
		StringBuffer sb = new StringBuffer( 1000 );
		Table tableAnnotation = (Table)clazz.getAnnotation( Table.class );
		
		sb.append( "	<class-descriptor class=\"" ).append( clazz.getName() ).append( "\" table=\"" );
		sb.append( tableAnnotation.name() ).append( "\">\r\n" );

		getClassFields( clazz, sb, null );
		getReferences( clazz, sb );
		sb.append( "	</class-descriptor>\r\n" );
		
		System.out.println( sb.toString() );
	}

	
	private static String javaToOjbDataType( Class<? extends Object> dataType ) {
		if ( dataType.equals( String.class ) ) {
			return "VARCHAR";
		} else if (dataType.equals(Long.class) || dataType.equals(Integer.class)) {
			return "BIGINT";
		} else if (dataType.equals(java.util.Date.class) || dataType.equals(java.sql.Date.class)) {
			return "DATE";
		} else if (dataType.equals(java.sql.Timestamp.class)) {
			return "TIMESTAMP";
		}
		return "VARCHAR";
	}
	
	private static void getClassFields( Class<? extends Object> clazz, StringBuffer sb, Map<String,AttributeOverride> overrides ) {
		// first get annotation overrides
		if ( overrides == null ) {
			overrides = new HashMap<String,AttributeOverride>();
		}
		if ( clazz.getAnnotation( AttributeOverride.class ) != null ) {
			AttributeOverride ao = (AttributeOverride)clazz.getAnnotation( AttributeOverride.class );
			if ( !overrides.containsKey(ao.name() ) ) {
				overrides.put(ao.name(), ao);
			}
		}
		if ( clazz.getAnnotation( AttributeOverrides.class ) != null ) {
			for ( AttributeOverride ao : ((AttributeOverrides)clazz.getAnnotation( AttributeOverrides.class )).value() ) {
				if ( !overrides.containsKey(ao.name() ) ) {
					overrides.put(ao.name(), ao);
				}
				overrides.put(ao.name(),ao);
			}
		}
		for ( Field field : clazz.getDeclaredFields() ) {
			Id id = (Id)field.getAnnotation( Id.class );
			Column column = (Column)field.getAnnotation( Column.class );
			if ( column != null ) {
				sb.append( "		<field-descriptor name=\"" );
				sb.append( field.getName() );
				sb.append( "\" column=\"" );
				if ( overrides.containsKey(field.getName() ) ) {
					sb.append( overrides.get(field.getName()).column().name() );
				} else {
					sb.append( column.name() );
				}
				sb.append( "\" jdbc-type=\"" );
				sb.append( javaToOjbDataType( field.getType() ) );
				sb.append( "\" " );
				if ( id != null ) {
					sb.append( "primarykey=\"true\" " );
				}
				if ( field.getName().equals( "objectId" ) ) {
					sb.append( "index=\"true\" " );
				}
				if ( field.getType() == boolean.class ) {
					sb.append( "conversion=\"org.kuali.rice.krad.util.OjbCharBooleanConversion3\" " );
				}
				if ( field.getName().equals( "versionNumber" ) ) {
					sb.append( "locking=\"true\" " );
				}
				sb.append( "/>\r\n" );
			}
		}
		if ( !clazz.equals( PersistableBusinessObject.class ) && clazz.getSuperclass() != null ) {
			getClassFields( clazz.getSuperclass(), sb, overrides );
		}
	}

	private static void getReferences( Class<? extends Object> clazz, StringBuffer sb ) {
		for ( Field field : clazz.getDeclaredFields() ) {
			JoinColumns multiKey = (JoinColumns)field.getAnnotation( JoinColumns.class );
			JoinColumn singleKey = (JoinColumn)field.getAnnotation( JoinColumn.class );
			if ( multiKey != null || singleKey != null ) {
				List<JoinColumn> keys = new ArrayList<JoinColumn>();
				if ( singleKey != null ) {
					keys.add( singleKey );
				}
				if ( multiKey != null ) {
					for ( JoinColumn col : multiKey.value() ) {
						keys.add( col );
					}
				}
				OneToOne oneToOne = field.getAnnotation( OneToOne.class );
				if ( oneToOne != null ) {
					sb.append( "		<reference-descriptor name=\"" );
					sb.append( field.getName() );
					sb.append( "\" class-ref=\"" );
					if ( !oneToOne.targetEntity().getName().equals( "void" ) ) {
						sb.append( oneToOne.targetEntity().getName() );
					} else {
						sb.append( field.getType().getName() );
					}
					sb.append( "\" auto-retrieve=\"true\" auto-update=\"none\" auto-delete=\"none\" proxy=\"true\">\r\n" );
					for ( JoinColumn col : keys ) {
						sb.append( "			<foreignkey field-ref=\"" );
						sb.append( getPropertyFromField( clazz, col.name() ) );
						sb.append( "\" />\r\n" );
					}
					sb.append( "		</reference-descriptor>\r\n" );
				}
				ManyToOne manyToOne = field.getAnnotation( ManyToOne.class );
				if ( manyToOne != null ) {
					sb.append( "		<reference-descriptor name=\"" );
					sb.append( field.getName() );
					sb.append( "\" class-ref=\"" );
					if ( !manyToOne.targetEntity().getName().equals( "void" ) ) {
						sb.append( manyToOne.targetEntity().getName() );
					} else {
						sb.append( field.getType().getName() );
					}
					sb.append( "\" auto-retrieve=\"true\" auto-update=\"none\" auto-delete=\"none\" proxy=\"true\">\r\n" );
					for ( JoinColumn col : keys ) {
						sb.append( "			<foreignkey field-ref=\"" );
						sb.append( getPropertyFromField( clazz, col.name() ) );
						sb.append( "\" />\r\n" );
					}
					sb.append( "		</reference-descriptor>\r\n" );
				}
				OneToMany oneToMany = field.getAnnotation( OneToMany.class );
				if ( oneToMany != null ) {
					sb.append( "		<collection-descriptor name=\"" );
					sb.append( field.getName() );
					sb.append( "\" element-class-ref=\"" );
					sb.append( oneToMany.targetEntity().getName() );
					sb.append( "\" collection-class=\"org.apache.ojb.broker.util.collections.ManageableArrayList\" auto-retrieve=\"true\" auto-update=\"object\" auto-delete=\"object\" proxy=\"true\">\r\n" );
					for ( JoinColumn col : keys ) {
						sb.append( "			<inverse-foreignkey field-ref=\"" );
						sb.append( getPropertyFromField( clazz, col.name() ) );
						sb.append( "\" />\r\n" );
					}
					sb.append( "		</collection-descriptor>\r\n" );
				}
			}
		}
	}
	
	private static String getPropertyFromField( Class<? extends Object> clazz, String colName ) {
		for ( Field field : clazz.getDeclaredFields() ) {
			Column column = (Column)field.getAnnotation( Column.class );
			if ( column != null ) {
				if ( column.name().equals( colName ) ) {
					return field.getName();
				}
			}
		}
		return "";
	}
}
