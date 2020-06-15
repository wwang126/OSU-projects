package com.example.android.connectedweather;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.connectedweather.data.ForecastData;

import java.util.List;

public class ForecastDetailActivity extends AppCompatActivity{
    public static final String EXTRA_FORECAST_DATA = "ForecastData";
    private ForecastData mForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_FORECAST_DATA)) {
            mForecast = (ForecastData) intent.getSerializableExtra(EXTRA_FORECAST_DATA);

            TextView forecastDateTV = findViewById(R.id.tv_forecast_date);
            forecastDateTV.setText(mForecast.date);

            TextView forecastDataTV = findViewById(R.id.tv_forecast_data);
            String forecastData = mForecast.detailedForecast;
            forecastDataTV.setText(forecastData);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_location:
                viewLocation();
                return true;
            case R.id.action_share:
                shareForecast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void viewLocation() {
        if (mForecast != null) {
            Uri locationUri = Uri.parse("https://www.google.com/maps/place/Corvallis,+OR/data=!4m2!3m1!1s0x54c0409daa14d77d:0xd70d808f22bdc0be?sa=X&ved=2ahUKEwiMxfzC_8jnAhVkJTQIHXUnDyEQ8gEwAHoECGYQAQ");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, locationUri);

            PackageManager pm = getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(webIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (activities.size() > 0) {
                startActivity(webIntent);
            }
        }
    }

    private void shareForecast() {
        if (mForecast != null) {
            String shareText = mForecast.share;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(shareIntent, null);
            startActivity(chooserIntent);
        }
    }
}
