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
import rong.im.demo.util.AppUtil;
import rong.im.demo.util.Const;
import rong.im.demo.util.RemoteDBUtil;
import rong.im.demo.util.RongUtil;
import rong.im.demo.widget.LoginEditBox;
import rong.im.demo.widget.WaitingDialog;

public class LoginActivity extends AppCompatActivity implements TextWatcher, Handler.Callback {

    private static final String TAG = "LoginActivity";

    private static final int STATE_AUTO_LOGIN = 0;
    private static final int STATE_LOGIN = 1;
    private static final int STATE_REQUEST_TOKEN = 2;
    private static final int STATE_CONNECT_IM_SERVER = 3;
    private static final int STATE_LOGIN_SUCCESS = 4;

    private LoginEditBox username;
    private LoginEditBox password;
    private Button login;
    private TextView signUp;
    private WaitingDialog waitingDialog;

    private int state = STATE_AUTO_LOGIN;
    private Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (state == STATE_AUTO_LOGIN) {
            autoLogin();
        }
    }

    private void initView() {
        username = new LoginEditBox(findViewById(R.id.layout_username), "登录名", "你的登录名");
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
                waitingDialog.setText("正在登录...");
                waitingDialog.show();
                state = STATE_LOGIN;
                AppUtil.saveLoginInfo(username.getText(), AppUtil.md5(password.getText()));
                RemoteDBUtil.loginToServer(username.getText(), AppUtil.md5(password.getText()), handler);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        if (intent.getBooleanExtra("logout", false)) {
            username.setText("");
            password.setText("");
            return;
        }

        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            finish();
        }
    }

    private void autoLogin() {
        String cachedUsername = AppUtil.getCachedUsername();
        String cachedPassword = AppUtil.getCachedPasswordMD5();
        if (!cachedUsername.isEmpty() && !cachedPassword.isEmpty()) {
            username.setText(cachedUsername);
            password.setText("********");
            waitingDialog = new WaitingDialog(LoginActivity.this);
            waitingDialog.setText("正在登录...");
            waitingDialog.show();
            Log.d(TAG, "cachedPassword = " + cachedPassword);
            RemoteDBUtil.loginToServer(cachedUsername, cachedPassword, handler);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "handleMessage state = " + state + "; msg.what = " + msg.what);
        if (msg.what == Const.REQUEST_FAILED) {
            if (state == STATE_AUTO_LOGIN || state == STATE_LOGIN) {
                AppUtil.clearUserInfoCached();
            }
            waitingDialog.dismiss();
            Toast.makeText(LoginActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            return true;
        }

        switch (++state) {
            case STATE_LOGIN:
                handler.sendEmptyMessage(Const.REQUEST_SUCCESS);
            case STATE_REQUEST_TOKEN:
                RemoteDBUtil.requestToken(RemoteDBUtil.getCurrentUser(), handler);
                break;
            case STATE_CONNECT_IM_SERVER:
                String token = (String) msg.obj;
                RongUtil.connectIMServer(token, handler);
                break;
            case STATE_LOGIN_SUCCESS:
                waitingDialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
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
