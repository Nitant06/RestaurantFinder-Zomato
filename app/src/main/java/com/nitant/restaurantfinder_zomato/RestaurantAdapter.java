package com.nitant.restaurantfinder_zomato;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    public RestaurantAdapter(Context context, List<Restaurant> restaurants) {
        super(context, 0, restaurants);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Restaurant currentRestaurant = getItem(position);

        TextView cuisineName = (TextView)listItemView.findViewById(R.id.CuisineLabel);
        cuisineName.setText(currentRestaurant.getCuisine());

        TextView restaurantName = (TextView)listItemView.findViewById(R.id.RestaurantName);
        restaurantName.setText(currentRestaurant.getName());

        TextView restaurantLocation = (TextView)listItemView.findViewById(R.id.location);
        restaurantLocation.setText(currentRestaurant.getCity());

        TextView avgCost = (TextView)listItemView.findViewById(R.id.price);
        avgCost.setText(Integer.toString(currentRestaurant.getCost()));

        return listItemView;

    }

}
