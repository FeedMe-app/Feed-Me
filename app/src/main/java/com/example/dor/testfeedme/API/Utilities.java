package com.example.dor.testfeedme.API;

import android.app.AppComponentFactory;
import android.content.Context;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.example.dor.testfeedme.Models.Ingredient;
import com.example.dor.testfeedme.R;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class Utilities {
    private static List<String> ingredients = new ArrayList<>();


    public static List<String> getIngredients(Context context)
    {
        if (ingredients.size() == 0){
            buildIngredientsList(context);
        }
        return ingredients;
    }

    private static void buildIngredientsList(Context context)
    {
        ArrayList<Integer> res = new ArrayList<>();
        for (char ch = 'a'; ch <= 'z'; ch++){
            res.add(context.getResources().getIdentifier(String.valueOf(ch), "raw", context.getPackageName()));
        }

        for (Integer r : res){
            InputStream in = context.getResources().openRawResource(r);
            JSONObject jsonObj = new JSONObject();
            JSONParser jsonParser = new JSONParser();
            try {
                jsonObj = (JSONObject)jsonParser.parse(new InputStreamReader(in, "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Set<String> keys = jsonObj.keySet();
            System.out.println(keys.toString());
            ingredients.addAll(keys);
        }
    }
}
