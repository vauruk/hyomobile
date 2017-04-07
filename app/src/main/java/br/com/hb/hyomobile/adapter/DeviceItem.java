package br.com.hb.hyomobile.adapter;

import android.support.annotation.NonNull;

import java.util.Comparator;

import br.com.hb.hyomobile.db.model.BeaconPerson;

/**
 * Created by vanderson on 14/03/2017.
 *
 */

public class DeviceItem {
    private String deviceName;
    private String address;
    private BeaconPerson person ;
    private int rssi;

    private double distance;
    private boolean connected;
    public DeviceItem(){}

    public DeviceItem(String deviceName, String address, double distance, int rssi) {
        this.deviceName = deviceName;
        this.address = address;
        this.distance = distance;
        this.rssi = rssi;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

      @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceItem that = (DeviceItem) o;

        return address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public BeaconPerson getPerson() {
        if(person == null){
            person = new BeaconPerson();
        }
        return person;
    }

    public void setPerson(BeaconPerson person) {
        this.person = person;
    }


}
