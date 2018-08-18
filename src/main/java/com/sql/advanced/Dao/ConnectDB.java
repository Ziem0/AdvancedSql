package com.sql.advanced.Dao;

import org.flywaydb.core.Flyway;
import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    private static Connection conn = null;
    private static String DB_URL = "jdbc:sqlite:src/main/resources/base.db";

    private ConnectDB() {
    }

    public static Connection getConn() {
        if (conn == null) {
            synchronized (ConnectDB.class) {
                if (conn == null) {
                    getConnection();
                }
            }
        }
        return conn;
    }

    private static void getConnection() {
        try {
            DriverManager.registerDriver(new JDBC());
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void migrate() {
        Flyway fw = new Flyway();
        fw.setDataSource(DB_URL, "none", "none");
        fw.clean();
        fw.migrate();
    }
}
