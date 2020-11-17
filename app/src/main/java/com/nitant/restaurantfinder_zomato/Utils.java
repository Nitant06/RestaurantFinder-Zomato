package com.nitant.restaurantfinder_zomato;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();

    public static List<Restaurant> fetchRestaurantData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Restaurant> restaurants = extractFeatureFromJson(jsonResponse);

        return restaurants;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", " application/json");
            urlConnection.setRequestProperty("user-key", "d1a2af2c8d1dff6f1581fe84aee27f30");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Restaurant> extractFeatureFromJson(String restaurantJSON) {

        if (TextUtils.isEmpty(restaurantJSON)) {
            return null;
        }

        List<Restaurant> restaurants = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(restaurantJSON);

            JSONArray restaurantArray = baseJsonResponse.getJSONArray("restaurants");

            for (int i = 0; i < restaurantArray.length(); i++) {

                JSONObject currentRestaurant = restaurantArray.getJSONObject(i);

                JSONObject restaurantProperties = currentRestaurant.getJSONObject("restaurant");

                String cuisineName = restaurantProperties.getString("cuisines");

                String name = restaurantProperties.getString("name");

                int avgCost = restaurantProperties.getInt("average_cost_for_two");

                String location = restaurantProperties.getJSONObject("location").getString("city");

                String url = restaurantProperties.getString("url");

                Restaurant restaurant = new Restaurant(cuisineName,name,avgCost,location,url);

                restaurants.add(restaurant);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return restaurants;
    }

}
