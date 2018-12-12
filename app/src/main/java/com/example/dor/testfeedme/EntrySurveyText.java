package com.example.dor.testfeedme;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.dor.testfeedme.API.Utilities;
import com.example.dor.testfeedme.Models.DownloadImageTask;
import com.example.dor.testfeedme.Models.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EntrySurveyText extends AppCompatActivity implements View.OnClickListener {

    private List<String> suggestedAllergies;
    private List<String> Ingredients;
    private List<Recipe> recipes = new ArrayList<>();
    private Boolean isKosher;
    private String FoodType;
    private DownloadImageTask imageViewHandler;
    private int currRecipeIndex = 0;

    private Button addAllergyBtn;
    private Button nextBtn;
    private Button likeBtn;
    private Button passBtn;
    private Button continueBtn;
    private AutoCompleteTextView textView;
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

        InitializeListeners();

        suggestedAllergies = new ArrayList<>();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void InitializeListeners(){
        GetAllIngredients();

        InitializeTextViewListener();

        addAllergyBtn = findViewById(R.id.addAllergyBtn);
        addAllergyBtn.setOnClickListener(this);

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

    private void InitializeTextViewListener() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                this.Ingredients);
        textView = findViewById(R.id.IngredientSearch);
        textView.setAdapter(adapter);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0 ||
                        !Ingredients.contains(s.toString().trim())){
                    addAllergyBtn.setEnabled(false);
                } else {
                    addAllergyBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addAllergyBtn:
                AddAllergy();
                textView.setText("");
                break;
            case R.id.nextBtn:
                if (validateEntrySurveyText()){
                    goToImageSurvey();
                }
                break;
            case R.id.yesBtn:

                break;
            case R.id.noBtn:
                generateNewRecipe();
                break;
            case R.id.continueBtn:
                CompleteRegistration();
                break;
        }
    }

    private boolean validateEntrySurveyText() {
        Boolean rc = false;
        if (KosherRadioGroup.getCheckedRadioButtonId() == -1){
            ((RadioButton)findViewById(R.id.radio_kosher)).setError(getString(R.string.chooseLabel));
        }
        else if (VeganRadioGroup.getCheckedRadioButtonId() == -1){
            ((RadioButton)findViewById(R.id.radio_regular)).setError(getString(R.string.chooseLabel));
        }
        else{
            rc = true;
        }
        return rc;
    }

    private void CompleteRegistration() {
    }

    private void generateNewRecipe() {
        currRecipeIndex++;
        if (currRecipeIndex < recipes.size()){
            imageViewHandler = new DownloadImageTask(im);
            try {
                Bitmap res = imageViewHandler.execute(recipes.get(currRecipeIndex).getImgUrl()).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tv.setText(recipes.get(currRecipeIndex).getName());
            return;
        }
        setContentView(R.layout.last_registration_layout);

        continueBtn = findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(this);
    }

    private void AddAllergy() {
        suggestedAllergies.add(((AutoCompleteTextView)findViewById(R.id.IngredientSearch)).getText().toString());
    }

    private void GetAllIngredients(){
        Utilities.setContext(this);
        this.Ingredients = Utilities.getIngredients();
    }

    private void getAllRecipes(){
        Utilities.setContext(this);
        this.recipes = Utilities.buildTenRecipes();
    }

    public void goToImageSurvey(){
         getAllRecipes();

        setContentView(R.layout.image_survey_layout);

        im = findViewById(R.id.imageView);
        imageViewHandler = new DownloadImageTask(im);
        try {
            Bitmap res = imageViewHandler.execute(recipes.get(currRecipeIndex).getImgUrl()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tv = findViewById(R.id.imageTitle);
        tv.setText(recipes.get(currRecipeIndex).getName());

        likeBtn = findViewById(R.id.yesBtn);
        likeBtn.setOnClickListener(this);

        passBtn = findViewById(R.id.noBtn);
        passBtn.setOnClickListener(this);
    }


}
