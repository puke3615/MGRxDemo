package com.chriszou.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chriszou.rxjavademo.DiscoveryActivity.OperatorSuppressError;
import com.chriszou.rxjavademo.data.SearchUserResult;
import com.chriszou.rxjavademo.data.User;
import com.chriszou.rxjavademo.utils.Networker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiaochuang on 3/17/16.
 */
public class AnotherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discovery_activity);

        //搜索框
        EditText searchBox = (EditText) findViewById(R.id.search_box);

        //点击搜索的事件流
        Observable<String> clickStream = RxView.clicks(findViewById(R.id.refresh))
                                                .map(ignored -> "Refresh button clicked");
        //启动流
        Observable<String> startupStream = Observable.just("on create");

        //搜索框的内容改变的事件流
        Observable<String> textChangeStream = RxTextView.afterTextChangeEvents(searchBox)
                .map(textChangedEvent -> textChangedEvent.editable().toString());


        Observable<String> recommendedUserRequestStream = startupStream.mergeWith(clickStream)
                                    .mergeWith(textChangeStream.filter(s -> s.length()==0));
        Observable<List<User>> usersStream = recommendedUserRequestStream.observeOn(Schedulers.io())
                                                                        .map(s -> getRecommendedUsers());
        Subscription recommendedUserSub = usersStream.observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(users -> updateUserList(users),
                                                                e -> showError(e));

        Observable<String> searchButtonClickStream = RxView.clicks(findViewById(R.id.search_button))
                .map(ignored -> searchBox.getText().toString())
                .filter(s -> s.length()>0);

        Observable<String> searchOnTextChangeStream = textChangeStream.filter(s -> s.length() >= 3)
                                                                    .debounce(500, TimeUnit.MILLISECONDS)
                                                                    .distinctUntilChanged();
        Observable<String> searchUserRequestStream = searchOnTextChangeStream
                                                                .mergeWith(searchButtonClickStream);
        Observable<List<User>> searchUserResultStream = searchUserRequestStream
                                                        .observeOn(Schedulers.io())
                                                        .map(s -> searchUsers(s))
                                                        .lift(new OperatorSuppressError<>(e -> e.printStackTrace()));
        Subscription searchUserSub = searchUserResultStream.observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(users -> updateUserList(users),
                                                                    e -> showError(e));
    }

    private void showError(Throwable message) {
        message.printStackTrace();
    }

    private void updateUserList(List<User> users) {
        System.out.println("update user list");
        LinearLayout usersLayout = (LinearLayout) findViewById(R.id.usersLayout);
        usersLayout.removeAllViews();
        if (users == null) return;
        for (int i = 0; i < users.size(); i++) {
            usersLayout.addView(new UserView(this, users.get(i)));
        }
    }

    private List<User> searchUsers(String name) {
        Log.d("zyzy", "search users: " + name);
        try {
            String response = new Networker().get("https://api.github.com/search/users?q=" + name);
            SearchUserResult userResult = new Gson().fromJson(response, SearchUserResult.class);
            return userResult.users;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void performSearch(String name) {
    }


    private List<User> getRecommendedUsers() {
        System.out.println("getting recommended users");

        int randomOffset = (int) Math.floor(Math.random() * 500);
        String url = "https://api.github.com/users?since=" + randomOffset;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", "token 068a064038efb0676aee728b54d1960820856917")
                .build();
        OkHttpClient client = new OkHttpClient();
        try {
            List<User> users = getUsersFromJsonArray(client.newCall(request).execute().body().string());
            Collections.shuffle(users);
            return users;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<User> getUsersFromJsonArray(String response) {
        Type listType = new TypeToken<List<User>>() {
        }.getType();
        return new Gson().fromJson(response, listType);
    }
}
