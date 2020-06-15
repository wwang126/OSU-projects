package com.example.android.connectedweather;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import com.example.android.connectedweather.data.ForecastData;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastItemViewHolder> {

    private ArrayList<ForecastData> mForecastData;
    private OnForecastItemClickListener mOnForecastItemClickListener;

    public ForecastAdapter(OnForecastItemClickListener onForecastItemClickListener) {
        mOnForecastItemClickListener = onForecastItemClickListener;
    }

    public void updateForecastData(ArrayList<ForecastData> forecastData) {
        mForecastData = forecastData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mForecastData != null) {
            return mForecastData.size();
        } else {
            return 0;
        }
    }

    @Override
    public ForecastItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastItemViewHolder holder, int position) {
        holder.bind(mForecastData.get(position).forecast);
    }

    public interface OnForecastItemClickListener {
        void onForecastItemClick(ForecastData forecast);
    }

    class ForecastItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mForecastTextView;

        public ForecastItemViewHolder(View itemView) {
            super(itemView);
            mForecastTextView = itemView.findViewById(R.id.tv_forecast_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnForecastItemClickListener.onForecastItemClick(
                            mForecastData.get(getAdapterPosition())
                    );
                }
            });
        }

        public void bind(String forecast) {
            mForecastTextView.setText(forecast);
        }
    }
}
