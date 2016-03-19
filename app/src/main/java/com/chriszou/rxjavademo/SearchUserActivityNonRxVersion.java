package com.chriszou.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chriszou.rxjavademo.data.SearchUserResult;
import com.chriszou.rxjavademo.data.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiaochuang on 3/18/16.
 */
public class SearchUserActivityNonRxVersion extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        EditText searchBox = (EditText) findViewById(R.id.search_box);


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                onQueryStringChanged(s.toString());
            }
        });


    }

    private String lastQueryString;
    private long lastQueryTimestamp;

    private void onQueryStringChanged(String newQueryString) {
        if (TextUtils.isEmpty(newQueryString)) {
            return;
        }

        if (newQueryString.equals(lastQueryString)) {
            return;
        }

        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastQueryTimestamp < 500) {
            return;
        }

        lastQueryString = newQueryString;
        lastQueryTimestamp = currentTimeMillis;

        performSearch(newQueryString);
    }

    OkHttpClient client = new OkHttpClient();

    private void performSearch(String query) {
        //Search for string
        try {
            Request request = new Request.Builder()
                    .get()
                    .url("https://api.github.com/search/users?q=" + query)
                    .build();
            Response response = client.newCall(request).execute();
            SearchUserResult userResult = new Gson().fromJson(response.body().string(), SearchUserResult.class);
            updateList(userResult.users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateList(List<User> users) {

    }
}
