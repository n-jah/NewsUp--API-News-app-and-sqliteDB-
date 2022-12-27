package com.example.news_app;

import static com.example.news_app.loginActivity.flag;
import static com.example.news_app.loginActivity.getFlag;
import static com.example.news_app.loginActivity.setFlag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.news_app.adapters.CustomAdapter;
import com.example.news_app.api.RequestManager;
import com.example.news_app.models.NewsApiResponse;
import com.example.news_app.models.NewsHeadLines;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener {
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;
    public Button b1,b2,b3,b4,b5,b6,b7;
    SearchView searchView;
    BottomNavigationView bottomNavigationView;

    public static String category ="";
    public static String lang="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("values",MODE_PRIVATE);
        Boolean f =sharedPreferences.getBoolean("flag",true);
        setFlag(f);

        //check if it should loginin or no



        searchView= findViewById(R.id.search_main);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Searching...");
                dialog.show();
                RequestManager manager = new RequestManager(getBaseContext());
                manager.setLan(lang);
                manager.getNewsHeadlines(listener,category,query);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        dialog= new ProgressDialog(this);
        dialog.setTitle("Fetching Articles..");
        dialog.show();

        b1= findViewById(R.id.btn1);
        b1.setOnClickListener(this);
        b2= findViewById(R.id.btn2);
        b2.setOnClickListener(this);
        b3= findViewById(R.id.btn3);
        b3.setOnClickListener(this);
        b4= findViewById(R.id.btn4);
        b4.setOnClickListener(this);
        b5= findViewById(R.id.btn5);
        b5.setOnClickListener(this);
        b6= findViewById(R.id.btn6);
        b6.setOnClickListener(this);
        b7= findViewById(R.id.btn7);
        b7.setOnClickListener(this);

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener,manager.getCategories(),null);


        bottomNavigationView= findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        manager.getNewsHeadlines(listener,category,null);
                        dialog.setTitle("update");
                        dialog.show();
                        return true;
                    case R.id.trend:
                        category="general";
                        manager.getNewsHeadlines(listener,category,null);
                        dialog.setTitle("update");
                        dialog.show();
                        return true;
                    case R.id.setting:
                        startActivity(new Intent(MainActivity.this,SettingActivity.class));

                }
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();



        SharedPreferences sharedPreferences = getSharedPreferences("values",MODE_PRIVATE);
        lang =sharedPreferences.getString("langValue","");
        category=sharedPreferences.getString("catValue","");
        setFlag(sharedPreferences.getBoolean("flag",true));
        if (getFlag()==false){
            startActivity(new Intent(MainActivity.this,Login.class));
        }

        RequestManager manager = new RequestManager(this);
        manager.setLan(lang);
        manager.getNewsHeadlines(listener,category,null);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences("values",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("flag",getFlag());
        editor.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        RequestManager manager = new RequestManager(this);


        switch (item.getItemId()){
            case R.id.setting_eg:
                lang = "eg";
                manager.setLan(lang);
                manager.getNewsHeadlines(listener,manager.getCategories(),null);
                dialog.setTitle("update");
                dialog.show();
                break;
                
            case R.id.setting_it:
                lang = "it";
                manager.setLan(lang);
                manager.getNewsHeadlines(listener,manager.getCategories(),null);
                dialog.setTitle("update");
                dialog.show();
                break;

            case R.id.setting_us:
                lang = "us";
                manager.setLan(lang);
                manager.getNewsHeadlines(listener,manager.getCategories(),null);
                dialog.setTitle("update");
                dialog.show();
                break;
                
            case R.id.log_out:

                setFlag(false);
                SharedPreferences sharedPreferences = getSharedPreferences("values",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("flag",false);
                editor.commit();
                startActivity(new Intent(getApplicationContext(),Login.class));
                Toast.makeText(getApplicationContext(), "sign up", Toast.LENGTH_SHORT).show();
        }
            SharedPreferences sharedPreferences = getSharedPreferences("values",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("langValue",lang);
            editor.putString("catValue",category);
            editor.apply();

        return super.onOptionsItemSelected(item);
    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadLines> list, String message) {
            if(list.isEmpty()){
                Toast.makeText(MainActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }else {
                showNews(list);
                dialog.dismiss();
            }
        }
        @Override
        public void onError(String message) {

        }
    };

    private void showNews(List<NewsHeadLines> list) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter = new CustomAdapter(this,list,this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void OnNewsClicked(NewsHeadLines headLines) {
        startActivity(new Intent(MainActivity.this,DetailsActivity.class)
                .putExtra("data",headLines));
    }

    @Override
    public void onClick(View view) {
        
        Button button = (Button) view;
        category = button.getText().toString();
        dialog.setTitle("fetching new articles of"+category+"..");
        dialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences("values",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("catValue",category).commit();


        RequestManager manager = new RequestManager(this);
        manager.setCategories(category);
        manager.getNewsHeadlines(listener,category,null);


    }

}