package rong.im.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rong.im.demo.R;
import rong.im.demo.model.User;
import rong.im.demo.util.BmobUtil;
import rong.im.demo.util.Const;
import rong.im.demo.util.RongUtil;
import rong.im.demo.widget.LoginEditBox;
import rong.im.demo.widget.WaitingDialog;

public class LoginActivity extends AppCompatActivity implements TextWatcher, Handler.Callback {

    private static final String TAG = "LoginActivity";

    private static final int STATE_LOGIN = 1;
    private static final int STATE_REQUEST_TOKEN = 2;
    private static final int STATE_CONNECT_IM_SERVER = 3;

    private LoginEditBox username;
    private LoginEditBox password;
    private Button login;
    private TextView signUp;
    private WaitingDialog waitingDialog;

    private int state;
    private Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        username = new LoginEditBox(findViewById(R.id.layout_username), "用户名", "你的登录名");
        password = new LoginEditBox(findViewById(R.id.layout_password), "密码", "填写密码");
        login = (Button) findViewById(R.id.login);
        signUp = (TextView) findViewById(R.id.sign_up);

        username.setTextChangedListener(this);
        password.setTextChangedListener(this);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitingDialog = new WaitingDialog(LoginActivity.this);
                waitingDialog.show();
                state = STATE_LOGIN;
                BmobUtil.loginToServer(getUser(), LoginActivity.this, handler);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private User getUser() {
        User user = new User();
        user.username = username.getText();
        user.password = password.getText();
        return user;
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "handleMessage state = " + state + "; msg.what = " + msg.what);
        if (msg.what == Const.REQUEST_FAILED) {
            waitingDialog.dismiss();
            Toast.makeText(LoginActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            return true;
        }

        switch (state++) {
            case STATE_LOGIN:
                BmobUtil.requestToken(getUser(), this, handler);
                break;
            case STATE_REQUEST_TOKEN:
                String token = (String) msg.obj;
                RongUtil.connectIMServer(token, handler);
                break;
            case STATE_CONNECT_IM_SERVER:
                waitingDialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
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
