package com.example.se_328.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se_328.Activities.Update_Add_User;
import com.example.se_328.Database;
import com.example.se_328.Models.User;
import com.example.se_328.R;

import java.util.List;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder>
{



    List<User> userList;
    boolean shouldInteract=false;
    Context context;

    public Adapter2 (Context context, List<User> userList, boolean shouldInteract)
    {
        this.userList=userList;
        this.shouldInteract=shouldInteract;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_user_sql , null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {


        User user = userList.get(position);
        holder.txtUserId.setText(String.valueOf(user.getUserId()));
        holder.txtFName.setText(user.getFirstName());
        holder.txtLName.setText(user.getLastName());
        holder.txtEmailAddress.setText(user.getEmailAddress());
        holder.txtPhoneNumber.setText(user.getPhoneNumber());



        if(shouldInteract)
        {
            holder.lyt_Interact.setVisibility(View.VISIBLE);
            holder.btnInsert.setVisibility(View.GONE);
            holder.btnUpdate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    context.startActivity(new Intent(context, Update_Add_User.class).putExtra("User",user).putExtra("isUpdatetoSqlite",true));

                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    confirmDelete(user,position);
                }
            });
        }
        else
        {
            holder.lyt_Interact.setVisibility(View.GONE);
            holder.btnInsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v)
                {
                    Database database=new Database(context);
                    if(database.insertUser(user))
                    {
                        Toast.makeText(context,"User Inserted",Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(context,"Failed to Insert User",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

    }

    private void confirmDelete(User user,int pos)
    {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        alertDialog.setTitle("Delete ?");
        alertDialog.setMessage("Are you Sure to Delete this User ? ");
        alertDialog.setNegativeButton("No",null);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Database database=new Database( context);
                if(database.deleteUser(user))
                {
                    userList.remove(pos);
                    notifyDataSetChanged();

                }
                else
                {
                    Toast.makeText(context,"Failed to Delete",Toast.LENGTH_LONG).show();
                }


            }
        });

        alertDialog.show();

    }

    @Override
    public int getItemCount()
    {
        return userList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView txtUserId;
        TextView txtFName;
        TextView txtLName;
        TextView txtPhoneNumber;
        TextView txtEmailAddress;
        LinearLayout lyt_Interact;
        Button btnUpdate;
        Button btnDelete;
        Button btnInsert;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            lyt_Interact=itemView.findViewById(R.id.lyt_Interact);
            txtUserId=itemView.findViewById(R.id.txtUserId);
            txtFName=itemView.findViewById(R.id.txtFName);
            txtLName=itemView.findViewById(R.id.txtLName);
            txtPhoneNumber=itemView.findViewById(R.id.txtPhoneNumber);
            txtEmailAddress=itemView.findViewById(R.id.txtEmailAddress);

            btnUpdate=itemView.findViewById(R.id.btnUpdate);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnInsert=itemView.findViewById(R.id.btnInsert);
        }
    }
}
