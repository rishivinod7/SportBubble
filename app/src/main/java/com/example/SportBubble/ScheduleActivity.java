package com.example.SportBubble;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    Spinner spinner1,spinner2;
    EditText name, email;
    Button check, book;
    TextView test;
    ArrayList<String> GroundNames = new ArrayList<>();
    ArrayList<String> Timings = new ArrayList<>();
    String URL="https://5d3fe3a2c516a90014e891c2.mockapi.io/All";
//    String URL="http://www.mocky.io/v2/5d41228131000062005390f6";
    String TimeURL="https://5d3fe3a2c516a90014e891c2.mockapi.io/All/1/GroundsJSON";
    String placeholder1, placeholder2 = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        spinner1=(Spinner)findViewById(R.id.showGrounds);
        spinner2=(Spinner)findViewById(R.id.showSlots);
        check = (Button) findViewById(R.id.CheckAvail);
        book = (Button) findViewById(R.id.book);
        name = findViewById(R.id.name_book);
        email = findViewById(R.id.email_book);
        test = findViewById(R.id.jsonview);

        copyFiles();




        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroundNames = new ArrayList<>();
                try {
                    putSpinner1();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                loadSpinnerData(URL);
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String ground = spinner1.getItemAtPosition(spinner1.getSelectedItemPosition()).toString();
                        Toast.makeText(getApplicationContext(),ground, Toast.LENGTH_LONG).show();
                        spinner2.setVisibility(View.VISIBLE);
                        placeholder1 = ground;
                        Timings = new ArrayList<>();
                        try {
                            putSpinner2();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        loadSpinnerData2(TimeURL);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String timing = spinner2.getItemAtPosition(spinner2.getSelectedItemPosition()).toString();
                        placeholder2 = timing;
                        book.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            submitData(placeholder1, placeholder2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });



    }

    private void copyFiles() {
        //Copy files from assets to internal storage files
        JSONArray arr = null;
        JSONArray arr2 = null;
        try {
            arr = new JSONArray(readasset());
            arr2 = new JSONArray(readasset2());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Writer output = null;
        Writer output2 = null;
        File file = new File( getFilesDir(),"timingsfile.json");
        File file2 = new File( getFilesDir(),"groundsfile.json");
        if(file.exists() && file2.exists())
            return;
        try {
            output = new BufferedWriter(new FileWriter(file));
            output2 = new BufferedWriter(new FileWriter(file2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output.write(arr.toString());
            output2.write(arr2.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output.close();
            output2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.setReadable(true, false);
        file.setWritable(true, false);
        file.setExecutable(true, false);
        file2.setReadable(true, false);
        file2.setWritable(true, false);
        file2.setExecutable(true, false);
    }

//    private void loadSpinnerData(String url) {
//        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
//        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try{
//                    JSONArray jsonArray=new JSONArray(response);
//                    for(int i=0;i<jsonArray.length();i++){
//                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
//                        if(jsonObject1.getInt("id")==1){
//                            JSONArray jsonArray1 = jsonObject1.getJSONArray("Grounds");
//                            for(int a=0;a<jsonArray1.length();a++){
//                                JSONObject jsonObject2=jsonArray1.getJSONObject(a);
//                                String ground=jsonObject2.getString("Name");
//                                GroundNames.add(ground);
////                            System.out.println("reached");
//                            }
//                        }
//
//                    }
//
//
//
//
//                    spinner1.setAdapter(new ArrayAdapter<String>(ScheduleActivity.this, android.R.layout.simple_spinner_dropdown_item, GroundNames));
//                }catch (JSONException e){e.printStackTrace();}
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(policy);
//        requestQueue.add(stringRequest);
//    }
//    private void loadSpinnerData2(String url) {
//        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
//        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try{
//                    JSONArray jsonArray=new JSONArray(response);
//                    for(int i=0;i<jsonArray.length();i++){
//                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
//                        if(jsonObject1.getString("Name").equals(placeholder1)){
//                            JSONArray jsonArray1 = jsonObject1.getJSONArray("Timings");
//                            for(int a=0;a<jsonArray1.length();a++){
//                                JSONObject jsonObject2=jsonArray1.getJSONObject(a);
//                                if(jsonObject2.getBoolean("Availability")==true){
//                                    String time =jsonObject2.getString("Day")+ " "+ jsonObject2.getString("Time");
//                                    Timings.add(time);
//                                }
//                            }
//                        }
//
//                    }
//
//
//
//
//                    spinner2.setAdapter(new ArrayAdapter<String>(ScheduleActivity.this, android.R.layout.simple_spinner_dropdown_item, Timings));
//                }catch (JSONException e){e.printStackTrace();}
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(policy);
//        requestQueue.add(stringRequest);
//    }

    private void submitData(String ground, String time) throws JSONException, IOException {
        JSONArray arr = new JSONArray(readTimings());
        for(int i = 0; i < arr.length(); i++){

            JSONObject jsonObj = (JSONObject)arr.getJSONObject(i); // get the josn object
            if (jsonObj.getString("Name").equals(ground)) {
                JSONArray jsonArray1 = jsonObj.getJSONArray("Timings");
                for (int a = 0; a < jsonArray1.length(); a++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(a);
                    if (jsonObject2.getBoolean("Availability") == true) {
                        if((jsonObject2.getString("Day")+" "+jsonObject2.getString("Time")).equals(time)){
                            jsonObject2.put("Availability", false);

                            Writer output = null;
                            File file = new File( getFilesDir(),"timingsfile.json");
                            file.setReadable(true, false);
                            file.setWritable(true, false);
                            file.setExecutable(true, false);
                            output = new BufferedWriter(new FileWriter(file));
                            output.write(arr.toString());
                            output.close();




                        }
                    }
                }
            }
//            test.setText(arr.toString());// display and verify your Json with updated value
        }




    }

    public String readGrounds() {
        String json = null;
        try {
            Context ctx = getApplicationContext();
            FileInputStream is = ctx.openFileInput("groundsfile.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public String readTimings() {
        String json = null;
        try {
            Context ctx = getApplicationContext();
            FileInputStream is = ctx.openFileInput("timingsfile.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public void putSpinner1() throws JSONException {
        JSONArray jsonArray=new JSONArray(readGrounds());
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject1=jsonArray.getJSONObject(i);
            if(jsonObject1.getInt("id")==1){
                JSONArray jsonArray1 = jsonObject1.getJSONArray("Grounds");
                for(int a=0;a<jsonArray1.length();a++){
                    JSONObject jsonObject2=jsonArray1.getJSONObject(a);
                    String ground=jsonObject2.getString("Name");
                    GroundNames.add(ground);
                }
            }

        }
        spinner1.setAdapter(new ArrayAdapter<String>(ScheduleActivity.this, android.R.layout.simple_spinner_dropdown_item, GroundNames));
    }
    public void putSpinner2() throws JSONException {
        JSONArray jsonArray = new JSONArray(readTimings());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            if (jsonObject1.getString("Name").equals(placeholder1)) {
                JSONArray jsonArray1 = jsonObject1.getJSONArray("Timings");
                for (int a = 0; a < jsonArray1.length(); a++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(a);
                    if (jsonObject2.getBoolean("Availability") == true) {
                        String time = jsonObject2.getString("Day") + " " + jsonObject2.getString("Time");
                        Timings.add(time);
                    }
                }
            }

        }


        spinner2.setAdapter(new ArrayAdapter<String>(ScheduleActivity.this, android.R.layout.simple_spinner_dropdown_item, Timings));
    }
    private String readasset(){
        String json = null;
        try {
            InputStream is = getAssets().open("timings.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private String readasset2(){
        String json = null;
        try {
            InputStream is = getAssets().open("grounds.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}


