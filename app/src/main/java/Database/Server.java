package Database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.dor.testfeedme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Register.OnEmailCheckListener;
import Users.RegularUser;

import static java.lang.String.valueOf;

public class Server{


    private FirebaseAuth mAuth;
    private DatabaseReference db;


    public Server(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void registerNewUser(final RegularUser user, String pass){

        mAuth.createUserWithEmailAndPassword(user.getEmail(), pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.child("Users").child(user.getEmail().replace(".", "|")).child("Details")
                                    .setValue(user);
                        }//End-if isSuccessful

                        else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Log.d(user.getEmail(), valueOf(R.string.email_exist));
                            } catch (Exception e) {
                                Log.d(user.getEmail(), valueOf(R.string.email_exist) + e.getMessage());

                            }
                        } //End else
                    }
                });
    }


    public void checkIfEmailExists(String email,final OnEmailCheckListener listener) {

        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean isExists = task.getResult().getSignInMethods().isEmpty();
                listener.onSucess(isExists);
            }
        });
    }



    public void completeRegister(final RegularUser user){


        db.child("Users").child(user.getEmail().replace(".", "|")).child("Allergies")
                .setValue(user.getAllergies());

        db.child("Users").child(user.getEmail().replace(".", "|")).child("Dislikes")
                .setValue(user.getDislikes());

        db.child("Users").child(user.getEmail().replace(".", "|")).child("Top10Ingredients")
                .setValue(user.getTop10FavIngredients());

        db.child("Users").child(user.getEmail().replace(".", "|")).child("top5Meal")
                .setValue(user.getTop5FavMeal());
    }
}