package me.jarvischen.gradledemo;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by chenfuduo on 2016/5/18.
 */
public class MyView extends Dialog {
    public MyView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pay_myview, null);
        setContentView(view);
    }
}
