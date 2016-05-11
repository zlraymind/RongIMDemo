package rong.im.demo.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rong.im.demo.R;
import rong.im.demo.model.User;
import rong.im.demo.util.AppUtil;

public class ContactItemView extends LinearLayout {

    public static final int SIMPLE = 0;
    public static final int DETAIL = 1;

    @IntDef({SIMPLE, DETAIL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface InfoMode {
    }

    private ImageView portrait;
    private TextView nickname_simple;
    private View layout_detail;
    private TextView username_detail;
    private TextView nickname_detail;

    private int infoMode = 0;
    private String searchText;

    public ContactItemView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        addView(inflater.inflate(R.layout.listitem_contact, null));
        initView();
    }

    private void initView() {
        portrait = (ImageView) findViewById(R.id.portrait);
        nickname_simple = (TextView) findViewById(R.id.user_simple);
        layout_detail = findViewById(R.id.user_detail);
        username_detail = (TextView) findViewById(R.id.username);
        nickname_detail = (TextView) findViewById(R.id.nickname);
    }

    public void setInfoMode(@InfoMode int mode) {
        infoMode = mode;
        if (infoMode == SIMPLE) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) portrait.getLayoutParams();
            params.width = AppUtil.dip2px(36);
            params.height = AppUtil.dip2px(36);
            portrait.setLayoutParams(params);

            nickname_simple.setVisibility(VISIBLE);
            layout_detail.setVisibility(GONE);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) portrait.getLayoutParams();
            params.width = AppUtil.dip2px(48);
            params.height = AppUtil.dip2px(48);
            portrait.setLayoutParams(params);

            nickname_simple.setVisibility(GONE);
            layout_detail.setVisibility(VISIBLE);
        }
    }

    public void setUserInfo(User user) {
        AppUtil.loadUriImage(user.portrait, portrait);
        if (infoMode == SIMPLE) {
            if (!searchText.isEmpty()) {
                SpannableStringBuilder builder = new SpannableStringBuilder(user.nickname);
                int start = user.nickname.toLowerCase().indexOf(searchText.toLowerCase());
                int end = 0;
                if (start >= 0) {
                    end = start + searchText.length();
                    builder.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                nickname_simple.setText(builder);
            } else {
                nickname_simple.setText(user.nickname);
            }
        } else {
            if (!searchText.isEmpty()) {
                SpannableStringBuilder builder = new SpannableStringBuilder(user.username);
                int start = user.username.toLowerCase().indexOf(searchText.toLowerCase());
                int end;
                if (start >= 0) {
                    end = start + searchText.length();
                    builder.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                username_detail.setText(builder);

                builder = new SpannableStringBuilder(user.nickname);
                start = user.nickname.toLowerCase().indexOf(searchText.toLowerCase());
                if (start >= 0) {
                    end = start + searchText.length();
                    builder.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                nickname_detail.setText(builder);
            } else {
                username_detail.setText(user.username);
                nickname_detail.setText(user.nickname);
            }
        }
    }

    public void setPortrait(int resId) {
        portrait.setImageResource(resId);
    }

    public void setNickname(String nick) {
        if (infoMode == SIMPLE) {
            nickname_simple.setText(nick);
        } else {
            nickname_detail.setText(nick);
        }
    }

    public void setSearchText(String text) {
        searchText = text;
    }
}
