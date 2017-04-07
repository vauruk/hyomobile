package br.com.hb.hyomobile.db.model;

import br.com.hb.hyomobile.app.annotation.Id;

/**
 * Created by vanderson on 21/03/2017.
 */

public class EntityApp {
    @Id
    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

