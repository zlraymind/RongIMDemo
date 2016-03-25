package rong.im.demo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.bmob.v3.Bmob;
import rong.im.demo.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextView txtUsername;
    private TextView txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "0d7dbcd610ef903a5e756753060812b9");
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("RongIM Demo");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        txtUsername = (TextView) findViewById(R.id.username);
        txtPassword = (TextView) findViewById(R.id.password);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView txtSignUp = (TextView) findViewById(R.id.sign_up);
        txtSignUp.setPaintFlags(txtSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
