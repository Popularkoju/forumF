package com.lintend.forum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lintend.forum.Activity.MyPostAnswerDisplayActivity;
import com.lintend.forum.DataModule;
import com.lintend.forum.R;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder> {
    String idd;

    Context c;
    List<DataModule> m;
    public MyPostAdapter(FragmentActivity activity, List<DataModule> mydata) {
        c= activity;
        m = mydata;

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
    public void onBindViewHolder(@NonNull final MyPostAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.uname.setText(m.get(i).getName());
        myViewHolder.questionAsked.setText(m.get(i).getQuestion());
        myViewHolder.time.setText(m.get(i).getTime());
       // myViewHolder.qid.setText(m.get(i).getPost_qid());




        myViewHolder.btnseeAns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                idd = m.get(myViewHolder.getAdapterPosition()).getPost_qid();
                Intent i = new Intent(c, MyPostAnswerDisplayActivity.class);
                i.putExtra("aid", idd);
                Toast.makeText(c, idd, Toast.LENGTH_SHORT).show();
                c.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return m.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnseeAns ;
        CardView cardView;
        TextView uname,  time , questionAsked, qid;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            btnseeAns = itemView.findViewById(R.id.btnViewanswer);
            uname = itemView.findViewById(R.id.myPostusername);
            questionAsked = itemView.findViewById(R.id.questionT);
           // qid=itemView.findViewById(R.id.qid);
            cardView = itemView.findViewById(R.id.cardView_post);
        }
    }
}
