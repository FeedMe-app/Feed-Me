package Register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dor.testfeedme.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import API.Utilities;
import Database.Server;
import MainActivity.MainActivity;
import Models.DownloadImageTask;
import Models.Ingredient;
import Models.Recipe;
import Users.RegularUser;

public class EntrySurveyText extends AppCompatActivity implements View.OnClickListener {

    private List<String> allergies;
    private List<String> dislikes;
    private List<String> Ingredients;
    private List<String> recipes = new ArrayList<>();
    private List<Recipe> chosenRecipes = new ArrayList<>();
    private List<Recipe> chunckOfTenRecipes = new ArrayList<>();
    private Boolean isKosher;
    private String FoodType;
    private DownloadImageTask imageViewHandler;
    private int currRecipeIndex = 0;
    private final int MAXIMUM_CHOSEN_RECIPES = 10;
    private RegularUser newUser;
    private Server sv = new Server();
    private Button addAllergyBtn;
    private Button addDislikeBtn;
    private Button nextBtn;
    private Button likeBtn;
    private Button passBtn;
    private Button continueBtn;
    private RadioGroup KosherRadioGroup;
    private RadioGroup VeganRadioGroup;
    private ImageView im;
    private TextView tv;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_survey_text);

        findViewById(R.id.AllergiesLinearLayout).setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
        findViewById(R.id.DislikesLinearLayout).setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);

        InitializeListeners();

        allergies = new ArrayList<>();
        dislikes = new ArrayList<>();
        getAllRecipes();
        loadTenRecipes();
        Bundle data = getIntent().getExtras();
        newUser= data.getParcelable("newUser");


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addAllergyBtn:
                AddAllergy();
                ((AutoCompleteTextView)findViewById(R.id.IngredientSearch)).setText("");
                break;

            case R.id.addDisklikeBtn:
                addDislike();
                ((AutoCompleteTextView)findViewById(R.id.DislikesSearch)).setText("");
                break;

            case R.id.nextBtn:
                goToImageSurvey();
                break;

            case R.id.yesBtn:
                HandleLikedRecipe();
                break;

            case R.id.noBtn:
                generateNewRecipe();
                break;

            case R.id.continueBtn:
                CompleteRegistration();
                break;
        }
    }


    private void CompleteRegistration() {
        newUser.setAllergies(allergies);
        newUser.setDislikes(dislikes);
        newUser.setTop5FavMeal(GetTop5Recipes());
        newUser.setTop10FavIngredients(GetTop10Ingreds());
        sv.completeRegister(newUser);
        Toast.makeText(EntrySurveyText.this, getString(R.string.register_success), Toast.LENGTH_LONG).show();
        Intent login = new Intent(EntrySurveyText.this, MainActivity.class);
        startActivity(login);
    }



    private List<String> GetTop10Ingreds() {
        List<String> rc = new ArrayList<>();
        HashMap<Ingredient, Integer> ingredsCount = new HashMap<>();
        List<Ingredient> ingreds = new ArrayList<>();
        for (Recipe rec : chosenRecipes){
            ingreds.addAll(rec.getIngredients());
        }
        for (Ingredient ingred : ingreds){
            if (ingredsCount.containsKey(ingred)){
                ingredsCount.put(ingred, ingredsCount.get(ingred) + 1);
            }
            else {
                ingredsCount.put(ingred, 1);
            }
        }
        ingredsCount = sortMap(ingredsCount);
        int counter = 0;
        for (Ingredient in : ingredsCount.keySet()){
            counter++;
            if (counter < 10){
                rc.add(in.getKey());
            }
        }

        return rc;
    }



    private HashMap<Ingredient, Integer> sortMap(HashMap<Ingredient, Integer> map) {
        List<Map.Entry<Ingredient, Integer>> capitalList = new LinkedList<>(map.entrySet());

        Collections.sort(capitalList, new Comparator<Map.Entry<Ingredient, Integer>>() {
            @Override
            public int compare(Map.Entry<Ingredient, Integer> o1, Map.Entry<Ingredient, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        Collections.reverse(capitalList);

        HashMap<Ingredient, Integer> result = new HashMap<>();
        for (Map.Entry<Ingredient, Integer> entry : capitalList)
        {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }



    private List<String> GetTop5Recipes() {
        List<String> rc = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            rc.add(chosenRecipes.get(i).getName());
        }
        return rc;
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void InitializeListeners(){
        GetAllIngredients();

        InitializeTextViewListeners();

        addAllergyBtn = findViewById(R.id.addAllergyBtn);
        addAllergyBtn.setOnClickListener(this);

        addDislikeBtn = findViewById(R.id.addDisklikeBtn);
        addDislikeBtn.setOnClickListener(this);

        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);

        InitializeRadioGroupsListeners();
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void InitializeRadioGroupsListeners() {
        InitializeKosherRadioGroup();
        InitializeVeganRadioGroup();
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void InitializeVeganRadioGroup() {
        VeganRadioGroup = findViewById(R.id.VeganRadioGroup);
        VeganRadioGroup.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        VeganRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                FoodType = rb.getText().toString();
                if (((RadioButton)findViewById(R.id.radio_regular)).getError() != null){
                    ((RadioButton)findViewById(R.id.radio_regular)).setError(null);
                }
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void InitializeKosherRadioGroup() {
        KosherRadioGroup = findViewById(R.id.KosherRadioGroup);
        KosherRadioGroup.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        KosherRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                isKosher = rb.getText().toString() == "Kosher" ? true : false;
                if (((RadioButton)findViewById(R.id.radio_kosher)).getError() != null){
                    ((RadioButton)findViewById(R.id.radio_kosher)).setError(null);
                }
            }
        });
    }



    private void InitializeTextViewListener(int tvId, final int btnId){

        ((AutoCompleteTextView)findViewById(tvId)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0 ||
                        !Ingredients.contains(s.toString().trim())){
                    findViewById(btnId).setEnabled(false);
                } else {
                    findViewById(btnId).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    private void InitializeTextViewListeners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                this.Ingredients);

        ((AutoCompleteTextView)findViewById(R.id.IngredientSearch)).setAdapter(adapter);
        InitializeTextViewListener(R.id.IngredientSearch, R.id.addAllergyBtn);

        ((AutoCompleteTextView)findViewById(R.id.DislikesSearch)).setAdapter(adapter);
        InitializeTextViewListener(R.id.DislikesSearch, R.id.addDisklikeBtn);

    }



    private void addDislike() {
        dislikes.add(((AutoCompleteTextView)findViewById(R.id.DislikesSearch)).getText().toString());
    }



    private void HandleLikedRecipe() {
        chosenRecipes.add(chunckOfTenRecipes.get(currRecipeIndex));
        generateNewRecipe();
    }



    private void generateNewRecipe() {
        if (chosenRecipes.size() < MAXIMUM_CHOSEN_RECIPES){
            currRecipeIndex++;
            if (currRecipeIndex == chunckOfTenRecipes.size())
            {

                loadTenRecipes();
                currRecipeIndex = 1;

            }
            Recipe currRec = chunckOfTenRecipes.get(currRecipeIndex);
            imageViewHandler = new DownloadImageTask(im);
            try {
                //Bitmap res = imageViewHandler.execute(recipes.get(currRecipeIndex).getImgUrl()).get();
                Bitmap res = imageViewHandler.execute(currRec.getImgUrl()).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tv.setText(currRec.getName());
            return;
        }
        setContentView(R.layout.last_registration_layout);

        continueBtn = findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(this);
    }



    private void AddAllergy() {
        allergies.add(((AutoCompleteTextView)findViewById(R.id.IngredientSearch)).getText().toString());
    }



    private void GetAllIngredients(){
        setUtilitesContext();
        this.Ingredients = Utilities.getIngredients();
    }



    private void setUtilitesContext(){
        Utilities.setContext(this);
    }



    private void getAllRecipes(){
        setUtilitesContext();
        this.recipes = Utilities.getRecipes();
    }

    private void loadTenRecipes() {
        chunckOfTenRecipes = new ArrayList<>();
        Random rand = new Random();
        int randNum = rand.nextInt(recipes.size() - 10);
        for (int i=0; i<10 ; i++)
        {
            chunckOfTenRecipes.add(Utilities.loadRecipe(recipes.get(randNum)));
            randNum += 1;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void goToImageSurvey(){
        currRecipeIndex++;
        setContentView(R.layout.image_survey_layout);
        im = findViewById(R.id.imageView);
        imageViewHandler = new DownloadImageTask(im);
        Recipe currRec = chunckOfTenRecipes.get(currRecipeIndex);
        if (currRecipeIndex == chunckOfTenRecipes.size())
        {
            loadTenRecipes();
            currRecipeIndex = 1;
        }
        try {
            // Bitmap res = imageViewHandler.execute(recipes.get(currRecipeIndex).getImgUrl()).get();
            Bitmap res = imageViewHandler.execute(currRec.getImgUrl()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tv = findViewById(R.id.imageTitle);
        tv.setText(currRec.getName());

        LinearLayout ll = findViewById(R.id.likePassLinearLayout);
        ll.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);

        likeBtn = findViewById(R.id.yesBtn);
        likeBtn.setOnClickListener(this);

        passBtn = findViewById(R.id.noBtn);
        passBtn.setOnClickListener(this);

    }


}