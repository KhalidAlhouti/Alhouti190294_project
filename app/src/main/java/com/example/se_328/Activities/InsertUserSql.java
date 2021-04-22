package com.example.se_328.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.se_328.Adapters.Adapter;
import com.example.se_328.Adapters.Adapter2;
import com.example.se_328.Database;
import com.example.se_328.Models.User;
import com.example.se_328.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InsertUserSql extends AppCompatActivity {

    RecyclerView recyclerView;
    List<User> userList=new ArrayList<>();
    Adapter2 adapter2;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user_sql);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Insert User To Sqlite");


        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter2=new Adapter2(this,userList,false);
        recyclerView.setAdapter(adapter2);

        Toast.makeText(this,"Loading Data...",Toast.LENGTH_LONG).show();
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                userList.clear();
                for(DataSnapshot child:snapshot.getChildren())
                {
                    userList.add(child.getValue(User.class));
                }

                if(userList.isEmpty())
                {
                    Toast.makeText(InsertUserSql.this,"No Data Found",Toast.LENGTH_LONG).show();
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(InsertUserSql.this,"Error : "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
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