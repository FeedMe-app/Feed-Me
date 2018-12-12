package com.example.dor.testfeedme.Models;

import java.util.List;

public class Recipe
{
    String name;
    String imgUrl;
    List<Ingredient> Ingredients;
    List<Label> Labels;
    List<String> instructions;

    public Recipe(String name, String imgUrl, List<Ingredient> Ingredients, List<Label> Labels, List<String> instructions)
    {
        this.name = name;
        this.imgUrl = imgUrl;
        this.Ingredients = Ingredients;
        this.Labels = Labels;
        this.instructions = instructions;
    }

    public Recipe(){}

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Ingredient> getIngredients()
    {
        return Ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients)
    {
        Ingredients = ingredients;
    }

    public List<Label> getLabels()
    {
        return Labels;
    }

    public void setLabels(List<Label> labels)
    {
        Labels = labels;
    }

    public List<String> getInstructions()
    {
        return instructions;
    }

    public void setInstructions(List<String> instructions)
    {
        this.instructions = instructions;
    }

}
