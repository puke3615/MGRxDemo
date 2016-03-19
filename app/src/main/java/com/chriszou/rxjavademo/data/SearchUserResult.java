package com.chriszou.rxjavademo.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaochuang on 3/19/16.
 */
public class SearchUserResult {

    @SerializedName("total_count")
    public final int totalCount;

    @SerializedName("items")
    public final List<User> users;

    public SearchUserResult(int totalCount, List<User> users) {
        this.totalCount = totalCount;
        this.users = users;
    }
}
