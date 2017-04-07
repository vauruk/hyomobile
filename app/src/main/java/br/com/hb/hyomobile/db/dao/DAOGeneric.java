package br.com.hb.hyomobile.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.hb.hyomobile.app.annotation.Id;
import br.com.hb.hyomobile.app.annotation.Table;
import br.com.hb.hyomobile.db.DataStore;
import br.com.hb.hyomobile.db.model.EntityApp;


/**
 * Created by vauruk on 19/03/16.
 */
public abstract class DAOGeneric implements DAO {
    protected static DataStore dataStore;
    protected SQLiteDatabase db;

   // private final String TABLE = "NAME_TABLE";

    public DAOGeneric(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /*
        Criado para criar o metodo query do SQLite
     */
    protected Cursor createQuerySqLite(SQLiteDatabase db, EntityApp obj, String whereClause, String[] whereArgs, String orderBy) {
        return db.query(getTableName(obj), loadArrayColunn(obj), whereClause, whereArgs, null, null, orderBy);

    }

    private String[] loadArrayColunn(EntityApp obj) {
        List<String> listColunn = new ArrayList<String>();
        Class<?> clazz1 = obj.getClass();
        //TODO vanderson revisar assim que possivel
        listColunn.add("id");
        for (Field field : clazz1.getDeclaredFields()) {
            field.setAccessible(true);
            listColunn.add(field.getName());
        }

        return listColunn.toArray(new String[listColunn.size()]);
    }

    protected String getTableName(EntityApp obj) {
        Class clazz1 = obj.getClass();
        String nameTable = "";
        if (clazz1.isAnnotationPresent(Table.class)) {
            Table an = (Table) clazz1.getAnnotation(Table.class);
            nameTable = an.name();
        }
        return nameTable;
    }

    @Override
    public EntityApp carregar(int id) {
        return null;
    }

    public long createInsertSqlite(EntityApp entidade) {

        return db.insert(getTableName(entidade), null, createContentValues(entidade));
    }


    private ContentValues createContentValues(EntityApp entidade) {
        Class<?> clazz1 = entidade.getClass();
        ContentValues values = new ContentValues();

        for (Field field : clazz1.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (!field.isAnnotationPresent(Id.class)) {
                    values.put(field.getName(), (String) field.get(entidade));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return values;
    }

    /**
     * Implementa o delete no formato sqlite android
     * @param entidade
     * @return inteiro se houver um delete executado com sucesso.
     */
    public int createDeleteSqlite(EntityApp entidade) {
        return db.delete(getTableName(entidade), "id=?", new String[]{String.valueOf(entidade.getId())});
    }

}
