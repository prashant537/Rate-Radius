package com.example.prashant_tripathi.rateradius;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 12-07-2017.
 */
public class FeedbackOthers extends AppCompatActivity {
    private  String user,JSON_STRING;
    RatingBar feedback_others_rating;
    EditText feedback_others_feed;
    Button feedback_others_post,feedback_others_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        user=bundle.getString("user");

        setContentView(R.layout.feedback_others);

        Toolbar top=(Toolbar)findViewById(R.id.toolbar_top_1);
        Toolbar bottom=(Toolbar)findViewById(R.id.toolbar_bottom);

        TextView username=(TextView)findViewById(R.id.toolbar_top1_username);
        username.setText("@" + user);

        setSupportActionBar(top);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setSupportActionBar(bottom);

        top.setBackgroundColor(Color.parseColor("#8fbc8f"));
        bottom.setBackgroundColor(Color.parseColor("#8fbc8f"));

        Init();

    }

    public void  Init(){
        feedback_others_rating=(RatingBar)findViewById(R.id.feedback_others_rating);
        feedback_others_feed=(EditText)findViewById(R.id.feedback_others_textbox);
        feedback_others_post=(Button)findViewById(R.id.feedback_others_post);
        feedback_others_view=(Button)findViewById(R.id.feedback_others_view);
    }


    public void OptionSelected(View v){
        switch(v.getId()){
            case R.id.feedback_others_post:
                PostFeedback();
                break;

            case R.id.feedback_others_view:
                Intent m=new Intent(getApplicationContext(),ViewOtherFeedbacks.class);
                m.putExtra("user", user);
                startActivity(m);
                feedback_others_rating.setRating(0);
                feedback_others_feed.setText("");
                break;

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




    private void PostFeedback(){
        float rate=feedback_others_rating.getRating();
        final String rating=Float.toString(rate).trim();
        final String feedback=feedback_others_feed.getText().toString().trim();
        Date d=new Date();
        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String time=dateformat.format(d).toString().trim();

        System.out.println(time);


        class Posting extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(FeedbackOthers.this,"This May Take A While ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if(s.equals("Success")) {
                    feedback_others_rating.setRating(0);
                    feedback_others_feed.setText("");
                    Intent i=new Intent(getApplicationContext(),ViewOtherFeedbacks.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }
                else if(s.equals("Failed")){
                    Toast.makeText(getApplicationContext(), "Some Error Occured ", Toast.LENGTH_LONG).show();
                }
            }




            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params=new HashMap<>();
                params.put(Config.KEY_USERNAME,user);
                params.put(Config.KEY_RATING,rating);
                params.put(Config.KEY_POST_TIME,time);
                params.put(Config.KEY_FEEDBACK,feedback);

                HashMap<String,String> params1=new HashMap<>();
                params1.put(Config.KEY_USERNAME,user);
                params1.put(Config.KEY_RATING,rating);
                params1.put(Config.KEY_POST_TIME,time);
                params1.put(Config.KEY_FEEDBACK,feedback);
                params1.put(Config.KEY_CATEGORY,"OTHERS");

                RequestHandler rh = new RequestHandler();
                String res=rh.SendPostRequest(Config.URL_POST_OTHER_FEEDBACKS,params);
                res=rh.SendPostRequest(Config.URL_POST_ALL_FEEDBACKS,params1);
                System.out.println(res);
                return res;
            }
        }

        Posting post=new Posting();
        post.execute();


    }

}
