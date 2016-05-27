package me.jarvischen.rxandroiddemo;

import android.app.Activity;

/**
 * Created by chenfuduo on 2016/5/24.
 */
public class ExampleActivityAndName {
    public final Class<? extends Activity> mExampleActivityClass;
    public final String mExampleName;

    public ExampleActivityAndName(Class<? extends Activity> mExampleActivityClass,
                                  String mExampleName) {
        this.mExampleActivityClass = mExampleActivityClass;
        this.mExampleName = mExampleName;
    }
}
