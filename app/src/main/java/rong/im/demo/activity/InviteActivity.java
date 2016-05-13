package rong.im.demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import rong.im.demo.R;
import rong.im.demo.util.AppUtil;
import rong.im.demo.util.Const;
import rong.im.demo.util.RemoteDBUtil;
import rong.im.demo.widget.WaitingDialog;

public class InviteActivity extends AppCompatActivity implements Handler.Callback {

    private static final String TAG = "InviteActivity";

    private ImageView back;
    private Button send;
    private EditText inviteEdit;
    private ImageView clearText;
    private WaitingDialog waitingDialog;

    private Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        send = (Button) findViewById(R.id.send);
        inviteEdit = (EditText) findViewById(R.id.invite_edit);
        clearText = (ImageView) findViewById(R.id.clear_text);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra("username");
                RemoteDBUtil.sendInvitation(username, AppUtil.getCurrentUser().username, handler);
                waitingDialog = new WaitingDialog(InviteActivity.this);
                waitingDialog.setText("正在发送邀请...");
                waitingDialog.show();
            }
        });
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteEdit.setText("");
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        waitingDialog.dismiss();
        if (msg.what == Const.REQUEST_SUCCESS) {
            finish();
        } else if (msg.what == Const.REQUEST_FAILED) {
            Toast.makeText(InviteActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
