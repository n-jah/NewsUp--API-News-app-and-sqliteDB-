package com.example.news_app;

import static com.example.news_app.loginActivity.flag;
import static com.example.news_app.loginActivity.getFlag;
import static com.example.news_app.loginActivity.setFlag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText username,password;
    Button btnSignup,btnSignIn;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        //
        //        // add database to get data from it
        dbHelper = new DBHelper(this);

        intfin();
        listeners();

    }

    @Override
    public void onBackPressed() {
        if (getFlag()){
            super.onBackPressed();
        }else {
            Toast.makeText(this, "sign up first !", Toast.LENGTH_SHORT).show();
        }
    }

    private void listeners() {

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("")|| pass.equals("")){
                    Toast.makeText(Login.this, "Fill all the fields", Toast.LENGTH_SHORT).show();

                }else{
                    Boolean userCheckResult = dbHelper.checkUserName(user);
                    if (userCheckResult == false){
                        Boolean regResult = dbHelper.insertData(user,pass);
                        if (regResult == true){
                            Toast.makeText(Login.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, loginActivity.class);
                                startActivity(intent);
                                finish();

                        }else {
                            Toast.makeText(Login.this, "Registration field .", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(Login.this, "User already exists.\n go ahead to sign in", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),loginActivity.class);
                    startActivity(intent) ;
                    finish();


            }
        });

    }


    private void intfin() {
        username = findViewById(R.id.username_et);
        password = findViewById(R.id.password_et);
        btnSignup = findViewById(R.id.btnsignup);
        btnSignIn = findViewById(R.id.btnsignin);
    }
}