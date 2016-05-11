package rong.im.demo.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import rong.im.demo.R;

public class DiscoveryFragment extends Fragment {

    private static final String TAG = "DiscoveryFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_discovery, container,false);
        DiscoveryListItem streamShow = new DiscoveryListItem(root.findViewById(R.id.discovery_stream_show), R.mipmap.panel_stream_show, "视频直播", true);
        streamShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "视频直播");
            }
        });

        DiscoveryListItem watchShow = new DiscoveryListItem(root.findViewById(R.id.discovery_watch_show), R.mipmap.panel_watch_show, "观看直播", false);
        watchShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "观看直播");
            }
        });
        return root;
    }

    private class DiscoveryListItem {
        private View rootView;

        public DiscoveryListItem(View root, @DrawableRes int imgId, String name, boolean showUnderline) {
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
