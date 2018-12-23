package com.lintend.forum.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lintend.forum.DataModule;
import com.lintend.forum.R;
import com.lintend.forum.adapter.HomeTabAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeTabActivity extends Fragment {


    RecyclerView recyclerView;
//    Button like, answer;
    ProgressBar progressBar;

    String url = "http://popularkoju.com.np/id1277129_lintendforum/question_display.php";
    RequestQueue requestQueue;
    ListView list;
    List<DataModule> mydata = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.hometab_layout, container, false);
//        like = v.findViewById(R.id.btnlike);
//        progressBar = v.findViewById(R.id.cardProgressDialog);
//        answer = v.findViewById(R.id.btncomment);


        return v;


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.home_tab_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));





        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array1 = new JSONArray(response);

                    for (int i = 0; i < array1.length(); i++) {


                        JSONObject obj1 = array1.getJSONObject(i);
                        DataModule m = new DataModule();
                        m.setName(obj1.getString("name"));
                        m.setQuestion(obj1.getString("question"));
                        m.setTime(obj1.getString("date_time"));
                        m.setId(obj1.getString("id"));
                        mydata.add(m);

                    }
                    Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_LONG).show();
                    recyclerView.setAdapter(new HomeTabAdapter(getContext(), mydata));


                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(stringRequest);



    }





}