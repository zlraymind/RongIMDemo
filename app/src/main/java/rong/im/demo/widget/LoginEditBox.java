package rong.im.demo.widget;

import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import rong.im.demo.R;

public class LoginEditBox {

    private View rootView;
    private EditText content;

    public LoginEditBox(View root, String subject, String hint) {
        rootView = root;
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(subject);
        content = (EditText) rootView.findViewById(R.id.content);
        content.setHint(hint);
    }

    public String getText() {
        return content.getText().toString();
    }

    public void setTextChangedListener(TextWatcher watcher) {
        content.addTextChangedListener(watcher);
    }
}
