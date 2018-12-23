package com.lintend.forum.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.lintend.forum.adapter.QuestionAnswerActivityAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionAnswerDisplay extends AppCompatActivity {

    TextView uname, time1, question1; // question


SessionManager sm;
String id;

    TextView text;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    List<DataModule> mydata = new ArrayList<>();
    String url = "http://popularkoju.com.np/id1277129_lintendforum/answerdisplay.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer_display);
        text = findViewById(R.id.tttt);
        recyclerView = findViewById(R.id.answer_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


      //  sm = new SessionManager(this);
        id= getIntent().getStringExtra("id");



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
                    recyclerView.setAdapter(new QuestionAnswerActivityAdapter(getApplicationContext(), mydata));

                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> mymap = new HashMap<>();
                mymap.put("id", id);
                return mymap;
            }
        };

        requestQueue.add(stringRequest);
    }


}




