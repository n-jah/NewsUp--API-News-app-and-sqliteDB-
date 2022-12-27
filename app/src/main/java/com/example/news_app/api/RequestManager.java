package com.example.news_app.api;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.news_app.OnFetchDataListener;
import com.example.news_app.R;
import com.example.news_app.models.NewsApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager extends AppCompatActivity {
    Context context;
    String lan="us";
    static String categories;

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }
    public String getLan() {
        return lan;
    }

//    String[] countries= context.getResources().getStringArray(R.array.country_api);
//    String[] categories= context.getResources().getStringArray(R.array.categories);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getNewsHeadlines(OnFetchDataListener listener, String category, String query){
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        Call<NewsApiResponse> call = callNewsApi.callHeadlines(getLan(),getCategories(),query,context.getString(R.string.api_key));
        try{
            call.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(context, "Error?", Toast.LENGTH_SHORT).show();
                    }
                    listener.onFetchData(response.body().getArticles(), response.message());
                }

                @Override
                public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                    listener.onError("Request Failed!");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public RequestManager(Context context) {
        this.context = context;
    }
    public interface CallNewsApi {
        @GET("top-headlines")
        Call<NewsApiResponse> callHeadlines(
            @Query("country")String country,
            @Query("category")String category,
            @Query("q")String query,
            @Query("apiKey")String api_key
        );
    }
}
