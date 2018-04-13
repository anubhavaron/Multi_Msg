package com.example.tusharsk.multi_msg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pc on 4/14/2018.
 */

public class contacts_list_dapter extends RecyclerView.Adapter<contacts_list_dapter.NUMBERVIEWHOLDER>{


    ArrayList<String> contact_name;
    ArrayList<String> contact_number;

    private Context mcontext;
    public contacts_list_dapter(Context context)
    {

        this.mcontext=context;



    }




    @Override
    public contacts_list_dapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_contacts,parent,false);
        return new contacts_list_dapter.NUMBERVIEWHOLDER(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(contacts_list_dapter.NUMBERVIEWHOLDER holder, int position) {
        holder.textView.setText(contact_name.get(position)+" "+contact_number.get(position));
    }

    @Override
    public int getItemCount() {

        if(contact_name==null)
            return 0;
        else
            return contact_name.size();



    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder
    {
        TextView textView;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            textView=(TextView)view.findViewById(R.id.text_view_8);

        }



    }
    public void swapCursor(ArrayList<String> contact_name,ArrayList<String> contact_number) {
        // Always close the previous mCursor first

        if (contact_name.size() != 0) {
            // Force the RecyclerView to refresh
            this.contact_name=contact_name;
            this.contact_number=contact_number;
            this.notifyDataSetChanged();
        }
    }



}