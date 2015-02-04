package org.kuali.ole.utility;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseComparer {

    public static void main(String[] args) {
        new DatabaseComparer().execute();
    }

    public void execute() {
        Credentials c1 = getOleFsAntCredentials();
        Credentials c2 = getOleFsMvnCredentials();
        Connection conn1 = getConnection(c1);
        Connection conn2 = getConnection(c2);
        List<Table> tables1 = getTables(conn1);
        List<Table> tables2 = getTables(conn2);
        System.out.println(tables1.size() + " " + tables2.size());
        boolean equals = equals(tables1, tables2);
        System.out.println(equals);
    }

    /**
     * Return true if the tables have the same name and the same number of rows. False otherwise.
     */
    public boolean equals(Table table1, Table table2) {
        String name1 = table1.getName();
        String name2 = table2.getName();
        int rows1 = table1.getRowCount();
        int rows2 = table2.getRowCount();
        return name1.equals(name2) && rows1 == rows2;
    }

    public boolean equals(List<Table> tables1, List<Table> tables2) {
        // Make sure the lists are the same size
        if (tables1.size() != tables2.size()) {
            return false;
        }

        // Both lists are the same size, so just grab the size from one of them
        int size = tables1.size();

        // Sort both lists on table name
        Collections.sort(tables1);
        Collections.sort(tables2);

        // Iterate through the list and compare the table from each db
        for (int i = 0; i < size; i++) {
            Table table1 = tables1.get(i);
            Table table2 = tables2.get(i);
            boolean equals = equals(table1, table2);
            if (!equals) {
                return false;
            }
        }
        return true;
    }

    public Credentials getOleFsAntCredentials() {
        Credentials c = new Credentials();
        c.setUsername("olefsant");
        c.setPassword("olefsant");
        c.setUrl("jdbc:mysql://localhost/olefsant");
        return c;
    }

    public Credentials getOleFsMvnCredentials() {
        Credentials c = new Credentials();
        c.setUsername("olefsmvn");
        c.setPassword("olefsmvn");
        c.setUrl("jdbc:mysql://localhost/olefsmvn");
        return c;
    }

    public Connection getConnection(Credentials c) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(c.getUrl(), c.getUsername(), c.getPassword());
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected void showResultSet(ResultSet rs) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                int columnIndex = i + 1;
                String name = md.getColumnName(columnIndex);
                System.out.print(name + " ");
            }
            System.out.println();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    protected int getRowCount(Connection c, Table table) {
        String sql = "select count(*) from " + table.getName();
        PreparedStatement s = null;
        try {
            s = c.prepareStatement(sql);
            ResultSet rs = s.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

    }

    protected List<Table> getTables(Connection c) {
        try {
            DatabaseMetaData md = c.getMetaData();
            ResultSet rs = md.getTables(null, null, null, null);
            List<Table> tables = new ArrayList<Table>();
            int count = 0;
            while (rs.next()) {
                count++;
                String name = rs.getString(3);
                Table table = new Table();
                table.setName(name);
                int rowCount = getRowCount(c, table);
                table.setRowCount(rowCount);
                tables.add(table);
                if ((count % 300) == 0) {
                    System.out.println();
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
            return tables;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
