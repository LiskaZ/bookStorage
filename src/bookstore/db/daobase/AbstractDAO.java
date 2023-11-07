package bookstore.db.daobase;

import bookstore.MainProgram;
import bookstore.dataobjects.DBObject;
import bookstore.dataobjects.annotations.DBFKEntity;
import bookstore.dataobjects.annotations.DBFKEntityList;
import bookstore.dataobjects.annotations.DBField;
import bookstore.dataobjects.annotations.DBPrimaryKey;
import bookstore.db.DBConnection;

import java.awt.*;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public abstract class AbstractDAO<T extends DBObject> implements IDAO<T>{

    private DBQueryCreator<T> queryCreator;
    protected DBObjectMetaInfoHelper<T> infoHelper;

    public AbstractDAO(T t){
        infoHelper = new DBObjectMetaInfoHelper<>(t);
        queryCreator = new DBQueryCreator<T>(infoHelper);
    }

    @Override
    public boolean store(T obj)
    {
        if(obj.getID() == DBConnection.INVALID_ID)
        {
            return insert(obj);
        }
        else {
            return update(obj);
        }
    }

    public boolean store(Object obj)
    {
        return store((T)obj);
    }

    @Override
    public T load(int id) {
        var res = loadInternal(id, EAGER);
        return res.isEmpty() ? null : res.firstElement();
    }

    public Vector<T> loadAll(){
        return loadInternal(DBConnection.INVALID_ID, EAGER);
    }

    public Vector<T> loadAllFor(Class<? extends DBObject> clazz, int id)
    {
        return loadInternalFor(clazz, id, EAGER);
    }
    @Override
    public T loadLazy(int id) {
        var res = loadInternal(id, LAZY);
        return res.isEmpty() ? null : res.firstElement();
    }
    @Override
    public Vector<T> loadAllLazy(){
        return loadInternal(DBConnection.INVALID_ID, LAZY);
    }

    public Vector<T> loadAllLazyFor(Class<? extends DBObject> clazz, int id)
    {
        return loadInternalFor(clazz, id, LAZY);
    }

    @Override
    public boolean remove(T obj) {
        return remove(infoHelper.<Integer>getFieldValueT(infoHelper.getPKField(), obj));
    }

    @Override
    public boolean remove(int id) {

        DBConnection c = MainProgram.getDBConnection();

        return c.deleteQuery(queryCreator.createDeleteQuery(id));
    }

    private boolean update(T obj) {

        DBConnection c = MainProgram.getDBConnection();

        int id = c.insertQuery(queryCreator.createUpdateQuery(obj));
        if(id != DBConnection.INVALID_ID)
        {
            storeForeignEntitiesList(obj);
            deleteForeignEntitiesList(obj);
            return true;
        }

        return false;
    }

    private boolean deleteForeignEntitiesList(T obj) {
        boolean res = true;
        for (Field f : infoHelper.getAnnotationFields(DBFKEntityList.class)) {
            f.setAccessible(true);
            if (f.getType() == Vector.class) {
                String sql = queryCreator.createDeleteFKListQuery(f, obj);

                DBConnection c = MainProgram.getDBConnection();
                res = c.query(sql);
            }
        }

        return res;
    }

    private boolean insert(T obj) {

        DBConnection c = MainProgram.getDBConnection();

        int id = c.insertQuery(queryCreator.createInsertQuery(obj));
        if(id != DBConnection.INVALID_ID)
        {
            obj.setID(id);
            storeForeignEntitiesList(obj);
            storeForeignSingleEntities(obj);

            // update foreign key ids in obj for foreign entities
            return c.insertQuery(queryCreator.createUpdateQuery(obj)) == id;
        }

        return false;
    }

    public Vector<T> find(T searchParam) {
        Vector<T> v = new Vector<>();
        DBConnection c = MainProgram.getDBConnection();

        try {
            ResultSet res = c.selectQuery(queryCreator.createFindQuery(searchParam));
            while (res.next()) {
                T instance = (T) infoHelper.createInstance();
                if(instance != null && readFromResultSet(res, instance, true))
                {
                    v.add(instance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return v;
    }

    protected Vector<T> loadInternal(int id, boolean eager) {

        Vector<T> v = new Vector<T>();
        DBConnection c = MainProgram.getDBConnection();

        try {
            ResultSet res = c.selectQuery(queryCreator.createSelectQuery(id));

            while (res.next()) {
                T obj = (T) infoHelper.createInstance();
                if(obj != null && readFromResultSet(res, obj, eager))
                {
                    v.add(obj);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return v;
    }

    protected Vector<T> loadInternalFor(Class<? extends  DBObject> clazz, int fkId, boolean eager)
    {
        for(Field f: infoHelper.getFKFields())
        {
            if(f.getType() == clazz)
            {
                Vector<T> v = new Vector<T>();
                DBConnection c = MainProgram.getDBConnection();

                String fkCol = f.getAnnotation(DBFKEntity.class).name();

                try {
                    ResultSet res = c.selectQuery(queryCreator.createSelectQueryFK(fkCol, fkId));

                    while (res.next()) {
                        T obj = (T) infoHelper.createInstance();
                        if(obj != null && readFromResultSet(res, obj, eager))
                        {
                            v.add(obj);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return v;
            }
        }
        return null;
    }

    protected boolean readFromResultSet(ResultSet res, T obj, boolean eager) {
        try {
            for (Field f : infoHelper.getAnnotationFields(DBPrimaryKey.class)) {
                if (f.getType() == Integer.TYPE) {
                    f.set(obj, res.getInt(f.getAnnotation(DBPrimaryKey.class).name()));
                }
            }
            for (Field f : infoHelper.getDBFields()) {
                if (f.getType() == Integer.TYPE) {
                    f.set(obj, res.getInt(f.getAnnotation(DBField.class).name()));
                } else if (f.getType() == String.class) {
                    f.set(obj, res.getString(f.getAnnotation(DBField.class).name()));
                }
            }
            if(eager) {
                for (Field f : infoHelper.getFKFields()) {
                    f.setAccessible(true);
                    AbstractDAO<? extends IDAO> dao = infoHelper.createDao(f.getType());
                    if (infoHelper.isFKFieldCascade(f)) {
                        f.set(obj, dao.load(res.getInt(f.getAnnotation(DBFKEntity.class).name())));
                    }
                    else {
                        f.set(obj, dao.loadLazy(res.getInt(f.getAnnotation(DBFKEntity.class).name())));
                    }
                }
                for (Field f : infoHelper.getAnnotationFields(DBFKEntityList.class)) {
                    f.setAccessible(true);
                    if (f.getType() == Vector.class) {
                        try {
                            Vector<? extends DBObject> vec = (Vector<? extends DBObject>) f.get(obj);
                            vec.clear();
                            Class<?> foreignType = f.getAnnotation(DBFKEntityList.class).foreignType();
                            AbstractDAO<? extends DBObject> dao = infoHelper.createDao(foreignType);

                            Vector<? extends DBObject> foreignObjects = new Vector<>();
                            if(eager) {
                                foreignObjects = dao.loadAllFor(infoHelper.getClassOfType(), obj.getID());
                            }
                            else {
                                foreignObjects = dao.loadAllLazyFor(infoHelper.getClassOfType(), obj.getID());
                            }

                            f.set(obj, foreignObjects);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean storeForeignEntitiesList(T parentObjToStore) {
        boolean res = true;
        for (Field f : infoHelper.getAnnotationFields(DBFKEntityList.class)) {
            f.setAccessible(true);
            if (f.getType() == Vector.class) {
                try {
                    Vector<? extends DBObject> childVector = (Vector<? extends DBObject>)f.get(parentObjToStore);
                    Class<?> vectorObjectType = f.getAnnotation(DBFKEntityList.class).foreignType();

                    for(DBObject childObjectToStore: childVector) {
                        storeForeignEntityPrivate(parentObjToStore, vectorObjectType, childObjectToStore);
                    }
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return res;
    }

    private boolean storeForeignSingleEntities(T parentObjToStore) {
        boolean res = true;
        for (Field f : infoHelper.getAnnotationFields(DBFKEntity.class)) {
            if(f.getAnnotation(DBFKEntity.class).cascade()) {
                f.setAccessible(true);
                try {
                    DBObject singleEntityObj = (DBObject) f.get(parentObjToStore);
                    storeForeignEntityPrivate(parentObjToStore, f.getType(), singleEntityObj);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return res;
    }

    private void storeForeignEntityPrivate(T parentObjToStore, Class<?> childObjectType, DBObject childObjectToStore) throws IllegalAccessException {
        if(null != childObjectToStore) {
            AbstractDAO<? extends DBObject> dao = infoHelper.createDao(childObjectType);

            Field fkFieldInChildObj = infoHelper.getForeignKeyField(childObjectToStore);
            if (null != fkFieldInChildObj) {
                fkFieldInChildObj.setAccessible(true);
                DBObject foreignObject = ((DBObject) fkFieldInChildObj.get(childObjectToStore));
                if (null == foreignObject) {
                    fkFieldInChildObj.set(childObjectToStore, infoHelper.createInstance());
                }
                ((DBObject) fkFieldInChildObj.get(childObjectToStore)).setID(parentObjToStore.getID());
            }
            dao.store(childObjectToStore);
        }
    }

    protected boolean hasPK()
    {
        return infoHelper.hasPK();
    }

    protected String getPKColName()
    {
        return infoHelper.getPKColName();
    }

}