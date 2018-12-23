package com.lintend.forum.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lintend.forum.R;
import com.lintend.forum.SessionManager;

import java.util.HashMap;

public class SearchTabActivity extends Fragment {
    SessionManager sessionManager;
    TextView detail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_tab_layout, null);
            detail=v.findViewById(R.id.session);
            sessionManager = new SessionManager(getActivity());

        HashMap<String, String> map= sessionManager.getUserDetails();
        String userEmail = map.get(SessionManager.KEY_EMAIL);

            detail.setText(userEmail );
        Toast.makeText(getActivity(), userEmail,Toast.LENGTH_SHORT).show();



        return v;
    }
}
