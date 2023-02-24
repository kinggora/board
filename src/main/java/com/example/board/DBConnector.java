package com.example.board;

import java.sql.*;

public class DBConnector {
    private static final String URL = "jdbc:mariadb://localhost:3306/board";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "0909";

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static void close(Connection con, Statement st, ResultSet rs){
        closeResultSet(rs);
        closeStatement(st);
        closeConnection(con);
    }

    private static void closeConnection(Connection con) {
        if(con != null){
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void closeStatement(Statement st) {
        if(st != null){
            try {
                st.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void closeResultSet(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
