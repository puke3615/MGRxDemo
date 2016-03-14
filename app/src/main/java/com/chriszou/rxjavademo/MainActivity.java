package com.chriszou.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rx.Observable;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> observable = Observable.from(new String[]{"torvalds", "jakewharton", "chriszou"});
        observable.subscribe(s -> {
            System.out.println(s);
        }, throwable -> {
            throwable.printStackTrace();
        });
   }
}
