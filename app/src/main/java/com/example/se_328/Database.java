package com.example.se_328;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.se_328.Models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Database extends SQLiteOpenHelper {
    private static final String DBNAME="Mydb.db";
    private static final int DBVERSION=1;
    private SQLiteDatabase db;
    public Database (Context context)
    {
        super(context,DBNAME,null,DBVERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table  if not exists 'Users'(userId INTEGER,firstName TEXT,lastName TEXT,phoneNumber TEXT,emailAddress TEXT);");
            }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF  EXISTS 'Users'");
        onCreate(db);
    }
    public boolean insertUser(User user)
    {
          db = this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("userId" , user.getUserId());
        cv.put("firstName" , user.getFirstName());
        cv.put("lastName" , user.getLastName());
        cv.put("phoneNumber" , user.getPhoneNumber());
        cv.put("emailAddress" , user.getEmailAddress());

        if(UpdateUser(user)) //First we will see if user is already there in database
        {
            return true;
        }
        else
            return db.insert("Users", null, cv)>0;

    }
    public boolean UpdateUser(User user)
    {
        db = this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("userId" , user.getUserId());
        cv.put("firstName" , user.getFirstName());
        cv.put("lastName" , user.getLastName());
        cv.put("phoneNumber" , user.getPhoneNumber());
        cv.put("emailAddress" , user.getEmailAddress());
        return db.update("Users", cv, "userId="+user.getUserId(),null)>0;

    }

    public boolean deleteUser(User user)
    {
        db = this.getWritableDatabase();
        return db.delete("Users", "userId="+user.getUserId(),null)>0;

    }
    public  List<User> getUsers()
    {

        db=this.getReadableDatabase();
        List<User> userList=new ArrayList<>();
        String[] Columns = {"userId","firstName","lastName","phoneNumber","emailAddress"};
        Cursor cursor=db.query("Users" , Columns , null , null , null , null , null);
        try
        {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
                User user=new User();
                user.setUserId(cursor.getInt(0));
                user.setFirstName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
                user.setPhoneNumber(cursor.getString(3));
                user.setEmailAddress(cursor.getString(4));
                userList.add(user);

            }
        } finally
        {
            cursor.close();
        }
        return userList;
    }

}

