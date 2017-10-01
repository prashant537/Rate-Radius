package com.example.prashant_tripathi.rateradius;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prashant_Tripathi on 03-08-2017.
 */
public class ViewProfile extends AppCompatActivity{
    String user,JSON_STRING,view_user;
    TextView view_profile_username,view_profile_full_name,view_profile_empid,view_profile_designation,view_profile_bio,view_profile_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);
        Bundle bundle=getIntent().getExtras();
        user=bundle.getString("user");
        view_user=bundle.getString("view_user");

        Toolbar bottom=(Toolbar)findViewById(R.id.toolbar_bottom);
        setSupportActionBar(bottom);
        Init();
    }

    public void Init(){
        view_profile_username=(TextView)findViewById(R.id.view_profile_username);
        view_profile_full_name=(TextView)findViewById(R.id.view_profile_full_name);
        view_profile_empid=(TextView)findViewById(R.id.view_profile_empid);
        view_profile_designation=(TextView)findViewById(R.id.view_profile_designation);
        view_profile_bio=(TextView)findViewById(R.id.view_profile_bio);
        view_profile_email=(TextView)findViewById(R.id.view_profile_email);
        GetUserDetails();
    }


    public void OptionSelected(View v){
        switch(v.getId()){
            case R.id.toolbar_bottom_home:
                Intent i=new Intent(getApplicationContext(),Home.class);
                i.putExtra("user",user);
                startActivity(i);
                finish();
                break;

            case R.id.toolbar_bottom_my_feedbacks:
                Intent j=new Intent(getApplicationContext(),MyFeedbacks.class);
                j.putExtra("user", user);
                startActivity(j);
                break;

            case R.id.toolbar_bottom_edit_profile:
                Intent k=new Intent(getApplicationContext(),EditProfile.class);
                k.putExtra("user",user);
                startActivity(k);
                break;

            case R.id.toolbar_bottom_logout:
                Intent l=new Intent(getApplicationContext(),Welcome.class);
                SharedPreferences share=getSharedPreferences("RateRadius",MODE_PRIVATE);
                SharedPreferences.Editor ed=share.edit();
                ed.remove("user");
                ed.remove("pass");
                ed.commit();
                startActivity(l);
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
                String usr = jo.getString(Config.KEY_USERNAME);
                String fname=jo.getString(Config.KEY_FIRST_NAME);
                String lname=jo.getString(Config.KEY_LAST_NAME);
                String bio=jo.getString(Config.KEY_BIO);
                String empid=jo.getString(Config.KEY_EMPID);
                String designation=jo.getString(Config.KEY_DESIGNATION);
                String email=jo.getString(Config.KEY_EMAIL);

                view_profile_username.setText(usr);
                view_profile_username.setEnabled(false);

                view_profile_full_name.setText(fname + " " + lname);
                view_profile_full_name.setEnabled(false);

                view_profile_bio.setText(bio);
                view_profile_bio.setEnabled(false);

                view_profile_empid.setText(empid);
                view_profile_empid.setEnabled(false);

                view_profile_designation.setText(designation);
                view_profile_designation.setEnabled(false);

                view_profile_email.setText(email);
                view_profile_email.setEnabled(false);
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
                loading = ProgressDialog.show(ViewProfile.this, "This May Take A While ...", "Please Wait ...", false, false);
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
                String s = rh.SendGetRequestParam(Config.URL_EDIT_PROFILE,view_user);
                return s;
            }
        }

        ReceiveJson rj = new ReceiveJson();
        rj.execute();

    }

}
