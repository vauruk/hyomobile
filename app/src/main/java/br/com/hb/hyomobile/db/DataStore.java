package br.com.hb.hyomobile.db;

import android.content.Context;

/**
 * Created by vanderson on 21/03/2017.
 */

public class DataStore {

    private static DataStore instance;

    private static DBHelper dbHelper;

    protected DataStore() {
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public static DataStore sharedInstance(Context context) {
        if (instance == null) {
            instance = new DataStore();
            instance.setDbHelper(new DBHelper(context));
        }
        return instance;
    }
}
