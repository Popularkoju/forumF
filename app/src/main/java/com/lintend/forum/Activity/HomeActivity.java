package com.lintend.forum.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lintend.forum.R;
import com.lintend.forum.SessionManager;

public class HomeActivity extends AppCompatActivity {
  public BottomNavigationViewEx bottomNavigationView;
  Toolbar toolbar;

    FragmentTransaction ft;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        bottomNavigationView = findViewById(R.id.buttom_nav_bar);


            final SessionManager sm = new SessionManager(this);
            sm.isLoggedIn();

        ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.layoutmid,new HomeTabActivity());
        ft.commit();







        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.ic_home: {
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.layoutmid, new HomeTabActivity());
                        ft.commit();
                        break;
                    }

                    case R.id.ic_trending: {
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.layoutmid, new TrendingTabActivity());
                        ft.commit();
                        break;
                    }
                    case R.id.ic_post: {
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.layoutmid, new PostTabActivity());
                        ft.commit();
                        break;
                    }


                    case R.id.ic_search: {
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.layoutmid, new SearchTabActivity());
                        ft.commit();
                        break;
                    }
                    case R.id.ic_about: {
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.layoutmid, new About_swipable_activity());
                        ft.commit();
                        break;
                    }

                }
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }
    }

