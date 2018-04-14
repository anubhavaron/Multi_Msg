package com.example.tusharsk.multi_msg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pc on 4/14/2018.
 */

public class History_adapter extends RecyclerView.Adapter<History_adapter.NUMBERVIEWHOLDER>{


    ArrayList<String> date=new ArrayList<String>();
    ArrayList<String> time=new ArrayList<String>();;
    ArrayList<String> msg=new ArrayList<String>();;


    private Context mcontext;
    public History_adapter(Context context)
    {

        this.mcontext=context;



    }




    @Override
    public History_adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_history,parent,false);
        return new History_adapter.NUMBERVIEWHOLDER(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(History_adapter.NUMBERVIEWHOLDER holder, int position) {
        holder.date.setText(time.get(position));
        holder.time.setText(date.get(position));
        holder.msg.setText(msg.get(position));
    }

    @Override
    public int getItemCount() {

        if(msg.size()==0)
            return 0;
        else
            return msg.size();



    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder
    {
        TextView date;
        TextView time;
        TextView msg;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            date=(TextView)view.findViewById(R.id.date_10);
            time=(TextView)view.findViewById(R.id.time_10);
            msg=(TextView)view.findViewById(R.id.msg_10);

        }



    }
    public void swapCursor(ArrayList<String> date,ArrayList<String> time,ArrayList<String> msg) {
        // Always close the previous mCursor first

        if (date.size() != 0) {
            // Force the RecyclerView to refresh
            this.date=date;
            this.time=time;
            this.msg=msg;
            this.notifyDataSetChanged();
        }
    }



}