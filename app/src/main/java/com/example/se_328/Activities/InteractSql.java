package com.example.se_328.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.se_328.R;

public class InteractSql extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_interact_sql);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Interact With Sqlite");
    }

    public void onclickShowAllUsers (View view) {

        startActivity(new Intent(InteractSql.this,ShowAllUsersFromSql.class));
    }
    public void onClickInserData (View view)
    {
        startActivity(new Intent(InteractSql.this,InsertUserSql.class));
    }

    public void onClickUpdateDelete (View view)
    {
        startActivity(new Intent(InteractSql.this,Update_Delete_User_Sql.class));
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
        super.onBackPressed();
        finish();
    }


}