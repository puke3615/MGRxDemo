package com.chriszou.rxjavademo.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaochuang on 3/16/16.
 */
public class User {
    @SerializedName("login")
    public final String username;
    @SerializedName("avatar_url")
    public final String avartarUrl;
    @SerializedName("followers")
    public final int followerCount;
    @SerializedName("public_repos")
    public final int repoCount;

    public User(String username, String avartarUrl, int followerCount, int repoCount) {
        this.username = username;
        this.avartarUrl = avartarUrl;
        this.followerCount = followerCount;
        this.repoCount = repoCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", avartarUrl='" + avartarUrl + '\'' +
                ", followerCount='" + followerCount + '\'' +
                ", repoCount='" + repoCount + '\'' +
                '}';
    }
}
