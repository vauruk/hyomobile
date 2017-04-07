package br.com.hb.hyomobile.db.dao;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.hb.hyomobile.db.DataStore;
import br.com.hb.hyomobile.db.model.BeaconPerson;
import br.com.hb.hyomobile.db.model.EntityApp;

/**
 * Created by vauruk on 19/03/16.
 * A implementacao especifica para o UC relacionado
 *
 */
public class BeaconPersonDAO extends DAOGeneric {


    public BeaconPersonDAO(DataStore dataStore){
        super(dataStore);
    }


    @Override
    public List<BeaconPerson> listar(EntityApp entidade, String whereClause, String[] whereArgs , String orderBy) {
        db = super.dataStore.getDbHelper().getReadableDatabase();
        Cursor cursor = createQuerySqLite(db, entidade,whereClause, whereArgs, "name");
        List<BeaconPerson> lista = new ArrayList<BeaconPerson>();
        if(cursor.moveToFirst() && cursor.getCount()>0){
            do{
                lista.add(new BeaconPerson(cursor.getInt(0), cursor.getString(3), cursor.getString(1), cursor.getString(2) ));
            }while (cursor.moveToNext());

        }
        db.close();
        return lista;
    }

    @Override
    public void gravar( EntityApp entidade) {
        db = dataStore.getDbHelper().getWritableDatabase();

        long result = super.createInsertSqlite(entidade);

    }

    @Override
    public void excluir(EntityApp entidade) {
        db = dataStore.getDbHelper().getWritableDatabase();

       int result = super.createDeleteSqlite(entidade);
    }

    @Override
    public void alterar(EntityApp obj) {

    }

}

