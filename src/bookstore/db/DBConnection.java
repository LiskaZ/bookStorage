package bookstore.db;

import java.sql.*;

public class DBConnection {

    public static final int INVALID_ID = -1;

    private static IDBConnecter connecter = new DBConnecter();

    public DBConnection() {}

    public Connection connect() {
        return DBConnection.connecter.connect();
    }

    public ResultSet getKeyword(String key) {
        String sql = String.format("SELECT * FROM keywords WHERE keyword='%s'", key);
        try {
            Connection conn = this.connect();
            Statement s = conn.createStatement();
            return s.executeQuery(sql);
        } catch (SQLException e) {
             e.printStackTrace();
        }
        return null;
    }

    public ResultSet getBooks() {
        String sql = "SELECT * FROM books";
        try {
            Connection conn = this.connect();
            Statement s = conn.createStatement();
            return s.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insertKeyword(String key) {
        String sql = "INSERT INTO keywords(keyword) VALUES(?)";
        int insertId = INVALID_ID;
        ResultSet resultSet = getKeyword(key);

        try {
            if ( resultSet == null|| !resultSet.next()) {
                try (
                    Connection conn = this.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, key);
                    pstmt.executeUpdate();
                    insertId = getKeyword(key).getInt("id");

                } catch (SQLException e) {
                    System.out.printf("During Query: \"%s\"%n", sql);
                    e.printStackTrace();
                }
            } else {
                try {
                    insertId = resultSet.getInt("id");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return insertId;
    }

    public void insertBook(String author, String title, String description, int language, int type, int rating, int key1, int key2, int key3, int key4, int key5, int key6, int key7) {
        String sql = "INSERT INTO books(author, title, description, language, type, rating, key1, key2, key3, key4, key5, key6, key7) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try
        {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, author);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setInt(4, language);
            pstmt.setInt(5, type);
            pstmt.setInt(6, rating);
            pstmt.setInt(7, key1);
            pstmt.setInt(8, key2);
            pstmt.setInt(9, key3);
            pstmt.setInt(10, key4);
            pstmt.setInt(11, key5);
            pstmt.setInt(12, key6);
            pstmt.setInt(13, key7);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("During Query: \"%s\"%n", sql);
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public boolean deleteQuery(String sql)
    {
        boolean res = false;
        try {
            Statement s = connect().createStatement();
            if(!s.execute(sql) && s.getUpdateCount() >= 0) {
                res = true;
            }
            //close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public boolean close()
    {
        return connecter.disconnect();
    }

    public ResultSet selectQuery(String sql)
    {
        try {
            Statement s = connect().createStatement();
            return s.executeQuery(sql);
        }
        catch (SQLException e) {
            System.err.println(String.format("During Query :\"%s\"", sql));
            e.printStackTrace();
        }

        return null;
    }


}
