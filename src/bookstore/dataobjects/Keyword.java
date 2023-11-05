package bookstore.dataobjects;


import bookstore.dataobjects.annotations.DBEntity;
import bookstore.dataobjects.annotations.DBFKEntity;
import bookstore.dataobjects.annotations.DBField;

import java.awt.*;

@DBEntity(tableName = "Keywords")
public class Keyword extends DBObject{
    @DBField(name = "keyword")
    private String keyword;

    public Keyword(String text) {
        this.keyword = text;
    }

    public Keyword() {
        this.keyword = "";
    }
}
