package com.example.dor.testfeedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dor.testfeedme.Users.RegularUser;

import Database.Server;

public class signUp extends AppCompatActivity implements View.OnClickListener {

    EditText email_SignUp, password_SignUp, firstName_SignUp, lastName_SignUp, yearOfBirth_SignUp;
    Button signUpButton;


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
        signUpButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.signUp_button:

                //Check if email exists
                Server sv = new Server();
                sv.checkIfEmailExists(email_SignUp.getText().toString(), new OnEmailCheckListener() {
                    @Override
                    public void onSucess(Boolean emailExist) {
                        if(!emailExist){
                            email_SignUp.setError(getString(R.string.email_exist));
                            email_SignUp.requestFocus();
                        }
                    }

                });
                //End function

                //Check details user
                if (ValidateFields()){
                    goToEntrySurvey();
                }
                break;
        }
    }

    private void goToEntrySurvey() {
        Intent entrySurveyActivity = new Intent(signUp.this, EntrySurveyText.class);
        RegularUser user = new RegularUser(firstName_SignUp.getText().toString(),
                lastName_SignUp.getText().toString(),
                email_SignUp.getText().toString(),
                yearOfBirth_SignUp.getText().toString());
        //Add new user to firebase
        Server server = new Server(user, password_SignUp.getText().toString());
        server.registerNewUser();
        //Full register
        entrySurveyActivity.putExtra("newUser", user);
        //entrySurveyActivity.putExtra("userPassword", password_SignUp.getText().toString());
        startActivity(entrySurveyActivity);
    }


    private Boolean ValidateFields(){
        Boolean rc = true;
        final String firstName = firstName_SignUp.getText().toString();
        final String lastName = lastName_SignUp.getText().toString();
        final String email = email_SignUp.getText().toString();
        final String password = password_SignUp.getText().toString();
        final String yearOfBirth = yearOfBirth_SignUp.getText().toString();

        if (!firstName.matches("[a-zA-Z]+")){
            String msg;
            if (firstName.isEmpty()){
                msg = getString(R.string.requiredError);
            }
            else {
                msg = getString(R.string.onlyLettersError);
            }
            firstName_SignUp.setError(msg);
            firstName_SignUp.requestFocus();
            rc = false;
        }


        else if (!lastName.matches("[a-zA-Z]+")){
            String msg;
            if (lastName.isEmpty()){
                msg = getString(R.string.requiredError);
            }
            else {
                msg = getString(R.string.onlyLettersError);
            }
            lastName_SignUp.setError(msg);
            lastName_SignUp.requestFocus();
            rc = false;
        }


        else if (yearOfBirth.length() != 4) {
            yearOfBirth_SignUp.setError(getString(R.string.short_tearOfBirth));
            yearOfBirth_SignUp.requestFocus();
            rc = false;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_SignUp.setError(getString(R.string.email_required));
            email_SignUp.requestFocus();
            rc = false;
        }



        else if (password.length() < 6) {
            password_SignUp.setError(getString(R.string.short_password));
            password_SignUp.requestFocus();
            rc = false;
        }

        return rc;
    }


//    public void registerUser() {
//        /////Add user to Database/////
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            final RegularUser user = new RegularUser(firstName, lastName, email, yearOfBirth);
//                            db.child("Users").child(user.getEmail().replace(".", "|")).setValue(user);
//
////                            Ingredient in = new Ingredient("Salt");
////                            Ingredient two = new Ingredient("pepper");
////                            user.setTop10FavIngredients(in);
////                            user.setTop10FavIngredients(two);
//
//                            db.child("Top10Ingredients").child(user.getEmail().replace(".", "|"))
//                                    .setValue(user.getTop10FavIngredients());
//
//                            db.child("top5Meal").child(user.getEmail().replace(".", "|"))
//                                    .setValue(user.getTop5FavMeal());
//
//
//                            Toast.makeText(signUp.this, getString(R.string.register_success), Toast.LENGTH_LONG).show();
//                            Intent login = new Intent(signUp.this, MainActivity.class);
//                            startActivity(login);
//                        }//End-if isSuccessful
//
//                        else {
//                            try {
//                                throw task.getException();
//                            } catch (FirebaseAuthUserCollisionException existEmail) {
//                                Log.d(email, getString(R.string.email_exist));
//                                Toast.makeText(signUp.this, getString(R.string.email_exist), Toast.LENGTH_LONG).show();
//                            } catch (Exception e) {
//                                Log.d(email, getString(R.string.email_exist) + e.getMessage());
//
//                            }
//                        } //End else
//                    }
//
//                });
//    }

}