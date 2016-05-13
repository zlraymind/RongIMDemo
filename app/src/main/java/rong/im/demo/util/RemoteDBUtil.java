package rong.im.demo.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import rong.im.demo.model.User;

public class RemoteDBUtil {

    private static final String TAG = "RemoteDBUtil";

    public static void checkUserFromServer(String username, final Handler handler) {
        BmobQuery<LoginUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.findObjects(AppUtil.getContext(), new FindListener<LoginUser>() {
            @Override
            public void onSuccess(List<LoginUser> object) {
                Log.d(TAG, "checkUserFromServer onSuccess");
                handler.sendEmptyMessage(Const.REQUEST_SUCCESS);
            }

            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "checkUserFromServer onFailure code = " + code + "; msg = " + msg);
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    public static void uploadPortraitToServer(String picPath, final Handler handler) {
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(AppUtil.getContext(), new UploadFileListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "uploadPortraitToServer onSuccess");
                Message message = Message.obtain();
                message.what = Const.REQUEST_SUCCESS;
                message.obj = bmobFile.getFileUrl(AppUtil.getContext());
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e(TAG, "uploadPortraitToServer onFailure code = " + code + "; msg = " + msg);
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    public static void addUserToServer(User user, final Handler handler) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(user.username);
        loginUser.setPassword(user.password);
        loginUser.setNickname(user.nickname);
        loginUser.setPortrait(user.portrait);

        loginUser.signUp(AppUtil.getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "addUserToServer onSuccess");
                handler.sendEmptyMessage(Const.REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e(TAG, "addUserToServer onFailure code = " + code + "; msg = " + msg);
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    public static void loginToServer(User user, final Handler handler) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(user.username);
        loginUser.setPassword(user.password);

        loginUser.login(AppUtil.getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "loginToServer onSuccess");
                handler.sendEmptyMessage(Const.REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e(TAG, "loginToServer onFailure code = " + code + "; msg = " + msg);
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    public static void requestToken(User user, final Handler handler) {
        JSONObject params = new JSONObject();
        try {
            params.put("userId", user.username);
            params.put("name", user.nickname);
            params.put("portraitUri", user.portrait);
        } catch (JSONException e) {
            e.printStackTrace();
            Message msg = Message.obtain();
            msg.what = Const.REQUEST_FAILED;
            msg.obj = "LoginUser's json string create error.";
            handler.sendMessage(msg);
            return;
        }

        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(AppUtil.getContext(), "requestToken", params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                Message msg = Message.obtain();
                try {
                    JSONObject jsonObj = new JSONObject(result.toString());
                    msg.what = Const.REQUEST_SUCCESS;
                    msg.obj = jsonObj.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = Const.REQUEST_FAILED;
                    msg.obj = "Request token's json parser error.";
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int code, String reason) {
                Message msg = Message.obtain();
                msg.what = Const.REQUEST_FAILED;
                msg.obj = reason;
                handler.sendMessage(msg);
            }
        });
    }

    public static void queryLoginUser(String searchText, final Handler handler) {
        BmobQuery<LoginUser> queryID = new BmobQuery<>();
        queryID.addWhereContains("username", searchText);
        BmobQuery<LoginUser> queryNick = new BmobQuery<>();
        queryNick.addWhereContains("nickname", searchText);
        List<BmobQuery<LoginUser>> queries = new ArrayList<>();
        queries.add(queryID);
        queries.add(queryNick);

        BmobQuery<LoginUser> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        mainQuery.setLimit(50);
        mainQuery.findObjects(AppUtil.getContext(), new FindListener<LoginUser>() {
            @Override
            public void onSuccess(List<LoginUser> object) {
                Log.d(TAG, "queryLoginUser onSuccess");
                ArrayList<User> userList = new ArrayList<>();
                for (LoginUser user : object) {
                    Log.d(TAG, "user.name = " + user.getUsername());
                    userList.add(new User(user.getUsername(), user.getNickname(), user.getPortrait()));
                }
                Message message = Message.obtain();
                message.what = Const.REQUEST_SUCCESS;
                message.obj = userList;
                handler.sendMessage(message);
            }

            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "queryLoginUser onFailure code = " + code + "; msg = " + msg);
                Message message = Message.obtain();
                message.what = Const.REQUEST_FAILED;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    public static void sendInvitation(final String username, final String inviter, final Handler handler) {
        BmobQuery<Invitation> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.addWhereEqualTo("inviter", inviter);
        query.findObjects(AppUtil.getContext(), new FindListener<Invitation>() {
            @Override
            public void onSuccess(List<Invitation> list) {
                Log.d(TAG, "sendInvitation onSuccess find list.size() = " + list.size());
                for (BmobObject item : list) {
                    Log.d(TAG, "item.getTableName() = " + item.getTableName());
                    item.setTableName(item.getClass().getSimpleName());
                }
                new Invitation().deleteBatch(AppUtil.getContext(), list, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "111");
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        Log.d(TAG, "222 code = " + code +"; msg = " + msg);
                    }
                });

                Invitation table = new Invitation(username, inviter);
                table.save(AppUtil.getContext(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        handler.sendEmptyMessage(Const.REQUEST_SUCCESS);
                    }

                    @Override
                    public void onFailure(int code, String reason) {
                        Message message = Message.obtain();
                        message.what = Const.REQUEST_FAILED;
                        message.obj = reason;
                        handler.sendMessage(message);
                    }
                });
            }

            @Override
            public void onError(int code, String reason) {
                if (code == 101) {
                    Invitation table = new Invitation(username, inviter);
                    table.save(AppUtil.getContext(), new SaveListener() {
                        @Override
                        public void onSuccess() {
                            handler.sendEmptyMessage(Const.REQUEST_SUCCESS);
                        }

                        @Override
                        public void onFailure(int code, String reason) {
                            Message message = Message.obtain();
                            message.what = Const.REQUEST_FAILED;
                            message.obj = reason;
                            handler.sendMessage(message);
                        }
                    });
                } else {
                    Message message = Message.obtain();
                    message.what = Const.REQUEST_FAILED;
                    message.obj = reason;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private static class LoginUser extends BmobUser {
        private String nickname;
        private String portrait;

        public void setNickname(String name) {
            nickname = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setPortrait(String path) {
            portrait = path;
        }

        public String getPortrait() {
            return portrait;
        }
    }

    private static class Invitation extends BmobObject {
        public String username;
        public String inviter;

        public Invitation(String username, String inviter) {
            this.username = username;
            this.inviter = inviter;
        }
    }
}
