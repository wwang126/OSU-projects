package com.example.steammarketapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.steammarketapp.data.MarketItem;
import com.example.steammarketapp.data.OwnedItem;
import com.example.steammarketapp.utils.SteamMarketUtils;

import java.util.List;

public class OwnedAdapter extends RecyclerView.Adapter<OwnedAdapter.OwnedItemViewHolder> {

    private List<OwnedItem> mOwnedItems;
    private OnOwnedItemClickListener mOwnedItemClickListener;

    public interface OnOwnedItemClickListener {
        void onOwnedItemClick(OwnedItem marketItem);
    }

    public OwnedAdapter(OnOwnedItemClickListener clickListener){
        mOwnedItemClickListener = clickListener;
    }

    public void updateMarketItems(List<OwnedItem> ownedItems){
        mOwnedItems = ownedItems;
        notifyDataSetChanged();
    }

    @Override public int getItemCount() {
        if (mOwnedItems != null){
            return mOwnedItems.size();
        } else{
            return 0;
        }
    }

    @Override
    public OwnedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.owned_list_item, parent, false);
        return new OwnedItemViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(OwnedItemViewHolder holder, int position) {
        holder.bind(mOwnedItems.get(position));
    }

    class OwnedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mOwnedItemNameTV;
        private TextView mOwnedItemCostTV;
        private ImageView mOwnedItemIconIV;

        public OwnedItemViewHolder(View itemView){
            super(itemView);
            mOwnedItemNameTV = itemView.findViewById(R.id.tv_owned_item_name);
            mOwnedItemCostTV = itemView.findViewById(R.id.tv_owned_item_purchase_cost);
            mOwnedItemIconIV = itemView.findViewById(R.id.iv_owned_item_icon);
            itemView.setOnClickListener(this);
        }

        public void bind(OwnedItem ownedItem){
            mOwnedItemNameTV.setText(ownedItem.name);
            mOwnedItemCostTV.setText(ownedItem.costPurchasedStr);
            String iconURL = SteamMarketUtils.buildIconURL(ownedItem.icon);
            Glide.with(mOwnedItemIconIV.getContext()).load(iconURL).into(mOwnedItemIconIV);
        }

        @Override
        public void onClick(View v){
            OwnedItem ownedItem = mOwnedItems.get(getAdapterPosition());
            mOwnedItemClickListener.onOwnedItemClick(ownedItem);
        }
    }
}
