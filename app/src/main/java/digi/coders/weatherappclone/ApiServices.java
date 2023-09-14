package digi.coders.weatherappclone;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("current")
    Call<JsonObject>getWeatherData(
            @Query("access_key")String access_key,
            @Query("query")String query
    );
}
