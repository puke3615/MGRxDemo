package com.chriszou.rxjavademo.model;

import com.chriszou.rxjavademo.data.SearchUserResult;
import com.chriszou.rxjavademo.data.User;
import com.chriszou.rxjavademo.utils.Networker;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaochuang on 3/19/16.
 */
public class UserModel {

    public List<User> searchUsers(String query) {
        try {
            String response = new Networker().get("https://api.github.com/search/users?q=chriszou");
            SearchUserResult userResult = new Gson().fromJson(response, SearchUserResult.class);
            return userResult.users;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
