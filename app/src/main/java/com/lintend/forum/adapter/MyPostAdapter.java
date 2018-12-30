package com.lintend.forum.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.lintend.forum.Activity.MyPostAnswerDisplayActivity;
import com.lintend.forum.DataModule;
import com.lintend.forum.R;
import com.lintend.forum.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder> {
    String idd, iddd, original,qid; // for accessing question id from different onclick

    SessionManager sm;
    ProgressDialog progressDialog;

    String deleteUrl ="http://popularkoju.com.np/id1277129_lintendforum/delete_mypost.php";
    String updateURL = "http://popularkoju.com.np/id1277129_lintendforum/post_update.php";
    RequestQueue requestQueue;

    Context c;
    List<DataModule> m;


    public MyPostAdapter(FragmentActivity activity, List<DataModule> mydata) {
        c= activity;
        m = mydata;


    }

    @NonNull
    @Override
    public MyPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.mypost_tab_list, null);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPostAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.uname.setText(m.get(i).getName());
        myViewHolder.questionAsked.setText(m.get(i).getQuestion());
        myViewHolder.time.setText(m.get(i).getTime());
       // myViewHolder.qid.setText(m.get(i).getPost_qid());
         // id of relative question
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                idd = m.get(myViewHolder.getAdapterPosition()).getPost_qid();
                Intent i = new Intent(c, MyPostAnswerDisplayActivity.class);
                i.putExtra("aid", idd);
                //Toast.makeText(c, idd, Toast.LENGTH_SHORT).show();
                c.startActivity(i);
            }
        });

        sm = new SessionManager(c);
        HashMap<String, String> map = sm.getUserDetails();
        final String userEmail = map.get(SessionManager.KEY_EMAIL);

        //edit------------------------------------------------------------------------------------------------------------

        myViewHolder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View vi = LayoutInflater.from(c).inflate(R.layout.post_tab_layout, null);

                Button post = vi.findViewById(R.id.btnpost);
                 final EditText question = vi.findViewById(R.id.questionType);

                original = m.get(myViewHolder.getAdapterPosition()).getQuestion();
                question.setText(original);
               // final String update = question.getText().toString().trim();
                progressDialog = new ProgressDialog(c);
                progressDialog.setMessage("Please Wait, Updating your question..");

                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(c);
                alert.setView(vi);
                final android.support.v7.app.AlertDialog dialog = alert.create();
                dialog.show();

                 post.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Toast.makeText(c, "button clicked kk", Toast.LENGTH_SHORT).show();
                         progressDialog.show();
                         qid = m.get(myViewHolder.getAdapterPosition()).getPost_qid();
                         requestQueue = Volley.newRequestQueue(c);
                         StringRequest stringRequest = new StringRequest(Request.Method.POST, updateURL, new Response.Listener<String>() {
                             @Override
                             public void onResponse(String response) {
                                 try {
                                     JSONObject object = new JSONObject(response);
                                     if(object.names().get(0).equals("success")){
                                         progressDialog.dismiss();
                                         dialog.dismiss();
                                         // Toast.makeText(c, "Deleted", Toast.LENGTH_SHORT).show();
                                         Toast toast = new Toast(c);
                                         toast.setGravity(Gravity.CENTER, 0, -150);
                                         toast.setDuration(Toast.LENGTH_SHORT);
                                         //LayoutInflater inflater = getLayoutInflater();
                                         View converview = LayoutInflater.from(c).inflate(R.layout.custom_toast_success, null);
                                         toast.setView(converview);
                                         toast.show();

                                     }else{
                                         progressDialog.dismiss();
                                         dialog.dismiss();
                                         Toast.makeText(c, "Update Failed", Toast.LENGTH_SHORT).show();
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                     progressDialog.dismiss();
                                     dialog.dismiss();
                                     Toast.makeText(c, "Exception Caught", Toast.LENGTH_SHORT).show();
                                 }
                             }

                         }, new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError error) {
                                 Toast.makeText(c, " No internet", Toast.LENGTH_SHORT).show();
                             }
                         }){
                             @Override
                             protected Map<String, String> getParams() throws AuthFailureError {
                                 Map<String, String> map = new HashMap<>();
                                 map.put("id", qid);
                                 map.put("email", userEmail);
                                 map.put("question_title",question.getText().toString().trim());
                                 return map;

                             }

                         };
                         requestQueue.add(stringRequest);

                     }
                 });


            }
        });
        //edit ends
        //Delete start --------------------------------------------------------------------------------------------------
        myViewHolder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iddd = m.get(myViewHolder.getAdapterPosition()).getPost_qid();
                /*Toast.makeText(c, iddd + " " + userEmail, Toast.LENGTH_SHORT).show();*/

                progressDialog = new ProgressDialog(c);
                progressDialog.setMessage("Please Wait, Deleting...");
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(c);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to Delete?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       progressDialog.show();
                       requestQueue=Volley.newRequestQueue(c);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    if(object.names().get(0).equals("success")){
                                        progressDialog.dismiss();
                                       // Toast.makeText(c, "Deleted", Toast.LENGTH_SHORT).show();
                                        Toast toast = new Toast(c);
                                        toast.setGravity(Gravity.CENTER, 0, -150);
                                        toast.setDuration(Toast.LENGTH_SHORT);
                                        //LayoutInflater inflater = getLayoutInflater();
                                        View converview = LayoutInflater.from(c).inflate(R.layout.custom_toast_success, null);
                                        toast.setView(converview);
                                        toast.show();
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(c, "Delete Failed", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(c, "Exception caught", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(c, "No internet", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("id", iddd);
                                map.put("email", userEmail);
                                return map;

                            }
                        };
                        requestQueue.add(stringRequest);


                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();

            }


        });
        //----------delete ends-----------------------------------------------------------------------------------------------------------------



    }

    @Override
    public int getItemCount() {
        return m.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnedit, btndelete ;
        CardView cardView;
        TextView uname,  time , questionAsked, qid;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
                btnedit = itemView.findViewById(R.id.btnupdate);
                btndelete=itemView.findViewById(R.id.btnDelete);
            uname = itemView.findViewById(R.id.myPostusername);
            questionAsked = itemView.findViewById(R.id.questionT);
           // qid=itemView.findViewById(R.id.qid);
            cardView = itemView.findViewById(R.id.cardView_post);
        }
    }
}
