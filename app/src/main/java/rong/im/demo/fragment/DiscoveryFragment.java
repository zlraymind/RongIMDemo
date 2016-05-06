package rong.im.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rong.im.demo.R;
import rong.im.demo.widget.PanelItem;

public class DiscoveryFragment extends Fragment {

    private static final String TAG = "DiscoveryFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_discovery, container,false);
        PanelItem streamShow = new PanelItem(root.findViewById(R.id.discovery_stream_show), R.mipmap.panel_stream_show, "视频直播", true);
        streamShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "视频直播");
            }
        });

        PanelItem watchShow = new PanelItem(root.findViewById(R.id.discovery_watch_show), R.mipmap.panel_watch_show, "观看直播", false);
        watchShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "观看直播");
            }
        });
        return root;
    }
}
