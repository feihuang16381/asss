package com.cqut.icode.asss_android.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.common.MyApplication;

import butterknife.BindView;

/**
 * Created by 10713 on 2017/7/15.
 * 活动描述：首页-->侧边菜单-->修改密码
 */
public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.old_password)
    TextInputLayout oldPassword;
    @BindView(R.id.new_password)
    TextInputLayout newPassword;
    @BindView(R.id.confirm_password)
    TextInputLayout confirmPassword;
    @BindView(R.id.determine_modify)
    Button determineModify;

    private String username, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        initialize();
    }

    private void initialize() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //取代原本的actionbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        determineModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword(){
        boolean  cancel = false;

        String old_psd = oldPassword.getEditText().getText().toString().trim();
        String new_psd = newPassword.getEditText().getText().toString().trim();
        String confirm_psd = confirmPassword.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(old_psd)) {
            oldPassword.setError("旧密码不能为空");
            cancel = true;
        }
        else
            oldPassword.setErrorEnabled(false);

        if (TextUtils.isEmpty(new_psd)) {
            newPassword.setError("新密码不能为空");
            cancel = true;
        }
        else
            newPassword.setErrorEnabled(false);
        if(TextUtils.isEmpty(confirm_psd)) {
            confirmPassword.setError("确认密码不能为空");
            cancel = true;
        }
        else if(!confirm_psd.equals(new_psd)) {
            confirmPassword.setError("密码不一致");
            cancel = true;
        }
        else
            confirmPassword.setErrorEnabled(false);

        if(cancel){

        }
        else {
            SharedPreferences info = getSharedPreferences("passwordinfo", Activity.MODE_PRIVATE);
            username = info.getString("username","");
            password = info.getString("password","");
//            username = "张三";
//            password = "123";
            Toast.makeText(MyApplication.getContext(),username,Toast.LENGTH_SHORT).show();
            if(!old_psd.equals(password)) {
                oldPassword.setError("密码错误");
                cancel = true;
            }
            else {
                oldPassword.setErrorEnabled(false);

                info.edit().putString("username", username).commit();
                info.edit().putString("password", confirm_psd).commit();
                Toast.makeText(MyApplication.getContext(),"修改成功", Toast.LENGTH_SHORT).show();
            }


        }
    }

}
