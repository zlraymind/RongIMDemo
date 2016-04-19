package rong.im.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rong.im.demo.App;
import rong.im.demo.R;
import rong.im.demo.activity.LoginActivity;
import rong.im.demo.activity.MainActivity;
import rong.im.demo.util.AppUtil;
import rong.im.demo.widget.PanelItem;

public class MeFragment extends Fragment {

    private static final String TAG = "MeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_me, container,false);
        PanelItem logout = new PanelItem(root.findViewById(R.id.me_logout), R.mipmap.panel_logout, "退出登录", false);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "退出登录");
                Activity activity = MeFragment.this.getActivity();
                AppUtil.clearUserInfo(activity);
                Intent intent = new Intent();
                intent.setClass(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            }
        });
        return root;
    }
}
