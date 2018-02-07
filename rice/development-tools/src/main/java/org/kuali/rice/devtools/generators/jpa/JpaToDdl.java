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
import javax.persistence.OneToMany;
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
public class JpaToDdl {

	public static void main( String[] args ) throws ClassNotFoundException {
		
		Class<? extends PersistableBusinessObjectBase> clazz =
                (Class<? extends PersistableBusinessObjectBase>) Class.forName(args[0]);

		
		StringBuffer sb = new StringBuffer( 1000 );
		StringBuffer pk = new  StringBuffer();
		Table tableAnnotation = (Table)clazz.getAnnotation( Table.class );
		
		sb.append( "CREATE TABLE " ).append( tableAnnotation.name().toLowerCase() ).append( " (\r\n" );

		getClassFields( tableAnnotation.name().toLowerCase(), clazz, sb, pk, null );
		pk.append( " )\r\n" );
		sb.append( pk );
		sb.append( ")\r\n" );
		sb.append( "/\r\n" );
		System.out.println( sb.toString() );
		sb.setLength( 0 );
		getReferences( clazz, sb );
		System.out.println( sb.toString() );
	}

	
	private static String javaToSqlDataType( Class<? extends Object> dataType ) {
		if ( dataType.equals( String.class ) ) {
			return "VARCHAR(40)";
		} else if (dataType.equals(Long.class) || dataType.equals(Integer.class)) {
			return "NUMBER(?,?)";
		} else if (dataType.equals(java.util.Date.class) || dataType.equals(java.sql.Date.class)) {
			return "DATE";
		} else if (dataType.equals(java.sql.Timestamp.class)) {
			return "TIMESTAMP";
		}
		return "VARCHAR(40)";
	}
	
	private static void getClassFields( String tableName, Class<? extends Object> clazz, StringBuffer sb, StringBuffer pk, Map<String,AttributeOverride> overrides ) {
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
				sb.append( "\t" );
				if ( field.getName().equals( "objectId" ) ) {
					sb.append( "obj_id\t\tVARCHAR2(36) NOT NULL" );
				} else if ( field.getName().equals( "versionNumber" ) ) {
					sb.append( "ver_nbr\t\tNUMBER(8,0)" );
				} else {
					if ( overrides.containsKey(field.getName() ) ) {
						sb.append( overrides.get(field.getName()).column().name().toLowerCase() );
					} else {
						sb.append( column.name().toLowerCase() );
					}
					sb.append( "\t\t" );
					if ( field.getType() == boolean.class ) {
						sb.append( "VARCHAR2(1) DEFAULT 'Y'" );
					} else {
						sb.append( javaToSqlDataType( field.getType() ) );
					}
					if ( id != null ) {
						if ( pk.length() == 0 ) {
							pk.append( "\tCONSTRAINT " + tableName + "p1 PRIMARY KEY ( " );
						} else {
							pk.append( ", " );
						}
						if ( overrides.containsKey(field.getName() ) ) {
							pk.append( overrides.get(field.getName()).column().name().toLowerCase() );
						} else {
							pk.append( column.name().toLowerCase() );
						}
					}
				}
				sb.append( ",\r\n" );
			}
		}
		if ( !clazz.equals( PersistableBusinessObject.class ) && clazz.getSuperclass() != null ) {
			getClassFields( tableName, clazz.getSuperclass(), sb, pk, overrides );
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
				OneToMany oneToMany = field.getAnnotation( OneToMany.class );
				if ( oneToMany != null ) {
					sb.append( "ALTER TABLE " );
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
