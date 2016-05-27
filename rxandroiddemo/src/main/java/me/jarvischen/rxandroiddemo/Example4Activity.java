package me.jarvischen.rxandroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rx.Observer;
import rx.subjects.PublishSubject;

public class Example4Activity extends AppCompatActivity {

    private TextView counterDisplayer;
    private Button incrementButton;
    //关于Subject的解释：Represents an object that is both an Observable and an Observer.
    //也就是说Subject既是Observable又是Observer，内部实现继承了Observable实现了Observer接口
    //可以把Subject想像成一个管道，从一端把数据注入，结果就会从另一端输出。
    //PublishSubject是Subject中的最简单的一种
    //使用PublishSubject时，一旦数据从一端注入，结果立即从另一端输出。
    private PublishSubject<Integer> counterEmitter;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example4);
        initView();
        createCounterEmitter();
    }

    private void createCounterEmitter() {
        counterEmitter = PublishSubject.create();
        counterEmitter.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                counterDisplayer.setText(String.valueOf(integer));
            }
        });
    }

    private void initView() {
        counterDisplayer = (TextView) findViewById(R.id.counter_display);
        counterDisplayer.setText(String.valueOf(counter));
        incrementButton = (Button) findViewById(R.id.increment_button);
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                //由于Subject也是Observer，所以它也有onNext方法
                //通过onNext方法把数据注入管道的输入端，可以理解为在一端中观察自增按钮是否被点击，然后把信息告知管道另一端的Observer
                counterEmitter.onNext(counter);
            }
        });
    }
}
