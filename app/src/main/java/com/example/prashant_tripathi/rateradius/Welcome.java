package com.example.prashant_tripathi.rateradius;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Prashant_Tripathi on 08-07-2017.
 */
public class Welcome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }

    public void OptionSelected(View v){
        switch(v.getId()){
            case R.id.welcome_join_now:
                Intent i=new Intent(getApplicationContext(),JoinNow.class);
                startActivity(i);
                break;
            case R.id.welcome_sign_in:
                Intent j=new Intent(getApplicationContext(),SignIn.class);
                startActivity(j);
                break;
        }
    }
}
