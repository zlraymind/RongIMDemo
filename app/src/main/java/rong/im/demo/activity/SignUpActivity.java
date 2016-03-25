package rong.im.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.UploadFileListener;
import rong.im.demo.R;

public class SignUpActivity extends AppCompatActivity implements Handler.Callback {

    private static final String TAG = "SignUpActivity";

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CROP = 1;

    private static final int STATE_ADD_USER_FAILED = 0;
    private static final int STATE_ADD_USER_SUCCESS = 1;
    private static final int STATE_UPLOAD_PORTRAIT_FAILED = 2;
    private static final int STATE_UPLOAD_PORTRAIT_SUCCESS = 3;

    private TextView txtUsername;
    private TextView txtPassword;
    private TextView txtNickname;
    private ImageView imgPortrait;

    private Uri takePhotoUri;
    private Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case STATE_ADD_USER_FAILED:
                Toast.makeText(this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                break;
            case STATE_ADD_USER_SUCCESS:
                uploadPortrait();
                break;
            case STATE_UPLOAD_PORTRAIT_FAILED:
                Toast.makeText(this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                break;
            case STATE_UPLOAD_PORTRAIT_SUCCESS:
                updatePortraitToServer((String) msg.obj);
                break;
            default:
        }
        return true;
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("RongIM Demo");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        txtUsername = (TextView) findViewById(R.id.username);
        txtPassword = (TextView) findViewById(R.id.password);
        txtNickname = (TextView) findViewById(R.id.nickname);

        imgPortrait = (ImageView) findViewById(R.id.portrait);
        imgPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File path = getApplicationContext().getExternalCacheDir();
                String name = System.currentTimeMillis() + ".jpg";
                File file = new File(path, name);
                takePhotoUri = Uri.fromFile(file);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUri);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkValid = checkInfoValid();
                if (!checkValid.equals("OK")) {
                    Toast.makeText(SignUpActivity.this, checkValid, Toast.LENGTH_SHORT).show();
                    return;
                }
                addUserToServer();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String checkInfoValid() {
        if (txtUsername.length() == 0) {
            return "错误! 用户名不能为空.";
        }
        if (txtPassword.length() < 6) {
            return "错误! 密码不能少于6位.";
        }
        if (imgPortrait.getTag() == null) {
            return "错误! 头像不能为空.";
        }
        return "OK";
    }

    private void addUserToServer() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("username", txtUsername.getText());
            jsonObj.put("password", txtPassword.getText());
            jsonObj.put("nickname", txtNickname.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
        ace.callEndpoint(SignUpActivity.this, "addUser", jsonObj, new CloudCodeListener() {
            @Override
            public void onSuccess(Object object) {
                Log.d(TAG, "callEndpoint->addUser: response = " + object.toString());

                // parser result
                int code = 0;
                try {
                    JSONObject result = new JSONObject(object.toString());
                    code = result.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message msg = Message.obtain();
                if (code == 0) {
                    Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    msg.what = STATE_ADD_USER_SUCCESS;
                    handler.sendMessage(msg);
                } else {
                    msg.what = STATE_ADD_USER_FAILED;
                    switch (code) {
                        case 202:
                            msg.obj = "错误! 用户名已存在.";
                            break;
                        default:
                    }
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        });
    }

    private void uploadPortrait() {
        final BmobFile bmobFile = new BmobFile(new File(takePhotoUri.getPath()));
        bmobFile.uploadblock(SignUpActivity.this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Message msg = Message.obtain();
                msg.what = STATE_UPLOAD_PORTRAIT_SUCCESS;
                msg.obj = bmobFile.getFileUrl(SignUpActivity.this);
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int code, String reason) {
                Message msg = Message.obtain();
                msg.what = STATE_UPLOAD_PORTRAIT_FAILED;
                msg.obj = reason;
                handler.sendMessage(msg);
            }
        });
    }

    private void updatePortraitToServer(String uri) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("username", txtUsername.getText());
            jsonObj.put("password", txtPassword.getText());
            jsonObj.put("portrait", uri);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
        ace.callEndpoint(SignUpActivity.this, "updatePortrait", jsonObj, new CloudCodeListener() {
            @Override
            public void onSuccess(Object object) {
                Log.d(TAG, "callEndpoint->updatePortrait: response = " + object.toString());

                // parser result
//                int code = 0;
//                try {
//                    JSONObject result = new JSONObject(object.toString());
//                    code = result.getInt("code");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                Message msg = Message.obtain();
//                if (code == 0) {
//                    Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                    msg.what = STATE_ADD_USER_SUCCESS;
//                    handler.sendMessage(msg);
//                } else {
//                    msg.what = STATE_ADD_USER_FAILED;
//                    switch (code) {
//                        case 202:
//                            msg.obj = "错误! 用户名已存在.";
//                            break;
//                        default:
//                    }
//                    handler.sendMessage(msg);
//                }
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() requestCode = " + requestCode + "; resultCode = " + resultCode);
        if (resultCode != RESULT_OK) {
            return;
        }

        // the portrait image's resolution is 200x200, no more than 50kb
        if (requestCode == REQUEST_CAMERA) {
            startPhotoZoom(takePhotoUri);
        } else if (requestCode == REQUEST_CROP) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(takePhotoUri.getPath(), options);
            bitmap = compressImage(bitmap, 50);

            if (bitmap != null) {
                imgPortrait.setImageBitmap(bitmap);
                imgPortrait.setTag(takePhotoUri);
                try {
                    FileOutputStream fos = new FileOutputStream(takePhotoUri.getPath());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // picture crop
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("output", uri);
        intent.putExtra("outputFormat", "JPEG");
        startActivityForResult(intent, REQUEST_CROP);
    }

    // compress image until it's size less than @size
    private Bitmap compressImage(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) {
            baos.reset();
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }

}
