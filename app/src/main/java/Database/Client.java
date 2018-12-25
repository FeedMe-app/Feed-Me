
package Database;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import API.Utilities;
import Models.Ingredient;
import Models.IngredientLine;
import Models.Instructions;
import Models.Label;
import Models.Recipe;
import Users.RegularUser;

public class Client {


    private DatabaseReference db;
    private RegularUser user;
    private String email;
    private List<String> topIngreds = new ArrayList<>();
    private List<String> topMeals = new ArrayList<>();
    private List<String> allergies = new ArrayList<>();
    private List<String> dislikes = new ArrayList<>();
    private String classificiation;
    private boolean isKosher;
    private Recipe recipe;
    private String recipeName;
    private List<Recipe> recipes = new ArrayList<>();
    boolean ingreds, meals, alregs, dlikes;

    private static Client _instance;

    private Client(){
        db = FirebaseDatabase.getInstance().getReference();
        this.user = new RegularUser();
    }

    public static Client The(){
        if (_instance == null){
            _instance = new Client();
        }
        return _instance;
    }

    private void getTop5Meals(String email) {
        db.child("Users").child(email.replace(".", "|")).child("top5Meal")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Iterable<DataSnapshot> topMealsIter = dataSnapshot.getChildren();
                            for (DataSnapshot dss : topMealsIter){
                                topMeals.add(dss.getValue().toString());
                            }
                        }
                        meals = true;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getAllergies(String email) {
        db.child("Users").child(email.replace(".", "|")).child("Allergies")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Iterable<DataSnapshot> allergiesIter = dataSnapshot.getChildren();
                            for (DataSnapshot dss : allergiesIter){
                                allergies.add(dss.getValue().toString());
                            }
                        }
                        alregs = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getDislikes(String email) {
        db.child("Users").child(email.replace(".", "|")).child("Dislikes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Iterable<DataSnapshot> dislikesIter = dataSnapshot.getChildren();
                            for (DataSnapshot dss : dislikesIter){
                                dislikes.add(dss.getValue().toString());
                            }
                        }
                        dlikes = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getTop10Ingredients(String email) {
        db.child("Users").child(email.replace(".", "|")).child("Top10Ingredients")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Iterable<DataSnapshot> topIngredsIter = dataSnapshot.getChildren();
                            for (DataSnapshot dss : topIngredsIter){
                                topIngreds.add(dss.getValue().toString());
                            }
                        }
                        ingreds = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



    public void getAllRecipes(final GetRecipeFromDatabase callback){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.child("Recipes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dss : dataSnapshot.getChildren()){
                                String name = String.valueOf(dss.child("name").getValue());
                                String img = String.valueOf(dss.child("imgUrl").getValue());
                                List<Label> labels = new ArrayList<>();
                                List<Ingredient> ingredients = new ArrayList<>();
                                List<IngredientLine> ingredientLines = new ArrayList<>();
                                List<Instructions> instructions = new ArrayList<>();
                                for (DataSnapshot labelDss : dss.child("labels").getChildren()){
                                    labels.add(labelDss.getValue(Label.class));
                                }
                                for (DataSnapshot ingredDss : dss.child("ingredients").getChildren()){
                                    ingredients.add(ingredDss.getValue(Ingredient.class));
                                }
                                for (DataSnapshot ingredLineDss : dss.child("ingredientLine").getChildren()){
                                    ingredientLines.add(ingredLineDss.getValue(IngredientLine.class));
                                }
                                for (DataSnapshot instrucDss : dss.child("instructions").getChildren()){
                                    instructions.add(instrucDss.getValue(Instructions.class));
                                }
                                recipes.add(new Recipe(name, img, ingredients, labels, instructions, ingredientLines));
                            }
                            callback.onCallbackRecipe(recipes);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public void getUserFromDatabase(final String email, final GetDataFromFirebase myCallback){

        db.child("Users").child(email.replace(".", "|")).child("Details")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            user = dataSnapshot.getValue(RegularUser.class);
                            getAllergies(email);
                            getDislikes(email);
                            getTop5Meals(email);
                            getTop10Ingredients(email);
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    while (!ingreds || !meals || !alregs || !dlikes) { }
                                    user.setTop10FavIngredients(topIngreds);
                                    user.setTop5FavMeal(topMeals);
                                    user.setAllergies(allergies);
                                    user.setDislikes(dislikes);

                                    myCallback.onCallback(user);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


//    public void getUserFromDatabase(String email, final GetDataFromFirebase myCallback){
//
//        db.child("Users").child(email.replace(".", "|")).child("Details")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            user = dataSnapshot.getValue(RegularUser.class);
//                            myCallback.onCallback(user);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//        }

}