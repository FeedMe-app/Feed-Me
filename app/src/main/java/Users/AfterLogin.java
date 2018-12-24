package Users;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dor.testfeedme.R;

import java.util.List;

import Database.Client;
import Database.GetDataFromFirebase;
import Database.GetExtraUserData;

public class AfterLogin extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle aToggle;
    String userEmail;
    RegularUser user;
    Client client;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);


        InitializeSideBarMenu();
        GetUserExtraDetails();


    }

    private void GetUserExtraDetails() {
        client.getUserExtraDetails(HandleExtraDataRecevied());
    }

    private GetExtraUserData HandleExtraDataRecevied(){
        return new GetExtraUserData() {
            @Override
            public void onCallback(List<String> topIngreds, List<String> topMeals) {
                user.setTop10FavIngredients(topIngreds);
                user.setTop5FavMeal(topMeals);
                StartRecipesSuggestions();
            }
        };
    }

    private void StartRecipesSuggestions(){

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
        client = new Client(userEmail);
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

        user = client.getUserFromDatabase(new GetDataFromFirebase() {
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
                Toast.makeText(AfterLogin.this, "yesss", Toast.LENGTH_SHORT).show();
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
}
