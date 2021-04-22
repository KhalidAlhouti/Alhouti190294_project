package com.example.se_328.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.se_328.Database;
import com.example.se_328.Models.User;
import com.example.se_328.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class Update_Add_User extends AppCompatActivity
{


    EditText etId;
    EditText etFName;
    EditText etLName;
    EditText etPhoneNumber;
    EditText etEmail;
    Button btnSave;
    User user;
    boolean isUpdate=false;
    boolean isUpdatetoSqlite;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__add__user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add User");

        user=new User();

        btnSave=findViewById(R.id.btnSave);
        etId=findViewById(R.id.etId);
        etFName=findViewById(R.id.etFName);
        etLName=findViewById(R.id.etLName);
        etPhoneNumber=findViewById(R.id.etPhoneNumber);
        etEmail=findViewById(R.id.etEmail);
        etId=findViewById(R.id.etId);

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickSave(v);
            }
        });
        if(getIntent().getExtras()!=null)
        {
            user= (User) getIntent().getExtras().getSerializable("User");
            isUpdatetoSqlite=getIntent().getExtras().getBoolean("isUpdatetoSqlite",false);
            isUpdate=true;

            getSupportActionBar().setTitle("Update User");

            btnSave.setText("Update");
            etId.setText(user.getUserId()+"");
            etFName.setText(user.getFirstName());
            etLName.setText(user.getLastName());
            etPhoneNumber.setText(user.getPhoneNumber());
            etEmail.setText(user.getEmailAddress());

            etId.setEnabled(false);
        }
    }


    public void onClickSave(View view)
    {

        for(EditText editText:new EditText[]{etId,etFName,etLName,etPhoneNumber,etEmail})
        {
            if(editText.getText().toString().isEmpty())
            {
                editText.setError("Required Field");
                editText.requestFocus();
                return;
            }

        }

        user.setUserId(Integer.valueOf(etId.getText().toString().trim()));
        user.setFirstName(etFName.getText().toString());
        user.setLastName(etLName.getText().toString());
        user.setPhoneNumber(etPhoneNumber.getText().toString());
        user.setEmailAddress(etEmail.getText().toString());



        if(isUpdatetoSqlite)
        {
            Database database=new Database(this);
            boolean isUpdatedSuccessfully=database.UpdateUser(user);
            String succesMessage=isUpdate?"Updated Successfully":"Saved Successfully";
            String failMessage=isUpdate?"Failed To Update":"Failed To Save";
            Toast.makeText(Update_Add_User.this,isUpdatedSuccessfully?succesMessage:failMessage,Toast.LENGTH_LONG).show();



        }
        else
        {
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            FirebaseDatabase.getInstance().getReference("users").child(isUpdate?user.getUserKey():String.valueOf(user.getUserId())).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {

                    progressDialog.dismiss();

                    String succesMessage=isUpdate?"Updated Successfully":"Saved Successfully";
                    String failMessage=isUpdate?"Failed To Update":"Failed To Save";
                    Toast.makeText(Update_Add_User.this,task.isSuccessful()?succesMessage:failMessage,Toast.LENGTH_LONG).show();


                }
            });
        }

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

}