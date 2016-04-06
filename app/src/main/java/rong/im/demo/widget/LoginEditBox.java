package rong.im.demo.widget;

import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import rong.im.demo.R;

public class LoginEditBox {

    private View rootView;
    private EditText content;
    private ImageView underline;

    public LoginEditBox(View root, String subject, String hint) {
        rootView = root;
        TextView title = (TextView) rootView.findViewById(R.id.title);
        content = (EditText) rootView.findViewById(R.id.content);
        underline = (ImageView) rootView.findViewById(R.id.underline);

        title.setText(subject);
        content.setHint(hint);
        content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                underline.setImageResource(hasFocus ? R.drawable.login_underline_enable : R.drawable.login_underline_disable);
            }
        });
    }

    public String getText() {
        return content.getText().toString();
    }

    public void setTextChangedListener(TextWatcher watcher) {
        content.addTextChangedListener(watcher);
    }

    public void setInputType(int type) {
        content.setInputType(type);
    }
}
