/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.test;


public class TestMessageBean {//implements MessageListener {

    private static int counter = 0;

    /*public void onMessage(Message msg) {
        try {
        	final String message = "message recieved: \'"+((TextMessage)msg).getText()+"\'";
            System.out.println(message + ". count=" + ++counter);
            Transaction transaction = Current.getTransactionManager().getTransaction();
            System.out.println("what's my threads transaction 1? " + transaction);
            TransactionTemplate txTemplate = SpringServiceLocator.getTransactionTemplate();
            txTemplate.execute(new TransactionCallback() {
            	public Object doInTransaction(TransactionStatus status) {
            		try{
            		Transaction transaction = Current.getTransactionManager().getTransaction();
                    System.out.println("what's my threads transaction 2? " + transaction);
            		} catch (Exception e) { throw new RuntimeException(e); }
            		//System.out.println("My datassource? " + SpringServiceLocator.getEdenDataSource());
            		JdbcTemplate template = new JdbcTemplate(SpringServiceLocator.getEdenDataSource());
                    return template.execute(new ConnectionCallback() {
                    	public Object doInConnection(Connection connection) throws SQLException {
                    		String sql = "INSERT INTO ACTIVEMQ_TEST VALUES(?)";
                    		PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    		preparedStatement.setString(1, message);
                    		preparedStatement.executeUpdate();
                            System.out.println("doing prepared statement.");
                    		return null;
                    	}
                    });
            	}
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("caught exception jmsing");
        }
    }*/
    
//    public void onMessage(Message msg) {
//        try {
//        	final String message = "message recieved: \'"+((TextMessage)msg).getText()+"\'";
//        	JdbcTemplate template = new JdbcTemplate(SpringServiceLocator.getEdenDataSource());
//        	template.execute(new ConnectionCallback() {
//        		public Object doInConnection(Connection connection) throws SQLException {
//        			String sql = "INSERT INTO ACTIVEMQ_TEST VALUES(?)";
//        			PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        			preparedStatement.setString(1, message);
//        			preparedStatement.executeUpdate();
//        			System.out.println("doing prepared statement.");
//        			return null;
//        		}
//        	});
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("caught exception jmsing");
//        }
//    }
//
//    public static boolean isDone() {
//        return counter == 1000;
//    }
}
