package com.example.se_328.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.se_328.Adapters.Adapter;
import com.example.se_328.Models.User;
import com.example.se_328.R;
import com.example.se_328.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InteractFirebase extends AppCompatActivity
{

    RecyclerView recyclerView;
    List<User> userList=new ArrayList<>();
    Adapter adapter;
    boolean isActivityRunning=true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_firebase);


        loadWeather();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Interact Firebase");

        adapter=new Adapter(this,userList,true);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        Toast.makeText(this, "Loading Data...", Toast.LENGTH_LONG).show();
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                userList.clear();
                for(DataSnapshot child:snapshot.getChildren())
                {
                    User value = child.getValue(User.class);
                    value.setUserKey(child.getKey());
                    userList.add(value);
                }

                if(userList.isEmpty())
                {
                    Toast.makeText(InteractFirebase.this,"No Data Found",Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(InteractFirebase.this,"Error : "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



    }
    private void loadWeather ()
    {

        TextView txtTemp=findViewById (R.id.txtTemp);
        ImageView imgWeather=findViewById (R.id.imgweather);
        TextView txtHumidity=findViewById (R.id.txtHumidity);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.openweathermap.org/data/2.5/weather?q=CITY&appid=e5c256b7940686e7620e95e5cd053f0a";
        url=url.replace ("CITY", SharedPref.getCity (this));
        StringRequest stringRequest=new StringRequest (Request.Method.GET, url,
                new Response.Listener<String> () {
                    @Override
                    public void onResponse (String response)
                    {
                        if(!isActivityRunning)
                            return;


                        try
                        {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONObject jsonObjectweather=(JSONObject) jsonObject.getJSONArray("weather").get(0);
                            String icon=jsonObjectweather.getString("icon");

                            JSONObject jsonObjectmain=jsonObject.getJSONObject("main");
                            Double temp = jsonObjectmain.getDouble ("temp") - 273.15;

                            txtTemp.setText (String.format ("%.2f", temp)+"°C");
                            Picasso.get ().load ("https://openweathermap.org/img/w/icon.png".replace ("icon",icon)).into (imgWeather);
                            txtHumidity.setText (jsonObjectmain.getDouble ("humidity")+" ");

                        } catch (Exception e)
                        {
                            txtTemp.setText ("City Not Found..");
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse (VolleyError error)
            {

                txtTemp.setText ("---");
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        isActivityRunning=false;
        finish();
        super.onBackPressed();

    }
    public void onClickAddNewUser(View view)
    {
        startActivity(new Intent(this,Update_Add_User.class));
    }
}