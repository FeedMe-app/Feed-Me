package com.example.dor.testfeedme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import API.RecipeConfig;
import API.Utilities;
import Database.Client;
import Database.GetDataFromFirebase;
import Database.Server;
import Models.DownloadImageTask;
import Models.Recipe;
import Register.OnSwipeTouchListener;
import Users.RegularUser;

public class GenerateSuggestionsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle aToggle;
    String userEmail;
    RegularUser userDetails;
    NavigationView navigationView;
    private Button feedMeBtn;
    private List<Recipe> recipesToChooseFrom;
    private int currRecipeIndex;
    private ImageView im;
    private DownloadImageTask imageViewHandler;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generate_suggestions);

        findViewById(R.id.feedMeBtn).setEnabled(false);

        userDetails = new RegularUser();

        Bundle data = getIntent().getExtras();
        userEmail= data.getString("userEmail");


        InitializeSideBarMenu();
        GetUserDetails();
    }

    private void GetUserDetails() {
        Client.The().getUserFromDatabase(userEmail, new GetDataFromFirebase() {
            @Override
            public void onCallback(RegularUser user) {
                userDetails = user;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InitializeButtonListener();
                        addFullNameToHeaderMenu();
                    }
                });

            }
        });
    }

    private void InitializeButtonListener() {
        feedMeBtn = findViewById(R.id.feedMeBtn);
        feedMeBtn.setEnabled(true);
        feedMeBtn.setOnClickListener(this);
    }


    private void InitializeSideBarMenu() {
        drawerLayout = findViewById(R.id.drawer);
        aToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(aToggle);
        aToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            NavigationView navigationView = findViewById(R.id.menuLayout);
            View headerView = navigationView.inflateHeaderView(R.layout.header_menu);

            TextView emailMenu = headerView.findViewById(R.id.email_menu);
            emailMenu.setText(userDetails.getEmail());

            TextView fullnameMenu = headerView.findViewById(R.id.name_menu);
            fullnameMenu.setText(userDetails.getFirstName() + " " + userDetails.getLastName());

            TextView classifictionMenu = headerView.findViewById(R.id.classifiction_menu);
            classifictionMenu.setText(userDetails.getUserClassification());
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
                logOutMenu();
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

        TextView premium = findViewById(R.id.Premium);
        String text = (userDetails.isPremium()) ? "Premium" : "Premium Not Actived";
        premium.setText(text);

        TextView classifictionProfile = findViewById(R.id.classificrion_profile);
        classifictionProfile.setText(userDetails.getUserClassification());

        TextView yearOfBirthProfile = findViewById(R.id.yearOfBirth_profile);
        yearOfBirthProfile.setText(userDetails.getYearOfBirth());
    }


    private void logOutMenu(){
        Client.The().logOutUser();
        Intent intent = new Intent(GenerateSuggestionsActivity.this, MainActivity.MainActivity.class);
        startActivity(intent);

    }



	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void HandleUserChooseRecipe() {
        if (recipesToChooseFrom == null || recipesToChooseFrom.size() == 0){
            Log.i("TOP INGREDIENTS: ", userDetails.getTop10FavIngredients().toString());
            RecipeConfig config = new RecipeConfig(userDetails.getTop10FavIngredients());
            if (userDetails.getAllergies().size() > 0){
                config.setAllergies(userDetails.getAllergies());
            }
            if (userDetails.getDislikes().size() > 0){
                config.setDislikes(userDetails.getDislikes());
            }
            recipesToChooseFrom = Utilities.findRecpiesByUserPreferences(config);
        }
        StartShowingRecipes();
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void StartShowingRecipes() {
        setContentView(R.layout.layout_choose_recipe);
        InitializeSideBarMenu();
        addFullNameToHeaderMenu();
        im = findViewById(R.id.imageView);
        im.setOnTouchListener(new OnSwipeTouchListener(GenerateSuggestionsActivity.this) {
            public void onSwipeRight() {
                HandleChooseBtn();
            }
            public void onSwipeLeft() {
                generateNewRecipe();
            }
        });
        showNewRecipe();
    }

    private void showNewRecipe() {
        if (currRecipeIndex < recipesToChooseFrom.size() - 1){
            imageViewHandler = new DownloadImageTask(im);
            Recipe currRec = recipesToChooseFrom.get(currRecipeIndex);
            currRecipeIndex++;
            try {
                Bitmap res = imageViewHandler.execute(currRec.getImgUrl()).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tv = findViewById(R.id.chooseRecipeImageTitle);
            tv.setText(currRec.getName());
        }
        else{
            currRecipeIndex = 0;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.feedMeBtn:
                HandleUserChooseRecipe();
                break;
        }
    }

    private void HandleChooseBtn() {
        Intent showRecipeIntent = new Intent(GenerateSuggestionsActivity.this,
                                                            ShowRecipeActivity.class);
        Recipe curr = recipesToChooseFrom.get(currRecipeIndex - 1);
        userDetails.getRecipeHistory().add(curr.getName());
        Server.The().UpdateUserRecipeHistory(userEmail, userDetails.getRecipeHistory());
        showRecipeIntent.putExtra("currRecipe", curr);
        showRecipeIntent.putExtra("currUser", userDetails);
        startActivity(showRecipeIntent);
    }

    private void generateNewRecipe(){
        showNewRecipe();
    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.activity_generate_suggestions);
        InitializeButtonListener();
        InitializeSideBarMenu();
        addFullNameToHeaderMenu();
    }
}
