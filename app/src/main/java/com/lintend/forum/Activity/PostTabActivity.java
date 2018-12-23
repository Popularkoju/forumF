package com.lintend.forum.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.lintend.forum.SessionManager;
import com.lintend.forum.loginStuffs.LoginActivity;
import com.lintend.forum.loginStuffs.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostTabActivity extends Fragment {
    Button post;
    EditText question;
    RequestQueue requestQueue;
    ProgressDialog dialog;
    String url = "http://popularkoju.com.np/id1277129_lintendforum/question_entry.php";
    Calendar calendar= Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
    String strtime =  mdformat.format(calendar.getTime());

     String currentdate = DateFormat.getDateInstance( DateFormat.MEDIUM).format(calendar.getTime());
     String currrentdateTime = currentdate + " "+"at" +" " + strtime;




   // String userEmail = map.get(SessionManager.KEY_EMAIL);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.post_tab_layout, null);
        post = v.findViewById(R.id.btnpost);
        question = v.findViewById(R.id.questionType);

        dialog =new ProgressDialog(getContext());
        dialog.setMessage("Please Wait, Posting your question");
        final SessionManager sessionManager = new SessionManager(getActivity());
        final HashMap<String, String> map= sessionManager.getUserDetails();



        post.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                dialog.show();

                requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
                StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj1 = new JSONObject(response);

                            if (obj1.names().get(0).equals("success")) {
                                dialog.dismiss();

                                Toast.makeText(getContext(), "post successful", Toast.LENGTH_SHORT).show();



                            } else {
                                dialog.cancel();
                                Toast.makeText(getContext(), "post failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Exception Caught", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> myMap = new HashMap<>();
                        myMap.put("question_title", question.getText().toString());
                        myMap.put("email",map.get(sessionManager.KEY_EMAIL));
                        myMap.put("date_time", currrentdateTime);
                        return myMap;

                    }




                };

                requestQueue.add(sr);
            }





    });

return  v ;

}




}
