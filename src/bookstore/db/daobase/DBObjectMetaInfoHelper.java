package bookstore.db.daobase;

import bookstore.dataobjects.DBObject;
import bookstore.dataobjects.annotations.DBEntity;
import bookstore.dataobjects.annotations.DBFKEntity;
import bookstore.dataobjects.annotations.DBField;
import bookstore.dataobjects.annotations.DBPrimaryKey;

import java.io.IOException;
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
            fields.add(getDBFieldColName(f));
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
        else if(hasFKs() && isFKField(f))
        {
            return getFKColName(f);
        }

        return "";
    }

    private String getFKColName(Field f) {
        return f.getAnnotation(DBFKEntity.class).name();
    }

    private boolean isFKField(Field f) {
        return f.isAnnotationPresent(DBFKEntity.class);
    }

    private boolean hasFKs() {
        return !getAnnotationFields(DBFKEntity.class).isEmpty();
    }

    public String getDBFieldName(Field f) {
        if(isDBField(f))
        {
            return f.getAnnotation(DBField.class).name();
        }
        else if(isFKField(f))
        {
            return f.getAnnotation(DBFKEntity.class).name();
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
        for(Field f: getFKFields()){
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
        f.setAccessible(true);
        FieldType o = null;
        try {
            o = (FieldType)f.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return o;
    }

    public Object getFieldValue(Field f, T obj) {
        f.setAccessible(true);
        Object o = null;
        try {
            o = f.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return o;
    }

    public boolean isFieldValueNonNull(Field f, T obj) {
        f.setAccessible(true);
        boolean isNull = true;
        try {
            isNull = f.get(obj) == null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

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

    private String getDBPrimaryKeyName(Field f) {
        if(isPKField(f))
        {
            return f.getAnnotation(DBPrimaryKey.class).name();
        }

        return "";
    }

    private Vector<Field> getAnnotationFields(Class<?> c, Class<? extends Annotation> ann)
    {
        Vector<Field> fields = new Vector<>();
        if(c != Object.class){
            for(Field f: c.getDeclaredFields()){
                if(f.isAnnotationPresent(ann)){
                    f.setAccessible(true);
                    fields.add(f);
                }
            }
            fields.addAll(getAnnotationFields(c.getSuperclass(), ann));
        }

        return fields;
    }


    public Vector<Field> getDBFields()
    {
        return getAnnotationFields(DBField.class);
    }

    public Vector<Field> getAnnotationFields(Class<? extends Annotation> ann)
    {
        return getAnnotationFields(type.getClass(), ann);
    }

    private boolean isPKField(Field f) {
        return f.isAnnotationPresent(DBPrimaryKey.class);
    }

    private boolean isDBField(Field f) {
        return f.isAnnotationPresent(DBField.class);
    }
    public Vector<Field> getFKFields() {
        Vector<Field> fields = new Vector<>();
        for(Field f: getAnnotationFields(DBFKEntity.class))
        {
            fields.add(f);
        }

        return fields;
    }

    public Vector<String> getFKFieldNames() {
        Vector<String> s = new Vector<>();
        for(Field f: getAnnotationFields(DBFKEntity.class))
        {
            s.add(f.getAnnotation(DBFKEntity.class).name());
        }

        return s;
    }

    public boolean isFKFieldCascade(Field f) {
        return isFKField(f) && f.getAnnotation(DBFKEntity.class).cascade();
    }

    public AbstractDAO<? extends IDAO> createDao(Class<?> c) {

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        AbstractDAO<? extends IDAO> dao = null;
        try {
            dao = (AbstractDAO<? extends IDAO>) loader.loadClass(getDaoClassName(c)).getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dao;
    }

    private String getDaoClassName(Class<?> c) throws ClassNotFoundException, IOException {
        return "bookstore.db." + c.getSimpleName() + "DAO";
    }

    public Field getForeignKeyField(DBObject i)
    {
        for(Field f: i.getClass().getDeclaredFields())
        {
            if(f.getType() == type.getClass())
            {
                return f;
            }
        }

        return null;
    }

    public Field getForeignKeyFieldLocal(DBObject i)
    {
        for(Field f: type.getClass().getDeclaredFields())
        {
            if(f.getType() ==  i.getClass())
            {
                return f;
            }
        }

        return null;
    }

    public Class<? extends DBObject> getClassOfType()
    {
        return (Class<? extends DBObject>) type.getClass();
    }
}
