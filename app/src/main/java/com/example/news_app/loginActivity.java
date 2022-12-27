package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {
EditText username,password;
Button btnLogin;
DBHelper myDB;
static Boolean flag =false ;

    public static Boolean getFlag() {
        return flag;
    }

    public static void setFlag(Boolean flag) {
        loginActivity.flag = flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);
        getSupportActionBar().hide(); //hide the title bar
        intviews();

    }


    private void intviews() {
    username = findViewById(R.id.username_et_login);
    password = findViewById(R.id.password_et_login);
    btnLogin = findViewById(R.id.Sign_in_in_login);
    myDB = new DBHelper(this);
    }


    public void onClick(View view) {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            try {
                if (user.equals("")||user.equals("")){
                    Toast.makeText(loginActivity.this, "please fill the fields ", Toast.LENGTH_SHORT).show();

                }else{
                    Boolean result =myDB.checkUserNamePassword(user,pass);
                    if(result == true){
                        try {

                            setFlag(true);
                            SharedPreferences sharedPreferences = getSharedPreferences("values",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("flag",true);
                            editor.commit();
                        } finally {
                            finish();

                        }
                    }else{

                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    @Override
    public void onBackPressed() {
        setFlag(false);
        finish();
    }
}