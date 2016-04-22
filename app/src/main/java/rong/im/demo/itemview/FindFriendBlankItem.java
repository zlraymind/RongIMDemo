package rong.im.demo.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rong.im.demo.R;

public class FindFriendBlankItem extends ItemView {

    public static final String TAG = "FindFriendBlankItem";

    LayoutInflater inflater;

    public FindFriendBlankItem(Context context) {
        inflater = LayoutInflater.from(context);
        setTag(TAG);
    }

    @Override
    public View getView(ViewGroup parent) {
        return inflater.inflate(R.layout.find_new_friend_blank, parent, false);
    }
}
