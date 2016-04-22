package rong.im.demo.itemview;

import android.view.View;
import android.view.ViewGroup;

public abstract class ItemView {

    String tag;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public abstract View getView(ViewGroup parent);
}
