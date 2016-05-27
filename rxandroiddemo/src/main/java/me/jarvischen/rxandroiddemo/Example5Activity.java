package me.jarvischen.rxandroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Func1;

public class Example5Activity extends AppCompatActivity {

    private TextView valueDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example5);
        valueDisplay = (TextView) findViewById(R.id.value_display);
        Single.just(4).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return String.valueOf(integer);
            }
        }).subscribe(new SingleSubscriber<String>() {
            @Override
            public void onSuccess(String value) {
                valueDisplay.setText(value);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }
}
