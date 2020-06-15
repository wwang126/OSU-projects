package com.example.android.connectedweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.connectedweather.utils.ForecastUtils;
import com.example.android.connectedweather.utils.NetworkUtils;
import com.example.android.connectedweather.data.ForecastData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.OnForecastItemClickListener {

    private RecyclerView mForecastListRV;
    private ForecastAdapter mForecastAdapter;
    private Toast mToast;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mErrorMessageTV;
    private ForecastUtils parser;
    private ForecastQuery query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mForecastListRV = findViewById(R.id.rv_forecast_list);

        mForecastListRV.setLayoutManager(new LinearLayoutManager(this));
        mForecastListRV.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(this);
        mForecastListRV.setAdapter(mForecastAdapter);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mErrorMessageTV = findViewById(R.id.tv_error_message);
        query = new ForecastQuery();
        query.execute();
    }

    @Override
    public void onForecastItemClick(ForecastData forecast) {
        Intent intent = new Intent(this, ForecastDetailActivity.class);
        intent.putExtra(ForecastDetailActivity.EXTRA_FORECAST_DATA, forecast);
        startActivity(intent);
    }

    public class ForecastQuery extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String ... strings) {
            String url = "https://api.openweathermap.org/data/2.5/forecast?q=Corvallis,US&APPID=22220afffb5a9cfced287377a6a16dfe";
            String forecastResults = null;
            try {
                forecastResults = NetworkUtils.doHttpGet(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return forecastResults;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
            if (s != null) {
                mErrorMessageTV.setVisibility(View.INVISIBLE);
                ArrayList<ForecastData> forecast = parser.parseForecastResults(s);
                mForecastAdapter.updateForecastData(
                        new ArrayList<ForecastData>(forecast)
                );
            } else {
                mErrorMessageTV.setVisibility(View.VISIBLE);
            }
        }
    }
}
