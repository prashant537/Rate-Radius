package com.example.prashant_tripathi.rateradius;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 09-07-2017.
 */
public class JoinNow extends AppCompatActivity {
    TextView join_now_sign_in;
    EditText join_now_first_name,join_now_last_name,join_now_emp_id,join_now_email,join_now_username,join_now_password;
    Button join_now_join_now;
    private String JSON_STRING,emp_id,first="",last="",empid="",email="",username="",pwd="",desig="",usr="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_now);
        Init();
    }

    public void Init() {
        join_now_sign_in = (TextView)findViewById(R.id.join_now_sign_in);
        join_now_first_name = (EditText)findViewById(R.id.join_now_first_name);
        join_now_last_name = (EditText)findViewById(R.id.join_now_last_name);
        join_now_emp_id = (EditText)findViewById(R.id.join_now_emp_id);

        join_now_email = (EditText)findViewById(R.id.join_now_email);
        join_now_username = (EditText) findViewById(R.id.join_now_username);
        join_now_password = (EditText) findViewById(R.id.join_now_password);
        join_now_join_now = (Button) findViewById(R.id.join_now_join_now);

    }


    public void OptionSelected(View v){
        switch (v.getId()){
            case R.id.join_now_join_now:
                emp_id=join_now_emp_id.getText().toString().trim();
                username=join_now_username.getText().toString().trim();
                pwd=join_now_password.getText().toString().trim();
                if(username.length()<=0||username.length()>15){
                    join_now_username.setError("Max. 15 Characters Allowed");
                    join_now_username.setText("");
                    join_now_username.setFocusable(true);
                }
                else if(pwd.length()==0||pwd.contains(" ")){
                    join_now_password.setError("Enter A Valid Password");
                    join_now_password.setText("");
                    join_now_password.setFocusable(true);
                }
                else {
                    GetUserDetails();
                }
                break;

            case R.id.join_now_sign_in:
                Intent i=new Intent(getApplicationContext(),SignIn.class);
                startActivity(i);
                finish();
                break;
        }
    }



    private void CreateProfile(){

        class InsertDetails extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(JoinNow.this,"Creating Profile","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equals("Success")){
                    String user = join_now_username.getText().toString().trim();
                        SharedPreferences share = getSharedPreferences("RateRadius", MODE_PRIVATE);
                        SharedPreferences.Editor ed = share.edit();
                        ed.putString("user", user);
                        ed.putString("pass", pwd);
                        ed.commit();

                        Intent i = new Intent(JoinNow.this, Home.class);
                        i.putExtra("user", user);
                        startActivity(i);
                        finish();
                }
                else if(s.equals("Failed")){
                    Toast.makeText(JoinNow.this,"Some Error Occured",Toast.LENGTH_LONG).show();
                }

            }


            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params=new HashMap<>();
                params.put(Config.KEY_FIRST_NAME,first);
                params.put(Config.KEY_LAST_NAME,last);
                params.put(Config.KEY_EMPID,empid);
                params.put(Config.KEY_EMAIL,email);
                params.put(Config.KEY_USERNAME,username);
                params.put(Config.KEY_PASSWORD,pwd);
                params.put(Config.KEY_DESIGNATION,desig);

                RequestHandler rh=new RequestHandler();
                String res=rh.SendPostRequest(Config.URL_REGISTER_USER, params);
                System.out.println(res);
                return res;
            }
        }

        InsertDetails insert=new InsertDetails();
        insert.execute();
    }


    private void CheckUserDetails() {
        JSONObject jsonObject = null;
        String fname="",lname="",eid="",mail="";
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                fname=jo.getString(Config.KEY_FIRST_NAME);
                lname=jo.getString(Config.KEY_LAST_NAME);
                eid=jo.getString(Config.KEY_EMPID);
                mail=jo.getString(Config.KEY_EMAIL);
                desig=jo.getString(Config.KEY_DESIGNATION);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        first=join_now_first_name.getText().toString().trim();
        last=join_now_last_name.getText().toString().trim();
        empid=join_now_emp_id.getText().toString().trim();
        email=join_now_email.getText().toString().trim();

        if(empid.equals(eid)&&first.equals(fname)&&last.equals(lname)&&email.equals(mail)){
            GetUserExistence();
        }
        else{
           Toast.makeText(getApplicationContext(),"Not A Member Of HighRadius Family ",Toast.LENGTH_LONG).show();
        }
    }



    private void GetUserDetails() {
        class ReceiveJson extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JoinNow.this, "This May Take A While ...", "Please Wait ...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                System.out.println("json "+JSON_STRING);
                CheckUserDetails();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.SendGetRequestParam(Config.URL_USER_DETAILS,emp_id);
                return s;
            }
        }

        ReceiveJson rj = new ReceiveJson();
        rj.execute();

    }




    private void CheckUserExistence() {
        JSONObject jsonObject = null;
        String chk="";
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                chk=jo.getString(Config.KEY_USERNAME);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!chk.equals("")){
            Toast.makeText(getApplicationContext(),"Already Registered",Toast.LENGTH_LONG).show();
        }
        else{
            usr=join_now_username.getText().toString().trim();
            GetUsernameValidation();
        }
    }



    private void GetUserExistence() {
        class ReceiveJson extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JoinNow.this, "", "", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                CheckUserExistence();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.SendGetRequestParam(Config.URL_USER_EXISTENCE,emp_id);
                return s;
            }
        }

        ReceiveJson rj = new ReceiveJson();
        rj.execute();

    }



    private void CheckUsernameExistence() {
        JSONObject jsonObject = null;
        String chk="";
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                chk=jo.getString(Config.KEY_EMPID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!chk.equals("")){
            Toast.makeText(getApplicationContext(),"Username Already Exists",Toast.LENGTH_LONG).show();
        }
        else{
         CreateProfile();
        }
    }



    private void GetUsernameValidation() {
        class ReceiveJson extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JoinNow.this, "This May Take A While ...", "Please Wait ...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                CheckUsernameExistence();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.SendGetRequestParam(Config.URL_USERNAME_VALIDATION,usr);
                return s;
            }
        }

        ReceiveJson rj = new ReceiveJson();
        rj.execute();

    }

}
