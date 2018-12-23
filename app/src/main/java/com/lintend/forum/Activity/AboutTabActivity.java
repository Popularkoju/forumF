package com.lintend.forum.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.lintend.forum.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AboutTabActivity extends Fragment {
    SessionManager sm;
    RequestQueue requestQueue;

    String url = "http://popularkoju.com.np/id1277129_lintendforum/account_details.php";
    Button logout;
    TextView name;
    TextView email;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_tab_layout, null);

        logout = v.findViewById(R.id.logout);
        name = v.findViewById(R.id.accountName);
        email = v.findViewById(R.id.accountEmail);

        sm = new SessionManager(getActivity());
        HashMap<String, String> map = sm.getUserDetails();
        final String userEmail = map.get(SessionManager.KEY_EMAIL);

        email.setText(userEmail);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager s = new SessionManager(getActivity());
                s.logoutUser();
            }
        });

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj1 = new JSONObject(response);


                    String account_name = obj1.getString("name");
                    name.setText(account_name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> myMap = new HashMap<>();

                myMap.put("email", userEmail);
                return myMap;

            }



        };


        requestQueue.add(sr);
        return v;
    }
}






