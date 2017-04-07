package br.com.hb.hyomobile.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.UUID;

import br.com.hb.hyomobile.R;
import br.com.hb.hyomobile.db.dao.DAOFactory;
import br.com.hb.hyomobile.db.dao.DAOFactorySQLite;

/**
 * Created by vanderson on 09/03/2017.
 */

public abstract class AppActivity extends AppCompatActivity{

    protected static final String PREFS_NAME = "prefs_hyohospital";
    protected static final String ID_USER = "id_user";
    protected static final String AUTH_TOKEN = "auth_token";

    protected static final String USERNAME = "login_name";
    protected static final String PASSWORD = "password";
    private static DAOFactory daoFactory;


    protected ProgressDialog progressDialog ;
    //metedo abstrato
    protected abstract Context getContext();
    protected abstract void observer();

    protected void hideToolBar() {
        getSupportActionBar().hide();

    }


    protected void setTextToolBar(String nameToolBar) {

        getSupportActionBar().setTitle(nameToolBar);

    }

    protected static DAOFactory getDaoFactory() {
        if (daoFactory == null) {
            daoFactory = new DAOFactorySQLite();
        }
        return daoFactory;
    }

    @Override
    public void onBackPressed() {
    /*    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        super.onBackPressed();
    }
    protected SharedPreferences getSharedPreferences(){
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPref;
    }

    protected  void setUserAppSharedPreferences(final String userId, final String auth_token, final String username, final String password) {

        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(ID_USER, userId);
        editor.putString(AUTH_TOKEN, auth_token);

        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    protected String getUserName(){
        return getSharedPreferences().getString(USERNAME, "");
    }

    protected  void deleteUserSharedPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(ID_USER);
        editor.remove(AUTH_TOKEN);

        editor.remove(USERNAME);
        editor.remove(PASSWORD);
        //editor.clear();
        editor.commit();
    }

    protected void startLoading(){
        startLoading(null, null);
    }
    protected void startLoading(String title, String message){
        if(title == null ){
            title = getResources().getString(R.string.wait);
        }
        if(message == null){
            message = getResources().getString(R.string.loading);
        }
        progressDialog = ProgressDialog.show(getContext(), title, message);
    }

    protected void stopLoading(){
        progressDialog.dismiss();
        progressDialog = null;
    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }*/
    /*private void showProfile(){

        final Dialog dialogInfo = new Dialog(this);
        dialogInfo.setContentView(R.layout.show_profile);
        dialogInfo.setTitle("Mostrar Informações");
        dialogInfo.setCancelable(true);
        KiiUser user = KiiUser.getCurrentUser();

        final TextView displayName = (TextView) dialogInfo.findViewById(R.id.show_display_name);
        final TextView email = (TextView) dialogInfo.findViewById(R.id.show_email);
        final TextView aniversario = (TextView) dialogInfo.findViewById(R.id.show_anivesario);

        displayName.setText("Dysplay Name: "+user.getDisplayname());
        email.setText("Email: "+user.getEmail());
        aniversario.setText("Aniversário:"+user.getString(DATA_NASCIMENTO));

        dialogInfo.show();
    }*/


  /*  private void changePassword() {
        final Dialog dialogChange = new Dialog(this);
        dialogChange.setContentView(R.layout.change_password);
        dialogChange.setTitle("Trocar senha");
        dialogChange.setCancelable(true);

        Button gravar = (Button) dialogChange.findViewById(R.id.btnMudarSenha);

        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText passwordOld = (EditText) dialogChange.findViewById(R.id.editPasswordOld);
                final EditText password = (EditText) dialogChange.findViewById(R.id.editChangePassword);
                final EditText password2 = (EditText) dialogChange.findViewById(R.id.editChangePasswordRetry);
                final String passOld = passwordOld.getText().toString();
                final String pass1 = password.getText().toString();
                final String pass2 = password2.getText().toString();

                if((pass1.length() > 0 && pass2.length() > 0) && pass1.equals(pass2) ){
                    String username = getUserName();
                    KiiUser.logIn(new KiiUserCallBack() {
                        @Override
                        public void onLoginCompleted(int token, KiiUser user, Exception exception) {
                            if (exception != null) {
                                logged("Erro ao Logar: ", "erro ao logar com usuario", true, true);
                                return;
                            }
                            user.changePassword(new KiiUserCallBack() {
                                @Override
                                public void onChangePasswordCompleted(int token, Exception exception) {
                                    if (exception != null) {
                                        String er = "Erro ao mudar senha";

                                        logged("Mudanca de senha:", er, true, true);

                                        return;
                                    }else{
                                        logged("Change Passwor:", "Senha mudada com sucesso.", true, true);
                                    }
                                }
                            }, pass1, passOld);
                        }
                    }, username, passOld);

                    dialogChange.dismiss();
                }else{
                    Toast.makeText(getContext(), "Senhas não são iguais", Toast.LENGTH_LONG);
                }
            }
        });
        dialogChange.show();
    }
    private void changeProfile() {

        final Dialog dialogChange = new Dialog(this);
        dialogChange.setContentView(R.layout.change_profile);
        dialogChange.setTitle("Mudar Senha");
        dialogChange.setCancelable(true);

        Button cancelar = (Button) dialogChange.findViewById(R.id.btnCancelarProfile);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChange.dismiss();
            }
        });

        Button gravar = (Button) dialogChange.findViewById(R.id.btnGravarProfile);
        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText displayName = (EditText) dialogChange.findViewById(R.id.editChangeDysplayName);
                final EditText email = (EditText) dialogChange.findViewById(R.id.editChangeEmail);
                final EditText aniversario = (EditText) dialogChange.findViewById(R.id.editChangeAniversario);

                final String txtDisplayName = displayName.getText().toString();
                final String txtEmail = email.getText().toString();
                final String txtAniversario = aniversario.getText().toString();


                salvarProfile(txtDisplayName, txtEmail,txtAniversario);
                dialogChange.dismiss();

                aniversario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                observer();
            }
        });
        dialogChange.show();
    }*/


    protected String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    protected void logged(String tag, String message, boolean isLogCat, boolean isErro){
        if(isLogCat) {
            if(isErro){
                Log.e(tag, message);
            }else {
                Log.i(tag, message);
            }
        }
        // Snackbar.make(getContext(), "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        //Toast.makeText(getContext(), "----> "+message, Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}


