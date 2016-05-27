package me.jarvischen.rxandroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Example3Activity extends AppCompatActivity {

    private Subscription tvShowSubscription;
    private RecyclerView recyclerView;
    private ProgressBar loader;
    private TextView errorMessage;
    private SimpleStringAdapter adapter;
    private RestClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example3);
        client = new RestClient(this);
        initView();
        createSingle();
    }

    private void createSingle() {
        //Single是Observable的精简版
        //Single和Observable的回调方法不一样
        Single<List<String>> single = Single.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                //return client.getFavoriteTvShows();
                return client.getFavoriteTvShowsWithException();
            }
        });
        tvShowSubscription = single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //这里是SingleSubscriber
                .subscribe(new SingleSubscriber<List<String>>() {
                    @Override
                    public void onSuccess(List<String> tvShows) {
                        displayTvShows(tvShows);
                    }

                    @Override
                    public void onError(Throwable error) {
                        displayErrorMessage();
                    }
                });
    }

    private void displayErrorMessage() {
        loader.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void displayTvShows(List<String> tvShows) {
        adapter.setStrings(tvShows);
        loader.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.tv_show_List);
        errorMessage = (TextView) findViewById(R.id.error_message);
        loader = (ProgressBar) findViewById(R.id.loader);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleStringAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvShowSubscription != null && !tvShowSubscription.isUnsubscribed()) {
            tvShowSubscription.unsubscribe();
        }
    }
}
