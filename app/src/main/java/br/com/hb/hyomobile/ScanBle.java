package br.com.hb.hyomobile;

import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.hb.hyomobile.adapter.DeviceItem;

import br.com.hb.hyomobile.adapter.DeviceListAdapter;
import br.com.hb.hyomobile.app.AppActivity;
import br.com.hb.hyomobile.db.dao.BeaconPersonDAO;
import br.com.hb.hyomobile.db.model.BeaconPerson;
import br.com.hb.hyomobile.util.URLDefaults;

public class ScanBle extends AppActivity implements View.OnClickListener, BeaconConsumer, RangeNotifier {

    protected static final String TAG = "MonitoringActivity";
    private BeaconPersonDAO dao;

    private BluetoothAdapter bTAdapter;

    private BeaconManager beaconManager;

    private DeviceListAdapter deviceListAdapter;

    private List<DeviceItem> deviceItemList = new ArrayList<DeviceItem>();
    private List<Beacon> beaconItemList = new ArrayList<Beacon>();

    private Button btnScan ;
    private ListView listViewDevice = null;
    public static int REQUEST_BLUETOOTH = 1;


    @Override
    protected Context getContext() {
        return ScanBle.this;
    }

    @Override
    protected void observer() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ble);

        setTextToolBar(getResources().getString(R.string.title_scan_ble));

        btnScan = (Button )findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);

        listViewDevice = (ListView) findViewById(R.id.listViewDevice);

        verifyOnluetooth();

        dao = (BeaconPersonDAO) getDaoFactory().createPersonBeaconDAO(this);

        deviceListAdapter = new DeviceListAdapter(getContext(), R.layout.item_device, deviceItemList, dao);
        listViewDevice.setAdapter(deviceListAdapter);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=fed8,m:2-2=00,p:3-3:-41,i:4-21v"));

        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    private void verifyOnluetooth() {
        bTAdapter = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (bTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else {
            if (!bTAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQUEST_BLUETOOTH);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnScan:
              //  beaconManager.bind(this);
               // super.onRestart();
               // btnScan.setText(getResources().getString(R.string.scan));
                break;
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            beaconManager.startRangingBeaconsInRegion(region);
        }catch(RemoteException e) {
            e.printStackTrace();
        }
        beaconManager.setRangeNotifier(this);
    }

    @Override
    public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {

         /* runOnUiThread(new Runnable() {
            @Override
            public void run() {*/
                deviceItemList.clear();
                //deviceListAdapter.notifyDataSetChanged();

                for (Beacon beacon : beacons) {

                    DeviceItem item = new DeviceItem(beacon.getBluetoothName(),
                            beacon.getBluetoothAddress(), beacon.getDistance(),
                            beacon.getRssi());

                    loadingBeaconPerson(item);

                }

                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //isConectedInternet();
            //}

       // });
    }


    /* Examples
    {
      "outputs": [
        {
          "status": "success",
          "data": {
            "status": "Info: 6102 - Monitor Atualizado ",
            "statusCode": 6102,
            "result": [
              {
                "_id": "NxaPD5FfCscZjFiAS",
                "firstName": "Vanderson",
                "lastName": "Vauruk",
                "updataDevice": "2017-03-23T21:47:57.053Z",
                "company": "HydroBytes"
              }
            ]
          }
        }
      ]
    }
    * */
    private void loadingBeaconPerson(final DeviceItem item) {

        /*
         String data = "[";
        for ( Beacon beacon : beacons ) {
            data += "{"+beacon.getBluetoothAddress()+"},";
        }
        data+= data.substring(0, data.length() -1);
        data += "]";
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("data", data);
        * */

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("macaddress", item.getAddress());

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = URLDefaults.getUrlGetPerson();

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.

                        try {
                            if(item.getDistance() <= 1) {
                                JSONArray arrayData = response.getJSONArray("outputs");
                                BeaconPerson person = null;
                                for (int i = 0; i < arrayData.length(); i++) {
                                    JSONObject data = arrayData.getJSONObject(i).getJSONObject("data");
                                    JSONArray arrayResult = data.getJSONArray("result");
                                    for (int j = 0; j < arrayResult.length(); j++) {
                                        JSONObject result = arrayResult.getJSONObject(j);
                                        person = new BeaconPerson();
                                        person.setName(result.getString("firstName") + " " + result.getString("lastName"));
                                        person.setBeaconAddress(result.getString("device"));
                                        person.setDataIn(result.getString("updataDevice"));
                                        item.setPerson(person);
                                    }
                                }
                                if (item.getPerson() != null) {
                                   // onPause();
                                   // btnScan.setText(getResources().getString(R.string.parado));
                                }
                                deviceItemList.add(item);
                            }

                            Collections.sort(deviceItemList, new Comparator<DeviceItem>() {
                                @Override
                                public int compare(DeviceItem o1, DeviceItem o2) {
                                    return o1.getAddress().compareTo(o2.getAddress());
                                }
                            });

                            deviceListAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> jsonHeader = new HashMap<>();
                jsonHeader.put("Content-Type", "application/json");
                jsonHeader.put("X-Auth-Token", getSharedPreferences().getString(AUTH_TOKEN, "") );
                jsonHeader.put("X-User-Id", getSharedPreferences().getString(ID_USER, ""));
                return jsonHeader;
            }


        };
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest );
    }


    protected boolean isConectedInternet(){

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if( netInfo!=null &&  netInfo.isConnectedOrConnecting()){

            return true;
        }else{
            Toast.makeText(this, "Sem conexão com internet ou sinal baixo!", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }
}
