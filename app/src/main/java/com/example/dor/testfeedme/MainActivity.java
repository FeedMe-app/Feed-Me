package com.example.dor.testfeedme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    Button signIn, signUp, enterAsGuest;
    EditText emailMain, passwordMain;
    //TextView forgotPasswordMain;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        //////////////// Sign Up ////////////////

        signUp = findViewById(R.id.main_signUp);
        signUp.setOnClickListener(this);

        Button signInAsGuestBtn = findViewById(R.id.main_guest);
        signInAsGuestBtn.setOnClickListener(this);

        signIn = findViewById(R.id.main_signIn);
        emailMain = findViewById(R.id.main_email);
        passwordMain = findViewById(R.id.main_password);
        signIn.setOnClickListener(this);
        //forgotPasswordMain = findViewById(R.id.main_ForgotPassword);
        //enterAsGuest = findViewById(R.id.main_guest);

    }


    public void onClick(View view){
        switch (view.getId()) {

            case R.id.main_signUp:
                Intent signUpActivity = new Intent(MainActivity.this, signUp.class);
                startActivity(signUpActivity);
                break;

            case R.id.main_signIn:
                    loginUser();
                break;
//
//            case R.id.main_guest:
//                signInWithFacebook();
//                break;
            case R.id.main_guest:
                startActivity(new Intent(this, EntrySurveyText.class));
                break;

            default:
                break;
        }

    }

    public void loginUser(){

        String email = emailMain.getText().toString();
        final String password = passwordMain.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
            return;
        }


        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                passwordMain.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.login_successfully), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    }


