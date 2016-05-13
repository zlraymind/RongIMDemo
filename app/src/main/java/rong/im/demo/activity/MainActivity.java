package rong.im.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import rong.im.demo.R;
import rong.im.demo.fragment.ContactFragment;
import rong.im.demo.fragment.DiscoveryFragment;
import rong.im.demo.fragment.MeFragment;
import rong.im.demo.util.RongUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ViewPager viewPager;

    ArrayList<TitleIcon> titleIconList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        titleIconList.add(new TitleIcon(findViewById(R.id.titleicon_im), R.mipmap.titlebar_im_nor, R.mipmap.titlebar_im_sel));
        titleIconList.add(new TitleIcon(findViewById(R.id.titleicon_contact), R.mipmap.titlebar_contact_nor, R.mipmap.titlebar_contact_sel));
        titleIconList.add(new TitleIcon(findViewById(R.id.titleicon_discovery), R.mipmap.titlebar_discovery_nor, R.mipmap.titlebar_discovery_sel));
        titleIconList.add(new TitleIcon(findViewById(R.id.titleicon_me), R.mipmap.titlebar_me_nor, R.mipmap.titlebar_me_sel));
        for (int i = 0; i < titleIconList.size(); i++) {
            final int index = i;
            titleIconList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index != viewPager.getCurrentItem()) {
                        titleIconList.get(viewPager.getCurrentItem()).setGradient(0.0f);
                        viewPager.setCurrentItem(index, false);
                    }
                }
            });
        }

        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), this));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                titleIconList.get(position).setGradient(1 - positionOffset);
                if (position + 1 < titleIconList.size()) {
                    titleIconList.get(position + 1).setGradient(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        RongIM.getInstance().disconnect();
        finish();
        return;
    }

    private class TitleIcon {

        private View rootView;
        private ImageView normal;
        private ImageView selected;

        public TitleIcon(View root, @DrawableRes int norId, @DrawableRes int selId) {
            rootView = root;
            normal = (ImageView) root.findViewById(R.id.normal);
            selected = (ImageView) root.findViewById(R.id.selected);
            normal.setImageResource(norId);
            selected.setImageResource(selId);
            setGradient(0.0f);
        }

        public void setGradient(@FloatRange(from = 0.0, to = 1.0) float alpha) {
            normal.setAlpha(1 - alpha);
            selected.setAlpha(alpha);
        }

        public void setOnClickListener(@Nullable View.OnClickListener l) {
            rootView.setOnClickListener(l);
        }
    }

    public static class MainAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragmentList = new ArrayList<>();

        public MainAdapter(FragmentManager fm, Context context) {
            super(fm);
            ConversationListFragment fragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + context.getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")
                    .build();
            fragment.setUri(uri);

            fragmentList.add(fragment);
            fragmentList.add(new ContactFragment());
            fragmentList.add(new DiscoveryFragment());
            fragmentList.add(new MeFragment());
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}
