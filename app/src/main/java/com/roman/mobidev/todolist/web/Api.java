package com.roman.mobidev.todolist.web;

import com.roman.mobidev.todolist.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by roman on 25.04.16.
 */
public interface Api {

    @POST("users/sign_up")
    Call<User> signUp(@Body User user);

    @PUT("users/sign_in")
    Call<User> signIn(@Body User user);

}
