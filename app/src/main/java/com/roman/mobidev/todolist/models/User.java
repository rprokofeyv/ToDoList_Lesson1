package com.roman.mobidev.todolist.models;

import android.text.TextUtils;

/**
 * Created by roman on 25.04.16.
 */
public class User {

    public static User createFromEmailAndPassword(String email, String password){
        User user = new User();
        user.email = email;
        user.nickname = email;
        user.password = password;
        return user;
    }

    public int id;
    public String email;
    public String nickname;
    public String password;
    public String jwt_token;

    public boolean isLogged(){
        return !TextUtils.isEmpty(jwt_token);
    }
}
