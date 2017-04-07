package br.com.hb.hyomobile.db.dao;

import android.content.Context;

/**
 * Created by vauruk on 19/03/16.
 * Nesta classe sera a fabrica de DAOs para qualquer UC pode
 * ser incluido nessa classe para se usar o patter DAO e FACTORY
 *
 */
public interface DAOFactory {

    /**
     *
     * @param context
     * @return
     */
    public abstract DAO createPersonBeaconDAO(Context context);
}
