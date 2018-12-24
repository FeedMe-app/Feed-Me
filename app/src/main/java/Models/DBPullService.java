package Models;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

import API.Utilities;
import Database.Client;
import Database.GetRecipeFromDatabase;

public class DBPullService extends IntentService {


    public DBPullService() {
        super("DBPullService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        try {
            Client client = new Client();
            client.getAllRecipes(new GetRecipeFromDatabase() {
                @Override
                public void onCallbackRecipe(List<Recipe> recipes) {
                    Utilities.recipes = recipes;
                }
            });
        } catch(Exception e){

        }

    }
}
