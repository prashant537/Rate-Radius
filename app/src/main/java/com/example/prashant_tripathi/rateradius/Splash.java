package com.example.prashant_tripathi.rateradius;

/**
 * Created by Prashant_Tripathi on 04-08-2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new MyTask().execute();
    }
    private class MyTask extends AsyncTask<Void,Void,Void> {
        ProgressDialog progress;
        SharedPreferences share;
        String user,pass;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(Splash.this);
            progress.setTitle("Getting Ready ...");
            progress.setMessage("This May Take A While ...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            share=getSharedPreferences("RateRadius",MODE_PRIVATE);
            user=share.getString("user","");
            pass=share.getString("pass","");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            if(user.equals("")&&pass.equals("")){
                Intent i=new Intent(Splash.this,Welcome.class);
                startActivity(i);
                finish();
            }
            else{
                Intent i=new Intent(Splash.this,Home.class);
                i.putExtra("user",user);
                startActivity(i);
                finish();
            }
        }
    }
}

