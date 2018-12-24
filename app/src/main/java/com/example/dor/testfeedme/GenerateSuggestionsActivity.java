package com.example.dor.testfeedme;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import Database.Client;
import Database.GetDataFromFirebase;
import Database.GetExtraUserData;
import Users.RegularUser;

public class GenerateSuggestionsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle aToggle;
    String userEmail;
    RegularUser userDetails;
    Client client;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_suggestions);

        userDetails = new RegularUser();
        InitializeSideBarMenu();
        GetUserExtraDetails();
    }

    private void GetUserExtraDetails() {
        client.getUserExtraDetails(userEmail, HandleExtraDataRecevied());
    }


    private GetExtraUserData HandleExtraDataRecevied(){
        return new GetExtraUserData() {
            @Override
            public void onCallback(List<String> topIngreds, List<String> topMeals) {
                userDetails.setTop10FavIngredients(topIngreds);
                userDetails.setTop5FavMeal(topMeals);
                StartRecipesSuggestions();
            }
        };
    }

    private void StartRecipesSuggestions(){
        String something;
        System.out.println();
    }

    private void InitializeSideBarMenu() {
        drawerLayout = findViewById(R.id.drawer);
        aToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(aToggle);
        aToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle data = getIntent().getExtras();
        userEmail= data.getString("userEmail");
        client = new Client();
        addFullNameToHeaderMenu();

        // declaring the NavigationView
        navigationView = findViewById(R.id.menuLayout);
        // assigning the listener to the NavigationView
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(aToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void addFullNameToHeaderMenu(){

         client.getUserFromDatabase(userEmail, new GetDataFromFirebase() {
            NavigationView navigationView = findViewById(R.id.menuLayout);
            View headerView = navigationView.inflateHeaderView(R.layout.header_menu);
            @Override
            public void onCallback(RegularUser user) {
                userDetails = user;
                TextView emailMenu = headerView.findViewById(R.id.email_menu);
                emailMenu.setText(userDetails.getEmail());

                TextView fullnameMenu = headerView.findViewById(R.id.name_menu);
                fullnameMenu.setText(userDetails.getFirstName() + " " + userDetails.getLastName());

                TextView classifictionMenu = headerView.findViewById(R.id.classifiction_menu);
                classifictionMenu.setText(userDetails.getUserClassification());
            }
        });

    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.profile:
                profileMenu();
                break;
            case R.id.recipeHistory:
                // do you click actions for the second selection
                break;
            case R.id.share:
                // do you click actions for the third selection
                break;
            case R.id.setting:
                // do you click actions for the third selection
                break;
            case R.id.logOut:
                // do you click actions for the third selection
                break;
        }

        return false;

    }

    private void profileMenu(){
        setContentView(R.layout.profile_menu);

        TextView emailProfile = findViewById(R.id.email_profile);
        emailProfile.setText(userDetails.getEmail());

        TextView fullnameProfile = findViewById(R.id.name_profile);
        fullnameProfile.setText(userDetails.getFirstName() + " " + userDetails.getLastName());

        TextView classifictionProfile = findViewById(R.id.classificrion_profile);
        classifictionProfile.setText(userDetails.getUserClassification());

        TextView yearOfBirthProfile = findViewById(R.id.yearOfBirth_profile);
        yearOfBirthProfile.setText(userDetails.getYearOfBirth());
    }

}
