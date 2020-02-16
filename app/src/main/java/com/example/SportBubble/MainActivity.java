package com.example.SportBubble;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.SportBubble.mapsdemo.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    private RecyclerView recyclerView;
    private Toolbar mTopToolbar;
    private List<ListItem> listItems;
    private MyAdapter myAdapter;
    private static final String REQUEST_URL = "https://newsapi.org/v2/top-headlines?country=gb&category=sports&apiKey=d52790891481498680bc13da559f2b9f";

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageview);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        listItems = new ArrayList<>();
        loadRecyclerViewData();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadRecyclerViewData();
                pullToRefresh.setRefreshing(false);
            }
        });
//        imageView.setClipToOutline(true);

    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(i);
        }
        if (id == R.id.book_ground) {
            Intent i = new Intent(MainActivity.this, ScheduleActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void loadRecyclerViewData(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject baseobject = new JSONObject(response);
                            JSONArray articles = baseobject.getJSONArray("articles");

                            for (int i=0; i<articles.length(); i++){
                                JSONObject jsonObject = articles.getJSONObject(i);
                                ListItem listItem = new ListItem(jsonObject.getString("title"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("urlToImage"),
                                        jsonObject.getString("url"));

                                progressDialog.dismiss();
                                listItems.add(listItem);
                            }

                            myAdapter = new MyAdapter(getApplicationContext(), listItems, new ClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                                    i.putExtra("url", listItems.get(+position).getmDetailURL());
                                    startActivity(i);
                                    // do what ever you want to do with it
                                }});


                            recyclerView.setAdapter(myAdapter);


                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
