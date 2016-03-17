package com.chriszou.rxjavademo;

import android.app.Activity;
import android.os.Bundle;

import com.jakewharton.rxbinding.view.RxView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiaochuang on 3/17/16.
 */
public class AnotherActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.another_activity);

        Observable<String> requestStream = RxView.clicks(findViewById(R.id.refresh)).startWith((Void)null).map(aVoid -> {
            int randomOffset = (int) Math.floor(Math.random() * 500);
            return "https://api.github.com/users?since=" + randomOffset;
        });

        Observable<String> responseStream = requestStream.observeOn(Schedulers.io()).map(url -> requestUrl(url));
        responseStream.subscribeOn(AndroidSchedulers.mainThread()).subscribe(response -> showUsers(response));


    }

    private void showUsers(String response) {
        System.out.println(response);
    }

    private String requestUrl(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        try {
            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "dumb body";
        }
    }
}
