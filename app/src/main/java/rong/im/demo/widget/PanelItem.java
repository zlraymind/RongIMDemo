package rong.im.demo.widget;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import rong.im.demo.R;

public class PanelItem {

    private View rootView;

    public PanelItem(View root, @DrawableRes int imgId, String name, boolean showUnderline) {
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
