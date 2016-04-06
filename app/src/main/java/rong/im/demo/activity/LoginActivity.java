package rong.im.demo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rong.im.demo.R;
import rong.im.demo.model.LoginUser;
import rong.im.demo.util.BmobUtil;
import rong.im.demo.util.Const;
import rong.im.demo.widget.LoginEditBox;

public class LoginActivity extends AppCompatActivity  implements TextWatcher, Handler.Callback {

    private static final String TAG = "LoginActivity";

    private LoginEditBox username;
    private LoginEditBox password;
    private Button login;
    private TextView signup;

    private Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("融云演示");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        username = new LoginEditBox(findViewById(R.id.layout_username), "用户名", "你的登录名");
        password = new LoginEditBox(findViewById(R.id.layout_password), "密码", "填写密码");
        login = (Button) findViewById(R.id.login);
        signup = (TextView) findViewById(R.id.sign_up);

        username.setTextChangedListener(this);
        password.setTextChangedListener(this);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUtil.loginToServer(getLoginUser(), LoginActivity.this, handler);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private LoginUser getLoginUser() {
        LoginUser user = new LoginUser();
        user.setUsername(username.getText());
        user.setPassword(password.getText());
        return user;
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "handleMessage msg.what = " + msg.what);
        if (msg.what == Const.REQUEST_SUCCESS) {

        } else {
            Toast.makeText(LoginActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean notEmpty = !username.getText().isEmpty() && !password.getText().isEmpty();
        login.setEnabled(notEmpty);
    }
}
