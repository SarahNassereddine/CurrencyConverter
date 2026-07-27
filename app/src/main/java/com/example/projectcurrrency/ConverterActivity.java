package com.example.projectcurrrency;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;



public class ConverterActivity extends AppCompatActivity {

    TextInputEditText etAmount;
    TextView tvResult;
    ArrayList<String> currencyList;
    HashMap<String, Double> ratesHashMap;
    Spinner spinnerFrom, spinnerTo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_converter);
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
        etAmount=findViewById(R.id.etAmount);
        tvResult=findViewById(R.id.tvResult);
        spinnerFrom=findViewById(R.id.spinnerFrom);
        spinnerTo=findViewById(R.id.spinnerTo);
        currencyList=new ArrayList<>();
        ratesHashMap =new HashMap<>();

        fetchRates();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.converter_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_profile){
            Intent i=new Intent(this, ProfileActivity.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.action_chat){
            Intent i=new Intent(this, ChatPoolActivity.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.action_logout){
        FirebaseAuth.getInstance().signOut();

        Intent i=new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    private void fetchRates(){
     String url= "https://api.exchangerate-api.com/v4/latest/USD";
        RequestQueue queue= Volley.newRequestQueue(this);
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null,
                response->{
                    try{
                        JSONObject rates=response.getJSONObject("rates");
                        currencyList.clear();
                        ratesHashMap.clear();
                        Iterator<String> keys=rates.keys();
                        while(keys.hasNext()){
                            String currency=keys.next();
                            double rate =rates.getDouble(currency);

                            currencyList.add(currency);
                            ratesHashMap.put(currency, rate);
                        }
                        ArrayAdapter<String> adapter =new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_dropdown_item,
                                currencyList);
                        spinnerFrom.setAdapter(adapter);
                        spinnerTo.setAdapter(adapter);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                },
                error->{
                    Toast.makeText(this, "check your connection", Toast.LENGTH_SHORT).show();
                }
                );
        queue.add(request);
    }
    public void convertBtnHandler(View v){
        String amountStr=etAmount.getText().toString();
        if(amountStr.isEmpty()){

            Toast.makeText(this, "Please put an amount", Toast.LENGTH_SHORT).show();
            return;
        }
        Double amount = Double.parseDouble(amountStr);
        String from= spinnerFrom.getSelectedItem().toString();
        String to=spinnerTo.getSelectedItem().toString();

        double fromeRate= ratesHashMap.get(from);
        double toRate= ratesHashMap.get(to);

        double result = amount/ fromeRate*toRate;
        tvResult.setBackgroundColor(Color.parseColor("#E2EDF6"));
        tvResult.setText(String.format("%.2f", result));
    }

}