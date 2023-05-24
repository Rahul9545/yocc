package com.bigv.app.yocc.activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.componants.Toasty;

import quickutils.core.QuickUtils;
import com.bigv.app.yocc.R;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.ChangePasswordPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;

/**
 * Created by MiTHUN on 20/11/17.
 */

public class ChangePasswordActivity extends BaseActivity {

    private EditText userIdEditText;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button changePasswordButton;
    private Toolbar toolbar;


    @Override
    protected void genrateId() {

        setContentView(R.layout.change_password_activity);

        userIdEditText = findViewById(R.id.change_password_user_id_et);
        oldPasswordEditText = findViewById(R.id.change_password_old_password_et);
        newPasswordEditText = findViewById(R.id.change_password_new_password_et);
        confirmPasswordEditText = findViewById(R.id.change_password_confirm_password_et);
        changePasswordButton = findViewById(R.id.change_password_btn);

        initToolbar();
    }

    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setSupportActionBar(Toolbar toolbar) {
       // toolbar.setTitle("CHANGE PASSWORD");
        toolbar.setTitle(getResources().getString(R.string.str_title_change_pass));
    }


    @Override
    protected void registerEvents() {

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordButtonOnClick();
            }
        });
    }

    private void changePasswordButtonOnClick() {
        if (isFormValid()) {
            if (isUserIdOrPasswordMatch()) {
                if (isNewPasswordMatch()) {

                    final ChangePasswordPojo changePasswordPojo = new ChangePasswordPojo();
                    changePasswordPojo.setUserID(userIdEditText.getText().toString());
                    changePasswordPojo.setOldPassword(oldPasswordEditText.getText().toString());
                    changePasswordPojo.setNewPassword(newPasswordEditText.getText().toString());

                    new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {

                        public ResultPojo result = null;

                        @Override
                        public void doInBackgroundOpration(SyncServer syncServer) {

                            result = syncServer.changePassword(changePasswordPojo);
                        }

                        @Override
                        public void onFinished() {
                            if (!AUtils.isNull(result)) {

                                if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                                    Toasty.success(ChangePasswordActivity.this, getString(R.string.passwordChangedSuccess), Toast.LENGTH_SHORT).show();
                                    ChangePasswordActivity.this.finish();
                                } else {
                                    Toasty.error(ChangePasswordActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toasty.error(ChangePasswordActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).execute();

                } else {

                    Toasty.error(this, "The password and confirmation password do not match", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toasty.error(this, "User id or password not valid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isFormValid() {

        if (AUtils.isNullString(userIdEditText.getText().toString())) {
            Toasty.warning(this, "Please enter user id", Toast.LENGTH_SHORT).show();
            return false;
        } else if (AUtils.isNullString(oldPasswordEditText.getText().toString())) {
            Toasty.warning(this, "Please enter old password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (AUtils.isNullString(newPasswordEditText.getText().toString())) {
            Toasty.warning(this, "Please enter new password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (AUtils.isNullString(confirmPasswordEditText.getText().toString())) {
            Toasty.warning(this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (newPasswordEditText.getText().toString().length() < 6) {
            Toasty.warning(this, "The new password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isNewPasswordMatch() {

        if (newPasswordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {

            return true;
        }
        return false;
    }

    private boolean isUserIdOrPasswordMatch() {

        if (userIdEditText.getText().toString().equals(QuickUtils.prefs.getString(AUtils.USER_LOGIN_ID, "")) &&
                oldPasswordEditText.getText().toString().equals(QuickUtils.prefs.getString(AUtils.USER_LOGIN_PASSWORD, ""))) {
            return true;
        }
        return true;
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

//        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
//        if (id == R.id.toolbar) {
//            return true;
//        }

        ChangePasswordActivity.this.finish();

        return super.onOptionsItemSelected(item);
    }


}
