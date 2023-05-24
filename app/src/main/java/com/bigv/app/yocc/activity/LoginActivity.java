package com.bigv.app.yocc.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.HomeScreenPojo;
import com.bigv.app.yocc.pojo.LoginPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.google.gson.Gson;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.componants.Toasty;

import quickutils.core.QuickUtils;

/**
 * Created by mithun on 12/9/17.
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView turmOfUseEditText;
    private Button loginButton;
    private String username;
    private String password;
    private CheckBox showPasswordCheckBox;

    @Override
    protected void genrateId() {

        setContentView(R.layout.login_activity);
        usernameEditText = findViewById(R.id.usernameET);
        passwordEditText = findViewById(R.id.passwordET);
        turmOfUseEditText = findViewById(R.id.turm_of_use_tv);
        turmOfUseEditText.setPaintFlags(turmOfUseEditText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        loginButton = findViewById(R.id.loginBtn);
        showPasswordCheckBox = findViewById(R.id.show_password_checkbox);
    }

    @Override
    protected void registerEvents() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AUtils.getDeviceDrawable(LoginActivity.this);
                loginButtonOnClick();
            }
        });

        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordEditText.setTypeface(Typeface.DEFAULT);
                } else {
                    passwordEditText.setInputType(129);
                    passwordEditText.setTypeface(Typeface.DEFAULT);
                }
            }
        });
        turmOfUseEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, TOUActivity.class));
            }
        });
    }

    private void loginButtonOnClick() {

        if (!AUtils.isNullString(usernameEditText.getText().toString()) && !AUtils.isNullString(passwordEditText.getText().toString())) {

            username = usernameEditText.getText().toString().trim();
            QuickUtils.prefs.save(AUtils.USERNAME, username);
            Log.i("checkLogin", "userName: "+QuickUtils.prefs.getString(AUtils.USERNAME,""));
            password = passwordEditText.getText().toString();
            QuickUtils.prefs.save(AUtils.PASSWORD, password);
            Log.i("checkLogin", "password: "+QuickUtils.prefs.getString(AUtils.PASSWORD,""));

            new MyAsyncTask(LoginActivity.this, true, new MyAsyncTask.AsynTaskListener() {

                private LoginPojo loginPojo = null;
                private HomeScreenPojo homeScreenPojo = null;

                @Override
                public void doInBackgroundOpration(SyncServer syncServer) {

                    loginPojo = syncServer.loginUser(username, password);
                    if (!AUtils.isNull(loginPojo)) {
                        if (loginPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                            if (!AUtils.isNull(loginPojo.getUserType()) && (loginPojo.getUserType().equals("2"))) {

                                QuickUtils.prefs.save(AUtils.IS_LOGIN_BY_AGENT, true);
                                QuickUtils.prefs.save(AUtils.USER_TYPE_ID, loginPojo.getUserType());

                                if (!AUtils.isNullString(loginPojo.getUserId())) {

                                    QuickUtils.prefs.save(AUtils.AGENT_ID, loginPojo.getUserId());
                                } else {
                                    QuickUtils.prefs.save(AUtils.AGENT_ID, "0");
                                }
                            } else {

                                QuickUtils.prefs.save(AUtils.AGENT_ID, loginPojo.getUserId());
                                QuickUtils.prefs.save(AUtils.USER_TYPE_ID, loginPojo.getUserType());
                                QuickUtils.prefs.save(AUtils.IS_LOGIN_BY_AGENT, false);

                            }

                            homeScreenPojo = syncServer.getHomeScreenItems();
                            String todaysDate = AUtils.getTodaysDate(AUtils.SERVER_DATE_FORMATE);
                            syncServer.getCallDetailsList(todaysDate + " 00:00:00", todaysDate + " 23:59:59");
                            syncServer.getCallDetailsSummery(todaysDate + " 00:00:00", todaysDate + " 23:59:59");
                            syncServer.getWeekWiseCallDuration();
                            syncServer.getWeekWiseCall();
                            syncServer.getMonthWiseCall();
//                            syncServer.getHourWiseCall();
                            syncServer.getCallPriorityList();
                        }
                    }
                }

                @Override
                public void onFinished() {

                    if (!AUtils.isNull(loginPojo)) {
                        if (loginPojo.getStatus().equalsIgnoreCase(AUtils.STATUS_SUCCESS)) {

//                            if (!AUtils.isNull(homeScreenPojo)) {

                            Toasty.success(LoginActivity.this, getString(R.string.loginSuccess), Toast.LENGTH_SHORT).show();
                            QuickUtils.prefs.save(AUtils.USER_DATA, new Gson().toJson(loginPojo));
                            QuickUtils.prefs.save(AUtils.IS_USER_LOGIN, true);
                            QuickUtils.prefs.save(AUtils.USER_LOGIN_ID, username);
                            QuickUtils.prefs.save(AUtils.USER_LOGIN_PASSWORD, password);

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            LoginActivity.this.finish();
//                            } else {
//                                //home screen data not fatch
//                                Toasty.error(LoginActivity.this, getString(R.string.noData), Toast.LENGTH_SHORT).show();
//                            }

                        } else if (loginPojo.getStatus().equalsIgnoreCase(AUtils.STATUS_EXPIRED)) {
                            //login status expired
                            Toasty.warning(LoginActivity.this, getString(R.string.loginExpired), Toast.LENGTH_SHORT).show();
                        } else if (loginPojo.getStatus().equalsIgnoreCase(AUtils.STATUS_ERROR)) {
                            //login status error
                            Toasty.error(LoginActivity.this, getString(R.string.loginFailed), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //login pojo is null
                        Toasty.error(LoginActivity.this, getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();

        } else {
            Toasty.warning(this, "Please enter username and password.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initData() {

    }
}