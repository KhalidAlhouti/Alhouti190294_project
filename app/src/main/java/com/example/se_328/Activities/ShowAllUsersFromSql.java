package com.example.se_328.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.se_328.Adapters.Adapter;
import com.example.se_328.Database;
import com.example.se_328.Models.User;
import com.example.se_328.R;

import java.util.ArrayList;
import java.util.List;

public class ShowAllUsersFromSql extends AppCompatActivity {


    RecyclerView recyclerView;
    List<User> userList=new ArrayList<>();
    Adapter adapter2;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_users_from_sql);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("All Users From Sqlite");


        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter2=new Adapter(this,userList,false);
        recyclerView.setAdapter(adapter2);

        Database database=new Database(this);

        userList.clear();
        userList.addAll(database.getUsers());
        adapter2.notifyDataSetChanged();
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
        finish();
        super.onBackPressed();

    }
}