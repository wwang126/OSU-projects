package com.example.android.basicweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private ArrayList<String> mForecastList;
    private ArrayList<String> mForecastExpanded;
    private OnForecastClicked mListener;

    public interface OnForecastClicked {
        void OnForecastClicked (String forecastExp);
    }

    public ForecastAdapter(OnForecastClicked listener) {
        mForecastList = new ArrayList<>();
        mForecastExpanded = new ArrayList<>();
        mListener = listener;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        String forecast = mForecastList.get(position);
        holder.bind(forecast);
    }

    public void addForecast(String forecast, String forecastExp) {
        mForecastList.add(0, forecast);
        mForecastExpanded.add(0, forecastExp);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return mForecastList.size();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView mForecastTV;

        public ForecastViewHolder(final View itemView) {
            super(itemView);
            mForecastTV = itemView.findViewById(R.id.tv_forecast_text);
            mForecastTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String forecastText = mForecastExpanded.get(getAdapterPosition());
                    mListener.OnForecastClicked(forecastText);
                }
            });
        }

        public void removeFromList() {
            int position = getAdapterPosition();
            mForecastList.remove(position);
            mForecastExpanded.remove(position);
            notifyItemRemoved(position);
        }

        void bind(String newForecastText) {
            mForecastTV.setText(newForecastText);
        }
    }
}
