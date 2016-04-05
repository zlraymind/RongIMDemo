package rong.im.demo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.w3c.dom.Text;

import rong.im.demo.R;

public class LoginDialog extends PopupWindow {

    private TextView title;
    private TextView body;
    private TextView confirm;
    private Activity parent;

    public LoginDialog(Context context) {
//        super(,
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = parent.getWindow().getAttributes();
                lp.alpha = 1f;
                parent.getWindow().setAttributes(lp);
            }
        });

        View view = LayoutInflater.from(context).inflate(R.layout.login_dialog, null);
        setContentView(view);

        title = (TextView) view.findViewById(R.id.title);
        body = (TextView) view.findViewById(R.id.body);
        confirm = (TextView) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setBody(String text) {
        body.setText(text);
    }

    public void show(@NonNull Activity parent) {
        this.parent = parent;
        WindowManager.LayoutParams lp = parent.getWindow().getAttributes();
        lp.alpha = 0.3f;
        parent.getWindow().setAttributes(lp);

        View view = parent.findViewById(android.R.id.content);
        showAtLocation(view, Gravity.CENTER, 0, 30);
    }

    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams lp = parent.getWindow().getAttributes();
        lp.alpha = 1f;
        parent.getWindow().setAttributes(lp);
    }
}
