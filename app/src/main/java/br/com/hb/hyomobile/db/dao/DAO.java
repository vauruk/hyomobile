package br.com.hb.hyomobile.db.dao;

import java.util.List;

import br.com.hb.hyomobile.db.model.EntityApp;

/**
 * Created by vauruk on 21/03/17.
 * Padronizacao de metodos a serem implementado
 */
public interface DAO {

    public List<?> listar(EntityApp tag, String whereClause, String[] whereArgs, String orderBy);
    public void gravar(EntityApp obj);
    public void excluir(EntityApp obj);
    public void alterar(EntityApp obj);
    public EntityApp carregar(int id);
}