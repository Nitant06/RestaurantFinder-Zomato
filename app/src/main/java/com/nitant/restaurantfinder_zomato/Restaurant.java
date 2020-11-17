package com.nitant.restaurantfinder_zomato;

public class Restaurant {

    public String mName;
    public String mCuisine;
    public int mCost;
    public String mCity;
    public String mUrl;

    public Restaurant(String Cuisine,String Name,int Cost,String City,String url){

        mCuisine = Cuisine;
        mName = Name;
        mCost = Cost;
        mCity = City;
        mUrl = url;
    }

    public String getCuisine() {
        return mCuisine;
    }

    public String getName(){
        return mName;
    }

    public int getCost() {
        return mCost;
    }

    public String getCity() {
        return mCity;
    }

    public String getUrl(){
        return mUrl;
    }
}
