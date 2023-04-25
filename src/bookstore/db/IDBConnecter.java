package bookstore.db;

import java.sql.Connection;

public interface IDBConnecter{

    public Connection connect();
    public boolean disconnect();
    public boolean isConnected();
}