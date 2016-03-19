package com.chriszou.rxjavademo;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chriszou.rxjavademo.data.User;
import com.squareup.picasso.Picasso;

/**
 * Created by xiaochuang on 3/19/16.
 */
public class UserView extends RelativeLayout {

    public UserView(Context context, User user) {
        super((context));
        LayoutInflater.from(context).inflate(R.layout.user_view_layout, this);
        updateViews(user);
    }

    private void updateViews(User user) {
        Picasso.with(getContext())
                .load(user.avartarUrl)
                .resizeDimen(R.dimen.avatar_size, R.dimen.avatar_size)
                .into((ImageView) findViewById(R.id.avatar));
        ((TextView)findViewById(R.id.username)).setText(user.username);
        ((TextView)findViewById(R.id.follower_count)).setText(user.followerCount+" followers");
    }

}
