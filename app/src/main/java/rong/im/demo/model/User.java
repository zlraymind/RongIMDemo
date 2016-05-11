package rong.im.demo.model;

public class User {

    public String username;
    public String password;
    public String nickname;
    public String portrait;

    public User() {

    }

    public User(String username, String nickname, String portrait) {
        this.username = username;
        this.nickname = nickname;
        this.portrait = portrait;
    }
}
