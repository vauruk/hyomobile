package br.com.hb.hyomobile.db.dao;

import android.content.Context;

import br.com.hb.hyomobile.db.DataStore;

/**
 * Created by vauruk on 19/03/17.
 * Implementacao da DAO Factory para criar qualquer outra classe DAO
 *
 */
public class DAOFactorySQLite implements DAOFactory {

    @Override
    public DAO createPersonBeaconDAO(Context context) {
        return new BeaconPersonDAO(DataStore.sharedInstance(context));
    }
}
