package com.example.dor.testfeedme.Users;

import java.util.ArrayList;
import java.util.List;

public class RegularUser implements user{

    private String firstName, lastName, email;
    private String yearOfBirth;
    private List<String> allergies;
    private List<String> top5FavMeal;
    private List<String> top10FavIngredients;
    private List<String> userClassification;
    private static int userID=0;


    ////////////////Constructor////////////

    public RegularUser(String firstName, String lastName, String email, String yearOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.yearOfBirth = yearOfBirth;
        allergies = new ArrayList<>();
        top5FavMeal = new ArrayList<>();
        top10FavIngredients = new ArrayList<>();
        userClassification = new ArrayList<>();
        userID++;
    }

    public RegularUser() {

    }

    public int getUserID() {
        return userID;
    }
/////////////Getters and Setters////////////

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public List getAllergies() {
        return allergies;
    }

    public void setAllergies(List allergies) {
        this.allergies = allergies;
    }

    public List<String> getTop5FavMeal() {
        return top5FavMeal;
    }

    public void setTop5FavMeal(List<String> top5FavMeal) {
        this.top5FavMeal = top5FavMeal;
    }

    public List<String> getTop10FavIngredients() {
        return top10FavIngredients;
    }

    public void setTop10FavIngredients(List<String> top10FavIngredients) {
        this.top10FavIngredients = top10FavIngredients;
    }

    public List<String> getUserClassification() {
        return userClassification;
    }

    public void setUserClassification(List<String> userClassification) {
        this.userClassification = userClassification;
    }

    /////////////END Getters and Setters////////////


    @Override
    public boolean appendAllergies(String allergic) {
        return false;
    }

    @Override
    public void deleteUser() {

    }
}
