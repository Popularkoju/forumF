package com.lintend.forum.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lintend.forum.R;
import com.lintend.forum.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Report extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    String report_option, ans_id, ques_id;

    Button btnReport;
    EditText reportG;
    RequestQueue requestQueue;

    SessionManager sm;
    String userEmail;

    String reportURL = "http://popularkoju.com.np/id1277129_lintendforum/report.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        radioGroup=findViewById(R.id.radioGroup);
        btnReport=findViewById(R.id.btnReport);

        reportG = findViewById(R.id.edt_report);

        sm = new SessionManager(getApplicationContext());
        HashMap<String, String> map = sm.getUserDetails();
       userEmail = map.get(SessionManager.KEY_EMAIL);

       ans_id= getIntent().getStringExtra("answer_id");
       ques_id = getIntent().getStringExtra("question_id");


        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioID = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioID);
                if(radioID == R.id.option_other){
                    if(reportG.getText().toString().trim().isEmpty()){
                        reportG.setError("Please fill the form");
                    }else {
                        report_option = reportG.getText().toString().trim();
                    }
                }else{
                    report_option = radioButton.getText().toString().trim();
                }

               // Toast.makeText(Report.this, radioButton.getText() +" "+ reportG.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                Toast.makeText(Report.this, report_option, Toast.LENGTH_SHORT).show();

                requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, reportURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.names().get(0).equals("success")){
                                reportG.setText(" ");
                                Toast.makeText(getApplicationContext(), "Report Successfully sent", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getApplicationContext(), "Report sending failed", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Exception Caught", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("email",userEmail);
                        map.put("aid",ans_id);
                        map.put("qid", ques_id);
                        map.put("report_type", report_option);
                        return map;
                    }
                };
                requestQueue.add(stringRequest);


            }
        });


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        int radioID = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioID);

        if(radioID == R.id.option_other){
            reportG.setVisibility(View.VISIBLE);
        }else{
            reportG.setVisibility(View.INVISIBLE);
        }
        }
    }

