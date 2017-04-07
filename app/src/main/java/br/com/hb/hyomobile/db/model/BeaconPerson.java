package br.com.hb.hyomobile.db.model;

import br.com.hb.hyomobile.app.annotation.Table;

/**
 * Created by vanderson on 21/03/2017.
 */

@Table(name = "tbl_beacon_person")
public class BeaconPerson extends EntityApp {
    private String name = "<Desconhecido>";
    private String beaconAddress;
    private String dataIn;

    public BeaconPerson (){}

    public BeaconPerson(int id, String name, String beaconAddress, String dateIn) {
        super.setId(id);
        this.name = name;
        this.beaconAddress = beaconAddress;
        this.dataIn = dateIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeaconAddress() {
        return beaconAddress;
    }

    public void setBeaconAddress(String beaconAddress) {
        this.beaconAddress = beaconAddress;
    }

    public String getDataIn() {
        return dataIn;
    }

    public void setDataIn(String dataIn) {
        this.dataIn = dataIn;
    }
}
