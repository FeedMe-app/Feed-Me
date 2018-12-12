package com.example.dor.testfeedme.API;

import android.app.AppComponentFactory;
import android.content.Context;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.example.dor.testfeedme.Models.Ingredient;
import com.example.dor.testfeedme.Models.IngredientLine;
import com.example.dor.testfeedme.Models.Instructions;
import com.example.dor.testfeedme.Models.Label;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    public static Recipe loadRecipe(String name)
    {
        String path = "/home/aviad/IdeaProjects/FeedMeAPI/src/lib/ten_recipes.json";
        Recipe recipeObject = new Recipe();
        JSONObject recipe = findRecipe(name,path);
        if(recipe != null)
        {
            String headline = (String)recipe.get("headline");
            recipeObject.setName(headline);

            String image = (String)recipe.get("image");
            recipeObject.setImgUrl(image);

            List<Map.Entry> labels = getJSONArray((JSONArray) recipe.get("labels"));
            Iterator<Map.Entry> itr = labels.iterator();
            while(itr.hasNext())
            {
                Map.Entry<String, String> tmp = itr.next();
                Label label = new Label(tmp.getKey(),tmp.getValue());
                recipeObject.getLabels().add(label);
            }

            List<Map.Entry> ing = getJSONArray((JSONArray) recipe.get("ingredients"));
            Iterator <Map.Entry> itrs = ing.iterator();
            while(itrs.hasNext())
            {
                Map.Entry<String, String> tmp = itrs.next();
                IngredientLine ingredient = new IngredientLine(tmp.getKey(),tmp.getValue());
                recipeObject.getIngredientLine().add(ingredient);
            }

            List<Map.Entry> contains = getJSONArray((JSONArray) recipe.get("contains"));
            Iterator <Map.Entry> itrc = contains.iterator();
            while(itrc.hasNext())
            {
                Map.Entry<String, String> tmp = itrc.next();
                Ingredient product = new Ingredient(tmp.getKey());
                recipeObject.getIngredients().add(product);
            }

            List<Map.Entry> instructions = getJSONArray((JSONArray) recipe.get("instructions"));
            Iterator <Map.Entry> itri = instructions.iterator();
            while(itri.hasNext())
            {
                Map.Entry<String, String> tmp = itri.next();
                Instructions instruction = new Instructions(tmp.getKey());
                recipeObject.getInstructions().add(instruction);
            }

            return recipeObject;
        }
        else
        {
            return null;
        }
    }
    public static JSONObject findRecipe(String name, String path)
    {
        JSONParser jsonParser = new JSONParser();
        Object object;
        try
        {
            object=jsonParser.parse(new FileReader(path));
            JSONObject jsonObject=(JSONObject)object;

            JSONObject recipe = (JSONObject) jsonObject.get(name);
            return recipe;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private static List<Map.Entry> getJSONArray (JSONArray arr)
    {
        List<Map.Entry> listOfKeyValuePair = new ArrayList<>();
        Iterator itr = arr.iterator();
        itr.next();
        while (itr.hasNext())
        {
            Object object = itr.next();
            JSONObject jsonObject = (JSONObject) object;
            Map.Entry<String, String> keyValue = new AbstractMap.SimpleEntry<String, String>(jsonObject.keySet().iterator().next().toString(),"");
            keyValue.setValue(jsonObject.values().iterator().next().toString());
            listOfKeyValuePair.add(keyValue);
        }
        return listOfKeyValuePair;
    }
    public static List<Recipe> buildTenRecipes(String path)
    {
        List<Recipe> listOfRecipes = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        Object object;
        try
        {
            object = jsonParser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) object;

            for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();)
            {
                String key = (String) iterator.next();
                listOfRecipes.add(loadRecipe(key));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return listOfRecipes;
    }
}
