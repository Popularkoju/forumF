package com.lintend.forum.Activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.lintend.forum.DataModule;
import com.lintend.forum.R;
import com.lintend.forum.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AboutTabActivity extends Fragment {
    SessionManager sm;
    RequestQueue requestQueue;
    private static final int INT_CONST_CAM = 5, SELECT_FILE = 0;
    int permissionCheck;
    Uri dataUri;
    ProgressDialog dialog;
    Bitmap bitmap;

    String url = "http://popularkoju.com.np/id1277129_lintendforum/account_details.php";
    String  imageUploadUrl= "http://popularkoju.com.np/id1277129_lintendforum/images_upload.php";
    Button logout, btnchooseImage, btnuploadImage;
    ImageView userImage;
    TextView name, message;
    TextView email;

    Context applicationContext = HomeActivity.getContextOfAppliction();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_tab_layout, null);



        logout = v.findViewById(R.id.logout);
        name = v.findViewById(R.id.accountName);
        email = v.findViewById(R.id.accountEmail);
        btnchooseImage = v.findViewById(R.id.chooseImage);
        btnuploadImage = v.findViewById(R.id.uploadImage);
        userImage = v.findViewById(R.id.userImage);

        message = v.findViewById(R.id.messageHere);

        sm = new SessionManager(getActivity());
        HashMap<String, String> map = sm.getUserDetails();
        final String userEmail = map.get(SessionManager.KEY_EMAIL);

        email.setText(userEmail);

        // choose image -------------------------------------------------------------------
        btnchooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
                }
                selectImage();
                }
        });


        //image upload  --------------------------------------------------------------------------------
        btnuploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataUri!=null){
                    dialog=new ProgressDialog(getContext());
                    dialog.setMessage("UPLOADING...");
                   // dialog.setCancelable(false);
                    //dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.show();

                     requestQueue = Volley.newRequestQueue(getContext());
                     StringRequest stringRequest = new StringRequest(Request.Method.POST, imageUploadUrl, new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             try {
                                 JSONObject object =  new JSONObject(response);
                                  if(object.names().get(0).equals("success")){
                                      dialog.dismiss();
                                      Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                  }else{
                                      dialog.dismiss();
                                      Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                                  }
                             } catch (JSONException e) {
                                 e.printStackTrace();
                                    dialog.dismiss();
                                 Toast.makeText(getContext(), "Exception Caught", Toast.LENGTH_SHORT).show();
                             }

                         }
                     }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {
                             dialog.dismiss();
                             Toast.makeText(getContext(), "No internet", Toast.LENGTH_SHORT).show();
                         }
                     }){
                         @Override
                         protected Map<String, String> getParams() throws AuthFailureError {
                             Map<String, String > mymap = new HashMap<>();
                             mymap.put("email",userEmail);
                             mymap.put("url", "hkhk");
                             mymap.put("name","jjjjj");
                             return mymap;
                         }
                     };
                     requestQueue.add(stringRequest);


                }
            }
        });

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
                    String images = (obj1.getString("image"));
                    String  imgURL = "http://popularkoju.com.np/id1277129_lintendforum/";
                    Glide.with(getContext())
                            .load(imgURL+images)
                            .into(userImage);





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

    public void selectImage() {
//        final CharSequence[] items = {"Camera", "Gallery"};
//        final AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
//        builder.setTitle("Select Image");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (items[which].equals("Camera")){
//                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(i,INT_CONST_CAM);
//
//                }else if(items[which].equals("Gallery")){
//                         Intent i = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
////              */* for all types of data , image/* all types of images
//                        i.setType("image/*");
//                        startActivityForResult(i.createChooser(i,"Select File"), SELECT_FILE); // create field int_ cons
//
//                }
//              //  builder.setNegativeButton("Cancel", null);
//                builder.show();
//            }
//        });





        Intent i = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
    //            */* for all types of data , image/* all types of images
        i.setType("image/*");
        startActivityForResult(i, INT_CONST_CAM); // create field int_ cons
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     /*   if (resultCode == RESULT_OK) {
            if((requestCode == INT_CONST_CAM )){
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                userImage.setImageBitmap(bmp);

            }else if ((requestCode == SELECT_FILE )) {
                dataUri = data.getData(); // data here is from Intent data above(parameter)
                userImage.setImageURI(dataUri);
            }*/
        if(requestCode == INT_CONST_CAM && resultCode==RESULT_OK ){
            dataUri= data.getData(); // data here is from Intent data above(parameter)
            try {
                bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), dataUri);

                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


//           userImage.setImageURI(dataUri);



        }

        }


    }








