package com.example.journey_sharing_12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClickSignUp(View v){
        int viewId =v.getId();
        if(viewId ==R.id.signup){
            Intent intent = new Intent(this,SignUpActivity.class);
            startActivity(intent);

        }
    }

    public void onClickLogin(View v){
            int viewId = v.getId();
//            if(viewId == R.id.login){
////                EditText editText = findViewById(R.id.)
//            }
        }


}