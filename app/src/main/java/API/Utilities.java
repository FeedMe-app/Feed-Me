package API;

import android.content.Context;
import android.widget.Toast;

import Database.Client;
import Database.GetRecipeFromDatabase;
import Models.Ingredient;
import Models.IngredientLine;
import Models.Instructions;
import Models.Label;
import Models.Recipe;
import Users.AfterLogin;

import com.example.dor.testfeedme.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class Utilities {
    private static Context context;
    private static List<String> ingredients = new ArrayList<>();
    public static boolean ApplicationLoaded = false;
    static Recipe recipeAns;
    public static List<Recipe> recipes;
    public static void setContext(Context _context){
        context = _context;
    }


    public static List<String> getIngredients()
    {
        if (ingredients.size() == 0){
            buildIngredientsList();
        }
        return ingredients;
    }

    private static void buildIngredientsList()
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
//    public static Recipe loadRecipe(String name)
//    {
//        Recipe recipeObject = new Recipe();
//        JSONObject recipe = findRecipe(name);
//        if(recipe != null)
//        {
//            String headline = (String)recipe.get("headline");
//            recipeObject.setName(headline);
//
//            String image = (String)recipe.get("image");
//            recipeObject.setImgUrl(image);
//
//            if(recipe.get("labels") != null)
//            {
//                List<Map.Entry> labels = getJSONArray((JSONArray) recipe.get("labels"));
//                Iterator<Map.Entry> itr = labels.iterator();
//                while(itr.hasNext())
//                {
//                    Map.Entry<String, String> tmp = itr.next();
//                    Label label = new Label(tmp.getKey(),tmp.getValue());
//                    recipeObject.getLabels().add(label);
//                }
//            }
//
//
//            List<Map.Entry> ing = getJSONArray((JSONArray) recipe.get("ingredients"));
//            Iterator <Map.Entry> itrs = ing.iterator();
//            while(itrs.hasNext())
//            {
//                Map.Entry<String, String> tmp = itrs.next();
//                IngredientLine ingredient = new IngredientLine(tmp.getKey(),tmp.getValue());
//                recipeObject.getIngredientLine().add(ingredient);
//            }
//
//            List<Map.Entry> contains = getJSONArray((JSONArray) recipe.get("contains"));
//            Iterator <Map.Entry> itrc = contains.iterator();
//            while(itrc.hasNext())
//            {
//                Map.Entry<String, String> tmp = itrc.next();
//                Ingredient product = new Ingredient(tmp.getKey());
//                recipeObject.getIngredients().add(product);
//            }
//
//            List<Map.Entry> instructions = getJSONArray((JSONArray) recipe.get("instructions"));
//            Iterator <Map.Entry> itri = instructions.iterator();
//            while(itri.hasNext())
//            {
//                Map.Entry<String, String> tmp = itri.next();
//                Instructions instruction = new Instructions(tmp.getKey());
//                recipeObject.getInstructions().add(instruction);
//            }
//
//            return recipeObject;
//        }
//        else
//        {
//            return null;
//        }
//    }
    public static JSONObject findRecipe(String name)
    {
        InputStream in = context.getResources().openRawResource(R.raw.recipe_data);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj;
        try
        {
            jsonObj = (JSONObject)jsonParser.parse(new InputStreamReader(in, "UTF-8"));

            JSONObject recipe = (JSONObject)jsonObj.get(name);
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
        if(itr.hasNext())
        {
            itr.next();
        }

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
    public static List<String> getRecipes()
    {
        List<String> listOfRecipes = new ArrayList<>();
        InputStream in = context.getResources().openRawResource(R.raw.recipe_data);
        JSONObject jsonObj;
        JSONParser jsonParser = new JSONParser();
        try {
            jsonObj = (JSONObject)jsonParser.parse(new InputStreamReader(in, "UTF-8"));
            for(Iterator iterator = jsonObj.keySet().iterator(); iterator.hasNext();)
            {
                String key = (String) iterator.next();
                listOfRecipes.add(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listOfRecipes;
    }

//    public static Recipe loadRecipeByName(Client rec)
//    {
//        recipeAns = rec.getRecipeFromDatabase(new GetRecipeFromDatabase() {
//            @Override
//            public void onCallbackRecipe(Recipe recipe) {
//                recipeAns = recipe;
//            }
//        });
//        return recipeAns;
//    }
}
