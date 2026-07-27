package com.example.projectcurrrency;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Button logInBtn;
    TextInputEditText etEmail, etPassword;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        db= FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!= null){
            Intent i=new Intent(this, ConverterActivity.class);
            startActivity(i);
            finish();
        }

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("  Currency Converter");
            getSupportActionBar().setIcon(R.drawable.ic_currency_exchange);
        }

        logInBtn=findViewById(R.id.logInBtn);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        firebaseAuth=FirebaseAuth.getInstance();

    }
    public void logInBtnHandler(View v){
        String email=etEmail.getText().toString().trim();
        String password=etPassword.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()){

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,task->
            {
                if(task.isSuccessful()){
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(this, ConverterActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(this, "login failed:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
    public void signUpBtnHandler(View v){
        String email=etEmail.getText().toString().trim();
        String password=etPassword.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()){

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        }
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task->{
            if(task.isSuccessful()){
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(this, ConverterActivity.class);

                // to ensure a good user experience the user doen't need to login every time:
                String uid = firebaseAuth.getCurrentUser().getUid();
                HashMap<String, Object> user = new HashMap<>();
                user.put("username", "Guest");
                user.put("location", "Unknown");
                user.put("profileUrl", null );
                db.collection("users").document(uid).set(user);

                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(this, "SignUp failed:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}