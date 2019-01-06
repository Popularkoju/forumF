package com.lintend.forum.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lintend.forum.DataModule;
import com.lintend.forum.SessionManager;
import com.lintend.forum.adapter.HomeTabAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class MySevice extends Service {
    List<DataModule> mydata;
    SessionManager sm;
    String url = "http://popularkoju.com.np/id1277129_lintendforum/question_display.php";


    public MySevice() {
    }

    @Override
    public void onCreate() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                        Log.i("services-log", "fetching Staus");
                        loadQuestions();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void loadQuestions() {


        RequestQueue requestQueue = Volley.newRequestQueue(MySevice.this);
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
                    // Toast.makeText(getContext(), "Refreshed ", Toast.LENGTH_LONG).show();


                    //  recyclerView.setAdapter(new HomeTabAdapter(getContext(), mydata));


                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MySevice.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
             /* new AlertDialog.Builder(getActivity())
                      .setIcon(android.R.drawable.ic_dialog_alert)
                      .setTitle("NO INTERNET")
                      .setMessage("Connect Please")
                      .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              getActivity().finish();
                          }
                      })
                      .show();
*/

            }
        });
        requestQueue.add(stringRequest);
    }
}



