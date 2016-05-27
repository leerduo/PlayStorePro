package me.jarvischen.rxjavademo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //RxJava--->异步
        //RxJava--->一个在Java VM上使用可观测的序列来组成异步的、基于事件的程序的库

        //第一步：创建观察者(Observer)---方式一
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }
        };

        //第一步：创建观察者(Observer)---方式二
        //除了通过Observer接口，还可以通过Subscriber，Subscriber实现了Observer接口
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }
        };


        //第二步，创建被观察者(Observable)--->方式一
        //RxJava使用create()方法来创建一个Observable，并为它定义事件触发规则
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });


        //第二步，创建被观察者(Observable)--->方式二
        //方式二是两种快捷创建事件队列的方法
        //create()方法是RxJava最基本的创建事件序列的方法。基于这个方法，RxJava还提供了一些方法用来快捷创建事件队列
        //方式二的第一种
        //通过just(T...)将传入的参数依次发送出来
        Observable<String> observableJust = Observable.just("Hello", "Hi", "Aloha");


        //第二步，创建被观察者(Observable)--->方式二
        //方式二是两种快捷创建事件队列的方法
        //create()方法是RxJava最基本的创建事件序列的方法。基于这个方法，RxJava还提供了一些方法用来快捷创建事件队列
        //方式二的第一种
        //通过from(T[])/from(Iterable<? extends T>)将传入的数组或Iterable拆分成具体对象后，依次发送出去
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable<String> fromObservable = Observable.from(words);


        //第三步，订阅(subscribe)--->方式一
        //observer在subscribe()过程中最终会被转换成Subscriber对象
        observable.subscribe(observer);
        //或者通过下面的订阅方式
        observable.subscribe(subscriber);
        //通过查看源码，可以得知subscribe()一共做了三件事
        //第一是：调用Subscriber.onStart();
        //第二是：调用Observable中的OnSubscribe(Subscriber)方法；
        //第三是：将传入的Subscriber作为Subcription返回，这是方便unSubscribe()。


        //第三步，订阅(subscribe)--->方式二
        //除了observable.subscribe(observer)和observable.subscribe(subscriber)之外，subscribe()还支持
        //不完整定义的回调
        //RxJava会自动根据定义创建出Subscriber。
        Action1<String> onNextAction = new Action1<String>() {
            //onNext()
            @Override
            public void call(String s) {
                Log.d(TAG, "call: " + s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            //onError()
            @Override
            public void call(Throwable throwable) {
                Log.d(TAG, "call: " + "Error Handling");
            }
        };
        Action0 onCompletedAction = new Action0() {
            //onCompleted()
            @Override
            public void call() {
                Log.d(TAG, "call: " + "onCompleted");
            }
        };

        //Action0和Action1是接口

        //自动创建Subscriber，并使用onNextAction来定义onNext
        observable.subscribe(onNextAction);
        //自动创建Subscriber，并使用onNextAction和onErrorAction来定义onNext()和onError()
        observable.subscribe(onNextAction, onErrorAction);
        //自动创建Subscriber，并使用onNextAction、onErrorAction和onCompleted()来定义onNext()、onError()和onCompleted
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);

        //=====================================================================

        //线程控制--->Scheduler调度器
        //在不指定线程的情况下，RxJava遵循线程不变的原则，即：在哪个线程调用subscribe()，就在哪个线程生产事件，在哪个线程生产事件，就在哪个
        //线程消费事件。如果需要切换线程，则需要Scheduler调度器
        //RxJava内置的几个Schedulers
        //直接在当前线程运行，相当于不指定线程，这是默认的Scheduler
        Schedulers.immediate();
        //总是启用新线程，并在新线程中执行操作。
        Schedulers.newThread();
        //IO操作(读写文件，数据库，网络信息交互等)所使用的Scheduler。行为模式和newThread()差不多，区别在于io()方法的内部实现是一个无数量上限的线程池
        //可以重用空闲的线程，因此多数情况下io()比newThread()更有效率。不要把计算工作放在io()中，可以避免创建不必要的线程。
        Schedulers.io();
        //计算所使用的Schedulers，这里的计算指的是CPU密集型的计算，即不会被IO等操作限制性能的操作，例如图形的计算等等
        //这个Scheduler使用固定的线程池，大小为CPU的核数，不要把IO操作放在这里，否则IO操作的等待时间会浪费CPU。
        Schedulers.computation();
        //指定在Android主线程运行
        AndroidSchedulers.mainThread();
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "call: " + integer);
                    }
                });

        //变换
        //所谓变换--->就是将事件序列中的对象或者整个序列进行加工处理，转换成不同的事件或者事件序列

        //变换示例--->针对事件对象
        //这里输入类型是String，变换后为Bitmap
        //参数类型也由String转换为Bitmap
        Observable.just("images/logo.png")//输入类型 String
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) {//参数类型 String
                        return getBitmapFromPath(filePath);//返回类型 Bitmap
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {//参数类型 Bitmap
                        showBitmap(bitmap);
                    }
                });

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
                .flatMap(new Func1<Student, Observable<Course>>() {//flatMap()中返回的是个Observable对象，并且这个Observable对象并不是被直接发送到了Subscriber的回调方法中。
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(subscriberFlatMap);
        //flatMap()的原理是这样的：
        //1.使用传入的事件对象创建一个Observable对象
        //2.并不发送这个Observable对象，而是将它激活，于是它开始发送事件
        //3.每一个创建出来的Observable发送的事件，都被汇入同一个Observale，而这个Observable负责将这些事件统一交给Subscriber的回调方法

        //变换的原理--->基于lift(operator)变换方法
        //在Observable执行了lift(operator)方法之后，会返回一个新的Observable，这个新的Observable会像一个代理一样，负责接收原始的Observable发出的事件，并在处理后发给Subscriber.

        //只是一个示例---RxJava不建议这样做
        Observable<Integer> integerObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

            }
        });
        //将事件中的Integer对象转换为String的具体的Operator的例子
        integerObservable.lift(new Observable.Operator<String, Integer>() {
            @Override
            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                return new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        subscriber.onNext("" + integer);
                    }
                };
            }
        });


        //compose：对Observable整体的变换
        //compose(Transformer)和lift的区别在于：
        //lift()是针对事件项和事件序列的
        //compose()是针对Observable自身进行变换
    }



    private void showBitmap(Bitmap bitmap) {

    }

    private Bitmap getBitmapFromPath(String filePath) {
        return null;
    }
}
