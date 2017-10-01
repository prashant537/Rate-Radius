package com.example.prashant_tripathi.rateradius;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 02-08-2017.
 */
public class EditProfile extends AppCompatActivity {
    EditText edit_profile_username,edit_profile_first_name,edit_profile_last_name,edit_profile_bio,edit_profile_emp_id,
            edit_profile_email,edit_profile_password;
    String JSON_STRING,user="",fname="",lname="",desig="",empid="",bio="",email="",pwd="";
    Button edit_profile_save_changes,edit_profile_cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        Bundle bundle=getIntent().getExtras();
        user=bundle.getString("user");
        Init();
    }


    public void Init(){
        edit_profile_username=(EditText)findViewById(R.id.edit_profile_username);
        edit_profile_first_name=(EditText)findViewById(R.id.edit_profile_first_name);
        edit_profile_last_name=(EditText)findViewById(R.id.edit_profile_last_name);
        edit_profile_bio=(EditText)findViewById(R.id.edit_profile_bio);
        edit_profile_emp_id=(EditText)findViewById(R.id.edit_profile_emp_id);

        edit_profile_email=(EditText)findViewById(R.id.edit_profile_email);
        edit_profile_password=(EditText)findViewById(R.id.edit_profile_password);
        edit_profile_save_changes=(Button)findViewById(R.id.edit_profile_save_changes);
        edit_profile_cancel=(Button)findViewById(R.id.edit_profile_cancel);
        GetUserDetails();
    }


    public void OptionSelected(View v){

        switch (v.getId()){

            case R.id.edit_profile_save_changes:
                UpdateProfile();
                break;

            case R.id.edit_profile_cancel:
                Intent i=new Intent(EditProfile.this,Home.class);
                i.putExtra("user",user);
                startActivity(i);
                finish();
                break;
        }

    }


    private void ShowUserDetails() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                 user = jo.getString(Config.KEY_USERNAME);
                 fname=jo.getString(Config.KEY_FIRST_NAME);
                 lname=jo.getString(Config.KEY_LAST_NAME);
                 desig=jo.getString(Config.KEY_DESIGNATION);
                 bio=jo.getString(Config.KEY_BIO);
                 empid=jo.getString(Config.KEY_EMPID);
                 email=jo.getString(Config.KEY_EMAIL);
                 pwd=jo.getString(Config.KEY_PASSWORD);

                edit_profile_username.setText(user);

                edit_profile_first_name.setText(fname);
                edit_profile_first_name.setEnabled(false);

                edit_profile_last_name.setText(lname);
                edit_profile_last_name.setEnabled(false);

                edit_profile_bio.setText(bio);

                edit_profile_emp_id.setText(empid);
                edit_profile_emp_id.setEnabled(false);

                edit_profile_email.setText(email);
                edit_profile_password.setText(pwd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void GetUserDetails() {
        class ReceiveJson extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditProfile.this, "This May Take A While ...", "Please Wait ...", false, false);
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
                String s = rh.SendGetRequestParam(Config.URL_EDIT_PROFILE,user);
                return s;
            }
        }

        ReceiveJson rj = new ReceiveJson();
        rj.execute();

    }


    private void UpdateProfile(){

        final String first=edit_profile_first_name.getText().toString().trim();
        final String last=edit_profile_last_name.getText().toString().trim();
        final String empid=edit_profile_emp_id.getText().toString().trim();
        final String email=edit_profile_email.getText().toString().trim();
        final String username=edit_profile_username.getText().toString().trim();
        final String pwd=edit_profile_password.getText().toString().trim();
        final String bio=edit_profile_bio.getText().toString().trim();

        class UpdateDetails extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(EditProfile.this,"Updating Profile","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equals("Success")){
                    String user=edit_profile_username.getText().toString().trim();
                    Intent i=new Intent(EditProfile.this,Home.class);
                    i.putExtra("user",user);
                    startActivity(i);
                    Toast.makeText(EditProfile.this, "Successfully Updated Profile", Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(s.equals("Failed")){
                    Toast.makeText(EditProfile.this, "Some Error Occured", Toast.LENGTH_LONG).show();
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
                params.put(Config.KEY_BIO,bio);

                RequestHandler rh=new RequestHandler();
                String res=rh.SendPostRequest(Config.URL_UPDATE_USER,params);
                return res;
            }
        }

        UpdateDetails update=new UpdateDetails();
        update.execute();
    }

}
