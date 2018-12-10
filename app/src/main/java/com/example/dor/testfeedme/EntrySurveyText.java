package com.example.dor.testfeedme;

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
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntrySurveyText extends AppCompatActivity implements View.OnClickListener {

    private List<String> Allergies;
    private List<String> Ingredients;

    private Button addAllergyBtn;
    private AutoCompleteTextView textView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_survey_text);

        findViewById(R.id.AllergiesLinearLayout).setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);

        InitializeListeners();

        Allergies = new ArrayList<>();

    }

    private void InitializeListeners(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                GetAllIngrediets());
        textView = findViewById(R.id.IngredientSearch);
        textView.setAdapter(adapter);
        InitializeTextViewListener();

        addAllergyBtn = findViewById(R.id.addAllergyBtn);
        addAllergyBtn.setOnClickListener(this);
    }

    private void InitializeTextViewListener() {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0 ||
                        !countries.contains(s.toString().trim())){
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

    private static final List<String> countries = Arrays.asList("Belgium", "France", "Italy", "Germany", "Spain");

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addAllergyBtn:
                AddAllergy();
                textView.setText("");
                break;


        }
    }

    private void AddAllergy() {
        Allergies.add(((AutoCompleteTextView)findViewById(R.id.IngredientSearch)).getText().toString());
    }

    private List<String> GetAllIngrediets(){
        return countries;
    }
}
