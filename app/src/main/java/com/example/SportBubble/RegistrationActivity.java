package com.example.SportBubble;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegistrationActivity extends AppCompatActivity {

    EditText email,uname,pass;
    Button reg, login;
    Intent intent;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        email =(EditText) findViewById(R.id.email);
        uname = (EditText)findViewById(R.id.username);
        reg = (Button) findViewById(R.id.sign_up);
        pass = (EditText)findViewById(R.id.password);
        db = new DatabaseHelper(this);

        TextView signIn_text = findViewById(R.id.signIn_text);
        signIn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDataEntered()){
                    String user = uname.getText().toString().trim();
                    String pwd = pass.getText().toString().trim();
                    String Email = email.getText().toString().trim();

                    checkDataEntered();
                    long val = db.addUser(user,pwd,Email);
                    if(val > 0){
                        Toast.makeText(RegistrationActivity.this,"You have registered",Toast.LENGTH_SHORT).show();
                        Intent moveToLogin = new Intent(RegistrationActivity.this,LoginActivity.class);
                        startActivity(moveToLogin);
                    }
                    else{
                        Toast.makeText(RegistrationActivity.this,"Registration Error",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }



    boolean checkDataEntered() {

        if (!isEmail(email)) {
            email.setError("Enter valid email!");
            return false;
        }
        if (isEmpty(uname)) {
            uname.setError("Enter your username!");
            return false;
        }
        if (isEmpty(pass)) {
            pass.setError("Enter your password!");
            return false;
        }
        return true;

    }

}