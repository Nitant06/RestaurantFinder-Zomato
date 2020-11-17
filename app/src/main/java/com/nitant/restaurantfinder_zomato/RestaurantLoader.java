package com.nitant.restaurantfinder_zomato;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

public class RestaurantLoader extends AsyncTaskLoader<List<Restaurant>> {

    private String mUrl;

    public RestaurantLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Restaurant> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Restaurant> restaurants = Utils.fetchRestaurantData(mUrl);
        return restaurants;
    }

}
