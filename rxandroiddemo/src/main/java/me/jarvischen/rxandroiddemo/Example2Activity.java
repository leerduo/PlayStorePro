package me.jarvischen.rxandroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Example2Activity extends AppCompatActivity {
    private Subscription tvShowSubscription;
    private RecyclerView recyclerView;
    private ProgressBar loader;
    private SimpleStringAdapter adapter;
    private RestClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example2);
        client = new RestClient(this);
        initView();
        createObservable();
    }

    private void createObservable() {
        //这里使用Observable.fromCallable()方法创建的Observable
        //Example1Activity中通过Observable.just(getColorList())方法创建的Observable
        //那么为什么这里不使用Observable.just(client.getFavoriteTvShows())来创建Observable呢？
        //如果使用的话，client.getFavoriteTvShows()方法会发起网络请求，会被立即执行并阻塞UI线程
        //使用Observable.fromCallable的好处：
        //一是：获取数据的代码只会在Observer订阅之后执行
        //二是：获取数据的代码可以在子线程中执行
        Observable<List<String>> observable = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return client.getFavoriteTvShows();
            }
        });
        tvShowSubscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> tvShows) {
                displayTvShows(tvShows);
            }
        });
    }

    private void displayTvShows(List<String> tvShows) {
        adapter.setStrings(tvShows);
        loader.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.tv_show_List);
        loader = (ProgressBar) findViewById(R.id.loader);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleStringAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvShowSubscription != null && !tvShowSubscription.isUnsubscribed()){
            tvShowSubscription.unsubscribe();
        }
    }
}
