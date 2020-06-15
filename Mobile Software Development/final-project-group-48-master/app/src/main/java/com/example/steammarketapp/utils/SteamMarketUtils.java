package com.example.steammarketapp.utils;


import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import com.example.steammarketapp.data.MarketItem;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class SteamMarketUtils {
    private static final String TAG = SteamMarketUtils.class.getSimpleName();


    /* URL EXAMPLE for CSGO community marketplace front page:
        https://steamcommunity.com/market/search/?appid=730

      URL EXAMPLE for specific weapon (ak-47):
        https://steamcommunity.com/market/search?q=&category_730_Weapon%5B%5D=tag_weapon_ak47&appid=730

      URL EXAMPLE for search "ak-47" and type Collectible:
      https://steamcommunity.com/market/search?q=ak-47&category_730_Type%5B0%5D=tag_CSGO_Type_Collectible&appid=730

    FULL URL EXAMPLE with all queries (knife type selected):
        https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B0%5D=any&category_730_ProPlayer%5B0%5D=any
        &category_730_StickerCapsule%5B0%5D=any&category_730_TournamentTeam%5B0%5D=any&category_730_Weapon%5B0%5D=any
        &category_730_Type%5B0%5D=tag_CSGO_Type_Knife&appid=730

        https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any
        &category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=any
        &category_730_Rarity%5B%5D=tag_Rarity_Legendary&category_730_Type%5B%5D=tag_CSGO_Type_WeaponCase&appid=730
     */


    private final static String STEAM_MARKET_BASE_URL = "https://steamcommunity.com/market/search/render";
    private final static String STEAM_MARKET_QUERY_PARAM = "query";
    private final static String STEAM_MARKET_APPID_PARAM = "appid";
    private final static String STEAM_MARKET_APPID = "730"; // Use CSGO's appid
    private final static String STEAM_MARKET_NORENDER_PARAM = "norender";
    private final static String STEAM_MARKET_TYPE_PARAM = "category_730_Type%5B0%5D";
    private final static String STEAM_MARKET_RARITY_PARAM = "category_730_Rarity%5B%5D";

    //Format string from generation application icons
    private final static String MARKET_ICON_URL_FORMAT_STR = "https://steamcommunity-a.akamaihd.net/economy/image/%s";
    public static String buildIconURL(String icon){
        return String.format(MARKET_ICON_URL_FORMAT_STR, icon);
    }


    /*
        static classes for use with JSON parsing
     */

    static class SteamMarketResults {
        public SteamMarketResultsItem[] results;
        public int total_count;
    }

    static class SteamMarketResultsItem {
        public String name;
        public String hash_name;
        public String sell_price_text;
        public String app_icon;
        SteamMarketResultsAssetDescription asset_description;
    }
    static class SteamMarketResultsAssetDescription{
        public String icon_url;
        public String type;

    }

    public static String buildSearchURL(String search, String type, String rarity) throws UnsupportedEncodingException {
        Uri.Builder builder = Uri.parse(STEAM_MARKET_BASE_URL).buildUpon();

        builder.appendQueryParameter(STEAM_MARKET_QUERY_PARAM, search);

        //Must use URLDecoder because builder changes "%" characters in url to "%25"
        if (!type.equals("")) {
            String decodedType = java.net.URLDecoder.decode(STEAM_MARKET_TYPE_PARAM, StandardCharsets.UTF_8.name());
            Log.d(TAG, "decoded type param: " + decodedType);
            builder.appendQueryParameter(decodedType, type);
        }

        if (!rarity.equals("")) {
            String decodedRarity = java.net.URLDecoder.decode(STEAM_MARKET_RARITY_PARAM, StandardCharsets.UTF_8.name());
            Log.d(TAG, "decoded rarity param: " + decodedRarity);
            builder.appendQueryParameter(decodedRarity, rarity);
        }

        builder.appendQueryParameter(STEAM_MARKET_APPID_PARAM, STEAM_MARKET_APPID);
        builder.appendQueryParameter(STEAM_MARKET_NORENDER_PARAM, "1");
        return builder.build().toString();
    }

    /*
        Use this page to help with parsing:
            https://steamcommunity.com/market/search/render/?appid=730&norender=1
     */

    public static ArrayList<MarketItem> parseItemJSON(String itemJSON) {
        Gson gson = new Gson();
        SteamMarketResults marketResults = gson.fromJson(itemJSON, SteamMarketResults.class);
        if (marketResults != null && marketResults.results != null) {
            ArrayList<MarketItem> marketItems = new ArrayList<>();

            // loop through all parsed items and condense each one by one into single object
            for (SteamMarketResultsItem resultsItem : marketResults.results) {
                MarketItem marketItem = new MarketItem();

                marketItem.hash_name = resultsItem.hash_name;
                marketItem.name = resultsItem.name;
                marketItem.costStr = resultsItem.sell_price_text;
                marketItem.icon = resultsItem.asset_description.icon_url;
                marketItem.type = resultsItem.asset_description.type;

                marketItems.add(marketItem);
            }
            return marketItems;
        } else
            return null;
    }
}
