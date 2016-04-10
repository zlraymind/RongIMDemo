package rong.im.demo.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import rong.im.demo.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ViewPager viewPager;

    ArrayList<TitleIcon> titleIconList = new ArrayList<>();
    ArrayList<View> viewPagerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initConversationList();
    }

    private void initView() {
        titleIconList.add(new TitleIcon(findViewById(R.id.titleicon_im), R.mipmap.titlebar_im_nor, R.mipmap.titlebar_im_sel));
        titleIconList.add(new TitleIcon(findViewById(R.id.titleicon_contact), R.mipmap.titlebar_contact_nor, R.mipmap.titlebar_contact_sel));
        titleIconList.add(new TitleIcon(findViewById(R.id.titleicon_discovery), R.mipmap.titlebar_discovery_nor, R.mipmap.titlebar_discovery_sel));
        titleIconList.add(new TitleIcon(findViewById(R.id.titleicon_me), R.mipmap.titlebar_me_nor, R.mipmap.titlebar_me_sel));

        viewPagerList.add(getLayoutInflater().inflate(R.layout.main_pager_im, null));
        viewPagerList.add(getLayoutInflater().inflate(R.layout.main_pager_contact, null));
        viewPagerList.add(getLayoutInflater().inflate(R.layout.main_pager_discovery, null));
        viewPagerList.add(getLayoutInflater().inflate(R.layout.main_pager_me, null));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewPagerList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewPagerList.get(position));
                return viewPagerList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewPagerList.get(position));
            }
        });
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

        for (int i = 0; i < titleIconList.size(); i++) {
            final int index = i;
            titleIconList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleIconList.get(viewPager.getCurrentItem()).setGradient(0.0f);
                    viewPager.setCurrentItem(index, false);
                }
            });
        }
    }

    private void initConversationList() {
        ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversation_list);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")
                .build();
        fragment.setUri(uri);
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
}
