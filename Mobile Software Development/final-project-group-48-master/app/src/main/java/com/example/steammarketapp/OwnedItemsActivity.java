package com.example.steammarketapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.steammarketapp.data.LoginDatabase;
import com.example.steammarketapp.data.MarketItem;
import com.example.steammarketapp.data.OwnedItem;
import com.example.steammarketapp.utils.SteamMarketUtils;

import java.util.List;

public class OwnedItemsActivity extends AppCompatActivity implements OwnedAdapter.OnOwnedItemClickListener {
    public static final String EXTRA_MARKET_ITEM = "OwnedItemsActivity";

    private RecyclerView mOwnedItemsRV;
    private OwnedAdapter mOwnedAdapter;
    private OwnedViewModel mOwnedViewModel;

    private Toast mToast;

    @Override
    protected void  onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_items);

        //Recycler View for owned items
        mOwnedItemsRV = findViewById(R.id.rv_owned_items);
        mOwnedViewModel = new ViewModelProvider(this).get(OwnedViewModel.class);
        mOwnedAdapter = new OwnedAdapter(this);
        mOwnedItemsRV.setAdapter(mOwnedAdapter);
        mOwnedItemsRV.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String balStr = preferences.getString(getString(R.string.pref_balance_key), "1000"); // get balance from prefrences
        String title = "Current Balance: " + balStr;
        setTitle(title);


        mOwnedViewModel.getAllItems().observe(this, new Observer<List<OwnedItem>>() {
            @Override
            public void onChanged(List<OwnedItem> ownedItems) {
                mOwnedAdapter.updateMarketItems(ownedItems);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.market_item_detail, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.action_buy_market_item:
                //TO DO add buying
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onOwnedItemClick(OwnedItem item){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String balStr = preferences.getString(getString(R.string.pref_balance_key), "1000"); // get balance from prefrences
        Double bal = Double.valueOf(balStr);
        Double cost = Double.valueOf(item.costPurchasedStr.substring(1));
        bal = bal + cost;
        bal = Math.floor(bal*100)/100;
        balStr = bal.toString();
        //Update data base here
        mOwnedViewModel.deleteItem(item);
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this,"Item Sold!",Toast.LENGTH_SHORT);
        mToast.show();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.pref_balance_key),balStr).commit();
        String title = "Current Balance: " + balStr;
        setTitle(title);
    }
}
