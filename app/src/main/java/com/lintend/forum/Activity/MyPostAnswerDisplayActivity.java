package com.lintend.forum.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.lintend.forum.adapter.MyPostAnswerDisplayAdapter;
import com.lintend.forum.adapter.QuestionAnswerActivityAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPostAnswerDisplayActivity extends AppCompatActivity {



        String idd;
        ImageView btnsend;
        SessionManager sm;
        String userEmail;
        EditText answer_send;
        RecyclerView recyclerView;
        RequestQueue requestQueue;
        List<DataModule> mydata = new ArrayList<>();



    Calendar calendar= Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
    String strtime =  mdformat.format(calendar.getTime());
    String currentdate = DateFormat.getDateInstance( DateFormat.MEDIUM).format(calendar.getTime());
    String currrentdateTime = currentdate + " "+"at" +" " + strtime;

        ProgressDialog progressDialog;
        String url = "http://popularkoju.com.np/id1277129_lintendforum/answerdisplay.php";
        String answerUrl = "http://popularkoju.com.np/id1277129_lintendforum/answer_entry.php";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_my_post_display);
           // text = findViewById(R.id.tttt);
            answer_send = findViewById(R.id.answer_send);
            btnsend = findViewById(R.id.btnSendAnswer);


            recyclerView = findViewById(R.id.answer_recycleview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


            //  sm = new SessionManager(this);
            idd= getIntent().getStringExtra("aid");


            progressDialog = new ProgressDialog(MyPostAnswerDisplayActivity.this);
            progressDialog.setMessage("Please Wait, Posting your answer");
            sm = new SessionManager(getApplicationContext());
            HashMap<String, String> map = sm.getUserDetails();
            userEmail = map.get(SessionManager.KEY_EMAIL);


            final String answer_given = answer_send.getText().toString().trim();



    // Answer Sending Start ---------------------------------------------------------
            btnsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (answer_send.getText().toString().isEmpty()) {
                        answer_send.setError("Answer Field cannot be Empty");
                    } else {
                        progressDialog.show();
                        requestQueue = Volley.newRequestQueue(MyPostAnswerDisplayActivity.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, answerUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj1 = new JSONObject(response);
                                    if (obj1.names().get(0).equals("success")) {
                                        progressDialog.dismiss();

                                        //Toast.makeText(c, "Answer posted Successfully", Toast.LENGTH_LONG).show();
                                        Toast toast = new Toast(getApplicationContext());
                                        toast.setGravity(Gravity.CENTER, 0, -150);
                                        toast.setDuration(Toast.LENGTH_SHORT);
                                        View converview = LayoutInflater.from(MyPostAnswerDisplayActivity.this).inflate(R.layout.custom_toast_post_success, null);
                                        toast.setView(converview);
                                        toast.show();

                                    } else {
                                        progressDialog.dismiss();

                                        Toast.makeText(MyPostAnswerDisplayActivity.this, "Failed, Something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    progressDialog.dismiss();

                                    e.printStackTrace();
                                    Toast.makeText(MyPostAnswerDisplayActivity.this, "Exception Caught", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MyPostAnswerDisplayActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> mymap = new HashMap<>();
                                mymap.put("answer", answer_send.getText().toString());
                                mymap.put("id", idd);
                                mymap.put("email", userEmail);
                                mymap.put("answer_date_time", currrentdateTime);
                                return mymap;

                            }
                        };
                        requestQueue.add(stringRequest);
                    }


               }
            });

     // Answer sending  ends------------------------------------------------------------------


// Data Fetchin  _-____-----------------------------______________________----------------------------------------
            requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray array1 = new JSONArray(response);
                        //loop
                        for (int i = 0; i < array1.length(); i++) {
                            JSONObject obj1 = array1.getJSONObject(i);
                            DataModule m = new DataModule();
                            m.setName(obj1.getString("name"));
                            m.setTime(obj1.getString("date_time"));
                            m.setAnswers(obj1.getString("answer"));
                            m.setVote_count(obj1.getString("vote"));
                            m.setAnswer_id(obj1.getString("answer_id"));
                            mydata.add(m);


                        }
                        recyclerView.setAdapter(new MyPostAnswerDisplayAdapter(getApplicationContext(), mydata));

                    } catch (Exception e) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MyPostAnswerDisplayActivity.this, "No Connection", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> mymap = new HashMap<>();
                    mymap.put("id", idd);
                    return mymap;
                }
            };

            requestQueue.add(stringRequest);
        }





    }




