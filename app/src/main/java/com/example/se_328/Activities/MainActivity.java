package com.example.se_328.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.se_328.R;
import com.example.se_328.SharedPref;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{


    boolean isActivityRunning=true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadWeather();
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
    protected void onResume () {
        super.onResume ();
        loadWeather ();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        isActivityRunning=false;
    }

    public void onClickFetchFromFirebase(View view)
    {
        startActivity(new Intent(MainActivity.this,Firebase_all_record_list.class));
    }
    public void onClickInterActFirebase(View view)
    {
        startActivity(new Intent(MainActivity.this,InteractFirebase.class));
    }

    public void onClickChangeCity (View view) {
        startActivity(new Intent(MainActivity.this,ChangeCityName.class));
    }

    public void onClickInterActSql (View view) {
        startActivity(new Intent(MainActivity.this,InteractSql.class));
    }
}