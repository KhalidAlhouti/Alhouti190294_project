package com.example.se_328.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.se_328.R;
import com.example.se_328.SharedPref;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class ChangeCityName extends AppCompatActivity {


    Button btnUpdateCityName;
    EditText editTextCityName;
    boolean isActivityRunning=true;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_change_city_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Change City Name");
        editTextCityName=findViewById (R.id.editTextCityName);
        btnUpdateCityName=findViewById (R.id.btnUpdateCityName);

        btnUpdateCityName.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View v) {

                if(editTextCityName.getText ().toString ().isEmpty ())
                {
                    editTextCityName.setError ("Required Field");
                    editTextCityName.requestFocus ();
                    return;
                }

                Toast.makeText (ChangeCityName.this,"City Name Updated",Toast.LENGTH_LONG).show ();
                SharedPref.setCity (ChangeCityName.this,editTextCityName.getText ().toString ());
                loadWeather();

            }
        });
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed () {
        super.onBackPressed ();
        isActivityRunning=false;
        finish ();
    }
}