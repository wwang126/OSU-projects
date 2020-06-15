package com.example.android.basicweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.OnForecastClicked{

    private RecyclerView mForecastListRV;
    private ForecastAdapter mForecastAdapter;

    private Toast mToast;

    private String[] sForecasts = {
            "Wed April 12 - Sunny and Warm - 75F",
            "Thu April 13 - Overcast and Cool - 50F",
            "Fri April 14 - Overcast and Very Cool - 40F",
            "Sat April 15 - Clear and Cold - 32F",
            "Sun April 16 - Mild Snow and Very Cold - 25F",
            "Mon April 17 - Clear and Very Cool - 44F",
            "Tue April 18 - Sunny and Cool - 65F",
            "Wed April 19 - Sunny and Very Warm - 80F",
            "Thu April 20 - Overcast and Cool - 50F",
            "Fri April 21 - Overcast and Very Cool - 40F",
            "Sat April 22 - Clear and Cold - 32F",
            "Sun April 23 - Mild Snow and Very Cold - 25F",
            "Mon April 24 - Clear and Very Cool - 44F",
            "Tue April 25 - Sunny and Cool - 65F",
            "Wed April 26 - Sunny and Very Warm - 80F"
    };
    private String[] sForecastsExp = {
            "Wednesday April 12, A nice and sunny day with expected high of 75F and a low of 50F",
            "Thursday April 13, A cooler overcast day expected high of 50F and a low of 30F",
            "Friday April 14, A very cool overcast day expected high of 40F and a low of 35F",
            "Saturday April 15,  A clear and cold day expected high of 38F and a low of 30F",
            "Sunday April 16, Mild Snow expected and an expected high of 32F and a low of 20F",
            "Monday April 17,  A clear and very cool day expected high of 50F and a low of 38F",
            "Tuesday April 18 , A cooler sunny day with expected high of 65F and a low of 45F",
            "Wednesday April 19, A nice and sunny day with expected high of 82F and a low of 70F",
            "Thursday April 20 , A cooler overcast day expected high of 50F and a low of 30F",
            "Friday April 21,  A overcast and very cool day with expected high of 50F and a low of 35F",
            "Saturday April 22,  A clear and cold day expected high of 38F and a low of 30F",
            "Sunday April 23, Mild Snow expected and an expected high of 32F and a low of 20F",
            "Monday April 24,  A clear and very cool day expected high of 50F and a low of 38F",
            "Tuesday April 25, A cooler sunny day with expected high of 65F and a low of 45F",
            "Wednesday April 26, A nice and sunny day with expected high of 82F and a low of 70F"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToast = null;
        mForecastListRV = findViewById(R.id.rv_forecast_list);
        mForecastListRV.setLayoutManager(new LinearLayoutManager(this));
        mForecastListRV.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(this);
        mForecastListRV.setAdapter(mForecastAdapter);

        mForecastListRV.setItemAnimator(new DefaultItemAnimator());

        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[0],sForecastsExp[0]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[1],sForecastsExp[1]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[2],sForecastsExp[2]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[3],sForecastsExp[3]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[4],sForecastsExp[4]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[5],sForecastsExp[5]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[6],sForecastsExp[6]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[7],sForecastsExp[7]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[8],sForecastsExp[8]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[9],sForecastsExp[9]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[10],sForecastsExp[10]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[11],sForecastsExp[11]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[12],sForecastsExp[12]);
        mForecastListRV.scrollToPosition(0);
        mForecastAdapter.addForecast(sForecasts[13],sForecastsExp[13]);
    }
    @Override
    public void OnForecastClicked(String forecastExp){
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, forecastExp, Toast.LENGTH_LONG);
        mToast.show();
    }
}
