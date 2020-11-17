package com.nitant.restaurantfinder_zomato;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Restaurant>> {

    private String Restaurants_REQUEST_URL = "";

    private static final int Restaurant_Loader_ID = 1;

    private RestaurantAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private SearchView mSearchViewField;

    private View circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView restaurantListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        restaurantListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new RestaurantAdapter(this, new ArrayList<Restaurant>());

        restaurantListView.setAdapter(mAdapter);

        final Button mSearchButton =(Button)findViewById(R.id.SearchButton);

        circleProgressBar = findViewById(R.id.loading_indicator);

        mSearchViewField = (SearchView) findViewById(R.id.searchTextView);
        mSearchViewField.onActionViewExpanded();
        mSearchViewField.setIconified(true);
        mSearchViewField.setQueryHint("Search Cuisines Nearby Eg.Italian,Chinese etc");

        mSearchButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                String str = mSearchViewField.getQuery().toString();

                if(str.isEmpty()){
                    Toast.makeText(MainActivity.this,"Search Cannot be Empty",Toast.LENGTH_LONG).show();
                }
                else {
                    updateQueryUrl(str);
                    restartLoader();
                }

                }

        });

        restaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Restaurant currentRestaurant = mAdapter.getItem(position);

                Uri restaurantUri = Uri.parse(currentRestaurant.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,restaurantUri);

                startActivity(websiteIntent);
            }
        });


        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(Restaurant_Loader_ID, null, this);
        }
        else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    private String updateQueryUrl(String searchValue) {

        if (searchValue.contains(" ")) {
            searchValue = searchValue.replace(" ", "+");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("https://developers.zomato.com/api/v2.1/search?q=").append(searchValue);//.append("&entity_id=94741&entity_type=zone&cuisines=55");
        Restaurants_REQUEST_URL = sb.toString();
        return Restaurants_REQUEST_URL;
    }

    @Override
    public Loader<List<Restaurant>> onCreateLoader(int i, Bundle bundle) {

        return new RestaurantLoader(this, Restaurants_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Restaurant>> loader, List<Restaurant> restaurants) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_restaurants);

        mAdapter.clear();

        if (restaurants != null && !restaurants.isEmpty()) {
            mAdapter.addAll(restaurants);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Restaurant>> loader) {
        mAdapter.clear();
    }

    public void restartLoader() {
        mEmptyStateTextView.setVisibility(GONE);
        circleProgressBar.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(Restaurant_Loader_ID, null, MainActivity.this);
    }


}