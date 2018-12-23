package com.lintend.forum.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lintend.forum.DataModule;
import com.lintend.forum.R;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder> {

    Context c;
    List<DataModule> m;
    public MyPostAdapter(FragmentActivity activity, List<DataModule> mydata) {
        c= activity;
        m = (List<DataModule>) mydata;

    }

    @NonNull
    @Override
    public MyPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.mypost_tab_list, null);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.uname.setText(m.get(i).getName());
        myViewHolder.questionAsked.setText(m.get(i).getQuestion());
        myViewHolder.time.setText(m.get(i).getTime());



    }

    @Override
    public int getItemCount() {
        return m.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnseeAns ;
        TextView uname,  time , questionAsked;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            btnseeAns = itemView.findViewById(R.id.btnViewanswer);
            uname = itemView.findViewById(R.id.myPostusername);
            questionAsked = itemView.findViewById(R.id.questionT);
        }
    }
}
