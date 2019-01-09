package com.lintend.forum.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import com.lintend.forum.DataModule;
import com.lintend.forum.R;
import com.lintend.forum.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPostAnswerDisplayAdapter extends RecyclerView.Adapter<MyPostAnswerDisplayAdapter.MyViewHolder> {
    RequestQueue requestQueue;
    SessionManager sm;

    String ans_id;
    String url = "http://popularkoju.com.np/id1277129_lintendforum/voting_task.php";
    Context c;
     List<DataModule> list;
    public MyPostAnswerDisplayAdapter(Context applicationContext, List<DataModule> mydata) {
        c= applicationContext;
        list= mydata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(c).inflate(R.layout.mypost_to_answer_display_layout,null);
        return new MyPostAnswerDisplayAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPostAnswerDisplayAdapter.MyViewHolder myViewHolder, final int i) {
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






        //counter
        /***************************************************Counter up*************************************************        */
        myViewHolder.counter_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String ans_id_vote = list.get(i).getAnswer_id();
               // Toast.makeText(c, ans_id, Toast.LENGTH_SHORT).show();

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
                                Toast toast = new Toast(c);
                                toast.setGravity(Gravity.CENTER, 0, -150);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                View converview = LayoutInflater.from(c).inflate(R.layout.custom_toast_vote_up, null);
                                toast.setView(converview);
                                toast.show();
                            }else{
                                Toast.makeText(c, "vote failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(c, "Exception Caught", Toast.LENGTH_SHORT).show();
                            Toast toast = new Toast(c);
                            toast.setGravity(Gravity.CENTER, 0, -150);
                            toast.setDuration(Toast.LENGTH_SHORT);
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
                        mymap.put("answer_id",ans_id_vote);
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
                                    Toast toast = new Toast(c);
                                    toast.setGravity(Gravity.CENTER, 0, -150);
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    View converview = LayoutInflater.from(c).inflate(R.layout.custom_toast_vote_up, null);
                                    toast.setView(converview);
                                    toast.show();
                                }else{
                                    Toast.makeText(c, "vote failed", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                               // Toast.makeText(c, "Exception Caught", Toast.LENGTH_SHORT).show();

                                Toast toast = new Toast(c);
                                toast.setGravity(Gravity.CENTER, 0, -150);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                View converview = LayoutInflater.from(c).inflate(R.layout.custom_toast_vote_done, null);
                                toast.setView(converview);
                                toast.show();                           }


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
                            mymap.put("email", userEmail);
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
      //  TextView username, time1, question;
        TextView ans_username, time2, answer;
        CardView cardView;
        ImageView counter_up, counter_down; //counter
        TextView counter;
        View convert;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            /*username = itemView.findViewById(R.id.username);
            time1 = itemView.findViewById(R.id.time);
            question = itemView.findViewById(R.id.question);*/
            cardView =itemView.findViewById(R.id.cardView);
            //answer part
            ans_username = itemView.findViewById(R.id.username_answer);
            time2=itemView.findViewById(R.id.time_answer);
            answer=itemView.findViewById(R.id.answerT);

            //counter
            counter= itemView.findViewById(R.id.counter);
            counter_up= itemView.findViewById(R.id.arrowup);
            counter_down= itemView.findViewById(R.id.arrowdown);
            convert = itemView;
        }
    }


}





