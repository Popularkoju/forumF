package com.lintend.forum.Activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.bumptech.glide.request.RequestOptions;
import com.lintend.forum.DataModule;
import com.lintend.forum.R;
import com.lintend.forum.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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

    EditText nameUpdate, contactUpdate; // profile update
    EditText newPassword,oldPassword,newPasswordConfirm; // password change

    RequestQueue requestQueue;
    private static final int INT_CONST_CAM = 5;
    int permissionCheck;
    Uri dataUri; // image
    ProgressDialog dialog;
    Bitmap bitmap; // image

    AlertDialog dialogs,alertDialog; // update profile and change password alert dialogs



    String url = "http://popularkoju.com.np/id1277129_lintendforum/account_details.php";
    String  imageUploadUrl= "http://popularkoju.com.np/id1277129_lintendforum/images_upload.php";
    String  imageUrl= "http://popularkoju.com.np/id1277129_lintendforum/user_image_display.php";
    String editProfileURL = "http://popularkoju.com.np/id1277129_lintendforum/user_info_update.php";
    String changePasswordURL = "http://popularkoju.com.np/id1277129_lintendforum/password_update.php";

    Button logout;
    ImageView btnchooseImage, btnuploadImage;
    ImageView userImage, moreOptions;
    TextView name, accountContact;
    TextView email;

    Context applicationContext = HomeActivity.getContextOfAppliction(); // called from mainActivity

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_profile_layout, null);



        logout = v.findViewById(R.id.logout);
        name = v.findViewById(R.id.accountName);
        email = v.findViewById(R.id.accountEmail);
       btnchooseImage = v.findViewById(R.id.chooseImage);
       btnuploadImage = v.findViewById(R.id.uploadImage);
        userImage = v.findViewById(R.id.userImage);

        moreOptions = v.findViewById(R.id.moreOption);
        accountContact=v.findViewById(R.id.accountPhone);

      //  message = v.findViewById(R.id.messageHere);

        sm = new SessionManager(getActivity());
        HashMap<String, String> map = sm.getUserDetails();
        final String userEmail = map.get(SessionManager.KEY_EMAIL);

        email.setText(userEmail);




        // choose image -------------------------------------------------------------------
        btnchooseImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectImage();
                permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
                }
                }
        });


        //image upload  --------------------------------------------------------------------------------
        btnuploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataUri!=null){
                    dialog=new ProgressDialog(getContext());
                    dialog.setMessage("UPLOADING...");
                    /*dialog.setCancelable(false);
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);*/
                    dialog.show();

                    ByteArrayOutputStream bit = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100, bit);
                    byte[] imageBytes =bit.toByteArray();
                    final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);


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
                             mymap.put("url", imageString);
                             mymap.put("name",name.getText().toString());
                             return mymap;
                         }
                     };
                     requestQueue.add(stringRequest);


                }
            }
        });

        // more option part----------------------------------------------------------------


        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(),moreOptions);
                popupMenu.getMenuInflater().inflate(R.menu.more_option_in_profile, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit_profile_name:
                                 View v  = LayoutInflater.from(getContext()).inflate(R.layout.edit_profile_layout, null);
                                 nameUpdate = v.findViewById(R.id.update_name);
                                 contactUpdate = v.findViewById(R.id.update_contact);

                                 nameUpdate.setText(name.getText().toString());
                                 contactUpdate.setText(accountContact.getText().toString());
                                Button update = v.findViewById(R.id.btnUpdate_profile);

                               AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                  builder.setView(v);
                                     alertDialog = builder.create();
                                    alertDialog.show();

                                    update.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(nameUpdate.getText().toString().isEmpty()){
                                                nameUpdate.setError("Name Cannot be empty");
                                            }else {
        /*update profile method called */    updateProfile();
                                            }
                                            }
                                    });
                                    break;

