package com.lintend.forum.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.lintend.forum.adapter.HomeTabAdapter;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class HomeTabActivity extends Fragment {

// search widget
    SearchView searchView;


    FloatingActionButton floatingActionButton;
    ImageView searchV,seach2;
    EditText editTextSerch;

    RecyclerView recyclerView;
//    Button like, answer;
    ProgressDialog progressDialog;

    //
    Calendar calendar= Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
    String strtime =  mdformat.format(calendar.getTime());

    String currentdate = DateFormat.getDateInstance( DateFormat.MEDIUM).format(calendar.getTime());
    String currrentdateTime = currentdate + " "+"at" +" " + strtime;

    String url = "http://popularkoju.com.np/id1277129_lintendforum/question_display.php";
    String url1 = "http://popularkoju.com.np/id1277129_lintendforum/question_entry.php";
    RequestQueue requestQueue;
    /*ListView list;
    HomeTabAdapter adapter;*/
    List<DataModule> mydata = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.hometab_layout, container, false);

        searchView = v.findViewById(R.id.searchview); // search view
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//spinner



        /*--------------------------------------------------------------------------searchView--------------------------------------------------------------------------*/
     /*   searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                new HomeTabAdapter(getContext(), mydata).getFilter().filter(s);
                return false;
            }
        });*/


//        like = v.findViewById(R.id.btnlike);
//        progressBar = v.findViewById(R.id.cardProgressDialog);
//        answer = v.findViewById(R.id.btncomment);
      //  searchV = v.findViewById(R.id.serchview);
        //seach2 = v.findViewById(R.id.serchview2);
        //editTextSerch= v. findViewById(R.id.edtsearch);
        floatingActionButton = v.findViewById(R.id.fab);
     /*   searchV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSerch.setVisibility(View.VISIBLE);
                seach2.setVisibility(View.VISIBLE);
                searchV.setVisibility(View.GONE);
            }
        });

        seach2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextSerch.setVisibility(View.GONE);
                seach2.setVisibility(View.GONE);
                searchV.setVisibility(View.VISIBLE);

            }
        });*/






 /* *************************************floatingActionButton*********************************************** */
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                View vi = LayoutInflater.from(getContext()).inflate(R.layout.post_tab_layout, null);

                final Button post = vi.findViewById(R.id.btnpost);
                final EditText question = vi.findViewById(R.id.questionType);

                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please Wait, Posting your question");
                final SessionManager sessionManager = new SessionManager(getActivity());
                final HashMap<String, String> map = sessionManager.getUserDetails();

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setView(vi);
                final AlertDialog dialog = alert.create();
                dialog.show();



                    post.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (question.getText().toString().trim().isEmpty()) {
                                question.setError("Question field cannot be empty ");
                            } else {
                                progressDialog.show();

                                requestQueue = Volley.newRequestQueue(getContext());
                                StringRequest sr = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject obj1 = new JSONObject(response);

                                            if (obj1.names().get(0).equals("success")) {
                                                progressDialog.dismiss();
                                                dialog.dismiss();

                                               /* Snackbar.make(getView(), "Question Posted Successfully", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();*/
                                                Toast toast = new Toast(getContext());
                                                toast.setGravity(Gravity.CENTER, 0, -150);
                                                toast.setDuration(Toast.LENGTH_SHORT);
                                                View converview = LayoutInflater.from(getContext()).inflate(R.layout.custom_toast_post_success, null);
                                                toast.setView(converview);
                                                toast.show();




                                            } else {
                                                progressDialog.cancel();
                                                dialog.dismiss();
                                                Snackbar.make(getView(), "Question Post Failed", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }
                                        } catch (Exception e) {
                                            progressDialog.dismiss();
                                            dialog.dismiss();
                                            Toast.makeText(getContext(), "Exception Caught", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Snackbar.make(getView(), "No Internet Connection", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> myMap = new HashMap<>();
                                        myMap.put("question_title", question.getText().toString());
                                        myMap.put("email", map.get(sessionManager.KEY_EMAIL));
                                        myMap.put("date_time", currrentdateTime);
                                        return myMap;

                                    }


                                };

                                requestQueue.add(sr);

                            }
                        }

                    });


                }

        });


        return v;


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                   // Toast.makeText(getContext(), "Refreshed ", Toast.LENGTH_LONG).show();



                          recyclerView.setAdapter(new HomeTabAdapter(getContext(), mydata));



                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
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


   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.ic_search){
            Toast.makeText(getContext(), "searching", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }*/




}