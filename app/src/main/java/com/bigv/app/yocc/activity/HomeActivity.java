package com.bigv.app.yocc.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.fragment.CallDetailsFragment;
import com.bigv.app.yocc.fragment.HomeFragment;
import com.bigv.app.yocc.fragment.LiveCallsFragment;
import com.bigv.app.yocc.pojo.HomeScreenPojo;
import com.bigv.app.yocc.pojo.LoginPojo;
import com.bigv.app.yocc.pojo.PasswordCheckPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.webservices.LoginWebservices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mithsoft.lib.adapter.TabViewPagerAdapter;
import com.mithsoft.lib.componants.Toasty;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mithun on 12/9/17.
 * Updated by Rahul Rokade 12/01/2023
 */

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    boolean doubleBackToExitPressedOnce;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView navigationUserName;
    private ImageView navigationUserImageView;
    private TextView initialWordNavTextView;
    private TabLayout tabLayout;
    private HomeScreenPojo homeScreenPojo;
    private FloatingActionButton flBtnCallify;
    float dX;
    float dY;
    int lastAction;
    private Context context;

    //    https://www.learn2crack.com/2014/06/android-sliding-navigation-drawer-example.html
    private ViewPager viewPager;
    private Handler handler = new Handler();
    private LoginPojo loginPojo;
    private TextView navigationUserNumber;
    private Menu navigationMenu;
    private MenuItem settingMenuItem;
    private MenuItem addressMenuItem;
    private MenuItem remarkNotificationMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_list);

        initComponants();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void initComponants() {
        ganrateId();
        chePassWordApi();
        //checkHomeStatus();
        registerEvents();
        initActionBar();
        initTabLayout();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initTabLayout() {

        TabViewPagerAdapter viewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager(), this);
        viewPagerAdapter.addFrag(HomeFragment.newInstance(), null, "Home", 0);
        viewPagerAdapter.addFrag(LiveCallsFragment.newInstance(), null, "Live Calls", 1);
        viewPagerAdapter.addFrag(CallDetailsFragment.newInstance(), null, "Call Details", 2);
//        viewPagerAdapter.addFrag(AnalyticReportFragment.newInstance(), null, "Analytic Report", 3);

        viewPager.setAdapter(viewPagerAdapter);

        //This will tell the pager to keep all of them in memory and not destroy/create with every swipe (keep a close look on the memory management)
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount() - 1);

        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 2){
                    chePassWordApi();
                   // Toast.makeText(HomeActivity.this, " Rahul call", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    chePassWordApi();
                   // Toast.makeText(HomeActivity.this, " Rahul Home", Toast.LENGTH_SHORT).show();
                }else if (position == 1){
                    chePassWordApi();
                   // Toast.makeText(HomeActivity.this, "Rahul Live", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    private void ganrateId() {
        context = this;
        tabLayout = findViewById(R.id.tabLayout);
//         make the tab view title text scrollable
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager = findViewById(R.id.viewPagerTab);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        navigationUserNumber = headerLayout.findViewById(R.id.navigationUserNumber);
        initialWordNavTextView = headerLayout.findViewById(R.id.initialWordNavTextView);
        navigationUserName = headerLayout.findViewById(R.id.navigationUserName);
        navigationUserImageView = headerLayout.findViewById(R.id.navigationUserImageView);
        flBtnCallify = findViewById(R.id.fl_callify_btn);

        navigationMenu = navigationView.getMenu();
        settingMenuItem = navigationMenu.findItem(R.id.nav_setting);
        addressMenuItem = navigationMenu.findItem(R.id.nav_address_book);
        remarkNotificationMenuItem = navigationMenu.findItem(R.id.nav_noti);
        navigationDataSet();
    }

    private void navigationDataSet() {

        loginPojo = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.USER_DATA, ""), LoginPojo.class);

        if (!AUtils.isNullString(loginPojo.getCompanyName())) {

            navigationUserName.setText(loginPojo.getCompanyName());
        }

//        if (!AUtils.isNullString(loginPojo.getLogoUrl())) {
//
//            initialWordNavTextView.setVisibility(View.GONE);
//            navigationUserImageView.setVisibility(View.VISIBLE);
//
//            Glide.with(HomeActivity.this)
//                    .load(loginPojo.getLogoUrl())
//                    .placeholder(R.drawable.unknown_person)
//                    .error(R.drawable.unknown_person)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(navigationUserImageView);
//
//        } else {
        if (!AUtils.isNullString(loginPojo.getCompanyName())) {

            initialWordNavTextView.setVisibility(View.VISIBLE);
            navigationUserImageView.setVisibility(View.GONE);

            initialWordNavTextView.setText("" + loginPojo.getCompanyName().charAt(0));
        }
        if (!AUtils.isNullString(loginPojo.getYoccNumber())) {

            navigationUserNumber.setText(loginPojo.getYoccNumber());
        }

        if (QuickUtils.prefs.getBoolean(AUtils.IS_LOGIN_BY_AGENT, false)) {

            settingMenuItem.setVisible(false);
            addressMenuItem.setVisible(false);
            remarkNotificationMenuItem.setVisible(false);
        }
    }

    private void initActionBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        ((ActionBar) actionBar).setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
