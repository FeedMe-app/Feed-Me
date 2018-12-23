package Users;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.dor.testfeedme.R;

import Database.Claint;
import Database.GetDataFromFirebase;

public class AfterLogin extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle aToggle;
    String userEmail;
    RegularUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        drawerLayout = findViewById(R.id.drawer);

        aToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(aToggle);
        aToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = new RegularUser();

        Bundle data = getIntent().getExtras();
        userEmail= data.getString("userEmail");
        Claint claint = new Claint(userEmail);

        user = claint.getUserFromDatabase(new GetDataFromFirebase() {
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

}
