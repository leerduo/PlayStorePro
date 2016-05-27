package me.jarvischen.retrofitrxandroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TextView info;

    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.info);
        loader = (ProgressBar) findViewById(R.id.loader);
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();
        GithubService githubService = retrofit.create(GithubService.class);
        Observable<UserInfo> observable = githubService.getGithubUserInfo("leerduo");
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<UserInfo, String>() {
                    @Override
                    public String call(UserInfo userInfo) {
                        String name = userInfo.getName();
                        String blog = userInfo.getBlog();
                        String info = "姓名:" + name + "\n" + "博客:" + blog;
                        return info;
                    }
                }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                loader.setVisibility(View.GONE);
                info.setVisibility(View.VISIBLE);
                info.setText(s);
            }
        });
    }
}
