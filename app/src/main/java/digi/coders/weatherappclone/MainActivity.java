package digi.coders.weatherappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final String ACCESS_KEY = "d5e57c6f115975ffb1dc9771a41b3e7c";
    String QUERY = "Lucknow";

    EditText etdlocation;

    ImageView weatherIcon;

    Button btnSearch, btnRefresh;

    MaterialToolbar mtbar;

    TextView observation_time, txtLocationName, txtLocalTime, txtTemperature, txtWeatherDescriptions, txtWindSpeed, txtVisibility, txtHumidity, txtWindDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        observation_time = findViewById(R.id.observation_time);
        txtLocationName = findViewById(R.id.txtLocationName);
        txtLocalTime = findViewById(R.id.txtLocalTime);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtWeatherDescriptions = findViewById(R.id.txtWeatherDescriptions);
        txtWindSpeed = findViewById(R.id.txtWindSpeed);
        txtVisibility = findViewById(R.id.txtVisibility);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtWindDir = findViewById(R.id.txtWindDir);

        etdlocation = findViewById(R.id.etdlocation);
        weatherIcon = findViewById(R.id.weatherIcon);
        btnSearch = findViewById(R.id.btnSearch);
        btnRefresh = findViewById(R.id.btnRefresh);
        mtbar = findViewById(R.id.mtbar);

        getWeatherdata();

        mtbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QUERY = etdlocation.getText().toString();
                etdlocation.setText("");
                getWeatherdata();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherdata();
                Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getWeatherdata() {
        RetrofitClient.getClient().getWeatherData(ACCESS_KEY, QUERY).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(MainActivity.this, "Weather API Calling Success", Toast.LENGTH_SHORT).show();
                JsonObject jsonObject = response.body().getAsJsonObject();

                String time = jsonObject.getAsJsonObject("current").get("observation_time").getAsString();
                observation_time.setText("Time: " + time);

                String LocationName = jsonObject.getAsJsonObject("location").get("name").getAsString();
                String Country = jsonObject.getAsJsonObject("location").get("country").getAsString();
                String Region = jsonObject.getAsJsonObject("location").get("region").getAsString();
                txtLocationName.setText(LocationName + ", " + Region + ", " + Country);

                String LocalTime = jsonObject.getAsJsonObject("location").get("localtime").getAsString();
                txtLocalTime.setText("Refreshed At: " + LocalTime);

                int tem = jsonObject.getAsJsonObject("current").get("temperature").getAsInt();
                txtTemperature.setText(tem + "");

                String iconUrl = jsonObject.getAsJsonObject("current").getAsJsonArray("weather_icons").get(0).getAsString();
                Picasso.get().load(iconUrl).placeholder(R.drawable.baseline_wb_sunny_24).into(weatherIcon);

                String weatherDis = jsonObject.getAsJsonObject("current").getAsJsonArray("weather_descriptions").get(0).getAsString();
                txtWeatherDescriptions.setText(weatherDis);

                int windSpeed = jsonObject.getAsJsonObject("current").get("wind_speed").getAsInt();
                txtWindSpeed.setText("Wind: " + windSpeed + " Kmph");

                String windDir = jsonObject.getAsJsonObject("current").get("wind_dir").getAsString();
                txtWindDir.setText("Wind Dir: " + windDir + " Kmph");

                int humidity = jsonObject.getAsJsonObject("current").get("humidity").getAsInt();
                txtHumidity.setText("Humidity: " + humidity);

                int visibility = jsonObject.getAsJsonObject("current").get("visibility").getAsInt();
                txtVisibility.setText("Visibility: " + visibility);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}