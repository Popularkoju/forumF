package com.lintend.forum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lintend.forum.Activity.QuestionAnswerDisplay;
import com.lintend.forum.Activity.Report;
import com.lintend.forum.DataModule;
import com.lintend.forum.R;
import com.lintend.forum.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionAnswerActivityAdapter extends RecyclerView.Adapter<QuestionAnswerActivityAdapter.MyViewHolder> {

    RequestQueue requestQueue;
    String url = "http://popularkoju.com.np/id1277129_lintendforum/voting_task.php";
   // String reportURL = "http://popularkoju.com.np/id1277129_lintendforum/report.php";
        SessionManager sm;


    String ans_id, answer_ids;



    Context c;
    List<DataModule> list;
    public QuestionAnswerActivityAdapter(Context mainActivity, List<DataModule> mydata) {
        c = mainActivity;
        list = mydata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(c).inflate(R.layout.answer_display_layout,null);



        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final int ii = i;
        myViewHolder.ans_username.setText(list.get(i).getName());
        myViewHolder.time2.setText(list.get(i).getTime());
        myViewHolder.answer.setText(list.get(i).getAnswers());
        myViewHolder.counter.setText(list.get(i).getVote_count());
          ans_id = list.get(i).getAnswer_id();




        String vi= list.get(i).getVote_count();
        final int[] count = {Integer.parseInt(vi)};
        sm = new SessionManager(c);
        HashMap<String, String> map = sm.getUserDetails();
        final String userEmail = map.get(SessionManager.KEY_EMAIL);


        myViewHolder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer_ids = list.get(i).getAnswer_id();
                String from_adapter =list.get(i).getToAdapter_qid(); // question answer form  adapter
                //Toast.makeText(c, answer_ids, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(c, Report.class);
                i.putExtra("answer_id",answer_ids);
               i.putExtra("question_id", from_adapter);
                c.startActivity(i);
            }
        });







        //counter
        /***************************************************Counter up*************************************************        */
        myViewHolder.counter_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               count[0]++;
               myViewHolder.counter.setText(String.valueOf(count[0]));

               final String vl= myViewHolder.counter.getText().toString();


               requestQueue = Volley.newRequestQueue(c);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj1 =  new JSONObject(response);

                            if(obj1.names().get(1).equals("success")){
                               // Toast.makeText(c, "Vote up", Toast.LENGTH_SHORT).show();

                                    Toast toast = new Toast(c);
                                    toast.setGravity(Gravity.CENTER, 0, -150);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    //LayoutInflater inflater = getLayoutInflater();
                                    View converview = LayoutInflater.from(c).inflate(R.layout.custom_toast_vote_up, null);
                                    toast.setView(converview);
                                    toast.show();

                            }else{
                               Toast.makeText(c, "You have Already Voted", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(c, "You have already voted", Toast.LENGTH_SHORT).show();
                            Toast toast = new Toast(c);
                            toast.setGravity(Gravity.CENTER, 0, -150);
                            toast.setDuration(Toast.LENGTH_LONG);
                            //LayoutInflater inflater = getLayoutInflater();
                            View converview = LayoutInflater.from(c).inflate(R.layout.custom_toast_vote_done, null);
                            toast.setView(converview);
                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c, "No internet connectivity", Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> mymap = new HashMap<>();
                        mymap.put("answer_id",ans_id);
                        mymap.put("vote_count",vl);
                        mymap.put("email", userEmail);
                        return  mymap;
                    }


                };
                requestQueue.add(stringRequest);
                }
        });
/***************************************************Counter down*************************************************        */
        myViewHolder.counter_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count[0] == 0){
                    count[0] = 0;

                }else {
                    count[0]--;

                    myViewHolder.counter.setText(String.valueOf(count[0]));
                    final String vld= myViewHolder.counter.getText().toString();


                    requestQueue = Volley.newRequestQueue(c);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj1 =  new JSONObject(response);

                                if(obj1.names().get(1).equals("success")){
                                   // Toast.makeText(c, "Vote down", Toast.LENGTH_SHORT).show();
                                    Toast toast = new Toast(c);
                                    toast.setGravity(Gravity.CENTER, 0, -150);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    //LayoutInflater inflater = getLayoutInflater();
                                    View converview = LayoutInflater.from(c).inflate(R.layout.custom_toast_vote_up, null);
                                    toast.setView(converview);
                                    toast.show();

                                }else{
                                    Toast.makeText(c, "You have already voted", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Toast.makeText(c, "You have already voted", Toast.LENGTH_SHORT).show();
                                Toast toast = new Toast(c);
                                toast.setGravity(Gravity.CENTER, 0, -150);
                                toast.setDuration(Toast.LENGTH_LONG);
                                //LayoutInflater inflater = getLayoutInflater();
                                View converview = LayoutInflater.from(c).inflate(R.layout.custom_toast_vote_done, null);
                                toast.setView(converview);
                                toast.show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(c, "No internet connectivity", Toast.LENGTH_SHORT).show();

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> mymap = new HashMap<>();
                            mymap.put("answer_id",ans_id);
                            mymap.put("vote_count",vld);
                            mymap.put("email",userEmail );
                            return  mymap;
                        }
                    };
                    requestQueue.add(stringRequest);
                }


            }
        });

    }

 /**********************************************************************************finish ****************************************/


    @Override
    public int getItemCount() {
        return list.size();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username, time1, question;
        TextView ans_username, time2, answer, report;

        ImageView counter_up, counter_down; //counter
        TextView counter;
        View convert;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            /*username = itemView.findViewById(R.id.username);
            time1 = itemView.findViewById(R.id.time);
            question = itemView.findViewById(R.id.question);*/

            //answer part
            ans_username = itemView.findViewById(R.id.username_answer);
            time2=itemView.findViewById(R.id.time_answer);
            answer=itemView.findViewById(R.id.answerT);
            report = itemView.findViewById(R.id.report);

            //counter
            counter= itemView.findViewById(R.id.counter);
            counter_up= itemView.findViewById(R.id.arrowup);
            counter_down= itemView.findViewById(R.id.arrowdown);
            convert = itemView;

        }

        }

    }




