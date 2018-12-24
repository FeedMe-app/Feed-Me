package Database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Users.RegularUser;

public class Client {


    private DatabaseReference db;
    private RegularUser user;
    private String email;

    public Client(String email){
        db = FirebaseDatabase.getInstance().getReference();
        this.user = new RegularUser();
        this.email = email;
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
