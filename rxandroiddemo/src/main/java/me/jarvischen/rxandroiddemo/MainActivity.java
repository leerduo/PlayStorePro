package me.jarvischen.rxandroiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.example_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExampleAdapter(this,getExamples()));
    }

    public List<ExampleActivityAndName> getExamples() {
        List<ExampleActivityAndName> exampleActivityAndName = new ArrayList<>();
        exampleActivityAndName.add(new ExampleActivityAndName(Example1Activity.class,"Example 1: Simple Color List"));
        exampleActivityAndName.add(new ExampleActivityAndName(Example2Activity.class,"Example 2: Favorite Tv Shows"));
        exampleActivityAndName.add(new ExampleActivityAndName(Example3Activity.class,"Example 3: Improved Favorite Tv Shows"));
        exampleActivityAndName.add(new ExampleActivityAndName(Example4Activity.class,"Example 4: Button Counter"));
        exampleActivityAndName.add(new ExampleActivityAndName(Example5Activity.class,"Example 5: Value Display"));
        exampleActivityAndName.add(new ExampleActivityAndName(Example6Activity.class,"Example 6: City Search"));
        exampleActivityAndName.add(new ExampleActivityAndName(OfficialDemoActivity.class,"Example 7: Officail Demo"));
        return exampleActivityAndName;
    }
}