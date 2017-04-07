package br.com.hb.hyomobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.hb.hyomobile.app.AppActivity;
import br.com.hb.hyomobile.util.URLDefaults;

public class Login extends AppActivity implements View.OnClickListener{


    private EditText txtLogin;
    private EditText txtPassword;
    private Button btnLogin;

    @Override
    protected Context getContext() {
        return Login.this;
    }

    @Override
    protected void observer() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideToolBar();

        txtLogin = (EditText) findViewById(R.id.txtLogin);

        txtPassword = (EditText) findViewById(R.id.txtPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()){
            case R.id.btnLogin:

                startLoading();
                String login = txtLogin.getText().toString();
                String password = txtPassword.getText().toString() ;
                logged(getContext().toString(), "Logando ", true, false);
                this.login(login, password, v);
                stopLoading();
                break;
        }
    }

    private void login(final String login, final String password, final View v) {

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("username", login);//"wsadmin@hydrobytes.com.br"
        jsonParams.put("password", password);//"restadmin12345"


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = URLDefaults.getUrlLogin();

        // Request a string response from the provided URL.
        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.

                        try {
                           JSONObject data = response.getJSONObject("data");
                           String user_id =  data.get("userId").toString();
                           String authToken = data.get("authToken").toString();

                           setUserAppSharedPreferences(user_id, authToken, login, password);

                           Intent i = new Intent();
                           i.setClass(getContext(), MainActivity.class);
                           startActivity(i);
                           finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(v, error.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> jsonHeader = new HashMap<>();
                jsonHeader.put("Content-Type", "application/json");

                return jsonHeader;
            }


        };
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest );

    }
}
