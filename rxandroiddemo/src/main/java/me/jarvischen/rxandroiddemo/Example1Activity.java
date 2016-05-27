package me.jarvischen.rxandroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;

public class Example1Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    SimpleStringAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example1);
        recyclerView = (RecyclerView) findViewById(R.id.color_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleStringAdapter(this);
        recyclerView.setAdapter(adapter);
        Observable<List<String>> observable = Observable.just(getColorList());
        observable.subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> colors) {
                adapter.setStrings(colors);
            }
        });
    }

    private static List<String> getColorList(){
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            colors.add("blue" + i);
        }
        return colors;
    }
}
