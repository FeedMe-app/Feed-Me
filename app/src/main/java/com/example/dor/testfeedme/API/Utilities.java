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
    List<String> ingredients = new ArrayList<>();
    int listSize;
    public Utilities()
    {

    }
    public List<String> getIngredients()
    {
        return this.ingredients;
    }

    public void buildIngredientsList()
    {
        File dir = new File("./ingredients");
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
            this.ingredients.addAll(keys);
        }
        this.listSize = ingredients.size();

    }

    @Override
    public String toString() {
        String ans = "";
        for(String s : this.ingredients) {
            ans += s;

        }
        return ans;
    }
}
