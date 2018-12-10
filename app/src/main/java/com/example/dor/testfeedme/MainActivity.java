package com.example.dor.testfeedme;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    Button signIn, signUp, enterAsGuest;
    TextInputLayout emailMain, passwordMain;
    TextView forgotPasswordMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////////////// Sign Up ////////////////

        signUp = findViewById(R.id.main_signUp);
        signUp.setOnClickListener(this);

        Button signInAsGuestBtn = findViewById(R.id.main_guest);
        signInAsGuestBtn.setOnClickListener(this);

//        signIn = findViewById(R.id.main_signIn);
//        enterAsGuest = findViewById(R.id.main_guest);
//        emailMain = findViewById(R.id.main_email);
//        passwordMain = findViewById(R.id.main_password);
//        forgotPasswordMain = findViewById(R.id.main_ForgotPassword);
    }


    public void onClick(View view){
        switch (view.getId()) {

            case R.id.main_signUp:
                Intent signUpActivity = new Intent(MainActivity.this, signUp.class);
                startActivity(signUpActivity);
                break;
//
//            case R.id.main_signIn:
//                Intent signUp = new Intent(MainActivity.this, SignUp.class);
//                startActivity(signUp);
//                break;
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

}
