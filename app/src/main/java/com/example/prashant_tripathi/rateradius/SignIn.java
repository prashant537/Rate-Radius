package com.example.prashant_tripathi.rateradius;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Prashant_Tripathi on 09-07-2017.
 */
public class SignIn extends AppCompatActivity {
    EditText sign_in_username,sign_in_password;
    private String JSON_STRING,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        Init();
    }

    public void Init(){
        sign_in_username=(EditText)findViewById(R.id.sign_in_username);
        sign_in_password=(EditText)findViewById(R.id.sign_in_password);
    }


    private void ShowUserDetails() {
        JSONObject jsonObject = null;
        String usr="",pwd="";
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                usr=jo.getString(Config.KEY_USERNAME);
                pwd=jo.getString(Config.KEY_PASSWORD);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(usr.equals(sign_in_username.getText().toString())&&pwd.equals(sign_in_password.getText().toString())&&(!sign_in_username.getText().toString().equals("")&&(!sign_in_password.getText().toString().equals("")))){
            SharedPreferences share=getSharedPreferences("RateRadius",MODE_PRIVATE);
            SharedPreferences.Editor ed=share.edit();
            ed.putString("user",user);
            ed.putString("pass", pwd);
            ed.commit();

            Intent a=new Intent(getApplicationContext(),Home.class);
            a.putExtra("user", sign_in_username.getText().toString().trim());
            startActivity(a);
            finish();
            sign_in_username.setText("");
            sign_in_password.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(),"Invalid Credentials ...",Toast.LENGTH_LONG).show();
        }

    }


    private void CheckValidation() {
        class ReceiveJson extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignIn.this, "This May Take A While ...", "Please Wait ...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                ShowUserDetails();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.SendGetRequestParam(Config.URL_SIGN_IN,user);
                return s;
            }
        }

        ReceiveJson rj = new ReceiveJson();
        rj.execute();
    }



    public  void OptionSelected(View v){
        switch(v.getId()){
            case R.id.sign_in_sign_in:
                user=sign_in_username.getText().toString().trim();
                CheckValidation();
                break;

            case R.id.sign_in_join_now:
                Intent b=new Intent(getApplicationContext(),JoinNow.class);
                startActivity(b);
                finish();
                break;

            case R.id.sign_in_forgot:
                Intent c=new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(c);
                finish();
                break;
        }
    }


 /*   public void SendEmail(View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL , new String[]{"recipient@example.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SignIn.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }*/
}
