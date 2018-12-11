package com.example.dor.testfeedme.API;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UtilitiesTest {

    @Test
    public void getIngredietsTest(){
        List<String> ingreds = Utilities.getIngredients();
        assertNotEquals(0, ingreds.size());
    }
}