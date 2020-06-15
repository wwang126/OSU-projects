package com.example.steammarketapp.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * This class is used for storing the items owned by the user. Every entry represents a single item
 * own by that user.
 */
@Entity(tableName = "ownedItems")
public class OwnedItem implements Serializable {

    /**
     * Key used to differentiate different owned items
     */
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int key;
    /**
     * The hash name from the steam market
     */
    public String hash_name;
    /**
     * The plain text name of the item
     */
    public String name;
    /**
     * Cost of the item at time of purchase
     */
    public double costPurchased;
    /**
     * Cost of the item at time of purchase as string
     */
    public String costPurchasedStr;
    /**
     * icon_url string to fetch image
     */
    public String icon;
    /**
     * Owner of the item
     */
    public String ownerID;
}
