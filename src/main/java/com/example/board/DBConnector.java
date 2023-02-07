package com.example.board;

import java.sql.*;

public class DBConnector {

    static final String DRIVER = "org.mariadb.jdbc.Driver";
    static final String URL = "jdbc:mariadb://localhost:3306/board";
    static final String USERNAME = "root";

    static final String PASSWORD = "0909";

    public static Connection getConnection(){
        Connection conn = null;

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
