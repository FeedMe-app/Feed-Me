package Database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Users.RegularUser;

public class Claint {


    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private RegularUser user;

    public Claint(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
    }



    public RegularUser getUserFromDatabase(String email){
        user = new RegularUser();

        db.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = (RegularUser) snapshot.getValue();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return user;
    }

}
