package bookstore.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnecter implements IDBConnecter {

    private Connection conn = null;

    public Connection connect() {
        try {
            if(conn == null || conn.isClosed()) {
                try {
                    // bookstore.components.db parameters
                    String url = "jdbc:sqlite:bookStore.db";
                    // create a connection to the database
                    conn = DriverManager.getConnection(url);
                    System.out.println("Connection to SQLite has been established.");
                } catch (
                        SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    @Override
    public boolean disconnect() {
        boolean res = true;
        if(conn == null) {
            try {
                conn.close();
                conn = null;
                System.out.println("Connection to SQLite has been closed.");
            } catch (SQLException e) {
                e.printStackTrace();
                res = false;
            }
        }
        return res;
    }

    @Override
    public boolean isConnected() {
        return conn != null;
    }
}