package com.lintend.forum.loginStuffs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.lintend.forum.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email , password;
    Button register, login;
    String url = "http://popularkoju.com.np//id1277129_lintendforum/register.php";
    RequestQueue requestQueue;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog=new ProgressDialog(this);
        dialog.setMessage("Registering user ,please wait");
        dialog.setCancelable(false);

        setContentView(R.layout.register_layout);
        name =findViewById(R.id.Rname);
        email = findViewById(R.id.Remail);
        password=findViewById(R.id.Rpassword);
        register=findViewById(R.id.Rbtnsignup);
        login=findViewById(R.id.Rbtnlogin);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }

        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj1 = new JSONObject(response);
                            //   String status = obj1.names().get(0).equals("success");

                            //boolean success=obj1.getBoolean("success");
                            if (obj1.names().get(0).equals("success")) {
                                //if(success){
                                Toast.makeText(RegisterActivity.this, obj1.getString("success"), Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
//                                Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
//                                startActivity(i);
                            } else {
                                dialog.cancel();
                                Toast.makeText(RegisterActivity.this, "Registration failed. User Exist", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Exception Caught", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> myMap = new HashMap<>();
                        myMap.put("username", name.getText().toString());
                        myMap.put("email", email.getText().toString().trim());
                        myMap.put("password", password.getText().toString().trim());
                        return myMap;

                    }


                };

                requestQueue.add(sr);
            }



        });
    }
}

