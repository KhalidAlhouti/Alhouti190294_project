package com.example.se_328.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.se_328.Adapters.Adapter;
import com.example.se_328.Models.User;
import com.example.se_328.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Firebase_all_record_list extends AppCompatActivity
{


    RecyclerView recyclerView;
    List<User> userList=new ArrayList<>();
    Adapter adapter;
    boolean isActivityRunning=true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_all_record_list);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Firebase All Records");

        adapter=new Adapter(this,userList,false);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


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
                    Toast.makeText(Firebase_all_record_list.this,"No Data Found",Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Firebase_all_record_list.this,"Error : "+error.getMessage(),Toast.LENGTH_LONG).show();
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
        isActivityRunning=false;
        finish();
        super.onBackPressed();

    }
}