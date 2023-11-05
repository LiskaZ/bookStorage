package bookstore.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.*;

public class DBConnection {

    Logger logger = LoggerFactory.getLogger(DBConnection.class);

    public static final int INVALID_ID = -1;

    private static IDBConnecter connecter = new DBConnecter();

    public static void setDBConnecter(IDBConnecter connecter)
    {
        DBConnection.connecter = connecter;
    }

    public DBConnection()
    {
    }

    public Connection getConnection() {
        return connect();
    }

    public int insertQuery(String sql)
    {
        int insertId = INVALID_ID;
        try {
            Statement s = getConnection().createStatement();
            s.execute(sanitizeQuery(sql));
            if(s.getUpdateCount() > 0) {
                insertId = lastInsertId();
            }
        }
        catch (SQLException e) {
            logger.info(String.format("During Query: \"%s\"", sql));
            e.printStackTrace();
        }

        return insertId;
    }

    public boolean deleteQuery(String sql)
    {
        boolean res = false;
        try {
            Statement s = getConnection().createStatement();
            if(!s.execute(sanitizeQuery(sql)) && s.getUpdateCount() >= 0) {
                res = true;
            }
            //close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public boolean query(String sql)
    {
        boolean res = false;
        try {
            Statement s = getConnection().createStatement();
            if(!s.execute(sanitizeQuery(sql))) {
                if (s.getUpdateCount() >= 0)
                {
                    res = true;
                }
            } else {
                res = true;
            }
            //close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    private Connection connect() {
        return DBConnection.connecter.connect();
    }

    public static boolean isIdValid(int id)
    {
        return INVALID_ID != id;
    }

    private int lastInsertId()
    {
        try {
            Statement s = getConnection().createStatement();
            ResultSet set = s.executeQuery(sanitizeQuery("SELECT last_insert_rowid() as id"));
            if(set.next())
            {
                return set.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return DBConnection.INVALID_ID;
    }

    public boolean close()
    {
        return connecter.disconnect();
    }

    private String sanitizeQuery(String s)
    {
        s = s.trim();
        if(!s.endsWith(";"))
        {
            s += ";";
        }

        return s;
    }

    public ResultSet selectQuery(String sql)
    {
        try {
            Statement s = getConnection().createStatement();
            return s.executeQuery(sanitizeQuery(sql));
        }
        catch (SQLException e) {
            logger.error(String.format("During Query :\"%s\"", sql));
            e.printStackTrace();
        }

        return null;
    }
}
