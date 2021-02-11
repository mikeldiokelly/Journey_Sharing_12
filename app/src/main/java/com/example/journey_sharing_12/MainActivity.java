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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int GOOGLE_SIGN_IN_CODE = 10005;
    private TextView register;
    private Button signIn;
    private EditText email, password;
    private FirebaseAuth mAuth;


    SignInButton googleSignIn;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;


    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        register = (TextView) findViewById(R.id.signup);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.loginBtn);
        signIn.setOnClickListener(this);

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);


        // google login
        googleSignIn = findViewById(R.id.googleSignInBtn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null){
            Toast.makeText(this,"User is logged in already",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MapActivity.class));
        }
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign = signInClient.getSignInIntent();
                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                Toast.makeText(this,"Google Account is connected to our application",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MapActivity.class));

            } catch (ApiException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signup:
                Toast.makeText(this,"Y2",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.loginBtn:
                Toast.makeText(this,"2",Toast.LENGTH_SHORT).show();
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
