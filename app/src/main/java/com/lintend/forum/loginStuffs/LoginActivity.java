package com.lintend.forum.loginStuffs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lintend.forum.Activity.HomeActivity;
import com.lintend.forum.R;
import com.lintend.forum.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    SessionManager sessionManager;

    EditText email, password;
    Button login, signup;
    RequestQueue requestQueue;
    String url ="http://popularkoju.com.np//id1277129_lintendforum/login.php";
    ProgressDialog dialog ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email =findViewById(R.id.email);
        password =findViewById(R.id.password);
        login = findViewById(R.id.btnlogin);
        signup=findViewById(R.id.btnsignup);

        dialog= new ProgressDialog(this);
        dialog.setMessage("Please wait. Loging in");
        requestQueue = Volley.newRequestQueue(this);


        sessionManager = new SessionManager(LoginActivity.this);
        if(sessionManager.isLoggedIn()){
            startActivity(new Intent(this,HomeActivity.class));
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, RegisterActivity.class) );
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("error",response);
                        try {
                            JSONObject obj1 = new JSONObject(response);

                            String emailE =email.getText().toString();


                            if (obj1.names().get(0).equals("success")) {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                //created session

                                sessionManager.createLoginSession(emailE);
//                                new
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);

                               LoginActivity.this.finish();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Exception Caught", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "No Internet Connectivity", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> mymap = new HashMap<>();

                        mymap.put("email", email.getText().toString());
                        mymap.put("password", password.getText().toString());
                        return mymap;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });




    }
}

