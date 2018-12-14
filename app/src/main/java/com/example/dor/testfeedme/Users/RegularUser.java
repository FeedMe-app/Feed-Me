package com.example.dor.testfeedme.Users;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RegularUser implements user, Parcelable {

    private String firstName, lastName, email;
    private String yearOfBirth;
    private List<String> allergies;
    private List<String> dislikes;
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
        dislikes = new ArrayList<>();
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

    public List<String> getDislikes() { return dislikes; }

    public void setDislikes(List<String> dislikes){ this.dislikes = dislikes; }

    /////////////END Getters and Setters////////////


    @Override
    public boolean appendAllergies(String allergic) {
        return false;
    }

    @Override
    public void deleteUser() {

    }

    public RegularUser(Parcel in){
        readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RegularUser createFromParcel(Parcel in ) {
            return new RegularUser( in );
        }

        public RegularUser[] newArray(int size) {
            return new RegularUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(yearOfBirth);
    }

    private void readFromParcel(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        yearOfBirth = in.readString();
    }
}
