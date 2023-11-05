package bookstore.db;

import bookstore.dataobjects.annotations.DBEntity;
import bookstore.dataobjects.annotations.DBField;
import bookstore.dataobjects.annotations.DBPrimaryKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

public class DBObjectMetaInfoHelper<T> {

    private T type = null;

    public DBObjectMetaInfoHelper(T type)
    {
        this.type = type;
    }

    public String getDbTableName()
    {
        Class<?> clazz = type.getClass();
        if(clazz.isAnnotationPresent(DBEntity.class))
        {
            return clazz.getAnnotation(DBEntity.class).tableName();
        }

        return "";
    }

    public Vector<String> getDBFieldNames()
    {
        Vector<String> fields = new Vector<>();
        for(Field f: getDBFields()){
            fields.add(getDBFieldName(f));
        }
        return fields;
    }

    public String getDBFieldColName(Field f)
    {
        if(isDBField(f))
        {
            return getDBFieldName(f);
        }
        else if(hasPK() && isPKField(f))
        {
            return getDBPrimaryKeyName(f);
        }

        return "";
    }

    public String getDBFieldName(Field f) {
        if(isDBField(f))
        {
            return f.getAnnotation(DBField.class).name();
        }

        return "";
    }

    public Vector<Field> getNonNullFields(T obj){
        Vector<Field> fields = new Vector<>();
        for(Field f: getDBFields()){
            if(isFieldValueNonNull(f, obj)){
                fields.add(f);
            }
        }
        return fields;
    }

    public String getPKColName()
    {
        var field = getPKField();
        if(field != null) {
            return field.getAnnotation(DBPrimaryKey.class).name();
        }

        return null;
    }

    public boolean hasPK()
    {
        return getPKField() != null;
    }

    public Field getPKField(){
        for (var f : getAnnotationFields(DBPrimaryKey.class)) {
            if (f.isAnnotationPresent(DBPrimaryKey.class)) {
                return f;
            }
        }
        return null;
    }

    public <FieldType> FieldType getFieldValueT(Field f, T obj) {
        boolean canAccess = makeFieldAccessable(f, obj);
        FieldType o = null;
        try {
            o = (FieldType)f.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        restoreAccessibility(f, canAccess);
        return o;
    }

    public Object getFieldValue(Field f, T obj) {
        boolean canAccess = makeFieldAccessable(f, obj);
        Object o = null;
        try {
            o = f.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        restoreAccessibility(f, canAccess);
        return o;
    }

    public boolean isFieldValueNonNull(Field f, T obj) {
        boolean canAccess = makeFieldAccessable(f, obj);
        boolean isNull = true;
        try {
            isNull = f.get(obj) == null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        restoreAccessibility(f, canAccess);

        return !isNull;
    }

    public T createInstance()
    {
        try {
            return (T) type.getClass().getDeclaredConstructor().newInstance();
        }
        catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public Vector<Field> getDBFieldsAccessible()
    {
        return getAnnotationFieldsAccessible(DBField.class);
    }

    public Vector<Field> getAnnotationFieldsAccessible(Class<? extends Annotation> ann) {
        return getAnnotationFields(ann, true);
    }

    private String getDBPrimaryKeyName(Field f) {
        if(isPKField(f))
        {
            return f.getAnnotation(DBPrimaryKey.class).name();
        }

        return "";
    }

    private Vector<Field> getAnnotationFields(Class<?> c, Class<? extends Annotation> ann, boolean accessible)
    {
        Vector<Field> fields = new Vector<>();
        if(c != Object.class){
            for(Field f: c.getDeclaredFields()){
                if(f.isAnnotationPresent(ann)){
                    f.setAccessible(accessible);
                    fields.add(f);
                }
            }
            fields.addAll(getAnnotationFields(c.getSuperclass(), ann, accessible));
        }

        return fields;
    }


    private Vector<Field> getDBFields()
    {
        return getAnnotationFields(DBField.class);
    }

    private Vector<Field> getAnnotationFields(Class<? extends Annotation> ann)
    {
        return getAnnotationFields(type.getClass(), ann, false);
    }

    private Vector<Field> getAnnotationFields(Class<? extends Annotation> ann, boolean accessible)
    {
        return getAnnotationFields(type.getClass(), ann, accessible);
    }

    private boolean makeFieldAccessable(Field f, T obj)
    {
        boolean canAccess = f.canAccess(obj);
        if(!canAccess) {
            f.setAccessible(true);
        }

        return canAccess;
    }

    private void restoreAccessibility(Field f, boolean accessible)
    {
        f.setAccessible(accessible);
    }

    private boolean isPKField(Field f) {
        return f.isAnnotationPresent(DBPrimaryKey.class);
    }

    private boolean isDBField(Field f) {
        return f.isAnnotationPresent(DBField.class);
    }
}