/* pass change  starts*/
                            case R.id.change_password:

                                View vi  = LayoutInflater.from(getContext()).inflate(R.layout.change_profile_password_layout, null);
                                oldPassword = vi.findViewById(R.id.oldPassword);
                                  newPassword = vi.findViewById(R.id.newPassword);
                                 newPasswordConfirm = vi.findViewById(R.id.confirmPassword);
                               Button  updatePassword = vi.findViewById(R.id.btnUpdate_password);

                                AlertDialog.Builder builders = new AlertDialog.Builder(getContext());
                                builders.setView(vi);
                                dialogs = builders.create();
                                dialogs.show();

                                updatePassword.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(oldPassword.getText().toString().isEmpty()){
                                            oldPassword.setError("Please fill the form");
                                        }else if(newPassword.getText().toString().isEmpty()){
                                            newPassword.setError("Please fill the form");
                                        }else if(newPasswordConfirm.getText().toString().isEmpty()) {
                                            newPasswordConfirm.setError("Please fill the form");
                                        }else if(newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())){
                                            Toast.makeText(getContext(), "password match", Toast.LENGTH_SHORT).show();
/*password change  method called */            changePassword();
                                        }else{
                                            newPasswordConfirm.setError("Password didn't match");

                                        }
                                    }
                                });
                                break;
                        }

                        return true;
                    }
                });

                popupMenu.show();

            }
        });

        //more option ends -------------------------------MORE OPTION ENDS -----------------------------------


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager s = new SessionManager(getActivity());
                s.logoutUser();
            }
        });
        userImage();

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj1 = new JSONObject(response);
                    String account_name = obj1.getString("name");
                    name.setText(account_name);
                    String account_contact = obj1.getString("contact");
                    accountContact.setText(account_contact);

                /*    String images = (obj1.getString("image"));
                    String  imgURL = "http://popularkoju.com.np/id1277129_lintendforum/";
                    Glide.with(getContext())
                            .load(imgURL+images)
                            .apply(RequestOptions.circleCropTransform())
                            .into(userImage);*/

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

    /*-----------------------------------------------IMAGE SELECTION---------------------------------------*/

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

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
    //            */* for all types of data , image/* all types of images
        i.setType("image/*");
        startActivityForResult(i.createChooser(i, "Select Image"), INT_CONST_CAM); // create field int_ cons
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
        if(requestCode == INT_CONST_CAM && resultCode==RESULT_OK && data!=null && data.getData()!=null ){
            dataUri= data.getData(); // data here is from Intent data above(parameter)


           /* try {
                InputStream inputStream = applicationContext.getContentResolver().openInputStream(dataUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                userImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            try {
                bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), dataUri);

                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

//           userImage.setImageURI(dataUri);
        }
        }

     /*--------------------------------------------IMAGE DISPLAY-----------------------------------------*/
        public void userImage(){

            sm = new SessionManager(getActivity());
            HashMap<String, String> map = sm.getUserDetails();
            final String userEmail_i = map.get(SessionManager.KEY_EMAIL);
            requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest sr = new StringRequest(Request.Method.POST, imageUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj1 = new JSONObject(response);
                        String images = (obj1.getString("image"));
                        String  imgURL = "http://popularkoju.com.np/id1277129_lintendforum/";
                        Glide.with(getContext())
                            .load(imgURL+images)
                            .apply(RequestOptions.circleCropTransform())
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

                    myMap.put("email", userEmail_i);
                    return myMap;
                    }
            };
            requestQueue.add(sr);
        }

     /*--------------------------------------------UPDATE PROFILE---------------------------------------------*/

        public void updateProfile(){
            sm = new SessionManager(getActivity());
            HashMap<String, String> map = sm.getUserDetails();
            final String userEmail = map.get(SessionManager.KEY_EMAIL);

         requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, editProfileURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        Toast.makeText(getContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    }else{
                        alertDialog.dismiss();
                        Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    alertDialog.dismiss();
                    Toast.makeText(getContext(), "exception caught", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No internet", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email",userEmail);
                map.put("name", nameUpdate.getText().toString());
                map.put("contact",contactUpdate.getText().toString());

                return map;
            }
        };
        requestQueue.add(stringRequest);



        }

        /* ---------------------------------------------CHANGE PASSWORD----------------------------------------*/
        public void changePassword(){
            sm = new SessionManager(getActivity());
            HashMap<String, String> map = sm.getUserDetails();
            final String userEmail = map.get(SessionManager.KEY_EMAIL);

            requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, changePasswordURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if(obj.names().get(0).equals("success")){
                            dialogs.dismiss();
                            Toast.makeText(getContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();


                        }else{
                            dialogs.dismiss();
                            Toast.makeText(getContext(), "Failed, Check your old password", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialogs.dismiss();
                        Toast.makeText(getContext(), "exception caught", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "No internet", Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("email",userEmail);
                    map.put("password", oldPassword.getText().toString());
                    map.put("newpassword",newPasswordConfirm.getText().toString());

                    return map;
                }
            };
            requestQueue.add(stringRequest);





        }


    }








