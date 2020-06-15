package com.example.android.sqliteweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.sqliteweather.data.ForecastLocation;
import com.example.android.sqliteweather.utils.OpenWeatherMapUtils;

import java.text.DateFormat;
import java.util.List;

public class ForecastLocationAdapter extends RecyclerView.Adapter<ForecastLocationAdapter.ForecastLocationViewHolder> {

    private List<ForecastLocation> mForecastLocations;
    private OnForecastLocationClickListener mForecastLocationClickListener;

    public interface OnForecastLocationClickListener {
        void onForecastLocationClick(ForecastLocation location);
    }

    public ForecastLocationAdapter(OnForecastLocationClickListener clickListener) {
        mForecastLocationClickListener = clickListener;
    }

    public void updateForecastItems(List<ForecastLocation> locations) {
        mForecastLocations = locations;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mForecastLocations != null) {
            return mForecastLocations.size();
        } else {
            return 0;
        }
    }

    @Override
    public ForecastLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.location_list_item, parent, false);
        return new ForecastLocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForecastLocationViewHolder holder, int position) {
        holder.bind(mForecastLocations.get(position));
    }

    class ForecastLocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mForecastLocationTV;

        public ForecastLocationViewHolder(View itemView) {
            super(itemView);
            mForecastLocationTV = itemView.findViewById(R.id.tv_location_item);
            itemView.setOnClickListener(this);
        }

        public void bind(ForecastLocation location) {
            mForecastLocationTV.setText(location.location);
        }

        @Override
        public void onClick(View v) {
            ForecastLocation forecastLocation = mForecastLocations.get(getAdapterPosition());
            mForecastLocationClickListener.onForecastLocationClick(forecastLocation);
        }
    }
}
