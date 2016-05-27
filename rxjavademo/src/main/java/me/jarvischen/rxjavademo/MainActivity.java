package me.jarvischen.rxjavademo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout ll;
    private TextView textView;
    StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);
        ll = (LinearLayout) findViewById(R.id.ll);
        sb = new StringBuilder();

        //示例---打印字符串数组
        String[] names = {"chenfuduo", "hejian", "wangchao", "wanyoubao"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        //Log.d(TAG, "call: " + name);
                        sb.append(name + "\n");
                        textView.setText(sb);
                    }
                });

        //示例二---由id取得图片并显示
        final int drawableRes = R.mipmap.ic_launcher;
        final ImageView imageView = new ImageView(this);
        ll.setGravity(Gravity.CENTER);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //Log.d(TAG, "onError: ");
            }

            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
                ll.addView(imageView);
            }
        });

        //实例---Scheduler的使用一
        //典型的后台处理数据，主线程显示
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        //Log.d(TAG, "call: " + number);
                    }
                });

        //实例---Scheduler的使用二
        final int drawableRes2 = R.mipmap.ic_launcher;
        final ImageView imageView2 = new ImageView(this);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(drawableRes2);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        imageView2.setImageDrawable(drawable);
                        ll.addView(imageView2);
                    }
                });


       /* //示例---打印出一组学生的名字
        Student[] students = {new Student("chenfuduo1"),new Student("wanyoubao2"),new Student("hejian2")};
        Subscriber<String> subscriberFlatMap = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String name) {
                Log.d(TAG, "onNext: " + name);
                sb.append(name + "\n");
                textView.setText(sb);
            }
        };
        Observable.from(students)
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribe(subscriberFlatMap);*/


        /*//示例---打印出每个学生所修课程的名字--->通过for循环实现
        List<Course> chenCourses = new ArrayList<>();
        List<Course> wanCourses = new ArrayList<>();
        List<Course> heCourses = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            chenCourses.add(new Course("Math" + i));
            wanCourses.add(new Course("Math" + i));
            heCourses.add(new Course("Math" + i));
        }

        Student[] students = {new Student("chenfuduo1",chenCourses),
                new Student("wanyoubao2",wanCourses),new Student("hejian2",heCourses)};
        Subscriber<Student> subscriberFlatMap = new Subscriber<Student>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Student student) {
                List<Course> courses = student.getCourses();
                for (int i = 0; i < courses.size(); i++) {
                    Course course = courses.get(i);
                    Log.d(TAG, "onNext: " + course.getName());
                }
            }
        };
        Observable.from(students)
                .subscribe(subscriberFlatMap);*/

        //示例---打印出每个学生所修课程的名字--->通过flatMap实现
        List<Course> chenCourses = new ArrayList<>();
        List<Course> wanCourses = new ArrayList<>();
        List<Course> heCourses = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            chenCourses.add(new Course("Math" + i));
            wanCourses.add(new Course("Math" + i));
            heCourses.add(new Course("Math" + i));
        }

        Student[] students = {new Student("chenfuduo1",chenCourses),
                new Student("wanyoubao2",wanCourses),new Student("hejian2",heCourses)};
        Subscriber<Course> subscriberFlatMap = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.d(TAG, "onNext: " + course.getName());
            }
        };
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(subscriberFlatMap);
    }
}
