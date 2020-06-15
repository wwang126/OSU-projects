package com.example.steammarketapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.steammarketapp.data.MarketItem;
import com.example.steammarketapp.utils.SteamMarketUtils;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketItemViewHolder> {

    private List<MarketItem> mMarketItems;
    private OnMarketItemClickListener mMarketItemClickListener;

    public interface OnMarketItemClickListener{
        void onMarketItemClick(MarketItem marketItem);
    }

    public MarketAdapter(OnMarketItemClickListener clickListener){
        mMarketItemClickListener = clickListener;
    }

    public void updateMarketItems(List<MarketItem> marketItems){
        mMarketItems = marketItems;
        notifyDataSetChanged();
    }

    @Override public int getItemCount() {
        if (mMarketItems != null){
            return mMarketItems.size();
        } else{
            return 0;
        }
    }

    @Override
    public MarketItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.market_list_item, parent, false);
        return new MarketItemViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MarketItemViewHolder holder, int position) {
        holder.bind(mMarketItems.get(position));
    }

    class MarketItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mMarketItemNameTV;
        private TextView mMarketItemCostTV;
        private ImageView mMarketItemIconIV;

        public MarketItemViewHolder(View itemView){
            super(itemView);
            mMarketItemNameTV = itemView.findViewById(R.id.tv_market_item_name);
            mMarketItemCostTV = itemView.findViewById(R.id.tv_market_item_cost);
            mMarketItemIconIV = itemView.findViewById(R.id.iv_market_item_icon);
            itemView.setOnClickListener(this);
        }

        public void bind(MarketItem marketItem){
            mMarketItemNameTV.setText(marketItem.name);
            mMarketItemCostTV.setText(marketItem.costStr);
            String iconURL = SteamMarketUtils.buildIconURL(marketItem.icon);
            Glide.with(mMarketItemIconIV.getContext()).load(iconURL).into(mMarketItemIconIV);
        }

        @Override
        public void onClick(View v){
            MarketItem marketItem = mMarketItems.get(getAdapterPosition());
            mMarketItemClickListener.onMarketItemClick(marketItem);
        }
    }
}
