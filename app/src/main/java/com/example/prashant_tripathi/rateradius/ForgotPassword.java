package com.example.prashant_tripathi.rateradius;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 04-08-2017.
 */
public class ForgotPassword extends AppCompatActivity{
    EditText forgot_password_email;
    TextView forgot_password_sign_in,forgot_password_join_now;
    Button forgot_password_get_password;
    private String JSON_STRING,email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        Init();
    }


    public void Init(){
        forgot_password_email=(EditText)findViewById(R.id.forgot_password_email);
        forgot_password_sign_in=(TextView)findViewById(R.id.forgot_password_sign_in);
        forgot_password_join_now=(TextView)findViewById(R.id.forgot_password_join_now);
        forgot_password_get_password=(Button)findViewById(R.id.forgot_password_get_password);
    }

    public void ChangeActivity(){
        Intent i=new Intent(ForgotPassword.this,SignIn.class);
        startActivity(i);
        finish();
    }

    public void OptionSelected(View v){
        switch (v.getId()){
            case R.id.forgot_password_get_password:
                 email=forgot_password_email.getText().toString().trim();
                 GetPassword();
                 break;

            case R.id.forgot_password_sign_in:
                Intent i=new Intent(getApplicationContext(),SignIn.class);
                startActivity(i);
                finish();
                break;

            case R.id.forgot_password_join_now:
                Intent j=new Intent(getApplicationContext(),JoinNow.class);
                startActivity(j);
                finish();
                break;
        }
    }


    private void SendPassword() {
        JSONObject jsonObject = null;
        String pwd="",usr="";
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                pwd= jo.getString(Config.KEY_PASSWORD);
                usr=jo.getString(Config.KEY_USERNAME);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(pwd.equals("")){
                Toast.makeText(getApplicationContext(),"Enter A Valid Email ID",Toast.LENGTH_LONG).show();
        }

        else {
            String msg = "Username : " + usr + "\nEmail ID : " + email + "\n Password : " + pwd;
            SendPassword send = new SendPassword(this, email, "Password Request", msg);
            send.execute();
        }
    }


    private void GetPassword() {
        class ReceiveJson extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ForgotPassword.this, "Fetching Details ...", "Please Wait ...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                SendPassword();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.SendGetRequestParam(Config.URL_GET_PASSWORD,email);
                return s;
            }
        }

        ReceiveJson rj = new ReceiveJson();
        rj.execute();
    }

}
