package Database;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.dor.testfeedme.R;
import com.example.dor.testfeedme.Users.RegularUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Register.OnEmailCheckListener;

import static java.lang.String.valueOf;

public class Server {

    private RegularUser user;
    private String pass;
    private FirebaseAuth mAuth;
    private DatabaseReference db;

    public Server(RegularUser user, String pass) {
        this.user = user;
        this.pass = pass;
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
    }

    public Server(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void registerNewUser(){

        mAuth.createUserWithEmailAndPassword(user.getEmail(), this.pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.child("Users").child(user.getEmail().replace(".", "|")).setValue(user);
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



    public void completeRegister(){
        user.setAllergies(user.getAllergies());
        user.setDislikes(user.getDislikes());
        user.setTop5FavMeal(user.getTop5FavMeal());
        user.setTop10FavIngredients(user.getTop10FavIngredients());

        db.child("Allergies").child(user.getEmail().replace(".", "|"))
                .setValue(user.getAllergies());

        db.child("Dislikes").child(user.getEmail().replace(".", "|"))
                .setValue(user.getDislikes());

        db.child("Top10Ingredients").child(user.getEmail().replace(".", "|"))
                .setValue(user.getTop10FavIngredients());

        db.child("top5Meal").child(user.getEmail().replace(".", "|"))
                .setValue(user.getTop5FavMeal());
    }


    public Server(Parcel in){
        readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Server createFromParcel(Parcel in) {
            return new Server(in);
        }

        @Override
        public RegularUser[] newArray(int size) {
            return new RegularUser[size];

        }
    };


    private void readFromParcel(Parcel in) {
        this.user = user;
    }

    public RegularUser getUser() {
        return user;
    }
}
