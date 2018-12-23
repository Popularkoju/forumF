package com.lintend.forum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.lintend.forum.DataModule;
import com.lintend.forum.R;
import com.lintend.forum.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeTabAdapter extends RecyclerView.Adapter<HomeTabAdapter.MyViewHolder> {

    String like = "1234";
    RequestQueue requestQueue;
    String url = "http://popularkoju.com.np/id1277129_lintendforum/answer_entry.php";

    Calendar calendar= Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
    String strtime =  mdformat.format(calendar.getTime());

    String currentdate = DateFormat.getDateInstance( DateFormat.MEDIUM).format(calendar.getTime());
    String currrentdateTime = currentdate + " "+"at" +" " + strtime;


    SessionManager sm;






   private   Context c;
    List<DataModule> mydata;



     public HomeTabAdapter(Context c, List<DataModule> mydata){
        this.c =c;
        this.mydata = mydata;
    }



    @NonNull
    @Override
    public HomeTabAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.home_tab_list_item, null);


        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;



    }

    @Override
    public void onBindViewHolder(@NonNull final HomeTabAdapter.MyViewHolder myViewHolder, int i) {
       final DataModule dm = mydata.get(i);

        myViewHolder.like_count.setText(like);

        myViewHolder.name.setText(mydata.get(i).getName());
        myViewHolder.time.setText(mydata.get(i).getTime());
        myViewHolder.question.setText(mydata.get(i).getQuestion());
        final String id = mydata.get(i).getId();






//posting answer from alert dialog
        myViewHolder.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(c).inflate(R.layout.add_postanswer_layout, null);
               final EditText answer = view.findViewById(R.id.enteranswer);
               final  Button post= view.findViewById(R.id.btnpost);
                AlertDialog.Builder alert =new AlertDialog.Builder(c);
                alert.setView(view);
                final AlertDialog dialog =alert.create();
                dialog.show();

                sm =new SessionManager(c);
                HashMap<String, String> map = sm.getUserDetails();
                final String userEmail = map.get(SessionManager.KEY_EMAIL);

                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestQueue= Volley.newRequestQueue(c);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj1 = new JSONObject(response);
                                    if(obj1.names().get(0).equals("success")){
                                        Toast.makeText(c, "Answer posted Successfully",Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }else {
                                        Toast.makeText(c, "Failed, Something went wrong",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(c, "Exception Caught", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(c, "No internet connection", Toast.LENGTH_SHORT).show();

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String , String> mymap = new HashMap<>();
                                mymap.put("answer",answer.getText().toString());
                                mymap.put("id",id);
                                mymap.put("email",userEmail);
                                mymap.put("answer_date_time", currrentdateTime);
                                return  mymap;

                            }
                        };
                        requestQueue.add(stringRequest);
                    }
                });

                }
        });
        // end of posting

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, QuestionAnswerDisplay.class);
               /* i.putExtra("username",dm.getName());
                i.putExtra("time", dm.getTime());
                i.putExtra("question",dm.getQuestion());*/
                i.putExtra("id",id);
                c.startActivity(i);


            }
        });

    }

    @Override
    public int getItemCount() {
        return mydata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  name, question, time;
        TextView like, answer;
        TextView like_count;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            time =itemView.findViewById(R.id.time);
            question = itemView.findViewById(R.id.questionTitle);
            answer = itemView.findViewById(R.id.btncomment);
            cardView = itemView.findViewById(R.id.cardView);
            like_count = itemView.findViewById(R.id.like_counter);






        }




        }


        }



