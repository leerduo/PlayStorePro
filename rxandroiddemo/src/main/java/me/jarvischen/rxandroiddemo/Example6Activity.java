package me.jarvischen.rxandroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class Example6Activity extends AppCompatActivity {

    private RestClient client;
    private RecyclerView recyclerView;
    private TextView noResultIndicator;
    private EditText inputSearch;
    private SimpleStringAdapter adapter;

    private PublishSubject<String> searchResultsSubject;
    private Subscription textWatchSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example6);
        client = new RestClient(this);
        initView();
        createObservables();
        listenToSearchInput();
    }

    private void listenToSearchInput() {
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchResultsSubject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void createObservables() {
        searchResultsSubject = PublishSubject.create();
        textWatchSubscription = searchResultsSubject
                //debounce这个方法告诉searchResultsSubject在没有数据传入到400毫秒时才发送数据。
                .debounce(400, TimeUnit.MICROSECONDS)
                .observeOn(Schedulers.io())
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {
                        return client.searchForCity(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> cities) {
                        handleSearchResults(cities);
                    }
                });
    }

    private void handleSearchResults(List<String> cities) {
        if (cities.isEmpty()) {
            showNoSearchResults();
        } else {
            showSearchResults(cities);
        }
    }

    private void showSearchResults(List<String> cities) {
        noResultIndicator.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.setStrings(cities);
    }

    private void showNoSearchResults() {
        noResultIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }


    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.search_results);
        noResultIndicator = (TextView) findViewById(R.id.no_result_indicator);
        inputSearch = (EditText) findViewById(R.id.input_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleStringAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textWatchSubscription != null && !textWatchSubscription.isUnsubscribed()) {
            textWatchSubscription.unsubscribe();
        }
    }
}
