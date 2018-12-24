package com.example.dor.testfeedme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import API.RecipeConfig;
import API.Utilities;
import Database.Client;
import Database.GetDataFromFirebase;
import Database.GetExtraUserData;
import Models.DownloadImageTask;
import Models.Recipe;
import Register.EntrySurveyText;
import Register.OnSwipeTouchListener;
import Users.RegularUser;

public class GenerateSuggestionsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle aToggle;
    String userEmail;
    RegularUser user;
    Client client;
    NavigationView navigationView;
    private Button feedMeBtn;
    private Button passBtn;
    private Button chooseBtn;
    private List<Recipe> recipesToChooseFrom;
    private int currRecipeIndex;
    private ImageView im;
    private DownloadImageTask imageViewHandler;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_suggestions);

        InitializeSideBarMenu();
        GetUserExtraDetails();
        InitializeButtonListener();
    }

    private void InitializeButtonListener() {
        feedMeBtn = findViewById(R.id.feedMeBtn);
        feedMeBtn.setOnClickListener(this);
    }

    private void GetUserExtraDetails() {
        client.getUserExtraDetails(userEmail, HandleExtraDataRecevied());
    }

    private GetExtraUserData HandleExtraDataRecevied(){
        return new GetExtraUserData() {
            @Override
            public void onCallback(List<String> topIngreds, List<String> topMeals, List<String> allergies, List<String> dislikes) {
                user.setTop10FavIngredients(topIngreds);
                user.setTop5FavMeal(topMeals);
                user.setAllergies(allergies);
                user.setDislikes(dislikes);
            }
        };
    }

    private void InitializeSideBarMenu() {
        drawerLayout = findViewById(R.id.drawer);
        aToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(aToggle);
        aToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = new RegularUser();

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

        user = client.getUserFromDatabase(userEmail, new GetDataFromFirebase() {
            NavigationView navigationView = findViewById(R.id.menuLayout);
            View headerView = navigationView.inflateHeaderView(R.layout.header_menu);
            @Override
            public void onCallback(RegularUser user) {
                TextView emailMenu = headerView.findViewById(R.id.email_menu);
                emailMenu.setText(user.getEmail());

                TextView fullnameMenu = headerView.findViewById(R.id.name_menu);
                fullnameMenu.setText(user.getFirstName() + " " + user.getLastName());

                TextView classifictionMenu = headerView.findViewById(R.id.classifiction_menu);
                classifictionMenu.setText(user.getUserClassification());
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.profile:
                Toast.makeText(GenerateSuggestionsActivity.this, "yesss", Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.feedMeBtn:
                HandleUserChooseRecipe();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void HandleUserChooseRecipe() {
        RecipeConfig config = new RecipeConfig(user.getTop10FavIngredients());
        if (user.getAllergies().size() > 0){
            config.setAllergies(user.getAllergies());
        }
        if (user.getDislikes().size() > 0){
            config.setDislikes(user.getDislikes());
        }
        recipesToChooseFrom = Utilities.findRecpiesByUserPreferences(config);
        StartShowingRecipes();
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void StartShowingRecipes() {
        setContentView(R.layout.layout_choose_recipe);
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

    private void HandleChooseBtn() {
        Intent showRecipeIntent = new Intent(GenerateSuggestionsActivity.this,
                                                            ShowRecipeActivity.class);
        showRecipeIntent.putExtra("currRecipe", recipesToChooseFrom.get(currRecipeIndex - 1));
        startActivity(showRecipeIntent);
    }

    private void generateNewRecipe(){
        showNewRecipe();
    }
}
