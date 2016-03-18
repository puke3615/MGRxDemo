package com.chriszou.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by xiaochuang on 3/18/16.
 */
public class SearchActivity extends Activity {

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


        //RxJava version
        RxTextView.afterTextChangeEvents(searchBox)
                .map(textChangedEvent -> textChangedEvent.editable().toString())
                .filter(s -> s.length() > 3)
                .distinctUntilChanged()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(s -> {

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

    private void performSearch(String string) {
        //Search for string
    }
}
