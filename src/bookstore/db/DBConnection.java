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

    public long insertBook(String author, String title, String description, int language, int type, int rating, int key1, int key2, int key3, int key4, int key5, int key6, int key7) {
        String sql = "INSERT INTO books(author, title, description, language, type, rating, key1, key2, key3, key4, key5, key6, key7) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        long id = 0;
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
            if(pstmt.executeUpdate() > 0)
            {
                if(pstmt.getGeneratedKeys().next())
                {
                    id = pstmt.getGeneratedKeys().getLong(1);
                }
            }
        } catch (SQLException e) {
            System.out.printf("During Query: \"%s\"%n", sql);
            e.printStackTrace();
        } finally {
            close();
        }

        return id;
    }

    public boolean deleteBook(String bookID) {


        boolean res = false;
        try {
            ResultSet book = getBook(bookID);
            if (book != null) {
                for (int i = 8; i <= 14; i++) {
                    if (book.getInt(i) != 0) {
                        deleteKeyword(book.getString(i));
                    }
                }
            }

            String sql = "DELETE FROM books WHERE id = " + bookID;
            Statement s = this.connect().createStatement();

            if(s.execute(sql) && s.getUpdateCount() >= 0) {
                res = true;
            }
            //close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public boolean deleteKeyword(String keywordsID)
    {
        String sql = "DELETE FROM keywords WHERE id = " + keywordsID;

        boolean res = false;
        try {
            Statement s = this.connect().createStatement();

            if(s.execute(sql) && s.getUpdateCount() >= 0) {
                res = true;
            }
            //close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }


    public ResultSet getBook(String bookID)
    {
        try {
            String sql = "SELECT * FROM books WHERE id = " + bookID;
            Statement s = this.connect().createStatement();

            return  s.executeQuery(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    };

    public boolean close()
    {
        return connecter.disconnect();
    }


}
