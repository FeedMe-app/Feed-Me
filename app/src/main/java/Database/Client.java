
package Database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import Users.RegularUser;

public class Client {


    private DatabaseReference db;
    private RegularUser user;
    private String email;
    private List<String> topIngreds = new ArrayList<>();
    private List<String> topMeals = new ArrayList<>();

    public Client(String email){
        db = FirebaseDatabase.getInstance().getReference();
        this.user = new RegularUser();
        this.email = email;
    }


    public void getUserExtraDetails(final GetExtraUserData callBack){
        getTop10Ingredients();
        getTop5Meals();
        callBack.onCallback(topIngreds, topMeals);
    }

    private void getTop5Meals() {
        db.child("Users").child(email.replace(".", "|")).child("top5Meal")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Iterable<DataSnapshot> topMealsIter = dataSnapshot.getChildren();
                            for (DataSnapshot dss : topMealsIter){
                                topMeals.add(dss.getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getTop10Ingredients() {
        db.child("Users").child(email.replace(".", "|")).child("Top10Ingredients")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Iterable<DataSnapshot> topIngredsIter = dataSnapshot.getChildren();
                            for (DataSnapshot dss : topIngredsIter){
                                topIngreds.add(dss.getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public RegularUser getUserFromDatabase(final GetDataFromFirebase myCallback){

        db.child("Users").child(email.replace(".", "|")).child("Details")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            user = dataSnapshot.getValue(RegularUser.class);
                            myCallback.onCallback(user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return user;
    }

}