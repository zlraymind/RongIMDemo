package rong.im.demo.widget;

import android.app.Dialog;
import android.content.Context;

import rong.im.demo.R;

public class WaitingDialog extends Dialog {

    public WaitingDialog(Context context) {
        super(context, R.style.WaitingDialog);
        setContentView(R.layout.dialog_waitting);
    }
}
