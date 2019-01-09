package com.lintend.forum.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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

public class QuestionAnswerDisplay extends AppCompatActivity {

   // TextView uname, time1, question1; // question



    String id,idd;
    String userEmail;

    SessionManager sm;
    ProgressDialog progressDialog;
    EditText edtAnswer;
    ImageView btnAnswerSend;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    List<DataModule> mydata = new ArrayList<>();
    String url = "http://popularkoju.com.np/id1277129_lintendforum/answerdisplay.php";
    String answerUrl =  "http://popularkoju.com.np/id1277129_lintendforum/answer_entry.php";

    Calendar calendar= Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
    String strtime =  mdformat.format(calendar.getTime());
    String currentdate = DateFormat.getDateInstance( DateFormat.MEDIUM).format(calendar.getTime());
    String currrentdateTime = currentdate + " "+"at" +" " + strtime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer_display);
       // text = findViewById(R.id.tttt);

        edtAnswer = findViewById(R.id.answer_send);
        btnAnswerSend = findViewById(R.id.btnSendAnswer);

        recyclerView = findViewById(R.id.answer_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


      //  sm = new SessionManager(this);
        id= getIntent().getStringExtra("id"); // question id
        idd= getIntent().getStringExtra("id"); // question id

        progressDialog = new ProgressDialog(QuestionAnswerDisplay.this);
        progressDialog.setMessage("Please Wait, Posting your answer");
        sm = new SessionManager(getApplicationContext());
        HashMap<String, String> map = sm.getUserDetails();
        userEmail = map.get(SessionManager.KEY_EMAIL);

   // Answer Sending part Start -----------------------------------------------------------
   btnAnswerSend.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           if (edtAnswer.getText().toString().isEmpty()) {
               edtAnswer.setError("Answer Field cannot be Empty");
           } else {
               progressDialog.show();
               requestQueue = Volley.newRequestQueue(QuestionAnswerDisplay.this);
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
                               View converview = LayoutInflater.from(QuestionAnswerDisplay.this).inflate(R.layout.custom_toast_post_success, null);
                               toast.setView(converview);
                               toast.show();
                               edtAnswer.setText("");


                           } else {
                               progressDialog.dismiss();

                               Toast.makeText(QuestionAnswerDisplay.this, "Failed, Something went wrong", Toast.LENGTH_LONG).show();
                           }
                       } catch (JSONException e) {
                           progressDialog.dismiss();

                           e.printStackTrace();
                           Toast.makeText(QuestionAnswerDisplay.this, "Exception Caught", Toast.LENGTH_SHORT).show();
                       }

                   }
               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Toast.makeText(QuestionAnswerDisplay.this, "No internet connection", Toast.LENGTH_SHORT).show();

                   }
               }) {
                   @Override
                   protected Map<String, String> getParams() throws AuthFailureError {
                       Map<String, String> mymap = new HashMap<>();
                       mymap.put("answer", edtAnswer.getText().toString());
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

   // Sendind Ends -------------------------------------------------------------------------------



// Data Display Starts------------------------------------------------------------------------------------------------------------------

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

                        m.setToAdapter_qid(idd); // question id from intent
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




