package bookstore.db.daobase;


import bookstore.dataobjects.DBObject;

import java.util.Vector;

public interface IDAO<T extends DBObject> {

    static final boolean EAGER = true;
    static final boolean LAZY = false;

    public T load(int id);
    public Vector<T> loadAll();
    public Vector<T> loadAllFor(Class<? extends DBObject> clazz, int id);

    public T loadLazy(int id);
    public Vector<T> loadAllLazy();
    public Vector<T> loadAllLazyFor(Class<? extends DBObject> clazz, int id);

    public boolean store(T obj);
    public boolean store(Object obj);

    public boolean remove(T obj);
    public boolean remove(int id);
}
