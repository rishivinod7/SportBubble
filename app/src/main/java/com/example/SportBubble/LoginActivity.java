package com.example.SportBubble;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    EditText user;
    EditText pass;
    DatabaseHelper db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);

        if(sharedPreferences.getBoolean("logged",false)){
            Toast.makeText(LoginActivity.this, "Logged back in", Toast.LENGTH_SHORT).show();

            goToMainActivity();
        }


        db = new DatabaseHelper(this);
        user = findViewById(R.id.etUsername);
        pass = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.login);
        TextView signUp_text = findViewById(R.id.signUp_text);
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();
                String userName = user.getText().toString().trim();
                String pwd = pass.getText().toString().trim();
                Boolean res = db.checkUser(userName, pwd);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (res == true) {
                    sharedPreferences.edit().putString("username", userName).commit();
                    sharedPreferences.edit().putString("password", pwd).commit();
                    sharedPreferences.edit().putBoolean("logged",true).apply();
                    goToMainActivity();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void goToMainActivity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }


    void checkDataEntered() {

        if (isEmpty(user)) {
            user.setError("Enter your username!");
        }
        if (isEmpty(pass)) {
            pass.setError("Enter your password!");
        }

    }

}


