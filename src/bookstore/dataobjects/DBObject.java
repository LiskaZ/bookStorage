package bookstore.dataobjects;

import bookstore.dataobjects.annotations.DBPrimaryKey;
import bookstore.db.DBConnection;

public class DBObject
{
    @DBPrimaryKey(name = "ID")
    private int ID = DBConnection.INVALID_ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
