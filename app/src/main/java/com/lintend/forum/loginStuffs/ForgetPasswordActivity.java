package com.lintend.forum.loginStuffs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
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
import com.lintend.forum.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.lintend.forum.R.layout.reset_password;

public class ForgetPasswordActivity extends AppCompatActivity {

    TextView back;
    EditText email;
    FloatingActionButton next;
    RequestQueue requestQueue;
    String getEmail;
    EditText newPw, confirmPw;
    Button reset;
    AlertDialog dialogs;

    ProgressDialog progressDialog;
    String forgetPasswordURL = "http://popularkoju.com.np/id1277129_lintendforum/forget_pw_email_check.php";
    String pwResetURL = "http://popularkoju.com.np/id1277129_lintendforum/password_reset.php";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword_layout);

        back = findViewById(R.id.back);
        email=findViewById(R.id.email);
        next=findViewById(R.id.next);

        getEmail= email.getText().toString().trim();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ... ");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });



    /* --------------------------------------------NEXT BUTTON----------------------------------------------*/
    next.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (email.getText().toString().isEmpty()) {
                email.setError("Email cannot be empty");
                } else {
                progressDialog.show();

                requestQueue = Volley.newRequestQueue(ForgetPasswordActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, forgetPasswordURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.names().get(0).equals("match")) {
                                progressDialog.dismiss();
                                alertDisplay();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordActivity.this, "No user exist with provided email", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Exception caught", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgetPasswordActivity.this, "No internet ", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("email", email.getText().toString());
                        return map;
                    }
                };
                requestQueue.add(stringRequest);

            }
        }

    });

    }


    public void alertDisplay() {
        View v = LayoutInflater.from(this).inflate(R.layout.reset_password, null);
        newPw = v.findViewById(R.id.newPassword);
        confirmPw = v.findViewById(R.id.confirmPassword);
        reset = v.findViewById(R.id.btnreset_password);

        AlertDialog.Builder builders = new AlertDialog.Builder(ForgetPasswordActivity.this);
        builders.setView(v);
        dialogs = builders.create();
        dialogs.show();


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(newPw.getText().toString().isEmpty() || confirmPw.getText().toString().isEmpty()){
                    newPw.setError("Field cannot be empty");
                    confirmPw.setError("Field cannot be empty");
                }else if (newPw.getText().toString().equals(confirmPw.getText().toString())) {

                    progressDialog.show();
                    requestQueue = Volley.newRequestQueue(ForgetPasswordActivity.this);
                    StringRequest sr = new StringRequest(Request.Method.POST, pwResetURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject object = new JSONObject(response);

                                if (object.names().get(0).equals("success")) {
                                    progressDialog.dismiss();
                                    dialogs.dismiss();
                                    Toast.makeText(ForgetPasswordActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    }else{
                                    progressDialog.dismiss();
                                    dialogs.dismiss();
                                    Toast.makeText(ForgetPasswordActivity.this, "Failed! Try again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map =new HashMap<>();
                            map.put("email", getEmail);
                            map.put("newpassword", confirmPw.getText().toString().trim());
                            return  map;
                        }
                    };
                    requestQueue.add(sr);

                }else{
                    newPw.setError("Password do not match");
                    confirmPw.setError("Password do not match");
                }

            }

        });


    }
}
