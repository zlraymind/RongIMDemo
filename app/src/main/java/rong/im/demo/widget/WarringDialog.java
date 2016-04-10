package rong.im.demo.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import rong.im.demo.R;

public class WarringDialog extends Dialog {

    private TextView title;
    private TextView body;
    private TextView confirm;


    public WarringDialog(Context context) {
        super(context, R.style.WarringDialog);
        setContentView(R.layout.dialog_warring);

        title = (TextView) findViewById(R.id.title);
        body = (TextView) findViewById(R.id.body);
        confirm = (TextView) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WarringDialog.this.dismiss();
            }
        });
    }

    public void setSubject(String text) {
        title.setText(text);
    }

    public void setBody(String text) {
        body.setText(text);
    }
}
