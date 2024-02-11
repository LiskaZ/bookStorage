package bookstore.db.daobase;

import bookstore.dataobjects.DBObject;
import bookstore.dataobjects.annotations.DBEntity;
import bookstore.dataobjects.annotations.DBFKEntity;
import bookstore.dataobjects.annotations.DBFKEntityList;
import bookstore.db.DBConnection;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Vector;

public class DBQueryCreator<T> {

    private static final String FIELD_SEPARATOR = ", ";
    private static final String SINGLE_QUOTE = "'";

    private DBObjectMetaInfoHelper<T> infoHelper;

    public DBQueryCreator(DBObjectMetaInfoHelper<T> infoHelper) {
        this.infoHelper = infoHelper;
    }

    public String createInsertQuery(T obj) {
        return "INSERT INTO " + infoHelper.getDbTableName() + "(" + getColumnStringNonNullFields(obj) + ")" +
                " VALUES (" + getValuesStringNonNullFields(obj) + ")";
    }

    public String createUpdateQuery(T obj) {
        return "UPDATE " + infoHelper.getDbTableName() +
                " SET " + getColumnAndValueStringNonNullFields(obj) +
                " WHERE " + getColumnAndValueString(infoHelper.getPKField(), obj);
    }

    public String createSelectQuery(int id) {
        boolean addPK = id != DBConnection.INVALID_ID;
        String s = "SELECT " + getColumnString() +
                " FROM " + infoHelper.getDbTableName();
        if (addPK) {
            s += " WHERE " + infoHelper.getPKColName() + " = " + id;
            s += " LIMIT 1";
        }
        return s;
    }

    public String createDeleteQuery(int id) {
        return "DELETE FROM " + infoHelper.getDbTableName() +
                " WHERE " + infoHelper.getPKColName() + " = " + escapeInt(id);
    }

    public String createFindQuery(T searchParam) {
        return "SELECT FROM " + infoHelper.getDbTableName() + " WHERE keyword = " + searchParam.toString();
    }

    private String getColumnStringNonNullFields(T obj) {
        String s = "";
        for (Field f : infoHelper.getNonNullFields(obj)) {
            s += infoHelper.getDBFieldName(f) + FIELD_SEPARATOR;
        }

        return normalizeQuery(s);
    }

    private String getValuesStringNonNullFields(T obj) {

        String valString = "";
        for (Field f : infoHelper.getNonNullFields(obj)) {
            valString += escapeValue(f, obj) + FIELD_SEPARATOR;
        }

        return normalizeQuery(valString);
    }

    private String getColumnString() {
        String s = "";
        var fields = infoHelper.getDBFieldNames();
        for(String fkName: infoHelper.getFKFieldNames()){
            fields.add(0, fkName);
        }
        if (infoHelper.hasPK()) {
            fields.add(0, infoHelper.getPKColName());
        }
        for (var f : fields) {
            s += f + FIELD_SEPARATOR;
        }

        return normalizeQuery(s);
    }

    private String getColumnAndValueString(Field f, T obj) {
        if (infoHelper.isFieldValueNonNull(f, obj)) {
            return infoHelper.getDBFieldColName(f) + " = " + escapeValue(f, obj);
        }

        return "";
    }

    private String getColumnAndValueStringNonNullFields(T obj) {
        String s = "";
        for (Field f : infoHelper.getNonNullFields(obj)) {
            s += getColumnAndValueString(f, obj) + FIELD_SEPARATOR;
        }

        return normalizeQuery(s);
    }

    private String normalizeQuery(String s) {
        s = s.trim();
        if (s.endsWith(FIELD_SEPARATOR.trim())) {
            s = s.substring(0, s.length() - 1);
        }

        return s;
    }

    private String escapeValue(Field f, T obj) {
        Object val = infoHelper.getFieldValue(f, obj);
        if (val != null) {
            if (f.getType() == Integer.class || f.getType() == Integer.TYPE) {
                return escapeInt((Integer)val);
            } else if (f.getType() == String.class) {
                return escapeString(((String) val));
            } else if (f.getType() == java.time.LocalDate.class) {
                return escapeString(((java.time.LocalDate) val).toString());
            } else if(f.getType().getSuperclass() == DBObject.class){
                try {
                    return escapeInt(((DBObject)f.get(obj)).getID());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return "0";
                }
            }
        }
        return "";
    }

    private String escapeString(String s) {
        return SINGLE_QUOTE + s + SINGLE_QUOTE;
    }

    private String escapeInt(int integer){
        return Integer.valueOf(integer).toString();
    }

    public String createDeleteFKListQuery(Field f, DBObject obj) {

        String sql = "";
        try {
            Class<?> foreignType = f.getAnnotation(DBFKEntityList.class).foreignType();
            sql = "DELETE FROM " + foreignType.getAnnotation(DBEntity.class).tableName() + " WHERE ID NOT IN (";
            Vector<? extends DBObject> vec = (Vector<? extends DBObject>)f.get(obj);
            for(DBObject i: vec) {
                sql += i.getID() + ", ";
            }
            sql = normalizeQuery(sql);
            sql += ") AND ";

            Field fkField = infoHelper.getForeignKeyField(vec.elementAt(0));
            sql += fkField.getAnnotation(DBFKEntity.class).name() + " = " + ((DBObject)obj).getID();

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return sql;
    }

    public String createSelectQueryFK(String fkColName, int fkId) {
        String s = "SELECT " + getColumnString() +
                " FROM " + infoHelper.getDbTableName();
        s += " WHERE " + fkColName + " = " + fkId;
        return s;
    }
}
