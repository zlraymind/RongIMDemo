package rong.im.demo.activity;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import rong.im.demo.R;
import rong.im.demo.model.User;
import rong.im.demo.util.BmobUtil;
import rong.im.demo.util.Const;
import rong.im.demo.util.RongUtil;
import rong.im.demo.widget.LoginEditBox;
import rong.im.demo.widget.WaitingDialog;
import rong.im.demo.widget.WarringDialog;

public class SignUpActivity extends AppCompatActivity implements TextWatcher, Handler.Callback {

    private static final String TAG = "SignUpActivity";
    private static final int STATE_CHECK_USERNAME = 1;
    private static final int STATE_UPLOAD_PORTRAIT = 2;
    private static final int STATE_SIGN_UP = 3;
    private static final int STATE_LOGIN = 4;
    private static final int STATE_REQUEST_TOKEN = 5;
    private static final int STATE_CONNECT_IM_SERVER = 6;

    private LoginEditBox nickname;
    private LoginEditBox username;
    private LoginEditBox password;
    private ImageView back;
    private ImageView portrait;
    private Button signUp;
    private WaitingDialog waitingDialog;

    private int state;
    private Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
    }

    private void initView() {
        nickname = new LoginEditBox(findViewById(R.id.layout_nickname), "昵称", "例如:张无忌");
        username = new LoginEditBox(findViewById(R.id.layout_username), "用户名", "您的用户名");
        password = new LoginEditBox(findViewById(R.id.layout_password), "密码", "填写密码");
        back = (ImageView) findViewById(R.id.back);
        portrait = (ImageView) findViewById(R.id.portrait);
        signUp = (Button) findViewById(R.id.sign_up);

        nickname.setTextChangedListener(this);
        username.setTextChangedListener(this);
        password.setTextChangedListener(this);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputPanel();

                if (username.getText().length() < 4) {
                    WarringDialog dialog = new WarringDialog(SignUpActivity.this);
                    dialog.setSubject("格式错误");
                    dialog.setBody("您输入的用户名长度需大于4位");
                    dialog.show();
                    return;
                }
                if (password.getText().length() < 6) {
                    WarringDialog dialog = new WarringDialog(SignUpActivity.this);
                    dialog.setSubject("格式错误");
                    dialog.setBody("您输入的密码长度需大于6位");
                    dialog.show();
                    return;
                }

                waitingDialog = new WaitingDialog(SignUpActivity.this);
                waitingDialog.show();
                state = STATE_CHECK_USERNAME;
                BmobUtil.checkUserFromServer(username.getText(), SignUpActivity.this, handler);
            }
        });
    }

    private void hideInputPanel() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private User getUser() {
        User user = new User();
        user.username = username.getText();
        user.password = password.getText();
        user.nickname = nickname.getText();
        if (portrait.getTag() == null) {
            user.portrait = Const.DEFAULT_PORTRAIT;
        } else {
            user.portrait = (String) portrait.getTag();
        }
        return user;
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "handleMessage state = " + state + "; msg.what = " + msg.what);
        if (msg.what == Const.REQUEST_FAILED) {
            waitingDialog.dismiss();
            Toast.makeText(SignUpActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            return true;
        }

        switch (state++) {
            case STATE_CHECK_USERNAME:
                if (portrait.getTag() == null) {
                    state = STATE_SIGN_UP;
                    BmobUtil.addUserToServer(getUser(), this, handler);
                } else {
                    BmobUtil.uploadPortraitToServer((String) portrait.getTag(), this, handler);
                }
                break;
            case STATE_UPLOAD_PORTRAIT:
                BmobUtil.addUserToServer(getUser(), this, handler);
                break;
            case STATE_SIGN_UP:
                BmobUtil.loginToServer(getUser(), this, handler);
                break;
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
                intent.setClass(SignUpActivity.this, MainActivity.class);
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
        boolean notEmpty = !nickname.getText().isEmpty() && !username.getText().isEmpty() && !password.getText().isEmpty();
        signUp.setEnabled(notEmpty);
    }

//    // picture crop
//    public void startPhotoZoom(Uri uri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 200);
//        intent.putExtra("output", uri);
//        intent.putExtra("outputFormat", "JPEG");
//        startActivityForResult(intent, REQUEST_CROP);
//    }
//
//    // compress image until it's size less than @size
//    private Bitmap compressImage(Bitmap image, int size) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > size) {
//            baos.reset();
//            options -= 10;
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
//        }
//
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//        return BitmapFactory.decodeStream(isBm, null, null);
//    }

}
