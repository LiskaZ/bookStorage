package bookstore.db;

import java.awt.*;
import java.lang.reflect.Field;

public class DBQueryCreator<T> {

    private static final String FIELD_SEPARATOR = ", ";
    private static final String SINGLE_QUOTE = "'";

    private DBObjectMetaInfoHelper<T> helper;

    public DBQueryCreator(DBObjectMetaInfoHelper<T> helper) {
        this.helper = helper;
    }

    public String createInsertQuery(T obj) {
        return "INSERT INTO " + helper.getDbTableName() + "(" + getColumnStringNonNullFields(obj) + ")" +
                " VALUES (" + getValuesStringNonNullFields(obj) + ")";
    }

    public String createUpdateQuery(T obj) {
        return "UPDATE " + helper.getDbTableName() +
                " SET " + getColumnAndValueStringNonNullFields(obj) +
                " WHERE " + getColumnAndValueString(helper.getPKField(), obj);
    }

    public String createSelectQuery(int id) {
        boolean addPK = id != DBConnection.INVALID_ID;
        String s = "SELECT " + getColumnString() +
                " FROM " + helper.getDbTableName();
        if (addPK) {
            s += " WHERE " + helper.getPKColName() + " = " + id;
            s += " LIMIT 1";
        }
        return s;
    }

    private String getColumnStringNonNullFields(T obj) {
        String s = "";
        for (Field f : helper.getNonNullFields(obj)) {
            s += helper.getDBFieldName(f) + FIELD_SEPARATOR;
        }

        return normalizeQuery(s);
    }

    private String getValuesStringNonNullFields(T obj) {

        String valString = "";
        for (Field f : helper.getNonNullFields(obj)) {
            valString += escapeValue(f, obj) + FIELD_SEPARATOR;
        }

        return normalizeQuery(valString);
    }

    private String getColumnString() {
        String s = "";
        var fields = helper.getDBFieldNames();
        if (helper.hasPK()) {
            fields.add(helper.getPKColName());
        }
        for (var f : fields) {
            s += f + FIELD_SEPARATOR;
        }

        return normalizeQuery(s);
    }

    private String getColumnAndValueString(Field f, T obj) {
        if (helper.isFieldValueNonNull(f, obj)) {
            return helper.getDBFieldColName(f) + " = " + escapeValue(f, obj);
        }

        return "";
    }

    private String getColumnAndValueStringNonNullFields(T obj) {
        String s = "";
        for (Field f : helper.getNonNullFields(obj)) {
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
        Object val = helper.getFieldValue(f, obj);
        if (val != null) {
            if (f.getType() == Integer.TYPE) {
                return escapeInt((Integer)val);
            } else if (f.getType() == String.class) {
                return escapeString(((String) val));
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

    public String createDeleteQuery(int id) {

        return "DELETE FROM " + helper.getDbTableName() + " WHERE " + helper.getPKColName() + " = " + escapeInt(id);
    }
}
