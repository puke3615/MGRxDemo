package com.chriszou.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chriszou.rxjavademo.data.User;
import com.chriszou.rxjavademo.utils.Networker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiaochuang on 3/18/16.
 */
public class SearchActivity extends Activity {

    private LinearLayout usersLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        EditText searchBox = (EditText) findViewById(R.id.search_box);
        usersLayout = (LinearLayout) findViewById(R.id.usersLayout);

        //RxJava version
//        RxTextView.afterTextChangeEvents(searchBox)
//                .map(textChangedEvent -> textChangedEvent.editable().toString())
//                .filter(s -> s.length() > 3)
//                .distinctUntilChanged()
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .map(query -> searchUsers(query))
//                .subscribe(users -> updateList(users));


        Observable<String> requestStream =
                RxView.clicks(findViewById(R.id.refresh)).startWith((Void)null).map(ignored -> {
            int randomOffset = (int) Math.floor(Math.random() * 500);
            return "https://api.github.com/users?since=" + randomOffset;
        }).subscribeOn(AndroidSchedulers.mainThread());

        Observable<List<User>> responseStream = requestStream
                .observeOn(Schedulers.io())
                .map(url -> {
                    try {
                        return new Networker().get(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "[]";
                    }
                })
                .map(response -> getUsers(response));

        responseStream.observeOn(AndroidSchedulers.mainThread())
                      .subscribe(users -> updateList(users));
    }

    private List<User> getUsers(String response) {
        Type listType = new TypeToken<List<User>>(){}.getType();
        return new Gson().fromJson(response, listType);
    }

    private void updateList(List<User> users) {
        usersLayout.removeAllViews();
        for (int i=0; i<5 && i<users.size(); i++) {
            usersLayout.addView(new UserView(this, users.get(i)));
        }
    }


}
