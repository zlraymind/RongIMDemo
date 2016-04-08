package rong.im.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import rong.im.demo.R;

public class MainActivity extends AppCompatActivity {

    ImageView nor;
    ImageView sel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        nor = (ImageView) findViewById(R.id.titlebar_im_nor);
//        sel = (ImageView) findViewById(R.id.titlebar_im_sel);
//
//        nor.setAlpha(0.1f);
//        sel.setAlpha(0.9f);
    }
}
