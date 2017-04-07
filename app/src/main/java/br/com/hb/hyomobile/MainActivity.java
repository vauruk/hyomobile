package br.com.hb.hyomobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.hb.hyomobile.adapter.BeaconPersonListAdapter;
import br.com.hb.hyomobile.app.AppActivity;
import br.com.hb.hyomobile.db.dao.BeaconPersonDAO;
import br.com.hb.hyomobile.db.model.BeaconPerson;
import br.com.hb.hyomobile.util.URLDefaults;

public class MainActivity extends AppActivity implements View.OnClickListener {

    private BeaconPersonListAdapter beaconPersonListAdapter;
    private BeaconPersonDAO dao;
    private ListView listViewBeaconPerson;
    private List<BeaconPerson> listBeaconPersonSelect = new ArrayList<BeaconPerson>();
    private final int REQUEST_CODE = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewBeaconPerson = (ListView) findViewById(R.id.listBeaconPerson);

        dao = (BeaconPersonDAO) getDaoFactory().createPersonBeaconDAO(this);

        listBeaconPersonSelect = dao.listar(new BeaconPerson(), null, null, "name");

        beaconPersonListAdapter = new BeaconPersonListAdapter(getContext(), R.layout.item_beacon_person, listBeaconPersonSelect, dao);

        listViewBeaconPerson.setAdapter(beaconPersonListAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoading();

                Intent i = new Intent();
                i.setClass(getContext(), ScanBle.class);
                startActivityForResult(i, REQUEST_CODE );

                stopLoading();
                Snackbar.make(view, "Escaneando beacons", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                listBeaconPersonSelect = dao.listar(new BeaconPerson(), null, null, "name");
                beaconPersonListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected Context getContext() {
        return MainActivity.this;
    }

    @Override
    protected void observer() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

           /* case R.id.action_settings:
                //onBackPressed();
                Toast.makeText(getContext(),"Settings Clicado",Toast.LENGTH_LONG).show();

                break;*/
            case R.id.menu_logout:
                logout();

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void logout() {
        // Instantiate the RequestQueue.
        startLoading();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = URLDefaults.getUrlLogout();

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
               null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
                            String status = response.getString("status");

                            Intent i = new Intent();
                            i.setClass(getContext(), Login.class);
                            startActivity(i);
                            finish();
                            deleteUserSharedPreferences();
                            stopLoading();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopLoading();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*Snackbar.make(v, error.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                stopLoading();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> jsonHeader = new HashMap<>();
                jsonHeader.put("Content-Type", "application/json");
                jsonHeader.put("X-Auth-Token", getSharedPreferences().getString(AUTH_TOKEN, ""));
                jsonHeader.put("X-User-Id", getSharedPreferences().getString(ID_USER, ""));

                return jsonHeader;
            }


        };
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest );
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()){
            case R.id.goScanBle:

                break;
        }*/
    }
}
