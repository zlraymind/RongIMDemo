package rong.im.demo.model;

import cn.bmob.v3.BmobUser;

public class LoginUser extends BmobUser {

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
