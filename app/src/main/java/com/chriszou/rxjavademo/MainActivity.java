package com.chriszou.rxjavademo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.msgView).setOnClickListener(v -> rxjavaTest());
        rxjavaTest();
    }

    private void rxjavaTest() {
        Observable<String> observable = Observable.defer(() -> Observable.from(getSourceItems()));
        observable.map(s -> "https://api.github.com/users/".concat(s))
                .toList()
                .flatMap(strings -> Observable.from(strings))
                .zipWith(observable, (url, name) -> new Pair<>(name, url))
                .toList()
                .flatMap(pairs -> Observable.from(pairs))
                .map(pair -> getUser(pair.second))
                .toSortedList((user, user2) -> {
                    if (user.followerCount == user2.followerCount) return 0;
                    if (user.followerCount > user2.followerCount) return -1;
                    return 1;
                })
                .flatMap(users -> Observable.from(users))
                .take(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> System.out.println(s), e -> e.printStackTrace()
                );
    }

    @NonNull
    private String[] getSourceItems() {
        return new String[]{"torvalds", "jakewharton", "dhh", "chriszou", "chrisbanes", "ryanb"};
    }

    private void printThread(String s) {
        System.out.println(s + Thread.currentThread().getId());
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    private User getUser(String url) {
        User user = null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            user = new Gson().fromJson(response.body().string(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }
}
