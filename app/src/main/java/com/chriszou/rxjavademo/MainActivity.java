package com.chriszou.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rx.Observable;
import rx.Observer;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> observable = Observable.from(new String[]{"torvalds", "jakewharton", "chriszou"});
        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("on completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("on error");
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println("on next");
                System.out.println(s);
            }
        });
   }
}
