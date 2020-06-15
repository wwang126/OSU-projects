package com.example.steammarketapp.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "marketItems")
public class MarketItem implements Serializable {
    /**
     * Hash name used for querying item directly
     */
    @PrimaryKey
    @NonNull
    public String hash_name;
    /**
     *  Name of item
     */
    public String name;
    /**
     * icon_url string to fetch image
     */
    public String icon;
    /**
     * AppID of app attached to item
     */
    public int app_ID; // do we still need this since we are just doing CSGO which has same appid  (730)?
    /**
     * Cost of Item on market in string form
     */
    public String costStr;
    /**
     * Cost of Item on market in int form
     */
    public double cost;
    /**
     * Type and Rarity of Item in string form
     */
    public String type;

}
