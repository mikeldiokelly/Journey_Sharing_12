package com.example.journey_sharing_12;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private Button signIn;
    private EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        register = (TextView) findViewById(R.id.signup);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.button2);
        signIn.setOnClickListener(this);

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signup:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.button2:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String _email = email.getText().toString().trim();
        String _password = password.getText().toString().trim();

        if(_email.isEmpty() || _password.isEmpty()){
            email.setError("fill email and password");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(_email).matches()){
            email.setError("fill valid email");
            email.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, ProfileDashboard.class));
                }
                else{
                    Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
