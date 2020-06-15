package com.example.steammarketapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steammarketapp.data.MarketItem;
import com.example.steammarketapp.data.Status;
import com.example.steammarketapp.utils.NetworkUtils;
import com.example.steammarketapp.utils.SteamMarketUtils;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MarketAdapter.OnMarketItemClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMarketItemsRV;
    private ProgressBar mLoadingIndicationPB;
    private TextView mLoadingErrorMessageTV;
    private DrawerLayout mDrawerLayout;
    private EditText mSearchBoxET;

    private MarketAdapter mMarketAdapter;
    private MarketViewModel mMarketViewModel;
    private MarketSearchViewModel mSearchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_meu);

        //Remove shadow from action bar
        getSupportActionBar().setElevation(0);

        mSearchBoxET = findViewById(R.id.et_search_box);
        mLoadingIndicationPB = findViewById(R.id.pb_loading_indicator_main);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message_main);

        //Recycler view for Market Items
        mMarketItemsRV = findViewById(R.id.rv_market_items);
        mMarketViewModel = new ViewModelProvider(this).get(MarketViewModel.class);
        mMarketAdapter = new MarketAdapter(this);
        mMarketItemsRV.setAdapter(mMarketAdapter);
        mMarketItemsRV.setLayoutManager(new GridLayoutManager(this,2));

        //Commenting out to test search
        //Update Location list as soon as the db changes
//        mMarketViewModel.getAllItems().observe(this, new Observer<List<MarketItem>>() {
//            @Override
//            public void onChanged(List<MarketItem> marketItems) {
//                mMarketAdapter.updateMarketItems(marketItems);
//            }
//        });

        //TESTING ViewModel for Searching items
        mSearchViewModel = new ViewModelProvider(this).get(MarketSearchViewModel.class);
        mSearchViewModel.getSearchResults().observe(this, new Observer<List<MarketItem>>() {
            @Override
            public void onChanged(List<MarketItem> marketItems) {
                mMarketAdapter.updateMarketItems(marketItems);
            }
        });

        //add getLoadingStatus here later
        mSearchViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status == Status.LOADING) {
                    mLoadingIndicationPB.setVisibility(View.VISIBLE);
                } else if (status == Status.SUCCESS) {
                    mLoadingIndicationPB.setVisibility(View.INVISIBLE);
                    mMarketItemsRV.setVisibility(View.VISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                } else {
                    mLoadingIndicationPB.setVisibility(View.INVISIBLE);
                    mMarketItemsRV.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

        //Search Button functionality
        Button searchButton = findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    try {
                        doMarketSearch(searchQuery);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //Nav Bar stuff
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nv_nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        //Commenting out to test search
        //Add in dummy data for testing
//        MarketItem item = new MarketItem();
//        item.hash_name = " Shadow Daggers  Blue Steel (Minimal Wear)";
//        item.name = " Shadow Daggers  Blue Steel (Minimal Wear)";
//        item.icon = "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpovbSsLQJfw-bbeQJD_eO0mJWOk8j4OrzZgiVUuMcjj-rF8In221K2-ENqZTqmd9fDd1Q8NVHT81Psl7vr0cTvuprN1zI97fJ4ylzC";
//        item.costStr = "$87.56";
//        item.cost = 87.56;
//        item.app_ID = 70;
//        mMarketViewModel.insertItem(item);
//
//        item = new MarketItem();
//        item.hash_name = "2";
//        item.name = " Shadow Daggers  Slaughter (Factory New)";
//        item.icon = "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpovbSsLQJfw-bbeQJD4eOskYKZlsj4OrzZgiVQuJxw3OrHptitigXk-RVkYzz7I4SXdFVtZlmE-lK7xeq6gJa-u53K1zI97VbkI_gt";
//        item.costStr = "$137.65";
//        item.cost = 87.56;
//        item.app_ID = 70;
//        mMarketViewModel.insertItem(item);
//
//        item = new MarketItem();
//        item.hash_name = "Shadow Daggers  Forest DDPAT (Battle-Scarred)";
//        item.name = " Shadow Daggers  Forest DDPAT (Battle-Scarred)";
//        item.icon = "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpovbSsLQJfw-bbeQJK9eOykJCKg8j8NrrHjyVXucch2-rFpd3x3VaxqkFtZ2ylI4OWdlRsaFCD-QPrk-jqgcK0upXB1zI97c9XYxtY";
//        item.costStr = "$98.24";
//        item.cost = 87.56;
//        item.app_ID = 70;
//        mMarketViewModel.insertItem(item);
//
//        item = new MarketItem();
//        item.hash_name = " Falchion Knife  Case Hardened (Well-Worn)";
//        item.name = "Falchion Knife  Case Hardened (Well-Worn)";
//        item.icon = "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpovbSsLQJf1fLEcjVL49KJlZG0mP74Nr_um25V4dB8xOzHotqkjQG2_kZpYD_yINPAdgU3aV6F_lPrxu3n0cXuuZ7JyXJguSQ8pSGKDU0ODQw";
//        item.costStr = "$110.74";
//        item.cost = 87.56;
//        item.app_ID = 70;
//        mMarketViewModel.insertItem(item);

    }

    @Override
    public void onMarketItemClick(MarketItem item){
        Intent intent = new Intent(this, MarketItemDetailedActivity.class);
        intent.putExtra(MarketItemDetailedActivity.EXTRA_MARKET_ITEM, item);
        startActivity(intent);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.login:
                Intent LoginIntent = new Intent(this, LoginActivity.class);
                startActivity(LoginIntent);
                return true;
            case R.id.register:
                Intent RegisterIntent = new Intent(this, RegisterActivity.class);
                startActivity(RegisterIntent);
                return true;
            case R.id.camera:
                Intent CameraIntent = new Intent(this, CameraActivity.class);
                startActivity(CameraIntent);
                return true;
            case R.id.profile:
                Intent ProfileIntent = new Intent(this, ProfileActivity.class);
                startActivity(ProfileIntent);
                return true;
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.my_items:
                Intent owned_items = new Intent(this, OwnedItemsActivity.class);
                startActivity(owned_items);
                return true;
            default:
                return false;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doMarketSearch(String searchQuery) throws UnsupportedEncodingException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String type = preferences.getString(
                getString(R.string.pref_type_key),
                getString(R.string.pref_type_default)
        );

        String rarity = preferences.getString(
                getString(R.string.pref_rarity_key),
                getString(R.string.pref_rarity_default)
        );

        String url = SteamMarketUtils.buildSearchURL(searchQuery, type, rarity);
        Log.d(TAG, "query url: " + url);
        mSearchViewModel.loadSearchResults(searchQuery, type, rarity);
    }

}