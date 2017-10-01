package com.example.prashant_tripathi.rateradius;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

/**
 * Created by Prashant_Tripathi on 10-07-2017.
 */
public class Home extends AppCompatActivity {
    private String user,designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Bundle bundle=getIntent().getExtras();
        user=bundle.getString("user");

        Toolbar bottom=(Toolbar)findViewById(R.id.toolbar_bottom_2);
        setSupportActionBar(bottom);
        bottom.setBackgroundColor(Color.parseColor("#c0c0c0"));
    }


    public void OptionSelected(View v){
        switch(v.getId()){
            case R.id.home_admin_fwd:
                Intent i=new Intent(getApplicationContext(),FeedbackAdmin.class);
                i.putExtra("user",user);
                startActivity(i);
                break;
            case R.id.home_food_fwd:
                Intent j=new Intent(getApplicationContext(),FeedbackFood.class);
                j.putExtra("user", user);
                startActivity(j);
                break;
            case R.id.home_hr_fwd:
                Intent k=new Intent(getApplicationContext(),FeedbackHr.class);
                k.putExtra("user", user);
                startActivity(k);
                break;
            case R.id.home_mgmt_fwd:
                Intent l=new Intent(getApplicationContext(),FeedbackMgmt.class);
                l.putExtra("user", user);
                startActivity(l);
                break;
            case R.id.home_others_fwd:
                Intent m=new Intent(getApplicationContext(),FeedbackOthers.class);
                m.putExtra("user",user);
                startActivity(m);
                break;
            case R.id.home_view_feedback_fwd:
                Intent n=new Intent(getApplicationContext(),ViewAllFeedbacks.class);
                n.putExtra("user",user);
                startActivity(n);
                break;
            case R.id.toolbar_bottom2_my_feedbacks:
                Intent o=new Intent(getApplicationContext(),MyFeedbacks.class);
                o.putExtra("user", user);
                startActivity(o);
                break;
            case R.id.toolbar_bottom2_edit_profile:
                Intent p=new Intent(getApplicationContext(),EditProfile.class);
                p.putExtra("user",user);
                startActivity(p);
                break;
            case R.id.toolbar_bottom2_logout:
                Intent q=new Intent(getApplicationContext(),Welcome.class);
                SharedPreferences share=getSharedPreferences("RateRadius",MODE_PRIVATE);
                SharedPreferences.Editor ed=share.edit();
                ed.remove("user");
                ed.remove("pass");
                ed.commit();
                startActivity(q);
                finish();
                break;

        }
    }


}
