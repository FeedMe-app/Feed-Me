package com.example.dor.testfeedme;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.*;

public class ReadFromDatabase {


    public static void main(String args[]) {

        DatabaseReference  dbRef = database.getReference("Users");
        dbRef.addValueEventListener(changeListener);

        DatabaseReference rootRef = getInstance().getReference();
        DatabaseReference tripsRef = rootRef.child("Users");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String email = ds.child("email").getValue(String.class);
                    String name = ds.child("firstName").getValue(String.class);
                    String lastName = ds.child("lastName").getValue(String.class);
                    list.add(name);
                }
                System.out.println(list.get(0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        tripsRef.addListenerForSingleValueEvent(valueEventListener);

    }
}