//        setTitleActionBar("Big V Telecom Pvt Ltd.");
//        setSubtitleActionBar("9272231058");
        setTitleActionBar("" + loginPojo.getCompanyName());
        setSubtitleActionBar("" + loginPojo.getYoccNumber());
    }

    public void setTitleActionBar(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    public void setSubtitleActionBar(CharSequence title) {
        getSupportActionBar().setSubtitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                chePassWordApi();
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.nav_notification:
                Toast.makeText(this, "notification", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void registerEvents() {

        flBtnCallify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CallActivity.class).putExtra("fab_button",true));
            }
        });

        flBtnCallify.setOnTouchListener(new View.OnTouchListener() {
            float dX;
            float dY;
            float startX;
            float startY;
            int lastAction;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        startX = event.getRawX();
                        startY = event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        v.setY(event.getRawY() + dY);
                        v.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(startX - event.getRawX()) < 10 && Math.abs(startY - event.getRawY()) < 10){
                            Toast.makeText(v.getContext(), "Float button Clicked!", Toast.LENGTH_SHORT).show();

                        }
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_address_book:
                                startActivity(new Intent(HomeActivity.this, AddressBookMenuActivity.class));
                                break;
                            case R.id.nav_setting:
                                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                                break;
                            case R.id.nav_noti:
                                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                                break;
                            case R.id.nav_logout:
                                logoutOnClick();
                                break;
                        }
                        return true;
                    }
                });
    }

    private void logoutOnClick() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Application will be logout");

        alert.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutOptionOnClick();
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();

    }

    private void logoutOptionOnClick() {

        QuickUtils.prefs.remove(AUtils.USER_DATA);
        QuickUtils.prefs.remove(AUtils.USERNAME);
        QuickUtils.prefs.remove(AUtils.PASSWORD);
        QuickUtils.prefs.remove(AUtils.IS_USER_LOGIN);
        QuickUtils.prefs.remove(AUtils.KEY);

        Toasty.success(HomeActivity.this, "Logout success", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        HomeActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            // drawer is open
            mDrawerLayout.closeDrawers();

        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                // popping backstack
                getFragmentManager().popBackStack();
            } else {
                // nothing on backstack, calling super
//                super.onBackPressed();
                doubleClickBackPressToExit();
            }
        }
    }

    private void checkHomeStatus() {
        if (!AUtils.isNull(QuickUtils.prefs.getString(AUtils.HOME_SCREEN_POJO, null))) {
            homeScreenPojo = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.HOME_SCREEN_POJO, null), HomeScreenPojo.class);

            if (homeScreenPojo.isStatus() == false){
                logoutOptionOnClick();
                finishAffinity();
            }else if (homeScreenPojo.isStatus() == true){
                //initTabLayout();
            }
        }
    }

    private void chePassWordApi(){
        LoginWebservices checkPass = AUtils.createService(LoginWebservices.class,AUtils.SERVER_URL);
        checkPass.checkPassApi(QuickUtils.prefs.getString(AUtils.PASSWORD,""),
                QuickUtils.prefs.getString(AUtils.KEY,""),QuickUtils.prefs.getString(AUtils.USERNAME,"")).enqueue(new Callback<PasswordCheckPojo>() {
            @Override
            public void onResponse(Call<PasswordCheckPojo> call, Response<PasswordCheckPojo> response) {
                if (response.body() != null){
                    if (response.body().isPasswordstatus() == false){
                        Log.i("Rahul", "password not Matched home activity");
                        logoutOptionOnClick();
                        finishAffinity();
                    }else if (response.body().isPasswordstatus() == true){
                        Log.i("Rahul", "password Match home Activity - check pass Api");
                       // initTabLayout();
                    }
                }
            }

            @Override
            public void onFailure(Call<PasswordCheckPojo> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"No Internet Connection,\nIf internet ON then reopen your app", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doubleClickBackPressToExit() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        // Does the user really want to exit?
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.press_back_again), Toast.LENGTH_LONG).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
