package com.example.dor.testfeedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dor.testfeedme.Users.RegularUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class signUp extends AppCompatActivity implements View.OnClickListener{

    EditText email_SignUp, password_SignUp, firstName_SignUp, lastName_SignUp, yearOfBirth_SignUp;
    Button signUpButton;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        email_SignUp = findViewById(R.id.signUp_email);
        password_SignUp = findViewById(R.id.signUp_password);
        firstName_SignUp = findViewById(R.id.signUp_firstName);
        lastName_SignUp = findViewById(R.id.signUp_lastName);
        yearOfBirth_SignUp = findViewById(R.id.signUp_yearOfBirth);
        signUpButton = findViewById(R.id.signUp_button);
        //Firebase
        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUp_button:
                registerUser();
                goToEntrySurvey();
                break;
        }

    }

    private void goToEntrySurvey() {
        Intent entrySurveyActivity = new Intent(signUp.this, EntrySurveyText.class);
        startActivity(entrySurveyActivity);
    }


    public void registerUser(){
        final String firstName = firstName_SignUp.getText().toString();
        final String lastName = lastName_SignUp.getText().toString();
        final String email = email_SignUp.getText().toString();
        final String password = password_SignUp.getText().toString();
        final String yearOfBirth = yearOfBirth_SignUp.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_SignUp.setError(getString(R.string.email_required));
            email_SignUp.requestFocus();
            return;
        }

        if(password.length() < 6){
            password_SignUp.setError(getString(R.string.short_password));
            password_SignUp.requestFocus();
            return;
        }

        if(yearOfBirth.length() != 4){
            yearOfBirth_SignUp.setError(getString(R.string.short_tearOfBirth));
            yearOfBirth_SignUp.requestFocus();
            return;
        }

        /////Add user to Database/////
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            RegularUser user = new RegularUser(firstName, lastName, email, password, yearOfBirth);
                            FirebaseDatabase.getInstance().getReference().child("Users").push().setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(signUp.this, getString(R.string.register_success), Toast.LENGTH_LONG).show();

                                            }

                                            else{
                                                //display ant message
                                            }
                                        }
                                    });
                        }//End-if


                        else{
                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthUserCollisionException existEmail){
                                Log.d(email, getString(R.string.email_exist));
                                Toast.makeText(signUp.this, getString(R.string.email_exist), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.d(email, getString(R.string.email_exist) + e.getMessage());

                            }
                        } //End else
                    }
                });
    }

}