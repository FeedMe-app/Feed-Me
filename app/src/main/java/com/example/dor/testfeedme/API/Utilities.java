package com.example.dor.testfeedme.API;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Utilities {
    private static List<String> ingredients = new ArrayList<>();

    public static List<String> getIngredients()
    {
        if (ingredients.size() == 0){
            buildIngredientsList();
        }
        return ingredients;
    }

    private static void buildIngredientsList()
    {
        String ingredientsFolderPath = "src\\main\\java\\com\\example\\dor\\testfeedme\\API\\lib\\ingredients";
        File dir = new File(ingredientsFolderPath);
        File[] directoryListing = dir.listFiles();

        for (File child : directoryListing) {
            JSONParser parser = new JSONParser();
            Object obj = null;
            try {
                obj = parser.parse(new FileReader(child.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            JSONObject jsonObject = (JSONObject) obj;
            Set<String> keys = jsonObject.keySet();
            System.out.println(keys.toString());
            ingredients.addAll(keys);
        }
    }
}
