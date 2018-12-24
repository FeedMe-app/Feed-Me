
package Database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    private Recipe recipe;
    private String recipeName;
    private List<Recipe> recipes = new ArrayList<>();
    public Client(){
        db = FirebaseDatabase.getInstance().getReference();
        this.user = new RegularUser();
        //this.email = email;
    }

    public Client(String recipeName){
        db = FirebaseDatabase.getInstance().getReference();
        this.recipe = new Recipe();
        this.recipeName = recipeName;
    }

//    public Recipe getRecipeFromDatabase(final GetRecipeFromDatabase myCallback){
//
//        db.child("Recipes").child(this.recipeName)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            recipe = dataSnapshot.getValue(Recipe.class);
//                            myCallback.onCallbackRecipe(recipe);
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//        return recipe;
//    }

    public void getAllRecipes(final GetRecipeFromDatabase callback){
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


    public RegularUser getUserFromDatabase(String email, final GetDataFromFirebase myCallback){

        db.child("Users").child(email.replace(".", "|")).child("Details")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            user = dataSnapshot.getValue(RegularUser.class);
                            myCallback.onCallback(user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return user;
    }

}