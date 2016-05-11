package rong.im.demo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.rong.imlib.RongIMClient;
import rong.im.demo.R;
import rong.im.demo.activity.LoginActivity;
import rong.im.demo.util.AppUtil;

public class MeFragment extends Fragment {

    private static final String TAG = "MeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_me, container, false);
        MeListItem logout = new MeListItem(root.findViewById(R.id.me_logout), R.mipmap.panel_logout, "退出登录", false);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = MeFragment.this.getActivity();
                AppUtil.clearUserInfoCached();
                RongIMClient.getInstance().logout();

                Intent intent = new Intent();
                intent.setClass(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            }
        });
        return root;
    }

    private class MeListItem {
        private View rootView;

        public MeListItem(View root, @DrawableRes int imgId, String name, boolean showUnderline) {
            rootView = root;

            ImageView image = (ImageView) rootView.findViewById(R.id.image);
            image.setImageResource(imgId);
            TextView title = (TextView) rootView.findViewById(R.id.title);
            title.setText(name);
            View underline = rootView.findViewById(R.id.underline);
            underline.setVisibility(showUnderline ? View.VISIBLE : View.INVISIBLE);
        }

        public void setOnClickListener(@Nullable View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }

    }
}
