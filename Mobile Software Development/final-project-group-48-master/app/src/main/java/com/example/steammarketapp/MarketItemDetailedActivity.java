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

public class MarketItemDetailedActivity extends AppCompatActivity implements OwnedAdapter.OnOwnedItemClickListener {
    public static final String EXTRA_MARKET_ITEM = "MarketItemDetailedActivity";

    private MarketItem mItem;
    private RecyclerView mOwnedItemsRV;
    private OwnedAdapter mOwnedAdapter;
    private OwnedViewModel mOwnedViewModel;
    Button mButtonBuy;
    Button mButtonSell;
    LoginDatabase db;

    private ImageView mMarketItemIconIV;
    private TextView mMarketItemNameTV;
    private TextView mMarketItemCostTV;
    private TextView mMarketItemTypeTV;
    private Toast mToast;

    @Override
    protected void  onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_item_detailed);

        //Button for buy and sell
        mButtonBuy = (Button) findViewById(R.id.btn_buy);



        //Set Market Item at top
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_MARKET_ITEM)){
            mItem = (MarketItem)intent.getSerializableExtra(EXTRA_MARKET_ITEM);
            //set cost
            mMarketItemCostTV = findViewById(R.id.tv_market_item_cost_detailed);
            mMarketItemCostTV.setText(mItem.costStr);
            //set name
            mMarketItemNameTV = findViewById(R.id.tv_market_item_name_detailed);
            mMarketItemNameTV.setText(mItem.name);
            //set type
            mMarketItemTypeTV = findViewById(R.id.tv_market_item_type);
            mMarketItemTypeTV.setText(mItem.type);
            //set image
            mMarketItemIconIV = findViewById(R.id.iv_market_item_icon_detailed);
            String iconURL = SteamMarketUtils.buildIconURL(mItem.icon);
            Glide.with(mMarketItemIconIV.getContext()).load(iconURL).into(mMarketItemIconIV);
        }
        //Recycler View for owned items
        mOwnedItemsRV = findViewById(R.id.rv_owned_items_market_det);
        mOwnedViewModel = new ViewModelProvider(this).get(OwnedViewModel.class);
        mOwnedAdapter = new OwnedAdapter(this);
        mOwnedItemsRV.setAdapter(mOwnedAdapter);
        mOwnedItemsRV.setLayoutManager(new LinearLayoutManager(this));

        //String ownerID = "1";//Get owner id here
        String hashName = mItem.hash_name;

        mOwnedViewModel.getItemsByHashName(hashName).observe(this, new Observer<List<OwnedItem>>() {
            @Override
            public void onChanged(List<OwnedItem> ownedItems) {
                mOwnedAdapter.updateMarketItems(ownedItems);
            }
        });

        //Set onclick listeners for button
        mButtonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyItem();
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String balStr = preferences.getString(getString(R.string.pref_balance_key), "1000"); // get balance from prefrences
        String title = "Current Balance:" + balStr;
        setTitle(title);
        /*
        OwnedItem item = new OwnedItem();
        item.hash_name = "2";
        item.name = " Shadow Daggers  Slaughter (Factory New)";
        item.icon = "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpovbSsLQJfw-bbeQJD4eOskYKZlsj4OrzZgiVQuJxw3OrHptitigXk-RVkYzz7I4SXdFVtZlmE-lK7xeq6gJa-u53K1zI97VbkI_gt";
        item.costPurchased = 137.65;
        item.costPurchasedStr = "$138.65";
        mOwnedViewModel.insertItem(item);*/

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
        Double cost = Double.valueOf(mItem.costStr.substring(1));
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
    public void buyItem(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String balStr = preferences.getString(getString(R.string.pref_balance_key), "1000"); // get balance from prefrences
        Double bal = Double.valueOf(balStr);
        Double cost = Double.valueOf(mItem.costStr.substring(1));
        bal = bal - cost;
        bal = Math.floor(bal*100)/100;
        balStr = bal.toString();
        if(cost < bal){
            if(mToast != null){
                mToast.cancel();
            }
            mToast = Toast.makeText(this,"Item Purchased",Toast.LENGTH_SHORT);
            mToast.show();
            OwnedItem item = new OwnedItem();
            item.costPurchased = mItem.cost;
            item.costPurchasedStr = mItem.costStr;
            item.hash_name = mItem.hash_name;
            item.name = mItem.name;
            item.icon = mItem.icon;
            mOwnedViewModel.insertItem(item);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(getString(R.string.pref_balance_key),balStr).commit();
            String title = "Current Balance: " + balStr;
            setTitle(title);

        }
        else{
            if(mToast != null){
                mToast.cancel();
            }
            mToast =  Toast.makeText(this,"Insufficient Balance to buy item",Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}
