package com.example.dor.testfeedme;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Users.RegularUser;

public class GenerateSuggestionsActivity extends AppCompatActivity {

    private DatabaseReference mCurrentUser;
    private FirebaseAuth auth;

    private FirebaseUser currentUser;
    private RegularUser currRegularUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_suggestions);

        auth = FirebaseAuth.getInstance();

        currentUser = auth.getCurrentUser();

        mCurrentUser = FirebaseDatabase.getInstance().getReference("Users");
//
//        GetCurrentRegularUser();

    }

    private void GetCurrentRegularUser() {
        mCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currRegularUser = dataSnapshot.getValue(RegularUser.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
