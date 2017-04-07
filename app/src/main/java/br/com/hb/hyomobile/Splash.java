package br.com.hb.hyomobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import br.com.hb.hyomobile.app.AppActivity;

public class Splash extends AppActivity {

    @Override
    protected Context getContext() {
        return Splash.this;
    }

    @Override
    protected void observer() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hideToolBar();
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {

                String idUdser = getSharedPreferences().getString(ID_USER, "");

                Intent intent = new Intent();

                if(idUdser!= null && idUdser.length()>0){
                    intent.setClass(Splash.this, MainActivity.class);
                }else {
                    intent.setClass(Splash.this, Login.class);
                }
                startActivity(intent);
                finish();

            }
        }, 3000);
    }
}
