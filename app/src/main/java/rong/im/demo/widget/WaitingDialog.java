package rong.im.demo.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import org.w3c.dom.Text;

import rong.im.demo.R;

public class WaitingDialog extends Dialog {

    private TextView body;

    public WaitingDialog(Context context) {
        super(context, R.style.WaitingDialog);
        setContentView(R.layout.dialog_waitting);
        body = (TextView) findViewById(R.id.body);
        setCancelable(false);
    }

    public void setText(String text) {
        body.setText(text);
    }
}
